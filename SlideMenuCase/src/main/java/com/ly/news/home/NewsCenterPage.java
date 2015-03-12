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
import com.ly.news.MainActivity;
import com.ly.news.base.BasePage;
import com.ly.news.bean.NewsCenterCategory;
import com.ly.news.utils.GsonUtils;
import com.ly.news.utils.HMApi;

import java.util.ArrayList;
import java.util.List;


public class NewsCenterPage extends BasePage {

    private ArrayList<String> menuNewCenterList = new ArrayList<String>();

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
                        processData(responseInfo.result.toString());
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                    }
                });
    }

    private void processData(String result) {
        NewsCenterCategory category = GsonUtils.jsonToBean(result,
                NewsCenterCategory.class);
        if (category.retcode!= 200){
            return;
        }

        List<NewsCenterCategory.CenterCategory> data = category.data;
        menuNewCenterList.clear();
        for (NewsCenterCategory.CenterCategory cate:data){
            menuNewCenterList.add(cate.title);
        }

        ((MainActivity)ctx).getMenuFragment2().initNewsCenterMenu(menuNewCenterList);
    }
}
