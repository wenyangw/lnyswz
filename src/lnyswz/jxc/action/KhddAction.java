package lnyswz.jxc.action;

import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Khdd;
import lnyswz.jxc.bean.KhddDet;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.KhddServiceI;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
		try {
			Khdd k = khddService.cancelKhdd(khdd);
			if(k.getIsCancel().equals("1")){
                j.setMsg("取消客户订单成功！");
                j.setSuccess(true);
			} else {
                j.setMsg("订单已处理，无法取消！");
            }
            j.setObj(k);
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
		try {
			Khdd k = khddService.refuseKhdd(khdd);
			if (k != null) {
				j.setSuccess(true);
				j.setMsg("退回客户订单成功！");
				j.setObj(k);
			} else {
				j.setMsg("退回客户订单不成功！");
			}
		} catch (Exception e) {
			j.setMsg("退回客户订单失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 处理订单
	 */
	public void handleKhdd(){
		Json j = new Json();
		try {
			Khdd k = khddService.handleKhdd(khdd);
			if (k != null) {
				j.setSuccess(true);
				j.setMsg("退回客户订单成功！");
				j.setObj(k);
			} else {
				j.setMsg("退回客户订单不成功！");
			}
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
		DataGrid d = khddService.getKhdds(khdd);

		if (d != null){
			j.setObj(d);
			j.setSuccess(true);
		}
		writeJson(j);
	}

    public void getKhddsByYwy() {
	    Json j = new Json();
	    DataGrid d = khddService.getKhddsByYwy(khdd);
	    if (d != null) {
            j.setObj(d);
            j.setSuccess(true);
        }
	    writeJson(j);
    }

	public void getKhddDet(){
	    Json j = new Json();
	    List<KhddDet> l = khddService.getKhddDet(khdd);
	    if (l != null && l.size() > 0) {
	    	j.setSuccess(true);
			j.setObj(l);
		}
		writeJson(j);
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
