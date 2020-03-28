package lnyswz.jxc.bean;

import java.util.List;

/**
 * 部门类
 * @author wangwy
 *
 */
public class Department{

	private String id;
	private String depName;
	private int orderNum;
	private String valid;
	
	//通讯录根结点
	private String text;
	private List<User> children;
	
	private int page;
	private int rows;
	private String ids;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<User> getChildren() {
		return children;
	}
	public void setChildren(List<User> children) {
		this.children = children;
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
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	
}
