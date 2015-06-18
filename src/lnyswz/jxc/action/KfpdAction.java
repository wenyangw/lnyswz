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
import lnyswz.jxc.bean.Kfpd;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.KfpdServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Export;

/**
 * 库房盘点Action
 * 
 * @author 王文阳
 * 
 */
@Namespace("/jxc")
@Action("kfpdAction")
public class KfpdAction extends BaseAction implements ModelDriven<Kfpd> {
	private Logger logger = Logger.getLogger(KfpdAction.class);
	private Kfpd kfpd = new Kfpd();
	private KfpdServiceI kfpdService;

	/**
	 * 保存数据
	 */
	public void save() {
		User user = (User) session.get("user");
		kfpd.setCreateId(user.getId());
		kfpd.setCreateName(user.getRealName());
		Json j = new Json();
		try {
			kfpdService.save(kfpd);
			// 添加成功
			j.setSuccess(true);
			j.setMsg("保存库房盘点成功！");
		} catch (Exception e) {
			j.setMsg("保存库房盘点失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 冲减单据
	 */
	public void cjKfpd() {
		User user = (User) session.get("user");
		kfpd.setCjId(user.getId());
		kfpd.setCjName(user.getRealName());;
		Json j = new Json();
		try {
			kfpdService.cjKfpd(kfpd);
			// 添加成功
			j.setSuccess(true);
			j.setMsg("冲减库房盘点成功！");
		} catch (Exception e) {
			j.setMsg("冲减库房盘点失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	public void getSpkc(){
		writeJson(kfpdService.getSpkc(kfpd));
	}
	
	public void datagrid() {
		writeJson(kfpdService.datagrid(kfpd));
	}

	public void detDatagrid() {
		writeJson(kfpdService.detDatagrid(kfpd.getKfpdlsh()));
	}

	@Override
	public Kfpd getModel() {
		return kfpd;
	}

	@Autowired
	public void setKfpdService(KfpdServiceI kfpdService) {
		this.kfpdService = kfpdService;
	}

}
