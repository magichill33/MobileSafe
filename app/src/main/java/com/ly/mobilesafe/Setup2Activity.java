package com.ly.mobilesafe;

import com.ly.mobilesafe.ui.SettingItemView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Setup2Activity extends BaseSetupActivity {

    private SettingItemView siv_setup2_sim;
    //读取手机sim卡的信息
    private TelephonyManager tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        siv_setup2_sim = (SettingItemView) findViewById(R.id.siv_setupt_sim);
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String sim = sp.getString("sim", null);
        if(TextUtils.isEmpty(sim))
        {
            siv_setup2_sim.setChecked(false);
        }else{
            siv_setup2_sim.setChecked(true);
        }
        siv_setup2_sim.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Editor editor =  sp.edit();
                if(siv_setup2_sim.isChecked())
                {
                    editor.putString("sim", null);
                    siv_setup2_sim.setChecked(false);
                }else{
                    //保存sim卡序列号
                    String sim = tm.getSimSerialNumber();
                    editor.putString("sim", sim);
                    siv_setup2_sim.setChecked(true);
                }
                editor.commit();
            }
        });
    }

    @Override
    public void showNext() {
        String sim = sp.getString("sim", null);
        if(TextUtils.isEmpty(sim)){
            //没有绑定
            Toast.makeText(this, "sim卡没有绑定", 1).show();
            return ;
        }

        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
        //要求在finish()或者startActivity(intent);后面执行；
        overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);

    }

    @Override
    public void showPre() {
        Intent intent = new Intent(this,Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
    }

}
