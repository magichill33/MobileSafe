package com.lilosoft.xtcm.utils;

import org.json.JSONException;

/**
 * @category XML数据处理
 * @author William Liu
 */
public interface XmlParseInterface {

    /**
     * @category 服务器返回指令处理
     * @return
     */
    abstract int xmlParseToOrder(String response) throws JSONException;

}
