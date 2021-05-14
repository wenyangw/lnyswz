package lnyswz.jxc.action;


import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.bean.Rkfk;
import lnyswz.jxc.service.RkfkServiceI;
import lnyswz.jxc.util.Export;
import lnyswz.jxc.util.Util;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 销售回款Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("rkfkAction")
public class RkfkAction extends BaseAction implements ModelDriven<Rkfk>{
	private static final long serialVersionUID = 1L;
	private Rkfk rkfk = new Rkfk();
	private RkfkServiceI rkfkService;
	
	/**
	 * 保存付款
	 */
	public void save(){		
		User user = (User)session.get("user");
		rkfk.setCreateId(user.getId());
		rkfk.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			//将获得的前台内容传入Service
			Rkfk r = rkfkService.save(rkfk);
			//添加成功
			j.setSuccess(true);
			j.setMsg("保存付款成功");
			j.setObj(r);
		}catch(Exception e){
			j.setMsg("保存付款失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 取消付款
	 */
	public void cancelRkfk(){
		User user = (User)session.get("user");
		rkfk.setCancelId(user.getId());
		rkfk.setCancelName(user.getRealName());
		Json j = new Json();
		try{
			rkfkService.cancelRkfk(rkfk);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("取消入库付款成功！");
		}catch(Exception e){
			j.setMsg("取消入库付款失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
//	public void printRkfk() {
//		User user = (User)session.get("user");
////		rkfk.setCreateName(user.getRealName());
//		rkfk.setCreateName(user == null ? Util.unicodeToString(rkfk.getCreateName()) : user.getRealName());
//		DataGrid dg = rkfkService.printRkfk(rkfk);
//		Export.print(dg, Util.getReportName(rkfk.getBmbh(), "report_rkfk.json"));
//		//Export.print(dg, Constant.REPORT_XSHK.get(rkfk.getBmbh()));
//	}
	
	
//	public void exportRkfk() {
//		User user = (User)session.get("user");
//		rkfk.setCreateName(user.getRealName());
//		Json j = new Json();
//		String type = Export.getExportType(rkfk.getType());
//		String location = "/export/rkfk_" + rkfk.getGysbh() + "_" + DateUtil.dateToStringWithTime(new Date(),"yyyyMMddHHmmss") + "." + type;
//		DataGrid dg = rkfkService.printRkfk(rkfk);
//		Export.export(dg, Util.getReportName(rkfk.getBmbh(), "report_rkfk.json"), location, type);
//		//Export.export(dg, Constant.REPORT_XSHK.get(rkfk.getBmbh()), location, type);
//		j.setSuccess(true);
//		j.setObj(location);
//		j.setMsg("导出成功");
//		writeJson(j);
//	}
	
	/**
	 * 返回
	 */
	public void datagrid(){
		writeJson(rkfkService.datagrid(rkfk));
	}
	
	public void detDatagrid(){
		writeJson(rkfkService.detDatagrid(rkfk));
	}
	
	@Override
	public Rkfk getModel() {
		return rkfk;
	}
	
	@Autowired
	public void setRkfkService(RkfkServiceI rkfkService) {
		this.rkfkService = rkfkService;
	}
}
