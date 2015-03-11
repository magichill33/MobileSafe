package com.ly.news.utils;

import com.google.gson.Gson;

/**
 * Created by magichill33 on 2015/3/11.
 */
public class GsonUtils {
    public static<T> T jsonToBean(String jsonResult,Class<T> clzz){
        Gson gson = new Gson();
        T t = gson.fromJson(jsonResult,clzz);
        return t;
    }
}
