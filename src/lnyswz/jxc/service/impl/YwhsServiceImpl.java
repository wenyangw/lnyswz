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
import lnyswz.jxc.bean.Fh;
import lnyswz.jxc.bean.KfrkDet;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.bean.Ywhs;
import lnyswz.jxc.bean.YwhsDet;
import lnyswz.jxc.bean.Ywrk;
import lnyswz.jxc.bean.YwrkDet;
import lnyswz.jxc.model.TCgjh;
import lnyswz.jxc.model.TCgjhDet;
import lnyswz.jxc.model.TCk;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TFhzz;
import lnyswz.jxc.model.TKfrk;
import lnyswz.jxc.model.THw;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TRklx;
import lnyswz.jxc.model.TSpDet;
import lnyswz.jxc.model.TYwhs;
import lnyswz.jxc.model.TYwhsDet;
import lnyswz.jxc.model.TCgxq;
import lnyswz.jxc.model.TGys;
import lnyswz.jxc.model.TJsfs;
import lnyswz.jxc.model.TYwrk;
import lnyswz.jxc.model.TYwrkDet;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.model.TRole;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.model.TUser;
import lnyswz.jxc.service.YwhsServiceI;
import lnyswz.jxc.util.Constant;

/**
 * 采购需求实现类
 * @author 王文阳
 *
 */
@Service("ywhsService")
public class YwhsServiceImpl implements YwhsServiceI {
	private Logger logger = Logger.getLogger(YwhsServiceImpl.class);
	private BaseDaoI<TYwhs> ywhsDao;
	private BaseDaoI<TYwhsDet> detDao;
	private BaseDaoI<TYwzz> ywzzDao;
	private BaseDaoI<TFhzz> fhzzDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TSpDet> spDetDao;
	private BaseDaoI<TDepartment> depDao;

