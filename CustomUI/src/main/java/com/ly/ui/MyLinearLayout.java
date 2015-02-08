package com.ly.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by magichill33 on 2015/2/7.
 */
public class MyLinearLayout extends LinearLayout{
    private static final String TAG = "MyLinearLayout";

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 拦截事件派发
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int count = getChildCount();
        int width = getWidth()/count; //每个listview的宽度
        int height = getHeight(); //每个listview的高度
        float eventX = event.getX();
        Log.i(TAG,"width::"+width);
        Log.i(TAG,"eventX::"+eventX);
        event.setLocation(width / 2, event.getY());

        if (eventX<width){ //滑动左边的listview
            //event.setLocation(width/2,event.getY()); //设置滑动点位置
            getChildAt(0).dispatchTouchEvent(event); //派发事件，好让其listview执行onTouchEvent方法
            return true;
        }else if (eventX>width&&eventX<2*width){ //滑动中间的listview
            float eventY = event.getY();
            if (eventY<height/2){ //上半部分滑动，所有listview都滑动
                //event.setLocation(width/2,event.getY());
                for (int i=0;i<count;i++){
                    View child = getChildAt(i);
                    child.dispatchTouchEvent(event);
                }
            }else {
               // event.setLocation(width/2,event.getY());
                getChildAt(1).dispatchTouchEvent(event);
                return true;
            }
        }else if (eventX>2*width){
           // event.setLocation(width/2,event.getY());
            getChildAt(2).dispatchTouchEvent(event);
            return true;
        }

        return true;
    }
}
