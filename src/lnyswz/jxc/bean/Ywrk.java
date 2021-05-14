package lnyswz.jxc.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购需求类
 * @author wangwy
 *
 */
public class Ywrk {
	
	private String ywrklsh;
	private int createId;
	private Date createTime;
	private String createName;
	private String bmbh;
	private String bmmc;
	private String gysbh;
	private String gysmc;
	private String rklxId;
	private String rklxmc;
	private String ckId;
	private String ckmc;
	private BigDecimal hjje;
	private BigDecimal fkje;
	private BigDecimal yfje;
	private String bz;
	private String cjYwrklsh;
	private String isCj;
	private int cjId;
	private Date cjTime;
	private String cjName;
	private String isZs;
	private String shdz;
	private String isDep;
	private String depId;
	private String depName;
	private String beYwrklsh;
	private Date fpDate;
	private String menuId;
	private String lxbh;
	
	private String search;
	
	private int id;
	private String spbh;
	private String spmc;
	private String spcd;
	private String sppp;
	private String spbz;
	private String zjldwmc;
	private String cjldwId;
	private String cjldwmc;
	private BigDecimal zdwsl;
	private BigDecimal cdwsl;
	private BigDecimal thsl;
	private BigDecimal cthsl;
	private BigDecimal zdwdj;
	private BigDecimal cdwdj;
	private BigDecimal spje;
	private BigDecimal fkedje;
	
	//前台传入明细json
	private String datagrid;
	private int page;
	private int rows;
	
	private String xskplsh;

	private String lsh;
	private String ywrklshs;
	private String kfrklshs;
	private String ywbtlsh;
	//来自采购计划直送的明细id
	private String cgjhDetIds;
	//由直送业务入库生成提货单时传入的参数
	private String ywrkDetIds;
	
	//--采购计划直送导入流水号
	private String cgjhlshs;
	private String fromOther;

	public String getYwrklsh() {
		return ywrklsh;
	}

