package lnyswz.jxc.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.KhCx;
import lnyswz.jxc.bean.SelectCommon;
import lnyswz.jxc.bean.Xsth;
import lnyswz.jxc.bean.XsthDet;
import lnyswz.jxc.model.TDict;
import lnyswz.jxc.model.TXsth;
import lnyswz.jxc.model.TXsthDet;
import lnyswz.jxc.service.SelectCommonServiceI;

@Service("selectCommonService")
public class SelectCommonServiceImpl implements SelectCommonServiceI {
	private BaseDaoI<Object> selectCommonDao;
	private BaseDaoI<TDict> dictDao;
	private BaseDaoI<TXsth> xsthDao;
	private BaseDaoI<TXsthDet> xsthDetDao;

	/**
	 * DataGrid 显示
	 */
	@Override
	public DataGrid selectCommonList(SelectCommon d) {
		DataGrid dg = new DataGrid();
		String condition = "";
		String conditionCount = "";
		
		// '00' 变量 , '01' 字段，'02' 表, '03' 视图‘04’存储过程
		String dictSql = "from TDict t where t.genre >= '03' and ename= '"
				+ d.getQuery() + "'";
		TDict dicts = dictDao.get(dictSql);
		if (dicts.getGenre().equals("04")) {
			String execHql = "execute " + dicts.getTname() + "'" + d.getDid()
					+ "','" + d.getUserId() + "'";
			execHql = spellFor(d.getExec(), execHql);
			String treeExec = "";
			treeExec = spellFor(d.getTreeExec(), treeExec);
			List<Object[]> list = selectCommonDao.findBySQL(execHql + ","
					+ d.getPage() + "," + d.getRows() + ",0" + treeExec);
			if (d.getPage() == 1) {
				dg.setTotal(selectCommonDao.countBySQL(execHql + ","
						+ d.getPage() + "," + d.getRows() + ",1" + treeExec));
			} else {
				dg.setTotal((long) d.getTotal());
			}

			dg.setRows(list);
			String exec = execHql + "," + 1 + "," + dg.getTotal() + ",0"
					+ treeExec;
			dg.setObj(exec);
		} else {
			String sql = "select " + d.getCon() + " from " + dicts.getTname()
					+ "";
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
			if (dicts.getInGroupBy() != null) {
				sql += " " + dicts.getInGroupBy();
			}
			if (dicts.getIsHj() != null ) {
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

			if (d.getIsFilter().equals("01")) {
				sql = "select * from ( " + sql + ")as fil "
						+ dicts.getFilterWhere();
			}
			conditionCount += sql;

			if (dicts.getOrderBy().trim().length() > 0) {
				sql += " " + dicts.getOrderBy();
			}
			String totalHql = "select count(*) from (" + conditionCount
					+ ") as count";
			List<Object[]> list = selectCommonDao.findBySQL(sql, d.getPage(),
					d.getRows());
			dg.setRows(list);
			dg.setTotal(selectCommonDao.countSQL(totalHql));
			dg.setObj(d.getHqls());

		}

		return dg;
	}

	@Override
	public DataGrid selectCommonTree(SelectCommon d) {
		DataGrid dg = new DataGrid();
		String condition = "";
		String dictSql = "from TDict t where t.genre >= '03' and ename= '"
				+ d.getQuery() + "'";
		TDict dicts = dictDao.get(dictSql);
		if (dicts.getGenre().equals("04")) {
			String execHql = "execute " + dicts.getTname() + "'" + d.getDid()
					+ "','" + d.getUserId() + "'";
			execHql = spellFor(d.getExec(), execHql);
			execHql += "," + d.getPage() + "," + d.getRows() + ",5";
			execHql = spellFor(d.getTreeExec(), execHql);
			
			List<Object[]> list = selectCommonDao.findBySQL(execHql);
			dg.setRows(list);
			
			String exec = execHql ;
			
			dg.setObj(exec);
		} else {
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
			if (dicts.getOrderByTree() != null) {
				if (dicts.getOrderByTree().trim().length() > 0) {
					sql += " " + dicts.getOrderByTree();
				}
			}		
			List<Object[]> list = selectCommonDao.findBySQL(sql);
			
			dg.setRows(list);
			dg.setObj(sql);
		}
		return dg;
	}

	@Override
	public List<Object[]> Exprot(SelectCommon d) {
		String condition = "";
		String dictSql = "from TDict t where t.genre >= '03' and ename= '"
				+ d.getQuery() + "'";
		TDict dicts = dictDao.get(dictSql);
		
		// 判断是执行sql语句还是存储过程
		if (dicts.getGenre().equals("04")) {
			
			List<Object[]> list = selectCommonDao.findBySQL(d.getSqls().trim());
			return list;
		}
		// 执行sql语句
		else {
			String sql = "select  " + d.getCon() + " from " + dicts.getTname()
					+ "";
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

			if (d.getIsFilter().equals("01")) {
				sql = "select * from ( " + sql + ")as fil "
						+ dict.getFilterWhere();
			}
			if (dict.getOrderBy().trim().length() > 0) {
				sql += " " + dicts.getOrderBy();
			}
			String sq = sql.replace("abc", "(").replace("xyz", ")")
					.replace("fgh", ",").replace("mno", "'")
					.replace("mno", "'");
			List<Object[]> list = selectCommonDao.findBySQL(sq);
			return list;
		}
	}

	public String spellFor(String exec, String sql) {
		if (exec != null && exec.length() > 0) {
			for (String path : exec.split(",")) {
				sql += ",'" + path.trim() + "'";
			}
		}
		return sql;

	}

	@Override
	public DataGrid selectCommonByFreeSpell(SelectCommon sel) {
		DataGrid dg = new DataGrid();
		StringBuffer strBuffer = new StringBuffer("select ");
		strBuffer.append(sel.getField());
		strBuffer.append(" from ");
		strBuffer.append(sel.getTable());
		strBuffer.append(" " + sel.getWhere());

		// String sql = "select " + sel.getField() + " from " + sel.getTable() +
		// " " + sel.getWhere();
		// String totalHql =
		// "select count(*) from ("+conditionCount+") as count";
		List<Object[]> list = selectCommonDao.findBySQL(strBuffer.toString(),
				sel.getPage(), sel.getRows());

		dg.setRows(list);
		dg.setTotal((long) list.size());

		return dg;
	}
	
	
	@Override
	public DataGrid listKhByYwy(SelectCommon d) {
		DataGrid dg = new DataGrid();
		//execute m_kh '部门ide','ywyId'
		String execHql = "execute m_ywy_kh '" + d.getDid() + "','" + d.getUserId() + "','" + d.getSearch().trim() + "','" + d.getPage() + "','" + d.getRows() + "'";
		List<KhCx> l = new ArrayList<KhCx>();
		List<Object[]> list = selectCommonDao.findBySQL(execHql);
		for(Object[] o : list){
			KhCx k = getKhByYwy(o);
			l.add(k);
		}
		String countexecHql = "execute m_ywy_kh '" + d.getDid() + "','" + d.getUserId() + "','" + d.getSearch().trim() + "','0','0'";
		dg.setTotal(selectCommonDao.countSQL(countexecHql));
		
		dg.setRows(l);
		return dg;
	}
	
	@Override
	public DataGrid listKhByYwyXsth(SelectCommon d) {
		// TODO Auto-generated method stub
		DataGrid dg = new DataGrid();
		
		String sql = "select distinct xsthlsh, hjje, hjsl, ckmc, bz from v_xsth_det  where bmbh = '" + d.getDid() + "' and ywyId = '" + d.getUserId() + "' and khbh = '" + d.getKhbh() + "' and zdwsl = cksl and isCancel = '0'  and SUBSTRING(xsthlsh,1,2) > 16  "; 
		String sqlOrder = " order by xsthlsh desc";
		List<Xsth> l = new ArrayList<Xsth>();
		List<Object[]> list = xsthDao.findBySQL(sql+sqlOrder, d.getPage(), d.getRows());
		for(Object[] o : list){
			Xsth x = getKhByYwyXsth(o);

			l.add(x);
		}
		String totalHql = "select count(*)  from (" + sql + ")as count";
		dg.setTotal(selectCommonDao.countSQL(totalHql));
		dg.setRows(l);
		return dg;
	}
	
	
	
	private Xsth getKhByYwyXsth(Object[] o) {
		Xsth x = new Xsth();
		String xsthlsh = (String)o[0];
		BigDecimal hjje = new BigDecimal(o[1].toString());
		BigDecimal hjsl = new BigDecimal(o[2].toString());
		String ckmc = (String)o[3];
		String bz = (String)o[4];
		
		x.setXsthlsh(xsthlsh);
		x.setHjje(hjje);
		x.setHjsl(hjsl);
		x.setCkmc(ckmc);
		x.setBz(bz);
		return x;
	}
	
	private KhCx getKhByYwy(Object[] o) {
		KhCx k = new KhCx();
		String khbh = (String)o[0];
		String khmc = (String)o[1];
		BigDecimal sxje = new BigDecimal(o[2].toString());
		int sxzq = (Integer)o[3];
		BigDecimal ysje = new BigDecimal(o[4].toString());
		BigDecimal thje = new BigDecimal(o[5].toString());
		
		k.setKhbh(khbh);
		k.setKhmc(khmc);
		k.setSxje(sxje);
		k.setSxzq(sxzq);
		k.setThje(thje);
		k.setYsje(ysje);
		return k;
	}

	@Autowired
	public void setSelectCommonDao(BaseDaoI<Object> selectCommonDao) {
		this.selectCommonDao = selectCommonDao;
	}

	@Autowired
	public void setDictDao(BaseDaoI<TDict> dictDao) {
		this.dictDao = dictDao;
	}
	@Autowired
	public void setXsthDao(BaseDaoI<TXsth> xsthDao) {
		this.xsthDao = xsthDao;
	}
	@Autowired
	public void setXsthDetDao(BaseDaoI<TXsthDet> xsthDetDao) {
		this.xsthDetDao = xsthDetDao;
	}

}
