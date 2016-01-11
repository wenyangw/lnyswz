package lnyswz.jxc.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lnyswz.jxc.model.TXskpDet;

/**
 * 采购需求类
 * @author wangwy
 *
 */
public class Xskp {
	
	private String xskplsh;
	private Date createTime;
	private int createId;
	private String createName;
	private String bmbh;
	private String bmmc;
	private String khbh;
	private String khmc;
	private String dzdh;
	private String khh;
	private String sh;
	private String ckId;
	private String ckmc;
	private String fhId;
	private String fhmc;
	private String jsfsId;
	private String jsfsmc;
	private String fplxId;
	private String fplxmc;
	private String bookmc;
	private String fyr;
	private String thfs;
	private String shdz;
	private String thr;
	private String ch;
	private int ywyId;
	private String ywymc;
	private BigDecimal hjje;
	private BigDecimal hjse;
	private BigDecimal hkje;
	private BigDecimal yfje;
	private String bz;
	private String fromTh;
	private String isCj;
	private Integer cjId;
	private String cjName;
	private Date cjTime;
	private String cjXskplsh;
	private String isTh;
	private Date thTime;
	private String thdlsh;
	private String isHk;
	private Date hkTime;
	private String isSx;
	private String isZs;
	private String isFh;
	
	//det字段
	private int id;
	private String spmc;
	private String spcd;
	private String sppp;
	private String spbz;
	private BigDecimal zhxs;
	private String zjldwId;
	private String zjldwmc;
	private String cjldwId;
	private String cjldwmc;
	private BigDecimal zdwsl;
	private BigDecimal cdwsl;
	private BigDecimal zdwdj;
	private BigDecimal cdwdj;
	private BigDecimal spje;
	private BigDecimal spse;
	private BigDecimal xscb;
	private BigDecimal lastThsl;
	private BigDecimal thsl;
	//前台传入明细json
	private String datagrid;
	
	private String search;
	private Date payTime;
	private BigDecimal hkedje;
	private String isUp;
	private int postponeDay;
	
//	private String xsthKhbh;
//	private int xsthYwyId;
	
	private String ywrklsh;
	private String xsthlshs;
	private String xsthDetIds;
	private String xskpDetIds;
	private String needXsth;
	private String spbh;
	private int page;
	private int rows;
	
	private String lxbh; 
	private String menuId;

	private String otherBm;
	private String fromOther;

	public String getXskplsh() {
		return xskplsh;
	}

