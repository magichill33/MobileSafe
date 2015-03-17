package com.lilosoft.xtcm.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lilosoft.xtcm.database.DatabaseHelper;
import com.lilosoft.xtcm.instantiation.Contacts.ContactsInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class CommonNumberDao {

    /**
     * 复制db_contacts.db 从Assets目录复制到sdcard
     *
     * @param context
     */
    public static void copyDB(Context context) {
        try {
            InputStream inputStream = context.getAssets()
                    .open("db_contacts.db");
            File file = new File(Environment.getExternalStorageDirectory(),
                    "db_contacts.db");
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            inputStream.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 判断文件是否存在
     *
     * @return
     */
    public static boolean isExist() {
        File file = new File(Environment.getExternalStorageDirectory(),
                "db_contacts.db");
        return file.exists();
    }

    /**
     * 获取组数据
     *
     * @return
     */
    public static List<Map<String, Object>> getGroupData(Context context) {

        List<Map<String, Object>> groupData = new ArrayList<Map<String, Object>>();
        //File file = new File(Environment.getExternalStorageDirectory(),
        //        "db_contacts.db");
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query("ct", new String[] { "_id", "dept" },
                    null, null, "dept", null, "_id asc");
            while (cursor.moveToNext()) {
                String dept = cursor.getString(1);

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("dept", dept);
                groupData.add(map);
            }
            cursor.close();
            db.close();
        }
        return groupData;
    }

    /**
     * 获取子数据 思路： 1 确定组数据，根据组数据里面的dept来查询子数据
     *
     * @return
     */
    public static List<List<Map<String, Object>>> getChildData(Context context) {
        List<List<Map<String, Object>>> childData = new ArrayList<List<Map<String, Object>>>();

        List<Map<String, Object>> groupData = getGroupData(context);
        for (Map<String, Object> map : groupData) {
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            String dept = (String) map.get("dept");
            // 确定子表
            String table = "ct";
            File file = new File(Environment.getExternalStorageDirectory(),
                    "db_contacts.db");
            SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
            if (db.isOpen()) {
                Cursor cursor = db.query(table, new String[] { "mobilephone",
                                "name", "shortnum" }, "dept=?", new String[] { dept },
                        null, null, null);
                while (cursor.moveToNext()) {
                    String mobilephone = cursor.getString(0);
                    String name = cursor.getString(1);
                    String shortnum = cursor.getString(2);

                    Map<String, Object> hashMap = new HashMap<String, Object>();
                    hashMap.put("phone", "办理号码：" + mobilephone + "  短号："
                            + shortnum);
                    hashMap.put("name", name);
                    hashMap.put("shortnum", shortnum);
                    hashMap.put("mobilephone", mobilephone);
                    list.add(hashMap);
                }
                cursor.close();
                db.close();
            }

            childData.add(list);
        }

        return childData;
    }

    public static boolean writeIntoDataBase(Context context,
                                            List<ContactsInfo> infos) {

        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();

        if (db.isOpen()) {
            // sqlite中没有truncate
            // db.execSQL("truncate table ct");
            db.execSQL("DROP TABLE IF EXISTS ct");
            String sql = "CREATE TABLE ct(_id integer primary key autoincrement,"
                    + "dept varchar(50),name varchar(20),mobilephone varchar(20),shortnum varchar(20))";
            db.execSQL(sql);

            for (ContactsInfo info : infos) {
                ContentValues values = new ContentValues();
                values.put("dept", info.deptName);
                values.put("name", info.linkMan);
                values.put("mobilephone", info.phoneNumber);
                values.put("shortnum", info.shortNumber);
                db.insert("ct", "name", values);
            }
            db.close();
            return true;
        }
        return false;
    }
}
