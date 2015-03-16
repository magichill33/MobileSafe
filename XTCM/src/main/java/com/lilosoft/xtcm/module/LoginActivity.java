package com.lilosoft.xtcm.module;

import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.HomeBaseActivity;
import com.lilosoft.xtcm.base.NormalBaseActivity;
import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.constant.Limit;
import com.lilosoft.xtcm.constant.TableStructure;
import com.lilosoft.xtcm.database.SharedPreferencesFactory;
import com.lilosoft.xtcm.instantiation.User;
import com.lilosoft.xtcm.instantiation.UserInfo;
import com.lilosoft.xtcm.network.HttpConnection;
import com.lilosoft.xtcm.utils.ActivityManager;
import com.lilosoft.xtcm.utils.GISCommonTools;
import com.lilosoft.xtcm.utils.JsonParseInterface;
import com.lilosoft.xtcm.utils.LogFactory;
import com.lilosoft.xtcm.utils.UpdateManager;
import com.lilosoft.xtcm.views.MPProgressBar;

/**
 * @category 登录
 * @author William Liu
 *
 */
public class LoginActivity extends NormalBaseActivity implements
        OnClickListener, OnCheckedChangeListener, JsonParseInterface {

    protected static final int SUCCESS_EVENTDATA = 0;
    private final static String TAG = "LoginActivity";
    /**
     * 登录成功
     */
    private final static int MSG_LOGIN_SUCCESS_ORDER = 0x0FFFFFFF;
    /**
     * 登录失败
     */
    private final static int MSG_LOGIN_LOST_ORDER = 0x00FFFFFF;
    Thread updateThread=new Thread(new Runnable() {

        @Override
        public void run() {
            Looper.prepare();
            isUpdate();
            Looper.loop();
        }
    });
    private String err_Msg;
    /**
     * @category 主线程处理
     */
    @SuppressLint("HandlerLeak")
    private Handler myHandle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS_EVENTDATA:
                    dismissProgressDialog();
//				updateThread.start();
//				try {
//					updateThread.join();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
                    initUser();


                    break;
                case Config.SHOW_PROGRESS_DIALOG:
                    showProgressDialog(R.string.logining);
                    break;
                case Config.DISMISS_PROGRESS_DIALOG:
                    dismissProgressDialog();
                    break;
                case MSG_LOGIN_SUCCESS_ORDER:
                    LogFactory.e(TAG, "Login Success");
                {
                    if (v_savepsw.isChecked()) {
                        String psw = v_psw.getText().toString();
                        if (v_autologin.isChecked())
                            new SharedPreferencesFactory().savaUserInfo(
                                    mContext, v_userName.getText().toString(),
                                    psw, true);
                        else
                            new SharedPreferencesFactory().savaUserInfo(
                                    mContext, v_userName.getText().toString(),
                                    psw, false);
                    } else
                        new SharedPreferencesFactory().savaUserInfo(mContext,
                                v_userName.getText().toString(), "0", false);
                }

                if (User.limit.equals(Limit.limits[0])) {
                    startActivity(new Intent(mContext, HomeBaseActivity.class));
                } else if (User.limit.equals(Limit.limits[1])) {
                    startActivity(new Intent(mContext,
                            HomeLowPermissionActivity.class));
                }
                LoginActivity.this.finish();

                break;
                case MSG_LOGIN_LOST_ORDER:
                    v_psw.setText("");
                    if (null != err_Msg && !"".equals(err_Msg)) {
                        Toast.makeText(LoginActivity.this, err_Msg,
                                Toast.LENGTH_LONG).show();
                        err_Msg = "";
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.error_data,
                                Toast.LENGTH_LONG).show();
                    }
                    LogFactory.e(TAG, "Login lost");
                    break;
                case Config.MSG_LOST_404:
                    Toast.makeText(mContext, "Sorry 404", Toast.LENGTH_LONG).show();
                    break;
                case Config.MSG_LOST_CONN:
                    Toast.makeText(mContext, R.string.error_connection,
                            Toast.LENGTH_LONG).show();
                    break;
                case Config.MSG_LOST_JSON:
                    Toast.makeText(mContext, R.string.error_data, Toast.LENGTH_LONG)
                            .show();
                    break;
            }

        }

    };
    private Message m;
    /**
     * @category 登录请求处理
     */
    private Thread loginThread = new Thread(new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            LogFactory.e(TAG, "login thread start");
            m = new Message();
            m.what = Config.SHOW_PROGRESS_DIALOG;
            myHandle.sendMessage(m);
            try {
                HttpConnection httpConnection = new HttpConnection();
                action(httpConnection.getData(
                        HttpConnection.CONNECTION_COMMON_LOGIN, v_userName
                                .getText().toString(), v_psw.getText()
                                .toString()));
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                LogFactory.e(TAG, "ClientProtocolException 404");
                m = new Message();
                m.what = Config.MSG_LOST_404;
                myHandle.sendMessage(m);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                LogFactory.e(TAG, "IOException");
                m = new Message();
                m.what = Config.MSG_LOST_CONN;
                myHandle.sendMessage(m);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                LogFactory.e(TAG, "JSONException");
                m = new Message();
                m.what = Config.MSG_LOST_JSON;
                myHandle.sendMessage(m);
            } finally {
                m = new Message();
                m.what = Config.DISMISS_PROGRESS_DIALOG;
                myHandle.sendMessage(m);
                LogFactory.e(TAG, "login thread end");
            }
        }

    });
    private AutoCompleteTextView v_userName;
    private EditText v_psw;
    private CheckBox v_savepsw;
    private CheckBox v_autologin;
    private Button v_submit;
    private Button v_exit;

    @Override
    protected void installViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_login);
        initViews();

        showProgressDialog("数据加载中，请稍候！");
        new Thread(new Runnable() {
            @Override
            public void run() {
//				new AutoUpdateEvent(LoginActivity.this).updateEvent();// 自动更新事件分类信息
                updateEvent(false);
				 /*Log.i(TAG, LoginActivity.this.getSharedPreferences(
				 Config.SHARED_PREFERENCES_NAME, MODE_PRIVATE)
				 .getBoolean(Config.SHARED_PREFERENCES_ISUPDATE, false)+"");*/
                Message message = Message.obtain();
                message.what = SUCCESS_EVENTDATA;
                myHandle.sendMessage(message);
            }
        }).start();
        initConfig();
        SharedPreferencesFactory.getConfig(mContext);
        SharedPreferencesFactory.getMapConfig(mContext);
        LogFactory.e(TAG, Environment.getExternalStorageDirectory().toString());
    }

    /**
     * 检测版本号，是否更新分类信息
     * @return true 更新  false 不更新
     */
    private boolean checkVison(){


        return false;
    }

    /**
     * 自动更新事件分类信息
     *
     * @param isupdate
     */
    private void updateEvent(boolean isupdate) {
        if (!isupdate) {
            SharedPreferences sharedPreferences = LoginActivity.this
                    .getSharedPreferences(Config.SHARED_PREFERENCES_NAME,
                            MODE_PRIVATE);
            String typeVison = sharedPreferences.getString(Config.TypeVison, "4");
            new AutoUpdateEvent(LoginActivity.this).updateEvent(typeVison);// 自动更新事件分类信息
			/*SharedPreferences sharedPreferences = LoginActivity.this
					.getSharedPreferences(Config.SHARED_PREFERENCES_NAME,
							MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putBoolean(Config.SHARED_PREFERENCES_ISUPDATE, false);
			editor.commit();*/
        }

    }

    @Override
    protected void registerEvents() {
        // TODO Auto-generated method stub
        v_savepsw.setOnCheckedChangeListener(this);
        v_autologin.setOnCheckedChangeListener(this);
        v_submit.setOnClickListener(this);
        v_exit.setOnClickListener(this);
    }

    private void initViews() {
        mPProgressBar = (MPProgressBar) findViewById(R.id.mPProgressBar);
        v_userName = (AutoCompleteTextView) findViewById(R.id.login_user_name);
        v_psw = (EditText) findViewById(R.id.login_password);
        v_savepsw = (CheckBox) findViewById(R.id.login_check_savepwd);
        v_autologin = (CheckBox) findViewById(R.id.login_check_autologin);
        v_submit = (Button) findViewById(R.id.login_submit);
        v_exit = (Button) findViewById(R.id.login_exit);
    }

        @SuppressWarnings({ "rawtypes", "unchecked" })
    private void initUser() {

//		if (!GISCommonTools.CheckNet(LoginActivity.this)) {
//			// toastUtil.showToast(R.string.network_error);
//		} else {
//			UpdateManager updateManager = new UpdateManager(LoginActivity.this);
//			updateManager.checkUpdate();
//		}

        SharedPreferencesFactory allUserName = new SharedPreferencesFactory();
        String[] allUserNames = allUserName.getAllUserName(mContext);
        if (null != allUserNames) {
            v_userName.setAdapter(new ArrayAdapter(this,
                    R.layout.view_autotext_item, allUserNames));
            v_userName.setThreshold(1);
            UserInfo userInfo = allUserName.getTopUser(mContext);
            if (null != userInfo) {
                v_userName.setText(userInfo.getUsername());
                if (!"0".equals(userInfo.getPasscode())) {
                    v_psw.setText(userInfo.getPasscode());
                    v_savepsw.setChecked(true);

                    if (allUserName.isAutoLogin(mContext)) {
                        LogFactory.e(TAG, "isAutoLogin");
                        v_autologin.setChecked(true);
                        boolean b = getIntent().getBooleanExtra("unlogin",
                                false);
                        if (!b) {
                            threadG = new Thread(loginThread);
                            threadG.start();
                        }
                    }
                }
            }
        } else {
            v_savepsw.setChecked(true);
            v_autologin.setChecked(true);
        }
    };

    private void initConfig() {

        // if (!Config.LOGLEVEL)
        // new SharedPreferencesFactory().getConfig(mContext);
        File file = new File(Config.FILES_NAME_URL);
        if (!file.exists()) {
            file.mkdirs(); // 新建文件夹
            // try {
            // file.createNewFile(); // 新建文件
            // } catch (IOException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
        }
        // ...............

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub
        switch (buttonView.getId()) {
            case R.id.login_check_savepwd:
                if (!isChecked) {
                    v_autologin.setChecked(isChecked);
                }
                break;
            case R.id.login_check_autologin:
                if (isChecked) {
                    v_savepsw.setChecked(isChecked);
                }
                break;
        }

    }

