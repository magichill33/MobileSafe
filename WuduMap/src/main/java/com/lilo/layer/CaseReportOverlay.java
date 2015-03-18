package com.lilo.layer;

import com.lilo.service.RunQueryByPoint;
import com.lilo.util.CaseReportUtil;
import com.lilo.widget.TipForm;
import com.supermap.android.maps.DefaultItemizedOverlay;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.OverlayItem;
import com.supermap.android.maps.Point2D;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;

public class CaseReportOverlay extends TouchGridOverlay{
    private Context context;
    private DefaultItemizedOverlay overlay;
    private MapView mapView;
    private String url;
    private String dataSet;
    private CaseReportUtil caseReportUtil;

    public CaseReportOverlay(Paint polygonPaint,Context context,MapView mapView,
                             DefaultItemizedOverlay overlay,String url,String dataSet,Handler handler,
                             CaseReportUtil caseReportUtil) {
        super(polygonPaint, context,handler);
        this.context = context;
        this.overlay = overlay;
        this.mapView = mapView;
        this.url = url;
        this.dataSet = dataSet;
        this.caseReportUtil = caseReportUtil;
    }

    @Override
    public void queryDataByPoint(Point2D touchPoint,Handler handler) {
        addLabelOverlay(overlay, mapView, touchPoint ,handler);
    }

    /**
     * 采集时标记事件
     */
    protected void addLabelOverlay(DefaultItemizedOverlay overlay,MapView map,Point2D point,Handler handler)
    {
        if(map.getOverlays().contains(overlay))
        {
            map.getOverlays().remove(overlay);
            if(overlay.size() > 0)
            {
                overlay.clear();
            }
        }
        caseReportUtil.setTouchPoint(point);
        OverlayItem layItem = new OverlayItem(point,"","");
        overlay.addItem(layItem);
        map.getOverlays().add(overlay);
        map.invalidate();
        TipForm tipForm = TipForm.newInstance();
        tipForm.showProgressDialog(context);
        new RunQueryByPoint(url,dataSet,handler).execute(point);
    }

}
