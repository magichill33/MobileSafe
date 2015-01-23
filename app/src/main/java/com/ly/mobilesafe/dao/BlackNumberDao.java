package com.ly.mobilesafe.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ly.mobilesafe.dao.db.BlackNumberDBOpenHelper;
import com.ly.mobilesafe.domain.BlackNumberInfo;

public class BlackNumberDao {
    private BlackNumberDBOpenHelper helper;

    public BlackNumberDao(Context context)
    {
        helper = new BlackNumberDBOpenHelper(context);
    }

    /**
     * 查询黑名单号码是是否存在
     * @param number
     * @return
     */
    public boolean find(String number)
    {
        boolean result = false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from blacknumber where number=?",
                new String[]{number});
        if(cursor.moveToNext())
        {
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 查询黑名单号码的拦截模式
     * @param number
     * @return 返回号码的拦截模式，不是黑名单号码返回null
     */
    public String findMode(String number)
    {
        String result = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select mode from blacknumber where number=?",
                new String[]{number});
        if(cursor.moveToNext())
        {
            result = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return result;
    }


    /**
     * 查询全部黑名单号码
     * @return
     */
    public List<BlackNumberInfo> findAll()
    {
        List<BlackNumberInfo> result = new ArrayList<BlackNumberInfo>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select number,mode from blacknumber order by" +
                        " _id desc",
                null);
        while(cursor.moveToNext())
        {
            BlackNumberInfo info = new BlackNumberInfo();
            String number = cursor.getString(0);
            String mode = cursor.getString(1);
            info.setNumber(number);
            info.setMode(mode);
            result.add(info);
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 分批查询数据
     * @param offset
     * @param maxnumber
     * @return
     */
    public List<BlackNumberInfo> findPart(int offset,int maxnumber)
    {
        List<BlackNumberInfo> result = new ArrayList<BlackNumberInfo>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select number,mode from blacknumber order by" +
                        " _id desc limit ? offset ?",
                new String[]{maxnumber+"",offset+""});
        while(cursor.moveToNext())
        {
            BlackNumberInfo info = new BlackNumberInfo();
            String number = cursor.getString(0);
            String mode = cursor.getString(1);
            info.setNumber(number);
            info.setMode(mode);
            result.add(info);
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 添加黑名单号码
     * @param number
     * @param mode
     */
    public void add(String number,String mode)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("mode",mode);
        db.insert("blacknumber", null, values);
        db.close();
    }

    /**
     * 修改黑名单号码的拦截模式
     * @param number 要修改的黑名单号码
     * @param newmode 新的拦截模式
     */
    public void update(String number,String newmode)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put("number", number);
        values.put("mode",newmode);
        db.update("blacknumber", values,"number=?", new String[]{number});
        db.close();
    }

    /**
     * 删除黑名单号码
     * @param number 要删除的黑名单号码
     */
    public void delete(String number)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("blacknumber","number=?", new String[]{number});
        db.close();
    }
}
