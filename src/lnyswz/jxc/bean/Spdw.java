package lnyswz.jxc.bean;

/**
 * 商品段位类
 * @author wangwy
 *
 */
public class Spdw {
	private String id;
	private String spdwmc;
	private int splbId;
	private String splbmc;
	private String depId;
	private int page;
	private int rows;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSpdwmc() {
		return spdwmc;
	}
	public void setSpdwmc(String spdwmc) {
		this.spdwmc = spdwmc;
	}
	public int getSplbId() {
		return splbId;
	}
	public void setSplbId(int splbId) {
		this.splbId = splbId;
	}
	public String getSplbmc() {
		return splbmc;
	}
	public void setSplbmc(String splbmc) {
		this.splbmc = splbmc;
	}
	public String getDepId() {
		return depId;
	}
	public void setDepId(String depId) {
		this.depId = depId;
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
