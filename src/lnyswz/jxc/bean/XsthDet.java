package lnyswz.jxc.bean;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * TXsthDet generated by hbm2java
 */
public class XsthDet{

	private int id;
	private String xsthlsh;
	private String spbh;
	private String spmc;
	private String spcd;
	private String sppp;
	private String spbz;
	private String zjldwId;
	private String zjldwmc;
	private String cjldwId;
	private String cjldwmc;
	private BigDecimal zhxs;
	private BigDecimal zdwsl;
	private BigDecimal cdwsl;
	private BigDecimal zdwdj;
	private BigDecimal cdwdj;
	private BigDecimal spje;
	private BigDecimal spse;
	private BigDecimal sphj;
	private BigDecimal cksl;
	private BigDecimal kpsl;
	private BigDecimal thsl;
	private BigDecimal zdwthsl;
	private BigDecimal cdwthsl;
	private BigDecimal zdwytsl;
	private BigDecimal cdwytsl;
	private String kfcklshs;

	private BigDecimal zdwrksl;
	private BigDecimal cdwrksl;
	
	private String Shdz;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getXsthlsh() {
		return xsthlsh;
	}
	public void setXsthlsh(String xsthlsh) {
		this.xsthlsh = xsthlsh;
	}
	public String getSpbh() {
		return spbh;
	}
	public void setSpbh(String spbh) {
		this.spbh = spbh;
	}
	public String getSpmc() {
		return spmc;
	}
	public void setSpmc(String spmc) {
		this.spmc = spmc;
	}
	public String getSpcd() {
		return spcd;
	}
	public void setSpcd(String spcd) {
		this.spcd = spcd;
	}
	public String getSppp() {
		return sppp;
	}
	public void setSppp(String sppp) {
		this.sppp = sppp;
	}
	public String getSpbz() {
		return spbz;
	}
	public void setSpbz(String spbz) {
		this.spbz = spbz;
	}
	public String getZjldwId() {
		return zjldwId;
	}
	public void setZjldwId(String zjldwId) {
		this.zjldwId = zjldwId;
	}
	public String getZjldwmc() {
		return zjldwmc;
	}
	public void setZjldwmc(String zjldwmc) {
		this.zjldwmc = zjldwmc;
	}
	public String getCjldwId() {
		return cjldwId;
	}
	public void setCjldwId(String cjldwId) {
		this.cjldwId = cjldwId;
	}
	public String getCjldwmc() {
		return cjldwmc;
	}
	public void setCjldwmc(String cjldwmc) {
		this.cjldwmc = cjldwmc;
	}
	public BigDecimal getZhxs() {
		return zhxs;
	}
	public void setZhxs(BigDecimal zhxs) {
		this.zhxs = zhxs;
	}
	public BigDecimal getZdwsl() {
		return zdwsl;
	}
	public void setZdwsl(BigDecimal zdwsl) {
		this.zdwsl = zdwsl;
	}
	public BigDecimal getCdwsl() {
		return cdwsl;
	}
	public void setCdwsl(BigDecimal cdwsl) {
		this.cdwsl = cdwsl;
	}
	public BigDecimal getZdwdj() {
		return zdwdj;
	}
	public void setZdwdj(BigDecimal zdwdj) {
		this.zdwdj = zdwdj;
	}
	public BigDecimal getCdwdj() {
		return cdwdj;
	}
	public void setCdwdj(BigDecimal cdwdj) {
		this.cdwdj = cdwdj;
	}
	public BigDecimal getSpje() {
		return spje;
	}
	public void setSpje(BigDecimal spje) {
		this.spje = spje;
	}
	public BigDecimal getSpse() {
		return spse;
	}
	public void setSpse(BigDecimal spse) {
		this.spse = spse;
	}
	public BigDecimal getSphj() {
		return sphj;
	}
	public void setSphj(BigDecimal sphj) {
		this.sphj = sphj;
	}
	public BigDecimal getCksl() {
		return cksl;
	}
	public void setCksl(BigDecimal cksl) {
		this.cksl = cksl;
	}
	public BigDecimal getKpsl() {
		return kpsl;
	}
	public void setKpsl(BigDecimal kpsl) {
		this.kpsl = kpsl;
	}
	public BigDecimal getThsl() {
		return thsl;
	}
	public void setThsl(BigDecimal thsl) {
		this.thsl = thsl;
	}
	public BigDecimal getZdwthsl() {
		return zdwthsl;
	}
	public void setZdwthsl(BigDecimal zdwthsl) {
		this.zdwthsl = zdwthsl;
	}
	public BigDecimal getCdwthsl() {
		return cdwthsl;
	}
	public void setCdwthsl(BigDecimal cdwthsl) {
		this.cdwthsl = cdwthsl;
	}
	public BigDecimal getZdwytsl() {
		return zdwytsl;
	}
	public void setZdwytsl(BigDecimal zdwytsl) {
		this.zdwytsl = zdwytsl;
	}
	public BigDecimal getCdwytsl() {
		return cdwytsl;
	}
	public void setCdwytsl(BigDecimal cdwytsl) {
		this.cdwytsl = cdwytsl;
	}
	public String getKfcklshs() {
		return kfcklshs;
	}
	public void setKfcklshs(String kfcklshs) {
		this.kfcklshs = kfcklshs;
	}
	public BigDecimal getZdwrksl() {
		return zdwrksl;
	}
	public void setZdwrksl(BigDecimal zdwrksl) {
		this.zdwrksl = zdwrksl;
	}
	public BigDecimal getCdwrksl() {
		return cdwrksl;
	}
	public void setCdwrksl(BigDecimal cdwrksl) {
		this.cdwrksl = cdwrksl;
	}
	public String getShdz() {
		return Shdz;
	}
	public void setShdz(String shdz) {
		Shdz = shdz;
	}

	

}
