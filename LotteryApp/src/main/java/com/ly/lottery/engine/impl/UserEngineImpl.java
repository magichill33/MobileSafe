package com.ly.lottery.engine.impl;


import android.util.Xml;

import com.ly.lottery.ConstantValue;
import com.ly.lottery.bean.ShoppingCart;
import com.ly.lottery.bean.Ticket;
import com.ly.lottery.bean.User;
import com.ly.lottery.engine.BaseEngine;
import com.ly.lottery.engine.UserEngine;
import com.ly.lottery.net.HttpClientUtil;
import com.ly.lottery.net.protocal.Message;
import com.ly.lottery.net.protocal.element.BalanceElement;
import com.ly.lottery.net.protocal.element.BetElement;
import com.ly.lottery.net.protocal.element.UserLoginElement;
import com.ly.lottery.util.DES;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

/**
 * Created by Administrator on 2015/2/9.
 */
public class UserEngineImpl extends BaseEngine implements UserEngine{
    //第一步：获取到登录用的xml
    //创建登录有element
    //设置用户数据
    //第二步：发送xml到服务器端，等待回复
    //HttpClientUtil.sendXML
    //判断输入流非空
    //第三步：数据的校验(MD%数据校验)
    //原始数据还原：时间戳(解析) + 密码(常量) + body明文（解析+解密DES）
    //timestamp＋digest+body
    //第四步：请求结果的数据处理
    //body部分的第二次解析，解析的是明文内容

