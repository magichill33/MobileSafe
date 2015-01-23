package com.ly.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import com.ly.mobilesafe.utils.StreamTools;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

    protected static final String TAG = "SplashActivity";
    protected static final int SHOW_UPDATE_DIALOG = 0;
    protected static final int ENTER_HOME = 1;
    protected static final int URL_ERROR = 2;
    protected static final int NETWORK_ERROR = 3;
    protected static final int JSON_ERROR = 4;
    private String description;
    private String apkurl;

    private TextView tv_splash_version;
    private TextView tv_update_info;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sp = getSharedPreferences("config", MODE_PRIVATE);

        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        tv_update_info = (TextView) findViewById(R.id.tv_update_info);
        tv_splash_version.setText("版本号："+getVersionName());
        boolean update = sp.getBoolean("update", false);
        copyDB();
        if(update)
        {
            checkUpdate();
        }else{
            //自动升级已经关闭
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    enterHome();
                }
            }, 2000);
        }
        AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
        aa.setDuration(500);
        findViewById(R.id.rl_root_splash).startAnimation(aa);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_UPDATE_DIALOG:// 显示升级的对话框
                    Log.i(TAG, "显示升级的对话框");
                    showUpdateDialog();
                    break;
                case ENTER_HOME:// 进入主页面
                    enterHome();
                    break;

                case URL_ERROR:// URL错误
                    enterHome();
                    Toast.makeText(getApplicationContext(), "URL错误",Toast.LENGTH_SHORT).show();

                    break;

                case NETWORK_ERROR:// 网络异常
                    enterHome();
                    Toast.makeText(SplashActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    break;

                case JSON_ERROR:// JSON解析出错
                    enterHome();
                    Toast.makeText(SplashActivity.this, "JSON解析出错", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }

    };

    /**
     * 检查是否有新版本，如果有就升级
     */
    private void checkUpdate()
    {
        new Thread(){

            public void run()
            {
                Message msg = Message.obtain();
                long startIime = System.currentTimeMillis();
                try {
                    URL url = new URL(getString(R.string.serverurl));
                    //联网
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(4000);
                    int code = conn.getResponseCode();
                    if(code == 200)
                    {
                        //联网成功
                        InputStream ips = conn.getInputStream();
                        //把流转换成String
                        String result = StreamTools.readFromStream(ips);
                        Log.i(TAG, "联网成功"+result);
                        //json解析
                        JSONObject obj = new JSONObject(result);
                        String version = obj.getString("version");
                        apkurl = obj.getString("apkurl");
                        description = obj.getString("description");

                        //校验是否有新版本
                        if(getVersionName().equals(version))
                        {
                            //版本一致，进入主界面
                            msg.what = ENTER_HOME;
                        }else{
                            //有新版本，弹出升级对话框
                            msg.what = SHOW_UPDATE_DIALOG;
                        }
                    }

                } catch (MalformedURLException e) {
                    msg.what = URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    msg.what = NETWORK_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = JSON_ERROR;
                    e.printStackTrace();
                }finally{
                    long endTime = System.currentTimeMillis();
                    long dTime = endTime - startIime;
                    if(dTime<2000)
                    {
                        try {
                            Thread.sleep(2000-dTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 显示升级对话框
     */
    private void showUpdateDialog()
    {
        AlertDialog.Builder  builder = new Builder(SplashActivity.this);
        builder.setTitle("提示升级");
        builder.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
                dialog.dismiss();
            }
        });
        builder.setMessage(description);
        builder.setPositiveButton("立刻升级", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED))
                {
                    FinalHttp finalhttp = new FinalHttp();
                    finalhttp.download(apkurl,
                            Environment.getExternalStorageDirectory().getAbsolutePath()+
                                    "/mobilesafe2.0.apk", new AjaxCallBack<File>() {
                                @Override
                                public void onFailure(Throwable t, int errorNo,
                                                      String strMsg) {
                                    t.printStackTrace();
                                    Toast.makeText(getApplicationContext(),
                                            "下载失败", Toast.LENGTH_LONG).show();
                                    super.onFailure(t, errorNo, strMsg);
                                }

                                @Override
                                public void onLoading(long count, long current) {
                                    super.onLoading(count, current);
                                    tv_update_info.setVisibility(View.VISIBLE);
                                    //当前下载百分比
                                    int progress = (int) (current/count*100);
                                    tv_update_info.setText("下载进度："+progress+"%");
                                }

                                @Override
                                public void onSuccess(File t) {
                                    super.onSuccess(t);
                                    installAPK(t);
                                }

                                private void installAPK(File t){
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                                    intent.setDataAndType(Uri.fromFile(t),
                                            "application/vnd.android.package-archive");
                                    startActivity(intent);
                                }
                            });
                }
            }
        });
        builder.setNegativeButton("下次再说", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterHome();
            }
        });
        builder.show();
    }

    protected void enterHome()
    {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 得到应用程序的版本名称
     */
    private String getVersionName()
    {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    /**
     * //path 把address.db这个数据库拷贝到data/data/《包名》/files/address.db
     */
    private void copyDB(){
        try {
            File file = new File(getFilesDir(),"address.db");
            if(file.exists()&&file.length()>0)
            {
                Log.i(TAG, "数据存在，不拷贝");
            }else{
                InputStream is = getAssets().open("address.db");
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while((len=is.read(buffer))!=-1)
                {
                    fos.write(buffer,0,len);
                }
                is.close();
                fos.close();

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
