package lnyswz.jxc.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * TXskpDet generated by hbm2java
 */
@Entity
@Table(name = "t_xskp_det")
public class TXskpDet implements java.io.Serializable {

	private int id;
	private TXskp TXskp;
	private String spbh;
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
	private BigDecimal cLastThsl;
	private BigDecimal thsl;

	public TXskpDet() {
	}

	public TXskpDet(int id, TXskp TXskp, String spbh, String spmc, String spcd, String sppp, String spbz, BigDecimal zhxs,
			String zjldwId, String zjldwmc, String cjldwId, String cjldwmc, BigDecimal zdwsl, BigDecimal cdwsl, BigDecimal zdwdj,
			BigDecimal cdwdj, BigDecimal spje, BigDecimal spse, BigDecimal xscb, BigDecimal lastThsl, BigDecimal cLastThsl, BigDecimal thsl) {
		this.id = id;
		this.TXskp = TXskp;
		this.spbh = spbh;
		this.spmc = spmc;
		this.spcd = spcd;
		this.sppp = sppp;
		this.spbz = spbz;
		this.zhxs = zhxs;
		this.zjldwId = zjldwId;
		this.zjldwmc = zjldwmc;
		this.cjldwId = cjldwId;
		this.cjldwmc = cjldwmc;
		this.zdwsl = zdwsl;
		this.cdwsl = cdwsl;
		this.zdwdj = zdwdj;
		this.cdwdj = cdwdj;
		this.spje = spje;
		this.spse = spse;
		this.xscb = xscb;
		this.lastThsl = lastThsl;
		this.cLastThsl = cLastThsl;
		this.thsl = thsl;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "xskplsh", nullable = false)
	public TXskp getTXskp() {
		return this.TXskp;
	}

	public void setTXskp(TXskp TXskp) {
		this.TXskp = TXskp;
	}

	@Column(name = "spbh", nullable = false, length = 7)
	public String getSpbh() {
		return this.spbh;
	}

	public void setSpbh(String spbh) {
		this.spbh = spbh;
	}

	@Column(name = "spmc", nullable = false, length = 50)
	public String getSpmc() {
		return this.spmc;
	}

	public void setSpmc(String spmc) {
		this.spmc = spmc;
	}

	@Column(name = "spcd", nullable = false, length = 20)
	public String getSpcd() {
		return this.spcd;
	}

	public void setSpcd(String spcd) {
		this.spcd = spcd;
	}

	@Column(name = "sppp", length = 20)
	public String getSppp() {
		return this.sppp;
	}

	public void setSppp(String sppp) {
		this.sppp = sppp;
	}

	@Column(name = "spbz", length = 20)
	public String getSpbz() {
		return this.spbz;
	}

	public void setSpbz(String spbz) {
		this.spbz = spbz;
	}

	@Column(name = "zhxs", precision = 18, scale = 3)
	public BigDecimal getZhxs() {
		return this.zhxs;
	}

	public void setZhxs(BigDecimal zhxs) {
		this.zhxs = zhxs;
	}

	@Column(name = "zjldwId", nullable = false, length = 2)
	public String getZjldwId() {
		return this.zjldwId;
	}

	public void setZjldwId(String zjldwId) {
		this.zjldwId = zjldwId;
	}

	@Column(name = "zjldwmc", nullable = false, length = 50)
	public String getZjldwmc() {
		return this.zjldwmc;
	}

	public void setZjldwmc(String zjldwmc) {
		this.zjldwmc = zjldwmc;
	}

	@Column(name = "cjldwId", length = 2)
	public String getCjldwId() {
		return this.cjldwId;
	}

	public void setCjldwId(String cjldwId) {
		this.cjldwId = cjldwId;
	}

	@Column(name = "cjldwmc", length = 50)
	public String getCjldwmc() {
		return this.cjldwmc;
	}

	public void setCjldwmc(String cjldwmc) {
		this.cjldwmc = cjldwmc;
	}

	@Column(name = "zdwsl", nullable = false, precision = 18, scale = 3)
	public BigDecimal getZdwsl() {
		return this.zdwsl;
	}

	public void setZdwsl(BigDecimal zdwsl) {
		this.zdwsl = zdwsl;
	}

	@Column(name = "cdwsl", precision = 18, scale = 3)
	public BigDecimal getCdwsl() {
		return this.cdwsl;
	}

	public void setCdwsl(BigDecimal cdwsl) {
		this.cdwsl = cdwsl;
	}

	@Column(name = "zdwdj", nullable = false, precision = 18, scale = 4)
	public BigDecimal getZdwdj() {
		return this.zdwdj;
	}

	public void setZdwdj(BigDecimal zdwdj) {
		this.zdwdj = zdwdj;
	}

	@Column(name = "cdwdj", precision = 18, scale = 4)
	public BigDecimal getCdwdj() {
		return this.cdwdj;
	}

	public void setCdwdj(BigDecimal cdwdj) {
		this.cdwdj = cdwdj;
	}

	@Column(name = "spje", nullable = false, precision = 18, scale = 4)
	public BigDecimal getSpje() {
		return this.spje;
	}

	public void setSpje(BigDecimal spje) {
		this.spje = spje;
	}

	@Column(name = "spse", nullable = false, precision = 18, scale = 4)
	public BigDecimal getSpse() {
		return this.spse;
	}

	public void setSpse(BigDecimal spse) {
		this.spse = spse;
	}

	@Column(name = "xscb", nullable = false, precision = 18, scale = 4)
	public BigDecimal getXscb() {
		return this.xscb;
	}

	public void setXscb(BigDecimal xscb) {
		this.xscb = xscb;
	}

	@Column(name = "lastThsl", precision = 18, scale = 3)
	public BigDecimal getLastThsl() {
		return lastThsl;
	}

	public void setLastThsl(BigDecimal lastThsl) {
		this.lastThsl = lastThsl;
	}
	
	@Column(name = "cLastThsl", precision = 18, scale = 3)
	public BigDecimal getcLastThsl() {
		return cLastThsl;
	}

	public void setcLastThsl(BigDecimal cLastThsl) {
		this.cLastThsl = cLastThsl;
	}

	@Column(name = "thsl", precision = 18, scale = 3)
	public BigDecimal getThsl() {
		return thsl;
	}

	public void setThsl(BigDecimal thsl) {
		this.thsl = thsl;
	}


}
