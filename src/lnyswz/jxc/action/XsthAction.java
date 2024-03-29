package lnyswz.jxc.action;


import java.util.Date;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Xsth;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.XsthServiceI;
import lnyswz.jxc.util.Export;
import lnyswz.jxc.util.Util;
/**
 * 销售提货Action
 * @author 王文阳
 * @edited
 * 	2015.08.12 增加打印销售合同
 * 
 * 
 */
@Namespace("/jxc")
@Action("xsthAction")
public class XsthAction extends BaseAction implements ModelDriven<Xsth>{
	private static final long serialVersionUID = 1L;
	private Xsth xsth = new Xsth();
	private XsthServiceI xsthService;

	/**
	 * 保存数据
	 */
	public void save(){
		User user = (User)session.get("user");
		if (user != null) {
			xsth.setCreateId(user.getId());
			xsth.setCreateName(user.getRealName());
		}
		Json j = new Json();
		if(xsthService.isSaved(xsth)){
			j.setSuccess(false);
			j.setMsg("本单据已保存，请核对后操作！");
		}else {
			try {
				j.setObj(xsthService.save(xsth));
				//添加成功
				j.setSuccess(true);
				j.setMsg("保存销售提货成功！");
			} catch (Exception e) {
				j.setMsg("保存销售提货失败！");
				e.printStackTrace();
			}
		}
		writeJson(j);
	}
	
