package lnyswz.jxc.action;

import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.KhUser;
import lnyswz.jxc.service.KhUserServiceI;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 登录操作Action
 * 
 * @author 王文阳
 * 
 */
@Namespace("/jxc")
@Action(value = "loginWxAction")
public class LoginWxAction extends BaseAction implements ModelDriven<KhUser> {

	private static final long serialVersionUID = 1L;

//	private KhUserServiceI khUserService;
	// 模型驱动获得传入的对象
	private KhUser khUser = new KhUser();

	public void login() {
		Json j = new Json();
		j.setSuccess(true);
		j.setObj(khUser);
		writeJson(j);
	}

	@Override
	public KhUser getModel() {
		return khUser;
	}


//	@Autowired
//	public void setKhUserService(KhUserServiceI khUserService) {
//		this.khUserService = khUserService;
//	}
}
