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
	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public Ywsh save(Ywsh ywsh) {
		String lsh = LshServiceImpl.updateLsh(ywsh.getBmbh(), ywsh.getLxbh(), lshDao);
		TYwsh tYwsh = new TYwsh();
		BeanUtils.copyProperties(ywsh, tYwsh);
		tYwsh.setCreateTime(new Date());
		tYwsh.setYwshlsh(lsh);
		tYwsh.setBmmc(depDao.load(TDepartment.class, ywsh.getBmbh()).getDepName());

		tYwsh.setIsCj("0");
		
		Department dep = new Department();
		dep.setId(ywsh.getBmbh());
		dep.setDepName(tYwsh.getBmmc());
		
		Ck ck = new Ck();
		ck.setId(ywsh.getCkId());
		ck.setCkmc(tYwsh.getCkmc());
		
		//处理商品明细
		Set<TYwshDet> tDets = new HashSet<TYwshDet>();
		ArrayList<YwshDet> ywshDets = JSON.parseObject(ywsh.getDatagrid(), new TypeReference<ArrayList<YwshDet>>(){});
		for(YwshDet ywshDet : ywshDets){
			TYwshDet tDet = new TYwshDet();
			BeanUtils.copyProperties(ywshDet, tDet);
			
			if("".equals(ywshDet.getCjldwId()) || null == ywshDet.getZhxs()){
				tDet.setCdwdj(Constant.BD_ZERO);
				tDet.setCdwsl(Constant.BD_ZERO);
				tDet.setZhxs(Constant.BD_ZERO);
			}else{
				if(ywshDet.getZhxs().compareTo(Constant.BD_ZERO) == 0){
					tDet.setCdwdj(Constant.BD_ZERO);
					tDet.setCdwsl(Constant.BD_ZERO);
				}else{
					tDet.setCdwdj(ywshDet.getZdwdj().multiply(ywshDet.getZhxs()).multiply(Constant.SHUILV));
					tDet.setCdwsl(ywshDet.getZdwsl().divide(ywshDet.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
				}
			}
			Sp sp = new Sp();
			BeanUtils.copyProperties(ywshDet, sp);

			tDet.setQdwcb(YwzzServiceImpl.getDwcb(ywsh.getBmbh(), ywshDet.getSpbh(), ywzzDao));
			tDet.setTYwsh(tYwsh);

			//更新业务总账
			YwzzServiceImpl.updateYwzzSl(sp, dep, ck, ywshDet.getZdwsl(), ywshDet.getSpje(), null, null, Constant.UPDATE_RK, ywzzDao);
			tDet.setDwcb(YwzzServiceImpl.getDwcb(ywsh.getBmbh(), ywshDet.getSpbh(), ywzzDao));
			tDets.add(tDet);
			
		}
		tYwsh.setTYwshDets(tDets);
		ywshDao.save(tYwsh);		
		
		OperalogServiceImpl.addOperalog(ywsh.getCreateId(), ywsh.getBmbh(), ywsh.getMenuId(), tYwsh.getYwshlsh(), 
				"生成业务盘点单", operalogDao);
		
		Ywsh rYwsh = new Ywsh();
		rYwsh.setYwshlsh(lsh);
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
		if(ywsh.getFromOther() != null){
			hql += " and t.isCj = '0' and t.TKfpd = null";
		}
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TYwsh> l = ywshDao.find(hql, params, ywsh.getPage(), ywsh.getRows());
		List<Ywsh> nl = new ArrayList<Ywsh>();
		for(TYwsh t : l){
			Ywsh c = new Ywsh();
			BeanUtils.copyProperties(t, c);
			if(t.getTKfpd() != null){
				c.setKfpdlsh(t.getTKfpd().getKfpdlsh());
			}
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
		List<TYwshDet> l = detDao.find(hql, params);
		List<YwshDet> nl = new ArrayList<YwshDet>();
		for(TYwshDet t : l){
			YwshDet c = new YwshDet();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid listAudits(Ywsh ywsh){
		DataGrid dg = new DataGrid();
		String sql = "select th.bmbh, th.bmmc, th.xsthlsh, th.ywymc, th.khmc, th.jsfsmc from t_audit_set t "
				+ " left join t_xsth th on th.bmbh = t.bmbh"
				+ " where t.userId = ? and th.needAudit <> '0' and th.isAudit = '0'";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", ywsh.getCreateId());
		
		List<Object[]> lists = ywshDao.findBySQL(sql, params, ywsh.getPage(), ywsh.getRows());
		
		List<Ywsh> ywhss = new ArrayList<Ywsh>();
		for(Object[] o : lists){
			Ywsh y = new Ywsh();
			y.setBmbh((String)o[0]);
			y.setBmmc((String)o[1]);
			y.setLsh((String)o[2]);
			
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
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
}
