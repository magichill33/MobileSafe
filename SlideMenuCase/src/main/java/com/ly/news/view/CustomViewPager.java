package com.ly.news.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2015/3/11.
 */
public class CustomViewPager extends LazyViewPager{
    private boolean setTouchModel = false;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (setTouchModel)
            return super.onInterceptTouchEvent(ev);
        else
            return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (setTouchModel)
            return super.onTouchEvent(ev);
        else
            return false;
    }
}
