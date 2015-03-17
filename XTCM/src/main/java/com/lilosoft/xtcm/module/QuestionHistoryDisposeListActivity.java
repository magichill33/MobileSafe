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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.NormalBaseActivity;
import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.constant.LayoutStructure;
import com.lilosoft.xtcm.constant.TableStructure;
import com.lilosoft.xtcm.instantiation.FileBean;
import com.lilosoft.xtcm.instantiation.HistoryDisposeBean;
import com.lilosoft.xtcm.instantiation.HistoryDisposeListBean;
import com.lilosoft.xtcm.instantiation.User;
import com.lilosoft.xtcm.network.HttpConnection;
import com.lilosoft.xtcm.utils.JsonParseInterface;
import com.lilosoft.xtcm.utils.LogFactory;
import com.lilosoft.xtcm.views.MPProgressBar;
import com.lilosoft.xtcm.views.TitleBar;
import com.lilosoft.xtcm.views.TitleBar.STYLE;
import com.lilosoft.xtcm.views.adapter.SmartWindowAdapter;

/**
 * @category 历史
 * @author William Liu
 *
 */
public class QuestionHistoryDisposeListActivity extends NormalBaseActivity
        implements OnClickListener, JsonParseInterface {

    /**
     * 上报成功
     */
    private final static int MSG_HISTORY_SUCCESS_ORDER = 0x0FFFFFFF;
    /**
     * 上报失败
     */
    private final static int MSG_HISTORY_LOST_ORDER = 0x00FFFFFF;
    /**
     * 没有数据
     */
    private final static int MSG_HISTORY_NONE_DATA = 0x000FFFFF;
    /**
     * 单条数据采集完毕
     */
    private final static int MSG_HISTORY_GATHER_DONE = 0x0000FFFF;
    public static HistoryDisposeBean bean = null;
    private final String TAG = "ReadyDisposeActivity";
    private TitleBar mTitleBar;
    private ListView listView;
    private Button up;
    private Button down;
    private List<? extends Map<String, ?>> data;
    private int dataCount = 0;
    private int pageNum = 1;
    private int maxPage = 1;
    private Message m;
    Thread historyListThread = new Thread(new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            LogFactory.e(TAG, "HistoryList thread start");
            m = new Message();
            m.what = Config.SHOW_PROGRESS_DIALOG;
            myHandle.sendMessage(m);
            try {
                HttpConnection httpConnection = new HttpConnection();
                action(httpConnection.getData(
                        HttpConnection.CONNECTION_QUESTION_HISTORY_LIST,
                        User.username,
                        TableStructure.V_ACT_QUESTION_HISTORY_TYPE_DISPOSE,
                        pageNum + "",
                        TableStructure.V_ACT_QUESTION_HISTORY_LIST_MAX + ""));
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
                LogFactory.e(TAG, "HistoryList thread end");
            }
        }
    });
    private Thread historyThread = new Thread(new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            LogFactory.e(TAG, "History One thread start");
            m = new Message();
            m.what = Config.SHOW_PROGRESS_DIALOG;
            myHandle.sendMessage(m);
            try {
                HttpConnection httpConnection = new HttpConnection();

                HistoryDisposeListBean disposeBean = (HistoryDisposeListBean) data
                        .get(key).get("data");

                action(httpConnection.getData(
                        HttpConnection.CONNECTION_QUESTION_HISTORY,
                        disposeBean.getCASEID()));

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
                LogFactory.e(TAG, "History One thread end");
            }
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
                case Config.SHOW_PROGRESS_DIALOG:
                    showProgressDialog("交互中…");
                    break;
                case Config.DISMISS_PROGRESS_DIALOG:
                    dismissProgressDialog();
                    break;
                case MSG_HISTORY_GATHER_DONE:
                    LogFactory.e(TAG, "Data gather_done");
                    break;
                case MSG_HISTORY_NONE_DATA:
                    Toast.makeText(mContext, R.string.none_data, Toast.LENGTH_LONG)
                            .show();
                    break;
                case MSG_HISTORY_SUCCESS_ORDER:
                    LogFactory.e(TAG, "History Activity Success");

                    SmartWindowAdapter adapter = new SmartWindowAdapter(mContext,
                            data, R.layout.view_history_dispose_item, null,
                            LayoutStructure.toHistoryDisposeItem);
                    listView.setAdapter(adapter);
                    break;
                case MSG_HISTORY_LOST_ORDER:
                    if (null != err_Msg && !"".equals(err_Msg)) {
                        Toast.makeText(mContext, err_Msg, Toast.LENGTH_LONG).show();
                        err_Msg = "";
                    } else {
                        Toast.makeText(mContext, R.string.error_data,
                                Toast.LENGTH_LONG).show();
                    }
                    LogFactory.e(TAG, "History Activity lost");
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
    protected void installViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_question_history_frame);
        initTitleBar();
        initValue();
    }

    @Override
    protected void registerEvents() {
        // TODO Auto-generated method stub
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                key = arg2;
                threadG = new Thread(historyThread);
                threadG.start();
            }

        });
        up.setOnClickListener(this);
        down.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initValue();
        initListView();

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.page_up:
                if (pageNum != 1) {
                    pageNum--;
                    initListView();
                } else {
                    Toast.makeText(mContext, "没有了", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.page_down:
                if (0 == dataCount) {
                    Toast.makeText(mContext, "没有数据", Toast.LENGTH_SHORT).show();
                } else if (pageNum != maxPage) {
                    pageNum++;
                    initListView();
                } else {
                    Toast.makeText(mContext, "没有了", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * @category 初始化titleBar
     */
    protected void initTitleBar() {

        mTitleBar = (TitleBar) findViewById(R.id.titlebar);

        mTitleBar.changeStyle(STYLE.NOT_BTN_AND_TITLE);

        mTitleBar.centerTextView.setText(R.string.function_history_bl);

    }

    protected void initValue() {

        mPProgressBar = (MPProgressBar) findViewById(R.id.mPProgressBar);
        listView = (ListView) findViewById(R.id.history_list);
        up = (Button) findViewById(R.id.page_up);
        down = (Button) findViewById(R.id.page_down);
    }

    protected void initListView() {
        // TODO Auto-generated method stub
        threadG = new Thread(historyListThread);
        threadG.start();

    }

    private List<? extends Map<String, ?>> getListData(JSONArray jsonTypeData) {

        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        try {
            for (int i = 0; i < jsonTypeData.length(); i++) {
                JSONObject obj = jsonTypeData.getJSONObject(i);

                Map<String, Object> m = new HashMap<String, Object>();
                m.put("data",
                        new HistoryDisposeListBean(obj.getString("CASEID"), obj
                                .getString("TASKID"), obj.getString("SIGNID"),
                                obj.getString("HANDLEID"), obj
                                .getString("CASECODE"), obj
                                .getString("CASETITLE"), obj
                                .getString("CASESTATUS"), obj
                                .getString("CASEPARENTITEM1"), obj
                                .getString("CASEPARENTITEM2"), obj
                                .getString("CASEITEM"), obj
                                .getString("DISPATCHTIME"), obj
                                .getString("DISPATCHTYPE"), obj
                                .getString("DISPATCHPERSONID"), obj
                                .getString("FEEDBACKDEALTIME"), obj
                                .getString("RN"),obj.getString("CASEDESCRIPTION")));

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

    private void action(String response) {
        // TODO Auto-generated method stub
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

    @SuppressLint("UseValueOf")
    @Override
    public int jsonParseToOrder(String response) throws JSONException {
        // TODO Auto-generated method stub
        if (null != response && !"".equals(response)) {
            JSONObject jsobj = new JSONObject(response);
            if (TableStructure.V_ACT_QUESTION_HISTORY_LIST.equals(jsobj
                    .getString(TableStructure.COVER_HEAD))) {
                if (!"[]".equals(jsobj.getString(TableStructure.COVER_BODY))) {
                    JSONArray jsonTypeData = jsobj
                            .getJSONArray(TableStructure.COVER_BODY);
                    if (null != jsonTypeData) {
                        data = getListData(jsonTypeData);
                    }
                    dataCount = new Integer(
                            jsobj.getString(TableStructure.R_READY_HISTORY_LIST_RECORD_COUNT));
                    if (0 != dataCount) {
                        maxPage = dataCount
                                / TableStructure.V_ACT_QUESTION_HISTORY_LIST_MAX;
                        if (dataCount
                                % TableStructure.V_ACT_QUESTION_HISTORY_LIST_MAX != 0)
                            maxPage += 1;
                    }
                    return MSG_HISTORY_SUCCESS_ORDER;

                } else {
                    data = null;
                    return MSG_HISTORY_NONE_DATA;
                }
            } else if (TableStructure.V_ACT_QUESTION_HISTORY.equals(jsobj
                    .getString(TableStructure.COVER_HEAD))) {
                JSONObject dispose = jsobj
                        .getJSONObject(TableStructure.COVER_BODY);
                List<FileBean> approveFileList = null;
                List<FileBean> deptFileList = null;
                if (!"[]".equals(dispose.getString("ApproveFileList"))) {
                    JSONArray fileBeans = dispose
                            .getJSONArray("ApproveFileList");
                    approveFileList = new ArrayList<FileBean>();
                    for (int i = 0; i < fileBeans.length(); i++) {
                        JSONObject obj = fileBeans.getJSONObject(i);
                        FileBean bean = new FileBean(obj.getString("FName"),
                                obj.getString("FType"), obj.getString("FData"));
                        approveFileList.add(bean);
                    }
                }
                if (!"[]".equals(dispose.getString("DeptFileList"))) {
                    JSONArray fileBeans = dispose.getJSONArray("DeptFileList");
                    deptFileList = new ArrayList<FileBean>();
                    for (int i = 0; i < fileBeans.length(); i++) {
                        JSONObject obj = fileBeans.getJSONObject(i);
                        FileBean bean = new FileBean(obj.getString("FName"),
                                obj.getString("FType"), obj.getString("FData"));
                        deptFileList.add(bean);
                    }
                }

                bean = new HistoryDisposeBean(
                        dispose.getString("DISPATCHWARMINGTIME"),
                        dispose.getString("CASEITEM"),
                        dispose.getString("CASECODE"),
                        dispose.getString("CASEDESCRIPTION"),
                        dispose.getString("CASESOURCE"),
                        dispose.getString("CASETITLE"),
                        dispose.getString("SIGNTIME"),
                        dispose.getString("GRIDCODE"),
                        dispose.getString("CREATETIME"),
                        dispose.getString("PUTONRECORDWARNINGTIME"),
                        dispose.getString("PUTONRECORDTIME"),
                        dispose.getString("PutonerdUser"),
                        dispose.getString("DeptName"),
                        dispose.getString("FEEDBACKDEALRESULT"),
                        dispose.getString("FEEDBACKDEALTIME"),
                        dispose.getString("ConfrimPerson"), approveFileList,
                        deptFileList);

                startActivity(new Intent(mContext,
                        QuestionHistoryDisposeActivity.class));

                return MSG_HISTORY_GATHER_DONE;
            }
        } else {
            err_Msg = "数据异常！";
            LogFactory.e(TAG, "not data!");
        }
        return MSG_HISTORY_LOST_ORDER;

    }

}
