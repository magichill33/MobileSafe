package com.ly.news.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ly.news.base.BasePage;

/**
 * Created by Administrator on 2015/3/11.
 */
public class FunctionPage extends BasePage{
    /**
     * 1.画界面
     * 2.初始化数据
     *
     * @param context
     */
    public FunctionPage(Context context) {
        super(context);
    }

    @Override
    public View initView(LayoutInflater inflater) {
        TextView textView = new TextView(ctx);
        textView.setText("我是首页");
        return textView;
    }

    @Override
    public void initData() {

    }
}
