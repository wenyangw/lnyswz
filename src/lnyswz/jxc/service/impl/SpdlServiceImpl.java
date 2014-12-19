package lnyswz.jxc.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.mapping.Array;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Spdl;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TRole;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.model.TSpdl;
import lnyswz.jxc.service.SpdlServiceI;

/**
 * 商品大类实现类
 * @author 王文阳
 *
 */
@Service("spdlService")
public class SpdlServiceImpl implements SpdlServiceI {
	private Logger logger = Logger.getLogger(SpdlServiceImpl.class);
	private BaseDaoI<TSpdl> spdlDao;
	private BaseDaoI<TDepartment> depDao;
	
	/**
	 * 增加商品大类
	 */
	@Override
	public Spdl add(Spdl spdl) {
		TSpdl t = new TSpdl();
		BeanUtils.copyProperties(spdl, t);
		spdlDao.save(t);
		BeanUtils.copyProperties(t, spdl);
		return spdl;
	}

	/**
	 * 修改商品大类
	 */
	@Override
	public void edit(Spdl spdl) {
		TSpdl t = spdlDao.get(TSpdl.class, spdl.getId());
		BeanUtils.copyProperties(spdl, t);
		// 设置商品大类所属部门
		if (spdl.getDepIds() != null) {
			Set<TDepartment> deps = new HashSet<TDepartment>();
			for (String id : spdl.getDepIds().split(",")) {
				TDepartment dep = depDao.load(TDepartment.class, id.trim());
				deps.add(dep);
			}
			t.setTDepartments(deps);
		}
	}

	/**
	 * 删除商品大类
	 */
	@Override
	public void delete(String id) {
		TSpdl t = spdlDao.get(TSpdl.class, id);
		spdlDao.delete(t);
	}

	/**
	 * 获得商品大类列表，选择用，无分页
	 */
	@Override
	public List<Spdl> listSpdls() {
		String hql = "from TSpdl t";
		List<TSpdl> l = spdlDao.find(hql);
		if(l != null && l.size() > 0){
			return changeSpdl(l);
		}
		return null;
	}

	/**
	 * 获得商品大类信息，管理用，有分页
	 */
	@Override
	public DataGrid datagrid(Spdl spdl) {
		String hql = "from TSpdl t";
		String countHql = "select count(id) " + hql;
		DataGrid dg = new DataGrid();
		dg.setTotal(spdlDao.count(countHql));
		dg.setRows(changeSpdl(spdlDao.find(hql, spdl.getPage(), spdl.getRows())));
		return dg;
	}
	/**
	 * 数据转换
	 * @param l
	 * @return
	 */
	private List<Spdl> changeSpdl(List<TSpdl> l){
		List<Spdl> nl = new ArrayList<Spdl>();
		for(TSpdl t : l){
			Spdl s = new Spdl();
			BeanUtils.copyProperties(t, s);
			s.setText(t.getSpdlmc());
			//商品大类所属部门
			Set<TDepartment> deps = t.getTDepartments();
			if (deps != null && deps.size() > 0) {
				String depIds = "";
				String depNames = "";
				boolean b = false;
				for (TDepartment dep : deps) {
					if (dep != null) {
						if (b) {
							depIds += ",";
							depNames += ",";
						}
						depIds += dep.getId();
						depNames += dep.getDepName();
						b = true;
					}

				}
				s.setDepIds(depIds);
				s.setDepNames(depNames);
			}
			nl.add(s);
		}
		return nl;
	}
	
	@Autowired
	public void setSpdlDao(BaseDaoI<TSpdl> spdlDao) {
		this.spdlDao = spdlDao;
	}

	@Autowired
	public void setDepDao(BaseDaoI<TDepartment> depDao) {
		this.depDao = depDao;
	}

}
