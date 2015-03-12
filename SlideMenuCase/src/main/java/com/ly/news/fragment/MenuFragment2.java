package com.ly.news.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ly.news.MainActivity;
import com.ly.news.R;
import com.ly.news.base.BaseFragment;
import com.ly.news.base.QLBaseAdapter;
import com.ly.news.home.NewsCenterPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by magichill33 on 2015/3/11.
 */
public class MenuFragment2 extends BaseFragment{
    public static int newsCenterPosition = 0; //当前选中菜单项
    private MainActivity act;
    private FragmentManager fragManager;
    private NewsCenterPage newsCenterFragment;
    @ViewInject(R.id.tv_menu_classify)
    private TextView classifyTv;
    private int menuType = 0;
    private MenuAdapter newsCenterAdapter = null;
    private ArrayList<String> newsCenterMenu = new ArrayList<String>();
    @ViewInject(R.id.lv_menu_news_center)
    private ListView newsCenterclassifyLv; //新闻菜单的listView
    @ViewInject(R.id.lv_menu_smart_service)
    private ListView smartServiceclassifyLv;
    @ViewInject(R.id.lv_menu_govaffairs)
    private ListView govAffairsclassifyLv;

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.layout_left_menu,null);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void initData(Bundle saveInstanceState) {
        act = (MainActivity) ctx;
        switchMenu(menuType);
    }

    public void initNewsCenterMenu(ArrayList<String> menuList){
        newsCenterMenu.clear();
        newsCenterMenu.addAll(menuList);
        if (newsCenterAdapter == null){
            newsCenterAdapter = new MenuAdapter(ctx,newsCenterMenu);
            newsCenterclassifyLv.setAdapter(newsCenterAdapter);
            newsCenterclassifyLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //当前位置等于点击位置直接切换
                    LogUtils.d("click item");
                    if (position == newsCenterPosition){
                        slidingMenu.toggle();
                        return;
                    }
                    newsCenterPosition = position;
                    LogUtils.d("position::"+newsCenterPosition);
                    newsCenterAdapter.setSelectedPosition(newsCenterPosition);
                }
            });
        }else {
            newsCenterAdapter.notifyDataSetChanged();
        }
        newsCenterAdapter.setSelectedPosition(newsCenterPosition);
    }

    public void switchMenu(int type) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    class MenuAdapter extends QLBaseAdapter<String,ListView>{

        private int selectedPosition = 0; //选中的位置

        protected MenuAdapter(Context context, List<String> list) {
            super(context, list);
        }

        public void setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = View.inflate(ctx,R.layout.layout_item_menu,null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.tv_menu_item);
            ImageView iv = (ImageView) convertView
                    .findViewById(R.id.iv_menu_item);
            tv.setText(list.get(position));
            if (selectedPosition == position){
                convertView.setSelected(true);
                convertView.setPressed(true);
                convertView.setBackgroundResource(R.drawable.menu_item_bg_select);
                tv.setTextColor(ctx.getResources().getColor(
                        R.color.menu_item_text_color));
                iv.setBackgroundResource(R.drawable.menu_arr_select);
            }else {
                convertView.setSelected(false);
                convertView.setPressed(false);
                convertView.setBackgroundColor(Color.TRANSPARENT);
                tv.setTextColor(ctx.getResources().getColor(
                        R.color.white));
                iv.setBackgroundResource(R.drawable.menu_arr_normal);
            }
            return convertView;
        }
    }
}
