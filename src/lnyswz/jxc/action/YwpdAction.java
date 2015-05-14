package lnyswz.jxc.action;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Ywpd;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.YwpdServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Export;

/**
 * 业务盘点Action
 * 
 * @author 王文阳
 * 
 */
@Namespace("/jxc")
@Action("ywpdAction")
public class YwpdAction extends BaseAction implements ModelDriven<Ywpd> {
	private Logger logger = Logger.getLogger(YwpdAction.class);
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
		ywpd.setCreateName(user.getRealName());
		DataGrid dg = ywpdService.printYwpd(ywpd);
		Export.print(dg, Constant.REPORT_YWPD.get(ywpd.getBmbh()));
	}
	
	public void getSpkc(){
		writeJson(ywpdService.getSpkc(ywpd));
	}
	
	public void toKfpd() {
		writeJson(ywpdService.toKfpd(ywpd.getYwpdlsh()));
	}

	public void datagrid() {
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
