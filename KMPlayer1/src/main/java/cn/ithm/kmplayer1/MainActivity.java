package cn.ithm.kmplayer1;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.ithm.kmplayer1.util.PromptManager;
import cn.ithm.kmplayer1.view.ChannelFragment;
import cn.ithm.kmplayer1.view.HomeFragment;
import cn.ithm.kmplayer1.view.manager.UIManager;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private ImageView home;
	private ImageView channel;
	private ImageView search;
	private ImageView myself;

	private TextView title;

	private HomeFragment homeFragment;
	private ChannelFragment channelFragment;

	private int isExit = 0;
	private long lasttime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.il_main);
		GloableParameters.MAIN = this;
		init();
	}

	private void init() {
		initBottom();
		initHome();
		initChannel();
	}

	private void initChannel() {
		channelFragment = new ChannelFragment();
	}

	private void initHome() {
		homeFragment = new HomeFragment();
		UIManager.getInstance().changeFragment(homeFragment, null);
	}

	private void initBottom() {
		home = (ImageView) findViewById(R.id.ii_bottom_home);
		channel = (ImageView) findViewById(R.id.ii_bottom_channel);
		search = (ImageView) findViewById(R.id.ii_bottom_search);
		myself = (ImageView) findViewById(R.id.ii_bottom_lottery_myself);
		title = (TextView) findViewById(R.id.ii_title_content);

		home.setOnClickListener(this);
		channel.setOnClickListener(this);
		search.setOnClickListener(this);
		myself.setOnClickListener(this);
		title.setText("首页");
	}

	@Override
	public void onClick(View v) {
		home.setImageResource(getImageId(0, false));
		channel.setImageResource(getImageId(1, false));
		search.setImageResource(getImageId(2, false));
		myself.setImageResource(getImageId(3, false));

		switch (v.getId()) {
			case R.id.ii_bottom_home:
				title.setText("首页");
				home.setImageResource(getImageId(0, true));
				UIManager.getInstance().changeFragment(homeFragment, null);
				break;
			case R.id.ii_bottom_channel:
				title.setText("频道");
				channel.setImageResource(getImageId(1, true));
				UIManager.getInstance().changeFragment(channelFragment, null);
				break;
			case R.id.ii_bottom_search:
				title.setText("本地视频");
				search.setImageResource(getImageId(2, true));
				break;
			case R.id.ii_bottom_lottery_myself:
				title.setText("我的影视大全");
				myself.setImageResource(getImageId(3, true));
				break;
		}

	}

	private int getImageId(int paramInt, boolean paramBoolean) {
		switch (paramInt) {
			default:
				return -1;
			case 0:
				if (paramBoolean)
					return R.drawable.ic_tab_home_press;
				return R.drawable.ic_tab_home;
			case 1:
				if (paramBoolean)
					return R.drawable.ic_tab_channel_press;
				return R.drawable.ic_tab_channel;
			case 2:
				if (paramBoolean)
					return R.drawable.ic_tab_search_press;
				return R.drawable.ic_tab_search;
			case 3:
				if (paramBoolean)
					return R.drawable.ic_tab_my_press;
				return R.drawable.ic_tab_my;
		}

	}
	
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((event.getAction() == 0) && (keyCode == KeyEvent.KEYCODE_BACK)) {
			long currenttime = System.currentTimeMillis();
			if ((currenttime - lasttime) > 1000) {
				this.isExit = 0;
				lasttime = currenttime;
			}

			if (this.isExit == 0) {
				this.isExit++;
				PromptManager.showToast(this, "再点一次可退出");
				return true;
			}
			if (this.isExit == 1) {
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
