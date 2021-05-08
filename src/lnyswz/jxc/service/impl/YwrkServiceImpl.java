package lnyswz.jxc.service.impl;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import lnyswz.jxc.bean.*;
import lnyswz.jxc.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.ProBean;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.service.YwrkServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Util;

/**
 * 采购需求实现类
 * @author 王文阳
 *
 */
@Service("ywrkService")
public class YwrkServiceImpl implements YwrkServiceI {
	private BaseDaoI<TYwrk> ywrkDao;
	private BaseDaoI<TYwrkDet> detDao;
	private BaseDaoI<TYwzz> ywzzDao;
	private BaseDaoI<TKfrk> kfrkDao;
	private BaseDaoI<TXskp> xskpDao;
	private BaseDaoI<TXsth> xsthDao;
	private BaseDaoI<TYfzz> yfzzDao;
	private BaseDaoI<TCgjhDet> cgjhDetDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TSpDet> spDetDao;
	private BaseDaoI<TDepartment> depDao;


	private BaseDaoI<TSp> spDao;
	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public Ywrk save(Ywrk ywrk) {
		TYwrk tYwrk = new TYwrk();
		BeanUtils.copyProperties(ywrk, tYwrk);

		tYwrk.setBmmc(depDao.load(TDepartment.class, ywrk.getBmbh()).getDepName());

		tYwrk.setIsCj("0");
		tYwrk.setFkje(BigDecimal.ZERO);

		Department dep = new Department();
		dep.setId(ywrk.getBmbh());
		dep.setDepName(tYwrk.getBmmc());
		
		Ck ck = new Ck();
		ck.setId(ywrk.getCkId());
		ck.setCkmc(tYwrk.getCkmc());


		
		
		//从暂估入库传入
		Set<TYwrk> zgYwrks = null;
		Set<TCgjhDet> zCgjhDets = null;
		Set<TKfrk> zKfrks = null;
		//Set<TXsth> zXsths = null;
		Map<String, Set<TXsth>> zXsths = null;
		if (ywrk.getYwrklshs() != null && ywrk.getYwrklshs().length() > 0) {
			String[] lshs = ywrk.getYwrklshs().split(",");
			zgYwrks = new HashSet<TYwrk>();
			zCgjhDets = new HashSet<TCgjhDet>();
			zKfrks = new HashSet<TKfrk>();
			zXsths = new HashMap<String, Set<TXsth>>();
			for(String lsh : lshs){
				TYwrk zYwrk = ywrkDao.get(TYwrk.class, lsh);
				if (zYwrk.getTCgjhs() != null && zYwrk.getTCgjhs().size() > 0){
					zCgjhDets.addAll(zYwrk.getTCgjhs());
				}
				if (zYwrk.getTKfrks() != null && zYwrk.getTKfrks().size() > 0){
					zKfrks.addAll(zYwrk.getTKfrks());
				}
				zgYwrks.add(zYwrk);

				for(TYwrkDet tt : zYwrk.getTYwrkDets()){
					if(zXsths.containsKey(tt.getSpbh())){
						zXsths.get(tt.getSpbh()).addAll(tt.getTXsths());
					}else{
						zXsths.put(tt.getSpbh(), tt.getTXsths());
					}
				}
				
				Ywrk zgYwrk = new Ywrk();
				BeanUtils.copyProperties(zYwrk, zgYwrk);
				zgYwrk.setLxbh("01");
				zgYwrk.setCjId(ywrk.getCreateId());
				zgYwrk.setCjName(ywrk.getCreateName());
				zgYwrk.setMenuId(ywrk.getMenuId());
				cjYwrk(zgYwrk);
			}
		}
		
		
		//处理商品明细
		Set<TYwrkDet> tDets = new HashSet<TYwrkDet>();
		ArrayList<YwrkDet> ywrkDets = JSON.parseObject(ywrk.getDatagrid(), new TypeReference<ArrayList<YwrkDet>>(){});
		for(YwrkDet ywrkDet : ywrkDets){
			TYwrkDet tDet = new TYwrkDet();
			BeanUtils.copyProperties(ywrkDet, tDet);
			if(tDet.getThsl() == null || tDet.getThsl().compareTo(BigDecimal.ZERO) == 0){
				tDet.setThsl(Constant.BD_ZERO);
				tDet.setCthsl(Constant.BD_ZERO);
			}else{
				if(ywrkDet.getCjldwId() != null && ywrkDet.getCjldwId() != "" && ywrkDet.getCjldwId().trim().length() != 0){
					tDet.setCthsl(tDet.getThsl().divide(tDet.getZhxs(), 3, BigDecimal.ROUND_HALF_UP));
				} else {
					tDet.setCthsl(Constant.BD_ZERO);
				}
			}
			
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
			YwzzServiceImpl.updateYwzzSl(sp, dep, ck, ywrkDet.getZdwsl(), tDet.getCdwsl(), ywrkDet.getSpje(), null, null, Constant.UPDATE_RK, ywzzDao);
			tDet.setDwcb(YwzzServiceImpl.getDwcb(ywrk.getBmbh(), ywrkDet.getSpbh(), ywzzDao));
			tDets.add(tDet);
			tDet.setTYwrk(tYwrk);
			
		}
		tYwrk.setTYwrkDets(tDets);
		
		String ywrklsh = LshServiceImpl.updateLsh(ywrk.getBmbh(), ywrk.getLxbh(), lshDao);
		tYwrk.setYwrklsh(ywrklsh);
		tYwrk.setCreateTime(new Date());

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
		
		if(zCgjhDets != null && zCgjhDets.size() > 0){
			tYwrk.setTCgjhs(zCgjhDets);
		}
		if(zKfrks != null && zKfrks.size() > 0){
			for(TKfrk k : zKfrks){
				TKfrk tK = kfrkDao.load(TKfrk.class, k.getKfrklsh());
				tK.setTYwrk(tYwrk);
			}
		}
		
		ywrkDao.save(tYwrk);		
		
		if(zXsths != null && zXsths.size() > 0){
			for(TYwrkDet tD : tYwrk.getTYwrkDets()){
				if(zXsths.containsKey(tD.getSpbh())){
					for(TXsth tX : zXsths.get(tD.getSpbh())){
						TXsth tXs = xsthDao.load(TXsth.class, tX.getXsthlsh());
						Set<TYwrkDet> tts = new HashSet<TYwrkDet>();
						tts.add(tD);
						tXs.setTYwrks(tts);
					}
				}
			}
		}

		if(zgYwrks != null){
			for(TYwrk zz : zgYwrks){ 
				zz.setBeYwrklsh(ywrklsh);
			}
		}

		if (Constant.RKLX_ZS.equals(ywrk.getRklxId())) {
			YfzzServiceImpl.updateYfzzJe(dep, new Gys(ywrk.getGysbh(), ywrk.getGysmc()), ywrk.getHjje(), Constant.UPDATE_YF_RK, yfzzDao);
		}

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
		TYwrkDet tDet = null;
		Sp sp = null;
		for(TYwrkDet yTDet : yTYwrkDets){
			tDet = new TYwrkDet();
			BeanUtils.copyProperties(yTDet, tDet, new String[]{"id", "TXsths"});
			sp = new Sp();
			BeanUtils.copyProperties(yTDet, sp);
			tDet.setZdwsl(yTDet.getZdwsl().negate());
			if(yTDet.getCdwsl() != null){
				tDet.setCdwsl(yTDet.getCdwsl().negate());
			}
			tDet.setSpje(yTDet.getSpje().negate());
			//更新冲减前dwcb
			tDet.setQdwcb(YwzzServiceImpl.getDwcb(tYwrk.getBmbh(), tDet.getSpbh(), ywzzDao));
			
			//更新业务总账
			YwzzServiceImpl.updateYwzzSl(sp, dep, ck, tDet.getZdwsl(), tDet.getCdwsl(), tDet.getSpje(), null, null, Constant.UPDATE_RK, ywzzDao);
			
			//取消由直送入库生成的提货单关联
			if(yTDet.getTXsths() != null && yTDet.getTXsths().size() > 0){
				for(TXsth tX : yTDet.getTXsths()){
					TXsth ttX = xsthDao.load(TXsth.class, tX.getXsthlsh());
					ttX.setTYwrks(null);
				}
			}
			
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

		if(ywrk.getBmbh().equals("05")) {
			String ckSql = "select cks from v_zy_cks where createId = ?";
			Map<String, Object> ckParams = new HashMap<String, Object>();
			ckParams.put("0", ywrk.getCreateId());
			Object cks = ywrkDao.getBySQL(ckSql, ckParams);

			if(cks != null){
				hql += " and t.ckId in " + cks.toString();
			}
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
			//hql += " and t.createId = :createId";
			//params.put("createId", ywrk.getCreateId());
			if(ywrk.getSearch() != null && ywrk.getSearch().length() > 0){
				//hql += " and (t.ywrklsh like :search or t.gysbh like :search or t.gysmc like :search or t.bz like :search)";
				//params.put("search", "%" + ywrk.getSearch() + "%");
				hql += " and (" + 
						Util.getQueryWhere(ywrk.getSearch(), new String[]{"t.ywrklsh", "t.gysbh", "t.gysmc", "t.bz"}, params)
						+ ")";
			}else{
				hql += " or (t.bmbh = :bmbh and t.rklxId = :rklxId and t.isCj = '0')";
				params.put("rklxId", Constant.RKLX_ZG);
			}
		}
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TYwrk> l = ywrkDao.find(hql, params, ywrk.getPage(), ywrk.getRows());
		List<Ywrk> nl = new ArrayList<Ywrk>();
		Ywrk c = null;
		Set<TKfrk> tKfrks = null;
		Set<TCgjhDet> tCgjhs = null;
		for(TYwrk t : l){
			c = new Ywrk();
			BeanUtils.copyProperties(t, c);
			tKfrks = t.getTKfrks();
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
			tCgjhs = t.getTCgjhs();
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
		YwrkDet c = null;
		for(TYwrkDet t : l){
			c = new YwrkDet();
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
			//hql += " and (t.TYwrk.ywrklsh like :search or t.TYwrk.gysbh like :search or t.TYwrk.gysmc like :search or t.TYwrk.bz like :search)"; 
			//params.put("search", "%" + ywrk.getSearch() + "%");
			hql += " and (" + 
					Util.getQueryWhere(ywrk.getSearch(), new String[]{"t.TYwrk.ywrklsh", "t.TYwrk.gysbh", "t.TYwrk.gysmc", "t.TYwrk.bz"}, params)
					+ ")";
			
		}
		
		//在销售提货中查看
		if(ywrk.getFromOther() != null){
			hql += " and t.TYwrk.isZs = '1' and t.TYwrk.isCj = '0' and t.zdwsl > t.thsl";
		}
		
		String countHql = "select count(*) " + hql;
		hql += " order by t.TYwrk.createTime desc ";
		
		List<TYwrkDet> l = detDao.find(hql, params);
		List<Ywrk> nl = new ArrayList<Ywrk>();
		Ywrk c = null;
		for(TYwrkDet t : l){
			//在销售提货流程中，由直送计划生成的入库不显示
			if("fromXsth".equals(ywrk.getFromOther()) && t.getTYwrk().getTCgjhs().size() == 0){
				c = new Ywrk();
				BeanUtils.copyProperties(t, c);
				c.setYwrklsh(t.getTYwrk().getYwrklsh());
				c.setCreateTime(t.getTYwrk().getCreateTime());
				c.setCreateName(t.getTYwrk().getCreateName());
				c.setGysbh(t.getTYwrk().getGysbh());
				c.setGysmc(t.getTYwrk().getGysmc());
				c.setCkId(t.getTYwrk().getCkId());
				c.setCkmc(t.getTYwrk().getCkmc());
				c.setRklxId(t.getTYwrk().getRklxId());
				c.setRklxmc(t.getTYwrk().getRklxmc());
				
				nl.add(c);
			}
		}
		
		datagrid.setRows(nl);
		datagrid.setTotal(ywrkDao.count(countHql, params));
		return datagrid;
	}

	@Override
	public DataGrid ywrkmx(Ywrk ywrk) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TYwrkDet t where t.TYwrk.bmbh = :bmbh and t.TYwrk.createTime > :createTime and t.TYwrk.rklxId = '01' and t.TYwrk.isCj = '0'";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", ywrk.getBmbh());
		if(ywrk.getCreateTime() != null){
			params.put("createTime", ywrk.getCreateTime());
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		if(ywrk.getSearch() != null){
			//hql += " and (t.TYwrk.ywrklsh like :search or t.TYwrk.gysbh like :search or t.TYwrk.gysmc like :search or t.TYwrk.bz like :search)";
			//params.put("search", "%" + ywrk.getSearch() + "%");
			hql += " and (" +
					Util.getQueryWhere(ywrk.getSearch(), new String[]{"t.TYwrk.ywrklsh", "t.TYwrk.gysbh", "t.TYwrk.gysmc", "t.TYwrk.bz"}, params)
					+ ")";

		}

		String countHql = "select count(*) " + hql;
		hql += " order by t.TYwrk.createTime desc ";

		List<TYwrkDet> l = detDao.find(hql, params);
		List<Ywrk> nl = new ArrayList<Ywrk>();
		Ywrk c = null;
		for(TYwrkDet t : l){
			c = new Ywrk();
			BeanUtils.copyProperties(t, c);
			c.setYwrklsh(t.getTYwrk().getYwrklsh());
			c.setCreateTime(t.getTYwrk().getCreateTime());
			c.setGysmc(t.getTYwrk().getGysmc());
//			if (t.getSpbh().substring(1, 2).equals("4") && t.getCjldwId().equals("07")){
//				c.setZjldwmc(t.getCjldwmc());
//				c.setZdwsl(t.getCdwsl());
//				c.setZdwdj(t.getCdwdj());
//			}

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
		//String cgjhlsh = "";
		
		TYwrk tYwrk = ywrkDao.load(TYwrk.class, ywrk.getYwrklsh());
		for (TKfrk y : tYwrk.getTKfrks()) {
			kfrklsh += y.getKfrklsh() + ",";
		}
		if(tYwrk.getTCgjhs() != null && tYwrk.getTCgjhs().size() > 0){
			for (TCgjhDet c : tYwrk.getTCgjhs()) {
				if(kfrklsh.indexOf(c.getTCgjh().getCgjhlsh()) < 0){
					kfrklsh += c.getTCgjh().getCgjhlsh() + ",";
				}
			}
		}
		List<YwrkDet> nl = new ArrayList<YwrkDet>();
		YwrkDet ywrkDet = null;
		for (TYwrkDet yd : tYwrk.getTYwrkDets()) {
			ywrkDet = new YwrkDet();
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
	public DataGrid printKfrk(Ywrk ywrk) {
		DataGrid datagrid = new DataGrid();
		TYwrk tYwrk = ywrkDao.load(TYwrk.class, ywrk.getYwrklsh());
				
		List<KfrkDet> nl = new ArrayList<KfrkDet>();
		BigDecimal hj = Constant.BD_ZERO;
		KfrkDet kfrkDet = null;
		for (TYwrkDet yd : tYwrk.getTYwrkDets()) {
			kfrkDet = new KfrkDet();
			BeanUtils.copyProperties(yd, kfrkDet);
			nl.add(kfrkDet);
			hj = hj.add(yd.getCdwsl());
		}
		int num = nl.size();
		if (num < Constant.REPORT_NUMBER) {
			for (int i = 0; i < (Constant.REPORT_NUMBER - num); i++) {
				nl.add(new KfrkDet());
			}
		}
		//Kfrk kfrk = new Kfrk();
		//BeanUtils.copyProperties(yk, kfrk);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "库   房   入   库   单");
		map.put("kfrklsh", ywrk.getYwrklsh());
		map.put("bmmc", tYwrk.getBmmc());
		map.put("printName", ywrk.getCreateName());
		map.put("createTime", DateUtil.dateToString(tYwrk.getCreateTime(), DateUtil.DATETIME_NOSECOND_PATTERN));
		map.put("printTime", DateUtil.dateToString(new Date()));
		map.put("gysbh", tYwrk.getGysbh());
		map.put("gysmc", tYwrk.getGysmc());
		map.put("ckmc", tYwrk.getCkmc());
		map.put("hj", hj);
		map.put("bz", tYwrk.getBz());
		
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
	public DataGrid changeYwrk(Ywrk ywrk) {
		DataGrid dg = new DataGrid();
		String sql = "select spbh, sum(zdwsl) zdwsl, sum(spje) spje, sum(thsl) thsl, sum(cdwsl) cdwsl, sum(cthsl) cthsl from t_ywrk_det t"
				+ " where t.ywrklsh in (" + ywrk.getYwrklshs() + ")"
				+ " group by t.spbh";
		//Map<String, Object> params = new HashMap<String, Object>();
		//params.put("0", "(" + ywrk.getYwrklshs() + ")");
		
		List<Object[]> l = detDao.findBySQL(sql);
		List<YwrkDet> nl = new ArrayList<YwrkDet>();
		
		for(Object[] os : l){
			String spbh = (String)os[0];
			BigDecimal zdwsl = new BigDecimal(os[1].toString());
			BigDecimal spje = new BigDecimal(os[2].toString());
			BigDecimal thsl = new BigDecimal(os[3].toString());
			BigDecimal cdwsl = new BigDecimal(os[4].toString());
			BigDecimal cthsl = new BigDecimal(os[5].toString());
			
			TSp sp = spDao.get(TSp.class, spbh);
			YwrkDet yd = new YwrkDet();
			
			yd.setSpbh(spbh);
			yd.setSpmc(sp.getSpmc());
			yd.setSpcd(sp.getSpcd());
			yd.setSppp(sp.getSppp());
			yd.setSpbz(sp.getSpbz());
			
			yd.setZdwsl(zdwsl);
			yd.setZjldwId(sp.getZjldw().getId());
			yd.setZjldwmc(sp.getZjldw().getJldwmc());
			yd.setZdwdj(spje.divide(zdwsl, 4, BigDecimal.ROUND_HALF_UP));
			if(sp.getCjldw() != null){
				yd.setCjldwId(sp.getCjldw().getId());
				yd.setCjldwmc(sp.getCjldw().getJldwmc());
				yd.setZhxs(sp.getZhxs());
				yd.setCdwsl(cdwsl);
				//yd.setCdwsl(zdwsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
				yd.setCdwdj(yd.getZdwdj().multiply(new BigDecimal(1).add(Constant.SHUILV)).multiply(sp.getZhxs()).setScale(4, BigDecimal.ROUND_HALF_UP));
			}
			yd.setSpje(spje);
			yd.setThsl(thsl);
			yd.setCthsl(cthsl);
			
			nl.add(yd);
		}
		nl.add(new YwrkDet());
		
		dg.setRows(nl);
		return dg;
	}
	
	@Override
	public DataGrid toKfrk(String ywrklsh){
		//单据表头处理
		TYwrk tYwrk = ywrkDao.load(TYwrk.class, ywrklsh);
		Ywrk ywrk = new Ywrk();
		BeanUtils.copyProperties(tYwrk, ywrk);
		//商品明细处理
		String sql = "select spbh, sum(zdwsl) zdwsl, sum(cdwsl) cdwsl from t_ywrk_det t ";
		
		if(ywrklsh != null && ywrklsh.trim().length() > 0){
			sql += "where ywrklsh = " + ywrklsh;
		}
		sql += " group by spbh";
		
		List<Object[]> l = detDao.findBySQL(sql);
		List<YwrkDet> nl = new ArrayList<YwrkDet>();
		
		for(Object[] os : l){
			String spbh = (String)os[0];
			BigDecimal zdwsl = new BigDecimal(os[1].toString());
			BigDecimal cdwsl = new BigDecimal(os[2].toString());
			
			TSp sp = spDao.get(TSp.class, spbh);
			YwrkDet yd = new YwrkDet();
			BeanUtils.copyProperties(sp, yd);
			yd.setZdwsl(zdwsl);
			yd.setZjldwId(sp.getZjldw().getId());
			yd.setZjldwmc(sp.getZjldw().getJldwmc());
			if(sp.getCjldw() != null){
				yd.setCjldwId(sp.getCjldw().getId());
				yd.setCjldwmc(sp.getCjldw().getJldwmc());
				yd.setZhxs(sp.getZhxs());
				//yd.setCdwsl(zdwsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_UP));
				yd.setCdwsl(cdwsl);
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
		YwrkDet ywrkDet = null;
		for(TYwrkDet tYwrkDet : l){
			ywrkDet = new YwrkDet();
			BeanUtils.copyProperties(tYwrkDet, ywrkDet);
			
			nl.add(ywrkDet);
		}
		
		DataGrid dg = new DataGrid();
		dg.setRows(nl);
		return dg;
	}
	
	@Override
	public DataGrid toXsth(Ywrk ywrk){
		String ywrkDetIds= ywrk.getYwrkDetIds();
		//String sql = "select spbh, zdwsl, thsl thsl, cdwsl, cthsl from t_ywrk_det where zdwsl <> thsl";
		
		String sql = "select det.spbh, zdwsl, thsl thsl, cdwsl, cthsl, isnull(xsdj, 0) xsdj, isnull(zz.dwcb, 0) dwcb"
				+ " from v_ywrk det left join t_sp_det sp on sp.depId = det.bmbh and sp.spbh = det.spbh"
				+ " left join t_ywzz zz on det.bmbh = zz.bmbh and det.spbh = zz.spbh and zz.jzsj = convert(char(6), getDATE(), 112) and zz.ckId is null"
				+ " where zdwsl <> thsl";
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(ywrkDetIds != null && ywrkDetIds.trim().length() > 0){
			sql += " and det.id in (" + ywrkDetIds + ")";
		}

		List<Object[]> l = detDao.findBySQL(sql, params);
		
		List<XsthDet> nl = new ArrayList<XsthDet>();
		
		for(Object[] os : l){
			String spbh = (String)os[0];
			BigDecimal zdwrksl = new BigDecimal(os[1].toString());
			BigDecimal zdwthsl = new BigDecimal(os[2].toString());
			BigDecimal cdwrksl = new BigDecimal(os[3].toString());
			BigDecimal xsdj = new BigDecimal(os[5].toString());
			BigDecimal dwcb = new BigDecimal(os[6].toString());
			
			TSp sp = spDao.get(TSp.class, spbh);
			XsthDet xd = new XsthDet();
			BeanUtils.copyProperties(sp, xd);
			xd.setZjldwId(sp.getZjldw().getId());
			xd.setZjldwmc(sp.getZjldw().getJldwmc());
			xd.setKpsl(zdwrksl);
			xd.setThsl(zdwthsl);
			if(sp.getCjldw() != null){
				xd.setCjldwId(sp.getCjldw().getId());
				xd.setCjldwmc(sp.getCjldw().getJldwmc());
				xd.setZhxs(sp.getZhxs());
			}
			xd.setZdwsl(zdwrksl.subtract(zdwthsl));
			if(sp.getCjldw() != null){
				xd.setCdwsl(cdwrksl);
				//xd.setCdwsl(xd.getZdwsl().divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
			}
			if(xsdj.compareTo(BigDecimal.ZERO) != 0){
				xd.setZdwdj(xsdj.multiply(new BigDecimal(1).add(Constant.SHUILV)).setScale(2, BigDecimal.ROUND_HALF_UP));
				xd.setSpje(xd.getZdwdj().multiply(xd.getZdwsl()).setScale(2, BigDecimal.ROUND_HALF_UP));
			}
			xd.setDwcb(dwcb);
			
			nl.add(xd);
		}
		
		nl.add(new XsthDet());
		DataGrid dg = new DataGrid();
		dg.setRows(nl);
		return dg;
		
	}
	
	@Override
	public Ywrk getYwrk(Ywrk ywrk){
		Ywrk y = new Ywrk();
		TYwrk tYwrk = ywrkDao.load(TYwrk.class, ywrk.getYwrklsh());
		BeanUtils.copyProperties(tYwrk, y);
		return y;
	}

	@Override
	public List<Gys> listGysYf(Ywrk ywrk) {
		String sql = "select ywrk.gysbh, ywrk.gysmc, isnull(dt.sxzq, 0) sxzq, isnull(dt.sxje, 0) sxje, isnull(yf.qcje, 0) + isnull(yf.rkje, 0) - isnull(yf.fkje, 0) yfje " +
				" from (select distinct bmbh, gysbh, gysmc from t_ywrk where isCj = '0' and rklxId = ? and bmbh = ? and createTime > ?) ywrk" +
				" left join t_gys_det dt on ywrk.bmbh = dt.depId and ywrk.gysbh = dt.gysbh" +
				" left join t_yfzz yf on ywrk.bmbh = yf.bmbh and ywrk.gysbh = yf.gysbh and jzsj = CONVERT(char(6), getDate(), 112)";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", Constant.RKLX_ZS);
		params.put("1", ywrk.getBmbh());

		Calendar cal = Calendar.getInstance();
//		cal.set(cal.get(Calendar.YEAR) - 1, cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
		cal.add(Calendar.YEAR, -1);
		params.put("2", cal.getTime());

		List<Object[]> list = ywrkDao.findBySQL(sql, params);
		List<Gys> gyses = new ArrayList<Gys>();

		if (list.size() == 0) {
			return gyses;
		}

		Gys gys = null;
		for (Object[] y : list) {
			gys = new Gys();
			gys.setGysbh(y[0].toString());
			gys.setGysmc(y[1].toString());
			gys.setSxzq(Integer.parseInt(y[2].toString()));
			gys.setSxje(new BigDecimal(y[3].toString()));
			gys.setYfje(new BigDecimal(y[4].toString()));
			gyses.add(gys);
		}
		return gyses;
	}

	@Override
	public List<Ywrk> listYwrkNoFk(Ywrk ywrk) {
		String hql = "from TYwrk where isCj = '0' and rklxId = :rklxId and bmbh = :bmbh and gysbh = :gysbh and hjje <> fkje order by createTime desc";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rklxId", Constant.RKLX_ZS);
		params.put("bmbh", ywrk.getBmbh());
		params.put("gysbh", ywrk.getGysbh());
		List<TYwrk> list = ywrkDao.find(hql, params);
		List<Ywrk> ywrks = new ArrayList<Ywrk>();

		if (list.size() == 0) {
			return ywrks;
		}

		Ywrk y = null;
		for (TYwrk tYwrk : list) {
			y = new Ywrk();
			y.setCreateTime(tYwrk.getCreateTime());
			y.setYwrklsh(tYwrk.getYwrklsh());
			y.setHjje(tYwrk.getHjje());
			ywrks.add(y);
		}

		return ywrks;
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
	public void setXsthDao(BaseDaoI<TXsth> xsthDao) {
		this.xsthDao = xsthDao;
	}

	@Autowired
	public void setYfzzDao(BaseDaoI<TYfzz> yfzzDao) {
		this.yfzzDao = yfzzDao;
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
