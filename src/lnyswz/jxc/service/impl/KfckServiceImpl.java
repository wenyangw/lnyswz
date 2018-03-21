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

import lnyswz.jxc.util.Export;
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
import lnyswz.jxc.bean.Ck;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.Fh;
import lnyswz.jxc.bean.Hw;
import lnyswz.jxc.bean.Kfck;
import lnyswz.jxc.bean.KfckDet;
import lnyswz.jxc.bean.Kfrk;
import lnyswz.jxc.bean.KfrkDet;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.model.TCgjhDet;
import lnyswz.jxc.model.TCgxqDet;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TFhzz;
import lnyswz.jxc.model.THw;
import lnyswz.jxc.model.TKfck;
import lnyswz.jxc.model.TKfckDet;
import lnyswz.jxc.model.TKfrk;
import lnyswz.jxc.model.TKfrkDet;
import lnyswz.jxc.model.TKfzz;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TXsth;
import lnyswz.jxc.model.TXsthDet;
import lnyswz.jxc.service.KfckServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Util;

/**
 * 库房出库实现类
 * @author 王文阳
 *
 */
@Service("kfckService")
public class KfckServiceImpl implements KfckServiceI {
	private Logger logger = Logger.getLogger(KfckServiceImpl.class);
	private BaseDaoI<TKfck> kfckDao;
	private BaseDaoI<TKfckDet> detDao;
	private BaseDaoI<TKfzz> kfzzDao;
	private BaseDaoI<TFhzz> fhzzDao;
	private BaseDaoI<TXsthDet> xsthDetDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<THw> hwDao;
	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public Kfck save(Kfck kfck) {
		TKfck tKfck = new TKfck();
		BeanUtils.copyProperties(kfck, tKfck);
		tKfck.setCreateTime(new Date());
		String lsh = LshServiceImpl.updateLsh(kfck.getBmbh(), kfck.getLxbh(), lshDao);
		tKfck.setKfcklsh(lsh);
		tKfck.setBmmc(depDao.load(TDepartment.class, kfck.getBmbh()).getDepName());
		tKfck.setIsCj("0");
		
		Department dep = new Department();
		dep.setId(kfck.getBmbh());
		dep.setDepName(tKfck.getBmmc());
		
		Ck ck = new Ck();
		ck.setId(kfck.getCkId());
		ck.setCkmc(kfck.getCkmc());
		
		Fh fh = null;
		if(kfck.getFhId() != null){
			fh = new Fh();
			fh.setId(kfck.getFhId());
			fh.setFhmc(kfck.getFhmc());
		}
		
		//如果从销售提货生成的出库，进行关联
		int[] intDetIds = null;
		Set<TXsthDet> tXsths = null;
		String xsthDetIds = kfck.getXsthDetIds();
		if(xsthDetIds != null && xsthDetIds.trim().length() > 0){
			String[] strDetIds = xsthDetIds.split(",");
			intDetIds = new int[strDetIds.length];
			tXsths = new HashSet<TXsthDet>();
			int i = 0;
			for(String detId : xsthDetIds.split(",")){
				intDetIds[i] = Integer.valueOf(detId);
//				TXsthDet tXsthDet = xsthDetDao.load(TXsthDet.class, Integer.valueOf(detId));
//				tXsths.add(tXsthDet);
				i++;
			}
//			tKfck.setTXsths(tXsths);
			Arrays.sort(intDetIds);
		}
		
		//处理商品明细
		Set<TKfckDet> tDets = new HashSet<TKfckDet>();
		ArrayList<KfckDet> kfckDets = JSON.parseObject(kfck.getDatagrid(), new TypeReference<ArrayList<KfckDet>>(){});
		for(KfckDet kfckDet : kfckDets){
			TKfckDet tDet = new TKfckDet();
			BeanUtils.copyProperties(kfckDet, tDet);
			Sp sp = new Sp();
			BeanUtils.copyProperties(kfckDet, sp);
			
			if("".equals(kfckDet.getCjldwId())){
				tDet.setCdwsl(Constant.BD_ZERO);
				tDet.setZhxs(Constant.BD_ZERO);
			}else{
				if(kfckDet.getZhxs().compareTo(Constant.BD_ZERO) == 0){
					tDet.setCdwsl(Constant.BD_ZERO);
				}
			}
			
			Hw hw = new Hw();
			hw.setId(kfckDet.getHwId());
			hw.setHwmc(hwDao.load(THw.class, kfckDet.getHwId()).getHwmc());
			tDet.setHwmc(hw.getHwmc());
			
			tDet.setTKfck(tKfck);
			//更新库房总账
			KfzzServiceImpl.updateKfzzSl(sp, dep, ck, hw, kfckDet.getSppc(), kfckDet.getZdwsl(), tDet.getCdwsl(), Constant.UPDATE_CK, kfzzDao);
			
			//更新分户
			if("1".equals(kfck.getIsFh())){
				FhzzServiceImpl.updateFhzzSl(sp, dep, fh, kfckDet.getZdwsl(), Constant.UPDATE_CK, fhzzDao);
			}
			
			//更新对应t_xsth_det中的cksl和ccksl
			if(intDetIds != null){
				BigDecimal cksl = kfckDet.getZdwsl();
				BigDecimal ccksl = BigDecimal.ZERO;
				if(kfckDet.getCdwsl() != null){
					ccksl = kfckDet.getCdwsl();
				}
				for(int detId : intDetIds){
					TXsthDet xsthDet = xsthDetDao.load(TXsthDet.class, detId);
					if(kfckDet.getSpbh().equals(xsthDet.getSpbh())){
						BigDecimal wcsl = xsthDet.getZdwsl().subtract(xsthDet.getCksl());
						BigDecimal cwcsl = xsthDet.getCdwsl().subtract(xsthDet.getCcksl());
						tXsths.add(xsthDet);
						if(cksl.compareTo(wcsl) == 1){
							xsthDet.setCksl(xsthDet.getCksl().add(wcsl));
							xsthDet.setCcksl(xsthDet.getCcksl().add(cwcsl));
							cksl = cksl.subtract(wcsl);
							ccksl = ccksl.subtract(cwcsl);
						}else{
							xsthDet.setCksl(xsthDet.getCksl().add(cksl));
							xsthDet.setCcksl(xsthDet.getCcksl().add(ccksl));
							tDet.setLastThsl(cksl);
							tDet.setcLastThsl(ccksl);
							break;
						}
					}
				}
			}
			
			tDets.add(tDet);
		}
		tKfck.setTXsths(tXsths);
		tKfck.setTKfckDets(tDets);
		kfckDao.save(tKfck);		
		
		OperalogServiceImpl.addOperalog(kfck.getCreateId(), kfck.getBmbh(), kfck.getMenuId(), tKfck.getKfcklsh(), "生成库房出库", operalogDao);

		Export.createDoc(lsh);

		Kfck rKfck = new Kfck();
		rKfck.setKfcklsh(lsh);
		return rKfck;
	}
	
