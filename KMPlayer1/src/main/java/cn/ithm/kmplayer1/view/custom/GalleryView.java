package cn.ithm.kmplayer1.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

public class GalleryView extends Gallery {
	public static boolean ISAUTOFLING = true;

	public GalleryView(Context paramContext) {
		super(paramContext);
	}

	public GalleryView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}

	public GalleryView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
	}

	private boolean isScrollingLeft(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2) {
		return paramMotionEvent2.getX() > paramMotionEvent1.getX();
	}

	public boolean onDown(MotionEvent paramMotionEvent) {
		return super.onDown(paramMotionEvent);
	}

	public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
		if (isScrollingLeft(paramMotionEvent1, paramMotionEvent2)) {
			ISAUTOFLING = false;
			scrollToLeft();
			return false;
		}
		ISAUTOFLING = false;
		scrollToRight();
		return false;
	}

	public void scrollToLeft() {
		onScroll(null, null, -1.0F, 0.0F);
		super.onKeyDown(21, null);
	}

	public void scrollToRight() {
		onScroll(null, null, 1.0F, 0.0F);
		onKeyDown(22, null);
	}
}
