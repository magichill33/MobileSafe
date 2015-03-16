package com.lilosoft.xtcm.base;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKLocationManager;
import com.lilosoft.xtcm.module.AutoReportLocusService;

public class BMapApiDemoApp extends Application {

    private static final String TAG = "BMapApiDemoApp";
    private static final int SECOND = 1000;
    private static final int MINUTE = SECOND * 60;
    private static final int AUTO_REPORT_LOCUS_TIME = MINUTE * 1;// 轨迹自动上报间隔时间为2分钟
    static BMapApiDemoApp mDemoApp;
    public BMapManager mBMapMan = null;

    public String mStrKey = "E3041FEDFA4A24627A4B76539E07658B0FE44A5D";
    boolean m_bKeyRight = true;

    @Override
    public void onCreate() {
        mDemoApp = this;
        mBMapMan = new BMapManager(this);
        boolean isSuccess = mBMapMan
                .init(this.mStrKey, new MyGeneralListener());
        if (isSuccess) {
            mBMapMan.getLocationManager().setNotifyInternal(10, 5);
            mBMapMan.getLocationManager().setLocationCoordinateType(MKLocationManager.MK_COORDINATE_WGS84);

            startAlarm();
        } else {
        }
        super.onCreate();
    }

    private void startAlarm() {
        Log.d(TAG, "start alarm");
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent collectIntent = new Intent(this, AutoReportLocusService.class);
        PendingIntent collectSender
                = PendingIntent.getService(this, 0, collectIntent, 0);
        am.cancel(collectSender);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME
                , SystemClock.elapsedRealtime()
                , AUTO_REPORT_LOCUS_TIME
                , collectSender);
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        if (mBMapMan != null) {
            mBMapMan.destroy();
            mBMapMan = null;
        }
        super.onTerminate();
    }

    public static class MyGeneralListener implements MKGeneralListener {
        @Override
        public void onGetNetworkState(int iError) {
            Log.d("MyGeneralListener", "onGetNetworkState error is " + iError);
            Toast.makeText(BMapApiDemoApp.mDemoApp.getApplicationContext(),
                    "Network error", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onGetPermissionState(int iError) {
            Log.d("MyGeneralListener", "onGetPermissionState error is "
                    + iError);
            if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
                Toast.makeText(BMapApiDemoApp.mDemoApp.getApplicationContext(),
                        "Permission deny! Add key in BMapApiDemoApp.java!", Toast.LENGTH_LONG).show();
                BMapApiDemoApp.mDemoApp.m_bKeyRight = false;
            }
        }
    }

}
