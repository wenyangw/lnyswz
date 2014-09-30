package lnyswz.jxc.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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
		c.setTitle("销售分析");
		
		String sql = "select jzsj, xsje from v_xstj where bmbh = ? and substring(jzsj, 1, 4) = ?";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", chart.getBmbh());
		params.put("1", "2014");
		List<Object[]> lists = xskpDao.findBySQL(sql, params);
		
		List<Serie> series = new ArrayList<Serie>();
		Serie serie1 = new Serie();
		serie1.setName("2014年");

		List<BigDecimal> data1 = new ArrayList<BigDecimal>();
		
		if(lists != null && lists.size() > 0){
			for(Object[] o : lists){
				data1.add(new BigDecimal(o[1].toString()));
			}
			serie1.setData(data1);
			series.add(serie1);
		}
		
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
		
		c.setSeries(series);
		
		return c;
	}

	@Autowired
	public void setXskpDao(BaseDaoI<TXskp> xskpDao) {
		this.xskpDao = xskpDao;
	}
}
