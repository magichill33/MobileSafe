package com.lilo.layer;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;

import com.lilo.widget.TipForm;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.Point2D;
import com.supermap.android.maps.PolygonOverlay;
import com.supermap.services.components.commontypes.Geometry;

public abstract class TouchGridOverlay extends PolygonOverlay
{
    private boolean isAddPoint;
    private int touchDownX;
    private int touchDownY;
    private int touchX;
    private int touchY;
    private List<Geometry> touchGeoList;
    private Context context;
    private Handler handler;

    public TouchGridOverlay(Paint polygonPaint,Context context,Handler handler) {
        super(polygonPaint);
        this.context = context;
        this.handler = handler;
    }

    public void initTouchLays(List<Geometry> touchGeoList)
    {
        this.touchGeoList = touchGeoList;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
                isAddPoint = true;
                touchDownX = Math.round(event.getX());
                touchDownY = Math.round(event.getY());
                break;
            case MotionEvent.ACTION_MOVE: // 捕获手指触摸移动动作
                int x = Math.round(event.getX());
                int y = Math.round(event.getY());
                if (Math.abs(x - touchDownX) > 4 || Math.abs(y - touchDownY) > 4) {
                    isAddPoint = false;// 平移不加入该点
                }
                break;
            case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作
                if (isAddPoint) {
                    touchX = Math.round(event.getX());
                    touchY = Math.round(event.getY());
                    // 记录点击位置
                    Point2D touchPoint = mapView.getProjection().fromPixels(touchX, touchY);

                    Boolean isIn = false;
                    for(Geometry geo:touchGeoList)
                    {
                        isIn = Geometry.isPointInPolygon(new com.supermap.services.components.
                                commontypes.Point2D(touchPoint.x, touchPoint.y), geo);
                        if(isIn)
                            break;
                    }
                    if(isIn)
                    {
                        queryDataByPoint(touchPoint,handler);

                    }else
                    {
                        TipForm.showToast("请在划定的区域内点击",context.getApplicationContext());
                    }


                }
                break;
        }
        return false;
    }

    public abstract void queryDataByPoint(Point2D touchPoint,Handler handler);

}