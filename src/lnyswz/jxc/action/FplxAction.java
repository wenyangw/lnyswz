package lnyswz.jxc.action;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Fplx;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.FplxServiceI;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/jxc")
@Action("fplxAction")
public class FplxAction extends BaseAction implements ModelDriven<Fplx> {
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(LoginAction.class);
	private Fplx fplx = new Fplx();
	private FplxServiceI fplxService;

	/**
	 * 增加发票类型
	 */
	public void add() {
		Json j = new Json();
		User u = (User) session.get("user");
		fplx.setUserId(u.getId());
		try {
			Fplx r = fplxService.add(fplx);
			j.setSuccess(true);
			j.setMsg("增加发票类型成功");
			j.setObj(r);
		} catch (Exception e) {
			j.setMsg("增加发票类型失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改发票类型
	 */
	public void edit() {
		Json j = new Json();
		User u = (User) session.get("user");
		fplx.setUserId(u.getId());
		try {
			fplxService.edit(fplx);
			j.setSuccess(true);
			j.setMsg("编辑发票类型成功！");
		} catch (Exception e) {
			j.setMsg(e.getStackTrace().toString());
		}
		writeJson(j);
	}

	/**
	 * 删除发票类型信息
	 */
	public void delete() {
		Json j = new Json();
		User u = (User) session.get("user");
		fplx.setUserId(u.getId());
		try {
			fplxService.delete(fplx);
			j.setSuccess(true);
			j.setMsg("删除发票类型成功！");
		} catch (Exception e) {
			j.setMsg(e.getStackTrace().toString());
		}
		writeJson(j);
	}

	public void listFplx() {
		super.writeJson(fplxService.listFplx(fplx));
	}

	public void datagrid() {

		super.writeJson(fplxService.datagrid(fplx));
	}

	public Fplx getModel() {
		return fplx;
	}

	@Autowired
	public void setFplxService(FplxServiceI fplxService) {
		this.fplxService = fplxService;
	}
}