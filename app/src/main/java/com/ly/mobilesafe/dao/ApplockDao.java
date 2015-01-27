package com.ly.mobilesafe.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ly.mobilesafe.dao.db.ApplockDBOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/1/27.
 */
public class ApplockDao {
    private ApplockDBOpenHelper helper;
    private Context context;

    public ApplockDao(Context context) {
        this.context = context;
        helper = new ApplockDBOpenHelper(context);
    }

    public void add(String packname){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("packname",packname);
        db.insert("applock",null,values);
        db.close();
        Intent intent = new Intent();
        intent.setAction("com.ly.mobilesafe.applockchange");
        context.sendBroadcast(intent); //发送广播通知数据改变了
    }

    public void delete(String packname){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("applock","packname =?",new String[]{packname});
        db.close();
        Intent intent = new Intent();
        intent.setAction("com.ly.mobilesafe.applockchange");
        context.sendBroadcast(intent); //发送广播通知数据改变了
    }

    /**
     * 查询对应包名的程序是否存在
     * @param packname
     * @return
     */
    public boolean find(String packname){
        boolean result = false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("applock", null, "packname=?", new String[]{packname}, null,
                null, null);
        if(cursor.moveToNext()){
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }

    public List<String> findAll(){
        List<String> packnames = new ArrayList<String>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("applock", new String[]{"packname"}, null, null, null,
                null, null);
        while (cursor.moveToNext()){
            String name = cursor.getString(0);
            packnames.add(name);
        }
        cursor.close();
        db.close();

        return packnames;
    }
}
