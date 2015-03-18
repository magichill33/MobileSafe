package com.lilo.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.lilo.layer.CaseReportOverlay;
import com.lilo.layer.TouchGridOverlay;
import com.lilo.model.MessageEnum;
import com.lilo.sm.LiloMapActivity;
import com.lilo.sm.R;
import com.lilo.widget.TipForm;
import com.supermap.android.data.GetFeaturesResult;
import com.supermap.android.maps.DefaultItemizedOverlay;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.OverlayItem;
import com.supermap.android.maps.Point2D;
import com.supermap.services.components.commontypes.Feature;

public class CaseReportUtil {

    private MapView mapView;
    private Context context;
    private DefaultItemizedOverlay labelOverlay;
    private Point2D touchPoint;
    private DrawRegionUtil drawRegionUtil;

    public CaseReportUtil(MapView mapView, Context context,DefaultItemizedOverlay labelOverlay) {
        super();
        this.mapView = mapView;
        this.context = context;
        this.labelOverlay = labelOverlay;
    }

    public void initCaseReport(String url,String dataSet,String gridCodes)
    {
        /**
         * 点击网格handler
         */
        Handler touchGridHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MessageEnum.QUERY_SUCCESS:
                        TipForm.newInstance().dismissDialog();
                        GetFeaturesResult gridResult = (GetFeaturesResult) msg.obj;
                        postGridInfo(gridResult,getTouchPoint());
                        break;
                    case MessageEnum.QUERY_FAILED:
                        TipForm.newInstance().dismissDialog();
                        Toast.makeText(context,"获取网格信息失败", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        TipForm.newInstance().dismissDialog();
                        break;
                }
            }
        };

        CaseReportOverlay caseReportOverlay = new CaseReportOverlay(
                new DrawUtil().getPolygonPaint(Color.WHITE,0,0), context, mapView, labelOverlay,
                url,dataSet,touchGridHandler,this);
        DrawRegionUtil drawRegionUtil = new DrawRegionUtil(mapView, context, caseReportOverlay);
        setDrawRegionUtil(drawRegionUtil);
        drawRegionUtil.clearRegionLayer();
        drawRegionUtil.drawRegionByCodes(url, dataSet, gridCodes);
    }

    /**
     * 根据经纬度，网格编码回显案件
     * @param point2d
     * @param gridCode
     */
    public void locateCaseByCoord(String url,String dataSet,Point2D point,String gridCode)
    {
        DrawRegionUtil drawRegionUtil = new DrawRegionUtil(mapView, context, null);
        drawRegionUtil.clearRegionLayer();
        drawRegionUtil.drawRegionByCodes(url, dataSet, gridCode);

        if(mapView.getOverlays().contains(labelOverlay))
        {
            mapView.getOverlays().remove(labelOverlay);
            if(labelOverlay.size() > 0)
            {
                labelOverlay.clear();
            }
        }
        OverlayItem layItem = new OverlayItem(point,"","");
        labelOverlay.addItem(layItem);
        mapView.getOverlays().add(labelOverlay);
        mapView.invalidate();
    }

    public Point2D getTouchPoint() {
        return touchPoint;
    }

    public void setTouchPoint(Point2D touchPoint) {
        this.touchPoint = touchPoint;
    }

    public DrawRegionUtil getDrawRegionUtil() {
        return drawRegionUtil;
    }

    public void setDrawRegionUtil(DrawRegionUtil drawRegionUtil) {
        this.drawRegionUtil = drawRegionUtil;
    }

    /**
     * 提交采集的数据
     */
    protected void postGridInfo(GetFeaturesResult gridResult,Point2D touchPoint)
    {
        if (gridResult == null || gridResult.features == null || gridResult.featureCount == 0) {
            TipForm.showToast("查询结果为空!", context);
            return;
        }

        Feature feature = gridResult.features[0];
        String gridCode = "";
        for(int i = 0;i<feature.fieldNames.length;i++)
        {
            if(feature.fieldNames[i].equals(context.getResources().getString(R.string.gridCode)))
            {
                gridCode = feature.fieldValues[i];
                break;
            }
        }

        TipForm.showToast("网格编码：" + gridCode + "坐标：" + touchPoint.x + "," + touchPoint.y,context);
    }

}
