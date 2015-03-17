package com.lilosoft.xtcm.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKLocationManager;
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
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.ags.query.QueryTask;
import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.instantiation.Part;
import com.lilosoft.xtcm.utils.CenterPointUtil;
import com.lilosoft.xtcm.utils.LogFactory;

/**
 * 用来进行部件信息采集的activity
 *
 * @author ly
 *
 */
public class GatherPartsActivity extends Activity {

    private static final String TAG = "GatherPartsActivity";
    protected final int getAddSuccess = 0;
    protected final int getAddFail = 1;
    // 百度MapAPI的管理类
    public BMapManager mBMapMan = null;
    // 授权Key
    // TODO: 请输入您的Key,
    // 申请地址：http://dev.baidu.com/wiki/static/imap/key/
    public String mStrKey = "E3041FEDFA4A24627A4B76539E07658B0FE44A5D";
    protected double latitude;
    protected double longitude;
    private MapView map;
    // private PopupContainer popupContainer;
    private PopupDialog popupDialog;
    private ProgressDialog progressDialog;
    private AtomicInteger count;
    // private String categoryId = "01";
    // private String layerId = "01"; //大类ID
    private GraphicsLayer filterLayer = new GraphicsLayer();
    private ImageButton gpsButton;
    // private LocationManager locManager;
    // 定位相关
    private GraphicsLayer gpsLayer = new GraphicsLayer();
    private Drawable locationAble;
    private ImageView iv_mark;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partsmap);
        // ArcGISRuntime.setClientId("1eFHW78avlnRUPHm");
        final GraphicsLayer partsLayer = new GraphicsLayer();
        map = (MapView) findViewById(R.id.map);

        iv_mark = (ImageView) findViewById(R.id.iv_mark);

        gpsButton = (ImageButton) findViewById(R.id.btnLocate);
        // locManager = (LocationManager)
        // getSystemService(Context.LOCATION_SERVICE);
        Intent intent = getIntent();
        Bundle bundler = intent.getExtras();
