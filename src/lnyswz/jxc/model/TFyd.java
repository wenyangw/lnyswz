package lnyswz.jxc.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * TFyd generated by hbm2java
 */
@Entity
@Table(name = "t_fyd")
public class TFyd implements java.io.Serializable {

	private String fydlsh;
	private String bmbh;
	private Date createTime;
	private String status;
	private String publisher;
	private String publishercn;
	private String checkCode;
	private String tzdbh;
	private String cbsydsno;
	private String bsno;
	private String bname;
	private String isbn;
	private Date tzrq;
	private String yc;
	private BigDecimal price;
	private int tzdys;
	private int yangshu;
	private int zongym;
	private int cbzs;
	private int kbgg;
	private String cpgg;
	private int dwyz;
	private String zdr;
	private String zdfs;
	private String zzfs;
	private Set<TFydDet> TFydDets = new HashSet<TFydDet>(0);
	
	public TFyd() {
	}

	public TFyd(String fydlsh, String bmbh, Date createTime,	String status, String publisher, String publishercn, String checkCode, String tzdbh, String cbsydsno, String bsno, String bname, String isbn,
			Date tzrq, String yc, BigDecimal price, int tzdys, int yangshu, int zongym, int cbzs, int kbgg, String cpgg, int dwyz, String zdr,
			String zdfs, String zzfs, Set<TFydDet> TFydDets) {
		this.fydlsh = fydlsh;
		this.bmbh = bmbh;
		this.createTime = createTime;
		this.status = status;
		this.publisher = publisher;
		this.publishercn = publishercn;
		this.checkCode = checkCode;
		this.tzdbh = tzdbh;
		this.cbsydsno = cbsydsno;
		this.bsno = bsno;
		this.bname = bname;
		this.isbn = isbn;
		this.tzrq = tzrq;
		this.yc = yc;
		this.price = price;
		this.tzdys = tzdys;
		this.yangshu = yangshu;
		this.zongym = zongym;
		this.cbzs = cbzs;
		this.kbgg = kbgg;
		this.cpgg = cpgg;
		this.dwyz = dwyz;
		this.zdr = zdr;
		this.zdfs = zdfs;
		this.zzfs = zzfs;
		this.TFydDets = TFydDets;
	}


	@Id
	@Column(name = "fydlsh", unique = true, nullable = false, length = 12)
	public String getFydlsh() {
		return fydlsh;
	}

	public void setFydlsh(String fydlsh) {
		this.fydlsh = fydlsh;
	}
	
	@Column(name = "bmbh", nullable = false, length = 2)
	public String getBmbh() {
		return bmbh;
	}

	public void setBmbh(String bmbh) {
		this.bmbh = bmbh;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime", nullable = false, length = 23)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "status", nullable = false, length = 1)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	@Column(name = "publisher", nullable = false, length = 4)
	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	@Column(name = "publishercn", nullable = false, length = 20)
	public String getPublishercn() {
		return publishercn;
	}

	public void setPublishercn(String publishercn) {
		this.publishercn = publishercn;
	}

	@Column(name = "checkCode", nullable = false, length = 20)
	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	@Column(name = "tzdbh", nullable = false, length = 20)
	public String getTzdbh() {
		return tzdbh;
	}

	public void setTzdbh(String tzdbh) {
		this.tzdbh = tzdbh;
	}

	@Column(name = "cbsydsno", nullable = false, length = 20)
	public String getCbsydsno() {
		return cbsydsno;
	}

	public void setCbsydsno(String cbsydsno) {
		this.cbsydsno = cbsydsno;
	}

	@Column(name = "bsno", nullable = false, length = 20)
	public String getBsno() {
		return bsno;
	}

	public void setBsno(String bsno) {
		this.bsno = bsno;
	}

	@Column(name = "bname", nullable = false, length = 100)
	public String getBname() {
		return bname;
	}

	public void setBname(String bname) {
		this.bname = bname;
	}

	@Column(name = "isbn", nullable = false, length = 20)
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tzrq", nullable = false, length = 23)
	public Date getTzrq() {
		return tzrq;
	}

	public void setTzrq(Date tzrq) {
		this.tzrq = tzrq;
	}

	@Column(name = "yc", nullable = false, length = 20)
	public String getYc() {
		return yc;
	}

	public void setYc(String yc) {
		this.yc = yc;
	}

	@Column(name = "price", precision = 18, scale = 2)
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Column(name = "tzdys", nullable = false)
	public int getTzdys() {
		return tzdys;
	}

	public void setTzdys(int tzdys) {
		this.tzdys = tzdys;
	}

	@Column(name = "yangshu", nullable = false)
	public int getYangshu() {
		return yangshu;
	}

	public void setYangshu(int yangshu) {
		this.yangshu = yangshu;
	}

	@Column(name = "zongym", nullable = false)
	public int getZongym() {
		return zongym;
	}

	public void setZongym(int zongym) {
		this.zongym = zongym;
	}

	@Column(name = "cbzs", nullable = false)
	public int getCbzs() {
		return cbzs;
	}

	public void setCbzs(int cbzs) {
		this.cbzs = cbzs;
	}

	@Column(name = "kbgg", nullable = false)
	public int getKbgg() {
		return kbgg;
	}

	public void setKbgg(int kbgg) {
		this.kbgg = kbgg;
	}

	@Column(name = "cpgg", nullable = false, length = 20)
	public String getCpgg() {
		return cpgg;
	}

	public void setCpgg(String cpgg) {
		this.cpgg = cpgg;
	}
	
	@Column(name = "dwyz", nullable = false)
	public int getDwyz() {
		return dwyz;
	}

	public void setDwyz(int dwyz) {
		this.dwyz = dwyz;
	}

	@Column(name = "zdr", nullable = false, length = 20)
	public String getZdr() {
		return zdr;
	}

	public void setZdr(String zdr) {
		this.zdr = zdr;
	}

	@Column(name = "zdfs", nullable = false, length = 20)
	public String getZdfs() {
		return zdfs;
	}

	public void setZdfs(String zdfs) {
		this.zdfs = zdfs;
	}

	@Column(name = "zzfs", nullable = false, length = 20)
	public String getZzfs() {
		return zzfs;
	}

	public void setZzfs(String zzfs) {
		this.zzfs = zzfs;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "TFyd", cascade=CascadeType.ALL)
	public Set<TFydDet> getTFydDets() {
		return this.TFydDets;
	}

	public void setTFydDets(Set<TFydDet> TFydDets) {
		this.TFydDets = TFydDets;
	}
	
}
