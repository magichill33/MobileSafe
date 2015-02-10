package com.ly.lottery.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ly.lottery.ConstantValue;
import com.ly.lottery.view.manager.BaseUI;

/**
 * Created by Administrator on 2015/2/10.
 */
public class FirstUI extends BaseUI
{
    public FirstUI(Context context) {
        super(context);
    }

    @Override
    protected void setListener() {

    }

    /**
     * 解决目标界面每次都创建新界面问题
     */
    @Override
    protected void init() {


    }

    /**
     * 获取需要在中间容器加载的控件
     * @return
     */
    @Override
    public View getChild() {
        //简单界面：
        TextView textView = new TextView(context);

        ViewGroup.LayoutParams layoutParams = new
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        textView.setLayoutParams(layoutParams);

        textView.setBackgroundColor(Color.BLUE);
        textView.setText("这是第一个界面");
        return textView;
    }

    @Override
    public int getID() {
        return ConstantValue.VIEW_FIRST;
    }


}
