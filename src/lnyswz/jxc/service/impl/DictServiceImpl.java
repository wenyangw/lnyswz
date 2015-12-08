package lnyswz.jxc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Dict;
import lnyswz.jxc.model.TDict;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.service.DictServiceI;

/**
 * 字典实现类
 * 
 * @author 王文阳
 * 
 */
@Service("dictService")
public class DictServiceImpl implements DictServiceI {
	private BaseDaoI<TDict> dictDao;
	private BaseDaoI<TOperalog> opeDao;

	/**
	 * 增加字典
	 */
	@Override
	public Dict add(Dict d) {
		TDict t = new TDict();
		BeanUtils.copyProperties(d, t);
		// 设置id
		t.setId(UUID.randomUUID().toString());
		if (d.getDisplay() == null) {
			t.setDisplay("0");
		}
		// 保存
		dictDao.save(t);
		Dict dict = new Dict();
		BeanUtils.copyProperties(t, dict);
		OperalogServiceImpl.addOperalog(d.getUserId(), d.getDepId(), d
				.getMenuId(), dict.getId(), "添加字典信息", opeDao);
		return dict;
	}

	/**
	 * 修改字典
	 */
	@Override
	public void edit(Dict d) {
		// 通过传入的id获取字典
		TDict t = dictDao.load(TDict.class, d.getId());
		// 设置字典属性
		BeanUtils.copyProperties(d, t);
		OperalogServiceImpl.addOperalog(d.getUserId(), d.getDepId(), d
				.getMenuId(), d.getId(), "编辑字典信息", opeDao);
	}

	/**
	 * 删除字典
	 */
	@Override
	public void delete(Dict dict) {
		if (dict.getIds() != null) {
			// 拆分id
			for (String id : dict.getIds().split(",")) {
				if (!id.trim().equals("0")) {
					TDict t = dictDao.load(TDict.class, id);
					dictDao.delete(t);
//					OperalogServiceImpl
//							.addOperalog(dict.getUserId(), dict.getDepId(),
//									dict.getMenuId(), id, "删除字典信息", opeDao);
				}

			}
		}

	}

	/**
	 * 获得字典信息，供管理用，有分页
	 */
	@Override
	public DataGrid datagrid(Dict d) {
		String hql = "from TDict t ";
		Map<String, Object> params = new HashMap<String, Object>();
		if (d.getTjsx() == null) {
			if (d.getEname() != null && d.getEname().length() > 0) {
				hql += " where t.tname = :ename and t.genre = '01' ";
				params.put("ename", d.getEname());
			}
		} else {
			hql += " where t.ename like :ename";
			if (d.getTjsx().equals("_")) {
				d.setTjsx("[_]");
			}
			params.put("ename", "%" + d.getTjsx() + "%");
		}
		String where = " order by orderNum"; 
		String countHql = "select count(id) " + hql;
		hql = hql + where;
		DataGrid datagrid = new DataGrid();
		List<TDict> l = dictDao.find(hql, params, d.getPage(), d.getRows());
		datagrid.setTotal(dictDao.count(countHql, params));
		datagrid.setRows(changeDict(l));
		return datagrid;
	}

	/**
	 * 获得字典信息，供查询用，无分页
	 */
	@Override
	public List<Dict> listDict(Dict dict) {
		String hql = "from TDict t where genre >= '03' order by cname ";
		List<TDict> list = dictDao.find(hql);
		return changeDict(list);
	}
	/**
	 * 获取查询tree
	 */
	@Override
	public List<Dict> selectTree(Dict dict) {
		String dictSql = "from TDict t where t.genre >= '03' and ename= '"
				+ dict.getSelectType() + "'";
		TDict dicts = dictDao.get(dictSql);
		String hql = "from TDict t where genre = '01' and t.tname ='"+dicts.getEname()+"' order by orderNum";
		List<TDict> list = dictDao.find(hql);
		return changeDict(list);
	}
	
	@Override
	public Dict isSelectType(Dict dict) {
		String hql = "from TDict t where genre >= '03' and ename= '"+ dict.getSelectType() + "'";
		TDict td = dictDao.get(hql);
		Dict d = new Dict();
		BeanUtils.copyProperties(td, d);
		return d;

	}
	
	@Override
	public boolean isNeedDep(Dict dict) {
		String hql = "from TDict t where genre >= '03' and ename= '"+ dict.getSelectType() + "'";
		TDict d = dictDao.get(hql);
		if(d.getIsDepName().equals("1")){
			return true;
		}else{
			return false;
		}

	}

	/*
	 * 查询(non-Javadoc)
	 * 
	 * @see lnyswz.jxc.service.DictServiceI#listDicts(lnyswz.jxc.bean.Dict)
	 */
	public List<Dict> listFields(Dict dict) {
		String hql = "from TDict t";
		Map<String, Object> params = new HashMap<String, Object>();
//		String sql="from TDict t where t.genre = '03' and tname= '"+dict.getSelectType()+"'";
//		System.out.println("_____________________________________________"+sql);
//		TDict d=dictDao.get(sql);
//		System.out.println("_________________________________________"+d.getEname());
		if (dict.getSelectType() != null && dict.getSelectType().length() > 0) {
			// SqlSelected 用来判断sql语句选择 用于区别查询条件字段 和显示字段区别
			// 当SqlSelected 有值时为查询条件字段
			String where = " where t.genre = '01'";
			if (dict.getSqlSelected() != null) {
				where += " and t.display = '1'";
			}
			if (dict.getIsShow() != null) {
				where += " and isShow = '1' ";
			}

			where += " and t.tname = :tname   order by orderNum";
			hql += where;
			String dictSql = "from TDict t where t.genre >= '03' and ename= '"
					+ dict.getSelectType() + "'";
			TDict dicts = dictDao.get(dictSql);
			params.put("tname", dicts.getEname());
		}
		List<TDict> list = dictDao.find(hql, params);
		return changeDict(list);
	}

	/**
	 * 数据转换
	 * 
	 * @param l
	 * @return
	 */
	private List<Dict> changeDict(List<TDict> l) {
		List<Dict> nl = new ArrayList<Dict>();
		for (TDict td : l) {
			Dict d = new Dict();
			BeanUtils.copyProperties(td, d);
			d.setText(td.getCname());
			Map<String, Object> attributes = new HashMap<String, Object>();
			attributes.put("ename", td.getEname());

			d.setAttributes(attributes);
			nl.add(d);
		}
		return nl;
	}

	@Autowired
	public void setDictDao(BaseDaoI<TDict> dictDao) {
		this.dictDao = dictDao;
	}

	@Autowired
	public void setOpeDao(BaseDaoI<TOperalog> opeDao) {
		this.opeDao = opeDao;
	}

}
