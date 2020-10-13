package lnyswz.jxc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Ywpd;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.YwpdServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Export;
import lnyswz.jxc.util.Util;

/**
 * 业务盘点Action
 * 
 * @author 王文阳
 * 
 */
@Namespace("/jxc")
@Action("ywpdAction")
public class YwpdAction extends BaseAction implements ModelDriven<Ywpd> {
	private static final long serialVersionUID = 1L;
	private Ywpd ywpd = new Ywpd();
	private YwpdServiceI ywpdService;

	/**
	 * 保存数据
	 */
	public void save() {
		User user = (User) session.get("user");
		ywpd.setCreateId(user.getId());
		ywpd.setCreateName(user.getRealName());
		Json j = new Json();
		try {
			j.setObj(ywpdService.save(ywpd));
			// 添加成功
			j.setSuccess(true);
			j.setMsg("保存业务盘点成功！");
		} catch (Exception e) {
			j.setMsg("保存业务盘点失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 冲减单据
	 */
	public void cjYwpd() {
		User user = (User) session.get("user");
		ywpd.setCjId(user.getId());
		ywpd.setCjName(user.getRealName());;
		Json j = new Json();
		try {
			j.setObj(ywpdService.cjYwpd(ywpd));
			// 添加成功
			j.setSuccess(true);
			j.setMsg("冲减业务盘点成功！");
		} catch (Exception e) {
			j.setMsg("冲减业务盘点失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	public void printYwpd() {
		User user = (User) session.get("user");
//		ywpd.setCreateName(user.getRealName());
		ywpd.setCreateName(user == null ? Util.unicodeToString(ywpd.getCreateName()) : user.getRealName());
		DataGrid dg = ywpdService.printYwpd(ywpd);
		Export.print(dg, Util.getReportName(ywpd.getBmbh(), "report_ywpd.json"));
		//Export.print(dg, Constant.REPORT_YWPD.get(ywpd.getBmbh()));
	}
	
	public void getSpkc(){
		writeJson(ywpdService.getSpkc(ywpd));
	}
	
	public void toKfpd() {
		writeJson(ywpdService.toKfpd(ywpd.getYwpdlsh()));
	}

	public void datagrid() {
		User user = (User) session.get("user");
		ywpd.setCreateId(user.getId());
		writeJson(ywpdService.datagrid(ywpd));
	}

	public void detDatagrid() {
		writeJson(ywpdService.detDatagrid(ywpd.getYwpdlsh()));
	}

	@Override
	public Ywpd getModel() {
		return ywpd;
	}

	@Autowired
	public void setYwpdService(YwpdServiceI ywpdService) {
		this.ywpdService = ywpdService;
	}

}
