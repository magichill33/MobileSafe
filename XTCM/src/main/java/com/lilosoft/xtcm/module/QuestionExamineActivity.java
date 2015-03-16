package com.lilosoft.xtcm.module;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.NormalBaseActivity;
import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.constant.TableStructure;
import com.lilosoft.xtcm.instantiation.ExamineBean;
import com.lilosoft.xtcm.instantiation.FileBean;
import com.lilosoft.xtcm.instantiation.User;
import com.lilosoft.xtcm.network.HttpConnection;
import com.lilosoft.xtcm.utils.ImageTool;
import com.lilosoft.xtcm.utils.JsonParseInterface;
import com.lilosoft.xtcm.utils.LogFactory;
import com.lilosoft.xtcm.views.MPProgressBar;
import com.lilosoft.xtcm.views.TitleBar;
import com.lilosoft.xtcm.views.TitleBar.STYLE;

/**
 * 待核实任务详情
 *
 */
@SuppressLint("SimpleDateFormat")
public class QuestionExamineActivity extends NormalBaseActivity implements
        JsonParseInterface, OnClickListener, OnLongClickListener {

    /**
     * 上报成功
     */
    private final static int MSG_EXAMINE_SUBMIT_SUCCESS_ORDER = 0x0FFFFFFF;
    /**
     * 上报失败
     */
    private final static int MSG_EXAMINE_SUBMIT_LOST_ORDER = 0x00FFFFFF;
    private final static int MSG_INIT_SUCCESS = 0x0F1;
    private final static int MSG_INIT_LOSE = 0x0F2;
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
                case MSG_INIT_SUCCESS:
                    initViews();
                    break;
                case MSG_INIT_LOSE:
                    Toast.makeText(mContext, R.string.error_load_data,
                            Toast.LENGTH_LONG).show();
                    break;
                case MSG_EXAMINE_SUBMIT_SUCCESS_ORDER:
                    finish();
                    Toast.makeText(mContext, err_Msg, Toast.LENGTH_LONG).show();
                    break;
                case MSG_EXAMINE_SUBMIT_LOST_ORDER:
                    Toast.makeText(mContext, err_Msg, Toast.LENGTH_LONG).show();
                    break;
                case Config.SHOW_PROGRESS_DIALOG:
                    showProgressDialog("交互中…");
                    break;
                case Config.DISMISS_PROGRESS_DIALOG:
                    dismissProgressDialog();
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
    private final String TAG = "QuestionExamineActivity";
    List<FileBean> list = null;
    private String pass = "0";
    private TitleBar mTitleBar;
    private View layout_info;
    private View layout_media;
    private Intent selectImgIntent, selectAudioIntent,
            takePictureFromCameraIntent;
    private String SD_CARD_TEMP_DIR;
    private MENUTYPE menu;
    private int id = 0;
    private TextView q_e_title, q_e_type, q_e_description, q_e_source,
            q_e_create_time;
    private EditText q_e_verifyeed_back_content;
    private Drawable drawable1, drawable2, drawable3, drawable1a, drawable2a,
            drawable3a;
    private String s1 = "", s2 = "", s3 = "", s1a = "", s2a = "", s3a = "";
    private String media_p11text = "", media_p21text = "", media_p31text = "",
            media_p11atext = "", media_p21atext = "", media_p31atext = "",
            media_r11text = "", media_r21text = "", media_r31text = "";
    private ImageView media_p11, media_p21, media_p31, media_p11a, media_p21a,
            media_p31a, media_r11, media_r21, media_r31, h_img_preview;
    private View q_e_success;
    private View q_e_lose;
    private String err_Msg;
    private Message m;
    Thread initThread = new Thread(new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            m = new Message();
            m.what = Config.SHOW_PROGRESS_DIALOG;
            myHandle.sendMessage(m);
            try {
                readData();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                m = new Message();
                m.what = MSG_INIT_LOSE;
                myHandle.sendMessage(m);
            } finally {
                m = new Message();
                m.what = Config.DISMISS_PROGRESS_DIALOG;
                myHandle.sendMessage(m);
            }
        }
    });
    private Thread examineSubmitThread = new Thread(new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
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
                if ("" != (media_p11atext)) {
                    String img1 = media_p11atext;
                    int begin1 = img1.lastIndexOf("/") + 1;
                    int end1 = img1.lastIndexOf(".");
                    imgName1 = img1.substring(begin1, end1)
                            + ("jpeg".equals(img1.substring(end1 + 1)) ? ".jpg"
                            : img1.substring(end1));
                    imgType1 = "jpeg".equals(img1.substring(end1 + 1)) ? "jpg"
                            : img1.substring(end1 + 1);
                    // imgData1 = s1a;
                    imgData1 = s1;
                    if ("" != (media_p21atext)) {
                        String img2 = media_p21atext;
                        int begin2 = img2.lastIndexOf("/") + 1;
                        int end2 = img2.lastIndexOf(".");
                        imgName2 = img2.substring(begin2, end2)
                                + ("jpeg".equals(img2.substring(end2 + 1)) ? ".jpg"
                                : img2.substring(end2));
                        imgType2 = "jpeg".equals(img2.substring(end2 + 1)) ? "jpg"
                                : img2.substring(end2 + 1);
                        // imgData2 = s2a;
                        imgData2 = s2;
                        if ("" != (media_p31atext)) {
                            String img3 = media_p31atext;
                            int begin3 = img3.lastIndexOf("/") + 1;
                            int end3 = img3.lastIndexOf(".");
                            imgName3 = img3.substring(begin3, end3)
                                    + ("jpeg".equals(img3.substring(end3 + 1)) ? ".jpg"
                                    : img3.substring(end3));
                            imgType3 = "jpeg".equals(img3.substring(end3 + 1)) ? "jpg"
                                    : img3.substring(end3 + 1);
                            // imgData3 = s3a;
                            imgData3 = s3;
                        }
                    }
                }
                HttpConnection httpConnection = new HttpConnection();
                // 核实确认接口传参
                action(httpConnection.getData(
                        HttpConnection.CONNECTION_READY_EXAMINE_SUBMIT, pass,
                        examineBean.getINSPECTID(), q_e_verifyeed_back_content
                                .getText().toString(), examineBean
                                .getCASECODE(), User.username, imgName1,
                        imgData1, imgType1, imgName2, imgData2, imgType2,
                        imgName3, imgData3, imgType3));

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
                LogFactory.e(TAG, "examineList thread end");
            }
        }
    });
    private ExamineBean examineBean = ReadyExamineActivity.bean;
    private boolean imgIsShow;
    OnClickListener picListener = new OnClickListener() {

        @SuppressWarnings({ "deprecation", "unused" })
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Bitmap bm = null;
            switch (v.getId()) {
                case R.id.media_p11:
                    bm = new BitmapDrawable(
                            ImageTool.tryGetBitmap(Config.FILES_NAME_URL
                                    + list.get(0).getfName())).getBitmap();
                    h_img_preview.setImageBitmap(bm);
                    h_img_preview.setVisibility(0);
                    imgIsShow = true;
                    break;
                case R.id.media_p21:
                    bm = new BitmapDrawable(
                            ImageTool.tryGetBitmap(Config.FILES_NAME_URL
                                    + list.get(1).getfName())).getBitmap();
                    h_img_preview.setImageBitmap(bm);
                    h_img_preview.setVisibility(0);
                    imgIsShow = true;
                    break;
                case R.id.media_p31:
                    bm = new BitmapDrawable(
                            ImageTool.tryGetBitmap(Config.FILES_NAME_URL
                                    + list.get(2).getfName())).getBitmap();
                    h_img_preview.setImageBitmap(bm);
                    h_img_preview.setVisibility(0);
                    imgIsShow = true;

                    break;
                default:
                    if (imgIsShow) {
                        if (bm != null && !bm.isRecycled()) {
                            bm.recycle();
                            bm = null;
                            System.gc();
                        }
                        h_img_preview.setVisibility(8);
                        imgIsShow = false;
                    }
                    break;
            }
        }
    };

    @Override
    protected void installViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_question_examine);
        initTitleBar();
        mPProgressBar = (MPProgressBar) findViewById(R.id.mPProgressBar);

        layout_info = findViewById(R.id.layout_info);
        layout_media = findViewById(R.id.layout_media);

        q_e_title = (TextView) findViewById(R.id.q_e_title);
        q_e_type = (TextView) findViewById(R.id.q_e_type);
        q_e_description = (TextView) findViewById(R.id.q_e_description);
        q_e_source = (TextView) findViewById(R.id.q_e_source);
        q_e_create_time = (TextView) findViewById(R.id.q_e_create_time);

        media_p11 = (ImageView) findViewById(R.id.media_p11);
        media_p21 = (ImageView) findViewById(R.id.media_p21);
        media_p31 = (ImageView) findViewById(R.id.media_p31);
        media_p11a = (ImageView) findViewById(R.id.media_p11a);
        media_p21a = (ImageView) findViewById(R.id.media_p21a);
        media_p31a = (ImageView) findViewById(R.id.media_p31a);
        q_e_verifyeed_back_content = (EditText) findViewById(R.id.q_e_verifyeed_back_content);
        h_img_preview = (ImageView) findViewById(R.id.h_img_preview);

        q_e_success = (Button) findViewById(R.id.q_e_success);
        q_e_lose = (Button) findViewById(R.id.q_e_lose);
        inflateType();
        threadG = new Thread(initThread);
        threadG.start();

    }

    @Override
    protected void registerEvents() {
        // TODO Auto-generated method stub
        q_e_success.setOnClickListener(this);
        q_e_lose.setOnClickListener(this);

        // 处理后
        media_p11a.setOnClickListener(this);
        media_p11a.setOnLongClickListener(this);

        media_p21a.setOnClickListener(this);
        media_p21a.setOnLongClickListener(this);

        media_p31a.setOnClickListener(this);
        media_p31a.setOnLongClickListener(this);

        media_p11.setOnClickListener(picListener);
        media_p21.setOnClickListener(picListener);
        media_p31.setOnClickListener(picListener);
        h_img_preview.setOnClickListener(picListener);
    }

    private void readData() {

        // 初始化图片
        if (null != (list = examineBean.getApproveFileList())) {
            for (int i = 0; i < list.size(); i++) {
                ImageTool.writeFile(list.get(i));
            }
        }

        m = new Message();
        m.what = MSG_INIT_SUCCESS;
        myHandle.sendMessage(m);
    }

    /**
     * @category 初始化titleBar
     */
    protected void initTitleBar() {

        mTitleBar = (TitleBar) findViewById(R.id.titlebar);

        mTitleBar.changeStyle(STYLE.NOT_BTN_AND_TITLE);

        mTitleBar.centerTextView.setText(R.string.function_question_examine);

    }

    private void initViews() {
        // 初始化内容
        q_e_title.setText(examineBean.getCASETITLE());
        q_e_type.setText(examineBean.getCASEITEM());
        q_e_description.setText(examineBean.getCASEDESCRIPTION());
        q_e_source.setText(examineBean.getCASESOURCE());
        q_e_create_time.setText(examineBean.getCREATETIME());

        if (null != list) {
            switch (list.size()) {
                case 1:
                    media_p11.setVisibility(0);
                    media_p11.setImageDrawable(Drawable
                            .createFromPath(Config.FILES_NAME_URL
                                    + list.get(0).getfName()));
                    break;
                case 2:
                    media_p11.setVisibility(0);
                    media_p21.setVisibility(0);
                    media_p11.setImageDrawable(Drawable
                            .createFromPath(Config.FILES_NAME_URL
                                    + list.get(0).getfName()));
                    media_p21.setImageDrawable(Drawable
                            .createFromPath(Config.FILES_NAME_URL
                                    + list.get(1).getfName()));
                    break;
                case 3:
                    media_p11.setVisibility(0);
                    media_p21.setVisibility(0);
                    media_p31.setVisibility(0);
                    media_p11.setImageDrawable(Drawable
                            .createFromPath(Config.FILES_NAME_URL
                                    + list.get(0).getfName()));
                    media_p21.setImageDrawable(Drawable
                            .createFromPath(Config.FILES_NAME_URL
                                    + list.get(1).getfName()));
                    media_p31.setImageDrawable(Drawable
                            .createFromPath(Config.FILES_NAME_URL
                                    + list.get(2).getfName()));
                    break;

                default:
                    break;
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
        // TODO Auto-generated method stub
        if (null != response && !"".equals(response)) {
            JSONObject jsonObject = new JSONObject(response);

            if (null != jsonObject) {

                String operation = jsonObject
                        .getString(TableStructure.COVER_HEAD);

                if (TableStructure.V_ACT_READY_EXAMINE_SUBMIT.equals(operation)) {
                    JSONObject content = (JSONObject) ((jsonObject
                            .get(TableStructure.COVER_BODY)));
                    if ("1".equals(content
                            .getString(TableStructure.R_USER_RESPONSE_KEY))) {

                        err_Msg = content
                                .getString(TableStructure.R_USER_RESPONSE_MSG);
                        return MSG_EXAMINE_SUBMIT_SUCCESS_ORDER;
                    }
                    err_Msg = content
                            .getString(TableStructure.R_USER_RESPONSE_MSG);

                }
            }
        } else {
            err_Msg = "数据异常！";
            LogFactory.e(TAG, "not data!");
        }

        return MSG_EXAMINE_SUBMIT_LOST_ORDER;
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
    }

    private void takePictureFromCameraIntent() {
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
            case R.id.q_e_success:
                if ("".equals(q_e_verifyeed_back_content.getText().toString())) {
                    Toast.makeText(mContext, "提交内容不能为空！", Toast.LENGTH_LONG).show();
                } else {
                    if (Config.NETWORK) {
                        pass = "1"; // 通过
                        threadG = new Thread(examineSubmitThread);
                        threadG.start();
                    } else {
                        Toast.makeText(mContext, "无连接模式-通过", Toast.LENGTH_LONG)
                                .show();
                    }
                }
                break;
            case R.id.q_e_lose:
                if ("".equals(q_e_verifyeed_back_content.getText().toString())) {
                    Toast.makeText(mContext, "提交内容不能为空！", Toast.LENGTH_LONG).show();
                } else {
                    if (Config.NETWORK) {
                        pass = "0"; // 不合格
                        threadG = new Thread(examineSubmitThread);
                        threadG.start();
                    } else {
                        Toast.makeText(mContext, "无连接模式-不合格", Toast.LENGTH_LONG)
                                .show();
                    }
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
                        if ("" != media_p31atext) {
                            drawable1a = drawable2a;
                            drawable2a = drawable3a;
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

    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && null != data) {
            if (R.id.media_r11 == requestCode || R.id.media_r21 == requestCode
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

    private enum MENUTYPE {
        camera_operate_, delete_
    }
}
