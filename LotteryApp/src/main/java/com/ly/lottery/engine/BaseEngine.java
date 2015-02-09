package com.ly.lottery.engine;

import android.util.Xml;

import com.ly.lottery.ConstantValue;
import com.ly.lottery.net.HttpClientUtil;
import com.ly.lottery.net.protocal.Message;
import com.ly.lottery.util.DES;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

/**
 * Created by Administrator on 2015/2/9.
 */
public class BaseEngine {
    public Message getResult(String xml) {

        //第二步：发送xml到服务器端，等待回复
        //HttpClientUtil.sendXML
        //判断输入流非空
        //在这行代码没有判断网络类型
        HttpClientUtil util = new HttpClientUtil();
        InputStream ips = util.sendXml(ConstantValue.LOTTERY_URI, xml);
        if (ips != null) {
            Message result = new Message();
            //第三步：数据的校验(MD%数据校验)
            XmlPullParser parser = Xml.newPullParser();
            try {
                parser.setInput(ips, ConstantValue.ENCONDING);
                int eventType = parser.getEventType();
                String name;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    name = parser.getName();
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            if ("timestamp".equals(name)) {
                                result.getHeader().getTimestamp().setTagValue(parser.nextText());
                            }
                            if ("digest".equals(name)) {
                                result.getHeader().getDigest().setTagValue(parser.nextText());
                            }
                            if ("body".equals(name)) {
                                result.getBody().setServiceBodyInsideDESInfo(parser.nextText());
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //原始数据还原：时间戳(解析) + 密码(常量) + body明文（解析+解密DES）
            //timestamp＋digest+body
            DES des = new DES();
            String body = "<body>" + des.authcode(result.getBody().getServiceBodyInsideDESInfo(),
                    "DECODE", ConstantValue.DES_PASSWORD) + "</body>";
            String orgInfo = result.getHeader().getTimestamp().getTagValue() +
                    ConstantValue.AGENTER_PASSWORD + body;

            //利用工具生成手机端的MD5
            String md5Hex = DigestUtils.md5Hex(orgInfo);

            if (md5Hex.equals(result.getHeader().getDigest().getTagValue())) {
                return result;
            }

        }
        return null;
    }
}
