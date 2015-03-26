package lnyswz.jxc.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * TAudit generated by hbm2java
 */
@Entity
@Table(name = "t_audit")
public class TAudit implements java.io.Serializable {

	private int id;
	private String auditName;
	private TYwlx TYwlx;

	public TAudit() {
	}

	public TAudit(int id, String auditName, TYwlx TYwlx) {
		this.id = id;
		this.auditName = auditName;
		this.TYwlx = TYwlx;
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

	@Column(name = "auditName", nullable = false, length = 20)
	public String getAuditName() {
		return this.auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public TYwlx getTYwlx() {
		return TYwlx;
	}

	public void setTYwlx(TYwlx tYwlx) {
		TYwlx = tYwlx;
	}

}
