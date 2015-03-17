package com.lilosoft.xtcm.module;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.NormalBaseActivity;
import com.lilosoft.xtcm.constant.Config;

/**
 * @category 录音
 * @author William Liu
 *
 */
@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
public class AudioRecordActivity extends NormalBaseActivity implements
        OnClickListener, OnItemClickListener {
    private Button myButton1;
    private Button myButton2;
    private Button myButton3;
    private Button myButton4;
    private ListView myListView1;

    private Button record_sure;
    private Button record_cancel;

    private String strTempFile;
    private File myRecAudioFile;
    private File myRecAudioDir;
    private File myPlayFile;
    private MediaRecorder mMediaRecorder01;

    private ArrayList<String> recordFiles;
    private ArrayAdapter<String> adapter;
    private TextView myTextView1;
    private boolean sdCardExit;
    private boolean isStopRecord;

    @Override
    protected void installViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_record);

        myButton1 = (Button) findViewById(R.id.r_button01);
        myButton2 = (Button) findViewById(R.id.r_button02);
        myButton3 = (Button) findViewById(R.id.r_button03);
        myButton4 = (Button) findViewById(R.id.r_button04);
        myListView1 = (ListView) findViewById(R.id.r_listView);
        myTextView1 = (TextView) findViewById(R.id.r_textView);

        record_sure = (Button) findViewById(R.id.record_sure);
        record_cancel = (Button) findViewById(R.id.record_cancel);

        myButton2.setEnabled(false);
        myButton3.setEnabled(false);
        myButton4.setEnabled(false);
        initData();
    }

    @Override
    protected void registerEvents() {
        // TODO Auto-generated method stub
		/* 录音 */
        myButton1.setOnClickListener(this);
		/* 停止 */
        myButton2.setOnClickListener(this);
		/* 播放 */
        myButton3.setOnClickListener(this);
		/* 删除 */
        myButton4.setOnClickListener(this);

        record_sure.setOnClickListener(this);
        record_cancel.setOnClickListener(this);

        myListView1.setOnItemClickListener(this);
    }

    private void initData() {

        strTempFile = Config.FILES_NAME_URL;

		/* 判断SD Card是否插入 */
        sdCardExit = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
		/* 取得SD Card路径做为录音的文件位置 */
        if (sdCardExit)
            myRecAudioDir = new File(Config.FILES_NAME_URL.substring(0,
                    Config.FILES_NAME_URL.length() - 1));

		/* 取得SD Card目录里的所有.amr文件 */
        getRecordFiles();

        adapter = new ArrayAdapter<String>(this,
                R.layout.view_record_list_item, recordFiles);
		/* 将ArrayAdapter存入ListView对象中 */
        myListView1.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        if (mMediaRecorder01 != null && !isStopRecord) {
			/* 停止录音 */
            mMediaRecorder01.stop();
            mMediaRecorder01.release();
            mMediaRecorder01 = null;
        }
        super.onStop();
    }

    private void getRecordFiles() {
        recordFiles = new ArrayList<String>();
        if (sdCardExit) {
            File files[] = myRecAudioDir.listFiles();
            if (files != null) {

                for (int i = 0; i < files.length; i++) {
                    if (files[i].getName().indexOf(".") >= 0) {
						/* 读取.amr文件 */
                        String fileS = files[i].getName().substring(
                                files[i].getName().indexOf("."));
                        if (fileS.toLowerCase().equals(".amr")) {
                            recordFiles.add(files[i].getName());
                        }

                    }
                }
            }
        }
    }

    /* 开启播放录音文件的程序 */
    private void openFile(File f) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);

        String type = getMIMEType(f);
        intent.setDataAndType(Uri.fromFile(f), type);
        startActivity(intent);
    }

    private String getMIMEType(File f) {
        String end = f
                .getName()
                .substring(f.getName().lastIndexOf(".") + 1,
                        f.getName().length()).toLowerCase();
        String type = "";
        if (end.equals("mp3") || end.equals("aac") || end.equals("aac")
                || end.equals("amr") || end.equals("mpeg") || end.equals("mp4")) {
            type = "audio";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg")) {
            type = "image";
        } else {
            type = "*";
        }
        type += "/*";
        return type;
    }

    private String getRandomName() {
        String base = "LILOSOFT";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        sb.append(dateFormat.format(new Date()));
        for (int i = 0; i < 3; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.r_button01:
                try {
                    if (!sdCardExit) {
                        Toast.makeText(AudioRecordActivity.this, "请插入SD Card",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

				/* 建立录音档 */
                    // myRecAudioFile = getFilePath(strTempFile, getRandom()
                    // + ".amr");
                    strTempFile = getRandomName();

                    myRecAudioFile = File.createTempFile(strTempFile, ".amr",
                            myRecAudioDir);

                    mMediaRecorder01 = new MediaRecorder();
				/* 设定录音来源为麦克风 */
                    mMediaRecorder01.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mMediaRecorder01
                            .setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    mMediaRecorder01
                            .setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

                    mMediaRecorder01
                            .setOutputFile(myRecAudioFile.getAbsolutePath());

                    mMediaRecorder01.prepare();

                    mMediaRecorder01.start();

                    myTextView1.setText("录音中…");

                    myButton1.setEnabled(false);
                    myButton2.setEnabled(true);
                    myButton3.setEnabled(false);
                    myButton4.setEnabled(false);

                    isStopRecord = false;

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case R.id.r_button02:
                if (myRecAudioFile != null) {
				/* 停止录音 */
                    mMediaRecorder01.stop();
				/* 将录音文件名给Adapter */
                    adapter.add(myRecAudioFile.getName());
                    mMediaRecorder01.release();
                    mMediaRecorder01 = null;
                    myTextView1.setText("完成：" + myRecAudioFile.getName());
                    myButton1.setEnabled(true);
                    myButton3.setEnabled(true);
                    myButton4.setEnabled(true);

                    myPlayFile = new File(myRecAudioDir.getAbsolutePath()
                            + File.separator + myRecAudioFile.getName());

                    myButton2.setEnabled(false);

                    isStopRecord = true;
                }
                break;
            case R.id.r_button03:
                if (myPlayFile != null && myPlayFile.exists()) {
				/* 开启播放的程序 */
                    openFile(myPlayFile);
                }
                break;
            case R.id.r_button04:
                if (myPlayFile != null) {
				/* 因将Adapter移除文件名 */
                    adapter.remove(myPlayFile.getName());
				/* 删除文件 */
                    if (myPlayFile.exists())
                        myPlayFile.delete();
                    myTextView1.setText("完成删除");
                    myButton3.setEnabled(false);
                    myButton4.setEnabled(false);
                }
                break;
            case R.id.record_sure:
                if (null != myPlayFile && "" != myPlayFile.getPath()) {
                    setResult(Activity.RESULT_OK,
                            new Intent().putExtra("path", myPlayFile.getPath()));
                    finish();
                } else {
                    myTextView1.setText("请选择录音！");
                }
                break;
            case R.id.record_cancel:
                finish();
                break;

            default:
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
		/* 当有点选文件名时将删除及播放按钮Enable */
        myButton3.setEnabled(true);
        myButton4.setEnabled(true);

        myPlayFile = new File(myRecAudioDir.getAbsolutePath() + File.separator
                + ((CheckedTextView) arg1).getText());
        myTextView1.setText("已选：" + ((CheckedTextView) arg1).getText());
    }

}
