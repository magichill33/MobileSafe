package cn.ithm.kmplayer1.view;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.ithm.kmplayer1.ConstantValue;
import cn.ithm.kmplayer1.R;
import cn.ithm.kmplayer1.bean.Video;
import cn.ithm.kmplayer1.util.ImageCache;
import cn.ithm.kmplayer1.util.ImageCallback;
import cn.ithm.kmplayer1.util.NetWorkTask;
import cn.ithm.kmplayer1.view.adapter.LrucacheAdapter;

import com.alibaba.fastjson.JSON;

/**
 * 更多
 * 
 * @author Administrator
 * 
 */
public class ChannelFragment extends BaseFragment {
	protected static final String TAG = "ChannelFragment";
	private GridView channelGridView;
	private LrucacheAdapter adapter;
	private int currentpage = 0;

	private LinearLayout progressLinear;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		System.out.println("onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		System.out.println("onattach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("oncreate");

		super.onCreate(savedInstanceState);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("oncrate view");

		View view = inflater.inflate(R.layout.il_channel, container, false);

		channelGridView = (GridView) view.findViewById(R.id.ii_channle_grid);
		progressLinear = (LinearLayout) view.findViewById(R.id.progress_linear);
		progressLinear.setVisibility(View.INVISIBLE);

		adapter = new LrucacheAdapter(getActivity(), new ImageCallback() {

			@Override
			public void imageLoaded(Bitmap bitmap, Object tag) {
				ImageView imageView = (ImageView) channelGridView
						.findViewWithTag(tag);
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		});
		channelGridView.setAdapter(adapter);

		channelGridView.setOnScrollListener(new OnScrollListener() {

			// private int getLastVisiblePosition;
			// private int lastVisiblePositionY;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_IDLE) {
					// 滚动到底部
					if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
						// View v = (View) view.getChildAt(view.getChildCount()
						// - 1);
						// int[] location = new int[2];
						// v.getLocationOnScreen(location);// 获取在整个屏幕内的绝对坐标
						// int y = location[1];
						//
						// Log.d(TAG, "x" + location[0] + "y" + location[1]);
						//
						// if (view.getLastVisiblePosition() !=
						// getLastVisiblePosition && lastVisiblePositionY !=
						// y)// 第一次拖至底部
						// {
						progressLinear.setVisibility(View.VISIBLE);
						// getLastVisiblePosition =
						// view.getLastVisiblePosition();
						// lastVisiblePositionY = y;

						currentpage++;
						NetWorkTask localNetWorkTask = new NetWorkTask();
						Object[] params = new Object[3];
						params[0] = ChannelFragment.this;
						params[1] = false;
						params[2] = "http://app.video.baidu.com/adnativemovie/?beg="
								+ (currentpage * 20)
								+ "&end="
								+ (currentpage * 20 + 20);
						localNetWorkTask.executeProxy(params);
						// return;
						// }
						// else if (view.getLastVisiblePosition() ==
						// getLastVisiblePosition && lastVisiblePositionY ==
						// y)//
						// 第二次拖至底部
						// {
						//
						// }
					}

					// 未滚动到底部，第二次拖至底部都初始化
					// getLastVisiblePosition = 0;
					// lastVisiblePositionY = 0;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});

		if (getArguments() != null && getArguments().containsKey("data")) {
			String url = getArguments().getString("data");
			NetWorkTask localNetWorkTask = new NetWorkTask();
			Object[] params = new Object[3];
			params[0] = this;
			params[1] = true;
			params[2] = url;
			localNetWorkTask.executeProxy(params);
		}
		return view;
	}

	@Override
	public void onGetResult(int errorCode, Object result) {
		progressLinear.setVisibility(View.INVISIBLE);
		if (errorCode == ConstantValue.SUCCESS && result != null) {
			String json = result.toString();
			try {
				JSONObject jsonObject = new JSONObject(json);
				List<Video> videos = JSON.parseArray(
						jsonObject.getJSONObject("video_list").getString(
								"videos"), Video.class);
				// 设置Adappter 更新GridView
				List<Video> videos2 = adapter.getVideos();
				if (videos2 != null && videos2.size() > 0) {
					videos2.addAll(videos);
					adapter.setVideos(videos2);
				} else {
					adapter.setVideos(videos);
				}
				adapter.notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.onGetResult(errorCode, result);
	}

	@Override
	public void onDestroy() {
		System.out.println("ondestroy");
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		System.out.println("onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		System.out.println("ondetach");
		super.onDetach();
	}

	@Override
	public void onResume() {
		System.out.println("onresume");
		super.onResume();
	}

	@Override
	public void onPause() {
		System.out.println("onPause");
		ImageCache.getInstance().clear();
		super.onPause();
	}

	@Override
	public void onStart() {
		System.out.println("onstart");
		super.onStart();
	}

	@Override
	public void onStop() {
		System.out.println("onstop");
		super.onStop();
	}
}
