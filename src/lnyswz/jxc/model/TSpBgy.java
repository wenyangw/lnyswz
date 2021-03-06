package lnyswz.jxc.model;
// Generated 2013-8-14 10:42:53 by Hibernate Tools 4.0.0

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * TUser generated by hbm2java
 */
@Entity
@Table(name = "t_sp_bgy")
public class TSpBgy implements java.io.Serializable {

	private int id;
	private String depId;
	private String depName;
	private String ckId;
	private String ckmc;
	private int bgyId;
	private String bgyName;
	private String spbh;
	private String spmc;
	private String spcd;

	public TSpBgy() {
	}

	public TSpBgy(int id, String depId, String depName, String ckId, String ckmc, int bgyId, String bgyName, 
			String spbh, String spmc, String spcd) {
		this.id = id;
		this.depId = depId;
		this.depName = depName;
		this.ckId = ckId;
		this.ckmc =ckmc;
		this.bgyId = bgyId;
		this.bgyName = bgyName;
		this.spbh = spbh;
		this.spmc = spmc;
		this.spcd = spcd;
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

	@Column(name = "depId", nullable = false, length = 2)
	public String getDepId() {
		return this.depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}
	
	@Column(name = "depName", nullable = false, length = 20)
	public String getDepName() {
		return this.depName;
	}
	
	public void setDepName(String depName) {
		this.depName = depName;
	}

	@Column(name = "ckId", nullable = false, length = 2)
	public String getCkId() {
		return this.ckId;
	}

	public void setCkId(String ckId) {
		this.ckId = ckId;
	}
	
	@Column(name = "ckmc", nullable = false, length = 20)
	public String getCkmc() {
		return this.ckmc;
	}
	
	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}
	
	@Column(name = "bgyId", nullable = false)
	public int getBgyId() {
		return bgyId;
	}
	
	public void setBgyId(int bgyId) {
		this.bgyId = bgyId;
	}
	
	@Column(name = "bgyName", nullable = false, length = 20)
	public String getBgyName() {
		return bgyName;
	}
	
	public void setBgyName(String bgyName) {
		this.bgyName = bgyName;
	}

	@Column(name = "spbh", nullable = true, length = 7)
	public String getSpbh() {
		return this.spbh;
	}

	public void setSpbh(String spbh) {
		this.spbh = spbh;
	}
	
	@Column(name = "spmc", nullable = true, length = 100)
	public String getSpmc() {
		return this.spmc;
	}
	
	public void setSpmc(String spmc) {
		this.spmc = spmc;
	}
	
	@Column(name = "spcd", nullable = true, length = 20)
	public String getSpcd() {
		return this.spcd;
	}
	
	public void setSpcd(String spcd) {
		this.spcd = spcd;
	}

}
