package com.ly.main;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ImageView;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msv = (MyScrollView) findViewById(R.id.myscroll_view);

        for (int i = 0;i<ids.length;i++){
            ImageView iv = new ImageView(this);
            iv.setBackgroundColor(Color.RED);
            iv.setImageResource(ids[i]);
            msv.addView(iv);
        }
    }

}
