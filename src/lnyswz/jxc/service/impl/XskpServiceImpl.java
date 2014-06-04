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

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.ProBean;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Ck;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.Fh;
import lnyswz.jxc.bean.Kh;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.bean.Xskp;
import lnyswz.jxc.bean.XskpDet;
import lnyswz.jxc.bean.Xsth;
import lnyswz.jxc.bean.XsthDet;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TFhzz;
import lnyswz.jxc.model.TKh;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.model.TLszz;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.model.TXskp;
import lnyswz.jxc.model.TXskpDet;
import lnyswz.jxc.model.TXsth;
import lnyswz.jxc.model.TXsthDet;
import lnyswz.jxc.model.TYszz;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.service.XskpServiceI;
import lnyswz.jxc.util.Constant;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * 采购需求实现类
 * @author 王文阳
 *
 */
@Service("xskpService")
public class XskpServiceImpl implements XskpServiceI {
	private Logger logger = Logger.getLogger(XskpServiceImpl.class);
	private BaseDaoI<TXskp> xskpDao;
	private BaseDaoI<TXskpDet> detDao;
	private BaseDaoI<TXsth> xsthDao;
	private BaseDaoI<TXsthDet> xsthDetDao;
	private BaseDaoI<TYwzz> ywzzDao;
	private BaseDaoI<TYszz> yszzDao;
	private BaseDaoI<TFhzz> fhzzDao;
	private BaseDaoI<TLszz> lszzDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TOperalog> operalogDao;
	
	

