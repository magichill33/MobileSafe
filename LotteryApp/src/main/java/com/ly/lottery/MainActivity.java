package com.ly.lottery;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ly.lottery.util.PromptManager;
import com.ly.lottery.view.FirstUI;
import com.ly.lottery.view.Hall;
import com.ly.lottery.view.manager.BottomManager;
import com.ly.lottery.view.manager.MiddleManager;
import com.ly.lottery.view.manager.TitleManager;

public class MainActivity extends Activity {
    private RelativeLayout middleContainer; //中间占位的容器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.il_main);
        init();
    }

    private void init(){
        TitleManager manager = TitleManager.getInstance();
        manager.init(this);
        manager.showUnLoginTitle();

        BottomManager bottomManager = BottomManager.getInstance();
        bottomManager.init(this);
        bottomManager.showCommonBottom();

        middleContainer = (RelativeLayout) findViewById(R.id.ii_middle);
        MiddleManager middleManager = MiddleManager.getInstance();
        middleManager.setMiddleContainer(middleContainer);

        //建立观察者和被观察者之间的关系
        middleManager.addObserver(manager);
        middleManager.addObserver(bottomManager);

        middleManager.changeUI(Hall.class); //加载第一个界面
    }

    // a、用户返回键操作捕捉
    // b、响应返回键——切换到历史界面
    // LinkedList<String>——AndroidStack
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            boolean result = MiddleManager.getInstance().goBack();
            //返回键操作失败
            if (!result)
            {
                //Toast.makeText(MainActivity.this, "是否退出系统", Toast.LENGTH_LONG).show();
                PromptManager.showExitSystem(this);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
