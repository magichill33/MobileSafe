package com.ly.mobilesafe.receiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.ly.mobilesafe.R;
import com.ly.mobilesafe.service.UpdateWidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class MyWidget extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        /**
         * 启动一个服务用来定时更新Widget信息
         */
        Intent intent1 = new Intent(context, UpdateWidgetService.class);
        context.startService(intent1);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context,appWidgetManager,appWidgetIds);
    }


    @Override
    public void onEnabled(Context context) {
        Intent intent = new Intent(context,UpdateWidgetService.class);
        context.startService(intent);
    }

    @Override
    public void onDisabled(Context context) {
        Intent intent = new Intent(context,UpdateWidgetService.class);
        context.stopService(intent);
    }

}


