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
import lnyswz.jxc.bean.Cgjh;
import lnyswz.jxc.bean.CgjhDet;
import lnyswz.jxc.bean.Cgxq;
import lnyswz.jxc.bean.CgxqDet;
import lnyswz.jxc.bean.Ywhs;
import lnyswz.jxc.bean.YwhsDet;
import lnyswz.jxc.model.TCgjh;
import lnyswz.jxc.model.TCgjhDet;
import lnyswz.jxc.model.TCgxq;
import lnyswz.jxc.model.TCgxqDet;
import lnyswz.jxc.model.TCk;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TGys;
import lnyswz.jxc.model.TJsfs;
import lnyswz.jxc.model.TKfrk;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TRole;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.model.TUser;
import lnyswz.jxc.model.TYwhs;
import lnyswz.jxc.model.TYwhsDet;
import lnyswz.jxc.model.TYwrk;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.service.CgjhServiceI;
import lnyswz.jxc.util.Constant;

/**
 * 采购计划实现类
 * @author 王文阳
 *
 */
@Service("cgjhService")
public class CgjhServiceImpl implements CgjhServiceI {
	private Logger logger = Logger.getLogger(CgjhServiceImpl.class);
	private BaseDaoI<TCgjh> cgjhDao;
	private BaseDaoI<TCgjhDet> detDao;
	private BaseDaoI<TCgxqDet> cgxqDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TSp> spDao;
	private BaseDaoI<TYwzz> ywzzDao;
	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public Cgjh save(Cgjh cgjh) {
		String lsh = LshServiceImpl.updateLsh(cgjh.getBmbh(), cgjh.getLxbh(), lshDao);
		TCgjh tCgjh = new TCgjh();
		BeanUtils.copyProperties(cgjh, tCgjh, new String[]{"cancelId", "completeId"});
		Date createTime = new Date();
		tCgjh.setCreateTime(createTime);
		tCgjh.setCgjhlsh(lsh);
		tCgjh.setBmmc(depDao.load(TDepartment.class, cgjh.getBmbh()).getDepName());
		tCgjh.setIsCancel("0");
		tCgjh.setIsCompleted("0");
		tCgjh.setNeedAudit("0");
		if(tCgjh.getNeedAudit().equals("1")){
			tCgjh.setIsAudit("0");
		}else{
			tCgjh.setIsAudit("1");
		}
		
		tCgjh.setReturnHt("0");
		
		if(null == cgjh.getHjje()){
			tCgjh.setHjje(Constant.BD_ZERO);
		}
		
		//如果从需求生成的计划，进行关联，并将需求设置完成
		String cgxqDetIds = cgjh.getCgxqDetIds();
		if(cgxqDetIds != null && cgxqDetIds.trim().length() > 0){
			for(String detId : cgxqDetIds.split(",")){
				TCgxqDet tCgxqDet = cgxqDao.load(TCgxqDet.class, Integer.valueOf(detId));
				tCgxqDet.setTCgjh(tCgjh);
			}
		}
		//处理商品明细
		Set<TCgjhDet> tDets = new HashSet<TCgjhDet>();
		ArrayList<CgjhDet> cgjhDets = JSON.parseObject(cgjh.getDatagrid(), new TypeReference<ArrayList<CgjhDet>>(){});
		for(CgjhDet cgjhDet : cgjhDets){
			TCgjhDet tDet = new TCgjhDet();
			BeanUtils.copyProperties(cgjhDet, tDet);
			if(cgjhDet.getZdwdj() == null){
				tDet.setZdwdj(Constant.BD_ZERO);
				tDet.setCdwdj(Constant.BD_ZERO);
				tDet.setSpje(Constant.BD_ZERO);
			}
			if("".equals(cgjhDet.getCjldwId()) || null == cgjhDet.getCjldwId()){
				tDet.setCdwdj(Constant.BD_ZERO);
				tDet.setCdwsl(Constant.BD_ZERO);
				tDet.setZhxs(Constant.BD_ZERO);
			}else{
				if(cgjhDet.getZhxs().compareTo(Constant.BD_ZERO) == 0){
					tDet.setCdwdj(Constant.BD_ZERO);
					tDet.setCdwsl(Constant.BD_ZERO);
				}
			}
			tDet.setTCgjh(tCgjh);
			tDets.add(tDet);
		}
		tCgjh.setTCgjhDets(tDets);
		cgjhDao.save(tCgjh);
		OperalogServiceImpl.addOperalog(cgjh.getCreateId(), cgjh.getBmbh(), cgjh.getMenuId(), tCgjh.getCgjhlsh(), 
				"生成采购计划单", operalogDao);
		
		Cgjh rCgjh = new Cgjh();
		rCgjh.setCgjhlsh(lsh);
		return rCgjh;
	}
	
