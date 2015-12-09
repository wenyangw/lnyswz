package lnyswz.jxc.action;


import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;




import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Cgjh;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.CgjhServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Export;

/**
 * 采购计划Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("cgjhAction")
public class CgjhAction extends BaseAction implements ModelDriven<Cgjh>{
	private Logger logger = Logger.getLogger(CgjhAction.class);
	private Cgjh cgjh = new Cgjh();
	private CgjhServiceI cgjhService;
	
	/**
	 * 保存数据
	 */
	public void save(){
		User user = (User)session.get("user");
		cgjh.setCreateId(user.getId());
		cgjh.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			j.setObj(cgjhService.save(cgjh));		
			//添加成功
			j.setSuccess(true);
			j.setMsg("保存采购计划成功！");
		}catch(Exception e){
			j.setMsg("保存采购计划失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 取消单据
	 */
	public void cancel(){
		User user = (User)session.get("user");
		cgjh.setCancelId(user.getId());
		cgjh.setCancelName(user.getRealName());
		Json j = new Json();
		try{
			cgjhService.updateCancel(cgjh);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("取消采购计划成功！");
		}catch(Exception e){
			j.setMsg("取消采购计划失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 完成采购计划
	 */
	public void complete(){
		User user = (User)session.get("user");
		cgjh.setCompleteId(user.getId());
		cgjh.setCompleteName(user.getRealName());
		Json j = new Json();
		try{
			cgjhService.updateComplete(cgjh);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("完成采购计划成功！");
		}catch(Exception e){
			j.setMsg("完成采购计划失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 取消完成采购计划
	 */
	public void unComplete(){
		User user = (User)session.get("user");
		cgjh.setCompleteId(user.getId());
		cgjh.setCompleteName(user.getRealName());
		Json j = new Json();
		try{
			cgjhService.updateUnComplete(cgjh);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("取消完成采购计划成功！");
		}catch(Exception e){
			j.setMsg("取消完成采购计划失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	
	/**
	 * 采购计划改变合同标志
	 */
	public void changeHt(){
		User user = (User)session.get("user");
		cgjh.setHtId(user.getId());
		cgjh.setHtName(user.getRealName());
		Json j = new Json();
		try{
			cgjhService.updateIsHt(cgjh);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("采购计划合同标记成功！");
		}catch(Exception e){
			j.setMsg("采购计划合同标记失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
		
	/**
	 * 采购计划标记已收回合同
	 */
	public void ht(){
		User user = (User)session.get("user");
		cgjh.setHtId(user.getId());
		cgjh.setHtName(user.getRealName());
		Json j = new Json();
		try{
			cgjhService.updateHt(cgjh);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("采购计划合同收回标记成功！");
		}catch(Exception e){
			j.setMsg("采购计划合同收回标记失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 采购计划商品锁定
	 */
	public void lockSpInCgjh(){
		User user = (User)session.get("user");
		cgjh.setCreateId(user.getId());
		//cgjh.setHtName(user.getRealName());
		Json j = new Json();
		try{
			cgjhService.updateLockSpInCgjh(cgjh);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("采购计划商品锁定成功！");
		}catch(Exception e){
			j.setMsg("采购计划商品锁定失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 采购计划单商品退回
	 */
	public void backSpInCgjh(){
		User user = (User)session.get("user");
		cgjh.setCreateId(user.getId());
		Json j = new Json();
		try{
			cgjhService.updateBackSpInCgjh(cgjh);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("采购计划商品取消成功！");
		}catch(Exception e){
			j.setMsg("采购计划商品取消失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	public void printCgjh() {
		User user = (User) session.get("user");
		cgjh.setCreateName(user.getRealName());
		DataGrid dg = cgjhService.printCgjh(cgjh);
		Export.print(dg, Constant.REPORT_CGJH.get(cgjh.getBmbh()));
	}
	
	public void datagrid(){
		User user = (User)session.get("user");
		cgjh.setCreateId(user.getId());
		writeJson(cgjhService.datagrid(cgjh));
	}
	
	public void detDatagrid(){
		writeJson(cgjhService.detDatagrid(cgjh));
	}
	
	public void datagridDet(){
		writeJson(cgjhService.datagridDet(cgjh));
	}
	
	public void getSpkc(){
		writeJson(cgjhService.getSpkc(cgjh));
	}
	
	public void toKfrk(){
		writeJson(cgjhService.toKfrk(cgjh.getCgjhDetIds()));
	}
	
	public void toYwrk(){
		writeJson(cgjhService.toYwrk(cgjh.getCgjhDetIds()));
	}
	
	@Override
	public Cgjh getModel() {
		return cgjh;
	}

	@Autowired
	public void setCgjhService(CgjhServiceI cgjhService) {
		this.cgjhService = cgjhService;
	}

}
