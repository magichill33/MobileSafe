package com.ly.main;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {
    private EditText et_path;
    private MediaPlayer mediaPlayer;
    private Button bt_play,bt_pause,bt_stop,bt_replay;
    private SurfaceView sv;
    private SurfaceHolder holder;

    private int position;
    private String filepath;
    private SeekBar seekBar;
    private Timer timer;
    private TimerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_path = (EditText) findViewById(R.id.et_path);
        bt_pause = (Button) findViewById(R.id.bt_pause);
        bt_play = (Button) findViewById(R.id.bt_play);
        bt_stop = (Button) findViewById(R.id.bt_stop);
        bt_replay = (Button) findViewById(R.id.bt_replay);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        //得到surfaceview
        sv = (SurfaceView) findViewById(R.id.sv);
        //得到显示界面内容的容器
        holder = sv.getHolder();

        //在低版本模拟器上运行加上下面参数，不自己维护双缓冲区，
        //而是等待多媒体播放框架主动的推送数据
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                System.out.println("destoryed");
                if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
                    position = mediaPlayer.getCurrentPosition();
                    timer.cancel();
                    task.cancel();
                    timer = null;
                    task = null;
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                System.out.println("created");
                if(position>0){
                    try{
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(filepath);
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDisplay(holder);
                        mediaPlayer.prepare(); //准备开始播放 播放的逻辑是c代码在线程里执行
                        mediaPlayer.start();
                        mediaPlayer.seekTo(position);
                        bt_play.setEnabled(false);
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                bt_play.setEnabled(true);
                            }
                        });
                    }catch (IOException e){
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,"播放出错",Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                System.out.println("changed");
            }
        });


    }

    /**
     * 播放视频
     * @param view
     */
    public void play(View view){
        filepath = et_path.getText().toString().trim();
        if(filepath.startsWith("http://")){
            mediaPlayer = new MediaPlayer(); //设置播放路径数据源
            try {
                mediaPlayer.setDataSource(filepath);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDisplay(holder);
                mediaPlayer.prepare(); //准备开始播放 播放的逻辑是c代码在线程里执行
               /* mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        final MediaPlayer mp1 = mp;
                        mp.start();
                        seekBar.setMax(mp1.getDuration());
                        timer = new Timer();
                        task = new TimerTask() {
                            @Override
                            public void run() {
                                seekBar.setProgress(mp1.getCurrentPosition());
                            }
                        };
                        timer.schedule(task,0,500);
                    }
                });*/

                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                timer = new Timer();
                task = new TimerTask() {
                    @Override
                    public void run() {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                };
                timer.schedule(task,0,500);
                bt_play.setEnabled(false);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        bt_play.setEnabled(true);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this,"播放出错",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this,"文件不存在，请检查文件的路径",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 暂停
     * @param view
     */
    public void pause(View view){
        if ("继续".equals(bt_pause.getText().toString())){
            mediaPlayer.start();
            bt_pause.setText("暂停");
            return;
        }

        if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            bt_pause.setText("继续");
        }
    }

    /**
     * 停止
     * @param view
     */
    public void stop(View view){
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            timer.cancel();
            task.cancel();
            timer = null;
            task = null;
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        bt_pause.setText("暂停");
        bt_play.setEnabled(true);
    }

    /**
     * 重播
     * @param view
     */
    public void replay(View view){
        if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.seekTo(0);
        }else {
            play(view);
        }
        bt_pause.setText("暂停");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
