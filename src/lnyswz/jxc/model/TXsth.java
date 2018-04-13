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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TXsth generated by hbm2java
 */
@Entity
@Table(name = "t_xsth")
public class TXsth implements java.io.Serializable {

	private String xsthlsh;
	private Date createTime;
	private int createId;
	private String createName;
	private String bmbh;
	private String bmmc;
	private String isSx;
	private String khbh;
	private String khmc;
	private String ckId;
	private String ckmc;
	private String isFh;
	private String fhId;
	private String fhmc;
	private String isFhth;
	private int ywyId;
	private String ywymc;
	private String jsfsId;
	private String jsfsmc;
	private String thfs;
	private String shdz;
	private String thr;
	private String ch;
	private BigDecimal hjje;
	private BigDecimal hjsl;
	private String isLs;
	private String bookmc;
	private String bz;
	private String isZs;
	private String isCancel;
	private String cjXsthlsh;
	private Integer cancelId;
	private Date cancelTime;
	private String cancelName;
	private String locked;
	private Integer lockId;
	private Date lockTime;
	private String lockName;
	private String toFp;
	private String fromFp;
	private String isKp;
	private Integer kpId;
	private Date kpTime;
	private String kpName;
	private String fromRk;
	private String isHk;
	private Integer hkId;
	private Date hkTime;
	private String needAudit;
	private String isAudit;
	private BigDecimal ysfy;
	private BigDecimal yysfy;
	private int payDays;
	private String verifyCode;
	private String out;
	private Integer outId;
	private String outName;
	private Date outTime;
	private String sended;
	private Integer sendId;
	private String sendName;
	private Date sendTime;
	private String isFp;

	private Set<TXsthDet> TXsthDets = new HashSet<TXsthDet>(0);
	
	private Set<TYwrkDet> TYwrks = new HashSet<TYwrkDet>(0);
	
	public TXsth() {
	}

	public TXsth(String xsthlsh, Date createTime, int createId, String createName, String bmbh, String bmmc, String isSx,
			String khbh, String khmc, String ckId, String ckmc, String isFh, String fhId, String fhmc, String isFhth, 
			int ywyId, String ywymc, String jsfsId, String jsfsmc, String thfs, String shdz, String thr, String ch, 
			BigDecimal hjje, BigDecimal hjsl, String isLs, String bookmc, String bz, String isZs, String isCancel, 
			String cjXsthlsh, Integer cancelId, Date cancelTime, String cancelName, Integer lockId, Date lockTime, 
			String lockName, String locked, String toFp, String fromFp, String isKp, Integer kpId, Date kpTime, String kpName, String fromRk, String isHk,
			Integer hkId, Date hkTime, String needAudit, String isAudit, BigDecimal ysfy, BigDecimal yysfy,
			int payDays, String verifyCode, String out, Integer outId, String outName, Date outTime, String sended, Integer sendId, String sendName,
			Date sendTime, String isFp, Set<TXsthDet> TXsthDets, Set<TYwrkDet> TYwrks) {
		this.xsthlsh = xsthlsh;
		this.createTime = createTime;
		this.createId = createId;
		this.bmbh = bmbh;
		this.isSx = isSx;
		this.khbh = khbh;
		this.khmc = khmc;
		this.ckId = ckId;
		this.ckmc = ckmc;
		this.isFh = isFh;
		this.fhId = fhId;
		this.fhmc = fhmc;
		this.isFhth = isFhth;
		this.ywyId = ywyId;
		this.ywymc = ywymc;
		this.jsfsId = jsfsId;
		this.jsfsmc = jsfsmc;
		this.thfs = thfs;
		this.shdz = shdz;
		this.thr = thr;
		this.ch = ch;
		this.hjje = hjje;
		this.hjsl = hjsl;
		this.isLs = isLs;
		this.bookmc = bookmc;
		this.bz = bz;
		this.isZs = isZs;
		this.isCancel = isCancel;
		this.cjXsthlsh = cjXsthlsh;
		this.cancelId = cancelId;
		this.cancelTime = cancelTime;
		this.cancelName = cancelName;
		this.locked = locked;
		this.lockId = lockId;
		this.lockTime = lockTime;
		this.lockName = lockName;
		this.toFp = toFp;
		this.fromFp = fromFp;
		this.isKp = isKp;
		this.kpId = kpId;
		this.kpTime = kpTime;
		this.kpName = kpName;
		this.fromFp = fromRk;
		this.isHk = isHk;
		this.hkId = hkId;
		this.hkTime = hkTime;
		this.needAudit = needAudit;
		this.isAudit = isAudit;
		this.ysfy = ysfy;
		this.yysfy = yysfy;
		this.payDays = payDays;
		this.verifyCode = verifyCode;
		this.out = out;
		this.outId = outId;
		this.outName = outName;
		this.outTime = outTime;
		this.sended = sended;
		this.sendId = sendId;
		this.sendName = sendName;
		this.isFp = isFp;
		this.sendTime = sendTime;
		this.TXsthDets = TXsthDets;
		this.TYwrks = TYwrks;
	}

