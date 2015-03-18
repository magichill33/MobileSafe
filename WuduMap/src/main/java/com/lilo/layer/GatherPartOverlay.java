package com.lilo.layer;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;

import com.lilo.service.RunQueryByBuffer;
import com.lilo.sm.R;
import com.lilo.widget.TipForm;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.Point2D;

public class GatherPartOverlay extends TouchGridOverlay {

	private Context context;
	private MapView mapView;
	private String url;
	private String dataSet;
	
	public GatherPartOverlay(Paint polygonPaint, Context context,String url,String dataSet,
			Handler handler) {
		super(polygonPaint, context, handler);
		this.context = context;
		this.url = url;
		this.dataSet = dataSet;
	}

	@Override
	public void queryDataByPoint(Point2D touchPoint, Handler handler) {
		TipForm.newInstance().showProgressDialog(context);
  		new RunQueryByBuffer(url, dataSet,
  				handler).execute(touchPoint);
	}
	
	

}
