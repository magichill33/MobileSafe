package com.ly.lottery.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ly.lottery.ConstantValue;
import com.ly.lottery.R;
import com.ly.lottery.view.manager.BaseUI;

/**
 * Created by magichill33 on 2015/2/10.
 */
public class Hall extends BaseUI{
    // 第一步：加载layout（布局参数设置）
    // 第二步：初始化layout中控件
    // 第三步：设置监听

    private TextView ssqIssue;
    private ImageView ssqBet;

    public Hall(Context context) {
        super(context);
    }

    @Override
    protected void setListener() {
        ssqBet.setOnClickListener(this);
    }

    @Override
    protected void init() {
        showInMiddle = (android.view.ViewGroup) View.inflate(context, R.layout.il_hall,null);
        ssqIssue = (TextView) findViewById(R.id.ii_hall_ssq_summary);
        ssqBet = (ImageView) findViewById(R.id.ii_hall_ssq_bet);
    }

    @Override
    public int getID() {
        return ConstantValue.VIEW_HALL;
    }
}
