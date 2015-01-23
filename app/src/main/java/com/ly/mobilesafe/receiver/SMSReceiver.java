package com.ly.mobilesafe.receiver;

import com.ly.mobilesafe.R;
import com.ly.mobilesafe.service.GPSService;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG	="SMSReceiver";
    private SharedPreferences sp;

    @Override
    public void onReceive(Context context, Intent intent) {
        //写接收短信的代码
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        for(Object obj:objs)
        {
            //具体某一条短信
            SmsMessage sms = SmsMessage.createFromPdu((byte[])obj);
            //发送者
            String sender = sms.getOriginatingAddress();
            String safenumber = sp.getString("safenumber", "");
            String body = sms.getMessageBody();
            Log.i(TAG, body);
            if(sender.contains(safenumber))
            {
                if("#*location*#".equals(body))
                {
                    Intent i = new Intent(context,GPSService.class);
                    context.startService(i);
                    String lastLocation = sp.getString("lastlocation", null);
                    if(TextUtils.isEmpty(lastLocation))
                    {
                        SmsManager.getDefault().sendTextMessage(sender,
                                null,"getting location.....", null, null);
                    }else{
                        SmsManager.getDefault().sendTextMessage(sender,
                                null,lastLocation, null, null);
                    }
                    //abortBroadcast();
                }else if("#*alarm*#".equals(body))
                {
                    MediaPlayer player = MediaPlayer.create(context,R.raw.ylzs);
                    player.setLooping(false);
                    player.setVolume(1.0f, 1.0f);
                    player.start();


                }else if("#*wipedata*#".equals(body))
                {

                }else if("#*lockscreen*#".equals(body))
                {
//					Intent dintent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//					ComponentName cn = new ComponentName(context,DeviceAdminSample.class);
//					dintent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
//					//劝说用户开启管理员
//					intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
//							"开启送积分");
                    DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
                    dpm.lockNow();
                    dpm.resetPassword("123", 0);

                }

                abortBroadcast();
            }
        }

    }

}
