package com.ly.cloudstorage;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.ly.cloudstorage.net.IDataCallBack;


public class MainActivity extends SherlockActivity implements IDataCallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public void handleServiceResult(int requestCode, int errCode, Object data) {

    }
}
