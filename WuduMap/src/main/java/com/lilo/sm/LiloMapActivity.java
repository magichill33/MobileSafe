package com.lilo.sm;

import java.util.List;

import com.lilo.event.ButtonTouchListener;
import com.lilo.model.CaseModel;
import com.lilo.model.MessageEnum;
import com.lilo.util.CaseCheckUtil;
import com.lilo.util.CaseReportUtil;
import com.lilo.util.DataUtil;
import com.lilo.util.DrawHouseUtil;
import com.lilo.util.DrawUtil;
import com.lilo.util.HouseGatherUtil;
import com.lilo.util.PartUpdateUtil;
import com.lilo.widget.TipForm;
import com.supermap.android.data.GetFeaturesResult;
import com.supermap.android.maps.DefaultItemizedOverlay;
import com.supermap.android.maps.LayerView;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.Point2D;
import com.supermap.android.maps.PolygonOverlay;
import com.supermap.services.components.commontypes.Feature;
import com.supermap.services.components.commontypes.Geometry;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

public class LiloMapActivity extends Activity {

    private MapView mapView;
    private int titleBarHeight; //界面标题栏高度
    private String dataUrl; //数据服务地址


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lilo_map);

        mapView = (MapView) this.findViewById(R.id.mapview);
        dataUrl = getString(R.string.mainUrl) + "/" + getString(R.string.dataUrl);
        String map2d = getString(R.string.mainUrl) + "/" + getString(R.string.map2d);
        //String map2d = "http://10.1.1.8:8090/iserver/services/map-china400/rest/maps/China";
        LayerView layerView = new LayerView(this,map2d,4326);
        mapView.setBuiltInZoomControls(true);
        mapView.addLayer(layerView);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int moduleType = bundle.getInt("moduleType"); //模块类型，根据类型值调用不同的功能模块

        switch (moduleType) {
            case 0: //案件上报功能模块
                //案件上报
                String gridCodes = "62120210000601,62120210000602";
                DefaultItemizedOverlay labelOverlay = new DefaultItemizedOverlay(getResources().getDrawable(R.drawable.light_red));
                showCaseReportMoudel(mapView, this, labelOverlay, dataUrl, getString(R.string.region_grid),gridCodes);
                break;
            case 1: //房屋更新功能模块
                String gridCodes1 = bundle.getString("gridCodes");
                String houseCodes = bundle.getString("houseCodes");
                showHouseGatherMoudle(mapView, this, dataUrl, getString(R.string.region_grid),
                        getString(R.string.sm_house),gridCodes1,houseCodes);
                break;
            case 2: //民情通多个待核实事件功能模块
                List<CaseModel> caseModelList = bundle.getParcelableArrayList("caselist");
                String analyzeUrl = getString(R.string.mainUrl) + "/" +getString(R.string.networkUrl);
                caseCheckModule(mapView, this, analyzeUrl, caseModelList);
                break;
            case 3: //民情通单个案件核实功能模块
                Point2D casePoint = new Point2D(104.926917,33.388884); //案件点，点击详情时传过来
                String analyzeUrl1 = getString(R.string.mainUrl) + "/" +getString(R.string.networkUrl);
                singleCaseCheckModule(mapView, this, analyzeUrl1, casePoint);
                break;
            case 4: //领导通案件核实
                Point2D[] points = new Point2D[]{new Point2D(104.926917,33.388884),
                        new Point2D(104.942456,33.375698),
                        new Point2D(104.944951,33.374131)};
                leaderCaseCheckModule(mapView, this, points);
                break;
            case 5: //领导通单个案件核实
                Point2D casePoint1 = new Point2D(104.926917,33.388884);
                leaderSinglerCaseCheckModule(mapView, this, casePoint1, dataUrl, getString(R.string.region_dist));
                break;
            case 6: //案件定位
                Point2D casePoint6 = new Point2D(104.926879,33.388646);
                String gridCode6 = "62120210000602";
                locateCaseMoudel(casePoint6, gridCode6, mapView, this, dataUrl, getString(R.string.region_grid));
                break;
            case 7: //房屋定位
                String houseCode = "62120210000601004";
                locateHouseModule(houseCode, dataUrl, getString(R.string.sm_house), mapView, this);
                break;
            case 8: //部件更新功能模块
                String pType = bundle.getString("pType");
                String gridCodes8 = bundle.getString("gridCodes");
                String partsUrl = getString(R.string.mainUrl) + "/" + getString(R.string.partsUrl);
                partUpdateMoudle(mapView, this, pType, partsUrl, dataUrl, getString(R.string.wudu_cmp), getString(R.string.region_grid), gridCodes8);
                break;
            default:
                break;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.destroy();
    }

    /**
     * 事件上报模块
     */
    public void showCaseReportMoudel(MapView mapView,Context context,DefaultItemizedOverlay labelOverlay,
                                     String url,String dataSet,String gridCodes)
    {
        ImageButton btnMapVista = (ImageButton) findViewById(R.id.mapVista); //实景图显示按钮
        ImageButton btnMapSplit = (ImageButton) findViewById(R.id.mapSplit); //分屏显示按钮
        btnMapVista.setVisibility(View.VISIBLE); //设置为可见
        btnMapSplit.setVisibility(View.VISIBLE);

        CaseReportUtil caseReportUtil = new CaseReportUtil(mapView, context, labelOverlay);
        registerEvent(btnMapVista,gridCodes,caseReportUtil); //注册监听事件
        registerEvent(btnMapSplit,gridCodes,caseReportUtil);
        caseReportUtil.initCaseReport(url, dataSet, gridCodes);
    }

    /**
     * 房屋信息采集功能模块
     */
    public void showHouseGatherMoudle(MapView mapView,Context context,String url,
                                      String region_grid,String sm_house,String gridCodes,String houseCodes)
    {
        HouseGatherUtil houseGatherUtil = new HouseGatherUtil(mapView, context);
        houseGatherUtil.initHouseGather(url, region_grid, sm_house, gridCodes, houseCodes);
    }

    /**
     * 民情通多个案件核实功能
     * @param mapView
     * @param context
     */
    public void caseCheckModule(MapView mapView,Context context,String url,List<CaseModel> caseModelList)
    {
        DefaultItemizedOverlay locOverlay = new DefaultItemizedOverlay(
                getResources().getDrawable(R.drawable.location));
        DefaultItemizedOverlay gpsOverlay = new DefaultItemizedOverlay(
                getResources().getDrawable(R.drawable.cgps));
        ImageButton gpsButton = (ImageButton) findViewById(R.id.btnLocate);
        gpsButton.setVisibility(View.VISIBLE);

        mapView.post(new Runnable() {
            @Override
            public void run() {
                titleBarHeight = initHeight();
            }
        });

        CaseCheckUtil caseCheckUtil = new CaseCheckUtil(mapView, context,true); //正试使用时将true改为false
        caseCheckUtil.analyzeCasePath(url,locOverlay, caseModelList,gpsButton,gpsOverlay);
    }

    /**
     * 民情通单个案件核实
     * @param mapView
     * @param context
     * @param point
     */
    public void singleCaseCheckModule(MapView mapView,Context context,String url,Point2D point)
    {
        DefaultItemizedOverlay locOverlay = new DefaultItemizedOverlay(
                getResources().getDrawable(R.drawable.location));
        CaseCheckUtil caseCheckUtil = new CaseCheckUtil(mapView, context,true); //正试使用时将true改为false
        caseCheckUtil.showCaseDetailInfo(locOverlay, point, url);
    }

    /**
     * 领导通多个案件核实功能
     * @param mapView
     * @param context
     * @param points
     */
    public void leaderCaseCheckModule(MapView mapView,Context context,Point2D[] points)
    {
        CaseCheckUtil caseCheckUtil = new CaseCheckUtil(mapView, context, true);
        caseCheckUtil.showCaseInfo(points);
    }

    /**
     * 领导通单个案件核实功能
     * @param mapView
     * @param context
     * @param point
     * @param url
     * @param dataSet
     */
    public void leaderSinglerCaseCheckModule(MapView mapView,Context context,Point2D point,
                                             String url,String dataSet)
    {
        CaseCheckUtil caseCheckUtil = new CaseCheckUtil(mapView, context, true);
        caseCheckUtil.showSingleCase(point, url, dataSet);
    }

    /**
     * 案件定位功能
     * @param point
     * @param gridCode
     * @param mapView
     * @param context
     * @param url
     * @param dataSet
     */
    public void locateCaseMoudel(Point2D point,String gridCode,MapView mapView,Context context,
                                 String url,String dataSet)
    {
        DefaultItemizedOverlay labelOverlay = new DefaultItemizedOverlay(getResources().getDrawable(R.drawable.light_red));
        CaseReportUtil caseReportUtil = new CaseReportUtil(mapView, context, labelOverlay);
        caseReportUtil.locateCaseByCoord(url, dataSet, point, gridCode);
    }

    /**
     * 房屋定位功能
     * @param Code
     * @param url
     * @param dataSet
     * @param mapView
     * @param context
     */
    public void locateHouseModule(String houseCode,String url,String dataSet,MapView mapView,Context context)
    {
        DrawHouseUtil drawHouseUtil = new DrawHouseUtil(mapView, context);
        drawHouseUtil.drawHouseByCode(url, dataSet, houseCode);
    }

    /**
     * 部件更新功能
     * @param mapView
     * @param context
     * @param pType
     * @param partsUrl
     * @param regionUrl
     * @param wudu_cmp
     * @param region_grid
     * @param gridCodes
     */
    public void partUpdateMoudle(MapView mapView,Context context,String pType,String partsUrl,
                                 String regionUrl,String wudu_cmp,String region_grid,String gridCodes)
    {

        PartUpdateUtil partUpdateUtil = new PartUpdateUtil(mapView, context);
        partUpdateUtil.drawPartsOnMap(pType, partsUrl, regionUrl, wudu_cmp, region_grid, gridCodes);
    }




    protected void registerEvent(ImageButton imageButton,String codes,CaseReportUtil caseReportUtil) {

        imageButton.setOnTouchListener(new ButtonTouchListener(imageButton,codes,caseReportUtil,LiloMapActivity.this));
    }

    /**
     * 计算标题栏的高度
     * @return
     */
    private int initHeight(){
        Rect rect =new Rect();
        Window window = getWindow();
        mapView.getWindowVisibleDisplayFrame(rect);
        //状态栏的高度
        int statusBarHight =rect.top;
        //标题栏跟状态栏的总体高度
        int contentViewTop =window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        //标题栏的高度
        int titleBarHeight =contentViewTop -statusBarHight;
        return titleBarHeight;
    }

    public int getTitleBarHeight() {
        return titleBarHeight;
    }

    public void setTitleBarHeight(int titleBarHeight) {
        this.titleBarHeight = titleBarHeight;
    }


}