    public Message login1(User user){
        //第一步：获取到登录用的xml
        //创建登录有element
        //设置用户数据
        UserLoginElement element = new UserLoginElement();
        element.getActpassword().setTagValue(user.getPassword());
        Message message = new Message();
        message.getHeader().getUsername().setTagValue(user.getUsername());
        String xml = message.getXml(element);

        //第二步：发送xml到服务器端，等待回复
        //HttpClientUtil.sendXML
        //判断输入流非空
        //在这行代码没有判断网络类型
        HttpClientUtil util = new HttpClientUtil();
        InputStream ips = util.sendXml(ConstantValue.LOTTERY_URI, xml);
        if (ips!=null){
            Message result = new Message();
            //第三步：数据的校验(MD%数据校验)
            XmlPullParser parser = Xml.newPullParser();
            try {
                parser.setInput(ips,ConstantValue.ENCONDING);
                int eventType = parser.getEventType();
                String name;
                while (eventType!=XmlPullParser.END_DOCUMENT){
                    name = parser.getName();
                    switch (eventType){
                        case XmlPullParser.START_TAG:
                            if ("timestamp".equals(name)){
                                result.getHeader().getTimestamp().setTagValue(parser.nextText());
                            }
                            if ("digest".equals(name))
                            {
                                result.getHeader().getDigest().setTagValue(parser.nextText());
                            }
                            if ("body".equals(name))
                            {
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
                    "DECODE",ConstantValue.DES_PASSWORD) + "</body>";
            String orgInfo = result.getHeader().getTimestamp().getTagValue() +
                    ConstantValue.AGENTER_PASSWORD + body;

            //利用工具生成手机端的MD5
            String md5Hex = DigestUtils.md5Hex(orgInfo);
            //将手机端与服务器的进行比对
            if (md5Hex.equals(result.getHeader().getDigest().getTagValue()))
            {
                //第四步：请求结果的数据处理
                //body部分的第二次解析，解析的是明文内容
                parser = Xml.newPullParser();
                try {
                    parser.setInput(new StringReader(body));
                    int eventType = parser.getEventType();
                    String name;
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
                              break;
                      }
                        eventType = parser.next();
                    }
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

        return null;
    }

    @Override
    public Message login(User user) {
        //第一步：获取到登录用的xml
        //创建登录有element
        //设置用户数据
        UserLoginElement element = new UserLoginElement();
        element.getActpassword().setTagValue(user.getPassword());
        Message message = new Message();
        message.getHeader().getUsername().setTagValue(user.getUsername());
        String xml = message.getXml(element);

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

    @Override
    public Message getBalance(User user) {
        BalanceElement element = new BalanceElement();
        Message message = new Message();
        message.getHeader().getUsername().setTagValue(user.getUsername());
        String xml = message.getXml(element);

        Message result = super.getResult(xml);
        if (result!=null){
            // 第四步：请求结果的数据处理
            // body部分的第二次解析，解析的是明文内容
            XmlPullParser parser = Xml.newPullParser();
            DES des = new DES();
            String body = "<body>" + des.authcode(result.getBody().getServiceBodyInsideDESInfo(),
                    "DECODE", ConstantValue.DES_PASSWORD) + "</body>";
            try {
                parser.setInput(new StringReader(body));
                int eventType = parser.getEventType();
                String name;
                BalanceElement resultElement = null;
                while (eventType!=XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if ("errorcode".equals(name)){
                                result.getBody().getOelement().setErrorcode(parser.nextText());
                            }
                            if ("errormsg".equals(name)) {
                                result.getBody().getOelement().setErrormsg(parser.nextText());
                            }
                            //正对于当前请求
                            if ("element".equals(name)){
                                resultElement = new BalanceElement();
                                result.getBody().getElements().add(resultElement);
                            }

                            if ("investvalues".equals(name)){
                                if (resultElement!=null){
                                    resultElement.setInvestvalues(parser.nextText());
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

    @Override
    public Message bet(User user) {
        BetElement element = new BetElement();
        element.getLotteryid().setTagValue(ShoppingCart.getInstance().getLotteryid().toString());

        // 彩票的业务里面：
        // ①关于注数的计算
        // ②关于投注信息封装（用户投注号码）

        // 010203040506|01^01020304050607|01
        ShoppingCart shoppingCart = ShoppingCart.getInstance();
        StringBuffer codeBuffer = new StringBuffer();
        for (Ticket item:shoppingCart.getTickets())
        {
            codeBuffer.append("^").append(item.getRedNum().replaceAll(" ","")).
                    append("|").append(item.getBlueNum().replaceAll(" ",""));
        }
        element.getLotterycode().setTagValue(codeBuffer.substring(1));
        element.getIssue().setTagValue(shoppingCart.getIssue());
        element.getLotteryvalue().setTagValue(shoppingCart.getLotteryvalue()*100+"");
        element.getLotterynumber().setTagValue(shoppingCart.getLotterynumber().toString());
        element.getAppnumbers().setTagValue(shoppingCart.getAppnumbers().toString());
        element.getIssuesnumbers().setTagValue(shoppingCart.getIssuenumbers().toString());
        element.getIssueflag().setTagValue(shoppingCart.getIssuenumbers()>1?"1":"0");

        Message message = new Message();
        message.getHeader().getUsername().setTagValue(user.getUsername());

        String xml = message.getXml(element);
        Message result = super.getResult(xml);
        if (result!=null){
            // 第四步：请求结果的数据处理
            // body部分的第二次解析，解析的是明文内容
            XmlPullParser parser = Xml.newPullParser();
            try {

                DES des = new DES();
                String body = "<body>" + des.authcode(result.getBody().getServiceBodyInsideDESInfo(), "DECODE", ConstantValue.DES_PASSWORD) + "</body>";

                parser.setInput(new StringReader(body));

                int eventType = parser.getEventType();
                String name;

                BetElement resultElement = null;
                while (eventType!=XmlPullParser.END_DOCUMENT)
                {
                    switch (eventType){
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if ("errorcode".equals(name)){
                                result.getBody().getOelement().setErrorcode(parser.nextText());
                            }
                            if ("errormsg".equals(name)) {
                                result.getBody().getOelement().setErrormsg(parser.nextText());
                            }
                            // 正对于当前请求
                            if ("element".equals(name))
                            {
                                resultElement = new BetElement();
                                result.getBody().getElements().add(resultElement);
                            }
                            if ("actvalue".equals(name))
                            {
                                if (resultElement!=null){
                                    resultElement.setActvalue(parser.nextText());
                                }
                            }
                            break;
                    }
                    eventType = parser.next();
                }
                return result;
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return null;
    }
}
