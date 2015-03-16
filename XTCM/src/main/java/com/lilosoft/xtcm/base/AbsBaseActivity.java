package com.lilosoft.xtcm.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.lilosoft.xtcm.utils.ActivityManager;
import com.lilosoft.xtcm.views.MPProgressBar;

/**
 * @category 功能页面基类
 * @author William Liu
 *
 */
public abstract class AbsBaseActivity extends Activity {

    /**
     * @category 等待框状态
     */
    protected static boolean progressDialogIsShow = false;
    /**
     * @category 等待框对象
     */
    protected static MPProgressBar mPProgressBar;
    /**
     * @category 上下文对象
     */
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initFrameValues();
        installViews();
        registerEvents();
        ActivityManager.addActivity(this);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * @category 初始化内容
     */
    protected void initFrameValues() {
        mContext = this;
    }

    /**
     * @category 初始化试图
     */
    protected abstract void installViews();

    /**
     * @category 事件注册
     */
    protected abstract void registerEvents();

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        ActivityManager.removeTopActivity(this);
        super.onDestroy();
    }

}
