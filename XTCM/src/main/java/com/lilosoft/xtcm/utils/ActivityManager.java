package com.lilosoft.xtcm.utils;

import java.util.ArrayList;
import java.util.List;

import com.lilosoft.xtcm.base.AbsBaseActivity;

/**
 * @category 页面管理
 * @author yi Liu
 *
 */
public class ActivityManager {

    private final static String TAG = "ActivityManager";

    private static List<AbsBaseActivity> list = null;

    /**
     * @category 添加Activity
     * @param activity
     */
    public static void addActivity(AbsBaseActivity activity) {

        LogFactory.e(TAG, "addActivity_" + activity.getLocalClassName());
        if (list == null) {
            list = new ArrayList<AbsBaseActivity>();
        }
        list.add(activity);

    }

    /**
     * @category 删除TopActivity
     * @param activity
     */
    public static void removeTopActivity(AbsBaseActivity activity) {

        LogFactory.e(TAG, "removeTopActivity_" + activity.getLocalClassName());
        if (null != list) {
            list.remove(activity);
            activity.finish();
        }

    }

    /**
     * @category 获得长度
     * @return length
     */
    public static int getLength() {
        if (null != list) {
            return list.size();
        }
        return 1;
    }

    /**
     * @category 删除所有Activity
     */
    public static void removeAllActivity() {

        LogFactory.e(TAG, "removeAllActivity");
        if (null != list) {
            for (AbsBaseActivity iterable_element : list) {
                iterable_element.finish();
            }
        }
        removeList();

    }

    /**
     * @category 清除Activity列表
     */
    public static void removeList() {
        list = null;
    }

}
