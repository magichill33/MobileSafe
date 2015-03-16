package cn.ithm.kmplayer1.bean;

import java.util.Map;

import cn.ithm.kmplayer1.util.OnResultListener;

/**
 * 参数封装
 */
public class Params {
	public OnResultListener listener;
	public boolean isShowProgress = true;
	public String url;
	public Map<String ,String> params;
}
