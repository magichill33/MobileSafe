package com.ly.mobilesafe;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;


public class AntiVirusActivity extends Activity {
    private ProgressBar progressBar;
    private ImageView iv_Scan;
    private PackageManager pm;
    private TextView tv_scan_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_virus);
        tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
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
       /* tv_scan_status.setText("正在初始化8核杀毒引擎...");
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    progressBar.setMax(100);
                    for (int i=0;i<100;i++)
                        progressBar.setProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();*/
        List<PackageInfo> infos = pm.getInstalledPackages(0);
        for (PackageInfo info:infos){

        }
    }

}
