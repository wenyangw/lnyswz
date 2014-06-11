package lnyswz.jxc.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.ProBean;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.CgjhDet;
import lnyswz.jxc.bean.Ck;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.Hw;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.bean.Ywzz;
import lnyswz.jxc.model.TKfzz;
import lnyswz.jxc.model.TSpDet;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.service.YwzzServiceI;
import lnyswz.jxc.util.Constant;

/**
 * 库房总账实现类
 * @author 王文阳
 *
 */
@Service("ywzzService")
public class YwzzServiceImpl implements YwzzServiceI {
	private Logger logger = Logger.getLogger(YwzzServiceImpl.class);
	private BaseDaoI<TYwzz> ywzzDao;

	@Override
	public DataGrid listLowSps(Ywzz ywzz){
		String sql = "select spDet.depId, spDet.spbh, sp.spmc, sp.spcd, sp.sppp, sp.spbz, sp.zjldwId, dw.jldwmc, spDet.minKc, isnull((yw.qcsl + yw.rksl - yw.xssl), 0) - isnull((ls.qcsl + ls.lssl - ls.kpsl), 0) kcsl" + 
			" from t_sp_det spDet" +
			" left join t_sp sp on sp.spbh = spDet.spbh" +
			" left join t_jldw dw on dw.id = sp.zjldwId" +
			" left join t_ywzz yw on yw.bmbh = spDet.depId and yw.spbh = spDet.spbh and yw.jzsj = CONVERT(char(6), getDate(), 112) and yw.ckId is null" +
			" left join t_lszz ls on ls.bmbh = spDet.depId and ls.spbh = spDet.spbh and ls.jzsj = CONVERT(char(6), getDate(), 112) and ls.ckId is null" +
			" where spDet.depId = ? and spDet.minKc > 0 and spDet.minKc > isnull((yw.qcsl + yw.rksl - yw.xssl), 0) - isnull((ls.qcsl + ls.lssl - ls.kpsl), 0) ";
//		String sql = "select y.spbh, y.spmc, y.spcd, y.sppp, y.spbz, y.zjldwId, y.zjldwmc, s.minKc, y.qcsl + y.rksl - y.xssl " + 
//				" from t_ywzz y " +
//				" left join t_sp_det s on y.spbh = s.spbh and s.depId = y.bmbh " + 
//				" where bmbh = ? and jzsj = ? and ckId is null and qcsl + rksl - xssl < minKc";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", ywzz.getBmbh());
//		params.put("1", DateUtil.getCurrentDateString("yyyyMM"));
		
		List<Object[]> l = ywzzDao.findBySQL(sql, params);
		if(l != null && l.size() > 0){
			List<Ywzz> ywzzs = new ArrayList<Ywzz>();
			for(Object[] o : l){
				Ywzz y = new Ywzz();
				y.setSpbh((String)o[0]);
				y.setSpmc((String)o[1]);
				y.setSpcd((String)o[2]);
				y.setSppp((String)o[3]);
				y.setSpbz((String)o[4]);
				y.setZjldwId((String)o[5]);
				y.setZjldwmc((String)o[6]);
				y.setMinKc(new BigDecimal(o[7].toString()));
				y.setKcsl(new BigDecimal(o[8].toString()));
				ywzzs.add(y);
			}
			DataGrid dg = new DataGrid();
			dg.setRows(ywzzs);
			dg.setTotal((long)ywzzs.size());
			return dg;
		}
		return null;
	}
	
	@Override
	public DataGrid toCgjh(Ywzz ywzz) {
		if(ywzz.getSpbhs() != null && ywzz.getSpbhs().trim().length() > 0){
			List<CgjhDet> l = new ArrayList<CgjhDet>();
			for(String spbh : ywzz.getSpbhs().split(",")){
				CgjhDet cDet = new CgjhDet();
				String hql = "from TYwzz y where y.bmbh = :bmbh and y.spbh = :spbh and y.jzsj = :jzsj and y.ckId = null";
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("bmbh", ywzz.getBmbh());
				params.put("spbh", spbh);
				params.put("jzsj", DateUtil.getCurrentDateString("yyyyMM"));
				TYwzz y = ywzzDao.get(hql, params);
				BeanUtils.copyProperties(y, cDet);
				l.add(cDet);
			}
			DataGrid dg = new DataGrid();
			dg.setRows(l);
			return dg;
		}
		
		return null;
	}
	
