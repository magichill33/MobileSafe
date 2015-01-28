package com.ly.mobilesafe.receiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.ly.mobilesafe.R;
import com.ly.mobilesafe.service.UpdateWidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class MyWidget extends AppWidgetProvider {

    private boolean isEnable = true;
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i("MyWidget","onReceive方法....");
        /**
         * 启动一个服务用来定时更新Widget信息
         */
        if(isEnable){
            Intent intent1 = new Intent(context, UpdateWidgetService.class);
            context.startService(intent1);
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context,appWidgetManager,appWidgetIds);
    }


    @Override
    public void onEnabled(Context context) {
        Log.i("MyWidget","启用widget");
        isEnable = true;
        Intent intent = new Intent(context,UpdateWidgetService.class);
        context.startService(intent);
    }

    @Override
    public void onDisabled(Context context) {
        Log.i("MyWidget","停用widget");
        isEnable = false;
        Intent intent = new Intent(context,UpdateWidgetService.class);
        context.stopService(intent);
    }

}


