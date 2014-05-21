package lnyswz.jxc.bean;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

public class Xsth {
	private String xsthlsh;
	private Date createTime;
	private int createId;
	private String createName;
	private String bmbh;
	private String bmmc;
	private String isSx;
	private String khbh;
	private String khmc;
	private String sh;
	private String khh;
	private String dzdh;
	private String ckId;
	private String ckmc;
	private String isFh;
	private String isFhth;
	private String fhId;
	private String fhmc;
	private int ywyId;
	private String ywymc;
	private String jsfsId;
	private String jsfsmc;
	private String thfs;
	private String shdz;
	private String thr;
	private String ch;
	private BigDecimal hjje;
	private BigDecimal hjsl;
	private String isLs;
	private String bookmc;
	private String bz;
	private String isZs;
	private String isCancel;
	private String cjXsthlsh;
	private Integer cancelId;
	private Date cancelTime;
	private String cancelName;
	private String locked;
	private Integer lockId;
	private Date lockTime;
	private String lockName;
	private String toFp;
	private String fromFp;
	private String isKp;
	private Integer kpId;
	private Date kpTime;
	private String kpName;
	private String isHk;
	private Integer hkId;
	private Date hkTime;
	private String needAudit;
	private String isAudit;
	
	private int id;
	private String spbh;
	private String spmc;
	private String spcd;
	private String sppp;
	private String spbz;
	private String zjldwId;
	private String zjldwmc;
	private String cjldwId;
	private String cjldwmc;
	private BigDecimal zhxs;
	private BigDecimal zdwsl;
	private BigDecimal cdwsl;
	private BigDecimal zdwdj;
	private BigDecimal cdwdj;
	private BigDecimal spje;
	private BigDecimal cksl;
	private BigDecimal kpsl;
	private String kfcklshs;
	
	private String search;
	
	private String xskplsh;
	private String xsthDetIds;
	private String xskpDetIds;
	
	//是否已开始提货，冲减销售提货单时提示
	private String isTh;
	
	private BigDecimal zdwthsl;
	private BigDecimal zdwytsl;
	
	//向销售开票流程导入的提货单号
	private String xsthlshs;
	
	private String lxbh;
	private String menuId;
	private int bgyId;
	
	//前台传入明细json
	private String datagrid;
	private String fromOther;
	private int page;
	private int rows;
	
