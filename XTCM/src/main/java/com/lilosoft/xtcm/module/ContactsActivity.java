package com.lilosoft.xtcm.module;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.NormalBaseActivity;
import com.lilosoft.xtcm.dao.CommonNumberDao;
import com.lilosoft.xtcm.instantiation.Contacts;
import com.lilosoft.xtcm.network.HttpConnection;
import com.lilosoft.xtcm.views.FunctionItem;
import com.lilosoft.xtcm.views.MPProgressBar;
import com.lilosoft.xtcm.views.TitleBar;
import com.lilosoft.xtcm.views.TitleBar.STYLE;

/**
 * 联系人Activity
 */

public class ContactsActivity extends NormalBaseActivity implements OnClickListener {

    private static final int SUCCESS_UPDATA = 1;
    private static final int FALIED_UPDATA = 0;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS_UPDATA:
                    dismissProgressDialog();
                    Toast.makeText(mContext, "更新联系人数据成功，请进入联系人列表查看！", 1).show();
                    break;
                case FALIED_UPDATA:
                    dismissProgressDialog();
                    Toast.makeText(mContext, "更新联系人数据失败，请稍后再试！", 1).show();
                    break;
                default:
                    break;
            }
        }
    };
    private TitleBar mTitleBar;
    private FunctionItem functionItem;
    private FunctionItem functionItem1;

    @Override
    protected void installViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_contacts);
        initTitleBar();

        functionItem = (FunctionItem) findViewById(R.id.f_contactslist);
        functionItem1 = (FunctionItem) findViewById(R.id.f_updata);
        mPProgressBar = (MPProgressBar) findViewById(R.id.mPProgressBar);
    }

    private void initTitleBar() {
        // TODO Auto-generated method stub
        mTitleBar = (TitleBar) findViewById(R.id.titlebar);

        mTitleBar.changeStyle(STYLE.NOT_BTN_AND_TITLE);

        mTitleBar.centerTextView.setText("联系人");
    }

    @Override
    protected void registerEvents() {
        // TODO Auto-generated method stub
        functionItem.setOnClickListener(this);
        functionItem1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.f_contactslist:
                startActivity(new Intent(mContext, ContactsListActivity.class));
                break;
            case R.id.f_updata:
                updataContactsData();
                break;

            default:
                break;
        }
    }

    private void updataContactsData() {
        new AsyncTask<Void, Void, String>(){
            protected void onPreExecute() {
                showProgressDialog("正在更新数据");
            };

            @Override
            protected String doInBackground(Void... params) {
                HttpConnection hc = new HttpConnection();
                try {
                    return hc.getData(HttpConnection.CONNECTION_CONTACTS_DATA);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String result) {
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(mContext, "获取数据失败", 1);
                    finish();
                }else{
                    try {
                        Gson gson = new Gson();
                        final Contacts jc = gson.fromJson(result, Contacts.class);
                        new Thread() {
                            public void run() {
                                boolean b = CommonNumberDao.writeIntoDataBase(
                                        mContext, jc.body);
                                Message msg = Message.obtain();
                                if (b) {
                                    msg.what = SUCCESS_UPDATA;
                                } else {
                                    msg.what = FALIED_UPDATA;
                                }
                                mHandler.sendMessage(msg);
                            };
                        }.start();
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            };
        }.execute();

    }


}
