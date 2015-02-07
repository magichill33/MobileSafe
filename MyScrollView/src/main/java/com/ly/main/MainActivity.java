package com.ly.main;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class MainActivity extends ActionBarActivity {

    private MyScrollView msv;

    /**
     * 图片资源ID数组
     */
    private int[] ids = {
            R.drawable.a1,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4,
            R.drawable.a5,
            R.drawable.a6
    };

    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msv = (MyScrollView) findViewById(R.id.myscroll_view);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        for (int i = 0;i<ids.length;i++){
            ImageView iv = new ImageView(this);
            iv.setBackgroundColor(Color.RED);
            iv.setImageResource(ids[i]);
            msv.addView(iv);
        }

        msv.setPageChangeListener(new MyScrollView.MyPageChangeListener() {
            @Override
            public void moveToDest(int currId) {
                ((RadioButton)radioGroup.getChildAt(currId)).setChecked(true);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                msv.moveToDest(checkedId);
            }
        });

        //给自定义viewGroup添加测试的布局
        View view = getLayoutInflater().inflate(R.layout.temp,null);
        msv.addView(view,2);


        for (int i=0;i<msv.getChildCount();i++){
            //添加radioButton
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(i);

            radioGroup.addView(radioButton);
            if (i==0){
                radioButton.setChecked(true);
            }
        }

    }

}
