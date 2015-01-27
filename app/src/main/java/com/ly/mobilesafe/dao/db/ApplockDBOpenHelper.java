package com.ly.mobilesafe.dao.db;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/1/27.
 */
public class ApplockDBOpenHelper extends SQLiteOpenHelper{

    public ApplockDBOpenHelper(Context context){
        super(context,"applock.db",null,1);
    }

    public ApplockDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 初始化数据库的表结构
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table applock (_id integer primary key autoincrement," +
                "packname varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
