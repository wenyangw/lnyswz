package lnyswz.jxc.bean;

/**
 * 商品大类类
 * @author wangwy
 *
 */
public class Spdl {
	private String id;
	private String spdlmc;
	
	private String depIds;
	private String depNames;
	private String text;
	
	private int page;
	private int rows;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSpdlmc() {
		return spdlmc;
	}
	public void setSpdlmc(String spdlmc) {
		this.spdlmc = spdlmc;
	}
	public String getDepIds() {
		return depIds;
	}
	public void setDepIds(String depIds) {
		this.depIds = depIds;
	}
	public String getDepNames() {
		return depNames;
	}
	public void setDepNames(String depNames) {
		this.depNames = depNames;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
}
