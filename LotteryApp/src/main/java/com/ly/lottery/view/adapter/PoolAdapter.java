package com.ly.lottery.view.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ly.lottery.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by magichill33 on 2015/2/11.
 */
public class PoolAdapter extends BaseAdapter {
    private Context context;
    private int endNum;
    private List<Integer> selectedNums;
    private int selectedBgResid; // 选中的背景图片的资源id

    public PoolAdapter(Context context, int endNum, List<Integer> selectedNums, int selectedBgResid) {
        this.context = context;
        this.endNum = endNum;
        this.selectedNums = selectedNums;
        this.selectedBgResid = selectedBgResid;
    }

    @Override
    public int getCount() {
        return endNum;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView ball = new TextView(context);
        ball.setGravity(Gravity.CENTER);
        ball.setTextSize(16);
        DecimalFormat decimalFormat = new DecimalFormat("00");
        ball.setText(decimalFormat.format(position+1));

        // 获取到用户已选号码的集合，判读集合中有，背景图片修改为红色
        if (selectedNums.contains(position+1)){
           ball.setBackgroundResource(selectedBgResid);
        }else {
           ball.setBackgroundResource(R.drawable.id_defalut_ball);
        }

        return ball;
    }
}
