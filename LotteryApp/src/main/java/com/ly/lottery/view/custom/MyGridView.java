package com.ly.lottery.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ly.lottery.R;
import com.ly.lottery.util.DensityUtil;

/**
 * Created by Administrator on 2015/2/12.
 */
public class MyGridView extends GridView{
    private PopupWindow popupWindow;
    private TextView ball;
    private OnActionUpListener onActionUpListener;

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // ①手指按下
        // 显示放大的号码
        // ②手指滑动
        // 更新：号码内容+显示位置
        // ③手指抬起
        // 修改手指下面的球的背景
        View view = View.inflate(context, R.layout.il_gridview_item_pop,null);
        ball = (TextView) view.findViewById(R.id.ii_pretextView);

        popupWindow = new PopupWindow(context);
        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(null); //不设置背景
        popupWindow.setAnimationStyle(0);
        //设置pop的大小
        popupWindow.setWidth(DensityUtil.dip2px(context,55));
        popupWindow.setHeight(DensityUtil.dip2px(context,53));

    }

    public void setOnActionUpListener(OnActionUpListener onActionUpListener) {
        this.onActionUpListener = onActionUpListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //当手指按下的时候，获取到点击那个球
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        // The position of the item which contains the specified point,
        // or INVALID_POSITION if the point does not intersect an item.
        int position = pointToPosition(x,y);

        if (position == INVALID_POSITION)
        {
            hiddenPop();
            return false;
        }

        TextView child = (TextView) getChildAt(position);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                // 当手指按下的时候，接管ScrollView滑动
//			this.getParent();//获取到LinearL
                this.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                showPop(child);
                break;
            case MotionEvent.ACTION_MOVE:
                updatePop(child);
                break;
            case MotionEvent.ACTION_UP:
                //当手指按下的时候，放行，scrollView滑动
                this.getParent().getParent().requestDisallowInterceptTouchEvent(false);
                hiddenPop();
                if (onActionUpListener!=null){
                    onActionUpListener.onActionUp(child,position);
                }
                break;
            default:
                break;

        }

        return super.onTouchEvent(ev);
    }

    private void updatePop(TextView child) {
        ball.setText(child.getText());
        int yOffset = -(popupWindow.getHeight() + child.getHeight());
        int xOffset = -(popupWindow.getWidth() - child.getWidth())/2;
        popupWindow.update(child,xOffset,yOffset,-1,-1);
    }

    private void showPop(TextView child) {
        int yOffset = -(popupWindow.getHeight() + child.getHeight());
        int xOffset = -(popupWindow.getWidth() - child.getWidth())/2;
        ball.setText(child.getText());
        popupWindow.showAsDropDown(child,xOffset,yOffset);
    }

    private void hiddenPop() {
        if (popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }

    public interface OnActionUpListener{
        /**
         * 手指抬起
         * @param view
         * @param position
         */
        void onActionUp(View view,int position);
    }
}
