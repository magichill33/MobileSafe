package com.lilosoft.xtcm.module;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Context;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKLocationManager;
import com.lilosoft.xtcm.base.HomeBaseActivity;
import com.lilosoft.xtcm.database.SharedPreferencesFactory;
import com.lilosoft.xtcm.instantiation.User;
import com.lilosoft.xtcm.network.HttpConnection;

/**
 * 上报轨迹服务
 * 
 * @author yzy
 * 
 */
public class CopyOfAutoReportLocusService extends Service {

	private static final String TAG = "AutoReportLocusService";

	private static final int SECOND = 1000;
	private static final int MINUTE = SECOND * 60;
	private static final int AUTO_REPORT_LOCUS_TIME = MINUTE * 1;// 轨迹自动上报间隔时间为2分钟
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
			//writelog("test::" + System.currentTimeMillis());
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
	// 百度MapAPI的管理类
	public BMapManager mBMapMan = null;
	// 授权Key
	// TODO: 请输入您的Key,
	// 申请地址：http://dev.baidu.com/wiki/static/imap/key/
	public String mStrKey = "E3041FEDFA4A24627A4B76539E07658B0FE44A5D";
	Location location;
	String contextService = Context.LOCATION_SERVICE;
	String SCREEN_ON = "android.intent.action.SCREEN_ON";  
	String SCREEN_OFF = "android.intent.action.SCREEN_OFF";  
	ScreenStatusReceiver screenStatusReceiver;
	private boolean localPass = true;// 标记坐标是否改变
	// 通过系统服务，取得LocationManager对象
	private LocationManager locationManager;// 位置服务
	private PowerManager pm;
	private PowerManager.WakeLock wakeLock;
	private boolean locationFlag = false;
	private android.location.LocationListener locationListener = new android.location.LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			if(locationFlag){
				if (location != null) {
					latitude = location.getLatitude();
					longitude = location.getLongitude();
				}
				Log.e("android自带位置服务轨迹是:", latitude + "---" + longitude);
			}
		}
	};
	//高德地图监听器
	private AMapLocationListener  aMapLocationListener = new AMapLocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(Location arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(AMapLocation location) {
			if (location != null) {
				if(locationFlag){
					GeoPoint geo = new GeoPoint((int)(location.getLatitude()*1E6),(int)(location.getLongitude()*1E6));
					int x = geo.getLatitudeE6();//得到geo 纬度，单位微度(度* 1E6)
					double x1 = ((double)x)/1000000;
					int y = geo.getLongitudeE6();//得到geo 经度，单位微度(度* 1E6)
					double y1 = ((double) y) / 1000000;
					latitude = x1;
					longitude = y1;
					Log.e("高德地图轨迹是:", latitude + "---" + longitude);
				}
			}

		}
	};
	//高德地图
	private LocationManagerProxy mAMapLocManager = null;

	public static void writelog(String out) {
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + "Log" + File.separator;
		File textlog = new File(path + "my_log2.txt");
		try {
			if (Environment.getExternalStorageState().equals("mounted")) {
				File mfile = new File(path);
				if (!mfile.exists()) {
					mfile.mkdirs();
				}
				if (!textlog.exists()) {
					textlog.createNewFile();
				}
				FileOutputStream of = new FileOutputStream(textlog, true);
				OutputStreamWriter writer = new OutputStreamWriter(of, "utf-8");
				writer.append(DateFormate(System.currentTimeMillis()));
				writer.append("latitude=" + latitude);
				writer.append("longitude=" + longitude);
				writer.append("::   ");
				writer.append(out);
				writer.append("\n");
				writer.flush();
				writer.close();
				of.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (textlog.length() > 1024 * 1024 * 5) {
			textlog.renameTo(new File(path
					+ DateFormate(System.currentTimeMillis()) + ".txt"));
		}
	}

	public static String DateFormate(long time) {
		SimpleDateFormat mDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return mDateFormat.format(time);

	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.e(TAG, "服务已创建!");
		startAlarm();
		mBMapMan = new BMapManager(CopyOfAutoReportLocusService.this);
		mBMapMan.init(mStrKey, new MyGeneralListener());
		mBMapMan.getLocationManager().setNotifyInternal(10, 5);

		mBMapMan.getLocationManager().requestLocationUpdates(mLocationListener);
		// mBMapMan.getLocationManager().setLocationCoordinateType(MKLocationManager.MK_COORDINATE_WGS84);
		mBMapMan.start();

		//高德地图
		mAMapLocManager = LocationManagerProxy.getInstance(this);
		mAMapLocManager.setGpsEnable(true);
//		mAMapLocManager.requestLocationUpdates(
//				LocationProviderProxy.AMapNetwork, AUTO_REPORT_LOCUS_TIME, 10, aMapLocationListener);

//		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//		PendingIntent pi = PendingIntent
//				.getBroadcast(
//						this,
//						0,
//						new Intent("COM.LILOSOFT.XTCM.MODULE.AUTO_LOCATION_REPORT_SERVICE"),
//						0);
//		long now = System.currentTimeMillis();
//		am.setInexactRepeating(AlarmManager.RTC_WAKEUP, now,
//				AUTO_REPORT_LOCUS_TIME, pi);

		locationManager = (LocationManager) getSystemService(contextService);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);// 高精度
		criteria.setAltitudeRequired(false);// 不要求海拔
		criteria.setBearingRequired(false);// 不要求方位
		criteria.setCostAllowed(true);// 允许有花费
		criteria.setPowerRequirement(Criteria.POWER_LOW);// 低功耗
		// 从可用的位置提供器中，匹配以上标准的最佳提供器
		String provider = locationManager.getBestProvider(criteria, true);
		// 获得最后一次变化的位置
		// location = locationManager.getLastKnownLocation(provider);

//		locationManager.requestLocationUpdates(provider,
//				AUTO_REPORT_LOCUS_TIME, 0, locationListener);

		if ("" == User.username) {
			try {
				User.username = new SharedPreferencesFactory().getTopUser(
						createPackageContext("com.lilosoft.xtcm",
								Context.CONTEXT_INCLUDE_CODE
										| Context.CONTEXT_IGNORE_SECURITY))
						.getUsername();
				MyTimerTask timerTask = new MyTimerTask();
				Timer timer = new Timer(true);
				timer.schedule(timerTask, 0, AUTO_REPORT_LOCUS_TIME);// 每隔2分钟执行一次任务
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			MyTimerTask timerTask = new MyTimerTask();
			Timer timer = new Timer(true);
			timer.schedule(timerTask, 0, AUTO_REPORT_LOCUS_TIME);// 每隔2分钟执行一次任务
		}

		// 屏幕状态广播

		// 屏幕状态广播初始化
		screenStatusReceiver = new ScreenStatusReceiver();
		IntentFilter screenStatusIF = new IntentFilter();
		screenStatusIF.addAction(Intent.ACTION_SCREEN_ON);
		screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF);
		// 注册
		registerReceiver(screenStatusReceiver, screenStatusIF);
	}

	private void startAlarm() {
		Log.d(TAG, "start alarm");
		AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Intent collectIntent = new Intent(this, CopyOfAutoReportLocusService.class);
		PendingIntent collectSender
			= PendingIntent.getService(this, 0, collectIntent, 0);
		am.cancel(collectSender);
		am.setRepeating(AlarmManager.ELAPSED_REALTIME
			, SystemClock.elapsedRealtime()
			, 10 * 1000
			, collectSender);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG, "服务已开始!");
		return super.onStartCommand(intent, START_STICKY, startId);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		registerWakeLock();
	}

	private void registerWakeLock(){
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		// 保持cpu一直运行，不管屏幕是否黑屏
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				"CPUKeepRunning");
		wakeLock.acquire();
	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "服务已被銷燬!");
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		// startService(new Intent(
		// "COM.LILOSOFT.XTCM.MODULE.AUTO_LOCATION_REPORT_SERVICE"));
		if (locationListener != null) {
			locationManager.removeUpdates(locationListener);
		}
		wakeLock.release();
		unregisterReceiver(screenStatusReceiver);
	}	private LocationListener mLocationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				
				mBMapMan.getLocationManager().removeUpdates(mLocationListener);
				mBMapMan.stop();
			}
			Log.e("百度地图轨迹是:", latitude + "---" + longitude);
		}
	};

	public  void wakeUpAndUnlock(Context context,int paramInt){
        KeyguardManager km= (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"bright");
        //点亮屏幕
        wl.acquire();
        setScreenBrightness(context,paramInt);
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 1);
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 1);


        //释放
        wl.release();
    }

	private void setScreenBrightness(Context context,int paramInt){
		Window localWindow = ((Activity)HomeBaseActivity.mmContext).getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        float f = paramInt / 255.0F;
        localLayoutParams.screenBrightness = f;
        localWindow.setAttributes(localLayoutParams);
        Settings.System.putInt(HomeBaseActivity.mmContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);
      }

	private class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			handler.sendEmptyMessage(0);
		}
	}
	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	class MyGeneralListener implements MKGeneralListener {
		@Override
		public void onGetNetworkState(int iError) {
			Toast.makeText(CopyOfAutoReportLocusService.this, "您的网络出错啦！",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				Toast.makeText(CopyOfAutoReportLocusService.this,
						"请在BMapApiDemoApp.java文件输入正确的授权Key！", Toast.LENGTH_LONG)
						.show();
			}
		}
	}
	
    /**
	 * 屏幕状态广播
	 * @author yuegy
	 *
	 */
	class ScreenStatusReceiver extends BroadcastReceiver {
		String SCREEN_ON = "android.intent.action.SCREEN_ON";
		String SCREEN_OFF = "android.intent.action.SCREEN_OFF";

		@Override
		public void onReceive(Context context, Intent intent) {
			// 屏幕唤醒
			int brightness = 0;
			if(SCREEN_ON.equals(intent.getAction())){
				locationFlag = false;
				brightness = Settings.System.getInt(HomeBaseActivity.mmContext.getContentResolver(),
	                    Settings.System.SCREEN_BRIGHTNESS,0);
				//wakeUpAndUnlock(context,brightness);
//				startService(new Intent(
//						"COM.LILOSOFT.XTCM.MODULE.AUTO_LOCATION_REPORT_SERVICE"));
			}
			// 屏幕休眠
			else if(SCREEN_OFF.equals(intent.getAction())){
//				Log.e(TAG, SCREEN_OFF);
				brightness = Settings.System.getInt(HomeBaseActivity.mmContext.getContentResolver(),
	                    Settings.System.SCREEN_BRIGHTNESS,1);
				//wakeUpAndUnlock(context,brightness);
				locationFlag = true;
				wakeLock.release();
				registerWakeLock();
				startService(new Intent(
						"COM.LILOSOFT.XTCM.MODULE.AUTO_LOCATION_REPORT_SERVICE"));
			}
		}
	}
	


}
