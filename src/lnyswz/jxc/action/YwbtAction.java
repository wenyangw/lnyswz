package lnyswz.jxc.action;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Ywbt;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.YwbtServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Export;

/**
 * 业务补调Action
 * 
 * @author 王文阳
 * 
 */
@Namespace("/jxc")
@Action("ywbtAction")
public class YwbtAction extends BaseAction implements ModelDriven<Ywbt> {
	private Logger logger = Logger.getLogger(YwbtAction.class);
	private Ywbt ywbt = new Ywbt();
	private YwbtServiceI ywbtService;

	/**
	 * 保存数据
	 */
	public void save() {
		User user = (User) session.get("user");
		ywbt.setCreateId(user.getId());
		ywbt.setCreateName(user.getRealName());
		Json j = new Json();
		try {
			j.setObj(ywbtService.save(ywbt));
			// 添加成功
			j.setSuccess(true);
			j.setMsg("保存业务补调成功！");
		} catch (Exception e) {
			j.setMsg("保存业务补调失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	public void printYwbt() {
		User user = (User) session.get("user");
		ywbt.setCreateName(user.getRealName());
		DataGrid dg = ywbtService.printYwbt(ywbt);
		Export.print(dg, Constant.REPORT_YWBT.get(ywbt.getBmbh()));
	}
	
	public void datagrid() {
		writeJson(ywbtService.datagrid(ywbt));
	}

	public void detDatagrid() {
		writeJson(ywbtService.detDatagrid(ywbt.getYwbtlsh()));
	}
		
	@Override
	public Ywbt getModel() {
		return ywbt;
	}

	@Autowired
	public void setYwbtService(YwbtServiceI ywbtService) {
		this.ywbtService = ywbtService;
	}

}
