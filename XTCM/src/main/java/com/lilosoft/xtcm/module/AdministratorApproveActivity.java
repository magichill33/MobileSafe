package com.lilosoft.xtcm.module;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.HomeBaseActivity;
import com.lilosoft.xtcm.base.NormalBaseActivity;
import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.constant.TableStructure;
import com.lilosoft.xtcm.constant.TypeContent;
import com.lilosoft.xtcm.instantiation.User;
import com.lilosoft.xtcm.network.HttpConnection;
import com.lilosoft.xtcm.utils.JsonParseInterface;
import com.lilosoft.xtcm.utils.LogFactory;
import com.lilosoft.xtcm.views.TitleBar;
import com.lilosoft.xtcm.views.TitleBar.STYLE;

/**
 * @category 行政审批
 * @author William Liu
 * 
 */
public class AdministratorApproveActivity extends NormalBaseActivity implements
		OnClickListener, JsonParseInterface {

	/**
	 * 上报成功
	 */
	private final static int MSG_REPORT_SUCCESS_ORDER = 0x0FFFFFFF;
	/**
	 * 上报失败
	 */
	private final static int MSG_REPORT_LOST_ORDER = 0x00FFFFFF;
	private final String TAG = "AdministratorApproveActivity";
	private Message m;
	private Thread approveThread = new Thread(new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			HttpConnection httpConnection = new HttpConnection();
			try {
				action(httpConnection.getData(
						HttpConnection.CONNECTION_ADMIN_APPROVE, User.username,
						TypeContent.APPROVE_TYPE_VALUE[searchtype.getSelectedItemPosition()],
						"1", "10"));
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				LogFactory.e(TAG, "ClientProtocolException 404");
				m = new Message();
				m.what = Config.MSG_LOST_404;
				myHandle.sendMessage(m);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LogFactory.e(TAG, "IOException");
				m = new Message();
				m.what = Config.MSG_LOST_CONN;
				myHandle.sendMessage(m);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				LogFactory.e(TAG, "JSONException");
				m = new Message();
				m.what = Config.MSG_LOST_JSON;
				myHandle.sendMessage(m);
			} finally {
				m = new Message();
				m.what = Config.DISMISS_PROGRESS_DIALOG;
				myHandle.sendMessage(m);
				LogFactory.e(TAG, "report thread end");
			}
		}

	});
	private String err_Msg;
	/**
	 * @category 主线程处理
	 */
	@SuppressLint("HandlerLeak")
	private Handler myHandle = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Config.SHOW_PROGRESS_DIALOG:
				HomeBaseActivity.showProgressDialog("交互中…");
				break;
			case Config.DISMISS_PROGRESS_DIALOG:
				HomeBaseActivity.dismissProgressDialog();
				break;
			case MSG_REPORT_SUCCESS_ORDER:
				LogFactory.e(TAG, "report Success");
				break;
			case MSG_REPORT_LOST_ORDER:
				if (null != err_Msg && !"".equals(err_Msg)) {
					Toast.makeText(mContext, err_Msg, Toast.LENGTH_LONG).show();
					err_Msg = "";
				} else {
					Toast.makeText(mContext, R.string.error_data,
							Toast.LENGTH_LONG).show();
				}
				LogFactory.e(TAG, "report lost");
				break;
			case Config.MSG_LOST_404:
				Toast.makeText(mContext, "Sorry 404", Toast.LENGTH_LONG).show();
				break;
			case Config.MSG_LOST_CONN:
				Toast.makeText(mContext, R.string.error_connection,
						Toast.LENGTH_LONG).show();
				break;
			case Config.MSG_LOST_JSON:
				Toast.makeText(mContext, R.string.error_data, Toast.LENGTH_LONG)
						.show();
				break;
			}

		}

	};
	private TitleBar mTitleBar;
	private Spinner searchtype;
	private Button submit;

	@Override
	protected void installViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_admin_approve);
		initTitleBar();
		searchtype = (Spinner) findViewById(R.id.searchtype);
		submit = (Button) findViewById(R.id.submit);
		searchtype
				.setAdapter(new ArrayAdapter<String>(mContext,
						android.R.layout.simple_spinner_item,
						TypeContent.APPROVE_TYPE));

	}

	@Override
	protected void registerEvents() {
		// TODO Auto-generated method stub
		submit.setOnClickListener(this);
	}

	/**
	 * @category 初始化titleBar
	 */
	protected void initTitleBar() {

		mTitleBar = (TitleBar) findViewById(R.id.titlebar);

		mTitleBar.changeStyle(STYLE.NOT_BTN_AND_TITLE);

		mTitleBar.centerTextView.setText(R.string.function_xzsp);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		threadG = new Thread(approveThread);
		threadG.start();
	}

	/**
	 * @category 指令分发
	 * @param response
	 */
	private void action(String response) {
		m = new Message();
		try {
			m = new Message();
			m.what = jsonParseToOrder(response);
			myHandle.sendMessage(m);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogFactory.e(TAG, "action JSONException");
		}
	}

	@Override
	public int jsonParseToOrder(String response) throws JSONException {
		// TODO Auto-generated method stub

		if (null != response && !"".equals(response)) {
			JSONObject jsonObject = new JSONObject(response);

			if (null != jsonObject) {

				String operation = jsonObject
						.getString(TableStructure.COVER_HEAD);

				if (TableStructure.V_ACT_REPORT.equals(operation)) {
					JSONObject content = (JSONObject) ((jsonObject
							.get(TableStructure.COVER_BODY)));

					LogFactory.e(TAG, content.toString());

				}
			}
		} else {
			err_Msg = "数据异常！";
			LogFactory.e(TAG, "not data!");
		}

		return MSG_REPORT_LOST_ORDER;
	}

}
