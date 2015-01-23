package com.ly.mobilesafe.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2015/1/23.
 * 短信工具类
 */
public class SmsUtils {

    public static void backupSms(Context context,BackUpCallBack callBack) throws Exception {
        ContentResolver resolver = context.getContentResolver();
        File file = new File(Environment.getExternalStorageDirectory(),
                "backup.xml");
        FileOutputStream fos = new FileOutputStream(file);
        //把用户的短信一条一条读出来，按照一定的格式写到文件中去
        XmlSerializer serializer = Xml.newSerializer(); //获取xml文件的生成器
        //初始化生成器
        serializer.setOutput(fos,"utf-8");
        serializer.startDocument("utf-8", true);
        serializer.startTag(null, "smss");
        Uri uri = Uri.parse("content://sms/");
        Cursor cursor = resolver.query(uri,new String[]{"body","address","type","date"},
                null,null,null);
        int max = cursor.getCount();
        callBack.beforeBackup(max);
        serializer.attribute(null, "max", max + "");
        int process = 0;
        while (cursor.moveToNext())
        {
            String body = cursor.getString(0);
            String address = cursor.getString(1);
            String type = cursor.getString(2);
            String date = cursor.getString(3);

            serializer.startTag(null,"sms");
            serializer.startTag(null,"body");
            serializer.text(body);
            serializer.endTag(null,"body");
            serializer.startTag(null,"address");
            serializer.text(address);
            serializer.endTag(null,"address");
            serializer.startTag(null,"type");
            serializer.text(type);
            serializer.endTag(null,"type");
            serializer.startTag(null,"date");
            serializer.text(date);
            serializer.endTag(null,"date");
            serializer.endTag(null,"sms");
            process++;
            callBack.onSmsBackup(process);
        }
        cursor.close();
        serializer.endTag(null,"smss");
        serializer.endDocument();
        fos.close();
    }

    /**
     * 还原短信
     *
     * @param context
     * @param flag
     *            是否清理原来的短信
     */
    public static void restoreSms(Context context,boolean flag){
        Uri uri = Uri.parse("content://sms/");
        if(flag)
        {
            context.getContentResolver().delete(uri,null,null);
        }

        // 1.读取sd卡上的xml文件

        // Xml.newPullParser();

        // 2.读取max

        // 3.读取每一条短信信息，body date type address

        // 4.把短信插入到系统短息应用。
    }

    /**
     * 备份短信的回调接口
     */
    public interface BackUpCallBack{

        void beforeBackup(int max);
        void onSmsBackup(int process);
    }
}
