package lnyswz.jxc.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * TYwlx generated by hbm2java
 */
@Entity
@Table(name = "t_ywlx")
public class TYwlx implements java.io.Serializable {

	private String id;
	private String ywlxmc;

	public TYwlx() {
	}

	public TYwlx(String id, String ywlxmc) {
		this.id = id;
		this.ywlxmc = ywlxmc;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 2)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "ywlxmc", nullable = false, length = 50)
	public String getYwlxmc() {
		return this.ywlxmc;
	}

	public void setYwlxmc(String ywlxmc) {
		this.ywlxmc = ywlxmc;
	}

}
