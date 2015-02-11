package com.ly.lottery.engine;

import com.ly.lottery.net.protocal.Message;

/**
 * 公共数据处理
 * Created by Administrator on 2015/2/11.
 */
public interface CommonInfoEngine {
    /**
     * 获取当前销售期信息
     * @param integer
     * @return
     */
    Message getCurrentIssueInfo(Integer integer);
}
