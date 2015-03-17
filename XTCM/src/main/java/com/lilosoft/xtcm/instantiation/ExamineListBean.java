package com.lilosoft.xtcm.instantiation;

/**
 * @category 列表核查字段
 * @author William Liu
 *
 */
public class ExamineListBean {

    private String INSPECTID;// --1 InspectID 上传字段
    // --2 content VerifyeedBackContent 上传字段
    private String CASEID;
    private String CASECODE; // --3 CaseCode 上传字段
    private String CASETITLE; // 显示字段
    private String CASESTATUS;
    private String CASEPARENTITEM1;
    private String CASEPARENTITEM2;
    private String CASEITEM;
    private String PUTONRECORDWARNINGTIME; // 显示字段
    private String PUTONRECORDEXTENDEDTIME;
    private String PUTONRECORDUSERID; // --4 LoginName 上传字段
    private String BEGINVERIFYTIME;

    public ExamineListBean(String inspectid, String caseid, String casecode,
                           String casetitle, String casestatus, String caseparentitem1,
                           String caseparentitem2, String caseitem,
                           String putonrecordwarningtime, String putonrecordextendedtime,
                           String putonrecorduserid, String beginverifytime) {
        // TODO Auto-generated constructor stub
        INSPECTID = inspectid;
        CASEID = caseid;
        CASECODE = casecode;
        CASETITLE = casetitle;
        CASESTATUS = casestatus;
        CASEPARENTITEM1 = caseparentitem1;
        CASEPARENTITEM2 = caseparentitem2;
        CASEITEM = caseitem;
        PUTONRECORDWARNINGTIME = putonrecordwarningtime;
        PUTONRECORDEXTENDEDTIME = putonrecordextendedtime;
        PUTONRECORDUSERID = putonrecorduserid;
        BEGINVERIFYTIME = beginverifytime;

    }

    public String getINSPECTID() {
        return INSPECTID;
    }

    public void setINSPECTID(String iNSPECTID) {
        INSPECTID = iNSPECTID;
    }

    public String getCASEID() {
        return CASEID;
    }

    public void setCASEID(String cASEID) {
        CASEID = cASEID;
    }

    public String getCASECODE() {
        return CASECODE;
    }

    public void setCASECODE(String cASECODE) {
        CASECODE = cASECODE;
    }

    public String getCASETITLE() {
        return CASETITLE;
    }

    public void setCASETITLE(String cASETITLE) {
        CASETITLE = cASETITLE;
    }

    public String getCASESTATUS() {
        return CASESTATUS;
    }

    public void setCASESTATUS(String cASESTATUS) {
        CASESTATUS = cASESTATUS;
    }

    public String getCASEPARENTITEM1() {
        return CASEPARENTITEM1;
    }

    public void setCASEPARENTITEM1(String cASEPARENTITEM1) {
        CASEPARENTITEM1 = cASEPARENTITEM1;
    }

    public String getCASEPARENTITEM2() {
        return CASEPARENTITEM2;
    }

    public void setCASEPARENTITEM2(String cASEPARENTITEM2) {
        CASEPARENTITEM2 = cASEPARENTITEM2;
    }

    public String getCASEITEM() {
        return CASEITEM;
    }

    public void setCASEITEM(String cASEITEM) {
        CASEITEM = cASEITEM;
    }

    public String getPUTONRECORDWARNINGTIME() {
        return PUTONRECORDWARNINGTIME;
    }

    public void setPUTONRECORDWARNINGTIME(String pUTONRECORDWARNINGTIME) {
        PUTONRECORDWARNINGTIME = pUTONRECORDWARNINGTIME;
    }

    public String getPUTONRECORDEXTENDEDTIME() {
        return PUTONRECORDEXTENDEDTIME;
    }

    public void setPUTONRECORDEXTENDEDTIME(String pUTONRECORDEXTENDEDTIME) {
        PUTONRECORDEXTENDEDTIME = pUTONRECORDEXTENDEDTIME;
    }

    public String getPUTONRECORDUSERID() {
        return PUTONRECORDUSERID;
    }

    public void setPUTONRECORDUSERID(String pUTONRECORDUSERID) {
        PUTONRECORDUSERID = pUTONRECORDUSERID;
    }

    public String getBEGINVERIFYTIME() {
        return BEGINVERIFYTIME;
    }

    public void setBEGINVERIFYTIME(String bEGINVERIFYTIME) {
        BEGINVERIFYTIME = bEGINVERIFYTIME;
    }

}
