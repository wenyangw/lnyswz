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

import javax.persistence.Column;
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
import lnyswz.jxc.bean.Fh;
import lnyswz.jxc.bean.Hw;
import lnyswz.jxc.bean.KfrkDet;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.bean.Kfhs;
import lnyswz.jxc.bean.KfhsDet;
import lnyswz.jxc.model.TCgjh;
import lnyswz.jxc.model.TCgjhDet;
import lnyswz.jxc.model.TCk;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TFhzz;
import lnyswz.jxc.model.TKfrk;
import lnyswz.jxc.model.THw;
import lnyswz.jxc.model.TKfzz;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TRklx;
import lnyswz.jxc.model.TSpDet;
import lnyswz.jxc.model.TKfhs;
import lnyswz.jxc.model.TKfhsDet;
import lnyswz.jxc.model.TCgxq;
import lnyswz.jxc.model.TGys;
import lnyswz.jxc.model.TJsfs;
import lnyswz.jxc.model.TYwhs;
import lnyswz.jxc.model.TYwhsDet;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.model.TRole;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.model.TUser;
import lnyswz.jxc.service.KfhsServiceI;
import lnyswz.jxc.util.Constant;

/**
 * 采购需求实现类
 * @author 王文阳
 *
 */
@Service("kfhsService")
public class KfhsServiceImpl implements KfhsServiceI {
	private Logger logger = Logger.getLogger(KfhsServiceImpl.class);
	private BaseDaoI<TKfhs> kfhsDao;
	private BaseDaoI<TKfhsDet> detDao;
	private BaseDaoI<TYwhs> ywhsDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TSp> spDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TKfzz> kfzzDao;
	private BaseDaoI<TFhzz> fhzzDao;
	private BaseDaoI<THw> hwDao;
	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public void save(Kfhs kfhs) {
		TKfhs tKfhs = new TKfhs();

		BeanUtils.copyProperties(kfhs, tKfhs);
		
		tKfhs.setCreateTime(new Date());
		tKfhs.setKfhslsh(LshServiceImpl.updateLsh(kfhs.getBmbh(), kfhs.getLxbh(), lshDao));
		tKfhs.setBmmc(depDao.load(TDepartment.class, kfhs.getBmbh()).getDepName());
		tKfhs.setIsCj("0");

		TYwhs tYwhs = ywhsDao.load(TYwhs.class, kfhs.getYwhslsh());
		tYwhs.setTKfhs(tKfhs);
		
		Department dep = new Department();
		dep.setId(kfhs.getBmbh());
		dep.setDepName(tKfhs.getBmmc());
		
		Ck ck = new Ck();
		ck.setId(kfhs.getCkId());
		ck.setCkmc(kfhs.getCkmc());
		
//		Fh fh = null;
//		if(kfhs.getFhId() != null){
//			fh = new Fh();
//			fh.setId(kfhs.getFhId());
//			fh.setFhmc(kfhs.getFhmc());
//		}
		
		//处理商品明细
		Set<TKfhsDet> tDets = new HashSet<TKfhsDet>();
		
		ArrayList<KfhsDet> kfhsDets = JSON.parseObject(kfhs.getDatagrid(), new TypeReference<ArrayList<KfhsDet>>(){});
		for(KfhsDet kfhsDet : kfhsDets){
			TKfhsDet tDet = new TKfhsDet();
			
			BeanUtils.copyProperties(kfhsDet, tDet);
			tDet.setTKfhs(tKfhs);
			
			tDet.setHwmc(hwDao.load(THw.class, kfhsDet.getHwId()).getHwmc());

			TSp tSp = spDao.load(TSp.class, kfhsDet.getSpbh());
			if(null != tSp.getCjldw()){
				tDet.setCjldwId(tSp.getCjldw().getId());
				tDet.setCjldwmc(tSp.getCjldw().getJldwmc());
				tDet.setZhxs(tSp.getZhxs());
			}
			if(null == tDet.getZhxs() || tDet.getZhxs().compareTo(Constant.BD_ZERO) == 0){
				tDet.setCdwsl(Constant.BD_ZERO);
				tDet.setZhxs(Constant.BD_ZERO);
			}
//			else{
//				tDet.setCdwsl(tDet.getZdwsl().divide(tDet.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
//			}
			
			Sp sp = new Sp();
			BeanUtils.copyProperties(tDet, sp);

			//分户调号时不更新库房总账
//			if(null != kfhs.getFhId()){
//				//更新分户总账
//				if("1".equals(kfhsDet.getIsZj())){
//					FhzzServiceImpl.updateFhzzSl(sp, dep, fh, kfhsDet.getZdwsl(), Constant.UPDATE_RK, fhzzDao);
//				}else{
//					FhzzServiceImpl.updateFhzzSl(sp, dep, fh, kfhsDet.getZdwsl().negate(), Constant.UPDATE_RK, fhzzDao);
//				}
//				
//			}else{
				Hw hw = new Hw();
				hw.setId(kfhsDet.getHwId());
				hw.setHwmc(tDet.getHwmc());

				//更新库存总账
				if("1".equals(kfhsDet.getIsZj())){
					KfzzServiceImpl.updateKfzzSl(sp, dep, ck, hw, kfhsDet.getSppc(), kfhsDet.getZdwsl(), tDet.getCdwsl(), Constant.UPDATE_RK, kfzzDao);
				}else{
					KfzzServiceImpl.updateKfzzSl(sp, dep, ck, hw, kfhsDet.getSppc(), tDet.getZdwsl().negate(), tDet.getCdwsl().negate(), Constant.UPDATE_RK, kfzzDao);
				}
//			}
			
			tDet.setTKfhs(tKfhs);
			tDets.add(tDet);
		}
			
		tKfhs.setTKfhsDets(tDets);
		kfhsDao.save(tKfhs);		
		
		OperalogServiceImpl.addOperalog(kfhs.getCreateId(), kfhs.getBmbh(), kfhs.getMenuId(), tKfhs.getKfhslsh(), 
				"生成业务入库单", operalogDao);
	}
	
