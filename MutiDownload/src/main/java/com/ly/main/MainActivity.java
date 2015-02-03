package com.ly.main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static final int DOWNLOAD_ERROR = 1;
    private static final int THREAD_ERROR = 2;
    private static final int DOWNLOAD_FINISH = 3;
    private static final String TAG = "MainActivity";
    private EditText et_path;
    private EditText et_count;

    private int threadCount = 3; //线程数量
    private long blockSize; //每个下载区块的大小
    /**
     * 存放进度条
     * @param savedInstanceState
     */
    private LinearLayout ll_container;

    private int runningThreadCount; //正在运行的线程的数量

    /**
     * 进度条集合
     */
    private List<ProgressBar> pbs;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_path = (EditText) findViewById(R.id.et_path);
        et_count = (EditText) findViewById(R.id.et_count);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
    }

    public void downLoad(View view){
        final String path = et_path.getText().toString().trim();
        if (TextUtils.isEmpty(path)){
            Toast.makeText(this,"请输入下载路径",Toast.LENGTH_SHORT).show();
            return;
        }
        String count = et_count.getText().toString().trim();
        if (TextUtils.isEmpty(count)){
            Toast.makeText(this,"请输入线程数量",Toast.LENGTH_SHORT).show();
            return;
        }
        threadCount = Integer.parseInt(count);
        ll_container.removeAllViews(); //清空掉旧的进度条
        pbs = new ArrayList<ProgressBar>();
        for (int j=0;j<threadCount;j++){
            ProgressBar pb = (ProgressBar) View.inflate(this,R.layout.pb,null);
            ll_container.addView(pb);
            pbs.add(pb);
        }
        Toast.makeText(this,"开始下载",Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    int code = conn.getResponseCode();
                    if (code == 200){
                        long size = conn.getContentLength();
                        //得到服务端返回的文件的大小
                        Log.i(TAG,"服务器文件的大小："+size);
                        blockSize = size/threadCount;
                        //1.首先在本地创建一个大小跟服务器一模一样的空白文件
                        File file = new File(Environment.getExternalStorageDirectory(),
                                getFileName(path));
                        RandomAccessFile raf = new RandomAccessFile(file,"rw");
                        raf.setLength(size);
                        //2.开启若干个子线程分别去下载对应的资源
                        runningThreadCount = threadCount;
                        for (int i = 1;i<=threadCount;i++){
                            long startIndex = (i-1)*blockSize;
                            long endIndex = i*blockSize -1;
                            if (i==threadCount){
                                endIndex = size -1;
                            }
                            Log.i(TAG,"开启线程："+i+"下载的位置："+startIndex
                            +"~"+endIndex);
                            int threadSize = (int) (endIndex - startIndex);
                            pbs.get(i-1).setMax(threadSize);
                            new DownloadThread(i,startIndex,endIndex,path).start();
                        }
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what = DOWNLOAD_ERROR;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    private String getFileName(String path){
        int start = path.lastIndexOf("/");
        return path.substring(start);
    }

    private class DownloadThread extends Thread{

        private int threadId;
        private long startIndex;
        private long endIndex;
        private String path;

        private DownloadThread(int threadId, long startIndex, long endIndex, String path) {
            this.threadId = threadId;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.path = path;
        }

        @Override
        public void run() {
            int total = 0; //当前线程下载的总大小
            //存放下载位置的文件
            File positionFile = new File(Environment.getExternalStorageDirectory(),
                    getFileName(path)+threadId+".txt");
            try {
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                //接着从上一次的位置继续下载数据
                if (positionFile.exists() && positionFile.length()>0){
                    //判断是否有记录
                    FileInputStream fis = new FileInputStream(positionFile);
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(fis));
                    String lasttotalStr = br.readLine();
                    int lastTotal = Integer.parseInt(lasttotalStr);
                    Log.i(TAG,"上次线程"+threadId+"上载的总大小为："+lastTotal);
                    total += lastTotal;
                    startIndex += lastTotal;
                    fis.close();
                    br.close();
                }
                conn.setRequestProperty("Range","bytes="+startIndex+
                    "-"+endIndex);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(5000);
                int status = conn.getResponseCode();
                Log.i(TAG,"status::"+status);
                if (status/100 == 2){
                    InputStream ips = conn.getInputStream();
                    File file = new File(Environment.getExternalStorageDirectory(),
                            getFileName(path));
                    RandomAccessFile raf = new RandomAccessFile(file,"rw");
                    raf.seek(startIndex); //指定文件开始写的位置
                    Log.i(TAG,"第"+threadId+"个线程：写文件的开始位置："+
                            startIndex);
                    int len = 0;
                    byte[] buffer = new byte[1024];
                    while ((len = ips.read(buffer))!=-1){
                        RandomAccessFile rf = new RandomAccessFile(positionFile,
                                "rwd"); //不用磁盘缓存直接写
                        raf.write(buffer,0,len);
                        total +=len;
                        rf.write(String.valueOf(total).getBytes()); //将下载的位置写入文件
                        rf.close();
                        pbs.get(threadId-1).setProgress(total);
                    }
                    ips.close();
                    raf.close();
                }else {
                    Log.i(TAG,"访问网络出错");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.what = THREAD_ERROR;
                handler.sendMessage(msg);
            }finally {
                synchronized (MainActivity.class){
                    Log.i(TAG,"线程"+threadId+"下载完毕了");
                    runningThreadCount--;
                    if (runningThreadCount<1){
                        Log.i(TAG,"所有的线程都工作完毕了，删除临时记录的文件");
                        for (int i = 1;i<=threadCount;i++){
                            File f = new File(Environment.getExternalStorageDirectory(),
                                    getFileName(path) + i + ".txt");
                            Log.i(TAG,"是否删除："+f.delete());
                        }
                        Message msg = Message.obtain();
                        msg.what = DOWNLOAD_FINISH;
                        handler.sendMessage(msg);
                    }
                }
            }
        }
    }


}
