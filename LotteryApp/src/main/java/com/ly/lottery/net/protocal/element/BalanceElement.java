package com.ly.lottery.net.protocal.element;

import com.ly.lottery.net.protocal.Element;

import org.xmlpull.v1.XmlSerializer;

/**
 * Created by Administrator on 2015/2/15.
 */
public class BalanceElement extends Element{

    /***********回复信息*********************/
    //investvalues 可投注金额
    private String investvalues;

    public String getInvestvalues() {
        return investvalues;
    }

    public void setInvestvalues(String investvalues) {
        this.investvalues = investvalues;
    }
    /***********回复信息*********************/
    @Override
    public void serializerElement(XmlSerializer serializer) {

    }

    @Override
    public String getTransactionType() {
        return "11007";
    }
}
