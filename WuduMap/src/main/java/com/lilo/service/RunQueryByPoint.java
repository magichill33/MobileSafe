package com.lilo.service;

import com.lilo.model.MessageEnum;
import com.lilo.util.DataUtil;
import com.supermap.android.data.GetFeaturesResult;
import com.supermap.android.maps.Point2D;
import com.supermap.services.components.commontypes.Geometry;
import com.supermap.services.components.commontypes.GeometryType;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

/**
 * 根据点来查询空间数据
 * @author ly
 *
 */
public class RunQueryByPoint extends RunQueryTask
{
    private String url;
    private String dataSet;
    //private Point2D point;

    public RunQueryByPoint(String url, String dataSet,Handler handler) {
        super(handler);
        this.url = url;
        this.dataSet = dataSet;
        //this.point = point;
    }

    @Override
    protected GetFeaturesResult doInBackground(Point2D... args) {
        Geometry geometry = new Geometry();
        com.supermap.services.components.commontypes.Point2D[] points = new com.supermap.services.components.commontypes.Point2D[] { new com.supermap.services.components.commontypes.Point2D(
                args[0].x, args[0].y) };
        geometry.points = points;
        geometry.type = GeometryType.POINT;
        return DataUtil.excute_geometryQuery(url, geometry, dataSet);
    }

}