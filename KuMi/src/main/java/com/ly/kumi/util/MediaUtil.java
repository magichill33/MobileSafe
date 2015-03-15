package com.ly.kumi.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.ly.kumi.ConstantValue;
import com.ly.kumi.bean.Music;

public class MediaUtil {
	private List<Music> songList = new ArrayList<Music>();
	// 当前正在播放
	public static int CURRENTPOS = 0;
	public static int PLAYSTATE = ConstantValue.OPTION_PAUSE;

	private static MediaUtil instance = new MediaUtil();

	private MediaUtil() {
	}

	public static MediaUtil getInstacen() {
		return instance;
	}

	public List<Music> getSongList() {
		return songList;
	}

	public void initMusics(Context context) {
		songList.clear();
		Cursor cur = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ARTIST,
						MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DATA }, null, null, null);
		try {
			if (cur != null) {

				while (cur.moveToNext()) {
					Music m = new Music();
					m.setTitle(cur.getString(0));
					m.setDuration(cur.getString(1));
					m.setArtist(cur.getString(2));
					m.setId(cur.getString(3));
					m.setPath(cur.getString(4));
					songList.add(m);
				}
			}
		} catch (Exception e) {
		} finally {
			if (cur != null)
				cur.close();
		}

	}

	public Music getCurrent() {
		return songList.get(CURRENTPOS);
	}

}
