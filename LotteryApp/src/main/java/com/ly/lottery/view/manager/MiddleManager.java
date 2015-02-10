package com.ly.lottery.view.manager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.ly.lottery.R;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 中间容器管理工具
 * Created by Administrator on 2015/2/10.
 */
public class MiddleManager {

    private static final String TAG = "MiddleManager";
    private static MiddleManager instance = new MiddleManager();
    BaseUI currentUI = null;
    private RelativeLayout middleContainer;
    private Map<String,BaseUI> VIEWCACHE = new HashMap<String,BaseUI>();
    private LinkedList<String> HISTORY = new LinkedList<String>(); //用户操作的历史记录

    private MiddleManager() {
    }

    public static MiddleManager getInstance() {
        return instance;
    }

    public void setMiddleContainer(RelativeLayout middleContainer) {
        this.middleContainer = middleContainer;
    }

    /**
     * 切换界面:解决问题“在标题容器中每次点击都在创建一个目标界面”
     *
     * @param targetClazz
     */
    public void changeUI(Class<? extends BaseUI> targetClazz){
        //判断当前正在展示的界面与切换界面是否相同
        if (currentUI!=null && currentUI.getClass() == targetClazz){
            return;
        }

        BaseUI targetUI = null;
        String key = targetClazz.getSimpleName();
        if (VIEWCACHE.containsKey(key)){
            targetUI = VIEWCACHE.get(key);
        }else {
            try {
              Constructor<? extends BaseUI> constructor = targetClazz.getConstructor(Context.class);
              targetUI = constructor.newInstance(getContext());
              VIEWCACHE.put(key,targetUI);
            } catch (Exception e) {
                throw new RuntimeException("constructor new instance error");
            }
        }

        currentUI = targetUI;

        Log.i(TAG,targetUI.toString());
        //切换界面的核心代码
        middleContainer.removeAllViews();
        View child = targetUI.getChild();
        middleContainer.addView(child);
        child.startAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.ia_view_change));

        currentUI = targetUI;
        //将当前显示的界面放到栈顶
        HISTORY.addFirst(key);
    }

    public Context getContext(){
        return middleContainer.getContext();
    }

    /**
     * 返回键处理
     * @return
     */
    public boolean goBack() {
        // 记录一下用户操作历史
        // 频繁操作栈顶（添加）——在界面切换成功
        // 获取栈顶
        // 删除了栈顶
        // 有序集合
        if (HISTORY.size()>0){
            if (HISTORY.size() ==1){
                return false;
            }

            HISTORY.removeFirst();
            if (HISTORY.size()>0){
                String key = HISTORY.getFirst();
                BaseUI targetUI = VIEWCACHE.get(key);

                middleContainer.removeAllViews();
                middleContainer.addView(targetUI.getChild());
                currentUI = targetUI;
                return true;
            }
        }
        return false;
    }
}
