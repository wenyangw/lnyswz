package lnyswz.jxc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Button;
import lnyswz.jxc.bean.Catalog;
import lnyswz.jxc.bean.Lsh;
import lnyswz.jxc.model.TCatalog;
import lnyswz.jxc.service.CatalogServiceI;
import lnyswz.jxc.service.LshServiceI;

import com.opensymphony.xwork2.ModelDriven;

/**
 * 流水号Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("lshAction")
public class LshAction extends BaseAction implements ModelDriven<Lsh> {
	//模型驱动获得传入对象
	private Lsh lsh = new Lsh();
	private LshServiceI lshService;
	
	public void getLsh(){
		//lshService.getLsh(lsh);
		
		Json j = new Json();
		try{
			//将获得的前台内容传入Service
			String lshStr = lshService.getLsh(lsh.getBmbh(), lsh.getLxbh());
			j.setSuccess(true);
			//j.setMsg("增加功能按钮成功");
			j.setObj(lshStr);
		}catch(Exception e){
			//j.setMsg("增加功能按钮失败");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	@Override
	public Lsh getModel() {
		return lsh;
	}
	
	@Autowired
	public void setLshService(LshServiceI lshService) {
		this.lshService = lshService;
	}
}
