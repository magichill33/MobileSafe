package cn.ithm.kmplayer1.util;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

import cn.ithm.kmplayer1.GloableParameters;
import cn.ithm.kmplayer1.net.HttpClientUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class ImageDownload extends AsyncTask<String, Integer, Bitmap> {
	public static final String CACHE_TYPE_SOFT = "SOFT";
	public static final String CACHE_TYPE_LRU = "LRU";
	public static final String CACHE_TYPE_DIS = "DIS";

	private ImageCallback imageCallback;
	private Object tag;

	public ImageDownload(ImageCallback imageCallback) {
		this.imageCallback = imageCallback;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		Bitmap bitmap = loadImageFromUrl(params[0]);
		if (bitmap != null) {
			tag = params[1];
			String flag = CACHE_TYPE_SOFT;
			try {
				flag = params[2];
			} catch (Exception e) {
			}
			if (CACHE_TYPE_LRU.equals(flag)) {
				ImageCache.getInstance().put(tag, bitmap);
			} else if (CACHE_TYPE_DIS.equals(flag)) {

			} else {
				GloableParameters.IMGCACHE.put(tag, bitmap);
			}

		}
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (imageCallback != null)
			imageCallback.imageLoaded(result, tag);
		super.onPostExecute(result);
	}

	public static Bitmap loadImageFromUrl(String url) {
		// url=StringUtils.replace(url, "10.0.2.2", "192.168.1.101");
		InputStream i = null;
		try {
			HttpClientUtil adapter = new HttpClientUtil();
			i = adapter.loadImg(url);

			// http://blog.csdn.net/xianming01/article/details/8280434
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inPurgeable = true;
			opt.inInputShareable = true;

			return BitmapFactory.decodeStream(i, null, opt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
