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
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.NormalBaseActivity;
import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.constant.LayoutStructure;
import com.lilosoft.xtcm.constant.TableStructure;
import com.lilosoft.xtcm.instantiation.DisposeBean;
import com.lilosoft.xtcm.instantiation.FileBean;
import com.lilosoft.xtcm.instantiation.ReadyDisposeBean;
import com.lilosoft.xtcm.instantiation.User;
import com.lilosoft.xtcm.network.HttpConnection;
import com.lilosoft.xtcm.utils.JsonParseInterface;
import com.lilosoft.xtcm.utils.LogFactory;
import com.lilosoft.xtcm.views.MPProgressBar;
import com.lilosoft.xtcm.views.TitleBar;
import com.lilosoft.xtcm.views.TitleBar.STYLE;
import com.lilosoft.xtcm.views.adapter.SmartWindowAdapter;

/**
 * @category 待处理
 * @author William Liu
 *
 */
public class ReadyDisposeActivity extends NormalBaseActivity implements
        JsonParseInterface {

    /**
     * 上报成功
     */
    private final static int MSG_DISPOSE_SUCCESS_ORDER = 0x0FFFFFFF;
    /**
     * 上报失败
     */
    private final static int MSG_DISPOSE_LOST_ORDER = 0x00FFFFFF;
    /**
     * 没有数据
     */
    private final static int MSG_DISPOSE_NONE_DATA = 0x000FFFFF;
    /**
     * 单条数据采集完毕
     */
    private final static int MSG_DISPOSE_GATHER_DONE = 0x0000FFFF;
    public static DisposeBean bean = null;
    private final String TAG = "ReadyDisposeActivity";
    BaseAdapter baseAdapter = new BaseAdapter() {

        @Override
        public View getView(int i, View view, ViewGroup arg2) {
            view = LayoutInflater.from(ReadyDisposeActivity.this).inflate(
                    R.layout.view_ready_dispose_item, null);
            TextView textViewDispatchTime = (TextView) view
                    .findViewById(R.id.dispatchtime);

            TextView textViewTitle = (TextView) view
                    .findViewById(R.id.q_small_casetitle);
            TextView textViewTime = (TextView) view
                    .findViewById(R.id.q_small_dealwarmingtime);
            textViewDispatchTime.setText(((ReadyDisposeBean) data.get(i).get("data"))
                    .getDISPATCHTIME());
            textViewTitle.setText(((ReadyDisposeBean) data.get(i).get("data"))
                    .getCASEDESCRIPTION());
            textViewTime.setText(((ReadyDisposeBean) data.get(i).get("data"))
                    .getDEALWARMINGTIME());
            String status=((ReadyDisposeBean) data.get(i).get("data")).getCASESTATUS();
            if(status.equals("411")||status.equals("421")){
                textViewTitle.setTextColor(Color.RED);
            }
            return view;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.size();
        }
    };
    private TitleBar mTitleBar;
    private ListView listView;
    private List<? extends Map<String, ?>> data;
    private SmartWindowAdapter adapter;
    private Message m;
    private Thread disposeListThread = new Thread(new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            LogFactory.e(TAG, "disposeList thread start");
            m = new Message();
            m.what = Config.SHOW_PROGRESS_DIALOG;
            myHandle.sendMessage(m);
            try {
                HttpConnection httpConnection = new HttpConnection();
                action(httpConnection.getData(
                        HttpConnection.CONNECTION_READY_DISPOSE_LIST,
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
                LogFactory.e(TAG, "disposeList thread end");
            }
        }
    });
    private Thread disposeThread = new Thread(new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            LogFactory.e(TAG, "disposeOne thread start");
            m = new Message();
            m.what = Config.SHOW_PROGRESS_DIALOG;
            myHandle.sendMessage(m);
            try {
                HttpConnection httpConnection = new HttpConnection();

                ReadyDisposeBean disposeBean = (ReadyDisposeBean) data.get(key)
                        .get("data");

                action(httpConnection.getData(
                        HttpConnection.CONNECTION_READY_DISPOSE,
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
                LogFactory.e(TAG, "disposeOne thread end");
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
                case MSG_DISPOSE_GATHER_DONE:
                    LogFactory.e(TAG, "Data gather_done");
                    break;
                case MSG_DISPOSE_NONE_DATA:
                    Toast.makeText(mContext, R.string.none_data, Toast.LENGTH_LONG)
                            .show();
                    adapter = new SmartWindowAdapter(mContext,
                            new ArrayList<Map<String, Object>>(),
                            R.layout.view_ready_dispose_item, null,
                            LayoutStructure.toReadyDispose);
                    listView.setAdapter(adapter);

                    break;
                case MSG_DISPOSE_SUCCESS_ORDER:
                    LogFactory.e(TAG, "ReadyDisposeActivity Success");

                    // adapter = new SmartWindowAdapter(mContext, data,
                    // R.layout.view_ready_dispose_item, null,
                    // LayoutStructure.toReadyDispose);
                    listView.setAdapter(baseAdapter);

                    break;
                case MSG_DISPOSE_LOST_ORDER:
                    if (null != err_Msg && !"".equals(err_Msg)) {
                        Toast.makeText(mContext, err_Msg, Toast.LENGTH_LONG).show();
                        err_Msg = "";
                    } else {
                        Toast.makeText(mContext, R.string.error_data,
                                Toast.LENGTH_LONG).show();
                    }
                    LogFactory.e(TAG, "ReadyDisposeActivity lost");
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
        setContentView(R.layout.activity_list_normal);
        listView = (ListView) findViewById(R.id.ready_report_list);
        initTitleBar();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initValue();
        initListView();
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
                    threadG = new Thread(disposeThread);
                    threadG.start();
                } else {
                    bean = new DisposeBean("test", "test", "test", "test",
                            "test", "test", "test", "test", "test", "test",
                            "test", "test", "test", "test", "test", null,null);

                    startActivity(new Intent(mContext,
                            QuestionDisposeActivity.class));
                }

            }

        });

    }

    /**
     * @category 初始化titleBar
     */
    protected void initTitleBar() {

        mTitleBar = (TitleBar) findViewById(R.id.titlebar);

        mTitleBar.changeStyle(STYLE.NOT_BTN_AND_TITLE);

        mTitleBar.centerTextView.setText(R.string.function_ready_dispose);

    }

    protected void initValue() {

        mPProgressBar = (MPProgressBar) findViewById(R.id.mPProgressBar);
    }

    protected void initListView() {
        // TODO Auto-generated method stub
        if (Config.NETWORK) {
            threadG = new Thread(disposeListThread);
            threadG.start();
        } else {
            data = getListData();
            SmartWindowAdapter adapter = new SmartWindowAdapter(mContext, data,
                    R.layout.view_ready_dispose_item, null,
                    LayoutStructure.toReadyDispose);
            listView.setAdapter(adapter);
        }
    }

    private List<? extends Map<String, ?>> getListData() {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        Map<String, Object> m = new HashMap<String, Object>();
        m.put("data", new ReadyDisposeBean("test", "test", "test", "test",
                "test", "test", "test", "test", "test", "test", "test", "test",
                "test", "test", "test","test"));

        data.add(m);

        return data;
    }

    private List<? extends Map<String, ?>> getListData(JSONArray jsonTypeData) {

        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        try {
            for (int i = 0; i < jsonTypeData.length(); i++) {
                JSONObject obj = jsonTypeData.getJSONObject(i);

                Map<String, Object> m = new HashMap<String, Object>();
                m.put("data",
                        new ReadyDisposeBean(obj.getString("CASEID"), obj
                                .getString("TASKID"), obj.getString("SIGNID"),
                                obj.getString("HANDLEID"), obj
                                .getString("CASECODE"), obj
                                .getString("CASETITLE"), obj
                                .getString("CASESTATUS"), obj
                                .getString("CASEPARENTITEM1"), obj
                                .getString("CASEPARENTITEM2"), obj
                                .getString("CASEITEM"), obj
                                .getString("DISPATCHTIME"), obj
                                .getString("DEALWARMINGTIME"), obj
                                .getString("DEALEXTENDEDTIME"), obj
                                .getString("DISPATCHTYPE"), obj
                                .getString("DISPATCHPERSONID"),obj.getString("CASEDESCRIPTION")));


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

    @Override
    public int jsonParseToOrder(String response) throws JSONException {
        // TODO Auto-generated method stub
        if (null != response && !"".equals(response)) {
            JSONObject jsobj = new JSONObject(response);
            if (TableStructure.V_ACT_READY_DISPOSE_LIST.equals(jsobj
                    .getString(TableStructure.COVER_HEAD))) {
                if (!"[]".equals(jsobj.getString(TableStructure.COVER_BODY))) {
                    JSONArray jsonTypeData = jsobj
                            .getJSONArray(TableStructure.COVER_BODY);
                    if (null != jsonTypeData) {
                        data = getListData(jsonTypeData);
                    }
                    return MSG_DISPOSE_SUCCESS_ORDER;

                } else {
                    data = null;
                    return MSG_DISPOSE_NONE_DATA;
                }
            } else if (TableStructure.V_ACT_READY_DISPOSE.equals(jsobj
                    .getString(TableStructure.COVER_HEAD))) {
                JSONObject dispose = jsobj
                        .getJSONObject(TableStructure.COVER_BODY);
                List<FileBean> fileList = new ArrayList<FileBean>();

                if (!"[]".equals(dispose.getString("ApproveFileList"))) {
                    JSONArray fileBeans = dispose
                            .getJSONArray("ApproveFileList");
                    for (int i = 0; i < fileBeans.length(); i++) {
                        JSONObject obj = fileBeans.getJSONObject(i);
                        FileBean bean = new FileBean(obj.getString("FName"),
                                obj.getString("FType"), obj.getString("FData"));
                        fileList.add(bean);
                    }
                }
                List<FileBean> fileListHistory = new ArrayList<FileBean>();
                if (!"[]".equals(dispose.getString("DeptFileList"))) {
                    JSONArray fileBeans = dispose
                            .getJSONArray("DeptFileList");
                    for (int i = 0; i < fileBeans.length(); i++) {
                        JSONObject obj = fileBeans.getJSONObject(i);
                        FileBean bean = new FileBean(obj.getString("FName"),
                                obj.getString("FType"), obj.getString("FData"));
                        fileListHistory.add(bean);
                    }
                }

                ReadyDisposeBean disposeBean = (ReadyDisposeBean) data.get(key)
                        .get("data");

                bean = new DisposeBean(disposeBean.getTASKID(),
                        disposeBean.getHANDLEID(), disposeBean.getCASEID(),
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
                        dispose.getString("PutonerdUser"), fileList,fileListHistory);

                startActivity(new Intent(mContext,
                        QuestionDisposeActivity.class));

                return MSG_DISPOSE_GATHER_DONE;
            }
        } else {
            err_Msg = "数据异常！";
            LogFactory.e(TAG, "not data!");
        }
        return MSG_DISPOSE_LOST_ORDER;
    }

}
