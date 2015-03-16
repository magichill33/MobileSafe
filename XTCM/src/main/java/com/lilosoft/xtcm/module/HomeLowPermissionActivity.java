package com.lilosoft.xtcm.module;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.NormalBaseActivity;
import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.instantiation.User;
import com.lilosoft.xtcm.utils.ActivityManager;
import com.lilosoft.xtcm.utils.GISCommonTools;
import com.lilosoft.xtcm.utils.HttpFileUpTool;
import com.lilosoft.xtcm.utils.UpdateManager;
import com.lilosoft.xtcm.views.FunctionItem;
import com.lilosoft.xtcm.views.TitleBar;
import com.lilosoft.xtcm.views.TitleBar.STYLE;

/**
 * @category 首页-权限Limit.limit[1]
 * @author William Liu
 *
 */
public class HomeLowPermissionActivity extends NormalBaseActivity implements
        OnClickListener {

    private boolean isExit = false;
    private boolean hasTask = false;
    private TitleBar mTitleBar;
    private FunctionItem functionItem;
    private FunctionItem functionItem1,functionItem2,functionItem3,functionItem4;
    private void isUpdate() {
        // 验证更新的网络状态
        if (GISCommonTools.CheckNet(this)) {
            // 第一次登陆程序时进行验证是否启动更新
            if (getIntent().getSerializableExtra("ifUpdate") == null) {
                UpdateManager updateManager = new UpdateManager(this);
                updateManager.checkUpdate();
            }
        }
    }

    @Override
    protected void installViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_home_low_permission);
        initTitleBar();
        setViewsSize();
        functionItem = (FunctionItem) findViewById(R.id.f_lowfun1);
        functionItem1 = (FunctionItem) findViewById(R.id.f_lowfun2);
        functionItem2=(FunctionItem) findViewById(R.id.f_lowfun3);
        functionItem3=(FunctionItem) findViewById(R.id.f_lowfun4);
        functionItem4=(FunctionItem) findViewById(R.id.f_lowfun5);
        isUpdate();
//		Intent intent = new Intent(
//				"COM.LILOSOFT.XTCM.MODULE.AUTO_LOCATION_REPORT_SERVICE");
//		startService(intent);
    }

    @Override
    protected void registerEvents() {
        // TODO Auto-generated method stub
        functionItem.setOnClickListener(this);
        functionItem1.setOnClickListener(this);
        functionItem2.setOnClickListener(this);
        functionItem3.setOnClickListener(this);
        functionItem4.setOnClickListener(this);
    }

    /**
     * @category 初始化titleBar
     */
    protected void initTitleBar() {

        mTitleBar = (TitleBar) findViewById(R.id.titlebar);

        mTitleBar.changeStyle(STYLE.NOT_BTN_AND_TITLE);

        mTitleBar.centerTextView.setText(R.string.app_name);

        mTitleBar.tit_username.setText(User.limit + ":" + User.username);

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
            case R.id.f_lowfun1:
                startActivity(new Intent(mContext, ReadyDisposeActivity.class));
                break;
            case R.id.f_lowfun2:
                startActivity(new Intent(mContext, QuestionHistoryActivity.class));
                break;
            case R.id.f_lowfun3:
                startActivity(new Intent(mContext,
                        AdministratorApproveMenuActivity.class));
                break;
            case R.id.f_lowfun4:
                startActivity(new Intent(mContext,
                        ContactsActivity.class));
                break;
            case R.id.f_lowfun5:
                startActivity(new Intent(mContext,
                        PunchCardActivity.class));
                break;
            default:
                Toast.makeText(mContext, R.string.no_permission, Toast.LENGTH_SHORT)
                        .show();
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                isExit = false;
                hasTask = false;
            }
        };

        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (!isExit) {
                if (!hasTask) {
                    hasTask = true;
                    Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                    timer.schedule(task, 2000);
                } else {
                    ActivityManager.removeAllActivity();
                    finish();
                }
            }
        } else if (KeyEvent.KEYCODE_MENU == keyCode) {
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "登出");
        menu.add(0, 1, 0, "退出");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                startActivity(new Intent(mContext, LoginActivity.class).putExtra(
                        "unlogin", true));
                finish();
                break;
            case 1:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
