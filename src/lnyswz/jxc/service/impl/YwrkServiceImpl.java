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

import javax.print.DocFlavor;

import org.apache.log4j.Logger;
import org.apache.struts2.components.Bean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.ProBean;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Cgjh;
import lnyswz.jxc.bean.Ck;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.KfrkDet;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.bean.Ywrk;
import lnyswz.jxc.bean.YwrkDet;
import lnyswz.jxc.model.TCgjh;
import lnyswz.jxc.model.TCgjhDet;
import lnyswz.jxc.model.TCk;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TKfrk;
import lnyswz.jxc.model.THw;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TRklx;
import lnyswz.jxc.model.TSpDet;
import lnyswz.jxc.model.TXskp;
import lnyswz.jxc.model.TYwrk;
import lnyswz.jxc.model.TYwrkDet;
import lnyswz.jxc.model.TCgxq;
import lnyswz.jxc.model.TGys;
import lnyswz.jxc.model.TJsfs;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.model.TRole;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.model.TUser;
import lnyswz.jxc.service.YwrkServiceI;
import lnyswz.jxc.util.Constant;

/**
 * 采购需求实现类
 * @author 王文阳
 *
 */
@Service("ywrkService")
public class YwrkServiceImpl implements YwrkServiceI {
	private Logger logger = Logger.getLogger(YwrkServiceImpl.class);
	private BaseDaoI<TYwrk> ywrkDao;
	private BaseDaoI<TYwrkDet> detDao;
	private BaseDaoI<TYwzz> ywzzDao;
	private BaseDaoI<TKfrk> kfrkDao;
	private BaseDaoI<TXskp> xskpDao;
	private BaseDaoI<TCgjhDet> cgjhDetDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TSpDet> spDetDao;
	private BaseDaoI<TDepartment> depDao;


	private BaseDaoI<TSp> spDao;
	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public Ywrk save(Ywrk ywrk) {
		String ywrklsh = LshServiceImpl.updateLsh(ywrk.getBmbh(), ywrk.getLxbh(), lshDao);
		TYwrk tYwrk = new TYwrk();
		BeanUtils.copyProperties(ywrk, tYwrk);
		tYwrk.setCreateTime(new Date());
		tYwrk.setYwrklsh(ywrklsh);
		tYwrk.setBmmc(depDao.load(TDepartment.class, ywrk.getBmbh()).getDepName());

		tYwrk.setIsCj("0");
		
		//如果从库房入库生成的入库，进行关联
		String kfrklshs = ywrk.getKfrklshs();
		if(kfrklshs != null && kfrklshs.trim().length() > 0){
			for(String kfrklsh : kfrklshs.split(",")){
				TKfrk tKfrk = kfrkDao.load(TKfrk.class, kfrklsh);
				tKfrk.setTYwrk(tYwrk);
			}
		}
		
		if(ywrk.getXskplsh() != null && ywrk.getXskplsh().trim().length() > 0){
			TXskp tXskp = xskpDao.load(TXskp.class, ywrk.getXskplsh());
			tXskp.setTYwrk(tYwrk);
		}
		
		//如果从采购计划直送生成的入库，进行关联
		String cgjhDetIds = ywrk.getCgjhDetIds();
		if(cgjhDetIds != null && cgjhDetIds.trim().length() > 0){
			Set<TCgjhDet> tCgjhDets = new HashSet<TCgjhDet>();
			for(String cgjhDetId : cgjhDetIds.split(",")){
				TCgjhDet tCgjhDet = cgjhDetDao.load(TCgjhDet.class, Integer.valueOf(cgjhDetId));
				tCgjhDets.add(tCgjhDet);
			}
			tYwrk.setTCgjhs(tCgjhDets);
		}
		
		Department dep = new Department();
		dep.setId(ywrk.getBmbh());
		dep.setDepName(tYwrk.getBmmc());
		
		Ck ck = new Ck();
		ck.setId(ywrk.getCkId());
		ck.setCkmc(tYwrk.getCkmc());
		
		//处理商品明细
		Set<TYwrkDet> tDets = new HashSet<TYwrkDet>();
		ArrayList<YwrkDet> ywrkDets = JSON.parseObject(ywrk.getDatagrid(), new TypeReference<ArrayList<YwrkDet>>(){});
		for(YwrkDet ywrkDet : ywrkDets){
			TYwrkDet tDet = new TYwrkDet();
			BeanUtils.copyProperties(ywrkDet, tDet);
			
			if("".equals(ywrkDet.getCjldwId()) || null == ywrkDet.getZhxs()){
				tDet.setCdwdj(Constant.BD_ZERO);
				tDet.setCdwsl(Constant.BD_ZERO);
				tDet.setZhxs(Constant.BD_ZERO);
			}else{
				if(ywrkDet.getZhxs().compareTo(Constant.BD_ZERO) == 0){
					tDet.setCdwdj(Constant.BD_ZERO);
					tDet.setCdwsl(Constant.BD_ZERO);
				}
			}
			Sp sp = new Sp();
			BeanUtils.copyProperties(ywrkDet, sp);

			tDet.setQdwcb(YwzzServiceImpl.getDwcb(ywrk.getBmbh(), ywrkDet.getSpbh(), ywzzDao));

			//更新业务总账
			YwzzServiceImpl.updateYwzzSl(sp, dep, ck, ywrkDet.getZdwsl(), ywrkDet.getSpje(), null, null, Constant.UPDATE_RK, ywzzDao);
			tDet.setDwcb(YwzzServiceImpl.getDwcb(ywrk.getBmbh(), ywrkDet.getSpbh(), ywzzDao));
			tDets.add(tDet);
			tDet.setTYwrk(tYwrk);
			
		}
		tYwrk.setTYwrkDets(tDets);
		ywrkDao.save(tYwrk);		
				
		OperalogServiceImpl.addOperalog(ywrk.getCreateId(), ywrk.getBmbh(), ywrk.getMenuId(), ywrklsh, 
				"生成业务入库单", operalogDao);
		
		Ywrk rYwrk = new Ywrk();
		rYwrk.setYwrklsh(ywrklsh);
		return rYwrk;
	}
	
