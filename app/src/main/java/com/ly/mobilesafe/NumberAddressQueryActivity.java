package com.ly.mobilesafe;

import com.ly.mobilesafe.dao.NumberAddressQueryUtils;

import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class NumberAddressQueryActivity extends Activity {

    private EditText etPhone;
    private TextView tvResult;

    /**
     * 系统提供的振动服务
     */
    private Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_address_query);

        etPhone = (EditText) findViewById(R.id.et_phone);
        tvResult = (TextView) findViewById(R.id.tv_result);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        etPhone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null&&s.length()>2)
                {
                    String address = NumberAddressQueryUtils.queryNumber(s.toString());
                    tvResult.setText(address);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * 查询号码归属地
     * @param view
     */
    public void onNumberAddressQuery(View view)
    {
        String phone = etPhone.getText().toString().trim();
        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "号码为空", 0).show();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            etPhone.startAnimation(shake);
            //当电话号码为空的时候，就去振动手机提醒用户
//			 vibrator.vibrate(2000);
            long[] pattern = {200,200,300,300,1000,2000};
            //-1不重复 0循环振动 1；
            vibrator.vibrate(pattern, -1);
        }else{
            String address = NumberAddressQueryUtils.queryNumber(phone);
            tvResult.setText(address);
            //去数据库查询号码归属地
            //1.网络查询 ；2.本地的数据库--数据库
            //写一个工具类，去查询数据库
            Log.i("NumberAddressQueryActivity", "您要查询的电话号码=="+phone);
        }
    }
}
