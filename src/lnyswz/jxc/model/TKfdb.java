package lnyswz.jxc.model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * TKfdb generated by hbm2java
 */
@Entity
@Table(name = "t_kfdb")
public class TKfdb implements java.io.Serializable {

	private String kfdblsh;
	private Date createTime;
	private int createId;
	private String createName;
	private String bmbh;
	private String bmmc;
	private String ckIdF;
	private String ckmcF;
	private String ckIdT;
	private String ckmcT;
	private String bz;
	private String cjKfdblsh;
	private String isCj;
	private Date cjTime;
	private Integer cjId;
	private String cjName;
	
	private Set<TKfdbDet> TKfdbDets = new HashSet<TKfdbDet>(0);
	
	public TKfdb() {
	}

	public TKfdb(String kfdblsh, Date createTime, int createId, String createName, String bmbh, String bmmc, String ckIdF, String ckmcF,
                 String ckIdT, String ckmcT, String bz, String cjKfdblsh, String isCj, Date cjTime, Integer cjId, String cjName,
                 Set<TKfdbDet> TKfdbDets) {
		this.kfdblsh = kfdblsh;
		this.createTime = createTime;
		this.createId = createId;
		this.createName = createName;
		this.bmbh = bmbh;
		this.bmmc = bmmc;
		this.ckIdF = ckIdF;
		this.ckmcF = ckmcF;
		this.ckIdT = ckIdT;
		this.ckmcT = ckmcT;
		this.bz = bz;
		this.cjKfdblsh = cjKfdblsh;
		this.isCj = isCj;
		this.cjTime = cjTime;
		this.cjId = cjId;
		this.cjName = cjName;
		this.TKfdbDets = TKfdbDets;
	}

	@Id
	@Column(name = "kfdblsh", unique = true, nullable = false, length = 12)
	public String getKfdblsh() {
		return this.kfdblsh;
	}

	public void setKfdblsh(String kfdblsh) {
		this.kfdblsh = kfdblsh;
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

	@Column(name = "ckIdF", nullable = false, length = 2)
	public String getCkIdF() {
		return ckIdF;
	}

	public void setCkIdF(String ckIdF) {
		this.ckIdF = ckIdF;
	}

	@Column(name = "ckmcF", nullable = false, length = 20)
	public String getCkmcF() {
		return ckmcF;
	}

	public void setCkmcF(String ckmcF) {
		this.ckmcF = ckmcF;
	}
	
	@Column(name = "ckIdT", nullable = false, length = 2)
	public String getCkIdT() {
		return ckIdT;
	}

	public void setCkIdT(String ckIdT) {
		this.ckIdT = ckIdT;
	}

	@Column(name = "ckmcT", nullable = false, length = 20)
	public String getCkmcT() {
		return ckmcT;
	}

	public void setCkmcT(String ckmcT) {
		this.ckmcT = ckmcT;
	}
	
	@Column(name = "bz", length = 50)
	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	@Column(name = "cjKfdblsh", nullable = true, length = 12)
	public String getCjKfdblsh() {
		return cjKfdblsh;
	}

	public void setCjKfdblsh(String cjKfdblsh) {
		this.cjKfdblsh = cjKfdblsh;
	}

	@Column(name = "isCj", length = 1)
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "TKfdb", cascade=CascadeType.ALL)
	public Set<TKfdbDet> getTKfdbDets() {
		return this.TKfdbDets;
	}

	public void setTKfdbDets(Set<TKfdbDet> TKfdbDets) {
		this.TKfdbDets = TKfdbDets;
	}
	
}