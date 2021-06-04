package lnyswz.jxc.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.*;
import lnyswz.jxc.model.*;
import lnyswz.jxc.service.RkfkServiceI;
import lnyswz.jxc.service.YwrkServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Util;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 销售回款实现类
 * @author 王文阳
 *
 */
@Service("rkfkService")
public class RkfkServiceImpl implements RkfkServiceI {
	private YwrkServiceI ywrkService;
	private BaseDaoI<TRkfk> rkfkDao;
	private BaseDaoI<TYwrk> ywrkDao;
	private BaseDaoI<TYwrkGys> ywrkGysDao;
	private BaseDaoI<TFkRk> fkRkDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TUser> userDao;
	private BaseDaoI<TYfzz> yfzzDao;
	private BaseDaoI<TGys> gysDao;
	private BaseDaoI<TGysDet> gysDetDao;
	private BaseDaoI<TOperalog> operalogDao;
	
	@Override
	public Rkfk save(Rkfk rkfk) {
		TRkfk tRkfk = new TRkfk();
		BeanUtils.copyProperties(rkfk, tRkfk);
		String lsh = LshServiceImpl.updateLsh(rkfk.getBmbh(), rkfk.getLxbh(), lshDao);
		tRkfk.setRkfklsh(lsh);
		tRkfk.setCreateTime(new Date());
		tRkfk.setIsCancel("0");

		String depName = depDao.load(TDepartment.class, rkfk.getBmbh()).getDepName();
		tRkfk.setBmmc(depName);
		
		Department dep = new Department();
		dep.setId(rkfk.getBmbh());
		dep.setDepName(depName);
		
		Gys gys = new Gys();
		gys.setGysbh(rkfk.getGysbh());
		gys.setGysmc(rkfk.getGysmc());

		rkfkDao.save(tRkfk);

//		if(rkfk.getIsLs().equals("0")){
		//处理商品明细
		ArrayList<Ywrk> ywrks = JSON.parseObject(rkfk.getDatagrid(), new TypeReference<ArrayList<Ywrk>>(){});
		// TODO
		// 预付的处理
		//有入库记录的
		if(ywrks != null && ywrks.size() > 0){
			Set<TFkRk> tFkRks = new HashSet<TFkRk>();
			TFkRk tFkRk = null;
			TYwrkGys tYwrkGys = null;
			for(Ywrk y : ywrks){
				tFkRk = new TFkRk();
				tFkRk.setYwrklsh(y.getYwrklsh());
				tFkRk.setYwrkId(y.getYwrkId());
				tFkRk.setFkje(y.getFkje());
				tFkRk.setRkfklsh(lsh);
				tFkRk.setIsYf("0");
				tFkRk.setDeleted(0);
				tFkRks.add(tFkRk);
				fkRkDao.save(tFkRk);

				tYwrkGys = ywrkGysDao.load(TYwrkGys.class, y.getYwrkId());
				tYwrkGys.setFkje(tYwrkGys.getFkje().add(y.getFkje()));
			}
		}
		YfzzServiceImpl.updateYfzzJe(dep, gys, tRkfk.getFkje(), Constant.UPDATE_YF_FK, yfzzDao);

				
		OperalogServiceImpl.addOperalog(rkfk.getCreateId(), rkfk.getBmbh(), rkfk.getMenuId(), tRkfk.getRkfklsh(),
				"保存入库付款", operalogDao);

		Rkfk rRkfk = new Rkfk();
		rRkfk.setRkfklsh(lsh);
		return rRkfk;
	}



