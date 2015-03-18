package com.lilo.util;

import java.util.ArrayList;
import java.util.List;

import com.lilo.model.MessageEnum;
import com.lilo.service.RunQueryDataTask;
import com.lilo.sm.R;
import com.supermap.android.data.GetFeaturesResult;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.Point2D;
import com.supermap.services.components.commontypes.Feature;
import com.supermap.services.components.commontypes.Geometry;
import com.supermap.services.components.commontypes.Rectangle2D;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class DrawUtil {
    // 绘面风格
    public Paint getPolygonPaint(int color,int alpha,float width) {
        return getPolygonPaint(color, alpha, width, Paint.Style.STROKE);
    }

    public Paint getPolygonPaint(int color,int alpha,float width,Style style) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setAlpha(alpha);

        paint.setStyle(style);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(width);
        return paint;
    }


}
