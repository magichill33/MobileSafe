package com.ly.lottery;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.ly.lottery.bean.User;
import com.ly.lottery.engine.UserEngine;
import com.ly.lottery.engine.impl.UserEngineImpl;
import com.ly.lottery.net.protocal.Message;
import com.ly.lottery.net.protocal.element.CurrentIssueElement;
import com.ly.lottery.util.BeanFactoryUtil;

/**
 * Created by magichill33 on 2015/2/8.
 */
public class TestXML extends InstrumentationTestCase {

    private static final String TAG = "TestXML";

    public void testCreateXML(){
        Message message = new Message();
        CurrentIssueElement element = new CurrentIssueElement();
        element.getLotteryid().setTagValue("118");
        String xml = message.getXml(element);
        Log.i(TAG,xml);
    }

    public void testUserLogin(){
        UserEngineImpl engine = new UserEngineImpl();
        User user = new User();
        user.setUsername("13200000000");
        user.setPassword("0000000");
        Message login = engine.login(user);
        Log.i(TAG,login.getBody().getOelement().getErrorcode());
    }

    public void testUserLoginFactory(){
        UserEngine engine = BeanFactoryUtil.getImpl(UserEngine.class);
        User user = new User();
        user.setUsername("13200000000");
        user.setPassword("0000000");
        Message login = engine.login(user);
        Log.i(TAG,login.getBody().getOelement().getErrorcode() + "::" + login.getBody().getOelement().getErrormsg());
    }


}
