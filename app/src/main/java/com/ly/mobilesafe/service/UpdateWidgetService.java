package com.ly.mobilesafe.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

import com.ly.mobilesafe.R;
import com.ly.mobilesafe.receiver.MyWidget;
import com.ly.mobilesafe.utils.SystemInfoUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 用来定时更新widget信息
 */
public class UpdateWidgetService extends Service {
    protected static final String TAG = "UpdateWidgetService";
    private ScreenOffReceiver offReceiver;
    private ScreenOnReceiver onReceiver;
    private Timer timer;
    private TimerTask task;

    /**
     * widget管理器
     */
    private AppWidgetManager awm;

    public UpdateWidgetService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        onReceiver = new ScreenOnReceiver();
        offReceiver = new ScreenOffReceiver();
        registerReceiver(onReceiver,new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(offReceiver,new IntentFilter(Intent.ACTION_SCREEN_OFF));
        awm = AppWidgetManager.getInstance(this);
        startTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onReceiver);
        onReceiver = null;
        unregisterReceiver(offReceiver);
        offReceiver = null;
        stopTimer();
    }

    private void startTimer()
    {
        if(timer == null && task == null){
            timer = new Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    Log.i(TAG,"更新widget");
                    ComponentName provider = new ComponentName(
                            UpdateWidgetService.this, MyWidget.class);
                    RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
                    views.setTextViewText(R.id.process_count,
                            "正在运行的进程："+ SystemInfoUtils.
                                    getRunningProcessCount(UpdateWidgetService.this)+"个");
                    long size = SystemInfoUtils.getAvailMem(UpdateWidgetService.this);
                    views.setTextViewText(R.id.process_memory,"可用内存："+
                            Formatter.formatFileSize(getApplicationContext(),size));
                    //描述一个动作，这个动作是由另外一个应用程序执行的
                    //自定义一个广播事件，杀死后台进程的事件
                    Intent intent = new Intent();
                    intent.setAction("com.ly.mobilesafe.killall");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                            0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    views.setOnClickPendingIntent(R.id.btn_clear,pendingIntent);
                    awm.updateAppWidget(provider,views);
                }
            };
            timer.schedule(task,0,3000);
        }
    }

    private void stopTimer()
    {
        if(timer!=null&&task!=null){
            timer.cancel();
            task.cancel();
            timer = null;
            task = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class ScreenOffReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,"屏幕锁屏了...");
            stopTimer();
        }
    }

    private class ScreenOnReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,"屏幕解锁了...");
            startTimer();
        }
    }

}
