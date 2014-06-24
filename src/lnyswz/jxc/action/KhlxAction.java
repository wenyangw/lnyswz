package lnyswz.jxc.action;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Khlx;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.KhlxServiceI;

@Namespace("/jxc")
@Action("khlxAction")
public class KhlxAction extends BaseAction implements ModelDriven<Khlx> {
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(LoginAction.class);
	private Khlx khlx = new Khlx();
	private KhlxServiceI khlxService;

	/**
	 * 增加客户类型
	 */
	public void add() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			khlx.setUserId(u.getId());
			Khlx r = khlxService.add(khlx);
			j.setSuccess(true);
			j.setMsg("增加客户类型成功");
			j.setObj(r);
		} catch (Exception e) {
			j.setMsg("增加客户类型失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改客户类型
	 */
	public void edit() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			khlx.setUserId(u.getId());
			khlxService.edit(khlx);
			j.setSuccess(true);
			j.setMsg("编辑客户类型成功！");
		} catch (Exception e) {
			j.setMsg("编辑客户类型失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 删除客户类型
	 */
	public void delete() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			khlx.setUserId(u.getId());
			khlxService.delete(khlx);
			j.setSuccess(true);
			j.setMsg("删除客户类型成功！");
		} catch (Exception e) {
			j.setMsg("删除客户类型失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	public void listKhlxs() {
		super.writeJson(khlxService.listKhlxs(khlx));
	}

	public void datagrid() {
		super.writeJson(khlxService.datagrid(khlx));
	}

	public Khlx getModel() {
		return khlx;
	}

	@Autowired
	public void setKhlxService(KhlxServiceI khlxService) {
		this.khlxService = khlxService;
	}
}