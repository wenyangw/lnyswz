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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TCgjh generated by hbm2java
 */
@Entity
@Table(name = "t_cgjh")
public class TCgjh implements java.io.Serializable {

	private String cgjhlsh;
	private Date createTime;
	private Integer createId;
	private String createName;
	private String bmbh;
	private String bmmc;
	private String gysbh;
	private String gysmc;
	private String ckId;
	private String ckmc;
	private String jsfsId;
	private String jsfsmc;
	private BigDecimal hjje;
	private String bz;
	private String isHt;
	private String returnHt;
	private Integer htId;
	private Date htTime;
	private String htName;
	private String isCancel;
	private Integer cancelId;
	private Date cancelTime;
	private String cancelName;
	private String isCompleted;
	private Integer completeId;
	private Date completeTime;
	private String completeName;
	private String needAudit;
	private String isAudit;
	private String isZs;
	private Set<TCgjhDet> TCgjhDets = new HashSet<TCgjhDet>(0);
	private Set<TCgxqDet> TCgxqs = new HashSet<TCgxqDet>(0);
//	private TYwrk TYwrk;
	

	public TCgjh() {
	}

	public TCgjh(String cgjhlsh, Date createTime, Integer createId, String createName, String bmbh, String bmmc,
			Integer ywyId, String ywymc, String gysbh, String gysmc, String ckId, String ckmc, String dhfs, 
			Integer xqsj, String jsfsId, String jsfsmc, BigDecimal hjje, String bz,	
			String isHt, String returnHt, Integer htId, Date htTime, String htName, String isCancel, Date cancelTime,
			Integer cancelId, String cancelName, String isCompleted, Date completeTime, Integer completeId, 
			String completeName, String needAudit, String isAudit, String isZs, Set<TCgjhDet> TCgjhDets, 
			Set<TCgxqDet> TCgxqs) {
		this.cgjhlsh = cgjhlsh;
		this.createTime = createTime;
		this.createId = createId;
		this.createName = createName;
		this.bmbh = bmbh;
		this.bmmc = bmmc;
		this.gysbh = gysbh;
		this.gysmc = gysmc;
		this.ckId = ckId;
		this.ckmc = ckmc;
		this.jsfsId = jsfsId;
		this.hjje = hjje;
		this.bz = bz;
		this.isHt = isHt;
		this.returnHt = returnHt;
		this.htTime = htTime;
		this.htId = htId;
		this.htName = htName;
		this.isCancel = isCancel;
		this.cancelId = cancelId;
		this.cancelTime = cancelTime;
		this.cancelName = cancelName;
		this.isCompleted = isCompleted;
		this.completeId = completeId;
		this.completeTime = completeTime;
		this.completeName = completeName;
		this.needAudit = needAudit;
		this.isAudit = isAudit;
		this.isZs = isZs;
		this.TCgjhDets = TCgjhDets;
		this.TCgxqs = TCgxqs;
//		this.TYwrk = TYwrk;
	}

	@Id
	@Column(name = "cgjhlsh", unique = true, nullable = false, length = 12)
	public String getCgjhlsh() {
		return this.cgjhlsh;
	}

	public void setCgjhlsh(String cgjhlsh) {
		this.cgjhlsh = cgjhlsh;
	}

	@Column(name = "createId", nullable = false)
	public Integer getCreateId() {
		return this.createId;
	}
	
	public void setCreateId(Integer createId) {
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

	@Column(name = "gysbh", nullable = false, length = 8)
	public String getGysbh() {
		return gysbh;
	}

	public void setGysbh(String gysbh) {
		this.gysbh = gysbh;
	}

	@Column(name = "gysmc", nullable = false, length = 100)
	public String getGysmc() {
		return gysmc;
	}

	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
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
	
	@Column(name = "jsfsId", nullable = true, length = 2)
	public String getJsfsId() {
		return this.jsfsId;
	}
	
	public void setJsfsId(String jsfsId) {
		this.jsfsId = jsfsId;
	}
	
	@Column(name = "jsfsmc", nullable = true, length = 20)
	public String getJsfsmc() {
		return jsfsmc;
	}

	public void setJsfsmc(String jsfsmc) {
		this.jsfsmc = jsfsmc;
	}

	@Column(name = "hjje", nullable = false, precision = 18, scale = 6)
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

	@Column(name = "isHt", nullable = false, length = 1)
	public String getIsHt() {
		return this.isHt;
	}
	
	public void setIsHt(String isHt) {
		this.isHt = isHt;
	}
	
	@Column(name = "returnHt", length = 1)
	public String getReturnHt() {
		return this.returnHt;
	}
	
	public void setReturnHt(String returnHt) {
		this.returnHt = returnHt;
	}
	
	@Column(name = "htId")
	public Integer getHtId() {
		return this.htId;
	}
	
	public void setHtId(Integer htId) {
		this.htId = htId;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "htTime", length = 23)
	public Date getHtTime() {
		return this.htTime;
	}
	
	public void setHtTime(Date htTime) {
		this.htTime = htTime;
	}
	
	@Column(name = "htName", nullable = true, length = 20)
	public String getHtName() {
		return htName;
	}
	
	public void setHtName(String htName) {
		this.htName = htName;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cancelTime", length = 23)
	public Date getCancelTime() {
		return this.cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}
	
	@Column(name = "cancelName", nullable = true, length = 20)
	public String getCancelName() {
		return cancelName;
	}
	
	public void setCancelName(String cancelName) {
		this.cancelName = cancelName;
	}

	@Column(name = "isCompleted", nullable = false, length = 1)
	public String getIsCompleted() {
		return this.isCompleted;
	}

	public void setIsCompleted(String isCompleted) {
		this.isCompleted = isCompleted;
	}

	@Column(name = "completeId")
	public Integer getCompleteId() {
		return this.completeId;
	}
	
	public void setCompleteId(Integer completeId) {
		this.completeId = completeId;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "completeTime", length = 23)
	public Date getCompleteTime() {
		return this.completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	@Column(name = "completeName", length = 20)
	public String getCompleteName() {
		return completeName;
	}
	
	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}
	
	@Column(name = "needAudit", nullable = false, length = 1)
	public String getNeedAudit() {
		return this.needAudit;
	}

	public void setNeedAudit(String needAudit) {
		this.needAudit = needAudit;
	}

	@Column(name = "isAudit", nullable = false, length = 1)
	public String getIsAudit() {
		return this.isAudit;
	}

	public void setIsAudit(String isAudit) {
		this.isAudit = isAudit;
	}
	
	@Column(name = "isZs", nullable = false, length = 1)
	public String getIsZs() {
		return this.isZs;
	}

	public void setIsZs(String isZs) {
		this.isZs = isZs;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "TCgjh", cascade=CascadeType.ALL)
	public Set<TCgjhDet> getTCgjhDets() {
		return this.TCgjhDets;
	}

	public void setTCgjhDets(Set<TCgjhDet> TCgjhDets) {
		this.TCgjhDets = TCgjhDets;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "TCgjh")
	public Set<TCgxqDet> getTCgxqs() {
		return this.TCgxqs;
	}

	public void setTCgxqs(Set<TCgxqDet> TCgxqs) {
		this.TCgxqs = TCgxqs;
	}
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "ywrklsh")
//	public TYwrk getTYwrk() {
//		return this.TYwrk;
//	}
//
//	public void setTYwrk(TYwrk TYwrk) {
//		this.TYwrk = TYwrk;
//	}

}
