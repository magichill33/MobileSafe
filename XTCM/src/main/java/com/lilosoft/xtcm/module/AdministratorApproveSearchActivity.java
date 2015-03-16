package com.lilosoft.xtcm.module;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.HomeBaseActivity;
import com.lilosoft.xtcm.base.NormalBaseActivity;
import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.constant.TableStructure;
import com.lilosoft.xtcm.constant.TypeContent;
import com.lilosoft.xtcm.instantiation.AdminApproveBean;
import com.lilosoft.xtcm.instantiation.User;
import com.lilosoft.xtcm.network.HttpConnection;
import com.lilosoft.xtcm.utils.JsonParseInterface;
import com.lilosoft.xtcm.utils.LogFactory;
import com.lilosoft.xtcm.views.TitleBar;
import com.lilosoft.xtcm.views.TitleBar.STYLE;

/**
 * @category 行政审批
 * @author William Liu
 *
 */
@SuppressLint("UseValueOf")
public class AdministratorApproveSearchActivity extends NormalBaseActivity
        implements OnClickListener, OnItemSelectedListener, JsonParseInterface {

    /**
     * 上报成功
     */
    private final static int MSG_REPORT_SUCCESS_ORDER = 0x0FFFFFFF;
    /**
     * 上报失败
     */
    private final static int MSG_REPORT_LOST_ORDER = 0x00FFFFFF;
    public static List<? extends Map<String, ?>> data;
    public static int recordCount = 0;
    private final String TAG = "AdministratorApproveActivity";
    private Message m;
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
                case Config.SHOW_PROGRESS_DIALOG:
                    HomeBaseActivity.showProgressDialog("交互中…");
                    break;
                case Config.DISMISS_PROGRESS_DIALOG:
                    HomeBaseActivity.dismissProgressDialog();
                    break;
                case MSG_REPORT_SUCCESS_ORDER:
                    LogFactory.e(TAG, "report Success");
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
    private int typePosition = 0;
    private Thread approveThread = new Thread(new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            HttpConnection httpConnection = new HttpConnection();
            try {
                action(httpConnection
                        .getData(HttpConnection.CONNECTION_ADMIN_APPROVE,
                                User.username,
                                TypeContent.APPROVE_TYPE_VALUE[typePosition],
                                "1", "10"));
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
    private TitleBar mTitleBar;
    private Spinner searchtype;
    private Button submit;

    @Override
    protected void installViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_admin_approve);
        initTitleBar();
        searchtype = (Spinner) findViewById(R.id.searchtype);
        submit = (Button) findViewById(R.id.submit);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, TypeContent.APPROVE_TYPE);
        adapter.setDropDownViewResource(R.layout.view_drop_down_item);
        searchtype.setAdapter(adapter);

    }

    @Override
    protected void registerEvents() {
        // TODO Auto-generated method stub
        submit.setOnClickListener(this);
        searchtype.setOnItemSelectedListener(this);
    }

    /**
     * @category 初始化titleBar
     */
    protected void initTitleBar() {

        mTitleBar = (TitleBar) findViewById(R.id.titlebar);

        mTitleBar.changeStyle(STYLE.NOT_BTN_AND_TITLE);

        mTitleBar.centerTextView.setText(R.string.function_xzsp);

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        // threadG = new Thread(approveThread);
        // threadG.start();
        startActivity(new Intent(mContext,
                AdministratorApproveListActivity.class).putExtra("searchType",
                typePosition));
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        // TODO Auto-generated method stub
        typePosition = arg2;
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

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
            JSONObject jsobj = new JSONObject(response);
            if (TableStructure.V_ACT_ADMINI_APPROVE_LIST.equals(jsobj
                    .getString(TableStructure.COVER_HEAD))) {
                if (!"[]".equals(jsobj.getString(TableStructure.COVER_BODY))) {
                    JSONArray jsonTypeData = jsobj
                            .getJSONArray(TableStructure.COVER_BODY);
                    if (null != jsonTypeData) {
                        data = getListData(jsonTypeData);
                    }
                    recordCount = new Integer(
                            jsobj.getString(TableStructure.R_READY_HISTORY_LIST_RECORD_COUNT));

                    startActivity(new Intent(mContext,
                            AdministratorApproveListActivity.class));

                    return MSG_REPORT_SUCCESS_ORDER;

                } else {
                    data = null;
                    err_Msg = "没有数据!";
                }
            } else {
                err_Msg = "数据异常！";
                LogFactory.e(TAG, "not data!");
            }
        }
        return MSG_REPORT_LOST_ORDER;
    }

    private List<? extends Map<String, ?>> getListData(JSONArray jsonTypeData) {

        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        try {
            for (int i = 0; i < jsonTypeData.length(); i++) {
                JSONObject obj = jsonTypeData.getJSONObject(i);

                Map<String, Object> m = new HashMap<String, Object>();
                m.put("data",
                        new AdminApproveBean(obj.getString("ADTIVEID"), obj
                                .getString("ADTIVETITLE"), obj
                                .getString("ADTIVETYPE"), obj
                                .getString("ADTIVECONTENT"), obj
                                .getString("ADTIVEDATA"), obj.getString("RN")));

                data.add(m);

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(mContext, R.string.error_data, Toast.LENGTH_LONG)
                    .show();
        }

        return data;
    }

}
