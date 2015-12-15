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
import lnyswz.jxc.bean.Cgxq;
import lnyswz.jxc.bean.CgxqDet;
import lnyswz.jxc.bean.Ywrk;
import lnyswz.jxc.model.TCgxq;
import lnyswz.jxc.model.TCgxqDet;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.model.TUser;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.service.CgxqServiceI;
import lnyswz.jxc.util.Constant;

/**
 * 采购需求实现类
 * @author 王文阳
 *
 */
@Service("cgxqService")
public class CgxqServiceImpl implements CgxqServiceI {
	private Logger logger = Logger.getLogger(CgxqServiceImpl.class);
	private BaseDaoI<TCgxq> cgxqDao;
	private BaseDaoI<TCgxqDet> detDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TUser> userDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TSp> spDao;
	private BaseDaoI<TYwzz> ywzzDao;
	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public Cgxq save(Cgxq cgxq) {
		TCgxq tCgxq = new TCgxq();
		BeanUtils.copyProperties(cgxq, tCgxq);
		tCgxq.setCreateTime(new Date());
		String cgxqlsh = LshServiceImpl.updateLsh(cgxq.getBmbh(), cgxq.getLxbh(), lshDao);
		tCgxq.setCgxqlsh(cgxqlsh);
		tCgxq.setBmmc(depDao.load(TDepartment.class, cgxq.getBmbh()).getDepName());
		tCgxq.setIsAudit("0");
		
//		if("1".equals(cgxq.getIsLs())){
//			tCgxq.setJsfsmc(jsfsDao.load(TJsfs.class, cgxq.getJsfsId()).getJsmc());
//		}
		
		Set<TCgxqDet> tDets = new HashSet<TCgxqDet>();
		ArrayList<CgxqDet> cgxqDets = JSON.parseObject(cgxq.getDatagrid(), new TypeReference<ArrayList<CgxqDet>>(){});
		for(CgxqDet cgxqDet : cgxqDets){
			TCgxqDet tDet = new TCgxqDet();
			BeanUtils.copyProperties(cgxqDet, tDet);
			tDet.setIsCancel("0");
			tDet.setIsRefuse("0");
			tDet.setIsComplete("0");
			tDet.setTCgxq(tCgxq);
			if(cgxqDet.getZdwdj() == null){
				tDet.setZdwdj(Constant.BD_ZERO);
				tDet.setCdwdj(Constant.BD_ZERO);
				//tDet.setSpje(Constant.BD_ZERO);
			}
			if(cgxqDet.getZdwxsdj() == null){
				tDet.setZdwxsdj(Constant.BD_ZERO);
			}
			if("".equals(cgxqDet.getCjldwId()) || null == cgxqDet.getCjldwId()){
				tDet.setCdwdj(Constant.BD_ZERO);
				tDet.setCdwsl(Constant.BD_ZERO);
				tDet.setCdwxsdj(Constant.BD_ZERO);
				tDet.setZhxs(Constant.BD_ZERO);
			}else{
				if(cgxqDet.getZhxs().compareTo(Constant.BD_ZERO) == 0){
					tDet.setCdwdj(Constant.BD_ZERO);
					tDet.setCdwsl(Constant.BD_ZERO);
					tDet.setCdwxsdj(Constant.BD_ZERO);
				}
			}
			tDets.add(tDet);
		}
		tCgxq.setTCgxqDets(tDets);
		cgxqDao.save(tCgxq);
		OperalogServiceImpl.addOperalog(cgxq.getCreateId(), cgxq.getBmbh(), cgxq.getMenuId(), 
				tCgxq.getCgxqlsh(), "生成采购需求单", operalogDao);
		
		Cgxq rCgxq = new Cgxq();
		rCgxq.setCgxqlsh(cgxqlsh);
		return rCgxq;
	}
	
