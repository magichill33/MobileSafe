package com.ly.lottery.view.manager;

import android.content.Context;
import android.view.View;

/**
 * 所有界面的基类
 * Created by Administrator on 2015/2/10.
 */
public abstract class BaseUI {
    protected Context context;

    public BaseUI(Context context){
        this.context = context;
    }

    /**
     * 获取需要在中间容器加载的内容
     * @return
     */
    public abstract View getChild();
}
