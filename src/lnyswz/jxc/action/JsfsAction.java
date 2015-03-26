package lnyswz.jxc.action;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Jsfs;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.JsfsServiceI;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/jxc")
@Action("jsfsAction")
public class JsfsAction extends BaseAction implements ModelDriven<Jsfs> {
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(LoginAction.class);
	private Jsfs jsfs = new Jsfs();
	private JsfsServiceI jsfsService;

	/**
	 * 增加结算方式
	 */
	public void add() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			jsfs.setUserId(u.getId());
			Jsfs r = jsfsService.add(jsfs);
			j.setSuccess(true);
			j.setMsg("增加结算方式成功");
			j.setObj(r);
		} catch (Exception e) {
			j.setMsg("增加结算方式失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改结算方式
	 */
	public void edit() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			jsfs.setUserId(u.getId());
			jsfsService.edit(jsfs);
			j.setSuccess(true);
			j.setMsg("编辑结算方式成功！");
		} catch (Exception e) {
			j.setMsg("编辑结算方式失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 删除结算方式信息
	 */
	public void delete() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			jsfs.setUserId(u.getId());
			jsfsService.delete(jsfs);
			j.setSuccess(true);
			j.setMsg("删除结算方式成功！");
		} catch (Exception e) {
			j.setMsg("删除计算方式失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	public void datagrid() {
		super.writeJson(jsfsService.datagrid(jsfs));
	}

	public void listJsfs() {
		super.writeJson(jsfsService.listJsfs(jsfs));
	}

	public Jsfs getModel() {
		return jsfs;
	}

	@Autowired
	public void setJsfsService(JsfsServiceI jsfsService) {
		this.jsfsService = jsfsService;
	}
}