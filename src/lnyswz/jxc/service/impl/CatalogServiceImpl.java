package lnyswz.jxc.service.impl;

import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Catalog;
import lnyswz.jxc.model.TCatalog;
import lnyswz.jxc.service.CatalogServiceI;
import lnyswz.jxc.util.CatalogComparator;

/**
 * 模块实现类
 * @author 王文阳
 *
 */
@Service("catalogService")
public class CatalogServiceImpl implements CatalogServiceI {
	private final static Logger logger = Logger.getLogger(CatalogServiceImpl.class);
	private BaseDaoI<TCatalog> catalogDao;
	
	/**
	 * 增加模块
	 */
	@Override
	public Catalog add(Catalog c) {
		TCatalog t = new TCatalog();
		BeanUtils.copyProperties(c, t);
		//设置id
		t.setId(UUID.randomUUID().toString());
		//保存
		catalogDao.save(t);
		BeanUtils.copyProperties(t, c);
		return c;
	}
	
	/**
	 * 修改模块
	 */
	@Override
	public void edit(Catalog c) {
		//TODO 修改时提示名称重复
		//通过传入的id获取模块
		TCatalog t = catalogDao.get(TCatalog.class, c.getId());
		//设置模块属性
		BeanUtils.copyProperties(c, t);
	}
	
	/**
	 * 删除模块
	 */
	@Override
	public void delete(String ids) {
		if (ids != null) {
			//拆分id
			for (String id : ids.split(",")) {
				if (!id.trim().equals("0")) {
					TCatalog t = catalogDao.get(TCatalog.class, id.trim());
					if (t != null) {
						catalogDao.delete(t);
					}
				}
			}
		}
	}
	
	
	/**
	 * 获得所有模块，供选择用，无分页
	 */
	@Override
	public List<Catalog> listCatas(String type) {
		String hql = "from TCatalog t";
		Map<String, Object> params = new HashMap<String, Object>();
		if(!type.equals("")) {
			hql += " where t.type = :type";
			params.put("type", type);
		}
		List<TCatalog> l = catalogDao.find(hql, params);
		return changeCata(l);
	}

	/**
	 * 获得所有模块，有分页
	 */
	@Override
	public DataGrid datagrid(Catalog c) {
		DataGrid dg = new DataGrid();
		String hql = "from TCatalog t";
		List<TCatalog> l = catalogDao.find(hql);
		//获得总条数
		String totalHql = "select count(id) " + hql;
		//传入页码、每页条数
		l = catalogDao.find(hql, c.getPage(), c.getRows());
		dg.setTotal(catalogDao.count(totalHql));
		dg.setRows(changeCata(l));
		return dg;
	}
	
	/**
	 * 数据转换
	 * @param l
	 * @return
	 */
	private List<Catalog> changeCata(List<TCatalog> l) {
		//排序
		Collections.sort(l, new CatalogComparator());
		List<Catalog> nl = new ArrayList<Catalog>();
		//处理返回页面信息
		for(TCatalog t : l){
			Catalog c = new Catalog();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		return nl;
	}
	
	@Autowired
	public void setCatalogDao(BaseDaoI<TCatalog> catalogDao) {
		this.catalogDao = catalogDao;
	}

}