	@Override
	public void updateCancel(Cgjh cgjh) {
		TCgjh tCgjh = cgjhDao.get(TCgjh.class, cgjh.getCgjhlsh());
		tCgjh.setCancelId(cgjh.getCancelId());
		tCgjh.setCancelTime(new Date());
		tCgjh.setCancelName(cgjh.getCancelName());
		tCgjh.setIsCancel("1");
		
		Set<TCgxqDet> tCgxqs = tCgjh.getTCgxqs();
		if(tCgxqs != null && tCgxqs.size() > 0){
			for(TCgxqDet tCgxqDet : tCgxqs){
				tCgxqDet.setTCgjh(null);
			}
		}
		OperalogServiceImpl.addOperalog(cgjh.getCancelId(), cgjh.getBmbh(), cgjh.getMenuId(), cgjh.getCgjhlsh(), 
				"取消采购计划单", operalogDao);
	}
	
	@Override
	public DataGrid printCgjh(Cgjh cgjh) {
		DataGrid datagrid = new DataGrid();
		TCgjh tCgjh = cgjhDao.load(TCgjh.class, cgjh.getCgjhlsh());
		BigDecimal hjsl = Constant.BD_ZERO;
		
		String hql = "from TCgjhDet t where t.TCgjh.cgjhlsh = :cgjhlsh order by t.spbh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cgjhlsh", cgjh.getCgjhlsh());
		List<TCgjhDet> tCgjhDets = detDao.find(hql, params);
		
