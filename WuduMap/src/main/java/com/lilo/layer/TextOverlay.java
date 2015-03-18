package com.lilo.layer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.supermap.android.maps.MapView;
import com.supermap.android.maps.Overlay;
import com.supermap.android.maps.Point2D;

/**
 * 自定义Overlay 用来进行文字标注 
 */
public class TextOverlay extends Overlay {

    private Point2D cPoint;
    private String mark;

    public TextOverlay(Point2D cPoint, String mark) {
        super();
        this.cPoint = cPoint;
        this.mark = mark;
    }



    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        super.draw(canvas, mapView, shadow);
        Paint paint = new Paint();
        Point point = mapView.getProjection().toPixels(cPoint, null);
        paint.setTextSize(24);
        paint.setStrokeWidth(0.8f);
        paint.setARGB(255, 255, 0, 0);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText(mark, point.x, point.y, paint);
    }
}
