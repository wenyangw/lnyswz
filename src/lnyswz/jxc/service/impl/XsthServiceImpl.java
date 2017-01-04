package lnyswz.jxc.service.impl;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import lnyswz.jxc.bean.Cgjh;
import lnyswz.jxc.bean.CgxqDet;
import lnyswz.jxc.bean.Ck;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.Fh;
import lnyswz.jxc.bean.Kh;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.bean.SpBgy;
import lnyswz.jxc.bean.User;
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
import lnyswz.jxc.model.TSpBgy;
import lnyswz.jxc.model.TUser;
import lnyswz.jxc.model.TXskp;
import lnyswz.jxc.model.TXskpDet;
import lnyswz.jxc.model.TXsth;
import lnyswz.jxc.model.TXsthDet;
import lnyswz.jxc.model.TJsfs;
import lnyswz.jxc.model.TYszz;
import lnyswz.jxc.model.TYwrk;
import lnyswz.jxc.model.TYwrkDet;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.model.TZsqr;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.service.XsthServiceI;
import lnyswz.jxc.util.AmountToChinese;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Util;

/**
 * 销售提货实现类
 * @author 王文阳
 * @edited
 * 	2015.08.12 增加打印销售合同
 * 	2015.08.12 直送业务不记入临时出库（各部门一致吗）
 */
