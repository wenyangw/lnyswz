package lnyswz.jxc.service.impl;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lnyswz.jxc.bean.*;
import lnyswz.jxc.model.*;
import lnyswz.jxc.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.ProBean;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.service.XsthServiceI;

/**
 * 销售提货实现类
 * @author 王文阳
 * @edited
 * 	2015.08.12 增加打印销售合同
 * 	2015.08.12 直送业务不记入临时出库（各部门一致吗）
 */
@Service("xsthService")
public class XsthServiceImpl implements XsthServiceI {
	private BaseDaoI<TXsth> xsthDao;
	private BaseDaoI<TXsthDet> detDao;
	private BaseDaoI<TXskpDet> xskpDetDao;
	private BaseDaoI<TYwrkDet> ywrkDetDao;
	private BaseDaoI<TKfck> kfckDao;
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
	private BaseDaoI<TPrint> printDao;

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
		tXsth.setOut("0");
		tXsth.setSended("0");
		tXsth.setIsFp("0");
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

//			if("1".equals(xsth.getIsFh()) && "0".equals(xsth.getIsFhth())){
//				//if("1".equals(xsth.getIsFh())){
//				FhzzServiceImpl.updateFhzzSl(sp, dep, fh, xsthDet.getZdwsl(), Constant.UPDATE_RK, fhzzDao);
//			}
			