	public void setXskplsh(String xskplsh) {
		this.xskplsh = xskplsh;
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

	public String getDzdh() {
		return dzdh;
	}

	public void setDzdh(String dzdh) {
		this.dzdh = dzdh;
	}

	public String getKhh() {
		return khh;
	}

	public void setKhh(String khh) {
		this.khh = khh;
	}

	public String getSh() {
		return sh;
	}

	public void setSh(String sh) {
		this.sh = sh;
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

	public String getFplxId() {
		return fplxId;
	}

	public void setFplxId(String fplxId) {
		this.fplxId = fplxId;
	}

	public String getFplxmc() {
		return fplxmc;
	}

	public void setFplxmc(String fplxmc) {
		this.fplxmc = fplxmc;
	}

	public String getBookmc() {
		return bookmc;
	}

	public void setBookmc(String bookmc) {
		this.bookmc = bookmc;
	}

	public String getFyr() {
		return fyr;
	}

	public void setFyr(String fyr) {
		this.fyr = fyr;
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

	public BigDecimal getHjje() {
		return hjje;
	}

	public void setHjje(BigDecimal hjje) {
		this.hjje = hjje;
	}

	public BigDecimal getHjse() {
		return hjse;
	}

	public void setHjse(BigDecimal hjse) {
		this.hjse = hjse;
	}

	public BigDecimal getHkje() {
		return hkje;
	}

	public void setHkje(BigDecimal hkje) {
		this.hkje = hkje;
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

	public String getFromTh() {
		return fromTh;
	}

	public void setFromTh(String fromTh) {
		this.fromTh = fromTh;
	}

	public String getIsCj() {
		return isCj;
	}

	public void setIsCj(String isCj) {
		this.isCj = isCj;
	}

	public Integer getCjId() {
		return cjId;
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

	public Date getCjTime() {
		return cjTime;
	}

	public void setCjTime(Date cjTime) {
		this.cjTime = cjTime;
	}

	public String getCjXskplsh() {
		return cjXskplsh;
	}

	public void setCjXskplsh(String cjXskplsh) {
		this.cjXskplsh = cjXskplsh;
	}

	public String getIsTh() {
		return isTh;
	}

	public void setIsTh(String isTh) {
		this.isTh = isTh;
	}

	public Date getThTime() {
		return thTime;
	}

	public void setThTime(Date thTime) {
		this.thTime = thTime;
	}

	public String getThdlsh() {
		return thdlsh;
	}

	public void setThdlsh(String thdlsh) {
		this.thdlsh = thdlsh;
	}

	public String getIsHk() {
		return isHk;
	}

	public void setIsHk(String isHk) {
		this.isHk = isHk;
	}

	public Date getHkTime() {
		return hkTime;
	}

	public void setHkTime(Date hkTime) {
		this.hkTime = hkTime;
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

	public String getIsSx() {
		return isSx;
	}

	public void setIsSx(String isSx) {
		this.isSx = isSx;
	}

	public String getIsZs() {
		return isZs;
	}

	public void setIsZs(String isZs) {
		this.isZs = isZs;
	}

	public String getIsFh() {
		return isFh;
	}

	public void setIsFh(String isFh) {
		this.isFh = isFh;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public BigDecimal getZhxs() {
		return zhxs;
	}

	public void setZhxs(BigDecimal zhxs) {
		this.zhxs = zhxs;
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

	public BigDecimal getSpse() {
		return spse;
	}

	public void setSpse(BigDecimal spse) {
		this.spse = spse;
	}

	public BigDecimal getXscb() {
		return xscb;
	}

	public void setXscb(BigDecimal xscb) {
		this.xscb = xscb;
	}

	public BigDecimal getLastThsl() {
		return lastThsl;
	}

	public void setLastThsl(BigDecimal lastThsl) {
		this.lastThsl = lastThsl;
	}

	public BigDecimal getThsl() {
		return thsl;
	}

	public void setThsl(BigDecimal thsl) {
		this.thsl = thsl;
	}

	public String getDatagrid() {
		return datagrid;
	}

	public void setDatagrid(String datagrid) {
		this.datagrid = datagrid;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public BigDecimal getHkedje() {
		return hkedje;
	}

	public void setHkedje(BigDecimal hkedje) {
		this.hkedje = hkedje;
	}

	public String getIsUp() {
		return isUp;
	}

	public void setIsUp(String isUp) {
		this.isUp = isUp;
	}

	public int getPostponeDay() {
		return postponeDay;
	}

	public void setPostponeDay(int postponeDay) {
		this.postponeDay = postponeDay;
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

	public String getOtherBm() {
		return otherBm;
	}

	public void setOtherBm(String otherBm) {
		this.otherBm = otherBm;
	}

	public String getFromOther() {
		return fromOther;
	}

	public void setFromOther(String fromOther) {
		this.fromOther = fromOther;
	}

	public String getSpbh() {
		return spbh;
	}

	public void setSpbh(String spbh) {
		this.spbh = spbh;
	}

	public String getYwrklsh() {
		return ywrklsh;
	}

	public void setYwrklsh(String ywrklsh) {
		this.ywrklsh = ywrklsh;
	}

	public String getXsthlshs() {
		return xsthlshs;
	}

	public void setXsthlshs(String xsthlshs) {
		this.xsthlshs = xsthlshs;
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

	public String getNeedXsth() {
		return needXsth;
	}

	public void setNeedXsth(String needXsth) {
		this.needXsth = needXsth;
	}

}
