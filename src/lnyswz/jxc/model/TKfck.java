package lnyswz.jxc.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TKfck generated by hbm2java
 */
@Entity
@Table(name = "t_kfck")
public class TKfck implements java.io.Serializable {

	private String kfcklsh;
	private Date createTime;
	private int createId;
	private String createName;
	private String bmbh;
	private String bmmc;
	private String khbh;
	private String khmc;
	private String ckId;
	private String ckmc;
	private String isFh;
	private String fhId;
	private String fhmc;
	private String thr;
	private String ch;
	private String bz;
	private String cjKfcklsh;
	private String isCj;
	private Date cjTime;
	private Integer cjId;
	private String cjName;
	private Set<TKfckDet> TKfckDets = new HashSet<TKfckDet>(0);
	private Set<TXsthDet> TXsths = new HashSet<TXsthDet>(0);
//	private TXsth TXsth; 
	
	public TKfck() {
	}

	public TKfck(String kfcklsh, Date createTime, int createId, String createName, String bmbh, String bmmc, String khbh, String khmc,
			String ckId, String ckmc, String isFh, String fhId, String fhmc, String thr, String ch, String bz, String cjKfcklsh,
			String isCj, Date cjTime, Integer cjId, String cjName, Set<TKfckDet> TKfckDets, Set<TXsthDet> TXsths) {
		this.kfcklsh = kfcklsh;
		this.createTime = createTime;
		this.createId = createId;
		this.createName = createName;
		this.bmbh = bmbh;
		this.bmmc = bmmc;
		this.khbh = khbh;
		this.khmc = khmc;
		this.ckId = ckId;
		this.ckmc = ckmc;
		this.isFh = isFh;
		this.fhId = fhId;
		this.fhmc = fhmc;
		this.thr = thr;
		this.ch = ch;
		this.bz = bz;
		this.cjKfcklsh = cjKfcklsh;
		this.isCj = isCj;
		this.cjTime = cjTime;
		this.cjId = cjId;
		this.cjName = cjName;
		this.TKfckDets = TKfckDets;
		this.TXsths = TXsths;
	}

	@Id
	@Column(name = "kfcklsh", unique = true, nullable = false, length = 12)
	public String getKfcklsh() {
		return this.kfcklsh;
	}

	public void setKfcklsh(String kfcklsh) {
		this.kfcklsh = kfcklsh;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime", nullable = false, length = 23)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "createId", nullable = false)
	public int getCreateId() {
		return this.createId;
	}

	public void setCreateId(int createId) {
		this.createId = createId;
	}

	@Column(name = "createName", nullable = false, length = 20)
	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	@Column(name = "bmbh", nullable = false, length = 2)
	public String getBmbh() {
		return this.bmbh;
	}

	public void setBmbh(String bmbh) {
		this.bmbh = bmbh;
	}

	@Column(name = "bmmc", nullable = false, length = 20)
	public String getBmmc() {
		return bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	@Column(name = "khbh", length = 8)
	public String getKhbh() {
		return khbh;
	}

	public void setKhbh(String khbh) {
		this.khbh = khbh;
	}

	@Column(name = "khmc", length = 100)
	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	@Column(name = "ckId", nullable = false, length = 2)
	public String getCkId() {
		return ckId;
	}

	public void setCkId(String ckId) {
		this.ckId = ckId;
	}

	@Column(name = "ckmc", nullable = false, length = 20)
	public String getCkmc() {
		return ckmc;
	}

	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}

	@Column(name = "isFh", nullable = false, length = 1)
	public String getIsFh() {
		return isFh;
	}

	public void setIsFh(String isFh) {
		this.isFh = isFh;
	}

	@Column(name = "fhId", length = 2)
	public String getFhId() {
		return fhId;
	}

	public void setFhId(String fhId) {
		this.fhId = fhId;
	}

	@Column(name = "fhmc", length = 20)
	public String getFhmc() {
		return fhmc;
	}

	public void setFhmc(String fhmc) {
		this.fhmc = fhmc;
	}

	@Column(name = "thr", nullable = true, length = 20)
	public String getThr() {
		return thr;
	}

	public void setThr(String thr) {
		this.thr = thr;
	}

	@Column(name = "ch", nullable = true, length = 20)
	public String getCh() {
		return ch;
	}

	public void setCh(String ch) {
		this.ch = ch;
	}

	@Column(name = "bz", length = 100)
	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	@Column(name = "cjKfcklsh", length = 12)
	public String getCjKfcklsh() {
		return cjKfcklsh;
	}

	public void setCjKfcklsh(String cjKfcklsh) {
		this.cjKfcklsh = cjKfcklsh;
	}

	@Column(name = "isCj", nullable = false, length = 1)
	public String getIsCj() {
		return this.isCj;
	}

	public void setIsCj(String isCj) {
		this.isCj = isCj;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cjTime", length = 23)
	public Date getCjTime() {
		return this.cjTime;
	}

	public void setCjTime(Date cjTime) {
		this.cjTime = cjTime;
	}

	@Column(name = "cjId")
	public Integer getCjId() {
		return this.cjId;
	}

	public void setCjId(Integer cjId) {
		this.cjId = cjId;
	}

	@Column(name = "cjName", length = 20)
	public String getCjName() {
		return cjName;
	}

	public void setCjName(String cjName) {
		this.cjName = cjName;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "TKfck", cascade=CascadeType.ALL)
	public Set<TKfckDet> getTKfckDets() {
		return this.TKfckDets;
	}

	public void setTKfckDets(Set<TKfckDet> TKfckDets) {
		this.TKfckDets = TKfckDets;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "t_xsth_kfck", joinColumns = { @JoinColumn(name = "kfcklsh", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "xsthdetId", nullable = false, updatable = false) })
	public Set<TXsthDet> getTXsths() {
		return this.TXsths;
	}

	public void setTXsths(Set<TXsthDet> TXsths) {
		this.TXsths = TXsths;
	}
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "xsthdetId", nullable = true)
//	public TXsthDet getTXsthDet() {
//		return this.TXsthDet;
//	}
//
//	public void setTXsth(TXsthDet TXsthDet) {
//		this.TXsthDet = TXsthDet;
//	}

}
