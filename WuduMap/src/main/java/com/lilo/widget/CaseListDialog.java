package com.lilo.widget;

import java.util.ArrayList;
import java.util.List;

import com.lilo.model.CaseModel;
import com.lilo.sm.LiloMapActivity;
import com.lilo.sm.R;
import com.lilo.util.CaseCheckUtil;
import com.supermap.android.maps.Point2D;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.view.View.OnClickListener;

public class CaseListDialog extends Dialog {

    private ListView caseListView;
    private Context context;
    private List<CaseModel> caseModels;
    private CaseCheckUtil caseCheckUtil;

    // 记录按下事件点
    private float mTouchX;
    private float mTouchY;
    // 记录抬起事件点
    private float mTouchUpX;
    private float mTouchUpY;
    private List<Point2D> geoPoints = null;
    private CaseModel cas = null;
    /**
     * <p>
     * 当前窗口的布局，支持拖动动态布局
     * </p>
     */
    private WindowManager.LayoutParams lp;

    public CaseListDialog(Context context,CaseCheckUtil caseCheckUtil,List<CaseModel> models,int theme) {
        super(context,theme);
        this.context = context;
        this.caseModels = models;
        this.caseCheckUtil = caseCheckUtil;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_case);
        DisplayMetrics metric = new DisplayMetrics();
        LiloMapActivity mapActivity = (LiloMapActivity) context;
        mapActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 获取当前窗口布局
        lp = getWindow().getAttributes();
        lp.y = mapActivity.getTitleBarHeight();
        lp.width = (int) (metric.widthPixels*0.8);
        lp.height = (int) (metric.heightPixels*0.3);
        getWindow().setGravity(Gravity.CENTER | Gravity.TOP);
        getWindow().setAttributes(lp);
        caseListView = (ListView) findViewById(R.id.caseListView);
        caseListView.setAdapter(new CaseListAdpater(caseModels, caseCheckUtil,this,context));

    }

    /**
     * <p>
     * 相应触碰窗口事件，实现窗口的拖动
     * </p>
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
                // 获取相对View的坐标，即以此View左上角为原点
                mTouchX = event.getX();
                mTouchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE: // 捕获手指触摸移动动作
                break;
            case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作
                mTouchUpX = event.getX();
                mTouchUpY = event.getY();
                updateViewPosition();
                break;
        }
        return true;
    }

    private void updateViewPosition() {
        // 更新浮动窗口位置参数
        lp.x = (int) (lp.x + mTouchUpX - mTouchX); // 新位置X坐标
        lp.y = (int) (lp.y + mTouchUpY - mTouchY); // 新位置Y坐标
        this.onWindowAttributesChanged(lp); // 刷新显示
        this.show();
    }



}
