package lnyswz.jxc.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 业务补调类
 * @author wangwy
 *
 */
public class Ywbt {
	
	private String ywbtlsh;
	private int createId;
	private Date createTime;
	private String createName;
	private String bmbh;
	private String bmmc;
	private BigDecimal hjje;
	private String bz;
	private String ywrklsh;
	
	private String search;
	
	private String menuId;
	private String lxbh;
	
	private String spbh;
	//前台传入明细json
	private String datagrid;
	private int page;
	private int rows;
	
	private String fromOther;

	public String getYwbtlsh() {
		return ywbtlsh;
	}

	public void setYwbtlsh(String ywbtlsh) {
		this.ywbtlsh = ywbtlsh;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getCreateId() {
		return createId;
	}
	
	public void setCreateId(int createId) {
		this.createId = createId;
	}
	
	
	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getBmbh() {
		return bmbh;
	}
	
	public void setBmbh(String bmbh) {
		this.bmbh = bmbh;
	}
	
	public String getBmmc() {
		return bmmc;
	}
	
	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}
	
	public BigDecimal getHjje() {
		return hjje;
	}

	public void setHjje(BigDecimal hjje) {
		this.hjje = hjje;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getYwrklsh() {
		return ywrklsh;
	}

	public void setYwrklsh(String ywrklsh) {
		this.ywrklsh = ywrklsh;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getLxbh() {
		return lxbh;
	}

	public void setLxbh(String lxbh) {
		this.lxbh = lxbh;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getSpbh() {
		return spbh;
	}

	public void setSpbh(String spbh) {
		this.spbh = spbh;
	}

	public String getDatagrid() {
		return datagrid;
	}

	public void setDatagrid(String datagrid) {
		this.datagrid = datagrid;
	}

	public String getFromOther() {
		return fromOther;
	}

	public void setFromOther(String fromOther) {
		this.fromOther = fromOther;
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
