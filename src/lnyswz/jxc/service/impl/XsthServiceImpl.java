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
import lnyswz.jxc.bean.Xsth;
import lnyswz.jxc.bean.XsthDet;
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
import lnyswz.jxc.model.TXsth;
import lnyswz.jxc.model.TXsthDet;
import lnyswz.jxc.model.TJsfs;
import lnyswz.jxc.model.TYszz;
import lnyswz.jxc.model.TYwrk;
import lnyswz.jxc.model.TYwrkDet;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.service.XsthServiceI;
import lnyswz.jxc.util.Constant;

/**
 * 销售提货实现类
 * @author 王文阳
 *
 */
@Service("xsthService")
public class XsthServiceImpl implements XsthServiceI {
	private Logger logger = Logger.getLogger(XsthServiceImpl.class);
	private BaseDaoI<TXsth> xsthDao;
	private BaseDaoI<TXsthDet> detDao;
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
	public Xsth save(Xsth xsth) {
		TXsth tXsth = new TXsth();
		BeanUtils.copyProperties(xsth, tXsth);
		String lsh = LshServiceImpl.updateLsh(xsth.getBmbh(), xsth.getLxbh(), lshDao);
		tXsth.setXsthlsh(lsh);
		tXsth.setCreateTime(new Date());
		tXsth.setCreateId(xsth.getCreateId());
		tXsth.setCreateName(xsth.getCreateName());
		tXsth.setIsCancel("0");
		tXsth.setIsHk("0");
		tXsth.setIsKp("0");
		tXsth.setLocked("0");
		tXsth.setFromFp("0");

		String depName = depDao.load(TDepartment.class, xsth.getBmbh()).getDepName();
		tXsth.setBmmc(depName);
		
		tXsth.setNeedAudit("0");
		if(tXsth.getNeedAudit().equals("1")){
			tXsth.setIsAudit("0");
		}else{
			tXsth.setIsAudit("1");
		}
		
		String xskpDetIds= xsth.getXskpDetIds();
		TXskp xskp = null;
		int[] intDetIds = null;
		//如果从销售开票生成的销售提货，进行关联
		if(xskpDetIds != null && xskpDetIds.trim().length() > 0){
			tXsth.setFromFp("1");
			String[] strDetIds = xskpDetIds.split(",");
			intDetIds = new int[strDetIds.length];
//			Set<TXsthDet> tXsthDets = new HashSet<TXsthDet>();
			int i = 0;
			for(String detId : strDetIds){
				intDetIds[i] = Integer.valueOf(detId);
//				TXsthDet tXsthDet = xsthDetDao.load(TXsthDet.class, Integer.valueOf(detId));
//				tXsthDets.add(tXsthDet);
				i++;
			}
			xskp = xskpDetDao.load(TXskpDet.class, intDetIds[0]).getTXskp();
//			tXskp.setTXsths(tXsthDets);
			Arrays.sort(intDetIds);
		}
		
		Department dep = new Department();
		dep.setId(xsth.getBmbh());
		dep.setDepName(depName);
		
		Kh kh = new Kh();
		kh.setKhbh(xsth.getKhbh());
		kh.setKhmc(xsth.getKhmc());
		
		Fh fh = null;
		if("1".equals(xsth.getIsFh())){
			fh = new Fh();
			fh.setId(xsth.getFhId());
			fh.setFhmc(xsth.getFhmc());
		}
		
		if("1".equals(xsth.getIsSx()) && "0".equals(xsth.getIsFhth())){
			//更新授信客户应付金额
			YszzServiceImpl.updateYszzJe(dep, kh, xsth.getHjje(), Constant.UPDATE_YS_LS, yszzDao);
		}
		
		//处理商品明细
		Set<TXsthDet> tDets = new HashSet<TXsthDet>();
		ArrayList<XsthDet> xsthDets = JSON.parseObject(xsth.getDatagrid(), new TypeReference<ArrayList<XsthDet>>(){});
		for(XsthDet xsthDet : xsthDets){
			TXsthDet tDet = new TXsthDet();
			BeanUtils.copyProperties(xsthDet, tDet, new String[]{"id"});
			
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
			
			if("".equals(xsthDet.getCjldwId()) || null == xsthDet.getCjldwId()){
				tDet.setCdwdj(Constant.BD_ZERO);
				tDet.setCdwsl(Constant.BD_ZERO);
				tDet.setZhxs(Constant.BD_ZERO);
			}else{
				if(xsthDet.getZhxs().compareTo(Constant.BD_ZERO) == 0){
					tDet.setCdwdj(Constant.BD_ZERO);
					tDet.setCdwsl(Constant.BD_ZERO);
				}
			}
			
			tDet.setTXsth(tXsth);
			tDets.add(tDet);

			Sp sp = new Sp();
			BeanUtils.copyProperties(xsthDet, sp);
			
			if("0".equals(xsth.getIsFhth())){
				LszzServiceImpl.updateLszzSl(sp, dep, xsthDet.getZdwsl(), xsthDet.getSpje(), Constant.UPDATE_RK, lszzDao);
				if("1".equals(xsth.getIsFh())){
					FhzzServiceImpl.updateFhzzSl(sp, dep, fh, tDet.getZdwsl(), Constant.UPDATE_RK, fhzzDao);
				}
			}
			
			if(intDetIds != null){
				for(int detId : intDetIds){
					TXskpDet xskpDet = xskpDetDao.load(TXskpDet.class, detId);
					if(xsthDet.getSpbh().equals(xskpDet.getSpbh())){
						xskpDet.setThsl(xskpDet.getThsl().add(xsthDet.getZdwsl()));
						break;
//						BigDecimal wksl = xsthDet.getZdwsl().subtract(xsthDet.getKpsl());
//						xsthDets.add(xsthDet);
//						if(kpsl.compareTo(wksl) == 1){
//							xsthDet.setKpsl(xsthDet.getKpsl().add(wksl));
//							kpsl = kpsl.subtract(wksl);
//						}else{
//							xsthDet.setKpsl(xsthDet.getKpsl().add(kpsl));
//							tDet.setLastThsl(kpsl);
//							break;
//						}
					}
				}
			}
		}
		tXsth.setTXsthDets(tDets);
		xsthDao.save(tXsth);

		if(intDetIds != null){
			xskp.getTXsths().addAll(tDets);
		}
		
		OperalogServiceImpl.addOperalog(xsth.getCreateId(), xsth.getBmbh(), xsth.getMenuId(), tXsth.getXsthlsh(), 
				"生成销售提货单", operalogDao);
		
		Xsth rXsth = new Xsth();
		rXsth.setXsthlsh(lsh);
		return rXsth;
		
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
	public void cancelXsth(Xsth xsth) {
		Date now = new Date();
		String lsh = LshServiceImpl.updateLsh(xsth.getBmbh(), xsth.getLxbh(), lshDao);
		
		//获取原单据信息
		TXsth yTXsth = xsthDao.load(TXsth.class, xsth.getXsthlsh());
		//新增冲减单据信息
		TXsth tXsth = new TXsth();
		BeanUtils.copyProperties(yTXsth, tXsth);

		//更新原单据信息
		yTXsth.setIsCancel("1");
		yTXsth.setCancelId(xsth.getCancelId());
		yTXsth.setCancelName(xsth.getCancelName());
		yTXsth.setCancelTime(now);
		
		tXsth.setXsthlsh(lsh);
		tXsth.setCjXsthlsh(yTXsth.getXsthlsh());
		tXsth.setCreateId(xsth.getCancelId());
		tXsth.setCreateTime(now);
		tXsth.setCreateName(xsth.getCancelName());
		tXsth.setIsCancel("1");
		tXsth.setCancelId(xsth.getCancelId());
		tXsth.setCancelName(xsth.getCancelName());
		tXsth.setCancelTime(now);
		tXsth.setHjje(yTXsth.getHjje().negate());
		
		Department dep = new Department();
		dep.setId(tXsth.getBmbh());
		dep.setDepName(tXsth.getBmmc());
		
		Kh kh = new Kh();
		kh.setKhbh(tXsth.getKhbh());
		kh.setKhmc(tXsth.getKhmc());
		
		Fh fh= null;
		if("1".equals(tXsth.getIsFh())){
			fh = new Fh();
			fh.setId(tXsth.getFhId());
			fh.setFhmc(tXsth.getFhmc());
		}
		
		if("1".equals(tXsth.getIsSx()) && "0".equals(tXsth.getIsFhth())){
			//更新授信客户应付金额
			YszzServiceImpl.updateYszzJe(dep, kh, tXsth.getHjje(), Constant.UPDATE_YS_LS, yszzDao);
		}
		
		Set<TXsthDet> yTXsthDets = yTXsth.getTXsthDets();
		Set<TXsthDet> tDets = new HashSet<TXsthDet>();
		for(TXsthDet yTDet : yTXsthDets){
			TXsthDet tDet = new TXsthDet();
			BeanUtils.copyProperties(yTDet, tDet, new String[]{"id", "TKfcks", "TXskps"});
			tDet.setZdwsl(yTDet.getZdwsl().negate());
			if(yTDet.getCdwsl() != null){
				tDet.setCdwsl(yTDet.getCdwsl().negate());
			}
			tDet.setSpje(yTDet.getSpje().negate());
			tDet.setTXsth(tXsth);
			tDets.add(tDet);
			
			Set<TXskp> xskps = yTDet.getTXskps();
			if(xskps != null && xskps.size() > 0){
				TXskp xskp = xskps.iterator().next();
				xskp.getTXsths().remove(yTDet);
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

			if("0".equals(yTXsth.getIsFhth())){
				LszzServiceImpl.updateLszzSl(sp, dep, tDet.getZdwsl(), tDet.getSpje(), Constant.UPDATE_RK, lszzDao);
				if("1".equals(yTXsth.getIsFh())){
					FhzzServiceImpl.updateFhzzSl(sp, dep, fh, tDet.getZdwsl(), Constant.UPDATE_RK, fhzzDao);
				}
			}
		}

		tXsth.setTXsthDets(tDets);
		xsthDao.save(tXsth);
		
		OperalogServiceImpl.addOperalog(xsth.getCancelId(), xsth.getBmbh(), xsth.getMenuId(), tXsth.getCjXsthlsh() + "/" + tXsth.getXsthlsh(), 
			"取消销售提货单", operalogDao);
		
	}
	
	@Override
	public DataGrid printXsth(Xsth xsth) {
		DataGrid datagrid = new DataGrid();
		TXsth tXsth = xsthDao.load(TXsth.class, xsth.getXsthlsh());
		
		List<XsthDet> nl = new ArrayList<XsthDet>();
		int j = 0;
		Set<TXskp> xskps = null;
		for (TXsthDet yd : tXsth.getTXsthDets()) {
			XsthDet xsthDet = new XsthDet();
			BeanUtils.copyProperties(yd, xsthDet);
			nl.add(xsthDet);
			if(j == 0){
				xskps = yd.getTXskps();
			}
			j++;
		}
		int num = nl.size();
		if (num < Constant.REPORT_NUMBER) {
			for (int i = 0; i < (Constant.REPORT_NUMBER - num); i++) {
				nl.add(new XsthDet());
			}
		}
				
		String xskplsh = "";
		if(xskps != null && xskps.size() > 0){
			xskplsh += xskps.iterator().next().getXskplsh();
		}
		
		String bz = "";
		if(tXsth.getYwymc() != null){
			bz = " " + tXsth.getYwymc().trim();
		}
		if("0".equals(tXsth.getThfs())){
			bz += " 送货：";
		}else{
			bz += " 自提：";
		}
		if(tXsth.getShdz() != null){
			bz += " " + tXsth.getShdz();
		}
		if(tXsth.getThr() != null){
			bz += " " + tXsth.getThr();
		}
		if(tXsth.getCh() != null){
			bz += " " + tXsth.getCh();
		}
		bz += xskplsh;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "销   售   提   货   单");
		map.put("head", Constant.XSTH_HEAD.get(tXsth.getBmbh()));
		map.put("footer", Constant.XSTH_FOOT.get(tXsth.getBmbh()));
		if("1".equals(Constant.XSTH_PRINT_LSBZ.get(xsth.getBmbh()))){
			map.put("bmmc", tXsth.getBmmc() + "(" + (tXsth.getToFp().equals("1") ? "是" : "否") + ")");
		}else{
			map.put("bmmc", tXsth.getBmmc());
		}
		map.put("createTime", DateUtil.dateToString(tXsth.getCreateTime(), DateUtil.DATETIME_NOSECOND_PATTERN));
		map.put("xsthlsh", tXsth.getXsthlsh());
		map.put("khmc", tXsth.getKhmc());
		map.put("khbh", tXsth.getKhbh());
		map.put("fhmc", tXsth.getFhmc() != null ? "分户：" + tXsth.getFhmc() : "");
		map.put("ckmc", tXsth.getCkmc());
		map.put("hjje", tXsth.getHjje());
		map.put("hjsl", tXsth.getHjsl());
		map.put("bz", tXsth.getBz() + " " + bz.trim());
		map.put("printName", xsth.getCreateName());
		map.put("printTime", DateUtil.dateToString(new Date()));
		datagrid.setObj(map);
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid datagrid(Xsth xsth) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TXsth t where t.bmbh = :bmbh and t.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", xsth.getBmbh());

		//默认显示当前月数据
		if(xsth.getCreateTime() != null){
			params.put("createTime", xsth.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		
		if(xsth.getSearch() != null){
			hql += " and (t.xsthlsh like :search or t.khmc like :search or t.bz like :search or t.ywymc like :search)"; 
			params.put("search", "%" + xsth.getSearch() + "%");
			
		}
		
		//销售提货流程如果是业务员只有本人可以查询
		//在销售开票流程查询数据，未取消，未开票、不是分户
		if(xsth.getFromOther() != null){
			hql += " and t.isCancel = '0' and fhId = null and isKp = '0'";
		}
		
		if(xsth.getYwyId() > 0){
			hql += " and t.createId = :ywyId";
			params.put("ywyId", xsth.getYwyId());
		}
		
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		
		List<TXsth> l = xsthDao.find(hql, params, xsth.getPage(), xsth.getRows());
		List<Xsth> nl = new ArrayList<Xsth>();
		for(TXsth t : l){
			Xsth c = new Xsth();
			BeanUtils.copyProperties(t, c);
			
			//默认置0
			c.setIsTh("0");
			//String xskplshs = "";
			Set<String> xskplshs = new HashSet<String>();
			for(TXsthDet tXsthDet : t.getTXsthDets()){
				//如销售提货单已进行出库，显示关联出库单流水号
				if(tXsthDet.getTKfcks() != null && tXsthDet.getTKfcks().size() > 0){
					if("0".equals(c.getIsTh())){
						c.setIsTh("1");
					}
					//break;
				}
				//显示关联销售开票单流水号
				Set<TXskp> tXskps = tXsthDet.getTXskps();
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
		datagrid.setTotal(xsthDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(String xsthlsh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TXsthDet t where t.TXsth.xsthlsh = :xsthlsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("xsthlsh", xsthlsh);
		List<TXsthDet> l = detDao.find(hql, params);
		List<XsthDet> nl = new ArrayList<XsthDet>();
		for(TXsthDet t : l){
			XsthDet c = new XsthDet();
			BeanUtils.copyProperties(t, c);
			
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid datagridDet(Xsth xsth) {
		//库房出库流程查询，保管员只可查看本人负责商品记录，其他(总账员)可以查看全部
		DataGrid datagrid = new DataGrid();
		String hql = "from TXsthDet t where t.TXsth.bmbh = :bmbh and t.TXsth.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", xsth.getBmbh());
		if(xsth.getCreateTime() != null){
			params.put("createTime", xsth.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		if(xsth.getSearch() != null){
			hql += " and (t.TXsth.xsthlsh like :search or t.TXsth.khmc like :search or t.TXsth.bz like :search or t.TXsth.ywymc like :search)"; 
			params.put("search", "%" + xsth.getSearch() + "%");
			
		}
		
		//只查询未完成的有效数据
		if(xsth.getFromOther() != null){
			hql += " and t.TXsth.isCancel = '0'";
		}
		
		//保管员筛选
		if(xsth.getBgyId() > 0){
			hql += " and t.spbh in (select sb.spbh from TSpBgy sb where sb.depId = :depId and sb.bgyId = :bgyId and sb.ckId = :ckId)";
			params.put("depId", xsth.getBmbh());
			params.put("ckId", xsth.getCkId());
			params.put("bgyId", xsth.getBgyId());
		}
		
		//通过isKp字段来判断该查询来源，
		if("1".equals(xsth.getIsKp())){
			hql += " and t.TXsth.isLs = '1' and t.TXsth.isKp = '0'";
			//hql += " and t.zdwsl <> (select isnull(sum(tkd.zdwsl), 0) from TXskpDet tkd where tkd.TXskp in elements(t.TXskps) and tkd.spbh = t.spbh)";
			hql += " and t.zdwsl <> t.kpsl";
		}else{
			hql += " and t.TXsth.isZs = '0' and ((t.TXsth.isFh = '0' and t.TXsth.isFhth = '0') or (t.TXsth.isFh = '1' and t.TXsth.isFhth = '1'))";
			//hql += " and t.zdwsl <> (select isnull(sum(tkd.zdwsl), 0) from TKfckDet tkd where tkd.TKfck in elements(t.TKfcks) and tkd.spbh = t.spbh)";
			hql += " and t.zdwsl <> t.cksl";
		}
		
		String countHql = "select count(*) " + hql;
		hql += " order by t.TXsth.createTime desc ";
		List<TXsthDet> l = detDao.find(hql, params, xsth.getPage(), xsth.getRows());
		List<Xsth> nl = new ArrayList<Xsth>();
		for(TXsthDet t : l){
			Xsth c = new Xsth();
			BeanUtils.copyProperties(t, c);
			
			if(t.getTXsth().getKhbh() != null && t.getTXsth().getKhbh().trim().length() > 0){
				TKh tKh = khDao.load(TKh.class, t.getTXsth().getKhbh());
				c.setSh(tKh.getSh());
				c.setKhh(tKh.getKhh());
				c.setDzdh(tKh.getDzdh());
			}
			TXsth tXsth = t.getTXsth();
			BeanUtils.copyProperties(tXsth, c);

//			if(t.getTKfcks() != null){
//				//c.setKfcklshs(t.getTKfcks().getKfcklsh());
//				if("1".equals(xsth.getIsKp())){
//					c.setZdwytsl(getYksl(t.getTXsth().getXsthlsh(), t.getSpbh()));
//				}else{
//					c.setZdwytsl(getYtsl(t.getTXsth().getXsthlsh(), t.getSpbh()));
//				}
//			}
			nl.add(c);
		}
		datagrid.setTotal(detDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	private BigDecimal getYtsl(String xsthlsh, String spbh) {
		String detHql = "select isnull(sum(kfDet.zdwsl), 0) from t_kfck_det kfDet "
				+ "left join t_xsth_kfck xk on kfDet.kfcklsh = xk.kfcklsh "
				+ "left join t_xsth_det thDet on thDet.id = xk.xsthdetId and thDet.spbh = kfDet.spbh "
				+ "where thDet.xsthlsh = ? and thDet.spbh = ?";
		Map<String, Object> detParams = new HashMap<String, Object>();
		detParams.put("0", xsthlsh);
		detParams.put("1", spbh);
		BigDecimal yrsl = new BigDecimal(detDao.getBySQL(detHql, detParams).toString());
		return yrsl;
	}
	
	private BigDecimal getYksl(String xsthlsh, String spbh) {
		String detHql = "select isnull(sum(kpDet.zdwsl), 0) from t_xskp_det kpDet "
				+ "left join t_xsth_xskp xx on kpDet.xskplsh = xx.xskplsh "
				+ "left join t_xsth_det thDet on thDet.id = xx.xsthdetId and thDet.spbh = kpDet.spbh "
				+ "where thDet.xsthlsh = ? and thDet.spbh = ?";
		Map<String, Object> detParams = new HashMap<String, Object>();
		detParams.put("0", xsthlsh);
		detParams.put("1", spbh);
		BigDecimal yksl = new BigDecimal(detDao.getBySQL(detHql, detParams).toString());
		return yksl;
	}
	
		
	@Override
	public DataGrid toKfck(Xsth xsth){
//		String sql = "select xd.spbh, isnull(max(xd.zdwsl), 0) zdwthsl, isnull(sum(kd.zdwsl), 0) zdwytsl from t_xsth_det xd " +
//				"left join t_xsth_kfck xk on xd.id = xk.xsthdetId " +
//				"left join t_kfck_det kd on xk.kfcklsh = kd.kfcklsh and kd.spbh = xd.spbh ";
		String sql = "select spbh, isnull(sum(zdwsl), 0) zdwthsl, isnull(sum(cksl), 0) zdwytsl from t_xsth_det ";
		Map<String, Object> params = new HashMap<String, Object>();
		
		String xsthDetIds = xsth.getXsthDetIds();
		if(xsthDetIds != null && xsthDetIds.trim().length() > 0){
//			String[] xs = xsthDetIds.split(",");
			sql += "where id in (" + xsthDetIds + ")";
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
		
		List<XsthDet> nl = new ArrayList<XsthDet>();
		
		for(Object[] os : l){
			String spbh = (String)os[0];
			BigDecimal zdwthsl = new BigDecimal(os[1].toString());
			BigDecimal zdwytsl = new BigDecimal(os[2].toString());
			
			TSp sp = spDao.get(TSp.class, spbh);
			XsthDet xd = new XsthDet();
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
		nl.add(new XsthDet());
		DataGrid dg = new DataGrid();
		dg.setRows(nl);
		return dg;
		
	}
	
	@Override
	public void updateLock(Xsth xsth) {
		TXsth tXsth = xsthDao.get(TXsth.class, xsth.getXsthlsh());
		tXsth.setLockId(xsth.getLockId());
		tXsth.setLockTime(new Date());
		tXsth.setLockName(xsth.getLockName());
		tXsth.setLocked("1");
		OperalogServiceImpl.addOperalog(xsth.getLockId(), xsth.getBmbh(), xsth.getMenuId(), xsth.getXsthlsh(), 
				"锁定销售提货", operalogDao);
	}
	
	@Override
	public void updateUnlock(Xsth xsth) {
		TXsth tXsth = xsthDao.get(TXsth.class, xsth.getXsthlsh());
		tXsth.setLockId(xsth.getLockId());
		tXsth.setLockTime(new Date());
		tXsth.setLockName(xsth.getLockName());
		tXsth.setLocked("0");
		OperalogServiceImpl.addOperalog(xsth.getLockId(), xsth.getBmbh(), xsth.getMenuId(), xsth.getXsthlsh(), 
				"解锁销售提货", operalogDao);
	}
	
	@Override
	public DataGrid toXskp(String xsthDetIds){
//		String sql = "select xd.spbh, isnull(sum(xd.zdwsl), 0) zdwthsl, isnull(max(kd.zdwsl), 0) zdwytsl from t_xsth_det xd " +
//				"left join t_xsth_xskp xk on xd.id = xk.xsthdetId " +
//				"left join t_xskp_det kd on xk.xskplsh = kd.xskplsh and kd.spbh = xd.spbh ";
		String sql = "select spbh, isnull(sum(zdwsl), 0) zdwthsl, isnull(sum(kpsl), 0) zdwytsl from t_xsth_det ";
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(xsthDetIds != null && xsthDetIds.trim().length() > 0){
//			String[] xs = xsthDetIds.split(",");
			sql += "where id in (" + xsthDetIds + ")";
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
		
		List<XsthDet> nl = new ArrayList<XsthDet>();
		
		for(Object[] os : l){
			String spbh = (String)os[0];
			BigDecimal zdwthsl = new BigDecimal(os[1].toString());
			BigDecimal zdwytsl = new BigDecimal(os[2].toString());
			
			TSp sp = spDao.get(TSp.class, spbh);
			XsthDet xd = new XsthDet();
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
		//nl.add(new XsthDet());
		DataGrid dg = new DataGrid();
		dg.setRows(nl);
		return dg;
		
	}
	
	
	@Override
	public DataGrid getSpkc(Xsth xsth) {
		DataGrid dg = new DataGrid();
		List<ProBean> lists = new ArrayList<ProBean>();
		
		List<ProBean> yw = YwzzServiceImpl.getZzsl(xsth.getBmbh(), xsth.getSpbh(), xsth.getCkId(), ywzzDao);
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
		if(xsth.getFhId() != null && xsth.getFhId().trim().length() > 0){
			List<ProBean> fh = FhzzServiceImpl.getZzsl(xsth.getBmbh(), xsth.getSpbh(), xsth.getFhId(), fhzzDao);
			if(fh != null){
				lists.addAll(fh);
			}
		}
		
		dg.setRows(lists);
		dg.setTotal((long)lists.size());
		return dg;
	}
	
	
	@Autowired
	public void setXsthDao(BaseDaoI<TXsth> xsthDao) {
		this.xsthDao = xsthDao;
	}

	@Autowired
	public void setDetDao(BaseDaoI<TXsthDet> detDao) {
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