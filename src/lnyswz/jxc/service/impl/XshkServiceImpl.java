package lnyswz.jxc.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import lnyswz.jxc.bean.CgxqDet;
import lnyswz.jxc.bean.Ck;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.Fh;
import lnyswz.jxc.bean.Kh;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.bean.Xskp;
import lnyswz.jxc.bean.Xshk;
import lnyswz.jxc.bean.Ywrk;
import lnyswz.jxc.bean.YwrkDet;
import lnyswz.jxc.model.TCgjh;
import lnyswz.jxc.model.TCgjhDet;
import lnyswz.jxc.model.TCgxqDet;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TFh;
import lnyswz.jxc.model.TFhzz;
import lnyswz.jxc.model.THw;
import lnyswz.jxc.model.TKfck;
import lnyswz.jxc.model.TKfrk;
import lnyswz.jxc.model.TKh;
import lnyswz.jxc.model.TKhDet;
import lnyswz.jxc.model.TLszz;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TXskp;
import lnyswz.jxc.model.TXskpDet;
import lnyswz.jxc.model.TXshk;
import lnyswz.jxc.model.TJsfs;
import lnyswz.jxc.model.TYszz;
import lnyswz.jxc.model.TYwrk;
import lnyswz.jxc.model.TYwrkDet;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.service.XshkServiceI;
import lnyswz.jxc.util.Constant;

/**
 * 销售回款实现类
 * @author 王文阳
 *
 */
