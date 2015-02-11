package com.ly.lottery.view.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ly.lottery.net.NetUtil;
import com.ly.lottery.net.protocal.Message;
import com.ly.lottery.util.PromptManager;

/**
 * 所有界面的基类
 * Created by Administrator on 2015/2/10.
 */
public abstract class BaseUI implements View.OnClickListener{
    protected Context context;
    //显示到中间容器
    protected ViewGroup showInMiddle;

    public BaseUI(Context context){
        this.context = context;
        init();
        setListener();
    }

    protected abstract void setListener();//设置监听

     //初始化界面
    protected abstract void init();

    /**
     * 获取需要在中间容器加载的内容
     * @return
     */
    public View getChild(){
        // 设置layout参数

        // root=null
        // showInMiddle.getLayoutParams()=null
        // root!=null
        // return root

        // 当LayoutParams类型转换异常，向父容器看齐
        if (showInMiddle.getLayoutParams() == null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            showInMiddle.setLayoutParams(params);
        }
        return showInMiddle;
    }

    /**
     * 获取每个界面的标识--容器联动时的比对依据
     * @return
     */
    public abstract int getID();

    @Override
    public void onClick(View v) {

    }

    public View findViewById(int id){
        return showInMiddle.findViewById(id);
    }

    /**
     * 要出去的时候调用
     */
    public void onPause(){

    }

    /**
     * 进入到界面之后
     */
    public void onResume(){

    }

    protected abstract class MyHttpTask<Params> extends
            AsyncTask<Params,Void,Message>{
        /**
         * 类似与Thread.start方法 由于final修饰，无法Override，方法重命名 省略掉网络判断
         *
         * @param params
         * @return
         */
        public final AsyncTask<Params, Void, Message> executeProxy(
                Params... params) {
            if (NetUtil.checkNet(context)) {
                return super.execute(params);
            } else {
                PromptManager.showNoNetWork(context);
                return null;
            }

        }
    }
}
