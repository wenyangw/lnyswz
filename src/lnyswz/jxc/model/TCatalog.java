package lnyswz.jxc.model;
// Generated 2013-8-16 8:55:00 by Hibernate Tools 4.0.0


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TCatalog generated by hbm2java
 */
@Entity
@Table(name = "t_catalog")
public class TCatalog implements java.io.Serializable {

	private String id;
	private String catName;
	private int orderNum;
	private String iconCls;// 前面的小图标样式
	private String type;

	public TCatalog() {
	}

	public TCatalog(String id, String catName, int orderNum, String iconCls, String type) {
		this.id = id;
		this.catName = catName;
		this.orderNum = orderNum;
		this.iconCls = iconCls;
		this.type = type;
	}
	

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 36)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "catName", unique = true, nullable = false, length = 50)
	public String getCatName() {
		return this.catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}
	
	@Column(name = "orderNum", nullable = false)
	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	
	@Column(name = "iconCls", length = 50)
	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	@Column(name = "type", nullable = false, length = 1)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
