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
import lnyswz.jxc.bean.Fyd;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.FydServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Export;
import lnyswz.jxc.util.Util;

/**
 * 付印单Action
 * 
 * @author 王文阳
 * 
 */
@Namespace("/jxc")
@Action("fydAction")
public class FydAction extends BaseAction implements ModelDriven<Fyd> {
	private static final long serialVersionUID = 1L;
	private Fyd fyd = new Fyd();
	private FydServiceI fydService;
	
	public void changeFyd() {
		writeJson(fydService.changeFyd(fyd));
	}
	
	public void datagrid() {
		writeJson(fydService.datagrid(fyd));
	}

	public void detDatagrid() {
		writeJson(fydService.detDatagrid(fyd.getFydlsh()));
	}
	
	public void datagridDet() {
		writeJson(fydService.datagridDet(fyd));
	}
	
	@Override
	public Fyd getModel() {
		return fyd;
	}

	@Autowired
	public void setFydService(FydServiceI fydService) {
		this.fydService = fydService;
	}

}
