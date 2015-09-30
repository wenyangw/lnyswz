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
import lnyswz.jxc.bean.Ywhs;
import lnyswz.jxc.bean.YwhsDet;
import lnyswz.jxc.bean.Ywpd;
import lnyswz.jxc.bean.YwpdDet;
import lnyswz.jxc.model.TCgjh;
import lnyswz.jxc.model.TCgjhDet;
import lnyswz.jxc.model.TCk;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TKfpd;
import lnyswz.jxc.model.TKfrk;
import lnyswz.jxc.model.THw;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TRklx;
import lnyswz.jxc.model.TSpDet;
import lnyswz.jxc.model.TYwhs;
import lnyswz.jxc.model.TYwhsDet;
import lnyswz.jxc.model.TYwpd;
import lnyswz.jxc.model.TYwpdDet;
import lnyswz.jxc.model.TCgxq;
import lnyswz.jxc.model.TGys;
import lnyswz.jxc.model.TJsfs;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.model.TRole;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.model.TUser;
import lnyswz.jxc.service.YwpdServiceI;
import lnyswz.jxc.util.Constant;

/**
 * 业务盘点实现类
 * @author 王文阳
 *
 */
@Service("ywpdService")
public class YwpdServiceImpl implements YwpdServiceI {
	private Logger logger = Logger.getLogger(YwpdServiceImpl.class);
	private BaseDaoI<TYwpd> ywpdDao;
	private BaseDaoI<TYwpdDet> detDao;
	private BaseDaoI<TYwzz> ywzzDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TSp> spDao;
	private BaseDaoI<TSpDet> spDetDao;
	private BaseDaoI<TDepartment> depDao;

	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public Ywpd save(Ywpd ywpd) {
		String lsh = LshServiceImpl.updateLsh(ywpd.getBmbh(), ywpd.getLxbh(), lshDao);
		TYwpd tYwpd = new TYwpd();
		BeanUtils.copyProperties(ywpd, tYwpd);
		tYwpd.setCreateTime(new Date());
		tYwpd.setYwpdlsh(lsh);
		tYwpd.setBmmc(depDao.load(TDepartment.class, ywpd.getBmbh()).getDepName());

		tYwpd.setIsCj("0");
		
		Department dep = new Department();
		dep.setId(ywpd.getBmbh());
		dep.setDepName(tYwpd.getBmmc());
		
		Ck ck = new Ck();
		ck.setId(ywpd.getCkId());
		ck.setCkmc(tYwpd.getCkmc());
		
		//处理商品明细
		Set<TYwpdDet> tDets = new HashSet<TYwpdDet>();
		ArrayList<YwpdDet> ywpdDets = JSON.parseObject(ywpd.getDatagrid(), new TypeReference<ArrayList<YwpdDet>>(){});
		for(YwpdDet ywpdDet : ywpdDets){
			TYwpdDet tDet = new TYwpdDet();
			BeanUtils.copyProperties(ywpdDet, tDet);
			
			if("".equals(ywpdDet.getCjldwId()) || null == ywpdDet.getZhxs()){
				tDet.setCdwdj(Constant.BD_ZERO);
				tDet.setCdwsl(Constant.BD_ZERO);
				tDet.setZhxs(Constant.BD_ZERO);
			}else{
				tDet.setCdwdj(ywpdDet.getZdwdj().multiply(ywpdDet.getZhxs()).multiply(Constant.SHUILV));
				tDet.setCdwsl(ywpdDet.getCdwsl());
//				if(ywpdDet.getZhxs().compareTo(Constant.BD_ZERO) == 0){
//					tDet.setCdwdj(Constant.BD_ZERO);
//					tDet.setCdwsl(Constant.BD_ZERO);
//				}else{
//					tDet.setCdwdj(ywpdDet.getZdwdj().multiply(ywpdDet.getZhxs()).multiply(Constant.SHUILV));
//					tDet.setCdwsl(ywpdDet.getZdwsl().divide(ywpdDet.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
//				}
			}
			Sp sp = new Sp();
			BeanUtils.copyProperties(ywpdDet, sp);

			tDet.setQdwcb(YwzzServiceImpl.getDwcb(ywpd.getBmbh(), ywpdDet.getSpbh(), ywzzDao));
			tDet.setTYwpd(tYwpd);

			//更新业务总账
			YwzzServiceImpl.updateYwzzSl(sp, dep, ck, ywpdDet.getZdwsl(), tDet.getCdwsl(), ywpdDet.getSpje(), null, null, Constant.UPDATE_RK, ywzzDao);
			tDet.setDwcb(YwzzServiceImpl.getDwcb(ywpd.getBmbh(), ywpdDet.getSpbh(), ywzzDao));
			tDets.add(tDet);
			
		}
		tYwpd.setTYwpdDets(tDets);
		ywpdDao.save(tYwpd);		
		
		OperalogServiceImpl.addOperalog(ywpd.getCreateId(), ywpd.getBmbh(), ywpd.getMenuId(), tYwpd.getYwpdlsh(), 
				"生成业务盘点单", operalogDao);
		
		Ywpd rYwpd = new Ywpd();
		rYwpd.setYwpdlsh(lsh);
		return rYwpd;
	}
	
