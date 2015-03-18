package com.lilo.event;

import com.lilo.sm.LiloMapActivity;
import com.lilo.sm.LiloSplitActivity;
import com.lilo.sm.LiloVistaActivity;
import com.lilo.sm.R;
import com.lilo.util.CaseReportUtil;
import com.lilo.util.DrawRegionUtil;
import com.supermap.android.maps.Point2D;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;

public class ButtonTouchListener implements OnTouchListener {

	public final  float[] BT_SELECTED=new float[]
            { 2, 0, 0, 0, 2,
        0, 2, 0, 0, 2,
        0, 0, 2, 0, 2,
        0, 0, 0, 1, 0 };
	public final float[] BT_NOT_SELECTED=new float[]
            { 1, 0, 0, 0, 0,
        0, 1, 0, 0, 0,
        0, 0, 1, 0, 0,
        0, 0, 0, 1, 0 };
	private ImageButton button;
	private Context context;
	private String gridCodes;
	private CaseReportUtil caseReportUtil;
	
	public ButtonTouchListener(ImageButton btnMap2d,String gridCodes,CaseReportUtil caseReportUtil,Context context) {
		super();
		this.button = btnMap2d;
		this.context = context;
		this.gridCodes = gridCodes;
		this.caseReportUtil = caseReportUtil;
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			view.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
			view.setBackgroundDrawable(view.getBackground());
			button.setAlpha(120);
		}else if(event.getAction() == MotionEvent.ACTION_UP)
		{
			view.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
			view.setBackgroundDrawable(view.getBackground());
			button.setAlpha(255);
			LiloMapActivity activity = (LiloMapActivity) context;
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("cp", caseReportUtil.getDrawRegionUtil().getCenterPoint());
			bundle.putString("gridCodes", gridCodes);
			intent.putExtras(bundle);
			if(button.getId() == R.id.mapSplit)
			{
				intent.setClass(context, LiloSplitActivity.class);
			}else
			{
				intent.setClass(context, LiloVistaActivity.class);
			}
			context.startActivity(intent);
		}
		return false;
	}


}
