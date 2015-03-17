package com.lilosoft.xtcm.constant;

import java.io.File;

import android.content.SharedPreferences;
import android.os.Environment;

/**
 * @category 配置
 * @author Yi Liu
 *
 */
public class Config {

    public final static String TELNUM = "07283605522";

    /**
     * @category logLevel true=运营模式 false=工程模式
     */
    public final static boolean LOGLEVEL = false;

    /**
     * @category 网络模式 true=有网 false=无网
     */
    public final static boolean NETWORK = false;

    /**
     * @category 网络请求数据编码格式
     */
    public final static String CHARSET = "UTF-8";
    /**
     * @category 网络请求超时时间
     */
    public final static int CONNECTION_TIMEOUT = 300000;
    public final static int SO_TIMEOUT = 100000;
    /**
     * @category 数据库名
     */
    public final static String DATABASE_NAME = "xtcm.db";
    public final static int DATABASE_VERSION = 4;
    /**
     * @category 临时数据保存文件名
     */
    public final static String SHARED_PREFERENCES_NAME = "XTCM_sp";
    public final static String SHARED_PREFERENCES_ISUPDATE = "isUpdateEvent";//是否更新事件分类信息
    public final static String SHARED_PREFERENCES_IC = "XTCM_IC";
    public final static String SHARED_PREFERENCES_KEY = "key0x9480";
    /**
     * @category 等待框状态
     */
    public final static int DISMISS_PROGRESS_DIALOG = 0xDDFFFFFF;
    public final static int SHOW_PROGRESS_DIALOG = 0xDD000001;
    /**
     * @category ４０４
     */
    public final static int MSG_LOST_404 = 0x108FFFFF;
    /**
     * @category 连接失败
     */
    public final static int MSG_LOST_CONN = 0x1008FFFF;
    /**
     * @category 数据异常
     */
    public final static int MSG_LOST_JSON = 0x10008FFF;
    /**
     * @category TAB字段
     */
    public final static String A_TAB = "A_TAB";
    public final static String B_TAB = "B_TAB";
    public final static String C_TAB = "C_TAB";
    public final static String D_TAB = "D_TAB";
    public final static String E_TAB = "E_TAB";
    // 房屋的编码
    public static final String HOUSE_CODE = "HOUSE_CODE";
    public static final String FWDBM = "FWDBM";
    // 地址名称
    public static final String DZMC = "DESC1";
    public static final String TypeVison = "TypeVison";
    /**
     * @category 地址自动上报间隔
     */
    private final static int SECOND = 1000;

    /**
     * @category 正式环境
     */
    // private static String URL_ = "http://218.200.131.71/";
    //private static String URL_ = "http://192.168.4.102:8003/";
//	private static String URL_="http://192.168.4.251:8003/";
    private final static int MINUTE = SECOND * 60;
    public final static long AUTO_LOCATION_REPORT_TIME = MINUTE * 2;
    private final static String FILES_NAME = "Xiantao_city";
    public final static String FILES_NAME_URL = Environment
            .getExternalStorageDirectory()
            + File.separator
            + FILES_NAME
            + File.separator;
    public static double pCardDistance=200;
//	public final static String VERSION_URL = "http://192.168.1.253:8003/DCG/MobilesInterfaces/apk/updateConfig.xml";
    /**
     * @category 网络请求 URL
     */
//	 private static String URL_ = "http://221.233.244.57:8003/";
//	 private static String MAPURL_ = "http://192.168.4.252:6080/";

//	private static String URL_ = "http://192.168.120.117:8003/";//接口服务器
//	private static String MAPURL_ = "http://111.47.112.90:6080/";//地图服务器

	private static String URL_ = "http://111.47.112.90:8003/";//接口服务器
   // private static String URL_ = "http://192.168.0.135:8003/";//接口服务器
    /**
     * @category 更新
     */
//	public final static String VERSION_URL = URL_+"DCG/MobilesInterfaces/apk/updateConfig.xml";
    public final static String VERSION_URL = URL_+"DCG/MobilesInterfaces/apk/LeaderupdateConfig.xml";//领导地图无文字图层
    /**
     * @category 上传
     */
    public static String URL_REPORT = URL_
            + "DCG/MobilesInterfaces/Reported.aspx";
    /**
     * @category 下发
     */
    public static String URL_READY_ISSUED = URL_
            + "DCG/MobilesInterfaces/Issued.aspx";
    /**
     * @category 文件上传
     */
    public static String URL_READY_FILEUPLOAD = URL_
            + "DCG/MobilesInterfaces/FileUpLoad.asmx";
    //	private static String URL_ = "http://192.168.4.119:8003/";
    private static String MAPURL_ = "http://111.47.112.90:6080/";//地图服务器

