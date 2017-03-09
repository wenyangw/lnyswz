package lnyswz.jxc.bean;

import java.util.List;


/**
 * 模块类
 * @author wangwy
 *
 */
public class Chart{
	private String title;
	private List<String> categories;
	private List<Serie> series;
	
	private String bmbh;
	private String field;
	private int year;
	private String includeNb;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<Serie> getSeries() {
		return series;
	}

	public void setSeries(List<Serie> series) {
		this.series = series;
	}

	public String getBmbh() {
		return bmbh;
	}

	public void setBmbh(String bmbh) {
		this.bmbh = bmbh;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getIncludeNb() {
		return includeNb;
	}

	public void setIncludeNb(String includeNb) {
		this.includeNb = includeNb;
	}

}
