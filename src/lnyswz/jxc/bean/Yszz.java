package lnyswz.jxc.bean;

import java.math.BigDecimal;

import lnyswz.jxc.model.TSp;

/**
 * 应付总账类
 * @author wangwy
 *
 */
public class Yszz {
	private int id;
	private String bmbh;
	private String bmmc;
	private String khbh;
	private String khmc;
	private int ywyId;
	private String ywymc;
	private BigDecimal lsje;
	private BigDecimal qcje;
	private BigDecimal qcthje;
	private BigDecimal kpje;
	private BigDecimal thje;
	private BigDecimal hkje;
	private String jzsj;
	
	private int page;
	private int rows;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getYwyId() {
		return ywyId;
	}
	public void setYwyId(int ywyId) {
		this.ywyId = ywyId;
	}
	public String getYwymc() {
		return ywymc;
	}
	public void setYwymc(String ywymc) {
		this.ywymc = ywymc;
	}
	public BigDecimal getLsje() {
		return lsje;
	}
	public void setLsje(BigDecimal lsje) {
		this.lsje = lsje;
	}
	public BigDecimal getQcje() {
		return qcje;
	}
	public void setQcje(BigDecimal qcje) {
		this.qcje = qcje;
	}
	public BigDecimal getQcthje() {
		return qcthje;
	}
	public void setQcthje(BigDecimal qcthje) {
		this.qcthje = qcthje;
	}
	public BigDecimal getKpje() {
		return kpje;
	}
	public void setKpje(BigDecimal kpje) {
		this.kpje = kpje;
	}
	public BigDecimal getThje() {
		return thje;
	}
	public void setThje(BigDecimal thje) {
		this.thje = thje;
	}
	public BigDecimal getHkje() {
		return hkje;
	}
	public void setHkje(BigDecimal hkje) {
		this.hkje = hkje;
	}
	public String getJzsj() {
		return jzsj;
	}
	public void setJzsj(String jzsj) {
		this.jzsj = jzsj;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
}