	@Override
	public Xskp save(Xskp xskp) {
		String lsh = LshServiceImpl.updateLsh(xskp.getBmbh(), xskp.getLxbh(), lshDao);
		//获取前台传递的销售提货记录号
		String xsthDetIds = xskp.getXsthDetIds();

		//是否需要生成销售提货
		boolean needXsth = "1".equals(xskp.getNeedXsth());
		
		TXskp tXskp = new TXskp();
		BeanUtils.copyProperties(xskp, tXskp);
		tXskp.setCreateTime(new Date());
		tXskp.setCreateName(xskp.getCreateName());
		tXskp.setXskplsh(lsh);
		tXskp.setIsCj("0");
		
		String bmmc = depDao.load(TDepartment.class, xskp.getBmbh()).getDepName();
		tXskp.setBmmc(bmmc);
		
		tXskp.setNeedAudit("0");
		if(tXskp.getNeedAudit().equals("1")){
			tXskp.setIsAudit("0");
		}else{
			tXskp.setIsAudit("1");
		}
		
		Set<TXsthDet> xsthDets = null;
		Set<String> thdlshs = null;
		int[] intDetIds = null;
		//如果从销售提货生成的销售开票，进行关联
		if(xsthDetIds != null && xsthDetIds.trim().length() > 0){
			tXskp.setFromTh("1");
			
			String[] strDetIds = xsthDetIds.split(",");
			intDetIds = new int[strDetIds.length];
//			Set<TXsthDet> tXsthDets = new HashSet<TXsthDet>();
			thdlshs = new HashSet<String>();
			xsthDets = new HashSet<TXsthDet>();
			int i = 0;
			for(String detId : strDetIds){
				intDetIds[i] = Integer.valueOf(detId);
//				TXsthDet tXsthDet = xsthDetDao.load(TXsthDet.class, Integer.valueOf(detId));
//				tXsthDets.add(tXsthDet);
				i++;
			}
//			tXskp.setTXsths(tXsthDets);
			Arrays.sort(intDetIds);
		}else{
			tXskp.setFromTh("0");
		}
		
		Department dep = new Department();
		dep.setId(xskp.getBmbh());
		dep.setDepName(bmmc);
		
		Ck ck = new Ck();
		ck.setId(xskp.getCkId());
		ck.setCkmc(xskp.getCkmc());
		
		Kh kh = new Kh();
		kh.setKhbh(xskp.getKhbh());
		kh.setKhmc(xskp.getKhmc());

		tXskp.setIsHk("0");
		//授信客户，并且未从销售提货导入，更新应收
		if("1".equals(xskp.getIsSx())){
			if(xsthDetIds == null){
				YszzServiceImpl.updateYszzJe(dep, kh, tXskp.getHjje().add(tXskp.getHjse()), Constant.UPDATE_YS_KP, yszzDao);
			}else{
				YszzServiceImpl.updateYszzJe(dep, kh, tXskp.getHjje().add(tXskp.getHjse()), Constant.UPDATE_YS_KP_LS, yszzDao);
			}
		}
		
		//生成销售提货表头数据
		TXsth tXsth = null;
		//生成销售提货明细集合
		Set<TXsthDet> tXsthDets = null;
		BigDecimal hjsl = Constant.BD_ZERO;
		if(needXsth){
			tXskp.setIsTh("1");
			
			tXsth = new TXsth();
			BeanUtils.copyProperties(tXskp, tXsth);
			tXsth.setXsthlsh(LshServiceImpl.updateLsh(xskp.getBmbh(), Constant.XSTH_LX, lshDao));
			tXsth.setToFp("1");
			tXsth.setFromFp("1");
			tXsth.setIsKp("1");
			tXsth.setLocked("0");
			tXsth.setIsCancel("0");
			tXsth.setIsFh("1".equals(xskp.getIsFh()) ? "1" : "0");
			tXsth.setIsFhth("0");
			//tXsth.setThfs("1");
			tXsth.setIsLs("0");
			tXsth.setHjje(tXskp.getHjje().add(tXskp.getHjse()));
			
			tXsthDets = new HashSet<TXsthDet>();
		}else{
			tXskp.setIsTh("0");
		}
		
		//处理商品明细
		Set<TXskpDet> tDets = new HashSet<TXskpDet>();
		ArrayList<XskpDet> xskpDets = JSON.parseObject(xskp.getDatagrid(), new TypeReference<ArrayList<XskpDet>>(){});
		for(XskpDet xskpDet : xskpDets){
			TXskpDet tDet = new TXskpDet();
			BeanUtils.copyProperties(xskpDet, tDet);
			if("".equals(xskpDet.getCjldwId())){
				tDet.setCdwdj(Constant.BD_ZERO);
				tDet.setCdwsl(Constant.BD_ZERO);
				tDet.setZhxs(Constant.BD_ZERO);
			}else{
				if(xskpDet.getZhxs().compareTo(Constant.BD_ZERO) == 0){
					tDet.setCdwdj(Constant.BD_ZERO);
					tDet.setCdwsl(Constant.BD_ZERO);
				}
			}
			tDet.setLastThsl(Constant.BD_ZERO);
			tDet.setThsl(Constant.BD_ZERO);
			tDet.setTXskp(tXskp);
			
			Sp sp = new Sp();
			BeanUtils.copyProperties(xskpDet, sp);

			//获得单位成本
			BigDecimal dwcb = YwzzServiceImpl.getDwcb(xskp.getBmbh(), xskpDet.getSpbh(), ywzzDao);
			tDet.setXscb(dwcb.multiply(xskpDet.getZdwsl()));

			
			tDets.add(tDet);
			
			//生成销售提货明细数据
			if(needXsth){
				tDet.setLastThsl(xskpDet.getZdwsl());
				tDet.setThsl(xskpDet.getZdwsl());
				
				TXsthDet tXsthDet = new TXsthDet();
				BeanUtils.copyProperties(tDet, tXsthDet, new String[]{"id"});
				tXsthDet.setCksl(Constant.BD_ZERO);
				tXsthDet.setKpsl(tDet.getZdwsl());
				//发票单价不含税，提货单单价含税
				tXsthDet.setZdwdj(tDet.getZdwdj().multiply(new BigDecimal(1).add(Constant.SHUILV)));
				//提货单只有金额字段，要将发票中金额与税额相加
				tXsthDet.setSpje(tDet.getSpje().add(tDet.getSpse()));
				tXsthDets.add(tXsthDet);
				tXsthDet.setTXsth(tXsth);
				hjsl.add(tDet.getCdwsl());
			}
			
			//更新业务总账
			YwzzServiceImpl.updateYwzzSl(sp, dep, ck, tDet.getZdwsl(), tDet.getSpje(), tDet.getSpse(), tDet.getXscb(), Constant.UPDATE_CK, ywzzDao);
			
			//更新分户
			if("1".equals(xskp.getIsFh()) && (xsthDetIds == null || xsthDetIds.equals(""))){
				Fh fh = new Fh();
				fh.setId(xskp.getFhId());
				fh.setFhmc(xskp.getFhmc());
				
				FhzzServiceImpl.updateFhzzSl(sp, dep, fh, tDet.getZdwsl(), Constant.UPDATE_RK, fhzzDao);
			}
			
			//从销售提货生成销售开票，更新临时总账
			if(xsthDetIds != null && xsthDetIds.trim().length() > 0){
				LszzServiceImpl.updateLszzSl(sp, dep, tDet.getZdwsl(), tDet.getSpje().add(tDet.getSpse()), Constant.UPDATE_CK, lszzDao);
			}
			
//			TXsthDet tXsthDet = xsthDetDao.load(TXsthDet.class, Integer.valueOf(detId));
			if(intDetIds != null){
				BigDecimal kpsl = xskpDet.getZdwsl();
				for(int detId : intDetIds){
					TXsthDet xsthDet = xsthDetDao.load(TXsthDet.class, detId);
					thdlshs.add(xsthDet.getTXsth().getXsthlsh());
					if(xskpDet.getSpbh().equals(xsthDet.getSpbh())){
						BigDecimal wksl = xsthDet.getZdwsl().subtract(xsthDet.getKpsl());
						xsthDets.add(xsthDet);
						if(kpsl.compareTo(wksl) == 1){
							xsthDet.setKpsl(xsthDet.getKpsl().add(wksl));
							kpsl = kpsl.subtract(wksl);
						}else{
							xsthDet.setKpsl(xsthDet.getKpsl().add(kpsl));
							tDet.setLastThsl(kpsl);
							break;
						}
					}
				}
			}
			
		}
		tXskp.setTXsths(xsthDets);
		tXskp.setTXskpDets(tDets);
		if(thdlshs != null){
			tXskp.setBz(xskp.getBz() + thdlshs.toString());
		}
		
		
		//保存销售提货数据
		if(needXsth){
			tXskp.setTXsths(tXsthDets);
			tXsth.setHjsl(hjsl);
			tXsth.setTXsthDets(tXsthDets);
			xsthDao.save(tXsth);
		}
		
		//保存单据
		xskpDao.save(tXskp);
		
		OperalogServiceImpl.addOperalog(xskp.getCreateId(), xskp.getBmbh(), xskp.getMenuId(), tXskp.getXskplsh(), 
				"生成销售开票单", operalogDao);
		
		Xskp rXskp = new Xskp();
		rXskp.setXskplsh(lsh);
		rXskp.setFplxId(tXskp.getFplxId());
		return rXskp;
	}
	
