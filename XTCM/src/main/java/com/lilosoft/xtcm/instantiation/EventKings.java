package com.lilosoft.xtcm.instantiation;

/**
 * 事件分类实体
 * 
 * @author yzy
 * 
 */
public class EventKings {
	private String id;// 事件ID
	private String parentid;// 事件父节点ID
	private String mc;// 事件名称
	private String code;
	private String layer;

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getMc() {
		return mc;
	}

	public void setMc(String mc) {
		this.mc = mc;
	}

}
