package com.lilosoft.xtcm.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.RejectedExecutionException;

import android.util.Log;

import com.esri.android.map.TiledServiceLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.io.UserCredentials;
/**
 * ÌìµØÍ¼
 */
public class GoogleMapServiceLayer extends TiledServiceLayer {

	private TileInfo tiandituTileInfo;

	public GoogleMapServiceLayer() {
		this(null, true);
	}

	public GoogleMapServiceLayer(UserCredentials usercredentials, boolean flag) {
		super("");
		setCredentials(usercredentials);

		if (flag)
			try {
				getServiceExecutor().submit(new Runnable() {

					public final void run() {
						a.initLayer();
					}

					final GoogleMapServiceLayer a;

					{
						a = GoogleMapServiceLayer.this;
						// super();
					}
				});
				return;
			} catch (RejectedExecutionException _ex) {
			}
	}

	protected void initLayer() {
		this.buildTileInfo();
		this.setFullExtent(new Envelope(-20037508.342787, -20037508.342787,
				20037508.342787, 20037508.342787));
		// this.setDefaultSpatialReference(SpatialReference.create(4490));
		// //CGCS2000
		this.setDefaultSpatialReference(SpatialReference.create(102113)); // GCS_WGS_1984
		// this.setInitialExtent(new Envelope(90.52,33.76,113.59,42.88));
		this.setInitialExtent(new Envelope(-20037508.342787, -20037508.342787,
				20037508.342787, 20037508.342787));
		super.initLayer();
	}

	public void refresh() {
		try {
			getServiceExecutor().submit(new Runnable() {

				public final void run() {
					if (a.isInitialized())
						try {
							a.b();
							a.clearTiles();
							return;
						} catch (Exception exception) {
							Log.e("ArcGIS",
									"Re-initialization of the layer failed.",
									exception);
						}
				}

				final GoogleMapServiceLayer a;

				{
					a = GoogleMapServiceLayer.this;
					// super();
				}
			});
			return;
		} catch (RejectedExecutionException _ex) {
			return;
		}
	}

	final void b() throws Exception {

	}

	@Override
	protected byte[] getTile(int level, int col, int row) throws Exception {
		/**
         * 
         * */

		byte[] result = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			URL sjwurl = new URL(this.getTianDiMapUrl(level, col, row));
			HttpURLConnection httpUrl = null;
			BufferedInputStream bis = null;
			byte[] buf = new byte[1024];

			httpUrl = (HttpURLConnection) sjwurl.openConnection();
			httpUrl.connect();
			bis = new BufferedInputStream(httpUrl.getInputStream());

			while (true) {
				int bytes_read = bis.read(buf);
				if (bytes_read > 0) {
					bos.write(buf, 0, bytes_read);
				} else {
					break;
				}
			}
			;
			bis.close();
			httpUrl.disconnect();

			result = bos.toByteArray();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	@Override
	public TileInfo getTileInfo() {
		return this.tiandituTileInfo;
	}

	/**
     * 
     * */
	private String getTianDiMapUrl(int level, int col, int row) {
		StringBuilder url = new StringBuilder("http://mt");
		url.append(col % 4);
		url.append(".google.cn/vt/v=w2.114&hl=zh-CN&gl=cn&x=");
		url.append(col);
		url.append("&y=");
		url.append(row);
		url.append("&z=");
		url.append(level);
		url.append("&s=");

		System.out.println(url.toString());
		return url.toString();
	}

	private void buildTileInfo() {
		Point originalPoint = new Point(-20037508.342787, 20037508.342787);

		double[] res = { 156543.033928000000, 78271.516963999900,
				39135.758482000100, 19567.879240999900, 9783.939620499960,
				4891.969810249980, 2445.984905124990, 1222.992452562490,
				611.496226281380, 305.748113140558, 152.874056570411,
				76.437028285073, 38.218514142537, 19.109257071268,
				9.554628535634, 4.777314267949, 2.388657133975, 1.194328566855,
				0.597164283560, 0.298582141648 };
		double[] scale = { 591657527.591555000000, 295828763.795777000000,
				147914381.897889000000, 73957190.948944000000,
				36978595.474472000000, 18489297.737236000000,
				9244648.868618000000, 4622324.434309000000,
				2311162.217155000000, 1155581.108577000000,
				577790.554289000000, 288895.277144000000, 144447.638572000000,
				72223.819286000000, 36111.909643000000, 18055.954822000000,
				9027.977411000000, 4513.988705000000, 2256.994353000000,
				1128.497176000000 };
		int levels = 20;
		int dpi = 96;
		int tileWidth = 256;
		int tileHeight = 256;
		this.tiandituTileInfo = new com.esri.android.map.TiledServiceLayer.TileInfo(
				originalPoint, scale, res, levels, dpi, tileWidth, tileHeight);
		this.setTileInfo(this.tiandituTileInfo);
	}
}
