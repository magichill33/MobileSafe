package com.ly.main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImage;
import com.loopj.android.image.SmartImageView;
import com.ly.domain.NewInfo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends Activity {
    private static final String TAG = "MainActivity2";
    private final int SUCCESS = 0;
    private final int FAILED = 1;
    private List<NewInfo> newInfoList;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS:
                    newInfoList = (List<NewInfo>) msg.obj;
                    lvNews.setAdapter(new MyAdapter());
                    break;
                case FAILED:
                    Toast.makeText(MainActivity2.this,"当前网络崩溃了",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private ListView lvNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);

        init();
    }

    private void init(){
        lvNews = (ListView) findViewById(R.id.lv_news);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<NewInfo> infos = getNewsFromInternet();
                Message msg = new Message();
                if (infos!=null){
                    msg.what = SUCCESS;
                    msg.obj = infos;
                }else {
                    msg.what = FAILED;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    private List<NewInfo> getNewsFromInternet(){
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet("http://192.168.0.113:8080/NetEaseServer/new.xml");
        try {
            HttpResponse response = client.execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 200){
                InputStream ips = response.getEntity().getContent();
                List<NewInfo> infos = getNewListFromInputStream(ips);
                return infos;
            }else {
                Log.i(TAG, "访问失败: " + statusCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (client!=null){
                client.getConnectionManager().shutdown();
            }
        }
        return null;
    }

    private List<NewInfo> getNewListFromInputStream(InputStream ips) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(ips,"utf-8");
        int eventType = parser.getEventType();
        List<NewInfo> newInfoList = null;
        NewInfo newInfo = null;
        while (eventType!=XmlPullParser.END_DOCUMENT){
            String tagName = parser.getName(); //节点名称
            switch (eventType){
                case XmlPullParser.START_TAG:
                    if ("news".equals(tagName)){
                        newInfoList = new ArrayList<NewInfo>();
                    }else if ("new".equals(tagName)){
                        newInfo = new NewInfo();
                    }else if ("title".equals(tagName)){
                        newInfo.setTitle(parser.nextText());
                    }else if ("detail".equals(tagName)){
                        newInfo.setDetail(parser.nextText());
                    }else if ("comment".equals(tagName)){
                        newInfo.setComment(parser.nextText());
                    }else if ("image".equals(tagName)){
                        newInfo.setImageUrl(parser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("new".equals(tagName)){
                        newInfoList.add(newInfo);
                    }
                    break;
            }
            eventType = parser.next(); //取下一个事件类型
        }
        return newInfoList;
    }

    static class ViewHolder{
        public TextView tvTitle;
        public TextView tvDetail;
        public TextView tvComment;
        public SmartImageView sivIcon;
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return newInfoList.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            ViewHolder holder = null;
            if (view == null){
                holder = new ViewHolder();
                view = LayoutInflater.from(MainActivity2.this).inflate(R.layout.listview_item,null);
                holder.tvTitle = (TextView) view.findViewById(R.id.tv_listview_item_title);
                holder.tvDetail = (TextView) view.findViewById(R.id.tv_listview_item_detail);
                holder.tvComment = (TextView) view.findViewById(R.id.tv_listview_item_comment);
                holder.sivIcon = (SmartImageView) view.findViewById(R.id.siv_listView_item_icon);
                view.setTag(holder);
            }else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            NewInfo info = newInfoList.get(position);
            holder.tvTitle.setText(info.getTitle());
            holder.tvDetail.setText(info.getDetail());
            holder.tvComment.setText(info.getComment()+"跟帖");
            holder.sivIcon.setImageUrl(info.getImageUrl());
            return view;
        }
    }

}
