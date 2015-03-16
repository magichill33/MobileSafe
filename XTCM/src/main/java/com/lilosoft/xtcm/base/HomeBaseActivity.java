package com.lilosoft.xtcm.base;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.Toast;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.constant.Limit;
import com.lilosoft.xtcm.instantiation.FunctionMenuElement;
import com.lilosoft.xtcm.instantiation.User;
import com.lilosoft.xtcm.module.HomeActivity;
import com.lilosoft.xtcm.module.QuestionReportActivity;
import com.lilosoft.xtcm.module.ReadyExamineActivity;
import com.lilosoft.xtcm.module.ReadyReportActivity;
import com.lilosoft.xtcm.module.ReadyVerifyActivity;
import com.lilosoft.xtcm.utils.ActivityManager;
import com.lilosoft.xtcm.utils.GISCommonTools;
import com.lilosoft.xtcm.utils.UpdateManager;
import com.lilosoft.xtcm.views.MPProgressBar;
import com.lilosoft.xtcm.views.TitleBar;
import com.lilosoft.xtcm.views.TitleBar.STYLE;

/**
 * @category 初始框架
 * @author William Liu
 *
 */
public class HomeBaseActivity extends AbsBaseActivityGroup implements
        OnCheckedChangeListener {

    public static Context mmContext = null;
    /**
     * @category 等待框状态
     */
    public static boolean progressDialogIsShow = false;
    /**
     * @category 等待框对象
     */
    public static MPProgressBar mPProgressBar;
    /**
     * @category 标题栏对象
     */
    public static TitleBar mTitleBar;
    /**
     * @category 底部tab控件对象
     */
    public static TabHost tabHost;
    public static RadioButton tabBt1;
    public static RadioButton tabBt2;
    public static RadioButton tabBt3;
    public static RadioButton tabBt4;
    public static RadioButton tabBt5;
    /**
     * @category 广播接收
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (ActivityOrder.BROAD_CAST_CLOSE_APP.equals(intent.getAction())) { // 收到关闭应用广播
                HomeBaseActivity.this.finish();
                ActivityManager.removeAllActivity();
            } else if (ActivityOrder.BROAD_CAST_SHOW_DIALOG.equals(intent // 收到弹出等待框广播
                    .getAction())) {
                showProgressDialog(R.string.please_wait);
            } else if (ActivityOrder.BROAD_CAST_INVISIBLE_DIALOG.equals(intent // 收到去除等待框广播
                    .getAction())) {
                dismissProgressDialog();
            }
        }
    };
    /**
     * @category 菜单元素对象
     */
    private FunctionMenuElement[] elements;

    /***
     *
     * @category 三个按钮提示框
     *
     * @param icon
     *            图标 --为 0 时不设置
     * @param title
     *            标题
     * @param message
     *            提示内容
     * @param positiveText
     *            第一个按钮 （为空时不显示）
     * @param positiveEvent
     *            第一个按钮事件
     * @param neutralText
     *            第二个按钮 （为空时不显示）
     * @param neutralEvent
     *            第二个按钮事件
     * @param negativeText
     *            第三个按钮 （为空时不显示）
     * @param negativeEvent
     *            第三个按钮事件
     */
    public static void show3ButtonAlertDialog(int icon, String title,
                                              String message, String positiveText,
                                              android.content.DialogInterface.OnClickListener positiveEvent,
                                              String neutralText,
                                              android.content.DialogInterface.OnClickListener neutralEvent,
                                              String negativeText,
                                              android.content.DialogInterface.OnClickListener negativeEvent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if (0 != icon)
            builder.setIcon(icon);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, positiveEvent);
        if ("" != neutralText)
            builder.setNeutralButton(neutralText, neutralEvent);
        if ("" != negativeText)
            builder.setNegativeButton(negativeText, negativeEvent);
        builder.create();
        builder.show();
    }

    /**
     *
     * @category 个按钮提示框
     * @param icon
     * @param title
     * @param message
     * @param positiveText
     * @param positiveEvent
     * @param neutralText
     * @param neutralEvent
     */
    public static void show2ButtonAlertDialog(int icon, String title,
                                              String message, String positiveText,
                                              android.content.DialogInterface.OnClickListener positiveEvent,
                                              String neutralText,
                                              android.content.DialogInterface.OnClickListener neutralEvent) {
        show3ButtonAlertDialog(icon, title, message, positiveText,
                positiveEvent, neutralText, neutralEvent, "", null);
    }

    /**
     *
     * @category 单个按钮提示框
     * @param icon
     * @param title
     * @param message
     * @param positiveText
     * @param positiveEvent
     */
    public static void show1ButtonAlertDialog(int icon, String title,
                                              String message, String positiveText,
                                              android.content.DialogInterface.OnClickListener positiveEvent) {
        show3ButtonAlertDialog(icon, title, message, positiveText,
                positiveEvent, "", null, "", null);
    }

        /**
     *
     * @category 无按钮提示框
     * @param icon
     * @param title
     * @param message
     */
    public static void showNoButtonAlertDialog(int icon, String title,
                                               String message) {
        show3ButtonAlertDialog(icon, title, message, "", null, "", null, "",
                null);
    };

    /**
     * @category 显示等待框
     *
     * @param tipMsg
     *            等待框消息
     */
    public static void showProgressDialog(int tipMsg) {
        showProgressDialog(mContext.getResources().getString(tipMsg));
    }

    /**
     * @category 显示等待框
     *
     * @param tipMsg
     *            等待框消息
     */
    public static void showProgressDialog(String tipMsg) {
        progressDialogIsShow = true;
        mPProgressBar.setTextTipMsg(tipMsg);
        mPProgressBar.setTextEllipsis(mContext.getResources().getString(
                R.string.more_char));
        mPProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * @category 关闭等待框
     */
    public static void dismissProgressDialog() {
        if (progressDialogIsShow)
            mPProgressBar.setVisibility(View.GONE);
        progressDialogIsShow = false;
    }

    /**
     *
     * @category 供实现自定义动态获取功能使用
     * @author William Liu
     */
    private void FunctionMenuElement() {

        elements = new FunctionMenuElement[5];
        elements[0] = new FunctionMenuElement(Config.A_TAB,
                R.string.function_home, R.drawable.icon_1_n, new Intent(this,
                HomeActivity.class));
        elements[1] = new FunctionMenuElement(Config.B_TAB,
                R.string.function_report, R.drawable.icon_2_n, new Intent(this,
                QuestionReportActivity.class));
        elements[2] = new FunctionMenuElement(Config.C_TAB,
                R.string.function_ready_report, R.drawable.icon_3_n,
                new Intent(this, ReadyReportActivity.class));
        elements[3] = new FunctionMenuElement(Config.D_TAB,
                R.string.function_ready_verify, R.drawable.icon_5_n,
                new Intent(this, ReadyExamineActivity.class));
        elements[4] = new FunctionMenuElement(Config.E_TAB,
                R.string.function_ready_examine, R.drawable.icon_4_n,
                new Intent(this, ReadyVerifyActivity.class));

    }

    @Override
    protected void installViews() {
        // TODO Auto-generated method stub
        initTitleBar();
        initBottomTab();
        initValues();
        registerBroadcast();
        isUpdate();
        /**
         * 启动轨迹上报服务
         */
        Intent intent = new Intent(
                "COM.LILOSOFT.XTCM.MODULE.AUTO_LOCATION_REPORT_SERVICE");
        startService(intent);
    }

    // 检测是否启动更新
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
    protected void registerEvents() {
        // TODO Auto-generated method stub
        tabBt1.setOnCheckedChangeListener(this);
        tabBt2.setOnCheckedChangeListener(this);
        tabBt3.setOnCheckedChangeListener(this);
        tabBt4.setOnCheckedChangeListener(this);
        tabBt5.setOnCheckedChangeListener(this);
    }

    /**
     * @category 初始化titleBar
     */
    protected void initTitleBar() {

        mTitleBar = (TitleBar) findViewById(R.id.titlebar);

        mTitleBar.changeStyle(STYLE.NOT_BTN_AND_TITLE);

        mTitleBar.tit_username.setText(User.limit + ": " + User.username);

    }

    /**
     * @category 初始化底部栏
     */
    @SuppressWarnings("deprecation")
    private void initBottomTab() {

        tabHost = (TabHost) findViewById(R.id.tabhost);

        tabHost.setup(this.getLocalActivityManager());

        FunctionMenuElement();

        tabBt1 = (RadioButton) findViewById(R.id.radio_button0);
        tabBt2 = (RadioButton) findViewById(R.id.radio_button1);
        tabBt3 = (RadioButton) findViewById(R.id.radio_button2);
        tabBt4 = (RadioButton) findViewById(R.id.radio_button3);
        tabBt5 = (RadioButton) findViewById(R.id.radio_button4);

        for (int i = 0; i < 5; i++) {

            // Drawable icon;
            // Resources res = getResources();
            // icon = res.getDrawable(elements[i].getResIcon());
            // // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
            // icon.setBounds(0, 0, icon.getMinimumWidth(),
            // icon.getMinimumHeight());
            // tabBt1.setCompoundDrawables(null, icon, null, null);
            // tabBt1.setText(elements[i].getResLabel());

            tabHost.addTab(buildTabSpec(elements[i]));

        }
    }

    private void initValues() {
        mPProgressBar = (MPProgressBar) findViewById(R.id.mPProgressBar);
        mmContext =  mContext;
    }

    /**
     * @category 改变底部栏状态事件
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (User.limit.equals(Limit.limits[0]) || !Config.NETWORK) {
                switch (buttonView.getId()) {
                    // Client权限控制
                    case R.id.radio_button0:
                        mTitleBar.centerTextView.setText(R.string.app_name);
                        tabHost.setCurrentTabByTag(Config.A_TAB);
                        break;
                    case R.id.radio_button1:
                        mTitleBar.centerTextView.setText(R.string.function_report);
                        tabHost.setCurrentTabByTag(Config.B_TAB);
                        break;
                    case R.id.radio_button2:
                        mTitleBar.centerTextView
                                .setText(R.string.function_ready_report);
                        tabHost.setCurrentTabByTag(Config.C_TAB);
                        break;
                    case R.id.radio_button3:
                        mTitleBar.centerTextView
                                .setText(R.string.function_ready_verify);
                        tabHost.setCurrentTabByTag(Config.D_TAB);
                        break;
                    case R.id.radio_button4:
                        mTitleBar.centerTextView
                                .setText(R.string.function_ready_examine);
                        tabHost.setCurrentTabByTag(Config.E_TAB);
                        break;
                }
            } else {
                Toast.makeText(mContext, R.string.no_permission,
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * @category 添加底部栏元素
     * @param tag
     * @param resLabel
     * @param resIcon
     * @param content
     * @return
     */
    private TabHost.TabSpec buildTabSpec(FunctionMenuElement fuction) {
        return tabHost
                .newTabSpec(fuction.getTag())
                .setIndicator(getString(fuction.getResLabel()),
                        getResources().getDrawable(fuction.getResIcon()))
                .setContent(fuction.getContent());
    }

    /**
     * @category 广播发送
     * @param broadCast
     */
    public void sendBroadCast(String broadCast) {
        Intent intent = new Intent();
        intent.setAction(broadCast);
        super.sendBroadcast(intent); // 发送广播
    }

    /**
     * @category 注册广播监听
     */
    private void registerBroadcast() {
        IntentFilter in = new IntentFilter();
        in.addAction(ActivityOrder.BROAD_CAST_CLOSE_APP);
        in.addAction(ActivityOrder.BROAD_CAST_SHOW_DIALOG);
        in.addAction(ActivityOrder.BROAD_CAST_INVISIBLE_DIALOG);
        registerReceiver(broadcastReceiver, in);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        dismissProgressDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        return super.onCreateOptionsMenu(menu);
        // return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (progressDialogIsShow) {
            dismissProgressDialog();
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    /**
     *
     * @category 广播监听指令
     * @author William Liu
     *
     */
    public interface ActivityOrder {
        String BROAD_CAST_CLOSE_APP = "close_app";
        String BROAD_CAST_SHOW_DIALOG = "show_dialog";
        String BROAD_CAST_INVISIBLE_DIALOG = "invisible_dialog";
    }

}
