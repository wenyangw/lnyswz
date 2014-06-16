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
	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public Ywsh save(Ywsh ywsh) {
		TYwsh tYwsh = new TYwsh();

		BeanUtils.copyProperties(ywsh, tYwsh);
		
		tYwsh.setCreateTime(new Date());
		tYwsh.setCreateId(ywsh.getCreateId());
		tYwsh.setCreateName(ywsh.getCreateName());
		
		ywshDao.save(tYwsh);	
		
//		OperalogServiceImpl.addOperalog(ywsh.getCreateId(), ywsh.getBmbh(), ywsh.getMenuId(), tYwsh.getYwshlsh(), 
//				"生成业务盘点单", operalogDao);
//		
		Ywsh rYwsh = new Ywsh();
		return rYwsh;
	}
	
	
	@Override
	public DataGrid datagrid(Ywsh ywsh) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TYwsh t where t.bmbh = :bmbh and t.createTime > :createTime";
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
		String sql = "select th.bmbh, th.bmmc, a.auditName, th.xsthlsh, th.ywyId, th.ywymc, th.khbh, th.khmc, th.jsfsmc, th.hjje, th.bz from t_audit_set t "
				+ " left join t_xsth th on th.bmbh = t.bmbh"
				+ " left join t_audit a on t.auditId = a.id"
				+ " where t.userId = ? and th.needAudit <> '0' and th.isAudit = '0'";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", ywsh.getCreateId());
		
		List<Object[]> lists = ywshDao.findBySQL(sql, params, ywsh.getPage(), ywsh.getRows());
		
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
			
			y.setBmbh(bmbh);
			y.setBmmc(bmmc);
			y.setAuditName(auditName);
			y.setLsh(lsh);
			y.setYwymc(ywymc);
			y.setKhmc(khmc);
			y.setJsfsmc(jsfsmc);
			y.setHjje(hjje);
			y.setBz(bz);
			
			y.setYsje(YszzServiceImpl.getYsje(bmbh, khbh, ywyId, yszzDao));						
			ywhss.add(y);
		}
		
		
		dg.setRows(ywhss);
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
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
}
