package com.ly.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by magichill33 on 2015/2/5.
 */
public class ToggleButton extends View implements View.OnClickListener {

    /**
     * 做为背景的图片
     */
    private Bitmap backgroundBitmap;
    /**
     * 可以滑动的图片
     */
    private Bitmap slideBtn;
    private Paint paint;
    /**
     * 滑动按钮的左边届
     */
    private float slideBtn_left;


    /**
     * 在代码里面创建对象的时候，使用此构造方法
     */
    public ToggleButton(Context context) {
        super(context);
    }

    /**
     * 在布局文件中声名的view，创建时由系统自动调用。
     * @param context	上下文对象
     * @param attrs		属性集
     */
    public ToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    /**
     * 初始华
     */
    private void initView(){
        backgroundBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.switch_background);
        slideBtn = BitmapFactory.decodeResource(getResources(),
                R.drawable.slide_button);

        paint = new Paint(); //初始化 画笔
        paint.setAntiAlias(true); //打开抗矩齿

        //添加onclick事件监听
        setOnClickListener(this);
    }



    /**
     * View 对象显示的屏幕上，有几个重要步骤
     * 1.构造方法 创建对象。
     * 2.测量view的大小。onMeasure(int,int)
     * 3.确定view的位置，view自身有一些建议权，决定权在父view上 onLayout()
     * 4.绘制view的内容。onDraw(Canvas)
     *
     */

    /**
     * 测量尺寸的回调方法
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 设置当前view的大小
         * width: view宽度
         * height: view高度
         */
        setMeasuredDimension(backgroundBitmap.getWidth(),
                backgroundBitmap.getHeight());
    }

    //确定位置的时候调用此方法
    //自定义view的时候，作用不大
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     *当前开关的状态
     * true 为开
     */
    private boolean currState = false;
    /**
     * 绘制当前view的内容
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        /**
         *  backgroundBitmap	要绘制的图片
         * left	图片的左边届
         * top	图片的上边届
         * paint 绘制图片要使用的画笔
         */
        canvas.drawBitmap(backgroundBitmap,0,0,paint);
        //绘制可滑动的按键
        canvas.drawBitmap(slideBtn,slideBtn_left,0,paint);
    }

    /**
     * 判断是否发生拖动，
     * 如果拖动了，就不再响应 onclick 事件
     *
     */
    private boolean isDrag = false;

    @Override
    public void onClick(View v) {
        if (!isDrag){
            currState = !currState;
            flushState();
        }

    }

    /**
     * down 事件时的x值
     */
    private int firstX;
    /**
     * touch 事件的上一个x值
     */
    private int lastX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                firstX = lastX = (int) event.getX();
                isDrag = false;
                break;
            case MotionEvent.ACTION_MOVE:
                //判断是否发生拖动
                if (Math.abs(event.getX()-firstX)>5){
                    isDrag = true;
                }
                //计算手指在屏幕中移动的距离
                int dis = (int) (event.getX() - lastX);
                lastX = (int) event.getX();

                slideBtn_left += dis;
                break;
            case MotionEvent.ACTION_UP:
                if (isDrag){
                    int maxLeft = backgroundBitmap.getWidth() - slideBtn.getWidth(); // slideBtn
                    // 左边届最大值

                    if (slideBtn_left>maxLeft/2){
                        currState = true;
                    }else {
                        currState = false;
                    }
                    flushState();
                }
                break;

        }
        flushView();
        return true;
    }

    /**
     * 更新当开关的位置
     */
    private void flushState() {
        if (currState){
            slideBtn_left = backgroundBitmap.getWidth() - slideBtn.getWidth();
        }else {
            slideBtn_left = 0;
        }
        flushView();
    }

    /**
     * 刷新当前view
     */
    private void flushView() {
        /*
		 * 对 slideBtn_left  的值进行判断 ，确保其在合理的位置 即       0<=slideBtn_left <=  maxLeft
		 *
		 */
        int maxLeft = backgroundBitmap.getWidth() - slideBtn.getWidth();
        slideBtn_left = slideBtn_left>0?slideBtn_left:0;
        slideBtn_left = slideBtn_left<maxLeft?slideBtn_left:maxLeft;

        /**
         * 刷新当前视图 执行onDraw
         */
        invalidate();
    }
}
