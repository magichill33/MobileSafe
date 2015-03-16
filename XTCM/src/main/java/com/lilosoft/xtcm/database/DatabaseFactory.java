package com.lilosoft.xtcm.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lilosoft.xtcm.constant.TableStructure;
import com.lilosoft.xtcm.instantiation.EventKings;
import com.lilosoft.xtcm.instantiation.ReadyReportBean;
import com.lilosoft.xtcm.utils.LogFactory;

/**
 * @category 数据库操作类
 * @author William Liu
 * 
 */
public class DatabaseFactory {

	/**
	 * @category 数据服务
	 */
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase database;

	/**
	 * @category 构造方法
	 * @param context
	 */
	public DatabaseFactory(Context context) {
		databaseHelper = new DatabaseHelper(context);
		database = databaseHelper.getWritableDatabase();

	}

	/**
	 * 
	 * @category 数据插入
	 * @param bean
	 * 
	 */
	public void insert(ReadyReportBean bean) {
		String sql = "INSERT INTO " + TableStructure.TABLE_NAME_QUESTION + "("
				+ TableStructure.Q_QUESTION_TYPE + ","
				+ TableStructure.Q_QUESTION_TYPE1 + ","
				+ TableStructure.Q_QUESTION_TYPE2 + ","
				+ TableStructure.Q_QUESTION_LOCATION + ","
				+ TableStructure.Q_QUESTION_DESCRIPT + ","
				+ TableStructure.Q_QUESTION_BEFOR_IMG1 + ","
				+ TableStructure.Q_QUESTION_BEFOR_IMG2 + ","
				+ TableStructure.Q_QUESTION_BEFOR_IMG3 + ","
				+ TableStructure.Q_QUESTION_AFTER_IMG1 + ","
				+ TableStructure.Q_QUESTION_AFTER_IMG2 + ","
				+ TableStructure.Q_QUESTION_AFTER_IMG3 + ","
				+ TableStructure.Q_QUESTION_REC1 + ","
				+ TableStructure.Q_QUESTION_REC2 + ","
				+ TableStructure.Q_QUESTION_REC3 + ","
				+ TableStructure.Q_QUESTION_CASESNUM + ") VALUES ('"
				+ bean.getQ_QUESTION_TYPE() + "','"
				+ bean.getQ_QUESTION_TYPE1() + "','"
				+ bean.getQ_QUESTION_TYPE2() + "','"
				+ bean.getQ_QUESTION_LOCATION() + "','"
				+ bean.getQ_QUESTION_DESCRIPT() + "','"
				+ bean.getQ_QUESTION_BEFOR_IMG1() + "','"
				+ bean.getQ_QUESTION_BEFOR_IMG2() + "','"
				+ bean.getQ_QUESTION_BEFOR_IMG3() + "','"
				+ bean.getQ_QUESTION_AFTER_IMG1() + "','"
				+ bean.getQ_QUESTION_AFTER_IMG2() + "','"
				+ bean.getQ_QUESTION_AFTER_IMG3() + "','"
				+ bean.getQ_QUESTION_REC1() + "','" + bean.getQ_QUESTION_REC2()
				+ "','" + bean.getQ_QUESTION_REC3() + "','"
				+ bean.getQ_QUESTION_CASESNUM() + "')";
		LogFactory.e("DB", sql);
		database.execSQL(sql);
		close();

	}

	/**
	 * @category 数据修改
	 * @param bean
	 */
	public void update(ReadyReportBean bean) {
		String sql = "UPDATE " + TableStructure.TABLE_NAME_QUESTION + " SET "
				+ TableStructure.Q_QUESTION_TYPE + " = '"
				+ bean.getQ_QUESTION_TYPE() + "',"
				+ TableStructure.Q_QUESTION_TYPE1 + " = '"
				+ bean.getQ_QUESTION_TYPE1() + "',"
				+ TableStructure.Q_QUESTION_TYPE2 + " = '"
				+ bean.getQ_QUESTION_TYPE2() + "',"
				+ TableStructure.Q_QUESTION_LOCATION + " = '"
				+ bean.getQ_QUESTION_LOCATION() + "',"
				+ TableStructure.Q_QUESTION_DESCRIPT + " = '"
				+ bean.getQ_QUESTION_DESCRIPT() + "',"
				+ TableStructure.Q_QUESTION_BEFOR_IMG1 + " = '"
				+ bean.getQ_QUESTION_BEFOR_IMG1() + "',"
				+ TableStructure.Q_QUESTION_BEFOR_IMG2 + " = '"
				+ bean.getQ_QUESTION_BEFOR_IMG2() + "',"
				+ TableStructure.Q_QUESTION_BEFOR_IMG3 + " = '"
				+ bean.getQ_QUESTION_BEFOR_IMG3() + "',"
				+ TableStructure.Q_QUESTION_AFTER_IMG1 + " = '"
				+ bean.getQ_QUESTION_AFTER_IMG1() + "',"
				+ TableStructure.Q_QUESTION_AFTER_IMG2 + " = '"
				+ bean.getQ_QUESTION_AFTER_IMG2() + "',"
				+ TableStructure.Q_QUESTION_AFTER_IMG3 + " = '"
				+ bean.getQ_QUESTION_AFTER_IMG3() + "',"
				+ TableStructure.Q_QUESTION_REC1 + " = '"
				+ bean.getQ_QUESTION_REC1() + "',"
				+ TableStructure.Q_QUESTION_REC2 + " = '"
				+ bean.getQ_QUESTION_REC2() + "',"
				+ TableStructure.Q_QUESTION_REC3 + " = '"
				+ bean.getQ_QUESTION_REC3() + "',"
				+ TableStructure.Q_QUESTION_CASESNUM + " = '"
				+ bean.getQ_QUESTION_CASESNUM() + "'  WHERE "
				+ TableStructure.Q_QUESTION_ID + " = "
				+ bean.getQ_QUESTION_ID();

		;
		LogFactory.e("DB", sql);
		database.execSQL(sql);
		close();
	}

