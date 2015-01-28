package com.ly.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class EnterPwdActivity extends Activity {
    private EditText et_password;
    private TextView tv_name;
    private ImageView iv_icon;
    private String packname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pwd);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_name = (TextView) findViewById(R.id.tv_name);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        Intent intent = getIntent();
        packname = intent.getStringExtra("packname");
        PackageManager pm = getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            tv_name.setText(info.loadLabel(pm));
            iv_icon.setImageDrawable(info.loadIcon(pm));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {

        //回桌面
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addCategory(Intent.CATEGORY_MONKEY);
        startActivity(intent);
        //所有的activity最小化，不会执行ondestory，只执行onStop方法
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("EnterPwdActivity onStop");
        finish();
    }

    public void click(View view){
        String pwd = et_password.getText().toString().trim();
        if(TextUtils.isEmpty(pwd)){
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if("123".equals(pwd)){
            Intent intent = new Intent();
            intent.setAction("com.ly.mobilesafe.tempstop");
            intent.putExtra("packname",packname);
            sendBroadcast(intent);
            finish();
        }else {
            Toast.makeText(this,"密码错误。。。",Toast.LENGTH_SHORT).show();
        }
    }
}
