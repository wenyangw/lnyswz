package lnyswz.jxc.action;

import org.apache.log4j.Logger;
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
 * 业务盘点Action
 * 
 * @author 王文阳
 * 
 */
@Namespace("/jxc")
@Action("ywshAction")
public class YwshAction extends BaseAction implements ModelDriven<Ywsh> {
	private Logger logger = Logger.getLogger(YwshAction.class);
	private Ywsh ywsh = new Ywsh();
	private YwshServiceI ywshService;

	/**
	 * 保存数据
	 */
	public void audit() {
		User user = (User) session.get("user");
		ywsh.setCreateId(user.getId());
		ywsh.setCreateName(user.getRealName());
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
	public void xqshAudit() {
		User user = (User) session.get("user");
		ywsh.setCreateId(user.getId());
		ywsh.setCreateName(user.getRealName());
		Json j = new Json();
		try {
			ywshService.updateXqshAudit(ywsh);
			// 添加成功
			j.setSuccess(true);
			j.setMsg("采购需求审批成功！");
		} catch (Exception e) {
			j.setMsg("采购需求业务审批失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	public void refuse() {
		User user = (User) session.get("user");
		ywsh.setCreateId(user.getId());
		ywsh.setCreateName(user.getRealName());
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
	
	public void xqshRefuse() {
		User user = (User) session.get("user");
		ywsh.setCreateId(user.getId());
		ywsh.setCreateName(user.getRealName());
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


	public void datagrid() {
		User user = (User) session.get("user");
		ywsh.setCreateId(user.getId());
		writeJson(ywshService.datagrid(ywsh));
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
	
	public void detDatagrid() {
		writeJson(ywshService.detDatagrid(ywsh.getLsh()));
	}
	
	public void listAudits(){
		User user = (User) session.get("user");
		ywsh.setCreateId(user.getId());
//		ywsh.setCreateName(user.getRealName());
		writeJson(ywshService.listAudits(ywsh));
	}
	
	public void listCgxqAudits(){
		User user = (User) session.get("user");
		ywsh.setCreateId(user.getId());
//		ywsh.setCreateName(user.getRealName());
		writeJson(ywshService.listCgxqAudits(ywsh));
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
