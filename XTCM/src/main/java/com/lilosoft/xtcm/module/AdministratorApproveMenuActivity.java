package com.lilosoft.xtcm.module;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.NormalBaseActivity;
import com.lilosoft.xtcm.views.FunctionItem;
import com.lilosoft.xtcm.views.TitleBar;
import com.lilosoft.xtcm.views.TitleBar.STYLE;

/**
 * 行政审批GridView
 *
 * @author yzy
 *
 */
public class AdministratorApproveMenuActivity extends NormalBaseActivity
        implements OnClickListener {

    private TitleBar mTitleBar;
    private FunctionItem functionItem;
    private FunctionItem functionItem1;
    private FunctionItem functionItem2;
    private FunctionItem functionItem3;

    @Override
    protected void installViews() {
        setContentView(R.layout.activity_approve_menu);
        initTitleBar();
        setViewsSize();
        functionItem = (FunctionItem) findViewById(R.id.f_h_sb);
        functionItem1 = (FunctionItem) findViewById(R.id.f_h_hc);
        functionItem2 = (FunctionItem) findViewById(R.id.f_h_hs);
        functionItem3 = (FunctionItem) findViewById(R.id.f_h_bl);
        functionItem3.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void registerEvents() {
        functionItem.setOnClickListener(this);
        functionItem1.setOnClickListener(this);
        functionItem2.setOnClickListener(this);
        functionItem3.setOnClickListener(this);
    }

    /**
     * @category 初始化titleBar
     */
    protected void initTitleBar() {
        mTitleBar = (TitleBar) findViewById(R.id.titlebar);
        mTitleBar.changeStyle(STYLE.NOT_BTN_AND_TITLE);
        mTitleBar.centerTextView.setText(R.string.function_xzsp);
    }

    private void setViewsSize() {

        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();

        int topPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 130, getResources()
                        .getDisplayMetrics());

        int layoutHeight = (dm.heightPixels - topPx) / 4;

        LinearLayout functionLayout = (LinearLayout) findViewById(R.id.home_layout);
        LinearLayout functionLayout1 = (LinearLayout) findViewById(R.id.home_layout1);
        LinearLayout functionLayout2 = (LinearLayout) findViewById(R.id.home_layout2);
        LinearLayout functionLayout3 = (LinearLayout) findViewById(R.id.home_layout3);

        functionLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, layoutHeight));
        functionLayout1.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, layoutHeight));
        functionLayout2.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, layoutHeight));
        functionLayout3.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, layoutHeight));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 审批
             */
            case R.id.f_h_sb:
                startActivity(new Intent(mContext,
                        AdministratorApproveListActivity.class).putExtra(
                        "searchType", 0));
                break;
            /**
             * 投诉
             */
            case R.id.f_h_hc:
                startActivity(new Intent(mContext,
                        AdministratorApproveListActivity.class).putExtra(
                        "searchType", 1));
                break;
            /**
             * 通知
             */
            case R.id.f_h_hs:
                startActivity(new Intent(mContext,
                        AdministratorApproveListActivity.class).putExtra(
                        "searchType", 2));
                break;
        }
    }
}
