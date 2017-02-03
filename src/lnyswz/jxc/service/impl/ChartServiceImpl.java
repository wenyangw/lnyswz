package lnyswz.jxc.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Catalog;
import lnyswz.jxc.bean.Chart;
import lnyswz.jxc.bean.Serie;
import lnyswz.jxc.bean.Xskp;
import lnyswz.jxc.model.TCatalog;
import lnyswz.jxc.model.TXskp;
import lnyswz.jxc.service.CatalogServiceI;
import lnyswz.jxc.service.ChartServiceI;
import lnyswz.jxc.util.CatalogComparator;

/**
 * 图表实现类
 * @author 王文阳
 *
 */
@Service("chartService")
public class ChartServiceImpl implements ChartServiceI {
	private final static Logger logger = Logger.getLogger(ChartServiceImpl.class);
	private BaseDaoI<TXskp> xskpDao;
	
	@Override
	public Chart getXstj(Chart chart) {
		String sql = "";
		if(chart.getField().equals("xsje")){
			sql = "select jzsj, round(xsje / 10000, 2)";
		}else if(chart.getField().equals("xsml")){
			sql = "select jzsj, round((xsje - xscb) / 10000, 2)";
		}
		if(chart.getIncludeNb().equals("1")){
			sql += " from v_xstj";
		}else{
			sql += " from v_xstj_nonb";
		}
		sql += " where bmbh = ? and substring(jzsj, 1, 4) = ? order by jzsj";
		
		return getChartByMonth(chart, sql);
	}
	
	@Override
	public Chart getKctj(Chart chart) {
		String sql = "";
		if(chart.getField().equals("kcje")){
			sql = "select jzsj, round(kcje / 10000, 2)";
		}else if(chart.getField().equals("xscb")){
			sql = "select jzsj, round(xscb / 10000, 2)";
		}
		
		sql += " from v_kctj where bmbh = ? and substring(jzsj, 1, 4) = ? order by jzsj";
		
		return getChartByMonth(chart, sql);
	}
	
	@Override
	public Chart getXsjgfx(Chart chart) {
		String sql = "";
		if(chart.getField().equals("xsje")){
			sql = "select splbmc, round(xsje / 10000, 2)";
		}else if(chart.getField().equals("xsml")){
			sql = "select splbmc, round((xsje - xscb) / 10000, 2)";
		}
		if(chart.getIncludeNb().equals("1")){
			sql += " from v_xsjgfx";
		}else{
			sql += " from v_xsjgfx_nonb";
		}
		sql += " where bmbh = ? and jzsj = ? and splbmc is not null order by jzsj";
		
		return getPieByMonth(chart, sql);
	}
	
	@Override
	public Chart getXskhtj(Chart chart) {
		String sql = "";
		if(chart.getField().equals("xsje")){
			sql = "select jzsj, round(xsje / 10000, 2)";
		}else if(chart.getField().equals("xsml")){
			sql = "select jzsj, round((xsje - xscb) / 10000, 2)";
		}
		if(chart.getIncludeNb().equals("1")){
			sql += " from v_xstj";
		}else{
			sql += " from v_xstj_nonb";
		}
		sql += " where bmbh = ? and substring(jzsj, 1, 4) = ? order by jzsj";
		
		return getChartByMonth(chart, sql);
	}

	
	private Chart getPieByMonth(Chart chart, String sql) {
		Chart c = new Chart();
		//两年数据
		int year = 2;
		String[] years = new String[year];
		for(int y = 0; y < year; y++){
			years[y] = String.valueOf(DateUtil.getYear() - y);
		}
		
		List<Serie> series = new ArrayList<Serie>();
		for(String yy : years){
			Serie serie = new Serie();
			serie.setName(yy + "年");
			
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("0", chart.getBmbh());
			params.put("1", yy);
			List<Object[]> lists = xskpDao.findBySQL(sql, params);
			
			List<Object> cate = new ArrayList<Object>();
			List<Object> data = new ArrayList<Object>();
			
			if(lists != null && lists.size() > 0){
				for(Object[] o : lists){
					//data1.add(new BigDecimal(o[1].toString()));
					cate.add(o[0]);
					data.add(o[1]);
				}
				serie.setCate(cate);
				serie.setData(data);
				
				series.add(serie);
			}
			
		}
		c.setSeries(series);
		
		return c;
	}

	

	private Chart getChartByMonth(Chart chart, String sql) {
		Chart c = new Chart();
		//三年数据
		int year = 3;
		String[] years = new String[year];
		for(int y = 0; y < year; y++){
			years[y] = String.valueOf(DateUtil.getYear() - y);
		}
		
		List<Serie> series = new ArrayList<Serie>();
		for(String yy : years){
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("0", chart.getBmbh());
			params.put("1", yy);
			List<Object[]> lists = xskpDao.findBySQL(sql, params);
			
			Serie serie = new Serie();
			serie.setName(yy + "年");
			
			List<Object> data1 = new ArrayList<Object>();
			
			if(lists != null && lists.size() > 0){
				for(Object[] o : lists){
					//data1.add(new BigDecimal(o[1].toString()));
					data1.add(o[1]);
				}
				serie.setData(data1);
				series.add(serie);
			}
			
		}
		c.setSeries(series);
		
		List<String> categories = new ArrayList<String>();
		categories.add("一月");
		categories.add("二月");
		categories.add("三月");
		categories.add("四月");
		categories.add("五月");
		categories.add("六月");
		categories.add("七月");
		categories.add("八月");
		categories.add("九月");
		categories.add("十月");
		categories.add("十一月");
		categories.add("十二月");
		c.setCategories(categories);
		
		return c;
	}


	@Autowired
	public void setXskpDao(BaseDaoI<TXskp> xskpDao) {
		this.xskpDao = xskpDao;
	}
}
