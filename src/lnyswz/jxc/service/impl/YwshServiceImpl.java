package lnyswz.jxc.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Ywsh;
import lnyswz.jxc.model.TCgjh;
import lnyswz.jxc.model.TCgxq;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TXsth;
import lnyswz.jxc.model.TYszz;
import lnyswz.jxc.model.TYwsh;
import lnyswz.jxc.service.YwshServiceI;
import lnyswz.jxc.util.Constant;

/**
 * 业务审核实现类
 * @author 王文阳
 *
 */
@Service("ywshService")
public class YwshServiceImpl implements YwshServiceI {
	private Logger logger = Logger.getLogger(YwshServiceImpl.class);
	private BaseDaoI<TYwsh> ywshDao;
	private BaseDaoI<TYszz> yszzDao;
	private BaseDaoI<TXsth> xsthDao;
	private BaseDaoI<TCgxq> cgxqDao;
	private BaseDaoI<TCgjh> cgjhDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public void updateAudit(Ywsh ywsh) {
		TYwsh tYwsh = new TYwsh();
		BeanUtils.copyProperties(ywsh, tYwsh);
		
		tYwsh.setCreateTime(new Date());
		tYwsh.setCreateId(ywsh.getCreateId());
		tYwsh.setCreateName(ywsh.getCreateName());
		tYwsh.setIsAudit("1");
		tYwsh.setBmmc(depDao.load(TDepartment.class, ywsh.getBmbh()).getDepName());
		
		TXsth tXsth = xsthDao.load(TXsth.class, ywsh.getLsh());
		tXsth.setIsAudit(ywsh.getAuditLevel());
		
		ywshDao.save(tYwsh);	
		
		OperalogServiceImpl.addOperalog(ywsh.getCreateId(), ywsh.getBmbh(), ywsh.getMenuId(), tYwsh.getLsh(), 
				"业务审批通过", operalogDao);
		
	}
	
	@Override
	public void updateXqshAudit(Ywsh ywsh) {
		TYwsh tYwsh = new TYwsh();
		BeanUtils.copyProperties(ywsh, tYwsh);
		
		tYwsh.setCreateTime(new Date());
		tYwsh.setCreateId(ywsh.getCreateId());
		tYwsh.setCreateName(ywsh.getCreateName());
		tYwsh.setIsAudit("1");
		tYwsh.setBmmc(depDao.load(TDepartment.class, ywsh.getBmbh()).getDepName());
		
		TCgxq tCgxq = cgxqDao.load(TCgxq.class, ywsh.getLsh());
		tCgxq.setIsAudit(ywsh.getAuditLevel());
		
		ywshDao.save(tYwsh);	
		
		OperalogServiceImpl.addOperalog(ywsh.getCreateId(), ywsh.getBmbh(), ywsh.getMenuId(), tYwsh.getLsh(), 
				"采购需求审批通过", operalogDao);
		
	}
	
	@Override
	public void updateJhshAudit(Ywsh ywsh) {
		TYwsh tYwsh = new TYwsh();
		BeanUtils.copyProperties(ywsh, tYwsh);
		
		tYwsh.setCreateTime(new Date());
		tYwsh.setCreateId(ywsh.getCreateId());
		tYwsh.setCreateName(ywsh.getCreateName());
		tYwsh.setIsAudit("1");
		tYwsh.setBmmc(depDao.load(TDepartment.class, ywsh.getBmbh()).getDepName());
		
		TCgjh tCgjh = cgjhDao.load(TCgjh.class, ywsh.getLsh());
		tCgjh.setIsAudit(ywsh.getAuditLevel());
		
		ywshDao.save(tYwsh);	
		
		OperalogServiceImpl.addOperalog(ywsh.getCreateId(), ywsh.getBmbh(), ywsh.getMenuId(), tYwsh.getLsh(), 
				"采购计划审批通过", operalogDao);
	}
	
