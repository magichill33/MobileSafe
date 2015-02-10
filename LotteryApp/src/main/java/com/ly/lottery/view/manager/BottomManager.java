package com.ly.lottery.view.manager;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ly.lottery.R;

/**
 * 控件底部导航容器
 * Created by Administrator on 2015/2/10.
 */
public class BottomManager {
    protected static final String TAG = "BottomManager";

    private static BottomManager instance;
    /***********************底部菜单容器****************************/
    private RelativeLayout bottomMenuContainer;
    /************************底部导航**************************/
    private LinearLayout commonBottom; //购彩通用导航
    private LinearLayout playBottom; //购彩
    private ImageButton cleanButton;
    private ImageButton addButton;
    private TextView playBottomNotice;
    /************ 通用导航底部按钮 ************/
    private ImageButton homeButton;
    private ImageButton hallButton;
    private ImageButton rechargeButton;
    private ImageButton myselfButton;
    private BottomManager(){

    }

    public static BottomManager getInstance(){
        if (instance == null){
            synchronized(BottomManager.class) {
                if (instance == null){
                    instance = new BottomManager();
                }
            }

        }
        return instance;
    }

    public void init(Activity activity){
        bottomMenuContainer = (RelativeLayout) activity.findViewById(R.id.ii_bottom);
        commonBottom = (LinearLayout) activity.findViewById(R.id.ii_bottom_common);
        playBottom = (LinearLayout) activity.findViewById(R.id.ii_bottom_game);

        playBottomNotice = (TextView) activity.findViewById(R.id.ii_bottom_game_choose_notice);
        cleanButton = (ImageButton) activity.findViewById(R.id.ii_bottom_game_choose_clean);
        addButton = (ImageButton) activity.findViewById(R.id.ii_bottom_game_choose_ok);

        setListener();
    }

    /**
     * 设置监听
     */
    private void setListener(){
        cleanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"点击清空按键");
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"点击选好按键");
            }
        });
    }

    /**
     * 转换到通用导航
     */
    public void showCommonBottom(){
        if (bottomMenuContainer.getVisibility() ==View.GONE || bottomMenuContainer.
                getVisibility() == View.INVISIBLE)
        {
            bottomMenuContainer.setVisibility(View.VISIBLE);
        }

        commonBottom.setVisibility(View.VISIBLE);
        playBottom.setVisibility(View.INVISIBLE);
    }

    /**
     * 转换到购彩
     */
    public void showGameBottom() {
        if (bottomMenuContainer.getVisibility() == View.GONE || bottomMenuContainer.getVisibility() == View.INVISIBLE) {
            bottomMenuContainer.setVisibility(View.VISIBLE);
        }
        commonBottom.setVisibility(View.INVISIBLE);
        playBottom.setVisibility(View.VISIBLE);
    }

    /**
     * 设置玩法底部提示信息
     * @param notice
     */
    public void changeGameBottomNotice(String notice){
        playBottomNotice.setText(notice);
    }
}