	public DataGrid getDwcb(Ywzz ywzz){
		DataGrid dg = new DataGrid();
		String hql = "from TYwzz t where t.bmbh = :bmbh and t.jzsj = :jzsj and t.spbh = :spbh and t.ckId = null";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", ywzz.getBmbh());
		params.put("jzsj", DateUtil.getCurrentDateString("yyyyMM"));
		params.put("spbh", ywzz.getSpbh());
		
		TYwzz tYwzz = ywzzDao.get(hql, params);
		if(tYwzz != null){
			dg.setObj(ywzzDao.get(hql, params).getDwcb());
		}else{
			dg.setObj(Constant.BD_ZERO);
		}
		return dg;
	}
	
	public static BigDecimal getDwcb(String bmbh, String spbh, BaseDaoI<TYwzz> baseDao){
		String hql = "from TYwzz t where t.ckId = null and t.bmbh = :bmbh and t.spbh = :spbh and t.jzsj = :jzsj";
		Map<String , Object> params = new HashMap<String, Object>();
		params.put("jzsj", DateUtil.getCurrentDateString("yyyyMM"));
		params.put("bmbh", bmbh);
		params.put("spbh", spbh);
		TYwzz t = baseDao.get(hql, params);
		if(t != null){
			return t.getDwcb();
		}
		return Constant.BD_ZERO;
	}
	
	/**
	 * 更新业务总账数量
	 */
	public static void updateYwzzSl(Sp sp, Department dep, Ck ck, BigDecimal sl, BigDecimal je, BigDecimal se, BigDecimal cb,
			String type, BaseDaoI<TYwzz> baseDao) {
		
		String hql = "from TYwzz t where t.spbh = :spbh and t.bmbh = :bmbh and t.jzsj = :jzsj";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("spbh", sp.getSpbh());
		params.put("bmbh", dep.getId());
		params.put("jzsj", DateUtil.getCurrentDateString("yyyyMM"));
		//总账处理
		updateYwzz(sp, dep, null, sl, je, se, cb, type, baseDao, hql + " and t.ckId = null", params);
		if(!type.equals(Constant.UPDATE_BT)){
			//总账中仓库更新
			hql += " and t.ckId = :ckId";
			params.put("ckId", ck.getId());
			updateYwzz(sp, dep, ck, sl, je, se, cb, type, baseDao, hql, params);
		}
	}
	