	@Override
	public Ywpd cjYwpd(Ywpd ywpd) {
		Date now = new Date();
		//更新新单据
		String lsh = LshServiceImpl.updateLsh(ywpd.getBmbh(), ywpd.getLxbh(), lshDao);
		//更新原单据信息
		TYwpd yTYwpd = ywpdDao.get(TYwpd.class, ywpd.getYwpdlsh());
		//新增冲减单据信息
		TYwpd tYwpd = new TYwpd();
		BeanUtils.copyProperties(yTYwpd, tYwpd);
		
		yTYwpd.setCjId(ywpd.getCjId());
		yTYwpd.setCjTime(now);
		yTYwpd.setCjName(ywpd.getCjName());
		yTYwpd.setIsCj("1");


		tYwpd.setYwpdlsh(lsh);
		tYwpd.setCjYwpdlsh(yTYwpd.getYwpdlsh());
		tYwpd.setCreateTime(now);
		tYwpd.setCreateId(ywpd.getCjId());
		tYwpd.setCreateName(ywpd.getCjName());
		tYwpd.setIsCj("1");
		tYwpd.setCjTime(now);
		tYwpd.setCjName(ywpd.getCjName());
		tYwpd.setHjje(tYwpd.getHjje().negate());
		tYwpd.setBz(ywpd.getBz());
		
		Department dep = new Department();
		dep.setId(ywpd.getBmbh());
		dep.setDepName(depDao.load(TDepartment.class, ywpd.getBmbh()).getDepName());
		Ck ck = new Ck();
		ck.setId(tYwpd.getCkId());
		ck.setCkmc(tYwpd.getCkmc());
		
		Set<TYwpdDet> yTYwpdDets = yTYwpd.getTYwpdDets();
		Set<TYwpdDet> tDets = new HashSet<TYwpdDet>();
		for(TYwpdDet yTDet : yTYwpdDets){
			TYwpdDet tDet = new TYwpdDet();
			BeanUtils.copyProperties(yTDet, tDet, new String[]{"id"});
			Sp sp = new Sp();
			BeanUtils.copyProperties(yTDet, sp);
			tDet.setZdwsl(yTDet.getZdwsl().negate());
			if(yTDet.getCdwsl() != null){
				tDet.setCdwsl(yTDet.getCdwsl().negate());
			}
			tDet.setSpje(yTDet.getSpje().negate());
			//更新冲减前dwcb
			tDet.setQdwcb(YwzzServiceImpl.getDwcb(tYwpd.getBmbh(), tDet.getSpbh(), ywzzDao));
			
			//更新业务总账
			YwzzServiceImpl.updateYwzzSl(sp, dep, ck, tDet.getZdwsl(), tDet.getCdwsl(), tDet.getSpje(), null, null, Constant.UPDATE_RK, ywzzDao);
			
			//更新冲减后dwcb
			tDet.setDwcb(YwzzServiceImpl.getDwcb(tYwpd.getBmbh(), tDet.getSpbh(), ywzzDao));
			tDet.setTYwpd(tYwpd);
			
			tDets.add(tDet);
			
		}
		tYwpd.setTYwpdDets(tDets);
		ywpdDao.save(tYwpd);
		
		OperalogServiceImpl.addOperalog(ywpd.getCjId(), ywpd.getBmbh(), ywpd.getMenuId(), tYwpd.getCjYwpdlsh() + "/" + tYwpd.getYwpdlsh(), 
				"冲减业务入库单", operalogDao);
		
		Ywpd rYwpd = new Ywpd();
		rYwpd.setYwpdlsh(lsh);
		return rYwpd;
	}
	
