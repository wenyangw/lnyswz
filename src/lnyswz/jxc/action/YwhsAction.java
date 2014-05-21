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
import lnyswz.jxc.bean.Ywhs;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.YwhsServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Export;

/**
 * 业务换算Action
 * 
 * @author 王文阳
 * 
 */
@Namespace("/jxc")
@Action("ywhsAction")
public class YwhsAction extends BaseAction implements ModelDriven<Ywhs> {
	private Logger logger = Logger.getLogger(YwhsAction.class);
	private Ywhs ywhs = new Ywhs();
	private YwhsServiceI ywhsService;

	/**
	 * 保存数据
	 */
	public void save() {
		User user = (User) session.get("user");
		ywhs.setCreateId(user.getId());
		ywhs.setCreateName(user.getRealName());
		Json j = new Json();
		try {
			j.setObj(ywhsService.save(ywhs));
			// 添加成功
			j.setSuccess(true);
			j.setMsg("保存业务换算成功！");
		} catch (Exception e) {
			j.setMsg("保存业务换算失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 冲减单据
	 */
	public void cjYwhs() {
		User user = (User) session.get("user");
		ywhs.setCjId(user.getId());
		ywhs.setCjName(user.getRealName());;
		Json j = new Json();
		try {
			j.setObj(ywhsService.cjYwhs(ywhs));
			// 添加成功
			j.setSuccess(true);
			j.setMsg("冲减业务换算成功！");
		} catch (Exception e) {
			j.setMsg("冲减业务换算失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	public void getSpkc(){
		writeJson(ywhsService.getSpkc(ywhs));
	}
	
	public void toKfhs() {
		writeJson(ywhsService.toKfhs(ywhs.getYwhslsh()));
	}

	public void datagrid() {
		writeJson(ywhsService.datagrid(ywhs));
	}

	public void detDatagrid() {
		writeJson(ywhsService.detDatagrid(ywhs));
	}

	public void printYwhs() {
		User user = (User) session.get("user");
		ywhs.setCreateName(user.getRealName());
		DataGrid dg = ywhsService.printYwhs(ywhs);
		Export.print(dg, Constant.REPORT_YWHS.get(ywhs.getBmbh()));
	}
		
	@Override
	public Ywhs getModel() {
		return ywhs;
	}

	@Autowired
	public void setYwhsService(YwhsServiceI ywhsService) {
		this.ywhsService = ywhsService;
	}

}
