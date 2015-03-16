package com.lilosoft.xtcm.base;

import android.app.ActivityGroup;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.lilosoft.xtcm.R;

/**
 *
 * @category 外部框架底层实现方法
 * @author William Liu
 *
 */
@SuppressWarnings("deprecation")
public abstract class AbsBaseActivityGroup extends ActivityGroup {

    protected static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_frame);
        initFrameValues();
        installViews();
        registerEvents();
    }

    /**
     * @category 初始化框架
     */
    protected void initFrameValues() {
        mContext = this;
    }

    /**
     * @category 初始化视图
     */
    protected abstract void installViews();

    /**
     * @category 事件注册
     */
    protected abstract void registerEvents();
}
