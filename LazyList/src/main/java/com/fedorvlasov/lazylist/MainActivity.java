package com.fedorvlasov.lazylist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {

	public OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			adapter.imageLoader.clearCache();
			adapter.notifyDataSetChanged();
		}
	};
	ListView list;
	LazyAdapter adapter;
	private String[] mStrings = {
			"http://img4.imgtn.bdimg.com/it/u=67081693,688456503&fm=21&gp=0.jpg",
			"http://img1.imgtn.bdimg.com/it/u=1572619688,2556257561&fm=21&gp=0.jpg",
			"http://img1.imgtn.bdimg.com/it/u=3965959776,1683890537&fm=21&gp=0.jpg",
			"http://img5.imgtn.bdimg.com/it/u=3690315352,3211298795&fm=21&gp=0.jpg",
			"http://img2.imgtn.bdimg.com/it/u=2697922758,2950063512&fm=21&gp=0.jpg",
			"http://img2.imgtn.bdimg.com/it/u=1258036189,1965601165&fm=21&gp=0.jpg",
			"http://img0.imgtn.bdimg.com/it/u=4263977682,3758908600&fm=21&gp=0.jpg",
			"http://img2.imgtn.bdimg.com/it/u=1235190981,439023125&fm=21&gp=0.jpg",
			"http://img1.imgtn.bdimg.com/it/u=788561735,118444748&fm=21&gp=0.jpg",
			"http://img2.imgtn.bdimg.com/it/u=693819694,2630274538&fm=21&gp=0.jpg",
			"http://img0.imgtn.bdimg.com/it/u=4159023518,4044487886&fm=21&gp=0.jpg",
			"http://img1.imgtn.bdimg.com/it/u=742290634,3071738139&fm=21&gp=0.jpg",
			"http://img4.imgtn.bdimg.com/it/u=4012271569,3353532655&fm=21&gp=0.jpg",
			"http://img5.imgtn.bdimg.com/it/u=1735283443,2135287906&fm=21&gp=0.jpg",
			"http://img3.imgtn.bdimg.com/it/u=3761788634,2763310079&fm=21&gp=0.jpg",
			"http://img0.imgtn.bdimg.com/it/u=3024277644,2322495124&fm=21&gp=0.jpg",
			"http://img1.imgtn.bdimg.com/it/u=634036522,3808384027&fm=21&gp=0.jpg",
			"http://img1.imgtn.bdimg.com/it/u=217986749,3567933194&fm=21&gp=0.jpg",
			"http://img0.imgtn.bdimg.com/it/u=3703191626,2942193020&fm=21&gp=0.jpg",
			"http://img0.imgtn.bdimg.com/it/u=3516135959,2917562687&fm=21&gp=0.jpg",
			"http://img3.imgtn.bdimg.com/it/u=4221171651,3022045198&fm=21&gp=0.jpg",
			"http://img2.imgtn.bdimg.com/it/u=693819694,2630274538&fm=21&gp=0.jpg",
			"http://img1.imgtn.bdimg.com/it/u=634036522,3808384027&fm=21&gp=0.jpg",
			"http://img1.imgtn.bdimg.com/it/u=217986749,3567933194&fm=21&gp=0.jpg",
			"http://img0.imgtn.bdimg.com/it/u=3271263479,3178249433&fm=21&gp=0.jpg",
			"http://img3.imgtn.bdimg.com/it/u=2045525163,2709821110&fm=21&gp=0.jpg",
			"http://img3.imgtn.bdimg.com/it/u=2284426753,3117042336&fm=21&gp=0.jpg",
			"http://img2.imgtn.bdimg.com/it/u=4101535138,1640319884&fm=21&gp=0.jpg",
			"http://img2.imgtn.bdimg.com/it/u=3506485107,2049407097&fm=21&gp=0.jpg",
			"http://img4.imgtn.bdimg.com/it/u=2476155599,307950191&fm=21&gp=0.jpg",
			"http://img3.imgtn.bdimg.com/it/u=313954148,355031155&fm=21&gp=0.jpg",
			"http://img0.imgtn.bdimg.com/it/u=4044974583,2075481169&fm=21&gp=0.jpg",
			"http://img1.imgtn.bdimg.com/it/u=1881400439,3611997816&fm=21&gp=0.jpg",
			"http://img1.imgtn.bdimg.com/it/u=121110108,2105949427&fm=21&gp=0.jpg",
			"http://img2.imgtn.bdimg.com/it/u=2290000718,3811268662&fm=21&gp=0.jpg",
			"http://img1.imgtn.bdimg.com/it/u=3475648739,3441411714&fm=21&gp=0.jpg",
			"http://img4.imgtn.bdimg.com/it/u=2045120042,3609273420&fm=21&gp=0.jpg",
			"http://img0.imgtn.bdimg.com/it/u=1601711951,1429093843&fm=21&gp=0.jpg",
			"http://img2.imgtn.bdimg.com/it/u=1455922155,626779991&fm=21&gp=0.jpg",
			"http://img4.imgtn.bdimg.com/it/u=944437346,847543874&fm=21&gp=0.jpg",
			"http://img4.imgtn.bdimg.com/it/u=2702769000,2385763148&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2697922758,2950063512&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=1258036189,1965601165&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=4263977682,3758908600&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=1235190981,439023125&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=788561735,118444748&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=693819694,2630274538&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=4159023518,4044487886&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=742290634,3071738139&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=4012271569,3353532655&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1735283443,2135287906&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=3761788634,2763310079&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3024277644,2322495124&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=634036522,3808384027&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=217986749,3567933194&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3703191626,2942193020&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3516135959,2917562687&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=4221171651,3022045198&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=693819694,2630274538&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=634036522,3808384027&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=217986749,3567933194&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3271263479,3178249433&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=2045525163,2709821110&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=2284426753,3117042336&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=4101535138,1640319884&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=3506485107,2049407097&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2476155599,307950191&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=313954148,355031155&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=4044974583,2075481169&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=1881400439,3611997816&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=121110108,2105949427&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2290000718,3811268662&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=3475648739,3441411714&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2045120042,3609273420&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=1601711951,1429093843&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=1455922155,626779991&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=944437346,847543874&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2702769000,2385763148&fm=21&gp=0.jpg",
			"http://img2.imgtn.bdimg.com/it/u=2210843980,3241513832&fm=21&gp=0.jpg",
			"http://img2.imgtn.bdimg.com/it/u=2210843980,3241513832&fm=21&gp=0.jpg",
			"http://img3.imgtn.bdimg.com/it/u=1413717382,633492425&fm=21&gp=0.jpg",
			"http://img0.imgtn.bdimg.com/it/u=3809423354,1363072772&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=4263977682,3758908600&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=1235190981,439023125&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=788561735,118444748&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=693819694,2630274538&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=4159023518,4044487886&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=742290634,3071738139&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=4012271569,3353532655&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1735283443,2135287906&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=3761788634,2763310079&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3024277644,2322495124&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=634036522,3808384027&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=217986749,3567933194&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3703191626,2942193020&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3516135959,2917562687&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=4221171651,3022045198&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=693819694,2630274538&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=634036522,3808384027&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=217986749,3567933194&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3271263479,3178249433&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=2045525163,2709821110&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=2284426753,3117042336&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=4101535138,1640319884&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=3506485107,2049407097&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2476155599,307950191&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=313954148,355031155&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=4044974583,2075481169&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=1881400439,3611997816&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=121110108,2105949427&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2290000718,3811268662&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=3475648739,3441411714&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2045120042,3609273420&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=1601711951,1429093843&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=1455922155,626779991&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=944437346,847543874&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2702769000,2385763148&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2210843980,3241513832&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2210843980,3241513832&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=1413717382,633492425&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3809423354,1363072772&fm=21&gp=0.jpg" };

	/**
	 * 异步加载图片基本思想：
	 *  1. 先从内存缓存中获取图片显示（内存缓冲）
	 *  2. 获取不到的话从SD卡里获取（SD卡缓冲）
	 *  3. 都获取不到的话从网络下载图片并保存到SD卡同时加入内存并显示
	 *  4、采用线程池
        5、内存缓存+文件缓存
        6、内存缓存中网上很多是采用SoftReference来防止堆溢出，这儿严格限制只能使用最大JVM内存的1/4
        7、对下载的图片进行按比例缩放，以减少内存的消耗
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		list = (ListView) findViewById(R.id.list);
		adapter = new LazyAdapter(this, mStrings);
		list.setAdapter(adapter);

		Button b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(listener);
	}

	@Override
	public void onDestroy() {
		list.setAdapter(null);
		super.onDestroy();
	}
}