package com.lilosoft.xtcm.instantiation;

/**
 * @category 待核查数据
 * @author William Liu
 *
 */
public class ReadyDisposeBean {

    private String CASEID;
    private String TASKID; // -- 2 上传字段
    private String SIGNID;
    private String HANDLEID; // -- 3 上传字段
    private String CASECODE; // -- 1 上传字段
    // LoginName -- 4 上传字段
    private String CASETITLE; // 显示字段
    private String CASESTATUS;
    private String CASEPARENTITEM1;
    private String CASEPARENTITEM2;
    private String CASEITEM;
    private String DISPATCHTIME;
    private String DEALWARMINGTIME; // 显示字段
    private String DEALEXTENDEDTIME;
    private String DISPATCHTYPE;
    private String DISPATCHPERSONID;
    private String CASEDESCRIPTION;

    public ReadyDisposeBean(String caseid, String taskid, String signid,
                            String handleid, String casecode, String casetitle,
                            String casestatus, String caseparentitem1, String caseparentitem2,
                            String caseitem, String dispatchtime, String dealwarmingtime,
                            String dealextendedtime, String dispatchtype,
                            String dispatchpersonid,String casedescription) {
        // TODO Auto-generated constructor stub
        CASEID = caseid;
        TASKID = taskid;
        SIGNID = signid;
        HANDLEID = handleid;
        CASECODE = casecode;
        CASETITLE = casetitle;
        CASESTATUS = casestatus;
        CASEPARENTITEM1 = caseparentitem1;
        CASEPARENTITEM2 = caseparentitem2;
        CASEITEM = caseitem;
        DISPATCHTIME = dispatchtime;
        DEALWARMINGTIME = dealwarmingtime;
        DEALEXTENDEDTIME = dealextendedtime;
        DISPATCHTYPE = dispatchtype;
        DISPATCHPERSONID = dispatchpersonid;
        CASEDESCRIPTION=casedescription;
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

    public String getDEALWARMINGTIME() {
        return DEALWARMINGTIME;
    }

    public void setDEALWARMINGTIME(String dEALWARMINGTIME) {
        DEALWARMINGTIME = dEALWARMINGTIME;
    }

    public String getDEALEXTENDEDTIME() {
        return DEALEXTENDEDTIME;
    }

    public void setDEALEXTENDEDTIME(String dEALEXTENDEDTIME) {
        DEALEXTENDEDTIME = dEALEXTENDEDTIME;
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

    public String getCASEDESCRIPTION() {
        return CASEDESCRIPTION;
    }

    public void setCASEDESCRIPTION(String cASEDESCRIPTION) {
        CASEDESCRIPTION = cASEDESCRIPTION;
    }

}
