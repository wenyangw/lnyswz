package lnyswz.jxc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Ck;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.CkServiceI;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/jxc")
@Action("ckAction")
public class CkAction extends BaseAction implements ModelDriven<Ck> {
	private static final long serialVersionUID = 1L;
	private Ck ck = new Ck();
	private CkServiceI ckService;

	/**
	 * 增加仓库
	 */
	public void add() {
		User u = (User) session.get("user");
		ck.setUserId(u.getId());
		Ck r = ckService.add(ck);
		Json j = new Json();
		try {
			j.setSuccess(true);
			j.setMsg("增加仓库成功");
			j.setObj(r);
		} catch (Exception e) {
			j.setMsg("增加仓库失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改仓库
	 */
	public void edit() {
		Json j = new Json();
		User u = (User) session.get("user");
		ck.setUserId(u.getId());
		try {
			ckService.edit(ck);
			j.setSuccess(true);
			j.setMsg("编辑仓库成功！");
		} catch (Exception e) {
			j.setMsg(e.getStackTrace().toString());
		}
		writeJson(j);
	}

	/**
	 * 删除仓库信息
	 */
	public void delete() {
		Json j = new Json();
		User u = (User) session.get("user");
		ck.setUserId(u.getId());
		if (ckService.delete(ck)) {
			j.setSuccess(true);
			j.setMsg("删除成功");
		} else {
			j.setMsg("对不起，该信息被使用中！");
		}
		writeJson(j);
	}

	public void datagrid() {
		super.writeJson(ckService.datagrid(ck));
	}

	public void listCk() {
		writeJson(ckService.listCk(ck));
	}

	public Ck getModel() {
		return ck;
	}

	@Autowired
	public void setCkService(CkServiceI ckService) {
		this.ckService = ckService;
	}
}