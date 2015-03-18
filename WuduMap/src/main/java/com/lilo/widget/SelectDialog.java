package com.lilo.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class SelectDialog extends AlertDialog {

	Context mcontext;
	LinearLayout mainLayout;

	public SelectDialog(Context context, int theme) {
		super(context, theme);
		mcontext = context;
	}

	public SelectDialog(Context context) {
		super(context);
		mcontext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainLayout = new LinearLayout(mcontext);
		LinearLayout.LayoutParams parmas = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		setContentView(mainLayout, parmas);
	}

	public void setView(View view) {
		mainLayout.addView(view, LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);

	}
}
