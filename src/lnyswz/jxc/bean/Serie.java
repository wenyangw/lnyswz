package lnyswz.jxc.bean;

import java.math.BigDecimal;
import java.util.List;

public class Serie {
	private String name;
	private List<Object> cate;
	private List<Object> data;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Object> getCate() {
		return cate;
	}
	public void setCate(List<Object> cate) {
		this.cate = cate;
	}
	public List<Object> getData() {
		return data;
	}
	public void setData(List<Object> data) {
		this.data = data;
	}
	
}
