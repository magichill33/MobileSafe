package com.lilosoft.xtcm.module;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.AvoidXfermode.Mode;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.constant.TableStructure;
import com.lilosoft.xtcm.database.DatabaseFactory;
import com.lilosoft.xtcm.database.DatabaseHelper;
import com.lilosoft.xtcm.instantiation.EventKings;
import com.lilosoft.xtcm.network.HttpConnection;
import com.lilosoft.xtcm.utils.LogFactory;

/**
 * 自动更新事件分类信息
 *
 * @author yzy
 *
 */
public class AutoUpdateEvent {

    private static final String TAG = "AutoUpdateEvent";

    public static String PID = "bigMC";

    public static String CID = "bigID";

    public static List<EventKings> eventList = null;

    private Context context;

    public AutoUpdateEvent(Context context) {
        this.context = context;
    }

    /**
     * 自动更新事件分类信息
     */
    public void updateEvent(String typeVison) {
        HttpConnection httpConnection = new HttpConnection();
        try {
            String xml = httpConnection.getData(
                    HttpConnection.CONNECTION_EVENT_INFO, typeVison);
            if (!TextUtils.isEmpty(xml)) {
                Log.i("AutoUpdateEvent", xml);
                readStringXML(xml);
            } else {
                Log.i("AutoUpdateEvent", "xml为空");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析服务器响应的xml字符串
     *
     * @param xmlStr
     * @return
     */
    public void readStringXML(String xml) {
        List<EventKings> eventList = new ArrayList<EventKings>();
        EventKings ek = null;
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        XmlPullParser parser = Xml.newPullParser();
        String oldTypeVison = "";
        try {
            parser.setInput(new StringReader(xml));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("TypeVison".equals(parser.getName())) {
                            SharedPreferences sp = context.getSharedPreferences(
                                    Config.SHARED_PREFERENCES_NAME,
                                    context.MODE_PRIVATE);
                            oldTypeVison = sp.getString(Config.TypeVison, "4");
                            sp.edit()
                                    .putString(Config.TypeVison, parser.nextText())
                                    .commit();
                            LogFactory.e("JSON", parser.nextText());
                            // continue;
                        }
                        if ("MessageContent".equals(parser.getName())) {
                            ek = new EventKings();
                        } else if (ek != null) {
                            if ("EventCode".equals(parser.getName())) {
                                ek.setId(parser.nextText());
                            }
                            if ("EventParentID".equals(parser.getName())) {
                                ek.setParentid(parser.nextText());
                            }
                            if ("EventName".equals(parser.getName())) {
                                ek.setMc(parser.nextText());
                            }
                            if ("EXTEND1".equals(parser.getName())) {
                                ek.setCode(parser.nextText());
                            }
                            if ("EXTEND4".equals(parser.getName())) {
                                String layer = parser.nextText();
                                ek.setLayer(layer);
                                // if(!TextUtils.isEmpty(layer)){
                                // LogFactory.e(TAG, "layer = "+layer);
                                // }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("MessageContent".equals(parser.getName()) && null != ek) {
                            eventList.add(ek);
                            LogFactory.e("Auto", ek.toString());
                        }
                        break;
                }
                event = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * 将当前数据存到本地数据库
         */
        if (!context
                .getSharedPreferences(Config.SHARED_PREFERENCES_NAME,
                        context.MODE_PRIVATE).getString(Config.TypeVison, "4")
                .equals(oldTypeVison)) {
            try {
                db.execSQL("delete from EVENT_SPINNER where TYPE=" + "'"
                        + TableStructure.V_ACT_ADMINI_EVENT_TYPE + "'");
                db.beginTransaction();
                for (int i = 0; i < eventList.size(); i++) {
                    EventKings eking = eventList.get(i);
                    String sql = "INSERT INTO EVENT_SPINNER(EID,FATHERID,ITEMTYPENAME,CODE,LAYER,TYPE) VALUES (?,?,?,?,?,?)";
                    db.execSQL(
                            sql,
                            new Object[] { eking.getId(), eking.getParentid(),
                                    eking.getMc(), eking.getCode(),
                                    eking.getLayer(),
                                    TableStructure.V_ACT_ADMINI_EVENT_TYPE });
                }
                db.setTransactionSuccessful();
                db.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
                helper.close();
            }
        }
    }

    /**
     * 从本地数据库获取事件分类信息
     *
     * @param bigID
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    public String[] getBigEvent(String bigID) {
        DatabaseFactory data = new DatabaseFactory(context);
        eventList = data
                .getEventSpinnerList(TableStructure.V_ACT_ADMINI_EVENT_TYPE);// 获取自动更新事件分类信息
        List<String> bigList = new ArrayList<String>();
        for (int i = 0; i < eventList.size(); i++) {
            EventKings ek = eventList.get(i);
            if ("429004".equals(ek.getParentid())) {
                if (bigID == PID) {
                    bigList.add(ek.getMc());
                } else if (bigID == CID) {
                    bigList.add(ek.getId());
                }
            }
        }

        String[] bigArray = new String[bigList.size()];
        for (int i = 0; i < bigList.size(); i++) {
            bigArray[i] = bigList.get(i);
        }
        return bigArray;
    }
}
