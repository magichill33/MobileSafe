package com.ly.lottery.view.manager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.ly.lottery.ConstantValue;
import com.ly.lottery.R;
import com.ly.lottery.util.MemoryManager;
import com.ly.lottery.util.PromptManager;
import com.ly.lottery.util.SoftMap;
import com.ly.lottery.view.Hall;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;

/**
 * 中间容器管理工具
 * Created by Administrator on 2015/2/10.
 */
public class MiddleManager extends Observable{

    private static final String TAG = "MiddleManager";
    private static MiddleManager instance = new MiddleManager();
    // 利用手机内存空间，换应用应用的运行速度
    private static Map<String, BaseUI> VIEWCACHE;// K
    // :唯一的标示BaseUI的子类
    static {
        // 16M，如果不足<16M(模拟器)
        // 32M，真机
        if (MemoryManager.hasAcailMemory()){
            VIEWCACHE = new HashMap<String,BaseUI>();
        }else {
            VIEWCACHE = new SoftMap<String,BaseUI>();
        }
    }
    BaseUI currentUI = null;
    private RelativeLayout middleContainer;
    //private Map<String,BaseUI> VIEWCACHE = new HashMap<String,BaseUI>();
    private LinkedList<String> HISTORY = new LinkedList<String>(); //用户操作的历史记录

    private MiddleManager() {
    }

    public static MiddleManager getInstance() {
        return instance;
    }

    // 每增加一个界面150K——16M
    // 内存不足
    // 处理的方案：
    // 第一种：控制VIEWCACHE集合的size
    // 第二种：Fragment代替，replace方法，不会缓存界面
    // 第三种：降低BaseUI的应用级别
    // 强引用：当前（GC宁可抛出OOM，不会回收BaseUI）
    // 软引用：在OOM之前被GC回收掉
    // 弱引用：一旦被GC发现了就回收
    // 虚引用：一旦创建了就被回收了

    // 都存在优缺点
    // 第一种：代码实现简单，适应性不强
    // 第二种：上一个Fragment被回收了，当内存充足的时候，运行速度损失过多
    // 第三种：优点，缺点：虽然引用级别降低，但是必须等待GC去回收，必须要提供给GC一个回收的时间，所以一旦申请内存速度过快，不适用
    // 瀑布流——Lrucache

    public void setMiddleContainer(RelativeLayout middleContainer) {
        this.middleContainer = middleContainer;
    }

    public BaseUI getCurrentUI() {
        return currentUI;
    }

    public void changeUI(Class<? extends BaseUI> targetClazz,Bundle bundle){
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

        if (currentUI!=null)
        {
            // 在清理掉当前正在展示的界面之前——onPause方法
            currentUI.onPause();
        }

        Log.i(TAG,targetUI.toString());
        if (targetUI!=null){
            targetUI.setBundle(bundle);
        }


        //切换界面的核心代码
        middleContainer.removeAllViews();
        View child = targetUI.getChild();
        middleContainer.addView(child);
        child.startAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.ia_view_change));

        //在加载完界面之后--onResume
        targetUI.onResume();

        currentUI = targetUI;
        //将当前显示的界面放到栈顶
        HISTORY.addFirst(key);

        //当中间容器切换成功时，处理别外的两个容器的变化
        changeTitleAndBottom();
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

        if (currentUI!=null)
        {
            // 在清理掉当前正在展示的界面之前——onPause方法
            currentUI.onPause();
        }

        Log.i(TAG,targetUI.toString());
        //切换界面的核心代码
        middleContainer.removeAllViews();
        View child = targetUI.getChild();
        middleContainer.addView(child);
        child.startAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.ia_view_change));

        //在加载完界面之后--onResume
        targetUI.onResume();

        currentUI = targetUI;
        //将当前显示的界面放到栈顶
        HISTORY.addFirst(key);

        //当中间容器切换成功时，处理别外的两个容器的变化
        changeTitleAndBottom();
    }

    public Context getContext(){
        return middleContainer.getContext();
    }

    private void changeTitleAndBottom()
    {
        // 1、界面一对应未登陆标题和通用导航
        // 2、界面二对应通用标题和玩法导航

        // 当前正在展示的如果是第一个界面
        // 方案一：存在问题，比对的依据：名称 或者 字节码
        // 在界面处理初期，将所有的界面名称确定
        // 如果是字节码，将所有的界面都的创建完成
        // if(currentUI.getClass()==FirstUI.class){
        // TitleManager.getInstance().showUnLoginTitle();
        // BottomManager.getInstrance().showCommonBottom();
        // }
        // if(currentUI.getClass().getSimpleName().equals("SecondUI")){
        // TitleManager.getInstance().showCommonTitle();
        // BottomManager.getInstrance().showGameBottom();
        // }

        // 方案二：更换比对依据

      /*  switch (currentUI.getID()) {
            case ConstantValue.VIEW_FIRST:
                TitleManager.getInstance().showUnLoginTitle();
                BottomManager.getInstance().showCommonBottom();
                // LeftManager\RightManager
                break;
            case ConstantValue.VIEW_SECOND:
                TitleManager.getInstance().showCommonTitle();
                BottomManager.getInstance().showGameBottom();
                break;
            case 3:
                TitleManager.getInstance().showCommonTitle();
                BottomManager.getInstance().showGameBottom();
                break;
        }*/

        // 降低三个容器的耦合度
        // 当中间容器变动的时候，中间容器“通知”其他的容器，你们该变动了，唯一的标示传递，其他容器依据唯一标示进行容器内容的切换
        // 通知：
        // 广播：多个应用
        // 为中间容器的变动增加了监听——观察者设计模式


        // ①将中间容器变成被观察的对象
        // ②标题和底部导航变成观察者
        // ③建立观察者和被观察者之间的关系（标题和底部导航添加到观察者的容器里面）
        // ④一旦中间容器变动，修改boolean，然后通知所有的观察者.updata()
        setChanged();
        notifyObservers(currentUI.getID());
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
                if (targetUI!=null){
                    currentUI.onPause();
                    middleContainer.removeAllViews();
                    middleContainer.addView(targetUI.getChild());
                    targetUI.onResume();
                    currentUI = targetUI;
                    changeTitleAndBottom();
                }else {
                    //处理方式一：创建一个新的目标界面：存在问题——如果有其他的界面传递给被删除的界面
                    // 处理方式二：寻找一个不需要其他界面传递数据——跳转到首页
                    changeUI(Hall.class);
                    PromptManager.showToast(getContext(), "应用在低内存下运行");
                }

                return true;
            }
        }
        return false;
    }

    /**
     * 清空返回列表中信息
     */
    public void clear() {
        HISTORY.clear();
    }
}
