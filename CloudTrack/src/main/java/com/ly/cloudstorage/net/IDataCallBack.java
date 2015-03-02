package com.ly.cloudstorage.net;


public interface IDataCallBack {

	public void handleServiceResult(int requestCode, int errCode, Object data);
}