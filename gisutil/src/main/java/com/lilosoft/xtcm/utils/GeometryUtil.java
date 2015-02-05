package com.lilosoft.xtcm.utils;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.Unit;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.ags.query.QueryTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/2/4.
 */
public class GeometryUtil {
    private static GeometryUtil geometryUtil = null;
    SpatialReference spatialRef = SpatialReference.create(4326);
    private Polygon[] bufferEreas = null;
    private Map<Geometry,String> addrMap;

    private GeometryUtil(){
        addrMap = new HashMap<Geometry,String>();
    }

    public static GeometryUtil newInstance()
    {
        if(geometryUtil == null)
        {
            synchronized(GeometryUtil.class){
                if(geometryUtil==null){
                    geometryUtil = new GeometryUtil();
                }
            }
        }
        return geometryUtil;
    }

    /**
     * 查询符合条件的区域信息
     * @param codes 以逗号隔开
     * @param key
     * @param url
     * @return
     */
    protected List<Geometry> queryEreaByCode(String codes,String key,String url)
    {
        List<Geometry> geos = new ArrayList<Geometry>();
        Query query = new Query();
        String[] arr = codes.split(",");
        StringBuilder builder = new StringBuilder();
        builder.append(key+" in (");
        for (int i=0;i<arr.length;i++){
            builder.append("'");
            builder.append(arr[i]);
            builder.append("'");
            if (i<arr.length-1){
                builder.append(",");
            }
        }
        builder.append(")");
        String param = builder.toString();
        query.setWhere(param);
        query.setReturnGeometry(true);
        query.setOutFields(new String[]{"*"});
        QueryTask queryTask = new QueryTask(url);
        //Geometry geo = null;//
        try {
            FeatureSet fs = queryTask.execute(query);
            for (Graphic graphic:fs.getGraphics()){
                geos.add(graphic.getGeometry());
                addrMap.put(graphic.getGeometry(),graphic.getAttributeValue(key).toString());
            }
           // geo = fs.getGraphics()[0].getGeometry();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return geos;
    }

    /**
     * 创建多个缓冲区
     * @param codes
     * @param url
     * @param distance
     */
    public void createBufferErea(String codes,String key,String url,double distance)
    {
        List<Geometry> geos = queryEreaByCode(codes,key,url);

        Unit unit = spatialRef.getUnit();

        double adjustedAccuracy = distance;

        if (unit.getUnitType() == Unit.UnitType.ANGULAR) {
            adjustedAccuracy = metersToDegrees(distance);
        } else {
            unit = Unit.create(LinearUnit.Code.METER);
        }

        double[] distances = new double[geos.size()];
        for (int i=0;i<geos.size();i++){
            distances[i] = adjustedAccuracy;
        }


        bufferEreas = GeometryEngine.buffer((Geometry[]) geos.toArray(),spatialRef,distances,unit,false);
        //bufferErea = GeometryEngine.buffer(geo, spatialRef, adjustedAccuracy, unit);
    }

    private final double metersToDegrees(double distanceInMeters) {
        return distanceInMeters / 111319.5;
    }

    /**
     *判断一个点是否在缓冲区内
     * @param mp
     * @return
     */
    public boolean isInErea(Point mp,StringBuffer address)
    {
        boolean isIn = false;
        for (Polygon erea:bufferEreas)
        {
           isIn = GeometryEngine.contains(erea, mp, SpatialReference.create(4326));

           if (isIn)
           {
              address.append(addrMap.get(erea));
              break;
           }

        }
        return isIn;
    }
}
