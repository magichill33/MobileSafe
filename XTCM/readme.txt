项目名:城管通
 
一、结构说明:
com.lilosoft.xtcm.base //底层系统的父类，通用的方法
com.lilosoft.xtcm.constant //上行下行参数、固定的数据、系统参数配置
com.lilosoft.xtcm.database //数据存储
com.lilosoft.xtcm.instantiation //数据的实体类、数据耦合度太低，本不该有这么多类
com.lilosoft.xtcm.module //页面
com.lilosoft.xtcm.network //网络请求封装类
com.lilosoft.xtcm.utils //工具类、接口不多也放在这里
com.lilosoft.xtcm.views //自定义控件
com.lilosoft.xtcm.views.adapter //适配器
二、使用说明
Config类中配置{日志输出、网络模式、连接地址
	1.版本定制规范
		v0.1.0120Beta:
		   v-version
		   0-小于1为开发版本
		   1-此版本发布的次数 0开始
		   0120-发布日期
		   Beta-测试版本标记
		v1.0.0303Release:
		   v-version
	       1-大于0为正式版本第一版本
	       0-此版本发布的次数 0开始
	       0303-发布日期
	       Release-正式版本标记
	2.开发请在com.lilosoft.xtcm.constant.Config
		将LOGLEVEL属性修改
			
三、类中简要注释

///////////////////////////////////////////
///////////////////////////////////////////