package com.ly.mobilesafe;

import android.app.Activity;
import android.graphics.Color;
import android.os.Environment;
import android.os.StatFs;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ly.mobilesafe.domain.AppInfo;
import com.ly.mobilesafe.engine.AppInfoProvider;

import java.util.ArrayList;
import java.util.List;


public class AppManagerActivity extends Activity {
    private static final String TAG = "AppManagerActivity";
    private TextView tv_avail_rom;
    private TextView tv_avail_sd;

    private ListView lv_app_manager;
    private LinearLayout ll_loading;

    private List<AppInfo> appInfos; //所有的应用程序包信息
    private List<AppInfo> userAppInfos; //所有的用户应用程序的集合
    private List<AppInfo> systemAppInfos; //系统应用程序集合

    private TextView tv_status; //当前程序信息的状态

    private AppManagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);

        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_avail_rom = (TextView) findViewById(R.id.tv_avail_rom);
        tv_avail_sd = (TextView) findViewById(R.id.tv_avail_sd);

        long sd_size = getAvailSpace(Environment.getExternalStorageDirectory().getAbsolutePath());
        long rom_size = getAvailSpace(Environment.getDataDirectory().getAbsolutePath());
        tv_avail_rom.setText("内存可用空间："+ Formatter.formatFileSize(this,rom_size));
        tv_avail_sd.setText("sd卡可用空间："+Formatter.formatFileSize(this,sd_size));
        
        lv_app_manager = (ListView) findViewById(R.id.lv_app_manager);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        
        fillData();

        //给listView注册一个滚动的监听器
        lv_app_manager.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            /**
             * 滚动的时候调用的方法
             * @param view
             * @param firstVisibleItem 第一个可见条目在listView集合里面的位置。
             * @param visibleItemCount
             * @param totalItemCount
             */

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(userAppInfos!=null && systemAppInfos!=null)
                {
                    if(firstVisibleItem>userAppInfos.size())
                    {
                        tv_status.setText("系统程序："+systemAppInfos.size()+"个");
                    }else {
                        tv_status.setText("用户程序：" + userAppInfos.size() + "个");
                    }
                }
            }
        });
    }

    private void fillData() {
        ll_loading.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                appInfos = AppInfoProvider.getAppInfos(AppManagerActivity.this);
                userAppInfos = new ArrayList<AppInfo>();
                systemAppInfos = new ArrayList<AppInfo>();
                for (AppInfo appInfo:appInfos)
                {
                    if(appInfo.isUserApp())
                    {
                        userAppInfos.add(appInfo);
                    }else{
                        systemAppInfos.add(appInfo);
                    }
                }
                //加载listView的数据适配器
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(adapter == null)
                        {
                            adapter = new AppManagerAdapter();
                            lv_app_manager.setAdapter(adapter);
                        }else{
                            adapter.notifyDataSetChanged();
                        }
                        ll_loading.setVisibility(View.INVISIBLE);
                    }
                });

            }
        }.start();
    }

    /**
     * 获取某个目录的可用空间
     * @param path
     * @return
     */
    private long getAvailSpace(String path){
        StatFs statf = new StatFs(path);
        long size = statf.getBlockSize(); //获取分区大小
        long count = statf.getAvailableBlocks(); //获取可用分区的个数
        return size*count;
    }

    private class AppManagerAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return appInfos.size()+2;
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
            AppInfo appInfo;
            if(position==0){//显示用户程序有多少个的小标签
                TextView tv = new TextView(getApplicationContext());
                tv.setTextColor(Color.WHITE);
                tv.setBackgroundColor(Color.GRAY);
                tv.setText("用户程序："+userAppInfos.size()+"个");
                return tv;
            }else if(position == (userAppInfos.size()+1)) //显示系统程标签
            {
                TextView tv = new TextView(getApplicationContext());
                tv.setTextColor(Color.WHITE);
                tv.setBackgroundColor(Color.GRAY);
                tv.setText("系统程序："+systemAppInfos.size()+"个");
                return tv;
            }else if(position<=userAppInfos.size()) //用户程序
            {
                int newPos = position - 1;
                appInfo = userAppInfos.get(newPos);
            }else{ //系统程序
                int newPos = position - userAppInfos.size() - 2;
                appInfo = systemAppInfos.get(newPos);
            }
            View view;
            ViewHolder holder;
            if(convertView!=null && convertView instanceof RelativeLayout)
            {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }else{
                view = View.inflate(getApplicationContext(),
                        R.layout.list_item_appinfo,null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) view.findViewById(R.id.iv_app_icon);
                holder.tv_location = (TextView) view.findViewById(R.id.tv_app_location);
                holder.tv_name = (TextView) view.findViewById(R.id.tv_app_name);
                view.setTag(holder);

            }

            holder.iv_icon.setImageDrawable(appInfo.getIcon());
            holder.tv_name.setText(appInfo.getName());
            holder.tv_location.setText(appInfo.getPackname());
            return view;
        }
    }

    static class ViewHolder{
        TextView tv_name;
        TextView tv_location;
        ImageView iv_icon;
    }

}
