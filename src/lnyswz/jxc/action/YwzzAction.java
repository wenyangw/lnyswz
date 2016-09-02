package lnyswz.jxc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.jxc.bean.Ywzz;
import lnyswz.jxc.service.YwzzServiceI;

/**
 * 业务总账Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("ywzzAction")
public class YwzzAction extends BaseAction implements ModelDriven<Ywzz>{
	private static final long serialVersionUID = 1L;
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