	@Override
	public void cjXskp(Xskp xskp) {
		//??
		//是否需要生成销售提货
		//boolean needXsth = true;
		
		Date now = new Date();
		String lsh = LshServiceImpl.updateLsh(xskp.getBmbh(), xskp.getLxbh(), lshDao);
		//更新原单据信息
		TXskp yTXskp = xskpDao.get(TXskp.class, xskp.getXskplsh());
		//新增冲减单据信息
		TXskp tXskp = new TXskp();
		BeanUtils.copyProperties(yTXskp, tXskp, new String[]{"TXsths"});

		//更新原单据信息
		yTXskp.setCjId(xskp.getCjId());
		yTXskp.setCjTime(now);
		yTXskp.setCjName(xskp.getCjName());
		yTXskp.setIsCj("1");

		//??
		//直送不需要生成销售提货
		//if("0".equals(yTXskp.getIsTh())){
		//	needXsth = false;
		//}
		
		tXskp.setXskplsh(lsh);
		tXskp.setCjXskplsh(yTXskp.getXskplsh());
		tXskp.setCreateId(xskp.getCjId());
		tXskp.setCreateTime(now);
		tXskp.setCreateName(xskp.getCjName());
		tXskp.setIsCj("1");
		tXskp.setCjId(xskp.getCjId());
		tXskp.setCjName(xskp.getCjName());
		tXskp.setCjTime(now);
		tXskp.setHjje(tXskp.getHjje().negate());
		tXskp.setHjse(tXskp.getHjse().negate());
		tXskp.setBz(xskp.getBz());
		
		Department dep = new Department();
		dep.setId(xskp.getBmbh());
		dep.setDepName(depDao.load(TDepartment.class, xskp.getBmbh()).getDepName());
		
		Ck ck = new Ck();
		ck.setId(tXskp.getCkId());
		ck.setCkmc(tXskp.getCkmc());
		
		//??
		//TODO 同步生成的如何处理，已提货如何处理
		//先处理通过销售提货生成的销售开票单
		//原单据关联的销售提货清除,
		int[] intXsthDetIds = null;
		if(yTXskp.getTXsths() != null){
			intXsthDetIds = new int[yTXskp.getTXsths().size()];
			int i = 0;
			for(TXsthDet t : yTXskp.getTXsths()){
				intXsthDetIds[i] = t.getId();
				i++;
			}
			Arrays.sort(intXsthDetIds);
		}
		
		Set<TXskpDet> yTXskpDets = yTXskp.getTXskpDets();
		Set<TXskpDet> tDets = new HashSet<TXskpDet>();
		for(TXskpDet yTDet : yTXskpDets){
			TXskpDet tDet = new TXskpDet();
			BeanUtils.copyProperties(yTDet, tDet, new String[]{"id"});
			tDet.setZdwsl(yTDet.getZdwsl().negate());
			if(yTDet.getCdwsl() != null){
				tDet.setCdwsl(yTDet.getCdwsl().negate());
			}
			tDet.setSpje(yTDet.getSpje().negate());
			tDet.setSpse(yTDet.getSpse().negate());
			tDet.setXscb(yTDet.getXscb().negate());
			tDet.setTXskp(tXskp);
			tDets.add(tDet);

			//更新业务总账
			Sp sp = new Sp();
			BeanUtils.copyProperties(yTDet, sp);
			YwzzServiceImpl.updateYwzzSl(sp, dep, ck, tDet.getZdwsl(), tDet.getSpje(), tDet.getSpse(), tDet.getXscb(), Constant.UPDATE_CK, ywzzDao);

			if(yTXskp.getFhId() != null && yTXskp.getTXsths() == null){
				Fh fh = new Fh();
				fh.setId(yTXskp.getFhId());
				fh.setFhmc(yTXskp.getFhmc());
				
				FhzzServiceImpl.updateFhzzSl(sp, dep, fh, tDet.getZdwsl(), Constant.UPDATE_RK, fhzzDao);
			}
			
			//从销售提货生成销售开票，更新临时总账
			if("1".equals(yTXskp.getFromTh())){
//			if(yTXskp.getTXsths() != null){
				LszzServiceImpl.updateLszzSl(sp, dep, tDet.getZdwsl(), tDet.getSpje().add(tDet.getSpse()), Constant.UPDATE_CK, lszzDao);
				
				//冲减对应销售提货单中的kpsl
				BigDecimal kpsl = yTDet.getZdwsl();
				BigDecimal lastThsl = yTDet.getLastThsl();
			
				int j = 0;
				for(int i = intXsthDetIds.length - 1; i >= 0 ; i--){
					TXsthDet xsthDet = xsthDetDao.load(TXsthDet.class, intXsthDetIds[i]);
					if(yTDet.getSpbh().equals(xsthDet.getSpbh())){
						if(j == 0){
							xsthDet.setKpsl(xsthDet.getKpsl().subtract(lastThsl));
							if(kpsl.compareTo(lastThsl) == 0){
								break;
							}else{
								kpsl = kpsl.subtract(lastThsl);
							}
						}else{
							if(kpsl.compareTo(xsthDet.getKpsl()) == 1){
								xsthDet.setKpsl(Constant.BD_ZERO);
								kpsl = kpsl.subtract(xsthDet.getKpsl());
							}else{
								xsthDet.setKpsl(xsthDet.getKpsl().subtract(kpsl));
								break;
							}
						}
						j++;
					}
				}
			}
			
		}
		
		//授信客户，并且未从销售提货导入
		if("1".equals(tXskp.getIsSx())){
			Kh kh = new Kh();
			kh.setKhbh(tXskp.getKhbh());
			kh.setKhmc(tXskp.getKhmc());
			if(yTXskp.getTXsths() == null){
				YszzServiceImpl.updateYszzJe(dep, kh, tXskp.getHjje().add(tXskp.getHjse()), Constant.UPDATE_YS_KP, yszzDao);
			}else{
				YszzServiceImpl.updateYszzJe(dep, kh, tXskp.getHjje().add(tXskp.getHjse()), Constant.UPDATE_YS_KP_LS, yszzDao);
			}
		}

		if(yTXskp.getTXsths() != null){	
			yTXskp.setTXsths(null);
		}
		
		tXskp.setTXskpDets(tDets);
		xskpDao.save(tXskp);
	}
	
