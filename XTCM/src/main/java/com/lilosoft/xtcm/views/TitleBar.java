package com.lilosoft.xtcm.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lilosoft.xtcm.R;

/**
 * @ClassName TitleBar
 * @category TitleBar
 * @author William Liu
 * 
 */
public class TitleBar extends RelativeLayout implements IViewStyle {

	public Button leftBtn;
	public TextView centerTextView;
	public Button rightBtn;
	public TextView tit_username;
	@SuppressWarnings("unused")
	private int mStyle = 0x000000F0;
	// ======================================
	private Context mContext;
	private LayoutInflater mInflater;
	@SuppressWarnings("unused")
	private Resources mRes;

	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TitleBar(Context context) {
		super(context);
		init(context);
	}

	public TitleBar(Context context, int mStyle) {
		super(context);
		this.mStyle = mStyle;
		init(context);
	}

	@Override
	public void changeStyle(int style) {
		getViewByInflater(style);
	}

	private void init(Context context) {
		this.mContext = context;
		mRes = getResources();
		mInflater = LayoutInflater.from(mContext);
		// getViewByInflater(STYLE.TWO_BTN_AND_TITLE);
	}

	private void getViewByInflater(int style) {

		switch (style) {
		case STYLE.TWO_BTN_AND_TITLE: {
			buildTwoBtnAndTitle_Style();
			break;
		}
		case STYLE.BACK_BTN_AND_TITLE: {
			buildBackBtnAndTitle_Style();
			break;
		}
		default:
			buildNotBtnAndTitle_Style();
			break;
		}

	}

	private void buildTwoBtnAndTitle_Style() {
		mInflater.inflate(R.layout.view_titlebar_two_btn_and_title, this);
		leftBtn = (Button) findViewById(R.id.btn_left);
		rightBtn = (Button) findViewById(R.id.btn_right);
		centerTextView = (TextView) findViewById(R.id.tv_center);
		tit_username = (TextView) findViewById(R.id.tit_username);
	}

	private void buildBackBtnAndTitle_Style() {
		mInflater.inflate(R.layout.view_titlebar_two_btn_and_title, this);
		leftBtn = (Button) findViewById(R.id.btn_left);
		rightBtn = (Button) findViewById(R.id.btn_right);
		centerTextView = (TextView) findViewById(R.id.tv_center);
		rightBtn.setVisibility(View.INVISIBLE);
		tit_username = (TextView) findViewById(R.id.tit_username);
	}

	private void buildNotBtnAndTitle_Style() {
		mInflater.inflate(R.layout.view_titlebar_two_btn_and_title, this);
		leftBtn = (Button) findViewById(R.id.btn_left);
		rightBtn = (Button) findViewById(R.id.btn_right);
		centerTextView = (TextView) findViewById(R.id.tv_center);
		leftBtn.setVisibility(View.INVISIBLE);
		rightBtn.setVisibility(View.INVISIBLE);
		tit_username = (TextView) findViewById(R.id.tit_username);
	}

	public interface STYLE {

		int NOT_BTN_AND_TITLE = 0x000000F0;

		int BACK_BTN_AND_TITLE = 0x000000F4;

		int TWO_BTN_AND_TITLE = 0x000000F8;
	}

}