@Service("xshkService")
public class XshkServiceImpl implements XshkServiceI {
	private Logger logger = Logger.getLogger(XshkServiceImpl.class);
	private BaseDaoI<TXshk> xshkDao;
	private BaseDaoI<TXskpDet> xskpDetDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TKh> khDao;
	private BaseDaoI<TSp> spDao;
	private BaseDaoI<TYszz> yszzDao;
	private BaseDaoI<TYwzz> ywzzDao;
	private BaseDaoI<TFhzz> fhzzDao;
	private BaseDaoI<TLszz> lszzDao;
	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public Xshk save(Xshk xshk) {
		TXshk tXshk = new TXshk();
		BeanUtils.copyProperties(xshk, tXshk);
		String lsh = LshServiceImpl.updateLsh(xshk.getBmbh(), xshk.getLxbh(), lshDao);
		tXshk.setXshklsh(lsh);
		tXshk.setCreateTime(new Date());
		tXshk.setCreateId(xshk.getCreateId());
		tXshk.setCreateName(xshk.getCreateName());
		tXshk.setIsCancel("0");
		

		String depName = depDao.load(TDepartment.class, xshk.getBmbh()).getDepName();
		tXshk.setBmmc(depName);
		
		//根据系统设定进行处理
		if(xshk.getNeedAudit() != null){
			tXshk.setNeedAudit(xshk.getNeedAudit());
			tXshk.setIsAudit("0");
		}else{
			tXshk.setNeedAudit("0");
			tXshk.setIsAudit("1");
		}
		
		String xskpDetIds= xshk.getXskpDetIds();
		TXskp xskp = null;
		int[] intDetIds = null;
		//如果从销售开票生成的销售提货，进行关联
		if(xskpDetIds != null && xskpDetIds.trim().length() > 0){
			tXshk.setFromFp("1");
			String[] strDetIds = xskpDetIds.split(",");
			intDetIds = new int[strDetIds.length];
//			Set<TXshkDet> tXshkDets = new HashSet<TXshkDet>();
			int i = 0;
			for(String detId : strDetIds){
				intDetIds[i] = Integer.valueOf(detId);
//				TXshkDet tXshkDet = xshkDetDao.load(TXshkDet.class, Integer.valueOf(detId));
//				tXshkDets.add(tXshkDet);
				i++;
			}
			xskp = xskpDetDao.load(TXskpDet.class, intDetIds[0]).getTXskp();
//			tXskp.setTXshks(tXshkDets);
			Arrays.sort(intDetIds);
		}
		
		Department dep = new Department();
		dep.setId(xshk.getBmbh());
		dep.setDepName(depName);
		
		Kh kh = new Kh();
		kh.setKhbh(xshk.getKhbh());
		kh.setKhmc(xshk.getKhmc());
		
		Fh fh = null;
		if("1".equals(xshk.getIsFh())){
			fh = new Fh();
			fh.setId(xshk.getFhId());
			fh.setFhmc(xshk.getFhmc());
		}
		
		if("1".equals(xshk.getIsSx()) && "0".equals(xshk.getIsFhth())){
			//更新授信客户应付金额
			YszzServiceImpl.updateYszzJe(dep, kh, xshk.getHjje(), Constant.UPDATE_YS_LS, yszzDao);
		}
		
		//处理商品明细
		Set<TXshkDet> tDets = new HashSet<TXshkDet>();
		ArrayList<XshkDet> xshkDets = JSON.parseObject(xshk.getDatagrid(), new TypeReference<ArrayList<XshkDet>>(){});
		for(XshkDet xshkDet : xshkDets){
			TXshkDet tDet = new TXshkDet();
			BeanUtils.copyProperties(xshkDet, tDet, new String[]{"id"});
			
			if(tDet.getCksl() == null){
				tDet.setCksl(Constant.BD_ZERO);
			}
			
			if(tDet.getKpsl() == null){
				tDet.setKpsl(Constant.BD_ZERO);
			}
			
			if(tDet.getSpje() == null){
				tDet.setSpje(Constant.BD_ZERO);
			}
			
			if(tDet.getZdwdj() == null){
				tDet.setZdwdj(Constant.BD_ZERO);
			}
			
			if(tDet.getCdwdj() == null){
				tDet.setCdwdj(Constant.BD_ZERO);
			}
			
			if("".equals(xshkDet.getCjldwId()) || null == xshkDet.getCjldwId()){
				tDet.setCdwdj(Constant.BD_ZERO);
				tDet.setCdwsl(Constant.BD_ZERO);
				tDet.setZhxs(Constant.BD_ZERO);
			}else{
				if(xshkDet.getZhxs().compareTo(Constant.BD_ZERO) == 0){
					tDet.setCdwdj(Constant.BD_ZERO);
					tDet.setCdwsl(Constant.BD_ZERO);
				}
			}
			
			tDet.setTXshk(tXshk);
			tDets.add(tDet);

			Sp sp = new Sp();
			BeanUtils.copyProperties(xshkDet, sp);
			
			if("0".equals(xshk.getIsFhth())){
				LszzServiceImpl.updateLszzSl(sp, dep, xshkDet.getZdwsl(), xshkDet.getSpje(), Constant.UPDATE_RK, lszzDao);
				if("1".equals(xshk.getIsFh())){
					FhzzServiceImpl.updateFhzzSl(sp, dep, fh, tDet.getZdwsl(), Constant.UPDATE_RK, fhzzDao);
				}
			}
			
			if(intDetIds != null){
				for(int detId : intDetIds){
					TXskpDet xskpDet = xskpDetDao.load(TXskpDet.class, detId);
					if(xshkDet.getSpbh().equals(xskpDet.getSpbh())){
						xskpDet.setThsl(xskpDet.getThsl().add(xshkDet.getZdwsl()));
						break;
//						BigDecimal wksl = xshkDet.getZdwsl().subtract(xshkDet.getKpsl());
//						xshkDets.add(xshkDet);
//						if(kpsl.compareTo(wksl) == 1){
//							xshkDet.setKpsl(xshkDet.getKpsl().add(wksl));
//							kpsl = kpsl.subtract(wksl);
//						}else{
//							xshkDet.setKpsl(xshkDet.getKpsl().add(kpsl));
//							tDet.setLastThsl(kpsl);
//							break;
//						}
					}
				}
			}
		}
		tXshk.setTXshkDets(tDets);
		xshkDao.save(tXshk);

		if(intDetIds != null){
			xskp.getTXshks().addAll(tDets);
		}
		
		OperalogServiceImpl.addOperalog(xshk.getCreateId(), xshk.getBmbh(), xshk.getMenuId(), tXshk.getXshklsh(), 
				"生成销售提货单", operalogDao);
		
		Xshk rXshk = new Xshk();
		rXshk.setXshklsh(lsh);
		return rXshk;
		
	}

//	private void updateKhSx(String bmbh, String khbh, BigDecimal hjje) {
//		String hql = "from TKhDet t where t.TDepartment.id = :depId and t.TKh.khbh = :khbh";
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("depId", bmbh);
//		params.put("khbh", khbh);
//		TKhDet tDet = khDetDao.get(hql, params);
//		tDet.setYfje(tDet.getYfje().add(hjje));
//	}
	