	public void setYwrklsh(String ywrklsh) {
		this.ywrklsh = ywrklsh;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getRklxId() {
		return rklxId;
	}

	public void setRklxId(String rklxId) {
		this.rklxId = rklxId;
	}

	public String getRklxmc() {
		return rklxmc;
	}

	public void setRklxmc(String rklxmc) {
		this.rklxmc = rklxmc;
	}

	public BigDecimal getHjje() {
		return hjje;
	}

	public void setHjje(BigDecimal hjje) {
		this.hjje = hjje;
	}

	public BigDecimal getFkje() {
		return fkje;
	}

	public void setFkje(BigDecimal fkje) {
		this.fkje = fkje;
	}

	public BigDecimal getYfje() {
		return yfje;
	}

	public void setYfje(BigDecimal yfje) {
		this.yfje = yfje;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getCjYwrklsh() {
		return cjYwrklsh;
	}

	public void setCjYwrklsh(String cjYwrklsh) {
		this.cjYwrklsh = cjYwrklsh;
	}

	public String getIsCj() {
		return isCj;
	}

	public void setIsCj(String isCj) {
		this.isCj = isCj;
	}

	
	public int getCreateId() {
		return createId;
	}

	public void setCreateId(int createId) {
		this.createId = createId;
	}

	public Date getCjTime() {
		return cjTime;
	}

	public void setCjTime(Date cjTime) {
		this.cjTime = cjTime;
	}

	public int getCjId() {
		return cjId;
	}

	public void setCjId(int cjId) {
		this.cjId = cjId;
	}

	public String getCjName() {
		return cjName;
	}

	public void setCjName(String cjName) {
		this.cjName = cjName;
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

	public String getIsZs() {
		return isZs;
	}

	public void setIsZs(String isZs) {
		this.isZs = isZs;
	}

	public String getShdz() {
		return shdz;
	}

	public void setShdz(String shdz) {
		this.shdz = shdz;
	}

	public String getIsDep() {
		return isDep;
	}

	public void setIsDep(String isDep) {
		this.isDep = isDep;
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

	public String getBeYwrklsh() {
		return beYwrklsh;
	}

	public void setBeYwrklsh(String beYwrklsh) {
		this.beYwrklsh = beYwrklsh;
	}

	public Date getFpDate() {
		return fpDate;
	}

	public void setFpDate(Date fpDate) {
		this.fpDate = fpDate;
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

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSpbh() {
		return spbh;
	}

	public void setSpbh(String spbh) {
		this.spbh = spbh;
	}

	public String getSpmc() {
		return spmc;
	}

	public void setSpmc(String spmc) {
		this.spmc = spmc;
	}

	public String getSpcd() {
		return spcd;
	}

	public void setSpcd(String spcd) {
		this.spcd = spcd;
	}

	public String getSppp() {
		return sppp;
	}

	public void setSppp(String sppp) {
		this.sppp = sppp;
	}

	public String getSpbz() {
		return spbz;
	}

	public void setSpbz(String spbz) {
		this.spbz = spbz;
	}

	public String getZjldwmc() {
		return zjldwmc;
	}

	public void setZjldwmc(String zjldwmc) {
		this.zjldwmc = zjldwmc;
	}

	public String getCjldwId() {
		return cjldwId;
	}

	public void setCjldwId(String cjldwId) {
		this.cjldwId = cjldwId;
	}

	public String getCjldwmc() {
		return cjldwmc;
	}

	public void setCjldwmc(String cjldwmc) {
		this.cjldwmc = cjldwmc;
	}

	public BigDecimal getZdwsl() {
		return zdwsl;
	}

	public void setZdwsl(BigDecimal zdwsl) {
		this.zdwsl = zdwsl;
	}

	public BigDecimal getCdwsl() {
		return cdwsl;
	}

	public void setCdwsl(BigDecimal cdwsl) {
		this.cdwsl = cdwsl;
	}

	public BigDecimal getThsl() {
		return thsl;
	}

	public void setThsl(BigDecimal thsl) {
		this.thsl = thsl;
	}

	public BigDecimal getCthsl() {
		return cthsl;
	}

	public void setCthsl(BigDecimal cthsl) {
		this.cthsl = cthsl;
	}

	public BigDecimal getZdwdj() {
		return zdwdj;
	}

	public void setZdwdj(BigDecimal zdwdj) {
		this.zdwdj = zdwdj;
	}

	public BigDecimal getCdwdj() {
		return cdwdj;
	}

	public void setCdwdj(BigDecimal cdwdj) {
		this.cdwdj = cdwdj;
	}

	public BigDecimal getSpje() {
		return spje;
	}

	public void setSpje(BigDecimal spje) {
		this.spje = spje;
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

	public String getKfrklshs() {
		return kfrklshs;
	}

	public void setKfrklshs(String kfrklshs) {
		this.kfrklshs = kfrklshs;
	}

	public String getYwrklshs() {
		return ywrklshs;
	}

	public void setYwrklshs(String ywrklshs) {
		this.ywrklshs = ywrklshs;
	}

	public String getCgjhDetIds() {
		return cgjhDetIds;
	}

	public void setCgjhDetIds(String cgjhDetIds) {
		this.cgjhDetIds = cgjhDetIds;
	}

	public String getYwrkDetIds() {
		return ywrkDetIds;
	}

	public void setYwrkDetIds(String ywrkDetIds) {
		this.ywrkDetIds = ywrkDetIds;
	}

	public String getXskplsh() {
		return xskplsh;
	}

	public void setXskplsh(String xskplsh) {
		this.xskplsh = xskplsh;
	}
	
	public String getCgjhlshs() {
		return cgjhlshs;
	}

	public void setCgjhlshs(String cgjhlshs) {
		this.cgjhlshs = cgjhlshs;
	}

	public String getYwbtlsh() {
		return ywbtlsh;
	}

	public void setYwbtlsh(String ywbtlsh) {
		this.ywbtlsh = ywbtlsh;
	}

	public BigDecimal getFkedje() {
		return fkedje;
	}

	public void setFkedje(BigDecimal fkedje) {
		this.fkedje = fkedje;
	}

	public String getLsh() {
		return lsh;
	}

	public void setLsh(String lsh) {
		this.lsh = lsh;
	}
}
