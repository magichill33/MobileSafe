package com.ly.news.bean;

import java.util.List;

/**
 * Created by magichill33 on 2015/3/11.
 */
public class NewsCenterCategory {
    public List<CenterCategory> data;
    public List<Integer> extend;
    public int retcode;

    public static class CenterCategory{
        public List children;
        public int id;
        public String title;
        public int type;
        public String url;
        public String url1;
        public String dataurl;
        public String excurl;
        public String weekurl;

    }

    public static class CenterCategoryItem{
        public int id;
        public String title;
        public int type;
        public String url;
    }
}