		List<CgjhDet> nl = new ArrayList<CgjhDet>();
//		for (TCgjhDet yd : tCgjh.getTCgjhDets()) {
		for (TCgjhDet yd : tCgjhDets) {
			CgjhDet cgjhDet = new CgjhDet();
			BeanUtils.copyProperties(yd, cgjhDet);
			hjsl = hjsl.add(yd.getCdwsl());
			nl.add(cgjhDet);
		}
		int num = nl.size();
		if (num < Constant.REPORT_NUMBER) {
			for (int i = 0; i < (Constant.REPORT_NUMBER - num); i++) {
				nl.add(new CgjhDet());
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "采   购   计   划   单");
		map.put("gsmc", Constant.BMMCS.get(tCgjh.getBmbh()));
		map.put("gysbh", tCgjh.getGysbh());
		map.put("gysmc", tCgjh.getGysmc());
		map.put("bmmc", tCgjh.getBmmc());
		map.put("createTime", DateUtil.dateToString(tCgjh.getCreateTime(), DateUtil.DATETIME_NOSECOND_PATTERN));
		map.put("cgjhlsh", tCgjh.getCgjhlsh());
		map.put("hjsl", hjsl);
		map.put("hjje", tCgjh.getHjje());
		map.put("bz", tCgjh.getBz());
		map.put("printName", cgjh.getCreateName());
		map.put("printTime", DateUtil.dateToString(new Date()));
		datagrid.setObj(map);
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public void updateComplete(Cgjh cgjh) {
		TCgjh tCgjh = cgjhDao.get(TCgjh.class, cgjh.getCgjhlsh());
		tCgjh.setCompleteId(cgjh.getCompleteId());
		tCgjh.setCompleteTime(new Date());
		tCgjh.setCompleteName(cgjh.getCompleteName());
		tCgjh.setIsCompleted("1");
		OperalogServiceImpl.addOperalog(cgjh.getCompleteId(), cgjh.getBmbh(), cgjh.getMenuId(), cgjh.getCgjhlsh(), 
				"完成采购计划单", operalogDao);
	}
	
	@Override
	public void updateUnComplete(Cgjh cgjh) {
		TCgjh tCgjh = cgjhDao.get(TCgjh.class, cgjh.getCgjhlsh());
		tCgjh.setCompleteId(cgjh.getCompleteId());
		tCgjh.setCompleteTime(new Date());
		tCgjh.setCompleteName(cgjh.getCompleteName());
		tCgjh.setIsCompleted("0");
		OperalogServiceImpl.addOperalog(cgjh.getCompleteId(), cgjh.getBmbh(), cgjh.getMenuId(), cgjh.getCgjhlsh(), 
				"取消完成采购计划单", operalogDao);
	}
	
	@Override
	public void updateIsHt(Cgjh cgjh) {
		TCgjh tCgjh = cgjhDao.get(TCgjh.class, cgjh.getCgjhlsh());
		String isHt = tCgjh.getIsHt();
		if("1".equals(isHt)){
			tCgjh.setIsHt("0");
		}else{
			tCgjh.setIsHt("1");
		}
		OperalogServiceImpl.addOperalog(cgjh.getHtId(), cgjh.getBmbh(), cgjh.getMenuId(), cgjh.getCgjhlsh(), 
				"采购计划单转换合同标记", operalogDao);
	}
	
	@Override
	public void updateHt(Cgjh cgjh) {
		TCgjh tCgjh = cgjhDao.get(TCgjh.class, cgjh.getCgjhlsh());
		tCgjh.setHtId(cgjh.getHtId());
		tCgjh.setHtTime(new Date());
		tCgjh.setHtName(cgjh.getCompleteName());
		tCgjh.setReturnHt("1");
		OperalogServiceImpl.addOperalog(cgjh.getHtId(), cgjh.getBmbh(), cgjh.getMenuId(), cgjh.getCgjhlsh(), 
				"采购计划单标记合同收回", operalogDao);
	}
	
	@Override
	public DataGrid datagrid(Cgjh cgjh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TCgjh t where t.bmbh = :bmbh and t.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", cgjh.getBmbh());
		if(cgjh.getCreateTime() != null){
			params.put("createTime", cgjh.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		if(cgjh.getSearch() != null){
			hql += " and (t.cgjhlsh like :search or t.gysmc like :search or t.bz like :search)"; 
			params.put("search", "%" + cgjh.getSearch() + "%");
		}
		if(cgjh.getFromOther() != null){
			hql += " and t.isCancel = '0' and t.isCompleted = '0'";
			if(cgjh.getIsZs() != null){
				hql += " and isZs = '1' and ywrklsh = null";
			}
		}else{
			hql += " and createId = :createId or (t.bmbh = :bmbh and (t.isCompleted = '0' or (t.isHt = '1' and t.returnHt = '0')) and t.isCancel = '0')";
			params.put("createId", cgjh.getCreateId());
		}
		
		String countHql = "select count(*) " + hql;
		hql += " order by t.createTime desc ";
		List<TCgjh> l = cgjhDao.find(hql, params, cgjh.getPage(), cgjh.getRows());
		List<Cgjh> nl = new ArrayList<Cgjh>();
		for(TCgjh t : l){
			Cgjh c = new Cgjh();
			BeanUtils.copyProperties(t, c);
			
//			if(t.getTYwrk() != null){
//				c.setIsKfrk("1");
//			}
			
			Set<TCgxqDet> tCgxqs = t.getTCgxqs();
			if(tCgxqs != null && tCgxqs.size() > 0){
				String cgxqlshs = "";
				int i = 0;
				for(TCgxqDet tc : tCgxqs){
					cgxqlshs += tc.getTCgxq().getCgxqlsh();
					if(i < tCgxqs.size() - 1){
						cgxqlshs += ",";
					}
					i++;
				}
				c.setCgxqlshs(cgxqlshs);
			}
			boolean flag = false;
			Set<TCgjhDet> tDets = t.getTCgjhDets();
			loop:
			for(TCgjhDet tDet : tDets){
				if(tDet.getTKfrks() != null && tDet.getTKfrks().size() > 0){
					c.setIsKfrk("1");
					if(flag){
						break;
					}else{
						flag = true;
					}
				}
				if(tDet.getTYwrks() != null && tDet.getTYwrks().size() > 0){
					for(TYwrk tYwrk : tDet.getTYwrks()){
						if(tYwrk.getRklxId().equals(Constant.RKLX_ZG)){
							//暂估
							c.setIsKfrk("3");
							break loop;
						}
					}
					//正式
					c.setIsKfrk("2");
				}
			}
			nl.add(c);
		}
		datagrid.setTotal(cgjhDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(String cgjhlsh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TCgjhDet t where t.TCgjh.cgjhlsh = :cgjhlsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cgjhlsh", cgjhlsh);
		List<TCgjhDet> l = detDao.find(hql, params);
		List<CgjhDet> nl = new ArrayList<CgjhDet>();
		for(TCgjhDet t : l){
			CgjhDet c = new CgjhDet();
			BeanUtils.copyProperties(t, c);
			
			Set<TKfrk> tKfrks = t.getTKfrks();
			if(tKfrks != null && tKfrks.size() > 0){
				String kfrklshs = "";
				int i = 0;
				for(TKfrk k : tKfrks){
					
					if("0".equals(k.getIsCj())){
						kfrklshs += k.getKfrklsh();
						if(i < tKfrks.size() - 1){
							kfrklshs += ",";
						}
					}
					i++;
				}
				c.setKfrklshs(kfrklshs);
				
				c.setZdwyrsl(getYrsl(cgjhlsh, t.getSpbh()));
			}
			
			Set<TYwrk> tYwrks = t.getTYwrks();
			if(tYwrks != null && tYwrks.size() > 0){
				String ywrklshs = "";
				int i = 0;
				for(TYwrk k : tYwrks){
					
					if("0".equals(k.getIsCj())){
						ywrklshs += k.getYwrklsh();
						if(i < tYwrks.size() - 1){
							ywrklshs += ",";
						}
					}
					i++;
				}
				c.setYwrklshs(ywrklshs);
				
				c.setZdwrksl(getRksl(cgjhlsh, t.getSpbh()));
			}
			
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid datagridDet(Cgjh cgjh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TCgjhDet t where t.TCgjh.bmbh = :bmbh and t.TCgjh.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", cgjh.getBmbh());
		if(cgjh.getCreateTime() != null){
			params.put("createTime", cgjh.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		if(cgjh.getSearch() != null){
			hql += " and (t.TCgjh.cgjhlsh like :search or t.TCgjh.gysmc like :search or t.TCgjh.bz like :search)"; 
			params.put("search", "%" + cgjh.getSearch() + "%");
			
		}
		//TODO 仓库过滤
		
		//采购计划流程只查询未完成的有效数据
		if(cgjh.getFromOther() != null){
			hql += " and t.TCgjh.isCancel = '0' and t.TCgjh.isCompleted = '0' ";
			//是否直送
			if("1".equals(cgjh.getIsZs())){
				 hql += " and t.TCgjh.isZs = '1'";
			}else{
				hql += " and t.TCgjh.isZs = '0'";
			}
		}
		
		
		String countHql = "select count(*) " + hql;
		hql += " order by t.TCgjh.createTime desc ";
		List<TCgjhDet> l = detDao.find(hql, params, cgjh.getPage(), cgjh.getRows());
		List<Cgjh> nl = new ArrayList<Cgjh>();
		for(TCgjhDet t : l){
			Cgjh c = new Cgjh();
			BeanUtils.copyProperties(t, c);
			TCgjh tCgjh = t.getTCgjh();
			BeanUtils.copyProperties(tCgjh, c);
//			if(t.getTCgjh() != null){
//				c.setCgjhlsh(t.getTCgjh().getCgjhlsh());
//			}
			Set<TKfrk> tKfrks = t.getTKfrks();
			if(tKfrks != null && tKfrks.size() > 0){
				String kfrklshs = "";
				int i = 0;
				for(TKfrk tKfrk : tKfrks){
					if("0".equals(tKfrk.getIsCj())){
						kfrklshs += tKfrk.getKfrklsh();
						if(i < tKfrks.size() - 1){
							kfrklshs += ",";
						}
					}
					i++;
				}
				c.setKfrklshs(kfrklshs);
				
				c.setZdwyrsl(getYrsl(t.getTCgjh().getCgjhlsh(), t.getSpbh()));
			}
			if("1".equals(t.getTCgjh().getIsZs())){
				Set<TYwrk> tYwrks = t.getTYwrks();
				if(tYwrks != null && tYwrks.size() > 0){
					String ywrklshs = "";
					int i = 0;
					for(TYwrk tYwrk : tYwrks){
						if("0".equals(tYwrk.getIsCj())){
							ywrklshs += tYwrk.getYwrklsh();
							if(i < tYwrks.size() - 1){
								ywrklshs += ",";
							}
						}
						i++;
					}
					c.setYwrklshs(ywrklshs);
					
					c.setZdwyrsl(getYwrksl(t.getTCgjh().getCgjhlsh(), t.getSpbh()));
				}
			}
			
			nl.add(c);
		}
		datagrid.setTotal(detDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}

	private BigDecimal getYrsl(String cgjhlsh, String spbh) {
		String detHql = "select isnull(sum(kfDet.zdwsl), 0) from t_cgjh_det jhDet "
				+ "left join t_cgjh_kfrk ck on jhDet.id = ck.cgjhdetId "
				+ "left join t_kfrk_det kfDet on ck.kfrklsh = kfDet.kfrklsh and jhDet.spbh = kfDet.spbh "
				+ "where jhDet.cgjhlsh = ? and jhDet.spbh = ?";
		Map<String, Object> detParams = new HashMap<String, Object>();
		detParams.put("0", cgjhlsh);
		detParams.put("1", spbh);
		BigDecimal yrsl = new BigDecimal(detDao.getBySQL(detHql, detParams).toString());
		return yrsl;
	}
	
	private BigDecimal getRksl(String cgjhlsh, String spbh) {
		String detHql = "select isnull(sum(kfDet.zdwsl), 0) from t_cgjh_det jhDet "
				+ "left join t_cgjh_ywrk ck on jhDet.id = ck.cgjhdetId "
				+ "left join t_ywrk_det kfDet on ck.ywrklsh = kfDet.ywrklsh and jhDet.spbh = kfDet.spbh "
				+ "where jhDet.cgjhlsh = ? and jhDet.spbh = ?";
		Map<String, Object> detParams = new HashMap<String, Object>();
		detParams.put("0", cgjhlsh);
		detParams.put("1", spbh);
		BigDecimal rksl = new BigDecimal(detDao.getBySQL(detHql, detParams).toString());
		return rksl;
	}
	
	private BigDecimal getYwrksl(String cgjhlsh, String spbh) {
		String detHql = "select isnull(sum(ywDet.zdwsl), 0) from t_cgjh_det jhDet "
				+ "left join t_cgjh_ywrk cy on jhDet.id = cy.cgjhdetId "
				+ "left join t_ywrk_det ywDet on cy.ywrklsh = ywDet.ywrklsh and jhDet.spbh = ywDet.spbh "
				+ "where jhDet.cgjhlsh = ? and jhDet.spbh = ?";
		Map<String, Object> detParams = new HashMap<String, Object>();
		detParams.put("0", cgjhlsh);
		detParams.put("1", spbh);
		BigDecimal ywrksl = new BigDecimal(detDao.getBySQL(detHql, detParams).toString());
		return ywrksl;
	}
	
	@Override
	public DataGrid getSpkc(Cgjh cgjh) {
		DataGrid dg = new DataGrid();
		List<ProBean> lists = new ArrayList<ProBean>();
		
		List<ProBean> yw = YwzzServiceImpl.getZzsl(cgjh.getBmbh(), cgjh.getSpbh(), null, ywzzDao);
		if(yw != null){
			lists.addAll(yw);
		}
		
		dg.setRows(lists);
		dg.setTotal((long)lists.size());
		return dg;
	}
	
	@Override
	public DataGrid toKfrk(String cgjhDetIds){
		String sql = "select cd.spbh, isnull(max(cd.zdwsl), 0) zdwjhsl, isnull(sum(kd.zdwsl), 0) zdwyrsl from t_cgjh_det cd " +
				"left join t_cgjh_kfrk ck on cd.id = ck.cgjhdetId " +
				"left join t_kfrk k on k.kfrklsh = ck.kfrklsh " +
				"left join t_kfrk_det kd on k.kfrklsh = kd.kfrklsh and kd.spbh = cd.spbh ";
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(cgjhDetIds != null && cgjhDetIds.trim().length() > 0){
			String[] cs = cgjhDetIds.split(",");
			sql += "where ";
			for(int i = 0; i < cs.length; i++){
				sql += "cd.id = ? ";
				params.put(String.valueOf(i), cs[i]);
				if(i != cs.length - 1){
					sql += " or ";
				}
 			}
			sql += " group by cd.spbh";
		}
		
		List<Object[]> l = detDao.findBySQL(sql, params);
		
		List<CgjhDet> nl = new ArrayList<CgjhDet>();
		
		for(Object[] os : l){
			String spbh = (String)os[0];
			BigDecimal zdwjhsl = new BigDecimal(os[1].toString());
			BigDecimal zdwyrsl = new BigDecimal(os[2].toString());
			
			TSp sp = spDao.get(TSp.class, spbh);
			CgjhDet cd = new CgjhDet();
			cd.setSpbh(spbh);
			cd.setSpmc(sp.getSpmc());
			cd.setSpcd(sp.getSpcd());
			cd.setSppp(sp.getSppp());
			cd.setSpbz(sp.getSpbz());
			cd.setZdwjhsl(zdwjhsl);
			cd.setZdwyrsl(zdwyrsl);
			cd.setZjldwId(sp.getZjldw().getId());
			cd.setZjldwmc(sp.getZjldw().getJldwmc());
			if(sp.getCjldw() != null){
				cd.setCjldwId(sp.getCjldw().getId());
				cd.setCjldwmc(sp.getCjldw().getJldwmc());
				if(sp.getZhxs().compareTo(Constant.BD_ZERO) != 0){
					cd.setZhxs(sp.getZhxs());
					cd.setCdwjhsl(zdwjhsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
					cd.setCdwyrsl(zdwyrsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
				}else{
//					cd.setZhxs(ZERO);
//					cd.setCdwjhsl(ZERO);
//					cd.setCdwyrsl(ZERO);
				}
			}
			nl.add(cd);
		}
		nl.add(new CgjhDet());
		DataGrid dg = new DataGrid();
		dg.setRows(nl);
		return dg;
	}
	
	@Override
	public DataGrid toYwrk(String cgjhDetIds){
		//String sql = "select spbh, sum(zdwsl) zdwjhsl from t_cgjh_det t ";
		String sql = "select cd.spbh, isnull(max(cd.zdwsl), 0) zdwjhsl, isnull(sum(yd.zdwsl), 0) zdwyrsl"
				+ " from t_cgjh_det cd " +
				"left join t_cgjh_ywrk cy on cd.id = cy.cgjhdetId " +
				"left join t_ywrk y on y.ywrklsh = cy.ywrklsh " +
				"left join t_ywrk_det yd on y.ywrklsh = yd.ywrklsh and yd.spbh = cd.spbh ";
		
		//String sql = "select spbh,sum(zdwsl) zdwsl from t_cgjh_det d ";
		
		Map<String, Object> params = new HashMap<String, Object>();
		
//		if(cgjhlshs != null && cgjhlshs.trim().length() > 0){
//			String[] cs = cgjhlshs.split(",");
//			sql += "where ";
//			for(int i = 0; i < cs.length; i++){
////				sql += "cd.id = '" + cs[i] + "'";
//				sql += "d.cgjhlsh = ? ";
//				params.put(String.valueOf(i), cs[i]);
//				if(i != cs.length - 1){
//					sql += " or ";
//				}
//			}
////			sql += " group by cd.spbh";
//		}
//		sql += " group by spbh";
		
		if(cgjhDetIds != null && cgjhDetIds.trim().length() > 0){
			String[] cs = cgjhDetIds.split(",");
			sql += "where ";
			for(int i = 0; i < cs.length; i++){
				sql += "cd.id = ? ";
				params.put(String.valueOf(i), cs[i]);
				if(i != cs.length - 1){
					sql += " or ";
				}
 			}
			sql += " group by cd.spbh";
		}
		
		List<Object[]> l = detDao.findBySQL(sql, params);
		
		List<CgjhDet> nl = new ArrayList<CgjhDet>();
		
		for(Object[] os : l){
			String spbh = (String)os[0];
			BigDecimal zdwjhsl = new BigDecimal(os[1].toString());
			BigDecimal zdwyrsl = new BigDecimal(os[2].toString());
			
			TSp sp = spDao.get(TSp.class, spbh);
			CgjhDet cd = new CgjhDet();
			cd.setSpbh(spbh);
			cd.setSpmc(sp.getSpmc());
			cd.setSpcd(sp.getSpcd());
			cd.setSppp(sp.getSppp());
			cd.setSpbz(sp.getSpbz());
			cd.setZdwjhsl(zdwjhsl);
			cd.setZdwyrsl(zdwyrsl);
			cd.setZjldwId(sp.getZjldw().getId());
			cd.setZjldwmc(sp.getZjldw().getJldwmc());
			if(sp.getCjldw() != null){
				cd.setCjldwId(sp.getCjldw().getId());
				cd.setCjldwmc(sp.getCjldw().getJldwmc());
				if(sp.getZhxs().compareTo(Constant.BD_ZERO) != 0){
					cd.setZhxs(sp.getZhxs());
					cd.setCdwjhsl(zdwjhsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
					cd.setCdwyrsl(zdwyrsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
				}
			}
			nl.add(cd);
		}
		nl.add(new CgjhDet());
		DataGrid dg = new DataGrid();
		dg.setRows(nl);
		return dg;
	}
	
	@Autowired
	public void setCgjhDao(BaseDaoI<TCgjh> cgjhDao) {
		this.cgjhDao = cgjhDao;
	}

	@Autowired
	public void setDetDao(BaseDaoI<TCgjhDet> detDao) {
		this.detDao = detDao;
	}

	@Autowired
	public void setCgxqDao(BaseDaoI<TCgxqDet> cgxqDao) {
		this.cgxqDao = cgxqDao;
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
	public void setYwzzDao(BaseDaoI<TYwzz> ywzzDao) {
		this.ywzzDao = ywzzDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
	
}