	private static void updateYwzz(Sp sp, Department dep, Ck ck, BigDecimal sl, BigDecimal je, BigDecimal se, BigDecimal cb, 
			String type, BaseDaoI<TYwzz> baseDao, String hql, Map<String, Object> params) {
		TYwzz tYwzz = baseDao.get(hql, params);
		if(tYwzz == null){
			tYwzz = new TYwzz();
			BeanUtils.copyProperties(sp, tYwzz);
			tYwzz.setBmbh(dep.getId());
			tYwzz.setBmmc(dep.getDepName());
			if(ck != null){
				tYwzz.setCkId(ck.getId());
				tYwzz.setCkmc(ck.getCkmc());
			}
			
			tYwzz.setJzsj(DateUtil.getCurrentDateString("yyyyMM"));
			
			tYwzz.setQcsl(Constant.BD_ZERO);
			tYwzz.setQcje(Constant.BD_ZERO);
			if(null == sp.getZhxs()){
				tYwzz.setZhxs(Constant.BD_ZERO);
			}
			if(type.equals(Constant.UPDATE_RK)){
				tYwzz.setRksl(sl);
				if(ck == null){
					tYwzz.setRkje(je);
					if(sl == null){
						tYwzz.setDwcb(Constant.BD_ZERO);
					}else{
						tYwzz.setDwcb(je.divide(sl, 4, BigDecimal.ROUND_HALF_DOWN));
					}
				}else{
					tYwzz.setRkje(Constant.BD_ZERO);
					tYwzz.setDwcb(Constant.BD_ZERO);
				}
				tYwzz.setXssl(Constant.BD_ZERO);
				tYwzz.setXsje(Constant.BD_ZERO);
				tYwzz.setXsse(Constant.BD_ZERO);
				tYwzz.setXscb(Constant.BD_ZERO);
			}else if(type.equals(Constant.UPDATE_CK)){
				tYwzz.setXssl(sl);
				if(ck == null){
					tYwzz.setXsje(je);
					tYwzz.setXsse(se);
					tYwzz.setXscb(cb);
				}else{
					tYwzz.setXsje(Constant.BD_ZERO);
					tYwzz.setXsse(Constant.BD_ZERO);
					tYwzz.setXscb(Constant.BD_ZERO);
				}
				tYwzz.setRksl(Constant.BD_ZERO);
				tYwzz.setRkje(Constant.BD_ZERO);
				tYwzz.setDwcb(Constant.BD_ZERO);
			}else if(type.equals(Constant.UPDATE_BT)){
				if(ck == null){
					tYwzz.setRkje(je);
					if(sl == null){
						tYwzz.setDwcb(Constant.BD_ZERO);
					}else{
						tYwzz.setDwcb(je.divide(sl, 4, BigDecimal.ROUND_HALF_DOWN));
					}
					tYwzz.setRksl(Constant.BD_ZERO);
					tYwzz.setXssl(Constant.BD_ZERO);
					tYwzz.setXsje(Constant.BD_ZERO);
					tYwzz.setXsse(Constant.BD_ZERO);
					tYwzz.setXscb(Constant.BD_ZERO);
				}
			}
			baseDao.save(tYwzz);
		}else{
			if(type.equals(Constant.UPDATE_RK)){
				tYwzz.setRksl(tYwzz.getRksl().add(sl));
				if(ck == null){
					tYwzz.setRkje(tYwzz.getRkje().add(je));
					BigDecimal kcje = tYwzz.getQcje().add(tYwzz.getRkje()).subtract(tYwzz.getXscb());
					BigDecimal kcsl = tYwzz.getQcsl().add(tYwzz.getRksl()).subtract(tYwzz.getXssl());
					if(kcsl.compareTo(Constant.BD_ZERO) != 0){
						tYwzz.setDwcb(kcje.divide(kcsl, 4, BigDecimal.ROUND_HALF_DOWN));
					}else{
						tYwzz.setDwcb(Constant.BD_ZERO);
					}
				}
			}else if(type.equals(Constant.UPDATE_CK)){
				tYwzz.setXssl(tYwzz.getXssl().add(sl));
				if(ck == null){
					tYwzz.setXsje(tYwzz.getXsje().add(je));
					tYwzz.setXsse(tYwzz.getXsse().add(se));
					tYwzz.setXscb(tYwzz.getXscb().add(cb));
				}
			}else if(type.equals(Constant.UPDATE_BT)){
				if(ck == null){
					tYwzz.setRkje(tYwzz.getRkje().add(je));
					BigDecimal kcje = tYwzz.getQcje().add(tYwzz.getRkje()).subtract(tYwzz.getXscb());
					BigDecimal kcsl = tYwzz.getQcsl().add(tYwzz.getRksl()).subtract(tYwzz.getXssl());
					if(kcsl.compareTo(Constant.BD_ZERO) != 0){
						tYwzz.setDwcb(kcje.divide(kcsl, 4, BigDecimal.ROUND_HALF_DOWN));
					}else{
						tYwzz.setDwcb(Constant.BD_ZERO);
					}
				}
			}
			
			
		}
	}
	

	public static List<ProBean> getZzsl(String bmbh, String spbh, String ckId, BaseDaoI<TYwzz> baseDao) {
		List<ProBean> resultList = new ArrayList<ProBean>();
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "select ckmc, qcsl + rksl - xssl from t_ywzz where bmbh = ? and spbh = ? and jzsj = ? ";
		params.put("0", bmbh);
		params.put("1", spbh);
		params.put("2", DateUtil.getCurrentDateString("yyyyMM"));
		if(ckId != null){
			sql += " and ckId = ? ";
			params.put("3", ckId);
		}else{
			sql += " and ckId is null ";
		}
		
		List<Object[]> l = baseDao.findBySQL(sql, params);
		if(l != null && l.size() > 0){
			for(Object[] o : l){
				ProBean yw = new ProBean();
				yw.setGroup("业务库存");
				yw.setName(ckId == null ? "库存数量" : (String)o[0]);
				yw.setValue("" + o[1]);
				resultList.add(yw);
			}
			return resultList;
		}
		return null;
	}

	@Autowired
	public void setYwzzDao(BaseDaoI<TYwzz> ywzzDao) {
		this.ywzzDao = ywzzDao;
	}
}
