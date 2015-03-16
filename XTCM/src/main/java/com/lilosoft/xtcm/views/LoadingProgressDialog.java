package com.lilosoft.xtcm.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.KeyEvent;

//自定义Dialog，禁用BACK按钮
public class LoadingProgressDialog extends ProgressDialog{

	public LoadingProgressDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}

}