	@Override
	public Ywrk cjYwrk(Ywrk ywrk) {
		Date now = new Date();
		//更新新单据
		String lsh = LshServiceImpl.updateLsh(ywrk.getBmbh(), ywrk.getLxbh(), lshDao);
		//更新原单据信息
		TYwrk yTYwrk = ywrkDao.get(TYwrk.class, ywrk.getYwrklsh());
		//新增冲减单据信息
		TYwrk tYwrk = new TYwrk();
		BeanUtils.copyProperties(yTYwrk, tYwrk, new String[]{"TCgjhs", "TKfrks"});
		
		yTYwrk.setCjId(ywrk.getCjId());
		yTYwrk.setCjTime(now);
		yTYwrk.setCjName(ywrk.getCjName());
		yTYwrk.setIsCj("1");
		if(yTYwrk.getTXskp() != null){
			yTYwrk.getTXskp().setTYwrk(null);
		}


		tYwrk.setYwrklsh(lsh);
		tYwrk.setCjYwrklsh(yTYwrk.getYwrklsh());
		tYwrk.setCreateTime(now);
		tYwrk.setCreateId(ywrk.getCjId());
		tYwrk.setCreateName(ywrk.getCjName());
		tYwrk.setIsCj("1");
		tYwrk.setCjTime(now);
		tYwrk.setCjName(ywrk.getCjName());
		tYwrk.setHjje(tYwrk.getHjje().negate());
		tYwrk.setBz(ywrk.getBz());
		
		Department dep = new Department();
		dep.setId(ywrk.getBmbh());
		dep.setDepName(depDao.load(TDepartment.class, ywrk.getBmbh()).getDepName());
		Ck ck = new Ck();
		ck.setId(tYwrk.getCkId());
		ck.setCkmc(tYwrk.getCkmc());
		
		//由库房入库生成的业务入库冲减
		if(yTYwrk.getTKfrks() != null){
			for(TKfrk tk : yTYwrk.getTKfrks()){
				tk.setTYwrk(null);
			}
		}
		
		//由采购计划（直送）生成的业务入库冲减
		if(yTYwrk.getTCgjhs() != null){
			yTYwrk.setTCgjhs(null);
		}
		
		
		Set<TYwrkDet> yTYwrkDets = yTYwrk.getTYwrkDets();
		Set<TYwrkDet> tDets = new HashSet<TYwrkDet>();
		for(TYwrkDet yTDet : yTYwrkDets){
			TYwrkDet tDet = new TYwrkDet();
			BeanUtils.copyProperties(yTDet, tDet, new String[]{"id"});
			Sp sp = new Sp();
			BeanUtils.copyProperties(yTDet, sp);
			tDet.setZdwsl(yTDet.getZdwsl().negate());
			if(yTDet.getCdwsl() != null){
				tDet.setCdwsl(yTDet.getCdwsl().negate());
			}
			tDet.setSpje(yTDet.getSpje().negate());
			//更新冲减前dwcb
			tDet.setQdwcb(YwzzServiceImpl.getDwcb(tYwrk.getBmbh(), tDet.getSpbh(), ywzzDao));
			
			//更新业务总账
			YwzzServiceImpl.updateYwzzSl(sp, dep, ck, tDet.getZdwsl(), tDet.getSpje(), null, null, Constant.UPDATE_RK, ywzzDao);
			
			//更新冲减后dwcb
			tDet.setDwcb(YwzzServiceImpl.getDwcb(tYwrk.getBmbh(), tDet.getSpbh(), ywzzDao));
			tDet.setTYwrk(tYwrk);
			
			tDets.add(tDet);
			
		}
		tYwrk.setTYwrkDets(tDets);
		ywrkDao.save(tYwrk);
		
		OperalogServiceImpl.addOperalog(ywrk.getCjId(), ywrk.getBmbh(), ywrk.getMenuId(), tYwrk.getCjYwrklsh() + "/" + tYwrk.getYwrklsh(), 
				"冲减业务入库单", operalogDao);
		
		Ywrk rYwrk = new Ywrk();
		rYwrk.setYwrklsh(lsh);
		return rYwrk;
	}
	