	private BaseDaoI<TSp> spDao;
	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public Ywhs save(Ywhs ywhs) {
		String lsh = LshServiceImpl.updateLsh(ywhs.getBmbh(), ywhs.getLxbh(), lshDao);
		TYwhs tYwhs = new TYwhs();
		BeanUtils.copyProperties(ywhs, tYwhs);
		tYwhs.setCreateTime(new Date());
		tYwhs.setYwhslsh(lsh);
		tYwhs.setBmmc(depDao.load(TDepartment.class, ywhs.getBmbh()).getDepName());

		tYwhs.setIsCj("0");
		
		Department dep = new Department();
		dep.setId(ywhs.getBmbh());
		dep.setDepName(tYwhs.getBmmc());
		
		Ck ck = new Ck();
		ck.setId(ywhs.getCkId());
		ck.setCkmc(ywhs.getCkmc());
		
		Fh fh = null;
		if(ywhs.getFhId() != null){
			fh = new Fh();
			fh.setId(ywhs.getFhId());
			fh.setFhmc(ywhs.getFhmc());
		}
		
		//处理商品明细
		Set<TYwhsDet> tDets = new HashSet<TYwhsDet>();
		ArrayList<YwhsDet> ywhsDets = JSON.parseObject(ywhs.getDatagrid(), new TypeReference<ArrayList<YwhsDet>>(){});
		for(int i = 0; i < ywhsDets.size(); i++){
			TYwhsDet tDet = new TYwhsDet();
			
			YwhsDet zywhsDet = ywhsDets.get(i);
			
			tDet.setZspbh(zywhsDet.getSpbh());
			tDet.setZspmc(zywhsDet.getSpmc());
			tDet.setZspcd(zywhsDet.getSpcd());
			tDet.setZsppp(zywhsDet.getSppp());
			tDet.setZspbz(zywhsDet.getSpbz());
			tDet.setZzjldwId(zywhsDet.getZjldwId());
			tDet.setZzjldwmc(zywhsDet.getZjldwmc());
			tDet.setZcjldwId(zywhsDet.getCjldwId());
			tDet.setZcjldwmc(zywhsDet.getCjldwmc());
			tDet.setZzhxs(zywhsDet.getZhxs());
			tDet.setZzdwsl(zywhsDet.getZdwsl());
			tDet.setZcdwsl(zywhsDet.getCdwsl());
			tDet.setZzdwdj(zywhsDet.getZdwdj());
			tDet.setZspje(zywhsDet.getSpje());
			tDet.setZisZs(zywhsDet.getIsZs());
			
			tDet.setZqdwcb(YwzzServiceImpl.getDwcb(ywhs.getBmbh(), zywhsDet.getSpbh(), ywzzDao));
			tDet.setTYwhs(tYwhs);

			Sp zsp = new Sp();
			BeanUtils.copyProperties(zywhsDet, zsp);
			TSp zTSp = spDao.load(TSp.class, zywhsDet.getSpbh());
			if(zTSp.getCjldw() != null){
				zsp.setCjldwId(zTSp.getCjldw().getId());
				zsp.setCjldwmc(zTSp.getCjldw().getJldwmc());
				zsp.setZhxs(zTSp.getZhxs());
			}
			

			//更新业务总账
			YwzzServiceImpl.updateYwzzSl(zsp, dep, ck, zywhsDet.getZdwsl(), tDet.getZcdwsl(), zywhsDet.getSpje(), null, null, Constant.UPDATE_RK, ywzzDao);
			tDet.setZdwcb(YwzzServiceImpl.getDwcb(ywhs.getBmbh(), zywhsDet.getSpbh(), ywzzDao));
			
			//更新分户账
			if(ywhs.getFhId() != null){
				FhzzServiceImpl.updateFhzzSl(zsp, dep, fh, zywhsDet.getZdwsl().negate(), Constant.UPDATE_RK, fhzzDao);
			}
			
			i++;
			
			YwhsDet jywhsDet = ywhsDets.get(i);
			
			tDet.setJspbh(jywhsDet.getSpbh());
			tDet.setJspmc(jywhsDet.getSpmc());
			tDet.setJspcd(jywhsDet.getSpcd());
			tDet.setJsppp(jywhsDet.getSppp());
			tDet.setJspbz(jywhsDet.getSpbz());
			tDet.setJzjldwId(jywhsDet.getZjldwId());
			tDet.setJzjldwmc(jywhsDet.getZjldwmc());
			tDet.setJcjldwId(jywhsDet.getCjldwId());
			tDet.setJcjldwmc(jywhsDet.getCjldwmc());
			tDet.setJzhxs(jywhsDet.getZhxs());
			tDet.setJzdwsl(jywhsDet.getZdwsl());
			tDet.setJcdwsl(jywhsDet.getCdwsl());
			tDet.setJzdwdj(jywhsDet.getZdwdj());
			tDet.setJspje(jywhsDet.getSpje());
			tDet.setJisZs(jywhsDet.getIsZs());
			
			tDet.setJqdwcb(YwzzServiceImpl.getDwcb(ywhs.getBmbh(), jywhsDet.getSpbh(), ywzzDao));

			Sp jsp = new Sp();
			BeanUtils.copyProperties(jywhsDet, jsp);
			TSp jTSp = spDao.load(TSp.class, jywhsDet.getSpbh());
			if(jTSp.getCjldw() != null){
				jsp.setCjldwId(zTSp.getCjldw().getId());
				jsp.setCjldwmc(zTSp.getCjldw().getJldwmc());
				jsp.setZhxs(jTSp.getZhxs());
			}
			
			//更新业务总账
			YwzzServiceImpl.updateYwzzSl(jsp, dep, ck, jywhsDet.getZdwsl().negate(), tDet.getJcdwsl().negate(), jywhsDet.getSpje().negate(), null, null, Constant.UPDATE_RK, ywzzDao);
			tDet.setJdwcb(YwzzServiceImpl.getDwcb(ywhs.getBmbh(), jywhsDet.getSpbh(), ywzzDao));
			
			//更新分户账
			if(ywhs.getFhId() != null){
				FhzzServiceImpl.updateFhzzSl(jsp, dep, fh, jywhsDet.getZdwsl(), Constant.UPDATE_RK, fhzzDao);
			}
			
			tDet.setTYwhs(tYwhs);
			tDets.add(tDet);
		}

		tYwhs.setTYwhsDets(tDets);
		ywhsDao.save(tYwhs);		
		
		OperalogServiceImpl.addOperalog(ywhs.getCreateId(), ywhs.getBmbh(), ywhs.getMenuId(), tYwhs.getYwhslsh(), 
				"生成业务入库单", operalogDao);
		
		Ywhs rYwhs = new Ywhs();
		rYwhs.setYwhslsh(lsh);
		return rYwhs;
	}
	