	@Override
	public void cancelRkfk(Rkfk rkfk) {
		Date now = new Date();
		String lsh = LshServiceImpl.updateLsh(rkfk.getBmbh(), rkfk.getLxbh(), lshDao);
		
		//获取原单据信息
		TRkfk yTRkfk = rkfkDao.load(TRkfk.class, rkfk.getRkfklsh());

		//新增冲减单据信息
		TRkfk tRkfk = new TRkfk();
		BeanUtils.copyProperties(yTRkfk, tRkfk, new String[]{"TFkRks"});

		//更新原单据信息
		yTRkfk.setIsCancel("1");
		yTRkfk.setCancelId(rkfk.getCancelId());
		yTRkfk.setCancelName(rkfk.getCancelName());
		yTRkfk.setCancelTime(now);
		
		tRkfk.setRkfklsh(lsh);
		tRkfk.setCancelRkfklsh(yTRkfk.getRkfklsh());
		tRkfk.setCreateId(rkfk.getCancelId());
		tRkfk.setCreateTime(now);
		tRkfk.setCreateName(rkfk.getCancelName());
		tRkfk.setIsCancel("1");
		tRkfk.setCancelId(rkfk.getCancelId());
		tRkfk.setCancelName(rkfk.getCancelName());
		tRkfk.setCancelTime(now);
		tRkfk.setFkje(yTRkfk.getFkje().negate());
		
		Department dep = new Department();
		dep.setId(tRkfk.getBmbh());
		dep.setDepName(tRkfk.getBmmc());
		
		Gys gys = new Gys();
		gys.setGysbh(tRkfk.getGysbh());
		gys.setGysmc(tRkfk.getGysmc());

		//更新供应商应付金额
		YfzzServiceImpl.updateYfzzJe(dep, gys, tRkfk.getFkje(), Constant.UPDATE_YF_FK, yfzzDao);

//		Set<TFkRk> tFkRks = yTRkfk.getTFkRks();
		List<TFkRk> tFkRks = FkRkServiceImpl.listByRkfk(yTRkfk.getRkfklsh(), fkRkDao);
		TYwrkGys tYwrkGys = null;
		for(TFkRk tFkRk : tFkRks){
			tYwrkGys = ywrkGysDao.load(TYwrkGys.class, tFkRk.getYwrkId());
			tYwrkGys.setFkje(tYwrkGys.getFkje().subtract(tFkRk.getFkje()));
			if("1".equals(tFkRk.getIsYf())) {
				tYwrkGys.setYfje(tYwrkGys.getYfje().subtract(tFkRk.getFkje()));
			}
			tFkRk.setDeleted(1);
		}

//		Iterator<TFkRk> it = tFkRks.iterator();
//		while(it.hasNext()){
//			TFkRk t = it.next();
//			fkRkDao.delete(t);
//		}
			
		rkfkDao.save(tRkfk);
		
		OperalogServiceImpl.addOperalog(rkfk.getCancelId(), rkfk.getBmbh(), rkfk.getMenuId(), tRkfk.getCancelRkfklsh() + "/" + tRkfk.getRkfklsh(), 
			"取消入库付款", operalogDao);
		
	}
	
//	@Override
//	public DataGrid printRkfk(Rkfk rkfk) {
//		DataGrid dg = new DataGrid();
//
//		Gys gys = GysServiceImpl.getGyssx(rkfk.getGysbh(), rkfk.getBmbh(), rkfk.getYwyId(), gysDao, gysDetDao, gyslxDao);
//
//		TUser u = userDao.load(TUser.class, rkfk.getYwyId());
//
//		//BigDecimal ysje = YfzzServiceImpl.getYsje(rkfk.getBmbh(), rkfk.getGysbh(), rkfk.getYwyId(), DateUtil.dateToString(rkfk.getSelectTime(), "yyyyMM"), yfzzDao);
//		//BigDecimal lsje = YfzzServiceImpl.getLsje(rkfk.getBmbh(), rkfk.getGysbh(), rkfk.getYwyId(), yfzzDao);
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("gsmc", Constant.BMMCS.get(rkfk.getBmbh()));
//		map.put("gysmc", gys.getGysmc());
//		map.put("gysbh", rkfk.getGysbh());
//		map.put("sxje", new DecimalFormat("##0").format(gys.getSxje()));
//		map.put("sxzq", gys.getSxzq());
//		map.put("ywymc", u.getRealName());
//		if(DateUtil.getMonth(rkfk.getSelectTime()).equals(DateUtil.getMonthPattern())){
//			map.put("selectTime", new Date());
//		}else{
//			map.put("selectTime", DateUtil.getLastDayInMonth(rkfk.getSelectTime()));
//		}
//
//		String hql = "from TYwrk t where t.bmbh = :bmbh and t.gysbh = :gysbh and t.ywyId = :ywyId and t.jsfsId = :jsfsId and createTime < :createTime and (t.hjje + t.hjse) <> t.hkje and t.isCj = '0'";
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("bmbh", rkfk.getBmbh());
//		params.put("gysbh", rkfk.getGysbh());
//		params.put("ywyId", rkfk.getYwyId());
//		params.put("jsfsId", Constant.XSKP_JSFS_QK);
//		params.put("createTime", DateUtil.dateIncreaseByMonth(rkfk.getSelectTime(), 1));
//		List<TYwrk> tYwrks = ywrkDao.find(hql, params);
//
//		List<Ywrk> ywrks = new ArrayList<Ywrk>();
//		for(TYwrk tYwrk : tYwrks){
//			Ywrk x = new Ywrk();
//			x.setYwrklsh(tYwrk.getYwrklsh());
//			x.setCreateTime(tYwrk.getCreateTime());
//			x.setPayTime(DateUtil.dateIncreaseByDay(tYwrk.getCreateTime(), gys.getSxzq()));
//			x.setHjje(tYwrk.getHjje().add(tYwrk.getHjse()));
//			x.setHkedje(tYwrk.getHkje());
//			ywrks.add(x);
//		}
//
//		String sql = "SELECT qmje, lsje, dbo.getYsjeCircle(bmbh, gysbh, ywyId, jzsj, 0, 0) AS ysje_0, dbo.getYsjeCircle(bmbh, gysbh, ywyId, jzsj, 1, 30) AS ysje_1,"
//				+ " dbo.getYsjeCircle(bmbh, gysbh, ywyId, jzsj, 31, 90) AS ysje_2, dbo.getYsjeCircle(bmbh, gysbh, ywyId, jzsj, 91, 180) AS ysje_3,"
//				+ " dbo.getYsjeCircle(bmbh, gysbh, ywyId, jzsj, 181, 7300) AS ysje_4"
//				+ " FROM dbo.v_yfzz_sxfxb"
//				+ " where bmbh = ? and gysbh = ? and ywyId = ? and jzsj = ?";
//		Map<String, Object> sqlParams = new HashMap<String, Object>();
//		sqlParams.put("0", rkfk.getBmbh());
//		sqlParams.put("1", rkfk.getGysbh());
//		sqlParams.put("2", rkfk.getYwyId());
//		sqlParams.put("3", DateUtil.dateToString(rkfk.getSelectTime(), "yyyyMM"));
//		Object[] ys = ywrkDao.getMBySQL(sql, sqlParams);
//
//		map.put("ysje", new BigDecimal(ys[0].toString()));
//		map.put("lsje", new BigDecimal(ys[1].toString()));
//		map.put("ysje00", new BigDecimal(ys[2].toString()));
//		map.put("ysje01", new BigDecimal(ys[3].toString()));
//		map.put("ysje02", new BigDecimal(ys[4].toString()));
//		map.put("ysje03", new BigDecimal(ys[5].toString()));
//		map.put("ysje04", new BigDecimal(ys[6].toString()));
//
//		dg.setObj(map);
//		dg.setRows(ywrks);
//		return dg;
//	}
	
