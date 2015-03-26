package test.lilo.com.gameprototype.bean;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

/**
 * 精灵(共性)
 */
public abstract class Sprite {
	// 图片
	protected Bitmap img;
	//位置
	protected Point pos;

	public Sprite(Bitmap img, Point pos) {
		super();
		this.img = img;
		this.pos = pos;

		if (this.pos == null) {
			this.pos = new Point(0, 0);
		}
	}

	// 绘制自己

	public void draw(Canvas canvas) {
		if (img != null)
			canvas.drawBitmap(img, pos.x, pos.y, null);
	}

	public Point getPos() {
		return pos;
	}
	
	
}
