package com.ly.lottery.util;

import java.io.IOException;
import java.util.Properties;

/**
 * 工厂类
 * Created by Administrator on 2015/2/9.
 */
public class BeanFactoryUtil {
    //依据配置文件加载实例
    private static Properties properties;

    static {
        properties = new Properties();
        //bean.properties必须在src的根目录下
        try {
          /*  properties.load(BeanFactoryUtil.class.
                    getClassLoader().getResourceAsStream("bean.properties"));*/
           properties.load(BeanFactoryUtil.class.getResourceAsStream("/assets/bean.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static<T> T getImpl(Class<T> clazz)
    {
        String key = clazz.getSimpleName();
        String className = properties.getProperty(key);
        try {
            return (T)Class.forName(className).newInstance();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
