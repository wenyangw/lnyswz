package lnyswz.jxc.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * TSp generated by hbm2java
 */
@Entity
@Table(name = "t_sp")
public class TSp implements java.io.Serializable {

	private String spbh;
	private String spmc;
	private TSpdw TSpdw;
	private String spcd;
	private String sppp;
	private String spbz;
	private TJldw zjldw;
	private TJldw cjldw;
	private BigDecimal zhxs;
	private int yxq;
	private Set<TSpDet> TSpDets = new HashSet<TSpDet>(0);

	public TSp() {
	}


	public TSp(String spbh, String spmc, TSpdw TSpdw, String spcd, String sppp, String spbz, TJldw zjldw, TJldw cjldw, BigDecimal zhxs, int yxq, Set<TSpDet> TSpDets) {
		this.spbh = spbh;
		this.spmc = spmc;
		this.TSpdw = TSpdw;
		this.spcd = spcd;
		this.sppp = sppp;
		this.spbz = spbz;
		this.zjldw = zjldw;
		this.cjldw = cjldw;
		this.zhxs = zhxs;
		this.yxq = yxq;
		this.TSpDets = TSpDets;
	}

	@Id
	@Column(name = "spbh", unique = true, nullable = false, length = 7)
	public String getSpbh() {
		return this.spbh;
	}
	
	public void setSpbh(String spbh) {
		this.spbh = spbh;
	}
	
	@Column(name = "spmc", nullable = false, length = 100)
	public String getSpmc() {
		return spmc;
	}


	public void setSpmc(String spmc) {
		this.spmc = spmc;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "spdwId", nullable = false)
	public TSpdw getTSpdw() {
		return this.TSpdw;
	}

	public void setTSpdw(TSpdw TSpdw) {
		this.TSpdw = TSpdw;
	}

	@Column(name = "spcd", nullable = false, length = 50)
	public String getSpcd() {
		return this.spcd;
	}

	public void setSpcd(String spcd) {
		this.spcd = spcd;
	}

	@Column(name = "sppp", nullable = true, length = 50)
	public String getSppp() {
		return sppp;
	}

	public void setSppp(String sppp) {
		this.sppp = sppp;
	}
	
	@Column(name = "spbz", nullable = true, length = 50)
	public String getSpbz() {
		return spbz;
	}

	public void setSpbz(String spbz) {
		this.spbz = spbz;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "zjldwId", nullable = false)
	public TJldw getZjldw() {
		return zjldw;
	}

	public void setZjldw(TJldw zjldw) {
		this.zjldw = zjldw;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cjldwId", nullable = true)
	public TJldw getCjldw() {
		return cjldw;
	}

	public void setCjldw(TJldw cjldw) {
		this.cjldw = cjldw;
	}

	@Column(name = "zhxs", nullable = true)
	public BigDecimal getZhxs() {
		return zhxs;
	}

	public void setZhxs(BigDecimal zhxs) {
		this.zhxs = zhxs;
	}
	@Column(name = "yxq", nullable = true)
	public int getYxq() {
		return yxq;
	}

	public void setYxq(int yxq) {
		this.yxq = yxq;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "TSp",cascade=CascadeType.ALL)
	public Set<TSpDet> getTSpDets() {
		return TSpDets;
	}

	public void setTSpDets(Set<TSpDet> tSpDets) {
		TSpDets = tSpDets;
	}

}