	/**
	 * @category 数据删除
	 * @param questionId
	 */
	public void delete(String questionId) {
		String sql = "DELETE FROM " + TableStructure.TABLE_NAME_QUESTION
				+ " WHERE " + TableStructure.Q_QUESTION_ID + " = " + questionId;
		LogFactory.e("DB", sql);
		database.execSQL(sql);
		close();
	}

	public boolean check(ReadyReportBean questionBean) {
		String sql = "select * from " + TableStructure.TABLE_NAME_QUESTION
				+ " where " + TableStructure.Q_QUESTION_TYPE + " = '"
				+ questionBean.getQ_QUESTION_TYPE() + "' and "
				+ TableStructure.Q_QUESTION_TYPE1 + " = '"
				+ questionBean.getQ_QUESTION_TYPE1() + "' and "
				+ TableStructure.Q_QUESTION_TYPE2 + " = '"
				+ questionBean.getQ_QUESTION_TYPE2() + "' and "
				+ TableStructure.Q_QUESTION_LOCATION + " = '"
				+ questionBean.getQ_QUESTION_LOCATION() + "' and "
				+ TableStructure.Q_QUESTION_DESCRIPT + " = '"
				+ questionBean.getQ_QUESTION_DESCRIPT() + "' and "
				+ TableStructure.Q_QUESTION_BEFOR_IMG1 + " = '"
				+ questionBean.getQ_QUESTION_BEFOR_IMG1() + "' and "
				+ TableStructure.Q_QUESTION_BEFOR_IMG2 + " = '"
				+ questionBean.getQ_QUESTION_BEFOR_IMG2() + "' and "
				+ TableStructure.Q_QUESTION_BEFOR_IMG3 + " = '"
				+ questionBean.getQ_QUESTION_BEFOR_IMG3() + "' and "
				+ TableStructure.Q_QUESTION_AFTER_IMG1 + " = '"
				+ questionBean.getQ_QUESTION_AFTER_IMG1() + "' and "
				+ TableStructure.Q_QUESTION_AFTER_IMG2 + " = '"
				+ questionBean.getQ_QUESTION_AFTER_IMG2() + "' and "
				+ TableStructure.Q_QUESTION_AFTER_IMG3 + " = '"
				+ questionBean.getQ_QUESTION_AFTER_IMG3() + "' and "
				+ TableStructure.Q_QUESTION_REC1 + " = '"
				+ questionBean.getQ_QUESTION_REC1() + "' and "
				+ TableStructure.Q_QUESTION_REC2 + " = '"
				+ questionBean.getQ_QUESTION_REC2() + "' and "
				+ TableStructure.Q_QUESTION_REC3 + " = '"
				+ questionBean.getQ_QUESTION_REC3() + "' and "
				+ TableStructure.Q_QUESTION_CASESNUM + " = '"
				+ questionBean.getQ_QUESTION_CASESNUM() + "'";
		Cursor cursor = database.rawQuery(sql, null);
		if (1 == cursor.getCount()) {
			return true;
		}
		return false;
	}

