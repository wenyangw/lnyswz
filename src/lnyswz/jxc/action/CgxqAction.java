package lnyswz.jxc.action;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;


import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Cgxq;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.CgxqServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Export;

/**
 * 采购需求Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("cgxqAction")
public class CgxqAction extends BaseAction implements ModelDriven<Cgxq>{
	private static final long serialVersionUID = 1L;
	private Cgxq cgxq = new Cgxq();
	private CgxqServiceI cgxqService;
	
	/**
	 * 保存数据
	 */
	public void save(){
		User user = (User)session.get("user");
		cgxq.setCreateId(user.getId());
		cgxq.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			j.setObj(cgxqService.save(cgxq));		
			//添加成功
			j.setSuccess(true);
			j.setMsg("保存采购需求成功！");
		}catch(Exception e){
			j.setMsg("保存采购需求失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 取消单据
	 */
	public void cancel(){
		User user = (User)session.get("user");
		cgxq.setCancelId(user.getId());
		cgxq.setCancelName(user.getRealName());
		Json j = new Json();
		try{
			cgxqService.updateCancel(cgxq);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("取消采购需求成功！");
		}catch(Exception e){
			j.setMsg("取消采购需求失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 退回单据
	 */
	public void refuse(){
		User user = (User)session.get("user");
		cgxq.setRefuseId(user.getId());
		cgxq.setRefuseName(user.getRealName());
		Json j = new Json();
		try{
			cgxqService.updateRefuse(cgxq);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("退回采购需求成功！");
		}catch(Exception e){
			j.setMsg("退回采购需求失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 完成采购需求，不再进行采购计划
	 */
	public void complete(){
		User user = (User)session.get("user");
		cgxq.setRefuseId(user.getId());
		cgxq.setRefuseName(user.getRealName());
		Json j = new Json();
		try{
			cgxqService.updateComplete(cgxq);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("完成采购需求成功！");
		}catch(Exception e){
			j.setMsg("完成采购需求失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
		
	/**
	 * 完成采购需求,不再进行调拨
	 */
	public void dbxq(){
		User user = (User)session.get("user");
		cgxq.setRefuseId(user.getId());
		cgxq.setRefuseName(user.getRealName());
		Json j = new Json();
		try{
			cgxqService.updateDbxq(cgxq);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("采购需求调拨完成成功！");
		}catch(Exception e){
			j.setMsg("采购需求调拨完成失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	public void printCgxq() {
		User user = (User) session.get("user");
		cgxq.setCreateName(user.getRealName());
		DataGrid dg = cgxqService.printCgxq(cgxq);
		Export.print(dg, Constant.REPORT_CGXQ.get(cgxq.getBmbh()));
	}
	
	public void datagrid(){
		//操作员
		User user = (User)session.get("user");
		cgxq.setCreateId(user.getId());
		writeJson(cgxqService.datagrid(cgxq));
	}
	
	public void detDatagrid(){
		writeJson(cgxqService.detDatagrid(cgxq.getCgxqlsh()));
	}
	
	public void toCgjh(){
		writeJson(cgxqService.toCgjh(cgxq.getCgxqDetIds()));
	}
	
	public void toYwdb(){
		writeJson(cgxqService.toYwdb(cgxq));
	}
	
	public void getSpkc(){
		writeJson(cgxqService.getSpkc(cgxq));
	}
	
	@Override
	public Cgxq getModel() {
		return cgxq;
	}

	@Autowired
	public void setCgxqService(CgxqServiceI cgxqService) {
		this.cgxqService = cgxqService;
	}

}
