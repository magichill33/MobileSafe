package com.lilosoft.xtcm.instantiation;

import java.util.ArrayList;
import java.util.List;

public class AdminApproveBean {

	private String ADTIVEID; // 审批编号
	private String ADTIVETITLE; // 审批标题
	private String ADTIVETYPE; // 审批类型
	private String ADTIVECONTENT; // 审批内容
	private String ADTIVEDATA; // 审批时间
	private String RN;
	private List<String> list = null;

	public AdminApproveBean(String adtiveid, // 审批编号
			String adtivetitle, // 审批标题
			String adtivetype, // 审批类型
			String adtivecontent, // 审批内容
			String adtivedata, // 审批时间
			String rn) {
		// TODO Auto-generated constructor stub
		this.ADTIVEID = adtiveid; // 审批编号
		this.ADTIVETITLE = adtivetitle; // 审批标题
		this.ADTIVETYPE = adtivetype; // 审批类型
		this.ADTIVECONTENT = adtivecontent; // 审批内容
		this.ADTIVEDATA = adtivedata;
		this.RN = rn;
		setList();
	}

	public String getADTIVEID() {
		return ADTIVEID;
	}

	public void setADTIVEID(String aDTIVEID) {
		ADTIVEID = aDTIVEID;
	}

	public String getADTIVETITLE() {
		return ADTIVETITLE;
	}

	public void setADTIVETITLE(String aDTIVETITLE) {
		ADTIVETITLE = aDTIVETITLE;
	}

	public String getADTIVETYPE() {
		return ADTIVETYPE;
	}

	public void setADTIVETYPE(String aDTIVETYPE) {
		ADTIVETYPE = aDTIVETYPE;
	}

	public String getADTIVECONTENT() {
		return ADTIVECONTENT;
	}

	public void setADTIVECONTENT(String aDTIVECONTENT) {
		ADTIVECONTENT = aDTIVECONTENT;
	}

	public String getADTIVEDATA() {
		return ADTIVEDATA;
	}

	public void setADTIVEDATA(String aDTIVEDATA) {
		ADTIVEDATA = aDTIVEDATA;
	}

	public String getRN() {
		return RN;
	}

	public void setRN(String rN) {
		RN = rN;
	}
	
	public List<String> getList() {
		return list;
	}

	public void setList() {

		if (null != ADTIVEID) {
			list = new ArrayList<String>();
//			list.add("审批编号:");
//			list.add(ADTIVEID);
			list.add("审批类型:");
			list.add(ADTIVETYPE);
			list.add("审批时间:");
			list.add(ADTIVEDATA);
			list.add("审批标题:");
			list.add(ADTIVETITLE);
			list.add("审批内容:");
			list.add(ADTIVECONTENT);
		}

	}

}
