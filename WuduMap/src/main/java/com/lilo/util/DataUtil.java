package com.lilo.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.Set;

import android.util.Log;

import com.supermap.android.commons.EventStatus;
import com.supermap.android.data.EditFeaturesParameters;
import com.supermap.android.data.EditFeaturesResult;
import com.supermap.android.data.EditFeaturesService;
import com.supermap.android.data.EditFeaturesService.EditFeaturesEventListener;
import com.supermap.android.data.EditType;
import com.supermap.android.data.GetFeaturesByBufferParameters;
import com.supermap.android.data.GetFeaturesByBufferService;
import com.supermap.android.data.GetFeaturesByGeometryParameters;
import com.supermap.android.data.GetFeaturesByGeometryService;
import com.supermap.android.data.GetFeaturesBySQLParameters;
import com.supermap.android.data.GetFeaturesBySQLService;
import com.supermap.android.data.GetFeaturesByGeometryService.GetFeaturesEventListener;
import com.supermap.android.data.GetFeaturesByIDsParameters;
import com.supermap.android.data.GetFeaturesByIDsService;
import com.supermap.android.data.GetFeaturesResult;
import com.supermap.android.maps.BoundingBox;
import com.supermap.android.maps.MapController;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.Point2D;
import com.supermap.services.components.commontypes.Feature;
import com.supermap.services.components.commontypes.Geometry;
import com.supermap.services.components.commontypes.GeometryType;
import com.supermap.services.components.commontypes.QueryParameter;
import com.supermap.services.components.commontypes.Rectangle2D;
import com.supermap.services.components.commontypes.SpatialQueryMode;
import com.supermap.services.rest.util.JsonConverter;

/**
 * <p>
 * 实现数据编辑和数据查询功能的工具类
 * </p>
 * @author ${Author}
 * @version ${Version}
 *
 */
public class DataUtil {
    /**
     * <p>
     * 执行IDS查询
     * </p>
     * @param url 数据查询服务地址
     * @return
     */
    public static GetFeaturesResult excute_idsQuery(String queryurl) {
        Log.d("Test info ", "Start doIDQuery");
        Log.d("Test info1 ", "queryurl=" + queryurl);
        // 构造查询参数
        int[] ids = { 110, 239 };
        GetFeaturesByIDsParameters parameters = new GetFeaturesByIDsParameters();
        String[] dtnames = { "World:Countries" };
        parameters.datasetNames = dtnames;
        parameters.IDs = ids;
        parameters.toIndex = 500;
        GetFeaturesByIDsService service = new GetFeaturesByIDsService(queryurl);
        MyGetFeaturesEventListener listener = new MyGetFeaturesEventListener();
        service.process(parameters, listener);
        try {
            listener.waitUntilProcessed();
        } catch (Exception e) {
            e.printStackTrace();
        }
        GetFeaturesResult result = listener.getReult();
        Log.d("excute_idsQuery GetFeaturesResult", JsonConverter.toJson(result));
        return result;
    }

    /**
     * <p>
     * 执行几何查询
     * </p>
     * @param url
     * @param point
     * @return
     */
    public static GetFeaturesResult excute_geometryQuery(String url, Geometry geometry ,String dataSet) {
        Log.d("Test info ", "Start doGeometryQuery");
        Log.d("Test info1 ", "queryurl=" + url);
        // 定义几何查询参数
        GetFeaturesByGeometryParameters params = new GetFeaturesByGeometryParameters();
        String[] datasetNames = new String[] {dataSet};
        params.datasetNames = datasetNames;
        params.geometry = geometry;
        params.spatialQueryMode = SpatialQueryMode.INTERSECT;
        Log.d("GeometryParameters", JsonConverter.toJson(params));

        // 与服务器交互
        GetFeaturesByGeometryService service = new GetFeaturesByGeometryService(url);
        MyGetFeaturesEventListener listener = new MyGetFeaturesEventListener();
        service.process(params, listener);
        try {
            listener.waitUntilProcessed();
        } catch (Exception e) {
            e.printStackTrace();
        }
        GetFeaturesResult result = listener.getReult();
        Log.d("GetGeometryQueryResult", JsonConverter.toJson(result));
        return result;

    }

