package com.lilo.model;

public class PartField {
    public static String objCode = "OBJCODE"; //标识码
    public static String objName = "OBJNAME"; //标准名称
    public static String deptCode1 = "DEPTCODE1"; //主管部门代码
    public static String deptName1 = "DEPTNAME1"; //主管部门全称
    public static String deptCode2 = "DEPTCODE2"; //责任单位代码
    public static String deptName2 = "DEPTNAME2"; //责任单位全称
    public static String deptName3 = "DEPTCODE3"; //养护单位 全称
    public static String deptCode3 = "DEPTNAME3"; //养护单位代码
    public static String bgCode = "BGCODE"; //所在单元网格代码
    public static String objState = "OBJSTATE"; //状态(完好/破损/丢失/占用)
    public static String objUptodate = "OBJUPTODAT"; //现势性(在用/作废)
    public static String orDate = "ORDATE"; //初始时间
    public static String chDate = "CHDATE"; //变更时间
    public static String dataSource = "DATASOURCE"; //数据来源(实测/地形图/其它/调绘)
    public static String picture = "PICTURE"; //部件照片
    public static String note = "NOTE_"; //备注
    public static String material = "MATERIAL"; //材质,不适合于部件编号为(0120~0121，0124~0126,0131~0137)
    public static String tradeScope = "TRADESCOPE"; //经营范围 适合于(0125)
    public static String num = "NUM_"; //健身设施数目 适合于(0126)
    public static String spec = "SPEC"; //规格开状和尺寸(0403)
    public static String sculptName = "SCULPTNAME"; //雕塑名称(0406)
    public static String siteName = "SITENAME"; //工地名称(0602)
    public static String stopName = "STOPNAME"; //站名 (0203)

    public static void setValue(String key,String value,Part desti)
    {

    }
}
