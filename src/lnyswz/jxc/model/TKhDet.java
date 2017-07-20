package lnyswz.jxc.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * TKhDet generated by hbm2java
 */
@Entity
@Table(name = "t_kh_det")
public class TKhDet implements java.io.Serializable {

	private String id;
	private TKh TKh;
	private TDepartment TDepartment;
	private Integer ywyId;
	private String khlxId;
	private String isSx;
	private String lxr;
	private Integer sxzq;
	private BigDecimal sxje;
	private BigDecimal lsje;
	private String isUp;
	private int postponeDay;
	private String isOther;
	private BigDecimal limitPer;
	private BigDecimal limitJe;
	private String isLocked;
	private String isDef;
	private String info;

	public TKhDet() {
	}

	public TKhDet(String id, TKh TKh, TDepartment TDepartment, String lxr, Integer ywyId, String khlxId, 
			String isSx, Integer sxzq, BigDecimal sxje, BigDecimal lsje, String isUp, int postponeDay, String isOther,
			BigDecimal limitPer, BigDecimal limitJe, String isLocked, String isDef, String info) {
		this.id = id;
		this.TKh = TKh;
		this.TDepartment = TDepartment;
		this.lxr = lxr;
		this.ywyId = ywyId;
		this.khlxId = khlxId;
		this.isSx = isSx;
		this.sxzq = sxzq;
		this.sxje = sxje;
		this.lsje = lsje;
		this.isUp = isUp;
		this.postponeDay = postponeDay;
		this.isOther = isOther;
		this.limitPer = limitPer;
		this.limitJe = limitJe;
		this.isLocked = isLocked;
		this.isDef = isDef;
		this.info = info;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 36)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "khbh", nullable = false)
	public TKh getTKh() {
		return this.TKh;
	}

	public void setTKh(TKh TKh) {
		this.TKh = TKh;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "depId", nullable = false)
	public TDepartment getTDepartment() {
		return TDepartment;
	}

	public void setTDepartment(TDepartment tDepartment) {
		TDepartment = tDepartment;
	}

	@Column(name = "ywyId")
	public Integer getYwyId() {
		return ywyId;
	}

	public void setYwyId(Integer ywyId) {
		this.ywyId = ywyId;
	}

	@Column(name = "khlxId")
	public String getKhlxId() {
		return khlxId;
	}

	public void setKhlxId(String khlxId) {
		this.khlxId = khlxId;
	}

	@Column(name = "isSx", length = 1)
	public String getIsSx() {
		return isSx;
	}

	public void setIsSx(String isSx) {
		this.isSx = isSx;
	}

	@Column(name = "lxr", length = 50)
	public String getLxr() {
		return this.lxr;
	}

	public void setLxr(String lxr) {
		this.lxr = lxr;
	}

	@Column(name = "sxzq")
	public Integer getSxzq() {
		return this.sxzq;
	}

	public void setSxzq(Integer sxzq) {
		this.sxzq = sxzq;
	}

	@Column(name = "sxje")
	public BigDecimal getSxje() {
		return this.sxje;
	}

	public void setSxje(BigDecimal sxje) {
		this.sxje = sxje;
	}

	@Column(name = "lsje")
	public BigDecimal getLsje() {
		return lsje;
	}

	public void setLsje(BigDecimal lsje) {
		this.lsje = lsje;
	}

	@Column(name = "isUp", length = 1)
	public String getIsUp() {
		return isUp;
	}

	public void setIsUp(String isUp) {
		this.isUp = isUp;
	}

	@Column(name = "postponeDay")
	public int getPostponeDay() {
		return postponeDay;
	}

	public void setPostponeDay(int postponeDay) {
		this.postponeDay = postponeDay;
	}

	@Column(name = "isOther", length = 1)
	public String getIsOther() {
		return isOther;
	}

	public void setIsOther(String isOther) {
		this.isOther = isOther;
	}

	@Column(name = "limitPer")
	public BigDecimal getLimitPer() {
		return limitPer;
	}

	public void setLimitPer(BigDecimal limitPer) {
		this.limitPer = limitPer;
	}

	@Column(name = "limitJe")
	public BigDecimal getLimitJe() {
		return limitJe;
	}

	public void setLimitJe(BigDecimal limitJe) {
		this.limitJe = limitJe;
	}

	@Column(name = "isLocked", length = 1)
	public String getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(String isLocked) {
		this.isLocked = isLocked;
	}

	@Column(name = "isDef", length = 1)
	public String getIsDef() {
		return isDef;
	}

	public void setIsDef(String isDef) {
		this.isDef = isDef;
	}

	@Column(name = "info", length = 50)
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