    /**
     * <p>
     * 执行缓冲区查询
     * </p>
     * @param url
     * @param geometry
     * @return
     */
    public static GetFeaturesResult excute_bufferQuery(String url,String dataset, Geometry geometry,double distance) {
        Log.d("Test info ", "Start doBufferQuery");
        Log.d("Test info1 ", "queryurl=" + url);
        // 定义几何查询参数
        GetFeaturesByBufferParameters params = new GetFeaturesByBufferParameters();
        String[] datasetNames = new String[] { dataset };
        params.datasetNames = datasetNames;
        params.geometry = geometry;
        params.bufferDistance = distance;
        Log.d("GeometryParameters", JsonConverter.toJson(params));

        // 与服务器交互
        GetFeaturesByBufferService service = new GetFeaturesByBufferService(url);
        MyGetFeaturesEventListener listener = new MyGetFeaturesEventListener();
        service.process(params, listener);
        try {
            listener.waitUntilProcessed();
        } catch (Exception e) {
            e.printStackTrace();
        }
        GetFeaturesResult result = listener.getReult();
        Log.d("GetBufferQueryResult", JsonConverter.toJson(result));
        return result;

    }


    /**
     * <p>
     * 执行SQL查询
     * </p>
     * @param url data服务地址
     * @return
     */
    public static GetFeaturesResult excute_geoSQL(String url,String dataSet,String condition) {
        // 定义SQL查询参数
        GetFeaturesBySQLParameters params = new GetFeaturesBySQLParameters();
        String[] datasetNames = new String[] { dataSet };
        params.datasetNames = datasetNames;
        QueryParameter queryParameter = new QueryParameter();
        queryParameter.attributeFilter = condition;
        params.queryParameter = queryParameter;

        // 与服务器交互
        GetFeaturesBySQLService geoSQLService = new GetFeaturesBySQLService(url);
        MyGetFeaturesEventListener listener = new MyGetFeaturesEventListener();
        geoSQLService.process(params, listener);
        try {
            listener.waitUntilProcessed();
        } catch (Exception e) {
            e.printStackTrace();
        }
        GetFeaturesResult result = listener.getReult();
        Log.d("SQLQueryResult", JsonConverter.toJson(result));
        return result;

    }

    /**
     * <p>
     * 执行添加地物
     * </p>
     * @param url 数据编辑服务地址
     * @param feature 要增加或修改的地物要素集合
     * @return
     */
    public static EditFeaturesResult excute_geoAdd(String url, Feature[] feature) {
        // 定义添加地物参数
        EditFeaturesParameters addFeatureParam = new EditFeaturesParameters();
        addFeatureParam.editType = EditType.ADD;
        addFeatureParam.features = feature;
        Log.d("addFeatureParam", JsonConverter.toJson(addFeatureParam));
        // 与服务器交互
        EditFeaturesService editFeaturesService = new EditFeaturesService(url);
        MyEditFeaturesEventListener listener = new MyEditFeaturesEventListener();
        editFeaturesService.process(addFeatureParam, listener);
        try {
            listener.waitUntilProcessed();
        } catch (Exception e) {
            e.printStackTrace();
        }
        EditFeaturesResult addResult = listener.getReult();
        Log.d("addResult", JsonConverter.toJson(addResult));
        return addResult;

    }

    /**
     * <p>
     * 执行选择地物
     * </p>
     * @param url data服务地址
     * @param point 输入的点对象，用来查询地物
     * @return
     */
    public static GetFeaturesResult excute_geoSel(String url, Point2D point) {
        // 定义几何查询参数
        GetFeaturesByGeometryParameters params = new GetFeaturesByGeometryParameters();
        String[] datasetNames = new String[] { "Jingjin:Landuse_R" };
        params.datasetNames = datasetNames;
        Geometry geometry = new Geometry();
        com.supermap.services.components.commontypes.Point2D[] points = new com.supermap.services.components.commontypes.Point2D[] { new com.supermap.services.components.commontypes.Point2D(
                point.x, point.y) };
        geometry.points = points;
        geometry.type = GeometryType.POINT;
        params.geometry = geometry;
        params.spatialQueryMode = SpatialQueryMode.INTERSECT;
        // Log.d("GeometryParameters", JsonConverter.toJson(params));

        // 与服务器交互
        GetFeaturesByGeometryService geoSelService = new GetFeaturesByGeometryService(url);
        MyGetFeaturesEventListener listener = new MyGetFeaturesEventListener();
        geoSelService.process(params, listener);
        try {
            listener.waitUntilProcessed();
        } catch (Exception e) {
            e.printStackTrace();
        }
        GetFeaturesResult result = listener.getReult();
        Log.d("GetFeaturesResult", JsonConverter.toJson(result));
        return result;

    }

