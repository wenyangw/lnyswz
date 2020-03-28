package lnyswz.jxc.action;

import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Catalog;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.CatalogServiceI;
import lnyswz.jxc.service.UserServiceI;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录操作Action
 * 
 * @author 王文阳
 * 
 */
@Namespace("/admin")
@Action(value = "loginMAction")
public class LoginMAction extends BaseAction implements ModelDriven<User> {

	private static final long serialVersionUID = 1L;

	private UserServiceI userService;
	private CatalogServiceI catalogService;
	// 模型驱动获得传入的对象
	private User user = new User();

	public void login() {
		Json j = new Json();

		// 获得登录用户信息
		if (!user.getPassword().equals("")) {
			User u = userService.login(user);
			// 获得模块信息
			//List<Catalog> top = catalogService.listCatas();
			// 登录成功
			if (u != null) {
				j.setSuccess(true);
				j.setMsg("登录成功！");
				j.setObj(u);
			}else{
				j.setMsg("登录失败！");
			}
		}else{
			j.setMsg("登录失败！");
		}
		writeJson(j);
	}

	@Override
	public User getModel() {
		return user;
	}


	@Autowired
	public void setUserService(UserServiceI userService) {
		this.userService = userService;
	}

	@Autowired
	public void setCatalogService(CatalogServiceI catalogService) {
		this.catalogService = catalogService;
	}

}
