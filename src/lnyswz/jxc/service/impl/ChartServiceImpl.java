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
		Chart c = new Chart();
		
		String sql = "";
		if(chart.getField().equals("xsje")){
			sql = "select jzsj, round(xsje / 10000, 2) from v_xstj where bmbh = ? and substring(jzsj, 1, 4) = ? order by jzsj";
		}else if(chart.getField().equals("xsml")){
			sql = "select jzsj, round((xsje - xscb) / 10000, 2) from v_xstj where bmbh = ? and substring(jzsj, 1, 4) = ? order by jzsj";
		}
		
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
			
			List<BigDecimal> data1 = new ArrayList<BigDecimal>();
			
			if(lists != null && lists.size() > 0){
				for(Object[] o : lists){
					data1.add(new BigDecimal(o[1].toString()));
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