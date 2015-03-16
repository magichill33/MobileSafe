package com.lilosoft.xtcm.utils;

import org.json.JSONException;

/**
 * @category json数据处理
 * @author William Liu
 */
public interface JsonParseInterface {

	/**
	 * @category 服务器返回指令处理
	 * @return
	 */
	abstract int jsonParseToOrder(String response) throws JSONException;

}
