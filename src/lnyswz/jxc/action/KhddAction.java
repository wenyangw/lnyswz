package lnyswz.jxc.action;

import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Khdd;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.KhddServiceI;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 客户订单Action
 * @author 王文阳
 * @edited
 * 	2020.04.23 新建
 * 
 */
@Namespace("/jxc")
@Action("khddAction")
public class KhddAction extends BaseAction implements ModelDriven<Khdd>{
	private Khdd khdd = new Khdd();
	private KhddServiceI khddService;

	/**
	 * 保存数据
	 */
	public void saveKhdd(){
		User user = (User)session.get("user");
		if (user != null) {
			khdd.setCreateId(user.getId());
			khdd.setCreateName(user.getRealName());
		}
		Json j = new Json();
		try {
			j.setObj(khddService.saveKhdd(khdd));
			//添加成功
			j.setSuccess(true);
			j.setMsg("保存客户订单成功！");
		} catch (Exception e) {
			j.setMsg("保存客户订单失败！");
			e.printStackTrace();
		}

		writeJson(j);
	}
	
	/**
	 * 取消订单
	 */
	public void cancelKhdd(){
		Json j = new Json();
		User user = (User) session.get("user");
		if(user != null) {
			khdd.setCancelId(user.getId());
			khdd.setCancelName(user.getRealName());
		}
		try {
			String ret = khddService.cancelKhdd(khdd);
			j.setSuccess(ret.equals(""));
			j.setMsg(ret);
		} catch (Exception e) {
			j.setMsg("取消客户订单失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 退回订单
	 */
	public void refuseKhdd(){
		Json j = new Json();
		User user = (User) session.get("user");
		if(user != null) {
			khdd.setRefuseId(user.getId());
			khdd.setRefuseName(user.getRealName());
		}
		try {
			khddService.refuseKhdd(khdd);
			j.setSuccess(true);
			j.setMsg("退回客户订单成功！");
		} catch (Exception e) {
			j.setMsg("退回客户订单失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	public void getKhdd(){
		Json j = new Json();
		j.setObj(khddService.getKhdd(khdd));
		writeJson(j);
	}

	public void getKhdds(){
		Json j = new Json();
		User u = (User)session.get("user");
		if (u != null) {
			khdd.setCreateId(u.getId());
			if ("1".equals(u.getIsYwy())) {
				khdd.setYwyId(u.getId());
			}
		}
		j.setObj(khddService.getKhdds(khdd));
		writeJson(j);
	}

	public void getKhddDet(){
		writeJson(khddService.getKhddDet(khdd));
	}

	@Override
	public Khdd getModel() {
		return khdd;
	}

	@Autowired
	public void setKhddService(KhddServiceI khddService) {
		this.khddService = khddService;
	}

}
