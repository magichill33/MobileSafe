package com.ly.mobilesafe;

import java.util.List;

import com.ly.mobilesafe.dao.BlackNumberDao;
import com.ly.mobilesafe.domain.BlackNumberInfo;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CallSmsSafeActivity extends Activity {
    public static final String TAG = "CallSmsSafeActivity";
    private ListView lv_callsms_safe;
    private List<BlackNumberInfo> infos;
    private BlackNumberDao dao;
    private CallSmsSafeAdapter adapter;

    private LinearLayout ll_loading;
    /**
     * 分批查询数据
     * 位置 和 条数
     */
    private int offset = 0;
    private int maxnumber = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_sms_safe);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        lv_callsms_safe = (ListView) findViewById(R.id.lv_callsms_safe);
        dao = new BlackNumberDao(this);
        fillData();

        //listView注册一个滚动事件的监听器
        lv_callsms_safe.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState)
                {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        //判断当前listView滚动的位置
                        //获取最后一个可见条目在集合里面的位置
                        int lastPosition = lv_callsms_safe.getLastVisiblePosition();

                        if(lastPosition==(infos.size()-1)&&offset<infos.size())
                        {
                            Log.i(TAG,"列表被移动了最后一个位置");
                            offset += maxnumber;
                            fillData();
                        }
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    private void fillData() {
        ll_loading.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                if(infos==null)
                {
                    infos = dao.findPart(offset,maxnumber);
                }else{
                    infos.addAll(dao.findPart(offset,maxnumber));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ll_loading.setVisibility(View.INVISIBLE);
                        if(adapter==null)
                        {
                            adapter = new CallSmsSafeAdapter();
                            lv_callsms_safe.setAdapter(adapter);
                        }else{
                            adapter.notifyDataSetChanged();
                        }

                    }
                });
            }
        }.start();
    }

    private class CallSmsSafeAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;
            if(convertView==null)
            {
                view = View.inflate(getApplicationContext(),
                        R.layout.list_item_callsms, null);
                holder = new ViewHolder();
                holder.tv_number = (TextView) view.findViewById(R.id.tv_black_number);
                holder.tv_mode = (TextView) view.findViewById(R.id.tv_block_mode);
                holder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
                view.setTag(holder);
            }else{
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }

            holder.tv_number.setText(infos.get(position).getNumber());
            String mode = infos.get(position).getMode();
            if("1".equals(mode))
            {
                holder.tv_mode.setText("电话拦截");
            }else if("2".equals(mode))
            {
                holder.tv_mode.setText("短信拦截");
            }else{
                holder.tv_mode.setText("全部拦截");
            }
            holder.iv_delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new Builder(CallSmsSafeActivity.this);
                    builder.setTitle("警告");
                    builder.setMessage("确定要删除这条记录么?");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dao.delete(infos.get(position).getNumber());
                            infos.remove(position);
                            //通知listView数据适配器更新
                            CallSmsSafeAdapter.this.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.show();
                }
            });
            return view;
        }

    }

    static class ViewHolder{
        TextView tv_number;
        TextView tv_mode;
        ImageView iv_delete;
    }

    private EditText et_blacknumber;
    private CheckBox cb_phone;
    private CheckBox cb_sms;
    private Button btnOK;
    private Button btnCancel;
    /**
     * 添加黑名单事件
     * @param view
     */
    public void addBlackNumber(View view)
    {
        Builder builer = new Builder(CallSmsSafeActivity.this);
        final AlertDialog dialog = builer.create();
        View contentView = View.inflate(this, R.layout.dialog_add_blacknumber, null);
        et_blacknumber = (EditText) contentView.findViewById(R.id.et_blacknumber);
        cb_phone = (CheckBox) contentView.findViewById(R.id.cb_phone);
        cb_sms = (CheckBox) contentView.findViewById(R.id.cb_sms);
        btnCancel = (Button) contentView.findViewById(R.id.cancel);
        btnOK = (Button) contentView.findViewById(R.id.ok);
        dialog.setView(contentView, 0, 0, 0, 0);
        dialog.show();
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String blacknumber = et_blacknumber.getText().toString().trim();
                if(TextUtils.isEmpty(blacknumber))
                {
                    Toast.makeText(getApplicationContext(),
                            "黑名单号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String mode;
                if(cb_phone.isChecked()&&cb_sms.isChecked())
                {
                    mode = "3";
                }else if(cb_phone.isChecked())
                {
                    mode = "1";
                }else if(cb_sms.isChecked())
                {
                    mode = "2";
                }else{
                    Toast.makeText(getApplicationContext(), "请选择拦截模式", Toast.LENGTH_SHORT).show();
                    return;
                }

                dao.add(blacknumber, mode);
                BlackNumberInfo info = new BlackNumberInfo();
                info.setNumber(blacknumber);
                info.setMode(mode);
                infos.add(0, info);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }
}
