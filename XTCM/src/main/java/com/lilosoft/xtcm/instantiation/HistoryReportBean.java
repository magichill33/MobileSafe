package com.lilosoft.xtcm.instantiation;

import java.util.ArrayList;
import java.util.List;

/**
 * @category 历史已上报字段
 * @author William Liu
 *
 */
public class HistoryReportBean {

    private List<String> list = null;
    private String DISPATCHWARMINGTIME; // 派遣预警时间
    private String CASEITEM; // 获取案件的分类
    private String CASECODE; // 案件编号
    private String CASEDESCRIPTION; // 案件内容
    private String CASESOURCE; // 案件来源
    private String CASETITLE; // 案件简介
    private String SIGNTIME; // 签收时间
    private String GRIDCODE; // 网格编号
    private String CREATETIME; // 案件创建时间
    private String PUTONRECORDWARNINGTIME; // 案件预警时间
    private String PUTONRECORDTIME; // 立案时间
    private String DeptName; // 处置部门
    private String FEEDBACKDEALRESULT; // 反馈办理结果
    private List<FileBean> ApproveFileList;

    public HistoryReportBean(String dispatchwarmingtime, String caseitem,
                             String casecode, String casedescription, String casesource,
                             String casetitle, String signtime, String gridcode,
                             String createtime, String putonrecordwarningtime,
                             String putonrecordtime, String deptname, String feedbackdealresult,
                             List<FileBean> approvefilelist) {
        // TODO Auto-generated constructor stub
        this.DISPATCHWARMINGTIME = dispatchwarmingtime;
        this.CASEITEM = caseitem;
        this.CASECODE = casecode;
        this.CASEDESCRIPTION = casedescription;
        this.CASESOURCE = casesource;
        this.CASETITLE = casetitle;
        this.SIGNTIME = signtime;
        this.GRIDCODE = gridcode;
        this.CREATETIME = createtime;
        this.PUTONRECORDWARNINGTIME = putonrecordwarningtime;
        this.PUTONRECORDTIME = putonrecordtime;
        this.DeptName = deptname;
        this.FEEDBACKDEALRESULT = feedbackdealresult;
        this.ApproveFileList = approvefilelist;
        setList();

    }

    public List<String> getList() {
        return list;
    }

    public void setList() {

        if (null != CASECODE) {
            list = new ArrayList<String>();
            list.add("派遣预警时间:");
            list.add(DISPATCHWARMINGTIME);
            list.add("获取案件的分类:");
            list.add(CASEITEM);
            list.add("案件编号:");
            list.add(CASECODE);
            list.add("案件内容:");
            list.add(CASEDESCRIPTION);
            list.add("案件来源:");
            list.add(CASESOURCE);
            list.add("案件简介:");
            list.add(CASETITLE);
            list.add("签收时间:");
            list.add(SIGNTIME);
            list.add("网格编号:");
            list.add(GRIDCODE);
            list.add("案件创建时间:");
            list.add(CREATETIME);
            list.add("案件预警时间:");
            list.add(PUTONRECORDWARNINGTIME);
            list.add("立案时间:");
            list.add(PUTONRECORDTIME);
            list.add("处置部门:");
            list.add(DeptName);
            list.add("反馈办理结果:");
            list.add(FEEDBACKDEALRESULT);
        }

    }

    public String getDISPATCHWARMINGTIME() {
        return DISPATCHWARMINGTIME;
    }

    public void setDISPATCHWARMINGTIME(String dISPATCHWARMINGTIME) {
        DISPATCHWARMINGTIME = dISPATCHWARMINGTIME;
    }

    public String getCASEITEM() {
        return CASEITEM;
    }

    public void setCASEITEM(String cASEITEM) {
        CASEITEM = cASEITEM;
    }

    public String getCASECODE() {
        return CASECODE;
    }

    public void setCASECODE(String cASECODE) {
        CASECODE = cASECODE;
    }

    public String getCASEDESCRIPTION() {
        return CASEDESCRIPTION;
    }

    public void setCASEDESCRIPTION(String cASEDESCRIPTION) {
        CASEDESCRIPTION = cASEDESCRIPTION;
    }

    public String getCASESOURCE() {
        return CASESOURCE;
    }

    public void setCASESOURCE(String cASESOURCE) {
        CASESOURCE = cASESOURCE;
    }

    public String getCASETITLE() {
        return CASETITLE;
    }

    public void setCASETITLE(String cASETITLE) {
        CASETITLE = cASETITLE;
    }

    public String getSIGNTIME() {
        return SIGNTIME;
    }

    public void setSIGNTIME(String sIGNTIME) {
        SIGNTIME = sIGNTIME;
    }

    public String getGRIDCODE() {
        return GRIDCODE;
    }

    public void setGRIDCODE(String gRIDCODE) {
        GRIDCODE = gRIDCODE;
    }

    public String getCREATETIME() {
        return CREATETIME;
    }

    public void setCREATETIME(String cREATETIME) {
        CREATETIME = cREATETIME;
    }

    public String getPUTONRECORDWARNINGTIME() {
        return PUTONRECORDWARNINGTIME;
    }

    public void setPUTONRECORDWARNINGTIME(String pUTONRECORDWARNINGTIME) {
        PUTONRECORDWARNINGTIME = pUTONRECORDWARNINGTIME;
    }

    public String getPUTONRECORDTIME() {
        return PUTONRECORDTIME;
    }

    public void setPUTONRECORDTIME(String pUTONRECORDTIME) {
        PUTONRECORDTIME = pUTONRECORDTIME;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getFEEDBACKDEALRESULT() {
        return FEEDBACKDEALRESULT;
    }

    public void setFEEDBACKDEALRESULT(String fEEDBACKDEALRESULT) {
        FEEDBACKDEALRESULT = fEEDBACKDEALRESULT;
    }

    public List<FileBean> getApproveFileList() {
        return ApproveFileList;
    }

    public void setApproveFileList(List<FileBean> approveFileList) {
        ApproveFileList = approveFileList;
    }

}