	@Override
	public void cjKfhs(Kfhs kfhs) {
		Date now = new Date();

		//获取原单据信息
		TKfhs yTKfhs = kfhsDao.get(TKfhs.class, kfhs.getKfhslsh());
		
		//新增冲减单据信息
		TKfhs tKfhs = new TKfhs();
		BeanUtils.copyProperties(yTKfhs, tKfhs, new String[]{"TYwhs"});
		
		//更新原单据冲减信息
		yTKfhs.setCjId(kfhs.getCjId());
		yTKfhs.setCjTime(now);
		yTKfhs.setCjName(kfhs.getCjName());
		yTKfhs.setIsCj("1");
		
		TYwhs tYwhs = ywhsDao.load(TYwhs.class, yTKfhs.getTYwhs().getYwhslsh());
		tYwhs.setTKfhs(null);
		
		//更新新单据信息
		String lsh = LshServiceImpl.updateLsh(kfhs.getBmbh(), kfhs.getLxbh(), lshDao);
		tKfhs.setKfhslsh(lsh);
		tKfhs.setCjKfhslsh(yTKfhs.getKfhslsh());
		tKfhs.setCreateTime(now);
		tKfhs.setCreateId(kfhs.getCjId());
		tKfhs.setCreateName(kfhs.getCjName());
		tKfhs.setIsCj("1");
		tKfhs.setCjTime(now);
		tKfhs.setCjId(kfhs.getCjId());
		tKfhs.setCjName(kfhs.getCjName());
		tKfhs.setBz(kfhs.getBz());
		
		Department dep = new Department();
		dep.setId(kfhs.getBmbh());
		dep.setDepName(depDao.load(TDepartment.class, kfhs.getBmbh()).getDepName());
		
		Ck ck = new Ck();
		ck.setId(tKfhs.getCkId());
		ck.setCkmc(tKfhs.getCkmc());
		
//		Fh fh = null;
//		if(yTKfhs.getFhId() != null){
//			fh = new Fh();
//			fh.setId(yTKfhs.getFhId());
//			fh.setFhmc(yTKfhs.getFhmc());
//		}
		
		Set<TKfhsDet> yTKfhsDets = yTKfhs.getTKfhsDets();
		Set<TKfhsDet> tDets = new HashSet<TKfhsDet>();
		for(TKfhsDet yTDet : yTKfhsDets){
			TKfhsDet tDet = new TKfhsDet();
			BeanUtils.copyProperties(yTDet, tDet, new String[]{"id"});
			tDet.setZdwsl(yTDet.getZdwsl().negate());
			tDet.setCdwsl(yTDet.getCdwsl().negate());
			
			Sp sp = new Sp();
			BeanUtils.copyProperties(yTDet, sp);
			
//			if(null != yTKfhs.getFhId()){
//				//更新分户总账
//				if("1".equals(yTDet.getIsZj())){
//					FhzzServiceImpl.updateFhzzSl(sp, dep, fh, tDet.getZdwsl(), Constant.UPDATE_RK, fhzzDao);
//				}else{
//					FhzzServiceImpl.updateFhzzSl(sp, dep, fh, tDet.getZdwsl().negate(), Constant.UPDATE_RK, fhzzDao);
//				}
//			}else{
				Hw hw = new Hw();
				hw.setId(yTDet.getHwId());
				hw.setHwmc(yTDet.getHwmc());
				
				//更新库房总账
				if("1".equals(yTDet.getIsZj())){
					KfzzServiceImpl.updateKfzzSl(sp, dep, ck, hw, tDet.getSppc(), tDet.getZdwsl(), tDet.getCdwsl(), Constant.UPDATE_RK, kfzzDao);
				}else{
					KfzzServiceImpl.updateKfzzSl(sp, dep, ck, hw, tDet.getSppc(), tDet.getZdwsl().negate(), tDet.getCdwsl().negate(), Constant.UPDATE_RK, kfzzDao);
				}
//			}
			
			tDet.setTKfhs(tKfhs);
			
			tDets.add(tDet);
			
		}
		tKfhs.setTKfhsDets(tDets);
		kfhsDao.save(tKfhs);
		
		OperalogServiceImpl.addOperalog(kfhs.getCjId(), kfhs.getBmbh(), kfhs.getMenuId(), tKfhs.getCjKfhslsh() + "/" + tKfhs.getKfhslsh(), 
				"冲减业务调号单", operalogDao);
	}
	
