package cn.ithm.kmplayer1.bean;

import java.util.List;

public class Slice {
	private String name;
	private String type;
	private String tag;

	private List<Hot> hot;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public List<Hot> getHot() {
		return hot;
	}

	public void setHot(List<Hot> hot) {
		this.hot = hot;
	}

	@Override
	public String toString() {
		return "Slice [name=" + name + ", type=" + type + ", tag=" + tag + ", hot=" + hot + "]";
	}

}
