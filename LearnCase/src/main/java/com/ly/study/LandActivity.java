package com.ly.study;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class LandActivity extends Activity {
    private final String Tag = "LandActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land);
        Log.d(Tag,"onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(Tag,"onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Tag,"onResume");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(Tag,"onSaveInstanceState");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(Tag,"onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(Tag,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(Tag,"onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(Tag,"onRestart");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(Tag,"onRestoreInstanceState");
    }

    public void onChangeActivity(View view){
        Intent intent = new Intent(this,ViewActivity.class);
        startActivity(intent);
    }
}
