package lnyswz.jxc.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.*;
import lnyswz.jxc.model.*;
import lnyswz.jxc.service.KfdbServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Util;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 库房调拨实现类
 * @author 王文阳
 *
 */
@Service("kfdbService")
public class KfdbServiceImpl implements KfdbServiceI {
	private Logger logger = Logger.getLogger(KfdbServiceImpl.class);
	private BaseDaoI<TKfdb> kfdbDao;
	private BaseDaoI<TKfdbDet> detDao;
	private BaseDaoI<TYwdb> ywdbDao;
	private BaseDaoI<TKfzz> kfzzDao;
	private BaseDaoI<THw> hwDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TSp> spDao;
	private BaseDaoI<TOperalog> operalogDao;

	@Override
	public Kfdb save(Kfdb kfdb) {
		String lsh = LshServiceImpl.updateLsh(kfdb.getBmbh(), kfdb.getLxbh(), lshDao);
		TKfdb tKfdb = new TKfdb();
		BeanUtils.copyProperties(kfdb, tKfdb);
		tKfdb.setCreateTime(new Date());
		tKfdb.setKfdblsh(lsh);
		tKfdb.setBmmc(depDao.load(TDepartment.class, kfdb.getBmbh()).getDepName());
		
		tKfdb.setIsCj("0");

		TYwdb tYwdb = ywdbDao.get(TYwdb.class, kfdb.getYwdblsh());
		tYwdb.setKfdblsh(lsh);

		tKfdb.setBz(tKfdb.getBz() + "/" + tYwdb.getYwdblsh());

		Department dep = new Department();
		dep.setId(kfdb.getBmbh());
		dep.setDepName(tKfdb.getBmmc());

		Ck ckF = new Ck();
		ckF.setId(kfdb.getCkIdF());
		ckF.setCkmc(tKfdb.getCkmcF());
		Ck ckT = new Ck();
		ckT.setId(kfdb.getCkIdT());
		ckT.setCkmc(tKfdb.getCkmcT());

		THw tHw = hwDao.get(THw.class, ckF.getId());
		Hw hwF = new Hw();
		BeanUtils.copyProperties(tHw, hwF);
		tHw = hwDao.get(THw.class, ckT.getId());
		Hw hwT = new Hw();
		BeanUtils.copyProperties(tHw, hwT);

		//处理商品明细
		Set<TKfdbDet> tDets = new HashSet<TKfdbDet>();
		ArrayList<KfdbDet> kfdbDets = JSON.parseObject(kfdb.getDatagrid(), new TypeReference<ArrayList<KfdbDet>>(){});
		TKfdbDet tDet = null;
		Sp sp = null;
		for(KfdbDet kfdbDet : kfdbDets){
			tDet = new TKfdbDet();
			BeanUtils.copyProperties(kfdbDet, tDet, new String[]{"id"});
			tDet.setSppc(Constant.SPPC);
			
			if("".equals(kfdbDet.getCjldwId()) || kfdbDet.getZhxs() == null || kfdbDet.getZhxs().compareTo(Constant.BD_ZERO) == 0){
				tDet.setCdwsl(Constant.BD_ZERO);
				tDet.setZhxs(Constant.BD_ZERO);
				kfdbDet.setZhxs(Constant.BD_ZERO);
			}
			
			tDet.setTKfdb(tKfdb);
			tDets.add(tDet);

			sp = new Sp();
			BeanUtils.copyProperties(kfdbDet, sp);

			//更新库房总账
			KfzzServiceImpl.updateKfzzSl(sp, dep, ckF, hwF, Constant.SPPC, tDet.getZdwsl().negate(), tDet.getCdwsl().negate(), Constant.UPDATE_RK, kfzzDao);
			KfzzServiceImpl.updateKfzzSl(sp, dep, ckT, hwT, Constant.SPPC, tDet.getZdwsl(), tDet.getCdwsl(), Constant.UPDATE_RK, kfzzDao);
		}
		tKfdb.setTKfdbDets(tDets);
		
		kfdbDao.save(tKfdb);		
		OperalogServiceImpl.addOperalog(kfdb.getCreateId(), kfdb.getBmbh(), kfdb.getMenuId(), tKfdb.getKfdblsh(), "生成库房调拨", operalogDao);
		
		Kfdb rKfdb = new Kfdb();
		rKfdb.setKfdblsh(lsh);
		return rKfdb;
		
	}
	
