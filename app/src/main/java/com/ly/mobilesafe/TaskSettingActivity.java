package com.ly.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ly.mobilesafe.service.AutoCleanService;
import com.ly.mobilesafe.utils.ServiceUtils;


public class TaskSettingActivity extends Activity {
    private CheckBox cb_show_system;
    private CheckBox cb_auto_clean;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_setting);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        cb_show_system = (CheckBox) findViewById(R.id.cb_show_system);
        cb_auto_clean = (CheckBox) findViewById(R.id.cb_auto_clean);
        cb_show_system.setChecked(sp.getBoolean("showsystem",false));
        cb_show_system.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("showsystem",isChecked);
                editor.commit();
            }
        });

        cb_auto_clean.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent = new Intent(TaskSettingActivity.this, AutoCleanService.class);
                if(isChecked){
                    startService(intent);
                }else {
                    stopService(intent);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        boolean running = ServiceUtils.isServiceRunning(this,AutoCleanService.class.getName());
        cb_auto_clean.setChecked(running);
        super.onStart();
    }
}
