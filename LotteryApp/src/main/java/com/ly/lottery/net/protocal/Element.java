package com.ly.lottery.net.protocal;

import org.xmlpull.v1.XmlSerializer;

/**
 * Created by magichill33 on 2015/2/8.
 */
public abstract class Element {
    // 不会将所有的请求用到的叶子放到Element中
    // Element将作为所有请求的代表，Element所有请求的公共部分
    // 公共部分：
    // ①每个请求都需要序列化自己
    public abstract void serializerElement(XmlSerializer serializer);

    /**
     * 每个请求都有自己的标识
     * @return
     */
    public abstract String getTransactionType();
}
