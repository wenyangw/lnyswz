package lnyswz.jxc.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
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
import lnyswz.jxc.bean.Ck;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.Hw;
import lnyswz.jxc.bean.Xskp;
import lnyswz.jxc.bean.Ywdb;
import lnyswz.jxc.bean.YwdbDet;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.bean.Ywrk;
import lnyswz.jxc.bean.YwrkDet;
import lnyswz.jxc.model.TCgjh;
import lnyswz.jxc.model.TCgjhDet;
import lnyswz.jxc.model.TCgxqDet;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.THw;
import lnyswz.jxc.model.TYwdb;
import lnyswz.jxc.model.TYwdbDet;
import lnyswz.jxc.model.TKfzz;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.model.TYwrk;
import lnyswz.jxc.model.TYwrkDet;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.service.YwdbServiceI;
import lnyswz.jxc.util.Constant;

/**
 * 采购需求实现类
 * @author 王文阳
 *
 */
@Service("ywdbService")
public class YwdbServiceImpl implements YwdbServiceI {
	private Logger logger = Logger.getLogger(YwdbServiceImpl.class);
	private BaseDaoI<TYwdb> ywdbDao;
	private BaseDaoI<TYwdbDet> detDao;
	private BaseDaoI<TCgxqDet> cgxqDetDao;
	private BaseDaoI<TYwzz> ywzzDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TSp> spDao;
	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public Ywdb save(Ywdb ywdb) {
		String lsh = LshServiceImpl.updateLsh(ywdb.getBmbh(), ywdb.getLxbh(), lshDao);
		TYwdb tYwdb = new TYwdb();
		BeanUtils.copyProperties(ywdb, tYwdb);
		tYwdb.setCreateTime(new Date());
		tYwdb.setYwdblsh(lsh);
		tYwdb.setBmmc(depDao.load(TDepartment.class, ywdb.getBmbh()).getDepName());
		
		tYwdb.setIsCj("0");
		if(ywdb.getCgxqlsh() != null){
			tYwdb.setCgxqlsh(ywdb.getCgxqlsh());
		}
		
		//处理商品明细
		Set<TYwdbDet> tDets = new HashSet<TYwdbDet>();
		ArrayList<YwdbDet> ywdbDets = JSON.parseObject(ywdb.getDatagrid(), new TypeReference<ArrayList<YwdbDet>>(){});
		for(YwdbDet ywdbDet : ywdbDets){
			TYwdbDet tDet = new TYwdbDet();
			BeanUtils.copyProperties(ywdbDet, tDet);
			
			if(ywdb.getCgxqlsh() != null && ywdb.getCgxqlsh().length() != 0){
				TCgxqDet tCgxqDet = cgxqDetDao.load(TCgxqDet.class, ywdbDet.getCgxqDetId());
				tCgxqDet.setDbsl(tCgxqDet.getDbsl().add(ywdbDet.getZdwsl()));
				if (!("".equals(ywdbDet.getCjldwId()) || ywdbDet.getZhxs() == null || ywdbDet.getZhxs().compareTo(Constant.BD_ZERO) == 0)){
					tCgxqDet.setCdbsl(tCgxqDet.getCdbsl().add(ywdbDet.getCdwsl()));	
				}
			}
			
			if("".equals(ywdbDet.getCjldwId()) || ywdbDet.getZhxs() == null || ywdbDet.getZhxs().compareTo(Constant.BD_ZERO) == 0){
				tDet.setCdwsl(Constant.BD_ZERO);
				tDet.setZhxs(Constant.BD_ZERO);
				ywdbDet.setZhxs(Constant.BD_ZERO);
			}
			
			tDet.setTYwdb(tYwdb);
			tDets.add(tDet);

			Sp sp = new Sp();
			BeanUtils.copyProperties(ywdbDet, sp);
			Department dep = new Department();
			dep.setId(ywdb.getBmbh());
			dep.setDepName(tYwdb.getBmmc());
			Ck ckF = new Ck();
			ckF.setId(ywdb.getCkIdF());
			ckF.setCkmc(tYwdb.getCkmcF());
			Ck ckT = new Ck();
			ckT.setId(ywdb.getCkIdT());
			ckT.setCkmc(tYwdb.getCkmcT());
			
			//更新业务总账
			YwzzServiceImpl.updateYwzzSl(sp, dep, ckF, tDet.getZdwsl().negate(), tDet.getCdwsl().negate(), Constant.BD_ZERO, Constant.BD_ZERO, Constant.BD_ZERO,
				Constant.UPDATE_DB, ywzzDao);
			YwzzServiceImpl.updateYwzzSl(sp, dep, ckT, tDet.getZdwsl(), tDet.getCdwsl(), Constant.BD_ZERO, Constant.BD_ZERO, Constant.BD_ZERO,
				Constant.UPDATE_DB, ywzzDao);
		}
		tYwdb.setTYwdbDets(tDets);
		
		ywdbDao.save(tYwdb);		
		OperalogServiceImpl.addOperalog(ywdb.getCreateId(), ywdb.getBmbh(), ywdb.getMenuId(), tYwdb.getYwdblsh(), "生成业务调拨", operalogDao);
		
		Ywdb rYwdb = new Ywdb();
		rYwdb.setYwdblsh(lsh);
		return rYwdb;
		
	}
	
