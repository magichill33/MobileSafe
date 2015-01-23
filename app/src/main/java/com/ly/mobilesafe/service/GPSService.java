package com.ly.mobilesafe.service;

import java.io.IOException;
import java.io.InputStream;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class GPSService extends Service {

    //用到位置服务
    private LocationManager lm;
    private MyLocationListener listener;

    @Override
    public void onCreate() {
        super.onCreate();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new MyLocationListener();
        //注册监听位置服务
        //给位置提供设置条件
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = lm.getBestProvider(criteria, true);
        lm.requestLocationUpdates(provider, 3000, 0, listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lm.removeUpdates(listener);
        listener = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            String longitude = "lon:" + location.getLongitude() + "\n";
            String latitude = "lat:" + location.getLatitude() + "\n";
            String accuracy = "acc:" + location.getAccuracy() + "\n";

            //把标准的GPS坐标转换成火星坐标
            InputStream ips;
            try {
                ips = getAssets().open("axisoffset.dat");
                ModifyOffset offset = ModifyOffset.getInstance(ips);
                PointDouble pd = offset.s2c(new PointDouble(location.getLongitude(),
                        location.getLatitude()));
                longitude = "lon:" + pd.x + "\n";
                latitude = "lat:" + pd.y + "\n";
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            Editor editor = sp.edit();
            editor.putString("lastlocation", longitude + latitude + accuracy);
            editor.commit();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

    }

}

