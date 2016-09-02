package lnyswz.jxc.action;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Kfrk;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.KfrkServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Export;
/**
 * 采购需求Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("kfrkAction")
public class KfrkAction extends BaseAction implements ModelDriven<Kfrk>{
	private static final long serialVersionUID = 1L;
	private Kfrk kfrk = new Kfrk();
	private KfrkServiceI kfrkService;
	
	/**
	 * 保存数据
	 */
	public void save(){
		User user = (User)session.get("user");
		kfrk.setCreateId(user.getId());
		kfrk.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			j.setObj(kfrkService.save(kfrk));		
			//添加成功
			j.setSuccess(true);
			j.setMsg("保存库房入库成功！");
		}catch(Exception e){
			j.setMsg("保存库房入库失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 冲减单据
	 */
	public void cjKfrk(){
		User user = (User)session.get("user");
		kfrk.setCjId(user.getId());
		kfrk.setCjName(user.getRealName());
		Json j = new Json();
		try{
			kfrkService.cjKfrk(kfrk);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("冲减库房入库单成功！");
		}catch(Exception e){
			j.setMsg("冲减库房入库单失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	public void datagrid(){
		writeJson(kfrkService.datagrid(kfrk));
	}
	
	public void detDatagrid(){
		writeJson(kfrkService.detDatagrid(kfrk.getKfrklsh()));
	}
	
	public void toYwrk(){
		writeJson(kfrkService.toYwrk(kfrk.getKfrklshs()));
	}
	
	public void printKfrk() {
		User user = (User)session.get("user");
		kfrk.setCreateName(user.getRealName());
		DataGrid dg = kfrkService.printKfrk(kfrk);
		Export.print(dg, Constant.REPORT_KFRK.get(kfrk.getBmbh()));
	}
	
	@Override
	public Kfrk getModel() {
		return kfrk;
	}

	@Autowired
	public void setKfrkService(KfrkServiceI kfrkService) {
		this.kfrkService = kfrkService;
	}

}