	@Override
	public Ywhs cjYwhs(Ywhs ywhs) {
		Date now = new Date();
		//更新新单据
		String lsh = LshServiceImpl.updateLsh(ywhs.getBmbh(), ywhs.getLxbh(), lshDao);
		//更新原单据信息
		TYwhs yTYwhs = ywhsDao.get(TYwhs.class, ywhs.getYwhslsh());
		//新增冲减单据信息
		TYwhs tYwhs = new TYwhs();
		BeanUtils.copyProperties(yTYwhs, tYwhs);
		
		yTYwhs.setCjId(ywhs.getCjId());
		yTYwhs.setCjTime(now);
		yTYwhs.setCjName(ywhs.getCjName());
		yTYwhs.setIsCj("1");


		tYwhs.setYwhslsh(lsh);
		tYwhs.setCjYwhslsh(yTYwhs.getYwhslsh());
		tYwhs.setCreateTime(now);
		tYwhs.setCreateId(ywhs.getCjId());
		tYwhs.setCreateName(ywhs.getCjName());
		tYwhs.setIsCj("1");
		tYwhs.setCjTime(now);
		tYwhs.setCjName(ywhs.getCjName());
		tYwhs.setHjje(tYwhs.getHjje().negate());
		tYwhs.setBz(ywhs.getBz());
		
		Department dep = new Department();
		dep.setId(ywhs.getBmbh());
		dep.setDepName(depDao.load(TDepartment.class, ywhs.getBmbh()).getDepName());
		
		Ck ck = new Ck();
		ck.setId(tYwhs.getCkId());
		ck.setCkmc(tYwhs.getCkmc());
		
		Fh fh = null;
		if(yTYwhs.getFhId() != null){
			fh = new Fh();
			fh.setId(yTYwhs.getFhId());
			fh.setFhmc(yTYwhs.getFhmc());
		}
		
		Set<TYwhsDet> yTYwhsDets = yTYwhs.getTYwhsDets();
		Set<TYwhsDet> tDets = new HashSet<TYwhsDet>();
		for(TYwhsDet yTDet : yTYwhsDets){
			TYwhsDet tDet = new TYwhsDet();
			BeanUtils.copyProperties(yTDet, tDet, new String[]{"id"});
			tDet.setZzdwsl(yTDet.getZzdwsl().negate());
			tDet.setZcdwsl(yTDet.getZcdwsl().negate());
			tDet.setJzdwsl(yTDet.getJzdwsl().negate());
			tDet.setJcdwsl(yTDet.getJcdwsl().negate());
			tDet.setZspje(yTDet.getZspje().negate());
			tDet.setJspje(yTDet.getJspje().negate());
			
			//更新冲减前dwcb
			tDet.setZqdwcb(YwzzServiceImpl.getDwcb(tYwhs.getBmbh(), tDet.getZspbh(), ywzzDao));
			tDet.setJqdwcb(YwzzServiceImpl.getDwcb(tYwhs.getBmbh(), tDet.getJspbh(), ywzzDao));
			
			Sp zsp = new Sp();
			zsp.setSpbh(yTDet.getZspbh());
			zsp.setSpmc(yTDet.getZspmc());
			zsp.setSpcd(yTDet.getZspcd());
			zsp.setSppp(yTDet.getZsppp());
			zsp.setSpbz(yTDet.getZspbz());
			zsp.setZjldwId(yTDet.getZzjldwId());
			zsp.setZjldwmc(yTDet.getZzjldwmc());
			TSp zTSp = spDao.load(TSp.class, yTDet.getZspbh());
			if(zTSp.getCjldw() != null){
				zsp.setCjldwId(zTSp.getCjldw().getId());
				zsp.setCjldwmc(zTSp.getCjldw().getJldwmc());
			}
			//更新业务总账
			YwzzServiceImpl.updateYwzzSl(zsp, dep, ck, tDet.getZzdwsl(), tDet.getZcdwsl(), tDet.getZspje(), null, null, Constant.UPDATE_RK, ywzzDao);
			
			//更新分户账
			if(yTYwhs.getFhId() != null){
				FhzzServiceImpl.updateFhzzSl(zsp, dep, fh, tDet.getZzdwsl().negate(), Constant.UPDATE_RK, fhzzDao);
			}
			
			Sp jsp = new Sp();
			jsp.setSpbh(yTDet.getJspbh());
			jsp.setSpmc(yTDet.getJspmc());
			jsp.setSpcd(yTDet.getJspcd());
			jsp.setSppp(yTDet.getJsppp());
			jsp.setSpbz(yTDet.getJspbz());
			jsp.setZjldwId(yTDet.getJzjldwId());
			jsp.setZjldwmc(yTDet.getJzjldwmc());
			TSp jTSp = spDao.load(TSp.class, yTDet.getJspbh());
			if(jTSp.getCjldw() != null){
				jsp.setCjldwId(jTSp.getCjldw().getId());
				jsp.setCjldwmc(jTSp.getCjldw().getJldwmc());
			}
			YwzzServiceImpl.updateYwzzSl(jsp, dep, ck, tDet.getJzdwsl().negate(), tDet.getJcdwsl().negate(), tDet.getJspje().negate(), null, null, Constant.UPDATE_RK, ywzzDao);
			
			//更新分户账
			if(yTYwhs.getFhId() != null){
				FhzzServiceImpl.updateFhzzSl(jsp, dep, fh, tDet.getJzdwsl(), Constant.UPDATE_RK, fhzzDao);
			}
			//更新冲减后dwcb
			tDet.setZdwcb(YwzzServiceImpl.getDwcb(tYwhs.getBmbh(), tDet.getZspbh(), ywzzDao));
			tDet.setJdwcb(YwzzServiceImpl.getDwcb(tYwhs.getBmbh(), tDet.getJspbh(), ywzzDao));
			
			tDet.setTYwhs(tYwhs);
			
			tDets.add(tDet);
			
		}
		tYwhs.setTYwhsDets(tDets);
		ywhsDao.save(tYwhs);
		
		OperalogServiceImpl.addOperalog(ywhs.getCjId(), ywhs.getBmbh(), ywhs.getMenuId(), tYwhs.getCjYwhslsh() + "/" + tYwhs.getYwhslsh(), 
				"冲减业务调号单", operalogDao);
		
		Ywhs rYwhs = new Ywhs();
		rYwhs.setYwhslsh(lsh);
		return rYwhs;
	}
	
