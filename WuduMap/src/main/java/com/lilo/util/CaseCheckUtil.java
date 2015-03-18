package com.lilo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lilo.model.CaseModel;
import com.lilo.model.MessageEnum;
import com.lilo.service.RunQueryDataTask;
import com.lilo.sm.R;
import com.lilo.widget.CaseListDialog;
import com.lilo.widget.TipForm;
import com.supermap.android.data.GetFeaturesResult;
import com.supermap.android.maps.DefaultItemizedOverlay;
import com.supermap.android.maps.LineOverlay;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.OverlayItem;
import com.supermap.android.maps.Point2D;
import com.supermap.android.maps.PointOverlay;
import com.supermap.android.maps.MapView.MapViewEventListener;
import com.supermap.services.components.commontypes.Rectangle2D;

public class CaseCheckUtil {

    private MapView mapView;
    private Context context;
    private LocateUtil locateUtil;
    private List<PointOverlay> pointOverlays = new ArrayList<PointOverlay>(); //案件点图层集合
    private CaseListDialog caseListDialog; //案件列表窗体
    private List<LineOverlay> pathOverlays = null; //路径图层
    private String url;
    private List<Point2D> geoPoints = null; //路径点集合
    private PointOverlay caseOverlay = null;
    private Boolean isTest;

    public CaseCheckUtil(MapView mapView, Context context ,boolean isTest) {
        super();
        this.mapView = mapView;
        this.context = context;
        this.isTest = isTest;
        locateUtil = new LocateUtil(context.getApplicationContext());
        locateUtil.initLocationInfo();

    }

    /**
     * 路径分析前，进行相关初始化操作
     * @param url
     */
    protected void initAnalyzeParam(String url)
    {
        pathOverlays = new ArrayList<LineOverlay>(); //路径图层
        this.url = url;
        geoPoints = new ArrayList<Point2D>();
    }

