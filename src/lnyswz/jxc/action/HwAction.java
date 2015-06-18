package lnyswz.jxc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Hw;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.HwServiceI;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/jxc")
@Action("hwAction")
public class HwAction extends BaseAction implements ModelDriven<Hw> {
	private static final long serialVersionUID = 1L;
	private Hw hw = new Hw();
	private HwServiceI hwService;

	/**
	 * 增加货位
	 */
	public void add() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			hw.setUserId(u.getId());
			Hw r = hwService.add(hw);
			j.setSuccess(true);
			j.setMsg("增加货位成功");
			j.setObj(r);
		} catch (Exception e) {
			j.setMsg("增加货位失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改货位
	 */
	public void edit() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			hw.setUserId(u.getId());
			hwService.edit(hw);
			j.setSuccess(true);
			j.setMsg("编辑货位成功！");
		} catch (Exception e) {
			j.setMsg(e.getStackTrace().toString());
		}
		writeJson(j);
	}

	/**
	 * 删除货位信息
	 */
	public void delete() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			hw.setUserId(u.getId());
			hwService.delete(hw);
			j.setSuccess(true);
			j.setMsg("删除货位成功！");
		} catch (Exception e) {
			j.setMsg(e.getStackTrace().toString());
		}
		writeJson(j);
	}

	public void datagrid() {
		super.writeJson(hwService.datagrid(hw));
	}

	public void listHw() {
		super.writeJson(hwService.listHw(hw));
	}

	public Hw getModel() {
		return hw;
	}

	@Autowired
	public void setHwService(HwServiceI hwService) {
		this.hwService = hwService;
	}
}