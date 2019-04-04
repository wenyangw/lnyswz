package lnyswz.jxc.action;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Jldw;
import lnyswz.jxc.bean.KfbgSp;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.KfbgSpServiceI;

/**
 * 
 *
 */
@Namespace("/jxc")
@Action("kfbgSpAction")
public class KfbgSpAction extends BaseAction implements ModelDriven<KfbgSp>{
	private static final long serialVersionUID = 1L;
	private KfbgSp kfbgSp = new KfbgSp();
	private KfbgSpServiceI kfbgSpService;
	
	/**
	 * 添加功能按钮
	 */
	public void add(){
		Json j = new Json();
		try {
			j.setObj(kfbgSpService.add(kfbgSp));
			j.setSuccess(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
		writeJson(j);
		
		
	}

	
//	public void searchSp(){
//		writeJson(kfbgSpService.searchSp(kfbgSp));
//	}
	
	
	
	public void getKfbgSpZz(){
		writeJson(kfbgSpService.getKfbgSpZz(kfbgSp));
	}
	public void datagridKfbgSpMxs(){
		writeJson(kfbgSpService.datagridKfbgSpMxs(kfbgSp));
	}
	public void datagridKfbgSpMxByDate(){
		writeJson(kfbgSpService.datagridKfbgSpMxByDate(kfbgSp));
	}
	public void getKfbgSpMx(){
		writeJson(kfbgSpService.getKfbgSpMx(kfbgSp));
	}
	@Override
	public KfbgSp getModel() {
		return kfbgSp;
	}
	
	@Autowired
	public void setKfbgSpService(KfbgSpServiceI kfbgSpService) {
		this.kfbgSpService = kfbgSpService;
	}
}