	@Override
	public List<String> toJs(String xskplsh) {
		List<String> lists = new ArrayList<String>();
		String bmbh = null;
		String[] xskplshs = xskplsh.split(",");
		List<XskpDet> dets = new ArrayList<XskpDet>();
		
		for(int i = 0; i < xskplshs.length; i++){
			String hql = "from TXskpDet t where t.TXskp.xskplsh = :xskplsh order by t.spbh";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("xskplsh", xskplshs[i]);
			List<TXskpDet> tDets = detDao.find(hql, params);
			loop:
			for(TXskpDet tDet : tDets){
				for(XskpDet t : dets){
					if(t.getSpbh().equals(tDet.getSpbh())){
						t.setZdwsl(t.getZdwsl().add(tDet.getZdwsl()));
						t.setSpje(t.getSpje().add(tDet.getSpje()));
						t.setSpse(t.getSpse().add(tDet.getSpse()));
						break loop;
					}
				}
				XskpDet det = new XskpDet();
				BeanUtils.copyProperties(tDet, det);
				dets.add(det);
			}
			if(i == xskplshs.length - 1){
				TXskp t = xskpDao.load(TXskp.class, xskplshs[i]);
				bmbh = t.getBmbh();
				String head = "";
				String bz = "";
				bz += xskplsh.trim();
				if(!Constant.XSKP_JSFS_QK.equals(t.getJsfsId())){
					bz += "/" + t.getJsfsmc().trim();
				}
				bz += "/" + t.getYwyId();
				bz += "/" + t.getCkmc().trim();
				if(t.getFhmc() != null && t.getFhmc().trim().length() > 0){
					bz += "/" + t.getFhmc().trim();
				}
				if(t.getBookmc() != null && t.getBookmc().trim().length() > 0){
					bz += "/" + t.getBookmc().trim();
				}
				if(t.getBz() != null && t.getBz().trim().length() > 0){
					bz += "/" + t.getBz().trim();
				}
				head += "\"" + t.getXskplsh() + "\",";
				head += "\"" + dets.size() + "\",";
				head += "\"" + t.getKhmc().trim() + "\",";
				if("1".equals(t.getFplxId())){
					head += "\"" + t.getSh().trim() + "\",";
					head += "\"" + t.getDzdh().trim() + "\",";
					head += "\"" + t.getKhh().trim() + "\",";
				}else{
					head += "\"" + "\",";
					head += "\"" + "\",";
					head += "\"" + "\",";
				}
				head += "\"" + bz + "\",";
				head += "\"" + "\",";
				head += "\"" + "\"";
				lists.add(head);
			}
		}
		
		for(XskpDet det : dets){
			String detail = "";
			detail += "\"";
			if(Constant.XSKP_SPMC.get(bmbh).equals(Constant.XSKP_SPMC_WITHCD)){
				detail += getSpmcWithCd(det.getSpbh(), det.getSpmc(), det.getSpcd());
			}
			if(Constant.XSKP_SPMC.get(bmbh).equals(Constant.XSKP_SPMC_WITHPP)){
				detail += getSpmcWithPp(det.getSpbh(), det.getSpmc(), det.getSppp(), det.getSpbz());
			}
			detail += "\",";
			detail += "\"" + det.getZjldwmc().trim() + "\",";
			detail += "\"" + getSpgg(det.getSpmc()).trim() + "\",";
			detail += "\"" + det.getZdwsl() + "\",";
			detail += "\"" + det.getSpje() + "\",";
			detail += "\"" + Constant.SHUILV + "\",";
			detail += "\"" + Constant.FP_ONE + "\",";
			detail += "\"" + "\",";
			detail += "\"" + det.getSpse() + "\",";
			detail += "\"" + "\",";
			detail += "\"" + "\"";
			lists.add(detail);
		}
		return lists;
	}
	
