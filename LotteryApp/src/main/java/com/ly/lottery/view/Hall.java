package com.ly.lottery.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ly.lottery.ConstantValue;
import com.ly.lottery.R;
import com.ly.lottery.engine.CommonInfoEngine;
import com.ly.lottery.net.protocal.Element;
import com.ly.lottery.net.protocal.Message;
import com.ly.lottery.net.protocal.Oelement;
import com.ly.lottery.net.protocal.element.CurrentIssueElement;
import com.ly.lottery.util.BeanFactoryUtil;
import com.ly.lottery.util.PromptManager;
import com.ly.lottery.view.manager.BaseUI;

import org.apache.commons.lang3.StringUtils;

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

        //getCurrentIssueInfo();
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

    protected void changeNotice(Element element){
        CurrentIssueElement currentIssueElement = (CurrentIssueElement) element;
        String issue = currentIssueElement.getIssue();
        String lasttime = currentIssueElement.getLasttime();
        lasttime = getLasttime(lasttime);
        //第ISSUE期 还有TIME停售
        String text = context.getResources().getString(R.string.is_hall_common_summary);
        text = StringUtils.replaceEach(text,new String[]{ "ISSUE", "TIME" },
                new String[]{issue,lasttime});
        ssqIssue.setText(text);

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

}
