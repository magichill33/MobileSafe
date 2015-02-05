package com.ly.ui;

import android.view.View;
import android.view.animation.RotateAnimation;

/**
 * 让指定的View执行旋转离开的动画
 * Created by Administrator on 2015/2/4.
 */
public class MyUtils {
    /**
     * 让指定view 延时 执行旋转离开的动画
     * @param view
     * @param offset
     */
    public static void startAnimOut(View view,long offset){
        /*
		 * 默认圆为 为view的左上角，
		 * 水平向右 为 0度
		 * 顺时针旋转度数增加
		 */
        RotateAnimation animation = new RotateAnimation(0,180,
                view.getWidth()/2,view.getHeight());
        animation.setDuration(500);
        animation.setStartOffset(offset);
        animation.setFillAfter(true); //动画执行完以后，保持最后的状态
        view.startAnimation(animation);
    }

    public static void startAnimOut(View view){
        startAnimOut(view,0);
    }

    /**
     * 让指定的view 延时执行 旋转进入的动画
     * @param view
     * @param offset
     */
    public static void startAnimIn(View view,long offset){
        /*
		 * 默认圆为 为view的左上角，
		 * 水平向右 为 0度
		 * 顺时针旋转度数增加
		 */
        RotateAnimation animation = new RotateAnimation(180,0,
                view.getWidth()/2,view.getHeight());
        animation.setDuration(500);
        animation.setFillAfter(true);
        animation.setStartOffset(offset);
        view.startAnimation(animation);

    }

    public static void startAnimIn(View view){
        startAnimIn(view,0);
    }
}
