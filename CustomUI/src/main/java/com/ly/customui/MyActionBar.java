package com.ly.customui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MyActionBar extends ActionBarActivity {

    ActionBar actionBar;
    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_action_bar);
        txt = (TextView) findViewById(R.id.txt);
        actionBar = getSupportActionBar();
        //隐藏label
        //actionBar.setDisplayShowTitleEnabled(false);
        //设置是否显示应用程序图标
        actionBar.setDisplayShowHomeEnabled(true);
        //将应用程序图标设置为可点击的按钮
        actionBar.setHomeButtonEnabled(true);
        //将应用程序图标设置为可点击的按钮，并在图标上添加向左箭头
        //actionBar.setDisplayHomeAsUpEnabled(true);
    }


    public void showActionBar(View source){
        actionBar.show();
    }

    public void hideActionBar(View source){
        actionBar.hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        if(mi.isCheckable())
        {
            mi.setChecked(true);
        }
        // 判断单击的是哪个菜单项，并针对性的作出响应。
        switch (mi.getItemId())
        {
            case android.R.id.home:
                // 创建启动FirstActivity的Intent
                Intent intent = new Intent(this, RingActivity.class);
                // 添加额外的Flag，将Activity栈中处于FirstActivity之上的Activity弹出
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // 启动intent对应的Activity
                startActivity(intent);
                break;
            case R.id.font_10:
                txt.setTextSize(10 * 2);
                break;
            case R.id.font_12:
                txt.setTextSize(12 * 2);
                break;
            case R.id.font_14:
                txt.setTextSize(14 * 2);
                break;
            case R.id.font_16:
                txt.setTextSize(16 * 2);
                break;
            case R.id.font_18:
                txt.setTextSize(18 * 2);
                break;
            case R.id.red_font:
                txt.setTextColor(Color.RED);
                mi.setChecked(true);
                break;
            case R.id.green_font:
                txt.setTextColor(Color.GREEN);
                mi.setChecked(true);
                break;
            case R.id.blue_font:
                txt.setTextColor(Color.BLUE);
                mi.setChecked(true);
                break;
            case R.id.plain_item:
                Toast toast = Toast.makeText(MyActionBar.this, "您单击了普通菜单项",
                        Toast.LENGTH_SHORT);
                toast.show();
                break;
        }
        return true;
    }
}
