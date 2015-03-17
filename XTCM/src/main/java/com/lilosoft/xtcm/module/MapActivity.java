package com.lilosoft.xtcm.module;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.ags.query.QueryTask;
import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.NormalBaseActivity;
import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.instantiation.User;
import com.lilosoft.xtcm.utils.CenterPointUtil;
import com.lilosoft.xtcm.utils.LogFactory;
import com.lilosoft.xtcm.utils.XianTaoWMTSLayer;
import com.lilosoft.xtcm.views.MPProgressBar;
import com.lilosoft.xtcm.views.TitleBar;
import com.lilosoft.xtcm.views.TitleBar.STYLE;

public class MapActivity extends NormalBaseActivity {

    private final String TAG = "MapActivity";
    private final int MSG_LOST_NODATA = 0xDAB1C;
    private final int MSG_LOST_NOGRID = 0xDABC2;
    String wgbm = "";
    String gzqmc="";
    String wgmc = "";
    /**
     * @category 主线程处理
     */
    @SuppressLint("HandlerLeak")
    private Handler myHandle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case Config.SHOW_PROGRESS_DIALOG:
                    showProgressDialog("加载中…");
                    break;
                case Config.DISMISS_PROGRESS_DIALOG:
                    dismissProgressDialog();
                    if ("" != wgmc) {
                        Toast.makeText(mContext, wgmc, Toast.LENGTH_LONG).show();
                    }
                    break;
                case Config.MSG_LOST_CONN:
                    Toast.makeText(mContext, R.string.error_load_data,
                            Toast.LENGTH_LONG).show();
                    break;
                case MSG_LOST_NODATA:
                    Toast.makeText(mContext, R.string.grid_no_data_or_err,
                            Toast.LENGTH_LONG).show();
                    break;
                case MSG_LOST_NOGRID:
                    Toast.makeText(mContext, R.string.grid_no_permission_grid,
                            Toast.LENGTH_LONG).show();
                    break;
            }

        }

    };
    String[] grids = User.gridinfo;
    private TitleBar mTitleBar;
    private Button sure;
    private Button cancel;
    private Point reportPoint;
    private Point graphicPoint;
    private Point lonlatPoint;
    private Message m;
    private MapView mapView;
    private GraphicsLayer regionLayer;
    private GraphicsLayer gLayer;
    private GraphicsLayer textLayer;

    private boolean mIsHttpRequest;

    public Point getLonlatPoint() {
        return lonlatPoint;
    }

    private void drawArea() {
        LogFactory.e(TAG, "Map thread drawArea start");
        m = new Message();
        m.what = Config.SHOW_PROGRESS_DIALOG;
        myHandle.sendMessage(m);

        // String condition = "1=1";
        String condition = "*";
        if (null != grids && 0 != grids.length) {
            condition = "WGBM IN(";
            int lnth = grids.length;
            for (int i = 0; i < lnth; i++) {
                condition += "'" + grids[i] + "'";
                if (lnth - 1 != i) {
                    condition += ",";
                }
            }
            condition += ")";
            LogFactory.e(TAG + "_grids", condition);
        }
        if ("" != condition) {
            Query query = new Query();
            query.setReturnGeometry(true);
            // query.setWhere(condition);
            /**
             * 显示所有网格
             */
            query.setOutFields(new String[]{"*"});
            query.setWhere("1=1");
            //QueryTask queryTask = new QueryTask(Config.ARCGIS);
            QueryTask queryTask = new QueryTask(Config.WORKURL);
            try {
                FeatureSet fs = queryTask.execute(query);
                if (fs != null) {
                    Graphic[] grs = fs.getGraphics();
                    if (0 != grs.length) {
                        // grs = fs.getGraphics();
                        Polygon polygon = null;
                        regionLayer.removeAll();
                        textLayer.removeAll();
                        // selectedGrahpicLayer.removeAll();
                        ArrayList<Envelope> enveList = new ArrayList<Envelope>();
                        for (int i = 0; i < grs.length; i++) {
                            if (grs[i] != null && grs[i].getGeometry() != null) {

                                Geometry geometry = grs[i].getGeometry();

                                if (Geometry.Type.POLYGON == geometry.getType()) {
                                    Envelope envelope = new Envelope();
                                    polygon = (Polygon) geometry;
                                    polygon.queryEnvelope(envelope);
                                    enveList.add(envelope);
                                    // 定位
                                    // mapView.setExtent(polygon);
                                    /**
                                     * 绘制网格图层
                                     */
                                    SimpleFillSymbol sfs = new SimpleFillSymbol(
                                            0xcc00ff00);
                                    sfs.setAlpha(25);
                                    Graphic graphic = new Graphic(geometry, sfs);
                                    /**
                                     * 绘制文字图层
                                     */
								/*	TextSymbol textSymbol = new TextSymbol(12,
											(String) grs[i]
													.getAttributeValue("SSSQ")+
													grs[i].getAttributeValue("WGMC"),
													0xcc186494);*/
                                    TextSymbol textSymbol = new TextSymbol(12,
                                            (String) grs[i].getAttributeValue("GZQMC"),
                                            0xcc186494);
								/*	ArrayList<Envelope> al = new ArrayList<Envelope>();
									al.add(envelope);*/
									/*Graphic txtGraphic = new Graphic(
											CenterPointUtil.calcCenterPoint(al),
											textSymbol);*/
                                    Graphic txtGraphic = new Graphic(envelope.getCenter(),
                                            textSymbol);
//									textLayer.addGraphic(txtGraphic);
                                    regionLayer.addGraphic(graphic);
                                    // selectedGrahpicLayer.addGraphic(graphic);
                                    Log.v("grid-ly", "====结束定位  =");
                                    // break;
                                }
                            }
                        }
                        Point centerPoint = CenterPointUtil
                                .calcCenterPoint(enveList);
                        // mapView.zoomToResolution(centerPoint,
                        // 1.903568804664224E-5);

                        // mapView.centerAt(centerPoint, true);
                        /**
                         * GPS定位当前坐标
                         */
                        gLayer.removeAll();
                        reportPoint = new Point(
                                AutoReportLocusService.longitude,
                                AutoReportLocusService.latitude);
                        Log.e("cao",AutoReportLocusService.longitude+","+ AutoReportLocusService.latitude);
                        // reportPoint = new Point(113.47408229100957,
                        // 30.368182610723487);//测试点
                        lonlatPoint = reportPoint;
                        Drawable image1 = MapActivity.this.getBaseContext()
                                .getResources()
                                .getDrawable(R.drawable.location);
                        PictureMarkerSymbol picSymbol1 = new PictureMarkerSymbol(
                                image1);
                        Graphic graphic1 = new Graphic(reportPoint, picSymbol1);
                        gLayer.addGraphic(graphic1);
                        // mapView.centerAt(reportPoint, true);
                        new GetGridCode().start();
                        // mapView.centerAt(reportPoint, true);
                        mapView.zoomToResolution(reportPoint,
                                1.903568804664224E-5);
                    } else {
                        m = new Message();
                        m.what = MSG_LOST_NODATA;
                        myHandle.sendMessage(m);
                    }

                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogFactory.e(TAG, "Map thread drawArea err");
                m = new Message();
                m.what = Config.MSG_LOST_CONN;
                myHandle.sendMessage(m);
            } finally {
                LogFactory.e(TAG, "Map thread drawArea end");
                m = new Message();
                m.what = Config.DISMISS_PROGRESS_DIALOG;
                myHandle.sendMessage(m);
            }

        } else {
            m = new Message();
            m.what = Config.DISMISS_PROGRESS_DIALOG;
            myHandle.sendMessage(m);
            m = new Message();
            m.what = MSG_LOST_NOGRID;
            myHandle.sendMessage(m);

        }
    }

    @Override
    protected void installViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_map);
        mapView = (MapView) findViewById(R.id.map);
        sure = (Button) findViewById(R.id.sure);
        cancel = (Button) findViewById(R.id.cancel);
        initValue();

        initTitleBar();

        regionLayer = new GraphicsLayer();
        gLayer = new GraphicsLayer();
        textLayer = new GraphicsLayer();
        registerForContextMenu(mapView);


        {
            // Google Map
            // GoogleMapServiceLayer di = new GoogleMapServiceLayer();
            // mapView.addLayer(di);
        }
        {
            // 离线
            // String path = "file://" + Config.FILES_NAME_URL +
            // "shayang2d/Layers";
            // ArcGISLocalTiledLayer local = new ArcGISLocalTiledLayer(path);
        }

        ArcGISTiledMapServiceLayer local = new ArcGISTiledMapServiceLayer(
                Config.MAP);
        //XianTaoWMTSLayer local = new XianTaoWMTSLayer();
        mapView.addLayer(local);

        mapView.addLayer(regionLayer);
        mapView.addLayer(textLayer);
        mapView.addLayer(gLayer);
        // Point whPoint = new Point(113.455113, 30.362674);
        // Point cp = (Point) GeometryEngine.project(whPoint,
        // SpatialReference.create(4326), SpatialReference.create(102113));
        // mapView.centerAt(new Point(-20037508.342787, 20037508.342787), true);
        // mapView.centerAt((Point)geo, true);
        // mapView.setScale(288895.277144);
        // mapView.zoomToScale((Point)geo, 295828763.795777);
        // mapView.zoomToResolution(cp, 19.109257071268);
        mIsHttpRequest = true;
        new drawAreaThread().start();
        // Point p = (Point)geo;
        // Log.v("ly", cp.getX() + "," + cp.getY());
    }

    @SuppressWarnings("serial")
    @Override
    protected void registerEvents() {
        // TODO Auto-generated method stub
        mapView.setOnSingleTapListener(new OnSingleTapListener() {

            @Override
            public void onSingleTap(float x, float y) {
                // TODO Auto-generated method stub
                gLayer.removeAll();
                reportPoint = mapView.toMapPoint(new Point(x, y));
                lonlatPoint = reportPoint;
                // reportPoint = mapView.toMapPoint(new Point(x, y));
                // lonlatPoint = (Point) GeometryEngine.project(reportPoint,
                // SpatialReference.create(102113),
                // SpatialReference.create(4326));

                // red point
                Drawable image1 = MapActivity.this.getBaseContext()
                        .getResources().getDrawable(R.drawable.light_red);
                PictureMarkerSymbol picSymbol1 = new PictureMarkerSymbol(image1);
                Graphic graphic1 = new Graphic(reportPoint, picSymbol1);
                gLayer.addGraphic(graphic1);

                // chat
                // graphicPoint = mapView.toMapPoint(new Point(x - 5, y - 5));
                // Drawable image = MapActivity.this.getBaseContext()
                // .getResources().getDrawable(R.drawable.chat);
                // image.setBounds(10, 10, 0, 0);
                // PictureMarkerSymbol picSymbol = new
                // PictureMarkerSymbol(image);
                // Graphic graphic = new Graphic(graphicPoint, picSymbol);
                // gLayer.addGraphic(graphic);

                mapView.centerAt(reportPoint, true);
                new GetGridCode().start();

            }
        });
        sure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (null != lonlatPoint && "" != wgbm && null != wgbm) {
                    setResult(
                            Activity.RESULT_OK,
                            new Intent().putExtra("local", lonlatPoint.getX()
                                    + "," + lonlatPoint.getY() + "," + wgbm
                                    + "," + wgmc+","+gzqmc));
                    finish();
                } else if ("" == wgbm) {
                    Toast.makeText(MapActivity.this, "请点击网格内！",
                            Toast.LENGTH_SHORT).show();
                } else if (null == wgbm) {
                    Toast.makeText(MapActivity.this, "未获取到数据！",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MapActivity.this, "请选择地点！",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                MapActivity.this.finish();
            }
        });
    }

    /**
     * @category 初始化titleBar
     */
    protected void initTitleBar() {

        mTitleBar = (TitleBar) findViewById(R.id.titlebar);

        mTitleBar.changeStyle(STYLE.NOT_BTN_AND_TITLE);

        mTitleBar.centerTextView.setText(R.string.function_select_map);

    }

    protected void initValue() {

        mPProgressBar = (MPProgressBar) findViewById(R.id.mPProgressBar);
    }

    private class drawAreaThread extends Thread {

        @Override
        public void run() {
            if (mIsHttpRequest) {
                drawArea();
                mIsHttpRequest = false;
            }
        }
    }

    private class GetGridCode extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            LogFactory.e(TAG, "Map thread Get GridCode start");
            m = new Message();
            m.what = Config.SHOW_PROGRESS_DIALOG;
            myHandle.sendMessage(m);
            Query query = new Query();
            query.setGeometry(reportPoint);
            query.setOutFields(new String[] { "*" });
            // query.setReturnGeometry(true);

            // query.setGeometry(identifyPoint2);
            QueryTask queryTask2 = new QueryTask(Config.ARCGIS);
            FeatureSet fs = null;//
            try {
                fs = queryTask2.execute(query);
                // grs[0].getAttributes().get("WGBM").toString();
                wgbm = "";
                wgmc = "";
                {
                    // if (fs.getGraphics().length > 0) {
                    // Graphic gra = fs.getGraphics()[0];
                    // // wgbm = (String) gra.getAttributes().get("BGCODE");
                    // wgbm = (String) gra.getAttributes().get("WGBM");
                    // wgmc = (String) gra.getAttributes().get("WGMC");
                    // }
                    if (fs.getGraphics().length > 0) {
                        Graphic gra = fs.getGraphics()[0];
                        // wgbm = (String) gra.getAttributes().get("BGCODE");
                        String TempWGBM = (String) gra.getAttributes().get(
                                "WGBM");
                        wgbm = TempWGBM;
                        wgmc = (String) gra.getAttributes().get("WGMC");
                        gzqmc=(String) gra.getAttributes().get("GZQMC");
                        if (grids != null && grids.length > 0) {
                            for (int i = 0; i < grids.length; i++) {
                                if (TempWGBM.equals(grids[i])) {
                                    // wgbm = TempWGBM;
                                    // wgmc = (String)
                                    // gra.getAttributes().get("WGMC");
                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogFactory.e(TAG, "Map thread Get GridCode err");
                m = new Message();
                m.what = Config.MSG_LOST_CONN;
                myHandle.sendMessage(m);
            } finally {
                LogFactory.e(TAG, "Map thread Get GridCode end");
                m = new Message();
                m.what = Config.DISMISS_PROGRESS_DIALOG;
                myHandle.sendMessage(m);
            }
        }
    }

}