    //private static String MAPURL_ = "http://192.168.4.251:6080/";
//	private static String MAPURL_ = "http://192.168.4.250:6080/";
    /**
     * 打卡
     */
    public static final String PCARD_URL = MAPURL_
            + "/arcgis/rest/services/shayang/region/MapServer/2";
    // public final static String MAP =
    // "http://221.233.244.57:6080/arcgis/rest/services/xiantao2d/MapServer";
    // 沙洋
    // public final static String MAP =
    // "http://192.168.4.250:6080/arcgis/rest/services/shayang2d/MapServer";
    /**
     * @category 底图
     */
    // 仙桃
    public static String MAP = MAPURL_
            + "arcgis/rest/services/xiantao2d/MapServer";
    /**
     * @category 网格
     */
    // 仙桃
	/*public static String ARCGIS = MAPURL_
			+ "arcgis/rest/services/wg_xt_cg/MapServer/0";*/

//	public static String ARCGIS = MAPURL_+"arcgis/rest/services/wg21_xt_cg/MapServer/0";
//	public static String ARCGIS = MAPURL_+"arcgis/rest/services/xiantao/cm_grid_0811/MapServer/0";
    public static String ARCGIS = MAPURL_+"arcgis/rest/services/xiantao/cm_grid/MapServer/0";
//	public static String WORKURL = MAPURL_ + "arcgis/rest/services/xiantao/cm_work/MapServer/0";
    //http://111.47.112.90:6080/arcgis/rest/services/xiantao/cm_work/MapServer/0

    // public final static String ARCGIS =
    // "http://221.233.244.57:6080/arcgis/rest/services/wg21_xt_cg/MapServer/0";
    // public final static String ARCGIS =
    // "http://192.168.4.250:6080/arcgis/rest/services/wg_sy_cg/MapServer/0";
    //public static String ARCGIS = MAPURL_+"arcgis/rest/services/xiantao/cm_work/MapServer/0";
    public static String WORKURL = MAPURL_ + "arcgis/rest/services/xiantao/cm_workspace/MapServer/0";
    // 地址层、房层、网格id
    public static final String ADDRESS_LAYER_ID = MAPURL_+"/arcgis/rest/services/shayang/house/MapServer/0";
    public static final String HOUSE_LAYER_ID = MAPURL_+"/arcgis/rest/services/shayang/house/MapServer/1";
    //	public static final String WG_LAYER_ID = MAPURL_+"/arcgis/rest/services/shayang/region/MapServer/3";
    public static final String WG_LAYER_ID = MAPURL_+"/arcgis/rest/services/xiantao/cm_grid/MapServer/0";
    public static final String CHECK_PLACE=MAPURL_+"/arcgis/rest/services/xiantao/cm_check/MapServer/0";
    public static final String TILED_MAP_SERVICE = MAPURL_+"/arcgis/rest/services/shayang/map2d/MapServer";
    //	public static final String MAP_SERVICE = MAPURL_+"/arcgis/rest/services/shayang/region/MapServer";
    public static final String MAP_SERVICE = MAPURL_+"/arcgis/rest/services/xiantao/map2d/MapServer";

    /**
     * @category 获得地址
     * @return String
     */
    public static String getURL_() {
        return URL_;
    }

    /**
     * @category 获得地址
     * @return String
     */
    public static String getMapURL_() {
        return MAPURL_;
    }

    /**
     * @category 修改配置
     */
    public static void refreshURL_(String url) {
        URL_ = url;
        URL_REPORT = URL_ + "DCG/MobilesInterfaces/Reported.aspx";
        URL_READY_ISSUED = URL_ + "DCG/MobilesInterfaces/Issued.aspx";
        URL_READY_FILEUPLOAD = URL_ + "/DCG/MobilesInterfaces/FileUpLoad.asmx";
    }

    /**
     * @category 修改配置
     */
    public static void refreshMapURL_(String url) {
        MAPURL_ = url;
        MAP = MAPURL_ + "arcgis/rest/services/xiantao2d/MapServer";
        ARCGIS = MAPURL_ + "arcgis/rest/services/cm_grid_0811/MapServer/0";
    }
}