	private String getSpmcWithCd(String spbh, String spmc, String spcd){
		String r = "";
		r += spbh + "  ";
		if(spmc.indexOf(" ") > 0){
			r += spmc.substring(0, spmc.indexOf(" "));
		}else{
			r += spmc;
		}
		if(spcd != null){	
			r += "(" + spcd + ")";
		}
		return r.trim();
	}
	
	private String getSpmcWithPp(String spbh, String spmc, String sppp, String spbz){
		String r = "";
		r += spbh + " ";
		if(spmc.indexOf(" ") > 0){
			r += spmc.substring(0, spmc.indexOf(" "));
		}else{
			r += spmc;
		}
		if(sppp != null){	
			r += "(" + sppp + ")";
		}
		if(spbz != null){
			r += " " + spbz;
		}
		
		return r.trim();
	}
	
	private String getSpgg(String spmc){
		String spgg = "";
		if(spmc.indexOf("[") > 0){
			return spmc.substring(spmc.indexOf("[") + 1, spmc.indexOf("]"));
		}else{
			if(spmc.indexOf(" ") > 0){
				return spmc.substring(spmc.indexOf(" "));
			}
		}
		return spgg;
	}
	
	@Override
	public void createXsth(Xskp xskp) {
		TXskp tXskp = xskpDao.load(TXskp.class, xskp.getXskplsh());
		
		//生成销售提货表头数据
		TXsth tXsth = new TXsth();
		BeanUtils.copyProperties(tXskp, tXsth);
		tXsth.setCreateTime(new Date());
		tXsth.setCreateName(xskp.getCreateName());
		tXsth.setXsthlsh(LshServiceImpl.updateLsh(xskp.getBmbh(), Constant.XSTH_LX, lshDao));
		tXsth.setToFp("1");
		tXsth.setFromFp("1");
		tXsth.setIsKp("1");
		tXsth.setLocked("0");
		tXsth.setIsCancel("0");
		tXsth.setIsFh("0");
		tXsth.setIsFhth("0");
		tXsth.setIsLs("0");
		tXsth.setHjje(tXskp.getHjje().add(tXskp.getHjse()));
		
		tXsth.setNeedAudit("0");
		if(tXsth.getNeedAudit().equals("1")){
			tXsth.setIsAudit("0");
		}else{
			tXsth.setIsAudit("1");
		}
		
//		Set<TXskp> tXskps = new HashSet<TXskp>();
//		tXskps.add(tXskp);
		
		//处理商品明细
		Set<TXsthDet> tDets = new HashSet<TXsthDet>();
		BigDecimal hjsl = Constant.BD_ZERO;
		Set<TXskpDet> xskpDets = tXskp.getTXskpDets();
		for(TXskpDet xskpDet : xskpDets){
			TXsthDet tDet = new TXsthDet();
			BeanUtils.copyProperties(xskpDet, tDet, new String[]{"id"});

			hjsl = hjsl.add(xskpDet.getCdwsl());
			xskpDet.setLastThsl(tDet.getZdwsl());
			xskpDet.setThsl(tDet.getZdwsl());
			tDet.setCksl(Constant.BD_ZERO);
			tDet.setKpsl(xskpDet.getZdwsl());
			tDet.setZdwdj(xskpDet.getZdwdj().multiply(new BigDecimal(1).add(Constant.SHUILV)));
			tDet.setSpje(xskpDet.getSpje().add(xskpDet.getSpse()));
			
			tDet.setTXsth(tXsth);
//			tDet.setTXskps(tXskps);
			tDets.add(tDet);

			//如授信销售，处理临时总账
//					if("1".equals(xskp.getIsSx()) && xskp.getXsthlshs() == null){
//						LszzServiceImpl.updateLszzSl(sp, dep, tDet.getZdwsl(), tDet.getSpje().add(tDet.getSpse()), Constant.UPDATE_RK, lszzDao);
//					}
			
			//从销售提货生成销售开票，更新临时总账
//			if("1".equals(xskp.getIsSx()) && xsthDetIds != null){
//				LszzServiceImpl.updateLszzSl(sp, dep, tDet.getZdwsl(), tDet.getSpje().add(tDet.getSpse()), Constant.UPDATE_CK, lszzDao);
//			}
			
		}
		tXsth.setHjsl(hjsl);
		tXskp.setTXsths(tDets);
		
		tXsth.setTXsthDets(tDets);

		xsthDao.save(tXsth);
	
		OperalogServiceImpl.addOperalog(xskp.getCreateId(), xskp.getBmbh(), xskp.getMenuId(), tXsth.getXsthlsh(), 
				"生成销售提货单", operalogDao);
	}
	
