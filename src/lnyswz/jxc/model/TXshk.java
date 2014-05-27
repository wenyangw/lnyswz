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
	private BigDecimal hkje;
	private BigDecimal lastHkje;
	private String isCancel;
	private Integer cancelId;
	private String cancelName;
	private Date cancelTime;
	private String cancelXshklsh;
	private Set<TXskp> TXskps = new HashSet<TXskp>(0);

	public TXshk() {
	}

	public TXshk(String xshklsh, Date createTime, int createId, String createName, String bmbh, String bmmc, String khbh,
			String khmc, BigDecimal hkje, BigDecimal lastHkje, String isCancel, Integer cancelId, String cancelName, Date cancelTime,
			String cancelXshklsh, Set<TXskp> TXskps) {
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
		this.isCancel = isCancel;
		this.cancelId = cancelId;
		this.cancelName = cancelName;
		this.cancelTime = cancelTime;
		this.cancelXshklsh = cancelXshklsh;
		this.TXskps = TXskps;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "TXshk", cascade=CascadeType.ALL)
	public Set<TXskp> getTXskps() {
		return this.TXskps;
	}

	public void setTXskps(Set<TXskp> TXskps) {
		this.TXskps = TXskps;
	}

}
