package com.ly.kumi.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.ly.kumi.ConstantValue;
import com.ly.kumi.util.HandlerManager;
import com.ly.kumi.util.MediaUtil;
import com.ly.kumi.util.PromptManager;

import java.io.IOException;

import static android.media.MediaPlayer.OnCompletionListener;
import static android.media.MediaPlayer.OnErrorListener;
import static android.media.MediaPlayer.OnSeekCompleteListener;

public class MediaService extends Service implements OnCompletionListener,OnSeekCompleteListener,OnErrorListener{
    private static final String TAG = "MediaService";
    //mediaplayer
    private static MediaPlayer player;
    private static ProgressTask task;
    private String file;
    private int postion = 0;

    public MediaService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (player == null){
            player = new MediaPlayer();
            player.setOnSeekCompleteListener(this);
            player.setOnCompletionListener(this);
            player.setOnErrorListener(this);
        }

    }

    @Override
    public void onStart(Intent intent, int startId) {
        int option = intent.getIntExtra("option",-1);
        int progress = intent.getIntExtra("progress",-1);
        if (progress != -1){
            this.postion = progress;
        }

        switch (option){
            case ConstantValue.OPTION_PLAY:
                file = intent.getStringExtra("file");
                play(file);
                MediaUtil.PLAYSTATE = option;
                break;
            case ConstantValue.OPTION_PAUSE:
                postion = player.getCurrentPosition();
                pause();
                MediaUtil.PLAYSTATE = option;
                break;
            case ConstantValue.OPTION_CONTINUE:
                playerToPosiztion(postion);
                if (file==""||file==null){
                    file = intent.getStringExtra("file");
                    play(file);
                } else {
                    player.start();
                }
                MediaUtil.PLAYSTATE = option;
                break;
            case ConstantValue.OPTION_UPDATE_PROGESS:
                playerToPosiztion(postion);
                break;
        }

    }

    private void playerToPosiztion(int postion) {
        if (postion>0 && postion<player.getDuration()){
            player.seekTo(postion);
        }

    }

    private void pause() {
        if (player != null && player.isPlaying()) {
            player.pause();
        }
    }

    private void play(String path) {
        if (player == null){
            player = new MediaPlayer();
        }

        player.reset();
        try {
            player.setDataSource(path);
            player.prepare();
            player.start();

            if (task == null){
                task = new ProgressTask();
                task.execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        HandlerManager.getHandler().sendEmptyMessage(ConstantValue.PLAY_END);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        PromptManager.showToast(getApplicationContext(), "亲，音乐文件加载出错了");
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        if (player.isPlaying()) {
            player.start();
        }
    }

    private class ProgressTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            while (true){
                Log.i(TAG, "isPlaying:" + player.isPlaying());
                SystemClock.sleep(1000);
                publishProgress();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            if (player.isPlaying()){
                Message msg = Message.obtain();
                msg.what = ConstantValue.SEEKBAR_CHANGE;
                msg.arg1 = player.getCurrentPosition() + 1000;
                msg.arg2 = player.getDuration();
                //				HandlerManager.getHandler().sendMessage(msg);
            }

            super.onProgressUpdate(values);
        }
    }
}
