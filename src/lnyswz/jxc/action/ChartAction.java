package lnyswz.jxc.action;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.jxc.bean.Chart;
import lnyswz.jxc.service.ChartServiceI;
/**
 * 图表Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("chartAction")
public class ChartAction extends BaseAction implements ModelDriven<Chart>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Chart chart = new Chart();
	private ChartServiceI chartService;
	
	/**
	 * 销售数据对比
	 */
	public void getXstj(){
		writeJson(chartService.getXstj(chart));
	}
	
	/**
	 * 销售结构分析
	 */
	public void getXsjgfx(){
		writeJson(chartService.getXsjgfx(chart));
	}
	
	/**
	 * 库存对比
	 */
	public void getKctj(){
		writeJson(chartService.getKctj(chart));
	}
	
	/**
	 * 销售客户排名统计
	 */
	public void getXskhtj(){
		writeJson(chartService.getXskhtj(chart));
	}
	
	/**
	 * 销售客户分类排名统计
	 */
	public void getXskhfltj(){
		writeJson(chartService.getXskhfltj(chart));
	}
	
	/**
	 * in xskhfltj
	 */
	public void listKh(){
		writeJson(chartService.listKh(chart));
	}
	
	@Override
	public Chart getModel() {
		return chart;
	}

	@Autowired
	public void setChartService(ChartServiceI chartService) {
		this.chartService = chartService;
	}
	
	
}
