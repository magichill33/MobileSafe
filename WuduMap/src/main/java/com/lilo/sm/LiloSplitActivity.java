package com.lilo.sm;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.leador.TV.Enum.DataTypeEnum;
import com.leador.TV.Enum.ImageBtnShowMode;
import com.leador.TV.Exception.TrueMapException;
import com.leador.TV.Listeners.ImageGetListener;
import com.leador.TV.Listeners.ImageStateListener;
import com.leador.TV.Listeners.ImageTouchEvent;
import com.leador.TV.Listeners.ImageTouchListener;
import com.leador.TV.Marker.ControlBtnManager;
import com.leador.TV.Marker.MarkerInfo;
import com.leador.TV.Station.Coord;
import com.leador.TV.Station.StationInfo;
import com.leador.TV.TrueVision.TrueVision;
import com.lilo.event.LiloMapViewEventListener;
import com.lilo.model.MessageEnum;
import com.lilo.service.RunQueryByPoint;
import com.lilo.service.RunQueryDataTask;
import com.lilo.util.DataUtil;
import com.lilo.util.DensityUtil;
import com.lilo.widget.SelectDialog;
import com.lilo.widget.TipForm;
import com.supermap.android.data.GetFeaturesResult;
import com.supermap.android.maps.DefaultItemizedOverlay;
import com.supermap.android.maps.LayerView;
import com.supermap.android.maps.LineOverlay;
import com.supermap.android.maps.MBTilesLayerView;
import com.supermap.android.maps.MapController;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.OverlayItem;
import com.supermap.android.maps.Point2D;
import com.supermap.android.maps.PolygonOverlay;
import com.supermap.services.components.commontypes.Feature;
import com.supermap.services.components.commontypes.Geometry;
import com.supermap.services.components.commontypes.Rectangle2D;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 分屏展示实景
 * @author Administrator
 *
 */
