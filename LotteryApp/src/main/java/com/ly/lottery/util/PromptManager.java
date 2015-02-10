package com.ly.lottery.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.ly.lottery.R;

/**
 * Created by Administrator on 2015/2/10.
 */
public class PromptManager {
    private static ProgressDialog dialog;

    /**
     * 退出系统
     *
     * @param context
     */
    public static void showExitSystem(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.icon)//
                .setTitle(R.string.app_name)//
                .setMessage("是否退出应用").setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
                // 多个Activity——懒人听书：没有彻底退出应用
                // 将所有用到的Activity都存起来，获取全部，干掉
                // BaseActivity——onCreated——放到容器中
            }
        })//
                .setNegativeButton("取消", null)//
                .show();

    }
}
