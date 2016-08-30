package lnyswz.jxc.action;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.MonthHandler;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.MonthServiceI;

@Namespace("/admin")
@Action("monthAction")
public class MonthAction extends BaseAction implements ModelDriven<MonthHandler> {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(MonthAction.class);
	private MonthHandler monthHandler = new MonthHandler();
	private MonthServiceI monthService;

	/**
	 * 月末结账
	 */
	public void update() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			//month.setUserId(u.getId());
			// 将获得的前台内容传入Service
			MonthHandler month = monthService.update(monthHandler);
			j.setSuccess(true);
			j.setMsg("月末结转成功！");
			j.setObj(month);
		} catch (Exception e) {
			j.setMsg("月末结转失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	@Override
	public MonthHandler getModel() {
		return monthHandler;
	}

	@Autowired
	public void setMonthService(MonthServiceI monthService) {
		this.monthService = monthService;
	}
}
