package com.ly.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ly.ui.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerDemo extends Activity {

    private EditText et_input;
    private ImageView iv_downArrow;

    private List<String> msgList;

    private PopupWindow popwin;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_demo);

        et_input = (EditText) findViewById(R.id.et_input);
        iv_downArrow = (ImageView) findViewById(R.id.iv_down_arrow);
        msgList = new ArrayList<String>();
        for (int i=0;i<20;i++){
            msgList.add("1000000000"+i);
        }

        initListView();

        iv_downArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popwin = new PopupWindow(SpinnerDemo.this);
                popwin.setWidth(et_input.getWidth());
                popwin.setHeight(200);
                popwin.setContentView(listView);
                popwin.setOutsideTouchable(true);//点击popWin以外的区域，自动关闭
                popwin.showAsDropDown(et_input,0,0); //设置弹出窗口显示的位置
            }
        });
    }

    private void initListView(){
        listView = new ListView(this);
        listView.setBackgroundResource(R.drawable.listview_background);
        listView.setDivider(null); //设置条目之间的分隔线为null
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapter(new MyListAdapter());
    }

    static class ViewHolder{
        public TextView textView;
        public ImageView imageView;

    }

    private class MyListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return msgList.size();
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
            View view = null;
            ViewHolder holder = null;
            if (convertView == null){
                view = View.inflate(getApplicationContext(),R.layout.list_item,null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) view.findViewById(R.id.delete);
                holder.textView = (TextView) view.findViewById(R.id.tv_list_item);
                view.setTag(holder);
            }else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            holder.textView.setText(msgList.get(position));
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除对应的条目
                    msgList.remove(position);
                    //刷新listView
                    MyListAdapter.this.notifyDataSetChanged();
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    et_input.setText(msgList.get(position));
                    popwin.dismiss();
                }
            });
            return view;
        }
    }
}