	public String getXsthlsh() {
		return xsthlsh;
	}
	public void setXsthlsh(String xsthlsh) {
		this.xsthlsh = xsthlsh;
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
	public String getIsSx() {
		return isSx;
	}
	public void setIsSx(String isSx) {
		this.isSx = isSx;
	}
	public String getKhbh() {
		return khbh;
	}
	public void setKhbh(String khbh) {
		this.khbh = khbh;
	}
	public String getKhmc() {
		return khmc;
	}
	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}
	public String getSh() {
		return sh;
	}
	public void setSh(String sh) {
		this.sh = sh;
	}
	public String getKhh() {
		return khh;
	}
	public void setKhh(String khh) {
		this.khh = khh;
	}
	public String getDzdh() {
		return dzdh;
	}
	public void setDzdh(String dzdh) {
		this.dzdh = dzdh;
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
	public String getIsFh() {
		return isFh;
	}
	public void setIsFh(String isFh) {
		this.isFh = isFh;
	}
	public String getFhId() {
		return fhId;
	}
	public void setFhId(String fhId) {
		this.fhId = fhId;
	}
	public String getFhmc() {
		return fhmc;
	}
	public void setFhmc(String fhmc) {
		this.fhmc = fhmc;
	}
	public String getIsFhth() {
		return isFhth;
	}
	public void setIsFhth(String isFhth) {
		this.isFhth = isFhth;
	}
	public int getYwyId() {
		return ywyId;
	}
	public void setYwyId(int ywyId) {
		this.ywyId = ywyId;
	}
	public String getYwymc() {
		return ywymc;
	}
	public void setYwymc(String ywymc) {
		this.ywymc = ywymc;
	}
	public String getJsfsId() {
		return jsfsId;
	}
	public void setJsfsId(String jsfsId) {
		this.jsfsId = jsfsId;
	}
	public String getJsfsmc() {
		return jsfsmc;
	}
	public void setJsfsmc(String jsfsmc) {
		this.jsfsmc = jsfsmc;
	}
	public String getThfs() {
		return thfs;
	}
	public void setThfs(String thfs) {
		this.thfs = thfs;
	}
	public String getShdz() {
		return shdz;
	}
	public void setShdz(String shdz) {
		this.shdz = shdz;
	}
	public String getThr() {
		return thr;
	}
	public void setThr(String thr) {
		this.thr = thr;
	}
	public String getCh() {
		return ch;
	}
	public void setCh(String ch) {
		this.ch = ch;
	}
	public BigDecimal getHjje() {
		return hjje;
	}
	public void setHjje(BigDecimal hjje) {
		this.hjje = hjje;
	}
	public BigDecimal getHjsl() {
		return hjsl;
	}
	public void setHjsl(BigDecimal hjsl) {
		this.hjsl = hjsl;
	}
	public String getIsLs() {
		return isLs;
	}
	public void setIsLs(String isLs) {
		this.isLs = isLs;
	}
	public String getBookmc() {
		return bookmc;
	}
	public void setBookmc(String bookmc) {
		this.bookmc = bookmc;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getIsZs() {
		return isZs;
	}
	public void setIsZs(String isZs) {
		this.isZs = isZs;
	}
	public String getIsCancel() {
		return isCancel;
	}
	public void setIsCancel(String isCancel) {
		this.isCancel = isCancel;
	}
	public String getCjXsthlsh() {
		return cjXsthlsh;
	}
	public void setCjXsthlsh(String cjXsthlsh) {
		this.cjXsthlsh = cjXsthlsh;
	}
	public Integer getCancelId() {
		return cancelId;
	}
	public void setCancelId(Integer cancelId) {
		this.cancelId = cancelId;
	}
	public Date getCancelTime() {
		return cancelTime;
	}
	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}
	public String getCancelName() {
		return cancelName;
	}
	public void setCancelName(String cancelName) {
		this.cancelName = cancelName;
	}
	public String getLocked() {
		return locked;
	}
	public void setLocked(String locked) {
		this.locked = locked;
	}
	public Integer getLockId() {
		return lockId;
	}
	public void setLockId(Integer lockId) {
		this.lockId = lockId;
	}
	public Date getLockTime() {
		return lockTime;
	}
	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}
	public String getLockName() {
		return lockName;
	}
	public void setLockName(String lockName) {
		this.lockName = lockName;
	}
	public String getToFp() {
		return toFp;
	}
	public void setToFp(String toFp) {
		this.toFp = toFp;
	}
	public String getFromFp() {
		return fromFp;
	}
	public void setFromFp(String fromFp) {
		this.fromFp = fromFp;
	}
	public String getIsKp() {
		return isKp;
	}
	public void setIsKp(String isKp) {
		this.isKp = isKp;
	}
	public Integer getKpId() {
		return kpId;
	}
	public void setKpId(Integer kpId) {
		this.kpId = kpId;
	}
	public Date getKpTime() {
		return kpTime;
	}
	public void setKpTime(Date kpTime) {
		this.kpTime = kpTime;
	}
	public String getKpName() {
		return kpName;
	}
	public void setKpName(String kpName) {
		this.kpName = kpName;
	}
	public String getIsHk() {
		return isHk;
	}
	public void setIsHk(String isHk) {
		this.isHk = isHk;
	}
	public Integer getHkId() {
		return hkId;
	}
	public void setHkId(Integer hkId) {
		this.hkId = hkId;
	}
	public Date getHkTime() {
		return hkTime;
	}
	public void setHkTime(Date hkTime) {
		this.hkTime = hkTime;
	}
	public String getNeedAudit() {
		return needAudit;
	}
	public void setNeedAudit(String needAudit) {
		this.needAudit = needAudit;
	}
	public String getIsAudit() {
		return isAudit;
	}
	public void setIsAudit(String isAudit) {
		this.isAudit = isAudit;
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
	public String getZjldwId() {
		return zjldwId;
	}
	public void setZjldwId(String zjldwId) {
		this.zjldwId = zjldwId;
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
	public BigDecimal getZhxs() {
		return zhxs;
	}
	public void setZhxs(BigDecimal zhxs) {
		this.zhxs = zhxs;
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
	public BigDecimal getCksl() {
		return cksl;
	}
	public void setCksl(BigDecimal cksl) {
		this.cksl = cksl;
	}
	public BigDecimal getKpsl() {
		return kpsl;
	}
	public void setKpsl(BigDecimal kpsl) {
		this.kpsl = kpsl;
	}
	public String getKfcklshs() {
		return kfcklshs;
	}
	public void setKfcklshs(String kfcklshs) {
		this.kfcklshs = kfcklshs;
	}
	public String getIsTh() {
		return isTh;
	}
	public void setIsTh(String isTh) {
		this.isTh = isTh;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getXskplsh() {
		return xskplsh;
	}
	public void setXskplsh(String xskplsh) {
		this.xskplsh = xskplsh;
	}
	public String getXsthDetIds() {
		return xsthDetIds;
	}
	public void setXsthDetIds(String xsthDetIds) {
		this.xsthDetIds = xsthDetIds;
	}
	public String getXskpDetIds() {
		return xskpDetIds;
	}
	public void setXskpDetIds(String xskpDetIds) {
		this.xskpDetIds = xskpDetIds;
	}
	public BigDecimal getZdwthsl() {
		return zdwthsl;
	}
	public void setZdwthsl(BigDecimal zdwthsl) {
		this.zdwthsl = zdwthsl;
	}
	public BigDecimal getZdwytsl() {
		return zdwytsl;
	}
	public void setZdwytsl(BigDecimal zdwytsl) {
		this.zdwytsl = zdwytsl;
	}
	public String getXsthlshs() {
		return xsthlshs;
	}
	public void setXsthlshs(String xsthlshs) {
		this.xsthlshs = xsthlshs;
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
	public int getBgyId() {
		return bgyId;
	}
	public void setBgyId(int bgyId) {
		this.bgyId = bgyId;
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
