package com.lilo.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.lilo.model.MessageEnum;
import com.lilo.service.RunQueryDataTask;
import com.lilo.sm.R;
import com.supermap.android.data.GetFeaturesResult;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.Point2D;
import com.supermap.android.maps.PolygonOverlay;
import com.supermap.services.components.commontypes.Feature;
import com.supermap.services.components.commontypes.Geometry;
import com.supermap.services.components.commontypes.Rectangle2D;


public class DrawHouseUtil {
    private MapView mapView;
    private Context context;
    private List<PolygonOverlay> houseOverlays = new ArrayList<PolygonOverlay>(); //房屋图层集

    public DrawHouseUtil(MapView mapView, Context context) {
        super();
        this.mapView = mapView;
        this.context = context;
    }

    /**
     * 根据网格编码来查询房屋数据
     * @param url
     * @param dataSet
     * @param gridCodes
     */
    public void drawHouseByGridCodes(String url,String dataSet,String gridCodes)
    {
        String params = DataUtil.getFormatConditon(gridCodes);
        Handler houseHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MessageEnum.QUERY_SUCCESS:
                        GetFeaturesResult houseResult = (GetFeaturesResult) msg.obj;
                        drawHouseOnMap(houseResult);
                        break;
                    case MessageEnum.QUERY_FAILED:
                        Toast.makeText(context,"获取房屋信息失败", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
        //获取房屋数据
        new RunQueryDataTask(url,dataSet,
                "WGBM in (" + params + ")", houseHandler).execute("*");
    }

    /**
     * 根据据房屋编码来绘制已采集的房子
     * @param url
     * @param dataSet
     * @param houseCodes
     */
    public void drawHouseByHouseCodes(String url,String dataSet,String houseCodes)
    {
        String params = DataUtil.getFormatConditon(houseCodes);
        Handler gatherHouseHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MessageEnum.QUERY_SUCCESS:
                        GetFeaturesResult houseResult = (GetFeaturesResult) msg.obj;
                        drawGatherHouseOnMap(houseResult);
                        break;
                    case MessageEnum.QUERY_FAILED:
                        Toast.makeText(context,"获取房屋信息失败", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
        //获取采集的房屋数据
        new RunQueryDataTask(url,dataSet,
                "FWDBM in (" + params + ")", gatherHouseHandler).execute("*");
    }

    /**
     * 根据房屋编码绘制房屋数据
     * @param url
     * @param dataSet
     * @param code
     */
    public void drawHouseByCode(String url,String dataSet,String code)
    {
        Handler houseHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MessageEnum.QUERY_SUCCESS:
                        GetFeaturesResult houseResult = (GetFeaturesResult) msg.obj;
                        locateHouseByCode(houseResult,0xff0000);
                        break;
                    case MessageEnum.QUERY_FAILED:
                        Toast.makeText(context,"获取房屋信息失败", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
        //获取采集的房屋数据
        new RunQueryDataTask(url,dataSet,
                "FWDBM = '" + code + "'", houseHandler).execute("*");
    }

    protected void locateHouseByCode(GetFeaturesResult houseResult,int color)
    {
        if (houseResult == null || houseResult.features == null) {
            Toast.makeText(context, "查询结果为空!", Toast.LENGTH_LONG).show();
            return;
        }

        List<List<Point2D>> pointsLists = new ArrayList<List<Point2D>>();
        Feature feature = houseResult.features[0];

        Rectangle2D bounds = DataUtil.getGraphicsBound(houseResult);
        DataUtil.zoomMap(bounds, mapView,mapView.getController());

        Geometry geometry = feature.geometry;
        List<Point2D> points = DataUtil.getPiontsFromGeometry(geometry);
        if (geometry.parts.length > 1)
        {
            int num = 0;
            for (int j = 0; j < geometry.parts.length; j++)
            {
                int count = geometry.parts[j];
                List<Point2D> partList = points.subList(num, num + count);
                pointsLists.add(partList);
                num = num + count;
            }
        }
        else
        {
            pointsLists.add(points);
        }

        // 把所有查询的几何对象都高亮显示
        for (int m = 0; m < pointsLists.size(); m++) {
            List<Point2D> geoPointList = pointsLists.get(m);
            PolygonOverlay polygonOverlay = new PolygonOverlay(new DrawUtil().getPolygonPaint(color,150,1,Paint.Style.FILL));
            mapView.getOverlays().add(polygonOverlay);
            houseOverlays.add(polygonOverlay);
            polygonOverlay.setData(geoPointList);
            polygonOverlay.setShowPoints(false);

        }

        this.mapView.invalidate(); //刷新地图
    }

    protected void locateHouseByCode(GetFeaturesResult houseResult) {
        locateHouseByCode(houseResult, 0x2264ff);
    }

    /**
     * 绘制查询房屋数据
     * @param houseResult
     */
    protected void drawHouseOnMap(GetFeaturesResult houseResult)
    {
        if (houseResult == null || houseResult.features == null) {
            Toast.makeText(context, "查询结果为空!", Toast.LENGTH_LONG).show();
            return;
        }

        List<List<Point2D>> pointsLists = new ArrayList<List<Point2D>>();
        Feature[] queryfeatures = houseResult.features;

        for(Feature f:queryfeatures)
        {
            Geometry geometry = f.geometry;
            List<Point2D> points = DataUtil.getPiontsFromGeometry(geometry);
            if (geometry.parts.length > 1)
            {
                int num = 0;
                for (int j = 0; j < geometry.parts.length; j++)
                {
                    int count = geometry.parts[j];
                    List<Point2D> partList = points.subList(num, num + count);
                    pointsLists.add(partList);
                    num = num + count;
                }
            }
            else
            {
                pointsLists.add(points);
            }
        }

        // 把所有查询的几何对象都高亮显示
        for (int m = 0; m < pointsLists.size(); m++) {
            List<Point2D> geoPointList = pointsLists.get(m);
            PolygonOverlay polygonOverlay = new PolygonOverlay(new DrawUtil().getPolygonPaint(0x9696fe,50,1,Paint.Style.FILL));
            //PolygonOverlay polygonOverlay = new TouchPolygonOverlays(getPolygonPaint(0x9696fe,50,1,Paint.Style.FILL));
            mapView.getOverlays().add(polygonOverlay);
            houseOverlays.add(polygonOverlay);
            polygonOverlay.setData(geoPointList);
            polygonOverlay.setShowPoints(false);

        }


        this.mapView.invalidate(); //刷新地图
    }

    /**
     * 绘制已经采集的房屋数据
     * @param houseResult
     */
    protected void drawGatherHouseOnMap(GetFeaturesResult houseResult) {
        if (houseResult == null || houseResult.features == null) {
            Toast.makeText(context, "查询结果为空!", Toast.LENGTH_LONG).show();
            return;
        }

        List<List<Point2D>> pointsLists = new ArrayList<List<Point2D>>();
        Feature[] queryfeatures = houseResult.features;

        for(Feature f:queryfeatures)
        {
            Geometry geometry = f.geometry;
            List<Point2D> points = DataUtil.getPiontsFromGeometry(geometry);
            if (geometry.parts.length > 1)
            {
                int num = 0;
                for (int j = 0; j < geometry.parts.length; j++)
                {
                    int count = geometry.parts[j];
                    List<Point2D> partList = points.subList(num, num + count);
                    pointsLists.add(partList);
                    num = num + count;
                }
            }
            else
            {
                pointsLists.add(points);
            }
        }

        // 把所有查询的几何对象都高亮显示
        for (int m = 0; m < pointsLists.size(); m++) {
            List<Point2D> geoPointList = pointsLists.get(m);
            PolygonOverlay polygonOverlay = new PolygonOverlay(new DrawUtil().getPolygonPaint(0x2264ff,150,1,Paint.Style.FILL));
            //PolygonOverlay polygonOverlay = new TouchPolygonOverlays(getPolygonPaint(0x9696fe,50,1,Paint.Style.FILL));
            mapView.getOverlays().add(polygonOverlay);
            houseOverlays.add(polygonOverlay);
            polygonOverlay.setData(geoPointList);
            polygonOverlay.setShowPoints(false);

        }


        this.mapView.invalidate(); //刷新地图

    }

    /**
     * 清空绘制区域
     */
    public void clearHouselayer()
    {
        if(houseOverlays.size()!=0)
        {
            mapView.getOverlays().remove(houseOverlays);
            houseOverlays.clear();
        }
        mapView.invalidate();
    }
}
