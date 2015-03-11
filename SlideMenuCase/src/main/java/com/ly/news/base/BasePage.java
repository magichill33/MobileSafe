package com.ly.news.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Administrator on 2015/3/11.
 */
public abstract class BasePage {
    protected Context ctx;
    private View view;

    /**
     * 1.画界面
     * 2.初始化数据
     * @param context
     */
    public BasePage(Context context){
        ctx = context;
        LayoutInflater inflater = (LayoutInflater) ctx.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = initView(inflater);
    }

    public View getRootView()
    {
        return view;
    }

    public abstract View initView(LayoutInflater inflater);

    public abstract void initData();
}
