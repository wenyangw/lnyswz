package lnyswz.jxc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.bean.Ywsh;
import lnyswz.jxc.service.YwshServiceI;

/**
 * 业务审批Action
 * 
 * @author 王文阳
 * 
 */
@Namespace("/jxc")
@Action("ywshAction")
public class YwshAction extends BaseAction implements ModelDriven<Ywsh> {
	private static final long serialVersionUID = 1L;
	private Ywsh ywsh = new Ywsh();
	private YwshServiceI ywshService;

	/**
	 * 保存数据
	 */
	public void audit() {
		User user = (User) session.get("user");
		if(user != null){
			ywsh.setCreateId(user.getId());
			ywsh.setCreateName(user.getRealName());
		}
		Json j = new Json();
		try {
			ywshService.updateAudit(ywsh);
			// 添加成功
			j.setSuccess(true);
			j.setMsg("业务审批通过成功！");
		} catch (Exception e) {
			j.setMsg("业务审批通过失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 保存数据
	 */
	public void xjshAudit() {
		User user = (User) session.get("user");
		if(user != null) {
			ywsh.setCreateId(user.getId());
			ywsh.setCreateName(user.getRealName());
		}
		Json j = new Json();
		try {
			ywshService.updateXjshAudit(ywsh);
			// 添加成功
			j.setSuccess(true);
			j.setMsg("销售加价审批成功！");
		} catch (Exception e) {
			j.setMsg("销售加价审批失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 保存数据
	 */
	public void xqshAudit() {
		User user = (User) session.get("user");
		if(user != null) {
			ywsh.setCreateId(user.getId());
			ywsh.setCreateName(user.getRealName());
		}
		Json j = new Json();
		try {
			ywshService.updateXqshAudit(ywsh);
			// 添加成功
			j.setSuccess(true);
			j.setMsg("采购需求审批成功！");
		} catch (Exception e) {
			j.setMsg("采购需求审批失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 保存数据
	 */
	public void jhshAudit() {
		User user = (User) session.get("user");
		if(user != null) {
			ywsh.setCreateId(user.getId());
			ywsh.setCreateName(user.getRealName());
		}
		Json j = new Json();
		try {
			ywshService.updateJhshAudit(ywsh);
			// 添加成功
			j.setSuccess(true);
			j.setMsg("采购计划审批成功！");
		} catch (Exception e) {
			j.setMsg("采购计划审批失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	public void refuse() {
		User user = (User) session.get("user");
		if(user != null){
			ywsh.setCreateId(user.getId());
			ywsh.setCreateName(user.getRealName());
		}
		Json j = new Json();
		try {
			ywshService.updateRefuse(ywsh);
			// 添加成功
			j.setSuccess(true);
			j.setMsg("业务审批拒绝成功！");
		} catch (Exception e) {
			j.setMsg("业务审批拒绝失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	public void xjshRefuse() {
		User user = (User) session.get("user");
		if(user != null){
			ywsh.setCreateId(user.getId());
			ywsh.setCreateName(user.getRealName());
		}
		Json j = new Json();
		try {
			ywshService.updateXjshRefuse(ywsh);
			// 添加成功
			j.setSuccess(true);
			j.setMsg("销售加价审批拒绝成功！");
		} catch (Exception e) {
			j.setMsg("销售加价审批拒绝失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	public void xqshRefuse() {
		User user = (User) session.get("user");
		if(user != null){
			ywsh.setCreateId(user.getId());
			ywsh.setCreateName(user.getRealName());
		}
		Json j = new Json();
		try {
			ywshService.updateXqshRefuse(ywsh);
			// 添加成功
			j.setSuccess(true);
			j.setMsg("采购需求审批拒绝成功！");
		} catch (Exception e) {
			j.setMsg("采购需求审批拒绝失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	public void jhshRefuse() {
		User user = (User) session.get("user");
		if(user != null){
			ywsh.setCreateId(user.getId());
			ywsh.setCreateName(user.getRealName());
		}
		Json j = new Json();
		try {
			ywshService.updateJhshRefuse(ywsh);
			// 添加成功
			j.setSuccess(true);
			j.setMsg("采购计划审批拒绝成功！");
		} catch (Exception e) {
			j.setMsg("采购计划审批拒绝失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}


	public void datagrid() {
		User user = (User) session.get("user");
		ywsh.setCreateId(user.getId());
		writeJson(ywshService.datagrid(ywsh));
	}
	
	public void xqshDatagrid() {
		User user = (User) session.get("user");
		ywsh.setCreateId(user.getId());
		writeJson(ywshService.xqshDatagrid(ywsh));
	}
	
	public void jhshDatagrid() {
		User user = (User) session.get("user");
		ywsh.setCreateId(user.getId());
		writeJson(ywshService.jhshDatagrid(ywsh));
	}

	public void refreshYwsh() {
		User user = (User) session.get("user");
		ywsh.setCreateId(user.getId());
		writeJson(ywshService.refreshYwsh(ywsh));
	}
	
	public void refreshXqsh() {
		User user = (User) session.get("user");
		ywsh.setCreateId(user.getId());
		writeJson(ywshService.refreshXqsh(ywsh));
	}
	
	public void refreshJhsh() {
		User user = (User) session.get("user");
		ywsh.setCreateId(user.getId());
		writeJson(ywshService.refreshJhsh(ywsh));
	}

	public void refreshXjsh() {
		User user = (User) session.get("user");
		ywsh.setCreateId(user.getId());
		writeJson(ywshService.refreshXjsh(ywsh));
	}
	
	public void detDatagrid() {
		writeJson(ywshService.detDatagrid(ywsh.getLsh()));
	}
	
	public void listAudits(){
		User user = (User) session.get("user");
		if(user != null){
			ywsh.setCreateId(user.getId());
		}
//		ywsh.setCreateName(user.getRealName());
		writeJson(ywshService.listAudits(ywsh));
	}
	
	public void listCgxqAudits(){
		User user = (User) session.get("user");
		if(user != null){
			ywsh.setCreateId(user.getId());
		}
//		ywsh.setCreateName(user.getRealName());
		writeJson(ywshService.listCgxqAudits(ywsh));
	}
	
	public void listCgjhAudits(){
		User user = (User) session.get("user");
		if(user != null){
			ywsh.setCreateId(user.getId());
		}
//		ywsh.setCreateName(user.getRealName());
		writeJson(ywshService.listCgjhAudits(ywsh));
	}

	public void listXjshAudits(){
		User user = (User) session.get("user");
		if(user != null){
			ywsh.setCreateId(user.getId());
		}
		writeJson(ywshService.listXjshAudits(ywsh));
	}

	@Override
	public Ywsh getModel() {
		return ywsh;
	}

	@Autowired
	public void setYwshService(YwshServiceI ywshService) {
		this.ywshService = ywshService;
	}

}
