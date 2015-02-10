package com.ly.lottery.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ly.lottery.view.manager.BaseUI;

/**
 * Created by Administrator on 2015/2/10.
 */
public class FirstUI extends BaseUI
{
    private TextView textView;
    public FirstUI(Context context) {
        super(context);
        init();
    }

    /**
     * 解决目标界面每次都创建新界面问题
     */
    private void init()
    {
        textView = new TextView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        textView.setLayoutParams(layoutParams);
        textView.setText("这是第一个界面");
    }

    /**
     * 获取需要在中间容器加载的控件
     * @return
     */
    @Override
    public View getChild() {

        return textView;
    }
}
