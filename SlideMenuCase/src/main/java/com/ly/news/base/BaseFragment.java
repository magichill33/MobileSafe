package com.ly.news.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2015/3/11.
 */
public abstract class BaseFragment extends Fragment{
    protected View view;
    protected Context ctx;

    /**
     * Activity创建完成
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    /**
     * fragment创建完成
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getActivity();
    }

    /**
     * 绘制fragment中的view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = initView(inflater);
        return view;
    }

    public View getRootView(){
        return view;
    }

    /**
     * 初始化view
     * @param inflater
     * @return
     */
    public abstract View initView(LayoutInflater inflater);

    /**
     * 初始化数据
     * @param saveInstanceState
     */
    public abstract void initData(Bundle saveInstanceState);
}
