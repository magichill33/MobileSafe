package com.ly.customui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

   public void gotoWaterFall(View view){
       Intent intent = new Intent(this,WaterFallActivity.class);
       startActivity(intent);

   }

   public void gotoRing(View view){
        Intent intent = new Intent(this,RingActivity.class);
       startActivity(intent);
   }

    public void gotoSeniorRing(View view){
        Intent intent = new Intent(this,SeniorRingActivity.class);
        startActivity(intent);
    }
}
