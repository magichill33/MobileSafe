package com.lilosoft.xtcm.module;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lilosoft.xtcm.utils.LogFactory;

public class BootBroadcastReceiver extends BroadcastReceiver {
	// 重写onReceive方法
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(
				"COM.LILOSOFT.XTCM.MODULE.AUTO_LOCATION_REPORT_SERVICE");
		context.startService(service);
		LogFactory.v("城管通", "自动启动");
		// 启动应用，参数为需要自动启动的应用的包名
	}
}
