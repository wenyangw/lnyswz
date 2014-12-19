package lnyswz.jxc.action;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Gys;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.GysServiceI;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/jxc")
@Action("gysAction")
public class GysAction extends BaseAction implements ModelDriven<Gys> {
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(LoginAction.class);
	private Gys gys = new Gys();
	private GysServiceI gysService;

	/**
	 * 增加供应商
	 */
	public void add() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			gys.setUserId(u.getId());
			Gys r = gysService.add(gys);
			j.setSuccess(true);
			j.setMsg("增加供应商成功");
			j.setObj(r);
		} catch (Exception e) {
			j.setMsg("增加供应商失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改供应商
	 */
	public void edit() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			gys.setUserId(u.getId());
			gysService.edit(gys);
			j.setSuccess(true);
			j.setMsg("编辑供应商成功！");
		} catch (Exception e) {
			j.setMsg(e.getStackTrace().toString());
			j.setMsg("编辑供应商失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 删除供应商信息
	 */
	public void delete() {
		Json j = new Json();
		User u = (User) session.get("user");
		gys.setUserId(u.getId());
		if (!gysService.delete(gys)) {
			j.setMsg("对不起！此供应商被其他部门占用！");
		} else {
			j.setSuccess(true);
			j.setMsg("删除成功！");
		}
		writeJson(j);
	}

	public void datagrid() {
		super.writeJson(gysService.datagrid(gys));
	}

	/**
	 * 判断供应商信息是否存在
	 */
	public void existGys() {
		Json j = new Json();
		if (gysService.existGys(gys)) {
			j.setMsg("对不起！此供应商编号已存在！");
		} else {
			j.setSuccess(true);
		}
		writeJson(j);
	}

	/**
	 * 维护专属信息
	 */
	public void editDet() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			gys.setUserId(u.getId());
			gysService.editDet(gys);
			j.setSuccess(true);
			j.setMsg("商品专属信息维护成功!");
		} catch (Exception e) {
			j.setMsg("商品专属信息维护失败!");
			e.printStackTrace();
		}
		super.writeJson(j);
	}

	/**
	 * 删除供应商信息专属信息
	 */
	public void deleteDet() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			gys.setUserId(u.getId());
			gysService.deleteDet(gys);
			j.setSuccess(true);
			j.setMsg("删除供应商成功！");
		} catch (Exception e) {
			j.setMsg(e.getStackTrace().toString());
			e.printStackTrace();
			j.setMsg("删除供应商失败！");
		}
		writeJson(j);
	}

	public void listGys() {
		super.writeJson(gysService.listGys(gys));
	}

	/**
	 * 王文阳 2013.10.08 获得供应商信息
	 */
	public void loadGys() {
		Json j = new Json();
		try {
			Gys g = gysService.loadGys(gys.getGysbh());
			j.setSuccess(true);
			j.setObj(g);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.writeJson(j);
	}

	/**
	 * 王文阳 2013.10.08 获得供应商检索信息
	 */
	public void gysDg() {
		writeJson(gysService.gysDg(gys));
	}

	public Gys getModel() {
		return gys;
	}

	@Autowired
	public void setGysService(GysServiceI gysService) {
		this.gysService = gysService;
	}
}