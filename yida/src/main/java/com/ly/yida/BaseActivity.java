package com.ly.yida;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


/**
 *
 * @author 阿福
 * @version 创建时间：2011-4-1 上午09:43:38 BaseActivity类说明:Acitivty的子类
 *          基础该类的子类必须实现onCreate 方法 在该类中注册了一个BroadcastReceiver 用于接收退出消息
 *          在接收到消息之后结束自身
 *
 *          -------------------------------- 使用 在自己所有的activity中继承该类
 *          到需要退出程序的时候发送广播 Intent intent = new
 *          Intent(context.getPackageName()+".ExitListenerReceiver");
 *
 *          context.sendBroadcast(intent); 即可。
 *
 *         退出程序
 */
public abstract class BaseActivity extends FragmentActivity {


    /**
     * 退出事件监听
     *
     */
    public ExitListenerReceiver exitre = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regListener();
    }


    /**
     * 注册退出事件监听
     *
     */
    private void regListener() {
        exitre = new ExitListenerReceiver();
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction(this.getPackageName() + "."
                + "ExitListenerReceiver");
        this.registerReceiver(exitre, intentfilter);
    }

    /**
     * 注册取消退出事件监听
     *
     */
    private void unregisterListener() {
        if (exitre != null) {
            unregisterReceiver(exitre);
            exitre = null;
        }

    }
    /**
     * 广播放接收者，用于接收关闭Activity信息，并关闭没有关的Activity
     * @author 阿福
     *
     */
    class ExitListenerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {

            ((Activity) arg0).finish();

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterListener();

    }

}