package com.ly.news.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.ly.news.base.BasePage;
import com.ly.news.utils.HMApi;


public class NewsCenterPage extends BasePage {

	public NewsCenterPage(Context ct) {
		super(ct);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		TextView textView = new TextView(ctx);
		textView.setText("我是新闻中心");
		return textView;
	}

	@Override
	public void initData() {
        testGet();
	}

    private void testGet(){
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, HMApi.NEWS_CENTER_CATEGORIES,
                new RequestCallBack<Object>() {
                    @Override
                    public void onSuccess(ResponseInfo<Object> responseInfo) {
                        LogUtils.d(responseInfo.result.toString());
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                    }
                });
    }
}
