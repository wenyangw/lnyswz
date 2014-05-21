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
 * TKfzz generated by hbm2java
 */
@Entity
@Table(name = "t_kfzz")
public class TKfzz implements java.io.Serializable {

	private int id;
	private String bmbh;
	private String bmmc;
	private String ckId;
	private String ckmc;
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
	private String sppc;
	private String hwId;
	private String hwmc;
	private BigDecimal qcsl;
	private BigDecimal rksl;
	private BigDecimal cksl;
	private String jzsj;

	public TKfzz() {
	}

	public TKfzz(int id, String spbh, String spmc, String spcd, String sppp, String spbz, String zjldwId, String zjldwmc,
			String cjldwId, String cjldwmc, BigDecimal zhxs, String bmbh, String bmmc, String ckId, String ckmc, String hwId,
			String hwmc, String sppc, BigDecimal qcsl, BigDecimal rksl, BigDecimal cksl, String jzsj) {
		this.id = id;
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
		this.bmbh = bmbh;
		this.bmmc = bmmc;
		this.ckId = ckId;
		this.ckmc = ckmc;
		this.sppc = sppc;
		this.hwId = hwId;
		this.hwmc = hwmc;
		this.qcsl = qcsl;
		this.rksl = rksl;
		this.cksl = cksl;
		this.jzsj = jzsj;
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

	@Column(name = "ckId", length = 2)
	public String getCkId() {
		return ckId;
	}

	public void setCkId(String ckId) {
		this.ckId = ckId;
	}

	@Column(name = "ckmc", length = 20)
	public String getCkmc() {
		return ckmc;
	}

	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}

	@Column(name = "sppc", length = 10)
	public String getSppc() {
		return this.sppc;
	}

	public void setSppc(String sppc) {
		this.sppc = sppc;
	}

	@Column(name = "hwId", length = 2)
	public String getHwId() {
		return hwId;
	}

	public void setHwId(String hwId) {
		this.hwId = hwId;
	}

	@Column(name = "hwmc", length = 20)
	public String getHwmc() {
		return hwmc;
	}

	public void setHwmc(String hwmc) {
		this.hwmc = hwmc;
	}

	@Column(name = "qcsl", precision = 18, scale = 3)
	public BigDecimal getQcsl() {
		return this.qcsl;
	}

	public void setQcsl(BigDecimal qcsl) {
		this.qcsl = qcsl;
	}

	@Column(name = "rksl", precision = 18, scale = 3)
	public BigDecimal getRksl() {
		return this.rksl;
	}

	public void setRksl(BigDecimal rksl) {
		this.rksl = rksl;
	}

	@Column(name = "cksl", precision = 18, scale = 3)
	public BigDecimal getCksl() {
		return this.cksl;
	}

	public void setCksl(BigDecimal cksl) {
		this.cksl = cksl;
	}

	@Column(name = "jzsj", length = 6)
	public String getJzsj() {
		return this.jzsj;
	}

	public void setJzsj(String jzsj) {
		this.jzsj = jzsj;
	}

}