			if(intDetIds != null){
				for(int detId : intDetIds){
					TXskpDet xskpDet = xskpDetDao.load(TXskpDet.class, detId);
					if(xsthDet.getSpbh().equals(xskpDet.getSpbh())){
						xskpDet.setThsl(xskpDet.getThsl().add(xsthDet.getZdwsl()));
						//xskpDet.getTXskp().getTXsths().addAll(tDets);
						xskpDet.getTXskp().getTXsths().add(tDet);
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
		xsthDets.clear();
		xsthDets = null;
		tXsth.setTXsthDets(tDets);
		xsthDao.save(tXsth);
		
		tXsth.setTYwrks(ywrkDets);

		if("1".equals(xsth.getIsFh()) && "0".equals(xsth.getIsFhth())){
			for (TXsthDet tDet : tDets) {
				Sp s = new Sp();
				BeanUtils.copyProperties(tDet, s);
				FhzzServiceImpl.updateFhzzSl(s, dep, fh, tDet.getZdwsl(), Constant.UPDATE_RK, fhzzDao);
			}
		}

//		if(intDetIds != null){
//			xskp.getTXsths().addAll(tDets);
//		}
				
		OperalogServiceImpl.addOperalog(xsth.getCreateId(), xsth.getBmbh(), xsth.getMenuId(), tXsth.getXsthlsh(), 
				"生成销售提货单", operalogDao);

//		BufferedImage qrCode = QrCode.QrcodeImage(lsh);
//		QrCode.writeImage(qrCode, Util.getRootPath() + Constant.CODE_PATH + lsh + "z.png");
//
//		ZxingEAN13EncoderHandler handler = new ZxingEAN13EncoderHandler();
//		handler.encode(handler.getEAN13Code(lsh), 210, 60, Util.getRootPath() + Constant.CODE_PATH + lsh + ".png");

		Export.createCode(lsh);

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
		yTXsthDets.clear();
		yTXsthDets = null;

		if(ywrks != null && ywrks.size() > 0){
			yTXsth.setTYwrks(null);
		}
		
		tXsth.setTXsthDets(tDets);
		xsthDao.save(tXsth);
		
		OperalogServiceImpl.addOperalog(xsth.getCancelId(), xsth.getBmbh(), xsth.getMenuId(), tXsth.getCjXsthlsh() + "/" + tXsth.getXsthlsh(), 
			"取消销售提货单", operalogDao);
		
	}

	@Override
	public DataGrid getXsth(Xsth xsth) {
		DataGrid dg = new DataGrid();
		StringBuilder msg = new StringBuilder("");
		String thlb = xsth.getXsthlsh().substring(6, 8);
		boolean flag = true;

		if(thlb.equals("05")) {
			TXsth tXsth = xsthDao.get(TXsth.class, xsth.getXsthlsh());
			if (tXsth != null) {
				if(tXsth.getIsCancel().equals("0")) {
					if(tXsth.getIsZs().equals("0")) {
						if(tXsth.getIsFp().equals("0")){
							if(xsth.getType().equals("out") && tXsth.getOut().equals("1")) {
								flag = false;
								msg.append("单据已确认，\n请核对输入的流水号！！");
							}
							if(xsth.getType().equals("send")){
								if(tXsth.getOut().equals("0")) {
									flag = false;
									msg.append("单据还未出库复核，\n请核对输入的流水号！！");
								}
								if(tXsth.getSended().equals("1")) {
									flag = false;
									msg.append("单据已确认，\n请核对输入的流水号！！");
								}
							}
							if(flag){
								Xsth t = new Xsth();
								t.setXsthlsh(tXsth.getXsthlsh());
								t.setCreateTime(tXsth.getCreateTime());
								t.setBmmc(tXsth.getBmmc());
								t.setKhbh(tXsth.getKhbh());
								t.setKhmc(tXsth.getKhmc());
								t.setThfs(tXsth.getThfs());
								List<XsthDet> dets = new ArrayList<XsthDet>();
								XsthDet det = null;
								for (TXsthDet tXsthDet : tXsth.getTXsthDets()) {
									det = new XsthDet();
									det.setSpbh(tXsthDet.getSpbh());
									det.setSpmc(tXsthDet.getSpmc());
									det.setSpcd(tXsthDet.getSpcd());
									det.setSppp(tXsthDet.getSppp());
									det.setZjldwmc(tXsthDet.getZjldwmc());
									det.setZdwsl(tXsthDet.getZdwsl());
									dets.add(det);
								}
								dg.setObj(t);
								dg.setRows(dets);
							}
						}else {
							msg.append("单据为分批出库，\n请输入出库单流水号！！");
						}
					}else {
						msg.append("单据为直送单，\n请核对输入的流水号！！");
					}
				}else {
					msg.append("单据已被冲减，\n请核对输入的流水号！！");
				}
			} else {
				msg.append("未找到记录，\n请核对输入的流水号！！");
			}
		}else if(thlb.equals("11")){
			TKfck tKfck = kfckDao.get(TKfck.class, xsth.getXsthlsh());
			if (tKfck != null) {
				if(tKfck.getIsFp().equals("1")) {
					if (tKfck.getIsCj().equals("0")) {
						if(xsth.getType().equals("out") && tKfck.getOut().equals("1")) {
							flag = false;
							msg.append("单据已确认，\n请核对输入的流水号！！");
						}
						if(xsth.getType().equals("send")){
							if(tKfck.getOut().equals("0")) {
								flag = false;
								msg.append("单据还未出库复核，\n请核对输入的流水号！！");
							}
							if(tKfck.getSended().equals("1")) {
								flag = false;
								msg.append("单据已确认，\n请核对输入的流水号！！");
							}
						}
						if (flag) {
							Xsth t = new Xsth();
							t.setXsthlsh(tKfck.getKfcklsh());
							t.setCreateTime(tKfck.getCreateTime());
							t.setBmmc(tKfck.getBmmc());
							t.setKhbh(tKfck.getKhbh());
							t.setKhmc(tKfck.getKhmc());
							t.setThfs(tKfck.getThfs());
							List<XsthDet> dets = new ArrayList<XsthDet>();
							XsthDet det = null;
							for (TKfckDet tKfckDet : tKfck.getTKfckDets()) {
								det = new XsthDet();
								det.setSpbh(tKfckDet.getSpbh());
								det.setSpmc(tKfckDet.getSpmc());
								det.setSpcd(tKfckDet.getSpcd());
								det.setSppp(tKfckDet.getSppp());
								det.setZjldwmc(tKfckDet.getZjldwmc());
								det.setZdwsl(tKfckDet.getZdwsl());
								dets.add(det);
							}
							dg.setObj(t);
							dg.setRows(dets);
						}
					} else {
						msg.append("单据已被冲减，\n请核对输入的流水号！！");
					}
				}else{
					msg.append("此单据不是出库提货单，\n请核对输入的流水号！！");
				}
			} else {
				msg.append("未找到记录，\n请核对输入的流水号！！");
			}
		}
		dg.setMsg(msg.toString());
		return dg;
	}

	@Override
	public DataGrid getXsthOutList(Xsth xsth) {
		StringBuilder hqlXsth = new StringBuilder("from TXsth t");
		StringBuilder hqlKfck = new StringBuilder("from TKfck t");
		Map<String, Object> params = new HashMap<String, Object>();
		if(xsth.getType().equals("out")){
			String outHql = " where t.outId = :createId and t.outTime >= :createTime and t.outTime <= :endTime";
			hqlXsth.append(outHql);
			hqlKfck.append(outHql);
		}
		if(xsth.getType().equals("send")){
			String sendHql = " where t.sendId = :createId and t.sendTime >= :createTime and t.sendTime <= :endTime";
			hqlXsth.append(sendHql);
			hqlKfck.append(sendHql);
		}
		params.put("createId", xsth.getCreateId());
		params.put("createTime", xsth.getCreateTime());
		params.put("endTime", xsth.getEndTime());

		if(xsth.getKhmc() != null){
			String khmcHql = " and t.khmc like :khmc";
			hqlXsth.append(khmcHql);
			hqlKfck.append(khmcHql);
			params.put("khmc", "%" + xsth.getKhmc() + "%");
		}

		List<Xsth> results = new ArrayList<Xsth>();
		Xsth t;
		List<TXsth> tXsths = xsthDao.find(hqlXsth.toString(), params);
		if (tXsths != null && tXsths.size() > 0){
			for (TXsth tXsth : tXsths) {
				t = new Xsth();
				t.setXsthlsh(tXsth.getXsthlsh());
				t.setCreateTime(xsth.getType().equals("out") ? tXsth.getOutTime() : tXsth.getSendTime());
				t.setKhbh(tXsth.getKhbh());
				t.setKhmc(tXsth.getKhmc());
				t.setThfs(tXsth.getThfs());
				results.add(t);
			}
		}
		List<TKfck> tKfcks = kfckDao.find(hqlKfck.toString(), params);
		if (tKfcks != null && tKfcks.size() > 0){
			for (TKfck tKfck : tKfcks) {
				t = new Xsth();
				t.setXsthlsh(tKfck.getKfcklsh());
				t.setCreateTime(xsth.getType().equals("out") ? tKfck.getOutTime() : tKfck.getSendTime());
				t.setKhbh(tKfck.getKhbh());
				t.setKhmc(tKfck.getKhmc());
				t.setThfs(tKfck.getThfs());
				results.add(t);
			}
		}

		DataGrid dg = new DataGrid();
		if(results.size() > 0){
			dg.setMsg("");
			dg.setRows(results);
		}else{
			dg.setMsg("没有符合条件的记录");
		}

		return dg;
	}

	@Override
	public DataGrid getXsthOutDetail(Xsth xsth) {
		List<Xsth> results = new ArrayList<Xsth>();
		String thlb = xsth.getXsthlsh().substring(6, 8);
		Xsth t = null;
		if(thlb.equals("05")){
			Set<TXsthDet> tXsthDets = xsthDao.get(TXsth.class, xsth.getXsthlsh()).getTXsthDets();
			for (TXsthDet tXsthDet : tXsthDets) {
				t = new Xsth();
				t.setSpbh(tXsthDet.getSpbh());
				t.setSpmc(tXsthDet.getSpmc());
				t.setSpcd(tXsthDet.getSpcd());
				t.setSppp(tXsthDet.getSppp());
				t.setZjldwmc(tXsthDet.getZjldwmc());
				t.setZdwsl(tXsthDet.getZdwsl());
				results.add(t);
			}
		}
		if(thlb.equals("11")){
			Set<TKfckDet> tKfckDets = kfckDao.get(TKfck.class, xsth.getXsthlsh()).getTKfckDets();
			for (TKfckDet tKfckDet : tKfckDets) {
				t = new Xsth();
				t.setSpbh(tKfckDet.getSpbh());
				t.setSpmc(tKfckDet.getSpmc());
				t.setSpcd(tKfckDet.getSpcd());
				t.setSppp(tKfckDet.getSppp());
				t.setZjldwmc(tKfckDet.getZjldwmc());
				t.setZdwsl(tKfckDet.getZdwsl());
				results.add(t);
			}
		}

		DataGrid dg = new DataGrid();
		if(results.size() > 0){
			dg.setMsg("");
			dg.setRows(results);
		}else{
			dg.setMsg("没有符合条件的记录");
		}
		return dg;
	}

	@Override
	public DataGrid xsthSpDg(Xsth xsth) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TXsth t where t.isCancel = '0' and t.isFh = '0' and t.isZs = '0' and t.bmbh = :bmbh and t.needAudit <> t.isAudit and t.isAudit <> '9'";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", xsth.getBmbh());

		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";

		List<TXsth> l = xsthDao.find(hql, params, xsth.getPage(), xsth.getRows());
		List<Xsth> nl = new ArrayList<Xsth>();
		Xsth c = null;
		for(TXsth t : l){
			c = new Xsth();
			BeanUtils.copyProperties(t, c);

			nl.add(c);
		}
		datagrid.setTotal(xsthDao.count(countHql, params));
		datagrid.setRows(nl);

		l.clear();
		l = null;

		return datagrid;
	}

