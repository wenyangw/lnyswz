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
import lnyswz.jxc.bean.Ywsh;
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
import lnyswz.jxc.model.TXsth;
import lnyswz.jxc.model.TYszz;
import lnyswz.jxc.model.TYwhs;
import lnyswz.jxc.model.TYwhsDet;
import lnyswz.jxc.model.TYwsh;
import lnyswz.jxc.model.TCgxq;
import lnyswz.jxc.model.TGys;
import lnyswz.jxc.model.TJsfs;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.model.TRole;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.model.TUser;
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
	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public void updateAudit(Ywsh ywsh) {
		TYwsh tYwsh = new TYwsh();
		BeanUtils.copyProperties(ywsh, tYwsh);
		
		tYwsh.setCreateTime(new Date());
		tYwsh.setCreateId(ywsh.getCreateId());
		tYwsh.setCreateName(ywsh.getCreateName());
		tYwsh.setIsAudit("1");
		
		TXsth tXsth = xsthDao.load(TXsth.class, ywsh.getLsh());
		tXsth.setIsAudit(ywsh.getAuditLevel());
		
		ywshDao.save(tYwsh);	
		
		OperalogServiceImpl.addOperalog(ywsh.getCreateId(), ywsh.getBmbh(), ywsh.getMenuId(), tYwsh.getLsh(), 
				"业务审批通过", operalogDao);
		
	}
	
	@Override
	public void updateRefuse(Ywsh ywsh) {
		TYwsh tYwsh = new TYwsh();
		BeanUtils.copyProperties(ywsh, tYwsh);
		
		tYwsh.setCreateTime(new Date());
		tYwsh.setCreateId(ywsh.getCreateId());
		tYwsh.setCreateName(ywsh.getCreateName());
		tYwsh.setIsAudit("0");
		
		TXsth tXsth = xsthDao.load(TXsth.class, ywsh.getLsh());
		tXsth.setIsAudit(Constant.AUDIT_REFUSE);
		
		ywshDao.save(tYwsh);	
		
		OperalogServiceImpl.addOperalog(ywsh.getCreateId(), ywsh.getBmbh(), ywsh.getMenuId(), tYwsh.getLsh(), 
				"业务审批拒绝", operalogDao);
		
	}
	
	
	@Override
	public DataGrid datagrid(Ywsh ywsh) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TYwsh t where t.createId = :createId and t.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("createId", ywsh.getCreateId());
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
		String sql = "select th.bmbh, th.bmmc, a.auditName, th.xsthlsh, th.ywyId, th.ywymc, th.khbh, th.khmc, th.jsfsmc, th.hjje, th.bz, t.auditLevel, isnull(lx.khlxmc, '现款'), kh.sxzq, kh.sxje, a.ywlxId";
		String fromWhere = " from t_audit_set t "
				+ " left join t_xsth th on th.bmbh = t.bmbh"
				+ " left join t_audit a on t.auditId = a.id"
				+ " left join t_kh_det kh on th.bmbh = kh.depId and th.khbh = kh.khbh and th.ywyId = kh.ywyId"
				+ " left join t_khlx lx on kh.khlxId = lx.id"
				+ " where t.userId = ? and th.needAudit <> th.isAudit and t.auditLevel = 1 + th.isAudit";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", ywsh.getCreateId());
		
		List<Object[]> lists = ywshDao.findBySQL(sql + fromWhere, params, ywsh.getPage(), ywsh.getRows());
		
		List<Ywsh> ywhss = new ArrayList<Ywsh>();
		for(Object[] o : lists){
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
			
			y.setYsje(YszzServiceImpl.getYsje(bmbh, khbh, ywyId, yszzDao));
		
			String sql_levels = "select a.auditLevel, isnull(sh.createName, '') createName, isnull(sh.createTime, '') createTime"
					+ " from t_audit a"
					+ " left join t_ywsh sh on a.auditLevel = sh.auditLevel and SUBSTRING(sh.lsh, 5, 4) = a.bmbh + a.ywlxId and sh.lsh = ?"
					+ " left join t_xsth th on th.xsthlsh = ?"
					+ " where a.bmbh = ? and a.ywlxId = ? and a.auditLevel <= th.needAudit";
			Map<String, Object> params_levels = new HashMap<String, Object>();
			params_levels.put("0", lsh);
			params_levels.put("1", lsh);
			params_levels.put("2", ywsh.getBmbh());
			params_levels.put("3", ywlxId);
			
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
			
			String sql_latest = "select xs.createTime, th.hjje"
					+ " from v_xs_latest xs"
					+ "	left join t_xsth th on xs.xsthlsh = th.xsthlsh"
					+ " where xs.bmbh = ? and xs.khbh = ? and xs.ywyId = ?";
			Map<String, Object> params_latest = new HashMap<String, Object>();
			params_latest.put("0", ywsh.getBmbh());
			params_latest.put("1", khbh);
			params_latest.put("2", ywyId);
			
			Object[] ola = yszzDao.getBySQL(sql_latest, params_latest);
			if(ola != null){
				y.setTimeLatest(ola[0].toString());
				y.setHjjeLatest(ola[1] == null ? Constant.BD_ZERO : new BigDecimal(ola[1].toString()));
			}
			
			
			ywhss.add(y);
		}
		
		
		dg.setRows(ywhss);
		dg.setTotal(ywshDao.countSQL("select count(*) " + fromWhere, params));
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
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
}
