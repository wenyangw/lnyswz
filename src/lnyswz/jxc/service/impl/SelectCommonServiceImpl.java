package lnyswz.jxc.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.SelectCommon;
import lnyswz.jxc.model.TDict;
import lnyswz.jxc.service.SelectCommonServiceI;

@Service("selectCommonService")
public class SelectCommonServiceImpl implements SelectCommonServiceI {
	private BaseDaoI<Object> selectCommonDao;
	private BaseDaoI<TDict> dictDao;

	/**
	 * DataGrid 显示
	 */
	@Override
	public DataGrid selectCommonList(SelectCommon d) {
		DataGrid dg = new DataGrid();
		String condition = "";
		String dictSql = "from TDict t where t.genre = '03' and ename= '"
				+ d.getQuery() + "'";
		TDict dicts = dictDao.get(dictSql);

		String sql = "select " + d.getCon() + " from " + dicts.getTname() + "";
		condition += " where bmbh = '" + d.getDid() + "'";
		if (d.getHqls().trim().length() > 0) {
			condition += " " + d.getHqls();
		}
		sql += condition;

		String hql = "from TDict t where ename='" + d.getQuery() + "'";
		TDict dict = dictDao.get(hql);
//		String n = dict.getOrderBy();
		if (dict.getOrderBy().trim().length() > 0) {
			sql += " " + dicts.getOrderBy();
		}
		String totalHql = "select count(*) from " + dicts.getTname()
				+ condition;
		List<Object[]> list = selectCommonDao.findBySQL(sql, d.getPage(),
				d.getRows());
		if (dict.getSpecials().trim().length() > 0) {
			String hq = "select * from " + dict.getSpecials();
			String sq = "('" + d.getDid() + "',";
			if (d.getSpbh() == null) {
				sq += d.getSpbh() + ",";
			} else {
				sq += "'" + d.getSpbh() + "',";
			}

			if (d.getSpmc() == null) {
				sq += d.getSpmc() + ",";
			} else {
				sq += "'" + d.getSpmc() + "',";
			}
			if (d.getA_time() == null) {
				sq += d.getA_time() + ",";
			} else {
				sq += "'" + d.getA_time() + "',";
			}
			if (d.getB_time() == null) {
				sq += d.getB_time() + ")";
			} else {
				sq += "'" + d.getB_time() + "')";
			}
			hq += sq;
			List<Object[]> l = selectCommonDao.findBySQL(hq, d.getPage(),
					d.getRows());
			String dhq = "select * from kfmx" + sq;
			
			List<Object[]> h = selectCommonDao.findBySQL(dhq, d.getPage(),
					d.getRows());
			list.addAll(h);
			list.addAll(l);
		}
		dg.setRows(list);
		dg.setTotal(selectCommonDao.countSQL(totalHql));
		dg.setObj(d.getHqls());
		return dg;
	}

	@Override
	public DataGrid selectCommonTree(SelectCommon d) {
		DataGrid dg = new DataGrid();
		String condition = "";
		String dictSql = "from TDict t where t.genre = '03' and ename= '"
				+ d.getQuery() + "'";
		TDict dicts = dictDao.get(dictSql);
		String sql = "select distinct  " + d.getCon() + " from "
				+ dicts.getTname() + "";
		condition += " where bmbh = '" + d.getDid() + "'";
		if (d.getHqls().trim().length() > 0) {
			condition += " " + d.getHqls();
		}
		sql += condition;
		List<Object[]> list = selectCommonDao.findBySQL(sql);
		dg.setRows(list);
		dg.setObj(sql);
		return dg;
	}

	@Override
	public List<Object[]> Exprot(SelectCommon d) {
		String condition = "";
		String dictSql = "from TDict t where t.genre = '03' and ename= '"
				+ d.getQuery() + "'";
		TDict dicts = dictDao.get(dictSql);
		String sql = "select  " + d.getCon() + " from "
				+ dicts.getTname() + "";
		condition += " where bmbh = '" + d.getDid() + "'";

			
		if (d.getSqls().trim().length() > 0) {
			condition += " " + d.getSqls();
		}
		sql += condition;
		if (dicts.getOrderBy().trim().length() > 0) {
			sql += " " + dicts.getOrderBy();
		}
			String sq=sql.replace("abc","(").replace("xyz",")");
			List<Object[]> list = selectCommonDao.findBySQL(sq);
		return list;
	}

	@Autowired
	public void setSelectCommonDao(BaseDaoI<Object> selectCommonDao) {
		this.selectCommonDao = selectCommonDao;
	}

	@Autowired
	public void setDictDao(BaseDaoI<TDict> dictDao) {
		this.dictDao = dictDao;
	}

}