//		String partId = bundler.getString("partId");
//		String categoryId = bundler.getString("categoryId");
        String code = bundler.getString("CODE");
        String layer = bundler.getString("LAYER");
        locationAble = getResources().getDrawable(R.drawable.location);
        mBMapMan = new BMapManager(GatherPartsActivity.this);
        mBMapMan.init(mStrKey, new MyGeneralListener());
        mBMapMan.getLocationManager().setLocationCoordinateType(
                MKLocationManager.MK_COORDINATE_WGS84);
        mBMapMan.start();

        ArcGISTiledMapServiceLayer tileLayer = new ArcGISTiledMapServiceLayer(
                getResources().getString(R.string.map2d));
        // XianTaoWMTSLayer tileLayer = new XianTaoWMTSLayer();
        // GoogleMapServiceLayer tileLayer = new GoogleMapServiceLayer();
        map.addLayer(tileLayer);
        map.addLayer(partsLayer);
        map.addLayer(filterLayer);
        map.addLayer(gpsLayer);
        new Thread(new Runnable() {
            public void run() {

                locateMap(
                        GatherPartsActivity.this.getResources().getString(
                                R.string.cm_grid), map);
            }
        }).start();
        final String url = getResources().getString(R.string.cm_parts) + "/"
                + Integer.valueOf(layer);
        LogFactory.e(TAG, url);
        final String picName = "p" + code;
        map.setOnSingleTapListener(new OnSingleTapListener() {
            private static final long serialVersionUID = 1L;

            public void onSingleTap(float x, float y) {
                if (map.isLoaded()) {
                    map.centerAt(map.toMapPoint(x, y), true);
                    if (map.getResolution() > map.getMinResolution()) {
                        // Log.v("ly", Integer.valueOf(LayerId)+"");
                        Log.v("ly",
                                "res:" + map.getResolution() + "--"
                                        + map.getMinResolution());
                        new AlertDialog.Builder(GatherPartsActivity.this)
                                .setTitle("提示").setMessage("请将地图放到最大层显示部件")
                                .setPositiveButton("确定", null).show();
                    } else {
                        count = new AtomicInteger();
                        if (progressDialog == null
                                || !progressDialog.isShowing()) {
                            progressDialog = ProgressDialog.show(
                                    map.getContext(), "", "查询...");
                            progressDialog.getWindow().setLayout(400, 200);
                        }

                        int tolerance = 500;

                        Envelope env = new Envelope(map.toMapPoint(x, y),
                                tolerance * map.getResolution(), tolerance
                                * map.getResolution());
                        new RunQueryPartsLayerTask(partsLayer, env, picName,
                                map.getContext(), x, y).execute(url);
                    }

                }
            }
        });

        // bMapManager.getLocationManager().requestLocationUpdates(mLocationListener);
        // bMapManager.getLocationManager().setLocationCoordinateType(MKLocationManager.MK_COORDINATE_WGS84);
        // bMapManager.start();

        gpsButton.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundColor(0xcc818d89);
                    gpsLayer.removeAll();
                    MKLocationManager manager = mBMapMan.getLocationManager();
                    Location location = manager.getLocationInfo();
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        PictureMarkerSymbol picSymbol = new PictureMarkerSymbol(
                                locationAble);
                        Point point = new Point(longitude, latitude);
                        Graphic graphic = new Graphic(point, picSymbol);
                        gpsLayer.addGraphic(graphic);
                        // map.centerAt(point, true);
                        map.zoomToResolution(point, map.getMinResolution());
                    }
                    Log.i("百度地图轨迹是:", latitude + "---" + longitude);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // 再修改为抬起时的正常图片
                    v.setBackgroundColor(0xccffffff);
                }

                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.unpause();
    }

    /**
     * 初始化地图时将定位在一个合适的位置
     */
    private void locateMap(String url, MapView mapView) {
        Query query = new Query();
        query.setReturnGeometry(true);
        query.setWhere("1=1");
        // query.setOutFields(new String[]{"*"});
        QueryTask queryTask = new QueryTask(url);
        try {
            FeatureSet fs = queryTask.execute(query);
            if (fs != null) {
                Graphic[] graphics = fs.getGraphics();
                // Polygon polygon = null;

                ArrayList<Envelope> enveList = new ArrayList<Envelope>();

                for (int i = 0; i < graphics.length; i++) {
                    if (graphics[i] != null
                            && graphics[i].getGeometry() != null) {

                        Geometry geometry = graphics[i].getGeometry();

                        if (Geometry.Type.POLYGON == geometry.getType()) {

                            Envelope envelope = new Envelope();
                            geometry.queryEnvelope(envelope);
                            // polygon = (Polygon) geometry;
                            // polygon.queryEnvelope(envelope);
                            enveList.add(envelope);

                        }
                    }
                }

                mapView.setExtent(CenterPointUtil.getEnvelope(enveList));
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("DrawError", e.getMessage());
            e.printStackTrace();
        }
    }

    private void createPopupViews(Graphic[] graphics, String picName,
                                  ListView listParts, Context context) {

        int imgId = GatherPartsActivity.this.getResources().getIdentifier(
                picName, "drawable", this.getPackageName());
        Drawable iconPart = GatherPartsActivity.this.getResources()
                .getDrawable(imgId);
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (Graphic graphic : graphics) {
            Map<String, Object> listMap = new HashMap<String, Object>();
            listMap.put("image", iconPart);
            listMap.put("addr", graphic
                    .getAttributeValue((String) GatherPartsActivity.this
                            .getResources().getText(R.string.objCode)));
            listMap.put("graphic", graphic);
            listItems.add(listMap);
        }

        ListViewAdapter listViewAdapter = new ListViewAdapter(context,
                listItems);
        listParts.setAdapter(listViewAdapter);

    }

    /**
     * 计算标题栏的高度
     *
     * @return
     */
    private int initHeight() {
        Rect rect = new Rect();
        Window window = getWindow();
        map.getWindowVisibleDisplayFrame(rect);
        // 状态栏的高度
        int statusBarHight = rect.top;
        // 标题栏跟状态栏的总体高度
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT)
                .getTop();
        // 标题栏的高度
        int titleBarHeight = contentViewTop - statusBarHight;
        return titleBarHeight;
    }

    // 查询部件信息将其显示再图层上
    private class RunQueryPartsLayerTask extends
            AsyncTask<String, Void, FeatureSet> {

        private GraphicsLayer graphicsLayer;
        private Envelope envelope;
        private String picName;
        private Context context;
        private float xScreen;
        private float yScreen;
        private PopupDialog popupDialog;

        public RunQueryPartsLayerTask(GraphicsLayer graphicsLayer,
                                      Envelope env, String picName, Context context, float x, float y) {
            super();
            this.graphicsLayer = graphicsLayer;
            envelope = env;
            this.picName = picName;
            this.context = context;
            xScreen = x;
            yScreen = y;
        }

        private void clearLayer() {
            filterLayer.removeAll();
            gpsLayer.removeAll();
        }

        @Override
        protected FeatureSet doInBackground(String... urls) {
            for (String url : urls) {
                Query query = new Query();
                query.setReturnGeometry(true);
                query.setGeometry(envelope);
                // query.setWhere("1=1");
                // query.setMaxFeatures(10);
                query.setOutFields(new String[] { "*" });
                QueryTask queryTask = new QueryTask(url);
                try {
                    graphicsLayer.removeAll();
                    // filterLayer.removeAll();
                    clearLayer();
                    FeatureSet fs = queryTask.execute(query);
                    if (fs != null && fs.getGraphics().length > 0) {
                        Graphic[] graphics = fs.getGraphics();
                        Polygon polygon = null;

                        FeatureSet featureSet = new FeatureSet();
                        List<Graphic> list = new ArrayList<Graphic>();

                        int imgId = 0;
                        try {
                            imgId = GatherPartsActivity.this.getResources()
                                    .getIdentifier(picName, "drawable",
                                            GatherPartsActivity.this.getPackageName());
                            LogFactory.e(TAG, "imgID = "+imgId);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            LogFactory.e(TAG, "加载资源id出错");
                            e.printStackTrace();
                        }

                        for (int i = 0; i < graphics.length; i++) {
                            Geometry geometry = graphics[i].getGeometry();

                            Drawable drawable = GatherPartsActivity.this
                                    .getResources().getDrawable(imgId);
                            PictureMarkerSymbol picSymbol = new PictureMarkerSymbol(
                                    drawable);
                            // PictureDrawable drawable = new
                            // PictureDrawable(picture)
                            Graphic graphic = new Graphic(geometry, picSymbol);
                            graphicsLayer.addGraphic(graphic);
                            // graphic.setAttributes(graphics[i].getAttributes());
                            list.add(graphic);
                        }
                        int len = list.size();
                        Graphic[] gc = new Graphic[len];
                        for (int i = 0; i < len; i++) {
                            gc[i] = list.get(i);
                        }

                        featureSet.setGraphics(gc);
                        return fs;

                    }

                } catch (Exception e) {
                    Log.e("ly", e.getMessage());
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final FeatureSet result) {
            count.decrementAndGet();
            if (result == null) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                new AlertDialog.Builder(context).setTitle("提示")
                        .setMessage("此区域范围内没有部件信息")
                        .setPositiveButton("确定", null).show();
                return;
            }
            Graphic[] graphics = result.getGraphics();
            if (graphics == null || graphics.length == 0) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();

                return;
            }

            if (graphics.length > 0) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Log.v("ly", "length:" + graphics.length);
                popupDialog = new PopupDialog(context, graphics,
                        picName);
                Window win = popupDialog.getWindow();
                DisplayMetrics metric = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metric);
                int width = metric.widthPixels; // 屏幕宽度（像素）
                int height = metric.heightPixels; // 屏幕高度（像素）
                // android.view.WindowManager.LayoutParams lp = new
                // android.view.WindowManager.LayoutParams();
                WindowManager.LayoutParams wlp = win.getAttributes();
                win.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                // lp.x = width/2 - wlp.width/2;
                // lp.y = -170;
                // lp.x = 150;
                wlp.y = height / 2 - initHeight();
                win.setAttributes(wlp);
                // win.setGravity(Gravity.CENTER_VERTICAL);
                popupDialog.setCanceledOnTouchOutside(true); // 设置点击Dialog外部任意区域关闭Dialog
                popupDialog.setOnDismissListener(new OnDismissListener() {

                    public void onDismiss(DialogInterface dialog) {
                        iv_mark.setVisibility(View.GONE);
                    }
                });
                popupDialog.show();

                return;
            }

        }

    }

    /**
     * 弹出对话框类
     *
     * @author ly
     *
     */
    private class PopupDialog extends Dialog {
        private Graphic[] graphics;
        private String picName;
        private Context context;

        public PopupDialog(Context context, Graphic[] graphics, String picName) {
            super(context, R.style.PopupDialog);

            this.graphics = graphics;
            this.picName = picName;
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.setContentView(R.layout.partsitem);
            ListView listView = (ListView) this.findViewById(R.id.listParts);
            createPopupViews(graphics, picName, listView, context);
        }

    }

    /**
     * list适配器类
     *
     * @author ly
     *
     */
    private class ListViewAdapter extends BaseAdapter {

        private Context context;
        private List<Map<String, Object>> listItems;

        private CheckBox selectedCheckBox = null;

        public ListViewAdapter(Context context,
                               List<Map<String, Object>> listItems) {
            super();
            this.context = context;
            this.listItems = listItems;
        }

        public int getCount() {
            return listItems.size();
        }

        public Object getItem(int position) {
            return listItems.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        /**
         * ListView item设置
         */

        public View getView(int position, View convertView, ViewGroup parent) {
            final int selectID = position;
            ListItemView listItemView = null;

            if (convertView == null) {
                listItemView = new ListItemView();
                convertView = View.inflate(GatherPartsActivity.this, R.layout.list_item, null);
                listItemView.image = (ImageView) convertView
                        .findViewById(R.id.imageItem);
                listItemView.address = (TextView) convertView
                        .findViewById(R.id.addrItem);
                listItemView.btnPick = (Button) convertView
                        .findViewById(R.id.pickItem);
                listItemView.radionButton = (CheckBox) convertView
                        .findViewById(R.id.radioItem);
                // 设置控件集到convertView
                convertView.setTag(listItemView);
            } else {
                listItemView = (ListItemView) convertView.getTag();
            }

            final CheckBox cb = listItemView.radionButton;
            final Graphic graphic = (Graphic) listItems.get(position).get(
                    "graphic");
            final Drawable drawable = (Drawable) listItems.get(position).get(
                    "image");

            listItemView.image.setBackgroundDrawable((Drawable) listItems.get(
                    position).get("image"));
            listItemView.address.setText(listItems.get(position).get("addr").toString());

            listItemView.btnPick.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    postPartsData(graphic);
                }
            });

            listItemView.radionButton
                    .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {

                            if (selectedCheckBox != null
                                    && selectedCheckBox != cb) {
                                selectedCheckBox.setChecked(false);
                            }
                            selectedCheckBox = cb;
                            filterLayer.removeAll();
                            if (isChecked) {
								/*
								 * drawable.setColorFilter(Color.YELLOW,android.
								 * graphics.PorterDuff.Mode.MULTIPLY);
								 * PictureMarkerSymbol picSymbol = new
								 * PictureMarkerSymbol(drawable);
								 *
								 * Graphic g = new
								 * Graphic(graphic.getGeometry(), picSymbol);
								 * filterLayer.addGraphic(g);
								 */
								/*
								 * PictureMarkerSymbol picSymbol =
								 * (PictureMarkerSymbol) graphic.getSymbol();
								 * Drawable drawable = picSymbol.getDrawable();
								 * drawable.setAlpha(55);
								 */

                                map.centerAt((Point) graphic.getGeometry(),
                                        true);
                                iv_mark.setVisibility(View.VISIBLE);
                            }
                        }
                    });

            return convertView;
        }

        /**
         * 提交采集的部件数据
         */
        private void postPartsData(Graphic g) {
            Map<String, Object> attr = g.getAttributes();
//			Part part = new Part();
//			// attr.get(getAttrName(R.string.objectId));
//			part.setObjectId(attr.get(getAttrName(R.string.objectId))
//					.toString());
//			part.setObjCode(attr.get(getAttrName(R.string.objCode)).toString());
//			part.setObjName(attr.get(getAttrName(R.string.objName)).toString());
//			part.setOrgcode(attr.get(getAttrName(R.string.orgCode)).toString());
//			part.setDeptCode1(attr.get(getAttrName(R.string.deptCode1))
//					.toString());
//			part.setDeptName1(attr.get(getAttrName(R.string.deptName1))
//					.toString());
//			part.setDeptCode2(attr.get(getAttrName(R.string.deptCode2))
//					.toString());
//			part.setDeptName2(attr.get(getAttrName(R.string.deptName2))
//					.toString());
//			part.setDeptCode3(attr.get(getAttrName(R.string.deptCode3))
//					.toString());
//			part.setDeptName3(attr.get(getAttrName(R.string.deptName3))
//					.toString());
            Point point = (Point) g.getGeometry();
//			part.setLon(point.getX());
//			part.setLat(point.getY());
//			Log.v("ly", part.toString());

            Intent data = new Intent();

            data.putExtra(getResources().getString(R.string.objectId), attr.get(getAttrName(R.string.objectId))
                    .toString());
            data.putExtra(getResources().getString(R.string.objCode), attr.get(getAttrName(R.string.objCode))
                    .toString());
            data.putExtra(getResources().getString(R.string.objName), attr.get(getAttrName(R.string.objName))
                    .toString());
            data.putExtra(getResources().getString(R.string.orgCode), attr.get(getAttrName(R.string.orgCode))
                    .toString());
            data.putExtra(getResources().getString(R.string.deptCode1), attr.get(getAttrName(R.string.deptCode1))
                    .toString());
            data.putExtra(getResources().getString(R.string.deptName1), attr.get(getAttrName(R.string.deptName1))
                    .toString());
            data.putExtra(getResources().getString(R.string.deptCode2), attr.get(getAttrName(R.string.deptCode2))
                    .toString());
            data.putExtra(getResources().getString(R.string.deptName2), attr.get(getAttrName(R.string.deptName2))
                    .toString());
            data.putExtra(getResources().getString(R.string.deptCode3), attr.get(getAttrName(R.string.deptCode3))
                    .toString());
            data.putExtra(getResources().getString(R.string.deptName3), attr.get(getAttrName(R.string.deptName3))
                    .toString());
            data.putExtra("Lon",point.getX()+"");
            data.putExtra("Lat",point.getY()+"");

            setResult(Activity.RESULT_OK, data);
            finish();
        }

        private String getAttrName(int id) {
            return (String) GatherPartsActivity.this.getResources().getText(id);
        }

        public final class ListItemView {
            public ImageView image;
            public TextView address;
            public Button btnPick;
            public CheckBox radionButton;
        }

    }

    // 常用事件监听，用来处理通常的网络错误，授权验证错误等
    class MyGeneralListener implements MKGeneralListener {
        public void onGetNetworkState(int iError) {
            Toast.makeText(GatherPartsActivity.this, "您的网络出错啦！",
                    Toast.LENGTH_LONG).show();
        }

        public void onGetPermissionState(int iError) {
            if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
                // 授权Key错误：
                Toast.makeText(GatherPartsActivity.this,
                        "请在BMapApiDemoApp.java文件输入正确的授权Key！", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

}