    /**
     * <p>
     * 执行编辑地物
     * </p>
     * @param url 数据编辑服务地址
     * @param feature 要增加或修改的地物要素集合
     * @param ids 要删除或修改的矢量要素 ID 号集合
     * @return
     */
    public static EditFeaturesResult excute_geoEdit(String url, Feature[] feature, int[] ids) {
        // 定义添加地物参数
        EditFeaturesParameters editFeatureParam = new EditFeaturesParameters();
        editFeatureParam.editType = EditType.UPDATE;
        editFeatureParam.features = feature;
        editFeatureParam.IDs = ids;
        // 与服务器交互
        EditFeaturesService editFeaturesService = new EditFeaturesService(url);
        MyEditFeaturesEventListener listener = new MyEditFeaturesEventListener();
        editFeaturesService.process(editFeatureParam, listener);
        try {
            listener.waitUntilProcessed();
        } catch (Exception e) {
            e.printStackTrace();
        }
        EditFeaturesResult editResult = listener.getReult();
        return editResult;

    }

    /**
     * <p>
     * 执行删除地物
     * </p>
     * @param url 数据编辑服务地址
     * @param ids 要删除或修改的矢量要素 ID 号集合
     * @return
     */
    public static EditFeaturesResult excute_geoDel(String url, int[] ids) {
        // 定义删除地物参数
        EditFeaturesParameters delFeatureParam = new EditFeaturesParameters();
        delFeatureParam.editType = EditType.DELETE;
        delFeatureParam.IDs = ids;
        Log.d("EditFeaturesParameters", JsonConverter.toJson(delFeatureParam));
        // 与服务器交互
        EditFeaturesService editFeaturesService = new EditFeaturesService(url);
        MyEditFeaturesEventListener listener = new MyEditFeaturesEventListener();
        editFeaturesService.process(delFeatureParam, listener);
        try {
            listener.waitUntilProcessed();
        } catch (Exception e) {
            e.printStackTrace();
        }
        EditFeaturesResult delResult = listener.getReult();
        Log.d("delResult", JsonConverter.toJson(delResult));
        return delResult;

    }

    /**
     * <p>
     * 提取构成地物的点集合
     * </p>
     * @param geometry
     * @return
     */
    public static List<Point2D> getPiontsFromGeometry(Geometry geometry) {
        List<Point2D> geoPoints = new ArrayList<Point2D>();
        com.supermap.services.components.commontypes.Point2D[] points = geometry.points;
        for (com.supermap.services.components.commontypes.Point2D point : points) {
            Point2D geoPoint = new Point2D(point.x, point.y);
            geoPoints.add(geoPoint);
        }
        return geoPoints;
    }

    /**
     * <p>
     * 判断由data构成的多边形是否包含点gp
     * </p>
     * @param gp
     * @param data
     * @return
     */
    public static boolean contians(Point2D gp, List<Point2D> data) {
        int j = 0;
        int N = data.size() - 1;
        boolean oddNodes = false;
        double x = gp.getX();
        double y = gp.getY();
        for (int i = 0; i < N; i++) {
            j++;
            if (j == N) {
                j = 0;
            }
            if (((((Point2D) data.get(i)).getY() >= y) || (((Point2D) data.get(j)).getY() < y))
                    && ((((Point2D) data.get(j)).getY() >= y) || (((Point2D) data.get(i)).getY() < y)))
                continue;
            if (((Point2D) data.get(i)).getX() + (y - ((Point2D) data.get(i)).getY()) / (((Point2D) data.get(j)).getY() - ((Point2D) data.get(i)).getY())
                    * (((Point2D) data.get(j)).getX() - ((Point2D) data.get(i)).getX()) >= x) {
                continue;
            }
            oddNodes = !oddNodes;
        }
        return oddNodes;
    }

