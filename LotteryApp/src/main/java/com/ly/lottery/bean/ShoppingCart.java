package com.ly.lottery.bean;

import com.ly.lottery.GlobalParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车
 * Created by Administrator on 2015/2/13.
 */
public class ShoppingCart {
    private static ShoppingCart instance = new ShoppingCart();
    // appnumbers string 倍数
    // issuesnumbers string 追期
    // issueflag int * 是否多期追号 0否，1多期
    // bonusstop int * 中奖后是否停止：0不停，1停
    private Integer lotteryid;
    private String issue;

    // 投注
    // lotteryid string * 玩法编号
    // issue string * 期号（当前销售期）
    // lotterycode string * 投注号码，注与注之间^分割
    // lotterynumber string 注数
    // lotteryvalue string 方案金额，以分为单位
    private List<Ticket> tickets = new ArrayList<Ticket>();
    private Integer lotterynumber; //计算
    private Integer lotteryvalue;
    private Integer appnumbers = 1;
    private Integer issuenumbers = 1;

    private ShoppingCart(){

    }

    public static ShoppingCart getInstance()
    {
        return instance;
    }

    public static void setInstance(ShoppingCart instance) {
        ShoppingCart.instance = instance;
    }

    public Integer getLotteryid() {
        return lotteryid;
    }

    public void setLotteryid(Integer lotteryid) {
        this.lotteryid = lotteryid;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Integer getLotterynumber() {
        lotterynumber = 0;
        for (Ticket ticket:tickets){
            lotterynumber += ticket.getNum();
        }
        return lotterynumber;
    }

    public Integer getLotteryvalue() {
        lotteryvalue = 2*getLotterynumber()*appnumbers*issuenumbers;
        return lotteryvalue;
    }


    public Integer getAppnumbers() {
        return appnumbers;
    }


    public Integer getIssuenumbers() {
        return issuenumbers;
    }

    /**
     * 操作倍数
     * @param isAdd
     * @return
     */
    public boolean addAppnumbers(boolean isAdd){
        if (isAdd){
            appnumbers ++;
            if (appnumbers>99){
                appnumbers --;
                return false;
            }
            if (getLotteryvalue()> GlobalParams.MONEY){
                appnumbers--;
                return false;
            }
        }else {
            appnumbers --;
            if (appnumbers==0){
                appnumbers++;
                return false;
            }
        }
        return true;
    }

    /**
     * 操作追期
     * @param isAdd
     * @return
     */
    public boolean addIssuesnumbers(boolean isAdd){
        if (isAdd){
            issuenumbers ++;
            if (issuenumbers>99){
                issuenumbers --;
                return false;
            }

            if (getLotteryvalue()>GlobalParams.MONEY){
                issuenumbers --;
                return false;
            }
        }else {
            issuenumbers--;
            if (issuenumbers == 0){
                issuenumbers ++;
                return false;
            }
        }

        return true;
    }

    public void clear(){
        tickets.clear();
        lotterynumber = 0;
        lotteryvalue = 0;
        appnumbers = 1;
        issuenumbers = 1;
    }

}
