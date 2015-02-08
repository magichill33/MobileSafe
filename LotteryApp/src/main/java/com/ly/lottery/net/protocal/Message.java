package com.ly.lottery.net.protocal;

import android.util.Xml;

import com.ly.lottery.ConstantValue;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by magichill33 on 2015/2/8.
 */
public class Message {
    private Header header = new Header();
    private Body body = new Body();

    public Header getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }

    /**
     * 序列化协议
     */
    public void serializerMessage(XmlSerializer serializer)
    {
        try {
            serializer.startTag(null,"message");
            serializer.attribute(null,"version","1.0");

            header.serializerHeader(serializer,body.getWholeBody());
            //对body信息进行加密
            serializer.startTag(null,"body");
            serializer.text(body.getBodyInsideDESInfo());
            serializer.endTag(null,"body");

            serializer.endTag(null,"message");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取请求的xml文件
     * @param element
     * @return
     */
    public String getXml(Element element)
    {
        if (element == null){
            throw new IllegalArgumentException("element is null");
        }

        //请求标识需要设置，请求内容需要设置
        header.getTransactiontype().setTagValue(element.getTransactionType());
        body.getElements().add(element);

        //序列化
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument(ConstantValue.ENCONDING,null);
            this.serializerMessage(serializer);
            serializer.endDocument();
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
