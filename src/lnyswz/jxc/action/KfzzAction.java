package lnyswz.jxc.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Button;
import lnyswz.jxc.bean.Catalog;
import lnyswz.jxc.bean.Kfzz;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.ButtonServiceI;
import lnyswz.jxc.service.KfzzServiceI;
/**
 * 库房总账Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("kfzzAction")
public class KfzzAction extends BaseAction implements ModelDriven<Kfzz>{
	private Logger logger = Logger.getLogger(KfzzAction.class);
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
