package lnyswz.jxc.bean;

/**
 * 商品类别类
 * @author wangwy
 *
 */
public class Splb {
	private int id;
	private String splbmc;
	private String idFrom;
	private String idTo;
	private String spdlId;
	private String spdlmc;
	private String ids;
	private String depId;
	
	private int page;
	private int rows;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSplbmc() {
		return splbmc;
	}
	public void setSplbmc(String splbmc) {
		this.splbmc = splbmc;
	}
	public String getIdFrom() {
		return idFrom;
	}
	public void setIdFrom(String idFrom) {
		this.idFrom = idFrom;
	}
	public String getIdTo() {
		return idTo;
	}
	public void setIdTo(String idTo) {
		this.idTo = idTo;
	}
	public String getSpdlId() {
		return spdlId;
	}
	public void setSpdlId(String spdlId) {
		this.spdlId = spdlId;
	}
	public String getSpdlmc() {
		return spdlmc;
	}
	public void setSpdlmc(String spdlmc) {
		this.spdlmc = spdlmc;
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
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getDepId() {
		return depId;
	}
	public void setDepId(String depId) {
		this.depId = depId;
	}
}