	@Override
	public DataGrid toXsth(String xskpDetIds){
		String sql = "select spbh, isnull(zdwsl, 0) kpsl, isnull(thsl, 0) thsl from t_xskp_det where zdwsl <> thsl";
		sql += " and id in (" + xskpDetIds + ")";
		
		String hql = "from TXskpDet t where t.zdwsl <> t.thsl and t.id in (" + xskpDetIds + ")";
		List<TXskpDet> ll = detDao.find(hql);
		
		List<XskpDet> nl = new ArrayList<XskpDet>();
		for(TXskpDet tDet : ll){
			XskpDet kd = new XskpDet();
			BeanUtils.copyProperties(tDet, kd, 
					new String[]{"zdwdj", "cdwsl", "cdwdj", "spje", "spse", "xscb"});
			kd.setKpsl(kd.getZdwsl());
			kd.setZdwsl(null);
			
			nl.add(kd);
		}
		
		
//		for(Object[] os : l){
//			String spbh = (String)os[0];
//			BigDecimal kpsl = new BigDecimal(os[1].toString());
//			BigDecimal thsl = new BigDecimal(os[2].toString());
//			
//			TSp sp = spDao.get(TSp.class, spbh);
//			XskpDet xd = new XskpDet();
//			BeanUtils.copyProperties(sp, xd);
//			xd.setZjldwId(sp.getZjldw().getId());
//			xd.setZjldwmc(sp.getZjldw().getJldwmc());
//			xd.setKpsl(kpsl);
//			xd.setThsl(thsl);
//			if(sp.getCjldw() != null){
//				xd.setCjldwId(sp.getCjldw().getId());
//				xd.setCjldwmc(sp.getCjldw().getJldwmc());
//				xd.setZhxs(sp.getZhxs());
////				if(sp.getZhxs().compareTo(Constant.BD_ZERO) != 0){
////					xd.setCdwthsl(zdwthsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
////					xd.setCdwytsl(zdwytsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
////				}else{
////					xd.setCdwthsl(Constant.BD_ZERO);
////					xd.setCdwytsl(Constant.BD_ZERO);
////				}
//			}
//			nl.add(xd);
//		}
		//nl.add(new XsthDet());
		DataGrid dg = new DataGrid();
		dg.setRows(nl);
		return dg;
		
	}
	
