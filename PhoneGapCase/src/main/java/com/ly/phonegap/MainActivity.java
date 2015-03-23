package com.ly.phonegap;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.cordova.DroidGap;


public class MainActivity extends DroidGap {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set by <content src="index.html" /> in config.xml
        super.loadUrl("file:///android_asset/www/index.html");
        //super.loadUrl("file:///android_asset/www/index.html")
    }


}
