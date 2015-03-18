package com.lilo.event;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lilo.model.MessageEnum;
import com.lilo.sm.LiloSplitActivity;
import com.lilo.util.DataUtil;
import com.supermap.android.data.GetFeaturesResult;
import com.supermap.android.maps.DefaultItemizedOverlay;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.MapView.MapViewEventListener;
import com.supermap.android.maps.Point2D;
import com.supermap.services.components.commontypes.Geometry;
import com.supermap.services.components.commontypes.GeometryType;

public class LiloMapViewEventListener implements MapViewEventListener {
	
	private String url;
	private String dataset;
	private Handler handler;
	//private DefaultItemizedOverlay streetOverlay;
	private Context context;
	
	//private Boolean isMapMove = false;
	
	public LiloMapViewEventListener(String url, String dataset, Handler handler, DefaultItemizedOverlay overlay, Context context) {
		super();
		this.url = url;
		this.dataset = dataset;
		this.handler = handler;
		//this.streetOverlay = streetOverlay;
		this.context = context;
	}

	@Override
	public void longTouch(MapView mapView) {
		Log.v("ly", "longTouch");
	}

	@Override
	public void mapLoaded(MapView mapView) {
		Log.v("ly", "maploaded");
	}

	@Override
	public void move(MapView mapView) {
		Log.v("ly", "move");
	}

	@Override
	public void moveEnd(MapView mapView) {
		Log.v("ly", "moveEnd");
		
		LiloSplitActivity activity = (LiloSplitActivity) context;
		if(activity.isMapMove)
		{
			queryData(mapView);
		}
		activity.isMapMove = true;
	}

	@Override
	public void moveStart(MapView mapView) {
		Log.v("ly", "moveStart");
		
		LiloSplitActivity activity = (LiloSplitActivity) context;
		if(activity.isMapMove)
		{
			activity.showProgressDialog();
		    
			new Timer().schedule(new TimerTask() {
				
				@Override
				public void run() {
					LiloSplitActivity activity = (LiloSplitActivity) context;
					activity.dismissProgress();
				}
			}, 1000*15);
		}
	}

	@Override
	public void touch(MapView mapView) {
		Log.v("ly", "touch");
		//isMapMove = true;
	}

	@Override
	public void zoomEnd(MapView mapView) {
		Log.v("ly", "zoomEnd");
		
	}

	@Override
	public void zoomStart(MapView mapView) {
		Log.v("ly", "zoomStart");
	}

	protected void queryData(MapView mapView)
	{
		Point2D cp = mapView.getCenter();
		final LiloSplitActivity activity = (LiloSplitActivity) context;

	    Geometry geometry = new Geometry();
        com.supermap.services.components.commontypes.Point2D[] points = new com.supermap.services.components.commontypes.Point2D[] { new com.supermap.services.components.commontypes.Point2D(
                 cp.x, cp.y) };
        geometry.points = points;
        geometry.type = GeometryType.POINT;
        GetFeaturesResult result = DataUtil.excute_bufferQuery(url,dataset,geometry,0.0002);
        Message msg = new Message();
        if (result != null) {
            msg.obj = result;
            msg.what = MessageEnum.QUERY_SUCCESS;
        } else {
            msg.what = MessageEnum.QUERY_FAILED;
        }
        handler.sendMessage(msg);
	}
	
}
