package test.lilo.com.gameprototype.bean;

import android.graphics.Bitmap;
import android.graphics.Point;

/**
 * 小人
 * 
 * @author Administrator
 * 
 */
public class Man extends Sprite {

	public static final int DOWN = 0;
	private int speed = 10;

	public Man(Bitmap img, Point pos) {
		super(img, pos);
	}

	// 产生一个笑脸
	public Face createFace(Bitmap faceImg, Point touchPos) {
		// 坐标

		Point facePos = new Point(pos.x + 50, pos.y + 45);
		Face face = new Face(faceImg, facePos, touchPos);

		return face;
	}

	public void move(int d) {
		switch (d) {
		case DOWN:
			pos.y += speed;
			break;
		}
	}

}
