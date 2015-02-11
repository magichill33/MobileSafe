package com.ly.lottery.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.ly.lottery.ConstantValue;
import com.ly.lottery.R;
import com.ly.lottery.view.adapter.PoolAdapter;
import com.ly.lottery.view.manager.BaseUI;
import com.ly.lottery.view.manager.PlayGame;
import com.ly.lottery.view.manager.TitleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by magichill33 on 2015/2/11.
 */
public class PlaySSQ extends BaseUI implements PlayGame{
    // 通用三步

    // ①标题
    // 判断购彩大厅是否获取到期次信息
    // 如果获取到：拼装标题
    // 否则默认的标题展示

    // ②填充选号容器
    // ③选号：单击+机选红蓝球
    // 机选红蓝球：一注的要求
    // 红：6+蓝：1

    // ④手机摇晃处理
    // 加速度传感器：
    // 方式一：任意一个轴的加速度值在单位时间内（1秒），变动的速率达到设置好的阈值
    // 方式二：获取三个轴的加速度值，记录，当过一段时间之后再次获取三个轴的加速度值，计算增量，将相邻两个点的增量进行汇总，当达到设置好的阈值

    // ⑤提示信息+清空+选好了

    // 机选
    private Button randomRed;
    private Button randomBlue;

    // 选号容器
    private GridView redContainer;
    private GridView blueContainer;

    private PoolAdapter redAdapter;
    private PoolAdapter blueAdapter;

    private List<Integer> redNums;
    private List<Integer> blueNums;

    public PlaySSQ(Context context) {
        super(context);
    }


    @Override
    protected void setListener() {
        randomRed.setOnClickListener(this);
        randomBlue.setOnClickListener(this);

        redContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!redNums.contains(position+1)){
                    // 如果没有被选中
                    // 背景图片切换到红色
                    view.setBackgroundResource(R.drawable.id_redball);
                    // 摇晃的动画
                    view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.ia_ball_shake));
                    redNums.add(position + 1);
                }else {
                    //被先中
                    //还原图片
                    view.setBackgroundResource(R.drawable.id_defalut_ball);
                    redNums.remove((Object)(position+1));
                }
            }
        });

        blueContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 判断当前点击的item是否被选中了
                if (!blueNums.contains(position + 1)) {
                    // 如果没有被选中
                    // 背景图片切换到红色
                    view.setBackgroundResource(R.drawable.id_blueball);
                    // 摇晃的动画
                    view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.ia_ball_shake));
                    blueNums.add(position + 1);
                } else {
                    // 被选中
                    // 还原背景图片
                    view.setBackgroundResource(R.drawable.id_defalut_ball);
                    blueNums.remove((Object) (position + 1));
                }

            }
        });
    }

    @Override
    protected void init() {
        showInMiddle = (ViewGroup) View.inflate(context, R.layout.il_playssq,null);

        redContainer = (GridView) findViewById(R.id.ii_ssq_red_number_container);
        blueContainer = (GridView) findViewById(R.id.ii_ssq_blue_number_container);
        randomRed = (Button) findViewById(R.id.ii_ssq_random_red);
        randomBlue = (Button) findViewById(R.id.ii_ssq_random_blue);

        redNums = new ArrayList<Integer>();
        blueNums = new ArrayList<Integer>();

        redAdapter = new PoolAdapter(context,33,redNums,R.drawable.id_redball);
        blueAdapter = new PoolAdapter(context, 16, blueNums, R.drawable.id_blueball);

        redContainer.setAdapter(redAdapter);
        blueContainer.setAdapter(blueAdapter);

    }

    @Override
    public int getID() {
        return ConstantValue.VIEW_SSQ;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ii_ssq_random_red:

                break;
            case R.id.ii_ssq_random_blue:

                break;
        }
        super.onClick(v);
    }

    @Override
    public void onResume() {
        super.onResume();
        changeTitle();
    }

    @Override
    public void clear() {

    }

    @Override
    public void done() {

    }

    /**
     * 修改标题
     */
    private void changeTitle(){
        String titleInfo = "";
        // ①标题——界面之间的数据传递(Bundle)
        // 判断购彩大厅是否获取到期次信息
        if (bundle != null) {
            // 如果获取到：拼装标题
            titleInfo = "双色球第" + bundle.getString("issue") + "期";
        } else {
            // 否则默认的标题展示
            titleInfo = "双色球选号";
        }

        TitleManager.getInstance().changeTitle(titleInfo);

    }
}
