package lnyswz.jxc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Splb;
import lnyswz.jxc.model.TSpdl;
import lnyswz.jxc.model.TSplb;
import lnyswz.jxc.service.SplbServiceI;

/**
 * 商品类别实现类
 * @author 王文阳
 *
 */
@Service("splbService")
public class SplbServiceImpl implements SplbServiceI {
	private Logger logger = Logger.getLogger(SplbServiceImpl.class);
	private BaseDaoI<TSplb> splbDao;
	private BaseDaoI<TSpdl> spdlDao;
	/**
	 * 增加商品类别
	 */
	@Override
	public Splb add(Splb splb) {
		if(checkSplb(splb)){
			TSplb t = new TSplb();
			BeanUtils.copyProperties(splb, t);
			//获得商品类别所属商品大类
			TSpdl spdl = spdlDao.get(TSpdl.class, splb.getSpdlId());
			t.setTSpdl(spdl);
			splbDao.save(t);
			BeanUtils.copyProperties(t, splb);
			splb.setSpdlId(t.getTSpdl().getId());
			splb.setSpdlmc(t.getTSpdl().getSpdlmc());
			return splb;
		}
		return null;
	}

	/**
	 * 修改商品类别
	 */
	@Override
	public boolean edit(Splb splb) {
		if(checkSplb(splb)){
			TSplb t = splbDao.get(TSplb.class, splb.getId());
			BeanUtils.copyProperties(splb, t);
			TSpdl spdl = spdlDao.get(TSpdl.class, splb.getSpdlId());
			t.setTSpdl(spdl);
			return true;
		}
		return false;
	}

	/**
	 * 删除商品类别
	 */
	@Override
	public void delete(int id) {
		TSplb t = splbDao.get(TSplb.class, id);
		splbDao.delete(t);
	}

	/**
	 * 获得商品类别列表，供选择用
	 */
	@Override
	public List<Splb> listSplbs(Splb splb) {
		String hql = "select t from TSplb t join t.TSpdl.TDepartments deps where deps.id = :depId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", splb.getDepId());
		List<TSplb> l = splbDao.find(hql, params);
		logger.info("splbs:" + l.size());
		if(l != null && l.size() > 0){
			return changeSplb(l);
		}
		return null;
	}

	/**
	 * 获得商品类别，供管理用，有分页
	 */
	@Override
	public DataGrid datagrid(Splb splb) {
		String hql = "from TSplb t";
		Map<String, Object> params = new HashMap<String, Object>();
		if(splb.getSpdlId() != null && splb.getSpdlId().trim().length() > 0){
			hql += " where t.TSpdl.id = :spdlId";
			params.put("spdlId", splb.getSpdlId());
		}
		String countHql = "select count(id) " + hql;
		hql += " order by t.idFrom";
		DataGrid dg = new DataGrid();
		dg.setTotal(splbDao.count(countHql, params));
		dg.setRows(changeSplb(splbDao.find(hql, params, splb.getPage(), splb.getRows())));
		return dg;
	}
	
	
	/**
	 * 校验商品类别编码范围
	 * @param splb
	 * @return
	 */
	private boolean checkSplb(Splb splb){
		//String hql = "from TSplb t where t.TSpdl.id = :spdlId and ((:idFrom between t.idFrom and t.idTo) or (:idTo between t.idFrom and t.idTo)) ";
		String hql = "from TSplb t where t.id = :splbId and ((:idFrom between t.idFrom and t.idTo) or (:idTo between t.idFrom and t.idTo)) ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("splbId", splb.getId());
		params.put("idFrom", splb.getIdFrom());
		params.put("idTo", splb.getIdTo());
		List<TSplb> l = splbDao.find(hql, params);
		if(l != null && l.size() > 0){
			return false;
		}
		return true;
	}
	/**
	 * 数据转换
	 * @param l
	 * @return
	 */
	private List<Splb> changeSplb(List<TSplb> l){
		List<Splb> nl = new ArrayList<Splb>();
		for(TSplb t : l){
			Splb s = new Splb();
			BeanUtils.copyProperties(t, s);
			s.setSpdlId(t.getTSpdl().getId());
			s.setSpdlmc(t.getTSpdl().getSpdlmc());
			nl.add(s);
		}
		return nl;
	}
	
	@Autowired
	public void setSplbDao(BaseDaoI<TSplb> splbDao) {
		this.splbDao = splbDao;
	}
	
	@Autowired
	public void setSpdlDao(BaseDaoI<TSpdl> spdlDao) {
		this.spdlDao = spdlDao;
	}

}
