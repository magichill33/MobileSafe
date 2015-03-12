package com.ly.news.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ly.news.R;
import com.ly.news.base.BaseFragment;
import com.ly.news.base.BasePage;
import com.ly.news.home.FunctionPage;
import com.ly.news.home.GovAffairsPage;
import com.ly.news.home.NewsCenterPage;
import com.ly.news.home.SettingPage;
import com.ly.news.home.SmartServicePage;
import com.ly.news.view.CustomViewPager;
import com.ly.news.view.LazyViewPager;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * Created by Administrator on 2015/3/11.
 */
public class HomeFragment extends BaseFragment{
    List<BasePage> list = new ArrayList<BasePage>();
    /**
     * 1 初始化viewpager 通过使用adapter的形式去实现
     */
    @ViewInject(R.id.viewpager)
    private CustomViewPager viewPager;
    @ViewInject(R.id.main_radio)
    private RadioGroup main_radio;
    private int checkedId = R.id.rb_function;

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.frag_home2,null);
        //viewPager = (CustomViewPager) view.findViewById(R.id.viewpager);
       // main_radio = (RadioGroup) view.findViewById(R.id.main_radio);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void initData(Bundle saveInstanceState) {
        list.add(new FunctionPage(ctx));
        list.add(new NewsCenterPage(ctx));
        list.add(new SmartServicePage(ctx));
        list.add(new GovAffairsPage(ctx));
        list.add(new SettingPage(ctx));
        HomePageAdapter adapter = new HomePageAdapter(ctx,list);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new LazyViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //如果位置是0的话，才能出现滑动菜单。。如果是其他的tab出现的时候，滑动菜单就给屏蔽掉。
               /* if (0 == position){
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                }else {
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                }*/
                BasePage page = list.get(position);
                page.initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        main_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_function:
                        viewPager.setCurrentItem(0,false); //false取消切换动画
                        checkedId = 0;
                        break;
                    case R.id.rb_news_center:
                        viewPager.setCurrentItem(1,false);
                        checkedId = 1;
                        break;
                    case R.id.rb_smart_service:
                        viewPager.setCurrentItem(2,false);
                        checkedId = 2;
                        break;
                    case R.id.rb_gov_affairs:
                        viewPager.setCurrentItem(3,false);
                        checkedId = 3;
                        break;
                    case R.id.rb_setting:
                        viewPager.setCurrentItem(4,false);
                        checkedId = 4;
                        break;
                }
            }
        });
        main_radio.check(checkedId);
    }

    class HomePageAdapter extends PagerAdapter{

        private Context context;
        private List<BasePage> list;

        HomePageAdapter(Context context, java.util.List<BasePage> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position).getRootView());
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position).getRootView(),0);
            return list.get(position).getRootView();
        }
    }
}
