package lnyswz.jxc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.KhUser;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.KhUserServiceI;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/jxc")
@Action("khUserAction")
public class KhUserAction extends BaseAction implements ModelDriven<KhUser> {
	private static final long serialVersionUID = 1L;
	private KhUser khUser = new KhUser();
	private KhUserServiceI khUserService;

	/**
	 * 增加客户用户
	 */
	public void add() {
		Json j = new Json();
		try {
			KhUser r = khUserService.add(khUser);
			j.setSuccess(true);
			j.setMsg("增加客户用户成功");
			j.setObj(r);
		} catch (Exception e) {
			j.setMsg("增加客户用户失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改客户用户
	 */
	public void edit() {
		Json j = new Json();
		try {
			khUserService.edit(khUser);
			j.setSuccess(true);
			j.setMsg("编辑客户用户成功！");
		} catch (Exception e) {
			j.setMsg("编辑客户用户失败！");
		}
		writeJson(j);
	}

	/**
	 * 删除客户用户
	 */
	public void delete() {
		Json j = new Json();
		try {
			khUserService.delete(khUser);
			j.setSuccess(true);
			j.setMsg("删除客户用户成功！");
		} catch (Exception e) {
			j.setMsg("删除客户用户失败！");
		}
		writeJson(j);
	}

	/**
	 * 验证用户是否已在系统注册
	 */
	public void checkKhUser(){
		Json j = new Json();
        KhUser r = khUserService.checkKhUser(khUser);
        if(r != null) {
            j.setSuccess(true);
            j.setObj(r);
        }
		writeJson(j);
	}

	public KhUser getModel() {
		return khUser;
	}

	@Autowired
	public void setKhUserService(KhUserServiceI khUserService) {
		this.khUserService = khUserService;
	}
}