	@Override
	public void cancelXshk(Xshk xshk) {
		Date now = new Date();
		String lsh = LshServiceImpl.updateLsh(xshk.getBmbh(), xshk.getLxbh(), lshDao);
		
		//获取原单据信息
		TXshk yTXshk = xshkDao.load(TXshk.class, xshk.getXshklsh());
		//新增冲减单据信息
		TXshk tXshk = new TXshk();
		BeanUtils.copyProperties(yTXshk, tXshk);

		//更新原单据信息
		yTXshk.setIsCancel("1");
		yTXshk.setCancelId(xshk.getCancelId());
		yTXshk.setCancelName(xshk.getCancelName());
		yTXshk.setCancelTime(now);
		
		tXshk.setXshklsh(lsh);
		tXshk.setCjXshklsh(yTXshk.getXshklsh());
		tXshk.setCreateId(xshk.getCancelId());
		tXshk.setCreateTime(now);
		tXshk.setCreateName(xshk.getCancelName());
		tXshk.setIsCancel("1");
		tXshk.setCancelId(xshk.getCancelId());
		tXshk.setCancelName(xshk.getCancelName());
		tXshk.setCancelTime(now);
		tXshk.setHjje(yTXshk.getHjje().negate());
		
		Department dep = new Department();
		dep.setId(tXshk.getBmbh());
		dep.setDepName(tXshk.getBmmc());
		
		Kh kh = new Kh();
		kh.setKhbh(tXshk.getKhbh());
		kh.setKhmc(tXshk.getKhmc());
		
		Fh fh= null;
		if("1".equals(tXshk.getIsFh())){
			fh = new Fh();
			fh.setId(tXshk.getFhId());
			fh.setFhmc(tXshk.getFhmc());
		}
		
		if("1".equals(tXshk.getIsSx()) && "0".equals(tXshk.getIsFhth())){
			//更新授信客户应付金额
			YszzServiceImpl.updateYszzJe(dep, kh, tXshk.getHjje(), Constant.UPDATE_YS_LS, yszzDao);
		}
		
		Set<TXshkDet> yTXshkDets = yTXshk.getTXshkDets();
		Set<TXshkDet> tDets = new HashSet<TXshkDet>();
		for(TXshkDet yTDet : yTXshkDets){
			TXshkDet tDet = new TXshkDet();
			BeanUtils.copyProperties(yTDet, tDet, new String[]{"id", "TKfcks", "TXskps"});
			tDet.setZdwsl(yTDet.getZdwsl().negate());
			if(yTDet.getCdwsl() != null){
				tDet.setCdwsl(yTDet.getCdwsl().negate());
			}
			tDet.setSpje(yTDet.getSpje().negate());
			tDet.setTXshk(tXshk);
			tDets.add(tDet);
			
			Set<TXskp> xskps = yTDet.getTXskps();
			if(xskps != null && xskps.size() > 0){
				TXskp xskp = xskps.iterator().next();
				xskp.getTXshks().remove(yTDet);
				Set<TXskpDet> xskpDets = xskp.getTXskpDets();
				for(TXskpDet xskpDet : xskpDets){
					if(xskpDet.getSpbh().equals(yTDet.getSpbh())){
						xskpDet.setThsl(xskpDet.getThsl().subtract(yTDet.getZdwsl()));
						yTDet.setKpsl(Constant.BD_ZERO);
						break;
					}
				}
				tDet.setKpsl(Constant.BD_ZERO);
				
			}

			Sp sp = new Sp();
			BeanUtils.copyProperties(yTDet, sp);

			if("0".equals(yTXshk.getIsFhth())){
				LszzServiceImpl.updateLszzSl(sp, dep, tDet.getZdwsl(), tDet.getSpje(), Constant.UPDATE_RK, lszzDao);
				if("1".equals(yTXshk.getIsFh())){
					FhzzServiceImpl.updateFhzzSl(sp, dep, fh, tDet.getZdwsl(), Constant.UPDATE_RK, fhzzDao);
				}
			}
		}

		tXshk.setTXshkDets(tDets);
		xshkDao.save(tXshk);
		
		OperalogServiceImpl.addOperalog(xshk.getCancelId(), xshk.getBmbh(), xshk.getMenuId(), tXshk.getCjXshklsh() + "/" + tXshk.getXshklsh(), 
			"取消销售提货单", operalogDao);
		
	}
	