	@Override
	public DataGrid	xsthCarDg(Xsth xsth) {
		StringBuilder sqlCount = new StringBuilder("select count(*)");
		StringBuilder sql = new StringBuilder("select w.bmbh, w.bmmc, w.xsthlsh, w.createTime, w.khbh, w.khmc, w.ywymc, isnull(w.shdz, '') shdz, w.bz, w.hjsl, isnull(s.carNum, '') carNum");
		String sqlWhere = " from v_wait_car w  left join v_set_car s on w.xsthlsh = s.lsh where w.bmbh = ?";
		sqlCount.append(sqlWhere);
		sql.append(sqlWhere);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", xsth.getBmbh());

		if(xsth.getSearch() != null){
			String searchStr = " and (w.khmc like ? or w.xsthlsh like ?)";
			sql.append(searchStr);
			sqlCount.append(searchStr);
			params.put("1", "%" + xsth.getSearch() + "%");
			params.put("2", "%" + xsth.getSearch() + "%");
		}

		sql.append(" order by w.createTime desc");
		List<Xsth> results = new ArrayList<Xsth>();
		Xsth t;
		List<Object[]> tXsths = xsthDao.findBySQL(sql.toString(), params, xsth.getPage(), xsth.getRows());
		if (tXsths != null && tXsths.size() > 0){
			for (Object[] tXsth : tXsths) {
				t = new Xsth();
				t.setXsthlsh(tXsth[2].toString());
				t.setCreateTime(DateUtil.stringToDate(tXsth[3].toString(), DateUtil.DATETIME_PATTERN));
				t.setKhbh(tXsth[4].toString());
				t.setKhmc(tXsth[5].toString());
				t.setYwymc(tXsth[6].toString());
				t.setShdz(tXsth[7].toString());
				t.setBz(tXsth[8].toString());
				t.setHjsl(new BigDecimal(tXsth[9].toString()));
				t.setCarNum(tXsth[10].toString());
				results.add(t);
			}
		}

		DataGrid dg = new DataGrid();
		dg.setTotal(xsthDao.countSQL(sqlCount.toString(), params));
		dg.setRows(results);

		return dg;
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
		dets.clear();
		dets = null;
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
		String codePath = Util.getRootPath() + Constant.CODE_PATH + tXsth.getXsthlsh() + ".png";
		//map.put("codeFile", codePath.replace("/","\\"));
		map.put("codeFile", codePath);

		datagrid.setObj(map);
		datagrid.setRows(nl);

		savePrintRecord(xsth);

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
		
		dets.clear();
		dets = null;
		return datagrid;
	}

	
	@Override
	public DataGrid printXsht(Xsth xsth) {
		DataGrid datagrid = new DataGrid();
		TXsth tXsth = xsthDao.load(TXsth.class, xsth.getXsthlsh());
		
		List<XsthDet> nl = new ArrayList<XsthDet>();
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
		int payDays = 1;
		if(tXsth.getPayDays() != 0){
			payDays = tXsth.getPayDays(); 
		}else{
			if(khDet != null){
				if(((String)khDet[0]).equals("03")){
					payDays = 30;
				}
				if(((String)khDet[0]).equals("02")){
					payDays = Integer.valueOf(khDet[1].toString());
				}
			}
		}

		//客户基本信息
		TKh tKh = khDao.load(TKh.class, tXsth.getKhbh());

		String khkhh = "";
		String khzh = "";

		if(tKh.getKhh() != null && tKh.getKhh().trim().length() > 0) {
			String pattern = "((\\d+-)*\\d+$)";
			Pattern r = Pattern.compile(pattern);
			// 现在创建 matcher 对象
			Matcher m = r.matcher(tKh.getKhh());
			if (m.find()) {
				khzh = m.group(1);
				khkhh = tKh.getKhh().substring(0, tKh.getKhh().indexOf(khzh)).trim();
			}
		}
//		if(tKh.getDzdh() != null && tKh.getDzdh().trim().length() > 0) {
//			// 现在创建 matcher 对象
//			m = r.matcher(tKh.getDzdh());
//			if (m.find()) {
//				khdh = m.group(1);
//				khdz = tKh.getDzdh().substring(0, tKh.getDzdh().indexOf(khdh)).trim();
//				System.out.println("------------------------------" + khdz);
//			}
//		}

		DecimalFormat df=new DecimalFormat("#,##0.00");
		BigDecimal hjje_b=new BigDecimal(String.format("%.2f", tXsth.getHjje())); 
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bmmc", Constant.BMMCS.get(tXsth.getBmbh()));
		map.put("bmdz", Constant.BMDZS.get(tXsth.getBmbh()));
		//map.put("bmdh", Constant.BMDH.get(tXsth.getBmbh()));
		map.put("bmkhh", Constant.BMKHH.get(tXsth.getBmbh()));
		map.put("bmzh", Constant.BMZH.get(tXsth.getBmbh()));
		map.put("htdz", Constant.HTDZS.get(tXsth.getBmbh()));
		map.put("xsthlsh", tXsth.getXsthlsh());
		map.put("khmc", tXsth.getKhmc());
		map.put("khdz", tKh.getDzdh() == null ? "" : tKh.getDzdh());
		//map.put("khdh", khdh);
		map.put("khkhh", khkhh);
		map.put("khzh", khzh);
		map.put("shdz", tXsth.getShdz() == null ? "" : tXsth.getShdz());
		map.put("thr", tXsth.getThr() == null ? "" : tXsth.getThr());
		map.put("payDays", payDays);
		map.put("hjje", df.format(tXsth.getHjje()));
		map.put("hjje_b", AmountToChinese.numberToChinese(hjje_b));
		map.put("createTime", DateUtil.getYear(tXsth.getCreateTime()) + " 年 " +
				DateUtil.getMonth(tXsth.getCreateTime()) + " 月 " +
				DateUtil.getDay(tXsth.getCreateTime()) + " 日");

		datagrid.setObj(map);
		datagrid.setRows(nl);
		
		dets.clear();
		dets = null;
		return datagrid;
	}
	
