package com.lilo.service;

import com.lilo.model.MessageEnum;
import com.lilo.util.DataUtil;
import com.supermap.android.data.GetFeaturesResult;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

/**
 * 获取空间库中空间数据
 * @author Administrator
 *
 */
public class RunQueryDataTask extends AsyncTask<String, Void, GetFeaturesResult>{

    private String url;
    private String dataSet;
    private String condition;
    private Handler handler;

    public RunQueryDataTask(String url, String dataSet, String condition, Handler handler) {
        super();
        this.url = url;
        this.dataSet = dataSet;
        this.condition = condition;
        this.handler = handler;
    }

    @Override
    protected GetFeaturesResult doInBackground(String... params) {

        return DataUtil.excute_geoSQL(url, dataSet, condition);
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