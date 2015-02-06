package com.ly.main;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2015/2/6.
 */
public class MyScrollView extends ViewGroup{

    private static final String TAG = "MyScrollView";
    private MyScroller myScroller;
    /**
     * 手势识别的工具类
     */
    private GestureDetector detector;
    /**
     * 当前的ID值
     * 显示在屏幕上的子View的下标
     */
    private int currId = 0;
    /**
     * down 事件时的坐标
     */
    private int firstX = 0;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context ctx){

        myScroller = new MyScroller(ctx);
        detector = new GestureDetector(ctx,new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            /**
             * 响应手指在屏幕上的滑动事件
             * @param e1
             * @param e2
             * @param distanceX
             * @param distanceY
             * @return
             */
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                /**
                 * 移动当前view内容 移动一段距离
                 * disX X方向移动的距离 为正是，图片向左移动，为负时向右移动
                 * disY Y方向移动的距离
                 */
                scrollBy((int) distanceX,0);

                /**
                 * 将当前视图的基准点移动到某个点  坐标点
                 * x 水平方向X坐标
                 * Y 竖直方向Y坐标
                 *  scrollTo(x,  y);
                 */
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
    }

    /**
     * 对子View进行布局，确定子view的位置
     * @param changed 若为true，说明布局发生了变化
     * @param l 当前viewgroup 在其父view中的位置
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0;i<getChildCount();i++){
            View view = getChildAt(i); //取得下标为I的子View

            /**
             * 父view 会根据子view的需求，和自身的情况，来综合确定子view的位置,(确定他的大小)
             */
            //指定子view的位置  ,  左，上，右，下，是指在viewGround坐标系中的位置
            view.layout(0+i*getWidth(),0,getWidth()+i*getWidth(),
                    getHeight());

            Log.i(TAG,"Parent::"+getWidth());
            Log.i(TAG,"Child::"+getWidth());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        detector.onTouchEvent(event);

        //添加自己的事件解析
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                firstX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int nextId = 0;
                if (event.getX()-firstX>getWidth()/2){
                    nextId = currId - 1;
                }else if (firstX - event.getX()>getWidth()/2){
                    nextId = currId + 1;
                }else {
                    nextId = currId;
                }
                moveToDest(nextId);
                break;
        }

        return true;
    }

    /**
     * 移动到指定的屏幕上
     * @param nextId 屏幕的下标
     */
    private void moveToDest(int nextId) {
        /*
		 * 对 nextId 进行判断 ，确保 是在合理的范围
		 * 即  nextId >=0  && next <=getChildCount()-1
		 */

        //确保 currId>=0
        currId = (nextId>=0)?nextId:0;
        //确保 currId<=getChildCount-1
        currId = currId<getChildCount()?currId:(getChildCount()-1);
        Log.i(TAG,"currId::"+currId);
        //瞬间移动
       // scrollTo(currId*getWidth(),0);

        //最终位置 - 现在位置 = 要移动的距离
        int distance = currId*getWidth() - getScrollX();

        myScroller.startScroll(getScrollX(),0,distance,0);

        /**
         * 刷新当前View onDraw方法的执行
         */
        invalidate();
    }

    /**
     * invalidate() 会导致 computeScroll()这个方法的执行
     */
    @Override
    public void computeScroll() {
        if (myScroller.computeScrollOffset()){
            int newX = (int) myScroller.getCurrX();
            Log.i(TAG,"newX::"+newX);
            scrollTo(newX,0);
            invalidate();
        }
    }
}
