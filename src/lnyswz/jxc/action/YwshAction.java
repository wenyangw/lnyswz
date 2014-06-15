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
import lnyswz.jxc.bean.User;
import lnyswz.jxc.bean.Ywsh;
import lnyswz.jxc.service.YwshServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Export;

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
	public void save() {
		User user = (User) session.get("user");
		ywsh.setCreateId(user.getId());
		ywsh.setCreateName(user.getRealName());
		Json j = new Json();
		try {
			j.setObj(ywshService.save(ywsh));
			// 添加成功
			j.setSuccess(true);
			j.setMsg("保存业务盘点成功！");
		} catch (Exception e) {
			j.setMsg("保存业务盘点失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	public void datagrid() {
		writeJson(ywshService.datagrid(ywsh));
	}

	public void detDatagrid() {
		writeJson(ywshService.detDatagrid(ywsh.getYwshlsh()));
	}
	
	public void listAudits(){
		User user = (User) session.get("user");
		ywsh.setCreateId(user.getId());
//		ywsh.setCreateName(user.getRealName());
		writeJson(ywshService.listAudits(ywsh));
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
