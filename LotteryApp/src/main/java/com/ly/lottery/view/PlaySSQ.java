package com.ly.lottery.view;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.ly.lottery.ConstantValue;
import com.ly.lottery.R;
import com.ly.lottery.bean.ShoppingCart;
import com.ly.lottery.bean.Ticket;
import com.ly.lottery.engine.CommonInfoEngine;
import com.ly.lottery.net.protocal.Message;
import com.ly.lottery.net.protocal.Oelement;
import com.ly.lottery.net.protocal.element.CurrentIssueElement;
import com.ly.lottery.util.BeanFactoryUtil;
import com.ly.lottery.util.PromptManager;
import com.ly.lottery.view.adapter.PoolAdapter;
import com.ly.lottery.view.custom.MyGridView;
import com.ly.lottery.view.manager.BaseUI;
import com.ly.lottery.view.manager.BottomManager;
import com.ly.lottery.view.manager.MiddleManager;
import com.ly.lottery.view.manager.PlayGame;
import com.ly.lottery.view.manager.TitleManager;
import com.ly.lottery.view.sensor.ShakeListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private MyGridView redContainer;
    private GridView blueContainer;

    private PoolAdapter redAdapter;
    private PoolAdapter blueAdapter;

    private List<Integer> redNums;
    private List<Integer> blueNums;

    private SensorManager sensorManager;
    private ShakeListener shakeListener;

    public PlaySSQ(Context context) {
        super(context);
    }


    @Override
    protected void setListener() {
        randomRed.setOnClickListener(this);
        randomBlue.setOnClickListener(this);

/*        redContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });*/
        redContainer.setOnActionUpListener(new MyGridView.OnActionUpListener() {
            @Override
            public void onActionUp(View view, int position) {
                if (!redNums.contains(position+1)){
                    // 如果没有被选中
                    // 背景图片切换到红色
                    view.setBackgroundResource(R.drawable.id_redball);
                    redNums.add(position + 1);
                }else {
                    //被先中
                    //还原图片
                    view.setBackgroundResource(R.drawable.id_defalut_ball);
                    redNums.remove((Object)(position+1));
                }
                changeNotice();
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
                changeNotice();
            }
        });
    }

    @Override
    protected void init() {
        showInMiddle = (ViewGroup) View.inflate(context, R.layout.il_playssq,null);

        redContainer = (MyGridView) findViewById(R.id.ii_ssq_red_number_container);
        blueContainer = (GridView) findViewById(R.id.ii_ssq_blue_number_container);
        randomRed = (Button) findViewById(R.id.ii_ssq_random_red);
        randomBlue = (Button) findViewById(R.id.ii_ssq_random_blue);

        redNums = new ArrayList<Integer>();
        blueNums = new ArrayList<Integer>();

        redAdapter = new PoolAdapter(context,33,redNums,R.drawable.id_redball);
        blueAdapter = new PoolAdapter(context, 16, blueNums, R.drawable.id_blueball);

        redContainer.setAdapter(redAdapter);
        blueContainer.setAdapter(blueAdapter);

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

    }

    @Override
    public int getID() {
        return ConstantValue.VIEW_SSQ;
    }

    @Override
    public void onClick(View v) {
        Random random = new Random();
        switch (v.getId()) {
            case R.id.ii_ssq_random_red:
                redNums.clear();
                while (redNums.size()<6){
                    int num = random.nextInt(33)+1;
                    if (redNums.contains(num)){
                        continue;
                    }
                    redNums.add(num);
                }
                //处理展示
                redAdapter.notifyDataSetChanged();
                changeNotice();
                break;
            case R.id.ii_ssq_random_blue:
                blueNums.clear();
                int num = random.nextInt(16)+1;
                blueNums.add(num);
                blueAdapter.notifyDataSetChanged();
                changeNotice();
                break;
        }
        super.onClick(v);
    }

    @Override
    public void onResume() {
        super.onResume();
        changeTitle();
        changeNotice();
        clear();
        //注册
        shakeListener = new ShakeListener(context) {
            @Override
            public void randomCure() {
                randomCure();
            }
        };
        sensorManager.registerListener(shakeListener,sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_FASTEST);
    }

    /**
     * 机选一注
     */
    private void randomSSQ(){
        Random random = new Random();
        redNums.clear();
        while (redNums.size()<6){
            int num = random.nextInt(33)+1;
            if (redNums.contains(num)){
                continue;
            }
            redNums.add(num);
        }
        //处理展示

        blueNums.clear();
        int num = random.nextInt(16)+1;
        blueNums.add(num);

        blueAdapter.notifyDataSetChanged();
        redAdapter.notifyDataSetChanged();
        changeNotice();
    }

    @Override
    public void onPause() {
        super.onPause();
        //注销
        sensorManager.unregisterListener(shakeListener);
    }

    @Override
    public void clear() {
        redNums.clear();
        blueNums.clear();
        redAdapter.notifyDataSetChanged();
        blueAdapter.notifyDataSetChanged();
        changeNotice();
    }

    @Override
    public void done() {
        // ①判断：用户是否选择了一注投注
        if (redNums.size()>=6 && blueNums.size()>=1){
            // 一个购物车中，只能放置一个彩种，当前期的投注信息
            // ②判断：是否获取到了当前销售期的信息
            if (bundle!=null){
                // ③封装用户的投注信息：红球、蓝球、注数
                Ticket ticket = new Ticket();
                DecimalFormat decimalFormat = new DecimalFormat("00");
                StringBuffer redBuffer = new StringBuffer();
                for (Integer item:redNums)
                {
                    redBuffer.append(" ").append(decimalFormat.format(item));
                }
                ticket.setRedNum(redBuffer.substring(1));

                StringBuffer blueBuffer = new StringBuffer();
                for (Integer item:blueNums){
                    blueBuffer.append(" ").append(decimalFormat.format(item));
                }
                ticket.setBlueNum(blueBuffer.substring(1));

                ticket.setNum(calcBet());

                //创建彩票购物车，将投注信息添加到购物车中
                ShoppingCart.getInstance().getTickets().add(ticket);
                //设置采种的标识，设置采种期次
                ShoppingCart.getInstance().setLotteryid(getID());
                ShoppingCart.getInstance().setIssue(bundle.getString("issue"));
                //界面跳转--购物车展示
                MiddleManager.getInstance().changeUI(Shopping.class,bundle);

            }else {
                //重新获取其次信息
                getCurrentIssueInfo();
            }
        }

    }

    private void getCurrentIssueInfo() {
        new MyHttpTask<Integer>(){
            @Override
            protected void onPreExecute() {
                //显示滚动条
                PromptManager.showProgressDialog(context);
            }

            @Override
            protected Message doInBackground(Integer... params) {
                //获取数据--业务调用
                CommonInfoEngine commonInfoEngine = BeanFactoryUtil.getImpl(CommonInfoEngine.class);
                return commonInfoEngine.getCurrentIssueInfo(params[0]);
            }

            @Override
            protected void onPostExecute(Message message) {
                PromptManager.closeProgressDialog();
                if (message!=null){
                    Oelement oelement = message.getBody().getOelement();
                    if (ConstantValue.SUCCESS.equals(oelement.getErrorcode())){
                        CurrentIssueElement element = (CurrentIssueElement) message.getBody().getElements().get(0);
                        //创建bundle
                        bundle = new Bundle();
                        bundle.putString("issue",element.getIssue());
                        changeTitle();
                    }else {
                        PromptManager.showToast(context,oelement.getErrormsg());
                    }
                }else {
                    PromptManager.showToast(context, "服务器忙，请稍后重试……");
                }
            }
        }.executeProxy(ConstantValue.SSQ);
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

    private void changeNotice(){
        String notice = "";
        if (redNums.size()<6){
            notice = "您还需要选择"+(6-redNums.size()) + "个红球";
        }else if (blueNums.size()==0){
            notice = "您还需要选择1个蓝球";
        }else {
            notice = "共"+calcBet()+"注 "+calcBet()*2+"元";
        }

        BottomManager.getInstance().changeGameBottomNotice(notice);
    }

    /**
     * 计算注数
     * @return
     */
    private int calcBet(){
        int redC= (int) (factorial(redNums.size())/(factorial(6)*factorial(redNums.size()-6)));
        int blueC = blueNums.size();
        return redC*blueC;
    }

    /**
     * 计算一个数的阶乘
     * @param num
     * @return
     */
    private long factorial(int num){
        if (num>1){
            return num*factorial(num-1);
        }else if (num == 1||num == 0){
            return 1;
        }else {
            throw new IllegalArgumentException("num need larger than 0");
        }
    }
}