@Service("xsthService")
public class XsthServiceImpl implements XsthServiceI {
	private Logger logger = Logger.getLogger(XsthServiceImpl.class);
	private BaseDaoI<TXsth> xsthDao;
	private BaseDaoI<TXsthDet> detDao;
	private BaseDaoI<TXskpDet> xskpDetDao;
	private BaseDaoI<TYwrkDet> ywrkDetDao;
	private BaseDaoI<TZsqr> zsqrDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TKh> khDao;
	private BaseDaoI<TSp> spDao;
	private BaseDaoI<TSpBgy> spBgyDao;
	private BaseDaoI<TYszz> yszzDao;
	private BaseDaoI<TYwzz> ywzzDao;
	private BaseDaoI<TFhzz> fhzzDao;
	private BaseDaoI<TLszz> lszzDao;
	private BaseDaoI<TUser> userDao;
	
	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public Xsth save(Xsth xsth) {
		TXsth tXsth = new TXsth();
		//接收前台传入的数据
		BeanUtils.copyProperties(xsth, tXsth);
		//创建流水号
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
		tXsth.setYysfy(BigDecimal.ZERO);

		//最后一笔未还款销售
//		Xskp xskp = new Xskp();
//		xskp.setBmbh(xsth.getBmbh());
//		xskp.setKhbh(xsth.getKhbh());
//		xskp.setYwyId(xsth.getYwyId());
//		
//		Date payTime = KhServiceImpl.getPayTime(xskp, khDetDao);
		
		String depName = depDao.load(TDepartment.class, xsth.getBmbh()).getDepName();
		tXsth.setBmmc(depName);
		
		//根据系统设定进行处理
		tXsth.setIsAudit("0");
		if(xsth.getNeedAudit() != null){
			tXsth.setNeedAudit(xsth.getNeedAudit());
		}else{
			tXsth.setNeedAudit("0");
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
		
		String ywrkDetIds = xsth.getYwrkDetIds();
		Set<TYwrkDet> ywrkDets = null;
		//Set<String> ywrklshs = null;
		int[] ywrkDetIdInts = null;
		//如果从业务入库生成的销售提货，进行关联
		if(ywrkDetIds != null && ywrkDetIds.trim().length() > 0){
			tXsth.setFromRk("1");
			
			String[] strDetIds = ywrkDetIds.split(",");
			ywrkDetIdInts = new int[strDetIds.length];
			//ywrklshs = new HashSet<String>();
			ywrkDets = new HashSet<TYwrkDet>();
			int i = 0;
			for(String detId : strDetIds){
				ywrkDetIdInts[i] = Integer.valueOf(detId);
				i++;
			}
			Arrays.sort(ywrkDetIdInts);
		}else{
			tXsth.setFromRk("0");
		}
		
		
		Department dep = new Department();
		dep.setId(xsth.getBmbh());
		dep.setDepName(depName);
		
		Ck ck = new Ck();
		ck.setId(xsth.getCkId());
		ck.setCkmc(xsth.getCkmc());
		
		Kh kh = new Kh();
		kh.setKhbh(xsth.getKhbh());
		kh.setKhmc(xsth.getKhmc());
		
		Fh fh = null;
		if("1".equals(xsth.getIsFh())){
			fh = new Fh();
			fh.setId(xsth.getFhId());
			fh.setFhmc(xsth.getFhmc());
		}
		
		if(xsth.getJsfsId().equals(Constant.XSKP_JSFS_QK) && "0".equals(xsth.getIsFhth())){
		//if(xsth.getJsfsId().equals(Constant.XSKP_JSFS_QK)){
		//if("1".equals(xsth.getIsSx()) && "0".equals(xsth.getIsFhth())){
			User ywy = new User();
			ywy.setId(xsth.getYwyId());
			ywy.setRealName(xsth.getYwymc());
			//更新授信客户应付金额
			YszzServiceImpl.updateYszzJe(dep, kh, ywy, xsth.getHjje(), Constant.UPDATE_YS_TH, yszzDao);
		}
		
		//处理商品明细
		Set<TXsthDet> tDets = new HashSet<TXsthDet>();
		ArrayList<XsthDet> xsthDets = JSON.parseObject(xsth.getDatagrid(), new TypeReference<ArrayList<XsthDet>>(){});
		for(XsthDet xsthDet : xsthDets){
			TXsthDet tDet = new TXsthDet();
			BeanUtils.copyProperties(xsthDet, tDet, new String[]{"id"});
			
			//tDet.setLastRksl(Constant.BD_ZERO);
			if(tDet.getCksl() == null){
				tDet.setCksl(Constant.BD_ZERO);
			}
			if(tDet.getCcksl() == null){
				tDet.setCcksl(Constant.BD_ZERO);
			}
			
			if(tDet.getKpsl() == null){
				tDet.setKpsl(Constant.BD_ZERO);
			}
			
			if(tDet.getCkpsl() == null){
				tDet.setCkpsl(Constant.BD_ZERO);
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
			tDet.setThsl(Constant.BD_ZERO);
			tDet.setQrsl(BigDecimal.ZERO);
			
			tDet.setCompleted("0");
			
			tDet.setTXsth(tXsth);
			tDets.add(tDet);

			Sp sp = new Sp();
			BeanUtils.copyProperties(xsthDet, sp);
			
			//销售提货直接新生成且不是直送
			//直送业务入库生成的
			//直送出版的
			//计入临时总账
			if("1".equals(xsth.getIsLs()) && (!"1".equals(xsth.getIsZs()) 
					|| xsth.getFromOther().equals("cbs") 
					|| (ywrkDetIds != null && ywrkDetIds.trim().length() > 0))){
				LszzServiceImpl.updateLszzSl(sp, dep, ck, tDet.getZdwsl(), tDet.getCdwsl(), xsthDet.getSpje(), Constant.UPDATE_RK, lszzDao);
			}
			if("1".equals(xsth.getIsFh()) && "0".equals(xsth.getIsFhth())){
			//if("1".equals(xsth.getIsFh())){
				FhzzServiceImpl.updateFhzzSl(sp, dep, fh, tDet.getZdwsl(), Constant.UPDATE_RK, fhzzDao);
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
			//从直送入库生成提货单
			if(ywrkDetIdInts != null){
				BigDecimal thsl = xsthDet.getZdwsl();
				BigDecimal cthsl = xsthDet.getCdwsl() == null ? BigDecimal.ZERO : xsthDet.getCdwsl();
				//BigDecimal cthsl = xsthDet.getCdwsl();
				tDet.setKpsl(BigDecimal.ZERO);
				for(int detId : ywrkDetIdInts){
					TYwrkDet ywrkDet = ywrkDetDao.load(TYwrkDet.class, detId);
					//ywrklshs.add(ywrkDet.getTYwrk().getYwrklsh());
					if(xsthDet.getSpbh().equals(ywrkDet.getSpbh())){
						//BigDecimal wtsl = ywrkDet.getZdwsl().subtract(ywrkDet.getThsl());
						ywrkDets.add(ywrkDet);
						ywrkDet.setThsl(ywrkDet.getThsl().add(thsl));	
						ywrkDet.setCthsl(ywrkDet.getCthsl().add(cthsl));	
//						if(thsl.compareTo(wtsl) == 1){
//							ywrkDet.setThsl(ywrkDet.getThsl().add(wtsl));
//							thsl = thsl.subtract(wtsl);
//						}else{
//							ywrkDet.setThsl(ywrkDet.getThsl().add(thsl));
//							tDet.setLastRksl(thsl);
//							break;
//						}
					}
				}
			}
			
		}
		tXsth.setTXsthDets(tDets);
		xsthDao.save(tXsth);
		
		tXsth.setTYwrks(ywrkDets);

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
		BeanUtils.copyProperties(yTXsth, tXsth, new String[]{"TYwrks"});

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
		if(yTXsth.getHjsl() != null){
			tXsth.setHjsl(yTXsth.getHjsl().negate());
		}
		
		Department dep = new Department();
		dep.setId(tXsth.getBmbh());
		dep.setDepName(tXsth.getBmmc());
		
		Ck ck = new Ck();
		ck.setId(yTXsth.getCkId());
		ck.setCkmc(yTXsth.getCkmc());
		
		Kh kh = new Kh();
		kh.setKhbh(tXsth.getKhbh());
		kh.setKhmc(tXsth.getKhmc());
		
		Fh fh= null;
		if("1".equals(tXsth.getIsFh())){
			fh = new Fh();
			fh.setId(tXsth.getFhId());
			fh.setFhmc(tXsth.getFhmc());
		}
		
		if(tXsth.getJsfsId().equals(Constant.XSKP_JSFS_QK) && "0".equals(tXsth.getIsFhth())){
		//if(tXsth.getJsfsId().equals(Constant.XSKP_JSFS_QK)){
		//if("1".equals(tXsth.getIsSx()) && "0".equals(tXsth.getIsFhth())){
			User ywy = new User();
			ywy.setId(tXsth.getYwyId());
			ywy.setRealName(tXsth.getYwymc());
			//更新授信客户应付金额
			YszzServiceImpl.updateYszzJe(dep, kh, ywy, tXsth.getHjje(), Constant.UPDATE_YS_TH, yszzDao);
		}
		//关联的直送业务入库
		Set<TYwrkDet> ywrks = null;
		int[] intYwrkDetIds = null;
		if(yTXsth.getTYwrks() != null){
			ywrks = yTXsth.getTYwrks();
			intYwrkDetIds = new int[yTXsth.getTYwrks().size()];
			int i = 0;
			for(TYwrkDet t : yTXsth.getTYwrks()){
				intYwrkDetIds[i] = t.getId();
				i++;
			}
			Arrays.sort(intYwrkDetIds);
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
			
			//关联的销售开票
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
			
			//关联的直送业务入库
			if(ywrks != null && ywrks.size() > 0){
				BigDecimal thsl = yTDet.getZdwsl();
				BigDecimal cthsl = yTDet.getCdwsl();
				//BigDecimal lastRksl = yTDet.getLastRksl();
				
				//int j = 0;
				for(int i = intYwrkDetIds.length - 1; i >= 0 ; i--){
					TYwrkDet ywrkDet = ywrkDetDao.load(TYwrkDet.class, intYwrkDetIds[i]);
					if(yTDet.getSpbh().equals(ywrkDet.getSpbh())){
						ywrkDet.setThsl(ywrkDet.getThsl().subtract(thsl));
						ywrkDet.setCthsl(ywrkDet.getCthsl().subtract(cthsl));
//						if(j == 0){
//							ywrkDet.setThsl(ywrkDet.getThsl().subtract(lastRksl));
//							if(thsl.compareTo(lastRksl) == 0){
//								break;
//							}else{
//								thsl = thsl.subtract(lastRksl);
//							}
//						}else{
//							if(thsl.compareTo(ywrkDet.getThsl()) == 1){
//								ywrkDet.setThsl(Constant.BD_ZERO);
//								thsl = thsl.subtract(ywrkDet.getThsl());
//							}else{
//								ywrkDet.setThsl(ywrkDet.getThsl().subtract(thsl));
//								break;
//							}
//						}
//						j++;
					}
				}
			}

			Sp sp = new Sp();
			BeanUtils.copyProperties(yTDet, sp);

			if("1".equals(yTXsth.getIsLs())){
				//教材所有临时及其他部门当直送提货并未确认收货数量，冲减时不更新lszz（此处用非进行处理）
				if(!("1".equals(yTXsth.getIsZs()) && tDet.getThsl().compareTo(BigDecimal.ZERO) == 0)
						|| (ywrks != null && ywrks.size() > 0)
						|| xsth.getFromOther().equals("cbs")){
					LszzServiceImpl.updateLszzSl(sp, dep, ck, tDet.getZdwsl(), tDet.getCdwsl(), tDet.getSpje(), Constant.UPDATE_RK, lszzDao);
				}
				
			}
			if("1".equals(yTXsth.getIsFh()) && "0".equals(yTXsth.getIsFhth())){
			//if("1".equals(yTXsth.getIsFh())){
				FhzzServiceImpl.updateFhzzSl(sp, dep, fh, tDet.getZdwsl(), Constant.UPDATE_RK, fhzzDao);
			}
		}

		if(ywrks != null && ywrks.size() > 0){
			yTXsth.setTYwrks(null);
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
		String hql = "from TXsthDet t where t.TXsth.xsthlsh = :xsthlsh order by t.spbh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("xsthlsh", xsth.getXsthlsh());
		List<TXsthDet> dets = detDao.find(hql, params);
		//for (TXsthDet yd : tXsth.getTXsthDets()) {
		for (TXsthDet yd : dets) {
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
		
		if(tXsth.getBookmc() != null){
			bz = " " + tXsth.getBookmc().trim();
		}
		if(tXsth.getYwymc() != null){
			bz += " " + tXsth.getYwymc().trim();
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
				
		DecimalFormat df=new DecimalFormat("#,##0.00");
		BigDecimal hjje_b=new BigDecimal(String.format("%.2f", tXsth.getHjje())); 
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "销   售   提   货   单");
		map.put("head", Constant.XSTH_HEAD.get(tXsth.getBmbh()));
		map.put("footer", Constant.XSTH_FOOT.get(tXsth.getBmbh()));
		map.put("gsmc", Constant.BMMCS.get(tXsth.getBmbh()));
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
		map.put("hjje", df.format(tXsth.getHjje()));
		map.put("hjsl", tXsth.getHjsl());
		map.put("hjje_b", AmountToChinese.numberToChinese(hjje_b));
		map.put("bz", tXsth.getBz() + " " + bz.trim());
		map.put("memo", tXsth.getBz() + " " + bz.trim());
		map.put("printName", xsth.getCreateName());
		map.put("printTime", DateUtil.dateToString(new Date()));
		datagrid.setObj(map);
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid printShd(Xsth xsth) {
		DataGrid datagrid = new DataGrid();
							
		List<XsthDet> nl = new ArrayList<XsthDet>();
		String hql = "from TXsthDet t where t.id in (" + xsth.getXsthDetIds() + ") order by t.spbh";
		List<TXsthDet> dets = detDao.find(hql);
		for (TXsthDet yd : dets) {
			XsthDet xsthDet = new XsthDet();
			BeanUtils.copyProperties(yd, xsthDet);
			//将本次确认数量替换zdwsl
			xsthDet.setZdwsl(yd.getQrsl());
			if(yd.getZhxs().compareTo(BigDecimal.ZERO) != 0){
				xsthDet.setCdwsl(yd.getQrsl().divide(yd.getZhxs(), 3, BigDecimal.ROUND_HALF_UP));
			}
			nl.add(xsthDet);
		}
		TXsth tXsth = dets.get(0).getTXsth();
					
		int num = nl.size();
		if (num < 4) {
			for (int i = 0; i < (4 - num); i++) {
				nl.add(new XsthDet());
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "收   货   确   认   单");
		map.put("gsmc", Constant.BMMCS.get(tXsth.getBmbh()));
		map.put("khmc", tXsth.getKhmc());
		map.put("gysmc", dets.get(0).getTCgjh().getGysmc());
		map.put("shdz", tXsth.getShdz() == null ? "" : tXsth.getShdz());
		map.put("xsthlsh", tXsth.getXsthlsh());
		map.put("ywymc", tXsth.getYwymc());
		map.put("printTime", DateUtil.dateToString(new Date(), "yyyy-MM-dd"));
		
		datagrid.setObj(map);
		datagrid.setRows(nl);
		return datagrid;
	}

	
	@Override
	public DataGrid printXsht(Xsth xsth) {
		DataGrid datagrid = new DataGrid();
		TXsth tXsth = xsthDao.load(TXsth.class, xsth.getXsthlsh());
		
		List<XsthDet> nl = new ArrayList<XsthDet>();
		int j = 0;
		String hql = "from TXsthDet t where t.TXsth.xsthlsh = :xsthlsh order by t.spbh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("xsthlsh", xsth.getXsthlsh());
		List<TXsthDet> dets = detDao.find(hql, params);
		for (TXsthDet yd : dets) {
			XsthDet xsthDet = new XsthDet();
			BeanUtils.copyProperties(yd, xsthDet);
			//大连分公司的纸张合同使用吨为计量单位
			if(tXsth.getBmbh().equals("08") && yd.getSpbh().startsWith("4")){
				xsthDet.setZjldwmc(yd.getCjldwmc());
				xsthDet.setZdwsl(yd.getCdwsl());
				xsthDet.setZdwdj(yd.getCdwdj());
			}
			nl.add(xsthDet);
		}
		
		
		int num = nl.size();
		if (num < 4) {
			for (int i = 0; i < (4 - num); i++) {
				nl.add(new XsthDet());
			}
		}
		
		String sqlKh = "select khlxId, sxzq from t_kh_det where depId = ? and khbh = ? and ywyId = ?";
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.put("0", tXsth.getBmbh());
		sqlParams.put("1", tXsth.getKhbh());
		sqlParams.put("2", tXsth.getYwyId());
		
		Object[] khDet = detDao.getMBySQL(sqlKh, sqlParams);
		//付款天数，默认现款10天，月结(03)30天，授信按授信期
		int payDays = 10;
		
		
		if(khDet != null){
			if(((String)khDet[0]).equals("03")){
				payDays = 30;
			}
			if(((String)khDet[0]).equals("02")){
				payDays = Integer.valueOf(khDet[1].toString());
			}
		}
				
		DecimalFormat df=new DecimalFormat("#,##0.00");
		BigDecimal hjje_b=new BigDecimal(String.format("%.2f", tXsth.getHjje())); 
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bmmc", Constant.BMMCS.get(tXsth.getBmbh()));
		map.put("bmdz", Constant.BMDZS.get(tXsth.getBmbh()));
		map.put("htdz", Constant.HTDZS.get(tXsth.getBmbh()));
		map.put("xsthlsh", tXsth.getXsthlsh());
		map.put("khmc", tXsth.getKhmc());
		map.put("shdz", tXsth.getShdz() == null ? "" : tXsth.getShdz());
		map.put("thr", tXsth.getThr() == null ? "" : tXsth.getThr());
		map.put("payDays", payDays);
		map.put("hjje", df.format(tXsth.getHjje()));
		map.put("hjje_b", AmountToChinese.numberToChinese(hjje_b));
		
		datagrid.setObj(map);
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid printXsthByBgy(Xsth xsth) {
		DataGrid datagrid = new DataGrid();
		TXsth tXsth = xsthDao.load(TXsth.class, xsth.getXsthlsh());
		
		List<XsthDet> nl = new ArrayList<XsthDet>();
		BigDecimal hjsl = Constant.BD_ZERO;
//		int j = 0;
//		Set<TXskp> xskps = null;
		for (TXsthDet yd : tXsth.getTXsthDets()) {
			//String hql = "from TSpBgy t where t.depId = :bmbh and t.ckId = :ckId and t.spbh = :spbh and t.bgyId = :bgyId";
			String hql = "from TSpBgy t where t.depId = :bmbh and t.spbh = :spbh and t.bgyId = :bgyId";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("bmbh", tXsth.getBmbh());
			//params.put("ckId", tXsth.getCkId());
			params.put("spbh", yd.getSpbh());
			params.put("bgyId", xsth.getBgyId());
			
			TSpBgy tSpBgy = spBgyDao.get(hql, params);
			
			
			if(tSpBgy != null){
				XsthDet xsthDet = new XsthDet();
				BeanUtils.copyProperties(yd, xsthDet);
				hjsl = hjsl.add(yd.getCdwsl());
				nl.add(xsthDet);
//				if(j == 0){
//					xskps = yd.getTXskps();
//				}
//				j++;
			}
		}
		int num = nl.size();
		if (num < Constant.REPORT_NUMBER) {
			for (int i = 0; i < (Constant.REPORT_NUMBER - num); i++) {
				nl.add(new XsthDet());
			}
		}
				
//		String xskplsh = "";
//		if(xskps != null && xskps.size() > 0){
//			xskplsh += xskps.iterator().next().getXskplsh();
//		}
		
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
//		bz += xskplsh;
		
//		DecimalFormat df=new DecimalFormat("#,##0.00");
//		BigDecimal hjje_b=new BigDecimal(String.format("%.2f", tXsth.getHjje())); 
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "商  品  出  库  分  票  单");
		//map.put("head", Constant.XSTH_HEAD.get(tXsth.getBmbh()));
		//map.put("footer", Constant.XSTH_FOOT.get(tXsth.getBmbh()));
		//map.put("gsmc", Constant.BMMCS.get(tXsth.getBmbh()));
//		if("1".equals(Constant.XSTH_PRINT_LSBZ.get(xsth.getBmbh()))){
//			map.put("bmmc", tXsth.getBmmc() + "(" + (tXsth.getToFp().equals("1") ? "是" : "否") + ")");
//		}else{
//			map.put("bmmc", tXsth.getBmmc());
//		}
		
		map.put("bmmc", tXsth.getBmmc());
		map.put("createTime", DateUtil.dateToString(tXsth.getCreateTime(), DateUtil.DATETIME_NOSECOND_PATTERN));
		map.put("xsthlsh", tXsth.getXsthlsh());
		map.put("khmc", tXsth.getKhmc());
		map.put("khbh", tXsth.getKhbh());
		map.put("fhmc", tXsth.getFhmc() != null ? "分户：" + tXsth.getFhmc() : "");
		map.put("ckmc", tXsth.getCkmc());
		//map.put("hjje", df.format(tXsth.getHjje()));
		map.put("hjsl", hjsl);
		//map.put("hjje_b", AmountToChinese.numberToChinese(hjje_b));
		map.put("bz", tXsth.getBz() + " " + bz.trim());
		map.put("memo", tXsth.getBz());
		map.put("printName", xsth.getCreateName());
		map.put("printTime", DateUtil.dateToString(new Date()));
		map.put("bgyName", userDao.load(TUser.class, xsth.getBgyId()).getRealName());
		datagrid.setObj(map);
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid getSpBgys(Xsth xsth) {
		String sql = "select distinct bgy.bgyId, bgy.bgyName from t_xsth th "
				+ "left join t_xsth_det det on th.xsthlsh = det.xsthlsh "
				+ "left join t_sp_bgy bgy on th.bmbh = bgy.depId and det.spbh = bgy.spbh "
				+ "where th.xsthlsh = ?";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", xsth.getXsthlsh());
		
		List<Object[]> l = xsthDao.findBySQL(sql, params);
		if(l != null && l.size() > 0){
			DataGrid dg = new DataGrid();
			List<SpBgy> bgys = new ArrayList<SpBgy>();
			for(Object[] o : l){
				if(o[0] != null){
					SpBgy spBgy = new SpBgy();
					spBgy.setBgyId(Integer.valueOf(o[0].toString()));
					spBgy.setBgyName(o[1].toString());
					bgys.add(spBgy);
				}
			}
			dg.setRows(bgys);
			return dg;
		}
		return null;
	}
	
	@Override
	public DataGrid datagrid(Xsth xsth) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TXsth t where t.bmbh = :bmbh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", xsth.getBmbh());

		//默认显示当前月数据
		if(!(xsth.getSearch() != null && xsth.getSearch().equals("zf"))){
			hql += " and t.createTime > :createTime";
			if(xsth.getCreateTime() != null){
				params.put("createTime", xsth.getCreateTime()); 
			}else{
				//直发的统计起始时间
				//if(xsth.getSearch() != null && xsth.getSearch().equals("zf")){
				//	params.put("createTime", DateUtil.stringToDate("2016-03-21"));
				//}else{
					params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
				}
		}
		
		if(xsth.getSearch() != null){
			if(xsth.getSearch().equals("zf")){
				hql += " and t.xsthlsh in (select t.TXsth.xsthlsh from TXsthDet t where t.TXsth.isZs = '1' and t.TXsth.createTime > '2016-03-21' and TXsth.fromRk = '0' and t.completed = '0' and t.TXsth.isCancel = '0' and t.TXsth.needAudit = t.TXsth.isAudit";
				if(xsth.getBmbh().equals("04")){
					hql += " and t.TXsth.khbh not in (" + Constant.CBS_LIST + ")";
				}
				hql += ")";
			}else{
				//hql += " and (t.xsthlsh like :search or t.khbh like :search or t.khmc like :search or t.bz like :search or t.ywymc like :search or t.bookmc like :search)"; 
				hql += " and (" + Util.getQueryWhere(xsth.getSearch(), new String[]{"t.xsthlsh", "t.khbh", "t.khmc", "t.bz", "t.ywymc", "t.bookmc"}, params) + ")";
				
				//params.put("search", "%" + xsth.getSearch() + "%");
			}
			
		}
		
		//销售提货流程如果是业务员只有本人可以查询
		//在销售开票流程查询数据，未取消，未开票、不是分户
		if(xsth.getFromOther() != null){
			hql += " and t.isCancel = '0' and fhId = null and isKp = '0'";
		}
		
		if(xsth.getYwyId() > 0){
			hql += " and (t.createId = :ywyId or t.ywyId = :ywyId)";
			params.put("ywyId", xsth.getYwyId());
		}
		
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		
		List<TXsth> l = xsthDao.find(hql, params, xsth.getPage(), xsth.getRows());
		List<Xsth> nl = new ArrayList<Xsth>();
		for(TXsth t : l){
			Xsth c = getXsthRow(t);
			
			nl.add(c);
		}
		datagrid.setTotal(xsthDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}

	private Xsth getXsthRow(TXsth t) {
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
			
			String sql_sh = "select isnull(bz, '') bz from t_ywsh t where t.lsh = ?";
			Map<String, Object> params_sh = new HashMap<String, Object>();
			params_sh.put("0", t.getXsthlsh());
			
			List<Object[]> os = xsthDao.findBySQL(sql_sh, params_sh);
			if(os != null && os.size() > 0){
				String bzs = "";
				for(Object o : os){
					String sbz = o.toString().trim();
					if(!sbz.equals("")){
						bzs += sbz + ",";
					}
				}
				c.setShbz(bzs);
			}
			
		}
		
		String r = xskplshs.toString();
		
		c.setXskplsh(r.length() == 2 ? "" : r.substring(1, r.length() - 1));
		return c;
	}
	
	@Override
	public DataGrid refreshXsth(Xsth xsth) {
		DataGrid dg = new DataGrid();
		TXsth tXsth = xsthDao.get(TXsth.class, xsth.getXsthlsh());
		dg.setObj(getXsthRow(tXsth));
		
		return dg;
	}
	/**
	 * 业务审核显示商品明细时调用
	 */
	@Override
	public DataGrid detDatagrid(Xsth xsth) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TXsthDet t where t.TXsth.xsthlsh = :xsthlsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("xsthlsh", xsth.getXsthlsh());
		List<TXsthDet> l = detDao.find(hql, params);
		List<XsthDet> nl = new ArrayList<XsthDet>();
		for(TXsthDet t : l){
			XsthDet c = new XsthDet();
			BeanUtils.copyProperties(t, c);
			if(xsth.getFromOther() != null && xsth.getFromOther().equals("ywsh")){
				//取得成本后加税
				BigDecimal dwcb = YwzzServiceImpl.getDwcb(t.getTXsth().getBmbh(), t.getSpbh(), ywzzDao).multiply(new BigDecimal("1").add(Constant.SHUILV));
				if(dwcb.compareTo(BigDecimal.ZERO) == 0){
					c.setDwcb(BigDecimal.ZERO);
				}else{
					c.setDwcb(t.getZdwdj().subtract(dwcb).divide(t.getZdwdj(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(1, BigDecimal.ROUND_HALF_UP));
				}
			}
			if(t.getTCgjh() != null){
				c.setCgjhlsh(t.getTCgjh().getCgjhlsh());
			}
			
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid datagridDet(Xsth xsth) {
		//库房出库流程查询，保管员只可查看本人负责商品记录，其他(总账员)可以查看全部
		DataGrid datagrid = new DataGrid();
		String hql = "from TXsthDet t where t.TXsth.bmbh = :bmbh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", xsth.getBmbh());
		
		if(xsth.getFromOther() == null || !xsth.getFromOther().equals("fromCgjh")){
			hql += " and t.TXsth.createTime > :createTime";
			if(xsth.getCreateTime() != null){
				params.put("createTime", xsth.getCreateTime()); 
			}else{
				params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
			}
		}
		
		if(xsth.getSearch() != null && xsth.getSearch().length() > 0){
			if("fh".equals(xsth.getSearch())){
				hql += " and t.TXsth.fhId is not null and t.TXsth.isFhth = '0'";
			}else{
				//hql += " and (t.TXsth.xsthlsh like :search or t.TXsth.khbh like :search or t.TXsth.khmc like :search or t.TXsth.bookmc like :search or t.TXsth.bz like :search or t.TXsth.ywymc like :search)"; 
				//params.put("search", "%" + xsth.getSearch() + "%");
				hql += " and (" + Util.getQueryWhere(xsth.getSearch(), new String[]{"t.TXsth.xsthlsh", "t.TXsth.khbh", "t.TXsth.khmc", "t.TXsth.bookmc", "t.TXsth.bz", "t.TXsth.ywymc"}, params) + ")";
			}
		}
		
		//只查询未完成的有效数据
		if(xsth.getFromOther() != null){
			hql += " and t.TXsth.isCancel = '0'";
			if(Constant.NEED_AUDIT.equals("1")){
				hql += " and t.TXsth.isAudit = t.TXsth.needAudit";
			}
		}
		
		if(xsth.getFromOther() != null && xsth.getFromOther().equals("fromXskp")){
			//内部销售的不受限制
			hql += " and (t.TXsth.isZs = '0' or (t.TXsth.isZs = '1' and t.qrsl <> 0 or (t.qrsl = 0 and (t.TXsth.khbh in ('21010263', '21010608') or (t.TXsth.fromRk='1' and (t.TXsth.bmbh ='01' or t.TXsth.bmbh='05'))))))";
		}
		
		
		
		
		if(xsth.getFromOther().equals("fromCgjh")){
			hql += " and t.TXsth.isZs = '1' and t.TCgjh.cgjhlsh is null and t.TXsth.createTime > '2016-03-21' and t.TXsth.fromRk = '0' and t.completed = '0'" ;
			if(xsth.getBmbh().equals("04")){
				hql += " and t.TXsth.khbh not in (" + Constant.CBS_LIST + ")";
			}
		}
		
		//保管员筛选
		if(xsth.getBgyId() > 0){
			hql += " and t.spbh in (select sb.spbh from TSpBgy sb where sb.depId = :depId and sb.bgyId = :bgyId and sb.ckId = :ckId)";
			params.put("depId", xsth.getBmbh());
			params.put("ckId", xsth.getCkId());
			params.put("bgyId", xsth.getBgyId());
		}
		
		//通过isKp字段来判断该查询来源，
		if(xsth.getFromOther().equals("fromXskp") && "1".equals(xsth.getIsKp())){
			hql += " and t.TXsth.isLs = '1' and t.TXsth.isKp = '0'";
			//hql += " and t.zdwsl <> (select isnull(sum(tkd.zdwsl), 0) from TXskpDet tkd where tkd.TXskp in elements(t.TXskps) and tkd.spbh = t.spbh)";
			hql += " and t.zdwsl <> t.kpsl";
		}
		
		if(xsth.getFromOther().equals("fromKfck")){
			//hql += " and t.TXsth.isZs = '0' and ((t.TXsth.isFh = '0' and t.TXsth.isFhth = '0') or (t.TXsth.isFh = '1' and t.TXsth.isFhth = '1'))";
			hql += " and t.TXsth.isZs = '0' and (t.TXsth.isLs = '1' or t.TXsth.isFhth = '1' or (t.TXsth.isLs = '0' and t.TXsth.isFhth = '0'))";
			if(!"fh".equals(xsth.getSearch())){
				hql += " and (t.TXsth.fhId is null or t.TXsth.isFhth = '1')";
			}
			//hql += " and t.TXsth.isZs = '0'";
			//hql += " and t.zdwsl <> (select isnull(sum(tkd.zdwsl), 0) from TKfckDet tkd where tkd.TKfck in elements(t.TKfcks) and tkd.spbh = t.spbh)";
			hql += " and t.zdwsl <> t.cksl";
		}
		
		String countHql = "select count(id) " + hql;
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
			
			if(t.getTXskps() != null && t.getTXskps().size() > 0){
				c.setIsKp("1");
			}
			
			String sql = "select jhrk.ywrklsh from v_xsth_det thDet"
					+ "	left join t_cgjh_det jhDet on thDet.cgjhlsh = jhDet.cgjhlsh and thDet.spbh = jhDet.spbh"
					+ " left join t_cgjh_ywrk jhrk on jhDet.id = jhrk.cgjhdetId"
					+ " where thDet.id = ?";
			Map<String, Object> paramsSql = new HashMap<String, Object>();
			paramsSql.put("0", t.getId());
			
			Object y = detDao.getBySQL(sql, paramsSql);
			
			
			//			if(t.getTKfcks() != null){
//				//c.setKfcklshs(t.getTKfcks().getKfcklsh());
//				if("1".equals(xsth.getIsKp())){
//					c.setZdwytsl(getYksl(t.getTXsth().getXsthlsh(), t.getSpbh()));
//				}else{
//					c.setZdwytsl(getYtsl(t.getTXsth().getXsthlsh(), t.getSpbh()));
//				}
//			}
			if(xsth.getFromOther().equals("fromXskp") && tXsth.getJsfsId().equals("06") && tXsth.getIsZs().equals("1") && !(tXsth.getBmbh().equals("01") && (tXsth.getKhbh().equals("21010263") || tXsth.getKhbh().equals("21010608"))) && !(tXsth.getBmbh().equals("05") && tXsth.getFromRk().equals("1"))){
				if(y != null){
					nl.add(c);
				}
			}else{
				nl.add(c);
			}
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
		String sql = "select spbh, isnull(sum(zdwsl), 0) zdwthsl, isnull(sum(cksl), 0) zdwytsl, isnull(sum(cdwsl), 0) cdwthsl, isnull(sum(ccksl), 0) cdwytsl from t_xsth_det ";
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
			BigDecimal cdwthsl = new BigDecimal(os[3].toString());
			BigDecimal cdwytsl = new BigDecimal(os[4].toString());
			
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
				xd.setCdwthsl(cdwthsl);
				xd.setCdwytsl(cdwytsl);
//				if(sp.getZhxs().compareTo(Constant.BD_ZERO) != 0){
//					xd.setCdwthsl(zdwthsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
//					xd.setCdwytsl(zdwytsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
//				}else{
//					xd.setCdwthsl(Constant.BD_ZERO);
//					xd.setCdwytsl(Constant.BD_ZERO);
//				}
			}
			nl.add(xd);
		}
		nl.add(new XsthDet());
		DataGrid dg = new DataGrid();
		dg.setRows(nl);
		return dg;
		
	}
	
	@Override
	public DataGrid toCgjh(Xsth xsth){
		String sql = "select spbh, isnull(sum(zdwsl), 0) zdwsl, isnull(sum(cdwsl), 0) cdwsl from t_xsth_det ";
		Map<String, Object> params = new HashMap<String, Object>();
		
		String xsthDetIds = xsth.getXsthDetIds();
		if(xsthDetIds != null && xsthDetIds.trim().length() > 0){
			sql += "where id in (" + xsthDetIds + ")";
		}
		sql += " group by spbh";
		
		List<Object[]> l = detDao.findBySQL(sql, params);
		
		List<XsthDet> nl = new ArrayList<XsthDet>();
		
		for(Object[] os : l){
			String spbh = (String)os[0];
			BigDecimal zdwsl = new BigDecimal(os[1].toString());
			BigDecimal cdwsl = new BigDecimal(os[2].toString());
			
			TSp sp = spDao.get(TSp.class, spbh);
			XsthDet xd = new XsthDet();
			BeanUtils.copyProperties(sp, xd);
			xd.setShdz(xsth.getShdz());
			xd.setLxr(xsth.getThr());
			xd.setZjldwId(sp.getZjldw().getId());
			xd.setZjldwmc(sp.getZjldw().getJldwmc());
			xd.setZdwsl(zdwsl);
			if(sp.getCjldw() != null){
				xd.setCjldwId(sp.getCjldw().getId());
				xd.setCjldwmc(sp.getCjldw().getJldwmc());
				xd.setZhxs(sp.getZhxs());
				xd.setCdwsl(cdwsl);
//				if(sp.getZhxs().compareTo(Constant.BD_ZERO) != 0){
//					xd.setCdwthsl(zdwthsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
//					xd.setCdwytsl(zdwytsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
//				}else{
//					xd.setCdwthsl(Constant.BD_ZERO);
//					xd.setCdwytsl(Constant.BD_ZERO);
//				}
			}
			nl.add(xd);
		}
		nl.add(new XsthDet());
		DataGrid dg = new DataGrid();
		dg.setRows(nl);
		return dg;
	}
	
	/**
	 * 直送业务确认收货数量
	 * 库房检斤后修改实际数量
	 * 纸张要修改次数量
	 * 并同步更新临时总账、应收总账的数据
	 * 
	 */
	@Override
	public void updateThsl(Xsth xsth) {
		BigDecimal sl = BigDecimal.ZERO;
		BigDecimal csl = BigDecimal.ZERO;
		BigDecimal lsje = BigDecimal.ZERO;

		//获取修改的商品记录
		TXsthDet tXsthDet = detDao.load(TXsthDet.class, xsth.getId());
				
		
		//检查是否已修改过,未改过的将原zdwsl保存到thsl
//		if(tXsthDet.getThsl().compareTo(Constant.BD_ZERO) == 0){
//			tXsthDet.setThsl(tXsthDet.getZdwsl());
//			//tXsthDet.setZdwsl(BigDecimal.ZERO);
//		}
		
		//记录每次确认的提货数量
		TZsqr tZsqr = new TZsqr();
		tZsqr.setXsthlsh(tXsthDet.getTXsth().getXsthlsh());
		tZsqr.setSpbh(tXsthDet.getSpbh());
		tZsqr.setQrsl(xsth.getThsl());
		tZsqr.setCreateId(xsth.getCreateId());
		tZsqr.setCreateName(xsth.getCreateName());
		tZsqr.setCreateTime(new Date());
		zsqrDao.save(tZsqr);
				
		sl = xsth.getThsl();
		//将本次录入的数据与原数据比较，获得相差数量
		//if(xsth.getFromOther().equals("xsth") && tXsthDet.getThsl().compareTo(BigDecimal.ZERO) == 0){
		if(xsth.getFromOther() != null && xsth.getFromOther().equals("xsth")){
			if(tXsthDet.getThsl().compareTo(Constant.BD_ZERO) == 0){
				tXsthDet.setThsl(tXsthDet.getZdwsl());
				tXsthDet.setZdwsl(BigDecimal.ZERO);
			}

			tXsthDet.setQrsl(sl);
			tXsthDet.setZdwsl(tXsthDet.getZdwsl().add(sl));
			
			//本次数量对应的临时金额
			lsje = sl.multiply(tXsthDet.getZdwdj());
			//应收差额
			//ysje = sl.subtract(tXsthDet.getZdwsl()).multiply(tXsthDet.getZdwdj());
			
			//提货单总金额
			//thje = tXsthDet.getZdwsl().multiply(tXsthDet.getZdwdj());
		}else{
			if(tXsthDet.getThsl().compareTo(Constant.BD_ZERO) == 0){
				tXsthDet.setThsl(tXsthDet.getZdwsl());
			}
			//差额
			sl = sl.subtract(tXsthDet.getZdwsl());
			//库房确认数量
			tXsthDet.setZdwsl(xsth.getThsl());
			//获取相差金额
			lsje = sl.multiply(tXsthDet.getZdwdj());
			//ysje = lsje;
			//thje = lsje;
		}
		
		
		//更新修改后数量、金额
		tXsthDet.setSpje(tXsthDet.getZdwdj().multiply(tXsthDet.getZdwsl()));
		
		//如为纸张品种，修改次单位数量
		if(tXsthDet.getSpbh().substring(0, 1).equals("4")){
			if(tXsthDet.getZhxs().compareTo(BigDecimal.ZERO) != 0){
				tXsthDet.setCdwsl(tXsthDet.getZdwsl().divide(tXsthDet.getZhxs(), 3, BigDecimal.ROUND_HALF_UP));
				csl = sl.divide(tXsthDet.getZhxs(), 3, BigDecimal.ROUND_HALF_UP);
			}
		}
		
		TXsth tXsth = tXsthDet.getTXsth();
		Sp sp = new Sp();
		BeanUtils.copyProperties(tXsthDet, sp);
		Department dep = new Department();
		dep.setId(tXsth.getBmbh());
		dep.setDepName(tXsth.getBmmc());
		Ck ck = new Ck();
		ck.setId(tXsth.getCkId());
		ck.setCkmc(tXsth.getCkmc());
		
		//更新提货单的合计数量、金额
		if(!xsth.getFromOther().equals("xsth")){
			tXsth.setHjje(tXsth.getHjje().add(lsje));
			tXsth.setHjsl(tXsth.getHjsl().add(csl));
		}
		
		//更新临时总账数量
		LszzServiceImpl.updateLszzSl(sp, dep, ck, sl, csl, lsje, Constant.UPDATE_RK, lszzDao);
		
		//更新应收总账的金额(直送不更新)
		if(!xsth.getFromOther().equals("xsth") && tXsth.getJsfsId().equals(Constant.XSKP_JSFS_QK) && "1".equals(tXsth.getIsLs()) && "0".equals(tXsth.getIsFhth())){
			Kh kh = new Kh();
			kh.setKhbh(tXsth.getKhbh());
			kh.setKhmc(tXsth.getKhmc());
			User ywy = new User();
			ywy.setId(tXsth.getYwyId());
			ywy.setRealName(tXsth.getYwymc());
			
			//更新授信客户应付金额
			YszzServiceImpl.updateYszzJe(dep, kh, ywy, lsje, Constant.UPDATE_YS_TH, yszzDao);
		}
		
		OperalogServiceImpl.addOperalog(xsth.getCreateId(), xsth.getBmbh(), xsth.getMenuId(), String.valueOf(xsth.getId()), 
				"修改提货数量", operalogDao);
	}
	
	@Override
	public void updateZsComplete(Xsth xsth){
		BigDecimal sl = BigDecimal.ZERO;
		BigDecimal csl = BigDecimal.ZERO;
		BigDecimal lsje = BigDecimal.ZERO;
		
		TXsthDet tXsthDet = detDao.get(TXsthDet.class, xsth.getId());
		tXsthDet.setCompleted("1");
		
		lsje = tXsthDet.getZdwsl().subtract(tXsthDet.getThsl()).multiply(tXsthDet.getZdwdj());
		if(tXsthDet.getZhxs().compareTo(BigDecimal.ZERO) != 0){
			csl = tXsthDet.getCdwsl().subtract(tXsthDet.getThsl()).divide(tXsthDet.getZhxs(), 3, BigDecimal.ROUND_HALF_UP);
		}
		
		TXsth tXsth = tXsthDet.getTXsth();
		
		//只在直送确认完成时有数量更新，不需要进行判断
		tXsth.setHjje(tXsth.getHjje().add(lsje));
		tXsth.setHjsl(tXsth.getHjsl().add(csl));
		
		Department dep = new Department();
		dep.setId(tXsth.getBmbh());
		dep.setDepName(tXsth.getBmmc());
		Kh kh = new Kh();
		kh.setKhbh(tXsth.getKhbh());
		kh.setKhmc(tXsth.getKhmc());
		User ywy = new User();
		ywy.setId(tXsth.getYwyId());
		ywy.setRealName(tXsth.getYwymc());
			
		//更新授信客户应付金额
		YszzServiceImpl.updateYszzJe(dep, kh, ywy, lsje, Constant.UPDATE_YS_TH, yszzDao);
				
		OperalogServiceImpl.addOperalog(xsth.getCreateId(), xsth.getBmbh(), xsth.getMenuId(), String.valueOf(xsth.getId()), 
			"确认直送完成", operalogDao);
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
	public void updateYf(Xsth xsth) {
		
		//获取修改的商品记录
		TXsth tXsth = xsthDao.load(TXsth.class, xsth.getXsthlsh());
				
		
		//检查是否已修改过, 未改过的将原ysyf保存到yysfy
		if(tXsth.getYysfy() == null || tXsth.getYysfy().compareTo(BigDecimal.ZERO) == 0){
			tXsth.setYysfy(tXsth.getYsfy());
		}
		tXsth.setYsfy(xsth.getYsfy());
				
		OperalogServiceImpl.addOperalog(xsth.getCreateId(), xsth.getBmbh(), xsth.getMenuId(), String.valueOf(xsth.getXsthlsh()), 
				"修改运费", operalogDao);
	}
	
	@Override
	public DataGrid toXskp(String xsthDetIds){
//		String sql = "select xd.spbh, isnull(sum(xd.zdwsl), 0) zdwthsl, isnull(max(kd.zdwsl), 0) zdwytsl from t_xsth_det xd " +
//				"left join t_xsth_xskp xk on xd.id = xk.xsthdetId " +
//				"left join t_xskp_det kd on xk.xskplsh = kd.xskplsh and kd.spbh = xd.spbh ";
		//String sql = "select spbh, isnull(sum(zdwsl), 0) zdwthsl, isnull(sum(kpsl), 0) zdwytsl, max(zdwdj) zdwdj, max(cdwdj) cdwdj from t_xsth_det ";
		//String sql = "select spbh, isnull(sum(zdwsl), 0) zdwthsl, isnull(sum(kpsl), 0) zdwytsl, max(zdwdj) zdwdj, max(cdwdj) cdwdj, sum(spje) spje from t_xsth_det ";
		//String sql = "select thDet.spbh, isnull(max(thDet.zdwsl), 0) zdwthsl, isnull(max(thDet.kpsl), 0) zdwytsl, max(thDet.zdwdj) zdwdj, max(thDet.cdwdj) cdwdj,"
			//	+ " max(thDet.spje) - isnull(SUM(kpDet.spje + kpDet.spse), 0) spje from t_xsth_det thDet"
			//	+ " left join t_xsth_xskp xx on xx.xsthdetId = thDet.id"
			//	+ " left join t_xskp_det kpDet on xx.xskplsh = kpDet.xskplsh and thDet.spbh = kpDet.spbh";
		
		String sql = "select thDet.spbh, isnull(sum(thDet.zdwsl), 0) zdwthsl, isnull(sum(thDet.kpsl), 0) zdwytsl,"
				+ " max(thDet.zdwdj) zdwdj, max(thDet.cdwdj) cdwdj,"
				+ " cast(round(sum(thDet.zdwsl - thDet.kpsl) * max(thDet.zdwdj), 2) as numeric(12, 2))  spje,"
				+ " isnull(sum(thDet.cdwsl), 0) cdwthsl, isnull(sum(thDet.ckpsl), 0) cdwytsl, max(zz.dwcb) dwcb"
				+ " from t_xsth_det thDet "
				+ " left join t_ywzz zz on thDet.spbh = zz.spbh and SUBSTRING(thDet.xsthlsh, 5, 2) = zz.bmbh and "
				+ " zz.jzsj = '" + DateUtil.getCurrentDateString("yyyyMM") + "' and zz.ckId is null";
				//+ " sum(thDet.spje) - isnull(SUM(kp.spje), 0) spje"
				//+ " left join"
				//+ " (select tk.xsthdetId, kpDet.spbh, SUM(zdwsl) zdwsl, SUM(kpDet.spje + kpDet.spse) spje from t_xsth_xskp tk left join t_xskp_det kpDet on tk.xskplsh = kpDet.xskplsh"
				//+ " group by tk.xsthdetId, kpDet.spbh) kp on thDet.id = kp.xsthDetId and thDet.spbh = kp.spbh";

		
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(xsthDetIds != null && xsthDetIds.trim().length() > 0){
//			String[] xs = xsthDetIds.split(",");
			sql += " where thDet.id in (" + xsthDetIds + ")";
//			for(int i = 0; i < xs.length; i++){
//				sql += " xd.id = ? ";
//				params.put(String.valueOf(i), xs[i]);
//				if(i != xs.length - 1){
//					sql += " or ";
//				}
// 			}
		}
		sql += " group by thDet.spbh";
		
		List<Object[]> l = detDao.findBySQL(sql, params);
		
		List<XsthDet> nl = new ArrayList<XsthDet>();
		
		for(Object[] os : l){
			String spbh = (String)os[0];
			BigDecimal zdwthsl = new BigDecimal(os[1].toString());
			BigDecimal zdwytsl = new BigDecimal(os[2].toString());
			BigDecimal shui = new BigDecimal("1").add(Constant.SHUILV);
			BigDecimal zdwdj_ws = new BigDecimal(os[3].toString());
			BigDecimal zdwdj = zdwdj_ws.divide(shui, 4, BigDecimal.ROUND_HALF_UP);
			BigDecimal cdwdj = new BigDecimal(os[4].toString());
			BigDecimal sphj = new BigDecimal(os[5].toString());
			BigDecimal cdwthsl = new BigDecimal(os[6].toString());
			BigDecimal cdwytsl = new BigDecimal(os[7].toString());
			BigDecimal dwcb = new BigDecimal(os[8].toString());
			
			BigDecimal spje = sphj.divide(shui, 2, BigDecimal.ROUND_HALF_UP); 
			
			TSp sp = spDao.get(TSp.class, spbh);
			XsthDet xd = new XsthDet();
			BeanUtils.copyProperties(sp, xd);
			xd.setZjldwId(sp.getZjldw().getId());
			xd.setZjldwmc(sp.getZjldw().getJldwmc());
			xd.setZdwthsl(zdwthsl);
			xd.setZdwytsl(zdwytsl);
			xd.setDwcb(dwcb);
			if(sp.getCjldw() != null){
				xd.setCjldwId(sp.getCjldw().getId());
				xd.setCjldwmc(sp.getCjldw().getJldwmc());
				xd.setZhxs(sp.getZhxs());
				xd.setCdwthsl(cdwthsl);
				xd.setCdwytsl(cdwytsl);
//				if(sp.getZhxs().compareTo(Constant.BD_ZERO) != 0){
//					xd.setCdwthsl(zdwthsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
//					xd.setCdwytsl(zdwytsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
//				}else{
//					xd.setCdwthsl(Constant.BD_ZERO);
//					xd.setCdwytsl(Constant.BD_ZERO);
//				}
			}
			xd.setZdwsl(zdwthsl.subtract(zdwytsl));
			if(sp.getCjldw() != null){
				xd.setCdwsl(xd.getCdwthsl().subtract(xd.getCdwytsl()));
			}
			xd.setZdwdj(zdwdj);
			xd.setCdwdj(cdwdj);
			xd.setSpje(spje);
			xd.setSpse(sphj.subtract(spje));
			xd.setSphj(sphj);
			
			nl.add(xd);
		}
		
		nl.add(new XsthDet());
		DataGrid dg = new DataGrid();
		dg.setRows(nl);
		return dg;
		
	}
	
	
	@Override
	public DataGrid getSpkc(Xsth xsth) {
		DataGrid dg = new DataGrid();
		List<ProBean> lists = new ArrayList<ProBean>();
		BigDecimal sl = Constant.BD_ZERO;
		
		if(xsth.getIsFhth().equals("1") && xsth.getFhId().trim().length() > 0){
			List<ProBean> fh = FhzzServiceImpl.getZzsl(xsth.getBmbh(), xsth.getSpbh(), xsth.getFhId(), fhzzDao);
			if(fh != null){
				sl = sl.add(new BigDecimal(fh.get(0).getValue()));
				lists.addAll(fh);
			}
		}else{
			List<ProBean> yw = YwzzServiceImpl.getZzsl(xsth.getBmbh(), xsth.getSpbh(), xsth.getCkId(), ywzzDao);
			if(yw != null){
				sl = sl.add(new BigDecimal(yw.get(0).getValue()));
				lists.addAll(yw);
			}
			
			List<ProBean> ls = LszzServiceImpl.getZzsl(xsth.getBmbh(), xsth.getSpbh(), xsth.getCkId(), lszzDao);
			if(ls != null){
				sl = sl.subtract(new BigDecimal(ls.get(0).getValue()));
				lists.addAll(ls);
			}
		}
		
		ProBean slBean = new ProBean();
		slBean.setGroup("可提货数量");
		slBean.setName("数量");
		slBean.setValue(sl.toString());
		
		slBean.setXscb(YwzzServiceImpl.getDwcb(xsth.getBmbh(), xsth.getSpbh(), ywzzDao).multiply(new BigDecimal(1.17)).setScale(4, BigDecimal.ROUND_HALF_UP));
		
		lists.add(0, slBean);
		
		dg.setRows(lists);
		dg.setTotal((long)lists.size());
		return dg;
	}
	
	@Override
	public DataGrid getYsje(Xsth xsth) {
		DataGrid dg = new DataGrid();
		BigDecimal ysje = YszzServiceImpl.getYsjeNoLs(xsth.getBmbh(), xsth.getKhbh(), xsth.getYwyId(), DateUtil.getCurrentDateString("yyyyMM"), yszzDao);
		dg.setObj(ysje);
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
	public void setYwrkDetDao(BaseDaoI<TYwrkDet> ywrkDetDao) {
		this.ywrkDetDao = ywrkDetDao;
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
	public void setSpBgyDao(BaseDaoI<TSpBgy> spBgyDao) {
		this.spBgyDao = spBgyDao;
	}

	@Autowired
	public void setDepDao(BaseDaoI<TDepartment> depDao) {
		this.depDao = depDao;
	}

	@Autowired
	public void setZsqrDao(BaseDaoI<TZsqr> zsqrDao) {
		this.zsqrDao = zsqrDao;
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
	public void setUserDao(BaseDaoI<TUser> userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
}