    public static double getArea(List<Point2D> data) {
        double area = 0.0;
        for (int i = 0; i < data.size(); i++) {
            if (i < data.size() - 1) {
                Point2D p1 = data.get(i);
                Point2D p2 = data.get(i + 1);
                area += p1.x * p2.y - p2.x * p1.y;
            } else {
                area += data.get(i).x * data.get(0).y - data.get(0).x * data.get(i).y;
            }

        }
        area = Math.abs(area) / 2.0;
        return area;
    }

    /**
     * <p>
     * 修改组成地物的点集合
     * </p>
     * @param feature
     * @param featureMap
     */
    public static void resetGeometry(Feature feature, Map<String, List<Point2D>> featureMap) {
        if (feature != null && featureMap != null && feature.geometry != null && feature.geometry.parts.length == featureMap.size()) {
            if (featureMap.size() == 1) {
                List<Point2D> gps = featureMap.get(String.valueOf(feature.getID()));
                com.supermap.services.components.commontypes.Point2D[] points = new com.supermap.services.components.commontypes.Point2D[gps.size()];
                for (int i = 0; i < gps.size(); i++) {
                    Point2D point2D = gps.get(i);
                    com.supermap.services.components.commontypes.Point2D geoPoint = new com.supermap.services.components.commontypes.Point2D(point2D.x,
                            point2D.y);
                    points[i] = geoPoint;
                }
                feature.geometry.points = points;
                feature.geometry.parts[0] = points.length;
            } else {
                List<com.supermap.services.components.commontypes.Point2D> pointList = new ArrayList<com.supermap.services.components.commontypes.Point2D>();
                for (int i = 0; i < featureMap.size(); i++) {
                    List<Point2D> gps = featureMap.get(String.valueOf(feature.getID()) + i);
                    for (int j = 0; j < gps.size(); j++) {
                        Point2D point2D = gps.get(j);
                        com.supermap.services.components.commontypes.Point2D geoPoint = new com.supermap.services.components.commontypes.Point2D(point2D.x,
                                point2D.y);
                        pointList.add(geoPoint);
                    }
                    feature.geometry.parts[i] = gps.size();
                }
                com.supermap.services.components.commontypes.Point2D[] ps = new com.supermap.services.components.commontypes.Point2D[pointList.size()];
                feature.geometry.points = pointList.toArray(ps);
            }
        }
    }

    public static List<Point2D> copyList(List<Point2D> points) {
        List<Point2D> list = new ArrayList<Point2D>();
        Iterator<Point2D> it = points.iterator();
        while (it.hasNext()) {
            Point2D p = it.next();
            list.add(new Point2D(p.x, p.y));
        }
        return list;
    }

    public static Rectangle2D getGraphicsBound(GetFeaturesResult result)
    {
        double left = Double.MAX_VALUE;
        double bottom = Double.MAX_VALUE;
        double right = Double.MIN_VALUE;
        double top = Double.MIN_VALUE;
        //Rectangle2D rect = new Rectangle2D();
        for(Feature f:result.features)
        {
            Rectangle2D bound = f.geometry.getBounds();

            if(left > bound.getLeft())
                left = bound.getLeft();
            if(bottom > bound.getBottom())
                bottom = bound.getBottom();
            if(right < bound.getRight())
                right = bound.getRight();
            if(top < bound.getTop())
                top = bound.getTop();
        }

        return new Rectangle2D(left, bottom, right, top);
    }

