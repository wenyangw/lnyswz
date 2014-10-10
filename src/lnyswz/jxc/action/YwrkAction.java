package lnyswz.jxc.action;

import java.io.ObjectOutputStream;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Ywrk;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.YwrkServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Export;

/**
 * 业务入库Action
 * 
 * @author 王文阳
 * 
 */
@Namespace("/jxc")
@Action("ywrkAction")
public class YwrkAction extends BaseAction implements ModelDriven<Ywrk> {
	private Logger logger = Logger.getLogger(YwrkAction.class);
	private Ywrk ywrk = new Ywrk();
	private YwrkServiceI ywrkService;
	
	private static final String CONTENTTYPE = "application/octet-stream";

	/**
	 * 保存数据
	 */
	public void save() {
		User user = (User) session.get("user");
		ywrk.setCreateId(user.getId());
		ywrk.setCreateName(user.getRealName());
		Json j = new Json();
		try {
			j.setObj(ywrkService.save(ywrk));
			// 添加成功
			j.setSuccess(true);
			j.setMsg("保存业务入库成功！");
		} catch (Exception e) {
			j.setMsg("保存业务入库失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 冲减单据
	 */
	public void cjYwrk() {
		User user = (User) session.get("user");
		ywrk.setCjId(user.getId());
		ywrk.setCjName(user.getRealName());;
		Json j = new Json();
		try {
			
			// 添加成功
			j.setObj(ywrkService.cjYwrk(ywrk));
			j.setSuccess(true);
			j.setMsg("冲减业务入库单成功！");
		} catch (Exception e) {
			j.setMsg("冲减业务入库单失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	public void getSpkc(){
		writeJson(ywrkService.getSpkc(ywrk));
	}
	
	public void toKfrk() {
		writeJson(ywrkService.toKfrk(ywrk.getYwrklsh()));
	}

	public void toYwbt() {
		writeJson(ywrkService.toYwbt(ywrk.getYwrklsh()));
	}
	
	public void datagrid() {
		writeJson(ywrkService.datagrid(ywrk));
	}

	public void detDatagrid() {
		writeJson(ywrkService.detDatagrid(ywrk.getYwrklsh()));
	}
	
	public void datagridDet() {
		writeJson(ywrkService.datagridDet(ywrk));
	}

	public void printYwrk() {
		User user = (User) session.get("user");
		ywrk.setCreateName(user.getRealName());
		DataGrid dg = ywrkService.printYwrk(ywrk);
		Export.print(dg, Constant.REPORT_YWRK.get(ywrk.getBmbh()));
	}

	@Override
	public Ywrk getModel() {
		return ywrk;
	}

	@Autowired
	public void setYwrkService(YwrkServiceI ywrkService) {
		this.ywrkService = ywrkService;
	}

}
