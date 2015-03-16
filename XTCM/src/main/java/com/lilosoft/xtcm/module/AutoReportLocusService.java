package com.lilosoft.xtcm.module;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKSearch;
import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.BMapApiDemoApp;
import com.lilosoft.xtcm.instantiation.User;
import com.lilosoft.xtcm.network.HttpConnection;



public class AutoReportLocusService extends Service {

	private static final String TAG = "AutoReportLocusService";
	public static double latitude = 0.0;// 纬度
	public static double longitude = 0.0;// 经度
	Thread submitLocal = new Thread(new Runnable() {

		@Override
		public void run() {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			String currDate = format.format(Calendar.getInstance().getTime());
			Log.e(TAG, User.username + "--" + latitude + "--" + longitude
					+ "--" + currDate);
			HttpConnection httpConnection = new HttpConnection();
			try {
				httpConnection.getData(
						HttpConnection.CONNECTION_LOCATION_REPORT,
						User.username, "GridOfficer", longitude + "", latitude
								+ "");
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	});
	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			// getLocation();// 获取位置信息
			if (0 != latitude && 0 != longitude && User.username != null
					&& !"".equals(User.username)) {
				Thread thread = new Thread(submitLocal);// 上传轨迹及账号信息
				thread.start();
			} else {
				Log.e(TAG, "没有获取到地址!");
			}
		};
	};
	LocationListener mLocationListener = null;
	BMapApiDemoApp app = null;
	Location location;
	String contextService = Context.LOCATION_SERVICE;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		//写一个空的通知，保证服务不被杀死
//		Intent noti = new Intent(this, AutoReportLocusService.class);
//		Notification n = new Notification(R.drawable.logo, "城管通", java.lang.System.currentTimeMillis());
//		PendingIntent pendingIntent = PendingIntent.getService(this, 0, noti, 0);
//		n.setLatestEventInfo(this, "0", "城管通服务", pendingIntent);
//		startForeground(0x1982, n);

		app = (BMapApiDemoApp) this.getApplication();
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey,
					new BMapApiDemoApp.MyGeneralListener());
		}
		app.mBMapMan.start();

		// 注册定位事件
		mLocationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				Log.d(TAG, "location change");
				if (location != null) {
					String strLog = String.format("经度:%f\r\n" + "纬度:%f",
							location.getLongitude(),
							location.getLatitude());
					Log.d(TAG, strLog);
					latitude = location.getLatitude();
					longitude = location.getLongitude();
//					GeoPoint point = new GeoPoint((int)(location.getLatitude() * 1E6), (int)(location.getLongitude() * 1E6));
					handler.sendEmptyMessage(0);
					app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);
					app.mBMapMan.stop();
				}
			}
		};

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {//客户端启动此service，调用此方法
		app.mBMapMan.getLocationManager().requestLocationUpdates(
				mLocationListener);
		app.mBMapMan.start();
		return super.onStartCommand(intent, START_STICKY, startId);
//		return super.onStartCommand(intent, START_REDELIVER_INTENT, startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Intent intent = new Intent(this, AutoReportLocusService.class);
		startService(intent);
	}

}
