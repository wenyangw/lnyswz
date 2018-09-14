package lnyswz.jxc.action;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Kfhs;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.KfhsServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Export;
import lnyswz.jxc.util.Util;

/**
 * 业务换算Action
 * 
 * @author 王文阳
 * 
 */
@Namespace("/jxc")
@Action("kfhsAction")
public class KfhsAction extends BaseAction implements ModelDriven<Kfhs> {
	private static final long serialVersionUID = 1L;
	private Kfhs kfhs = new Kfhs();
	private KfhsServiceI kfhsService;

	/**
	 * 保存数据
	 */
	public void save() {
		User user = (User) session.get("user");
		kfhs.setCreateId(user.getId());
		kfhs.setCreateName(user.getRealName());
		Json j = new Json();
		try {
			kfhsService.save(kfhs);
			// 添加成功
			j.setSuccess(true);
			j.setMsg("保存库房调号成功！");
		} catch (Exception e) {
			j.setMsg("保存库房调号失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 冲减单据
	 */
	public void cjKfhs() {
		User user = (User) session.get("user");
		kfhs.setCjId(user.getId());
		kfhs.setCjName(user.getRealName());;
		Json j = new Json();
		try {
			kfhsService.cjKfhs(kfhs);
			// 添加成功
			j.setSuccess(true);
			j.setMsg("冲减库房调号成功！");
		} catch (Exception e) {
			j.setMsg("冲减库房调号失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	public void printKfhs() {
		User user = (User)session.get("user");
		kfhs.setCreateName(user.getRealName());
		DataGrid dg = kfhsService.printKfhs(kfhs);
		Export.print(dg, Util.getReportName(kfhs.getBmbh(), "report_kfhs.json"));
		//Export.print(dg, Constant.REPORT_KFHS.get(kfhs.getBmbh()));
	}
	
	public void datagrid() {
		User user = (User) session.get("user");
		kfhs.setCreateId(user.getId());
		writeJson(kfhsService.datagrid(kfhs));
	}

	public void detDatagrid() {
		writeJson(kfhsService.detDatagrid(kfhs.getKfhslsh()));
	}

	@Override
	public Kfhs getModel() {
		return kfhs;
	}

	@Autowired
	public void setKfhsService(KfhsServiceI kfhsService) {
		this.kfhsService = kfhsService;
	}

}
