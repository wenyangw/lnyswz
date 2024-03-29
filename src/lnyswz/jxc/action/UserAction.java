package lnyswz.jxc.action;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.UserServiceI;

/**
 * 用户Action
 * @author 王文阳
 *
 */
@Namespace("/admin")
@Action(value = "userAction")
public class UserAction extends BaseAction implements ModelDriven<User>{
	private static final long serialVersionUID = 1L;
	private UserServiceI userService;
	private User user = new User();
	
	/**
	 * 增加用户
	 */
	public void add(){
		Json j = new Json();
		//获取操作者id，供操作日志使用
		User u = (User)session.get("user");
		user.setOperaId(u.getId());
		try{
			j.setObj(userService.add(user));
			j.setSuccess(true);
			j.setMsg("增加用户成功！");
		}catch(Exception e){
			j.setMsg("增加用户失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 编辑用户
	 */
	public void edit(){
		Json j = new Json();
		//获取操作者id，供操作日志使用
		User u = (User)session.get("user");
		user.setOperaId(u.getId());
		try{
			userService.edit(user);
			j.setSuccess(true);
			j.setMsg("修改用户成功！");
		}catch(Exception e){
			j.setMsg("修改用户失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	/**
	 * 修改密码
	 */
	public void editPassword(){
		Json j = new Json();
		User u = (User)session.get("user");
		user.setId(u.getId());
		try{
			userService.editPassword(user);
			j.setSuccess(true);
			j.setMsg("修改用户成功！");
		}catch(Exception e){
			j.setMsg("修改用户失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	/**
	 * 判断密码是否正确
	 */
	public void checkPassword() {
		Json j = new Json();	
		User u = (User)session.get("user");
		user.setId(u.getId());
		if (userService.checkPassword(user)){
			j.setSuccess(true);	
		}
		writeJson(j);
	}
	
	/**
	 * 删除用户
	 */
	public void delete(){
		Json j = new Json();
		//获取操作者id，供操作日志使用
		User u = (User)session.get("user");
		user.setOperaId(u.getId());
		try{
			userService.delete(user);
			j.setSuccess(true);
			j.setMsg("删除用户成功！");
		}catch(Exception e){
			j.setMsg("删除用户失败！");
			e.printStackTrace();
		}
		writeJson(j); 
	}
	
	/**
	 * 返回用户列表，供管理用，有分页
	 */
	public void datagrid(){
		writeJson(userService.datagrid(user));
	}
	
	public void listYwys(){
		writeJson(userService.listYwys(user.getDid()));
	}
	/**
	 * 获取用户关联业务员账号
	 */
	public void listYwyByYwy(){
		writeJson(userService.listYwyByYwy(user.getId()));
	}
	public void listBgys(){
		User u = (User)session.get("user");
		user.setIsYwy(u.getIsYwy());
		writeJson(userService.listBgys(user));
	}
	
	public void getContacts(){
		writeJson(userService.getContacts(user));
	}

	/**
	 * 微信小程序验证注册的业务员是否有效
	 * @return
	 */
	public void checkYwy() {
		Json  json = new Json();
		User ywy = userService.checkYwy(user);
		if(ywy != null) {
			json.setSuccess(true);
		}
		writeJson(json);
	}
	
	@Override
	public User getModel() {
		return user;
	}
		
	@Autowired
	public void setUserService(UserServiceI userService) {
		this.userService = userService;
	}
}
