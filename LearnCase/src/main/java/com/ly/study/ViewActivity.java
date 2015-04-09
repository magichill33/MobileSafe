package com.ly.study;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ViewActivity extends Activity {
    private final String TAG = "LandActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView mTextView = new TextView(this);
        mTextView.setText("我是自定义action并且加了权限的Activity.");
        setContentView(mTextView);
        Log.d(TAG,"onCreate-1");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart-1");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume-1");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause-1");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop-1");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy-1");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"onRestart-1");
    }
}
