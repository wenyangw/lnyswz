package lnyswz.jxc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Lsh;
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
	private static final long serialVersionUID = 1L;
	//模型驱动获得传入对象
	private Lsh lsh = new Lsh();
	private LshServiceI lshService;
	
	public void getLsh(){
		Json j = new Json();
		try{
			//将获得的前台内容传入Service
			String lshStr = lshService.getLsh(lsh.getBmbh(), lsh.getLxbh());
			j.setSuccess(true);
			j.setObj(lshStr);
		}catch(Exception e){
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
