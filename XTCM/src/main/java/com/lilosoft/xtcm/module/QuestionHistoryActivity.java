package com.lilosoft.xtcm.module;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.NormalBaseActivity;
import com.lilosoft.xtcm.constant.Limit;
import com.lilosoft.xtcm.instantiation.User;
import com.lilosoft.xtcm.views.FunctionItem;
import com.lilosoft.xtcm.views.TitleBar;
import com.lilosoft.xtcm.views.TitleBar.STYLE;

/**
 * @category 历史
 * @author William Liu
 *
 */
public class QuestionHistoryActivity extends NormalBaseActivity implements
        OnClickListener {

    private TitleBar mTitleBar;
    private FunctionItem functionItem;
    private FunctionItem functionItem1;
    private FunctionItem functionItem2;
    private FunctionItem functionItem3;

    @Override
    protected void installViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_question_history);
        initTitleBar();
        setViewsSize();
        functionItem = (FunctionItem) findViewById(R.id.f_h_sb);
        functionItem1 = (FunctionItem) findViewById(R.id.f_h_hc);
        functionItem2 = (FunctionItem) findViewById(R.id.f_h_hs);
        functionItem3 = (FunctionItem) findViewById(R.id.f_h_bl);
        showItemWithPermission();
    }

    private void showItemWithPermission() {

        if (Limit.limits[0].equals(User.limit)) {
            functionItem3.setVisibility(View.INVISIBLE);
        } else if (Limit.limits[1].equals(User.limit)) {
            functionItem.img.setImageResource(R.drawable.icon05);
            functionItem.text.setText(R.string.function_history_bl);
            functionItem1.setVisibility(View.INVISIBLE);
            functionItem2.setVisibility(View.INVISIBLE);
            functionItem3.setVisibility(View.INVISIBLE);

        }

    }

    @Override
    protected void registerEvents() {
        // TODO Auto-generated method stub
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

        mTitleBar.centerTextView.setText(R.string.function_history);

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
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.f_h_sb:
                if (Limit.limits[1].equals(User.limit)) {
                    startActivity(new Intent(mContext,
                            QuestionHistoryDisposeListActivity.class));
                } else if (Limit.limits[0].equals(User.limit)) {
                    startActivity(new Intent(mContext,
                            QuestionHistoryReportListActivity.class));
                }
                break;
            case R.id.f_h_hc:
                startActivity(new Intent(mContext,
                        QuestionHistoryExamineListActivity.class));
                break;
            case R.id.f_h_hs:
                startActivity(new Intent(mContext,
                        QuestionHistoryVerifyListActivity.class));
                break;
            case R.id.f_h_bl:
                startActivity(new Intent(mContext,
                        QuestionHistoryDisposeListActivity.class));
                break;
            default:
                Toast.makeText(mContext, R.string.no_permission, Toast.LENGTH_SHORT)
                        .show();
                break;
        }
    }
}
