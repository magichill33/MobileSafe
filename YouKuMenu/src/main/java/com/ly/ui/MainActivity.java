package com.ly.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements View.OnClickListener {
    private final static String TAG = "MainActivity";
    private ImageView icon_menu;
    private ImageView icon_home;

    private RelativeLayout level1;
    private RelativeLayout level2;
    private RelativeLayout level3;

    private boolean isLevel1Show = true;
    private boolean isLevel2Show = true;
    private boolean isLevel3Show = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* StringBuffer title = new StringBuffer();
        setStr(title);
        Log.i(TAG,"TEST::"+title.toString());*/
        icon_home = (ImageView) findViewById(R.id.icon_home);
        icon_menu = (ImageView) findViewById(R.id.icon_menu);
        level1 = (RelativeLayout) findViewById(R.id.level1);
        level2 = (RelativeLayout) findViewById(R.id.level2);
        level3 = (RelativeLayout) findViewById(R.id.level3);

        icon_home.setOnClickListener(this);
        icon_menu.setOnClickListener(this);
    }

    private void setStr(StringBuffer str){
        str.append("helloworld");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icon_home:
                if (isLevel2Show){
                    MyUtils.startAnimOut(level2);
                    if (isLevel3Show){
                        MyUtils.startAnimOut(level3,200);
                        isLevel3Show = false;
                    }
                }else {
                    MyUtils.startAnimIn(level2);
                }
                isLevel2Show = !isLevel2Show;
                break;
            case R.id.icon_menu:
                if (isLevel3Show){
                    MyUtils.startAnimOut(level3);
                }else {
                    MyUtils.startAnimIn(level3);
                }
                isLevel3Show = !isLevel3Show;
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU){ //监听menu按键
            changeLevel1State();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void changeLevel1State() {
        Log.i(TAG,isLevel1Show+"");
        //如果第1级菜单是显示状态，那么就隐藏1，2，3级菜单
        if (isLevel1Show){
            MyUtils.startAnimOut(level1);
            if (isLevel2Show){
                MyUtils.startAnimOut(level2,100);
                isLevel2Show = false;
                if (isLevel3Show){
                    MyUtils.startAnimOut(level3,200);
                    isLevel3Show = false;
                }
            }
        }else {
            MyUtils.startAnimIn(level1);
            MyUtils.startAnimIn(level2,200);
            isLevel2Show = true;
        }
        isLevel1Show = !isLevel1Show;
    }
}
