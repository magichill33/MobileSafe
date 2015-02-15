package com.ly.lottery.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ly.lottery.ConstantValue;
import com.ly.lottery.GlobalParams;
import com.ly.lottery.R;
import com.ly.lottery.bean.User;
import com.ly.lottery.engine.UserEngine;
import com.ly.lottery.net.protocal.Message;
import com.ly.lottery.net.protocal.Oelement;
import com.ly.lottery.net.protocal.element.BalanceElement;
import com.ly.lottery.util.BeanFactoryUtil;
import com.ly.lottery.util.PromptManager;
import com.ly.lottery.view.manager.BaseUI;
import com.ly.lottery.view.manager.MiddleManager;

/**
 * 用户登陆+余额获取 两个登录入口：主动登录（购彩大厅）；被动登录（购物车）
 * Created by Administrator on 2015/2/10.
 */
public class UserLogin extends BaseUI{

    private EditText username;
    private ImageView ivClear;
    private EditText password;
    private Button btnLogin;

    public UserLogin(Context context) {
        super(context);
    }

    @Override
    protected void setListener() {
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (username.getText().toString().length()>0){
                    ivClear.setVisibility(View.VISIBLE);
                }
            }
        });

        ivClear.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    protected void init() {
        showInMiddle = (ViewGroup) View.inflate(context, R.layout.il_user_login, null);

        username = (EditText) findViewById(R.id.ii_user_login_username);
        ivClear = (ImageView) findViewById(R.id.ii_clear);
        password = (EditText) findViewById(R.id.ii_user_login_password);
        btnLogin = (Button) findViewById(R.id.ii_user_login);
    }

    @Override
    public int getID() {
        return ConstantValue.VIEW_LOGIN;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ii_clear:
                username.setText("");
                ivClear.setVisibility(View.INVISIBLE);
                break;
            case R.id.ii_user_login:
                //登录
                //用户输入信息
                if (checkUserInfo()){
                    //登录
                    final User user = new User();
                    user.setPassword(password.getText().toString());
                    user.setUsername(username.getText().toString());
                    new MyHttpTask<User>(){
                        @Override
                        protected void onPreExecute() {
                            PromptManager.showProgressDialog(context);
                            super.onPreExecute();
                        }

                        @Override
                        protected Message doInBackground(User... params) {
                            UserEngine engine = BeanFactoryUtil.getImpl(UserEngine.class);
                            Message login = engine.login(params[0]);
                            if (login!=null){
                                Oelement oelement = login.getBody().getOelement();
                                if (ConstantValue.SUCCESS.equals(oelement.getErrorcode()))
                                {
                                    //登陆成功了
                                    GlobalParams.isLogin = true;
                                    GlobalParams.USERNAME = params[0].getUsername();
                                    //成功了获取余额
                                    Message balance = engine.getBalance(params[0]);
                                    if (balance!=null){
                                        oelement = balance.getBody().getOelement();
                                        if (ConstantValue.SUCCESS.equals(oelement.getErrorcode()))
                                        {
                                            BalanceElement balanceElement = (BalanceElement) balance.getBody().getElements().get(0);
                                            GlobalParams.MONEY = Float.valueOf(balanceElement.getInvestvalues());
                                            return balance;
                                        }

                                    }

                                }
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Message message) {
                            PromptManager.closeProgressDialog();
                            if (message!=null){
                                // 界面跳转
                                PromptManager.showToast(context, "登录成功");
                                MiddleManager.getInstance().goBack();
                            }else {
                                PromptManager.showToast(context, "服务忙……");
                            }
                            super.onPostExecute(message);
                        }
                    }.executeProxy(user);
                }
                break;

        }
    }

    /**
     * 用户信息判断(根据实际情况进行验证)
     * @return
     */
    private boolean checkUserInfo()
    {
        return true;
    }
}
