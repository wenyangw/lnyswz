package lnyswz.jxc.action;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Ywdb;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.YwdbServiceI;
/**
 * 业务调拨Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("ywdbAction")
public class YwdbAction extends BaseAction implements ModelDriven<Ywdb>{
	private static final long serialVersionUID = 1L;
	private Ywdb ywdb = new Ywdb();
	private YwdbServiceI ywdbService;
	
	/**
	 * 保存数据
	 */
	public void save(){
		User user = (User)session.get("user");
		ywdb.setCreateId(user.getId());
		ywdb.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			j.setObj(ywdbService.save(ywdb));		
			//添加成功
			j.setSuccess(true);
			j.setMsg("保存业务调拨成功！");
		}catch(Exception e){
			j.setMsg("保存业务调拨失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 冲减单据
	 */
	public void cjYwdb(){
		User user = (User)session.get("user");
		ywdb.setCjId(user.getId());
		ywdb.setCjName(user.getRealName());
		Json j = new Json();
		try{
			ywdbService.cjYwdb(ywdb);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("冲减业务调拨成功！");
		}catch(Exception e){
			j.setMsg("冲减业务调拨失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	public void datagrid(){
		writeJson(ywdbService.datagrid(ywdb));
	}
	
	public void detDatagrid(){
		writeJson(ywdbService.detDatagrid(ywdb.getYwdblsh()));
	}
	
	public void getSpkc(){
		writeJson(ywdbService.getSpkc(ywdb));
	}
	
	@Override
	public Ywdb getModel() {
		return ywdb;
	}

	@Autowired
	public void setYwdbService(YwdbServiceI ywdbService) {
		this.ywdbService = ywdbService;
	}

}