	@Override
	public void updateRefuse(Ywsh ywsh) {
		TYwsh tYwsh = new TYwsh();
		BeanUtils.copyProperties(ywsh, tYwsh);
		
		tYwsh.setCreateTime(new Date());
		tYwsh.setCreateId(ywsh.getCreateId());
		tYwsh.setCreateName(ywsh.getCreateName());
		tYwsh.setIsAudit("0");
		tYwsh.setBmmc(depDao.load(TDepartment.class, ywsh.getBmbh()).getDepName());
		
		TXsth tXsth = xsthDao.load(TXsth.class, ywsh.getLsh());
		tXsth.setIsAudit(Constant.AUDIT_REFUSE);
		
		ywshDao.save(tYwsh);	
		
		OperalogServiceImpl.addOperalog(ywsh.getCreateId(), ywsh.getBmbh(), ywsh.getMenuId(), tYwsh.getLsh(), 
				"业务审批拒绝", operalogDao);
		
	}
	
	@Override
	public void updateXqshRefuse(Ywsh ywsh) {
		TYwsh tYwsh = new TYwsh();
		BeanUtils.copyProperties(ywsh, tYwsh);
		
		tYwsh.setCreateTime(new Date());
		tYwsh.setCreateId(ywsh.getCreateId());
		tYwsh.setCreateName(ywsh.getCreateName());
		tYwsh.setIsAudit("0");
		tYwsh.setBmmc(depDao.load(TDepartment.class, ywsh.getBmbh()).getDepName());
		
		TCgxq tCgxq = cgxqDao.load(TCgxq.class, ywsh.getLsh());
		tCgxq.setIsAudit(Constant.AUDIT_REFUSE);
		
		ywshDao.save(tYwsh);	
		
		OperalogServiceImpl.addOperalog(ywsh.getCreateId(), ywsh.getBmbh(), ywsh.getMenuId(), tYwsh.getLsh(), 
				"采购需求审批拒绝", operalogDao);
	}
	
