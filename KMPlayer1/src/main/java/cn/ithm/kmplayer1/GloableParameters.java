package cn.ithm.kmplayer1;

import cn.ithm.kmplayer1.util.SoftValueMap;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;

public class GloableParameters {
	/**
	 * wap的ip信息
	 */
	public static String PROXY_IP = "";
	/**
	 * wap的端口信息
	 */
	public static int PROXY_PORT = 0;

	public static FragmentActivity MAIN;

	public static SoftValueMap<Object, Bitmap> IMGCACHE = new SoftValueMap<Object, Bitmap>();

}
