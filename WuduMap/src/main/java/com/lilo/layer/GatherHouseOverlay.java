package com.lilo.layer;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;

import com.lilo.service.RunQueryByPoint;
import com.lilo.sm.R;
import com.lilo.util.CaseReportUtil;
import com.lilo.widget.TipForm;
import com.supermap.android.maps.DefaultItemizedOverlay;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.Point2D;

public class GatherHouseOverlay extends TouchGridOverlay {

	private Context context;
	private MapView mapView;
	private String url;
	private String dataSet;
	
	public GatherHouseOverlay(Paint polygonPaint, Context context,String url,String dataSet,
			Handler handler) {
		super(polygonPaint, context, handler);
		this.context = context;
		this.url = url;
		this.dataSet = dataSet;
	}

	@Override
	public void queryDataByPoint(Point2D touchPoint, Handler handler) {

		queryHouseByPoint(url, dataSet, touchPoint, handler);
	}
	
	protected void queryHouseByPoint(String url,String sm_house,Point2D touchPoint,Handler touchHouseHandler)
	{
		TipForm tipForm = TipForm.newInstance();
        tipForm.showProgressDialog(context);
		new RunQueryByPoint(url, sm_house,touchHouseHandler).execute(touchPoint);
	}

}
