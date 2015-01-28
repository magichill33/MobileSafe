package com.ly.mobilesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.ly.mobilesafe.EnterPwdActivity;
import com.ly.mobilesafe.dao.ApplockDao;

import java.util.List;

/**
 * 看门狗代码，监视系统程序的运行状态
 */
public class WatchDogService extends Service {
    private ActivityManager am;
    private boolean flag;
    private ApplockDao dao;
    private String tempStopProtectPackname;
    private InnerReceiver innerReceiver;
    private ScreenOffReceiver screenOffReceiver;
    private ScreenOnReceiver screenOnReceiver;
    private DataChangeReceiver dataChangeReceiver;

    private List<String> protectPacknames;
    private Intent intent;

    public WatchDogService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        screenOffReceiver = new ScreenOffReceiver();
        registerReceiver(screenOffReceiver,new IntentFilter(Intent.ACTION_SCREEN_OFF));
        screenOnReceiver = new ScreenOnReceiver();
        registerReceiver(screenOnReceiver,new IntentFilter(Intent.ACTION_SCREEN_ON));
        innerReceiver = new InnerReceiver();
        registerReceiver(innerReceiver,new IntentFilter("com.ly.mobilesafe.tempstop"));
        dataChangeReceiver = new DataChangeReceiver();
        registerReceiver(dataChangeReceiver,new IntentFilter("com.ly.mobilesafe.applockchange"));

        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        dao = new ApplockDao(this);
        protectPacknames = dao.findAll();
        flag = true;
        intent = new Intent(getApplicationContext(),
                EnterPwdActivity.class);
        //服务是没有任务栈信息的，在服务开启activity,要指定这个activity运行的任务栈
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        new Thread(){
            @Override
            public void run() {
                while (flag){
                    List<ActivityManager.RunningTaskInfo> infos = am.getRunningTasks(1);
                    String packname = infos.get(0).topActivity.getPackageName();
                    //Log.i("WatchDogService",packname + "::" + tempStopProtectPackname);
                    if(protectPacknames.contains(packname)){
                       if(!packname.equals(tempStopProtectPackname)){
                           intent.putExtra("packname",packname);
                           startActivity(intent);
                       }
                    }

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
        unregisterReceiver(innerReceiver);
        innerReceiver = null;
        unregisterReceiver(screenOnReceiver);
        screenOnReceiver = null;
        unregisterReceiver(screenOffReceiver);
        screenOffReceiver = null;
        unregisterReceiver(dataChangeReceiver);
        dataChangeReceiver = null;
    }

    /**
     * 锁屏接收广播
     */
    private class ScreenOffReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            tempStopProtectPackname = null;
            flag = false;
        }
    }

    private  class ScreenOnReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            flag = true;
        }
    }

    /**
     * 验证密码后发送此广播
     */
    private class InnerReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

            tempStopProtectPackname = intent.getStringExtra("packname");
            System.out.println("接收到了临时停止保护的广播事件:"+tempStopProtectPackname);
        }
    }

    /**
     * 锁屏数据发生改变后发送此广播
     *
     */
    private class DataChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("数据库的内容变华了");
            protectPacknames = dao.findAll();
        }
    }
}
