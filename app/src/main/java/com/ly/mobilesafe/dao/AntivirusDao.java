package com.ly.mobilesafe.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2015/1/29.
 * 病毒数据库查询业务类
 */
public class AntivirusDao {

    /**
     * 查询一个md5是否在病毒数据库里面存在
     * @param md5
     * @return
     */
    public static boolean isVirus(String md5){
        String path = "data/data/com.ly.mobilesafe/files/antivirus.db";
        boolean result = false;
        SQLiteDatabase db = SQLiteDatabase.
                openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor =db.rawQuery("select * from datable where md5=?",
                new String[]{md5});
        if (cursor.moveToNext()){
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }
}