public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_submit:
                int userNameLength = v_userName.getText().toString().length();
                int userPswLength = v_psw.getText().toString().length();
                if (0 == userNameLength) {
                    Toast.makeText(mContext, R.string.login_warning_usw_null,
                            Toast.LENGTH_LONG).show();
                } else if (userNameLength < 1 || userNameLength > 12) {
                    Toast.makeText(mContext, R.string.login_warning_usw_length,
                            Toast.LENGTH_LONG).show();
                } else {
                    if (0 == userPswLength) {
                        v_psw.setText("");
                        Toast.makeText(mContext, R.string.login_warning_psw_null,
                                Toast.LENGTH_LONG).show();
                    } else if (userPswLength < 1 || userPswLength > 12) {
                        v_psw.setText("");
                        Toast.makeText(mContext, R.string.login_warning_psw_length,
                                Toast.LENGTH_LONG).show();
                    } else {

                        if (Config.NETWORK) {
                            threadG = new Thread(loginThread);
                            threadG.start();
                        } else {
                            // 后台没有搭建完成 模拟登录
                            Toast.makeText(mContext, "登录成功", Toast.LENGTH_LONG)
                                    .show();
                            User.username = v_userName.getText().toString();
                            User.passcode = v_psw.getText().toString();

                            new SharedPreferencesFactory().savaUserInfo(mContext,
                                    v_userName.getText().toString(), v_psw
                                            .getText().toString(), true);

                            startActivity(new Intent(mContext,
                                    HomeBaseActivity.class));
                            LoginActivity.this.finish();
                        }
                    }
                }
                break;
            case R.id.login_exit:
                ActivityManager.removeAllActivity();
                LoginActivity.this.finish();
                break;
        }

    }

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

    /**
     * @category 指令分发
     * @param response
     */
    private void action(String response) {
        m = new Message();
        try {
            m = new Message();
            m.what = jsonParseToOrder(response);
            myHandle.sendMessage(m);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogFactory.e(TAG, "action JSONException");
        }
    }

    @Override
    public int jsonParseToOrder(String response) throws JSONException {
        if (null != response && !"".equals(response)) {
            JSONObject jsonObject = new JSONObject(response);

            if (null != jsonObject) {

                String operation = jsonObject
                        .getString(TableStructure.COVER_HEAD);

                if (TableStructure.V_ACT_LOGIN.equals(operation)) {
                    JSONObject content = (JSONObject) ((jsonObject
                            .get(TableStructure.COVER_BODY)));
                    if ("1".equals(content
                            .getString(TableStructure.R_USER_RESPONSE_KEY))) {

                        User.username = v_userName.getText().toString();
                        User.passcode = v_psw.getText().toString();
                        String limits[] = content.getString(
                                TableStructure.R_USER_RESPONSE_LIMIT)
                                .split(",");
                        for (int i = 0; i < limits.length; i++)
                            for (int j = 0; j < Limit.limits.length; j++) {
                                if (limits[i].equals(Limit.limits[j])) {
                                    User.limit = limits[i];
                                    break;
                                }
                            }

                        // 网格编号
                        if (!"".equals(content
                                .getString(TableStructure.R_USER_RESPONSE_GRIDINFO)))
                            User.gridinfo = content.getString(
                                    TableStructure.R_USER_RESPONSE_GRIDINFO)
                                    .split(",");

                        if (User.limit == "") {
                            err_Msg = "对不起，没有权限！";
                            return MSG_LOGIN_LOST_ORDER;
                        }

                        return MSG_LOGIN_SUCCESS_ORDER;
                    } else {
                        err_Msg = "登录失败";
                        // err_Msg = content
                        // .getString(TableStructure.R_USER_RESPONSE_MSG);
                    }
                }
            }
        } else {
            err_Msg = "数据异常！";
            LogFactory.e(TAG, "not data!");
        }

        return MSG_LOGIN_LOST_ORDER;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        if (!Config.LOGLEVEL) {
            menu.add(0, 0, 0, "服务器设置");
            menu.add(0, 1, 1, "地图设置");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                startActivity(new Intent(mContext, SetupActivity.class));
                break;
            case 1:
                startActivity(new Intent(mContext, MapSetupActivity.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