	@Override
	public void cjKfck(Kfck kfck) {
		Date now = new Date();
		String lsh = LshServiceImpl.updateLsh(kfck.getBmbh(), kfck.getLxbh(), lshDao);
		//更新原单据信息
		TKfck yTKfck = kfckDao.get(TKfck.class, kfck.getKfcklsh());
		//新增冲减单据信息
		TKfck tKfck = new TKfck();
		BeanUtils.copyProperties(yTKfck, tKfck, new String[]{"TXsths"});
		
		yTKfck.setCjId(kfck.getCjId());
		yTKfck.setCjTime(now);
		yTKfck.setCjName(kfck.getCjName());
		yTKfck.setIsCj("1");
		
		tKfck.setKfcklsh(lsh);
		tKfck.setCjKfcklsh(yTKfck.getKfcklsh());
		tKfck.setCreateTime(now);
		tKfck.setCreateId(kfck.getCjId());
		tKfck.setCreateName(kfck.getCjName());
		tKfck.setIsCj("1");
		tKfck.setCjId(kfck.getCjId());
		tKfck.setCjTime(now);
		tKfck.setCjName(kfck.getCjName());
		
		
		Department dep = new Department();
		dep.setId(kfck.getBmbh());
		dep.setDepName(tKfck.getBmmc());
		
		Ck ck = new Ck();
		ck.setId(tKfck.getCkId());
		ck.setCkmc(tKfck.getCkmc());
		
		Fh fh = null;
		if(tKfck.getFhId() != null){
			fh = new Fh();
			fh.setId(tKfck.getFhId());
			fh.setFhmc(tKfck.getFhmc());
		}
		
		int[] intXsthDetIds = null;
		if(yTKfck.getTXsths() != null){
			intXsthDetIds = new int[yTKfck.getTXsths().size()];
			int i = 0;
			for(TXsthDet t : yTKfck.getTXsths()){
				intXsthDetIds[i] = t.getId();
				i++;
			}
			Arrays.sort(intXsthDetIds);
		}
		
		Set<TKfckDet> yTKfckDets = yTKfck.getTKfckDets();
		Set<TKfckDet> tDets = new HashSet<TKfckDet>();
		for(TKfckDet yTDet : yTKfckDets){
			TKfckDet tDet = new TKfckDet();
			BeanUtils.copyProperties(yTDet, tDet, new String[]{"id"});
			tDet.setZdwsl(yTDet.getZdwsl().negate());
			if(yTDet.getCdwsl() != null){
				tDet.setCdwsl(yTDet.getCdwsl().negate());
			}
			tDet.setTKfck(tKfck);
			tDets.add(tDet);
			
			Sp sp = new Sp();
			BeanUtils.copyProperties(tDet, sp);
			
			Hw hw = new Hw();
			hw.setId(tDet.getHwId());
			hw.setHwmc(tDet.getHwmc());
			
			//更新库房总账
			KfzzServiceImpl.updateKfzzSl(sp, dep, ck, hw, tDet.getSppc(), tDet.getZdwsl(), tDet.getCdwsl(), Constant.UPDATE_CK, kfzzDao);
			
			//更新分户
			if("1".equals(tKfck.getIsFh())){
				FhzzServiceImpl.updateFhzzSl(sp, dep, fh, tDet.getZdwsl(), Constant.UPDATE_CK, fhzzDao);
			}
			
			if(yTKfck.getTXsths() != null){
				
				//冲减对应销售提货单中的cksl
				BigDecimal cksl = yTDet.getZdwsl();
				BigDecimal ccksl = yTDet.getCdwsl();
				BigDecimal lastThsl = yTDet.getLastThsl();
				BigDecimal cLastThsl = yTDet.getcLastThsl();
			
				int j = 0;
				for(int i = intXsthDetIds.length - 1; i >= 0 ; i--){
					TXsthDet xsthDet = xsthDetDao.load(TXsthDet.class, intXsthDetIds[i]);
					if(yTDet.getSpbh().equals(xsthDet.getSpbh())){
						if(j == 0){
							xsthDet.setCksl(xsthDet.getCksl().subtract(lastThsl));
							xsthDet.setCcksl(xsthDet.getCcksl().subtract(cLastThsl));
							if(cksl.compareTo(lastThsl) == 0){
								break;
							}else{
								cksl = cksl.subtract(lastThsl);
								ccksl = ccksl.subtract(cLastThsl);
							}
						}else{
							if(cksl.compareTo(xsthDet.getCksl()) == 1){
								xsthDet.setCksl(Constant.BD_ZERO);
								xsthDet.setCcksl(Constant.BD_ZERO);
								cksl = cksl.subtract(xsthDet.getCksl());
								ccksl = ccksl.subtract(xsthDet.getCcksl());
							}else{
								xsthDet.setCksl(xsthDet.getCksl().subtract(cksl));
								xsthDet.setCcksl(xsthDet.getCcksl().subtract(ccksl));
								break;
							}
						}
					}
					j++;
				}
			}
		}
		
		//原出库单关联销售提货，取消关联
		if(yTKfck.getTXsths() != null){
			yTKfck.setTXsths(null);
		}
				
		tKfck.setTKfckDets(tDets);
		kfckDao.save(tKfck);
		
		OperalogServiceImpl.addOperalog(kfck.getCjId(), kfck.getBmbh(), kfck.getMenuId(), tKfck.getCjKfcklsh() + "/" + tKfck.getKfcklsh(), "冲减库房出库单", operalogDao);
	}
	