	@Override
	public DataGrid printXsthByBgy(Xsth xsth) {
		DataGrid datagrid = new DataGrid();
		TXsth tXsth = xsthDao.load(TXsth.class, xsth.getXsthlsh());
		
		List<XsthDet> nl = new ArrayList<XsthDet>(tXsth.getTXsthDets().size());
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
		if(tXsth.getBookmc() != null){
			bz += " /" + tXsth.getBookmc();
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

		savePrintRecord(xsth);
//
//		Print print = new Print();
//		print.setLsh(xsth.getXsthlsh());
//		print.setPrintId(xsth.getCreateId());
//		print.setPrintName(xsth.getCreateName());
//		print.setPrintTime(new Date());
//		print.setType(xsth.getType());
//		print.setBgyId(xsth.getBgyId());
//
//		PrintServiceImpl.save(print, printDao);

		return datagrid;
	}

	private void savePrintRecord(Xsth xsth){
		Print print = new Print();
		print.setLsh(xsth.getXsthlsh());
		print.setPrintId(xsth.getCreateId());
		print.setPrintName(xsth.getCreateName());
		print.setPrintTime(new Date());
		print.setType(xsth.getType());
		print.setBgyId(xsth.getBgyId());

		PrintServiceImpl.save(print, printDao);
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
		
		l.clear();
		l = null;
		
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
		
		l.clear();
		l = null;
		
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
			if(tXsthDet.getTCgjh() != null){
				c.setCgjhlsh(tXsthDet.getTCgjh().getCgjhlsh());
			}
			
			String sql_sh = "select isnull(bz, '') bz from t_ywsh t where t.lsh = ? and t.bz <> ''";
			Map<String, Object> params_sh = new HashMap<String, Object>();
			params_sh.put("0", t.getXsthlsh());
			
			List<Object> os = xsthDao.findOneBySQL(sql_sh, params_sh);

			if(os != null && os.size() > 0){
				String bzs = "";
				for(Object o : os){
					String sbz = o.toString();
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
		
		l.clear();
		l = null;
		
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
				hql += " and (" + Util.getQueryWhere(xsth.getSearch(), new String[]{"t.TXsth.xsthlsh", "t.TXsth.khbh", "t.TXsth.khmc", "t.TXsth.bookmc", "t.TXsth.bz", "t.TXsth.ywymc", "t.TXsth.fhmc", "t.spbh"}, params) + ")";
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


			if(xsth.getFromOther().equals("fromKfck")){
				String sqlStatus = "select dbo.getXsthStatusInfo(xsthlsh) statusInfo, dbo.getCarNum(xsthlsh) carNum from t_xsth where xsthlsh = ?";
				Map<String, Object> paramsStatus = new HashMap<String, Object>();
				paramsStatus.put("0", tXsth.getXsthlsh());
				List<Object[]> o = detDao.findBySQL(sqlStatus, paramsStatus);
				//c.setType(detDao.getBySQL(sqlStatus, paramsStatus).toString());
				c.setType(o.get(0)[0].toString());
				c.setCarNum(o.get(0)[1].toString());
			}

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
		
		l.clear();
		l = null;
		
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
		return new BigDecimal(detDao.getBySQL(detHql, detParams).toString());
	}
	
	private BigDecimal getYksl(String xsthlsh, String spbh) {
		String detHql = "select isnull(sum(kpDet.zdwsl), 0) from t_xskp_det kpDet "
				+ "left join t_xsth_xskp xx on kpDet.xskplsh = xx.xskplsh "
				+ "left join t_xsth_det thDet on thDet.id = xx.xsthdetId and thDet.spbh = kpDet.spbh "
				+ "where thDet.xsthlsh = ? and thDet.spbh = ?";
		Map<String, Object> detParams = new HashMap<String, Object>();
		detParams.put("0", xsthlsh);
		detParams.put("1", spbh);
		return new BigDecimal(detDao.getBySQL(detHql, detParams).toString());
	}

	@Override
	public boolean isSaved(Xsth xsth){
		String hql = "from TXsth t where t.verifyCode = :verifyCode";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("verifyCode", xsth.getVerifyCode());
		TXsth tXsth = xsthDao.get(hql, params);
		if(tXsth != null){
			return true;
		}
		return false;
	}

	@Override
	public boolean isLocked(Xsth xsth){
		TXsth tXsth = xsthDao.load(TXsth.class, xsth.getXsthlsh());
		if("1".equals(tXsth.getLocked())){
			return true;
		}
		return false;
	}

	@Override
	public boolean isCancel(Xsth xsth){
		String[] ids = xsth.getXsthDetIds().split(",");
		for (String id : ids) {
			TXsthDet tDet = detDao.load(TXsthDet.class, Integer.parseInt(id));
			if("1".equals(tDet.getTXsth().getIsCancel())){
				return true;
			}
		}
		return false;
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
			}
			nl.add(xd);
		}
		nl.add(new XsthDet());
		DataGrid dg = new DataGrid();
		dg.setRows(nl);
		
		l.clear();

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
			}
			nl.add(xd);
		}
		nl.add(new XsthDet());
		DataGrid dg = new DataGrid();
		dg.setRows(nl);
		
