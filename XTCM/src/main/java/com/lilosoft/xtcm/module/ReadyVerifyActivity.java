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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.HomeBaseActivity;
import com.lilosoft.xtcm.base.TabBaseActivity;
import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.constant.LayoutStructure;
import com.lilosoft.xtcm.constant.TableStructure;
import com.lilosoft.xtcm.instantiation.FileBean;
import com.lilosoft.xtcm.instantiation.User;
import com.lilosoft.xtcm.instantiation.VerifyBean;
import com.lilosoft.xtcm.instantiation.VerifyListBean;
import com.lilosoft.xtcm.network.HttpConnection;
import com.lilosoft.xtcm.utils.JsonParseInterface;
import com.lilosoft.xtcm.utils.LogFactory;
import com.lilosoft.xtcm.views.MPProgressBar;
import com.lilosoft.xtcm.views.adapter.SmartWindowAdapter;

/**
 * @category 待核实
 * @author William Liu
 *
 */
public class ReadyVerifyActivity extends TabBaseActivity implements
        JsonParseInterface {

    /**
     * 上报成功
     */
    private final static int MSG_VERIFY_SUCCESS_ORDER = 0x0FFFFFFF;
    /**
     * 上报失败
     */
    private final static int MSG_VERIFY_LOST_ORDER = 0x00FFFFFF;
    /**
     * 没有数据
     */
    private final static int MSG_VERIFY_NONE_DATA = 0x000FFFFF;
    /**
     * 单条数据采集完毕
     */
    private final static int MSG_VERIFY_GATHER_DONE = 0x0000FFFF;
    public static VerifyBean bean = null;
    private final String TAG = "ReadyVerifyActivity";
    private ListView listView;
    private List<? extends Map<String, ?>> data;
    private SmartWindowAdapter adapter;
    private Message m;
    /**
     * @category 核实列表请求处理
     */
    private Thread verifyListThread = new Thread(new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            LogFactory.e(TAG, "Verify List thread start");
            m = new Message();
            m.what = Config.SHOW_PROGRESS_DIALOG;
            myHandle.sendMessage(m);
            try {
                HttpConnection httpConnection = new HttpConnection();
                action(httpConnection.getData(
                        HttpConnection.CONNECTION_READY_VERIFY_LIST,
                        User.username));
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
                LogFactory.e(TAG, "Verify List thread end");
            }
        }

    });
    /**
     * @category 核实单条数据请求
     */
    private Thread verifyThread = new Thread(new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            LogFactory.e(TAG, "Verify One thread start");
            m = new Message();
            m.what = Config.SHOW_PROGRESS_DIALOG;
            myHandle.sendMessage(m);
            try {
                HttpConnection httpConnection = new HttpConnection();
                VerifyListBean examineBean = (VerifyListBean) data.get(key)
                        .get("data");
                action(httpConnection.getData(
                        HttpConnection.CONNECTION_READY_VERIFY,
                        examineBean.getCASEID()));
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
                LogFactory.e(TAG, "Verify One thread end");
            }
        }
    });
    private Thread threadG = null;
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
                case MSG_VERIFY_GATHER_DONE:
                    LogFactory.e(TAG, "Data gather_done");
                    break;
                case MSG_VERIFY_NONE_DATA:
                    Toast.makeText(mContext, R.string.none_data, Toast.LENGTH_LONG)
                            .show();
                    adapter = new SmartWindowAdapter(ReadyVerifyActivity.this,
                            new ArrayList<Map<String, Object>>(),
                            R.layout.view_ready_verify_item, null,
                            LayoutStructure.toVerifyItem);
                    listView.setAdapter(adapter);
                    break;
                case MSG_VERIFY_SUCCESS_ORDER:
                    LogFactory.e(TAG, "Verify Success");
                    adapter = new SmartWindowAdapter(ReadyVerifyActivity.this,
                            data, R.layout.view_ready_verify_item, null,
                            LayoutStructure.toVerifyItem);
                    listView.setAdapter(adapter);
                    break;
                case MSG_VERIFY_LOST_ORDER:
                    if (null != err_Msg && !"".equals(err_Msg)) {
                        Toast.makeText(mContext, err_Msg, Toast.LENGTH_LONG).show();
                        err_Msg = "";
                    } else {
                        Toast.makeText(mContext, R.string.error_data,
                                Toast.LENGTH_LONG).show();
                    }
                    LogFactory.e(TAG, "Verify lost");
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
    private int key = 0;

    @Override
    protected void initListView() {
        // TODO Auto-generated method stub
        if (Config.NETWORK) {
            threadG = new Thread(verifyListThread);
            threadG.start();
        } else {
            data = getListData();
            SmartWindowAdapter adapter = new SmartWindowAdapter(
                    ReadyVerifyActivity.this, data,
                    R.layout.view_ready_verify_item, null,
                    LayoutStructure.toVerifyItem);
            listView.setAdapter(adapter);
        }

    }

    @Override
    protected void installViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_ready_verify);
        listView = (ListView) findViewById(R.id.ready_report_list);
        mPProgressBar = (MPProgressBar) findViewById(R.id.mPProgressBar);

    }

    @Override
    protected void registerEvents() {
        // TODO Auto-generated method stub
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                if (Config.NETWORK) {
                    key = arg2;
                    threadG = new Thread(verifyThread);
                    threadG.start();
                } else {
                    bean = new VerifyBean("test", "test", "test", "test",
                            "test", "test", "test", "test", "test", "test",
                            "test", "test", "test", null);

                    startActivity(new Intent(mContext,
                            QuestionVerifyActivity.class));
                }

            }

        });
    }

    private List<? extends Map<String, ?>> getListData() {

        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        Map<String, Object> m = new HashMap<String, Object>();
        m.put("data", new VerifyListBean("test", "test", "test", "test",
                "test", "test", "test", "test", "test", "test", "test", "test",
                "test", "test", "test", "test", "test", "test"));

        data.add(m);

        return data;

    }

    private List<? extends Map<String, ?>> getListData(JSONArray jsonTypeData) {

        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        try {
            for (int i = 0; i < jsonTypeData.length(); i++) {
                JSONObject obj = jsonTypeData.getJSONObject(i);

                Map<String, Object> m = new HashMap<String, Object>();

                VerifyListBean lll = new VerifyListBean(
                        obj.getString("CASEID"), obj.getString("TASKID"),
                        obj.getString("SIGNID"), obj.getString("HANDLEID"),
                        obj.getString("VERIFICATIONID"),
                        obj.getString("INSPECTID"), obj.getString("CASECODE"),
                        obj.getString("CASETITLE"),
                        obj.getString("CASESTATUS"),
                        obj.getString("CASEPARENTITEM1"),
                        obj.getString("CASEPARENTITEM2"),
                        obj.getString("CASEITEM"),
                        obj.getString("SIGNDEPTCODE"),
                        obj.getString("DEALUSERID"),
                        obj.getString("FEEDBACKDEALTIME"),
                        obj.getString("VERIFYPERSON"),
                        obj.getString("BEGINVERIFYTIME"),
                        obj.getString("VERIFYPERSON1"));

                m.put("data", lll);

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
            if (TableStructure.V_ACT_READY_VERIFY_LIST.equals(jsobj
                    .getString(TableStructure.COVER_HEAD))) {
                if (!"[]".equals(jsobj.getString(TableStructure.COVER_BODY))) {
                    JSONArray jsonTypeData = jsobj
                            .getJSONArray(TableStructure.COVER_BODY);
                    if (null != jsonTypeData) {
                        data = getListData(jsonTypeData);
                    }
                    return MSG_VERIFY_SUCCESS_ORDER;

                } else {
                    data = null;
                    return MSG_VERIFY_NONE_DATA;
                }

            } else if (TableStructure.V_ACT_READY_VERIFY.equals(jsobj
                    .getString(TableStructure.COVER_HEAD))) {
                JSONObject verify = jsobj
                        .getJSONObject(TableStructure.COVER_BODY);
                List<FileBean> fileList = null;
                if (!"[]".equals(verify.getString("ApproveFileList"))) {
                    JSONArray fileBeans = verify
                            .getJSONArray("ApproveFileList");
                    fileList = new ArrayList<FileBean>();
                    for (int i = 0; i < fileBeans.length(); i++) {
                        JSONObject obj = fileBeans.getJSONObject(i);
                        FileBean bean = new FileBean(obj.getString("FName"),
                                obj.getString("FType"), obj.getString("FData"));
                        fileList.add(bean);
                    }
                }

                bean = new VerifyBean(((VerifyListBean) data.get(key).get(
                        "data")).getINSPECTID(), verify.getString("CASEITEM"),
                        verify.getString("CASEITEM"),
                        verify.getString("CASECODE"),
                        verify.getString("CASEDESCRIPTION"),
                        verify.getString("CASESOURCE"),
                        verify.getString("CASETITLE"),
                        verify.getString("SIGNTIME"),
                        verify.getString("GRIDCODE"),
                        verify.getString("CREATETIME"),
                        verify.getString("PUTONRECORDWARNINGTIME"),
                        verify.getString("PUTONRECORDTIME"),
                        verify.getString("PutonerdUser"), fileList);

                startActivity(new Intent(mContext, QuestionVerifyActivity.class));
                return MSG_VERIFY_GATHER_DONE;

            }
        } else {
            err_Msg = "数据异常！";
            LogFactory.e(TAG, "not data!");
        }
        return MSG_VERIFY_LOST_ORDER;
    }
}
