package lnyswz.jxc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Rylx;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.RylxServiceI;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/jxc")
@Action("rylxAction")
public class RylxAction extends BaseAction implements ModelDriven<Rylx> {
	private static final long serialVersionUID = 1L;
	private Rylx rylx = new Rylx();
	private RylxServiceI rylxService;

	/**
	 * 增加人员类型
	 */
	public void add() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			rylx.setUserId(u.getId());
			Rylx r = rylxService.add(rylx);
			j.setSuccess(true);
			j.setMsg("增加人员类型成功");
			j.setObj(r);
		} catch (Exception e) {
			j.setMsg("增加人员类型失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改人员类型
	 */
	public void edit() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			rylx.setUserId(u.getId());
			rylxService.edit(rylx);
			j.setSuccess(true);
			j.setMsg("编辑人员类型成功！");
		} catch (Exception e) {
			j.setMsg("编辑人员类型失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 删除人员类型信息
	 */
	public void delete() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			rylx.setUserId(u.getId());
			rylxService.delete(rylx);
			j.setSuccess(true);
			j.setMsg("删除人员类型成功！");
		} catch (Exception e) {
			j.setMsg("删除人员类型失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	public void listRylx() {
		super.writeJson(rylxService.listRylx(rylx));
	}

	public void datagrid() {
		super.writeJson(rylxService.datagrid(rylx));
	}

	public Rylx getModel() {
		return rylx;
	}

	@Autowired
	public void setRylxService(RylxServiceI rylxService) {
		this.rylxService = rylxService;
	}
}