	@Id
	@Column(name = "xsthlsh", unique = true, nullable = false, length = 12)
	public String getXsthlsh() {
		return this.xsthlsh;
	}

	public void setXsthlsh(String xsthlsh) {
		this.xsthlsh = xsthlsh;
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

	@Column(name = "isFh", nullable = false, length = 1)
	public String getIsFh() {
		return isFh;
	}

	public void setIsFh(String isFh) {
		this.isFh = isFh;
	}

	@Column(name = "fhId", length = 2)
	public String getFhId() {
		return fhId;
	}

	public void setFhId(String fhId) {
		this.fhId = fhId;
	}

	@Column(name = "fhmc", length = 20)
	public String getFhmc() {
		return fhmc;
	}

	public void setFhmc(String fhmc) {
		this.fhmc = fhmc;
	}

	@Column(name = "isFhth", nullable = false, length = 1)
	public String getIsFhth() {
		return isFhth;
	}

	public void setIsFhth(String isFhth) {
		this.isFhth = isFhth;
	}

	
	@Column(name = "isSx", nullable = false, length = 1)
	public String getIsSx() {
		return isSx;
	}

	public void setIsSx(String isSx) {
		this.isSx = isSx;
	}

	@Column(name = "khbh", length = 8)
	public String getKhbh() {
		return khbh;
	}

	public void setKhbh(String khbh) {
		this.khbh = khbh;
	}

	@Column(name = "khmc", length = 100)
	public String getKhmc() {
		return khmc;
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

	@Column(name = "jsfsId", length = 2)
	public String getJsfsId() {
		return jsfsId;
	}

	public void setJsfsId(String jsfsId) {
		this.jsfsId = jsfsId;
	}

	@Column(name = "jsfsmc", length = 20)
	public String getJsfsmc() {
		return jsfsmc;
	}

	public void setJsfsmc(String jsfsmc) {
		this.jsfsmc = jsfsmc;
	}

	@Column(name = "thfs", nullable = false, length = 1)
	public String getThfs() {
		return thfs;
	}

	public void setThfs(String thfs) {
		this.thfs = thfs;
	}

	@Column(name = "shdz", length = 100)
	public String getShdz() {
		return shdz;
	}

	public void setShdz(String shdz) {
		this.shdz = shdz;
	}

	@Column(name = "thr", length = 20)
	public String getThr() {
		return thr;
	}
	
	public void setThr(String thr) {
		this.thr = thr;
	}
	
	@Column(name = "ch", length = 20)
	public String getCh() {
		return ch;
	}

	public void setCh(String ch) {
		this.ch = ch;
	}

	@Column(name = "hjje", precision = 18, scale = 4)
	public BigDecimal getHjje() {
		return this.hjje;
	}

	public void setHjje(BigDecimal hjje) {
		this.hjje = hjje;
	}

	@Column(name = "hjsl", precision = 18, scale = 3)
	public BigDecimal getHjsl() {
		return hjsl;
	}

	public void setHjsl(BigDecimal hjsl) {
		this.hjsl = hjsl;
	}
	
	@Column(name = "isLs", nullable = false, length = 1)
	public String getIsLs() {
		return isLs;
	}

	public void setIsLs(String isLs) {
		this.isLs = isLs;
	}

	@Column(name = "bookmc", length = 100)
	public String getBookmc() {
		return bookmc;
	}

	public void setBookmc(String bookmc) {
		this.bookmc = bookmc;
	}

	@Column(name = "bz", length = 100)
	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	@Column(name = "isZs", nullable = false, length = 1)
	public String getIsZs() {
		return isZs;
	}

	public void setIsZs(String isZs) {
		this.isZs = isZs;
	}

	@Column(name = "isCancel", nullable = false, length = 1)
	public String getIsCancel() {
		return this.isCancel;
	}

	public void setIsCancel(String isCancel) {
		this.isCancel = isCancel;
	}
	
	@Column(name = "cjXsthlsh", length = 20)
	public String getCjXsthlsh() {
		return cjXsthlsh;
	}

	public void setCjXsthlsh(String cjXsthlsh) {
		this.cjXsthlsh = cjXsthlsh;
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

	@Column(name = "cancelName", length = 20)
	public String getCancelName() {
		return cancelName;
	}

	public void setCancelName(String cancelName) {
		this.cancelName = cancelName;
	}

	@Column(name = "locked", nullable = false, length = 1)
	public String getLocked() {
		return this.locked;
	}
	
	public void setLocked(String locked) {
		this.locked = locked;
	}
	
	@Column(name = "lockId")
	public Integer getLockId() {
		return lockId;
	}

	public void setLockId(Integer lockId) {
		this.lockId = lockId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lockTime", length = 23)
	public Date getLockTime() {
		return lockTime;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	@Column(name = "lockName", length = 20)
	public String getLockName() {
		return lockName;
	}

	public void setLockName(String lockName) {
		this.lockName = lockName;
	}

	@Column(name = "toFp", nullable = false, length = 1)
	public String getToFp() {
		return this.toFp;
	}
	
	public void setToFp(String toFp) {
		this.toFp = toFp;
	}
	
	@Column(name = "fromFp", nullable = false, length = 1)
	public String getFromFp() {
		return this.fromFp;
	}
	
	public void setFromFp(String fromFp) {
		this.fromFp = fromFp;
	}
	
	@Column(name = "isKp", nullable = false, length = 1)
	public String getIsKp() {
		return this.isKp;
	}

	public void setIsKp(String isKp) {
		this.isKp = isKp;
	}

	@Column(name = "kpId")
	public Integer getKpId() {
		return this.kpId;
	}

	public void setKpId(Integer kpId) {
		this.kpId = kpId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "kpTime", length = 23)
	public Date getKpTime() {
		return this.kpTime;
	}

	public void setKpTime(Date kpTime) {
		this.kpTime = kpTime;
	}

	@Column(name = "kpName", length = 20)
	public String getKpName() {
		return kpName;
	}

	public void setKpName(String kpName) {
		this.kpName = kpName;
	}

	@Column(name = "fromRk", nullable = false, length = 1)
	public String getFromRk() {
		return fromRk;
	}

	public void setFromRk(String fromRk) {
		this.fromRk = fromRk;
	}

	@Column(name = "isHk", nullable = false, length = 1)
	public String getIsHk() {
		return this.isHk;
	}

	public void setIsHk(String isHk) {
		this.isHk = isHk;
	}

	@Column(name = "hkId")
	public Integer getHkId() {
		return this.hkId;
	}

	public void setHkId(Integer hkId) {
		this.hkId = hkId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "hkTime", length = 23)
	public Date getHkTime() {
		return this.hkTime;
	}

	public void setHkTime(Date hkTime) {
		this.hkTime = hkTime;
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
	
	@Column(name = "ysfy", precision = 18, scale = 2)
	public BigDecimal getYsfy() {
		return ysfy;
	}

	public void setYsfy(BigDecimal ysfy) {
		this.ysfy = ysfy;
	}

	@Column(name = "yysfy", precision = 18, scale = 2)
	public BigDecimal getYysfy() {
		return yysfy;
	}

	public void setYysfy(BigDecimal yysfy) {
		this.yysfy = yysfy;
	}

	@Column(name = "payDays")
	public int getPayDays() {
		return payDays;
	}

	public void setPayDays(int payDays) {
		this.payDays = payDays;
	}

	@Column(name = "verifyCode")
	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	@Column(name = "out", nullable = false, length = 1)
	public String getOut() {
		return out;
	}

	public void setOut(String out) {
		this.out = out;
	}

	@Column(name = "outId")
	public Integer getOutId() {
		return outId;
	}

	public void setOutId(Integer outId) {
		this.outId = outId;
	}

	@Column(name = "outName", length = 20)
	public String getOutName() {
		return outName;
	}

	public void setOutName(String outName) {
		this.outName = outName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "outTime", length = 23)
	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

	@Column(name = "sended", nullable = false, length = 1)
	public String getSended() {
		return sended;
	}

	public void setSended(String sended) {
		this.sended = sended;
	}

	@Column(name = "sendId")
	public Integer getSendId() {
		return sendId;
	}

	public void setSendId(Integer sendId) {
		this.sendId = sendId;
	}

	@Column(name = "sendName", length = 20)
	public String getSendName() {
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "sendTime", length = 23)
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	@Column(name = "isFp", length = 1)
	public String getIsFp() {
		return isFp;
	}

	public void setIsFp(String isFp) {
		this.isFp = isFp;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "TXsth", cascade=CascadeType.ALL)
	public Set<TXsthDet> getTXsthDets() {
		return TXsthDets;
	}

	public void setTXsthDets(Set<TXsthDet> tXsthDets) {
		TXsthDets = tXsthDets;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "t_ywrk_xsth", joinColumns = { @JoinColumn(name = "xsthlsh", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ywrkdetId", nullable = false, updatable = false) })
	public Set<TYwrkDet> getTYwrks() {
		return this.TYwrks;
	}
	
	public void setTYwrks(Set<TYwrkDet> TYwrks) {
		this.TYwrks = TYwrks;
	}
	
//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "TXsth")
//	public Set<TKfck> getTKfcks() {
//		return this.TKfcks;
//	}
//
//	public void setTKfcks(Set<TKfck> TKfcks) {
//		this.TKfcks = TKfcks;
//	}

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "xskplsh")
//	public TXskp getTXskp() {
//		return TXskp;
//	}
//
//	public void setTXskp(TXskp tXskp) {
//		TXskp = tXskp;
//	}
	
}