		l.clear();

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
		BigDecimal sl;
		BigDecimal csl = BigDecimal.ZERO;
		BigDecimal lsje;

		//获取修改的商品记录
		TXsthDet tXsthDet = detDao.load(TXsthDet.class, xsth.getId());

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
	public void updateShdz(Xsth xsth) {
		TXsth tXsth = xsthDao.get(TXsth.class, xsth.getXsthlsh());
		tXsth.setShdz(xsth.getShdz());
		OperalogServiceImpl.addOperalog(xsth.getCreateId(), xsth.getBmbh(), xsth.getMenuId(), xsth.getXsthlsh(),
				"修改送货地址", operalogDao);
	}

	@Override
	public void updateXsthOut(Xsth xsth) {
		String thlb = xsth.getXsthlsh().substring(6, 8);
		TUser tUser = userDao.get(TUser.class, xsth.getCreateId());
		if(thlb.equals("05")) {
			TXsth tXsth = xsthDao.get(TXsth.class, xsth.getXsthlsh());
			if(xsth.getType().equals("out")) {
				tXsth.setOut("1");
				tXsth.setOutId(xsth.getCreateId());
				tXsth.setOutName(tUser.getRealName());
				tXsth.setOutTime(new Date());
				//自提时，同步更新send
				if(tXsth.getThfs().equals("1")){
					tXsth.setSended("1");
					tXsth.setSendId(xsth.getCreateId());
					tXsth.setSendName(tUser.getRealName());
					tXsth.setSendTime(new Date());
				}
			}
			if(xsth.getType().equals("send")) {
				tXsth.setSended("1");
				tXsth.setSendId(xsth.getCreateId());
				tXsth.setSendName(tUser.getRealName());
				tXsth.setSendTime(new Date());
			}
		}
		if(thlb.equals("11")){
			TKfck tKfck = kfckDao.get(TKfck.class, xsth.getXsthlsh());
			if(xsth.getType().equals("out")) {
				tKfck.setOut("1");
				tKfck.setOutId(xsth.getCreateId());
				tKfck.setOutName(tUser.getRealName());
				tKfck.setOutTime(new Date());
				if(tKfck.getThfs().equals("1")){
					tKfck.setSended("1");
					tKfck.setSendId(xsth.getCreateId());
					tKfck.setSendName(tUser.getRealName());
					tKfck.setSendTime(new Date());
				}
				//最后一张分批出库后，同步更新对应t_xsth的out，自提要更新send
				String xsthlsh = tKfck.getTXsths().iterator().next().getTXsth().getXsthlsh();
				String sqlXsth = "select count(*) from v_xsth_det where out = '0' and zdwsl = cksl and xsthlsh = ?";
				Map<String, Object> paramsXsth = new HashMap<String, Object>();
				paramsXsth.put("0", xsthlsh);
				if(kfckDao.countSQL(sqlXsth, paramsXsth) > 0l){
					TXsth t = xsthDao.get(TXsth.class, xsthlsh);
					t.setOut("1");
					t.setOutId(xsth.getCreateId());
					t.setOutName(tUser.getRealName());
					t.setOutTime(new Date());
					if(t.getThfs().equals("1")){
						t.setSended("1");
						t.setSendId(xsth.getCreateId());
						t.setSendName(tUser.getRealName());
						t.setSendTime(new Date());
					}
				}
			}
			if(xsth.getType().equals("send")) {
				tKfck.setSended("1");
				tKfck.setSendId(xsth.getCreateId());
				tKfck.setSendName(tUser.getRealName());
				tKfck.setSendTime(new Date());

				//最后一张分批到货后，同步更新对应t_xsth的send
				String xsthlsh = tKfck.getTXsths().iterator().next().getTXsth().getXsthlsh();
				String sqlXsth = "select count(*) from v_xsth_det where sended = '0' and zdwsl = cksl and xsthlsh = ?";
				Map<String, Object> paramsXsth = new HashMap<String, Object>();
				paramsXsth.put("0", xsthlsh);
				if(kfckDao.countSQL(sqlXsth, paramsXsth) > 0l){
					TXsth t = xsthDao.get(TXsth.class, xsthlsh);
					t.setSended("1");
					t.setSendId(xsth.getCreateId());
					t.setSendName(tUser.getRealName());
					t.setSendTime(new Date());
				}
			}
		}
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

	/**
	 * 有同一品种出现两次以上且单价不同，合并开票时：
	 * 	04：按最大单价，尽量不开在一起
	 * 	其他：按金额合计为准，重新计算单价
	 */
	@Override
	public DataGrid toXskp(Xsth xsth){
		String sql;
		
		if(xsth.getBmbh().equals("04")){
			sql = "select thDet.spbh, isnull(sum(thDet.zdwsl), 0) zdwthsl, isnull(sum(thDet.kpsl), 0) zdwytsl,"
				+ " max(thDet.zdwdj) zdwdj, max(thDet.cdwdj) cdwdj,"
				+ " cast(round(sum(thDet.zdwsl - thDet.kpsl) * max(thDet.zdwdj), 2) as numeric(12, 2))  spje,"
				+ " isnull(sum(thDet.cdwsl), 0) cdwthsl, isnull(sum(thDet.ckpsl), 0) cdwytsl, max(zz.dwcb) dwcb"
				+ " from t_xsth_det thDet "
				+ " left join t_ywzz zz on thDet.spbh = zz.spbh and SUBSTRING(thDet.xsthlsh, 5, 2) = zz.bmbh and "
				+ " zz.jzsj = '" + DateUtil.getCurrentDateString("yyyyMM") + "' and zz.ckId is null";
		}else{
			sql = "select thDet.spbh, isnull(sum(thDet.zdwsl), 0) zdwthsl, isnull(sum(thDet.kpsl), 0) zdwytsl,"
				+ " convert(numeric(18,4), sum(spje) / sum(thDet.zdwsl)) zdwdj,"
				+ " convert(numeric(18, 4), case when sum(isnull(thDet.cdwsl, 0)) = 0 then 0 else sum(spje) / sum(thDet.cdwsl) end) cdwdj,"
				+ " convert(numeric(18, 2), sum(thDet.zdwsl - thDet.kpsl) * (sum(spje) / sum(thDet.zdwsl))) spje,"
				+ " isnull(sum(thDet.cdwsl), 0) cdwthsl, isnull(sum(thDet.ckpsl), 0) cdwytsl, max(zz.dwcb) dwcb"
				+ " from t_xsth_det thDet"
				+ " left join t_ywzz zz on thDet.spbh = zz.spbh and SUBSTRING(thDet.xsthlsh, 5, 2) = zz.bmbh and"
				+ " zz.jzsj = '" + DateUtil.getCurrentDateString("yyyyMM") + "' and zz.ckId is null";
		}
				
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(xsth.getXsthDetIds() != null && xsth.getXsthDetIds().trim().length() > 0){
			sql += " where thDet.id in (" + xsth.getXsthDetIds() + ")";
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
		
		l.clear();
		l = null;
		
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
	public void setKfckDao(BaseDaoI<TKfck> kfckDao) {
		this.kfckDao = kfckDao;
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
	public void setPrintDao(BaseDaoI<TPrint> printDao) {
		this.printDao = printDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
}
