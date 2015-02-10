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
public class SecondUI extends BaseUI{
    private TextView textView;
    public SecondUI(Context context) {
        super(context);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void init() {
        // 简单界面：
        textView = new TextView(context);

        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        textView.setLayoutParams(layoutParams);

        textView.setBackgroundColor(Color.RED);
        textView.setText("这是第二个界面");
    }

    @Override
    public View getChild() {
        return textView;
    }

    @Override
    public int getID() {
        return ConstantValue.VIEW_SECOND;
    }

}
