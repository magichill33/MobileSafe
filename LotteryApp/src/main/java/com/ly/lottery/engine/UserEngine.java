package com.ly.lottery.engine;

import com.ly.lottery.bean.User;
import com.ly.lottery.net.protocal.Message;

/**
 * Created by Administrator on 2015/2/9.
 */
public interface UserEngine {
    /**
     * 用户登录
     */
    Message login(User user);

}
