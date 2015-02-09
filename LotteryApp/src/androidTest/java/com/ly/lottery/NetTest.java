package com.ly.lottery;

import android.test.InstrumentationTestCase;

import com.ly.lottery.net.NetUtil;

/**
 * Created by Administrator on 2015/2/9.
 */
public class NetTest extends InstrumentationTestCase {
    public void testNetType(){
        NetUtil.checkNet(this.getInstrumentation().getTargetContext());
    }
}
