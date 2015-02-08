package com.ly.lottery;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.ly.lottery.net.protocal.Message;
import com.ly.lottery.net.protocal.element.CurrentIssueElement;

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

}
