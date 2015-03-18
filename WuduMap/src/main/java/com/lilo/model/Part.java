package com.lilo.model;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 部件基础类
 * @author Administrator
 *
 */
public class Part {
    private String objCode; //标识码
    private String objName; //标准名称
    private String deptCode1; //主管部门代码
    private String deptName1; //主管部门全称
    private String deptCode2; //责任单位代码
    private String deptName2; //责任单位全称
    private String deptName3; //养护单位 全称
    private String deptCode3; //养护单位代码
    private String bgCode; //所在单元网格代码
    //private String objLocation; //部件所在具体位置的描述
    private String objState; //状态(完好/破损/丢失/占用)
    private String objUptodate; //现势性(在用/作废)
    private String orDate; //初始时间
    private String chDate; //变更时间
    private String dataSource; //数据来源(实测/地形图/其它/调绘)
    private String picture; //部件照片
    private String note; //备注

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
    public String getBgCode() {
        return bgCode;
    }
    public void setBgCode(String bgCode) {
        this.bgCode = bgCode;
    }
    /*public String getObjLocation() {
        return objLocation;
    }
    public void setObjLocation(String objLocation) {
        this.objLocation = objLocation;
    }*/
    public String getObjState() {
        return objState;
    }
    public void setObjState(String objState) {
        this.objState = objState;
    }
    public String getObjUptodate() {
        return objUptodate;
    }
    public void setObjUptodate(String objUptodate) {
        this.objUptodate = objUptodate;
    }
    public String getOrDate() {
        return orDate;
    }
    public void setOrDate(String orDate) {
        this.orDate = orDate;
    }
    public String getChDate() {
        return chDate;
    }
    public void setChDate(String chDate) {
        this.chDate = chDate;
    }
    public String getDataSource() {
        return dataSource;
    }
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public String getDeptName3() {
        return deptName3;
    }
    public void setDeptName3(String deptName3) {
        this.deptName3 = deptName3;
    }
    public String getDeptCode3() {
        return deptCode3;
    }
    public void setDeptCode3(String deptCode3) {
        this.deptCode3 = deptCode3;
    }


    @Override
    public String toString() {
        return "Part [objCode=" + objCode + ", objName=" + objName
                + ", deptCode1=" + deptCode1 + ", deptName1=" + deptName1
                + ", deptCode2=" + deptCode2 + ", deptName2=" + deptName2
                + ", deptName3=" + deptName3 + ", deptCode3=" + deptCode3
                + ", bgCode=" + bgCode + ", objState=" + objState
                + ", objUptodate=" + objUptodate + ", orDate=" + orDate
                + ", chDate=" + chDate + ", dataSource=" + dataSource
                + ", picture=" + picture + ", note=" + note + "]";
    }

    protected Method[] concat(Method[] a, Method[] b) {
        final int alen = a.length;
        final int blen = b.length;
        if (alen == 0) {
            return b;
        }
        if (blen == 0) {
            return a;
        }
        final Method[] result = (Method[]) java.lang.reflect.Array.
                newInstance(a.getClass().getComponentType(), alen + blen);
        System.arraycopy(a, 0, result, 0, alen);
        System.arraycopy(b, 0, result, alen, blen);
        return result;
    }

    public List<Method> getMethods()
    {
        List<Method> methodList = new ArrayList<Method>();
        Method[] methods = this.getClass().getDeclaredMethods();
        Method[] pmethods = Part.class.getDeclaredMethods();
        Method[] totalMehMethods = concat(methods, pmethods);
        for(Method method:totalMehMethods)
        {
            if(method.getName().contains("set"))
            {
                methodList.add(method);
                //System.out.println(method.getName());
            }

        }

        return methodList;
    }

    public List<String> getFieldNames()
    {
        List<String> fieldList = new ArrayList<String>();
        Field[] fields = this.getClass().getDeclaredFields();
        //Field[] fields2 = super.getClass().getDeclaredFields();
        //Class sc = Part.getClass().getSuperclass();
        Field[] fields2 = Part.class.getDeclaredFields();
        for(Field field:fields)
        {
            //System.out.println(this.getClass().getName() + "::"+ field.toString());

            fieldList.add(field.getName().toUpperCase());
        }

        for(Field field:fields2)
        {
            //String fullName = field.toString();
            //fullName = fullName.substring(fullName.lastIndexOf(".")+1);
            fieldList.add(field.getName().toUpperCase());
            //System.out.println(this.getClass().getName() + "::"+ field.toString());
        }

        return fieldList;
    }

    public void setValue(String key,String value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        //List<String> fieldNames = getFieldNames();
        List<Method> methods = getMethods();

        for(Method method:methods)
        {
            String methodName = method.getName();
            methodName = methodName.substring(3);
            if(key.equals(methodName.toUpperCase()))
            {
                method.invoke(this, value);
                break;
            }
        }


    }
}