	@Override
	public DataGrid printYwpd(Ywpd ywpd) {
		DataGrid datagrid = new DataGrid();
		TYwpd tYwpd = ywpdDao.load(TYwpd.class, ywpd.getYwpdlsh());
		
		List<YwpdDet> nl = new ArrayList<YwpdDet>();
		for (TYwpdDet yd : tYwpd.getTYwpdDets()) {
			YwpdDet ywpdDet = new YwpdDet();
			BeanUtils.copyProperties(yd, ywpdDet);
			nl.add(ywpdDet);
		}
		
		int num = nl.size();
		if (num < Constant.REPORT_NUMBER) {
			for (int i = 0; i < (Constant.REPORT_NUMBER - num); i++) {
				nl.add(new YwpdDet());
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String lsh = "";
		if (tYwpd.getCjYwpdlsh() != null) {
			lsh += tYwpd.getCjYwpdlsh() + " - ";
		}
		lsh += tYwpd.getYwpdlsh();
		map.put("title", Constant.YWPD_TITLE.get(tYwpd.getPdlxId()));
		map.put("bmmc", tYwpd.getBmmc());
		map.put("createTime", DateUtil.dateToString(tYwpd.getCreateTime(), DateUtil.DATETIME_NOSECOND_PATTERN));
		map.put("ywpdlsh", lsh);
		map.put("hjje", tYwpd.getHjje());
		map.put("bz", tYwpd.getBz());
		map.put("ckmc", tYwpd.getCkmc());
		map.put("printName", ywpd.getCreateName());
		map.put("printTime", DateUtil.dateToString(new Date()));
		
		datagrid.setObj(map);
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid datagrid(Ywpd ywpd) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TYwpd t where t.bmbh = :bmbh and t.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", ywpd.getBmbh());
		if(ywpd.getCreateTime() != null){
			params.put("createTime", ywpd.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		if(ywpd.getFromOther() != null){
			hql += " and t.isCj = '0' and t.TKfpd = null";
		}
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TYwpd> l = ywpdDao.find(hql, params, ywpd.getPage(), ywpd.getRows());
		List<Ywpd> nl = new ArrayList<Ywpd>();
		for(TYwpd t : l){
			Ywpd c = new Ywpd();
			BeanUtils.copyProperties(t, c);
			if(t.getTKfpd() != null){
				c.setKfpdlsh(t.getTKfpd().getKfpdlsh());
			}
			nl.add(c);
		}
		datagrid.setTotal(ywpdDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(String ywpdlsh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TYwpdDet t where t.TYwpd.ywpdlsh = :ywpdlsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ywpdlsh", ywpdlsh);
		List<TYwpdDet> l = detDao.find(hql, params);
		List<YwpdDet> nl = new ArrayList<YwpdDet>();
		for(TYwpdDet t : l){
			YwpdDet c = new YwpdDet();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}
		
	@Override
	public DataGrid getSpkc(Ywpd ywpd) {
		DataGrid dg = new DataGrid();
		List<ProBean> lists = new ArrayList<ProBean>();
		
		List<ProBean> yw = YwzzServiceImpl.getZzsl(ywpd.getBmbh(), ywpd.getSpbh(), null, ywzzDao);
		if(yw != null){
			lists.addAll(yw);
		}
		
		String hql = "from TSpDet t where t.TDepartment.id = :depId and t.TSp.spbh = :spbh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", ywpd.getBmbh());
		params.put("spbh", ywpd.getSpbh());
		
		TSpDet tSpDet = spDetDao.get(hql, params);
		if(tSpDet != null && tSpDet.getLastRkdj().compareTo(Constant.BD_ZERO) != 0){
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
	public DataGrid toKfpd(String ywpdlsh){
		//单据表头处理
		TYwpd tYwpd = ywpdDao.load(TYwpd.class, ywpdlsh);
		Ywpd ywpd = new Ywpd();
		BeanUtils.copyProperties(tYwpd, ywpd);
		//商品明细处理
		String sql = "select spbh, sum(zdwsl) zdwsl, sum(cdwsl) cdwsl from t_ywpd_det t ";
		
		if(ywpdlsh != null && ywpdlsh.trim().length() > 0){
			sql += "where ywpdlsh = " + ywpdlsh;
		}
		sql += " group by spbh";
		
		List<Object[]> l = detDao.findBySQL(sql);
		List<YwpdDet> nl = new ArrayList<YwpdDet>();
		
		for(Object[] os : l){
			String spbh = (String)os[0];
			BigDecimal zdwsl = new BigDecimal(os[1].toString());
			BigDecimal cdwsl = new BigDecimal(os[2].toString());
			
			TSp sp = spDao.get(TSp.class, spbh);
			YwpdDet yd = new YwpdDet();
			yd.setSpbh(spbh);
			yd.setSpmc(sp.getSpmc());
			yd.setSpcd(sp.getSpcd());
			yd.setSppp(sp.getSppp());
			yd.setSpbz(sp.getSpbz());
			yd.setZdwsl(zdwsl);
			yd.setZjldwmc(sp.getZjldw().getJldwmc());
			if(sp.getCjldw() != null){
				yd.setCjldwId(sp.getCjldw().getId());
				yd.setCjldwmc(sp.getCjldw().getJldwmc());
				yd.setZhxs(sp.getZhxs());
				yd.setCdwsl(cdwsl);
//				yd.setCdwsl(zdwsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
			}
			nl.add(yd);
		}
		
		DataGrid dg = new DataGrid();
		dg.setObj(ywpd);
		dg.setRows(nl);
		return dg;
	}
	
	@Autowired
	public void setYwpdDao(BaseDaoI<TYwpd> ywpdDao) {
		this.ywpdDao = ywpdDao;
	}

	@Autowired
	public void setYwzzDao(BaseDaoI<TYwzz> ywzzDao) {
		this.ywzzDao = ywzzDao;
	}

	@Autowired
	public void setDetDao(BaseDaoI<TYwpdDet> detDao) {
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
