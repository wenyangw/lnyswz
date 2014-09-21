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

}