	@Override
	public DataGrid datagrid(Xskp xskp) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TXskp t where t.bmbh = :bmbh and t.createTime > :createTime";
		if(xskp.getFromOther() != null){
			hql += " and t.isCj = '0'";
		}
		Map<String, Object> params = new HashMap<String, Object>();
		if(xskp.getOtherBm() != null){
			params.put("bmbh", xskp.getOtherBm());
			hql += " and t.khbh = :khbhs and t.TYwrk is null";
			params.put("khbhs", Constant.XSKP_NB.get(xskp.getBmbh()));
		}else{
			params.put("bmbh", xskp.getBmbh());
		}
		if(xskp.getCreateTime() != null){
			params.put("createTime", xskp.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		
		if(xskp.getSearch() != null){
			hql += " and (t.xskplsh like :search or t.khmc like :search or t.bz like :search or t.ywymc like :search)"; 
			params.put("search", "%" + xskp.getSearch() + "%");
		}
		
		
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TXskp> l = xskpDao.find(hql, params, xskp.getPage(), xskp.getRows());
		List<Xskp> nl = new ArrayList<Xskp>();
		for(TXskp t : l){
			Xskp c = new Xskp();
			BeanUtils.copyProperties(t, c);
			
			//关联的销售提货单
			Set<TXsthDet> tXsths = t.getTXsths(); 
			if(tXsths != null && tXsths.size() > 0){
				String xsthlshs = "";
				int i = 0;
				for(TXsthDet txd : tXsths){
					xsthlshs += txd.getTXsth().getXsthlsh();
					if(i < tXsths.size() - 1){
						xsthlshs += ",";
					}
					i++;
				}
				c.setXsthlshs(xsthlshs);
			}
			
			if(t.getTYwrk() != null){
				c.setYwrklsh(t.getTYwrk().getYwrklsh());
			}
			nl.add(c);
		}
		datagrid.setTotal(xskpDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(String xskplsh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TXskpDet t where t.TXskp.xskplsh = :xskplsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("xskplsh", xskplsh);
		List<TXskpDet> l = detDao.find(hql, params);
		List<XskpDet> nl = new ArrayList<XskpDet>();
		for(TXskpDet t : l){
			XskpDet c = new XskpDet();
			BeanUtils.copyProperties(t, c);
//			TSp tSp = t.getTSp();
//			c.setSpbh(tSp.getSpbh());
//			c.setSpmc(tSp.getTSpdw().getSpdwmc());
//			c.setSpcd(tSp.getSpcd());
//			c.setSppp(tSp.getSppp());
//			c.setSpbz(tSp.getSpbz());
//			c.setZjldwmc(tSp.getZjldw().getJldwmc());
//			if(tSp.getCjldw() != null){
//				c.setCjldwmc(tSp.getCjldw().getJldwmc());
//			}
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid datagridDet(Xskp xskp) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TXskpDet t where t.TXskp.bmbh = :bmbh and t.TXskp.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", xskp.getBmbh());
		if(xskp.getCreateTime() != null){
			params.put("createTime", xskp.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		
		if(xskp.getSearch() != null){
			hql += " and (t.TXskp.xskplsh like :search or t.TXskp.khmc like :search or t.TXskp.bz like :search or t.TXskp.ywymc like :search)"; 
			params.put("search", "%" + xskp.getSearch() + "%");
		}
		
		if(xskp.getFromOther() != null){
			hql += " and t.TXskp.isCj = '0' and t.TXskp.isZs = '0' and t.TXskp.fromTh = '0' and t.zdwsl <> t.thsl";
		}
		
		String countHql = "select count(*) " + hql;
		hql += " order by t.TXskp.createTime desc ";
		List<TXskpDet> l = detDao.find(hql, params, xskp.getPage(), xskp.getRows());
		List<Xskp> nl = new ArrayList<Xskp>();
		for(TXskpDet t : l){
			Xskp c = new Xskp();
			BeanUtils.copyProperties(t, c);
			
			TXskp tXskp = xskpDao.load(TXskp.class, t.getTXskp().getXskplsh());
			BeanUtils.copyProperties(tXskp, c);
			nl.add(c);
		}
		datagrid.setTotal(detDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid getSpkc(Xskp xskp) {
		DataGrid dg = new DataGrid();
		List<ProBean> lists = new ArrayList<ProBean>();
		BigDecimal sl = Constant.BD_ZERO;
		
		List<ProBean> yw = YwzzServiceImpl.getZzsl(xskp.getBmbh(), xskp.getSpbh(), xskp.getCkId(), ywzzDao);
		if(yw != null){
			sl = sl.add(new BigDecimal(yw.get(0).getValue()));
			lists.addAll(yw);
		}
		
		ProBean slBean = new ProBean();
		slBean.setGroup("可销售数量");
		slBean.setName("数量");
		slBean.setValue(sl.toString());
		
		
//		List<ProBean> kf = KfzzServiceImpl.getZzsl(kfck.getBmbh(), kfck.getSpbh(), kfck.getCkId(), null, null, kfzzDao);
//		if(kf != null){
//			lists.addAll(kf);
//		}
//		List<ProBean> pc = KfzzServiceImpl.getZzsl(kfck.getBmbh(), kfck.getSpbh(), kfck.getCkId(), null, "", kfzzDao);
//		if(pc != null){
//			lists.addAll(pc);
//		}
//		if(kfck.getFhId() != null){
//			List<ProBean> fh = FhzzServiceImpl.getZzsl(kfck.getBmbh(), kfck.getSpbh(), kfck.getFhId(), fhzzDao);
//			if(fh != null){
//				lists.addAll(fh);
//			}
//		}
		
		lists.add(0, slBean);
		dg.setRows(lists);
		dg.setTotal((long)lists.size());
		return dg;
	}
	
//	@Override
//	public DataGrid toKfrk(String xskplsh){
//		//单据表头处理
//		TXskp tXskp = xskpDao.load(TXskp.class, xskplsh);
//		Xskp xskp = new Xskp();
//		BeanUtils.copyProperties(tXskp, xskp);
//		xskp.setKhbh(tXskp.getTKh().getKhbh());
//		xskp.setKhmc(tXskp.getTKh().getKhmc());
//		//商品明细处理
//		String sql = "select spbh, sum(zdwsl) zdwsl from t_xskp_det t ";
//		
//		if(xskplsh != null && xskplsh.trim().length() > 0){
//			sql += "where xskplsh = " + xskplsh;
//		}
//		sql += " group by spbh";
//		
//		List<Object[]> l = detDao.findBySQL(sql);
//		List<XskpDet> nl = new ArrayList<XskpDet>();
//		
//		for(Object[] os : l){
//			String spbh = (String)os[0];
//			BigDecimal zdwsl = new BigDecimal(os[1].toString());
//			
//			TSp sp = spDao.get(TSp.class, spbh);
//			XskpDet yd = new XskpDet();
//			yd.setSpbh(spbh);
//			yd.setSpmc(sp.getTSpdw().getSpdwmc());
//			yd.setSpcd(sp.getSpcd());
//			yd.setSppp(sp.getSppp());
//			yd.setSpbz(sp.getSpbz());
//			yd.setZdwsl(zdwsl);
//			yd.setZjldwmc(sp.getZjldw().getJldwmc());
//			if(sp.getCjldw() != null){
//				yd.setCjldwmc(sp.getCjldw().getJldwmc());
//				yd.setZhxs(sp.getZhxs());
//				yd.setCdwsl(zdwsl.divide(sp.getZhxs()));
//			}
//			nl.add(yd);
//		}
//		nl.add(new XskpDet());
//		
//		DataGrid dg = new DataGrid();
//		dg.setObj(xskp);
//		dg.setRows(nl);
//		return dg;
//	}
	
	
	@Override
	public DataGrid toYwrk(Xskp xskp){
		String hql = "from TXskpDet t where t.TXskp.xskplsh = :xskplsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("xskplsh", xskp.getXskplsh());
		List<TXskpDet> tDets = detDao.find(hql, params);
		
		List<XskpDet> nl = new ArrayList<XskpDet>();
		
		for(TXskpDet tDet : tDets){
			XskpDet det = new XskpDet();
			BeanUtils.copyProperties(tDet, det);
			
			nl.add(det);
		}
		nl.add(new XskpDet());
		DataGrid dg = new DataGrid();
		dg.setRows(nl);
		return dg;
		
	}
	
	@Autowired
	public void setXskpDao(BaseDaoI<TXskp> xskpDao) {
		this.xskpDao = xskpDao;
	}

	@Autowired
	public void setYwzzDao(BaseDaoI<TYwzz> ywzzDao) {
		this.ywzzDao = ywzzDao;
	}
	
	@Autowired
	public void setYszzDao(BaseDaoI<TYszz> yszzDao) {
		this.yszzDao = yszzDao;
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
	public void setDetDao(BaseDaoI<TXskpDet> detDao) {
		this.detDao = detDao;
	}

	@Autowired
	public void setXsthDao(BaseDaoI<TXsth> xsthDao) {
		this.xsthDao = xsthDao;
	}

	@Autowired
	public void setXsthDetDao(BaseDaoI<TXsthDet> xsthDetDao) {
		this.xsthDetDao = xsthDetDao;
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
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}

}
