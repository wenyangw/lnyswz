package lnyswz.jxc.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购需求类
 * @author wangwy
 *
 */
public class Cgxq {
	
	private int id;
	private String cgxqlsh;
	private int createId;
	private Date createTime;
	private String createName;
	private String bmbh;
	private String bmmc;
	private int ywyId;
	private String ywymc;
	private String gysbh;
	private String gysmc;
	private String khbh;
	private String khmc;
	private String dhfs;
	private String shdz;
	private Date dhsj;
	private Integer xqsj;
	private String jsfsId;
	private String jsfsmc;
	private BigDecimal hjje;
	private String bz;
	private String isCancel;
	private Integer cancelId;
	private Date cancelTime;
	private String cancelName;
	private String isRefuse;
	private Integer refuseId;
	private Date refuseTime;
	private String refuseName;
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
	private BigDecimal zdwxsdj;
	private BigDecimal cdwxsdj;
	private BigDecimal spje;
	private String cgjhlsh;
	private String isLs;
	
	private String search;
	
	private String menuId;
	private String lxbh;
	//向采购计划导出时，传递参数
	private String cgxqDetIds;
	private String fromOther;
	
	//前台传入明细json
	private String datagrid;
	private int page;
	private int rows;

	public String getCgxqlsh() {
		return cgxqlsh;
	}

	public void setCgxqlsh(String cgxqlsh) {
		this.cgxqlsh = cgxqlsh;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public String getDhfs() {
		return dhfs;
	}

	public void setDhfs(String dhfs) {
		this.dhfs = dhfs;
	}

	public String getShdz() {
		return shdz;
	}

	public void setShdz(String shdz) {
		this.shdz = shdz;
	}

	public Date getDhsj() {
		return dhsj;
	}

	public void setDhsj(Date dhsj) {
		this.dhsj = dhsj;
	}

	public Integer getXqsj() {
		return xqsj;
	}

	public void setXqsj(Integer xqsj) {
		this.xqsj = xqsj;
	}

	public String getJsfsId() {
		return jsfsId;
	}

	public void setJsfsId(String jsfsId) {
		this.jsfsId = jsfsId;
	}

	public String getJsfsmc() {
		return jsfsmc;
	}

	public void setJsfsmc(String jsfsmc) {
		this.jsfsmc = jsfsmc;
	}

	public BigDecimal getHjje() {
		return hjje;
	}

	public void setHjje(BigDecimal hjje) {
		this.hjje = hjje;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(String isCancel) {
		this.isCancel = isCancel;
	}

	public int getCreateId() {
		return createId;
	}

	public void setCreateId(int createId) {
		this.createId = createId;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public Integer getCancelId() {
		return cancelId;
	}

	public void setCancelId(Integer cancelId) {
		this.cancelId = cancelId;
	}

	public String getBmbh() {
		return bmbh;
	}

	public void setBmbh(String bmbh) {
		this.bmbh = bmbh;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getBmmc() {
		return bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	public String getCancelName() {
		return cancelName;
	}

	public void setCancelName(String cancelName) {
		this.cancelName = cancelName;
	}

	public String getIsRefuse() {
		return isRefuse;
	}

	public void setIsRefuse(String isRefuse) {
		this.isRefuse = isRefuse;
	}

	public Integer getRefuseId() {
		return refuseId;
	}

	public void setRefuseId(Integer refuseId) {
		this.refuseId = refuseId;
	}

	public Date getRefuseTime() {
		return refuseTime;
	}

	public void setRefuseTime(Date refuseTime) {
		this.refuseTime = refuseTime;
	}

	public String getRefuseName() {
		return refuseName;
	}

	public void setRefuseName(String refuseName) {
		this.refuseName = refuseName;
	}

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

	public BigDecimal getZdwxsdj() {
		return zdwxsdj;
	}

	public void setZdwxsdj(BigDecimal zdwxsdj) {
		this.zdwxsdj = zdwxsdj;
	}

	public BigDecimal getCdwxsdj() {
		return cdwxsdj;
	}

	public void setCdwxsdj(BigDecimal cdwxsdj) {
		this.cdwxsdj = cdwxsdj;
	}

	public BigDecimal getSpje() {
		return spje;
	}

	public void setSpje(BigDecimal spje) {
		this.spje = spje;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getLxbh() {
		return lxbh;
	}

	public void setLxbh(String lxbh) {
		this.lxbh = lxbh;
	}

	public String getDatagrid() {
		return datagrid;
	}

	public void setDatagrid(String datagrid) {
		this.datagrid = datagrid;
	}

	public String getCgjhlsh() {
		return cgjhlsh;
	}

	public void setCgjhlsh(String cgjhlsh) {
		this.cgjhlsh = cgjhlsh;
	}

	public String getCgxqDetIds() {
		return cgxqDetIds;
	}

	public void setCgxqDetIds(String cgxqDetIds) {
		this.cgxqDetIds = cgxqDetIds;
	}

	public String getFromOther() {
		return fromOther;
	}

	public void setFromOther(String fromOther) {
		this.fromOther = fromOther;
	}

	public String getIsLs() {
		return isLs;
	}

	public void setIsLs(String isLs) {
		this.isLs = isLs;
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
