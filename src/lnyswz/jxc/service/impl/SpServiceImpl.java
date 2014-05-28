package lnyswz.jxc.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.ProBean;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TJldw;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.model.TSpDet;
import lnyswz.jxc.model.TSpdw;
import lnyswz.jxc.service.SpServiceI;
import lnyswz.jxc.util.Constant;

/**
 * 商品实现类
 * @author 王文阳
 *
 */
@Service("spService")
public class SpServiceImpl implements SpServiceI {
	private Logger logger = Logger.getLogger(SpServiceImpl.class);
	private BaseDaoI<TSp> spDao;
	private BaseDaoI<TSpDet> detDao;
	private BaseDaoI<TSpdw> spdwDao;
	private BaseDaoI<TJldw> jldwDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TOperalog> operalogDao;
	
	/**
	 * 增加商品
	 */
	@Override
	public Sp add(Sp sp) {
		TSp t = new TSp();
		BeanUtils.copyProperties(sp, t);
		//设置商品段位
		if(sp.getSpdwId() != null && sp.getSpdwId().trim().length() > 0){
			TSpdw spdw = spdwDao.get(TSpdw.class, sp.getSpdwId());
			if(spdw != null){
				t.setTSpdw(spdw);
			}
		}
		//设置商品计量单位
		setJldw(t, sp);
		spDao.save(t);
		OperalogServiceImpl.addOperalog(sp.getUserId(), sp.getDepId(), sp.getMenuId(), t.getSpbh(), "增加商品记录", operalogDao);
		return changeSp(t, null);
	}

	/**
	 * 修改商品
	 */
	@Override
	public void edit(Sp sp) {
		TSp t = spDao.get(TSp.class, sp.getSpbh());
		BeanUtils.copyProperties(sp, t);
		//设置商品计量单位
		setJldw(t, sp);
		OperalogServiceImpl.addOperalog(sp.getUserId(), sp.getDepId(), sp.getMenuId(), t.getSpbh(), "修改商品记录", operalogDao);
	}
	
	/**
	 * 删除商品
	 */
	@Override
	public void delete(Sp sp) {
		TSp t = spDao.get(TSp.class, sp.getSpbh());
		spDao.delete(t);
		OperalogServiceImpl.addOperalog(sp.getUserId(), sp.getDepId(), sp.getMenuId(), t.getSpbh(), "删除商品记录", operalogDao);
	}
	
	/**
	 * 维护专属信息
	 */
	@Override
	public void editSpDet(Sp sp) {
		String keyId = "";
		//已存在，修改
		if(sp.getDetId() != 0){
			TSpDet det = detDao.get(TSpDet.class, sp.getDetId());
			BeanUtils.copyProperties(sp, det);
			keyId = sp.getSpbh() + "/" + det.getId();
 		}else{
			TSp t = spDao.get(TSp.class, sp.getSpbh());
			Set<TSpDet> dets= t.getTSpDets();
			TSpDet det = new TSpDet();
			BeanUtils.copyProperties(sp, det);
			det.setTSp(t);
			det.setTDepartment(depDao.get(TDepartment.class, sp.getDepId()));
			dets.add(det);
			keyId = sp.getSpbh() + "/" + det.getId();
		}
		OperalogServiceImpl.addOperalog(sp.getUserId(), sp.getDepId(), sp.getMenuId(), keyId, "修改商品专属信息", operalogDao);
	}
	
	/**
	 * 删除专属信息
	 */
	@Override
	public void deleteSpDet(Sp sp) {
		TSp tSp = spDao.get(TSp.class, sp.getSpbh());
		Set<TSpDet> dets = tSp.getTSpDets();
		tSp.setTSpDets(null);
		TSpDet det = detDao.get(TSpDet.class, sp.getDetId());
		dets.remove(det);
		detDao.delete(det);
		tSp.setTSpDets(dets);
		OperalogServiceImpl.addOperalog(sp.getUserId(), sp.getDepId(), sp.getMenuId(), sp.getSpbh() + "/" + sp.getDetId(), "删除商品专属信息", operalogDao);
	}
	
