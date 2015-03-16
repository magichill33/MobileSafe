package cn.ithm.kmplayer1.view;

import cn.ithm.kmplayer1.util.OnResultListener;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment implements OnResultListener {
	public Handler mainHandler = new Handler() {
		public void handleMessage(Message paramMessage) {
			super.handleMessage(paramMessage);
		}
	};

	public void onGetResult(int errorCode, Object result) {
	}
}
