package lnyswz.jxc.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lnyswz.jxc.bean.Menu;

/**
 * TSpDet generated by hbm2java
 */
@Entity
@Table(name = "t_sp_det")
public class TSpDet implements java.io.Serializable {

	private int id;
	private TSp TSp;
	private TDepartment TDepartment;
	private BigDecimal maxKc;
	private BigDecimal minKc;
	private BigDecimal xsdj;
	private BigDecimal specXsdj;
	private BigDecimal limitXsdj;
	private BigDecimal lastRkdj;

	public TSpDet() {
	}

	public TSpDet(int id, TSp TSp, TDepartment TDepartment, BigDecimal maxKc, BigDecimal minKc,
			BigDecimal xsdj, BigDecimal specXsdj, BigDecimal limitXsdj, BigDecimal lastRkdj) {
		this.id = id;
		this.TSp = TSp;
		this.TDepartment = TDepartment;
		this.maxKc = maxKc;
		this.minKc = minKc;
		this.xsdj = xsdj;
		this.specXsdj = specXsdj;
		this.limitXsdj = limitXsdj;
		this.lastRkdj = lastRkdj;
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
	@JoinColumn(name = "spbh", nullable = false)
	public TSp getTSp() {
		return this.TSp;
	}

	public void setTSp(TSp TSp) {
		this.TSp = TSp;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "depId", nullable = false)
	public TDepartment getTDepartment() {
		return TDepartment;
	}

	public void setTDepartment(TDepartment tDepartment) {
		TDepartment = tDepartment;
	}

	@Column(name = "maxKc")
	public BigDecimal getMaxKc() {
		return this.maxKc;
	}

	public void setMaxKc(BigDecimal maxKc) {
		this.maxKc = maxKc;
	}
	
	@Column(name = "minKc")
	public BigDecimal getMinKc() {
		return this.minKc;
	}

	public void setMinKc(BigDecimal minKc) {
		this.minKc = minKc;
	}
	
	@Column(name = "xsdj")
	public BigDecimal getXsdj() {
		return xsdj;
	}

	public void setXsdj(BigDecimal xsdj) {
		this.xsdj = xsdj;
	}

	@Column(name = "specXsdj")
	public BigDecimal getSpecXsdj() {
		return specXsdj;
	}

	public void setSpecXsdj(BigDecimal specXsdj) {
		this.specXsdj = specXsdj;
	}

	@Column(name = "limitXsdj")
	public BigDecimal getLimitXsdj() {
		return limitXsdj;
	}

	public void setLimitXsdj(BigDecimal limitXsdj) {
		this.limitXsdj = limitXsdj;
	}

	@Column(name = "lastRkdj")
	public BigDecimal getLastRkdj() {
		return lastRkdj;
	}

	public void setLastRkdj(BigDecimal lastRkdj) {
		this.lastRkdj = lastRkdj;
	}

	@Override
    public int hashCode() {
		int result = 17;  //任意素数   
		result = 31 * result + this.id; //c1,c2是什么看下文解释   
		return result;
    }
	
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TSpDet other = (TSpDet)obj;
        if (this.id == other.getId())
            return true;
        return false;
    }

}
