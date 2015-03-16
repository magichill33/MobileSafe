package com.lilosoft.xtcm.module;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.HomeBaseActivity;
import com.lilosoft.xtcm.base.TabBaseActivity;
import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.constant.Limit;
import com.lilosoft.xtcm.instantiation.User;
import com.lilosoft.xtcm.utils.ActivityManager;
import com.lilosoft.xtcm.views.FunctionItem;

/**
 * @category 主页
 * @author William Liu
 * 
 */
public class HomeActivity extends TabBaseActivity implements OnClickListener {

	private boolean isExit = false;
	private boolean hasTask = false;

	private FunctionItem functionItem;
	private FunctionItem functionItem1;
	private FunctionItem functionItem2;
	private FunctionItem functionItem3;
	private FunctionItem functionItem4;
	private FunctionItem functionItem5;
	private FunctionItem functionItem6;
	private FunctionItem functionItem7;
	private FunctionItem functionItem8;
	private FunctionItem functionItem10;
	//部件上报，联系人
	private FunctionItem functionItem11;
//	private FunctionItem functionItem12;
	//手动打卡
	private FunctionItem functionItem13;

	@Override
	protected void initListView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void installViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_home);
		setViewsSize();
		functionItem = (FunctionItem) findViewById(R.id.f_qreport);
		functionItem1 = (FunctionItem) findViewById(R.id.f_qreport1);
		functionItem2 = (FunctionItem) findViewById(R.id.f_qreport2);
		functionItem3 = (FunctionItem) findViewById(R.id.f_qreport3);
		// functionItem4 = (FunctionItem) findViewById(R.id.f_qreport4);
		functionItem5 = (FunctionItem) findViewById(R.id.f_qreport5);
		functionItem6 = (FunctionItem) findViewById(R.id.f_qreport6);
		functionItem7 = (FunctionItem) findViewById(R.id.f_qreport7);
		functionItem8 = (FunctionItem) findViewById(R.id.f_qreport8);
		functionItem10 = (FunctionItem) findViewById(R.id.f_qreport10);
		functionItem11 = (FunctionItem) findViewById(R.id.f_qreport11);
//		functionItem12 = (FunctionItem) findViewById(R.id.f_qreport12);
		functionItem13 = (FunctionItem) findViewById(R.id.f_qreport13);
		// showAndRegisterEventsItemWithPermission();

	}

	// private void showAndRegisterEventsItemWithPermission() {
	// if (Config.LOGLEVEL) {
	// functionItem8.setVisibility(View.INVISIBLE);
	// functionItem8.setOnClickListener(this);
	// }
	//
	// }

	@Override
	protected void registerEvents() {
		// TODO Auto-generated method stub
		functionItem.setOnClickListener(this);
		functionItem1.setOnClickListener(this);
		functionItem2.setOnClickListener(this);
		functionItem3.setOnClickListener(this);
		// functionItem4.setOnClickListener(this);
		functionItem5.setOnClickListener(this);
		functionItem6.setOnClickListener(this);
		functionItem7.setOnClickListener(this);
		functionItem8.setOnClickListener(this);
		functionItem10.setOnClickListener(this);
		functionItem11.setOnClickListener(this);
//		functionItem12.setOnClickListener(this);
		functionItem13.setOnClickListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				isExit = false;
				hasTask = false;
			}
		};

		if (KeyEvent.KEYCODE_BACK == keyCode) {
			if (!isExit) {
				if (!hasTask) {
					hasTask = true;
					Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
					timer.schedule(task, 2000);
				} else {
					ActivityManager.removeAllActivity();
					finish();
				}
			}
		} else if (KeyEvent.KEYCODE_MENU == keyCode) {
			return super.onKeyDown(keyCode, event);
		}
		return false;
	}

	private void setViewsSize() {

		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();

		int topPx = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 130, getResources()
						.getDisplayMetrics());

		int layoutHeight = (dm.heightPixels - topPx) / 4;

		LinearLayout functionLayout = (LinearLayout) findViewById(R.id.home_layout);
		LinearLayout functionLayout1 = (LinearLayout) findViewById(R.id.home_layout1);
		LinearLayout functionLayout2 = (LinearLayout) findViewById(R.id.home_layout2);
		// LinearLayout functionLayout3 = (LinearLayout)
		// findViewById(R.id.home_layout3);

		// functionLayout.setLayoutParams(new LinearLayout.LayoutParams(
		// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		// functionLayout1.setLayoutParams(new LinearLayout.LayoutParams(
		// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		// functionLayout2.setLayoutParams(new LinearLayout.LayoutParams(
		// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		// functionLayout3.setLayoutParams(new LinearLayout.LayoutParams(
		// LayoutParams.WRAP_CONTENT, layoutHeight));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// Client权限控制
		if (User.limit.equals(Limit.limits[0]) || !Config.NETWORK) {
			switch (v.getId()) {
			case R.id.f_qreport:
				HomeBaseActivity.tabHost.setCurrentTabByTag(Config.B_TAB);
				HomeBaseActivity.tabBt2.setChecked(true);
				break;
			case R.id.f_qreport1:
				HomeBaseActivity.tabHost.setCurrentTabByTag(Config.C_TAB);
				HomeBaseActivity.tabBt3.setChecked(true);
				break;
			case R.id.f_qreport2:
				HomeBaseActivity.tabHost.setCurrentTabByTag(Config.D_TAB);
				HomeBaseActivity.tabBt4.setChecked(true);
				break;
			case R.id.f_qreport3:
				HomeBaseActivity.tabHost.setCurrentTabByTag(Config.E_TAB);
				HomeBaseActivity.tabBt5.setChecked(true);
				break;
			/*
			 * case R.id.f_qreport4: // 待办理任务 startActivity(new
			 * Intent(HomeActivity.this, ReadyDisposeActivity.class)); break;
			 */
			case R.id.f_qreport5:
				startActivity(new Intent(HomeActivity.this,
						QuestionHistoryActivity.class));
				break;
			case R.id.f_qreport6:
				// 一键拨号
				startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ Config.TELNUM)));
				break;
			case R.id.f_qreport7:
				startActivity(new Intent(HomeActivity.this,
						QuestionOwnDisposeActivity.class));
				break;
			case R.id.f_qreport8:// 行政审批
				startActivity(new Intent(HomeActivity.this,
						AdministratorApproveMenuActivity.class));
				break;
			case R.id.f_qreport10:// 退出
				exit(this);
				break;
			case R.id.f_qreport11:// 联系人
				startActivity(new Intent(HomeActivity.this,
						ContactsActivity.class));
				break;
//			case R.id.f_qreport12:// 部件上报
//				startActivity(new Intent(HomeActivity.this,
//						PartReportActivity.class));
//				break;
			case R.id.f_qreport13:// 手动打卡
				startActivity(new Intent(HomeActivity.this,
						PunchCardActivity.class));
				break;
			default:
				Toast.makeText(mContext, R.string.no_permission,
						Toast.LENGTH_SHORT).show();
				break;
			}
		} else {
			Toast.makeText(mContext, R.string.no_permission, Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * 退出程序
	 * 
	 * @param cont
	 */
	private void exit(final Context cont) {
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.app_menu_surelogout);
		builder.setPositiveButton(R.string.sure,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 退出
						finish();
					}
				});
		builder.setNegativeButton(R.string.cancle,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(0, 0, 0, "登出");
		menu.add(0, 1, 0, "退出");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Intent intent = new Intent(mContext, LoginActivity.class);
			intent.putExtra("ifUpdate", "NO");
			intent.putExtra("unlogin", true);
			startActivity(intent);
			finish();
			break;
		case 1:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
