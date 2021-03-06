package lnyswz.jxc.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * TBgrk generated by hbm2java
 */
@Entity
@Table(name = "t_bgrk")
@DynamicUpdate(true)
public class TBgrk implements java.io.Serializable {

	private String bgrklsh;
	private Date createTime;
	private int createId;
	private String createName;
	private String bmbh;
	private String bmmc;
	private String gysbh;
	private String gysmc;
	private String bz;
	private String lsh;
	private String cjBgrklsh;
	private String isCj;
	private Date cjTime;
	private Integer cjId;
	private String cjName;
	private Set<TBgrkDet> TBgrkDets = new HashSet<TBgrkDet>(0);

	public TBgrk() {
	}

	public TBgrk(String bgcklsh, Date createTime, int createId, String createName, String bmbh, String bmmc, String gysbh, String gysmc,
                 String bz, String lsh, String cjBgrklsh, String isCj, Date cjTime, Integer cjId, String cjName, Set<TBgrkDet> TBgrkDets) {
		this.bgrklsh = bgrklsh;
		this.createTime = createTime;
		this.createId = createId;
		this.createName = createName;
		this.bmbh = bmbh;
		this.bmmc = bmmc;
		this.gysbh = gysbh;
		this.gysmc = gysmc;
		this.bz = bz;
		this.lsh = lsh;
		this.cjBgrklsh = cjBgrklsh;
		this.isCj = isCj;
		this.cjTime = cjTime;
		this.cjId = cjId;
		this.cjName = cjName;
		this.TBgrkDets = TBgrkDets;
	}

	@Id
	@Column(name = "bgrklsh", unique = true, nullable = false, length = 12)
	public String getBgrklsh() {
		return this.bgrklsh;
	}

	public void setBgrklsh(String bgrklsh) {
		this.bgrklsh = bgrklsh;
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

	@Column(name = "gysbh", length = 8)
	public String getGysbh() {
		return gysbh;
	}

	public void setGysbh(String gysbh) {
		this.gysbh = gysbh;
	}

	@Column(name = "gysmc", length = 100)
	public String getGysmc() {
		return gysmc;
	}

	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}

	@Column(name = "bz", length = 100)
	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	@Column(name = "lsh", length = 12)
	public String getLsh() {
		return lsh;
	}

	public void setLsh(String lsh) {
		this.lsh = lsh;
	}

	@Column(name = "cjBgrklsh", length = 12)
	public String getCjBgrklsh() {
		return cjBgrklsh;
	}

	public void setCjBgrklsh(String cjBgrklsh) {
		this.cjBgrklsh = cjBgrklsh;
	}

	@Column(name = "isCj", nullable = false, length = 1)
	public String getIsCj() {
		return this.isCj;
	}

	public void setIsCj(String isCj) {
		this.isCj = isCj;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cjTime", length = 23)
	public Date getCjTime() {
		return this.cjTime;
	}

	public void setCjTime(Date cjTime) {
		this.cjTime = cjTime;
	}

	@Column(name = "cjId")
	public Integer getCjId() {
		return this.cjId;
	}

	public void setCjId(Integer cjId) {
		this.cjId = cjId;
	}

	@Column(name = "cjName", length = 20)
	public String getCjName() {
		return cjName;
	}

	public void setCjName(String cjName) {
		this.cjName = cjName;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "TBgrk", cascade=CascadeType.ALL)
	public Set<TBgrkDet> getTBgrkDets() {
		return this.TBgrkDets;
	}

	public void setTBgrkDets(Set<TBgrkDet> TBgrkDets) {
		this.TBgrkDets = TBgrkDets;
	}
}
