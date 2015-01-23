package com.ly.mobilesafe.ui;

import com.ly.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {
	
	private CheckBox cb_status;
	private TextView tv_desc;
	private TextView tv_title;
	private String desc_on;
	private String desc_off;
	
	private void initView(Context context)
	{
		View.inflate(context, R.layout.setting_item_view, SettingItemView.this);
		cb_status = (CheckBox) findViewById(R.id.cb_status);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}
	
	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.ly.mobilesafe", "ly_title");
		setTitle(title);
		desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.ly.mobilesafe", "desc_on");
		desc_off = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.ly.mobilesafe", "desc_off");
		setDesc(desc_off);
	}

	public SettingItemView(Context context) {
		super(context);
		initView(context);
	}
	
	/**
	 * 判断是否选中
	 */
	public boolean isChecked()
	{
		return cb_status.isChecked();
	}
	
	/**
	 * 设置选中状态
	 */
	public void setChecked(boolean checked)
	{
		cb_status.setChecked(checked);
		if(checked)
		{
			tv_desc.setText(desc_on);
		}else{
			tv_desc.setText(desc_off);
		}
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
