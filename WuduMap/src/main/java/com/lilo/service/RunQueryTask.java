package com.lilo.service;

import com.lilo.model.MessageEnum;
import com.supermap.android.data.GetFeaturesResult;
import com.supermap.android.maps.Point2D;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public abstract class RunQueryTask extends AsyncTask<Point2D, Void, GetFeaturesResult> {
	
	private Handler handler;
	
	public RunQueryTask(Handler handler) {
		super();
		this.handler = handler;
	}

	@Override
	protected void onPostExecute(GetFeaturesResult result) {
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
