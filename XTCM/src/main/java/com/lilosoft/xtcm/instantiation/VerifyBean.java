package com.lilosoft.xtcm.instantiation;

import java.util.List;

/**
 * @category 单条待处理数据字段
 * @author William Liu
 *
 */
public class VerifyBean {

    private String InspectID;
    private String DISPATCHWARMINGTIME;
    private String CASEITEM;
    private String CASECODE;
    private String CASEDESCRIPTION;
    private String CASESOURCE;
    private String CASETITLE;
    private String SIGNTIME;
    private String GRIDCODE;
    private String CREATETIME;
    private String PUTONRECORDWARNINGTIME;
    private String PUTONRECORDTIME;
    private String PutonerdUser;
    private List<FileBean> ApproveFileList;

    public VerifyBean(String inspectid, String dispatchwarmingtime,
                      String caseitem, String casecode, String casedescription,
                      String casesource, String casetitle, String signtime,
                      String gridcode, String createtime, String putonrecordwarningtime,
                      String putonrecordtime, String putonerduser,
                      List<FileBean> approveFileList) {
        // TODO Auto-generated constructor stub
        InspectID = inspectid;
        DISPATCHWARMINGTIME = dispatchwarmingtime;
        CASEITEM = caseitem;
        CASECODE = casecode;
        CASEDESCRIPTION = casedescription;
        CASESOURCE = casesource;
        CASETITLE = casetitle;
        SIGNTIME = signtime;
        GRIDCODE = gridcode;
        CREATETIME = createtime;
        PUTONRECORDWARNINGTIME = putonrecordwarningtime;
        PUTONRECORDTIME = putonrecordtime;
        PutonerdUser = putonerduser;
        ApproveFileList = approveFileList;

    }

    public String getInspectID() {
        return InspectID;
    }

    public void setInspectID(String inspectID) {
        InspectID = inspectID;
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

    public String getPutonerdUser() {
        return PutonerdUser;
    }

    public void setPutonerdUser(String putonerdUser) {
        PutonerdUser = putonerdUser;
    }

    public List<FileBean> getApproveFileList() {
        return ApproveFileList;
    }

    public void setApproveFileList(List<FileBean> approveFileList) {
        ApproveFileList = approveFileList;
    }

}
