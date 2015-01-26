package com.ly.mobilesafe.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.ly.mobilesafe.R;
import com.ly.mobilesafe.domain.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 提供手机里面的进程信息
 * Created by Administrator on 2015/1/26.
 */
public class TaskInfoProvider {

    public static List<TaskInfo> getTaskInfos(Context context)
    {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
        for (ActivityManager.RunningAppProcessInfo processInfo:processInfos)
        {
            TaskInfo taskInfo = new TaskInfo();
            String packName = processInfo.processName;
            taskInfo.setPackname(packName);
            Debug.MemoryInfo[] memoryInfos = am.getProcessMemoryInfo(new int[]{processInfo.pid});
            long memsize = memoryInfos[0].getTotalPrivateDirty()*1024;
            taskInfo.setMemsize(memsize);

            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(packName,0);
                Drawable icon = applicationInfo.loadIcon(pm);
                String name = applicationInfo.loadLabel(pm).toString();
                taskInfo.setIcon(icon);
                taskInfo.setName(name);
                if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0)
                {
                    taskInfo.setUserTask(true);
                }else
                {
                    taskInfo.setUserTask(false);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                taskInfo.setIcon(context.getResources().getDrawable(
                        R.drawable.ic_default
                ));
                taskInfo.setName(packName);
            }
            taskInfos.add(taskInfo);
        }
        return taskInfos;
    }
}