    public static Rectangle2D getGraphicsBound(Point2D[] points)
    {
        double minX = points[0].x;
        double minY = points[0].y;
        double maxX = points[0].x;
        double maxY = points[0].y;
        //Rectangle2D rect = new Rectangle2D();
        for(Point2D point2d:points)
        {
            double lon = point2d.getX();
            double lat = point2d.getY();
            if(minX > lon)
            {
                minX = lon;
            }
            if(maxX < lon)
            {
                maxX = lon;
            }
            if(minY > lat)
            {
                minY = lat;
            }
            if(maxY < lat)
            {
                maxY = lat;
            }
        }

        return new Rectangle2D(minX,minY,maxX,maxY);
    }

    public static void zoomMap(final Rectangle2D bounds,final MapView mapView,final MapController mapController)
    {
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                BoundingBox bound1 = mapView.getBounds();
                Log.i("ly", "----" + bound1);
                if(bound1!=null)
                {
                    com.supermap.services.components.commontypes.Point2D cp = bounds.center();
                    double dividend = bound1.leftTop.x - bound1.rightBottom.x;
                    double divisor = bounds.leftBottom.x - bounds.rightTop.x;
                    double fac = dividend/divisor;
                    int currlevel = mapView.getZoomLevel();
                    //double zoomLevel = fac*currlevel;
                    double l = Math.log(fac)/Math.log(2);
                    int zoomLevel = (int) (Math.floor(l) + currlevel);
                    Log.i("ly", "Level:" + zoomLevel);
                    //mapController.setZoom();
                    mapController.setCenter(new Point2D(cp.x,cp.y));
                    mapController.setZoom(zoomLevel);
                }
            }
        }, 1500);
    }

    public static void zoomMap(final Rectangle2D bounds,final MapView mapView,final Point2D cp)
    {
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                BoundingBox bound1 = mapView.getBounds();
                Log.i("ly", "----" + bound1);
                if(bound1!=null)
                {
                    double dividend = bound1.leftTop.x - bound1.rightBottom.x;
                    double divisor = bounds.leftBottom.x - bounds.rightTop.x;
                    double fac = dividend/divisor;
                    int currlevel = mapView.getZoomLevel();
                    //double zoomLevel = fac*currlevel;
                    double l = Math.log(fac)/Math.log(2);
                    int zoomLevel = (int) (Math.floor(l) + currlevel);
                    Log.i("ly", "Level:" + zoomLevel);
                    //mapController.setZoom();
                    mapView.getController().setZoom(zoomLevel);
                    mapView.getController().setCenter(cp);
                }
            }

        },1500);


    }

    /**
     *
     * @param code 以逗号分割的字符串
     * @return
     */
    public static String getFormatConditon(String _codes)
    {
        String[] codes = _codes.split(",");
        StringBuffer paramsBuffer = new StringBuffer();
        for(String code:codes)
        {
            paramsBuffer.append("'");
            paramsBuffer.append(code);
            paramsBuffer.append("',");
        }
        String params = paramsBuffer.substring(0, paramsBuffer.lastIndexOf(","));
        return params;
    }

    /**
     * <p>
     * 实现查询结果的监听器，自己实现处理结果接口
     * </p>
     * @author ${Author}
     * @version ${Version}
     *
     */
    static class MyGetFeaturesEventListener extends GetFeaturesEventListener {
        private GetFeaturesResult lastResult;

        public MyGetFeaturesEventListener() {
            super();
        }

        public GetFeaturesResult getReult() {
            return lastResult;
        }

        @Override
        public void onGetFeaturesStatusChanged(Object sourceObject, EventStatus status) {
            if (sourceObject instanceof GetFeaturesResult) {
                lastResult = (GetFeaturesResult) sourceObject;
            }
        }

    }

    /**
     * <p>
     * 实现数据编辑结果的监听器，自己实现处理结果接口
     * </p>
     * @author ${Author}
     * @version ${Version}
     *
     */
    static class MyEditFeaturesEventListener extends EditFeaturesEventListener {
        private EditFeaturesResult lastResult;

        public MyEditFeaturesEventListener() {
            super();
        }

        public EditFeaturesResult getReult() {
            return lastResult;
        }

        @Override
        public void onEditFeaturesStatusChanged(Object sourceObject, EventStatus status) {
            if (sourceObject instanceof EditFeaturesResult) {
                lastResult = (EditFeaturesResult) sourceObject;
            }
        }

    }
}
