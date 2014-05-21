package lnyswz.jxc.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.struts2.components.Bean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.bean.Xsth;
import lnyswz.jxc.bean.Ywbt;
import lnyswz.jxc.bean.YwbtDet;
import lnyswz.jxc.bean.Ywrk;
import lnyswz.jxc.bean.YwrkDet;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TKfrk;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TSpDet;
import lnyswz.jxc.model.TYwbt;
import lnyswz.jxc.model.TYwbtDet;
import lnyswz.jxc.model.TYwrk;
import lnyswz.jxc.model.TYwrkDet;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.service.YwbtServiceI;
import lnyswz.jxc.util.Constant;

/**
 * 业务补调实现类
 * @author 王文阳
 *
 */
@Service("ywbtService")
public class YwbtServiceImpl implements YwbtServiceI {
	private Logger logger = Logger.getLogger(YwbtServiceImpl.class);
	private BaseDaoI<TYwbt> ywbtDao;
	private BaseDaoI<TYwbtDet> detDao;
	private BaseDaoI<TYwzz> ywzzDao;
	private BaseDaoI<TYwrk> ywrkDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TSpDet> spDetDao;
	private BaseDaoI<TDepartment> depDao;

	private BaseDaoI<TSp> spDao;
	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public Ywbt save(Ywbt ywbt) {
		String lsh = LshServiceImpl.updateLsh(ywbt.getBmbh(), ywbt.getLxbh(), lshDao);
		TYwbt tYwbt = new TYwbt();
		BeanUtils.copyProperties(ywbt, tYwbt);
		tYwbt.setCreateTime(new Date());
		tYwbt.setYwbtlsh(lsh);
		tYwbt.setBmmc(depDao.load(TDepartment.class, ywbt.getBmbh()).getDepName());
		
		TYwrk tYwrk = ywrkDao.load(TYwrk.class, ywbt.getYwrklsh());
		tYwrk.setTYwbt(tYwbt);
		
		Department dep = new Department();
		dep.setId(ywbt.getBmbh());
		dep.setDepName(tYwbt.getBmmc());
		
		//处理商品明细
		Set<TYwbtDet> tDets = new HashSet<TYwbtDet>();
		ArrayList<YwbtDet> ywbtDets = JSON.parseObject(ywbt.getDatagrid(), new TypeReference<ArrayList<YwbtDet>>(){});
		for(YwbtDet ywbtDet : ywbtDets){
			TYwbtDet tDet = new TYwbtDet();
			BeanUtils.copyProperties(ywbtDet, tDet, new String[]{"id"});
			if(null == ywbtDet.getBtje()){
				tDet.setSpje(Constant.BD_ZERO);
			}else{
				tDet.setSpje(ywbtDet.getBtje());
			}
			
			Sp sp = new Sp();
			BeanUtils.copyProperties(ywbtDet, sp);

			tDet.setQdwcb(YwzzServiceImpl.getDwcb(ywbt.getBmbh(), tDet.getSpbh(), ywzzDao));
			//更新业务总账
			YwzzServiceImpl.updateYwzzSl(sp, dep, null, null, tDet.getSpje(), null, null, Constant.UPDATE_BT, ywzzDao);
			tDet.setDwcb(YwzzServiceImpl.getDwcb(ywbt.getBmbh(), tDet.getSpbh(), ywzzDao));
			
			tDet.setTYwbt(tYwbt);
			tDets.add(tDet);
			
		}
		tYwbt.setTYwbtDets(tDets);
		ywbtDao.save(tYwbt);		
		
		OperalogServiceImpl.addOperalog(ywbt.getCreateId(), ywbt.getBmbh(), ywbt.getMenuId(), tYwbt.getYwbtlsh(), 
				"生成业务补调单", operalogDao);
		
		Ywbt rYwbt = new Ywbt();
		rYwbt.setYwbtlsh(lsh);
		return rYwbt;
	}
	
