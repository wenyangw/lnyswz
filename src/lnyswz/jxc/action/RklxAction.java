package lnyswz.jxc.action;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Rklx;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.RklxServiceI;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/jxc")
@Action("rklxAction")
public class RklxAction extends BaseAction implements ModelDriven<Rklx> {
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(LoginAction.class);
	private Rklx rklx = new Rklx();
	private RklxServiceI rklxService;

	/**
	 * 增加入库类型
	 */
	public void add() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			rklx.setUserId(u.getId());
			Rklx r = rklxService.add(rklx);
			j.setSuccess(true);
			j.setMsg("增加入库类型成功");
			j.setObj(r);
		} catch (Exception e) {
			j.setMsg("增加入库类型失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改入库类型
	 */
	public void edit() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			rklx.setUserId(u.getId());
			rklxService.edit(rklx);
			j.setSuccess(true);
			j.setMsg("编辑入库类型成功！");
		} catch (Exception e) {
			j.setMsg("编辑入库类型失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 删除入库类型
	 */
	public void delete() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			rklx.setUserId(u.getId());
			rklxService.delete(rklx);
			j.setSuccess(true);
			j.setMsg("删除入库类型成功！");
		} catch (Exception e) {
			j.setMsg("删除入库类型失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	public void listRklx() {
		super.writeJson(rklxService.listRklx(rklx));
	}

	public void datagrid() {
		super.writeJson(rklxService.datagrid(rklx));
	}

	public Rklx getModel() {
		return rklx;
	}

	@Autowired
	public void setRklxService(RklxServiceI rklxService) {
		this.rklxService = rklxService;
	}
}