	@Override
	public DataGrid datagrid(Ywrk ywrk) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TYwrk t where t.bmbh = :bmbh and t.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", ywrk.getBmbh());
		if(ywrk.getCreateTime() != null){
			params.put("createTime", ywrk.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		if(ywrk.getSearch() != null){
			hql += " and (t.ywrklsh like :search or t.gysmc like :search or t.bz like :search)"; 
			params.put("search", "%" + ywrk.getSearch() + "%");
			
		}
		
		if(ywrk.getFromOther() != null){
			hql += " and t.isCj = '0'";
			if(Constant.YWRK_FROM_YWBT.equals(ywrk.getFromOther())){
				hql += " and t.rklxId = :rklxId and t.TYwbt is null";
				params.put("rklxId", Constant.RKLX_ZS);
			}else{
				hql += " and t.isZs = '0' and t.TKfrks is empty";
			}
		}else{
			hql += " or (t.bmbh = :bmbh and t.rklxId = :rklxId and t.isCj = '0')";
			params.put("rklxId", Constant.RKLX_ZG);
		}
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TYwrk> l = ywrkDao.find(hql, params, ywrk.getPage(), ywrk.getRows());
		List<Ywrk> nl = new ArrayList<Ywrk>();
		for(TYwrk t : l){
			Ywrk c = new Ywrk();
			BeanUtils.copyProperties(t, c);
			Set<TKfrk> tKfrks = t.getTKfrks();
			if(tKfrks != null && tKfrks.size() > 0){
				String kfrklshs = "";
				int i = 0;
				for(TKfrk tc : tKfrks){
					kfrklshs += tc.getKfrklsh();
					if(i < tKfrks.size() - 1){
						kfrklshs += ",";
					}
					i++;
				}
				c.setKfrklshs(kfrklshs);
			}
			Set<TCgjhDet> tCgjhs = t.getTCgjhs();
			if(tCgjhs != null && tCgjhs.size() > 0){
				String cgjhlshs = "";
				int i = 0;
				for(TCgjhDet tc : tCgjhs){
					cgjhlshs += tc.getTCgjh().getCgjhlsh();
					if(i < tCgjhs.size() - 1){
						cgjhlshs += ",";
					}
					i++;
				}
				c.setCgjhlshs(cgjhlshs);
			}
			if(t.getTYwbt() != null){
				c.setYwbtlsh(t.getTYwbt().getYwbtlsh());
			}
			nl.add(c);
		}
		datagrid.setTotal(ywrkDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(String ywrklsh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TYwrkDet t where t.TYwrk.ywrklsh = :ywrklsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ywrklsh", ywrklsh);
		List<TYwrkDet> l = detDao.find(hql, params);
		List<YwrkDet> nl = new ArrayList<YwrkDet>();
		for(TYwrkDet t : l){
			YwrkDet c = new YwrkDet();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid datagridDet(Ywrk ywrk) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TYwrkDet t where t.TYwrk.bmbh = :bmbh and t.TYwrk.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", ywrk.getBmbh());
		if(ywrk.getCreateTime() != null){
			params.put("createTime", ywrk.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		if(ywrk.getSearch() != null){
			hql += " and (t.TYwrk.ywrklsh like :search or t.TYwrk.gysbh like :search or t.TYwrk.gysmc like :search or t.TYwrk.bz like :search)"; 
			params.put("search", "%" + ywrk.getSearch() + "%");
			
		}
		
		//在销售提货中查看
		if(ywrk.getFromOther() != null){
			hql += " and t.TYwrk.isZs = '1' and t.TYwrk.isCj = '0'";
		}
		
		String countHql = "select count(*) " + hql;
		hql += " order by t.TYwrk.createTime desc ";
		
		List<TYwrkDet> l = detDao.find(hql, params);
		List<Ywrk> nl = new ArrayList<Ywrk>();
		for(TYwrkDet t : l){
			Ywrk c = new Ywrk();
			
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		
		datagrid.setRows(nl);
		datagrid.setTotal(ywrkDao.count(countHql, params));
		return datagrid;
	}

	@Override
	public DataGrid printYwrk(Ywrk ywrk) {
		DataGrid datagrid = new DataGrid();
		String kfrklsh = "";
		TYwrk tYwrk = ywrkDao.load(TYwrk.class, ywrk.getYwrklsh());
		for (TKfrk y : tYwrk.getTKfrks()) {
			kfrklsh += y.getKfrklsh() + ",";
		}
		List<YwrkDet> nl = new ArrayList<YwrkDet>();
		for (TYwrkDet yd : tYwrk.getTYwrkDets()) {
			YwrkDet ywrkDet = new YwrkDet();
			BeanUtils.copyProperties(yd, ywrkDet);
			nl.add(ywrkDet);
		}
		int num = nl.size();
		if (num < Constant.REPORT_NUMBER) {
			for (int i = 0; i < (Constant.REPORT_NUMBER - num); i++) {
				nl.add(new YwrkDet());
			}
		}
//		Ywrk ywrk = new Ywrk();
//		BeanUtils.copyProperties(yk, ywrk);
//		ywrk.setKfrklshs(kfrklsh);
		Map<String, Object> map = new HashMap<String, Object>();
		String lsh = "";
		lsh += tYwrk.getYwrklsh();
		if (tYwrk.getCjYwrklsh() != null) {
			lsh += " - " + tYwrk.getCjYwrklsh();
		}
		map.put("title", "商   品   入   库   单");
		map.put("bmmc", tYwrk.getBmmc());
		map.put("createTime", DateUtil.dateToString(tYwrk.getCreateTime(), DateUtil.DATETIME_NOSECOND_PATTERN));
		map.put("ywrklsh", lsh);
		map.put("gysmc", tYwrk.getGysmc());
		map.put("gysbh", tYwrk.getGysbh());
		map.put("rklxmc", tYwrk.getRklxmc());
		map.put("hjje", tYwrk.getHjje());
		map.put("bz", tYwrk.getBz());
		map.put("kfrklsh", kfrklsh);
		if (tYwrk.getIsZs().equals("1")) {
			map.put("isZs", ("直送" + tYwrk.getShdz()));
		} else if (tYwrk.getIsZs().equals("0")) {
			map.put("isZs", "");
		}
		map.put("ckmc", tYwrk.getCkmc());
		map.put("printName", ywrk.getCreateName());
		map.put("printTime", DateUtil.dateToString(new Date()));
		datagrid.setObj(map);
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid getSpkc(Ywrk ywrk) {
		DataGrid dg = new DataGrid();
		List<ProBean> lists = new ArrayList<ProBean>();
		
		List<ProBean> yw = YwzzServiceImpl.getZzsl(ywrk.getBmbh(), ywrk.getSpbh(), null, ywzzDao);
		if(yw != null){
			lists.addAll(yw);
		}
		
		String hql = "from TSpDet t where t.TDepartment.id = :depId and t.TSp.spbh = :spbh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", ywrk.getBmbh());
		params.put("spbh", ywrk.getSpbh());
		
		TSpDet tSpDet = spDetDao.get(hql, params);
		if(tSpDet != null && tSpDet.getLastRkdj() != null && tSpDet.getLastRkdj().compareTo(Constant.BD_ZERO) != 0){
			ProBean rkdj = new ProBean();
			rkdj.setGroup("入库信息");
			rkdj.setName("最后入库价");
			rkdj.setValue("" + tSpDet.getLastRkdj());
			lists.add(rkdj);
		}
		
		dg.setRows(lists);
		dg.setTotal((long)lists.size());
		return dg;
	}
	
	@Override
	public DataGrid toKfrk(String ywrklsh){
		//单据表头处理
		TYwrk tYwrk = ywrkDao.load(TYwrk.class, ywrklsh);
		Ywrk ywrk = new Ywrk();
		BeanUtils.copyProperties(tYwrk, ywrk);
		//商品明细处理
		String sql = "select spbh, sum(zdwsl) zdwsl from t_ywrk_det t ";
		
		if(ywrklsh != null && ywrklsh.trim().length() > 0){
			sql += "where ywrklsh = " + ywrklsh;
		}
		sql += " group by spbh";
		
		List<Object[]> l = detDao.findBySQL(sql);
		List<YwrkDet> nl = new ArrayList<YwrkDet>();
		
		for(Object[] os : l){
			String spbh = (String)os[0];
			BigDecimal zdwsl = new BigDecimal(os[1].toString());
			
			TSp sp = spDao.get(TSp.class, spbh);
			YwrkDet yd = new YwrkDet();
			yd.setSpbh(spbh);
			yd.setSpmc(sp.getSpmc());
			yd.setSpcd(sp.getSpcd());
			yd.setSppp(sp.getSppp());
			yd.setSpbz(sp.getSpbz());
			yd.setZdwsl(zdwsl);
			yd.setZjldwmc(sp.getZjldw().getJldwmc());
			if(sp.getCjldw() != null){
				yd.setCjldwmc(sp.getCjldw().getJldwmc());
				yd.setZhxs(sp.getZhxs());
				yd.setCdwsl(zdwsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
			}
			nl.add(yd);
		}
		nl.add(new YwrkDet());
		
		DataGrid dg = new DataGrid();
		dg.setObj(ywrk);
		dg.setRows(nl);
		return dg;
	}
	
	@Override
	public DataGrid toYwbt(String ywrklsh){
		String hql = "from TYwrkDet t where t.TYwrk.ywrklsh = :ywrklsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ywrklsh", ywrklsh);
		
		List<TYwrkDet> l = detDao.find(hql, params);
		List<YwrkDet> nl = new ArrayList<YwrkDet>();
		for(TYwrkDet tYwrkDet : l){
			YwrkDet ywrkDet = new YwrkDet();
			BeanUtils.copyProperties(tYwrkDet, ywrkDet);
			
			nl.add(ywrkDet);
		}
		
		DataGrid dg = new DataGrid();
		dg.setRows(nl);
		return dg;
	}
	
	@Autowired
	public void setYwrkDao(BaseDaoI<TYwrk> ywrkDao) {
		this.ywrkDao = ywrkDao;
	}

	@Autowired
	public void setYwzzDao(BaseDaoI<TYwzz> ywzzDao) {
		this.ywzzDao = ywzzDao;
	}

	@Autowired
	public void setDetDao(BaseDaoI<TYwrkDet> detDao) {
		this.detDao = detDao;
	}

	@Autowired
	public void setKfrkDao(BaseDaoI<TKfrk> kfrkDao) {
		this.kfrkDao = kfrkDao;
	}

	@Autowired
	public void setXskpDao(BaseDaoI<TXskp> xskpDao) {
		this.xskpDao = xskpDao;
	}

	@Autowired
	public void setCgjhDetDao(BaseDaoI<TCgjhDet> cgjhDetDao) {
		this.cgjhDetDao = cgjhDetDao;
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
