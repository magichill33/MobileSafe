package com.ly.lottery.view;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ly.lottery.ConstantValue;
import com.ly.lottery.GlobalParams;
import com.ly.lottery.R;
import com.ly.lottery.bean.ShoppingCart;
import com.ly.lottery.bean.Ticket;
import com.ly.lottery.util.PromptManager;
import com.ly.lottery.view.manager.BaseUI;
import com.ly.lottery.view.manager.MiddleManager;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2015/2/13.
 */
public class Shopping extends BaseUI{

    private Button addOptional;//添加自选
    private Button addRandom; //添加机选

    private ListView shoppingList; //用户选择信息列表

    private ImageButton shoppingListClear; //清空购物车
    private TextView tvNotice; //提示信息
    private Button btnBuy; //购买

    private ShoppingAdapter shoppingAdapter;


    public Shopping(Context context) {
        super(context);
    }

    @Override
    protected void setListener() {
        addOptional.setOnClickListener(this);
        addRandom.setOnClickListener(this);
        shoppingListClear.setOnClickListener(this);
        btnBuy.setOnClickListener(this);
    }

    @Override
    protected void init() {
        showInMiddle = (ViewGroup) View.inflate(context,R.layout.il_shopping,null);
        addOptional = (Button) findViewById(R.id.ii_add_optional);
        addRandom = (Button) findViewById(R.id.ii_add_random);
        shoppingListClear = (ImageButton) findViewById(R.id.ii_shopping_list_clear);
        tvNotice = (TextView) findViewById(R.id.ii_shopping_lottery_notice);
        btnBuy = (Button) findViewById(R.id.ii_lottery_shopping_buy);
        shoppingList = (ListView) findViewById(R.id.ii_shopping_list);

        shoppingAdapter = new ShoppingAdapter();
        shoppingList.setAdapter(shoppingAdapter);
    }

    @Override
    public int getID() {
        return ConstantValue.VIEW_SHOPPING;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ii_add_optional:
                // 添加自选
                MiddleManager.getInstance().goBack();
                break;
            case R.id.ii_add_random:
                // 添加机选
                addRandom();
                changeNotice();
                break;
            case R.id.ii_shopping_list_clear:
                // 清空
                ShoppingCart.getInstance().getTickets().clear();
                shoppingAdapter.notifyDataSetChanged();
                changeNotice();
                break;
            case R.id.ii_lottery_shopping_buy:
                // 购买
                // ①判断：购物车中是否有投注
                if (ShoppingCart.getInstance().getTickets().size() >= 1) {
                    // ②判断：用户是否登录——被动登录
                    if (GlobalParams.isLogin) {
                        // ③判断：用户的余额是否满足投注需求
                        if (ShoppingCart.getInstance().getLotteryvalue() <= GlobalParams.MONEY) {
                            // ④界面跳转：跳转到追期和倍投的设置界面
                            MiddleManager.getInstance().changeUI(PreBet.class, bundle);
                        }else{
                            // 提示用户：充值去；界面跳转：用户充值界面
                            PromptManager.showToast(context, "充值去");
                        }
                    } else {
                        // 提示用户：登录去；界面跳转：用户登录界面
                        PromptManager.showToast(context, "登录去");
                        MiddleManager.getInstance().changeUI(UserLogin.class, bundle);
                    }
                } else {
                    // 分支
                    // 提示用户需要选择一注
                    PromptManager.showToast(context, "需要选择一注");
                }


                break;
        }
    }

    private void addRandom() {

        // 机选一注
        Random random = new Random();
        List<Integer> redNums = new ArrayList<Integer>();
        List<Integer> blueNums = new ArrayList<Integer>();

        // 机选红球
        while (redNums.size() < 6) {
            int num = random.nextInt(33) + 1;

            if (redNums.contains(num)) {
                continue;
            }
            redNums.add(num);
        }
        int num = random.nextInt(16) + 1;
        blueNums.add(num);

        // 封装Ticket
        Ticket ticket = new Ticket();
        DecimalFormat decimalFormat = new DecimalFormat("00");
        StringBuffer redBuffer = new StringBuffer();
        for (Integer item : redNums) {
            // redBuffer.append(decimalFormat.format(item)).append(" ");
            redBuffer.append(" ").append(decimalFormat.format(item));
        }
        ticket.setRedNum(redBuffer.substring(1));

        StringBuffer blueBuffer = new StringBuffer();
        for (Integer item : blueNums) {
            blueBuffer.append(" ").append(decimalFormat.format(item));
        }

        ticket.setBlueNum(blueBuffer.substring(1));

        ticket.setNum(1);
        // 添加到购物车
        ShoppingCart.getInstance().getTickets().add(ticket);
        // 更新界面
        shoppingAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        changeNotice();
    }

    public void changeNotice(){
        Integer lotterynumber = ShoppingCart.getInstance().getLotterynumber();
        Integer lotteryvalue = ShoppingCart.getInstance().getLotteryvalue();

        String noticeInfo = context.getResources().getString(R.string.is_shopping_list_notice);
        noticeInfo = StringUtils.replaceEach(noticeInfo,new String[]{
                "NUM", "MONEY" },new String[]{lotterynumber.toString(),lotteryvalue.toString()});
        tvNotice.setText(Html.fromHtml(noticeInfo));
    }

    private class ShoppingAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return ShoppingCart.getInstance().getTickets().size();
        }

        @Override
        public Object getItem(int position) {
            return ShoppingCart.getInstance().getTickets().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView==null){
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.il_shopping_row,null);
                holder.delete = (ImageButton) convertView.findViewById(R.id.ii_shopping_item_delete);
                holder.redNum = (TextView) convertView.findViewById(R.id.ii_shopping_item_reds);
                holder.blueNum = (TextView) convertView.findViewById(R.id.ii_shopping_item_blues);
                holder.num = (TextView) convertView.findViewById(R.id.ii_shopping_item_money);

                convertView.setTag(holder);

            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            Ticket ticket = ShoppingCart.getInstance().getTickets().get(position);
            holder.redNum.setText(ticket.getRedNum());
            holder.blueNum.setText(ticket.getBlueNum());
            holder.num.setText(ticket.getNum()+"注");
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShoppingCart.getInstance().getTickets().remove(position);
                    notifyDataSetChanged();

                    // 一般清空，ListView中没有设计增加项，做局部更新，不会调用notifyDataSetChanged去更新界面

                    changeNotice();
                }
            });
            return convertView;
        }
    }

    class ViewHolder {
        ImageButton delete;
        TextView redNum;
        TextView blueNum;
        TextView num;
    }
}
