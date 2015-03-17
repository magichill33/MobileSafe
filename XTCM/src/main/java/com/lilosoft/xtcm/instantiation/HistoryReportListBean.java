package com.lilosoft.xtcm.instantiation;

/**
 * @category 历史列表已上报字段
 * @author William Liu
 *
 */
public class HistoryReportListBean {

    private String ID;
    private String CASECODE;
    private String CASETITLE;
    private String CASESTATUS;
    private String CASEPARENTITEM1;
    private String CASEPARENTITEM2;
    private String CASEITEM;
    private String CREATETIME;
    private String COMPLAINANTERID;
    private String CASEDESCRIPTION;//上报描述

    public HistoryReportListBean(String id, String casecode, String casetitle,
                                 String casestatus, String caseparentitem1, String caseparentitem2,
                                 String caseitem, String createtime, String complainanterid,String caseDescription) {
        // TODO Auto-generated constructor stub
        this.ID = id;
        this.CASECODE = casecode;
        this.CASETITLE = casetitle;
        this.CASESTATUS = casestatus;
        this.CASEPARENTITEM1 = caseparentitem1;
        this.CASEPARENTITEM2 = caseparentitem2;
        this.CASEITEM = caseitem;
        this.CREATETIME = createtime;
        this.COMPLAINANTERID = complainanterid;
        this.CASEDESCRIPTION = caseDescription;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
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

    public String getCREATETIME() {
        return CREATETIME;
    }

    public void setCREATETIME(String cREATETIME) {
        CREATETIME = cREATETIME;
    }

    public String getCOMPLAINANTERID() {
        return COMPLAINANTERID;
    }

    public void setCOMPLAINANTERID(String cOMPLAINANTERID) {
        COMPLAINANTERID = cOMPLAINANTERID;
    }

    public String getCASEDESCRIPTION() {
        return CASEDESCRIPTION;
    }

    public void setCASEDESCRIPTION(String cASEDESCRIPTION) {
        CASEDESCRIPTION = cASEDESCRIPTION;
    }

}