	@Override
	public DataGrid datagrid(Kfhs kfhs) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TKfhs t where t.bmbh = :bmbh and t.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", kfhs.getBmbh());
		if(kfhs.getCreateTime() != null){
			params.put("createTime", kfhs.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
//		if(kfhs.getFromOther() != null){
//			hql += " and t.isCj = '0'";
//		}
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TKfhs> l = kfhsDao.find(hql, params, kfhs.getPage(), kfhs.getRows());
		List<Kfhs> nl = new ArrayList<Kfhs>();
		for(TKfhs t : l){
			Kfhs c = new Kfhs();
			BeanUtils.copyProperties(t, c);
			
			if(t.getTYwhs() != null){
				c.setYwhslsh(t.getTYwhs().getYwhslsh());
			}
			nl.add(c);
		}
		datagrid.setTotal(kfhsDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(String kfhslsh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TKfhsDet t where t.TKfhs.kfhslsh = :kfhslsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("kfhslsh", kfhslsh);
		
		List<TKfhsDet> l = detDao.find(hql, params);
		List<KfhsDet> nl = new ArrayList<KfhsDet>();
		for(TKfhsDet t : l){
			KfhsDet c = new KfhsDet();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}

		
	@Autowired
	public void setKfhsDao(BaseDaoI<TKfhs> kfhsDao) {
		this.kfhsDao = kfhsDao;
	}

	@Autowired
	public void setDetDao(BaseDaoI<TKfhsDet> detDao) {
		this.detDao = detDao;
	}

	@Autowired
	public void setYwhsDao(BaseDaoI<TYwhs> ywhsDao) {
		this.ywhsDao = ywhsDao;
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
	public void setHwDao(BaseDaoI<THw> hwDao) {
		this.hwDao = hwDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
}
