package lnyswz.jxc.action;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.SelectCommon;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.SelectCommonServiceI;
import lnyswz.jxc.util.Export;
import lnyswz.jxc.util.ExportExcel;

@Namespace("/jxc")
@Action("selectCommonAction")
public class SelectCommomAction extends BaseAction implements
		ModelDriven<SelectCommon> {
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(LoginAction.class);
	private SelectCommon selectCommon = new SelectCommon();
	private SelectCommonServiceI selectCommonService;

	/**
	 * 查询
	 */
	public void selectCommonList() {
		Json j = new Json();
		try {
			DataGrid r = selectCommonService.selectCommonList(selectCommon);
			j.setSuccess(true);
			j.setObj(r);
		} catch (Exception e) {
			j.setMsg("查询失败！");
			e.printStackTrace();
		}
//		super.writeJson(selectCommonService.selectCommonList(selectCommon));
		writeJson(j);
	}
	public void selectCommonTree() {
		Json j = new Json();
		try {
			DataGrid r = selectCommonService.selectCommonTree(selectCommon);
			j.setSuccess(true);
			j.setObj(r);
		} catch (Exception e) {
			j.setMsg("查询失败！");
			e.printStackTrace();
		}
//		super.writeJson(selectCommonService.selectCommonList(selectCommon));
		writeJson(j);
	}

	/**
	 * 导出excel
	 */
	public void ExportExcel() {
		Json j = new Json();
		String location = null;
		ExportExcel<Object[]> ex = new ExportExcel<Object[]>();
		String[] headers = selectCommon.getTitles().split(",");
		OutputStream out;
		List<Object[]> dataset = selectCommonService.Exprot(selectCommon);
		try {
			User u = (User) session.get("user");
			location = "/excel/"
					+ u.getUserName()
					+ DateUtil.dateToStringWithTime(new Date(),
							"yyyyMMddHHmmss") + ".xls";
			System.out.println("Time:"  + location);
			String address = Export.getRootPath() + location;
			out = new FileOutputStream(address);
			ex.exportExcel(headers, dataset, out);
			out.close();
			j.setSuccess(true);
			j.setObj(location);
			j.setMsg("导出成功");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			j.setMsg("导出失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}


	public SelectCommon getModel() {
		return selectCommon;
	}

	@Autowired
	public void setSelectCommonService(SelectCommonServiceI selectCommonService) {
		this.selectCommonService = selectCommonService;
	}
}