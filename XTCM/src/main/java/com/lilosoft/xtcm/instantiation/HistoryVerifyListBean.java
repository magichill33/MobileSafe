package com.lilosoft.xtcm.instantiation;

/**
 * @category 历史列表已核实字段
 * @author William Liu
 *
 */
public class HistoryVerifyListBean {

    private String CASEITEM;
    private String VERIFYEXTENDEDTIME;
    private String CASESTATUS;
    private String RN;
    private String INSPECTID;
    private String SIGNID;
    private String CASEID;
    private String DEALUSERID;
    private String VERIFYWARNINGTIME;
    private String VERIFYPERSON;
    private String CASEPARENTITEM1;
    private String CASEPARENTITEM2;
    private String VERIFICATIONID;
    private String CASECODE;
    private String HANDLEID;
    private String SIGNDEPTCODE;
    private String CASETITLE;
    private String TASKID;
    private String FINISHVERIFYTIME;
    private String FEEDBACKDEALTIME;
    private String CASEDESCRIPTION;// 已核实内容

    public HistoryVerifyListBean(String caseitem, String verifyextendedtime,
                                 String casestatus, String rn, String inspectid, String signid,
                                 String caseid, String dealuserid, String verifywarningtime,
                                 String verifyperson, String caseparentitem1,
                                 String caseparentitem2, String verificationid, String casecode,
                                 String handleid, String signdeptcode, String casetitle,
                                 String taskid, String finishverifytime, String feedbackdealtime,
                                 String casedescription) {
        // TODO Auto-generated constructor stub
        this.CASEITEM = caseitem;
        this.VERIFYEXTENDEDTIME = verifyextendedtime;
        this.CASESTATUS = casestatus;
        this.RN = rn;
        this.INSPECTID = inspectid;
        this.SIGNID = signid;
        this.CASEID = caseid;
        this.DEALUSERID = dealuserid;
        this.VERIFYWARNINGTIME = verifywarningtime;
        this.VERIFYPERSON = verifyperson;
        this.CASEPARENTITEM1 = caseparentitem1;
        this.CASEPARENTITEM2 = caseparentitem2;
        this.VERIFICATIONID = verificationid;
        this.CASECODE = casecode;
        this.HANDLEID = handleid;
        this.SIGNDEPTCODE = signdeptcode;
        this.CASETITLE = casetitle;
        this.TASKID = taskid;
        this.FINISHVERIFYTIME = finishverifytime;
        this.FEEDBACKDEALTIME = feedbackdealtime;
        this.CASEDESCRIPTION = casedescription;
    }

    public String getASEITEM() {
        return CASEITEM;
    }

    public String getVERIFYEXTENDEDTIME() {
        return VERIFYEXTENDEDTIME;
    }

    public void setVERIFYEXTENDEDTIME(String vERIFYEXTENDEDTIME) {
        VERIFYEXTENDEDTIME = vERIFYEXTENDEDTIME;
    }

    public String getCASESTATUS() {
        return CASESTATUS;
    }

    public void setCASESTATUS(String cASESTATUS) {
        CASESTATUS = cASESTATUS;
    }

    public String getRN() {
        return RN;
    }

    public void setRN(String rN) {
        RN = rN;
    }

    public String getINSPECTID() {
        return INSPECTID;
    }

    public void setINSPECTID(String iNSPECTID) {
        INSPECTID = iNSPECTID;
    }

    public String getSIGNID() {
        return SIGNID;
    }

    public void setSIGNID(String sIGNID) {
        SIGNID = sIGNID;
    }

    public String getCASEID() {
        return CASEID;
    }

    public void setCASEID(String cASEID) {
        CASEID = cASEID;
    }

    public String getDEALUSERID() {
        return DEALUSERID;
    }

    public void setDEALUSERID(String dEALUSERID) {
        DEALUSERID = dEALUSERID;
    }

    public String getVERIFYWARNINGTIME() {
        return VERIFYWARNINGTIME;
    }

    public void setVERIFYWARNINGTIME(String vERIFYWARNINGTIME) {
        VERIFYWARNINGTIME = vERIFYWARNINGTIME;
    }

    public String getVERIFYPERSON() {
        return VERIFYPERSON;
    }

    public void setVERIFYPERSON(String vERIFYPERSON) {
        VERIFYPERSON = vERIFYPERSON;
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

    public String getVERIFICATIONID() {
        return VERIFICATIONID;
    }

    public void setVERIFICATIONID(String vERIFICATIONID) {
        VERIFICATIONID = vERIFICATIONID;
    }

    public String getCASECODE() {
        return CASECODE;
    }

    public void setCASECODE(String cASECODE) {
        CASECODE = cASECODE;
    }

    public String getHANDLEID() {
        return HANDLEID;
    }

    public void setHANDLEID(String hANDLEID) {
        HANDLEID = hANDLEID;
    }

    public String getSIGNDEPTCODE() {
        return SIGNDEPTCODE;
    }

    public void setSIGNDEPTCODE(String sIGNDEPTCODE) {
        SIGNDEPTCODE = sIGNDEPTCODE;
    }

    public String getCASETITLE() {
        return CASETITLE;
    }

    public void setCASETITLE(String cASETITLE) {
        CASETITLE = cASETITLE;
    }

    public String getTASKID() {
        return TASKID;
    }

    public void setTASKID(String tASKID) {
        TASKID = tASKID;
    }

    public String getFINISHVERIFYTIME() {
        return FINISHVERIFYTIME;
    }

    public void setFINISHVERIFYTIME(String fINISHVERIFYTIME) {
        FINISHVERIFYTIME = fINISHVERIFYTIME;
    }

    public String getFEEDBACKDEALTIME() {
        return FEEDBACKDEALTIME;
    }

    public void setFEEDBACKDEALTIME(String fEEDBACKDEALTIME) {
        FEEDBACKDEALTIME = fEEDBACKDEALTIME;
    }

    public String getCASEDESCRIPTION() {
        return CASEDESCRIPTION;
    }

    public void setCASEDESCRIPTION(String cASEDESCRIPTION) {
        CASEDESCRIPTION = cASEDESCRIPTION;
    }

    public String getCASEITEM() {
        return CASEITEM;
    }

    public void setCASEITEM(String cASEITEM) {
        CASEITEM = cASEITEM;
    }

}
