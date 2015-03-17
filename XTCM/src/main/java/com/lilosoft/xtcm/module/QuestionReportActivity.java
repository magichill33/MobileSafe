package com.lilosoft.xtcm.module;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.HomeBaseActivity;
import com.lilosoft.xtcm.base.TabBaseActivity;
import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.constant.TableStructure;
import com.lilosoft.xtcm.constant.TypeContent;
import com.lilosoft.xtcm.database.DatabaseFactory;
import com.lilosoft.xtcm.instantiation.EventKings;
import com.lilosoft.xtcm.instantiation.FileBean;
import com.lilosoft.xtcm.instantiation.ReadyReportBean;
import com.lilosoft.xtcm.instantiation.User;
import com.lilosoft.xtcm.network.HttpConnection;
import com.lilosoft.xtcm.utils.ImageTool;
import com.lilosoft.xtcm.utils.JsonParseInterface;
import com.lilosoft.xtcm.utils.LogFactory;

/**
 * @category 问题上报
 * @author William Liu
 *
 */
@SuppressLint("SimpleDateFormat")
public class QuestionReportActivity extends TabBaseActivity implements
        OnClickListener, OnLongClickListener, JsonParseInterface {

    /**
     * 上报成功
     */
    private final static int MSG_REPORT_SUCCESS_ORDER = 0x0FFFFFFF;
    /**
     * 上报失败
     */
    private final static int MSG_REPORT_LOST_ORDER = 0x00FFFFFF;
    private final static int INIT = 0x0F1;
    /*
     * 事件分类属性
     */
    public static String PID = "bigMC";
    public static String CID = "bigID";
    public static List<EventKings> eventList = null;
    private final String TAG = "QuestionReportActivity";
    List<String> bigLists = null;
    List<String> middless = null;
    List<String> smallss = null;
    String middlename = null;
    String smallname = null;
    String code = null;
    String layer = null;
    private View layout_info;
    private View layout_media;
    private Spinner type, type1, type2;// 事件分类
    private int typePosition = 0;
    private int type1Position = 0;
    private int type2Position = 0;
    private ArrayAdapter<String> adapter, adapter1, adapter2;
    private String[] typeData;
    private String[][] type1Data;
    private String[][][] type2Data;
    private String[] typeId;
    private String[][] type1Id;
    private String[][][] type2Id;
    private TextView map;
    private EditText descript;
    private Intent selectImgIntent, selectAudioIntent,
            takePictureFromCameraIntent;
    private String SD_CARD_TEMP_DIR;
    private MENUTYPE menu;
    private int id = 0;
    private String mapStr = "";
    private Drawable drawable, drawable1, drawable2, drawable3;
    private String s1 = "", s2 = "", s3 = "";
    private String media_p11text = "", media_p21text = "", media_p31text = "";
    private ImageView media_p11, media_p21, media_p31;
    private TextView media_rt11, media_rt21, media_rt31;
    private ScaleAnimation plusScaleAnimation = null;
    private Message m;
    private Thread initThread = new Thread(new Runnable() {
        @Override
        public void run() {

            LogFactory.e(TAG, "init thread start");
            m = new Message();
            m.what = INIT;
            myHandle.sendMessage(m);

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
            dismissProgressDialog();
            switch (msg.what) {
                case INIT:
                    hasDataThenInit();
                    break;
                case Config.SHOW_PROGRESS_DIALOG:
                    HomeBaseActivity.showProgressDialog("交互中…");
                    break;
                case Config.DISMISS_PROGRESS_DIALOG:
                    HomeBaseActivity.dismissProgressDialog();
                    break;
                case MSG_REPORT_SUCCESS_ORDER:
                    LogFactory.e(TAG, "report Success");
                    reset();
                    break;
                case MSG_REPORT_LOST_ORDER:
                    if (null != err_Msg && !"".equals(err_Msg)) {
                        Toast.makeText(mContext, err_Msg, Toast.LENGTH_LONG).show();
                        err_Msg = "";
                    } else {
                        Toast.makeText(mContext, R.string.error_data,
                                Toast.LENGTH_LONG).show();
                    }
                    LogFactory.e(TAG, "report lost");
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
    private View submit;
    private View save;
    private EditText etCaseNum;
    private String casesNum;
    // 字符输入监听器
    TextWatcher caseNumListener = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            int len = s.toString().length();
            if (len == 1 && text.equals("0")) {// 判断不能输入0或者0开头的数字
                s.clear();
            } else {
                casesNum = s.toString();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }
    };
    /**
     * @category 问题上报请求处理
     */
    private Thread reportThread = new Thread(new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            LogFactory.e(TAG, "report thread start");
            m = new Message();
            m.what = Config.SHOW_PROGRESS_DIALOG;
            myHandle.sendMessage(m);
            try {
                String imgName1 = "";
                String imgData1 = "";
                String imgType1 = "";
                String imgName2 = "";
                String imgData2 = "";
                String imgType2 = "";
                String imgName3 = "";
                String imgData3 = "";
                String imgType3 = "";
                String[] mapInfo = mapStr.split(",");
                if ("" != (media_p11text)) {
                    String img1 = media_p11text;
                    int begin1 = img1.lastIndexOf("/") + 1;
                    int end1 = img1.lastIndexOf(".");
                    imgName1 = img1.substring(begin1, end1)
                            + ("jpeg".equals(img1.substring(end1 + 1)) ? ".jpg"
                            : img1.substring(end1));
                    imgType1 = "jpeg".equals(img1.substring(end1 + 1)) ? "jpg"
                            : img1.substring(end1 + 1);
                    imgData1 = s1;
                    if ("" != (media_p21text)) {
                        String img2 = media_p21text;
                        int begin2 = img2.lastIndexOf("/") + 1;
                        int end2 = img2.lastIndexOf(".");
                        imgName2 = img2.substring(begin2, end2)
                                + ("jpeg".equals(img2.substring(end2 + 1)) ? ".jpg"
                                : img2.substring(end2));
                        imgType2 = "jpeg".equals(img2.substring(end2 + 1)) ? "jpg"
                                : img2.substring(end2 + 1);
                        imgData2 = s2;
                        if ("" != (media_p31text)) {
                            String img3 = media_p31text;
                            int begin3 = img3.lastIndexOf("/") + 1;
                            int end3 = img3.lastIndexOf(".");
                            imgName3 = img3.substring(begin3, end3)
                                    + ("jpeg".equals(img3.substring(end3 + 1)) ? ".jpg"
                                    : img3.substring(end3));
                            imgType3 = "jpeg".equals(img3.substring(end3 + 1)) ? "jpg"
                                    : img3.substring(end3 + 1);
                            imgData3 = s3;
                        }
                    }
                }

                /**
                 * 获取第三级事件分类ID
                 */
                List<String> middles = new ArrayList<String>();// 第二级ID集合
                List<String> middleNames = new ArrayList<String>();// 第二级Name集合
                List<String> smalls = new ArrayList<String>();// 第三级ID集合
                List<String> smallNames = new ArrayList<String>();// 第三级Name集合
                String[] bigArray = new AutoUpdateEvent(
                        QuestionReportActivity.this).getBigEvent(CID);// 获取第一级ID数组
                String[] bigNameArray = new AutoUpdateEvent(
                        QuestionReportActivity.this).getBigEvent(PID);// 获取第一级名称数组
                String bigID = bigArray[type.getSelectedItemPosition()];// 获取当前选取的ID
                String bigName = bigNameArray[type.getSelectedItemPosition()];// 获取当前选取的Name
                for (int i = 0; i < eventList.size(); i++) {
                    EventKings event = eventList.get(i);
                    if (bigID.equals(event.getParentid())) {
                        middles.add(event.getId());
                        middleNames.add(event.getMc());
                    }
                }
                if (middles != null && middles.size() > 0) {
                    String middleID = middles.get(type1
                            .getSelectedItemPosition());// 获取当前第二级ID
                    for (int i = 0; i < eventList.size(); i++) {
                        EventKings event = eventList.get(i);
                        if (middleID.equals(event.getParentid())) {
                            smalls.add(event.getId());
                            smallNames.add(event.getMc());
                        }
                    }
                }
                String smallID = smalls.get(type2.getSelectedItemPosition());// 第三级ID
                String middleName = middleNames.get(type1
                        .getSelectedItemPosition());// 第二级Name
                String smallName = smallNames.get(type2
                        .getSelectedItemPosition());// 第二级Name
                // 案件数量
                // casesNum = etCaseNum.getText().toString();

                HttpConnection httpConnection = new HttpConnection();
                action(httpConnection.getData(
                        HttpConnection.CONNECTION_COMMON_REPORT,
                        casesNum == null || "".equals(casesNum) ? "1"
                                : casesNum,
                        User.username,
                        // type2Id[typePosition][type1Position][type2Position],
                        // typeData[typePosition]
                        // + "-"
                        // + type1Data[typePosition][type1Position]
                        // + "-"
                        // +
                        // type2Data[typePosition][type1Position][type2Position],
                        smallID, bigName + "-" + middleName + "-" + smallName,
                        descript.getText().toString(), mapInfo[0], mapInfo[1],
                        mapInfo[2], imgName1, imgData1, imgType1, imgName2,
                        imgData2, imgType2, imgName3, imgData3, imgType3));

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
                LogFactory.e(TAG, "report thread end");
            }
        }

    });
    //存储部件上报需要传递的信息
    private HashMap<String, String> events = new HashMap<String, String>();
    private HashMap<String, String> eventsLayer = new HashMap<String, String>();
    private String oBJECTID;
    private String oBJCODE;
    private String oBJNAME;
    private String orgCode;
    private String deptCode1;
    private String deptName1;
    private String deptCode2;
    private String deptName2;
    private String deptCode3;
    private String deptName3;
    private String lon;
    private String lat;
    /**
     * @category 问题上报请求处理
     */
    private Thread partReportThread = new Thread(new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            LogFactory.e(TAG, "report thread start");
            m = new Message();
            m.what = Config.SHOW_PROGRESS_DIALOG;
            myHandle.sendMessage(m);
            try {
                String imgName1 = "";
                String imgData1 = "";
                String imgType1 = "";
                String imgName2 = "";
                String imgData2 = "";
                String imgType2 = "";
                String imgName3 = "";
                String imgData3 = "";
                String imgType3 = "";
//				String[] mapInfo = mapStr.split(",");
                if ("" != (media_p11text)) {
                    String img1 = media_p11text;
                    int begin1 = img1.lastIndexOf("/") + 1;
                    int end1 = img1.lastIndexOf(".");
                    imgName1 = img1.substring(begin1, end1)
                            + ("jpeg".equals(img1.substring(end1 + 1)) ? ".jpg"
                            : img1.substring(end1));
                    imgType1 = "jpeg".equals(img1.substring(end1 + 1)) ? "jpg"
                            : img1.substring(end1 + 1);
                    imgData1 = s1;
                    if ("" != (media_p21text)) {
                        String img2 = media_p21text;
                        int begin2 = img2.lastIndexOf("/") + 1;
                        int end2 = img2.lastIndexOf(".");
                        imgName2 = img2.substring(begin2, end2)
                                + ("jpeg".equals(img2.substring(end2 + 1)) ? ".jpg"
                                : img2.substring(end2));
                        imgType2 = "jpeg".equals(img2.substring(end2 + 1)) ? "jpg"
                                : img2.substring(end2 + 1);
                        imgData2 = s2;
                        if ("" != (media_p31text)) {
                            String img3 = media_p31text;
                            int begin3 = img3.lastIndexOf("/") + 1;
                            int end3 = img3.lastIndexOf(".");
                            imgName3 = img3.substring(begin3, end3)
                                    + ("jpeg".equals(img3.substring(end3 + 1)) ? ".jpg"
                                    : img3.substring(end3));
                            imgType3 = "jpeg".equals(img3.substring(end3 + 1)) ? "jpg"
                                    : img3.substring(end3 + 1);
                            imgData3 = s3;
                        }
                    }
                }

                /**
                 * 获取第三级事件分类ID
                 */
                List<String> middles = new ArrayList<String>();// 第二级ID集合
                List<String> middleNames = new ArrayList<String>();// 第二级Name集合
                List<String> smalls = new ArrayList<String>();// 第三级ID集合
                List<String> smallNames = new ArrayList<String>();// 第三级Name集合
                String[] bigArray = new AutoUpdateEvent(
                        QuestionReportActivity.this).getBigEvent(CID);// 获取第一级ID数组
                String[] bigNameArray = new AutoUpdateEvent(
                        QuestionReportActivity.this).getBigEvent(PID);// 获取第一级名称数组
                String bigID = bigArray[type.getSelectedItemPosition()];// 获取当前选取的ID
                String bigName = bigNameArray[type.getSelectedItemPosition()];// 获取当前选取的Name
                for (int i = 0; i < eventList.size(); i++) {
                    EventKings event = eventList.get(i);
                    if (bigID.equals(event.getParentid())) {
                        middles.add(event.getId());
                        middleNames.add(event.getMc());
                    }
                }
                if (middles != null && middles.size() > 0) {
                    String middleID = middles.get(type1
                            .getSelectedItemPosition());// 获取当前第二级ID
                    for (int i = 0; i < eventList.size(); i++) {
                        EventKings event = eventList.get(i);
                        if (middleID.equals(event.getParentid())) {
                            smalls.add(event.getId());
                            smallNames.add(event.getMc());
                        }
                    }
                }
                String smallID = smalls.get(type2.getSelectedItemPosition());// 第三级ID
                String middleName = middleNames.get(type1
                        .getSelectedItemPosition());// 第二级Name
                String smallName = smallNames.get(type2
                        .getSelectedItemPosition());// 第二级Name
                // 案件数量
                // casesNum = etCaseNum.getText().toString();

                HttpConnection httpConnection = new HttpConnection();
                action(httpConnection.getData(
                        HttpConnection.CONNECTION_PART_REPORT,

                        User.username,
                        // type2Id[typePosition][type1Position][type2Position],
                        // typeData[typePosition]
                        // + "-"
                        // + type1Data[typePosition][type1Position]
                        // + "-"
                        // +
                        // type2Data[typePosition][type1Position][type2Position],
                        smallID, bigName + "-" + middleName + "-" + smallName,
                        descript.getText().toString(), lon, lat,
                        imgName1, imgData1, imgType1, imgName2, imgData2,
                        imgType2, imgName3, imgData3, imgType3, oBJECTID,
                        oBJCODE, oBJNAME, orgCode, deptCode1, deptName1,
                        deptCode2, deptName2, deptCode3, deptName3,
                        casesNum == null || "".equals(casesNum) ? "1"
                                : casesNum,layer));

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
                LogFactory.e(TAG, "report thread end");
            }
        }

    });

    @Override
    protected void installViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_question_report);

        layout_info = findViewById(R.id.layout_info);
        layout_media = findViewById(R.id.layout_media);

        type = (Spinner) findViewById(R.id.type);
        type1 = (Spinner) findViewById(R.id.type1);
        type2 = (Spinner) findViewById(R.id.type2);
        etCaseNum = (EditText) findViewById(R.id.et_casesnum);
        map = (TextView) findViewById(R.id.map);
        descript = (EditText) findViewById(R.id.descript);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        plusScaleAnimation = new ScaleAnimation(0f, 1f, 0f, // x轴缩放比例，y轴缩放比例
                1f, 0f, 0f); // 左上角开始
        plusScaleAnimation.setDuration(200);
        // 处理前

        media_p11 = (ImageView) findViewById(R.id.media_p11);
        media_p11.setAnimation(plusScaleAnimation);

        media_p21 = (ImageView) findViewById(R.id.media_p21);
        media_p21.setAnimation(plusScaleAnimation);

        media_p31 = (ImageView) findViewById(R.id.media_p31);
        media_p31.setAnimation(plusScaleAnimation);

        // 音频
        media_rt11 = (TextView) findViewById(R.id.media_rt11);

        media_rt21 = (TextView) findViewById(R.id.media_rt21);

        media_rt31 = (TextView) findViewById(R.id.media_rt31);

        submit = findViewById(R.id.submit);
        save = findViewById(R.id.save);

        etCaseNum.addTextChangedListener(caseNumListener);

        inflateType();
    }

    @Override
    protected void registerEvents() {
        // TODO Auto-generated method stub

        map.setOnClickListener(this);

        // 处理前
        media_p11.setOnClickListener(this);
        media_p11.setOnLongClickListener(this);

        media_p21.setOnClickListener(this);
        media_p21.setOnLongClickListener(this);

        media_p31.setOnClickListener(this);
        media_p31.setOnLongClickListener(this);

        // 音频

        // media_r11.setOnClickListener(this);
        // media_r11.setOnLongClickListener(this);
        //
        // media_r21.setOnClickListener(this);
        // media_r21.setOnLongClickListener(this);
        //
        // media_r31.setOnClickListener(this);
        // media_r31.setOnLongClickListener(this);

        submit.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    private void reset() {
        // typePosition = 0;
        // type1Position = 0;
        // type2Position = 0;
        // type.setAdapter(adapter);
        // type1.setAdapter(adapter1);
        // type2.setAdapter(adapter2);
        // etCaseNum.setText("");
        // map.setText("点击选择");
        descript.setText("");
        // etCaseNum.setText("");
        drawable1 = null;
        drawable2 = null;
        drawable3 = null;
        s1 = "";
        s2 = "";
        s3 = "";
        media_p11.setImageResource(R.drawable.add_button);
        media_p21.setImageResource(R.drawable.add_button);
        media_p31.setImageResource(R.drawable.add_button);
        media_p21.setVisibility(View.GONE);
        media_p31.setVisibility(View.GONE);
        media_p11text = "";
        media_p21text = "";
        media_p31text = "";
        media_rt11.setText("");
        media_rt21.setText("");
        media_rt31.setText("");
        media_rt11.setVisibility(View.GONE);
        media_rt21.setVisibility(View.GONE);
        media_rt31.setVisibility(View.GONE);
        Toast.makeText(mContext, err_Msg, Toast.LENGTH_LONG).show();
        // HomeBaseActivity.tabHost.setCurrentTabByTag(Config.A_TAB);
        // HomeBaseActivity.tabBt1.setChecked(true);
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
        // TODO Auto-generated method stub
        // {"head":"approve","body":{"isSuccess":"1","message":"提交成功"}}

        if (null != response && !"".equals(response)) {
            JSONObject jsonObject = new JSONObject(response);

            if (null != jsonObject) {

                String operation = jsonObject
                        .getString(TableStructure.COVER_HEAD);

                if (TableStructure.V_ACT_REPORT.equals(operation)||TableStructure.V_PART_REPORT.equals(operation)) {
                    JSONObject content = (JSONObject) ((jsonObject
                            .get(TableStructure.COVER_BODY)));
                    if ("1".equals(content
                            .getString(TableStructure.R_USER_RESPONSE_KEY))) {
                        if (null != ReadyReportActivity.readyReportBean) {
                            DatabaseFactory databaseFactory = new DatabaseFactory(
                                    this);
                            databaseFactory
                                    .delete(ReadyReportActivity.readyReportBean
                                            .getQ_QUESTION_ID());
                            ReadyReportActivity.readyReportBean = null;
                        }
                        err_Msg = content
                                .getString(TableStructure.R_USER_RESPONSE_MSG);

                        return MSG_REPORT_SUCCESS_ORDER;
                    } else {
                        err_Msg = content
                                .getString(TableStructure.R_USER_RESPONSE_MSG);
                    }
                }
            }
        } else {
            err_Msg = "数据异常！";
            LogFactory.e(TAG, "not data!");
        }

        return MSG_REPORT_LOST_ORDER;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.map:
                if(!type.getSelectedItem().toString().equals("部件分类")){
                    startActivityForResult(new Intent(mContext, MapActivity.class),
                            R.id.map);
                }else{
                    code = events.get(type2.getSelectedItem().toString());
                    layer = eventsLayer.get(type2.getSelectedItem().toString());
                    if (TextUtils.isEmpty(layer)) {
                        Toast.makeText(this, "图层为空", 1).show();
                        return;
                    } else {
                        LogFactory.e(TAG, "code = " + code);
                        LogFactory.e(TAG, "layer = " + layer);
                    }
                    Intent intent = new Intent(this, GatherPartsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("CODE", code);
                    bundle.putString("LAYER", layer);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, R.id.map+1);
                }
                break;
            case R.id.submit:
                boolean finishKey = true;
                // if (0 == typePosition) {
                // Toast.makeText(mContext, "请选择类型！！", Toast.LENGTH_SHORT).show();
                // finishKey = false;
                // }
                if (-1 == type2.getSelectedItemPosition()) {
                    Toast.makeText(mContext, "请选择类型！！", Toast.LENGTH_SHORT).show();
                    finishKey = false;
                } else if ("请选择位置".equals(map.getText().toString())) {
                    Toast.makeText(mContext, "请在地图上选择位置！", Toast.LENGTH_SHORT)
                            .show();
                    finishKey = false;
                } else if ("".equals(descript.getText().toString())) {
                    Toast.makeText(mContext, "问题描述不能为空！", Toast.LENGTH_SHORT)
                            .show();
                    finishKey = false;
                } else if ("".equals(s1)) {
                    Toast.makeText(mContext, "多媒体不能为空！", Toast.LENGTH_SHORT).show();
                    finishKey = false;
                } else if (finishKey) {
                    if (Config.NETWORK) {
                        showProgressDialog("正在上报案件，请稍等！");
                        if(type.getSelectedItem().toString().equals("部件分类")){
                            threadG = new Thread(partReportThread);
                            threadG.start();
                        }else{
                            threadG = new Thread(reportThread);
                            threadG.start();
                        }
                    } else {
                        Toast.makeText(mContext, "无网模式-上报！", Toast.LENGTH_SHORT)
                                .show();
                    }

                    // Writer writer = new FileWriter(new File(
                    // XTCMConfig.FILES_NAME_URL + "abc.txt"));
                    // writer.write(s1);
                    // writer.flush();
                    // writer.close();
                    // reset();
                    try {
                        ImageTool.writeFile(new FileBean("abc.txt", "", s1));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.save:
                boolean saveKey = true;
                // if (0 == typePosition) {
                // Toast.makeText(mContext, "请选择类型！！", Toast.LENGTH_SHORT).show();
                // saveKey = false;
                // }
                if (-1 == type2.getSelectedItemPosition()) {
                    Toast.makeText(mContext, "请选择类型！！", Toast.LENGTH_SHORT).show();
                    finishKey = false;
                } else if ("请选择位置".equals(map.getText().toString())) {
                    Toast.makeText(mContext, "请在地图上选择位置！", Toast.LENGTH_SHORT)
                            .show();
                    saveKey = false;
                } else if ("".equals(descript.getText().toString())) {
                    Toast.makeText(mContext, "问题描述不能为空！", Toast.LENGTH_SHORT)
                            .show();
                    saveKey = false;
                } else if ("".equals(s1)) {
                    Toast.makeText(mContext, "处理前图片不能为空！", Toast.LENGTH_SHORT)
                            .show();
                    saveKey = false;
                }
                if (saveKey) {
                    /**
                     * 获取第三级事件分类ID
                     */
                    List<String> middles = new ArrayList<String>();// 第二级ID集合
                    List<String> middleNames = new ArrayList<String>();// 第二级Name集合
                    List<String> smalls = new ArrayList<String>();// 第三级ID集合
                    List<String> smallNames = new ArrayList<String>();// 第三级Name集合
                    String[] bigArray = new AutoUpdateEvent(
                            QuestionReportActivity.this).getBigEvent(CID);// 获取第一级ID数组
                    String bigID = bigArray[type.getSelectedItemPosition()];// 获取当前选取的ID
                    String middleID = null;
                    for (int i = 0; i < eventList.size(); i++) {
                        EventKings event = eventList.get(i);
                        if (bigID.equals(event.getParentid())) {
                            middles.add(event.getId());
                            middleNames.add(event.getMc());
                        }
                    }
                    if (middles != null && middles.size() > 0) {
                        middleID = middles.get(type1.getSelectedItemPosition());// 获取当前第二级ID
                        for (int i = 0; i < eventList.size(); i++) {
                            EventKings event = eventList.get(i);
                            if (middleID.equals(event.getParentid())) {
                                smalls.add(event.getId());
                                smallNames.add(event.getMc());
                            }
                        }
                    }
                    String smallID = smalls.get(type2.getSelectedItemPosition());// 第三级ID
                    // 案件数量
                    // String casesNum = etCaseNum.getText().toString();
                    DatabaseFactory databaseFactory = new DatabaseFactory(this);
                    ReadyReportBean bean = new ReadyReportBean();
                    // bean.setQ_QUESTION_TYPE(typeId[typePosition]);
                    // bean.setQ_QUESTION_TYPE1(type1Id[typePosition][type1Position]);
                    // bean.setQ_QUESTION_TYPE2(type2Id[typePosition][type1Position][type2Position]);
                    bean.setQ_QUESTION_TYPE(bigID);
                    bean.setQ_QUESTION_TYPE1(middleID);
                    bean.setQ_QUESTION_TYPE2(smallID);
                    bean.setQ_QUESTION_LOCATION(mapStr);
                    bean.setQ_QUESTION_DESCRIPT(descript.getText().toString());
                    bean.setQ_QUESTION_BEFOR_IMG1(media_p11text);
                    bean.setQ_QUESTION_BEFOR_IMG2(media_p21text);
                    bean.setQ_QUESTION_BEFOR_IMG3(media_p31text);
                    bean.setQ_QUESTION_AFTER_IMG1("");
                    bean.setQ_QUESTION_AFTER_IMG2("");
                    bean.setQ_QUESTION_AFTER_IMG3("");
                    bean.setQ_QUESTION_REC1("");
                    bean.setQ_QUESTION_REC2("");
                    bean.setQ_QUESTION_REC3("");
                    bean.setQ_QUESTION_CASESNUM(casesNum == null
                            || "".equals(casesNum) ? "1" : casesNum);
                    if (null != ReadyReportActivity.readyReportBean) {
                        bean.setQ_QUESTION_ID(ReadyReportActivity.readyReportBean
                                .getQ_QUESTION_ID());
                        databaseFactory.update(bean);
                        Toast.makeText(mContext, "已修改！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!databaseFactory.check(bean)) {
                            databaseFactory.insert(bean);
                            Toast.makeText(mContext, "已保存！", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(mContext, "不能重复保存！", Toast.LENGTH_SHORT)
                                    .show();
                        }

                    }
                    databaseFactory.close();
                    ReadyReportActivity.readyReportBean = null;
                }
                break;
            default:
                openOptionsMenu(MENUTYPE.camera_operate_, v.getId());
                break;
        }
    }

    /***
     * 无内容不操作
     */
    @Override
    public boolean onLongClick(View v) {
        // TODO Auto-generated method stub
        boolean show = false;
        switch (v.getId()) {
            case R.id.media_p11:
                if (media_p11text != "") {
                    show = true;
                }
                break;
            case R.id.media_p21:
                if (media_p21text != "") {
                    show = true;
                }
                break;
            case R.id.media_p31:
                if (media_p31text != "") {
                    show = true;
                }
                break;
        }
        if (show) {
            openOptionsMenu(MENUTYPE.delete_, v.getId());
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        return super.onCreateOptionsMenu(menu);
        // return false;
    }

    /**
     *
     * @param MENUTYPE
     *            operate_ / delete_
     */
    public void openOptionsMenu(MENUTYPE menuKey, int id) {
        // TODO Auto-generated method stub
        this.menu = menuKey;
        this.id = id;
        super.openOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        if (this.menu == MENUTYPE.camera_operate_) {
            inflater.inflate(R.menu.operate_pic, menu);
        } else if (this.menu == MENUTYPE.delete_) {
            inflater.inflate(R.menu.operate_pic_d, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {

            /**
             * 拍照
             */
            case R.id.menu_take:
                takePictureFromCameraIntent();
                startActivityForResult(takePictureFromCameraIntent, 4);
                break;
            /**
             * 从相册获取
             */
            case R.id.menu_choose:
                startActivityForResult(selectImgIntent, 3);
                break;
            case R.id.menu_delete:
                switch (id) {
                    case R.id.media_p11:
                        if ("" != media_p31text) {
                            drawable1 = drawable2;
                            drawable2 = drawable3;
                            drawable3 = null;
                            s1 = s2;
                            s2 = s3;
                            s3 = "";
                            media_p11text = media_p21text;
                            media_p21text = media_p31text;
                            media_p31text = "";
                            media_p11.setImageDrawable(drawable1);
                            media_p21.setImageDrawable(drawable2);
                            media_p31.setImageResource(R.drawable.add_button);
                        } else if ("" != media_p21text) {
                            drawable1 = drawable2;
                            drawable2 = null;
                            s1 = s2;
                            s2 = "";
                            media_p11text = media_p21text;
                            media_p21text = "";
                            media_p11.setImageDrawable(drawable1);
                            media_p21.setImageResource(R.drawable.add_button);
                            media_p31.setVisibility(View.GONE);
                        } else {
                            drawable1 = null;
                            s1 = "";
                            media_p11text = "";
                            media_p11.setImageResource(R.drawable.add_button);
                            media_p21.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.media_p21:
                        if ("" != media_p31text) {
                            drawable2 = drawable3;
                            drawable3 = null;
                            s2 = s3;
                            s3 = "";
                            media_p21text = media_p31text;
                            media_p31text = "";
                            media_p21.setImageDrawable(drawable2);
                            media_p31.setImageResource(R.drawable.add_button);
                        } else {
                            drawable2 = null;
                            s2 = "";
                            media_p21text = "";
                            media_p21.setImageResource(R.drawable.add_button);
                            media_p31.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.media_p31:
                        s3 = "";
                        drawable3 = null;
                        media_p31text = "";
                        media_p31.setImageResource(R.drawable.add_button);
                        break;
                    case R.id.media_r11:

                        break;
                    case R.id.media_r21:

                        break;
                    case R.id.media_r31:

                        break;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        // TODO Auto-generated method stub
        this.menu = null;
        // this.id = 0;
        super.onOptionsMenuClosed(menu);
    }

    private void inflateType() {

        selectImgIntent = new Intent(Intent.ACTION_GET_CONTENT);
        selectImgIntent.addCategory(Intent.CATEGORY_OPENABLE);
        selectImgIntent.setType("image/*");
        // selectImgIntent.setType("audio/*"); // 选择音频
        // selectImgIntent.setType("video/*"); // 选择视频 （mp4 3gp 是android支持的视频格式）
        // selectImgIntent.setType("video/*;image/*");// 同时选择视频和图片
        // selectImgIntent.putExtra("crop", "true"); // 裁剪
        // selectImgIntent.putExtra("aspectX", 1);
        // selectImgIntent.putExtra("aspectY", 1);
        // selectImgIntent.putExtra("outputX", 80);
        // selectImgIntent.putExtra("outputY", 80);
        // selectImgIntent.putExtra("return-data", true);
        selectAudioIntent = new Intent(Intent.ACTION_GET_CONTENT);
        selectAudioIntent.addCategory(Intent.CATEGORY_OPENABLE);
        selectAudioIntent.setType("audio/*");

        // typeData = TypeContent.TYPE_TEXT;
        typeData = new AutoUpdateEvent(this).getBigEvent(PID);
        eventList = AutoUpdateEvent.eventList;
        type1Data = TypeContent.TYPE1_TEXT;
        type2Data = TypeContent.TYPE2_TEXT;
        typeId = TypeContent.TYPE_ID;
        type1Id = TypeContent.TYPE1_ID;
        type2Id = TypeContent.TYPE2_ID;

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, typeData);
        adapter.setDropDownViewResource(R.layout.view_drop_down_item);

        // adapter1 = new ArrayAdapter<String>(this,
        // android.R.layout.simple_spinner_item, type1Data[typePosition]);
        // adapter1.setDropDownViewResource(R.layout.view_drop_down_item);

        // adapter2 = new ArrayAdapter<String>(this,
        // android.R.layout.simple_spinner_item,
        // type2Data[typePosition][type1Position]);
        // adapter2.setDropDownViewResource(R.layout.view_drop_down_item);

        type.setAdapter(adapter);
        // type1.setAdapter(adapter1);
        // type2.setAdapter(adapter2);

        type.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if(type.getSelectedItem().toString().equals("部件分类")){
                    save.setVisibility(View.INVISIBLE);
                }else{
                    save.setVisibility(View.VISIBLE);
                }
                typePosition = arg2;
                // adapter1 = new ArrayAdapter<String>(mContext,
                // android.R.layout.simple_spinner_item, type1Data[arg2]);
                // adapter1.setDropDownViewResource(R.layout.view_drop_down_item);
                // type1.setAdapter(adapter1);
                String[] bigIds = new AutoUpdateEvent(
                        QuestionReportActivity.this).getBigEvent(CID);
                middless = new ArrayList<String>();
                for (int i = 0; i < eventList.size(); i++) {
                    EventKings kings = eventList.get(i);
                    if (kings.getParentid().equals(bigIds[arg2])) {
                        middless.add(kings.getMc());
                    }
                }
                adapter1 = new ArrayAdapter<String>(
                        QuestionReportActivity.this,
                        android.R.layout.simple_spinner_item, middless);
                adapter1.setDropDownViewResource(R.layout.view_drop_down_item);
                type1.setAdapter(adapter1);
                type1.setSelection(0, false);
                if (middlename != null && !"".equals(middlename)) {
                    type1.setSelection(getListIndex(middless, middlename), true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        type1.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                type1Position = arg2;
                // adapter2 = new ArrayAdapter<String>(mContext,
                // android.R.layout.simple_spinner_item,
                // type2Data[typePosition][arg2]);
                // adapter2.setDropDownViewResource(R.layout.view_drop_down_item);
                // type2.setAdapter(adapter2);

                int pos_1 = type.getSelectedItemPosition();
                String[] bigIds2 = new AutoUpdateEvent(
                        QuestionReportActivity.this).getBigEvent(CID);
                List<String> middles = new ArrayList<String>();
                smallss = new ArrayList<String>();
                for (int i = 0; i < eventList.size(); i++) {
                    EventKings kings = eventList.get(i);
                    if (kings.getParentid().equals(bigIds2[pos_1])) {
                        middles.add(kings.getId());
                    }
                }
                for (int i = 0; i < eventList.size(); i++) {
                    EventKings kings = eventList.get(i);
                    if (middles != null && middles.size() > 0) {
                        if (kings.getParentid().equals(middles.get(arg2))) {
                            smallss.add(kings.getMc());
                            events.put(kings.getMc(), kings.getCode());
                            eventsLayer.put(kings.getMc(), kings.getLayer());
                        }
                    }
                }

                adapter2 = new ArrayAdapter<String>(
                        QuestionReportActivity.this,
                        android.R.layout.simple_spinner_item, smallss);
                adapter2.setDropDownViewResource(R.layout.view_drop_down_item);
                type2.setAdapter(adapter2);
                // arrayAdapter03 = new
                // ArrayAdapter<String>(MdcjAddActivity.this,
                // android.R.layout.simple_spinner_item, smallss);
                // arrayAdapter03
                // .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // etType03.setAdapter(arrayAdapter03);
                type2.setSelection(0, false);
                if (smallname != null && !"".equals(smallname)) {
                    type2.setSelection(getListIndex(smallss, smallname), true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        type2.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                type2Position = arg2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

    }

    private void takePictureFromCameraIntent() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "当前设备无SD卡，无法进行拍照！", Toast.LENGTH_SHORT).show();
            return;
        }
        String base = "LILOSOFT";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        sb.append(dateFormat.format(new Date()));
        for (int i = 0; i < 3; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        SD_CARD_TEMP_DIR = Config.FILES_NAME_URL + sb.toString() + ".jpg";
        takePictureFromCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureFromCameraIntent.putExtra(
                android.provider.MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(SD_CARD_TEMP_DIR)));

    }

    /**
     * 地图,拍照返回数据
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && null != data) {
            String mediaPath = null;
            ContentResolver cr = null;
            Uri selectedImage = null;
            if(R.id.map+1 == requestCode){
                oBJECTID = data.getStringExtra(getResources().getString(
                        R.string.objectId));
                oBJCODE = data.getStringExtra(getResources().getString(
                        R.string.objCode));
                oBJNAME = data.getStringExtra(getResources().getString(
                        R.string.objName));
                orgCode = data.getStringExtra(getResources().getString(
                        R.string.orgCode));
                deptCode1 = data.getStringExtra(getResources().getString(
                        R.string.deptCode1));
                deptName1 = data.getStringExtra(getResources().getString(
                        R.string.deptName1));
                deptCode2 = data.getStringExtra(getResources().getString(
                        R.string.deptCode2));
                deptName2 = data.getStringExtra(getResources().getString(
                        R.string.deptName2));
                deptCode3 = data.getStringExtra(getResources().getString(
                        R.string.deptCode3));
                deptName3 = data.getStringExtra(getResources().getString(
                        R.string.deptName3));
                lon = data.getStringExtra("Lon");
                lat = data.getStringExtra("Lat");
                if (null != oBJECTID) {
                    map.setText("重新选择部件");
                }
            }else if (R.id.map == requestCode) {
                if (null != data.getStringExtra("local")
                        && null != data.getStringExtra("local")) {
                    mapStr = data.getStringExtra("local");
                    map.setText(mapStr.split(",")[4] + mapStr.split(",")[3]);
                    //descript.setText(mapStr.split(",")[4]);
                }
            } else if (R.id.media_rt11 == requestCode
                    || R.id.media_rt21 == requestCode
                    || R.id.media_rt31 == requestCode) {
                switch (requestCode) {
                    // case 3:
                    // startPhotoZoom(data.getData());
                    // break;
                    case R.id.media_rt11:
                        // media_r11.setText(data.getStringExtra("path"));
                        break;
                    case R.id.media_rt21:
                        // media_r21.setText(data.getStringExtra("path"));
                        break;
                    case R.id.media_rt31:
                        // media_r31.setText(data.getStringExtra("path"));
                        break;
                    default:
                        break;
                }
                /**
                 * 从相册获取返回数据
                 */
            } else if (3 == requestCode) {
                selectedImage = data.getData();
                if (selectedImage != null) {
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    cr = this.getContentResolver();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mediaPath = cursor.getString(columnIndex);
                    cursor.close();
                }
                startPhotoZoom(data.getData());
            } else if (R.id.media_p11 == requestCode
                    || R.id.media_p21 == requestCode
                    || R.id.media_p31 == requestCode
                    || R.id.media_p11a == requestCode
                    || R.id.media_p21a == requestCode
                    || R.id.media_p31a == requestCode) {

                InputStream in = null;
                Bitmap mbm = null;
                Bitmap photo = null;
                Bundle extras = data.getExtras();
                if (data != null) {
                    if (extras != null) {
                        photo = extras.getParcelable("data");
                    }
                }
                switch (requestCode) {
                    case R.id.media_p11:
                        try {
                            if (selectedImage != null) {
                                in = cr.openInputStream(selectedImage);
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 4;
                                Bitmap bitmap = BitmapFactory.decodeStream(in,
                                        null, options);
                            }
                            if (photo != null) {
                                // mbm = ImageTool.BitmapUtil(photo);
                                s1 = ImageTool.compressImage(photo);
                                drawable1 = new BitmapDrawable(photo);
                                media_p11.setImageDrawable(drawable1);
                            }
                            media_p11.setScaleType(ScaleType.CENTER_CROP);
                            media_p11.setVisibility(View.VISIBLE);
                            media_p11text = "/"
                                    + Calendar.getInstance().getTimeInMillis()
                                    + ".jpg";
                            media_p11.startAnimation(plusScaleAnimation);
                            media_p21.setVisibility(View.VISIBLE);
                            media_p21.startAnimation(plusScaleAnimation);
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        break;
                    case R.id.media_p21:
                        if (media_p11text.equals(mediaPath)) {
                            Toast.makeText(this, "这张照片已经存在！", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            try {
                                if (selectedImage != null) {
                                    in = cr.openInputStream(selectedImage);
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inSampleSize = 4;
                                    Bitmap bitmap = BitmapFactory.decodeStream(in,
                                            null, options);
                                }
                                if (photo != null) {
                                    // mbm = ImageTool.BitmapUtil(photo);
                                    s2 = ImageTool.compressImage(photo);
                                    drawable2 = new BitmapDrawable(photo);
                                    media_p21.setImageDrawable(drawable2);
                                }
                                media_p21.setScaleType(ScaleType.CENTER_CROP);
                                media_p21.setVisibility(View.VISIBLE);
                                media_p21text = "/"
                                        + Calendar.getInstance().getTimeInMillis()
                                        + ".jpg";
                                media_p21.startAnimation(plusScaleAnimation);
                                media_p31.setVisibility(View.VISIBLE);
                                media_p31.startAnimation(plusScaleAnimation);
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        break;
                    case R.id.media_p31:
                        if (media_p11text.equals(mediaPath)
                                || media_p21text.equals(mediaPath)) {
                            Toast.makeText(this, "这张照片已经存在！", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            try {
                                if (selectedImage != null) {
                                    in = cr.openInputStream(selectedImage);
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inSampleSize = 4;
                                    Bitmap bitmap = BitmapFactory.decodeStream(in,
                                            null, options);
                                }
                                if (photo != null) {
                                    // mbm = ImageTool.BitmapUtil(photo);
                                    s3 = ImageTool.compressImage(photo);
                                    drawable3 = new BitmapDrawable(photo);
                                    media_p31.setImageDrawable(drawable3);
                                }
                                media_p31.setScaleType(ScaleType.CENTER_CROP);
                                media_p31.setVisibility(View.VISIBLE);
                                media_p31text = "/"
                                        + Calendar.getInstance().getTimeInMillis()
                                        + ".jpg";
                                media_p31.startAnimation(plusScaleAnimation);
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        break;
                }
            }

            /**
             * 拍照返回数据
             */
        } else if (4 == requestCode) {
            startPhotoZoom(Uri.fromFile(new File(SD_CARD_TEMP_DIR)));
        } else {
            File file = null;
            InputStream in = null;
            Bitmap mbm = null;
            ContentResolver cr = this.getContentResolver();
            Bitmap photo = null;
            if (data != null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    photo = extras.getParcelable("data");
                }
            }

            if (null != SD_CARD_TEMP_DIR) {
                switch (requestCode) {
                    case R.id.media_p11:
                        if (media_p21text.equals(SD_CARD_TEMP_DIR)
                                || media_p31text.equals(SD_CARD_TEMP_DIR)) {
                            Toast.makeText(this, "这张照片已经存在！", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            file = new File(SD_CARD_TEMP_DIR);
                            if (file.exists()) {
                                try {
                                    Uri selectedImage = Uri.fromFile(file);
                                    media_p11.setVisibility(View.VISIBLE);
                                    in = cr.openInputStream(selectedImage);
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inSampleSize = 4;
                                    Bitmap bitmap1 = BitmapFactory.decodeStream(in,
                                            null, options);
                                    if (photo != null) {
                                        // mbm = ImageTool.BitmapUtil(photo);
                                        s1 = ImageTool.compressImage(photo);
                                        drawable1 = new BitmapDrawable(photo);
                                        media_p11.setImageDrawable(drawable1);
                                    }
                                    media_p11.setScaleType(ScaleType.CENTER_CROP);
                                    media_p11.setVisibility(View.VISIBLE);
                                    media_p11text = SD_CARD_TEMP_DIR;
                                    media_p11.startAnimation(plusScaleAnimation);
                                    media_p21.setVisibility(View.VISIBLE);
                                    media_p21.startAnimation(plusScaleAnimation);
                                } catch (FileNotFoundException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case R.id.media_p21:
                        if (media_p11text.equals(SD_CARD_TEMP_DIR)
                                || media_p31text.equals(SD_CARD_TEMP_DIR)) {
                            Toast.makeText(this, "这张照片已经存在！", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            file = new File(SD_CARD_TEMP_DIR);
                            if (file.exists()) {
                                try {
                                    Uri selectedImage = Uri.fromFile(file);
                                    media_p21.setVisibility(View.VISIBLE);
                                    in = cr.openInputStream(selectedImage);
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inSampleSize = 4;
                                    Bitmap bitmap1 = BitmapFactory.decodeStream(in,
                                            null, options);
                                    if (photo != null) {
                                        // mbm = ImageTool.BitmapUtil(photo);
                                        s2 = ImageTool.compressImage(photo);
                                        drawable2 = new BitmapDrawable(photo);
                                        media_p21.setImageDrawable(drawable2);
                                    }
                                    media_p21.setScaleType(ScaleType.CENTER_CROP);
                                    media_p21.setVisibility(View.VISIBLE);
                                    media_p21text = SD_CARD_TEMP_DIR;
                                    media_p21.startAnimation(plusScaleAnimation);
                                    media_p31.setVisibility(View.VISIBLE);
                                    media_p31.startAnimation(plusScaleAnimation);
                                } catch (FileNotFoundException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case R.id.media_p31:
                        if (media_p11text.equals(SD_CARD_TEMP_DIR)
                                || media_p21text.equals(SD_CARD_TEMP_DIR)) {
                            Toast.makeText(this, "这张照片已经存在！", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            file = new File(SD_CARD_TEMP_DIR);
                            if (file.exists()) {
                                try {
                                    Uri selectedImage = Uri.fromFile(file);
                                    media_p31.setVisibility(View.VISIBLE);
                                    in = cr.openInputStream(selectedImage);
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inSampleSize = 4;
                                    Bitmap bitmap1 = BitmapFactory.decodeStream(in,
                                            null, options);
                                    if (photo != null) {
                                        // mbm = ImageTool.BitmapUtil(photo);
                                        s3 = ImageTool.compressImage(photo);
                                        drawable3 = new BitmapDrawable(photo);
                                        media_p31.setImageDrawable(drawable3);
                                    }
                                    media_p31.setScaleType(ScaleType.CENTER_CROP);
                                    media_p31.setVisibility(View.VISIBLE);
                                    media_p31text = SD_CARD_TEMP_DIR;
                                    media_p31.startAnimation(plusScaleAnimation);
                                } catch (FileNotFoundException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void initListView() {
        // TODO Auto-generated method stub
        threadG = new Thread(initThread);
        threadG.start();

    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, id);
    }

    /**
     * 获取list值的索引
     *
     * @param strList
     * @param value
     * @return
     */
    private int getListIndex(List<String> strList, String value) {
        int listIndex = 0;
        if (strList != null && strList.size() > 0) {
            for (int i = 0; i < strList.size(); i++) {
                if (strList.get(i) == value) {
                    listIndex = i;
                }
            }
        }
        return listIndex;
    }

    /**
     * 获取Array值的索引
     *
     * @param strList
     * @param value
     * @return
     */
    private int getArrayIndex(String[] events, String value) {
        int arrayIndex = 0;
        if (events != null && events.length > 0) {
            for (int i = 0; i < events.length; i++) {
                if (events[i] == value) {
                    arrayIndex = i;
                }
            }
        }
        return arrayIndex;
    }

    @SuppressWarnings("deprecation")
    private void hasDataThenInit() {
        if (ReadyReportActivity.operation
                && null != ReadyReportActivity.readyReportBean) {
            String type_Id = ReadyReportActivity.readyReportBean
                    .getQ_QUESTION_TYPE();
            String type_Id1 = ReadyReportActivity.readyReportBean
                    .getQ_QUESTION_TYPE1();
            String type_Id2 = ReadyReportActivity.readyReportBean
                    .getQ_QUESTION_TYPE2();

            List<EventKings> listek = eventList;
            String bigname = null;
            for (int i = 0; i < listek.size(); i++) {
                EventKings ek = listek.get(i);
                if (ek.getId().equals(type_Id)) {
                    bigname = ek.getMc();
                }
                if (ek.getId().equals(type_Id1)) {
                    middlename = ek.getMc();
                }
                if (ek.getId().equals(type_Id2)) {
                    smallname = ek.getMc();
                }
            }
            type.setSelection(getArrayIndex(typeData, bigname));

            // for (int i = 0; i < typeId.length; i++) {
            // if (type_Id.equals(typeId[i]))
            // typePosition = i;
            // }
            // for (int i = 0; i < type1Id[typePosition].length; i++) {
            // String s = type1Id[typePosition][i];
            // if (type_Id1.equals(s))
            // type1Position = i;
            // }
            // for (int i = 0; i < type2Id[typePosition][type1Position].length;
            // i++) {
            // String s = type2Id[typePosition][type1Position][i];
            // if (type_Id2.equals(s))
            // type2Position = i;
            // }

            // {
            // type.setSelection(typePosition);
            // adapter1 = new ArrayAdapter<String>(mContext,
            // android.R.layout.simple_spinner_item,
            // type1Data[typePosition]);
            // adapter1.setDropDownViewResource(R.layout.view_drop_down_item);
            // type1.setAdapter(adapter1);
            // type1.setSelection(type1Position);
            // adapter2 = new ArrayAdapter<String>(mContext,
            // android.R.layout.simple_spinner_item,
            // type2Data[typePosition][type1Position]);
            // adapter2.setDropDownViewResource(R.layout.view_drop_down_item);
            // type2.setAdapter(adapter2);
            // type2.setSelection(type2Position);
            // }

            if (!"".equals(ReadyReportActivity.readyReportBean
                    .getQ_QUESTION_LOCATION())) {
                mapStr = ReadyReportActivity.readyReportBean
                        .getQ_QUESTION_LOCATION();
                map.setText(mapStr.split(",")[3]);
            }

            descript.setText(ReadyReportActivity.readyReportBean
                    .getQ_QUESTION_DESCRIPT());
            etCaseNum.setText(ReadyReportActivity.readyReportBean
                    .getQ_QUESTION_CASESNUM());
            File file = null;
            InputStream in = null;
            Bitmap mbm = null;
            ContentResolver cr = this.getContentResolver();
            {
                if (!"".equals(ReadyReportActivity.readyReportBean
                        .getQ_QUESTION_BEFOR_IMG1())) {
                    file = new File(
                            ReadyReportActivity.readyReportBean
                                    .getQ_QUESTION_BEFOR_IMG1());
                    if (file.exists()) {
                        try {
                            Uri selectedImage = Uri.fromFile(file);
                            media_p11.setVisibility(View.VISIBLE);
                            in = cr.openInputStream(selectedImage);
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 4;
                            Bitmap bitmap1 = BitmapFactory.decodeStream(in,
                                    null, options);
                            mbm = ImageTool.BitmapUtil(bitmap1);
                            s1 = ImageTool.compressImage(mbm);
                            drawable = new BitmapDrawable(mbm);
                            media_p11.setImageDrawable(drawable);
                            media_p11.setScaleType(ScaleType.CENTER_CROP);
                            media_p11.setVisibility(View.VISIBLE);
                            media_p11text = ReadyReportActivity.readyReportBean
                                    .getQ_QUESTION_BEFOR_IMG1();
                            media_p21.setVisibility(View.VISIBLE);
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

                if (!"".equals(ReadyReportActivity.readyReportBean
                        .getQ_QUESTION_BEFOR_IMG2())) {
                    file = new File(
                            ReadyReportActivity.readyReportBean
                                    .getQ_QUESTION_BEFOR_IMG2());
                    if (file.exists()) {
                        try {
                            Uri selectedImage = Uri.fromFile(file);
                            media_p21.setVisibility(View.VISIBLE);
                            in = cr.openInputStream(selectedImage);
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 4;
                            Bitmap bitmap1 = BitmapFactory.decodeStream(in,
                                    null, options);
                            mbm = ImageTool.BitmapUtil(bitmap1);
                            s2 = ImageTool.compressImage(mbm);
                            drawable = new BitmapDrawable(mbm);
                            media_p21.setImageDrawable(drawable);
                            media_p21.setScaleType(ScaleType.CENTER_CROP);
                            media_p21.setVisibility(View.VISIBLE);
                            media_p21text = ReadyReportActivity.readyReportBean
                                    .getQ_QUESTION_BEFOR_IMG2();
                            media_p31.setVisibility(View.VISIBLE);
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                if (!"".equals(ReadyReportActivity.readyReportBean
                        .getQ_QUESTION_BEFOR_IMG3())) {
                    file = new File(
                            ReadyReportActivity.readyReportBean
                                    .getQ_QUESTION_BEFOR_IMG3());
                    if (file.exists()) {
                        try {
                            Uri selectedImage = Uri.fromFile(file);
                            media_p31.setVisibility(View.VISIBLE);
                            in = cr.openInputStream(selectedImage);
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 4;
                            Bitmap bitmap1 = BitmapFactory.decodeStream(in,
                                    null, options);
                            mbm = ImageTool.BitmapUtil(bitmap1);
                            s3 = ImageTool.compressImage(mbm);
                            drawable = new BitmapDrawable(mbm);
                            media_p31.setImageDrawable(drawable);
                            media_p31.setScaleType(ScaleType.CENTER_CROP);
                            media_p31.setVisibility(View.VISIBLE);
                            media_p31text = ReadyReportActivity.readyReportBean
                                    .getQ_QUESTION_BEFOR_IMG3();
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }

            ReadyReportActivity.operation = false;

        }
    }

    private enum MENUTYPE {
        camera_operate_, delete_
    }
}
