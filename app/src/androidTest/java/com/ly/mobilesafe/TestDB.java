package com.ly.mobilesafe;

import android.test.InstrumentationTestCase;

import com.ly.mobilesafe.dao.BlackNumberDao;

import java.util.Random;

/**
 * Created by magichill33 on 2015/1/23.
 */
public class TestDB extends InstrumentationTestCase{
    public void testAdd() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(this.getInstrumentation().getTargetContext());
        long basenumber = 13500000000l;
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            dao.add(String.valueOf(basenumber+i), String.valueOf(random.nextInt(3)+1));
        }
    }
}
