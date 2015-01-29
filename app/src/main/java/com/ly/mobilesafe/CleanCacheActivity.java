package com.ly.mobilesafe;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


public class CleanCacheActivity extends Activity {
    private ProgressBar pb;
    private TextView tv_scan_status;
    private PackageManager pm;
    private LinearLayout ll_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_cache);
        tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
        pb = (ProgressBar) findViewById(R.id.pb);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        scanCache();
    }

    /**
     * 扫描手机里面所有应用程序的缓存信息
     */
    private void scanCache(){
        pm = getPackageManager();
        new Thread(){
            @Override
            public void run() {
                Method getPackageSizeInfoMethod = null;
                Method[] methods = PackageManager.class.getMethods();
                for (Method method:methods){
                    if("getPackageSizeInfo".equals(method.getName())){
                        getPackageSizeInfoMethod = method;
                        break;
                    }
                }

                List<PackageInfo> infos = pm.getInstalledPackages(0);
                pb.setMax(infos.size());
                int progress = 0;
                /**
                 * 用反射方式获取应用程序缓存大小
                 */
               for (PackageInfo info:infos){
                   try {
                       getPackageSizeInfoMethod.invoke(pm,
                               info.packageName,new MyDataObserver());
                       Thread.sleep(50);
                   } catch (IllegalAccessException e) {
                       e.printStackTrace();
                   } catch (InvocationTargetException e) {
                       e.printStackTrace();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   progress++;
                   pb.setProgress(progress);
               }

               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                      tv_scan_status.setText("扫描完毕。。。");
                   }
               });
            }
        }.start();
    }

   public void clearAll(View view){

   }

   private class MyDataObserver extends IPackageStatsObserver.Stub{

       @Override
       public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
           final long cache =pStats.cacheSize;
           long code = pStats.codeSize;
           long data = pStats.dataSize;
           String packname = pStats.packageName;
           final ApplicationInfo info;
           try {
               info = pm.getApplicationInfo(packname,0);
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       tv_scan_status.setText("正在扫描："+info.loadLabel(pm));
                       if(cache>0){
                           View view = View.inflate(getApplicationContext(),
                                   R.layout.list_item_cacheinfo,null);
                           TextView tv_cache = (TextView) view.findViewById(R.id.tv_cache_size);
                           tv_cache.setText("缓存大小："+ Formatter.formatFileSize(getApplicationContext(),
                                   cache));
                           TextView tv_name = (TextView) view.findViewById(R.id.tv_app_name);
                           tv_name.setText(info.loadLabel(pm));
                           ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
                           ll_container.addView(view,0);
                       }
                   }
               });
           } catch (PackageManager.NameNotFoundException e) {
               e.printStackTrace();
           }
       }
   }

}
