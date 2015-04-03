package lnyswz.jxc.action;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Gys;
import lnyswz.jxc.bean.Kh;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.KhServiceI;

import com.opensymphony.xwork2.ModelDriven;

@Namespace("/jxc")
@Action("khAction")
public class KhAction extends BaseAction implements ModelDriven<Kh> {
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(LoginAction.class);
	private Kh kh = new Kh();
	private KhServiceI khService;

	/**
	 * 增加客户
	 */
	public void add() {
		Json j = new Json();
		try {
			User u = (User)session.get("user");
			kh.setUserId(u.getId());
			Kh r = khService.add(kh);
			j.setSuccess(true);
			j.setMsg("增加客户成功");
			j.setObj(r);
		} catch (Exception e) {
			j.setMsg("增加客户失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改客户
	 */
	public void edit() {
		Json j = new Json();
		try {
			User u = (User)session.get("user");
			kh.setUserId(u.getId());
			khService.edit(kh);
			j.setSuccess(true);
			j.setMsg("编辑客户成功！");
		} catch (Exception e) {
			j.setMsg(e.getStackTrace().toString());
			j.setMsg("编辑客户失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 删除客户信息
	 */
	public void delete() {
		Json j = new Json();
		User u = (User)session.get("user");
		kh.setUserId(u.getId());
		if (!khService.delete(kh)) {
			j.setMsg("对不起！此客户被其他部门占用！");
		} else {
			j.setSuccess(true);
			j.setMsg("删除成功！");
		}
		writeJson(j);
	}

	public void datagrid() {
		super.writeJson(khService.datagrid(kh));
	}

	
	public void datagridDet() {
		super.writeJson(khService.datagridDet(kh));
	}

	/**
	 * 判断客户信息是否存在
	 */
	public void existKh() {
		Json j = new Json();
		if (khService.existKh(kh)) {
			j.setMsg("对不起！此客户编号已存在！");
		} else {
			j.setSuccess(true);
		}
		writeJson(j);
	}
	
	/**
	 * 判断客户信息是否存在
	 */
	public void existKhDet() {
		Json j = new Json();
		if (khService.existKhDet(kh)) {
			j.setMsg("对不起！此客户业务员授信已存在！");
		} else {
			j.setSuccess(true);
		}
		writeJson(j);
	}

	public void isSxkh(){
		Json j = new Json();
		if (khService.isSxkh(kh)) {
			j.setSuccess(true);
		}
		writeJson(j);
	}
	
	/**
	 * 增加专属信息
	 */
	public void addDet() {
		Json j = new Json();
		try {
			User u = (User)session.get("user");
			kh.setUserId(u.getId());
			Kh k = khService.addDet(kh);
			j.setSuccess(true);
			j.setMsg("客户授信维护成功!");
			j.setObj(k);
		} catch (Exception e) {
			j.setMsg("客户授信维护失败!");
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	
	/**
	 * 修改专属信息
	 */
	public void editDet() {
		Json j = new Json();
		try {
			User u = (User)session.get("user");
			kh.setUserId(u.getId());
			khService.editDet(kh);
			j.setSuccess(true);
			j.setMsg("客户授信维护成功!");
		} catch (Exception e) {
			j.setMsg("客户授信维护失败!");
			e.printStackTrace();
		}
		super.writeJson(j);
	}

	/**
	 * 删除客户信息专属信息
	 */
	public void deleteDet() {
		Json j = new Json();
		try {
			User u = (User)session.get("user");
			kh.setUserId(u.getId());
			khService.deleteDet(kh);
			j.setSuccess(true);
			j.setMsg("删除客户成功！");
		} catch (Exception e) {
			j.setMsg(e.getStackTrace().toString());
			e.printStackTrace();
			j.setMsg("删除客户失败！");
		}
		writeJson(j);
	}

	public void listKhs() {
		super.writeJson(khService.listKhs(kh));
	}

	/**
	 * 王文阳
	 * 2013.10.08
	 * 获得客户信息
	 */
	public void loadKh(){
		Json j = new Json();
		try{
			Kh k = khService.loadKh(kh.getKhbh(), kh.getDepId());
			j.setSuccess(true);
			j.setObj(k);
		}catch(Exception e){
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	
	
	/**
	 * 王文阳
	 * 2013.10.08
	 * 获得客户检索信息
	 */
	public void khDg(){
		writeJson(khService.khDg(kh));
	}
	
	public void checkKh(){
		Json j = new Json();
		Kh k = khService.checkKh(kh);
		if(k.getLsje().compareTo(k.getSxje()) >= 0){
			j.setMsg("该客户授信额度为" + k.getSxje() + ",目前应付金额为" + k.getLsje() + ",已不能继续销售！");
		}else{
			j.setSuccess(true);
		}
		super.writeJson(j);
	}
	
	public void sxkhDg(){
		writeJson(khService.sxkhDg(kh));
	}
	
	public void listKhByYwy(){
		writeJson(khService.listKhByYwy(kh));
	}
	
	public Kh getModel() {
		return kh;
	}

	@Autowired
	public void setKhService(KhServiceI khService) {
		this.khService = khService;
	}
}