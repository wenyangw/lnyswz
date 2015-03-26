package lnyswz.jxc.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "t_splb")
public class TSplb implements java.io.Serializable {

	private int id;
	private TSpdl TSpdl;
	private String splbmc;
	private String idFrom;
	private String idTo;
	private Set<TSpdw> TSpdws = new HashSet<TSpdw>(0);

	public TSplb() {
	}

	public TSplb(int id, TSpdl TSpdl, String splbmc, String idFrom, String idTo) {
		this.id = id;
		this.TSpdl = TSpdl;
		this.splbmc = splbmc;
		this.idFrom = idFrom;
		this.idTo = idTo;
	}

	public TSplb(int id, TSpdl TSpdl, String splbmc, String idFrom,
			String idTo, Set<TSpdw> TSpdws) {
		this.id = id;
		this.TSpdl = TSpdl;
		this.splbmc = splbmc;
		this.idFrom = idFrom;
		this.idTo = idTo;
		this.TSpdws = TSpdws;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "spdlId", nullable = false)
	public TSpdl getTSpdl() {
		return this.TSpdl;
	}

	public void setTSpdl(TSpdl TSpdl) {
		this.TSpdl = TSpdl;
	}

	@Column(name = "splbmc", nullable = false, length = 50)
	public String getSplbmc() {
		return this.splbmc;
	}

	public void setSplbmc(String splbmc) {
		this.splbmc = splbmc;
	}

	@Column(name = "idFrom", nullable = false, length = 2)
	public String getIdFrom() {
		return this.idFrom;
	}

	public void setIdFrom(String idFrom) {
		this.idFrom = idFrom;
	}

	@Column(name = "idTo", nullable = false, length = 2)
	public String getIdTo() {
		return this.idTo;
	}

	public void setIdTo(String idTo) {
		this.idTo = idTo;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "TSplb")
	public Set<TSpdw> getTSpdws() {
		return this.TSpdws;
	}

	public void setTSpdws(Set<TSpdw> TSpdws) {
		this.TSpdws = TSpdws;
	}
}
