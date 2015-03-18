package com.ly.demo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //访问应用demo里的ViewActivity
        Intent mIntent = new Intent();
        mIntent.setAction("android.tutor.action.VIEW");
        startActivity(mIntent);
        finish();
    }



}
