package com.lilosoft.xtcm.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.RejectedExecutionException;

import android.util.Log;

import com.esri.android.map.TiledServiceLayer;
import com.esri.android.map.TiledServiceLayer.TileInfo;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.io.UserCredentials;

public class XianTaoWMTSLayer extends TiledServiceLayer {
	
	private TianDiTuTiledMapServiceType _mapType;
	private TileInfo tiandituTileInfo;
	
/*	public XianTaoWMTSLayer(boolean initLayer) {
		super(initLayer);
	}*/
	
	public XianTaoWMTSLayer() {
		this(null, null, true);
	}

	public XianTaoWMTSLayer(TianDiTuTiledMapServiceType mapType) {
		this(mapType, null, true);
	}

	public XianTaoWMTSLayer(TianDiTuTiledMapServiceType mapType,
			UserCredentials usercredentials) {
		this(mapType, usercredentials, true);
	}

	public XianTaoWMTSLayer(TianDiTuTiledMapServiceType mapType,
			UserCredentials usercredentials, boolean flag) {
		super("");
		this._mapType = mapType;
		setCredentials(usercredentials);

		if (flag)
			try {
				getServiceExecutor().submit(new Runnable() {

					public final void run() {
						a.initLayer();
					}

					final XianTaoWMTSLayer a;

					{
						a = XianTaoWMTSLayer.this;
						// super();
					}
				});
				return;
			} catch (RejectedExecutionException _ex) {
			}
	}

	public TianDiTuTiledMapServiceType getMapType() {
		return this._mapType;
	}

	protected void initLayer() {
		this.buildTileInfo();
		this.setFullExtent(new Envelope(-180, -90, 180, 90));
		// this.setDefaultSpatialReference(SpatialReference.create(4490));
		// //CGCS2000
		this.setDefaultSpatialReference(SpatialReference.create(4490)); // GCS_WGS_1984
		// this.setInitialExtent(new Envelope(90.52,33.76,113.59,42.88));
		//this.setInitialExtent(new Envelope(108.31208162289873,
			//	28.43301753729623, 116.89833669751555, 33.7232802677077));
		this.setInitialExtent(new Envelope(70.0, 15.0, 135.0, 55.0));
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

				final XianTaoWMTSLayer a;

				{
					a = XianTaoWMTSLayer.this;
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

		/**
		 * 天地图矢量、影像
		 * */
		StringBuilder url = new StringBuilder("http://111.47.112.90:8718/newmap/ogc/xiantao/vector");
		
		url.append("/wmts?Service=WMTS&Request=GetTile&Version=1.0.0")
		.append("&Style=Default&Format=image/png").append("&serviceMode=KVP")
		.append("&layer=vector").append("&TileMatrixSet=TileMatrixSet_0")
		.append("&TileMatrix=").append(level)
		.append("&TileRow=").append(row)
		.append("&TileCol=").append(col);
		Log.v("ly", url.toString());
		return url.toString();
	}

	private void buildTileInfo() {
		Point originalPoint = new Point(-180, 90);

		double[] res = { 0.010986328125, 0.0054931640625, 
				0.00274658203125, 0.001373291015625,
				0.0006866455078125, 0.00034332275390625, 
				0.000171661376953125,0.0000858306884765625,
				0.00004291534423828125,0.000021457672119140625,
				0.000010728836059570313,0.000005364418029785156,
				0.000005364418029785156/2,0.000005364418029785156/4
				};
		double[] scale = {4622333.678977588,2311166.839488794,
				1155583.419744397,577791.7098721985,
				288895.85493609926,144447.92746804963,
				72223.96373402482,36111.98186701241,
				18055.990933506204,9027.995466753102,
				4513.997733376551,2256.998866688275,
				1128.499917984,564.249958992};
		int levels = 14;
		int dpi = 96;
		int tileWidth = 256;
		int tileHeight = 256;
		this.tiandituTileInfo = new com.esri.android.map.TiledServiceLayer.TileInfo(
				originalPoint, scale, res, levels, dpi, tileWidth, tileHeight);
		this.setTileInfo(this.tiandituTileInfo);
	}

	public enum TianDiTuTiledMapServiceType {
		/**
		 * 天地图矢量
		 * */
		VEC_C,
		/**
		 * 天地图影像
		 * */
		IMG_C,
		/**
		 * 天地图矢量标注
		 * */
		CVA_C,
		/**
		 * 天地图影像标注
		 * */
		CIA_C
	}
	

}
