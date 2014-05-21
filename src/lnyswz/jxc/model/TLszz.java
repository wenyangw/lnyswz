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
 * TLszz generated by hbm2java
 */
@Entity
@Table(name = "t_lszz")
public class TLszz implements java.io.Serializable {

	private int id;
	private String jzsj;
	private String bmbh;
	private String bmmc;
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
	private BigDecimal qcsl;
	private BigDecimal qcje;
	private BigDecimal lssl;
	private BigDecimal lsje;
	private BigDecimal kpsl;
	private BigDecimal kpje;

	public TLszz() {
	}

	public TLszz(int id, String jzsj, String bmbh, String bmmc, String spbh, String spmc, String spcd,
			String sppp, String spbz, String zjldwId, String zjldwmc, String cjldwId, String cjldwmc,
			BigDecimal zhxs, BigDecimal qcsl, BigDecimal qcje, BigDecimal lssl, BigDecimal lsje,
			BigDecimal kpsl, BigDecimal kpje) {
		this.id = id;
		this.jzsj = jzsj;
		this.bmbh = bmbh;
		this.bmmc = bmmc;
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
		this.qcsl = qcsl;
		this.qcje = qcje;
		this.lssl = lssl;
		this.lsje = lsje;
		this.kpsl = kpsl;
		this.kpje = kpje;
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
	
	@Column(name = "jzsj", nullable = false, length = 6)
	public String getJzsj() {
		return this.jzsj;
	}
	
	public void setJzsj(String jzsj) {
		this.jzsj = jzsj;
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
		return this.bmmc;
	}
	
	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
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

	@Column(name = "qcsl", nullable = false, precision = 18, scale = 3)
	public BigDecimal getQcsl() {
		return this.qcsl;
	}

	public void setQcsl(BigDecimal qcsl) {
		this.qcsl = qcsl;
	}

	@Column(name = "qcje", nullable = false, precision = 18, scale = 4)
	public BigDecimal getQcje() {
		return qcje;
	}

	public void setQcje(BigDecimal qcje) {
		this.qcje = qcje;
	}

	@Column(name = "lssl", nullable = false, precision = 18, scale = 3)
	public BigDecimal getLssl() {
		return this.lssl;
	}

	public void setLssl(BigDecimal lssl) {
		this.lssl = lssl;
	}

	@Column(name = "lsje", nullable = false, precision = 18, scale = 4)
	public BigDecimal getLsje() {
		return lsje;
	}

	public void setLsje(BigDecimal lsje) {
		this.lsje = lsje;
	}

	@Column(name = "kpsl", nullable = false, precision = 18, scale = 3)
	public BigDecimal getKpsl() {
		return kpsl;
	}

	public void setKpsl(BigDecimal kpsl) {
		this.kpsl = kpsl;
	}

	@Column(name = "kpje", nullable = false, precision = 18, scale = 4)
	public BigDecimal getKpje() {
		return kpje;
	}

	public void setKpje(BigDecimal kpje) {
		this.kpje = kpje;
	}
}
