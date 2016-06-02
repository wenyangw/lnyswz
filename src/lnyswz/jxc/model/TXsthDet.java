package lnyswz.jxc.model;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * TXsthDet generated by hbm2java
 */
@Entity
@Table(name = "t_xsth_det")
public class TXsthDet implements java.io.Serializable {

	private int id;
	private TXsth TXsth;
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
	private BigDecimal ccksl;
	private BigDecimal kpsl;
	private BigDecimal ckpsl;
	private BigDecimal thsl;
	private BigDecimal qrsl;
	private String completed;
	//private BigDecimal lastRksl;
	
	private Set<TKfck> TKfcks;
	private Set<TXskp> TXskps;
	private TCgjh TCgjh;
	
	public TXsthDet() {
	}

	public TXsthDet(int id, TXsth TXsth, String spbh, String spmc, String spcd, String sppp, String spbz, String zjldwId, 
			String zjldwmc, String cjldwId, String cjldwmc, BigDecimal zhxs, BigDecimal zdwsl, BigDecimal cdwsl, 
			BigDecimal zdwdj, BigDecimal cdwdj,	BigDecimal spje, BigDecimal cksl, BigDecimal ccksl, BigDecimal kpsl, BigDecimal ckpsl,
			BigDecimal thsl, BigDecimal qrsl, String completed, Set<TKfck> TKfcks, Set<TXskp> TXskps, TCgjh TCgjh) {
		this.id = id;
		this.TXsth = TXsth;
		this.spbh = spbh;
		this.spmc = spmc;
		this.spcd = spcd;
		this.sppp = sppp;
		this.spbz = spbz;
		this.zjldwId = zjldwId;
		this.zjldwmc = zjldwmc;
		this.cjldwId = cjldwId;
		this.cjldwmc = cjldwmc;
		this.zhxs = zhxs;
		this.zdwsl = zdwsl;
		this.cdwsl = cdwsl;
		this.zdwdj = zdwdj;
		this.cdwdj = cdwdj;
		this.spje = spje;
		this.cksl = cksl;
		this.ccksl = ccksl;
		this.kpsl = kpsl;
		this.ckpsl = ckpsl;
		this.thsl = thsl;
		this.qrsl = qrsl;
		this.completed = completed;
		//this.lastRksl = lastRksl;
		this.TKfcks = TKfcks;
		this.TXskps = TXskps;
		this.TCgjh = TCgjh;
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
	@JoinColumn(name = "xsthlsh")
	public TXsth getTXsth() {
		return TXsth;
	}

	public void setTXsth(TXsth tXsth) {
		TXsth = tXsth;
	}

	@Column(name = "spbh", nullable = false, length = 7)
	public String getSpbh() {
		return spbh;
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

	@Column(name = "spcd", nullable = false, length = 20)
	public String getSpcd() {
		return spcd;
	}

	public void setSpcd(String spcd) {
		this.spcd = spcd;
	}

	@Column(name = "sppp", length = 20)
	public String getSppp() {
		return sppp;
	}

	public void setSppp(String sppp) {
		this.sppp = sppp;
	}

	@Column(name = "spbz", length = 20)
	public String getSpbz() {
		return spbz;
	}

	public void setSpbz(String spbz) {
		this.spbz = spbz;
	}

	@Column(name = "zjldwId", nullable = false, length = 2)
	public String getZjldwId() {
		return zjldwId;
	}

	public void setZjldwId(String zjldwId) {
		this.zjldwId = zjldwId;
	}

	@Column(name = "zjldwmc", nullable = false, length = 20)
	public String getZjldwmc() {
		return zjldwmc;
	}

	public void setZjldwmc(String zjldwmc) {
		this.zjldwmc = zjldwmc;
	}

	@Column(name = "cjldwId", length = 2)
	public String getCjldwId() {
		return cjldwId;
	}

	public void setCjldwId(String cjldwId) {
		this.cjldwId = cjldwId;
	}

	@Column(name = "cjldwmc", length = 20)
	public String getCjldwmc() {
		return cjldwmc;
	}

	public void setCjldwmc(String cjldwmc) {
		this.cjldwmc = cjldwmc;
	}

	@Column(name = "zhxs", nullable = false, precision = 18, scale = 3)
	public BigDecimal getZhxs() {
		return zhxs;
	}

	public void setZhxs(BigDecimal zhxs) {
		this.zhxs = zhxs;
	}

	@Column(name = "zdwsl", nullable = false, precision = 18, scale = 3)
	public BigDecimal getZdwsl() {
		return this.zdwsl;
	}

	public void setZdwsl(BigDecimal zdwsl) {
		this.zdwsl = zdwsl;
	}

	@Column(name = "cdwsl", nullable = true, precision = 18, scale = 3)
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

	@Column(name = "cksl", nullable = false, precision = 18, scale = 3)
	public BigDecimal getCksl() {
		return cksl;
	}

	public void setCksl(BigDecimal cksl) {
		this.cksl = cksl;
	}

	@Column(name = "ccksl", nullable = false, precision = 18, scale = 3)
	public BigDecimal getCcksl() {
		return ccksl;
	}

	public void setCcksl(BigDecimal ccksl) {
		this.ccksl = ccksl;
	}

	@Column(name = "kpsl", nullable = false, precision = 18, scale = 3)
	public BigDecimal getKpsl() {
		return kpsl;
	}

	public void setKpsl(BigDecimal kpsl) {
		this.kpsl = kpsl;
	}
	
	@Column(name = "ckpsl", nullable = false, precision = 18, scale = 3)
	public BigDecimal getCkpsl() {
		return ckpsl;
	}

	public void setCkpsl(BigDecimal ckpsl) {
		this.ckpsl = ckpsl;
	}

	@Column(name = "thsl", nullable = false, precision = 18, scale = 3)
	public BigDecimal getThsl() {
		return thsl;
	}

	public void setThsl(BigDecimal thsl) {
		this.thsl = thsl;
	}

	@Column(name = "qrsl", nullable = false, precision = 18, scale = 3)
	public BigDecimal getQrsl() {
		return qrsl;
	}

	public void setQrsl(BigDecimal qrsl) {
		this.qrsl = qrsl;
	}
	
	@Column(name = "completed", length = 1)
	public String getCompleted() {
		return completed;
	}

	public void setCompleted(String completed) {
		this.completed = completed;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "TXsths")
	public Set<TKfck> getTKfcks() {
		return this.TKfcks;
	}

	public void setTKfcks(Set<TKfck> TKfcks) {
		this.TKfcks = TKfcks;
	}
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "TXsths")
	public Set<TXskp> getTXskps() {
		return this.TXskps;
	}
	
	public void setTXskps(Set<TXskp> TXskps) {
		this.TXskps = TXskps;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cgjhlsh")
	public TCgjh getTCgjh() {
		return this.TCgjh;
	}

	public void setTCgjh(TCgjh TCgjh) {
		this.TCgjh = TCgjh;
	}
}
