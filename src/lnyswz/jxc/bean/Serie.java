package lnyswz.jxc.bean;

import java.math.BigDecimal;
import java.util.List;

public class Serie {
	private String name;
	private List<BigDecimal> data;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<BigDecimal> getData() {
		return data;
	}
	public void setData(List<BigDecimal> data) {
		this.data = data;
	}
	
}