	/**
	 * 数据查找
	 * 
	 * @param questionId
	 * @return
	 */
	public List<ReadyReportBean> check(String... questionId) {
		List<ReadyReportBean> beans = new ArrayList<ReadyReportBean>();
		ReadyReportBean bean = null;
		String sql = "";
		if (questionId.length > 0) {
			sql = "SELECT * FROM " + TableStructure.TABLE_NAME_QUESTION
					+ " WHERE " + TableStructure.Q_QUESTION_ID + " = '"
					+ questionId[0] + "'";
		} else {
			sql = "SELECT * FROM " + TableStructure.TABLE_NAME_QUESTION;
		}
		LogFactory.e("DB", sql);

		Cursor cursor = database.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			bean = new ReadyReportBean();
			bean.setQ_QUESTION_ID(cursor.getString(cursor
					.getColumnIndex(TableStructure.Q_QUESTION_ID)));
			bean.setQ_QUESTION_TYPE(cursor.getString(cursor
					.getColumnIndex(TableStructure.Q_QUESTION_TYPE)));
			bean.setQ_QUESTION_TYPE1(cursor.getString(cursor
					.getColumnIndex(TableStructure.Q_QUESTION_TYPE1)));
			bean.setQ_QUESTION_TYPE2(cursor.getString(cursor
					.getColumnIndex(TableStructure.Q_QUESTION_TYPE2)));
			bean.setQ_QUESTION_LOCATION(cursor.getString(cursor
					.getColumnIndex(TableStructure.Q_QUESTION_LOCATION)));
			bean.setQ_QUESTION_DESCRIPT(cursor.getString(cursor
					.getColumnIndex(TableStructure.Q_QUESTION_DESCRIPT)));
			bean.setQ_QUESTION_BEFOR_IMG1(cursor.getString(cursor
					.getColumnIndex(TableStructure.Q_QUESTION_BEFOR_IMG1)));
			bean.setQ_QUESTION_BEFOR_IMG2(cursor.getString(cursor
					.getColumnIndex(TableStructure.Q_QUESTION_BEFOR_IMG2)));
			bean.setQ_QUESTION_BEFOR_IMG3(cursor.getString(cursor
					.getColumnIndex(TableStructure.Q_QUESTION_BEFOR_IMG3)));
			bean.setQ_QUESTION_AFTER_IMG1(cursor.getString(cursor
					.getColumnIndex(TableStructure.Q_QUESTION_AFTER_IMG1)));
			bean.setQ_QUESTION_AFTER_IMG2(cursor.getString(cursor
					.getColumnIndex(TableStructure.Q_QUESTION_AFTER_IMG2)));
			bean.setQ_QUESTION_AFTER_IMG3(cursor.getString(cursor
					.getColumnIndex(TableStructure.Q_QUESTION_AFTER_IMG3)));
			bean.setQ_QUESTION_REC1(cursor.getString(cursor
					.getColumnIndex(TableStructure.Q_QUESTION_REC1)));
			bean.setQ_QUESTION_REC2(cursor.getString(cursor
					.getColumnIndex(TableStructure.Q_QUESTION_REC2)));
			bean.setQ_QUESTION_REC3(cursor.getString(cursor
					.getColumnIndex(TableStructure.Q_QUESTION_REC3)));
			bean.setQ_QUESTION_CASESNUM(cursor.getString(cursor
					.getColumnIndex(TableStructure.Q_QUESTION_CASESNUM)));
			beans.add(bean);

		}

		return beans;
	}

	/**
	 * @category 关闭数据库
	 */
	public void close() {

		if (null != databaseHelper)
			if (null != database)
				if (database.isOpen())
					database.close();
		databaseHelper.close();

	}

	public List<EventKings> getEventSpinnerList(String type) {
		List<EventKings> lst = new ArrayList<EventKings>();
		// DatabaseHelper helper = new DatabaseHelper(context);
		// SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "select EID,FATHERID,ITEMTYPENAME,CODE,LAYER from EVENT_SPINNER where TYPE=? order by ID";
		Cursor cursor = database.rawQuery(sql, new String[] { type });
		while (cursor.moveToNext()) {
			EventKings event = new EventKings();
			event.setId(cursor.getString(cursor.getColumnIndex("EID")));
			event.setParentid(cursor.getString(cursor
					.getColumnIndex("FATHERID")));
			event.setMc(cursor.getString(cursor.getColumnIndex("ITEMTYPENAME")));
			event.setCode(cursor.getString(cursor.getColumnIndex("CODE")));
			event.setLayer(cursor.getString(cursor.getColumnIndex("LAYER")));
			lst.add(event);
		}
		// cursor.close();
		this.close();

		return lst;
	}
	
	public String getCodeByName(String name){
		String code = null;
		String sql = "select CODE from EVENT_SPINNER where ITEMTYPENAME=? order by ID";
		Cursor cursor = database.rawQuery(sql, new String[] { name });
		while (cursor.moveToNext()) {
			code = cursor.getString(cursor.getColumnIndex("CODE"));
		}
		this.close();
		return code;
	}

}
