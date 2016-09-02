package lnyswz.jxc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.jxc.bean.Kfzz;
import lnyswz.jxc.service.KfzzServiceI;
/**
 * 库房总账Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("kfzzAction")
public class KfzzAction extends BaseAction implements ModelDriven<Kfzz>{
	private static final long serialVersionUID = 1L;
	private Kfzz kfzz = new Kfzz();
	private KfzzServiceI kfzzService;
	
	public void findHwId(){
		writeJson(kfzzService.findHwId(kfzz));
	}
		
	@Override
	public Kfzz getModel() {
		return kfzz;
	}
	
	@Autowired
	public void setKfzzService(KfzzServiceI kfzzService) {
		this.kfzzService = kfzzService;
	}
}
