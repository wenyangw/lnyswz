package lnyswz.jxc.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库房商品明细类
 * @author 
 *
 */
public class KfbgSp {
	
	private int id;
	private String spbh;
	private String spmc;
	private String spcd;
	private String sppp;
	private String spbz;
	private String sppc;
	private String zdwmc;
	private String cdwmc;
	private String fdwmc;
	private BigDecimal zrksl;
	private BigDecimal zcksl;
	private BigDecimal frksl;
	private BigDecimal fcksl;
	private Date createTime;
	private String type;
	private String khmc;
	private String lsh;
	
	
	private int page;
	private int rows;
	private String search;
	
	private String showDate;
	private BigDecimal zdwsl;
	private BigDecimal fdwsl;
	
	private String bmmc;
	private String bmbh;
	
	
	
	
	
	
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
	public String getSppc() {
		return sppc;
	}
	public void setSppc(String sppc) {
		this.sppc = sppc;
	}
	public String getZdwmc() {
		return zdwmc;
	}
	public void setZdwmc(String zdwmc) {
		this.zdwmc = zdwmc;
	}
	public String getCdwmc() {
		return cdwmc;
	}
	public void setCdwmc(String cdwmc) {
		this.cdwmc = cdwmc;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getKhmc() {
		return khmc;
	}
	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}
	public String getShowDate() {
		return showDate;
	}
	public void setShowDate(String showDate) {
		this.showDate = showDate;
	}
	public BigDecimal getZrksl() {
		return zrksl;
	}
	public void setZrksl(BigDecimal zrksl) {
		this.zrksl = zrksl;
	}
	public BigDecimal getZcksl() {
		return zcksl;
	}
	public void setZcksl(BigDecimal zcksl) {
		this.zcksl = zcksl;
	}
	
	public BigDecimal getZdwsl() {
		return zdwsl;
	}
	public void setZdwsl(BigDecimal zdwsl) {
		this.zdwsl = zdwsl;
	}
	public String getFdwmc() {
		return fdwmc;
	}
	public void setFdwmc(String fdwmc) {
		this.fdwmc = fdwmc;
	}
	public BigDecimal getFrksl() {
		return frksl;
	}
	public void setFrksl(BigDecimal frksl) {
		this.frksl = frksl;
	}
	public BigDecimal getFcksl() {
		return fcksl;
	}
	public void setFcksl(BigDecimal fcksl) {
		this.fcksl = fcksl;
	}
	public BigDecimal getFdwsl() {
		return fdwsl;
	}
	public void setFdwsl(BigDecimal fdwsl) {
		this.fdwsl = fdwsl;
	}
	public String getLsh() {
		return lsh;
	}
	public void setLsh(String lsh) {
		this.lsh = lsh;
	}
	public String getBmmc() {
		return bmmc;
	}
	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}
	public String getBmbh() {
		return bmbh;
	}
	public void setBmbh(String bmbh) {
		this.bmbh = bmbh;
	}
	
	
	
	
}