	@Override
	public void updateCancel(Cgxq cgxq) {
		TCgxqDet tCgxqDet = detDao.load(TCgxqDet.class, cgxq.getId());
		tCgxqDet.setCancelId(cgxq.getCancelId());
		tCgxqDet.setCancelTime(new Date());
		tCgxqDet.setCancelName(cgxq.getCancelName());
		tCgxqDet.setIsCancel("1");			
		OperalogServiceImpl.addOperalog(cgxq.getCancelId(), cgxq.getBmbh(), cgxq.getMenuId(), 
				tCgxqDet.getTCgxq().getCgxqlsh() + "/" + cgxq.getId(), "取消采购需求记录", operalogDao);
	}
	
	@Override
	public void updateRefuse(Cgxq cgxq) {
		TCgxqDet tCgxqDet = detDao.load(TCgxqDet.class, cgxq.getId());
		tCgxqDet.setRefuseId(cgxq.getRefuseId());
		tCgxqDet.setRefuseTime(new Date());
		tCgxqDet.setRefuseName(cgxq.getRefuseName());
		tCgxqDet.setIsRefuse("1");			
		OperalogServiceImpl.addOperalog(cgxq.getRefuseId(), cgxq.getBmbh(), cgxq.getMenuId(), 
				tCgxqDet.getTCgxq().getCgxqlsh() + "/" + cgxq.getId(), "退回采购需求记录", operalogDao);
	}
	
	@Override
	public void updateComplete(Cgxq cgxq) {
		TCgxqDet tCgxqDet = detDao.load(TCgxqDet.class, cgxq.getId());
		tCgxqDet.setRefuseId(cgxq.getRefuseId());
		tCgxqDet.setRefuseTime(new Date());
		tCgxqDet.setRefuseName(cgxq.getRefuseName());
		tCgxqDet.setIsComplete("1");			
		OperalogServiceImpl.addOperalog(cgxq.getRefuseId(), cgxq.getBmbh(), cgxq.getMenuId(), 
				tCgxqDet.getTCgxq().getCgxqlsh() + "/" + cgxq.getId(), "完成采购需求记录", operalogDao);
	}
	
	@Override
	public DataGrid printCgxq(Cgxq cgxq) {
		DataGrid datagrid = new DataGrid();
		TCgxq tCgxq = cgxqDao.load(TCgxq.class, cgxq.getCgxqlsh());
		BigDecimal hjsl = Constant.BD_ZERO;
		
		String hql = "from TCgxqDet t where t.TCgxq.cgxqlsh = :cgxqlsh and t.isCancel = '0' order by t.spbh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cgxqlsh", cgxq.getCgxqlsh());
		List<TCgxqDet> tCgxqDets = detDao.find(hql, params);
		
