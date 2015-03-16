package cn.ithm.kmplayer1;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.cyberplayer.sdk.BCyberPlayerFactory;
import com.baidu.cyberplayer.sdk.BEngineManager;
import com.baidu.cyberplayer.sdk.BEngineManager.OnEngineListener;
import com.baidu.cyberplayer.sdk.BMediaController;
import com.baidu.cyberplayer.sdk.BVideoView;
import com.baidu.cyberplayer.sdk.BVideoView.OnCompletionListener;
import com.baidu.cyberplayer.sdk.BVideoView.OnErrorListener;
import com.baidu.cyberplayer.sdk.BVideoView.OnInfoListener;
import com.baidu.cyberplayer.sdk.BVideoView.OnPreparedListener;

public class VideoViewPlayingActivity1 extends Activity implements OnPreparedListener, OnCompletionListener, OnErrorListener,
		OnInfoListener {

	private final String TAG = "VideoViewPlayingActivity";

	private String mVideoSource = null;

	private BVideoView mVV = null;
	private BMediaController mVVCtl = null;
	private RelativeLayout mViewHolder = null;
	private LinearLayout mControllerHolder = null;

	private boolean mIsHwDecode = false;

	private final int UI_EVENT_PLAY = 0;

	private WakeLock mWakeLock = null;
	private static final String POWER_LOCK = "VideoViewPlayingActivity";

	// 播放状态
	private enum PLAYER_STATUS {
		PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED,
	}

	private PLAYER_STATUS mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;

	private int mLastPos = 0;

	private View.OnClickListener mPreListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.v(TAG, "pre btn clicked");
		}
	};

	private View.OnClickListener mNextListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.v(TAG, "next btn clicked");
		}
	};

	Handler mUIHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case UI_EVENT_PLAY:
					mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARING;
					if (mLastPos != 0) {
						// 如果有记录播放位置,先seek到想要播放的位置
						mVV.seekTo(mLastPos);
						mLastPos = 0;
					}
					// 设置播放源
					mVV.setVideoPath(mVideoSource);
					// 开始播放
					mVV.start();
					break;
				default:
					break;
			}
		}
	};

	// 您的ak
	private String AK = "6DxpbINasbmoT3p30TeHmO8T";
	// 您的sk的前16位
	private String SK = "G4f8ztzcZkxWIhCq";

	private TextView notice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		BCyberPlayerFactory.init(this);

		setContentView(R.layout.controllerplaying);

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, POWER_LOCK);

		mVideoSource = getIntent().getStringExtra("url");

		notice = (TextView) findViewById(R.id.notice);

		checkEngineInstalled();
	}

	/**
	 * 初始化界面
	 */
	private void initUI() {
		mViewHolder = (RelativeLayout) findViewById(R.id.view_holder);
		mControllerHolder = (LinearLayout) findViewById(R.id.controller_holder);
		// 创建BVideoView和BMediaController
		mVV = new BVideoView(this);
		mVVCtl = new BMediaController(this);
		mViewHolder.addView(mVV);
		mControllerHolder.addView(mVVCtl);

		// 注册listener
		mVV.setOnPreparedListener(this);
		mVV.setOnCompletionListener(this);
		mVV.setOnErrorListener(this);
		mVV.setOnInfoListener(this);
		mVVCtl.setPreNextListener(mPreListener, mNextListener);

		// 关联BMediaController
		mVV.setMediaController(mVVCtl);
		// 设置解码模式
		mVV.setDecodeMode(BVideoView.DECODE_SW);
	}

	/**
	 * 检测engine是否安装
	 * 
	 * @return
	 */
	private boolean isEngineInstalled() {
		BEngineManager mgr = BCyberPlayerFactory.createEngineManager();
		return mgr.EngineInstalled();
	}

	/**
	 * 检测engine是否安装,如果没有安装需要安装engine
	 */
	private void checkEngineInstalled() {
		if (isEngineInstalled()) {
			setInfo("CyberPlayerEngine Installed.\n");
			BEngineManager mgr = BCyberPlayerFactory.createEngineManager();
			mgr.initCyberPlayerEngine(AK, SK);
			initUI();

			// 发起一次播放任务,当然您不一定要在这发起
			mUIHandler.sendEmptyMessage(UI_EVENT_PLAY);
		} else {
			// 安装engine
			installEngine();
		}
	}

	/**
	 * 下载并安装engine
	 */
	private void installEngine() {
		BEngineManager mgr = BCyberPlayerFactory.createEngineManager();
		mgr.installAsync(mEngineListener);
	}

	private OnEngineListener mEngineListener = new OnEngineListener() {
		String info = "";

		String dlhead = "install engine: onDownload   ";
		String dlbody = "";

		@Override
		public boolean onPrepare() {
			info = "install engine: onPrepare.\n";
			setInfo(info);
			return true;
		}

		@Override
		public int onDownload(int total, int current) {
			if (dlhead != null) {
				info += dlhead;
				dlhead = null;
			}
			dlbody = current + "/" + total;
			setInfo(info + dlbody + "\n");
			return DOWNLOAD_CONTINUE;
		}

		@Override
		public int onPreInstall() {
			info += dlbody;
			info += "\n";
			info += "install engine: onPreInstall.\n";
			setInfo(info);

			return DOWNLOAD_CONTINUE;
		}

		@Override
		public void onInstalled(int result) {
			info += "install engine: onInstalled, ret = " + mRetInfo[result] + "\n";
			setInfo(info);
			if (result == OnEngineListener.RET_NEW_PACKAGE_INSTALLED) {
				// 安装完成

				BEngineManager mgr = BCyberPlayerFactory.createEngineManager();
				mgr.initCyberPlayerEngine(AK, SK);
				initUI();

				// 发起一次播放任务,当然您不一定要在这发起
				mUIHandler.sendEmptyMessage(UI_EVENT_PLAY);
			}
		}
	};

	private void setInfo(String info) {
		Message msg = new Message();
		msg.what = UPDATE_INFO;
		msg.obj = info;
		handler.sendMessage(msg);
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case UPDATE_INFO:
					notice.setText((String) msg.obj);
					break;
				default:
					break;
			}
		}
	};
	private final int UPDATE_INFO = 0;
	// 返回值对应的含义
	String[] mRetInfo = new String[] { "RET_NEW_PACKAGE_INSTALLED", "RET_NO_NEW_PACKAGE", "RET_STOPPED", "RET_CANCELED",
			"RET_FAILED_STORAGE_IO", "RET_FAILED_NETWORK", "RET_FAILED_ALREADY_RUNNING", "RET_FAILED_OTHERS",
			"RET_FAILED_ALREADY_INSTALLED", "RET_FAILED_INVALID_APK" };

	@Override
	protected void onPause() {
		super.onPause();
		Log.v(TAG, "onPause");
		// 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
		if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
			mLastPos = mVV.getCurrentPosition();
			mVV.stopPlayback();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(TAG, "onResume");
		if (null != mWakeLock && (!mWakeLock.isHeld())) {
			mWakeLock.acquire();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "onDestroy");
	}

	@Override
	public boolean onInfo(int what, int extra) {
		return false;
	}

	/**
	 * 播放出错
	 */
	@Override
	public boolean onError(int what, int extra) {
		Log.v(TAG, "onError");
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
		return true;
	}

	/**
	 * 播放完成
	 */
	@Override
	public void onCompletion() {
		Log.v(TAG, "onCompletion");
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
	}

	/**
	 * 播放准备就绪
	 */
	@Override
	public void onPrepared() {
		Log.v(TAG, "onPrepared");
		mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARED;
	}
}