    /**
     * 案件核实及最优路径方法
     * @param url
     * @param locOverlay
     * @param caseModelList
     * @param gpsButton
     * @param gpsOverlay
     */
    public void analyzeCasePath(String url,DefaultItemizedOverlay locOverlay,
                                List<CaseModel> caseModelList,ImageButton gpsButton,final DefaultItemizedOverlay gpsOverlay)
    {
        initAnalyzeParam(url);
        caseListDialog = new CaseListDialog(context,this,caseModelList,R.style.dialogTheme);
        caseListDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        mapView.addMapViewEventListener(new MapViewEventListener() {

            @Override
            public void zoomStart(MapView arg0) {
            }

            @Override
            public void zoomEnd(MapView arg0) {
            }

            @Override
            public void touch(MapView arg0) {
                caseListDialog.show();
                Log.i("ly", "touch_up");
            }

            @Override
            public void moveStart(MapView arg0) {
            }

            @Override
            public void moveEnd(MapView arg0) {
            }

            @Override
            public void move(MapView arg0) {
            }

            @Override
            public void mapLoaded(MapView arg0) {
            }

            @Override
            public void longTouch(MapView arg0) {
            }
        });

        gpsButton.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {

                    v.setBackgroundColor(0xcc818d89);
                    Location location = locateUtil.getLocation();
                    Point2D cp = new Point2D(location.getLongitude(), location.getLatitude());
                    float bearing = location.getBearing();
                    gpsOverlay.clear();
                    OverlayItem overlayItem = new OverlayItem(cp, "", "");
                    gpsOverlay.addItem(overlayItem);
                    mapView.getController().setCenter(cp);
                    mapView.invalidate();

                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    //再修改为抬起时的正常图片
                    v.setBackgroundColor(0xccffffff);
                }

                return false;
            }
        });

        Point2D locPoint = null;
        if(isTest)
        {
            locPoint = new Point2D(104.944951,33.374131); //测试数据
        }else{
            locPoint = locateUtil.locateMap(mapView);
        }
        geoPoints.clear();
        geoPoints.add(locPoint); //将当前点添加到路径分析点集合中去
        drawLocOverlay(locOverlay, locPoint); //绘制当前位置图层
        mapView.getOverlays().add(gpsOverlay);
        clearOverlay();
        List<Point2D> points = new ArrayList<Point2D>(); //案件点集合
        if(caseModelList!=null && caseModelList.size() > 0)
        {
            for(CaseModel caseModel:caseModelList)
            {
                Point2D mp = new Point2D(caseModel.getLon(),caseModel.getLat());
                drawPointOverlay(caseModel.getLon(), caseModel.getLat());
                points.add(mp);
            }
        }

        Rectangle2D bounds = DataUtil.getGraphicsBound(points.toArray(new Point2D[points.size()]));
        DataUtil.zoomMap(bounds, mapView,locPoint);
    }

    /**
     * 分析最优路径
     */
    public void analyzePath(List<Point2D> geoPoints)
    {
        TipForm.newInstance().showProgressDialog(context);
        List<List<Point2D>> pointLists = NetWorkAnalystUtil.excutePathService(url,geoPoints);
        if (pathOverlays.size() != 0) {
            mapView.getOverlays().removeAll(pathOverlays);
            pathOverlays.clear();
        }
        if(pointLists!=null)
        {
            for (int i = 0; i < pointLists.size(); i++) {
                List<Point2D> geoPointList = pointLists.get(i);
                LineOverlay lineOverlay = new LineOverlay();
                lineOverlay.setLinePaint(new DrawUtil().getPolygonPaint(0x1c51e7, 200, 5));
                mapView.getOverlays().add(lineOverlay);
                lineOverlay.setData(geoPointList);
                lineOverlay.setShowPoints(false);
                pathOverlays.add(lineOverlay);
            }
            mapView.invalidate();
        }else{
            TipForm.showToast("没有找到最佳路径", context.getApplicationContext());
        }

        TipForm.newInstance().dismissDialog();
    }

    /**
     * 绘制当前选中案件
     * @param point2D
     */
    public void drawCaseOverlay(Point2D point2D,Boolean islevel)
    {
        if(caseOverlay!= null && mapView.getOverlays().contains(caseOverlay))
        {
            mapView.getOverlays().remove(caseOverlay);
        }
        PointOverlay pol = new PointOverlay(new DrawUtil().getPolygonPaint(0xe11100, 200,20));
        pol.setData(point2D);
        mapView.getOverlays().add(pol);

        mapView.getController().setCenter(point2D);
        if(islevel)
        {
            mapView.getController().setZoom(5);
            //TipForm.showToast(mapView.getMaxZoomLevel() + "级", getApplicationContext());
        }

        caseOverlay = pol;
        mapView.invalidate();
    }

    /**
     * 获取路径分析点集合
     * @return
     */
    public List<Point2D> getGeoPoints()
    {
        return geoPoints;
    }

    /**
     * 民情通版显示案件详情方法
     * @param overlay 当前定位点图层
     * @param cmp  详情案件坐标
     */
    public void showCaseDetailInfo(DefaultItemizedOverlay overlay,Point2D cmp,String url)
    {
        initAnalyzeParam(url);
        Point2D locPoint = null;
        if(isTest)
        {
            locPoint = new Point2D(104.944951,33.374131); //测试数据
        }else{
            locPoint = locateUtil.locateMap(mapView);
        }
        drawLocOverlay(overlay,locPoint);
        clearOverlay();
        drawPointOverlay(cmp);
        List<Point2D> points = new ArrayList<Point2D>();
        points.add(locPoint);
        points.add(cmp);
        analyzePath(points);
        /**
         * 根据案件点分布情况来缩放地图
         */
        Rectangle2D bounds = DataUtil.getGraphicsBound(points.toArray(new Point2D[points.size()]));
        DataUtil.zoomMap(bounds, mapView, mapView.getController());
    }

    /**
     * 领导通显示案件信息
     * @param points
     */
    public void showCaseInfo(Point2D[] points)
    {
        clearOverlay();
        Rectangle2D bounds = DataUtil.getGraphicsBound(points);
        for(Point2D point:points)
        {
            drawPointOverlay(point);
        }
        Log.i("ly", bounds.toString());
        DataUtil.zoomMap(bounds, mapView, mapView.getController());
    }

    /**
     * 领导通显示单个案件
     * @param mp
     */
    public void showSingleCase(Point2D mp,String url,String dataSet)
    {
        clearOverlay();
        drawPointOverlay(mp);
        TipForm.newInstance().showProgressDialog(context);
        //获取区域数据
        new RunQueryDataTask(url,dataSet, "1=1", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MessageEnum.QUERY_SUCCESS:
                        TipForm.newInstance().dismissDialog();
                        GetFeaturesResult queryResult = (GetFeaturesResult) msg.obj;
                        Rectangle2D bounds = DataUtil.getGraphicsBound(queryResult);
                        DataUtil.zoomMap(bounds, mapView, mapView.getController());
                        break;
                    case MessageEnum.QUERY_FAILED:
                        TipForm.newInstance().dismissDialog();
                        Toast.makeText(context,"获取区域信息失败", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        TipForm.newInstance().dismissDialog();
                        break;
                }
            }
        }).execute("*");

    }

    /*
     * 绘制带有图标的图层
     */
    protected void drawLocOverlay(DefaultItemizedOverlay dOverlay,Point2D point2D)
    {
        if(mapView.getOverlays().contains(dOverlay))
        {
            mapView.getOverlays().remove(dOverlay);
            if(dOverlay.size() > 0)
            {
                dOverlay.clear();
            }
        }
        OverlayItem itme = new OverlayItem(point2D, null, null);
        dOverlay.addItem(itme);
        mapView.getOverlays().add(dOverlay);
        mapView.invalidate();
    }

    /**
     * 将案件绘制到图层上
     * @param point
     */
    protected void drawPointOverlay(Point2D point)
    {
        PointOverlay overlay = new PointOverlay(point, context);
        mapView.getOverlays().add(overlay);
        pointOverlays.add(overlay);
    }

    protected void drawPointOverlay(double lon,double lat)
    {
        Point2D mp = new Point2D(lon, lat);
        drawPointOverlay(mp);
    }

    /**
     * 清空绘制区域
     */
    protected void clearOverlay()
    {
        if (pointOverlays.size() != 0) {
            mapView.getOverlays().removeAll(pointOverlays);
            pointOverlays.clear();
        }
        mapView.invalidate();
    }
}
