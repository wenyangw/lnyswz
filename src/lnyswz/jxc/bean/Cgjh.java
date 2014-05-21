package lnyswz.jxc.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购需求类
 * @author wangwy
 *
 */
public class Cgjh {
	
	private String cgjhlsh;
	private Date createTime;
	private Integer createId;
	private String createName;
	private String bmbh;
	private String bmmc;
	private String gysbh;
	private String gysmc;
	private String ckId;
	private String ckmc;
	private String shdz;
	private String lxr;
	private Date dhsj;
	private String jsfsId;
	private String jsfsmc;
	private BigDecimal hjje;
	private String bz;
	private String isHt;
	private String returnHt;
	private Integer htId;
	private Date htTime;
	private String htName;
	private String isCancel;
	private Integer cancelId;
	private Date cancelTime;
	private String cancelName;
	private String isCompleted;
	private Integer completeId;
	private Date completeTime;
	private String completeName;
	private String needAudit;
	private String isAudit;
	private String isZs;
	
	private int id;
	private String spbh;
	private String spmc;
	private String spcd;
	private String sppp;
	private String spbz;
	private String spdj;
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
	//记录库房实际入库
	private BigDecimal zdwyrsl;
	//在计划列表中提示库房是否入库
	private String isKfrk;
	
	private String search;
	
	private String lxbh; 
	private String menuId; 
	//前台传入明细json
	private String datagrid;
	private int page;
	private int rows;
	//由采购需求生成计划时，传参数
	private String cgxqDetIds;
	//采购计划列表中，表示与采购需求的关系
	private String cgxqlshs;
	//向库房入库导出时，传递参数
	private String cgjhDetIds;
	//向业务入库导入直送计划
	private String cgjhlshs;
	private String kfrklshs;
	private String ywrklshs;
	private String fromOther;

	public String getCgjhlsh() {
		return cgjhlsh;
	}

	public void setCgjhlsh(String cgjhlsh) {
		this.cgjhlsh = cgjhlsh;
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

	public String getCkId() {
		return ckId;
	}

	public void setCkId(String ckId) {
		this.ckId = ckId;
	}

	public String getCkmc() {
		return ckmc;
	}

	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getShdz() {
		return shdz;
	}

	public void setShdz(String shdz) {
		this.shdz = shdz;
	}

	public String getLxr() {
		return lxr;
	}

	public void setLxr(String lxr) {
		this.lxr = lxr;
	}

	public Date getDhsj() {
		return dhsj;
	}

	public void setDhsj(Date dhsj) {
		this.dhsj = dhsj;
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

	public String getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(String isCompleted) {
		this.isCompleted = isCompleted;
	}

	public Integer getCreateId() {
		return createId;
	}

	public void setCreateId(Integer createId) {
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

	public Integer getCompleteId() {
		return completeId;
	}

	public void setCompleteId(Integer completeId) {
		this.completeId = completeId;
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

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	public String getCompleteName() {
		return completeName;
	}

	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}

	public String getIsHt() {
		return isHt;
	}

	public void setIsHt(String isHt) {
		this.isHt = isHt;
	}

	public String getReturnHt() {
		return returnHt;
	}

	public void setReturnHt(String returnHt) {
		this.returnHt = returnHt;
	}

	public Integer getHtId() {
		return htId;
	}

	public void setHtId(Integer htId) {
		this.htId = htId;
	}

	public Date getHtTime() {
		return htTime;
	}

	public void setHtTime(Date htTime) {
		this.htTime = htTime;
	}

	public String getHtName() {
		return htName;
	}

	public void setHtName(String htName) {
		this.htName = htName;
	}

	public String getNeedAudit() {
		return needAudit;
	}

	public void setNeedAudit(String needAudit) {
		this.needAudit = needAudit;
	}

	public String getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(String isAudit) {
		this.isAudit = isAudit;
	}

	public String getIsZs() {
		return isZs;
	}

	public void setIsZs(String isZs) {
		this.isZs = isZs;
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

	public String getSpdj() {
		return spdj;
	}

	public void setSpdj(String spdj) {
		this.spdj = spdj;
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

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getIsKfrk() {
		return isKfrk;
	}

	public void setIsKfrk(String isKfrk) {
		this.isKfrk = isKfrk;
	}

	public BigDecimal getZdwyrsl() {
		return zdwyrsl;
	}

	public void setZdwyrsl(BigDecimal zdwyrsl) {
		this.zdwyrsl = zdwyrsl;
	}

	public String getLxbh() {
		return lxbh;
	}

	public void setLxbh(String lxbh) {
		this.lxbh = lxbh;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getDatagrid() {
		return datagrid;
	}

	public void setDatagrid(String datagrid) {
		this.datagrid = datagrid;
	}

	public String getFromOther() {
		return fromOther;
	}

	public void setFromOther(String fromOther) {
		this.fromOther = fromOther;
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

	public String getCgxqDetIds() {
		return cgxqDetIds;
	}

	public void setCgxqDetIds(String cgxqDetIds) {
		this.cgxqDetIds = cgxqDetIds;
	}

	public String getCgxqlshs() {
		return cgxqlshs;
	}

	public void setCgxqlshs(String cgxqlshs) {
		this.cgxqlshs = cgxqlshs;
	}

	public String getCgjhDetIds() {
		return cgjhDetIds;
	}

	public void setCgjhDetIds(String cgjhDetIds) {
		this.cgjhDetIds = cgjhDetIds;
	}

	public String getCgjhlshs() {
		return cgjhlshs;
	}

	public void setCgjhlshs(String cgjhlshs) {
		this.cgjhlshs = cgjhlshs;
	}

	public String getKfrklshs() {
		return kfrklshs;
	}

	public void setKfrklshs(String kfrklshs) {
		this.kfrklshs = kfrklshs;
	}
	
	public String getYwrklshs() {
		return ywrklshs;
	}
	
	public void setYwrklshs(String ywrklshs) {
		this.ywrklshs = ywrklshs;
	}

}
