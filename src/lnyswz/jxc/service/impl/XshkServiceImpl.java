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
import lnyswz.jxc.bean.XskpDet;
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
	private BaseDaoI<TXskp> xskpDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TKh> khDao;
	private BaseDaoI<TSp> spDao;
	private BaseDaoI<TYszz> yszzDao;
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
		tXshk.setLastHkje(xshk.getLastHkje());

		String depName = depDao.load(TDepartment.class, xshk.getBmbh()).getDepName();
		tXshk.setBmmc(depName);
		
		Department dep = new Department();
		dep.setId(xshk.getBmbh());
		dep.setDepName(depName);
		
		Kh kh = new Kh();
		kh.setKhbh(xshk.getKhbh());
		kh.setKhmc(xshk.getKhmc());
		
		//处理商品明细
		Set<TXskp> tXskps = new HashSet<TXskp>();
		ArrayList<Xskp> xskps = JSON.parseObject(xshk.getDatagrid(), new TypeReference<ArrayList<Xskp>>(){});
		for(Xskp x : xskps){
			TXskp tXskp= xskpDao.load(TXskp.class, x.getXskplsh());
			tXskp.setHkje(x.getHkje());
			tXskp.setTXshk(tXshk);
			//tXskps.add(tXskp);
		}
		YszzServiceImpl.updateYszzJe(dep, kh, tXshk.getHkje(), Constant.UPDATE_HK, yszzDao);
		//tXshk.setTXskps(tXskps);
		xshkDao.save(tXshk);
				
//		OperalogServiceImpl.addOperalog(xshk.getCreateId(), xshk.getBmbh(), xshk.getMenuId(), tXshk.getXshklsh(), 
//				"生成销售提货单", operalogDao);
//		
		Xshk rXshk = new Xshk();
		rXshk.setXshklsh(lsh);
		return rXshk;
		
	}

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
		tXshk.setCancelXshklsh(yTXshk.getXshklsh());
		tXshk.setCreateId(xshk.getCancelId());
		tXshk.setCreateTime(now);
		tXshk.setCreateName(xshk.getCancelName());
		tXshk.setIsCancel("1");
		tXshk.setCancelId(xshk.getCancelId());
		tXshk.setCancelName(xshk.getCancelName());
		tXshk.setCancelTime(now);
		tXshk.setHkje(yTXshk.getHkje().negate());
		
		Department dep = new Department();
		dep.setId(tXshk.getBmbh());
		dep.setDepName(tXshk.getBmmc());
		
		Kh kh = new Kh();
		kh.setKhbh(tXshk.getKhbh());
		kh.setKhmc(tXshk.getKhmc());
		
		//更新授信客户应付金额
		YszzServiceImpl.updateYszzJe(dep, kh, tXshk.getHkje(), Constant.UPDATE_YS_KP, yszzDao);
		
		
		
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
	
	@Autowired
	public void setXshkDao(BaseDaoI<TXshk> xshkDao) {
		this.xshkDao = xshkDao;
	}

	@Autowired
	public void setXskpDao(BaseDaoI<TXskp> xskpDao) {
		this.xskpDao = xskpDao;
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
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
}