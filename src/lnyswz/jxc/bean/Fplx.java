package lnyswz.jxc.bean;

public class Fplx {
	private String id;
	private String fplxmc;
	private int page;
	private int rows;
	private String depId;
	private String menuId;
	private int userId;

	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFplxmc() {
		return fplxmc;
	}

	public void setFplxmc(String fplxmc) {
		this.fplxmc = fplxmc;
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