public class LiloSplitActivity extends Activity implements ImageGetListener,
        ImageStateListener,ImageTouchListener{

    public boolean isMapMove = true;
    private MapView mapView;
    private LayerView layerView;
    private MapController mapController;
    private Drawable streetDrawable;
    private ImageButton btnBack;
    /**
     * 影像控件，配置在布局文件中，也可以在代码中new
     */
    private TrueVision ldTV;
    private Handler handler; //实景路径绘制handler
    private Handler viewHandler; //实景查询handler
    private List<LineOverlay> pathOverlayers = new ArrayList<LineOverlay>(); //区域图层
    private ProgressDialog progressDialog;
    /**
     * 前后 和放大缩小控制按钮初始化
     *
     */
    private String beforeTag = "Before";
    private String afterTag = "After";
    private String zoominTag = "Zoomin";
    private String zoomOutTag = "Zoomout";
    private String holeingControlBtnTag = "";
    private String preTag = "prePlay";
    private String nextTag = "nextPlay";
    private boolean isPlay = false;
    Handler hodingHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                if (isPlay == false) {
                    endPlayImage();
                    return;
                }
                if (holeingControlBtnTag.equals(preTag)) {
                    ldTV.setControlShowMode("After", ImageBtnShowMode.visable);
                    ldTV.setControlShowMode("Before", ImageBtnShowMode.down);
                } else if (holeingControlBtnTag.equals(nextTag)) {
                    ldTV.setControlShowMode("After", ImageBtnShowMode.down);
                    ldTV.setControlShowMode("Before", ImageBtnShowMode.visable);
                }

                StationInfo currentStation = ldTV.getCurrentStationJuction();
                if (holeingControlBtnTag.equals(preTag)) {
                    if (currentStation.isNode()
                            && currentStation.getNodeIsBegin() == 1) {
                        selectNode(currentStation);
                    } else {
                        ldTV.findPreImage();
                    }
                } else if (holeingControlBtnTag.equals(nextTag)) {
                    currentStation = ldTV.getCurrentStationJuction();
                    if (currentStation.isNode()
                            && currentStation.getNodeIsBegin() == 0) {
                        selectNode(currentStation);
                    } else {
                        ldTV.findNextImage();
                    }
                }
            } catch (TrueMapException e) {
                endPlayImage();
            }
            super.handleMessage(msg);
        }
    };
    private LiloMapViewEventListener mapListener;
    //private DefaultItemizedOverlay streetOverlay;
    private ImageView vistaView;
    private String dataUrl;
    private String gridCodes;
    private Timer hodingTimer = new Timer();
    private TimerTask hodingTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lilo_street);
        Bundle bundle = getIntent().getExtras();
        mapView = (MapView) this.findViewById(R.id.mapview);
        ldTV = (TrueVision) this.findViewById(R.id.LDTVView);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LiloSplitActivity.this.finish();
            }
        });
        vistaView = (ImageView) this.findViewById(R.id.imgVista);
        Point2D point = (Point2D) bundle.getSerializable("cp");
        gridCodes = bundle.getString("gridCodes");
        initTV(point);
        initTvValue();
        String map2d = getString(R.string.mainUrl) + "/" + getString(R.string.map2d);
        dataUrl = getString(R.string.mainUrl) + "/" + getString(R.string.dataUrl);

        streetDrawable = getResources().getDrawable(R.drawable.vista);


        mapController = mapView.getController();
        layerView = new LayerView(this);
        layerView.setURL(map2d);
        mapView.setBuiltInZoomControls(true);
        mapView.addLayer(layerView);

        //streetOverlay = new DefaultItemizedOverlay(streetDrawable);
        //mapView.getOverlays().add(streetOverlay);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MessageEnum.QUERY_SUCCESS:
                        GetFeaturesResult queryResult = (GetFeaturesResult) msg.obj;
                        showQueryResult(queryResult);

                        break;
                    case MessageEnum.QUERY_FAILED:

                        Toast.makeText(LiloSplitActivity.this,"获取实景路径信息失败", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        // progressDialog.dismiss();
                        break;
                }
            }
        };
        viewHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MessageEnum.QUERY_SUCCESS:
                        GetFeaturesResult queryResult = (GetFeaturesResult) msg.obj;
                        //progressDialog.dismiss();
                        dismissProgress();
                        Point2D cp = mapView.getCenter();
                        if (queryResult == null || queryResult.features == null || queryResult.featureCount == 0) {
                            ShowToast("查询结果为空");
                        }else{
                            Feature f = queryResult.features[0];
                            Geometry geo = f.geometry;
                            com.supermap.services.components.commontypes.Point2D point = geo.points[0];
                            try {
                                isMapMove = false;
                                mapController.setCenter(new Point2D(point.x, point.y));
                                ldTV.locImgByLonlat(point.x, point.y, 0.001);

                            } catch (TrueMapException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case MessageEnum.QUERY_FAILED:
                        //progressDialog.dismiss();
                        dismissProgress();
                        Toast.makeText(LiloSplitActivity.this,"该区域没有实景信息", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
        mapListener = new LiloMapViewEventListener(dataUrl,getString(R.string.ld_link),viewHandler,null,this);
        //mapView.addMapViewEventListener(mapListener);
        clearRegionLay();
        new RunQueryDataTask(dataUrl, this.getString(R.string.ld_link), "1=1", handler).execute("*");
    }

    /**
     * 根据查询返回的结果集绘制区域
     * @param result
     */
    protected void showQueryResult(GetFeaturesResult result) {
        if (result == null || result.features == null) {
            Toast.makeText(this, "查询结果为空!", Toast.LENGTH_LONG).show();
            return;
        }
        List<List<Point2D>> pointsLists = new ArrayList<List<Point2D>>();
        Feature[] queryfeatures = result.features;


        Rectangle2D bounds = DataUtil.getGraphicsBound(result);
        DataUtil.zoomMap(bounds, mapView, mapController);

        for(Feature f:queryfeatures)
        {

            Geometry geometry = f.geometry;
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

        }

        // 把所有查询的几何对象都高亮显示
        for (int m = 0; m < pointsLists.size(); m++) {
            List<Point2D> geoPointList = pointsLists.get(m);
            //PolygonOverlay polygonOverlay = new PolygonOverlay(getPolygonPaint(Color.BLUE,180,3));
            LineOverlay path = new LineOverlay(getPolygonPaint(0x045ff5, 100, 10));
            mapView.getOverlays().add(path);
            pathOverlayers.add(path);
            path.setData(geoPointList);
            path.setShowPoints(false);

        }

        this.mapView.invalidate(); //刷新地图

       /* OverlayItem overlayItem = new OverlayItem(mapView.getCenter(),"标注", "标注");
        streetOverlay.addItem(overlayItem);
        mapView.getOverlays().add(streetOverlay);*/
      /*  removeOverlay();
        addOverlay();*/
        this.mapView.addMapViewEventListener(mapListener);
    }

    /**
     * 清空绘制区域
     */
    protected void clearRegionLay()
    {
        if (pathOverlayers.size() != 0) {
            mapView.getOverlays().removeAll(pathOverlayers);
            pathOverlayers.clear();
        }
        mapView.invalidate();
    }

    // 绘面风格
    private Paint getPolygonPaint(int color,int alpha,float width) {
        return getPolygonPaint(color, alpha, width, Paint.Style.STROKE);
    }

    private Paint getPolygonPaint(int color,int alpha,float width,Style style) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setAlpha(alpha);

        paint.setStyle(style);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(width);
        return paint;
    }

    /**
     *
     * 初始化影像控件 文件路径和访问方式
     */
    public void initTV(Point2D cp) {

        String dataPathTV = Environment.getExternalStorageDirectory().getPath()
                + "/wdq";
        try {
            // 初始化，离线，数据路径
            ldTV.ldTVInit(DataTypeEnum.offLine_Type, dataPathTV);
            // 将实现的接口注册，接口的回调函数才可用
            ldTV.setOnTouchViewClick(this);
            ldTV.setOnStateChanged(this);
            ldTV.setOnGetImage(this);
        } catch (TrueMapException e) {
            e.printStackTrace();
        }

        try {
            if(cp!=null)
            {
                ldTV.locImgByLonlat(cp.x, cp.y, 0.001);
            }else{
                ldTV.locImgByImgID("000266-5-201406110326110094");
            }

        } catch (TrueMapException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化控件按钮相关
     */
    public void initTvValue() {
        try {
            ControlBtnManager controlBtnManager = new ControlBtnManager(ldTV);
            Bitmap controlBmAfter0 = BitmapFactory.decodeResource(
                    getResources(), R.drawable.imaging_control_on_normal);
            Bitmap controlBmAfter1 = BitmapFactory.decodeResource(
                    getResources(), R.drawable.imaging_control_on_down);
            Bitmap controlBmAfter2 = BitmapFactory.decodeResource(
                    getResources(), R.drawable.imaging_control_on_disabled);
            Bitmap[] controlBmAfter = new Bitmap[3];
            controlBmAfter[0] = controlBmAfter0;
            controlBmAfter[1] = controlBmAfter1;
            controlBmAfter[2] = controlBmAfter2;

            controlBtnManager.addControlBtn(controlBmAfter, afterTag, 0, 0, 0,
                    0, 0, 0);

            Bitmap[] controlBmBefore = new Bitmap[3];
            Bitmap controlBmBefore0 = BitmapFactory.decodeResource(
                    getResources(), R.drawable.imaging_control_down_normal);
            Bitmap controlBmBefore1 = BitmapFactory.decodeResource(
                    getResources(), R.drawable.imaging_control_down_down);
            Bitmap controlBmBefore2 = BitmapFactory.decodeResource(
                    getResources(), R.drawable.imaging_control_down_disabled);
            controlBmBefore[0] = controlBmBefore0;
            controlBmBefore[1] = controlBmBefore1;
            controlBmBefore[2] = controlBmBefore2;
            controlBtnManager.addControlBtn(controlBmBefore, beforeTag, 0, 0,
                    0, 0, 0, 0);

            Bitmap[] controlBmZoomin = new Bitmap[3];
            Bitmap controlBmZoomin0 = BitmapFactory.decodeResource(
                    getResources(), R.drawable.imaging_control_narrow_normal);
            Bitmap controlBmZoomin1 = BitmapFactory.decodeResource(
                    getResources(), R.drawable.imaging_control_narrow_down);
            Bitmap controlBmZoomin2 = BitmapFactory.decodeResource(
                    getResources(), R.drawable.imaging_control_narrow_disabled);
            controlBmZoomin[0] = controlBmZoomin0;
            controlBmZoomin[1] = controlBmZoomin1;
            controlBmZoomin[2] = controlBmZoomin2;
            controlBtnManager.addControlBtn(controlBmZoomin, zoominTag, 0,
                    0.85, 40, 40, 0, 0);

            Bitmap controlBmZoomOut0 = BitmapFactory.decodeResource(
                    getResources(),
                    R.drawable.imaging_control_amplification_normal);
            Bitmap controlBmZoomOut1 = BitmapFactory.decodeResource(
                    getResources(),
                    R.drawable.imaging_control_amplification_down);
            Bitmap controlBmZoomOut2 = BitmapFactory.decodeResource(
                    getResources(),
                    R.drawable.imaging_control_amplification_disabled);
            Bitmap[] controlBmZoomOut = new Bitmap[3];
            controlBmZoomOut[0] = controlBmZoomOut0;
            controlBmZoomOut[1] = controlBmZoomOut1;
            controlBmZoomOut[2] = controlBmZoomOut2;
            controlBtnManager.addControlBtn(controlBmZoomOut, zoomOutTag, 0, 0,
                    0, 0, 0, 0);

            // Bitmap changeBm = BitmapFactory.decodeResource(getResources(),
            // R.drawable.change);
            // controlBtnManager.addControlBtn(changeBm, changeTag, 0.5, 1, 50,
            // 50, -20, -50);
            int dp = -130;
            int px = DensityUtil.dip2px(getApplicationContext(),dp);
            int beginDrawcontrol = px;
            ldTV.setControlBtnManager(controlBtnManager);
            ldTV.setOnTouchViewClick(this);
            ldTV.setOnStateChanged(this);
            ldTV.setOnGetImage(this);

            ldTV.setControlBtnLayOut(afterTag, 1, 1,
                    controlBmAfter0.getWidth(), controlBmAfter0.getHeight(),
                    beginDrawcontrol, beginDrawcontrol);
            ldTV.setControlBtnLayOut(beforeTag, 1, 1,
                    controlBmBefore0.getWidth(), controlBmBefore0.getHeight(),
                    beginDrawcontrol,
                    beginDrawcontrol + controlBmAfter0.getHeight()
                            + controlBmZoomin0.getHeight());
            ldTV.setControlBtnLayOut(zoominTag, 1, 1,
                    controlBmZoomin0.getWidth(), controlBmZoomin0.getHeight(),
                    beginDrawcontrol + controlBmZoomOut0.getWidth(),
                    beginDrawcontrol + controlBmAfter0.getHeight());
            ldTV.setControlBtnLayOut(zoomOutTag, 1, 1,
                    controlBmZoomOut0.getWidth(),
                    controlBmZoomOut0.getHeight(), beginDrawcontrol,
                    beginDrawcontrol + controlBmAfter0.getHeight());
            //ldTV.setControlShowMode(zoominTag, ImageBtnShowMode.disable);
        } catch (TrueMapException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void imageClick(ImageTouchEvent arg0) {

    }

    @Override
    public void imageFling(ImageTouchEvent arg0, ImageTouchEvent arg1,
                           float arg2, float arg3) {

    }

    @Override
    public void imageHold(ImageTouchEvent event) {
        //showToast("imageHold");
    }

    @Override
    public void imageMarkerSelected(ImageTouchEvent arg0,
                                    ArrayList<MarkerInfo> arg1) {

    }

    @Override
    public void imageTouch(ImageTouchEvent event) {
        StationInfo stationInfo = null;
        try {
            stationInfo = ldTV.getCurrentStationJuction();
        } catch (TrueMapException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Coord coord = stationInfo.getCoord();
        //TipForm.showToast(coord.getLon() + "," + coord.getLat(), getApplicationContext());
        try {
            ldTV.deleteMarker("1001");
            ldTV.addMarker("1001", "ly",
                    ldTV.getImageID(), event.imageScaleX, event.imageScaleY,
                    BitmapFactory.decodeResource(LiloSplitActivity.this.getResources(), R.drawable.light_red));
        } catch (TrueMapException e) {
            e.printStackTrace();
        }

        final Point2D touchPoint = new Point2D(coord.getLon(), coord.getLat());
        final double x = event.imageScaleX;
        final double y = event.imageScaleY;
        /**
         * 点击实景handler
         */
        Handler touchGridHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MessageEnum.QUERY_SUCCESS:
                        TipForm.newInstance().dismissDialog();
                        GetFeaturesResult gridResult = (GetFeaturesResult) msg.obj;
                        try {
                            postGridInfo(gridResult,touchPoint,x,y,ldTV.getImageID());
                        } catch (TrueMapException e) {
                            postGridInfo(gridResult,touchPoint,x,y,null);
                            e.printStackTrace();
                        }
                        break;
                    case MessageEnum.QUERY_FAILED:
                        TipForm.newInstance().dismissDialog();
                        Toast.makeText(LiloSplitActivity.this,"获取网格信息失败", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        TipForm.newInstance().dismissDialog();
                        break;
                }
            }
        };
        new RunQueryByPoint(dataUrl,getString(R.string.region_grid),touchGridHandler).execute(new Point2D(coord.getLon(),coord.getLat()));
    }

    /**
     * 信息采集提交方法
     * @param gridResult
     * @param touchPoint
     */
    protected void postGridInfo(GetFeaturesResult gridResult, Point2D touchPoint,double x,
                                double y,String imageId) {
        if (gridResult == null || gridResult.features == null || gridResult.featureCount == 0) {
            Toast.makeText(this, "查询结果为空!", Toast.LENGTH_LONG).show();
            return;
        }

        Feature feature = gridResult.features[0];
        String gridCode = "";
        for(int i = 0;i<feature.fieldNames.length;i++)
        {
            if(feature.fieldNames[i].equals(getString(R.string.gridCode)))
            {
                gridCode = feature.fieldValues[i];
                break;
            }
        }

        TipForm.showToast("网格编码：" + gridCode + "坐标：" + touchPoint.x + "," + touchPoint.y
                + "位置：" + x + "," + y + "图片ID：" + imageId,getApplicationContext());
    }

    @Override
    public void imageonDoubleTap(ImageTouchEvent arg0) {

    }

    @Override
    public void imageGetOver(boolean arg0, String arg1) {

    }

    @Override
    public void imageIDChanged(String arg0) {

    }

    @Override
    public void imageTypeChanged(String arg0) {

    }

    @Override
    public void yawChanged(double arg0) {

    }

    @Override
    public void zoomScalseChanged(double arg0) {

    }

    @Override
    public void getCamerasComplete(boolean arg0, TrueMapException arg1) {

    }

    @Override
    public void getCutImageComplete(boolean arg0, String arg1,
                                    TrueMapException arg2) {

    }

    @Override
    public void getSmallImageComplete(boolean arg0, String arg1,
                                      TrueMapException arg2) {

    }

    @Override
    public void getStationComplete(boolean arg0, String arg1,
                                   TrueMapException arg2) {

    }

    @Override
    public void controlBtnHold(String controlBtnTag) {
        if (controlBtnTag.equals("Before")) {
            holeingControlBtnTag = preTag;
            isPlay = true;
        } else if (controlBtnTag.equals("After")) {
            holeingControlBtnTag = nextTag;
            isPlay = true;
        }
        beginPlayImage();
    }

    private void beginPlayImage() {
        if (isPlay) {
            if (hodingTimer != null && hodingTask != null) {
                hodingTimer.cancel();
                hodingTask.cancel();
            }
            hodingTimer = new Timer();
            hodingTask = new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    hodingHandler.sendMessage(message);
                }
            };
            hodingTimer.schedule(hodingTask, 2000, 2000);
        }
    }

    private void endPlayImage() {
        hodingTask.cancel();
        hodingTimer.cancel();
        ldTV.setControlShowMode("Before", ImageBtnShowMode.visable);
        ldTV.setControlShowMode("After", ImageBtnShowMode.visable);
    }

    @Override
    public void controlBtnSelected(String controlBtnTag) {

        StationInfo currentStation = new StationInfo();
        try {
            if (controlBtnTag.equals(zoominTag)) {
                ldTV.zoomIn();
            } else if (controlBtnTag.equals(zoomOutTag)) {
                ldTV.zoomOut();
            } else if (controlBtnTag.equals(beforeTag)) {
                if (isPlay) {
                    isPlay = false;
                    return;
                }
                currentStation = ldTV.getCurrentStationJuction();

                if (currentStation.isNode()
                        && currentStation.getNodeIsBegin() == 1) {
                    selectNode(currentStation);
                } else {
                    ldTV.findPreImage();
                }
                moveStreet(currentStation);
            } else if (controlBtnTag.equals(afterTag)) {
                if (isPlay) {
                    isPlay = false;
                    return;
                }
                currentStation = ldTV.getCurrentStationJuction();
                if (currentStation.isNode()
                        && currentStation.getNodeIsBegin() == 0) {
                    selectNode(currentStation);
                } else {
                    ldTV.findNextImage();
                }
                moveStreet(currentStation);
            }
        } catch (TrueMapException e) {
            ShowToast(e.getMessage());
        }

    }

    /**
     * 选择路口弹出窗口提示
     *
     * @param currentStation
     *            当前站点信息
     * @throws TrueMapException
     */
    private void selectNode(StationInfo currentStation) throws TrueMapException {
        final SelectDialog selectDialog = new SelectDialog(this,
                android.R.style.Theme_Dialog);// 创建Dialog并设置样式主题
        Window win = selectDialog.getWindow();
        LayoutParams params = new LayoutParams();
        params.x = -80;// 设置x坐标
        params.y = -60;// 设置y坐标
        win.setAttributes(params);
        selectDialog.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
        selectDialog.show();
        selectDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
            }
        });
        LinearLayout nodesLayout = new LinearLayout(this);
        nodesLayout.setOrientation(LinearLayout.VERTICAL);
        final ArrayList<StationInfo> stationInfoList = currentStation
                .getNodeList();
        int length = stationInfoList.size();
        for (int i = 0; i < length; i++) {
            StationInfo stationInfo = stationInfoList.get(i);
            String stationAddress = stationInfo.getAddress();
            double yaw = stationInfo.getYaw();
            String stationId = stationInfo.getStationId();
            String cameraID = ldTV.getCameraID();
            String imgID = TrueVision.getImgIDByStationID(stationId, cameraID);
            LinearLayout nodeLayout = new LinearLayout(this);
            nodeLayout.setTag(imgID);
            nodeLayout.setOrientation(LinearLayout.HORIZONTAL);
            TextView textView = new TextView(this);
            ImageView imageView = new ImageView(this);
            nodeLayout.addView(imageView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.FILL_PARENT);
            nodeLayout.addView(textView);
            String textViewStr = "";
            if (stationAddress != null) {
                textViewStr = "";
                if (yaw <= 30 && yaw >= -30) {
                    textViewStr += "直行";
                    imageView.setImageResource(R.drawable.nogo);
                } else if (yaw > 30 && yaw <= 180) {
                    textViewStr += "右行";
                    imageView.setImageResource(R.drawable.nodegoright);
                } else if (yaw >= -180 && yaw < -30) {
                    textViewStr += "左行";
                    imageView.setImageResource(R.drawable.nodegoleft);
                }
                textViewStr += stationAddress;
            } else {
                textViewStr = "";
            }
            textView.setTextSize(20);
            textView.setTextColor(Color.RED);
            textView.setText(textViewStr);
            nodesLayout.setPadding(20, 20, 20, 20);
            nodesLayout.addView(nodeLayout);
            nodeLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    String imageID = (String) view.getTag();
                    try {
                        ldTV.locImgByImgID(imageID);
                        selectDialog.cancel();
                    } catch (TrueMapException e) {
                        e.printStackTrace();
                    }
                }

            });
            if (i != length - 1) {
                LinearLayout nullLayout = new LinearLayout(this);
                nullLayout.setBackgroundColor(Color.BLACK);
                nodesLayout.addView(nullLayout,
                        LinearLayout.LayoutParams.FILL_PARENT, 2);
            }
        }
        selectDialog.setView(nodesLayout);
    }

    /**
     * @param message
     *            弹出的提示信息
     *
     */
    protected void ShowToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
                .show();
    }

    /**
     *  弹出进度条dialog
     */
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(LiloSplitActivity.this,"", getResources().getString(R.string.querying),
                    true);
            progressDialog.getWindow().setLayout(600, 300);
        } else {
            progressDialog.show();
        }
    }


    public void dismissProgress()
    {
        if(progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }

    public void moveStreet(StationInfo currentStation)
    {
        Coord coord = currentStation.getCoord();
        double yaw = currentStation.getYaw();
        Point2D cp = new Point2D(coord.getLon(), coord.getLat());
        isMapMove = false;
        mapController.setCenter(cp);
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.vista)).getBitmap();
        // 设置旋转角度
        Matrix matrix = new Matrix();
        matrix.setRotate((float) yaw);
        // 重新绘制Bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);
        vistaView.setImageBitmap(bitmap);  
		/*removeOverlay();
		addOverlay(cp);*/
    }

    /**
     * @param message
     *            弹出的提示信息
     *
     */
    protected void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.destroy();
        ldTV.destroyDrawingCache();
    }

}
