package com.lilosoft.xtcm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.constant.TableStructure;

/**
 * @category 数据库服务
 * @author William Liu
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * @category 数据库操作构造方法
     * @param context
     */
    public DatabaseHelper(Context context) {
        // TODO Auto-generated constructor stub
        super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
    }

    /**
     * @category 创建数据库
     * @author William Liu
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        // TODO Auto-generated method stub
        String sql = "CREATE TABLE " + TableStructure.TABLE_NAME_QUESTION
                + " (" + TableStructure.Q_QUESTION_ID
                + " integer primary key autoincrement, "
                + TableStructure.Q_QUESTION_TYPE + " varchar(20), "
                + TableStructure.Q_QUESTION_TYPE1 + " varchar(20), "
                + TableStructure.Q_QUESTION_TYPE2 + " varchar(20), "
                + TableStructure.Q_QUESTION_LOCATION + " varchar(50), "
                + TableStructure.Q_QUESTION_DESCRIPT + " text, "
                + TableStructure.Q_QUESTION_BEFOR_IMG1 + " text, "
                + TableStructure.Q_QUESTION_BEFOR_IMG2 + " text, "
                + TableStructure.Q_QUESTION_BEFOR_IMG3 + " text, "
                + TableStructure.Q_QUESTION_AFTER_IMG1 + " text, "
                + TableStructure.Q_QUESTION_AFTER_IMG2 + " text, "
                + TableStructure.Q_QUESTION_AFTER_IMG3 + " text, "
                + TableStructure.Q_QUESTION_REC1 + " text, "
                + TableStructure.Q_QUESTION_REC2 + " text, "
                + TableStructure.Q_QUESTION_REC3 + " text,"
                + TableStructure.Q_QUESTION_CASESNUM + " varchar(50))";
        database.execSQL(sql);

        // 事件分类信息
        String event_sql = "CREATE TABLE EVENT_SPINNER(ID integer primary key autoincrement,"
                + "EID varchar(100),FATHERID varchar(100),ITEMTYPENAME varchar(100),CODE varchar(100),LAYER varchar(100),TYPE varchar(50))";
        database.execSQL(event_sql);

        //联系人信息
        String sql_contacts = "CREATE TABLE ct(_id integer primary key autoincrement," +
                "dept varchar(50),name varchar(20),mobilephone varchar(20),shortnum varchar(20))";
        database.execSQL(sql_contacts);
    }

    /**
     * @category 数据库修改
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        // TODO Auto-generated method stub
        database.execSQL("DROP TABLE IF EXISTS "
                + TableStructure.TABLE_NAME_QUESTION);
        database.execSQL("DROP TABLE IF EXISTS EVENT_SPINNER ");
        database.execSQL("DROP TABLE IF EXISTS ct");
        onCreate(database);
    }

}
