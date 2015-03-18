package com.lilo.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.lilo.layer.TextOverlay;
import com.lilo.layer.TouchGridOverlay;
import com.lilo.model.MessageEnum;
import com.lilo.service.RunQueryDataTask;
import com.lilo.widget.TipForm;
import com.supermap.android.data.GetFeaturesResult;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.OverlayItem;
import com.supermap.android.maps.Point2D;
import com.supermap.android.maps.PolygonOverlay;
import com.supermap.services.components.commontypes.Feature;
import com.supermap.services.components.commontypes.Geometry;
import com.supermap.services.components.commontypes.Rectangle2D;

public class DrawRegionUtil {
    private MapView mapView;
    private Context context;
    private List<Geometry> touchGeoList = new ArrayList<Geometry>();
    private List<TextOverlay> textOverlays = new ArrayList<TextOverlay>(); //区域文字
    private List<PolygonOverlay> polygonOverlays = new ArrayList<PolygonOverlay>(); //区域图层
    private TouchGridOverlay touchGridOverlay;
    private Point2D centerPoint;

    public DrawRegionUtil(MapView mapView, Context context,TouchGridOverlay touchGridOverlay) {
        this.mapView = mapView;
        this.context = context;
        this.touchGridOverlay = touchGridOverlay;
    }

    /*
     * 根据网格编码绘制区域
     */
    public void drawRegionByCodes(String url,String dataSet,String gridCodes)
    {
        String params = DataUtil.getFormatConditon(gridCodes);
        Handler regionHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MessageEnum.QUERY_SUCCESS:
                        GetFeaturesResult queryResult = (GetFeaturesResult) msg.obj;
                        showQueryResult(queryResult);
                        break;
                    case MessageEnum.QUERY_FAILED:
                        // progressDialog.dismiss();
                        Toast.makeText(context,"获取区域信息失败", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
        //获取区域数据
        new RunQueryDataTask(url,dataSet, "WGBM in (" + params + ")", regionHandler).execute("*");
    }


    /**
     * 根据查询返回的结果集绘制区域
     * @param result
     */
    protected void showQueryResult(GetFeaturesResult result) {

        if (result == null || result.features == null || result.featureCount == 0) {
            Toast.makeText(context, "查询区域结果为空!", Toast.LENGTH_LONG).show();
            return;
        }
        List<List<Point2D>> pointsLists = new ArrayList<List<Point2D>>();
        Feature[] queryfeatures = result.features;

        Rectangle2D bounds = DataUtil.getGraphicsBound(result);
        setCenterPoint(bounds.getCenter()); //设置网格区域中心点
        DataUtil.zoomMap(bounds, mapView,mapView.getController());

        for(Feature f:queryfeatures)
        {
            String labelName = null;
            for(int i = 0;i<f.fieldNames.length;i++)
            {
                if(f.fieldNames[i].equals("WGMC"))
                {
                    labelName = f.fieldValues[i];
                    break;
                }
            }
            Geometry geometry = f.geometry;
            touchGeoList.add(geometry);
            List<Point2D> points = DataUtil.getPiontsFromGeometry(geometry);
            if (geometry.parts.length > 1) {
                int num = 0;
                for (int j = 0; j < geometry.parts.length; j++) {
                    int count = geometry.parts[j];
                    List<Point2D> partList = points.subList(num, num + count);
                    pointsLists.add(partList);
                    num = num + count;
                }
            } else {
                pointsLists.add(points);
            }

            /**
             * 标注文字
             */
            Point2D center = new Point2D(geometry.getCenter().x,geometry.getCenter().y);
            TextOverlay textOverlay = new TextOverlay(center, labelName);
            mapView.getOverlays().add(textOverlay);
            textOverlays.add(textOverlay);
        }

        ArrayList<Point2D> points = new ArrayList<Point2D>();
        // 把所有查询的几何对象都高亮显示
        for (int m = 0; m < pointsLists.size(); m++) {
            List<Point2D> geoPointList = pointsLists.get(m);
            PolygonOverlay polygonOverlay = new PolygonOverlay(new DrawUtil().getPolygonPaint(Color.BLUE,180,3));
            mapView.getOverlays().add(polygonOverlay);
            polygonOverlays.add(polygonOverlay);
            polygonOverlay.setData(geoPointList);
            polygonOverlay.setShowPoints(false);
        }

        if(touchGridOverlay!=null)
        {
            touchGridOverlay.initTouchLays(touchGeoList);
            mapView.getOverlays().add(touchGridOverlay);
            polygonOverlays.add(touchGridOverlay);
        }

        mapView.invalidate(); //刷新地图
    }

    /**
     * 清空绘制区域
     */
    public void clearRegionLayer()
    {
        if (polygonOverlays.size() != 0) {
            mapView.getOverlays().remove(polygonOverlays);
            polygonOverlays.clear();
        }
        if(textOverlays.size()!=0)
        {
            mapView.getOverlays().remove(textOverlays);
            textOverlays.clear();
        }
        mapView.invalidate();
    }

    public Point2D getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(com.supermap.services.components.commontypes.Point2D point2d) {
        this.centerPoint = new Point2D(point2d.x, point2d.y);
    }
}
