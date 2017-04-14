package lnyswz.jxc.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.ProBean;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Ck;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.KfrkDet;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.bean.XsthDet;
import lnyswz.jxc.bean.Fyd;
import lnyswz.jxc.bean.FydDet;
import lnyswz.jxc.model.TCgjhDet;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TKfrk;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TSpDet;
import lnyswz.jxc.model.TXskp;
import lnyswz.jxc.model.TXsth;
import lnyswz.jxc.model.TFyd;
import lnyswz.jxc.model.TFydDet;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.service.FydServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Util;

/**
 * 付印单实现类
 * @author 王文阳
 *
 */
@Service("fydService")
public class FydServiceImpl implements FydServiceI {
	private Logger logger = Logger.getLogger(FydServiceImpl.class);
	private BaseDaoI<TFyd> fydDao;
	private BaseDaoI<TFydDet> detDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TSpDet> spDetDao;
	private BaseDaoI<TDepartment> depDao;


	private BaseDaoI<TSp> spDao;
	private BaseDaoI<TOperalog> operalogDao;
	
	@Override
	public DataGrid datagrid(Fyd fyd) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TFyd t where t.bmbh = :bmbh and t.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", fyd.getBmbh());
		if(fyd.getCreateTime() != null){
			params.put("createTime", fyd.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		
		
		if(fyd.getFromOther() != null){
			hql += " and t.isCj = '0'";
			if(Constant.YWRK_FROM_YWBT.equals(fyd.getFromOther())){
				hql += " and t.rklxId = :rklxId and t.TYwbt is null";
				params.put("rklxId", Constant.RKLX_ZS);
			}else{
				hql += " and t.isZs = '0' and t.TKfrks is empty";
			}
		}else{
			if(fyd.getSearch() != null && fyd.getSearch().length() > 0){
				//hql += " and (t.fydlsh like :search or t.gysbh like :search or t.gysmc like :search or t.bz like :search)";
				//params.put("search", "%" + fyd.getSearch() + "%");
				hql += " and (" + 
						Util.getQueryWhere(fyd.getSearch(), new String[]{"t.fydlsh", "t.gysbh", "t.gysmc", "t.bz"}, params)
						+ ")";
			}else{
				hql += " or (t.bmbh = :bmbh and t.rklxId = :rklxId and t.isCj = '0')";
				params.put("rklxId", Constant.RKLX_ZG);
			}
		}
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TFyd> l = fydDao.find(hql, params, fyd.getPage(), fyd.getRows());
		List<Fyd> nl = new ArrayList<Fyd>();
		Fyd c = null;
		Set<TKfrk> tKfrks = null;
		Set<TCgjhDet> tCgjhs = null;
		for(TFyd t : l){
			c = new Fyd();
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
		datagrid.setTotal(fydDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(String fydlsh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TFydDet t where t.TFyd.fydlsh = :fydlsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fydlsh", fydlsh);
		List<TFydDet> l = detDao.find(hql, params);
		List<FydDet> nl = new ArrayList<FydDet>();
		FydDet c = null;
		for(TFydDet t : l){
			c = new FydDet();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid datagridDet(Fyd fyd) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TFydDet t where t.TFyd.bmbh = :bmbh and t.TFyd.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", fyd.getBmbh());
		if(fyd.getCreateTime() != null){
			params.put("createTime", fyd.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		if(fyd.getSearch() != null){
			//hql += " and (t.TFyd.fydlsh like :search or t.TFyd.gysbh like :search or t.TFyd.gysmc like :search or t.TFyd.bz like :search)"; 
			//params.put("search", "%" + fyd.getSearch() + "%");
			hql += " and (" + 
					Util.getQueryWhere(fyd.getSearch(), new String[]{"t.TFyd.fydlsh", "t.TFyd.gysbh", "t.TFyd.gysmc", "t.TFyd.bz"}, params)
					+ ")";
			
		}
		
		//在销售提货中查看
		if(fyd.getFromOther() != null){
			hql += " and t.TFyd.isZs = '1' and t.TFyd.isCj = '0' and t.zdwsl > t.thsl";
		}
		
		String countHql = "select count(*) " + hql;
		hql += " order by t.TFyd.createTime desc ";
		
		List<TFydDet> l = detDao.find(hql, params);
		List<Fyd> nl = new ArrayList<Fyd>();
		Fyd c = null;
		for(TFydDet t : l){
			//在销售提货流程中，由直送计划生成的入库不显示
			if("fromXsth".equals(fyd.getFromOther()) && t.getTFyd().getTCgjhs().size() == 0){
				c = new Fyd();
				BeanUtils.copyProperties(t, c);
				c.setFydlsh(t.getTFyd().getFydlsh());
				c.setCreateTime(t.getTFyd().getCreateTime());
				c.setCreateName(t.getTFyd().getCreateName());
				c.setGysbh(t.getTFyd().getGysbh());
				c.setGysmc(t.getTFyd().getGysmc());
				c.setCkId(t.getTFyd().getCkId());
				c.setCkmc(t.getTFyd().getCkmc());
				c.setRklxId(t.getTFyd().getRklxId());
				c.setRklxmc(t.getTFyd().getRklxmc());
				
				nl.add(c);
			}
		}
		
		datagrid.setRows(nl);
		datagrid.setTotal(fydDao.count(countHql, params));
		return datagrid;
	}

	
	@Override
	public DataGrid changeFyd(Fyd fyd) {
		DataGrid dg = new DataGrid();
		String sql = "select spbh, sum(zdwsl) zdwsl, sum(spje) spje, sum(thsl) thsl, sum(cdwsl) cdwsl, sum(cthsl) cthsl from t_fyd_det t"
				+ " where t.fydlsh in (" + fyd.getFydlshs() + ")"
				+ " group by t.spbh";
		//Map<String, Object> params = new HashMap<String, Object>();
		//params.put("0", "(" + fyd.getFydlshs() + ")");
		
		List<Object[]> l = detDao.findBySQL(sql);
		List<FydDet> nl = new ArrayList<FydDet>();
		
		for(Object[] os : l){
			String spbh = (String)os[0];
			BigDecimal zdwsl = new BigDecimal(os[1].toString());
			BigDecimal spje = new BigDecimal(os[2].toString());
			BigDecimal thsl = new BigDecimal(os[3].toString());
			BigDecimal cdwsl = new BigDecimal(os[4].toString());
			BigDecimal cthsl = new BigDecimal(os[5].toString());
			
			TSp sp = spDao.get(TSp.class, spbh);
			FydDet yd = new FydDet();
			
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
		nl.add(new FydDet());
		
		dg.setRows(nl);
		return dg;
	}
	
	@Autowired
	public void setFydDao(BaseDaoI<TFyd> fydDao) {
		this.fydDao = fydDao;
	}

	@Autowired
	public void setDetDao(BaseDaoI<TFydDet> detDao) {
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
