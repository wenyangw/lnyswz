package lnyswz.jxc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Chart;
import lnyswz.jxc.bean.Kh;
import lnyswz.jxc.bean.Serie;
import lnyswz.jxc.model.TXskp;
import lnyswz.jxc.service.ChartServiceI;

/**
 * 图表实现类
 * @author 王文阳
 *
 */
@Service("chartService")
public class ChartServiceImpl implements ChartServiceI {
	private BaseDaoI<TXskp> xskpDao;
	
	@Override
	public Chart getXstj(Chart chart) {
		String sql = "";
		if(chart.getField().equals("xsje")){
			sql = "select jzsj, round(xsje / 10000, 2)";
		}else if(chart.getField().equals("xsml")){
			sql = "select jzsj, round((xsje - xscb) / 10000, 2)";
		}else if(chart.getField().equals("xssl")){
			sql = "select jzsj, xssl";
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
		}else if(chart.getField().equals("kcsl")){
			sql = "select jzsj, kcsl";
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
		String sql = null;
		if(chart.getField().equals("xsje")){
			sql = "exec p_tj_khxs ?, ?";
		}else if(chart.getField().equals("xsml")){
			sql = "";
		}
		
		return getChartByColumn(chart, sql);
	}
	
	@Override
	public Chart getXskhfltj(Chart chart) {
		String sql = null;
		if(chart.getField().equals("xsje")){
			sql = "exec p_tj_khflxs ?, ?, ?";
			//sql = "select top 20 ywymc, khmc, round(xsje / 10000, 2) xsje, round(bxsje / 10000, 2) bxsje";
		}else if(chart.getField().equals("xsml")){
			sql = "";
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", chart.getBmbh());
		params.put("1", chart.getYear());
		params.put("2", chart.getKhbh());
		List<Object[]> lists = xskpDao.findBySQL(sql, params);
		
		return getChart(chart, lists, 2, 1);
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

	private Chart getChartByColumn(Chart chart, String sql) {
		Chart c = new Chart();
		
		List<Serie> series = new ArrayList<Serie>();
					
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", chart.getBmbh());
		params.put("1", chart.getYear());
		List<Object[]> lists = xskpDao.findBySQL(sql, params);
		
		
		
		List<String> categories = new ArrayList<String>();
		List<Object> data1 = new ArrayList<Object>();
		List<Object> data2 = new ArrayList<Object>();
		
		if(lists != null && lists.size() > 0){
			for(Object[] o : lists){
				categories.add(o[1].toString() + "<br>" + o[0].toString());
				data1.add(o[2]);
				data2.add(o[3]);
			}
		}
		
		
		Serie serie = new Serie();
		serie.setName(chart.getYear() + "年");
		serie.setData(data1);
		series.add(serie);
		
		serie = new Serie();
		serie.setName(chart.getYear() - 1 + "年");
		serie.setData(data2);
		series.add(serie);
		
		c.setSeries(series);
		c.setCategories(categories);
		
		return c;
	}
	
	private Chart getXskhfltjChart(Chart chart, String sql) {
		Chart c = new Chart();
		
		List<Serie> series = new ArrayList<Serie>();
					
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", chart.getBmbh());
		params.put("1", chart.getYear());
		params.put("2", chart.getKhbh());
		List<Object[]> lists = xskpDao.findBySQL(sql, params);
		
		
		
		List<String> categories = new ArrayList<String>();
		List<Object> data1 = new ArrayList<Object>();
		List<Object> data2 = new ArrayList<Object>();
		
		if(lists != null && lists.size() > 0){
			for(Object[] o : lists){
				categories.add(o[0].toString());
				data1.add(o[1]);
				data2.add(o[2]);
			}
			
		}
		
		
		Serie serie = new Serie();
		serie.setName(chart.getYear() + "年");
		serie.setData(data1);
		series.add(serie);
		
		serie = new Serie();
		serie.setName(chart.getYear() - 1 + "年");
		serie.setData(data2);
		series.add(serie);
		
		c.setSeries(series);
		c.setCategories(categories);
		
		return c;
	}
	
	/*
	 * select mc, je1, je2 from ……
	 * cols:数据列数 2 -je1, je2
	 * from:取得数据的开始数 1 - 0, 1, 2
	 */
	private Chart getChart(Chart chart, List<Object[]> lists, int cols, int from) {
		Chart c = new Chart();
				
		List<Serie> series = new ArrayList<Serie>();
		
		for(int i = 0; i < cols; i++){
			series.add(new Serie());
			series.get(i).setName(chart.getYear() - i + "年");
			series.get(i).setData(new ArrayList<Object>());
		}
		
		List<String> categories = new ArrayList<String>();
		
		if(lists != null && lists.size() > 0){
			for(Object[] o : lists){
				categories.add(o[0].toString());
				for(int i = 0; i < cols; i++){
					series.get(i).getData().add(o[from + i]);
				}
			}
		}
		
		c.setSeries(series);
		c.setCategories(categories);
		
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
	
	@Override
	public DataGrid listKh(Chart chart) {
		String searchSql = "select khbh, khmc";
		String countSql = "select count(*)";
		String fromWhere = " from t_xskp where bmbh = ? and YEAR(createTime) = ?";
		String groupSql = " group by khbh, khmc";
		String orderSql = " order by sum(hjje + hjse) desc";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", chart.getBmbh());
		params.put("1", chart.getYear());
		if(chart.getSearch() != null && chart.getSearch().length() > 0){
			fromWhere += " and khmc like ?";
			params.put("2", "%" + chart.getSearch() + "%");	
		}
		
		List<Object[]> lists = xskpDao.findBySQL(searchSql + fromWhere + groupSql + orderSql, params, chart.getPage(), chart.getRows());
		List<Kh> ls = new ArrayList<Kh>();
		Kh kh = null;
		for(Object[] l : lists){
			kh = new Kh();
			kh.setKhbh(l[0].toString());
			kh.setKhmc(l[1].toString());
			ls.add(kh);
		}
		
		DataGrid dg = new DataGrid();
		dg.setRows(ls);
		dg.setTotal((long)xskpDao.findBySQL(countSql + fromWhere + groupSql, params).size());
		
		return dg;
	}


	@Autowired
	public void setXskpDao(BaseDaoI<TXskp> xskpDao) {
		this.xskpDao = xskpDao;
	}
}