	@Override
	public DataGrid datagrid(Kfck kfck) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TKfck t where t.bmbh = :bmbh and t.createTime > :createTime";
		if(kfck.getFromOther() != null){
			hql += " and t.isCj = '0'";
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", kfck.getBmbh());
		if(kfck.getCreateTime() != null){
			params.put("createTime", kfck.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		if(kfck.getSearch() != null){
			//hql += " and (t.kfcklsh like :search or t.khbh like :search or t.khmc like :search or t.bz like :search)"; 
			//params.put("search", "%" + kfck.getSearch() + "%");
			hql += " and (" + 
					Util.getQueryWhere(kfck.getSearch(), new String[]{"t.kfcklsh", "t.khbh", "t.khmc", "t.bz"}, params)
					+ ")";
		}
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TKfck> l = kfckDao.find(hql, params, kfck.getPage(), kfck.getRows());
		List<Kfck> nl = new ArrayList<Kfck>();
		for(TKfck t : l){
			Kfck c = new Kfck();
			BeanUtils.copyProperties(t, c);
			
			Set<TXsthDet> tXsths = t.getTXsths();  
			if(tXsths != null && tXsths.size() > 0){
				String xsthlshs = "";
				int i = 0;
				for(TXsthDet tx : tXsths){
					xsthlshs += tx.getTXsth().getXsthlsh();
					if(i < tXsths.size() - 1){
						xsthlshs += ",";
					}
					i++;
				}
				c.setXsthlshs(xsthlshs);
				
			}
			
			nl.add(c);
			
		}
		datagrid.setTotal(kfckDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(String kfcklsh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TKfckDet t where t.TKfck.kfcklsh = :kfcklsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("kfcklsh", kfcklsh);
		List<TKfckDet> l = detDao.find(hql, params);
		List<KfckDet> nl = new ArrayList<KfckDet>();
		for(TKfckDet t : l){
			KfckDet c = new KfckDet();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}
	
//	@Override
//	public DataGrid toYwrk(String kfcklshs){
//		String sql = "select spbh, sum(zdwsl) zdwsl from t_kfck_det t ";
//		
//		if(kfcklshs != null && kfcklshs.trim().length() > 0){
//			String[] ks = kfcklshs.split(",");
//			sql += "where ";
//			for(int i = 0; i < ks.length; i++){
//				sql += "t.kfcklsh = '" + ks[i] + "'";
//				if(i != ks.length - 1){
//					sql += " or ";
//				}
// 			}
//			
//		}
//		sql += " group by spbh";
//		
//		List<Object[]> l = detDao.findBySQL(sql);
//		List<KfckDet> nl = new ArrayList<KfckDet>();
//		
//		for(Object[] os : l){
//			String spbh = (String)os[0];
//			BigDecimal zdwsl = new BigDecimal(os[1].toString());
//			
//			TSp sp = spDao.get(TSp.class, spbh);
//			KfckDet kd = new KfckDet();
//			kd.setSpbh(spbh);
//			kd.setSpmc(sp.getTSpdw().getSpdwmc());
//			kd.setSpcd(sp.getSpcd());
//			kd.setSppp(sp.getSppp());
//			kd.setSpbz(sp.getSpbz());
//			kd.setZdwsl(zdwsl);
//			kd.setZjldwmc(sp.getZjldw().getJldwmc());
//			if(sp.getCjldw() != null){
//				kd.setCjldwmc(sp.getCjldw().getJldwmc());
//				kd.setZhxs(sp.getZhxs());
//				kd.setCdwsl(zdwsl.divide(sp.getZhxs()));
//			}
//			nl.add(kd);
//		}
//		nl.add(new KfckDet());
//		DataGrid dg = new DataGrid();
//		dg.setRows(nl);
//		return dg;
//	}
	
	@Override
	public DataGrid printKfck(Kfck kfck) {
		DataGrid datagrid = new DataGrid();
		TKfck tKfck = kfckDao.load(TKfck.class, kfck.getKfcklsh());
		
		TXsth tXsth = null;
		if(tKfck.getTXsths().size() > 0){
			tXsth = ((TXsthDet)tKfck.getTXsths().iterator().next()).getTXsth();
		}
		
		List<KfckDet> nl = new ArrayList<KfckDet>();
		BigDecimal hj = Constant.BD_ZERO;
		for (TKfckDet yd : tKfck.getTKfckDets()) {
			KfckDet kfckDet = new KfckDet();
			BeanUtils.copyProperties(yd, kfckDet);
			nl.add(kfckDet);
			hj = hj.add(yd.getCdwsl());
		}
		int num = nl.size();
		if (num < Constant.REPORT_NUMBER) {
			for (int i = 0; i < (Constant.REPORT_NUMBER - num); i++) {
				nl.add(new KfckDet());
			}
		}
		String bz = "";
		if(tXsth != null){
		bz = tXsth.getXsthlsh() + "/" + tKfck.getBz() + "/";
		if(tXsth.getYwymc() != null){
			bz += tXsth.getYwymc().trim();
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
		}
		
		//Kfrk kfrk = new Kfrk();
		//BeanUtils.copyProperties(yk, kfrk);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "库   房   出   库   单");
		map.put("kfcklsh", kfck.getKfcklsh());
		map.put("bmmc", tKfck.getBmmc());
		map.put("printName", kfck.getCreateName());
		map.put("createTime", DateUtil.dateToString(tKfck.getCreateTime(), DateUtil.DATETIME_NOSECOND_PATTERN));
		map.put("printTime", DateUtil.dateToString(new Date()));
		map.put("khbh", tKfck.getKhbh());
		map.put("khmc", tKfck.getKhmc());
		map.put("fhmc", tKfck.getFhmc() != null ? tKfck.getFhmc() : "");
		map.put("ckmc", tKfck.getCkmc());
		map.put("hjsl", hj);
		map.put("bz", bz);
		String codePath = Util.getRootPath() + Constant.CODE_PATH + kfck.getKfcklsh() + ".png";
		//map.put("codeFile", codePath.replace("/","\\"));
		map.put("codeFile", codePath);
		
		datagrid.setObj(map);
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid getSpkc(Kfck kfck) {
		DataGrid dg = new DataGrid();
		List<ProBean> lists = new ArrayList<ProBean>();
		
		List<ProBean> kf = KfzzServiceImpl.getZzsl(kfck.getBmbh(), kfck.getSpbh(), kfck.getCkId(), null, null, kfzzDao);
		if(kf != null){
			lists.addAll(kf);
		}
		List<ProBean> pc = KfzzServiceImpl.getZzsl(kfck.getBmbh(), kfck.getSpbh(), kfck.getCkId(), null, "", kfzzDao);
		if(pc != null){
			lists.addAll(pc);
		}
		if(kfck.getFhId() != null){
			List<ProBean> fh = FhzzServiceImpl.getZzsl(kfck.getBmbh(), kfck.getSpbh(), kfck.getFhId(), fhzzDao);
			if(fh != null){
				lists.addAll(fh);
			}
		}
		
		dg.setRows(lists);
		dg.setTotal((long)lists.size());
		return dg;
	}
	
	
	@Autowired
	public void setKfckDao(BaseDaoI<TKfck> kfckDao) {
		this.kfckDao = kfckDao;
	}

	@Autowired
	public void setKfzzDao(BaseDaoI<TKfzz> kfzzDao) {
		this.kfzzDao = kfzzDao;
	}

	@Autowired
	public void setFhzzDao(BaseDaoI<TFhzz> fhzzDao) {
		this.fhzzDao = fhzzDao;
	}
	
	@Autowired
	public void setXsthDetDao(BaseDaoI<TXsthDet> xsthDetDao) {
		this.xsthDetDao = xsthDetDao;
	}

	@Autowired
	public void setDetDao(BaseDaoI<TKfckDet> detDao) {
		this.detDao = detDao;
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
	public void setHwDao(BaseDaoI<THw> hwDao) {
		this.hwDao = hwDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}

}
