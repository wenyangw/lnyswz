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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * TYwbt generated by hbm2java
 */
@Entity
@Table(name = "t_ywbt")
public class TYwbt implements java.io.Serializable {

	private String ywbtlsh;
	private int createId;
	private Date createTime;
	private String createName;
	private String bmbh;
	private String bmmc;
	private BigDecimal hjje;
	private String bz;
	private Set<TYwbtDet> TYwbtDets = new HashSet<TYwbtDet>(0);
	private TYwrk TYwrk;
	
	public TYwbt() {
	}

	public TYwbt(String ywbtlsh, int createId, Date createTime,  String createName, String bmbh, String bmmc,
			BigDecimal hjje, String bz,	Set<TYwbtDet> TYwbtDets, TYwrk TYwrk) {
		this.ywbtlsh = ywbtlsh;
		this.createTime = createTime;
		this.createId = createId;
		this.createName = createName;
		this.bmbh = bmbh;
		this.bmmc = bmmc;
		this.hjje = hjje;
		this.bz = bz;
		this.TYwbtDets = TYwbtDets;
		this.TYwrk = TYwrk;
	}

	@Id
	@Column(name = "ywbtlsh", unique = true, nullable = false, length = 12)
	public String getYwbtlsh() {
		return this.ywbtlsh;
	}

	public void setYwbtlsh(String ywbtlsh) {
		this.ywbtlsh = ywbtlsh;
	}

	@Column(name = "createId", nullable = false)
	public int getCreateId() {
		return this.createId;
	}
	
	public void setCreateId(int createId) {
		this.createId = createId;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime", nullable = false, length = 23)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	@Column(name = "hjje", precision = 18, scale = 4)
	public BigDecimal getHjje() {
		return this.hjje;
	}

	public void setHjje(BigDecimal hjje) {
		this.hjje = hjje;
	}

	@Column(name = "bz", length = 100)
	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "TYwbt", cascade=CascadeType.ALL)
	public Set<TYwbtDet> getTYwbtDets() {
		return this.TYwbtDets;
	}

	public void setTYwbtDets(Set<TYwbtDet> TYwbtDets) {
		this.TYwbtDets = TYwbtDets;
	}

	@OneToOne(mappedBy="TYwbt", fetch = FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	public TYwrk getTYwrk() {
		return TYwrk;
	}

	public void setTYwrk(TYwrk tYwrk) {
		TYwrk = tYwrk;
	}
	
}
