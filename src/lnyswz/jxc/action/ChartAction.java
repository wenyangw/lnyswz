package lnyswz.jxc.action;


import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Button;
import lnyswz.jxc.bean.Chart;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.ButtonServiceI;
import lnyswz.jxc.service.XskpServiceI;
/**
 * 功能按钮Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("chartAction")
public class ChartAction extends BaseAction implements ModelDriven<Chart>{
	private Logger logger = Logger.getLogger(ChartAction.class);
	private Chart chart = new Chart();
	private XskpServiceI xskpService;
	
	/**
	 * 返回所有功能按钮，供管理用，有分页
	 */
	public void chartXsje(){
		writeJson(xskpService.getChartXsje());
	}
	
	@Override
	public Chart getModel() {
		return chart;
	}

	@Autowired
	public void setXskpService(XskpServiceI xskpService) {
		this.xskpService = xskpService;
	}
	
	
}
