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
import lnyswz.jxc.bean.Ywzz;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.ButtonServiceI;
import lnyswz.jxc.service.YwzzServiceI;
/**
 * 库房总账Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("ywzzAction")
public class YwzzAction extends BaseAction implements ModelDriven<Ywzz>{
	private Logger logger = Logger.getLogger(YwzzAction.class);
	private Ywzz ywzz = new Ywzz();
	private YwzzServiceI ywzzService;
	
	public void getDwcb(){
		writeJson(ywzzService.getDwcb(ywzz));
	}
	
	public void listLowSps(){
		writeJson(ywzzService.listLowSps(ywzz));
	}
	
	public void toCgjh(){
		writeJson(ywzzService.toCgjh(ywzz));
	}
	
	@Override
	public Ywzz getModel() {
		return ywzz;
	}
	
	@Autowired
	public void setYwzzService(YwzzServiceI ywzzService) {
		this.ywzzService = ywzzService;
	}
}
