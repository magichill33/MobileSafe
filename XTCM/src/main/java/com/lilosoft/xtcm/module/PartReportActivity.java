package com.lilosoft.xtcm.module;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.HomeBaseActivity;
import com.lilosoft.xtcm.base.NormalBaseActivity;
import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.constant.TableStructure;
import com.lilosoft.xtcm.constant.TypeContent;
import com.lilosoft.xtcm.database.DatabaseFactory;
import com.lilosoft.xtcm.instantiation.EventKings;
import com.lilosoft.xtcm.instantiation.User;
import com.lilosoft.xtcm.network.HttpConnection;
import com.lilosoft.xtcm.utils.ImageTool;
import com.lilosoft.xtcm.utils.LogFactory;
import com.lilosoft.xtcm.views.TitleBar;
import com.lilosoft.xtcm.views.TitleBar.STYLE;

public class PartReportActivity extends NormalBaseActivity implements
        OnClickListener, OnLongClickListener {

    /**
     * 上报成功
     */
    private final static int MSG_REPORT_SUCCESS_ORDER = 0x0FFFFFFF;
    /**
     * 上报失败
     */
    private final static int MSG_REPORT_LOST_ORDER = 0x00FFFFFF;
    /*
     * 事件分类属性
     */
    public static String PID = "bigMC";
    public static String CID = "bigID";
    public static List<EventKings> eventList = null;
    private final String TAG = "PartReportActivity";
    List<String> bigLists = null;
    List<String> middless = null;
    List<String> smallss = null;

    String middlename = null;
    String smallname = null;

    String code = null;
    String layer = null;
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
                // case INIT:
                // hasDataThenInit();
                // break;
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
    private TitleBar mTitleBar;
    private View layout_info;
    private View layout_media;
    private Spinner type, type1, type2;
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
    private EditText descript,etCaseNum;
    private HashMap<String, String> events = new HashMap<String, String>();
    private HashMap<String, String> eventsLayer = new HashMap<String, String>();
    private Message m;
    private Intent selectImgIntent, selectAudioIntent,
            takePictureFromCameraIntent;
    private String SD_CARD_TEMP_DIR;
    private MENUTYPE menu;
    private int id = 0;
    private String mapStr;
    private Drawable drawable1, drawable2, drawable3, drawable1a, drawable2a,
            drawable3a;
    private String s1 = "", s2 = "", s3 = "", s1a = "", s2a = "", s3a = "";
    private String media_p11text = "", media_p21text = "", media_p31text = "",
            media_p11atext = "", media_p21atext = "", media_p31atext = "",
            media_r11text = "", media_r21text = "", media_r31text = "";
    private ImageView media_p11, media_p21, media_p31, media_p11a, media_p21a,
            media_p31a, media_r11, media_r21, media_r31;
    private View submit;
    private View save;
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
    private String EXTEND4;
    private String BJID;
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
                String[] bigArray = new String[]{BJID};// 获取第一级ID数组
                String[] bigNameArray = new String[]{"部件分类"};// 获取第一级名称数组
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
                int tp2index = type2.getSelectedItemPosition();
                LogFactory.d(TAG, "tp2index = "+tp2index);
                String smallID = smalls.get(tp2index);// 第三级ID
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
//		setContentView(R.layout.activity_question_own_dispose);
        setContentView(R.layout.activity_part_report);
        initTitleBar();

        layout_info = findViewById(R.id.layout_info);
        layout_media = findViewById(R.id.layout_media);

        type = (Spinner) findViewById(R.id.type);
        type1 = (Spinner) findViewById(R.id.type1);
        type2 = (Spinner) findViewById(R.id.type2);
        map = (TextView) findViewById(R.id.map);
        descript = (EditText) findViewById(R.id.descript);
        etCaseNum = (EditText) findViewById(R.id.et_casesnum);
        // 处理前

        media_p11 = (ImageView) findViewById(R.id.media_p11);

        media_p21 = (ImageView) findViewById(R.id.media_p21);

        media_p31 = (ImageView) findViewById(R.id.media_p31);

        submit = findViewById(R.id.submit);
        save = findViewById(R.id.save);

        etCaseNum.addTextChangedListener(caseNumListener);
        LinearLayout ll_t_after = (LinearLayout) findViewById(R.id.ll_t_after);
        LinearLayout ll_i_after = (LinearLayout) findViewById(R.id.ll_i_after);

        ll_t_after.setVisibility(View.GONE);
        ll_i_after.setVisibility(View.GONE);

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

        submit.setOnClickListener(this);
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

        typeData = new AutoUpdateEvent(this).getBigEvent(PID);
        String[] type1Data = new String[]{"部件分类"};
        eventList = AutoUpdateEvent.eventList;
        // type1Data = TypeContent.TYPE1_TEXT;
        // type2Data = TypeContent.TYPE2_TEXT;
        // type2Id = TypeContent.TYPE2_ID;
        for (int i = 0; i < eventList.size(); i++) {
            EventKings kings = eventList.get(i);
            if (kings.getMc().equals("部件分类")) {
                BJID = kings.getId();
                break;
            }
        }

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, type1Data);
        adapter.setDropDownViewResource(R.layout.view_drop_down_item);

        // adapter1 = new ArrayAdapter<String>(this,
        // android.R.layout.simple_spinner_item, type1Data[typePosition]);
        // adapter1.setDropDownViewResource(R.layout.view_drop_down_item);
        //
        // adapter2 = new ArrayAdapter<String>(this,
        // android.R.layout.simple_spinner_item,
        // type2Data[typePosition][type1Position]);
        // adapter2.setDropDownViewResource(R.layout.view_drop_down_item);

        type.setAdapter(adapter);

        type.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                typePosition = arg2;
                // adapter1 = new ArrayAdapter<String>(mContext,
                // android.R.layout.simple_spinner_item, type1Data[arg2]);
                // adapter1.setDropDownViewResource(R.layout.view_drop_down_item);
                // type1.setAdapter(adapter1);
                String[] bigIds = new AutoUpdateEvent(PartReportActivity.this)
                        .getBigEvent(CID);
                middless = new ArrayList<String>();
                for (int i = 0; i < eventList.size(); i++) {
                    EventKings kings = eventList.get(i);
                    if (kings.getParentid().equals(BJID)) {
                        middless.add(kings.getMc());
                    }
                }
                adapter1 = new ArrayAdapter<String>(PartReportActivity.this,
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
                String[] bigIds2 = new AutoUpdateEvent(PartReportActivity.this)
                        .getBigEvent(CID);
                List<String> middles = new ArrayList<String>();
                smallss = new ArrayList<String>();
                for (int i = 0; i < eventList.size(); i++) {
                    EventKings kings = eventList.get(i);
                    if (kings.getParentid().equals(BJID)) {
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
                            // LogFactory.e(TAG,
                            // "code = "+kings.getCode()+" layer = "+kings.getLayer());
                        }
                    }
                }

                adapter2 = new ArrayAdapter<String>(PartReportActivity.this,
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

    private void initTitleBar() {
        // TODO Auto-generated method stub
        mTitleBar = (TitleBar) findViewById(R.id.titlebar);

        mTitleBar.changeStyle(STYLE.NOT_BTN_AND_TITLE);

        mTitleBar.centerTextView.setText(R.string.function_part_report);
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

            case R.id.menu_take:
                takePictureFromCameraIntent();
                startActivityForResult(takePictureFromCameraIntent, id);
                break;
            case R.id.menu_choose:
                startActivityForResult(selectImgIntent, id);
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
                    case R.id.media_p11a:
                        if ("" != media_p31atext) {
                            drawable1a = drawable2a;
                            drawable2a = drawable3a;
                            drawable3a = null;
                            s1a = s2a;
                            s2a = s3a;
                            s3a = "";
                            media_p11atext = media_p21atext;
                            media_p21atext = media_p31atext;
                            media_p31atext = "";
                            media_p11a.setImageDrawable(drawable1a);
                            media_p21a.setImageDrawable(drawable2a);
                            media_p31a.setImageResource(R.drawable.add_button);
                        } else if ("" != media_p21atext) {
                            drawable1a = drawable2a;
                            drawable2a = null;
                            s1a = s2a;
                            s2a = "";
                            media_p11atext = media_p21atext;
                            media_p21atext = "";
                            media_p11a.setImageDrawable(drawable1a);
                            media_p21a.setImageResource(R.drawable.add_button);
                            media_p31a.setVisibility(View.GONE);
                        } else {
                            drawable1a = null;
                            s1a = "";
                            media_p11atext = "";
                            media_p11a.setImageResource(R.drawable.add_button);
                            media_p21a.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.media_p21a:
                        if ("" != media_p31atext) {
                            drawable2a = drawable3a;
                            drawable3a = null;
                            s2a = s3a;
                            s3a = "";
                            media_p21atext = media_p31atext;
                            media_p31atext = "";
                            media_p21a.setImageDrawable(drawable2a);
                            media_p31a.setImageResource(R.drawable.add_button);
                        } else {
                            drawable2a = null;
                            s2a = "";
                            media_p21atext = "";
                            media_p21a.setImageResource(R.drawable.add_button);
                            media_p31a.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.media_p31a:
                        s3a = "";
                        drawable3a = null;
                        media_p31atext = "";
                        media_p31a.setImageResource(R.drawable.add_button);
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
        this.id = 0;
        super.onOptionsMenuClosed(menu);
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

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.map:
//			if (!type.getSelectedItem().toString().equals("部件分类")) {
//				Toast.makeText(this, "请选择部件分类！", 1).show();
//				return;
//			}
                code = events.get(type2.getSelectedItem().toString());
                layer = eventsLayer.get(type2.getSelectedItem().toString());
                if (TextUtils.isEmpty(layer)) {
                    LogFactory.e(TAG, "layer为空");
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
                startActivityForResult(intent, R.id.map);
                break;
            case R.id.submit:
                boolean finishKey = true;

                if ("请选择部件".equals(map.getText().toString())) {
                    Toast.makeText(mContext, "请在地图上选择部件！", Toast.LENGTH_SHORT)
                            .show();
                    finishKey = false;
                } else if ("".equals(descript.getText().toString())) {
                    Toast.makeText(mContext, "问题描述不能为空！", Toast.LENGTH_SHORT)
                            .show();
                    finishKey = false;
                } else if (finishKey) {
                    if (Config.NETWORK) {
                        threadG = new Thread(partReportThread);
                        threadG.start();
                    } else {
                        Toast.makeText(mContext, "无网模式-上报！", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                break;

            default:
                openOptionsMenu(MENUTYPE.camera_operate_, v.getId());
                break;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && null != data) {
            if (R.id.map == requestCode) {
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
            } else if (R.id.media_r11 == requestCode
                    || R.id.media_r21 == requestCode
                    || R.id.media_r31 == requestCode) {
                switch (requestCode) {
                    case R.id.media_r11:
                        // media_r11.setText(data.getStringExtra("path"));
                        break;
                    case R.id.media_r21:
                        // media_r21.setText(data.getStringExtra("path"));
                        break;
                    case R.id.media_r31:
                        // media_r31.setText(data.getStringExtra("path"));
                        break;
                    default:
                        break;
                }
            } else if (R.id.media_p11 == requestCode
                    || R.id.media_p21 == requestCode
                    || R.id.media_p31 == requestCode
                    || R.id.media_p11a == requestCode
                    || R.id.media_p21a == requestCode
                    || R.id.media_p31a == requestCode) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                ContentResolver cr = this.getContentResolver();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String mediaPath = cursor.getString(columnIndex);
                cursor.close();
                InputStream in = null;
                Bitmap mbm = null;
                switch (requestCode) {
                    case R.id.media_p11:
                        try {
                            in = cr.openInputStream(selectedImage);
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 4;
                            Bitmap bitmap = BitmapFactory.decodeStream(in, null,
                                    options);
                            mbm = ImageTool.BitmapUtil(bitmap);
                            s1 = ImageTool.getPicString(mbm);
                            drawable1 = new BitmapDrawable(mbm);
                            media_p11.setImageDrawable(drawable1);
                            media_p11.setScaleType(ScaleType.CENTER_CROP);
                            media_p11.setVisibility(View.VISIBLE);
                            media_p11text = mediaPath;
                            media_p21.setVisibility(View.VISIBLE);
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
                                in = cr.openInputStream(selectedImage);
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 4;
                                Bitmap bitmap = BitmapFactory.decodeStream(in,
                                        null, options);
                                mbm = ImageTool.BitmapUtil(bitmap);
                                s2 = ImageTool.getPicString(mbm);
                                drawable2 = new BitmapDrawable(mbm);
                                media_p21.setImageDrawable(drawable2);
                                media_p21.setScaleType(ScaleType.CENTER_CROP);
                                media_p21.setVisibility(View.VISIBLE);
                                media_p21text = mediaPath;
                                media_p31.setVisibility(View.VISIBLE);
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
                                in = cr.openInputStream(selectedImage);
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 4;
                                Bitmap bitmap = BitmapFactory.decodeStream(in,
                                        null, options);
                                mbm = ImageTool.BitmapUtil(bitmap);
                                s3 = ImageTool.getPicString(mbm);
                                drawable3 = new BitmapDrawable(mbm);
                                media_p31.setImageDrawable(drawable3);
                                media_p31.setScaleType(ScaleType.CENTER_CROP);
                                media_p31.setVisibility(View.VISIBLE);
                                media_p31text = mediaPath;
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        break;
                    case R.id.media_p11a:
                        try {
                            in = cr.openInputStream(selectedImage);
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 4;
                            Bitmap bitmap = BitmapFactory.decodeStream(in, null,
                                    options);
                            mbm = ImageTool.BitmapUtil(bitmap);
                            s1 = ImageTool.getPicString(mbm);
                            drawable1a = new BitmapDrawable(mbm);
                            media_p11a.setImageDrawable(drawable1a);
                            media_p11a.setScaleType(ScaleType.CENTER_CROP);
                            media_p11a.setVisibility(View.VISIBLE);
                            media_p11atext = mediaPath;
                            media_p21a.setVisibility(View.VISIBLE);
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        break;
                    case R.id.media_p21a:
                        if (media_p11atext.equals(mediaPath)) {
                            Toast.makeText(this, "这张照片已经存在！", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            try {
                                in = cr.openInputStream(selectedImage);
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 4;
                                Bitmap bitmap = BitmapFactory.decodeStream(in,
                                        null, options);
                                mbm = ImageTool.BitmapUtil(bitmap);
                                s2a = ImageTool.getPicString(mbm);
                                drawable2a = new BitmapDrawable(mbm);
                                media_p21a.setImageDrawable(drawable2a);
                                media_p21a.setScaleType(ScaleType.CENTER_CROP);
                                media_p21a.setVisibility(View.VISIBLE);
                                media_p21atext = mediaPath;
                                media_p31a.setVisibility(View.VISIBLE);
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        break;
                    case R.id.media_p31a:
                        if (media_p11atext.equals(mediaPath)
                                || media_p21atext.equals(mediaPath)) {
                            Toast.makeText(this, "这张照片已经存在！", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            try {
                                in = cr.openInputStream(selectedImage);
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 4;
                                Bitmap bitmap = BitmapFactory.decodeStream(in,
                                        null, options);
                                mbm = ImageTool.BitmapUtil(bitmap);
                                s3a = ImageTool.getPicString(mbm);
                                drawable3a = new BitmapDrawable(mbm);
                                media_p31a.setImageDrawable(drawable3a);
                                media_p31a.setScaleType(ScaleType.CENTER_CROP);
                                media_p31a.setVisibility(View.VISIBLE);
                                media_p31atext = mediaPath;
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        break;
                }
            }

        } else {
            File file = null;
            InputStream in = null;
            Bitmap mbm = null;
            ContentResolver cr = this.getContentResolver();
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
                                    mbm = ImageTool.BitmapUtil(bitmap1);
                                    s1 = ImageTool.getPicString(mbm);
                                    drawable1 = new BitmapDrawable(mbm);
                                    media_p11.setImageDrawable(drawable1);
                                    media_p11.setScaleType(ScaleType.CENTER_CROP);
                                    media_p11.setVisibility(View.VISIBLE);
                                    media_p11text = SD_CARD_TEMP_DIR;
                                    media_p21.setVisibility(View.VISIBLE);
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
                                    mbm = ImageTool.BitmapUtil(bitmap1);
                                    s2 = ImageTool.getPicString(mbm);
                                    drawable2 = new BitmapDrawable(mbm);
                                    media_p21.setImageDrawable(drawable2);
                                    media_p21.setScaleType(ScaleType.CENTER_CROP);
                                    media_p21.setVisibility(View.VISIBLE);
                                    media_p21text = SD_CARD_TEMP_DIR;
                                    media_p31.setVisibility(View.VISIBLE);
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
                                    mbm = ImageTool.BitmapUtil(bitmap1);
                                    s3 = ImageTool.getPicString(mbm);
                                    drawable3 = new BitmapDrawable(mbm);
                                    media_p31.setImageDrawable(drawable3);
                                    media_p31.setScaleType(ScaleType.CENTER_CROP);
                                    media_p31.setVisibility(View.VISIBLE);
                                    media_p31text = SD_CARD_TEMP_DIR;
                                } catch (FileNotFoundException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case R.id.media_p11a:
                        if (media_p21atext.equals(SD_CARD_TEMP_DIR)
                                || media_p31atext.equals(SD_CARD_TEMP_DIR)) {
                            Toast.makeText(this, "这张照片已经存在！", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            file = new File(SD_CARD_TEMP_DIR);
                            if (file.exists()) {
                                try {
                                    Uri selectedImage = Uri.fromFile(file);
                                    media_p11a.setVisibility(View.VISIBLE);
                                    in = cr.openInputStream(selectedImage);
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inSampleSize = 4;
                                    Bitmap bitmap1 = BitmapFactory.decodeStream(in,
                                            null, options);
                                    mbm = ImageTool.BitmapUtil(bitmap1);
                                    s1a = ImageTool.getPicString(mbm);
                                    drawable1a = new BitmapDrawable(mbm);
                                    media_p11a.setImageDrawable(drawable1a);
                                    media_p11a.setScaleType(ScaleType.CENTER_CROP);
                                    media_p11a.setVisibility(View.VISIBLE);
                                    media_p11atext = SD_CARD_TEMP_DIR;
                                    media_p21a.setVisibility(View.VISIBLE);
                                } catch (FileNotFoundException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case R.id.media_p21a:
                        if (media_p11atext.equals(SD_CARD_TEMP_DIR)
                                || media_p31atext.equals(SD_CARD_TEMP_DIR)) {
                            Toast.makeText(this, "这张照片已经存在！", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            file = new File(SD_CARD_TEMP_DIR);
                            if (file.exists()) {
                                try {
                                    Uri selectedImage = Uri.fromFile(file);
                                    media_p21a.setVisibility(View.VISIBLE);
                                    in = cr.openInputStream(selectedImage);
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inSampleSize = 4;
                                    Bitmap bitmap1 = BitmapFactory.decodeStream(in,
                                            null, options);
                                    mbm = ImageTool.BitmapUtil(bitmap1);
                                    s2a = ImageTool.getPicString(mbm);
                                    drawable2a = new BitmapDrawable(mbm);
                                    media_p21a.setImageDrawable(drawable2a);
                                    media_p21a.setScaleType(ScaleType.CENTER_CROP);
                                    media_p21a.setVisibility(View.VISIBLE);
                                    media_p21atext = SD_CARD_TEMP_DIR;
                                    media_p31a.setVisibility(View.VISIBLE);
                                } catch (FileNotFoundException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case R.id.media_p31a:
                        if (media_p11atext.equals(SD_CARD_TEMP_DIR)
                                || media_p21atext.equals(SD_CARD_TEMP_DIR)) {
                            Toast.makeText(this, "这张照片已经存在！", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            file = new File(SD_CARD_TEMP_DIR);
                            if (file.exists()) {
                                try {
                                    Uri selectedImage = Uri.fromFile(file);
                                    media_p31a.setVisibility(View.VISIBLE);
                                    in = cr.openInputStream(selectedImage);
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inSampleSize = 4;
                                    Bitmap bitmap1 = BitmapFactory.decodeStream(in,
                                            null, options);
                                    mbm = ImageTool.BitmapUtil(bitmap1);
                                    s3a = ImageTool.getPicString(mbm);
                                    drawable3a = new BitmapDrawable(mbm);
                                    media_p31a.setImageDrawable(drawable3a);
                                    media_p31a.setScaleType(ScaleType.CENTER_CROP);
                                    media_p31a.setVisibility(View.VISIBLE);
                                    media_p31atext = SD_CARD_TEMP_DIR;
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

    public int jsonParseToOrder(String response) throws JSONException {
        // TODO Auto-generated method stub
        // {"head":"approve","body":{"isSuccess":"1","message":"提交成功"}}

        if (null != response && !"".equals(response)) {
            JSONObject jsonObject = new JSONObject(response);

            if (null != jsonObject) {

                String operation = jsonObject
                        .getString(TableStructure.COVER_HEAD);

                if (TableStructure.V_PART_REPORT.equals(operation)) {
                    JSONObject content = (JSONObject) ((jsonObject
                            .get(TableStructure.COVER_BODY)));
                    if ("1".equals(content
                            .getString(TableStructure.R_USER_RESPONSE_KEY))) {
//						if (null != ReadyReportActivity.readyReportBean) {
//							DatabaseFactory databaseFactory = new DatabaseFactory(
//									this);
//							databaseFactory
//									.delete(ReadyReportActivity.readyReportBean
//											.getQ_QUESTION_ID());
//							ReadyReportActivity.readyReportBean = null;
//						}
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
        Toast.makeText(mContext, err_Msg, Toast.LENGTH_LONG).show();
        // HomeBaseActivity.tabHost.setCurrentTabByTag(Config.A_TAB);
        // HomeBaseActivity.tabBt1.setChecked(true);
    }

    @Override
    public boolean onLongClick(View v) {
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
            case R.id.media_p11a:
                if (media_p11atext != "") {
                    show = true;
                }
                break;
            case R.id.media_p21a:
                if (media_p21atext != "") {
                    show = true;
                }
                break;
            case R.id.media_p31a:
                if (media_p31atext != "") {
                    show = true;
                }
                break;
        }
        if (show) {
            openOptionsMenu(MENUTYPE.delete_, v.getId());
        }
        return false;
    }

    private enum MENUTYPE {
        camera_operate_, delete_
    }

}
