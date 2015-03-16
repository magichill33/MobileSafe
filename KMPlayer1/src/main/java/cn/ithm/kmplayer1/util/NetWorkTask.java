package cn.ithm.kmplayer1.util;

import android.content.Context;
import android.os.AsyncTask;
import cn.ithm.kmplayer1.ConstantValue;
import cn.ithm.kmplayer1.net.HttpClientUtil;
import cn.ithm.kmplayer1.net.NetUtil;
import cn.ithm.kmplayer1.view.BaseFragment;

public class NetWorkTask extends AsyncTask<Object, Integer, Object> {
	private static final String TAG = "NetWorkTask";
	private OnResultListener onResultListener;
	private Context mContext;
	private int mTag;

	protected Object doInBackground(Object[] params) {
		if ((params[0] instanceof OnResultListener))
			this.onResultListener = ((OnResultListener) params[0]);

		Object result = null;
		

		try {
			HttpClientUtil clientUtil = new HttpClientUtil();
			result = clientUtil.sendGet((String) params[2]);
		} catch (Exception e) {
			e.printStackTrace();
			result = ConstantValue.ERROR;
		}

		return result;
	}

	protected void onCancelled() {
		super.onCancelled();
	}

	protected void onPostExecute(Object result) {
		PromptManager.closeProgressDialog();
		if (this.onResultListener != null) {
			int errorCode = ConstantValue.SUCCESS;
			if (result != null) {
				try {
					errorCode = Integer.parseInt(result.toString());
				} catch (Exception e) {
				}
			} else {
				errorCode = ConstantValue.ERROR;
			}
			onResultListener.onGetResult(errorCode, errorCode == ConstantValue.ERROR ? null : result);
		}
	}

	/**
	 * 加强版的开始线程的操作（网络判断）
	 * 
	 * @param params
	 *            <p>
	 *            <li>index=0 的参数为Fragment
	 *            <li>index=1的参数为是否显示滚动条
	 *            <li>index=2链接
	 * @return
	 */
	public final AsyncTask<Object, Integer, Object> executeProxy(Object... params) {
		if (params[0] instanceof BaseFragment) {
			mContext = ((BaseFragment) params[0]).getActivity();
			// 判断网络的状态
			if (NetUtil.checkNetType(mContext)) {
				if ((Boolean) params[1]) {
					PromptManager.showProgressDialog(mContext);
				}
				return super.execute(params);
			} else {
				PromptManager.showNoNetWork(mContext);
			}
		}
		return null;
	}
}
