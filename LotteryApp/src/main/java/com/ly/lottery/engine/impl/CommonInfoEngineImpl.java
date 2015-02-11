package com.ly.lottery.engine.impl;

import android.util.Xml;

import com.ly.lottery.ConstantValue;
import com.ly.lottery.engine.BaseEngine;
import com.ly.lottery.engine.CommonInfoEngine;
import com.ly.lottery.net.protocal.Message;
import com.ly.lottery.net.protocal.element.CurrentIssueElement;
import com.ly.lottery.util.DES;

import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;

/**
 * Created by Administrator on 2015/2/11.
 */
public class CommonInfoEngineImpl extends BaseEngine implements CommonInfoEngine{
    @Override
    public Message getCurrentIssueInfo(Integer lotteryId) {
        //构建发送的xml
        CurrentIssueElement element = new CurrentIssueElement();
        element.getLotteryid().setTagValue(lotteryId.toString());
        Message message = new Message();
        String xml = message.getXml(element);
        //发送数据
        Message result = getResult(xml);
        if (result!=null){
            //第四步：请求结果的数据处理
            //body部分的第二次解析，解析的是明文内容
            XmlPullParser parser = Xml.newPullParser();
            try {
                DES des = new DES();
                String body = "<body>" + des.authcode(result.getBody().getServiceBodyInsideDESInfo(),
                        "DECODE", ConstantValue.DES_PASSWORD) + "</body>";

                parser.setInput(new StringReader(body));
                int eventType = parser.getEventType();
                String name;

                CurrentIssueElement resultElement = null;
                while (eventType!=XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if ("errorcode".equals(name)){
                                result.getBody().getOelement().setErrorcode(parser.nextText());
                            }

                            if ("errormsg".equals(name)){
                                result.getBody().getOelement().setErrormsg(parser.nextText());
                            }
                            // 判断是否含有element标签，如果有的话创建resultElement
                            if ("element".equals(name))
                            {
                                resultElement = new CurrentIssueElement();
                                result.getBody().getElements().add(resultElement);
                            }
                            //解析特殊的数据
                            if ("issue".equals(name))
                            {
                                if (resultElement!=null){
                                    resultElement.setIssue(parser.nextText());
                                }
                            }

                            if ("lasttime".equals(name))
                            {
                                if (resultElement!=null){
                                    resultElement.setLasttime(parser.nextText());
                                }
                            }
                            break;
                    }
                    eventType = parser.next();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
