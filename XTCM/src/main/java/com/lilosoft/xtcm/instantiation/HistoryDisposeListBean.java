package com.lilosoft.xtcm.instantiation;

/**
 * @category 历史列表已办理字段
 * @author William Liu
 *
 */
public class HistoryDisposeListBean {

    private String CASEID;
    private String TASKID;
    private String SIGNID;
    private String HANDLEID;
    private String CASECODE;
    private String CASETITLE;
    private String CASESTATUS;
    private String CASEPARENTITEM1;
    private String CASEPARENTITEM2;
    private String CASEITEM;
    private String DISPATCHTIME;
    private String DISPATCHTYPE;
    private String DISPATCHPERSONID;
    private String FEEDBACKDEALTIME;
    private String RN;
    private String CASEDESCRIPTION;


    public HistoryDisposeListBean(String caseid, String taskid, String signid,
                                  String handleid, String casecode, String casetitle,
                                  String casestatus, String caseparentitem1, String caseparentitem2,
                                  String caseitem, String dispatchtime, String dispatchtype,
                                  String dispatchpersonid, String feedbackdealtime, String rn,String casedescription) {
        // TODO Auto-generated constructor stub
        this.CASEID = caseid;
        this.TASKID = taskid;
        this.SIGNID = signid;
        this.HANDLEID = handleid;
        this.CASECODE = casecode;
        this.CASETITLE = casetitle;
        this.CASESTATUS = casestatus;
        this.CASEPARENTITEM1 = caseparentitem1;
        this.CASEPARENTITEM2 = caseparentitem2;
        this.CASEITEM = caseitem;
        this.DISPATCHTIME = dispatchtime;
        this.DISPATCHTYPE = dispatchtype;
        this.DISPATCHPERSONID = dispatchpersonid;
        this.FEEDBACKDEALTIME = feedbackdealtime;
        this.RN = rn;
        this.CASEDESCRIPTION=casedescription;

    }

    public String getCASEID() {
        return CASEID;
    }

    public void setCASEID(String cASEID) {
        CASEID = cASEID;
    }

    public String getTASKID() {
        return TASKID;
    }

    public void setTASKID(String tASKID) {
        TASKID = tASKID;
    }

    public String getSIGNID() {
        return SIGNID;
    }

    public void setSIGNID(String sIGNID) {
        SIGNID = sIGNID;
    }

    public String getHANDLEID() {
        return HANDLEID;
    }

    public void setHANDLEID(String hANDLEID) {
        HANDLEID = hANDLEID;
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

    public String getDISPATCHTIME() {
        return DISPATCHTIME;
    }

    public void setDISPATCHTIME(String dISPATCHTIME) {
        DISPATCHTIME = dISPATCHTIME;
    }

    public String getDISPATCHTYPE() {
        return DISPATCHTYPE;
    }

    public void setDISPATCHTYPE(String dISPATCHTYPE) {
        DISPATCHTYPE = dISPATCHTYPE;
    }

    public String getDISPATCHPERSONID() {
        return DISPATCHPERSONID;
    }

    public void setDISPATCHPERSONID(String dISPATCHPERSONID) {
        DISPATCHPERSONID = dISPATCHPERSONID;
    }

    public String getFEEDBACKDEALTIME() {
        return FEEDBACKDEALTIME;
    }

    public void setFEEDBACKDEALTIME(String fEEDBACKDEALTIME) {
        FEEDBACKDEALTIME = fEEDBACKDEALTIME;
    }

    public String getRN() {
        return RN;
    }

    public void setRN(String rN) {
        RN = rN;
    }

    public String getCASEDESCRIPTION() {
        return CASEDESCRIPTION;
    }

    public void setCASEDESCRIPTION(String cASEDESCRIPTION) {
        CASEDESCRIPTION = cASEDESCRIPTION;
    }

}
