package com.ly.lottery.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ly.lottery.ConstantValue;
import com.ly.lottery.GlobalParams;
import com.ly.lottery.R;
import com.ly.lottery.engine.CommonInfoEngine;
import com.ly.lottery.net.protocal.Element;
import com.ly.lottery.net.protocal.Message;
import com.ly.lottery.net.protocal.Oelement;
import com.ly.lottery.net.protocal.element.CurrentIssueElement;
import com.ly.lottery.util.BeanFactoryUtil;
import com.ly.lottery.util.PromptManager;
import com.ly.lottery.view.manager.BaseUI;
import com.ly.lottery.view.manager.MiddleManager;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by magichill33 on 2015/2/10.
 */
public class Hall extends BaseUI{
    // 第一步：加载layout（布局参数设置）
    // 第二步：初始化layout中控件
    // 第三步：设置监听

    private ListView categoryList; //彩种的入口
    private CategoryAdapter adapter;

    //viewPager配置
    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    private ImageView underLine;
    private List<View> pagers;

    private TextView fcTitle;// 福彩
    private TextView tcTitle;// 体彩
    private TextView gpcTitle;// 高频彩

    //记录viewpager上一个界面的position信息
    private int lastPosition = 0;


    public Hall(Context context) {
        super(context);
    }

