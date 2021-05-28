package lnyswz.jxc.bean;

import javax.persistence.*;
import java.math.BigDecimal;

public class YwrkGys {

	private long id;
	private String ywrklsh;
	private String gysbh;
	private String gysmc;
	private BigDecimal hjje;
	private BigDecimal fkje;
	private BigDecimal yfje;
	private String bz;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getYwrklsh() {
		return this.ywrklsh;
	}

	public void setYwrklsh(String ywrklsh) {
		this.ywrklsh = ywrklsh;
	}

	public String getGysbh() {
		return gysbh;
	}

	public void setGysbh(String gysbh) {
		this.gysbh = gysbh;
	}

	public String getGysmc() {
		return gysmc;
	}

	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}

	public BigDecimal getHjje() {
		return this.hjje;
	}

	public void setHjje(BigDecimal hjje) {
		this.hjje = hjje;
	}

	public BigDecimal getFkje() {
		return fkje;
	}

	public void setFkje(BigDecimal fkje) {
		this.fkje = fkje;
	}

	public BigDecimal getYfje() {
		return yfje;
	}

	public void setYfje(BigDecimal yfje) {
		this.yfje = yfje;
	}

	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
}
