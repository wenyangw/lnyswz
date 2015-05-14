package lnyswz.jxc.action;


import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;


import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Kfck;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.KfckServiceI;
/**
 * 采购需求Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("kfckAction")
public class KfckAction extends BaseAction implements ModelDriven<Kfck>{
	private Logger logger = Logger.getLogger(KfckAction.class);
	private Kfck kfck = new Kfck();
	private KfckServiceI kfckService;
	
	/**
	 * 保存数据
	 */
	public void save(){
		User user = (User)session.get("user");
		kfck.setCreateId(user.getId());
		kfck.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			kfckService.save(kfck);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("保存库房出库成功！");
		}catch(Exception e){
			j.setMsg("保存库房出库失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 冲减单据
	 */
	public void cjKfck(){
		User user = (User)session.get("user");
		kfck.setCjId(user.getId());
		kfck.setCjName(user.getRealName());
		Json j = new Json();
		try{
			kfckService.cjKfck(kfck);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("冲减库房出库单成功！");
		}catch(Exception e){
			j.setMsg("冲减库房出库单失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	public void datagrid(){
		writeJson(kfckService.datagrid(kfck));
	}
	
	public void detDatagrid(){
		writeJson(kfckService.detDatagrid(kfck.getKfcklsh()));
	}
	
	public void getSpkc(){
		writeJson(kfckService.getSpkc(kfck));
	}
	
//	public void toYwrk(){
//		writeJson(kfckService.toYwrk(kfck.getKfcklshs()));
//	}
	
	@Override
	public Kfck getModel() {
		return kfck;
	}

	@Autowired
	public void setKfckService(KfckServiceI kfckService) {
		this.kfckService = kfckService;
	}

}
