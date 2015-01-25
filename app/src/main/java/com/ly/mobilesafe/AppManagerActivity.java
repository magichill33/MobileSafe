package com.ly.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ly.mobilesafe.domain.AppInfo;
import com.ly.mobilesafe.engine.AppInfoProvider;
import com.ly.mobilesafe.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;


public class AppManagerActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "AppManagerActivity";
    private TextView tv_avail_rom;
    private TextView tv_avail_sd;

    private ListView lv_app_manager;
    private LinearLayout ll_loading;

    private List<AppInfo> appInfos; //所有的应用程序包信息
    private List<AppInfo> userAppInfos; //所有的用户应用程序的集合
    private List<AppInfo> systemAppInfos; //系统应用程序集合

    private TextView tv_status; //当前程序信息的状态

    private PopupWindow popupWindow; //弹出悬浮窗体

    private AppManagerAdapter adapter;

    private AppInfo appInfo; //被点击的条目

    private LinearLayout ll_start;
    private LinearLayout ll_share;
    private LinearLayout ll_uninstall;

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
                dismissPopupWindow();
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

        /**
         * 设置listview的点击事件
         */
        lv_app_manager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    return;
                }else if(position ==(userAppInfos.size()+1))
                {
                    return;
                }else if(position<=userAppInfos.size())
                {
                    int newPos = position - 1;
                    appInfo = userAppInfos.get(newPos);
                }else {
                    int newPos = position - userAppInfos.size() - 2;
                    appInfo = systemAppInfos.get(newPos);
                }

                dismissPopupWindow();
                View contentView = View.inflate(getApplicationContext(),
                        R.layout.popup_app_item,null);
                ll_start = (LinearLayout) contentView.findViewById(R.id.ll_start);
                ll_share = (LinearLayout) contentView.findViewById(R.id.ll_share);
                ll_uninstall = (LinearLayout) contentView.findViewById(R.id.ll_uninstall);

                ll_start.setOnClickListener(AppManagerActivity.this);
                ll_share.setOnClickListener(AppManagerActivity.this);
                ll_uninstall.setOnClickListener(AppManagerActivity.this);

                popupWindow = new PopupWindow(contentView,-2,-2);
                /**
                 * 动画效果的播放必须要求窗体有背景颜色
                 * 透明颜色也是颜色
                 */
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                int[] location = new int[2];
                view.getLocationInWindow(location);
                //在代码里面设置的宽高值，都是像素
                int dip = 60;
                int px = DensityUtil.dip2px(getApplicationContext(),dip);
                Log.i(TAG,"px="+px);
                popupWindow.showAtLocation(parent, Gravity.LEFT|Gravity.TOP,px,location[1]);
                ScaleAnimation sa = new ScaleAnimation(0.3f,1.0f,0.3f,1.0f,
                        Animation.RELATIVE_TO_SELF,0,
                        Animation.RELATIVE_TO_SELF,0.5f);
                sa.setDuration(300);
                AlphaAnimation aa = new AlphaAnimation(0.5f,1.0f);
                aa.setDuration(300);
                AnimationSet set = new AnimationSet(false);
                set.addAnimation(sa);
                set.addAnimation(aa);
                contentView.startAnimation(set);
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

    @Override
    public void onClick(View v) {
        dismissPopupWindow();
        switch (v.getId())
        {
            case R.id.ll_share:
                Log.i(TAG,"分享："+appInfo.getName());
                shareApplication();
                break;
            case R.id.ll_start:
                Log.i(TAG,"启动："+appInfo.getName());
                startApplication();
                break;
            case R.id.ll_uninstall:
                Log.i(TAG,"卸载："+appInfo.getName());
                if(appInfo.isUserApp()){
                    uninstallApplication();
                }else{
                    Toast.makeText(this, "系统应用只有获取root权限才可以卸载", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 分享一个应用程序
     */
    private void shareApplication(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"推荐您使用一款软件,名称叫："+appInfo.getName());
        startActivity(intent);
    }

    /**
     * 卸载应用
     */
    private void uninstallApplication(){
        // <action android:name="android.intent.action.VIEW" />
        // <action android:name="android.intent.action.DELETE" />
        // <category android:name="android.intent.category.DEFAULT" />
        // <data android:scheme="package" />
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setAction(Intent.ACTION_DELETE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:"+appInfo.getPackname()));
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //刷新界面
        fillData();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 开启一个应用程序
     */
    private  void startApplication()
    {
        // 查询这个应用程序的入口activity。 把他开启起来。
        PackageManager pm = getPackageManager();
        // Intent intent = new Intent();
        // intent.setAction("android.intent.action.MAIN");
        // intent.addCategory("android.intent.category.LAUNCHER");
        // //查询出来了所有的手机上具有启动能力的activity。
        // List<ResolveInfo> infos = pm.queryIntentActivities(intent,
        // PackageManager.GET_INTENT_FILTERS);
        Intent intent = pm.getLaunchIntentForPackage(appInfo.getPackname());
        if(intent!=null){
            startActivity(intent);
        }else{
            Toast.makeText(this, "不能启动当前应用", Toast.LENGTH_SHORT).show();
        }
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
            //holder.tv_location.setText(appInfo.getPackname());
            if (appInfo.isInRom()) {
                holder.tv_location.setText("手机内存");
            } else {
                holder.tv_location.setText("外部存储");
            }
            return view;
        }
    }

    static class ViewHolder{
        TextView tv_name;
        TextView tv_location;
        ImageView iv_icon;
    }


    private void dismissPopupWindow()
    {
        if(popupWindow!=null && popupWindow.isShowing())
        {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    @Override
    protected void onDestroy() {
        dismissPopupWindow();
        super.onDestroy();
    }
}