	@Override
	public void cjYwdb(Ywdb ywdb) {
		Date now = new Date(); 
		//获取原单据信息
		TYwdb yTYwdb = ywdbDao.get(TYwdb.class, ywdb.getYwdblsh());
		//新增冲减单据信息
		TYwdb tYwdb = new TYwdb();
		BeanUtils.copyProperties(yTYwdb, tYwdb);
		
		//更新原单据冲减信息
		yTYwdb.setCjId(ywdb.getCjId());
		yTYwdb.setCjTime(now);
		yTYwdb.setCjName(ywdb.getCjName());
		yTYwdb.setIsCj("1");
		
		//更新新单据信息
		String lsh = LshServiceImpl.updateLsh(ywdb.getBmbh(), ywdb.getLxbh(), lshDao);
		tYwdb.setYwdblsh(lsh);
		tYwdb.setCreateTime(now);
		tYwdb.setCreateId(ywdb.getCjId());
		tYwdb.setCreateName(ywdb.getCjName());
		tYwdb.setIsCj("1");
		tYwdb.setCjTime(new Date());
		tYwdb.setCjId(ywdb.getCjId());
		tYwdb.setCjName(ywdb.getCjName());
		tYwdb.setCjYwdblsh(yTYwdb.getYwdblsh());
		tYwdb.setBz(ywdb.getBz());
		
		Department dep = new Department();
		dep.setId(tYwdb.getBmbh());
		dep.setDepName(tYwdb.getBmmc());
		Ck ckF = new Ck();
		ckF.setId(tYwdb.getCkIdF());
		ckF.setCkmc(tYwdb.getCkmcF());

		Ck ckT = new Ck();
		ckT.setId(tYwdb.getCkIdT());
		ckT.setCkmc(tYwdb.getCkmcT());
		
		Set<TYwdbDet> yTYwdbDets = yTYwdb.getTYwdbDets();
		Set<TYwdbDet> tDets = new HashSet<TYwdbDet>();
		for(TYwdbDet yTDet : yTYwdbDets){
			TYwdbDet tDet = new TYwdbDet();
			BeanUtils.copyProperties(yTDet, tDet, new String[]{"id"});
			tDet.setZdwsl(yTDet.getZdwsl().negate());
			if(yTDet.getCdwsl() != null){
				tDet.setCdwsl(yTDet.getCdwsl().negate());
			}
			tDet.setTYwdb(tYwdb);
			tDets.add(tDet);
			
			if(yTYwdb.getCgxqlsh() != null && yTYwdb.getCgxqlsh().trim().length() > 0){
				String cgxqHql = "from TCgxqDet t where t.TCgxq.cgxqlsh = :cgxqlsh and t.spbh = :spbh";
				Map<String, Object> cgxqParams = new HashMap<String, Object>();
				cgxqParams.put("cgxqlsh", yTYwdb.getCgxqlsh());
				cgxqParams.put("spbh", yTDet.getSpbh());
				TCgxqDet tCgxqDet = cgxqDetDao.get(cgxqHql, cgxqParams);
				
				tCgxqDet.setDbsl(tCgxqDet.getDbsl().subtract(yTDet.getZdwsl()));
				if(yTDet.getCdwsl().compareTo(BigDecimal.ZERO) != 0){
					tCgxqDet.setCdbsl(tCgxqDet.getCdbsl().subtract(yTDet.getCdwsl()));
				}
			}
			
			Sp sp = new Sp();
			BeanUtils.copyProperties(tDet, sp);
			
			//更新业务总账
			YwzzServiceImpl.updateYwzzSl(sp, dep, ckF, tDet.getZdwsl().negate(), tDet.getCdwsl().negate(), Constant.BD_ZERO, Constant.BD_ZERO, Constant.BD_ZERO,
					Constant.UPDATE_DB, ywzzDao);
			YwzzServiceImpl.updateYwzzSl(sp, dep, ckT, tDet.getZdwsl(), tDet.getCdwsl(), Constant.BD_ZERO, Constant.BD_ZERO, Constant.BD_ZERO,
					Constant.UPDATE_DB, ywzzDao);
		}
		
		if(yTYwdb.getCgxqlsh() != null){
			yTYwdb.setCgxqlsh(null);
		}
		
		tYwdb.setTYwdbDets(tDets);
		ywdbDao.save(tYwdb);		
		OperalogServiceImpl.addOperalog(ywdb.getCjId(), ywdb.getBmbh(), ywdb.getMenuId(), tYwdb.getCjYwdblsh() + "/" + tYwdb.getYwdblsh(), "冲减业务调拨", operalogDao);
	}
	
