package com.lilosoft.xtcm.instantiation;

public class Part {
    //id
    private String objectId;
    //部件编号
    private String objCode;
    //部件名称
    private String objName;
    //所属区域编码
    private String orgcode;
    //主管部门编码
    private String deptCode1;
    //主管部门名称
    private String deptName1;
    //权属单位编码
    private String deptCode2;
    //权属单位名称
    private String deptName2;
    //养护单位编码
    private String deptCode3;
    //养护单位名称
    private String deptName3;
    //经度
    private double lon;
    //纬度
    private double lat;


    public String getObjectId() {
        return objectId;
    }
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
    public String getObjCode() {
        return objCode;
    }
    public void setObjCode(String objCode) {
        this.objCode = objCode;
    }
    public String getObjName() {
        return objName;
    }
    public void setObjName(String objName) {
        this.objName = objName;
    }
    public String getOrgcode() {
        return orgcode;
    }
    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode;
    }
    public String getDeptCode1() {
        return deptCode1;
    }
    public void setDeptCode1(String deptCode1) {
        this.deptCode1 = deptCode1;
    }
    public String getDeptName1() {
        return deptName1;
    }
    public void setDeptName1(String deptName1) {
        this.deptName1 = deptName1;
    }
    public String getDeptCode2() {
        return deptCode2;
    }
    public void setDeptCode2(String deptCode2) {
        this.deptCode2 = deptCode2;
    }
    public String getDeptName2() {
        return deptName2;
    }
    public void setDeptName2(String deptName2) {
        this.deptName2 = deptName2;
    }
    public String getDeptCode3() {
        return deptCode3;
    }
    public void setDeptCode3(String deptCode3) {
        this.deptCode3 = deptCode3;
    }
    public String getDeptName3() {
        return deptName3;
    }
    public void setDeptName3(String deptName3) {
        this.deptName3 = deptName3;
    }
    public double getLon() {
        return lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }
    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    @Override
    public String toString() {
        return "Part [objectId=" + objectId + ", objCode=" + objCode
                + ", objName=" + objName + ", orgcode=" + orgcode
                + ", deptCode1=" + deptCode1 + ", deptName1=" + deptName1
                + ", deptCode2=" + deptCode2 + ", deptName2=" + deptName2
                + ", deptCode3=" + deptCode3 + ", deptName3=" + deptName3
                + ", lon=" + lon + ", lat=" + lat + "]";
    }



}
