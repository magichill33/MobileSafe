package com.ly.mobilesafe;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ly.mobilesafe.domain.TaskInfo;
import com.ly.mobilesafe.engine.TaskInfoProvider;
import com.ly.mobilesafe.utils.SystemInfoUtils;

import java.util.ArrayList;
import java.util.List;


public class TaskManagerActivity extends Activity {
    private TextView tv_process_count;
    private TextView tv_mem_info;
    private LinearLayout ll_loading;
    private ListView lv_task_manager;
    private TextView tv_status;

    private List<TaskInfo> allTaskInfos;
    private List<TaskInfo> userTaskInfos;
    private List<TaskInfo> systemTaskInfos;

    private int processCount;
    private long availMem;
    private long totalMem;

    private TaskManagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        tv_mem_info = (TextView) findViewById(R.id.tv_mem_info);
        tv_process_count = (TextView) findViewById(R.id.tv_process_count);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        lv_task_manager = (ListView) findViewById(R.id.lv_task_manager);
        tv_status = (TextView) findViewById(R.id.tv_status);
        fillData();
        lv_task_manager.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(userTaskInfos!=null&&systemTaskInfos!=null){
                    if(firstVisibleItem>userTaskInfos.size()){
                        tv_status.setText("系统进程："+
                        systemTaskInfos.size()+"个");
                    }else {
                        tv_status.setText("用户进程："+
                                userTaskInfos.size()+"个");
                    }
                }
            }
        });

        lv_task_manager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskInfo taskInfo;
                if(position==0)
                {
                    return;
                }else if(position == userTaskInfos.size() + 1){
                    return;
                }else if(position <=userTaskInfos.size()){
                    taskInfo = userTaskInfos.get(position-1);
                }else {
                    taskInfo = systemTaskInfos.get(position-userTaskInfos.size()-2);
                }
                if(getPackageName().equals(taskInfo.getPackname())){
                    return;
                }
                ViewHolder holder = (ViewHolder) view.getTag();
                if(taskInfo.isChecked()){
                    taskInfo.setChecked(false);
                    holder.cb_status.setChecked(false);
                }else {
                    taskInfo.setChecked(true);
                    holder.cb_status.setChecked(true);
                }
            }
        });
    }

    private void fillData()
    {
        ll_loading.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                allTaskInfos = TaskInfoProvider.getTaskInfos(getApplicationContext());
                userTaskInfos = new ArrayList<TaskInfo>();
                systemTaskInfos = new ArrayList<TaskInfo>();
                for (TaskInfo info:allTaskInfos)
                {
                    if(info.isUserTask()){
                        userTaskInfos.add(info);
                    }else{
                        systemTaskInfos.add(info);
                    }
                }

                //更新设置界面
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ll_loading.setVisibility(View.INVISIBLE);
                        if(adapter==null){
                            adapter = new TaskManagerAdapter();
                            lv_task_manager.setAdapter(adapter);
                        }else {
                            adapter.notifyDataSetChanged();
                        }
                        setTitle();
                    }
                });
            }
        }.start();
    }

    private  void setTitle(){
        processCount = SystemInfoUtils.getRunningProcessCount(this);
        tv_process_count.setText("运行中的进程："+processCount);
        availMem = SystemInfoUtils.getAvailMem(this);
        totalMem = SystemInfoUtils.getTotalMem(this);
        tv_mem_info.setText("剩余/总内存："+
                Formatter.formatFileSize(this,availMem)+"/"
                + Formatter.formatFileSize(this,totalMem));
    }

    /**
     * 选中全部
     * @param view
     */
    public void selectAll(View view){
        for (TaskInfo info:allTaskInfos){
            if(getPackageName().equals(info.getPackname())){
                continue;
            }
            info.setChecked(true);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 选中相反的
     */
    public void selectOppo(View view){
        for (TaskInfo info:allTaskInfos){
            if(getPackageName().equals(info.getPackname())){
                continue;
            }
            info.setChecked(!info.isChecked());
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 清除选中的
     * @param view
     */
    public void killAll(View view){

    }

    /**
     * 进入设置
     * @param view
     */
    public void enterSetting(View view){

    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_memsize;
        CheckBox cb_status;
    }

    private class TaskManagerAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return allTaskInfos.size()+2;
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
            TaskInfo taskInfo;
            if(position==0)
            {
                TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundColor(Color.GRAY);
                tv.setTextColor(Color.WHITE);
                tv.setText("用户进程："+userTaskInfos.size()+"个");
                return tv;
            }else if(position == userTaskInfos.size() + 1){
                TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundColor(Color.GRAY);
                tv.setTextColor(Color.WHITE);
                tv.setText("系统进程："+systemTaskInfos.size()+"个");
                return tv;
            }else if(position <=userTaskInfos.size()){
                taskInfo = userTaskInfos.get(position-1);
            }else {
                taskInfo = systemTaskInfos.get(position-userTaskInfos.size()-2);
            }
            View view;
            ViewHolder holder;
            if(convertView!=null && convertView instanceof RelativeLayout){
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }else{
                view = View.inflate(getApplicationContext(),
                        R.layout.list_item_taskinfo,null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) view.findViewById(R.id.iv_task_icon);
                holder.tv_memsize = (TextView) view.findViewById(R.id.tv_task_memsize);
                holder.tv_name = (TextView) view.findViewById(R.id.tv_task_name);
                holder.cb_status = (CheckBox) view.findViewById(R.id.cb_status);
                view.setTag(holder);
            }
            holder.iv_icon.setImageDrawable(taskInfo.getIcon());
            holder.tv_memsize.setText(Formatter.formatFileSize(getApplicationContext(),
                    taskInfo.getMemsize()));
            holder.tv_name.setText(taskInfo.getName());
            holder.cb_status.setChecked(taskInfo.isChecked());
            if(getPackageName().equals(taskInfo.getPackname()))
            {
                holder.cb_status.setVisibility(View.INVISIBLE);
            }else{
                holder.cb_status.setVisibility(View.VISIBLE);
            }

            return view;
        }
    }

}
