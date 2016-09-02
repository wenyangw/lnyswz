package lnyswz.jxc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Pdlx;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.PdlxServiceI;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/jxc")
@Action("pdlxAction")
public class PdlxAction extends BaseAction implements ModelDriven<Pdlx> {
	private static final long serialVersionUID = 1L;
	private Pdlx pdlx = new Pdlx();
	private PdlxServiceI pdlxService;

	/**
	 * 增加盘点类型
	 */
	public void add() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			pdlx.setUserId(u.getId());
			Pdlx r = pdlxService.add(pdlx);
			j.setSuccess(true);
			j.setMsg("增加盘点类型成功");
			j.setObj(r);
		} catch (Exception e) {
			j.setMsg("增加盘点类型失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改盘点类型
	 */
	public void edit() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			pdlx.setUserId(u.getId());
			pdlxService.edit(pdlx);
			j.setSuccess(true);
			j.setMsg("编辑盘点类型成功！");
		} catch (Exception e) {
			j.setMsg("编辑盘点类型失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 删除盘点类型
	 */
	public void delete() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			pdlx.setUserId(u.getId());
			pdlxService.delete(pdlx);
			j.setSuccess(true);
			j.setMsg("删除盘点类型成功！");
		} catch (Exception e) {
			j.setMsg("删除盘点类型失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	public void listPdlx() {
		super.writeJson(pdlxService.listPdlx(pdlx));
	}

	public void datagrid() {
		super.writeJson(pdlxService.datagrid(pdlx));
	}

	public Pdlx getModel() {
		return pdlx;
	}

	@Autowired
	public void setPdlxService(PdlxServiceI pdlxService) {
		this.pdlxService = pdlxService;
	}
}