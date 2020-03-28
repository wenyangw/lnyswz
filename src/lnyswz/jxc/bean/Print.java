package lnyswz.jxc.bean;

import java.util.Date;

/**
 * 功能按钮类
 * @author wangwy
 *
 */
public class Print {
	private int id;
	private String lsh;
	private Date printTime;
	private int printId;
	private String printName;
	private int bgyId;
	private String type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLsh() {
		return lsh;
	}

	public void setLsh(String lsh) {
		this.lsh = lsh;
	}

	public Date getPrintTime() {
		return printTime;
	}

	public void setPrintTime(Date printTime) {
		this.printTime = printTime;
	}

	public int getPrintId() {
		return printId;
	}

	public void setPrintId(int printId) {
		this.printId = printId;
	}

	public String getPrintName() {
		return printName;
	}

	public void setPrintName(String printName) {
		this.printName = printName;
	}

	public int getBgyId() {
		return bgyId;
	}

	public void setBgyId(int bgyId) {
		this.bgyId = bgyId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
