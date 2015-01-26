package com.ly.mobilesafe;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.ly.mobilesafe.dao.BlackNumberDao;
import com.ly.mobilesafe.domain.TaskInfo;
import com.ly.mobilesafe.engine.TaskInfoProvider;

import java.util.List;
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

    public void testGetSysInfo()
    {
        List<TaskInfo> taskInfos = TaskInfoProvider.getTaskInfos(this.getInstrumentation().getTargetContext());
        for(TaskInfo info:taskInfos)
        {
            Log.i("TestDB",info.toString());
        }
    }
}
