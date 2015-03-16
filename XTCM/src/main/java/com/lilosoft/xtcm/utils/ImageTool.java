package com.lilosoft.xtcm.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.instantiation.FileBean;

/**
 * @category 图片处理类
 * @author William Liu
 * 
 */
@SuppressLint("DefaultLocale")
public class ImageTool {

	/**
	 * @category TAG
	 */
	private final static String TAG = "ImageTool";

	/**
	 * @category 文件输出流
	 */
	private static FileOutputStream fileOutputStream;

	/**
	 * @category 图片压缩
	 * @param bm
	 * @return
	 */
	public static Bitmap BitmapUtil(Bitmap bm) {
		int imgW = bm.getWidth();
		int imgH = bm.getHeight();
		float scale = (float) 1;
		if (imgW > 320) {
			scale = (float) 320 / imgW;
		}
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap newBm = Bitmap.createBitmap(bm, 0, 0, imgW, imgH, matrix, true);
		LogFactory.e(TAG + "_Pictrue Size", imgH + "*" + imgW);
		return newBm;
	}

	/**
	 * @category 图片转字节数组
	 * @param imagepath
	 * @return 字符形式图片
	 * @throws Exception
	 */
	public static String getPicString(Bitmap bitmap) {
		if (null != bitmap) {
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 50, bStream);
			return byteToString(bStream.toByteArray());
		}
		return "";
	}

	/**
	 * @category 写入图片至内存
	 * @param fileBean
	 */
	public static void writeFile(FileBean fileBean) {
		try {
			if (!new File(Config.FILES_NAME_URL + fileBean.getfName()).exists()) {
				fileOutputStream = new FileOutputStream(Config.FILES_NAME_URL
						+ fileBean.getfName());
				fileOutputStream.write(stringToBytes(fileBean.getfData()
						.toUpperCase()));
				fileOutputStream.flush();
				fileOutputStream.close();
				LogFactory.e(TAG + "_Write Pictrue", "\"" + fileBean.getfName()
						+ "\" add success");
			} else {
				LogFactory.e(TAG + "_Write Pictrue",
						"has \"" + fileBean.getfName() + "\" pictrue");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogFactory.e(TAG, "add \"" + fileBean.getfName() + "\" error");
		}
	}

	/**
	 * @category 字节数组转字符串
	 * @param bs
	 * @return
	 */
	private static String byteToString(byte[] bs) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		char[] out = new char[bs.length * 2];
		for (int i = 0; i < bs.length; i++) {
			byte c = bs[i];
			out[i * 2] = Digit[(c >>> 4) & 0X0F];
			out[i * 2 + 1] = Digit[c & 0X0F];
		}

		return new String(out);
	}

	/**
	 * @category 字符串转字节数组
	 * @param picS
	 * @return
	 */
	private static byte[] stringToBytes(String picS) {
		char[] cs = picS.toCharArray();
		byte[] outContent = new byte[cs.length / 2];

		for (int i = 0; i < outContent.length; i++) {
			int a = getIndex(cs[i * 2]);
			int b = getIndex(cs[i * 2 + 1]);

			byte f = (byte) ((a << 4) & 0x00f0);
			byte l = (byte) (b & 0x000f);

			outContent[i] = (byte) (f + l);
		}
		return outContent;

	}

	/**
	 * @category 进制转换10进制
	 * @param c
	 * @return
	 */
	private static int getIndex(char c) {

		char Digit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		for (int i = 0; i < Digit.length; i++) {
			if (c == Digit[i]) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * compute Sample Size
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	private static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	/**
	 * compute Initial Sample Size
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		// 上下限范围
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * get Bitmap
	 * 
	 * @param imgFile
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static Bitmap tryGetBitmap(String imgFile) {
		int minSideLength = 600;
		int maxNumOfPixels = (1 * 1024 * 1024);
		if (imgFile == null || imgFile.length() == 0)
			return null;

		try {
			@SuppressWarnings("resource")
			FileDescriptor fd = new FileInputStream(imgFile).getFD();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			// BitmapFactory.decodeFile(imgFile, options);
			BitmapFactory.decodeFileDescriptor(fd, null, options);

			options.inSampleSize = computeSampleSize(options, minSideLength,
					maxNumOfPixels);
			try {
				// 这里一定要将其设置回false，因为之前我们将其设置成了true
				// 设置inJustDecodeBounds为true后，decodeFile并不分配空间，即，BitmapFactory解码出来的Bitmap为Null,但可计算出原始图片的长度和宽度
				options.inJustDecodeBounds = false;

				Bitmap bmp = BitmapFactory.decodeFile(imgFile, options);
				return bmp == null ? null : bmp;
			} catch (OutOfMemoryError err) {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	// 图片质量压缩
	public static String compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 500) { // 循环判断如果压缩后图片是否大于500kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = null;
		ByteArrayOutputStream outStream = null;
		try {
			isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
			outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while (-1 != (len = isBm.read(buffer))) {
				outStream.write(buffer, 0, len);
			}
			outStream.close();
			isBm.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return byteToString(outStream.toByteArray());
	}

}
