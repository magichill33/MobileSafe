package com.lilosoft.xtcm.module;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.esri.core.geometry.Point;
import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.NormalBaseActivity;
import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.constant.TableStructure;
import com.lilosoft.xtcm.instantiation.User;
import com.lilosoft.xtcm.network.HttpConnection;
import com.lilosoft.xtcm.utils.GeometryUtil;
import com.lilosoft.xtcm.utils.LogFactory;
import com.lilosoft.xtcm.views.FunctionItem;
import com.lilosoft.xtcm.views.LoadingProgressDialog;
import com.lilosoft.xtcm.views.MPProgressBar;
import com.lilosoft.xtcm.views.TitleBar;
import com.lilosoft.xtcm.views.TitleBar.STYLE;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class PunchCardActivity extends NormalBaseActivity implements
        OnClickListener {
    private final static String TAG = "PunchCardActivity";
    private final static int MSG_PCARD_SUCCESS_ORDER = 0x0FFFFFFF;
    private final static int START_THREAD_AFTERNOON = 0x0123123;
    private final static int START_THREAD_EVENING = 0x0123343;
    Thread getrecordafternoon_thread = new Thread(new Runnable() {

        @Override
        public void run() {
            HttpConnection httpConnection = new HttpConnection();
            String response;
            try {
                response = httpConnection.getData(
                        HttpConnection.CONNECTION_COMMON_PCARD_GETRECORD,
                        User.username);
                if (null != response && !"".equals(response)) {
                    JSONObject jsonObject = new JSONObject(response);

                    if (null != jsonObject) {

                        String operation = jsonObject
                                .getString(TableStructure.COVER_HEAD);

                        if (TableStructure.PUNCHCARD_Record.equals(operation)) {
                            JSONArray content = jsonObject
                                    .getJSONArray(TableStructure.COVER_BODY);
                            if (content != null) {
                                JSONObject jsonObject2 = content
                                        .getJSONObject(0);

                                record_afternoon = (String) jsonObject2
                                        .getString("EXTEND5");
                                int i = record_afternoon.length();
                                if (record_afternoon != null
                                        && record_afternoon != "" && i > 0) {
                                    Message msg = new Message();
                                    msg.what = 678;
                                    myHandle.sendMessage(msg);
                                } else {
                                    Message msg = new Message();
                                    msg.what = START_THREAD_EVENING;
                                    myHandle.sendMessage(msg);
                                }
                            } else {
                                Message msg = new Message();
                                msg.what = START_THREAD_EVENING;
                                myHandle.sendMessage(msg);
                            }
                        }

                    }
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    });
    private final static int START_THREAD_GETUP = 0x0895443;
    private final static int SET_PCARD_PALCE = 0x0673343;
    /**
     *
     */
    private final static int MSG_pCARD_LOST_ORDER = 0x00FFFFFF;
    private final static int MSG_pCARD_CANNOT = 0x01FFFFFF;
    private final static int DISMISS_PRO = 0x11FF11FF;
    protected static MPProgressBar mPProgressBar;
    Thread afternoon_Thread = new Thread(new Runnable() {

        @Override
        public void run() {
            HttpConnection httpConnection = new HttpConnection();
            String response;
            String worktime = getbeijingtime();
            if (!punchpard(afternoon_time)) {
                myHandle.sendEmptyMessage(12314124);
            } else {
                String worktype = "0";
                try {
                    if (CanblePCard(stime, p_afternoon_time)) {
                        worktype = "1";

                    }

                    response = httpConnection.getData(
                            HttpConnection.CONNECTION_COMMON_PCARD,
                            User.username, getDate(), "", "", "", "", worktime,
                            worktype, adress, "", "");
                    action(response);

                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

    });
    Thread morningoff_Thread = new Thread(new Runnable() {

        @Override
        public void run() {

            HttpConnection httpConnection = new HttpConnection();
            String response;
            String worktime = getbeijingtime();
            if (!punchpard(morningoff_time)) {
                myHandle.sendEmptyMessage(12314124);
            } else {
                String offworktime = "";
                String worktype = "1";
                String offworktype = "";
                try {
                    if (CanblePCard(stime, p_morningoff_time)) {
                        worktype = "0";

                    }

                    response = httpConnection.getData(
                            HttpConnection.CONNECTION_COMMON_PCARD,
                            User.username, getDate(), "", "", "", offworktype,
                            "", "", adress, worktype, worktime);
                    action(response);

                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    });
    Thread evening_Thread = new Thread(new Runnable() {

        @Override
        public void run() {
            HttpConnection httpConnection = new HttpConnection();
            String response;
            String worktime = "";
            String offworktime = getbeijingtime();
            if (!punchpard(evening_time)) {
                myHandle.sendEmptyMessage(12314124);
            } else {
                String worktype = "";
                String offworktype = "1";
                try {
                    if (CanblePCard(stime, p_evening_time)) {
                        offworktype = "0";

                    }

                    response = httpConnection.getData(
                            HttpConnection.CONNECTION_COMMON_PCARD,
                            User.username, getDate(), worktime, offworktime,
                            worktype, offworktype, "", "", adress,"","");
                    action(response);

                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    });
    private TitleBar mTitleBar;
    // private Button workingpcard, offworkingpcard;
    private String currtDate;
    private Message m;
    private FunctionItem workingpcard, offworkingpcard, xiawupcard,shangwupcard;
    private boolean ispunchCard, get_placeid;
    private String morning_time, morningoff_time, allgap_time, afternoon_time,
            evening_time, allowlate_time, allowleave_time, pcard_gap_time;
    private String p_morning_time, p_morningoff_time, p_afternoon_time,
            p_evening_time;
    Thread getsetup_thread = new Thread(new Runnable() {

        @Override
        public void run() {
            HttpConnection httpConnection = new HttpConnection();
            String response;
            try {
                response = httpConnection.getData(
                        httpConnection.CONNECTION_COMMON_PCARD_SETUP, "");
                if (null != response && !"".equals(response)) {
                    JSONObject jsonObject = new JSONObject(response);

                    if (null != jsonObject) {

                        String operation = jsonObject
                                .getString(TableStructure.COVER_HEAD);

                        if (TableStructure.PUNCHCARD_SETUP.equals(operation)) {
                            JSONArray content = jsonObject
                                    .getJSONArray(TableStructure.COVER_BODY);
                            JSONObject jsonObject2 = content.getJSONObject(0);
                            morning_time = jsonObject2
                                    .getString(TableStructure.pcard_morning_time);
                            morningoff_time = jsonObject2.getString("EXTEND1");
                            allgap_time = jsonObject2.getString("EXTEND2");
                            afternoon_time = jsonObject2
                                    .getString(TableStructure.pcard_afternoon_time);
                            evening_time = jsonObject2
                                    .getString(TableStructure.pcard_evening_time);
                            allowlate_time = jsonObject2
                                    .getString(TableStructure.pcard_allowlate_time);
                            allowleave_time = jsonObject2
                                    .getString(TableStructure.pcard_allow_leave_time);
                            pcard_gap_time = jsonObject2
                                    .getString(TableStructure.pcard_gap_time);
                            Log.e(TAG, "--" + morning_time);
                            SimpleDateFormat sdf = new SimpleDateFormat(
                                    "HH:mm:ss");
                            try {
                                long str3 = sdf.parse(morningoff_time)
                                        .getTime()
                                        - Long.parseLong(allowlate_time)
                                        * 60
                                        * 1000;
                                p_morningoff_time = sdf.format(new Date(str3));
                                long str = sdf.parse(morning_time).getTime()
                                        + Long.parseLong(allowlate_time) * 60
                                        * 1000;
                                p_morning_time = sdf.format(new Date(str));
                                long str1 = sdf.parse(afternoon_time).getTime()
                                        + Long.parseLong(allowlate_time) * 60
                                        * 1000;
                                p_afternoon_time = sdf.format(new Date(str1));
                                long str2 = sdf.parse(evening_time).getTime()
                                        - Long.parseLong(allowleave_time) * 60
                                        * 1000;
                                p_evening_time = sdf.format(new Date(str2));

                                Log.e(TAG, p_morning_time + "--"
                                        + p_afternoon_time + "--"
                                        + p_evening_time);
                            } catch (NumberFormatException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            if (morning_time != null && morning_time != "") {
                                Message message = new Message();
                                message.what = 789;
                                myHandle.sendMessage(message);

                            }

							/*
							 * for(int i=0;i<content.length();i++){ JSONObject
							 * jsonObject2=content.optJSONObject(i);
							 * if(jsonObject2
							 * .getString(TableStructure.pcard_morning_time
							 * )!=null){
							 * morning_time=jsonObject2.getString(TableStructure
							 * .pcard_morning_time); }
							 * if(jsonObject2.getString(TableStructure
							 * .pcard_morning_time)!=null){
							 * afternoon_time=jsonObject2
							 * .getString(TableStructure.pcard_afternoon_time);
							 * } if(jsonObject2.getString(TableStructure.
							 * pcard_morning_time)!=null){
							 * evening_time=jsonObject2
							 * .getString(TableStructure.pcard_evening_time); }
							 * if(jsonObject2.getString(TableStructure.
							 * pcard_morning_time)!=null){
							 * allowlate_time=jsonObject2
							 * .getString(TableStructure.pcard_allowlate_time);
							 * } if(jsonObject2.getString(TableStructure.
							 * pcard_morning_time)!=null){
							 * allowleave_time=jsonObject2
							 * .getString(TableStructure
							 * .pcard_allow_leave_time); }
							 * if(jsonObject2.getString
							 * (TableStructure.pcard_morning_time)!=null){
							 * pcard_gap_time
							 * =jsonObject2.getString(TableStructure
							 * .pcard_gap_time); } }
							 */

                        }
                    }
                }

                Log.e(TAG, response);
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    });
    Thread morning_Thread = new Thread(new Runnable() {

        @Override
        public void run() {

            HttpConnection httpConnection = new HttpConnection();
            String response;
            String worktime = getbeijingtime();
            if (!punchpard(morning_time)) {
                myHandle.sendEmptyMessage(12314124);
            } else {
                String offworktime = "";
                String worktype = "0";
                String offworktype = "";
                try {
                    if (CanblePCard(stime, p_morning_time)) {
                        worktype = "1";

                    }

                    response = httpConnection.getData(
                            HttpConnection.CONNECTION_COMMON_PCARD,
                            User.username, getDate(), worktime, offworktime,
                            worktype, offworktype, "", "", adress, "", "");
                    action(response);

                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    });
    private String record_morning, record_afternoon;
    Thread getrecordmorning_thread = new Thread(new Runnable() {

        @Override
        public void run() {
            HttpConnection httpConnection = new HttpConnection();
            String response;
            try {
                response = httpConnection.getData(
                        HttpConnection.CONNECTION_COMMON_PCARD_GETRECORD,
                        User.username);
                if (null != response && !"".equals(response)) {
                    JSONObject jsonObject = new JSONObject(response);

                    if (null != jsonObject) {

                        String operation = jsonObject
                                .getString(TableStructure.COVER_HEAD);

                        if (TableStructure.PUNCHCARD_Record.equals(operation)) {
                            JSONArray content = jsonObject
                                    .getJSONArray(TableStructure.COVER_BODY);
                            if (content != null) {
                                JSONObject jsonObject2 = content
                                        .getJSONObject(0);
                                if (jsonObject2 != null) {
                                    record_morning = (String) jsonObject2
                                            .getString("GETRECORDTIME");
                                    int i = record_morning.length();

                                    if (record_morning != null
                                            && record_morning != "" && i > 0) {
                                        Message msg = new Message();
                                        msg.what = 123;
                                        myHandle.sendMessage(msg);
                                    } else {
                                        Message msg = new Message();
                                        msg.what = START_THREAD_AFTERNOON;
                                        myHandle.sendMessage(msg);
                                    }
                                }

                            } else {
                                Message msg = new Message();
                                msg.what = START_THREAD_AFTERNOON;
                                myHandle.sendMessage(msg);
                            }
                        }
                    }
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    });
    private String pcard_place_id, adress, stime;
    Thread getidThread = new Thread(new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            HttpConnection httpConnection = new HttpConnection();
            String response;
            try {
                response = httpConnection.getData(
                        HttpConnection.CONNECTION_COMMON_PCARD_PLACE,
                        User.username);
                if (response == null) {
                    get_placeid = false;
                    Message message = new Message();
                    message.what = SET_PCARD_PALCE;
                    myHandle.sendMessage(message);
                } else {

                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject != null) {
                        String operation = jsonObject
                                .getString(TableStructure.COVER_HEAD);
                        if (TableStructure.PUNCHCARD_place.equals(operation)) {
                            JSONArray content = jsonObject
                                    .getJSONArray(TableStructure.COVER_BODY);
                            JSONObject jsonObject2 = content.getJSONObject(0);
                            pcard_place_id = (String) jsonObject2
                                    .getString("PLID");
                            if (pcard_place_id.length() > 5) {
                                get_placeid = true;
                                Message message = new Message();
                                message.what = START_THREAD_GETUP;
                                myHandle.sendMessage(message);
                            } else {
                                get_placeid = false;
                                Message message = new Message();
                                message.what = SET_PCARD_PALCE;
                                myHandle.sendMessage(message);

                            }

                        }

                    }
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    });
    Thread pcardThread = new Thread(new Runnable() {

        @Override
        public void run() {
            GeometryUtil geometryUtil = GeometryUtil.newInstance();
            geometryUtil.createBufferErea(pcard_place_id, "CHECK_NUM",
                    Config.CHECK_PLACE, Config.pCardDistance);

            ispunchCard = geometryUtil.isInErea(new Point(
                    AutoReportLocusService.longitude,
                    AutoReportLocusService.latitude));

            // ispunchCard = geometryUtil
            // .isInErea(new Point(113.456207, 30.372846));

            // boolean ispunchCard = geometryUtil.isInErea(new Point(
            // 112.59458944907746,30.70599721910698), Config.pCardDistance);
            Log.e(TAG, "--" + ispunchCard);
            myHandle.sendEmptyMessage(DISMISS_PRO);
            if (!ispunchCard) {

                Log.e(TAG, "--" + adress);

                myHandle.sendEmptyMessage(MSG_pCARD_CANNOT);
            } else {
                adress = GeometryUtil.adress;
                if (adress == null) {
                    adress = "";
                }

            }

        }
    });
    private Handler myHandle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 12314124:
                    Toast.makeText(PunchCardActivity.this, "请在正确的时间段打卡！",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SET_PCARD_PALCE:
                    progressDialog.dismiss();
                    Toast.makeText(PunchCardActivity.this, "请设置打卡区！",
                            Toast.LENGTH_SHORT).show();

                    break;
                case DISMISS_PRO:
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    break;
                case Config.SHOW_PROGRESS_DIALOG:
                    showProgressDialog(R.string.logining);
                    break;
                case Config.DISMISS_PROGRESS_DIALOG:
                    dismissProgressDialog();
                    break;
                case START_THREAD_GETUP:
                    Thread thread = new Thread(getsetup_thread);
                    thread.start();
                    break;
                case MSG_PCARD_SUCCESS_ORDER:
                    Toast.makeText(PunchCardActivity.this, "打卡成功！",
                            Toast.LENGTH_SHORT).show();
                    break;
                case MSG_pCARD_LOST_ORDER:
                    Toast.makeText(PunchCardActivity.this, "打卡失败！",
                            Toast.LENGTH_SHORT).show();
                    break;
                case MSG_pCARD_CANNOT:

                    Toast.makeText(PunchCardActivity.this, "请到上班区打卡！",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 789:
                    Thread thread4 = new Thread(pcardThread);
                    thread4.start();
                    break;
                case 123:
                    if (!gaptime(record_morning)) {
                        Toast.makeText(PunchCardActivity.this, "当天打卡时间间隔太短！",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Thread thread1 = new Thread(afternoon_Thread);
                        thread1.start();
                    }
                    break;
                case 678:
                    if (!gaptime(record_afternoon)) {
                        Toast.makeText(PunchCardActivity.this, "当天打卡时间间隔太短！",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Thread thread1 = new Thread(evening_Thread);
                        thread1.start();
                    }
                    break;
                case START_THREAD_AFTERNOON:
                    Thread thread1 = new Thread(afternoon_Thread);
                    thread1.start();

                    break;
                case START_THREAD_EVENING:
                    Thread thread2 = new Thread(evening_Thread);
                    thread2.start();
                    break;

            }
        }

    };
    private LoadingProgressDialog progressDialog;

    @Override
    protected void installViews() {
        setContentView(R.layout.punchcard);
        initTitleBar();
        setViewsSize();
        Log.e(TAG, "[]");
        // mPProgressBar = (MPProgressBar) findViewById(R.id.mPProgressBar);

        workingpcard = (FunctionItem) findViewById(R.id.f_lowfun1);
        offworkingpcard = (FunctionItem) findViewById(R.id.f_lowfun2);
        xiawupcard = (FunctionItem) findViewById(R.id.f_lowfun3);
        shangwupcard=(FunctionItem) findViewById(R.id.f_lowfun4);
        progressDialog = new LoadingProgressDialog(this);
        progressDialog.setMessage("加载中...");
        progressDialog.show();
        currtDate = getAppDate();
        Thread sp = new Thread(getidThread);
        sp.start();

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

    private void initTitleBar() {
        mTitleBar = (TitleBar) findViewById(R.id.titlebar);

        mTitleBar.changeStyle(STYLE.NOT_BTN_AND_TITLE);

        mTitleBar.centerTextView.setText(R.string.function_pcard);

    }

    @Override
    protected void registerEvents() {
        workingpcard.setOnClickListener(this);
        offworkingpcard.setOnClickListener(this);
        xiawupcard.setOnClickListener(this);
        shangwupcard.setOnClickListener(this);

    }

    private boolean gaptime(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currDate = format.format(Calendar.getInstance().getTime());
        String str1 = str.replaceAll("/", "-");
        long s = 0;
        try {
            s = format.parse(currDate).getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            if (s - format.parse(str1).getTime() >= Long
                    .parseLong(pcard_gap_time) * 1000 * 60 * 60)

                return true;
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;

    }

    private String getbeijingtime() {
        java.util.Locale locale = java.util.Locale.CHINA; // 这是获得本地中国时区
        String pattern = "yyyy-MM-dd HH:mm:ss ";// 这是日期格式
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(pattern,
                locale);// 设定日期格式
        String pattern1 = "HH:mm:ss ";// 这是日期格式
        java.text.SimpleDateFormat df1 = new java.text.SimpleDateFormat(
                pattern1, locale);// 设定日期格式
        java.util.Date date = new java.util.Date();
        java.net.URL url = null;
        try {
            url = new URL("http://www.bjtime.cn");
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 取得资源对象
        java.net.URLConnection uc = null;
        try {
            uc = url.openConnection();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 生成连接对象
        try {
            uc.connect();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // 发出连接
        long ld = uc.getDate(); // 取得网站日期时间
        date = new Date(ld); // 转换为标准时间对象
        String bjTime = df.format(date);
        stime = df1.format(date);
        System.out.println("北京时间:" + bjTime);
        return bjTime;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.f_lowfun1:
                if (!get_placeid) {
                    Toast.makeText(PunchCardActivity.this, "请设置打卡区！",
                            Toast.LENGTH_SHORT).show();
                } else if (!ispunchCard) {
                    Toast.makeText(PunchCardActivity.this, "请到上班区打卡！",
                            Toast.LENGTH_SHORT).show();
                } else {

                    Thread thread1 = new Thread(morning_Thread);
                    thread1.start();
                }

                // Toast.makeText(this, "已打卡", Toast.LENGTH_SHORT).show();

                break;
            case R.id.f_lowfun4:
                if (!get_placeid) {
                    Toast.makeText(PunchCardActivity.this, "请设置打卡区！",
                            Toast.LENGTH_SHORT).show();
                } else if (!ispunchCard) {
                    Toast.makeText(PunchCardActivity.this, "请到上班区打卡！",
                            Toast.LENGTH_SHORT).show();
                } else {

                    Thread thread1 = new Thread(evening_Thread);
                    thread1.start();
                }
                // Toast.makeText(this, "已打卡", Toast.LENGTH_SHORT).show();
                break;
            case R.id.f_lowfun3:
                if (!get_placeid) {
                    Toast.makeText(PunchCardActivity.this, "请设置打卡区！",
                            Toast.LENGTH_SHORT).show();
                } else if (!ispunchCard) {
                    Toast.makeText(PunchCardActivity.this, "请到上班区打卡！",
                            Toast.LENGTH_SHORT).show();
                } else {

                    Thread thread1 = new Thread(afternoon_Thread);
                    thread1.start();
                }
                break;
            case R.id.f_lowfun2:
                if (!get_placeid) {
                    Toast.makeText(PunchCardActivity.this, "请设置打卡区！",
                            Toast.LENGTH_SHORT).show();
                } else if (!ispunchCard) {
                    Toast.makeText(PunchCardActivity.this, "请到上班区打卡！",
                            Toast.LENGTH_SHORT).show();
                } else {

                    Thread thread1 = new Thread(morningoff_Thread);
                    thread1.start();
                }
                break;

            default:
                break;
        }

    }

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

    private int jsonParseToOrder(String response) throws JSONException {
        if (null != response && !"".equals(response)) {
            JSONObject jsonObject = new JSONObject(response);

            if (null != jsonObject) {

                String operation = jsonObject
                        .getString(TableStructure.COVER_HEAD);

                if (TableStructure.PUNCHCARD.equals(operation)) {
                    JSONObject content = (JSONObject) ((jsonObject
                            .get(TableStructure.COVER_BODY)));
                    if ("1".equals(content
                            .getString(TableStructure.R_USER_RESPONSE_KEY))) {
                        return MSG_PCARD_SUCCESS_ORDER;

                    }
                }
            }
        }
        return MSG_pCARD_LOST_ORDER;

    }

    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(Calendar.getInstance().getTime());
        return date;

    }

    private String getAppDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentDate = sdf.format(new Date());
        return currentDate;
    }

    private long getNetDate() {
        LocationManager locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        Location location = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long time = locationManager.getLastKnownLocation(
                LocationManager.NETWORK_PROVIDER).getTime();
        return time;

        // URL url;
        // Date date = null;
        // try {
        // url = new URL("http://www.bjtime.cn");// 取得资源对象
        // URLConnection uc = url.openConnection();// 生成连接对象
        // uc.connect(); // 发出连接
        // long ld = uc.getDate(); // 取得网站日期时间
        // date = new Date(ld); // 转换为标准时间对象
        //
        // } catch (MalformedURLException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // return date;

    }

    private boolean punchpard(String str) {
        Date date1 = null, date2 = null, date3 = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            long l1 = sdf.parse(str).getTime() + Long.parseLong(allgap_time)
                    * 1000 * 60;
            long l2 = sdf.parse(str).getTime() - Long.parseLong(allgap_time)
                    * 1000 * 60;
            date1 = new Date(l1);
            date2 = new Date(l2);

            String str1 = getbeijingtime();
            date3 = sdf.parse(stime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (date3.before(date1) && date3.after(date2)) {
            return true;
        }
        return false;
    }

    private boolean CanblePCard(String currDate1, String startDate1) {
        Date currDate = null;
        Date startDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        // String currentDate = sdf.format(new Date());

        try {
            currDate = sdf.parse(currDate1);
            startDate = sdf.parse(startDate1);// 开始时间
            // endDate = sdf.parse(endDate1);// 结束时间

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // if (currDate.after(startDate) && currDate.before(endDate)) {
        // return true;
        // }
        if (currDate.after(startDate)) {
            return true;
        }
        return false;
        // SimpleDateFormat format = new
        // SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        // String date = format.format(Calendar.getInstance().getTime());
    }

}
