package cn.ithm.kmplayer1.view;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import cn.ithm.kmplayer1.ConstantValue;
import cn.ithm.kmplayer1.GloableParameters;
import cn.ithm.kmplayer1.R;
import cn.ithm.kmplayer1.VideoViewPlayingActivity;
import cn.ithm.kmplayer1.bean.Hot;
import cn.ithm.kmplayer1.bean.Slice;
import cn.ithm.kmplayer1.util.ImageCallback;
import cn.ithm.kmplayer1.util.NetWorkTask;
import cn.ithm.kmplayer1.view.adapter.SoftcacheAdapter;
import cn.ithm.kmplayer1.view.manager.UIManager;

import com.alibaba.fastjson.JSON;

/**
 * 首页
 * 
 * @author Administrator
 * 
 */
public class HomeFragment extends BaseFragment {
	private static final String TAG = "HomeFragment";
	private Dialog dialog = null;
	private GridView gridviewMovie;

	private SoftcacheAdapter adapter;

	int isExit = 0;

	private TextView recommendMoreMovie;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		System.out.println("**onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		System.out.println("**onattach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("**oncreate");
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		System.out.println("**oncrate view");
		View view = inflater.inflate(R.layout.il_home, container, false);

		this.recommendMoreMovie = ((TextView) view.findViewById(R.id.recommendMoreMovie));
		this.gridviewMovie = ((GridView) view.findViewById(R.id.gridviewMovie));

		adapter = new SoftcacheAdapter(getActivity(), 6);

		this.gridviewMovie.setAdapter(adapter);
		adapter.setCallback(new ImageCallback() {

			@Override
			public void imageLoaded(Bitmap bitmap, Object tag) {
				if (bitmap != null) {
					ImageView img = (ImageView) gridviewMovie.findViewWithTag(tag);
					if (img != null)
						img.setImageBitmap(bitmap);
					else {
						Log.i(TAG, tag.toString());
					}
				}
			}
		});

		this.recommendMoreMovie.setOnClickListener(this.myOnClick);

		gridviewMovie.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Hot item = (Hot) adapter.getItem(position);
				String url = item.getUrl();
//				 String url = StringUtils.replace(item.getUrl(), "10.0.2.2", "192.168.1.101");
				Intent intent = new Intent(getActivity(), VideoViewPlayingActivity.class);
				intent.putExtra("url", url);
				getActivity().startActivity(intent);
			}
		});

		return view;
	}

	View.OnClickListener myOnClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.recommendMoreMovie:
					ChannelFragment fragment = new ChannelFragment();
					Bundle bundle = new Bundle();
					bundle.putString("data", ConstantValue.URI + ConstantValue.VIDEO_URI);
					UIManager.getInstance().changeFragment(fragment, bundle);
					break;
			}

		}

	};

	@Override
	public void onDestroy() {
		System.out.println("**ondestroy");
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		System.out.println("**onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		System.out.println("**ondetach");
		super.onDetach();
	}

	@Override
	public void onResume() {
		System.out.println("**onresume");

		NetWorkTask localNetWorkTask = new NetWorkTask();
		Object[] params = new Object[3];
		params[0] = this;
		params[1] = true;
		params[2] = ConstantValue.URI + ConstantValue.SLICE_URI;
		localNetWorkTask.executeProxy(params);

		super.onResume();
	}

	@Override
	public void onPause() {
		System.out.println("**onstart");
		GloableParameters.IMGCACHE.clear();
		super.onPause();
	}

	@Override
	public void onStart() {
		System.out.println("**onstart");
		super.onStart();
	}

	@Override
	public void onStop() {
		System.out.println("**onstop");
		super.onStop();
	}

	@Override
	public void onGetResult(int errorCode, Object result) {
		if (errorCode == ConstantValue.SUCCESS && result != null) {
			String json = result.toString();
			try {
				JSONObject jsonObject = new JSONObject(json);
				List<Slice> parseArray = JSON.parseArray(jsonObject.getString("slices"), Slice.class);
				Slice slice = parseArray.get(0);
				List<Hot> hot = slice.getHot();

				// 设置Adappter 更新GridView
				adapter.setHotList(hot);
				adapter.notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
}