	@Override
	public DataGrid printYwhs(Ywhs ywhs) {
		DataGrid datagrid = new DataGrid();
		TYwhs tYwhs = ywhsDao.load(TYwhs.class, ywhs.getYwhslsh());
		List<YwhsDet> nl = new ArrayList<YwhsDet>();
		BigDecimal zhj = Constant.BD_ZERO;
		BigDecimal jhj = Constant.BD_ZERO;
		for (TYwhsDet yd : tYwhs.getTYwhsDets()) {
			YwhsDet ywhsDet = new YwhsDet();
			BeanUtils.copyProperties(yd, ywhsDet);
			zhj = zhj.add(yd.getZspje());
			jhj = jhj.add(yd.getJspje());
			nl.add(ywhsDet);
		}
		int num = nl.size() * 2;
		if (num < Constant.REPORT_NUMBER) {
			for (int i = 0; i < (Constant.REPORT_NUMBER - num); i += 2) {
				nl.add(new YwhsDet());
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		String lsh = "";
		if (tYwhs.getCjYwhslsh() != null) {
			lsh += tYwhs.getCjYwhslsh() + " - ";
		}
		lsh += tYwhs.getYwhslsh();
		map.put("title", "商   品   调   号   单");
		map.put("bmmc", tYwhs.getBmmc());
		map.put("createTime", DateUtil.dateToString(tYwhs.getCreateTime(), DateUtil.DATETIME_NOSECOND_PATTERN));
		map.put("ywhslsh", lsh);
		map.put("zhj", zhj);
		map.put("jhj", jhj);
		map.put("hjje", tYwhs.getHjje());
		map.put("bz", tYwhs.getBz());
		map.put("fhmc", tYwhs.getFhmc());
		map.put("ckmc", tYwhs.getCkmc());
		map.put("printName", ywhs.getCreateName());
		map.put("printTime", DateUtil.dateToString(new Date()));
		datagrid.setObj(map);
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid datagrid(Ywhs ywhs) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TYwhs t where t.bmbh = :bmbh and t.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", ywhs.getBmbh());
		if(ywhs.getCreateTime() != null){
			params.put("createTime", ywhs.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		if(ywhs.getFromOther() != null){
			hql += " and t.isCj = '0' and t.TKfhs = null and t.fhId = null";
		}
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TYwhs> l = ywhsDao.find(hql, params, ywhs.getPage(), ywhs.getRows());
		List<Ywhs> nl = new ArrayList<Ywhs>();
		for(TYwhs t : l){
			Ywhs c = new Ywhs();
			BeanUtils.copyProperties(t, c);
			if(t.getTKfhs() != null){
				c.setKfhslsh(t.getTKfhs().getKfhslsh());
			}
			nl.add(c);
		}
		datagrid.setTotal(ywhsDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(Ywhs ywhs) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TYwhsDet t where t.TYwhs.ywhslsh = :ywhslsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ywhslsh", ywhs.getYwhslsh());
		List<TYwhsDet> l = detDao.find(hql, params);
		List<YwhsDet> nl = new ArrayList<YwhsDet>();
		for(TYwhsDet t : l){
			if(ywhs.getFromOther() == null || "0".equals(t.getZisZs())){
				YwhsDet zc = new YwhsDet();
				zc.setIsZj("1");
				zc.setSpbh(t.getZspbh());
				zc.setSpmc(t.getZspmc());
				zc.setSpcd(t.getZspcd());
				zc.setSppp(t.getZsppp());
				zc.setSpbz(t.getZspbz());
				zc.setZjldwmc(t.getZzjldwmc());
				zc.setZdwsl(t.getZzdwsl());
				zc.setCjldwmc(t.getZcjldwmc());
				zc.setCdwsl(t.getZcdwsl());
				zc.setZdwdj(t.getZzdwdj());
				zc.setSpje(t.getZspje());
				zc.setIsZs(t.getZisZs());
				nl.add(zc);
			}
			if(ywhs.getFromOther() == null || "0".equals(t.getJisZs())){
				YwhsDet jc = new YwhsDet();
				jc.setIsZj("0");
				jc.setSpbh(t.getJspbh());
				jc.setSpmc(t.getJspmc());
				jc.setSpcd(t.getJspcd());
				jc.setSppp(t.getJsppp());
				jc.setSpbz(t.getJspbz());
				jc.setZjldwmc(t.getJzjldwmc());
				jc.setCjldwmc(t.getJcjldwmc());
				jc.setZdwsl(t.getJzdwsl());
				jc.setCdwsl(t.getJcdwsl());
				jc.setZdwdj(t.getJzdwdj());
				jc.setSpje(t.getJspje());
				jc.setIsZs(t.getJisZs());
				nl.add(jc);
			}
		}
		datagrid.setRows(nl);
		return datagrid;
	}

	@Override
	public DataGrid getSpkc(Ywhs ywhs) {
		DataGrid dg = new DataGrid();
		List<ProBean> lists = new ArrayList<ProBean>();
		
		List<ProBean> yw = YwzzServiceImpl.getZzsl(ywhs.getBmbh(), ywhs.getSpbh(), null, ywzzDao);
		if(yw != null){
			lists.addAll(yw);
		}
		
		String hql = "from TSpDet t where t.TDepartment.id = :depId and t.TSp.spbh = :spbh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", ywhs.getBmbh());
		params.put("spbh", ywhs.getSpbh());
		
		TSpDet tSpDet = spDetDao.get(hql, params);
		if(tSpDet != null && tSpDet.getLastRkdj().compareTo(Constant.BD_ZERO) != 0){
			ProBean rkdj = new ProBean();
			rkdj.setGroup("入库信息");
			rkdj.setName("最后入库价");
			rkdj.setValue("" + tSpDet.getLastRkdj());
			lists.add(rkdj);
		}
		
		dg.setRows(lists);
		dg.setTotal((long)lists.size());
		return dg;
	}
	
	@Override
	public DataGrid toKfhs(String ywhslsh){
		String hql = "from TYwhsDet t where t.TYwhs.ywhslsh = :ywhslsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ywhslsh", ywhslsh);
		
		List<TYwhsDet> tYwhsDets = detDao.find(hql, params);
		List<YwhsDet> ywhsDets = new ArrayList<YwhsDet>();
		for(TYwhsDet tYwhsDet : tYwhsDets){
			if("0".equals(tYwhsDet.getZisZs())){
				YwhsDet zc = new YwhsDet();
				zc.setIsZj("1");
				zc.setSpbh(tYwhsDet.getZspbh());
				zc.setSpmc(tYwhsDet.getZspmc());
				zc.setSpcd(tYwhsDet.getZspcd());
				zc.setSppp(tYwhsDet.getZsppp());
				zc.setSpbz(tYwhsDet.getZspbz());
				zc.setZjldwId(tYwhsDet.getZzjldwId());
				zc.setZjldwmc(tYwhsDet.getZzjldwmc());
				zc.setCjldwId(tYwhsDet.getZcjldwId());
				zc.setCjldwmc(tYwhsDet.getZcjldwmc());
				zc.setZhxs(tYwhsDet.getZzhxs());
				zc.setZdwsl(tYwhsDet.getZzdwsl());
				zc.setCdwsl(tYwhsDet.getZcdwsl());
				ywhsDets.add(zc);
			}
			if("0".equals(tYwhsDet.getJisZs())){
				YwhsDet jc = new YwhsDet();
				jc.setIsZj("0");
				jc.setSpbh(tYwhsDet.getJspbh());
				jc.setSpmc(tYwhsDet.getJspmc());
				jc.setSpcd(tYwhsDet.getJspcd());
				jc.setSppp(tYwhsDet.getJsppp());
				jc.setSpbz(tYwhsDet.getJspbz());
				jc.setZjldwId(tYwhsDet.getJzjldwId());
				jc.setZjldwmc(tYwhsDet.getJzjldwmc());
				jc.setCjldwId(tYwhsDet.getJcjldwId());
				jc.setCjldwmc(tYwhsDet.getJcjldwmc());
				jc.setZhxs(tYwhsDet.getJzhxs());
				jc.setZdwsl(tYwhsDet.getJzdwsl());
				jc.setCdwsl(tYwhsDet.getJcdwsl());
				ywhsDets.add(jc);
			}
		}
		
		
//		if(ywhslsh != null && ywhslsh.trim().length() > 0){
//			sql += "where ywhslsh = " + ywhslsh;
//		}
		
//		List<Object[]> l = detDao.findBySQL(sql);
//		List<YwhsDet> nl = new ArrayList<YwhsDet>();
//		
//		for(Object[] os : l){
//			String spbh = (String)os[0];
//			BigDecimal zdwsl = new BigDecimal(os[1].toString());
//			
//			TSp sp = spDao.get(TSp.class, spbh);
//			YwhsDet yd = new YwhsDet();
//			yd.setSpbh(spbh);
//			yd.setSpmc(sp.getSpmc());
//			yd.setSpcd(sp.getSpcd());
//			yd.setSppp(sp.getSppp());
//			yd.setSpbz(sp.getSpbz());
//			yd.setZdwsl(zdwsl);
//			yd.setZjldwmc(sp.getZjldw().getJldwmc());
//			if(sp.getCjldw() != null){
//				yd.setCjldwmc(sp.getCjldw().getJldwmc());
//				yd.setZhxs(sp.getZhxs());
//				yd.setCdwsl(zdwsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
//			}
//			nl.add(yd);
//		}
//		nl.add(new YwhsDet());
//		
		DataGrid dg = new DataGrid();
		//dg.setObj(ywhs);
		dg.setRows(ywhsDets);
		return dg;
	}
	
	
	@Autowired
	public void setYwhsDao(BaseDaoI<TYwhs> ywhsDao) {
		this.ywhsDao = ywhsDao;
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
	public void setDetDao(BaseDaoI<TYwhsDet> detDao) {
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
	public void setSpDao(BaseDaoI<TSp> spDao) {
		this.spDao = spDao;
	}
	
	@Autowired
	public void setSpDetDao(BaseDaoI<TSpDet> spDetDao) {
		this.spDetDao = spDetDao;
	}


	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
}