	@Override
	public DataGrid printXshk(Xshk xshk) {
		DataGrid datagrid = new DataGrid();
		TXshk tXshk = xshkDao.load(TXshk.class, xshk.getXshklsh());
		
		List<XshkDet> nl = new ArrayList<XshkDet>();
		int j = 0;
		Set<TXskp> xskps = null;
		for (TXshkDet yd : tXshk.getTXshkDets()) {
			XshkDet xshkDet = new XshkDet();
			BeanUtils.copyProperties(yd, xshkDet);
			nl.add(xshkDet);
			if(j == 0){
				xskps = yd.getTXskps();
			}
			j++;
		}
		int num = nl.size();
		if (num < Constant.REPORT_NUMBER) {
			for (int i = 0; i < (Constant.REPORT_NUMBER - num); i++) {
				nl.add(new XshkDet());
			}
		}
				
		String xskplsh = "";
		if(xskps != null && xskps.size() > 0){
			xskplsh += xskps.iterator().next().getXskplsh();
		}
		
		String bz = "";
		if(tXshk.getYwymc() != null){
			bz = " " + tXshk.getYwymc().trim();
		}
		if("0".equals(tXshk.getThfs())){
			bz += " 送货：";
		}else{
			bz += " 自提：";
		}
		if(tXshk.getShdz() != null){
			bz += " " + tXshk.getShdz();
		}
		if(tXshk.getThr() != null){
			bz += " " + tXshk.getThr();
		}
		if(tXshk.getCh() != null){
			bz += " " + tXshk.getCh();
		}
		bz += xskplsh;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "销   售   提   货   单");
		map.put("head", Constant.XSTH_HEAD.get(tXshk.getBmbh()));
		map.put("footer", Constant.XSTH_FOOT.get(tXshk.getBmbh()));
		if("1".equals(Constant.XSTH_PRINT_LSBZ.get(xshk.getBmbh()))){
			map.put("bmmc", tXshk.getBmmc() + "(" + (tXshk.getToFp().equals("1") ? "是" : "否") + ")");
		}else{
			map.put("bmmc", tXshk.getBmmc());
		}
		map.put("createTime", DateUtil.dateToString(tXshk.getCreateTime(), DateUtil.DATETIME_NOSECOND_PATTERN));
		map.put("xshklsh", tXshk.getXshklsh());
		map.put("khmc", tXshk.getKhmc());
		map.put("khbh", tXshk.getKhbh());
		map.put("fhmc", tXshk.getFhmc() != null ? "分户：" + tXshk.getFhmc() : "");
		map.put("ckmc", tXshk.getCkmc());
		map.put("hjje", tXshk.getHjje());
		map.put("hjsl", tXshk.getHjsl());
		map.put("bz", tXshk.getBz() + " " + bz.trim());
		map.put("printName", xshk.getCreateName());
		map.put("printTime", DateUtil.dateToString(new Date()));
		datagrid.setObj(map);
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid datagrid(Xshk xshk) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TXshk t where t.bmbh = :bmbh and t.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", xshk.getBmbh());

