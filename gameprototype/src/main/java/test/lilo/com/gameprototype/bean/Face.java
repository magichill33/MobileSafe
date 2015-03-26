package test.lilo.com.gameprototype.bean;

import android.graphics.Bitmap;
import android.graphics.Point;

/**
 * 笑脸
 * 
 * @author Administrator
 * 
 */
public class Face extends Sprite {
	private int speed = 3;

	private int dx = 0;
	private int dy = 0;

	public Face(Bitmap img, Point pos, Point touchPos) {
		super(img, pos);

		int x = touchPos.x - pos.x;
		int y = touchPos.y - pos.y;

		int d = (int) Math.sqrt(x * x + y * y);

		dx = x * speed / d;
		dy = y * speed / d;

	}

	/**
	 * 移动(①子线程控制；②每绘制一次，move方法调用)
	 */
	public void move() {
        // 改变笑脸的坐标

		pos.x += dx;
		pos.y += dy;
	}

}
