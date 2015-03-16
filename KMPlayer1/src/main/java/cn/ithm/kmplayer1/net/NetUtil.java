package cn.ithm.kmplayer1.net;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import cn.ithm.kmplayer1.GloableParameters;

/**
 * 判断网络类型
 * 
 * @author Administrator
 * 
 */
public class NetUtil {
	/**
	 * 判断网络类型
	 * 
	 * @return
	 */
	public static boolean checkNetType(Context context) {
		// 判断手机的链接渠道
		// WLAN（wi-fi）
		boolean isWIFI = isWIFIConnectivity(context);
		// 手机APN接入点
		boolean isMobile = isMobileConnectivity(context);
		// 当前无可利用的通信渠道
		if (!isWIFI && !isMobile) {
			return false;
		}

		if (isMobile) {
			// WAP还是NET——如果是WAP读取到代理的信息，如果是NET代理信息为NULL
			// 如果为WAP 必须记录代理的ip 和 端口信息
			readAPN(context);
		}

		return true;
	}

	private static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

	/**
	 * 获取当前正在链接的apn的信息
	 */
	private static void readAPN(Context context) {
		// ip和端口
		// 联系人
		ContentResolver resolver = context.getContentResolver();
//		Cursor cursor = resolver.query(PREFERRED_APN_URI, null, null, null, null);
//		if (cursor != null && cursor.moveToFirst()) {
//			GloableParameters.PROXY_IP = cursor.getString(cursor.getColumnIndex("proxy"));
//			GloableParameters.PROXY_PORT = cursor.getInt(cursor.getColumnIndex("port"));
//		}

	}

	/**
	 * 手机APN接入点
	 * 
	 * @param context
	 * @return
	 */
	private static boolean isMobileConnectivity(Context context) {
		// ConnectivityManager---systemService---Context
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (networkInfo != null) {
			return networkInfo.isConnected();
		}
		return false;
	}

	/**
	 * 判断手机的链接渠道 WLAN（wi-fi）
	 * 
	 * @param context
	 * @return
	 */
	private static boolean isWIFIConnectivity(Context context) {
		// ConnectivityManager---systemService---Context
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo != null) {
			return networkInfo.isConnected();
		}
		return false;
	}

	
}
