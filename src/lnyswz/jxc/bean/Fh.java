package lnyswz.jxc.bean;

public class Fh {
	private String id;
	private String fhmc;
	private String depId;
	private String depName;
	private int page;
	private int rows;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFhmc() {
		return fhmc;
	}

	public void setFhmc(String fhmc) {
		this.fhmc = fhmc;
	}

	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}
	
	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
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
