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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * TXshk generated by hbm2java
 */
@Entity
@Table(name = "t_xshk")
public class TXshk implements java.io.Serializable {
	private String xshklsh;
	private Date createTime;
	private int createId;
	private String createName;
	private String bmbh;
	private String bmmc;
	private String khbh;
	private String khmc;
	private int ywyId;
	private String ywymc;
	private BigDecimal hkje;
	private BigDecimal lastHkje;
	private BigDecimal yfje;
	private Date payTime;
	private String isLs;
	private String isYf;
	private String isCancel;
	private Integer cancelId;
	private String cancelName;
	private Date cancelTime;
	private String cancelXshklsh;
	private Set<THkKp> THkKps = new HashSet<THkKp>(0);

	public TXshk() {
	}

	public TXshk(String xshklsh, Date createTime, int createId, String createName, String bmbh, String bmmc, String khbh,
			String khmc, BigDecimal hkje, BigDecimal lastHkje, BigDecimal yfje, String isYf, String isCancel, Integer cancelId, String cancelName, Date cancelTime,
			String cancelXshklsh, Set<THkKp> THkKps) {
		this.xshklsh = xshklsh;
		this.createTime = createTime;
		this.createId = createId;
		this.createName = createName;
		this.bmbh = bmbh;
		this.bmmc = bmmc;
		this.khbh = khbh;
		this.khmc = khmc;
		this.hkje = hkje;
		this.lastHkje = lastHkje;
		this.yfje = yfje;
		this.isYf = isYf;
		this.isCancel = isCancel;
		this.cancelId = cancelId;
		this.cancelName = cancelName;
		this.cancelTime = cancelTime;
		this.cancelXshklsh = cancelXshklsh;
		this.THkKps = THkKps;
	}

	@Id
	@Column(name = "xshklsh", unique = true, nullable = false, length = 12)
	public String getXshklsh() {
		return this.xshklsh;
	}

	public void setXshklsh(String xshklsh) {
		this.xshklsh = xshklsh;
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

	@Column(name = "createName", nullable = false, length = 50)
	public String getCreateName() {
		return this.createName;
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
		return this.bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	@Column(name = "khbh", nullable = false, length = 8)
	public String getKhbh() {
		return this.khbh;
	}

	public void setKhbh(String khbh) {
		this.khbh = khbh;
	}

	@Column(name = "khmc", nullable = false, length = 50)
	public String getKhmc() {
		return this.khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	@Column(name = "ywyId")
	public int getYwyId() {
		return ywyId;
	}

	public void setYwyId(int ywyId) {
		this.ywyId = ywyId;
	}

	@Column(name = "ywymc", nullable = false, length = 20)
	public String getYwymc() {
		return ywymc;
	}

	public void setYwymc(String ywymc) {
		this.ywymc = ywymc;
	}

	@Column(name = "hkje", nullable = false, precision = 18, scale = 4)
	public BigDecimal getHkje() {
		return hkje;
	}

	public void setHkje(BigDecimal hkje) {
		this.hkje = hkje;
	}
	

	@Column(name = "lastHkje", nullable = false, precision = 18, scale = 4)
	public BigDecimal getLastHkje() {
		return lastHkje;
	}

	public void setLastHkje(BigDecimal lastHkje) {
		this.lastHkje = lastHkje;
	}

	@Column(name = "yfje", nullable = false, precision = 18, scale = 4)
	public BigDecimal getYfje() {
		return yfje;
	}

	public void setYfje(BigDecimal yfje) {
		this.yfje = yfje;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "payTime", nullable = false, length = 23)
	public Date getPayTime() {
		return payTime;
	}
	
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	
	@Column(name = "isLs", nullable = false, length = 1)
	public String getIsLs() {
		return isLs;
	}

	public void setIsLs(String isLs) {
		this.isLs = isLs;
	}

	@Column(name = "isYf", nullable = false, length = 1)
	public String getIsYf() {
		return this.isYf;
	}

	public void setIsYf(String isYf) {
		this.isYf = isYf;
	}
	
	@Column(name = "isCancel", nullable = false, length = 1)
	public String getIsCancel() {
		return this.isCancel;
	}

	public void setIsCancel(String isCancel) {
		this.isCancel = isCancel;
	}

	@Column(name = "cancelId")
	public Integer getCancelId() {
		return this.cancelId;
	}

	public void setCancelId(Integer cancelId) {
		this.cancelId = cancelId;
	}

	@Column(name = "cancelName", length = 20)
	public String getCancelName() {
		return this.cancelName;
	}

	public void setCancelName(String cancelName) {
		this.cancelName = cancelName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cancelTime", length = 23)
	public Date getCancelTime() {
		return this.cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	@Column(name = "cancelXshklsh", length = 12)
	public String getCancelXshklsh() {
		return this.cancelXshklsh;
	}

	public void setCancelXshklsh(String cancelXshklsh) {
		this.cancelXshklsh = cancelXshklsh;
	}

	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "TXshk")
	public Set<THkKp> getTHkKps() {
		return this.THkKps;
	}

	public void setTHkKps(Set<THkKp> THkKps) {
		this.THkKps = THkKps;
	}
	
}
