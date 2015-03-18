package com.lilo.util;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKLocationManager;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.Point2D;
public class LocateUtil {

    // 百度MapAPI的管理类
    public BMapManager mBMapMan = null;
    // 授权Key
    // TODO: 请输入您的Key,
    // 申请地址：http://dev.baidu.com/wiki/static/imap/key/
    public String mStrKey = "E3041FEDFA4A24627A4B76539E07658B0FE44A5D";
    private Context context;

    public LocateUtil(Context context) {
        super();
        this.context = context;
    }

    public void initLocationInfo()
    {
        mBMapMan = new BMapManager(context);
        mBMapMan.init(mStrKey, new MyGeneralListener(context));
        mBMapMan.getLocationManager().setLocationCoordinateType(MKLocationManager.MK_COORDINATE_WGS84);
        mBMapMan.start();
    }


    public Point2D locateMap(MapView map)
    {
        MKLocationManager manager = mBMapMan.getLocationManager();
        Location location =	manager.getLocationInfo();
        Point2D point = null;
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            point = new Point2D(longitude, latitude);
            //map.centerAt(point, true);
            map.getController().setCenter(point);
        }
        return point;
    }

    public Location getLocation()
    {
        MKLocationManager manager = mBMapMan.getLocationManager();
        Location location =	manager.getLocationInfo();
        return location;
    }

    // 常用事件监听，用来处理通常的网络错误，授权验证错误等
    class MyGeneralListener implements MKGeneralListener {
        private Context cxt;



        public MyGeneralListener(Context cxt) {
            super();
            this.cxt = cxt;
        }

        public void onGetNetworkState(int iError) {
            Toast.makeText(cxt, "您的网络出错啦！",
                    Toast.LENGTH_LONG).show();
        }

        public void onGetPermissionState(int iError) {
            if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
                // 授权Key错误：
                Toast.makeText(cxt,
                        "请在BMapApiDemoApp.java文件输入正确的授权Key！", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }
}
