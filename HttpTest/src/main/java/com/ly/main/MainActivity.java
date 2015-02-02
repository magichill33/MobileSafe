package com.ly.main;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ly.utils.NetUtils;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);

    }

    /**
     * 使用httpClient方式提交get请求
     * @param view
     */
    public void doHttpClientOfGet(View view){
        final String userName = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String state = NetUtils.loginHttpGet(userName, password);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,state,Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }

    public void doHttpClientOfPost(View view){
        final String userName = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String state = NetUtils.loginHttpPost(userName, password);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,state,Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }

    public void doGet(View view){
        final String userName = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String state = NetUtils.loginOfGet(userName,password);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG,state);
                        Toast.makeText(MainActivity.this,state,Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }

    public void doPost(View view){
        final String userName = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String state = NetUtils.loginOfPost(userName, password);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,state,Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }
}
