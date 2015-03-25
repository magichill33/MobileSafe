package com.ly.study;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    private final int MSG_HELLO = 0;
    private Handler mHandler;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.hello);

        new CustomThread().start();
        findViewById(R.id.send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "hello";
                Log.e("Test", "MainThread is ready to send msg:" + str);
                mHandler.obtainMessage(MSG_HELLO, str).sendToTarget();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"我是子线程",Toast.LENGTH_LONG).show();
                        }
                    });
                    //Toast.makeText(MainActivity.this,"我是子线程",Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void startService(View view)
    {
        Intent intent = new Intent();
        intent.setClass(this,MyService.class);
        startService(new Intent());
    }

    public void bindMyService(View view)
    {
        Intent intent = new Intent();
        intent.setClass(this,MyService.class);
        bindService(intent,new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        },BIND_AUTO_CREATE);
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
                          //  textView.setText("你好");
                            Toast.makeText(MainActivity.this,"haha",Toast.LENGTH_SHORT).show();
                    }
                }
            };
            Looper.loop();//4、启动消息循环
        }
    }


}
