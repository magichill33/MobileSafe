package com.lilosoft.xtcm.instantiation;

/**
 * @category 历史列表已核查字段
 * @author William Liu
 *
 */
public class HistoryExamineListBean {

    private String CASEID;
    private String INSPECTID;
    private String CASECODE;
    private String CASETITLE;
    private String CASESTATUS;
    private String CASEPARENTITEM1;
    private String CASEPARENTITEM2;
    private String CASEITEM;
    private String BEGINVERIFYTIME;
    private String VERIFYPERSON;
    private String ISPASSVERIFY;
    private String FINISHVERIFYTIME;
    private String CASEDESCRIPTION;//已核查描述

    public HistoryExamineListBean(String caseid, String inspectid,
                                  String casecode, String casetitle, String casestatus,
                                  String caseparentitem1, String caseparentitem2, String caseitem,
                                  String beginverifytime, String verifyperson, String ispassverify,
                                  String finishverifytime,String casedescription) {
        // TODO Auto-generated constructor stub
        this.CASEID = caseid;
        this.INSPECTID = inspectid;
        this.CASECODE = casecode;
        this.CASETITLE = casetitle;
        this.CASESTATUS = casestatus;
        this.CASEPARENTITEM1 = caseparentitem1;
        this.CASEPARENTITEM2 = caseparentitem2;
        this.CASEITEM = caseitem;
        this.BEGINVERIFYTIME = beginverifytime;
        this.VERIFYPERSON = verifyperson;
        this.ISPASSVERIFY = ispassverify;
        this.FINISHVERIFYTIME = finishverifytime;
        this.CASEDESCRIPTION=casedescription;
    }

    public String getCASEID() {
        return CASEID;
    }

    public void setCASEID(String cASEID) {
        CASEID = cASEID;
    }

    public String getINSPECTID() {
        return INSPECTID;
    }

    public void setINSPECTID(String iNSPECTID) {
        INSPECTID = iNSPECTID;
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

    public String getBEGINVERIFYTIME() {
        return BEGINVERIFYTIME;
    }

    public void setBEGINVERIFYTIME(String bEGINVERIFYTIME) {
        BEGINVERIFYTIME = bEGINVERIFYTIME;
    }

    public String getVERIFYPERSON() {
        return VERIFYPERSON;
    }

    public void setVERIFYPERSON(String vERIFYPERSON) {
        VERIFYPERSON = vERIFYPERSON;
    }

    public String getISPASSVERIFY() {
        return ISPASSVERIFY;
    }

    public void setISPASSVERIFY(String iSPASSVERIFY) {
        ISPASSVERIFY = iSPASSVERIFY;
    }

    public String getFINISHVERIFYTIME() {
        return FINISHVERIFYTIME;
    }

    public void setFINISHVERIFYTIME(String fINISHVERIFYTIME) {
        FINISHVERIFYTIME = fINISHVERIFYTIME;
    }

    public String getCASEDESCRIPTION() {
        return CASEDESCRIPTION;
    }

    public void setCASEDESCRIPTION(String cASEDESCRIPTION) {
        CASEDESCRIPTION = cASEDESCRIPTION;
    }

}
