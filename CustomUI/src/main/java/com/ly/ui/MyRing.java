package com.ly.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by magichill33 on 2015/2/7.
 */
public class MyRing extends View{

    /**
     * 圆环圆心的坐标
     */
    private int cx;
    private int cy;

    private Paint paint;

    private float  radius;
    private float strokeWidth;


    public MyRing(Context context) {
        super(context);
    }

    public MyRing(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void initView(){

        //初始化paint
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setAlpha(255);

        radius = 0;
        strokeWidth = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制圆环
        canvas.drawCircle(cx,cy,radius,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                cx = (int) event.getX();
                cy = (int) event.getY();
                initView();
                handler.sendEmptyMessage(0);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return true;
    }

    private void flushState(){
        radius+=10;
        strokeWidth = radius/4;
        int nextAlpha = paint.getAlpha() - 20;
        if (nextAlpha <=20){
            nextAlpha = 0;
        }
        paint.setAlpha(nextAlpha);
        paint.setStrokeWidth(strokeWidth);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            flushState();
            //刷新页面，执行onDraw方法
            invalidate();
            if (paint.getAlpha()!=0){
                handler.sendEmptyMessageDelayed(0,100);
            }
        }
    };

}
