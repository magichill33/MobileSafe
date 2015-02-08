package com.ly.lottery.net.protocal;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

/**
 * Created by magichill33 on 2015/2/8.
 */
public class Leaf {

    // <agenterid>889931</agenterid>
    // 处理的思路
    // ①包含的内容
    // ②序列化xml
    private String tagName;

    private String tagValue;

    public Leaf(String tagName) {
        this.tagName = tagName;
    }

    //处理常量
    public Leaf(String tagName, String tagValue) {
        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    /**
     * 序列化叶子
     * @param serializer
     */
    public void serializerLeaf(XmlSerializer serializer){
        try {
            serializer.startTag(null,tagName);
            if (tagValue == null){
                tagValue = "";
            }
            serializer.text(tagValue);
            serializer.endTag(null,tagName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

