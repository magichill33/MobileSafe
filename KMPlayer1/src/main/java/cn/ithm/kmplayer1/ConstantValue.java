package cn.ithm.kmplayer1;

public interface ConstantValue {
	String ENCODING = "utf-8";
	/**
	 * 服务器地址
	 */
//	String URI = "http://10.0.2.2:8080/KMPlayerService/res";
	String URI = "http://192.168.1.3:8080/KMPlayerService/res";
//	String URI = "http://192.168.1.104:8080/KMPlayerService/res/json/slice.txt";
	
	String SLICE_URI = "/json/slice.txt";
	String VIDEO_URI = "/json/video.txt";
	
	/**
	 * 分类
	 */
	int CATEGORY = 1;

	int ERROR = -100;
	int SUCCESS = 0;
	String IMAGE_PATH = "/img";
}
