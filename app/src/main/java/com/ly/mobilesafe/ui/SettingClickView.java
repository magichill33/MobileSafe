package com.ly.mobilesafe.ui;

import com.ly.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingClickView extends RelativeLayout {
	
	private TextView tv_desc;
	private TextView tv_title;
	private String desc_on;
	private String desc_off;
	
	private void initView(Context context)
	{
		View.inflate(context, R.layout.setting_click_view, SettingClickView.this);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}
	
	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.ly.mobilesafe", "ly_title");
		setTitle(title);
		desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.ly.mobilesafe", "desc_on");
		desc_off = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.ly.mobilesafe", "desc_off");
		setDesc(desc_off);
	}

	public SettingClickView(Context context) {
		super(context);
		initView(context);
	}
	
	
	/**
	 * 设置标题
	 * @param text
	 */
	public void setTitle(String text)
	{
		tv_title.setText(text);
	}
	
	/**
	 * 
	 */
	public void setDesc(String desc)
	{
		tv_desc.setText(desc);
	}
}
