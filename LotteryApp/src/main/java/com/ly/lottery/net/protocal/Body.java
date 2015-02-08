package com.ly.lottery.net.protocal;

import android.util.Xml;

import com.ly.lottery.ConstantValue;
import com.ly.lottery.util.DES;

import org.apache.commons.lang3.StringUtils;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by magichill33 on 2015/2/8.
 */
public class Body {

    private List<Element> elements = new ArrayList<Element>();

    public List<Element> getElements() {
        return elements;
    }

    /****************************************处理服务器回复*******************************************/
    private String serviceBodyInsideDESInfo; //服务器端回复的body中的DES加密的信息
    private Oelement oelement = new Oelement();
    public Oelement getOelement()
    {
        return oelement;
    }

    public String getServiceBodyInsideDESInfo() {
        return serviceBodyInsideDESInfo;
    }

    public void setServiceBodyInsideDESInfo(String serviceBodyInsideDESInfo) {
        this.serviceBodyInsideDESInfo = serviceBodyInsideDESInfo;
    }

    /****************************************处理服务器回复*******************************************/

    /**
     * 序列化请求
     * @param serializer
     */
    public void serializerBody(XmlSerializer serializer){
        /**
         * <body>
         <elements>
         <element>
         <lotteryid>118</lotteryid>
         <issues>1</issues>
         </element>
         </elements>
         </body>
         */
        try {
            serializer.startTag(null,"body");
            serializer.startTag(null,"elements");

            for (Element item:elements){
                item.serializerElement(serializer);
            }

            serializer.endTag(null,"elements");
            serializer.endTag(null,"body");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取到完整的body
     * @return
     */
    public String getWholeBody()
    {
        StringWriter writer = new StringWriter();
        XmlSerializer temp = Xml.newSerializer();

        try {
            temp.setOutput(writer);
            this.serializerBody(temp);
            temp.flush();
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getBodyInsideDESInfo(){
        String wholeBody = getWholeBody();
        String orgDesInfo = StringUtils.substringBetween(wholeBody,"<body>","</body>");

        // 加密
        // 加密调试——2天
        // ①加密算法实现不同
        // ②加密的原始数据不同
        DES des = new DES();
        return des.authcode(orgDesInfo,"ENCODE", ConstantValue.DES_PASSWORD);
    }
}