	@Override
	public DataGrid datagrid(Rkfk rkfk) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TRkfk t where t.bmbh = :bmbh and t.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", rkfk.getBmbh());

		//默认显示当前月数据
		if(rkfk.getCreateTime() != null){
			params.put("createTime", rkfk.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		
		if(rkfk.getSearch() != null){
			//hql += " and (t.rkfklsh like :search or t.gysmc like :search)"; 
			//params.put("search", "%" + rkfk.getSearch() + "%");
			hql += " and (" + 
					Util.getQueryWhere(rkfk.getSearch(), new String[]{"t.rkfklsh", "t.gysmc"}, params)
					+ ")";
		}
		
		String countHql = "select count(*)" + hql;
		hql += " order by t.createTime desc";
		
		List<TRkfk> l = rkfkDao.find(hql, params, rkfk.getPage(), rkfk.getRows());
		List<Rkfk> nl = new ArrayList<Rkfk>();
		Rkfk c = null;
		for(TRkfk t : l){
			c = new Rkfk();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setTotal(rkfkDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(Rkfk rkfk) {
		DataGrid datagrid = new DataGrid();
		TRkfk tRkfk = rkfkDao.load(TRkfk.class, rkfk.getRkfklsh());
//		Set<TFkRk> tFkRks = tRkfk.getTFkRks();
		List<TFkRk> tFkRks = FkRkServiceImpl.listByRkfk(tRkfk.getRkfklsh(), fkRkDao);
		List<Ywrk> ywrks = new ArrayList<Ywrk>();
		Ywrk ywrk = null;
		TYwrk tYwrk = null;
		for(TFkRk t : tFkRks){
			ywrk = new Ywrk();
			tYwrk = ywrkDao.load(TYwrk.class, t.getYwrklsh());
			ywrk.setYwrklsh(t.getYwrklsh());
			ywrk.setCreateTime(tYwrk.getFpDate());
			ywrk.setHjje(tYwrk.getHjje());
			ywrk.setFkje(t.getFkje());
			ywrk.setBz(tYwrk.getBz());
			ywrks.add(ywrk);
		}
		datagrid.setRows(ywrks);
		return datagrid;
	}

	@Override
	public boolean canCancel(String rkfklsh) {
		TRkfk tRkfk = rkfkDao.load(TRkfk.class, rkfklsh);
		return "0".equals(tRkfk.getIsCancel());
	}

	public static List<TRkfk> listRkfksWithYf(String bmbh, String gysbh, BaseDaoI<TRkfk> dao) {
		String hql = "from TRkfk t where t.bmbh = :bmbh and t.gysbh = :gysbh and t.isCancel = '0' and t.yfje <> 0 order by t.payTime, createTime";
		Map<String, Object> params = new HashMap<>();
		params.put("bmbh", bmbh);
		params.put("gysbh", gysbh);

		return dao.find(hql, params);

	}
	@Autowired
	public void setYwrkService(YwrkServiceI ywrkService) {
		this.ywrkService = ywrkService;
	}

	@Autowired
	public void setRkfkDao(BaseDaoI<TRkfk> rkfkDao) {
		this.rkfkDao = rkfkDao;
	}

	@Autowired
	public void setYwrkDao(BaseDaoI<TYwrk> ywrkDao) {
		this.ywrkDao = ywrkDao;
	}

	@Autowired
	public void setYwrkGysDao(BaseDaoI<TYwrkGys> ywrkGysDao) {
		this.ywrkGysDao = ywrkGysDao;
	}

	@Autowired
	public void setFkRkDao(BaseDaoI<TFkRk> fkRkDao) {
		this.fkRkDao = fkRkDao;
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
	public void setUserDao(BaseDaoI<TUser> userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setYfzzDao(BaseDaoI<TYfzz> yfzzDao) {
		this.yfzzDao = yfzzDao;
	}

	@Autowired
	public void setGysDao(BaseDaoI<TGys> gysDao) {
		this.gysDao = gysDao;
	}

	@Autowired
	public void setGysDetDao(BaseDaoI<TGysDet> gysDetDao) {
		this.gysDetDao = gysDetDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
}