	@Override
	public DataGrid datagrid(Ywdb ywdb) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TYwdb t where t.bmbh = :bmbh and t.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", ywdb.getBmbh());
		if(ywdb.getCreateTime() != null){
			params.put("createTime", ywdb.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		if(ywdb.getSearch() != null){
			hql += " and (t.ywdblsh like :search or t.bz like :search)"; 
			params.put("search", "%" + ywdb.getSearch() + "%");
			
		}
		
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TYwdb> l = ywdbDao.find(hql, params, ywdb.getPage(), ywdb.getRows());
		List<Ywdb> nl = new ArrayList<Ywdb>();
		for(TYwdb t : l){
			Ywdb c = new Ywdb();
			BeanUtils.copyProperties(t, c);
			
			nl.add(c);
		}
		datagrid.setTotal(ywdbDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(String ywdblsh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TYwdbDet t where t.TYwdb.ywdblsh = :ywdblsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ywdblsh", ywdblsh);
		List<TYwdbDet> l = detDao.find(hql, params);
		List<YwdbDet> nl = new ArrayList<YwdbDet>();
		for(TYwdbDet t : l){
			YwdbDet c = new YwdbDet();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid getSpkc(Ywdb ywdb) {
		DataGrid dg = new DataGrid();
		List<ProBean> lists = new ArrayList<ProBean>();
		BigDecimal sl = Constant.BD_ZERO;
		
		List<ProBean> yw = YwzzServiceImpl.getZzsl(ywdb.getBmbh(), ywdb.getSpbh(), ywdb.getCkId(), ywzzDao);
		if(yw != null){
			sl = sl.add(new BigDecimal(yw.get(0).getValue()));
			lists.addAll(yw);
		}
		
		ProBean slBean = new ProBean();
		slBean.setGroup("可调数量");
		slBean.setName("数量");
		slBean.setValue(sl.toString());
		lists.add(0, slBean);
		
		dg.setRows(lists);
		dg.setTotal((long)lists.size());
		return dg;
	}
		
	@Autowired
	public void setYwdbDao(BaseDaoI<TYwdb> ywdbDao) {
		this.ywdbDao = ywdbDao;
	}

	@Autowired
	public void setDetDao(BaseDaoI<TYwdbDet> detDao) {
		this.detDao = detDao;
	}

	@Autowired
	public void setCgxqDetDao(BaseDaoI<TCgxqDet> cgxqDetDao) {
		this.cgxqDetDao = cgxqDetDao;
	}

	@Autowired
	public void setYwzzDao(BaseDaoI<TYwzz> ywzzDao) {
		this.ywzzDao = ywzzDao;
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
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}

}
