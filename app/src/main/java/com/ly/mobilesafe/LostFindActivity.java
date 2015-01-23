package com.ly.mobilesafe;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {

    private SharedPreferences sp;

    private TextView tv_safenumber;
    private ImageView iv_protecting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        boolean configed = sp.getBoolean("configed",false);
        if(configed)
        {
            setContentView(R.layout.activity_lost_find);
            tv_safenumber = (TextView) findViewById(R.id.tv_safenumber);
            iv_protecting = (ImageView) findViewById(R.id.iv_protecting);

            String safenumber = sp.getString("safenumber", "");
            tv_safenumber.setText(safenumber);
            boolean protecting = sp.getBoolean("protecting", false);
            if(protecting)
            {
                iv_protecting.setImageResource(R.drawable.lock);
            }else{
                iv_protecting.setImageResource(R.drawable.unlock);
            }
        }else{
            //还没有设置向导
            Intent intent = new Intent(this,Setup1Activity.class);
            startActivity(intent);
            //关闭当前页面
            finish();
        }

    }

    /**
     * 重新进入手机防盗设置向导页面
     * @param view
     */
    public void reEnterSetup(View view)
    {
        Intent intent = new Intent(this,Setup1Activity.class);
        startActivity(intent);
        //关闭当前页面
        finish();
    }
}
