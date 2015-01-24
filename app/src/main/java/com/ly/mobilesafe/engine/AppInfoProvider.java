package com.ly.mobilesafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.ly.mobilesafe.domain.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by magichill33 on 2015/1/24.
 * 业务方法，提供手机里面安装的所有的应用程序信息
 */
public class AppInfoProvider {

    /**
     * 获取所有的安装的应用程序信息
     * @param context
     * @return
     */
    public static List<AppInfo> getAppInfos(Context context)
    {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        for(PackageInfo packInfo:packInfos)
        {
            AppInfo appInfo = new AppInfo();
            String packname = packInfo.packageName;
            String name = packInfo.applicationInfo.loadLabel(pm).toString();
            Drawable icon = packInfo.applicationInfo.loadIcon(pm);
            int flags = packInfo.applicationInfo.flags;
            if((flags& ApplicationInfo.FLAG_SYSTEM)==0)
            {
                //用户程序
                appInfo.setUserApp(true);
            }else{
                //系统程序
                appInfo.setUserApp(false);
            }

            if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)==0)
            {
                //手机内存
                appInfo.setInRom(true);
            }else{
                //手机外在储设备
                appInfo.setInRom(false);
            }
            appInfo.setPackname(packname);
            appInfo.setName(name);
            appInfo.setIcon(icon);
            appInfos.add(appInfo);
        }
        return appInfos;
    }
}
