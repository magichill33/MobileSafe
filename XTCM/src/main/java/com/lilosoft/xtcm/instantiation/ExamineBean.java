package com.lilosoft.xtcm.instantiation;

import java.util.List;

/**
 * @category 单条核查数据字段
 * @author William Liu
 * 
 */
public class ExamineBean {

	private String INSPECTID;
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
	private String DeptName;
	private String FEEDBACKDEALRESULT;
	private List<FileBean> ApproveFileList;
	private String DeptFileList;

	public ExamineBean(String inspectid, String dispatchwarmingtime,
			String caseitem, String casecode, String casedescription,
			String casesource, String casetitle, String signtime,
			String gridcode, String createtime, String putonrecordwarningtime,
			String putonrecordtime, String deptname, String feedbackdealresult,
			List<FileBean> approvefilelist, String deptfilelist) {

		INSPECTID = inspectid;
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
		DeptName = deptname;
		FEEDBACKDEALRESULT = feedbackdealresult;
		ApproveFileList = approvefilelist;
		DeptFileList = deptfilelist;

	}

	public String getINSPECTID() {
		return INSPECTID;
	}

	public void setINSPECTID(String iNSPECTID) {
		INSPECTID = iNSPECTID;
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

	public String getDeptFileList() {
		return DeptFileList;
	}

	public void setDeptFileList(String deptFileList) {
		DeptFileList = deptFileList;
	}

}
