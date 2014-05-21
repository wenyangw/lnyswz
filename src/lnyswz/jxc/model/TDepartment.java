package lnyswz.jxc.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * TDepartment generated by hbm2java
 */
@Entity
@Table(name = "t_department")
public class TDepartment implements java.io.Serializable {

	private String id;
	private String depName;
	private int orderNum;
	private Set<TUser> TUsers = new HashSet<TUser>(0);
	private Set<TSpdl> TSpdls = new HashSet<TSpdl>(0);
	

	public TDepartment() {
	}

	public TDepartment(String id, String depName, int orderNum, Set<TUser> TUsers, Set<TSpdl> TSpdls) {
		this.id = id;
		this.depName = depName;
		this.orderNum = orderNum;
		this.TUsers = TUsers;
		this.TSpdls = TSpdls;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 36)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "depName", unique = true, nullable = false, length = 100)
	public String getDepName() {
		return this.depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}
	
	@Column(name = "orderNum", nullable = false)
	public int getOrderNum() {
		return this.orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "TDepartment")
	public Set<TUser> getTUsers() {
		return this.TUsers;
	}

	public void setTUsers(Set<TUser> TUsers) {
		this.TUsers = TUsers;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "TDepartments")
	public Set<TSpdl> getTSpdls() {
		return TSpdls;
	}

	public void setTSpdls(Set<TSpdl> tSpdls) {
		TSpdls = tSpdls;
	}

}
