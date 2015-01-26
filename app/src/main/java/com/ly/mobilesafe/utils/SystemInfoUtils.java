package com.ly.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Administrator on 2015/1/26.
 * 系统信息的工具类
 */
public class SystemInfoUtils {

    /**
     * 获取正在运行的进程的数量
     * @param context
     * @return
     */
    public static int getRunningProcessCount(Context context)
    {
        //PackageManager 包管理器，相当于程序管理器，静态内容
        //ActivityManager 进程管理器，管理手机的活动信息;动态的内容
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        return infos.size();
    }

    /**
     * 获取手机可用的剩余内存
     * @param context
     * @return
     */
    public static  long getAvailMem(Context context)
    {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        return outInfo.availMem;
    }

    /**
     * 获取手机可用的总内存
     * @param context
     * @return
     */
    public static long getTotalMem(Context context)
    {
        //适用于android 2.3以上的版本
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
//        am.getMemoryInfo(outInfo);
//        return  outInfo.totalMem;
        long size = 0;
        File file = new File("/proc/meminfo");
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = br.readLine();
            StringBuilder sb = new StringBuilder();
            for(char c:line.toCharArray())
            {
                if(c>='0' && c<='9')
                {
                    sb.append(c);
                }
            }
            size = Long.parseLong(sb.toString())*1024;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return size;
    }
}
