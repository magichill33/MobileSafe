package com.ly.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class AdActivity extends Activity {

    private static final String TAG = "AdActivity";
    //图片资源ID
    private final int[] imageIds = {
      R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e
    };
    //图片标题集合
    private final String[] imageDesciptions = {
            "巩俐不低俗，我就不能低俗",
            "扑树又回来啦！再唱经典老歌引万人大合唱",
            "揭秘北京电影如何升级",
            "乐视网TV版大派送",
            "热血屌丝的反杀"
    };
    /**
     * 上一个页面的位置
     * @param savedInstanceState
     */
    protected int lastPosition = 0;
    private ViewPager viewPager;
    private LinearLayout pointGroup;
    private TextView imageDesc;
    private boolean isRunning = false;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //让viewPager滑动到下一页
            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            if (isRunning){
                handler.sendEmptyMessageDelayed(0, 2000);
            }

        }
    };
    private ArrayList<ImageView> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pointGroup = (LinearLayout) findViewById(R.id.point_group);
        imageDesc = (TextView) findViewById(R.id.image_desc);
        imageDesc.setText(imageDesciptions[0]);

        imageList = new ArrayList<ImageView>();

        int dip = 20;
        int px = DensityUtil.dip2px(this,dip);
        for (int i=0;i<imageIds.length;i++){
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imageIds[i]);
            imageList.add(imageView);
            //添加指示点
            ImageView point = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            params.rightMargin = px;
            point.setLayoutParams(params); //给控件设置布局
            point.setBackgroundResource(R.drawable.point_bg);
            if (i==0){
                point.setEnabled(true);
            }else {
                point.setEnabled(false);
            }

            pointGroup.addView(point);

        }
        viewPager.setAdapter(new MyPagerAdapter());
        //viewPager.setCurrentItem(Integer.MAX_VALUE/2-Integer.MAX_VALUE/2%imageList.size());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * 页面正在滑动的时候，回调
             * @param position
             * @param positionOffset
             * @param positionOffsetPixels
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**
             * 页面切换后调用
             * @param position 新的页面位置
             */
            @Override
            public void onPageSelected(int position) {
                position = position%imageList.size();
                imageDesc.setText(imageDesciptions[position]);
                pointGroup.getChildAt(position).setEnabled(true);
                pointGroup.getChildAt(lastPosition).setEnabled(false);
                lastPosition = position;
            }

            /**
             * 当页面状态发生变化的时候，回调
             * @param state
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /**
         * 自动循环
         * 1.定时器：Timer
         * 2.开子线程while true循环
         * 3.用handler 发送延时信息，实现循环
         */
        //isRunning = true;
       // handler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    protected void onDestroy() {
        isRunning = false;
        super.onDestroy();
    }

    private class MyPagerAdapter extends PagerAdapter{

        /**
         * 获得页面的总数
         * @return
         */
        @Override
        public int getCount() {
            //return imageList.size();
            return Integer.MAX_VALUE;
        }

        /**
         * 判断view和object的对应关系
         * @param view
         * @param object
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 获得相应位置上的view
         * @param container view的容器，其实就是viewpager自身
         * @param position 相应的位置
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.i(TAG,"instantateItem::"+position);
            position = position%imageList.size();
            //给container添加一个view
            container.addView(imageList.get(position));
            //返回一个和该view相对的object
            return imageList.get(position);
        }

        /**
         * 销毁对应位置上的object
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.i(TAG,"destroyItem::"+position);
            container.removeView((View) object);
            object = null;
        }
    }
}