    @Override
    protected void setListener() {
        fcTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(0);
            }
        });

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                TranslateAnimation animation = new TranslateAnimation(
                        lastPosition * GlobalParams.WIN_WIDTH / 3, position * GlobalParams.WIN_WIDTH / 3,
                        0, 0);
                animation.setDuration(300);
                animation.setFillAfter(true); //移动完成后停留到终点
                underLine.setAnimation(animation);
                lastPosition = position;
                // 滑动完成后
				/*
				 * switch(position) { case 1:// 当position从0移动到1 TranslateAnimation animation=new TranslateAnimation(0*GlobalParams.WIN_WIDTH/3, 1*GlobalParams.WIN_WIDTH/3, 0, 0);
				 * animation.setDuration(300); animation.setFillAfter(true);// 移动完后停留到终点
				 *
				 * underLine.startAnimation(animation); break; case 2:// 当position从1移动到2 animation=new TranslateAnimation(1*GlobalParams.WIN_WIDTH/3, 2*GlobalParams.WIN_WIDTH/3, 0,
				 * 0); animation.setDuration(300); animation.setFillAfter(true);// 移动完后停留到终点
				 *
				 * underLine.startAnimation(animation); break; }
				 */
                fcTitle.setTextColor(Color.BLACK);
                tcTitle.setTextColor(Color.BLACK);
                gpcTitle.setTextColor(Color.BLACK);

                switch (position) {
                    case 0:
                        fcTitle.setTextColor(Color.RED);
                        break;
                    case 1:
                        tcTitle.setTextColor(Color.RED);
                        break;
                    case 2:
                        gpcTitle.setTextColor(Color.RED);
                        break;
                }


            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void init() {
        showInMiddle = (android.view.ViewGroup) View.inflate(context, R.layout.il_hall, null);

        pager = (ViewPager) findViewById(R.id.ii_viewpager);
        pagerAdapter = new MyPagerAdapter();

        categoryList = new ListView(context);
        adapter = new CategoryAdapter();
        categoryList.setAdapter(adapter);
        categoryList.setFadingEdgeLength(0);  // 删除黑边（上下）
        
        initPager();
        pager.setAdapter(pagerAdapter);
        
        //初始化选项卡的下划线
        initTabStrip();
    }

    private void initTabStrip() {
        underLine = (ImageView) findViewById(R.id.ii_category_selector);

        fcTitle = (TextView) findViewById(R.id.ii_category_fc);
        tcTitle = (TextView) findViewById(R.id.ii_category_tc);
        gpcTitle = (TextView) findViewById(R.id.ii_category_gpc);

        fcTitle.setTextColor(Color.RED);
        // 屏幕宽度
        // GlobalParams.WIN_WIDTH;
        // 小图片的宽度
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.id_category_selector);
        // bitmap.getWidth();
        int offset = (GlobalParams.WIN_WIDTH/3 - bitmap.getWidth())/2;
        //设置图片初始位置--向右偏移
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset,0);
        underLine.setImageMatrix(matrix);

    }

    private void initPager(){
        pagers = new ArrayList<View>();
        pagers.add(categoryList);

        TextView item = new TextView(context);
        item.setText("体彩");
        pagers.add(item);

        item = new TextView(context);
        item.setText("高频彩");
        pagers.add(item);
    }

    @Override
    public void onResume() {
        getCurrentIssueInfo();
        super.onResume();
    }

    @Override
    public int getID() {
        return ConstantValue.VIEW_HALL;
    }

    /**
     * 获取当前销售期信息(双色球)
     */
    private void getCurrentIssueInfo(){
        new MyHttpTask<Integer>(){

            @Override
            protected Message doInBackground(Integer... params) {
                if (params!=null){
                    //获取数据--业务调用
                    CommonInfoEngine engine = BeanFactoryUtil.getImpl(CommonInfoEngine.class);
                    return engine.getCurrentIssueInfo(params[0]);
                }else {
                    return null;
                }

            }

            @Override
            protected void onPostExecute(Message result) {
                if (result!=null){
                    Oelement oelement = result.getBody().getOelement();
                    if (ConstantValue.SUCCESS.equals(oelement.getErrorcode()))
                    {
                        changeNotice(result.getBody().getElements().get(0));
                    }else {
                        PromptManager.showToast(context,oelement.getErrormsg());
                    }
                }else {
                    // 可能：网络不通、权限、服务器出错、非法数据……
                    // 如何提示用户
                    PromptManager.showToast(context, "服务器忙，请稍后重试……");
                }
                super.onPostExecute(result);
            }
        }.executeProxy(ConstantValue.SSQ);
    }

    private Bundle ssqBundle;

    protected void changeNotice(Element element){
        CurrentIssueElement currentIssueElement = (CurrentIssueElement) element;
        String issue = currentIssueElement.getIssue();
        String lasttime = currentIssueElement.getLasttime();
        lasttime = getLasttime(lasttime);
        //第ISSUE期 还有TIME停售
        String text = context.getResources().getString(R.string.is_hall_common_summary);
        text = StringUtils.replaceEach(text,new String[]{ "ISSUE", "TIME" },
                new String[]{issue,lasttime});

        // TODO 更新界面
        // 方式一：
        // adapter.notifyDataSetChanged();// 所有的item更新

        // 方式二：更新需要更新内容（没有必要刷新所有的信息）
        // 获取到需要更新控件的应用
        // TextView view = (TextView) needUpdate.get(0);
        // view.setText(text);

        // 方式三：不想维护needUpdate，如何获取需要更新的控件的引用
        // 将所有的item添加到ListView ，是不是有方式可以获取到ListView的孩子
        // categoryList.findViewById(R.id.ii_hall_lottery_summary);
        // tag The tag to search for, using "tag.equals(getTag())".
        TextView view = (TextView) categoryList.findViewWithTag(0);// tag :唯一
        if (view!=null)
        {
            view.setText(text);
        }

        ssqBundle = new Bundle();
        ssqBundle.putString("issue",issue);

    }

    /**
     * 将秒时间转换成日时分格式
     *
     * @param lasttime
     * @return
     */
    public String getLasttime(String lasttime) {
        StringBuffer result = new StringBuffer();
        if (StringUtils.isNumericSpace(lasttime)) {
            int time = Integer.parseInt(lasttime);
            int day = time / (24 * 60 * 60);
            result.append(day).append("天");
            if (day > 0) {
                time = time - day * 24 * 60 * 60;
            }
            int hour = time / 3600;
            result.append(hour).append("时");
            if (hour > 0) {
                time = time - hour * 60 * 60;
            }
            int minute = time / 60;
            result.append(minute).append("分");
        }
        return result.toString();
    }

    // 资源信息
    private int[] logoResIds = new int[] { R.drawable.id_ssq, R.drawable.id_3d, R.drawable.id_qlc , R.drawable.id_qlc};
    private int[] titleResIds = new int[] { R.string.is_hall_ssq_title, R.string.is_hall_3d_title, R.string.is_hall_qlc_title , R.string.is_hall_qlc_title };

    private class CategoryAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null){
                holder = new ViewHolder();

                convertView = View.inflate(context, R.layout.il_hall_lottery_item, null);

                holder.logo = (ImageView) convertView.findViewById(R.id.ii_hall_lottery_logo);
                holder.title = (TextView) convertView.findViewById(R.id.ii_hall_lottery_title);
                holder.summary = (TextView) convertView.findViewById(R.id.ii_hall_lottery_summary);
                // needUpdate.add(holder.summary);
                holder.bet = (ImageView) convertView.findViewById(R.id.ii_hall_lottery_bet);

                // A tag can be used to mark a view in its hierarchy and does not have to be unique within the hierarchy.
                // Tags can also be used to store data within a view without resorting to another data structure.
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.logo.setImageResource(logoResIds[position]);
            holder.title.setText(titleResIds[position]);
            holder.summary.setTag(position);
            holder.bet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 0){
                       MiddleManager.getInstance().changeUI(PlaySSQ.class,ssqBundle);
                    }
                }
            });

            return convertView;
        }
    }

    class ViewHolder{
        ImageView logo;
        TextView title;
        TextView summary;
        ImageView bet;
    }

    private class MyPagerAdapter extends PagerAdapter{
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = pagers.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return pagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = pagers.get(position);
            container.removeView(view);
        }
    }

}
