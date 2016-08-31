package lnyswz.jxc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.service.MonthServiceI;

@Namespace("/admin")
@Action("monthAction")
public class MonthAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	//private MonthHandler monthHandler = new MonthHandler();
	private MonthServiceI monthService;

	/**
	 * 月末结账
	 */
	public void update() {
		Json j = new Json();
		try {
			monthService.update();
			j.setSuccess(true);
			j.setMsg("月末结转成功！");
		} catch (Exception e) {
			j.setMsg("月末结转失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	@Autowired
	public void setMonthService(MonthServiceI monthService) {
		this.monthService = monthService;
	}
}
