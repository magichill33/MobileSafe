package com.ly.mobilesafe.service;

import java.lang.reflect.Method;
import java.net.URI;

import com.android.internal.telephony.ITelephony;
import com.ly.mobilesafe.dao.BlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources.Theme;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallSmsSafeService extends Service {

    public static final String TAG = "CallSmsSafeActivity";
    private InnerSmsReceiver receiver;
    private BlackNumberDao dao;
    private TelephonyManager tm;
    private MyListener listener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "服务启动");
        dao = new BlackNumberDao(this);
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        receiver = new InnerSmsReceiver();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        registerReceiver(receiver, filter);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "服务销毁");
        unregisterReceiver(receiver);
        receiver = null;
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        super.onDestroy();
    }

    public void endCall() {
        //加载servicemanager的字节码
        try {
            Class clazz = CallSmsSafeService.this.getClassLoader().
                    loadClass("android.os.ServiceManager");
            Method method = clazz.getDeclaredMethod("getService",
                    String.class);
            IBinder ibinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
            ITelephony.Stub.asInterface(ibinder).endCall();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void deleteCallLog(String incomingNumber)
    {
        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://call_log/calls");
        resolver.delete(uri, "number=?", new String[]{incomingNumber});
    }

    private class InnerSmsReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for(Object obj:objs)
            {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String sender = smsMessage.getOriginatingAddress();
                String body = smsMessage.getMessageBody();
                String mode = dao.findMode(sender);
                if("2".equals(mode)||"3".equals(mode))
                {
                    Log.i(TAG,"拦截短信");
                    abortBroadcast();
                }
            }
        }

    }

    private class MyListener extends PhoneStateListener
    {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    String result = dao.findMode(incomingNumber);
                    if("1".equals(result)||"3".equals(result))
                    {
                        Log.i(TAG, "挂断电话……");
                        //观察呼叫记录数据库内容的变化
                        Uri uri = Uri.parse("content://call_log/calls");
                        getContentResolver().registerContentObserver(uri,
                                true,new CallLogObserver(incomingNumber,new Handler()));
                        endCall();
                    }
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    private class CallLogObserver extends ContentObserver
    {
        private String incomingNumber;

        public CallLogObserver(String incomingNumber,Handler handler)
        {
            super(handler);
            this.incomingNumber = incomingNumber;
        }

        @Override
        public void onChange(boolean selfChange) {
            Log.i(TAG,"数据库的内容变化了，产生了呼叫记录");
            getContentResolver().unregisterContentObserver(this);
            deleteCallLog(incomingNumber);
            super.onChange(selfChange);
        }
    }

}
