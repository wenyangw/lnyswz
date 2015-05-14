package lnyswz.jxc.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.TreeNode;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Spdw;
import lnyswz.jxc.model.TSpdl;
import lnyswz.jxc.model.TSpdw;
import lnyswz.jxc.model.TSplb;
import lnyswz.jxc.service.SpdwServiceI;
import lnyswz.jxc.util.CatalogComparator;
import lnyswz.jxc.util.SplbComparator;

/**
 * 商品段位实现类
 * @author 王文阳
 *
 */
@Service("spdwService")
public class SpdwServiceImpl implements SpdwServiceI {
	private BaseDaoI<TSpdw> spdwDao;
	private BaseDaoI<TSpdl> spdlDao;
	private BaseDaoI<TSplb> splbDao;
	
	/**
	 * 增加商品段位
	 */
	@Override
	public Spdw add(Spdw spdw) {
		TSpdw t = new TSpdw();
		BeanUtils.copyProperties(spdw, t);
		//设置商品段位所属商品类别
		TSplb splb = splbDao.get(TSplb.class, spdw.getSplbId());
		t.setTSplb(splb);
		spdwDao.save(t);
		BeanUtils.copyProperties(t, spdw);
		spdw.setSplbmc(t.getTSplb().getSplbmc());
		return spdw;
	}

	/**
	 * 修改商品段位
	 */
	@Override
	public void edit(Spdw spdw) {
		TSpdw t = spdwDao.get(TSpdw.class, spdw.getId());
		BeanUtils.copyProperties(spdw, t);
		//设置商品段位所属商品类别
		TSplb splb = splbDao.get(TSplb.class, spdw.getSplbId());
		t.setTSplb(splb);
	}

	/**
	 * 删除商品段位
	 */
	@Override
	public void delete(String id) {
		TSpdw t = spdwDao.get(TSpdw.class, id);
		spdwDao.delete(t);
	}

	/**
	 * 获得商品段位列表，供商品管理时选择
	 */
	@Override
	public List<Spdw> getSpdws() {
		String hql = "from TSpdw t";
		List<TSpdw> l = spdwDao.find(hql);
		if(l != null && l.size() > 0){
			return changeSpdw(l);
		}
		return null;
	}
	
	/**
	 * 获得商品大类、商品类别树，供商品段位、商品管理时选择
	 */
	@Override
	public List<TreeNode> listSpfl(Spdw spdw) {
		List<TreeNode> tree = new ArrayList<TreeNode>();
		String hql = "select t from TSpdl t join t.TDepartments dep where dep.id = :depId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", spdw.getDepId());
		//获得商品大类
		List<TSpdl> spdls = spdlDao.find(hql, params);
		if(spdls != null && spdls.size() > 0){
			for(TSpdl spdl : spdls){
				TreeNode node = new TreeNode();
				node.setId(spdl.getId());
				node.setText(spdl.getSpdlmc());
				//获得商品类别
				List<TSplb> splbs = new ArrayList<TSplb>(spdl.getTSplbs());
//				Set<TSplb> splbs = new TreeSet<TSplb>();
//				Set<TSplb> splbs = spdl.getTSplbs();
				if(splbs != null && splbs.size() > 0){
					Collections.sort(splbs, new SplbComparator());
					List<TreeNode> children = new ArrayList<TreeNode>();
					for(TSplb splb : splbs){
						TreeNode n = new TreeNode();
						n.setId(String.valueOf(splb.getId()));
						n.setText(splb.getSplbmc());
						Map<String, Object> attributes = new HashMap<String, Object>();
						attributes.put("idFrom", spdl.getId() + splb.getIdFrom());
						attributes.put("idTo", spdl.getId() + splb.getIdTo());
						n.setAttributes(attributes);
						children.add(n);
					}
					node.setChildren(children);
					node.setState("close");
				}
				tree.add(node);
			}
		}
		return tree;
	}

	/**
	 * 获得商品段位，供管理用，有分页
	 */
	@Override
	public DataGrid datagrid(Spdw spdw) {
		String hql = "from TSpdw t join t.TSplb.TSpdl.TDepartments deps where deps.id = :depId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", spdw.getDepId());
		if(spdw.getSplbId() > 0){
			hql += " and t.TSplb.id = :splbId";
			params.put("splbId", spdw.getSplbId());
		}
		String countHql = "select count(*) " + hql;
		DataGrid dg = new DataGrid();
		dg.setTotal(spdwDao.count(countHql, params));
		dg.setRows(changeSpdw(spdwDao.find("select t " + hql + " order by t.id", params, spdw.getPage(), spdw.getRows())));
		return dg;
	}
	/**
	 * 数据转换
	 * @param l
	 * @return
	 */
	private List<Spdw> changeSpdw(List<TSpdw> l){
		List<Spdw> nl = new ArrayList<Spdw>();
		for(TSpdw t : l){
			Spdw s = new Spdw();
			BeanUtils.copyProperties(t, s);
			s.setSplbId(t.getTSplb().getId());
			s.setSplbmc(t.getTSplb().getSplbmc());
			nl.add(s);
		}
		return nl;
	}
	
	@Autowired
	public void setSpdwDao(BaseDaoI<TSpdw> spdwDao) {
		this.spdwDao = spdwDao;
	}
	@Autowired
	public void setSpdlDao(BaseDaoI<TSpdl> spdlDao) {
		this.spdlDao = spdlDao;
	}
	@Autowired
	public void setSplbDao(BaseDaoI<TSplb> splbDao) {
		this.splbDao = splbDao;
	}

}