	@Override
	public DataGrid spDg(Sp sp) {
		String hql = "from TSp t join t.TSpdw.TSplb.TSpdl.TDepartments deps where deps.id = :depId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", sp.getDepId());
		String countHql = "select count(spbh) " + hql;
		if(sp.getQuery() != null && sp.getQuery().trim().length() > 0){
			String where = " and (t.spbh like :spbh or t.spmc like :spmc)"; 
			hql += where;
			countHql += where;
			params.put("spbh", sp.getQuery() + "%");
			params.put("spmc", "%" + sp.getQuery() + "%");
		}
		DataGrid dg = new DataGrid();
		dg.setTotal(spDao.count(countHql, params));
		dg.setRows(changeSps(spDao.find("select t " + hql, params, sp.getPage(), sp.getRows()), sp.getDepId()));
		return dg;
	}

	/**
	 * 获得商品信息，供管理用，有分页
	 */
	@Override
	public DataGrid datagrid(Sp sp) {
		String hql = "from TSp t join t.TSpdw.TSplb.TSpdl.TDepartments deps where deps.id = :depId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", sp.getDepId());
		String countHql = "select count(spbh) " + hql;
		if(sp.getSpdwId() != null && sp.getSpdwId().trim().length() > 0){
			String where = " and t.TSpdw.id = :spdwId"; 
			hql += where;
			countHql += where;
			params.put("spdwId", sp.getSpdwId());
		}
		DataGrid dg = new DataGrid();
		dg.setTotal(spDao.count(countHql, params));
		dg.setRows(changeSps(spDao.find("select t " + hql, params, sp.getPage(), sp.getRows()), sp.getDepId()));
		return dg;
	}
	
	/**
	 * 获得商品信息，供管理用，有分页 
	 */
	@Override
	public DataGrid datagridBgy(Sp sp) {
		logger.info(sp.getBgyId());
		if(sp.getBgyId() != 0){
			String hql = "TSp t join t.TSpdw.TSplb.TSpdl.TDepartments deps where deps.id = :depId";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depId", sp.getDepId());
			String countHql = "select count(t.spbh) " + hql;
			DataGrid dg = new DataGrid();
			dg.setTotal(spDao.count(countHql, params));
			dg.setRows(changeSps(spDao.find("select t " + hql, params, sp.getPage(), sp.getRows()), sp.getDepId()));
			return dg;
		}
		return null;
	}
	
	
	
	/**
	 * 校验商品编码是否存在
	 */
	@Override
	public boolean existSp(Sp sp) {
		TSp t = spDao.get(TSp.class, sp.getSpbh());
		if(t != null){
			return true;
		}
		return false;
	}
	
	@Override
	public Sp loadSp(String spbh, String depId) {
		String hql = "from TSp t join t.TSpdw.TSplb.TSpdl.TDepartments deps where deps.id = :depId and t.spbh = :spbh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", depId);
		params.put("spbh", spbh);
		TSp sp = spDao.get("select t " + hql, params);
		if(sp != null){
			return changeSp(sp, depId);
		}
		return null;
	}
	
	@Override
	public DataGrid getSpkc(Sp sp) {
		DataGrid dg = new DataGrid();
		List<ProBean> lists = new ArrayList<ProBean>();
		String groupName = "业务库存";
		ProBean pb1 = new ProBean();
		pb1.setGroup(groupName);
		pb1.setName("业务库存");
		pb1.setValue("28.35");
		lists.add(pb1);
		
		ProBean pb2 = new ProBean();
		pb2.setGroup(groupName);
		pb2.setName("总账库存");
		pb2.setValue("25.25");
		lists.add(pb2);

		groupName = "库房库存";
		ProBean pb3 = new ProBean();
		pb3.setGroup(groupName);
		pb3.setName("库房库存");
		pb3.setValue("23.34");
		lists.add(pb3);
		
		ProBean pb4 = new ProBean();
		pb4.setGroup(groupName);
		pb4.setName("总库存");
		pb4.setValue("56.25");
		lists.add(pb4);
		
		dg.setRows(lists);
		dg.setTotal((long)lists.size());
		return dg;
	}
	
	/**
	 * 转换列表
	 * @param l
	 * @param did
	 * @return
	 */
	private List<Sp> changeSps(List<TSp> l, String did){
		List<Sp> nl = new ArrayList<Sp>();
		for(TSp t : l){
			nl.add(changeSp(t, did));
		}
		return nl;
	}
	
	/**
	 * 转换类对象
	 * @param t
	 * @param depId
	 * @return
	 */
	private Sp changeSp(TSp t, String depId){
		Sp s = new Sp();
		BeanUtils.copyProperties(t, s);
		s.setSpdwId(t.getTSpdw().getId());
		s.setSpdwmc(t.getTSpdw().getSpdwmc());
		s.setZjldwId(t.getZjldw().getId());
		s.setZjldwmc(t.getZjldw().getJldwmc());
		if(t.getCjldw() != null){
			s.setCjldwId(t.getCjldw().getId());
			s.setCjldwmc(t.getCjldw().getJldwmc());
		}
		Set<TSpDet> spDets = t.getTSpDets();
		if(spDets != null && spDets.size() > 0){
			for(TSpDet det : spDets){
				if(det.getTDepartment().getId().equals(depId) && det.getTSp().getSpbh().equals(t.getSpbh())){
					s.setDetId(det.getId());
					s.setDepId(depId);
					s.setMaxKc(det.getMaxKc());
					s.setMinKc(det.getMinKc());
					s.setXsdj(det.getXsdj());
					s.setLimitXsdj(det.getLimitXsdj());
				}
			}
		}
		return s;
	}
	
	/**
	 * 增加、修改商品信息时设置商品计算单位
	 * @param tSp
	 * @param sp
	 */
	public void setJldw(TSp tSp, Sp sp){
		//设置商品主计量单位
		if(sp.getZjldwId() != null && sp.getZjldwId().trim().length() > 0){
			TJldw zjldw = jldwDao.get(TJldw.class, sp.getZjldwId());
			if(zjldw != null){
				tSp.setZjldw(zjldw);
			}
		}
		//设置商品次计量单位
		if(sp.getCjldwId() != null && sp.getCjldwId().trim().length() > 0){
			TJldw cjldw = jldwDao.get(TJldw.class, sp.getCjldwId());
			if(cjldw != null){
				tSp.setCjldw(cjldw);
			}
		}
	}
	
	public static Sp getSpCdw(String spbh, BaseDaoI<TSp> baseDao){
		TSp tSp = baseDao.load(TSp.class, spbh);
		if(tSp.getCjldw() != null){
			Sp sp = new Sp();
			sp.setCjldwId(tSp.getCjldw().getId());
			sp.setCjldwmc(tSp.getCjldw().getJldwmc());
			sp.setZhxs(tSp.getZhxs());
			return sp;
		}
		return null;
	}
	
	@Autowired
	public void setSpDao(BaseDaoI<TSp> spDao) {
		this.spDao = spDao;
	}
	
	@Autowired
	public void setJldwDao(BaseDaoI<TJldw> jldwDao) {
		this.jldwDao = jldwDao;
	}
	
	@Autowired
	public void setSpdwDao(BaseDaoI<TSpdw> spdwDao) {
		this.spdwDao = spdwDao;
	}
	
	@Autowired
	public void setDetDao(BaseDaoI<TSpDet> detDao) {
		this.detDao = detDao;
	}

	@Autowired
	public void setDepDao(BaseDaoI<TDepartment> depDao) {
		this.depDao = depDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}

}