	/**
	 * 冲减单据
	 */
	public void cancelXsth(){
		Json j = new Json();
		User user = (User) session.get("user");
		if(user != null) {
			xsth.setCancelId(user.getId());
			xsth.setCancelName(user.getRealName());
		}
		try {
			j = xsthService.cancelXsth(xsth);
		} catch (Exception e) {
			j.setMsg("取消销售提货失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	public void toKfck(){
		Json j = new Json();
		if(xsthService.isCancel(xsth)){
			j.setSuccess(false);
			j.setMsg("选中的销售提货单已冲减，请刷新后重新选择！");
			writeJson(j);
		}else {
			writeJson(xsthService.toKfck(xsth));
		}
	}
	
	public void toCgjh(){
		writeJson(xsthService.toCgjh(xsth));
	}
	
	public void refreshXsth(){
		writeJson(xsthService.refreshXsth(xsth));
	}
	
	/**
	 * 修改销售提货数量
	 */
	public void updateThsl(){
		User user = (User)session.get("user");
		xsth.setCreateId(user.getId());
		xsth.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			xsthService.updateThsl(xsth);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("修改销售提货数量成功！");
		}catch(Exception e){
			j.setMsg("修改销售提货数量失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改销售提货数量
	 */
	public void updateResl(){
		User user = (User)session.get("user");
		xsth.setReId(user.getId());
		xsth.setReName(user.getRealName());
		Json j = new Json();
		try{
			xsthService.updateResl(xsth);
			//添加成功
			j.setSuccess(true);
			j.setMsg("销售提货退货操作成功！");
		}catch(Exception e){
			j.setMsg("销售提货退货操作失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 修改销售提货运费
	 */
	public void changeYf(){
		User user = (User)session.get("user");
		xsth.setCreateId(user.getId());
		xsth.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			xsthService.updateYf(xsth);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("修改销售运费成功！");
		}catch(Exception e){
			j.setMsg("修改销售运费失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 直送销售提货完成
	 */
	public void updateZsComplete(){
		User user = (User)session.get("user");
		xsth.setCreateId(user.getId());
		xsth.setLockName(user.getRealName());
		Json j = new Json();
		try{
			xsthService.updateZsComplete(xsth);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("完成直送销售提货成功！");
		}catch(Exception e){
			j.setMsg("完成直送销售提货失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 锁定销售提货
	 */
	public void updateLock(){
		User user = (User)session.get("user");
		xsth.setLockId(user.getId());
		xsth.setLockName(user.getRealName());
		Json j = new Json();
		try{
			xsthService.updateLock(xsth);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("锁定销售提货成功！");
		}catch(Exception e){
			j.setMsg("锁定销售提货失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 解锁销售提货
	 */
	public void updateUnlock(){
		User user = (User)session.get("user");
		xsth.setLockId(user.getId());
		xsth.setLockName(user.getRealName());
		Json j = new Json();
		try{
			xsthService.updateUnlock(xsth);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("解锁销售提货成功！");
		}catch(Exception e){
			j.setMsg("解锁销售提货失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改送货地址
	 */
	public void updateShdz(){
		User user = (User)session.get("user");
		xsth.setCreateId(user.getId());
		//xsth.setLockName(user.getRealName());
		Json j = new Json();
		try{
			xsthService.updateShdz(xsth);
			//添加成功
			j.setSuccess(true);
			j.setMsg("修改送货地址成功！");
		}catch(Exception e){
			j.setMsg("修改送货地址失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}


	/**
	 * 修改备注
	 */
	public void updateBz(){
		User user = (User)session.get("user");
		xsth.setCreateId(user.getId());
		Json j = new Json();
		try{
			xsthService.updateBz(xsth);
			//添加成功
			j.setSuccess(true);
			j.setMsg("修改备注成功！");
		}catch(Exception e){
			j.setMsg("修改备注失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	public void toXskp(){
		writeJson(xsthService.toXskp(xsth));
	}
	
	public void getSpBgys(){
		writeJson(xsthService.getSpBgys(xsth));
	}
	
	public void datagrid(){
		User u = (User)session.get("user");
		if (u != null) {
			xsth.setCreateId(u.getId());
			if ("1".equals(u.getIsYwy())) {
				xsth.setYwyId(u.getId());
			}
		}
		writeJson(xsthService.datagrid(xsth));
	}

	public void xsthSpDg(){
		User u = (User)session.get("user");
		xsth.setCreateId(u.getId());
		writeJson(xsthService.xsthSpDg(xsth));
	}



	public void detDatagrid(){
		writeJson(xsthService.detDatagrid(xsth));
	}
	
	public void datagridDet(){
		User u = (User)session.get("user");
		xsth.setCreateId(u.getId());
		if("1".equals(u.getIsBgy())){
			xsth.setBgyId(u.getId());
		}
		writeJson(xsthService.datagridDet(xsth));
	}
	
	public void printXsth() {
		User user = (User) session.get("user");

		if (user != null) {
			xsth.setCreateId(user.getId());
		}
		xsth.setCreateName(user == null ? Util.unicodeToString(xsth.getCreateName()) : user.getRealName());

		DataGrid dg = xsthService.printXsth(xsth);

		if(xsth.getBmbh().equals("05") && (xsth.getCreateId() == 19 || xsth.getCreateId() == 46)){
			Export.print(dg, Util.getReportName("07", "report_xsth.json"));
		}else {
			Export.print(dg, Util.getReportName(xsth.getBmbh(), "report_xsth.json"));
		}
		//Export.print(dg, Constant.REPORT_XSTH.get(xsth.getBmbh()));
	}

	public void printXsthRe() {
		User user = (User)session.get("user");
		if (user != null) {
			xsth.setCreateId(user.getId());
		}
		xsth.setCreateName(user == null ? Util.unicodeToString(xsth.getCreateName()) : user.getRealName());
		DataGrid dg = xsthService.printXsthRe(xsth);

		Export.print(dg, Util.getReportName(xsth.getBmbh(), "report_xsth_re.json"));

	}
	
	public void printXsht() {
		User user = (User)session.get("user");
//		xsth.setCreateName(user.getRealName());
		xsth.setCreateName(user == null ? Util.unicodeToString(xsth.getCreateName()) : user.getRealName());
		DataGrid dg = xsthService.printXsht(xsth);
		Export.print(dg, Util.getReportName(xsth.getBmbh(), "report_xsht.json"));
		//Export.print(dg, Constant.REPORT_XSHT.get(xsth.getBmbh()));
	}
	
	public void exportXsth() {
		User user = (User)session.get("user");
		xsth.setCreateName(user.getRealName());
		Json j = new Json();
		String type = Export.getExportType(xsth.getType());
		String location = "/export/xsth_" + xsth.getXsthlsh() + "_" + DateUtil.dateToStringWithTime(new Date(),"yyyyMMddHHmmss") + "." + type;
		xsth.setType("00");
		DataGrid dg = xsthService.printXsth(xsth);
		Export.export(dg, Util.getReportName(xsth.getBmbh(), "report_xsth.json"), location, type);
		//Export.export(dg, Constant.REPORT_XSTH.get(xsth.getBmbh()), location, type);
		j.setSuccess(true);
		j.setObj(location);
		j.setMsg("导出成功");
		writeJson(j);
	}
	
	public void exportXsht() {
		User user = (User)session.get("user");
		xsth.setCreateName(user.getRealName());
		Json j = new Json();
		String type = Export.getExportType(xsth.getType());
		String location = "/export/gxht_" + xsth.getXsthlsh() + "_" + DateUtil.dateToStringWithTime(new Date(),"yyyyMMddHHmmss") + "." + type;
		DataGrid dg = xsthService.printXsht(xsth);
		Export.export(dg, Util.getReportName(xsth.getBmbh(), "report_xsht.json"), location, type);
		//Export.export(dg, Constant.REPORT_XSHT.get(xsth.getBmbh()), location, type);
		j.setSuccess(true);
		j.setObj(location);
		j.setMsg("导出成功");
		writeJson(j);
	}
	
	public void printShd() {
		User user = (User)session.get("user");
//		xsth.setCreateName(user.getRealName());
		xsth.setCreateName(user == null ? Util.unicodeToString(xsth.getCreateName()) : user.getRealName());
		DataGrid dg = xsthService.printShd(xsth);
		Export.print(dg, Util.getReportName(xsth.getBmbh(), "report_shqr.json"));
		//Export.print(dg, Constant.REPORT_SHQR.get(xsth.getBmbh()));
	}
	
	public void exportShd() {
		User user = (User)session.get("user");
		xsth.setCreateName(user.getRealName());
		Json j = new Json();
		String type = Export.getExportType(xsth.getType());
		String location = "/export/shqrd_" + xsth.getXsthlsh() + "_" + DateUtil.dateToStringWithTime(new Date(),"yyyyMMddHHmmss") + "." + type;
		DataGrid dg = xsthService.printShd(xsth);
		Export.export(dg, Util.getReportName(xsth.getBmbh(), "report_shqr_export.json"), location, type);
		//Export.export(dg, Constant.REPORT_SHQR.get(xsth.getBmbh()), location, type);
		j.setSuccess(true);
		j.setObj(location);
		j.setMsg("导出成功");
		writeJson(j);
	}
	
	public void printThd() {
		User user = (User)session.get("user");
//		xsth.setCreateId(user.getId());
//		xsth.setCreateName(user.getRealName());
		xsth.setCreateName(user == null ? Util.unicodeToString(xsth.getCreateName()) : user.getRealName());
		DataGrid dg = xsthService.printXsth(xsth);
		Export.print(dg, Util.getReportName(xsth.getBmbh(), "report_xsth_kf.json"));
		//Export.print(dg, Constant.REPORT_XSTH_KF.get(xsth.getBmbh()));
	}
	
	public void printXsthByBgy() {
		User user = (User)session.get("user");
//		xsth.setCreateId(user.getId());
//		xsth.setCreateName(user.getRealName());
		if (user != null) {
			xsth.setCreateId(user.getId());
		}
		xsth.setCreateName(user == null ? Util.unicodeToString(xsth.getCreateName()) : user.getRealName());
		DataGrid dg = xsthService.printXsthByBgy(xsth);
		Export.print(dg, Util.getReportName(xsth.getBmbh(), "report_xsth_bgy.json"));
		//Export.print(dg, Constant.REPORT_XSTH_BGY.get(xsth.getBmbh()));
	}

	public void updateXsthOut(){
		Json j = new Json();
		User user = (User)session.get("user");
		if(user != null) {
			xsth.setCreateId(user.getId());
		}
		try{
			xsthService.updateXsthOut(xsth);
			//添加成功
			j.setSuccess(true);
			j.setMsg("确认成功！");
		}catch(Exception e){
			j.setMsg("确认失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	public void getXsthOutList(){

		writeJson(xsthService.getXsthOutList(xsth));
	}

	public void getXsthOutDetail(){
		writeJson(xsthService.getXsthOutDetail(xsth));
	}

	public void xsthCarDg(){
		User u = (User)session.get("user");
		xsth.setCreateId(u.getId());
		writeJson(xsthService.xsthCarDg(xsth));
	}



	public void getXsth(){
		writeJson(xsthService.getXsth(xsth));
	}

	public void loadXsth() {
		User user = (User)session.get("user");
		xsth.setCreateId(user.getId());
		writeJson(xsthService.loadXsth(xsth));
	}

	public void getSpkc(){
		writeJson(xsthService.getSpkc(xsth));
	}
	
	public void getYsje(){
		writeJson(xsthService.getYsje(xsth));
	}

	@Override
	public Xsth getModel() {
		return xsth;
	}

	@Autowired
	public void setXsthService(XsthServiceI xsthService) {
		this.xsthService = xsthService;
	}

}
