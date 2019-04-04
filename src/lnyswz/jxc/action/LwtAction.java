package lnyswz.jxc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.jxc.bean.Lwt;
import lnyswz.jxc.service.LwtServiceI;


@Namespace("/jxc")
@Action("lwtAction")
public class LwtAction extends BaseAction implements
		ModelDriven<Lwt> {
	private static final long serialVersionUID = 1L;
	private Lwt lwt = new Lwt();
	private LwtServiceI lwtService;

	
	/**
	 *手机
	 *根据业务员获取客户信息 
	 */
	public void listKhByYwy() {

		// super.writeJson(selectCommonService.selectCommonList(selectCommon));
		writeJson(lwtService.listKhByYwy(lwt));
	}

	/**
	 *手机
	 *根据业务员,客户获取销售提货信息 
	 */
	public void listKhByYwyXsth() {
		
		// super.writeJson(selectCommonService.selectCommonList(selectCommon));
		writeJson(lwtService.listKhByYwyXsth(lwt));
	}
	

	

	public Lwt getModel() {
		return lwt;
	}

	@Autowired
	public void setLwtService(LwtServiceI lwtService) {
		this.lwtService = lwtService;
	}
}