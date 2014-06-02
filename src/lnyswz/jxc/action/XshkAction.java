package lnyswz.jxc.action;


import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Xshk;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.XshkServiceI;
/**
 * 销售回款Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("xshkAction")
public class XshkAction extends BaseAction implements ModelDriven<Xshk>{
	private Logger logger = Logger.getLogger(XshkAction.class);
	private Xshk xshk = new Xshk();
	private XshkServiceI xshkService;
	
	/**
	 * 保存回款
	 */
	public void save(){		
		User user = (User)session.get("user");
		xshk.setCreateId(user.getId());
		xshk.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			//将获得的前台内容传入Service
			Xshk x = xshkService.save(xshk);
			//添加成功
			j.setSuccess(true);
			j.setMsg("保存回款成功");
			j.setObj(x);
		}catch(Exception e){
			j.setMsg("保存回款失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 取消回款
	 */
	public void cancelXshk(){
		User user = (User)session.get("user");
		xshk.setCancelId(user.getId());
		xshk.setCancelName(user.getRealName());
		Json j = new Json();
		try{
			xshkService.cancelXshk(xshk);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("取消销售回款成功！");
		}catch(Exception e){
			j.setMsg("取消销售回款失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 返回
	 */
	public void datagrid(){
		writeJson(xshkService.datagrid(xshk));
	}
	
	public void detDatagrid(){
		writeJson(xshkService.detDatagrid(xshk));
	}
	
	@Override
	public Xshk getModel() {
		return xshk;
	}
	
	@Autowired
	public void setXshkService(XshkServiceI xshkService) {
		this.xshkService = xshkService;
	}
}
