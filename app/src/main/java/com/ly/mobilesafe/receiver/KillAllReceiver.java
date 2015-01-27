package com.ly.mobilesafe.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

public class KillAllReceiver extends BroadcastReceiver {
    public KillAllReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("KillAllReceiver","自定义的消息接收到了...");
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo info:infos){
            am.killBackgroundProcesses(info.processName);
        }
    }
}
