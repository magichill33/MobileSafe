package com.ly.study;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView mTextView = new TextView(this);
        mTextView.setText("我是自定义action并且加了权限的Activity.");
        setContentView(mTextView);
    }


}
