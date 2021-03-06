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
 * TKfckDet generated by hbm2java
 */
public class KfckDet{

	private int id;
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
	private String hwId;
	private String hwmc;
	private String sppc;
	private BigDecimal zdwsl;
	private BigDecimal cdwsl;
	private BigDecimal lastThsl;
	private String kfcklsh;
	private BigDecimal zdwcksl;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getHwId() {
		return hwId;
	}
	public void setHwId(String hwId) {
		this.hwId = hwId;
	}
	public String getHwmc() {
		return hwmc;
	}
	public void setHwmc(String hwmc) {
		this.hwmc = hwmc;
	}
	public String getSppc() {
		return sppc;
	}
	public void setSppc(String sppc) {
		this.sppc = sppc;
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
	public BigDecimal getLastThsl() {
		return lastThsl;
	}
	public void setLastThsl(BigDecimal lastThsl) {
		this.lastThsl = lastThsl;
	}
	public String getKfcklsh() {
		return kfcklsh;
	}
	public void setKfcklsh(String kfcklsh) {
		this.kfcklsh = kfcklsh;
	}

	public BigDecimal getZdwcksl() {
		return zdwcksl;
	}

	public void setZdwcksl(BigDecimal zdwcksl) {
		this.zdwcksl = zdwcksl;
	}
}