	@Override
	public void cjKfdb(Kfdb kfdb) {
		Date now = new Date();
		//获取原单据信息
		TKfdb yTKfdb = kfdbDao.get(TKfdb.class, kfdb.getKfdblsh());
		//新增冲减单据信息
		TKfdb tKfdb = new TKfdb();
		BeanUtils.copyProperties(yTKfdb, tKfdb);
		
		//更新原单据冲减信息
		yTKfdb.setCjId(kfdb.getCjId());
		yTKfdb.setCjTime(now);
		yTKfdb.setCjName(kfdb.getCjName());
		yTKfdb.setIsCj("1");
		
		//更新新单据信息
		String lsh = LshServiceImpl.updateLsh(kfdb.getBmbh(), kfdb.getLxbh(), lshDao);
		tKfdb.setKfdblsh(lsh);
		tKfdb.setCreateTime(now);
		tKfdb.setCreateId(kfdb.getCjId());
		tKfdb.setCreateName(kfdb.getCjName());
		tKfdb.setIsCj("1");
		tKfdb.setCjTime(now);
		tKfdb.setCjId(kfdb.getCjId());
		tKfdb.setCjName(kfdb.getCjName());
		tKfdb.setCjKfdblsh(yTKfdb.getKfdblsh());
		tKfdb.setBz(kfdb.getBz());

		// 取消与业务调拨的关联
		String hqlYwdb = "from TYwdb where kfdblsh = :kfdblsh";
		Map<String, Object> paramsYwdb = new HashMap<String, Object>();
		paramsYwdb.put("kfdblsh", yTKfdb.getKfdblsh());
		TYwdb tYwdb = ywdbDao.get(hqlYwdb, paramsYwdb);
		tYwdb.setKfdblsh(null);

		Department dep = new Department();
		dep.setId(tKfdb.getBmbh());
		dep.setDepName(tKfdb.getBmmc());

		Ck ckF = new Ck();
		ckF.setId(tKfdb.getCkIdF());
		ckF.setCkmc(tKfdb.getCkmcF());
		Ck ckT = new Ck();
		ckT.setId(tKfdb.getCkIdT());
		ckT.setCkmc(tKfdb.getCkmcT());

		THw tHw = hwDao.get(THw.class, ckF.getId());
		Hw hwF = new Hw();
		BeanUtils.copyProperties(tHw, hwF);
		tHw = hwDao.get(THw.class, ckT.getId());
		Hw hwT = new Hw();
		BeanUtils.copyProperties(tHw, hwT);

		Set<TKfdbDet> yTKfdbDets = yTKfdb.getTKfdbDets();
		Set<TKfdbDet> tDets = new HashSet<TKfdbDet>();
		TKfdbDet tDet = null;
		Sp sp = null;
		for(TKfdbDet yTDet : yTKfdbDets){
			tDet = new TKfdbDet();
			BeanUtils.copyProperties(yTDet, tDet, new String[]{"id"});
			tDet.setZdwsl(yTDet.getZdwsl().negate());
			if(yTDet.getCdwsl() != null){
				tDet.setCdwsl(yTDet.getCdwsl().negate());
			}
			tDet.setTKfdb(tKfdb);
			tDets.add(tDet);

			sp = new Sp();
			BeanUtils.copyProperties(tDet, sp);
			
			//更新库房总账
			KfzzServiceImpl.updateKfzzSl(sp, dep, ckF, hwF, Constant.SPPC, tDet.getZdwsl().negate(), tDet.getCdwsl().negate(), Constant.UPDATE_RK, kfzzDao);
			KfzzServiceImpl.updateKfzzSl(sp, dep, ckT, hwT, Constant.SPPC, tDet.getZdwsl(), tDet.getCdwsl(), Constant.UPDATE_RK, kfzzDao);
		}

		tKfdb.setTKfdbDets(tDets);
		kfdbDao.save(tKfdb);
		OperalogServiceImpl.addOperalog(kfdb.getCjId(), kfdb.getBmbh(), kfdb.getMenuId(), tKfdb.getCjKfdblsh() + "/" + tKfdb.getKfdblsh(), "冲减业务调拨", operalogDao);
	}
	
	@Override
	public DataGrid datagrid(Kfdb kfdb) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TKfdb t where t.bmbh = :bmbh and t.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", kfdb.getBmbh());
		if(kfdb.getCreateTime() != null){
			params.put("createTime", kfdb.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		if(kfdb.getSearch() != null){
			hql += " and (" +
					Util.getQueryWhere(kfdb.getSearch(), new String[]{"t.kfdblsh", "t.bz"}, params)
					+ ")";
		}
		
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TKfdb> l = kfdbDao.find(hql, params, kfdb.getPage(), kfdb.getRows());
		List<Kfdb> nl = new ArrayList<Kfdb>();
		Kfdb k = null;
		for(TKfdb t : l){
			k = new Kfdb();
			BeanUtils.copyProperties(t, k);
			
			nl.add(k);
		}
		datagrid.setTotal(kfdbDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(String kfdblsh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TKfdbDet t where t.TKfdb.kfdblsh = :kfdblsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("kfdblsh", kfdblsh);
		List<TKfdbDet> l = detDao.find(hql, params);
		List<KfdbDet> nl = new ArrayList<KfdbDet>();
		KfdbDet k = null;
		for(TKfdbDet t : l){
			k = new KfdbDet();
			BeanUtils.copyProperties(t, k);
			nl.add(k);
		}
		datagrid.setRows(nl);
		return datagrid;
	}

	@Autowired
	public void setKfdbDao(BaseDaoI<TKfdb> kfdbDao) {
		this.kfdbDao = kfdbDao;
	}

	@Autowired
	public void setDetDao(BaseDaoI<TKfdbDet> detDao) {
		this.detDao = detDao;
	}

	@Autowired
	public void setYwdbDao(BaseDaoI<TYwdb> ywdbDao) {
		this.ywdbDao = ywdbDao;
	}

	@Autowired
	public void setKfzzDao(BaseDaoI<TKfzz> kfzzDao) {
		this.kfzzDao = kfzzDao;
	}

	@Autowired
	public void setHwDao(BaseDaoI<THw> hwDao) {
		this.hwDao = hwDao;
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
