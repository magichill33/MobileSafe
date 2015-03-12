package com.ly.news.base;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2015/3/12.
 */
public abstract class QLBaseAdapter<T,Q> extends BaseAdapter{

    protected Context context;
    protected List<T> list;
    protected Q view;  // 这里不一定是ListView,比如GridView,CustomListView

    protected QLBaseAdapter(Context context, List<T> list, Q view) {
        this.context = context;
        this.list = list;
        this.view = view;
    }

    protected QLBaseAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
