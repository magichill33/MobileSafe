package cn.ithm.kmplayer1.view.adapter;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.ithm.kmplayer1.GloableParameters;
import cn.ithm.kmplayer1.R;
import cn.ithm.kmplayer1.bean.Hot;
import cn.ithm.kmplayer1.bean.Video;
import cn.ithm.kmplayer1.util.ImageCache;
import cn.ithm.kmplayer1.util.ImageCallback;
import cn.ithm.kmplayer1.util.ImageDownload;
import cn.ithm.kmplayer1.view.adapter.SoftcacheAdapter.ViewHolder;

/**
 * @author Administrator
 * 
 */
public class LrucacheAdapter extends BaseAdapter {
	private Context context;
	private List<Video> videos;
	private ImageCallback callback;

	public LrucacheAdapter(Context context, ImageCallback callback) {
		super();
		this.context = context;
		this.callback = callback;
	}
	
	

	public List<Video> getVideos() {
		return videos;
	}



	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}



	@Override
	public int getCount() {
		int size = 0;
		if (videos != null) {
			size = videos.size();
		}
		return size;
	}

	@Override
	public Object getItem(int position) {
		return videos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.il_gridview_item, null);

			holder.itemImg = (ImageView) convertView.findViewById(R.id.item_img);
			holder.scord = (TextView) convertView.findViewById(R.id.scoreId);
			holder.title = (TextView) convertView.findViewById(R.id.txt_loading);

			holder.itemImg.setImageResource(R.drawable.id_default);

			convertView.setTag(holder);
		}

		if (videos != null) {
			try {
				Video video = videos.get(position);
				if (video != null) {
					String imgUrl = video.getImg_url();
					if (StringUtils.isNotBlank(imgUrl)) {
						holder.itemImg.setTag(video.getWorks_id());

						Bitmap bm = ImageCache.getInstance().get(video.getWorks_id());

						if (bm != null) {
							holder.itemImg.setImageBitmap(bm);
						} else {
							holder.itemImg.setImageResource(R.drawable.id_default);
							if (callback != null) {
								new ImageDownload(callback).execute(imgUrl, video.getWorks_id(),ImageDownload.CACHE_TYPE_LRU);
							}
						}
					}
				}

				holder.scord.setText(video.getRating() + "分");
				holder.title.setText(video.getTitle());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return convertView;

	}

	class ViewHolder {
		ImageView itemImg;
		TextView scord;
		TextView title;

	}

}
