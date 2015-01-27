package com.ly.mobilesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

/**
 * 启动一个服务用于自动清理进程
 */
public class AutoCleanService extends Service {
    private ScreenOffReceiver receiver;
    private ActivityManager am;

    public AutoCleanService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        receiver = new ScreenOffReceiver();
        registerReceiver(receiver,new IntentFilter(Intent.ACTION_SCREEN_OFF));
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        receiver = null;
        super.onDestroy();
    }

    //锁屏的广播事件是一个特殊的广播事件，在清单文件配置广播接收者是不会生效的。
    //只能在代码里面注册里面才会生效。
    private class ScreenOffReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("ScreenOffReceiver","屏幕锁屏了。。。");
            List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo info:infos){
                am.killBackgroundProcesses(info.processName);
            }
        }
    }
}
