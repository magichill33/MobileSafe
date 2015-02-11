package com.ly.lottery.net;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.ly.lottery.GlobalParams;

/**
 * Created by Administrator on 2015/2/9.
 */
public class NetUtil {
    /**
     * 检查用户的网络：是否有网络
     * @param context
     * @return
     */
    public static boolean checkNet(Context context){

        boolean isWIFI = isWIFIConnection(context);
        boolean isMobile = isMOBILEConnection(context);

        //如果Mobile连接，判断是哪个APN被选中了
        if (isMobile){
            //apn被选中的代理信息是否有内容，如果有wap方式
            readAPN(context);
        }

        if (!isWIFI && !isMobile)
        {
            return false;
        }
        return true;
    }

    /**
     * APN被选中,的代理信息是否有内容，如果有wap方式
     * @param context
     */
    private static void readAPN(Context context) {
       /* Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
        //4.0模拟器屏蔽掉该权限

        //操作联系人类似
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(PREFERRED_APN_URI,null,
                null,null,null);
        if (cursor!=null&&cursor.moveToFirst()){
            GlobalParams.PROXY = cursor.getString(cursor.getColumnIndex("proxy"));
            GlobalParams.PORT = cursor.getInt(cursor.getColumnIndex("port"));
        }*/
    }

    /**
     * 判断：Mobile连接
     * @param context
     * @return
     */
    private static boolean isMOBILEConnection(Context context)
    {
        NetworkInfo networkInfo = getNetworkInfo(context, ConnectivityManager.TYPE_MOBILE);
        if (networkInfo!=null)
        {
            return networkInfo.isConnected();
        }
        return false;
    }

    /**
     * 判断：wifi连接
     * @param context
     * @return
     */
    private static boolean isWIFIConnection(Context context){

        NetworkInfo networkInfo = getNetworkInfo(context,ConnectivityManager.TYPE_WIFI);
        if (networkInfo!=null)
        {
            return networkInfo.isConnected();
        }
        return false;
    }

    private static NetworkInfo getNetworkInfo(Context context,int type)
    {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        return manager.getNetworkInfo(type);
    }
}
