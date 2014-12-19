package lnyswz.jxc.action;

import java.util.List;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.jxc.bean.Catalog;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.CatalogServiceI;
import lnyswz.jxc.service.UserServiceI;

/**
 * 登录操作Action
 * 
 * @author 王文阳
 * 
 */
public class LoginAction extends BaseAction implements ModelDriven<User> {

	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(LoginAction.class);

	private UserServiceI userService;
	private CatalogServiceI catalogService;
	// 模型驱动获得传入的对象
	private User user = new User();

	@Action(value = "login", results = {
			@Result(name = SUCCESS, location = "/layout/index.jsp"),
			@Result(name = LOGIN, location = "/login.jsp") })
	public String login() {
		// 获得登录用户信息
		if (!user.getPassword().equals("")) {
			User u = userService.login(user);
			// 获得模块信息
			List<Catalog> top = catalogService.listCatas();
			// 登录成功
			if (u != null) {
				session.put("user", u);
				session.put("top", top);
				request.put("msg", "登录成功！");
				return SUCCESS;
			}
		}

		// 登录失败
		request.put("msg", "账号或密码错误，请重新输入！");
		return LOGIN;
	}

	/**
	 * 用户退出
	 * 
	 * @return
	 */
	@Action(value = "logout", results = { @Result(name = SUCCESS, location = "/login.jsp") })
	public String logout() {
		// 清空session
		session.clear();
		// 返回登录页面
		return SUCCESS;
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
