package lnyswz.jxc.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lnyswz.jxc.model.TXskp;

public class Xshk {
	private String xshklsh;
	private Date createTime;
	private int createId;
	private String createName;
	private String bmbh;
	private String bmmc;
	private String khbh;
	private String khmc;
	private BigDecimal hkje;
	private String isCancel;
	private Integer cancelId;
	private String cancelName;
	private Date cancelTime;
	private String cancelXshklsh;
	private String xskplshs;
	
	
	public String getXshklsh() {
		return xshklsh;
	}
	public void setXshklsh(String xshklsh) {
		this.xshklsh = xshklsh;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getCreateId() {
		return createId;
	}
	public void setCreateId(int createId) {
		this.createId = createId;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public String getBmbh() {
		return bmbh;
	}
	public void setBmbh(String bmbh) {
		this.bmbh = bmbh;
	}
	public String getBmmc() {
		return bmmc;
	}
	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}
	public String getKhbh() {
		return khbh;
	}
	public void setKhbh(String khbh) {
		this.khbh = khbh;
	}
	public String getKhmc() {
		return khmc;
	}
	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}
	public BigDecimal getHkje() {
		return hkje;
	}
	public void setHkje(BigDecimal hkje) {
		this.hkje = hkje;
	}
	public String getIsCancel() {
		return isCancel;
	}
	public void setIsCancel(String isCancel) {
		this.isCancel = isCancel;
	}
	public Integer getCancelId() {
		return cancelId;
	}
	public void setCancelId(Integer cancelId) {
		this.cancelId = cancelId;
	}
	public String getCancelName() {
		return cancelName;
	}
	public void setCancelName(String cancelName) {
		this.cancelName = cancelName;
	}
	public Date getCancelTime() {
		return cancelTime;
	}
	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}
	public String getCancelXshklsh() {
		return cancelXshklsh;
	}
	public void setCancelXshklsh(String cancelXshklsh) {
		this.cancelXshklsh = cancelXshklsh;
	}
	public String getXskplshs() {
		return xskplshs;
	}
	public void setXskplshs(String xskplshs) {
		this.xskplshs = xskplshs;
	}
}
