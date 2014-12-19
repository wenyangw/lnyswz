package lnyswz.jxc.bean;

import java.util.Date;


public class Kfrk{

	private String kfrklsh;
	private Date createTime;
	private int createId;
	private String createName;
	private String bmbh;
	private String bmmc;
	private String gysbh;
	private String gysmc;
	private String ckId;
	private String ckmc;
	private String shry;
	private String ch;
	private String zjh;
	private String bz;
	private String cjKfrklsh;
	private String isCj;
	private Date cjTime;
	private Integer cjId;
	private String cjName;
	
	private String search;
	
	private String cgjhlsh;
	private String ywrklsh;
	private String fromOther;
	private String kfrklshs;
	
	private String datagrid;
	//来自采购计划的明细id
	private String cgjhDetIds;
	//显示时，来自采购计划的流水号
	private String cgjhlshs;
	private String lxbh;
	private String menuId;
	
	private int page;
	private int rows;
	
	public Kfrk() {
	}

	public Kfrk(String kfrklsh, String gysbh, String gysmc, String shry, String ch, String zjh, String cgjhlsh, Date createTime,
			int createId, String bmbh, String bz, String isCj,
			Date cjTime, Integer cjId) {
		this.kfrklsh = kfrklsh;
		this.gysbh = gysbh;
		this.gysmc = gysmc;
		this.shry = shry;
		this.ch = ch;
		this.zjh = zjh;
		this.cgjhlsh = cgjhlsh;
		this.createTime = createTime;
		this.createId = createId;
		this.bmbh = bmbh;
		this.bz = bz;
		this.isCj = isCj;
		this.cjTime = cjTime;
		this.cjId = cjId;
	}

	public String getKfrklsh() {
		return this.kfrklsh;
	}

	public void setKfrklsh(String kfrklsh) {
		this.kfrklsh = kfrklsh;
	}

	public String getGysbh() {
		return gysbh;
	}

	public void setGysbh(String gysbh) {
		this.gysbh = gysbh;
	}

	public String getGysmc() {
		return gysmc;
	}

	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}

	
	public String getCkId() {
		return ckId;
	}

	public void setCkId(String ckId) {
		this.ckId = ckId;
	}

	public String getCkmc() {
		return ckmc;
	}

	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}

	public String getCgjhlsh() {
		return this.cgjhlsh;
	}

	public void setCgjhlsh(String cgjhlsh) {
		this.cgjhlsh = cgjhlsh;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getCreateId() {
		return this.createId;
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
		return this.bmbh;
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

	public String getShry() {
		return shry;
	}

	public void setShry(String shry) {
		this.shry = shry;
	}

	public String getCh() {
		return ch;
	}

	public void setCh(String ch) {
		this.ch = ch;
	}

	public String getZjh() {
		return zjh;
	}

	public void setZjh(String zjh) {
		this.zjh = zjh;
	}

	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getCjKfrklsh() {
		return cjKfrklsh;
	}

	public void setCjKfrklsh(String cjKfrklsh) {
		this.cjKfrklsh = cjKfrklsh;
	}

	public String getIsCj() {
		return this.isCj;
	}

	public void setIsCj(String isCj) {
		this.isCj = isCj;
	}

	public Date getCjTime() {
		return this.cjTime;
	}

	public void setCjTime(Date cjTime) {
		this.cjTime = cjTime;
	}

	public Integer getCjId() {
		return this.cjId;
	}

	public void setCjId(Integer cjId) {
		this.cjId = cjId;
	}

	public String getCjName() {
		return cjName;
	}

	public void setCjName(String cjName) {
		this.cjName = cjName;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getYwrklsh() {
		return ywrklsh;
	}

	public void setYwrklsh(String ywrklsh) {
		this.ywrklsh = ywrklsh;
	}

	public String getDatagrid() {
		return datagrid;
	}

	public void setDatagrid(String datagrid) {
		this.datagrid = datagrid;
	}

	public String getCgjhDetIds() {
		return cgjhDetIds;
	}

	public void setCgjhDetIds(String cgjhDetIds) {
		this.cgjhDetIds = cgjhDetIds;
	}

	public String getCgjhlshs() {
		return cgjhlshs;
	}

	public void setCgjhlshs(String cgjhlshs) {
		this.cgjhlshs = cgjhlshs;
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

	public String getFromOther() {
		return fromOther;
	}

	public void setFromOther(String fromOther) {
		this.fromOther = fromOther;
	}

	public String getKfrklshs() {
		return kfrklshs;
	}

	public void setKfrklshs(String kfrklshs) {
		this.kfrklshs = kfrklshs;
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
