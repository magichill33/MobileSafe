package com.ly.lottery.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ly.lottery.view.manager.BaseUI;

/**
 * Created by Administrator on 2015/2/10.
 */
public class SecondUI extends BaseUI{
    private TextView textView;
    public SecondUI(Context context) {
        super(context);
        init();
    }

    private void init()
    {
        textView = new TextView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        textView.setLayoutParams(layoutParams);
        textView.setText("这是第二个界面");
    }

    @Override
    public View getChild() {

        return textView;
    }
}