		//默认显示当前月数据
		if(xshk.getCreateTime() != null){
			params.put("createTime", xshk.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		
		if(xshk.getSearch() != null){
			hql += " and (t.xshklsh like :search or t.khmc like :search or t.bz like :search or t.ywymc like :search)"; 
			params.put("search", "%" + xshk.getSearch() + "%");
			
		}
		
		//销售提货流程如果是业务员只有本人可以查询
		//在销售开票流程查询数据，未取消，未开票、不是分户
		if(xshk.getFromOther() != null){
			hql += " and t.isCancel = '0' and fhId = null and isKp = '0'";
		}
		
		if(xshk.getYwyId() > 0){
			hql += " and t.createId = :ywyId";
			params.put("ywyId", xshk.getYwyId());
		}
		
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		
		List<TXshk> l = xshkDao.find(hql, params, xshk.getPage(), xshk.getRows());
		List<Xshk> nl = new ArrayList<Xshk>();
		for(TXshk t : l){
			Xshk c = new Xshk();
			BeanUtils.copyProperties(t, c);
			
			//默认置0
			c.setIsTh("0");
			//String xskplshs = "";
			Set<String> xskplshs = new HashSet<String>();
			for(TXshkDet tXshkDet : t.getTXshkDets()){
				//如销售提货单已进行出库，显示关联出库单流水号
				if(tXshkDet.getTKfcks() != null && tXshkDet.getTKfcks().size() > 0){
					if("0".equals(c.getIsTh())){
						c.setIsTh("1");
					}
					//break;
				}
				//显示关联销售开票单流水号
				Set<TXskp> tXskps = tXshkDet.getTXskps();
				if(tXskps != null && tXskps.size() > 0){
					for(TXskp tXskp : tXskps){
						xskplshs.add(tXskp.getXskplsh());
					}
				}
			}
			
			String r = xskplshs.toString();
			
			c.setXskplsh(r.length() == 2 ? "" : r.substring(1, r.length() - 1));
			
			nl.add(c);
		}
		datagrid.setTotal(xshkDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(String xshklsh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TXshkDet t where t.TXshk.xshklsh = :xshklsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("xshklsh", xshklsh);
		List<TXshkDet> l = detDao.find(hql, params);
		List<XshkDet> nl = new ArrayList<XshkDet>();
		for(TXshkDet t : l){
			XshkDet c = new XshkDet();
			BeanUtils.copyProperties(t, c);
			
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid datagridDet(Xshk xshk) {
		//库房出库流程查询，保管员只可查看本人负责商品记录，其他(总账员)可以查看全部
		DataGrid datagrid = new DataGrid();
		String hql = "from TXshkDet t where t.TXshk.bmbh = :bmbh and t.TXshk.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", xshk.getBmbh());
		if(xshk.getCreateTime() != null){
			params.put("createTime", xshk.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		if(xshk.getSearch() != null){
			hql += " and (t.TXshk.xshklsh like :search or t.TXshk.khmc like :search or t.TXshk.bz like :search or t.TXshk.ywymc like :search)"; 
			params.put("search", "%" + xshk.getSearch() + "%");
			
		}
		
		//只查询未完成的有效数据
		if(xshk.getFromOther() != null){
			hql += " and t.TXshk.isCancel = '0'";
			if(Constant.NEED_AUDIT.equals("1")){
				hql += " and t.TXshk.isAudit = '1'";
			}
		}
		
		//保管员筛选
		if(xshk.getBgyId() > 0){
			hql += " and t.spbh in (select sb.spbh from TSpBgy sb where sb.depId = :depId and sb.bgyId = :bgyId and sb.ckId = :ckId)";
			params.put("depId", xshk.getBmbh());
			params.put("ckId", xshk.getCkId());
			params.put("bgyId", xshk.getBgyId());
		}
		
		//通过isKp字段来判断该查询来源，
		if("1".equals(xshk.getIsKp())){
			hql += " and t.TXshk.isLs = '1' and t.TXshk.isKp = '0'";
			//hql += " and t.zdwsl <> (select isnull(sum(tkd.zdwsl), 0) from TXskpDet tkd where tkd.TXskp in elements(t.TXskps) and tkd.spbh = t.spbh)";
			hql += " and t.zdwsl <> t.kpsl";
		}else{
			hql += " and t.TXshk.isZs = '0' and ((t.TXshk.isFh = '0' and t.TXshk.isFhth = '0') or (t.TXshk.isFh = '1' and t.TXshk.isFhth = '1'))";
			//hql += " and t.zdwsl <> (select isnull(sum(tkd.zdwsl), 0) from TKfckDet tkd where tkd.TKfck in elements(t.TKfcks) and tkd.spbh = t.spbh)";
			hql += " and t.zdwsl <> t.cksl";
		}
		
		String countHql = "select count(*) " + hql;
		hql += " order by t.TXshk.createTime desc ";
		List<TXshkDet> l = detDao.find(hql, params, xshk.getPage(), xshk.getRows());
		List<Xshk> nl = new ArrayList<Xshk>();
		for(TXshkDet t : l){
			Xshk c = new Xshk();
			BeanUtils.copyProperties(t, c);
			
			if(t.getTXshk().getKhbh() != null && t.getTXshk().getKhbh().trim().length() > 0){
				TKh tKh = khDao.load(TKh.class, t.getTXshk().getKhbh());
				c.setSh(tKh.getSh());
				c.setKhh(tKh.getKhh());
				c.setDzdh(tKh.getDzdh());
			}
			TXshk tXshk = t.getTXshk();
			BeanUtils.copyProperties(tXshk, c);

//			if(t.getTKfcks() != null){
//				//c.setKfcklshs(t.getTKfcks().getKfcklsh());
//				if("1".equals(xshk.getIsKp())){
//					c.setZdwytsl(getYksl(t.getTXshk().getXshklsh(), t.getSpbh()));
//				}else{
//					c.setZdwytsl(getYtsl(t.getTXshk().getXshklsh(), t.getSpbh()));
//				}
//			}
			nl.add(c);
		}
		datagrid.setTotal(detDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	private BigDecimal getYtsl(String xshklsh, String spbh) {
		String detHql = "select isnull(sum(kfDet.zdwsl), 0) from t_kfck_det kfDet "
				+ "left join t_xshk_kfck xk on kfDet.kfcklsh = xk.kfcklsh "
				+ "left join t_xshk_det thDet on thDet.id = xk.xshkdetId and thDet.spbh = kfDet.spbh "
				+ "where thDet.xshklsh = ? and thDet.spbh = ?";
		Map<String, Object> detParams = new HashMap<String, Object>();
		detParams.put("0", xshklsh);
		detParams.put("1", spbh);
		BigDecimal yrsl = new BigDecimal(detDao.getBySQL(detHql, detParams).toString());
		return yrsl;
	}
	
	private BigDecimal getYksl(String xshklsh, String spbh) {
		String detHql = "select isnull(sum(kpDet.zdwsl), 0) from t_xskp_det kpDet "
				+ "left join t_xshk_xskp xx on kpDet.xskplsh = xx.xskplsh "
				+ "left join t_xshk_det thDet on thDet.id = xx.xshkdetId and thDet.spbh = kpDet.spbh "
				+ "where thDet.xshklsh = ? and thDet.spbh = ?";
		Map<String, Object> detParams = new HashMap<String, Object>();
		detParams.put("0", xshklsh);
		detParams.put("1", spbh);
		BigDecimal yksl = new BigDecimal(detDao.getBySQL(detHql, detParams).toString());
		return yksl;
	}
	
		
	@Override
	public DataGrid toKfck(Xshk xshk){
//		String sql = "select xd.spbh, isnull(max(xd.zdwsl), 0) zdwthsl, isnull(sum(kd.zdwsl), 0) zdwytsl from t_xshk_det xd " +
//				"left join t_xshk_kfck xk on xd.id = xk.xshkdetId " +
//				"left join t_kfck_det kd on xk.kfcklsh = kd.kfcklsh and kd.spbh = xd.spbh ";
		String sql = "select spbh, isnull(sum(zdwsl), 0) zdwthsl, isnull(sum(cksl), 0) zdwytsl from t_xshk_det ";
		Map<String, Object> params = new HashMap<String, Object>();
		
		String xshkDetIds = xshk.getXshkDetIds();
		if(xshkDetIds != null && xshkDetIds.trim().length() > 0){
//			String[] xs = xshkDetIds.split(",");
			sql += "where id in (" + xshkDetIds + ")";
//			sql += "where ";
//			for(int i = 0; i < xs.length; i++){
//				sql += " xd.id = ? ";
//				params.put(String.valueOf(i), xs[i]);
//				if(i != xs.length - 1){
//					sql += " or ";
//				}
// 			}
		}
		sql += " group by spbh";
		
		List<Object[]> l = detDao.findBySQL(sql, params);
		
		List<XshkDet> nl = new ArrayList<XshkDet>();
		
		for(Object[] os : l){
			String spbh = (String)os[0];
			BigDecimal zdwthsl = new BigDecimal(os[1].toString());
			BigDecimal zdwytsl = new BigDecimal(os[2].toString());
			
			TSp sp = spDao.get(TSp.class, spbh);
			XshkDet xd = new XshkDet();
			BeanUtils.copyProperties(sp, xd);
			xd.setZjldwId(sp.getZjldw().getId());
			xd.setZjldwmc(sp.getZjldw().getJldwmc());
			xd.setZdwthsl(zdwthsl);
			xd.setZdwytsl(zdwytsl);
			if(sp.getCjldw() != null){
				xd.setCjldwId(sp.getCjldw().getId());
				xd.setCjldwmc(sp.getCjldw().getJldwmc());
				xd.setZhxs(sp.getZhxs());
				if(sp.getZhxs().compareTo(Constant.BD_ZERO) != 0){
					xd.setCdwthsl(zdwthsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
					xd.setCdwytsl(zdwytsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
				}else{
					xd.setCdwthsl(Constant.BD_ZERO);
					xd.setCdwytsl(Constant.BD_ZERO);
				}
			}
			nl.add(xd);
		}
		nl.add(new XshkDet());
		DataGrid dg = new DataGrid();
		dg.setRows(nl);
		return dg;
		
	}
	
	@Override
	public void updateLock(Xshk xshk) {
		TXshk tXshk = xshkDao.get(TXshk.class, xshk.getXshklsh());
		tXshk.setLockId(xshk.getLockId());
		tXshk.setLockTime(new Date());
		tXshk.setLockName(xshk.getLockName());
		tXshk.setLocked("1");
		OperalogServiceImpl.addOperalog(xshk.getLockId(), xshk.getBmbh(), xshk.getMenuId(), xshk.getXshklsh(), 
				"锁定销售提货", operalogDao);
	}
	
	@Override
	public void updateUnlock(Xshk xshk) {
		TXshk tXshk = xshkDao.get(TXshk.class, xshk.getXshklsh());
		tXshk.setLockId(xshk.getLockId());
		tXshk.setLockTime(new Date());
		tXshk.setLockName(xshk.getLockName());
		tXshk.setLocked("0");
		OperalogServiceImpl.addOperalog(xshk.getLockId(), xshk.getBmbh(), xshk.getMenuId(), xshk.getXshklsh(), 
				"解锁销售提货", operalogDao);
	}
	
	@Override
	public DataGrid toXskp(String xshkDetIds){
//		String sql = "select xd.spbh, isnull(sum(xd.zdwsl), 0) zdwthsl, isnull(max(kd.zdwsl), 0) zdwytsl from t_xshk_det xd " +
//				"left join t_xshk_xskp xk on xd.id = xk.xshkdetId " +
//				"left join t_xskp_det kd on xk.xskplsh = kd.xskplsh and kd.spbh = xd.spbh ";
		String sql = "select spbh, isnull(sum(zdwsl), 0) zdwthsl, isnull(sum(kpsl), 0) zdwytsl from t_xshk_det ";
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(xshkDetIds != null && xshkDetIds.trim().length() > 0){
//			String[] xs = xshkDetIds.split(",");
			sql += "where id in (" + xshkDetIds + ")";
//			for(int i = 0; i < xs.length; i++){
//				sql += " xd.id = ? ";
//				params.put(String.valueOf(i), xs[i]);
//				if(i != xs.length - 1){
//					sql += " or ";
//				}
// 			}
		}
		sql += " group by spbh";
		
		List<Object[]> l = detDao.findBySQL(sql, params);
		
		List<XshkDet> nl = new ArrayList<XshkDet>();
		
		for(Object[] os : l){
			String spbh = (String)os[0];
			BigDecimal zdwthsl = new BigDecimal(os[1].toString());
			BigDecimal zdwytsl = new BigDecimal(os[2].toString());
			
			TSp sp = spDao.get(TSp.class, spbh);
			XshkDet xd = new XshkDet();
			BeanUtils.copyProperties(sp, xd);
			xd.setZjldwId(sp.getZjldw().getId());
			xd.setZjldwmc(sp.getZjldw().getJldwmc());
			xd.setZdwthsl(zdwthsl);
			xd.setZdwytsl(zdwytsl);
			if(sp.getCjldw() != null){
				xd.setCjldwId(sp.getCjldw().getId());
				xd.setCjldwmc(sp.getCjldw().getJldwmc());
				xd.setZhxs(sp.getZhxs());
				if(sp.getZhxs().compareTo(Constant.BD_ZERO) != 0){
					xd.setCdwthsl(zdwthsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
					xd.setCdwytsl(zdwytsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
				}else{
					xd.setCdwthsl(Constant.BD_ZERO);
					xd.setCdwytsl(Constant.BD_ZERO);
				}
			}
			nl.add(xd);
		}
		//nl.add(new XshkDet());
		DataGrid dg = new DataGrid();
		dg.setRows(nl);
		return dg;
		
	}
	
	
	@Override
	public DataGrid getSpkc(Xshk xshk) {
		DataGrid dg = new DataGrid();
		List<ProBean> lists = new ArrayList<ProBean>();
		
		List<ProBean> yw = YwzzServiceImpl.getZzsl(xshk.getBmbh(), xshk.getSpbh(), xshk.getCkId(), ywzzDao);
		if(yw != null){
			lists.addAll(yw);
		}
		
//		List<ProBean> kf = KfzzServiceImpl.getZzsl(kfck.getBmbh(), kfck.getSpbh(), kfck.getCkId(), null, null, kfzzDao);
//		if(kf != null){
//			lists.addAll(kf);
//		}
//		List<ProBean> pc = KfzzServiceImpl.getZzsl(kfck.getBmbh(), kfck.getSpbh(), kfck.getCkId(), null, "", kfzzDao);
//		if(pc != null){
//			lists.addAll(pc);
//		}
		if(xshk.getFhId() != null && xshk.getFhId().trim().length() > 0){
			List<ProBean> fh = FhzzServiceImpl.getZzsl(xshk.getBmbh(), xshk.getSpbh(), xshk.getFhId(), fhzzDao);
			if(fh != null){
				lists.addAll(fh);
			}
		}
		
		dg.setRows(lists);
		dg.setTotal((long)lists.size());
		return dg;
	}
	
	
	@Autowired
	public void setXshkDao(BaseDaoI<TXshk> xshkDao) {
		this.xshkDao = xshkDao;
	}

	@Autowired
	public void setDetDao(BaseDaoI<TXshkDet> detDao) {
		this.detDao = detDao;
	}

	@Autowired
	public void setXskpDetDao(BaseDaoI<TXskpDet> xskpDetDao) {
		this.xskpDetDao = xskpDetDao;
	}

	@Autowired
	public void setLshDao(BaseDaoI<TLsh> lshDao) {
		this.lshDao = lshDao;
	}

	@Autowired
	public void setKhDao(BaseDaoI<TKh> khDao) {
		this.khDao = khDao;
	}
	
	@Autowired
	public void setSpDao(BaseDaoI<TSp> spDao) {
		this.spDao = spDao;
	}

	@Autowired
	public void setDepDao(BaseDaoI<TDepartment> depDao) {
		this.depDao = depDao;
	}

	@Autowired
	public void setYszzDao(BaseDaoI<TYszz> yszzDao) {
		this.yszzDao = yszzDao;
	}

	@Autowired
	public void setYwzzDao(BaseDaoI<TYwzz> ywzzDao) {
		this.ywzzDao = ywzzDao;
	}

	@Autowired
	public void setFhzzDao(BaseDaoI<TFhzz> fhzzDao) {
		this.fhzzDao = fhzzDao;
	}
	
	@Autowired
	public void setLszzDao(BaseDaoI<TLszz> lszzDao) {
		this.lszzDao = lszzDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
}