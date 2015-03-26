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
		// '00' 变量 , '01' 字段，'02' 表, '03' 视图
		String dictSql = "from TDict t where t.genre = '03' and ename= '"
				+ d.getQuery() + "'";

		TDict dicts = dictDao.get(dictSql);
		String sql = "select " + d.getCon() + " from " + dicts.getTname() + "";
		if (dicts.getIsDepName() != null) {
			if (dicts.getIsDepName().equals("1")) {
				condition += " where bmbh = '" + d.getDid() + "' ";
				if (d.getHqls().trim().length() > 0) {
					condition += " and " + d.getHqls();
				}
			} else {
				if (d.getHqls().trim().length() > 0) {
					condition += " where " + d.getHqls();
				}
			}
		}

		sql += condition;
		//
		// String hql = "from TDict t where ename='" + d.getQuery() + "'";
		// TDict dict = dictDao.get(hql);
		// String n = dict.getOrderBy();

		if (dicts.getInGroupBy() != null) {
			sql += " " + dicts.getInGroupBy();

		}

		if (dicts.getIsHj() != null) {
			if (dicts.getOutGroupBy() != null
					&& dicts.getOutGroupBy().trim().length() > 0) {
				sql = "select * from (" + sql;
				sql += " " + dicts.getOutGroupBy() + " )as ab";
				if (dicts.getSqlWhere() != null
						&& dicts.getSqlWhere().trim().length() > 0) {
					sql += " " + dicts.getSqlWhere();
				}

			}
		}
		if (dicts.getOrderBy().trim().length() > 0) {
			sql += " " + dicts.getOrderBy();
		}
		String totalHql = "select count(*) from " + dicts.getTname()
				+ condition;
		List<Object[]> list = selectCommonDao.findBySQL(sql, d.getPage(),
				d.getRows());

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
		if (dicts.getIsDepName() != null) {
			if (dicts.getIsDepName().equals("1")) {
				condition += " where bmbh = '" + d.getDid() + "' ";
				if (d.getHqls().trim().length() > 0) {
					condition += " and " + d.getHqls();
				}
			} else {
				if (d.getHqls().trim().length() > 0) {
					condition += " where " + d.getHqls();
				}
			}
		}
		sql += condition;
		if(dicts.getOrderByTree()!=null){
			if (dicts.getOrderByTree().trim().length() > 0) {
				sql += " " + dicts.getOrderByTree();
			}
		}
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
		String sql = "select  " + d.getCon() + " from " + dicts.getTname() + "";
		if (dicts.getIsDepName() != null) {
			if (dicts.getIsDepName().equals("1")) {
				condition += " where bmbh = '" + d.getDid() + "' ";
				if (d.getSqls().trim().length() > 0) {
					condition += " and " + d.getSqls();
				}
			} else {
				if (d.getSqls().trim().length() > 0) {
					condition += " where " + d.getSqls();
				}
			}
		}
		sql += condition;

		String hql = "from TDict t where ename='" + d.getQuery() + "'";
		TDict dict = dictDao.get(hql);
		// String n = dict.getOrderBy();
		if (dict.getOrderBy().trim().length() > 0) {
			sql += " " + dicts.getOrderBy();
		}
		if (dict.getInGroupBy() != null) {
			sql += " " + dict.getInGroupBy();

		}

		if (dict.getIsHj() != null) {
			if (dict.getOutGroupBy() != null
					&& dict.getOutGroupBy().trim().length() > 0) {
				sql = "select * from (" + sql;
				sql += " " + dict.getOutGroupBy() + " )as ab";
				if (dict.getSqlWhere() != null
						&& dict.getSqlWhere().trim().length() > 0) {
					sql += " " + dict.getSqlWhere();
				}

			}
		}
		String sq = sql.replace("abc", "(").replace("xyz", ")")
				.replace("fgh", ",").replace("mno", "'").replace("mno", "'");
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