	@Override
	public DataGrid printYwbt(Ywbt ywbt) {
		DataGrid datagrid = new DataGrid();
		TYwbt tYwbt = ywbtDao.load(TYwbt.class, ywbt.getYwbtlsh());
		
		List<YwbtDet> nl = new ArrayList<YwbtDet>();
		for (TYwbtDet yd : tYwbt.getTYwbtDets()) {
			YwbtDet ywbtDet = new YwbtDet();
			BeanUtils.copyProperties(yd, ywbtDet);
			nl.add(ywbtDet);
		}
		int num = nl.size();
		if (num < Constant.REPORT_NUMBER) {
			for (int i = 0; i < (Constant.REPORT_NUMBER - num); i++) {
				nl.add(new YwbtDet());
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "商   品   入   库   单");
		map.put("bmmc", tYwbt.getBmmc());
		map.put("createTime", DateUtil.dateToString(tYwbt.getCreateTime(), DateUtil.DATETIME_NOSECOND_PATTERN));
		map.put("ywbtlsh", tYwbt.getYwbtlsh());
		map.put("ywrklsh", tYwbt.getTYwrk().getYwrklsh());
		map.put("rklxmc", "补调");
		map.put("ckmc", tYwbt.getTYwrk().getCkmc());
		map.put("hjje", tYwbt.getHjje());
		map.put("bz", tYwbt.getBz());

		map.put("printName", ywbt.getCreateName());
		map.put("printTime", DateUtil.dateToString(new Date()));
		datagrid.setObj(map);
		datagrid.setRows(nl);
		return datagrid;
	}
		
	@Override
	public DataGrid datagrid(Ywbt ywbt) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TYwbt t where t.bmbh = :bmbh and t.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", ywbt.getBmbh());
		if(ywbt.getCreateTime() != null){
			params.put("createTime", ywbt.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TYwbt> l = ywbtDao.find(hql, params, ywbt.getPage(), ywbt.getRows());
		List<Ywbt> nl = new ArrayList<Ywbt>();
		for(TYwbt t : l){
			Ywbt c = new Ywbt();
			BeanUtils.copyProperties(t, c);
			
			if(t.getTYwrk() != null){
				c.setYwrklsh(t.getTYwrk().getYwrklsh());
			}
			
			nl.add(c);
		}
		datagrid.setTotal(ywbtDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(String ywbtlsh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TYwbtDet t where t.TYwbt.ywbtlsh = :ywbtlsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ywbtlsh", ywbtlsh);
		List<TYwbtDet> l = detDao.find(hql, params);
		List<YwbtDet> nl = new ArrayList<YwbtDet>();
		for(TYwbtDet t : l){
			YwbtDet c = new YwbtDet();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Autowired
	public void setYwbtDao(BaseDaoI<TYwbt> ywbtDao) {
		this.ywbtDao = ywbtDao;
	}

	@Autowired
	public void setYwzzDao(BaseDaoI<TYwzz> ywzzDao) {
		this.ywzzDao = ywzzDao;
	}

	@Autowired
	public void setYwrkDao(BaseDaoI<TYwrk> ywrkDao) {
		this.ywrkDao = ywrkDao;
	}

	@Autowired
	public void setDetDao(BaseDaoI<TYwbtDet> detDao) {
		this.detDao = detDao;
	}

	@Autowired
	public void setLshDao(BaseDaoI<TLsh> lshDao) {
		this.lshDao = lshDao;
	}

	@Autowired
	public void setDepDao(BaseDaoI<TDepartment> depDao) {
		this.depDao = depDao;
	}

	@Autowired
	public void setSpDao(BaseDaoI<TSp> spDao) {
		this.spDao = spDao;
	}
	
	@Autowired
	public void setSpDetDao(BaseDaoI<TSpDet> spDetDao) {
		this.spDetDao = spDetDao;
	}


	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
}
