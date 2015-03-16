package com.lilosoft.xtcm.instantiation;

/**
 * @category 列表待处理字段
 * @author William Liu
 * 
 */
public class DisposeListBean {

	private String CASEID; // -- 上传字段
	private String TASKID;
	private String SIGNID;
	private String HANDLEID;
	private String CASECODE;
	private String CASETITLE;  //--1  显示
	private String CASESTATUS;
	private String CASEPARENTITEM1;
	private String CASEPARENTITEM2;
	private String CASEITEM;
	private String DISPATCHTIME;
	private String DEALWARMINGTIME;  // --2 显示
	private String DEALEXTENDEDTIME;
	private String DISPATCHTYPE;
	private String DISPATCHPERSONID;

	public DisposeListBean(String CASEID, String TASKID, String SIGNID,
			String HANDLEID, String CASECODE, String CASETITLE,
			String CASESTATUS, String CASEPARENTITEM1, String CASEPARENTITEM2,
			String CASEITEM, String DISPATCHTIME, String DEALWARMINGTIME,
			String DEALEXTENDEDTIME, String DISPATCHTYPE,
			String DISPATCHPERSONID) {
		// TODO Auto-generated constructor stub
		this.CASEID = CASEID;
		this.TASKID = TASKID;
		this.SIGNID = SIGNID;
		this.HANDLEID = HANDLEID;
		this.CASECODE = CASECODE;
		this.CASETITLE = CASETITLE;
		this.CASESTATUS = CASESTATUS;
		this.CASEPARENTITEM1 = CASEPARENTITEM1;
		this.CASEPARENTITEM2 = CASEPARENTITEM2;
		this.CASEITEM = CASEITEM;
		this.DISPATCHTIME = DISPATCHTIME;
		this.DEALWARMINGTIME = DEALWARMINGTIME;
		this.DEALEXTENDEDTIME = DEALEXTENDEDTIME;
		this.DISPATCHTYPE = DISPATCHTYPE;
		this.DISPATCHPERSONID = DISPATCHPERSONID;

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
}