		List<CgxqDet> nl = new ArrayList<CgxqDet>();
		for (TCgxqDet yd : tCgxqDets) {
			CgxqDet cgxqDet = new CgxqDet();
			BeanUtils.copyProperties(yd, cgxqDet);
			if (cgxqDet.getSpbh().substring(0, 1).equals("4")){
				cgxqDet.setZdwdj(Constant.BD_ZERO);
			}else{
				cgxqDet.setZdwdj(cgxqDet.getZdwdj().multiply(new BigDecimal("1").add(Constant.SHUILV)));
			}
			hjsl = hjsl.add(yd.getCdwsl());
			nl.add(cgxqDet);
		}
		int num = nl.size();
		if (num < Constant.REPORT_NUMBER) {
			for (int i = 0; i < (Constant.REPORT_NUMBER - num); i++) {
				nl.add(new CgxqDet());
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "采   购   需   求   单");
		//map.put("gsmc", Constant.BMMCS.get(tCgxq.getBmbh()));
		map.put("gysbh", tCgxq.getGysbh());
		map.put("gysmc", tCgxq.getGysmc());
		map.put("khbh", tCgxq.getKhbh());
		map.put("khmc", tCgxq.getKhmc());
		map.put("bmmc", tCgxq.getBmmc());
		map.put("createTime", DateUtil.dateToString(tCgxq.getCreateTime(), DateUtil.DATETIME_NOSECOND_PATTERN));
		map.put("cgxqlsh", tCgxq.getCgxqlsh());
		map.put("isZs", tCgxq.getIsZs().equals("1") ? "是" : "否");
		map.put("isLs", tCgxq.getIsLs().equals("1") ? "是" : "否");
		map.put("hjsl", hjsl);
		map.put("hjje", tCgxq.getHjje());
		map.put("bz", tCgxq.getBz());
		map.put("printName", cgxq.getCreateName());
		map.put("printTime", DateUtil.dateToString(new Date()));
		datagrid.setObj(map);
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid datagrid(Cgxq cgxq) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TCgxqDet t where t.TCgxq.bmbh = :bmbh"; // and t.TCgxq.createTime > :createTime"
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", cgxq.getBmbh());
//		if(cgxq.getCreateTime() != null){
//			params.put("createTime", cgxq.getCreateTime()); 
//		}else{
//			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
//		}
		if(cgxq.getSearch() != null){
			hql += " and (t.TCgxq.cgxqlsh like :search or t.TCgxq.gysmc like :search or t.bz like :search)"; 
			params.put("search", "%" + cgxq.getSearch() + "%");
			
		}
		//采购计划流程只查询未完成的有效数据
		if(cgxq.getFromOther() != null){
			hql += " and t.isCancel = '0' and t.isRefuse = '0' and cgjhlsh is null and needAudit = isAudit and t.isComplete = '0'";
		}else{
			//在当前流程，只有创建者可以查看自己的记录
			TUser tUser = userDao.load(TUser.class, cgxq.getCreateId());
			if(tUser.getTPost().getId().equals(Constant.USER_POSTID)){
				hql += " and t.TCgxq.createId = :createId";
				params.put("createId", cgxq.getCreateId());
			}
		}
		
		String countHql = "select count(*) " + hql;
		hql += " order by t.TCgxq.createTime desc ";
		List<TCgxqDet> l = detDao.find(hql, params, cgxq.getPage(), cgxq.getRows());
		List<Cgxq> nl = new ArrayList<Cgxq>();
		for(TCgxqDet t : l){
			Cgxq c = new Cgxq();
			BeanUtils.copyProperties(t, c);
			TCgxq tCgxq = t.getTCgxq();
			BeanUtils.copyProperties(tCgxq, c);
//			String[] excludeFields = new String[]{"cancelId"};
//			BeanUtils.copyProperties(t, c, excludeFields);
//			logger.info(t.getCancelId());
			if(t.getTCgjh() != null){
				c.setCgjhlsh(t.getTCgjh().getCgjhlsh());
			}
			
			String sql = "select dbo.getKfrkOfCgxq(?, ?) rksl";
			Map<String, Object> params_sql = new HashMap<String, Object>();
			params_sql.put("0", t.getTCgxq().getCgxqlsh());
			params_sql.put("1", t.getSpbh());
			
			Object o = cgxqDao.getBySQL(sql, params_sql);
			BigDecimal rksl = Constant.BD_ZERO;
			if(o != null){
				rksl = new BigDecimal(o.toString());
			}
			
			c.setRksl(rksl);
			
			nl.add(c);
		}
		datagrid.setTotal(detDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(String cgxqlsh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TCgxqDet t where t.TCgxq.cgxqlsh = :cgxqlsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cgxqlsh", cgxqlsh);
		List<TCgxqDet> l = detDao.find(hql, params);
		List<CgxqDet> nl = new ArrayList<CgxqDet>();
		for(TCgxqDet t : l){
			CgxqDet c = new CgxqDet();
			BeanUtils.copyProperties(t, c);
			
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}
	
//	@Override
//	public DataGrid detDatagrid(String cgxqlsh) {
//		DataGrid datagrid = new DataGrid();
//		String hql = "from TCgxqDet t where t.TCgxq.cgxqlsh = :cgxqlsh";
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("cgxqlsh", cgxqlsh);
//		List<TCgxqDet> l = detDao.find(hql, params);
//		List<Cgxq> nl = new ArrayList<Cgxq>();
//		for(TCgxqDet t : l){
//			Cgxq c = new Cgxq();
////			if(t.getCancelId() != null){
////				c.setCancelId(t.getCancelId());
////			}
//			nl.add(c);
//		}
//		datagrid.setRows(nl);
//		return datagrid;
//	}
	
	@Override
	public DataGrid getSpkc(Cgxq cgxq) {
		DataGrid dg = new DataGrid();
		List<ProBean> lists = new ArrayList<ProBean>();
		
		List<ProBean> yw = YwzzServiceImpl.getZzsl(cgxq.getBmbh(), cgxq.getSpbh(), null, ywzzDao);
		if(yw != null){
			lists.addAll(yw);
		}
		
		dg.setRows(lists);
		dg.setTotal((long)lists.size());
		return dg;
	}
	
	@Override
	public DataGrid toCgjh(String cgxqDetIds){
		String sql = "select spbh, sum(zdwsl) zdwsl from t_cgxq_det t ";
		
		if(cgxqDetIds != null && cgxqDetIds.trim().length() > 0){
			String[] cs = cgxqDetIds.split(",");
			sql += "where ";
			for(int i = 0; i < cs.length; i++){
				sql += "t.id = '" + cs[i] + "'";
				if(i != cs.length - 1){
					sql += " or ";
				}
 			}
		}
		sql += " group by spbh";
		logger.info("sql:" + sql);
		
		List<Object[]> l = detDao.findBySQL(sql);
		
		List<CgxqDet> nl = new ArrayList<CgxqDet>();
		
		for(Object[] os : l){
			String spbh = (String)os[0];
			BigDecimal zdwsl = new BigDecimal(os[1].toString());
			
			TSp sp = spDao.get(TSp.class, spbh);
			CgxqDet cd = new CgxqDet();
			cd.setSpbh(spbh);
			cd.setSpmc(sp.getSpmc());
			cd.setSpcd(sp.getSpcd());
			cd.setSppp(sp.getSppp());
			cd.setSpbz(sp.getSpbz());
			cd.setZjldwId(sp.getZjldw().getId());
			cd.setZjldwmc(sp.getZjldw().getJldwmc());
			cd.setZdwsl(zdwsl);
			if(sp.getCjldw() != null){
				cd.setCjldwId(sp.getCjldw().getId());
				cd.setCjldwmc(sp.getCjldw().getJldwmc());
				if(sp.getZhxs().compareTo(Constant.BD_ZERO) != 0){
					cd.setZhxs(sp.getZhxs());
					cd.setCdwsl(zdwsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
				}else{
					cd.setZhxs(Constant.BD_ZERO);
					cd.setCdwsl(Constant.BD_ZERO);
				}
			}
			
			nl.add(cd);
		}
		nl.add(new CgxqDet());
		DataGrid dg = new DataGrid();
		dg.setRows(nl);
		return dg;
	}
		
	@Autowired
	public void setCgxqDao(BaseDaoI<TCgxq> cgxqDao) {
		this.cgxqDao = cgxqDao;
	}

	@Autowired
	public void setDetDao(BaseDaoI<TCgxqDet> detDao) {
		this.detDao = detDao;
	}

	@Autowired
	public void setLshDao(BaseDaoI<TLsh> lshDao) {
		this.lshDao = lshDao;
	}

	@Autowired
	public void setUserDao(BaseDaoI<TUser> userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setDepDao(BaseDaoI<TDepartment> depDao) {
		this.depDao = depDao;
	}

	@Autowired
	public void setYwzzDao(BaseDaoI<TYwzz> ywzzDao) {
		this.ywzzDao = ywzzDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}

	@Autowired
	public void setSpDao(BaseDaoI<TSp> spDao) {
		this.spDao = spDao;
	}

}
