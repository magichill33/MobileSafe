package com.ly.study;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;


public class MainActivity extends Activity {

    private final int MSG_HELLO = 0;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new CustomThread().start();
        findViewById(R.id.send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "hello";
                Log.e("Test", "MainThread is ready to send msg:" + str);
                mHandler.obtainMessage(MSG_HELLO, str).sendToTarget();
            }
        });
    }

    class CustomThread extends Thread {

        @SuppressLint("HandlerLeak")
        @Override
        public void run() {
            //建立消息循环的步骤
            Looper.prepare();//1.初始化Looper
            mHandler = new Handler() {//2、绑定handler到CustomThread实例的Looper对象
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case MSG_HELLO:
                            Log.e("Test", "CustomThread receive msg:" + (String) msg.obj);
                    }
                }
            };
            Looper.loop();//4、启动消息循环
        }
    }


}