	@Override
	public void updateJhshRefuse(Ywsh ywsh) {
		TYwsh tYwsh = new TYwsh();
		BeanUtils.copyProperties(ywsh, tYwsh);
		
		tYwsh.setCreateTime(new Date());
		tYwsh.setCreateId(ywsh.getCreateId());
		tYwsh.setCreateName(ywsh.getCreateName());
		tYwsh.setIsAudit("0");
		tYwsh.setBmmc(depDao.load(TDepartment.class, ywsh.getBmbh()).getDepName());
		
		TCgjh tCgjh = cgjhDao.load(TCgjh.class, ywsh.getLsh());
		tCgjh.setIsAudit(Constant.AUDIT_REFUSE);
		
		ywshDao.save(tYwsh);	
		
		OperalogServiceImpl.addOperalog(ywsh.getCreateId(), ywsh.getBmbh(), ywsh.getMenuId(), tYwsh.getLsh(), 
				"采购计划审批拒绝", operalogDao);
	}
	
	
	@Override
	public DataGrid datagrid(Ywsh ywsh) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TYwsh t where t.bmbh = :bmbh and t.createTime > :createTime and substring(t.lsh, 7, 2) = '05'";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", ywsh.getBmbh());
		if(ywsh.getCreateTime() != null){
			params.put("createTime", ywsh.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}

		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TYwsh> l = ywshDao.find(hql, params, ywsh.getPage(), ywsh.getRows());
		List<Ywsh> nl = new ArrayList<Ywsh>();
		for(TYwsh t : l){
			Ywsh c = new Ywsh();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setTotal(ywshDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}

	@Override
	public DataGrid xqshDatagrid(Ywsh ywsh) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TYwsh t where t.bmbh = :bmbh and t.createTime > :createTime and substring(t.lsh, 7, 2) = '08'";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", ywsh.getBmbh());
		if(ywsh.getCreateTime() != null){
			params.put("createTime", ywsh.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}

		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TYwsh> l = ywshDao.find(hql, params, ywsh.getPage(), ywsh.getRows());
		List<Ywsh> nl = new ArrayList<Ywsh>();
		for(TYwsh t : l){
			Ywsh c = new Ywsh();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setTotal(ywshDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid jhshDatagrid(Ywsh ywsh) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TYwsh t where t.bmbh = :bmbh and t.createTime > :createTime and substring(t.lsh, 7, 2) = '09'";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", ywsh.getBmbh());
		if(ywsh.getCreateTime() != null){
			params.put("createTime", ywsh.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}

		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TYwsh> l = ywshDao.find(hql, params, ywsh.getPage(), ywsh.getRows());
		List<Ywsh> nl = new ArrayList<Ywsh>();
		for(TYwsh t : l){
			Ywsh c = new Ywsh();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setTotal(ywshDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}

	
	@Override
	public DataGrid detDatagrid(String ywshlsh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TYwshDet t where t.TYwsh.ywshlsh = :ywshlsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ywshlsh", ywshlsh);
		return datagrid;
	}
	
	@Override
	public DataGrid listAudits(Ywsh ywsh){
		DataGrid dg = new DataGrid();
		String sql = "select th.bmbh, th.bmmc, a.auditName, th.xsthlsh, th.ywyId, th.ywymc, th.khbh, th.khmc, th.jsfsmc, th.hjje, th.bz, t.auditLevel, isnull(lx.khlxmc, '现款'), kh.sxzq, kh.sxje, a.ywlxId, th.isAudit, th.createTime";
		String fromWhere = " from t_audit_set t "
				+ " left join t_audit a on t.auditId = a.id"
				+ " left join t_xsth th on th.bmbh = t.bmbh and th.isCancel = '0' and a.ywlxId = SUBSTRING(th.xsthlsh, 7, 2)"
				+ " left join t_kh_det kh on th.bmbh = kh.depId and th.khbh = kh.khbh and th.ywyId = kh.ywyId"
				+ " left join t_khlx lx on kh.khlxId = lx.id"
				+ " where t.bmbh = ? and t.userId = ? and th.needAudit <> '0' and th.needAudit <> th.isAudit and t.auditLevel = 1 + th.isAudit";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", ywsh.getBmbh());
		params.put("1", ywsh.getCreateId());
		
		List<Object[]> lists = ywshDao.findBySQL(sql + fromWhere + " order by th.createTime", params, ywsh.getPage(), ywsh.getRows());
		
		List<Ywsh> ywhss = new ArrayList<Ywsh>();
		for(Object[] o : lists){
			Ywsh y = getYwshRow(ywsh, o);
						
			ywhss.add(y);
		}
		
		
		dg.setRows(ywhss);
		dg.setTotal(ywshDao.countSQL("select count(*) " + fromWhere, params));
		return dg;
	}
	
	public DataGrid listCgxqAudits(Ywsh ywsh){
		DataGrid dg = new DataGrid();
		String sql = "select xq.bmbh, xq.bmmc, a.auditName, xq.cgxqlsh, xq.createId, xq.createName, xq.khbh, xq.khmc, xq.jsfsmc, xq.hjje, xq.bz, t.auditLevel, isnull(lx.khlxmc, '现款'), kh.sxzq, kh.sxje, a.ywlxId, xq.isAudit, xq.createTime";
		String fromWhere = " from t_audit_set t "
				+ " left join t_audit a on t.auditId = a.id"
				+ " left join t_cgxq xq on xq.bmbh = t.bmbh and xq.isLs = '1' and a.ywlxId = SUBSTRING(xq.cgxqlsh, 7, 2)"
				+ " left join t_kh_det kh on xq.bmbh = kh.depId and xq.khbh = kh.khbh and xq.ywyId = kh.ywyId"
				+ " left join t_khlx lx on kh.khlxId = lx.id"
				+ " where t.bmbh = ? and t.userId = ? and xq.needAudit <> '0' and xq.needAudit <> xq.isAudit and t.auditLevel = 1 + xq.isAudit";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", ywsh.getBmbh());
		params.put("1", ywsh.getCreateId());
		
		List<Object[]> lists = ywshDao.findBySQL(sql + fromWhere + " order by xq.createTime", params, ywsh.getPage(), ywsh.getRows());
		
		List<Ywsh> ywhss = new ArrayList<Ywsh>();
		for(Object[] o : lists){
			Ywsh y = getXqshRow(ywsh, o);
						
			ywhss.add(y);
		}
				
		dg.setRows(ywhss);
		dg.setTotal(ywshDao.countSQL("select count(*) " + fromWhere, params));
		return dg;
	}

	public DataGrid listCgjhAudits(Ywsh ywsh){
		DataGrid dg = new DataGrid();
		String sql = "select jh.bmbh, jh.bmmc, a.auditName, jh.cgjhlsh, jh.createId, jh.createName, jh.gysbh, jh.gysmc, jh.jsfsmc, jh.hjje, jh.bz, t.auditLevel, a.ywlxId, jh.isAudit, jh.createTime";
		String fromWhere = " from t_audit_set t "
				+ " left join t_audit a on t.auditId = a.id"
				+ " left join t_cgjh jh on jh.bmbh = t.bmbh and a.ywlxId = SUBSTRING(jh.cgjhlsh, 7, 2)"
				+ " where t.bmbh = ? and t.userId = ? and jh.needAudit <> '0' and jh.needAudit <> jh.isAudit and t.auditLevel = 1 + jh.isAudit";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", ywsh.getBmbh());
		params.put("1", ywsh.getCreateId());
		
		List<Object[]> lists = ywshDao.findBySQL(sql + fromWhere + " order by jh.createTime", params, ywsh.getPage(), ywsh.getRows());
		
		List<Ywsh> ywhss = new ArrayList<Ywsh>();
		for(Object[] o : lists){
			Ywsh y = getJhshRow(ywsh, o);
						
			ywhss.add(y);
		}
				
		dg.setRows(ywhss);
		dg.setTotal(ywshDao.countSQL("select count(*) " + fromWhere, params));
		return dg;
	}

	
	private Ywsh getYwshRow(Ywsh ywsh, Object[] o) {
		Ywsh y = new Ywsh();
		String bmbh = (String)o[0];
		String bmmc = (String)o[1];
		String auditName = (String)o[2];
		String lsh = (String)o[3];
		int ywyId = (Integer)o[4];
		String ywymc = (String)o[5];
		String khbh = (String)o[6];
		String khmc = (String)o[7];
		String jsfsmc = (String)o[8];
		BigDecimal hjje = new BigDecimal(o[9].toString());
		String bz = (String)o[10];
		String auditLevel = o[11].toString();
		String khlxmc = o[12].toString();
		int sxzq = o[13] == null ? 0 : Integer.valueOf(o[13].toString());
		BigDecimal sxje = o[14] == null ? Constant.BD_ZERO : new BigDecimal(o[14].toString());
		String ywlxId = o[15].toString();
		String isAudit = o[16].toString();
		Date createTime = DateUtil.stringToDate(o[17].toString(), DateUtil.DATETIME_PATTERN);
		
		y.setBmbh(bmbh);
		y.setBmmc(bmmc);
		y.setAuditName(auditName);
		y.setLsh(lsh);
		y.setYwymc(ywymc);
		y.setKhmc(khmc);
		y.setJsfsmc(jsfsmc);
		y.setHjje(hjje);
		y.setBz(bz);
		y.setAuditLevel(auditLevel);
		y.setKhlxmc(khlxmc);
		y.setSxzq(sxzq);
		y.setSxje(sxje);
		y.setIsAudit(isAudit);
		y.setCreateTime(createTime);
		
		y.setYsje(YszzServiceImpl.getYsje(bmbh, khbh, ywyId, null, yszzDao));

//			String sql_levels = "select a.auditLevel, isnull(sh.createName, '') createName, isnull(sh.createTime, '') createTime"
//					+ " from t_audit a"
//					+ " left join t_ywsh sh on a.auditLevel = sh.auditLevel and SUBSTRING(sh.lsh, 5, 4) = a.bmbh + a.ywlxId and sh.lsh = ?"
//					+ " left join t_xsth th on th.xsthlsh = ?"
//					+ " where a.bmbh = ? and a.ywlxId = ? and a.auditLevel <= th.needAudit";
		String sql_levels = "select distinct a.auditLevel, isnull(sh.createName, '') createName, isnull(sh.createTime, '') createTime"
				+ " from t_audit_set a"
				+ " left join t_ywsh sh on a.auditLevel = sh.auditLevel and SUBSTRING(sh.lsh, 5, 2) = a.bmbh and sh.lsh = ?"
				+ " left join t_xsth th on th.xsthlsh = ?"
				+ " where a.bmbh = ? and a.auditLevel <= th.needAudit";
		Map<String, Object> params_levels = new HashMap<String, Object>();
		params_levels.put("0", lsh);
		params_levels.put("1", lsh);
		params_levels.put("2", ywsh.getBmbh());
		//params_levels.put("3", ywlxId);
		
		List<Object[]> ols = yszzDao.findBySQL(sql_levels, params_levels);
		if(ols != null){
			String levels = "";
			String names = "";
			String times = "";
			int i = 0;
			for(Object[] ol : ols){
				levels += ol[0].toString();
				names += ol[1].toString();
				times += ol[2].toString();
				if(i < ols.size() - 1){
					levels += ",";
					names += ",";
					times += ",";
				}
				i++;
			}
			y.setLevels(levels);
			y.setNames(names);
			y.setTimes(times);
		}
		
		String sql_latest = "select top 1 createTime, hjje, lsh, DATEDIFF(day, dbo.returnPayTime(bmbh, khbh, ywyId,createTime), GETDATE())"
				+ " from v_xs_latest AS mx"
				+ " where bmbh = ? and khbh = ? and ywyId = ?"
				+ " order by bmbh, khbh, ywyId, createTime";
		
		Map<String, Object> params_latest = new HashMap<String, Object>();
		params_latest.put("0", ywsh.getBmbh());
		params_latest.put("1", khbh);
		params_latest.put("2", ywyId);
		
		Object[] ola = yszzDao.getMBySQL(sql_latest, params_latest);
		if(ola != null){
			y.setTimeLatest(ola[0].toString());
			y.setHjjeLatest(ola[1] == null ? Constant.BD_ZERO : new BigDecimal(ola[1].toString()));
			y.setDelayDays(Integer.parseInt(ola[3].toString()));
		}
		return y;
	}
	
	private Ywsh getXqshRow(Ywsh ywsh, Object[] o) {
		Ywsh y = new Ywsh();
		String bmbh = (String)o[0];
		String bmmc = (String)o[1];
		String auditName = (String)o[2];
		String lsh = (String)o[3];
		int ywyId = (Integer)o[4];
		String ywymc = (String)o[5];
		String khbh = (String)o[6];
		String khmc = (String)o[7];
		String jsfsmc = (String)o[8];
		BigDecimal hjje = new BigDecimal(o[9].toString());
		String bz = (String)o[10];
		String auditLevel = o[11].toString();
		String khlxmc = o[12].toString();
		int sxzq = o[13] == null ? 0 : Integer.valueOf(o[13].toString());
		BigDecimal sxje = o[14] == null ? Constant.BD_ZERO : new BigDecimal(o[14].toString());
		String ywlxId = o[15].toString();
		String isAudit = o[16].toString();
		Date createTime = DateUtil.stringToDate(o[17].toString(), DateUtil.DATETIME_PATTERN);
		
		y.setBmbh(bmbh);
		y.setBmmc(bmmc);
		y.setAuditName(auditName);
		y.setLsh(lsh);
		y.setYwymc(ywymc);
		y.setKhmc(khmc);
		y.setJsfsmc(jsfsmc);
		y.setHjje(hjje);
		y.setBz(bz);
		y.setAuditLevel(auditLevel);
		y.setKhlxmc(khlxmc);
		y.setSxzq(sxzq);
		y.setSxje(sxje);
		y.setIsAudit(isAudit);
		y.setCreateTime(createTime);
		
		y.setYsje(YszzServiceImpl.getYsje(bmbh, khbh, ywyId, null, yszzDao));

//			String sql_levels = "select a.auditLevel, isnull(sh.createName, '') createName, isnull(sh.createTime, '') createTime"
//					+ " from t_audit a"
//					+ " left join t_ywsh sh on a.auditLevel = sh.auditLevel and SUBSTRING(sh.lsh, 5, 4) = a.bmbh + a.ywlxId and sh.lsh = ?"
//					+ " left join t_xsth th on th.xsthlsh = ?"
//					+ " where a.bmbh = ? and a.ywlxId = ? and a.auditLevel <= th.needAudit";
		String sql_levels = "select distinct a.auditLevel, isnull(sh.createName, '') createName, isnull(sh.createTime, '') createTime"
				+ " from t_audit_set a"
				+ " left join t_ywsh sh on a.auditLevel = sh.auditLevel and SUBSTRING(sh.lsh, 5, 2) = a.bmbh and sh.lsh = ?"
				+ " left join t_cgxq th on th.cgxqlsh = ?"
				+ " where a.bmbh = ? and a.auditLevel <= th.needAudit";
		Map<String, Object> params_levels = new HashMap<String, Object>();
		params_levels.put("0", lsh);
		params_levels.put("1", lsh);
		params_levels.put("2", ywsh.getBmbh());
		//params_levels.put("3", ywlxId);
		
		List<Object[]> ols = yszzDao.findBySQL(sql_levels, params_levels);
		if(ols != null){
			String levels = "";
			String names = "";
			String times = "";
			int i = 0;
			for(Object[] ol : ols){
				levels += ol[0].toString();
				names += ol[1].toString();
				times += ol[2].toString();
				if(i < ols.size() - 1){
					levels += ",";
					names += ",";
					times += ",";
				}
				i++;
			}
			y.setLevels(levels);
			y.setNames(names);
			y.setTimes(times);
		}
		
		String sql_latest = "select top 1 createTime, hjje, lsh, DATEDIFF(day, dbo.returnPayTime(bmbh, khbh, ywyId,createTime), GETDATE())"
				+ " from v_xs_latest AS mx"
				+ " where bmbh = ? and khbh = ? and ywyId = ?"
				+ " order by bmbh, khbh, ywyId, createTime";
		
		Map<String, Object> params_latest = new HashMap<String, Object>();
		params_latest.put("0", ywsh.getBmbh());
		params_latest.put("1", khbh);
		params_latest.put("2", ywyId);
		
		Object[] ola = yszzDao.getMBySQL(sql_latest, params_latest);
		if(ola != null){
			y.setTimeLatest(ola[0].toString());
			y.setHjjeLatest(ola[1] == null ? Constant.BD_ZERO : new BigDecimal(ola[1].toString()));
			y.setDelayDays(Integer.parseInt(ola[3].toString()));
		}
		return y;
	}

	private Ywsh getJhshRow(Ywsh ywsh, Object[] o) {
		Ywsh y = new Ywsh();
		String bmbh = (String)o[0];
		String bmmc = (String)o[1];
		String auditName = (String)o[2];
		String lsh = (String)o[3];
		int createId = (Integer)o[4];
		String createName = (String)o[5];
		String gysbh = (String)o[6];
		String gysmc = (String)o[7];
		String jsfsmc = (String)o[8];
		BigDecimal hjje = new BigDecimal(o[9].toString());
		String bz = (String)o[10];
		String auditLevel = o[11].toString();
		String ywlxId = o[12].toString();
		String isAudit = o[13].toString();
		Date createTime = DateUtil.stringToDate(o[14].toString(), DateUtil.DATETIME_PATTERN);
		
		y.setBmbh(bmbh);
		y.setBmmc(bmmc);
		y.setAuditName(auditName);
		y.setLsh(lsh);
		y.setCreateName(createName);
		y.setGysmc(gysmc);
		y.setJsfsmc(jsfsmc);
		y.setHjje(hjje);
		y.setBz(bz);
		y.setAuditLevel(auditLevel);
		y.setIsAudit(isAudit);
		y.setCreateTime(createTime);
		

//			String sql_levels = "select a.auditLevel, isnull(sh.createName, '') createName, isnull(sh.createTime, '') createTime"
//					+ " from t_audit a"
//					+ " left join t_ywsh sh on a.auditLevel = sh.auditLevel and SUBSTRING(sh.lsh, 5, 4) = a.bmbh + a.ywlxId and sh.lsh = ?"
//					+ " left join t_xsth th on th.xsthlsh = ?"
//					+ " where a.bmbh = ? and a.ywlxId = ? and a.auditLevel <= th.needAudit";
		String sql_levels = "select distinct a.auditLevel, isnull(sh.createName, '') createName, isnull(sh.createTime, '') createTime"
				+ " from t_audit_set a"
				+ " left join t_ywsh sh on a.auditLevel = sh.auditLevel and SUBSTRING(sh.lsh, 5, 2) = a.bmbh and sh.lsh = ?"
				+ " left join t_cgjh th on th.cgjhlsh = ?"
				+ " where a.bmbh = ? and a.auditLevel <= th.needAudit";
		Map<String, Object> params_levels = new HashMap<String, Object>();
		params_levels.put("0", lsh);
		params_levels.put("1", lsh);
		params_levels.put("2", ywsh.getBmbh());
		//params_levels.put("3", ywlxId);
		
		List<Object[]> ols = yszzDao.findBySQL(sql_levels, params_levels);
		if(ols != null){
			String levels = "";
			String names = "";
			String times = "";
			int i = 0;
			for(Object[] ol : ols){
				levels += ol[0].toString();
				names += ol[1].toString();
				times += ol[2].toString();
				if(i < ols.size() - 1){
					levels += ",";
					names += ",";
					times += ",";
				}
				i++;
			}
			y.setLevels(levels);
			y.setNames(names);
			y.setTimes(times);
		}
		
//		String sql_latest = "select top 1 createTime, hjje, lsh, DATEDIFF(day, dbo.returnPayTime(bmbh, khbh, ywyId,createTime), GETDATE())"
//				+ " from v_xs_latest AS mx"
//				+ " where bmbh = ? and khbh = ? and ywyId = ?"
//				+ " order by bmbh, khbh, ywyId, createTime";
//		
//		Map<String, Object> params_latest = new HashMap<String, Object>();
//		params_latest.put("0", ywsh.getBmbh());
//		params_latest.put("1", khbh);
//		params_latest.put("2", ywyId);
		
//		Object[] ola = yszzDao.getMBySQL(sql_latest, params_latest);
//		if(ola != null){
//			y.setTimeLatest(ola[0].toString());
//			y.setHjjeLatest(ola[1] == null ? Constant.BD_ZERO : new BigDecimal(ola[1].toString()));
//			y.setDelayDays(Integer.parseInt(ola[3].toString()));
//		}
		return y;
	}

	
	@Override
	public DataGrid refreshYwsh(Ywsh ywsh) {
		DataGrid dg = new DataGrid();
		String sql = "select th.bmbh, th.bmmc, a.auditName, th.xsthlsh, th.ywyId, th.ywymc, th.khbh, th.khmc, th.jsfsmc, th.hjje,"
				+ " th.bz, t.auditLevel, isnull(lx.khlxmc, '现款'), kh.sxzq, kh.sxje, a.ywlxId, th.isAudit, th.createTime"
				+ " from t_audit_set t"
				+ " left join t_xsth th on th.bmbh = t.bmbh and th.isCancel = '0'"
				+ " left join t_audit a on t.auditId = a.id"
				+ " left join t_kh_det kh on th.bmbh = kh.depId and th.khbh = kh.khbh and th.ywyId = kh.ywyId"
				+ " left join t_khlx lx on kh.khlxId = lx.id"
				+ " where t.bmbh = ? and t.userId = ? and th.xsthlsh = ? and th.needAudit <> '0' and th.needAudit <> th.isAudit and t.auditLevel = 1 + th.isAudit";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", ywsh.getBmbh());
		params.put("1", ywsh.getCreateId());
		params.put("2", ywsh.getLsh());
		
		Object[] o = ywshDao.getMBySQL(sql, params);
		if(o != null){
			Ywsh y = getYwshRow(ywsh, o);
			dg.setObj(y);
		}
		
		return dg;
	}
	
	@Override
	public DataGrid refreshXqsh(Ywsh ywsh) {
		DataGrid dg = new DataGrid();
//		String sql = "select th.bmbh, th.bmmc, a.auditName, th.cgxqlsh, th.ywyId, th.ywymc, th.khbh, th.khmc, th.jsfsmc, th.hjje,"
//				+ " th.bz, t.auditLevel, isnull(lx.khlxmc, '现款'), kh.sxzq, kh.sxje, a.ywlxId, th.isAudit, th.createTime"
//				+ " from t_audit_set t"
//				+ " left join t_cgxq th on th.bmbh = t.bmbh and th.isCancel = '0'"
//				+ " left join t_audit a on t.auditId = a.id"
//				+ " left join t_kh_det kh on th.bmbh = kh.depId and th.khbh = kh.khbh and th.ywyId = kh.ywyId"
//				+ " left join t_khlx lx on kh.khlxId = lx.id"
//				+ " where t.bmbh = ? and t.userId = ? and th.cgxqlsh = ? and th.needAudit <> '0' and th.needAudit <> th.isAudit and t.auditLevel = 1 + th.isAudit";

		String sql = "select th.bmbh, th.bmmc, a.auditName, th.cgxqlsh, th.ywyId, th.ywymc, th.khbh, th.khmc, th.jsfsmc, th.hjje,"
				+ " th.bz, t.auditLevel, isnull(lx.khlxmc, '现款'), kh.sxzq, kh.sxje, a.ywlxId, th.isAudit, th.createTime"
				+ " from t_audit_set t"
				+ " left join t_audit a on t.auditId = a.id"
				+ " left join t_cgxq th on th.bmbh = t.bmbh and th.isLs = '1' and a.ywlxId = SUBSTRING(th.cgxqlsh, 7, 2)"
				+ " left join t_kh_det kh on th.bmbh = kh.depId and th.khbh = kh.khbh and th.ywyId = kh.ywyId"
				+ " left join t_khlx lx on kh.khlxId = lx.id"
				+ " where t.bmbh = ? and t.userId = ? and th.cgxqlsh = ? and th.needAudit <> '0' and th.needAudit <> th.isAudit and t.auditLevel = 1 + th.isAudit";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", ywsh.getBmbh());
		params.put("1", ywsh.getCreateId());
		params.put("2", ywsh.getLsh());
		
		Object[] o = ywshDao.getMBySQL(sql, params);
		if(o != null){
			Ywsh y = getXqshRow(ywsh, o);
			dg.setObj(y);
		}
		
		return dg;
	}
	
	@Override
	public DataGrid refreshJhsh(Ywsh ywsh) {
		DataGrid dg = new DataGrid();

		String sql = "select th.bmbh, th.bmmc, a.auditName, th.cgjhlsh, th.ywyId, th.ywymc, th.khbh, th.khmc, th.jsfsmc, th.hjje,"
				+ " th.bz, t.auditLevel, isnull(lx.khlxmc, '现款'), kh.sxzq, kh.sxje, a.ywlxId, th.isAudit, th.createTime"
				+ " from t_audit_set t"
				+ " left join t_audit a on t.auditId = a.id"
				+ " left join t_cgjh th on th.bmbh = t.bmbh and th.isLs = '1' and a.ywlxId = SUBSTRING(th.cgjhlsh, 7, 2)"
				+ " left join t_kh_det kh on th.bmbh = kh.depId and th.khbh = kh.khbh and th.ywyId = kh.ywyId"
				+ " left join t_khlx lx on kh.khlxId = lx.id"
				+ " where t.bmbh = ? and t.userId = ? and th.cgjhlsh = ? and th.needAudit <> '0' and th.needAudit <> th.isAudit and t.auditLevel = 1 + th.isAudit";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", ywsh.getBmbh());
		params.put("1", ywsh.getCreateId());
		params.put("2", ywsh.getLsh());
		
		Object[] o = ywshDao.getMBySQL(sql, params);
		if(o != null){
			Ywsh y = getXqshRow(ywsh, o);
			dg.setObj(y);
		}
		
		return dg;
	}

	
	@Autowired
	public void setYwshDao(BaseDaoI<TYwsh> ywshDao) {
		this.ywshDao = ywshDao;
	}

	@Autowired
	public void setYszzDao(BaseDaoI<TYszz> yszzDao) {
		this.yszzDao = yszzDao;
	}

	@Autowired
	public void setXsthDao(BaseDaoI<TXsth> xsthDao) {
		this.xsthDao = xsthDao;
	}

	@Autowired
	public void setCgxqDao(BaseDaoI<TCgxq> cgxqDao) {
		this.cgxqDao = cgxqDao;
	}

	@Autowired
	public void setCgjhDao(BaseDaoI<TCgjh> cgjhDao) {
		this.cgjhDao = cgjhDao;
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
