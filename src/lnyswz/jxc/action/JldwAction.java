package lnyswz.jxc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Jldw;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.JldwServiceI;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/jxc")
@Action("jldwAction")
public class JldwAction extends BaseAction implements ModelDriven<Jldw> {
	private static final long serialVersionUID = 1L;
	private Jldw jldw = new Jldw();
	private JldwServiceI jldwService;

	/**
	 * 增加计量单位
	 */
	public void add() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			jldw.setUserId(u.getId());
			Jldw r = jldwService.add(jldw);
			j.setSuccess(true);
			j.setMsg("增加计量单位成功");
			j.setObj(r);
		} catch (Exception e) {
			j.setMsg("增加计量单位失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改计量单位
	 */
	public void edit() {
		Json j = new Json();
		User u = (User) session.get("user");
		jldw.setUserId(u.getId());
		try {
			jldwService.edit(jldw);
			j.setSuccess(true);
			j.setMsg("编辑计量单位成功！");
		} catch (Exception e) {
			j.setMsg(" 编辑计量单位失败！");
		}
		writeJson(j);
	}

	/**
	 * 删除计量单位信息
	 */
	public void delete() {
		Json j = new Json();
		User u = (User) session.get("user");
		jldw.setUserId(u.getId());
		try {
			jldwService.delete(jldw);
			j.setSuccess(true);
			j.setMsg("删除计量单位成功！");
		} catch (Exception e) {
			j.setMsg("删除计量单位失败！");
		}
		writeJson(j);
	}

	public void listJldw() {
		super.writeJson(jldwService.listJldw(jldw));
	}

	public void datagrid() {

		super.writeJson(jldwService.datagrid(jldw));
	}

	public Jldw getModel() {
		return jldw;
	}

	@Autowired
	public void setJldwService(JldwServiceI jldwService) {
		this.jldwService = jldwService;
	}
}