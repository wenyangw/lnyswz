package lnyswz.jxc.action;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Ywrk;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.YwrkServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Export;
import lnyswz.jxc.util.Util;

/**
 * 业务入库Action
 * 
 * @author 王文阳
 * 
 */
@Namespace("/jxc")
@Action("ywrkAction")
public class YwrkAction extends BaseAction implements ModelDriven<Ywrk> {
	private static final long serialVersionUID = 1L;
	private Ywrk ywrk = new Ywrk();
	private YwrkServiceI ywrkService;
	
	/**
	 * 保存数据
	 */
	public void save() {
		User user = (User) session.get("user");
		ywrk.setCreateId(user.getId());
		ywrk.setCreateName(user.getRealName());
		Json j = new Json();
		try {
			boolean flag = true;
			if(ywrk.getYwrklshs() != null && ywrk.getYwrklshs().length() > 0){
				String[] lshs = ywrk.getYwrklshs().split(",");
				for(String lsh : lshs){
					ywrk.setYwrklsh(lsh);
					Ywrk y = ywrkService.getYwrk(ywrk);
					if(y.getIsCj().equals("1")){
						flag = false;
						break;
					}
				}
			}
			if(flag){
				j.setObj(ywrkService.save(ywrk));
				// 添加成功
				j.setSuccess(true);
				j.setMsg("保存业务入库成功！");
				
			}else{
				j.setSuccess(false);
				j.setMsg("该笔业务入库已转换，请不要重复提交！");
			}
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
			Ywrk y = ywrkService.getYwrk(ywrk);
			if(y.getIsCj().equals("1")){
				j.setSuccess(false);
				j.setMsg("该笔业务入库单已冲减，不要重复提交！");
			}else{
				// 添加成功
				j.setObj(ywrkService.cjYwrk(ywrk));
				j.setSuccess(true);
				j.setMsg("冲减业务入库单成功！");
			}
		} catch (Exception e) {
			j.setMsg("冲减业务入库单失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	public void getSpkc(){
		writeJson(ywrkService.getSpkc(ywrk));
	}
	
	public void changeYwrk() {
		writeJson(ywrkService.changeYwrk(ywrk));
	}
	
	public void toKfrk() {
		writeJson(ywrkService.toKfrk(ywrk.getYwrklsh()));
	}

	public void toYwbt() {
		writeJson(ywrkService.toYwbt(ywrk.getYwrklsh()));
	}
	
	public void toXsth() {
		writeJson(ywrkService.toXsth(ywrk));
	}
	
	public void datagrid() {
		User user = (User) session.get("user");
		ywrk.setCreateId(user.getId());
		writeJson(ywrkService.datagrid(ywrk));
	}

	public void detDatagrid() {
		writeJson(ywrkService.detDatagrid(ywrk.getYwrklsh()));
	}
	
	public void datagridDet() {
		writeJson(ywrkService.datagridDet(ywrk));
	}

	public void ywrkmx() {
		writeJson(ywrkService.ywrkmx(ywrk));
	}

	public void printYwrk() {
		User user = (User) session.get("user");
		ywrk.setCreateName(user == null ? Util.unicodeToString(ywrk.getCreateName()) : user.getRealName());
		DataGrid dg = ywrkService.printYwrk(ywrk);
		Export.print(dg, Util.getReportName(ywrk.getBmbh(), "report_ywrk.json"));
		//Export.print(dg, Constant.REPORT_YWRK.get(ywrk.getBmbh()));
	}
	
	public void printKfrk() {
		User user = (User) session.get("user");
//		ywrk.setCreateName(user.getRealName());
		ywrk.setCreateName(user == null ? Util.unicodeToString(ywrk.getCreateName()) : user.getRealName());
		DataGrid dg = ywrkService.printKfrk(ywrk);
		Export.print(dg, Util.getReportName(ywrk.getBmbh(), "report_kfrk.json"));
		//Export.print(dg, Constant.REPORT_KFRK.get(ywrk.getBmbh()));
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
