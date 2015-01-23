package com.ly.mobilesafe;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Setup4Activity extends BaseSetupActivity {

    private CheckBox cbProtecting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        cbProtecting = (CheckBox) findViewById(R.id.cb_protecting);
        boolean protecting = sp.getBoolean("protecting", false);
        if(protecting)
        {
            cbProtecting.setText("手机防盗已经开启");
            cbProtecting.setChecked(true);
        }else{
            cbProtecting.setText("手机防盗没有开启");
            cbProtecting.setChecked(false);
        }

        cbProtecting.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    cbProtecting.setText("手机防盗已经开启");
                }else{
                    cbProtecting.setText("手机防盗没有开启");
                }

                Editor editor = sp.edit();
                editor.putBoolean("protecting", isChecked);
                editor.commit();
            }
        });

    }

    @Override
    public void showNext() {
        Editor editor = sp.edit();
        editor.putBoolean("configed", true);
        editor.commit();

        Intent intent = new Intent(this,LostFindActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
    }

    @Override
    public void showPre() {
        Intent intent = new Intent(this,Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
    }

}
