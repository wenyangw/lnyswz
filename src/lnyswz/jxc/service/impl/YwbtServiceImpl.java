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

import lnyswz.jxc.bean.*;
import lnyswz.jxc.model.*;
import lnyswz.jxc.service.RkServiceI;
import lnyswz.jxc.service.YwrkServiceI;
import org.apache.log4j.Logger;
import org.apache.struts2.components.Bean;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.service.YwbtServiceI;
import lnyswz.jxc.util.Constant;

import static org.apache.cxf.tools.java2wsdl.processor.internal.SpringServiceBuilderFactory.getApplicationContext;

/**
 * 业务补调实现类
 * @author 王文阳
 *
 */
@Service("ywbtService")
public class YwbtServiceImpl implements YwbtServiceI {
	private Logger logger = Logger.getLogger(YwbtServiceImpl.class);
	private RkServiceI rkService;
	private BaseDaoI<TYwbt> ywbtDao;
	private BaseDaoI<TYwbtDet> detDao;
	private BaseDaoI<TYwzz> ywzzDao;
	private BaseDaoI<TYwrk> ywrkDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TDepartment> depDao;

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
		TYwbtDet tDet = null;
		Sp sp = null;
		for(YwbtDet ywbtDet : ywbtDets){
			tDet = new TYwbtDet();
			BeanUtils.copyProperties(ywbtDet, tDet, new String[]{"id"});
			if(null == ywbtDet.getBtje()){
				tDet.setSpje(Constant.BD_ZERO);
			}else{
				tDet.setSpje(ywbtDet.getBtje());
			}
			
			sp = new Sp();
			BeanUtils.copyProperties(ywbtDet, sp);

			tDet.setQdwcb(YwzzServiceImpl.getDwcb(ywbt.getBmbh(), tDet.getSpbh(), ywzzDao));
			//更新业务总账
			YwzzServiceImpl.updateYwzzSl(sp, dep, null, null, null, tDet.getSpje(), null, null, Constant.UPDATE_BT, ywzzDao);
			tDet.setDwcb(YwzzServiceImpl.getDwcb(ywbt.getBmbh(), tDet.getSpbh(), ywzzDao));
			
			tDet.setTYwbt(tYwbt);
			tDets.add(tDet);
			
		}
		tYwbt.setTYwbtDets(tDets);
		ywbtDao.save(tYwbt);

		// 处理应付
		if (!"".equals(ywbt.getGysbh()) && ywbt.getHjjea() != null) {
			rkService.saveYf(dep, new Gys(ywbt.getGysbh(), ywbt.getGysmc()), ywbt.getHjjea(), ywbt.getYwrklsh(), lsh, "业务补调");
		}

		if (!"".equals(ywbt.getGysbhb()) && ywbt.getHjjeb() != null) {
			rkService.saveYf(dep, new Gys(ywbt.getGysbhb(), ywbt.getGysmcb()), ywbt.getHjjeb(), ywbt.getYwrklsh(), lsh,"业务补调");
		}

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
		YwbtDet ywbtDet = null;
		for (TYwbtDet yd : tYwbt.getTYwbtDets()) {
			ywbtDet = new YwbtDet();
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
		map.put("gysmc", tYwbt.getTYwrk().getGysmc());
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
		StringBuilder hql = new StringBuilder(" from TYwbt t where t.bmbh = :bmbh and t.createTime > :createTime and t.createId = :createId");
		//String hql = " from TYwbt t where t.bmbh = :bmbh and t.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", ywbt.getBmbh());
		params.put("createId", ywbt.getCreateId());
		if(ywbt.getCreateTime() != null){
			params.put("createTime", ywbt.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		
		//String countHql = " select count(*)" + hql;
		StringBuilder countHql = new StringBuilder("select count(*)").append(hql);
		//hql += " order by t.createTime desc";
		hql.append(" order by t.createTime desc");
		List<TYwbt> l = ywbtDao.find(hql.toString(), params, ywbt.getPage(), ywbt.getRows());
		List<Ywbt> nl = new ArrayList<Ywbt>();
		Ywbt c = null;
		for(TYwbt t : l){
			c = new Ywbt();
			BeanUtils.copyProperties(t, c);

			String ywrkSql = "select ywrklsh, gysmc from t_ywrk where ywbtlsh = ?";
			Map<String, Object> ywrkParams = new HashMap<String, Object>();
			ywrkParams.put("0", t.getYwbtlsh());

			Object[] tYwrk = ywrkDao.getMBySQL(ywrkSql, ywrkParams);
			if(tYwrk != null) {
				c.setYwrklsh(tYwrk[0].toString());
				c.setGysmc(tYwrk[1].toString());
//				c.setGysmc(t.getTYwrk().getGysmc());
			}
			
//			if(t.getTYwrk() != null){
//				c.setYwrklsh(t.getTYwrk().getYwrklsh());
//			}
			
			nl.add(c);
		}
		datagrid.setTotal(ywbtDao.count(countHql.toString(), params));
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
		YwbtDet c = null;
		for(TYwbtDet t : l){
			c = new YwbtDet();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}

	@Autowired
	public void setRkService(RkServiceI rkService) {
		this.rkService = rkService;
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
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
}
