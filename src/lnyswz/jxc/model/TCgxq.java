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
 * TCgxq generated by hbm2java
 */
@Entity
@Table(name = "t_cgxq")
public class TCgxq implements java.io.Serializable {
	
	private String cgxqlsh;
	private int createId;
	private Date createTime;
	private String createName;
	private String bmbh;
	private String bmmc;
	private int ywyId;
	private String ywymc;
	private String gysbh;
	private String gysmc;
	private String khbh;
	private String khmc;
	private String dhfs;
	private String lxr;
	private String shdz;
	private Date dhsj;
	private Date xqsj;
	private String jsfsId;
	private String jsfsmc;
	private BigDecimal hjje;
	private String bz;
	private String isLs;
	private String isZs;
	private String needAudit;
	private String isAudit;
	
	private Set<TCgxqDet> TCgxqDets = new HashSet<TCgxqDet>(0);
	
	public TCgxq() {
	}
	
	public TCgxq(String cgxqlsh, int createId, Date createTime, String createName, String bmbh,
			String bmmc, int ywyId, String ywymc, String gysbh, String gysmc, String khbh, String khmc, String dhfs, String lxr,
			String shdz, Date dhsj, Date xqsj, String jsfsId, String jsfsmc, BigDecimal hjje,
			String bz, String isLs, String isZs, String needAudit, String isAudit, Set<TCgxqDet> TCgxqDets) {
		this.cgxqlsh = cgxqlsh;
		this.createId = createId;
		this.createTime = createTime;
		this.createName = createName;
		this.bmbh = bmbh;
		this.bmmc = bmmc;
		this.ywyId = ywyId;
		this.ywymc = ywymc;
		this.gysbh = gysbh;
		this.gysmc = gysmc;
		this.khbh = khbh;
		this.khmc = khmc;
		this.dhfs = dhfs;
		this.lxr = lxr;
		this.shdz = shdz;
		this.dhsj = dhsj;
		this.xqsj = xqsj;
		this.jsfsId = jsfsId;
		this.jsfsmc = jsfsmc;
		this.hjje = hjje;
		this.bz = bz;
		this.isLs = isLs;
		this.isZs = isZs;
		this.needAudit = needAudit;
		this.isAudit = isAudit;
		this.TCgxqDets = TCgxqDets;
		
	}

	@Id
	@Column(name = "cgxqlsh", unique = true, nullable = false, length = 12)
	public String getCgxqlsh() {
		return this.cgxqlsh;
	}

	public void setCgxqlsh(String cgxqlsh) {
		this.cgxqlsh = cgxqlsh;
	}

	@Column(name = "bmbh", nullable = false, length = 2)
	public String getBmbh() {
		return bmbh;
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
	
	@Column(name = "createId", nullable = false)
	public int getCreateId() {
		return createId;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime", nullable = false, length = 23)
	public Date getCreateTime() {
		return this.createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	
	@Column(name = "khbh", nullable = true, length = 8)
	public String getKhbh() {
		return khbh;
	}
	
	public void setKhbh(String khbh) {
		this.khbh = khbh;
	}
	
	@Column(name = "khmc", nullable = true, length = 100)
	public String getKhmc() {
		return khmc;
	}
	
	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	@Column(name = "ywyId")
	public int getYwyId() {
		return this.ywyId;
	}

	public void setYwyId(int ywyId) {
		this.ywyId = ywyId;
	}

	@Column(name = "ywymc", length = 20)
	public String getYwymc() {
		return ywymc;
	}
	
	public void setYwymc(String ywymc) {
		this.ywymc = ywymc;
	}
	
	@Column(name = "dhfs", length = 50)
	public String getDhfs() {
		return this.dhfs;
	}

	public void setDhfs(String dhfs) {
		this.dhfs = dhfs;
	}

	@Column(name = "lxr", length = 50)
	public String getLxr() {
		return lxr;
	}

	public void setLxr(String lxr) {
		this.lxr = lxr;
	}

	@Column(name = "shdz", length = 50)
	public String getShdz() {
		return this.shdz;
	}

	public void setShdz(String shdz) {
		this.shdz = shdz;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dhsj", length = 23)
	public Date getDhsj() {
		return this.dhsj;
	}

	public void setDhsj(Date dhsj) {
		this.dhsj = dhsj;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "xqsj")
	public Date getXqsj() {
		return this.xqsj;
	}

	public void setXqsj(Date xqsj) {
		this.xqsj = xqsj;
	}

	@Column(name = "jsfsId", length = 2)
	public String getJsfsId() {
		return this.jsfsId;
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
	
	@Column(name = "hjje", nullable = true, precision = 18, scale = 6)
	public BigDecimal getHjje() {
		return hjje;
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

	@Column(name = "isLs", nullable = false, length = 1)
	public String getIsLs() {
		return isLs;
	}

	public void setIsLs(String isLs) {
		this.isLs = isLs;
	}
	
	@Column(name = "isZs", nullable = false, length = 1)
	public String getIsZs() {
		return isZs;
	}

	public void setIsZs(String isZs) {
		this.isZs = isZs;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "TCgxq", cascade=CascadeType.ALL)
	public Set<TCgxqDet> getTCgxqDets() {
		return this.TCgxqDets;
	}

	public void setTCgxqDets(Set<TCgxqDet> TCgxqDets) {
		this.TCgxqDets = TCgxqDets;
	}

}
