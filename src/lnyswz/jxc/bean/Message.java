package lnyswz.jxc.bean;

import java.util.Date;

/**
 * 信息类
 * @author wangwy
 *
 */
public class Message {
	private int id;
	private int createId;
	private String createName;
	private Date createTime;
	private String subject;
	private String memo;
	private String opened;
	private String isCancel;
	private Date cancelTime;

	private String menuId;
	private String files;
	private String receiverIds;
	private String receiverNames;
	private int recId;

	private Date readTime;
	
	private int page;
	private int rows;
	private String datagrid;
	private String search;
	private String source;


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOpened() {
		return opened;
	}

	public void setOpened(String opened) {
		this.opened = opened;
	}

	public String getIsCancel() {
		return isCancel;
	}
	public void setIsCancel(String isCancel) {
		this.isCancel = isCancel;
	}
	public Date getCancelTime() {
		return cancelTime;
	}
	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getFiles() {
		return files;
	}

	public void setFiles(String files) {
		this.files = files;
	}

	public String getReceiverIds() {
		return receiverIds;
	}
	public void setReceiverIds(String receiverIds) {
		this.receiverIds = receiverIds;
	}
	public String getReceiverNames() {
		return receiverNames;
	}
	public void setReceiverNames(String receiverNames) {
		this.receiverNames = receiverNames;
	}

	public int getRecId() {
		return recId;
	}

	public void setRecId(int recId) {
		this.recId = recId;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
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

	public String getDatagrid() {
		return datagrid;
	}

	public void setDatagrid(String datagrid) {
		this.datagrid = datagrid;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
