package com.ly.mobilesafe;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ly.mobilesafe.dao.AntivirusDao;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.List;


public class AntiVirusActivity extends Activity {
    protected static final int SCANING = 0;
    protected static final int FINISH = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SCANING:
                    ScanInfo scanInfo = (ScanInfo) msg.obj;
                    tv_scan_status.setText("正在扫描："+scanInfo.name);
                    TextView tv = new TextView(getApplicationContext());
                    if (scanInfo.isVirus){
                        tv.setText("发现病毒："+scanInfo.name);
                        tv.setTextColor(Color.RED);
                    }else {
                        tv.setText("扫描安全："+scanInfo.name);
                        tv.setTextColor(Color.BLACK);
                    }
                    ll_container.addView(tv,0);
                    break;
                case FINISH:
                    tv_scan_status.setText("扫描完毕");
                    iv_Scan.clearAnimation();
                    break;
            }
        }
    };
    private ProgressBar progressBar;
    private ImageView iv_Scan;
    private PackageManager pm;
    private TextView tv_scan_status;
    private LinearLayout ll_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_virus);
        tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        iv_Scan = (ImageView) findViewById(R.id.iv_scan);
        RotateAnimation ra = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,
                0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        ra.setDuration(1000);
        ra.setRepeatCount(Animation.INFINITE);
        iv_Scan.startAnimation(ra);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        scanVirus();
    }

    /**
     * 扫描病毒
     */
    private void scanVirus(){
        pm = getPackageManager();
        tv_scan_status.setText("正在初始化8核杀毒引擎...");
        new Thread(){
            @Override
            public void run() {
                List<PackageInfo> infos = pm.getInstalledPackages(0);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressBar.setMax(infos.size());
                int progress = 0;
                for (PackageInfo info:infos){
                    //apk文件的完整的路径
                    String sourceDir = info.applicationInfo.sourceDir;
                    //zip包
                    String md5 = getFileMD5(sourceDir);
                    ScanInfo scanInfo = new ScanInfo();
                    scanInfo.packname = info.packageName;
                    scanInfo.name = info.applicationInfo.loadLabel(pm).toString();
                    if(AntivirusDao.isVirus(md5)){
                        scanInfo.isVirus = true;
                    }else {
                        scanInfo.isVirus = false;
                    }
                    Message msg = Message.obtain();
                    msg.obj = scanInfo;
                    msg.what = SCANING;
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progress++;
                    progressBar.setProgress(progress);
                }
                Message msg = Message.obtain();
                msg.what = FINISH;
                handler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 获取文件的md5值
     */
    private String getFileMD5(String path){
        try {
            //获取一个文件的特征信息，签名信息
            File file = new File(path);
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len=fis.read(buffer))!=-1){
                digest.update(buffer,0,len);
            }
           // byte[] result = digest.digest(password.getBytes());
            byte[] result = digest.digest();
            StringBuffer sb = new StringBuffer();
            // 把没一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                // System.out.println(str);
                if (str.length() == 1) {
                    sb.append("0");
                }
                sb.append(str);
            }

            // 标准的md5加密后的结果
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 扫描信息的内部类
     */
    class ScanInfo{
        String packname;
        String name;
        boolean isVirus;
    }

}
