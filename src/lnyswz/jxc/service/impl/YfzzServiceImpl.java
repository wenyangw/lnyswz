package lnyswz.jxc.service.impl;

import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.Gys;
import lnyswz.jxc.bean.Kh;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.model.TYfzz;
import lnyswz.jxc.model.TYszz;
import lnyswz.jxc.service.YfzzServiceI;
import lnyswz.jxc.service.YszzServiceI;
import lnyswz.jxc.util.Constant;
import org.springframework.stereotype.Service;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应付总账实现类
 * @author 王文阳
 *
 */
@Service("yfzzService")
public class YfzzServiceImpl implements YfzzServiceI {

	/**
	 * 更新应付
	 * 
	 */
	public static void updateYfzzJe(Department dep, Gys gys, BigDecimal je, String type, BaseDaoI<TYfzz> baseDao) {
		String hql = "from TYfzz t where t.bmbh = :bmbh and t.jzsj = :jzsj and t.gysbh = :gysbh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gysbh", gys.getGysbh());
		params.put("bmbh", dep.getId());
		params.put("jzsj", DateUtil.getCurrentDateString("yyyyMM"));
		
		//总账处理
		updateYfzz(dep, gys, je, type, baseDao, hql, params);
	}
	
	private static void updateYfzz(Department dep, Gys gys, BigDecimal je, String type, BaseDaoI<TYfzz> baseDao, String hql, Map<String, Object> params) {
		TYfzz tYfzz = baseDao.get(hql, params);
		if(tYfzz == null){
			tYfzz = new TYfzz();
			tYfzz.setBmbh(dep.getId());
			tYfzz.setBmmc(dep.getDepName());
			tYfzz.setGysbh(gys.getGysbh());
			tYfzz.setGysmc(gys.getGysmc());
			
			tYfzz.setJzsj(DateUtil.getCurrentDateString("yyyyMM"));
			
			tYfzz.setQcje(Constant.BD_ZERO);

			if(type.equals(Constant.UPDATE_YF_RK)){
				tYfzz.setRkje(je);
				tYfzz.setFkje(Constant.BD_ZERO);
			}
			
			if(type.equals(Constant.UPDATE_YF_FK)){
				tYfzz.setRkje(Constant.BD_ZERO);
				tYfzz.setFkje(je);
			}

			baseDao.save(tYfzz);
		}else{
			if(type.equals(Constant.UPDATE_YF_RK)){
				tYfzz.setRkje(tYfzz.getRkje().add(je));
			}
			if(type.equals(Constant.UPDATE_YF_FK)){
				tYfzz.setFkje(tYfzz.getFkje().add(je));
			}
		}
	}
	
	public static BigDecimal getYfje(String bmbh, String gysbh, String jzsj, BaseDaoI<TYfzz> yfzzDao){
		TYfzz tYfzz = getYfzz(bmbh, gysbh, jzsj, yfzzDao);
		if(tYfzz != null){
			return tYfzz.getQcje().add(tYfzz.getRkje()).subtract(tYfzz.getFkje());
		}
		return Constant.BD_ZERO; 
	}
	
	public static TYfzz getYfzz(String bmbh, String gysbh, String jzsj, BaseDaoI<TYfzz> yfzzDao) {
		String hql = "from TYfzz t where t.bmbh = :bmbh and t.gysbh = :gysbh and t.jzsj = :jzsj";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", bmbh);
		params.put("gysbh", gysbh);
		if(jzsj == null){
			jzsj = DateUtil.getCurrentDateString("yyyyMM");
		}
		params.put("jzsj", jzsj);
		TYfzz tYfzz = yfzzDao.get(hql, params);
		return tYfzz;
	}
	
	/**
	 * 销售回款时根据选定业务员列出对应的客户列表
	 * 2017-02-08
	 * @param bmbh
	 * @param ywyId
	 * @param yszzDao
	 * @return
	 */
//	public static List<Kh> getKhsByYwy(String bmbh, int ywyId, BaseDaoI<TYszz> yszzDao){
//		String sql = "select distinct mx.khbh, kh.khmc from "
//				+ " (select khbh from t_kh_det where depId = ? and ywyId = ?"
//				+ " union all"
//				+ " select khbh from t_yszz where jzsj = ? and bmbh = ? and ywyId = ?) mx"
//				+ " left join t_kh kh on mx.khbh = kh.khbh";
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("0", bmbh);
//		params.put("1", ywyId);
//		params.put("2", DateUtil.getCurrentDateString("yyyyMM"));
//		params.put("3", bmbh);
//		params.put("4", ywyId);
//
//		List<Object[]> results = yszzDao.findBySQL(sql, params);
//		List<Kh> khs = new ArrayList<Kh>();
//
//		Kh kh = null;
//		for(Object[] o : results){
//			if(o[0] != null && o[1] != null){
//				kh = new Kh();
//				kh.setKhbh(o[0].toString());
//				kh.setKhmc(o[1].toString());
//				khs.add(kh);
//			}
//
//		}
//		results.clear();
//		results = null;
//
//		return khs;
//	}
	
	/**
	 * 获取最早一笔未还款的发票(提货)或未开票的提货单
	 * @param bmbh
	 * @param khbh
	 * @param ywyId
	 * @param yszzDao
	 * @return
	 */
//	public static Object[] getLatestXs(String bmbh, String khbh, int ywyId, BaseDaoI<TYszz> yszzDao){
//		String sql = "select top 1 t.bmbh, t.khbh, t.lsh, t.createTime, t.payTime from v_xs_latest t where t.bmbh = ? and t.khbh = ? and t.ywyId = ? order by t.createTime";
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("0", bmbh);
//		params.put("1", khbh);
//		params.put("2", ywyId);
//
//		return yszzDao.getMBySQL(sql, params);
//
//	}
	
	/**
	 * 获取未还款的发票(提货)或未开票的提货单
	 * @param bmbh
	 * @param khbh
	 * @param ywyId
	 * @param yszzDao
	 * @return
	 */
//	public static List<Object[]> getLatestXses(String bmbh, String khbh, int ywyId, BaseDaoI<TYszz> yszzDao){
//		String sql = "select t.bmbh, t.khbh, t.lsh, t.createTime, t.payTime, t.hjje from v_xs_latest t where t.bmbh = ? and t.khbh = ? and t.ywyId = ? order by t.createTime";
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("0", bmbh);
//		params.put("1", khbh);
//		params.put("2", ywyId);
//
//		return yszzDao.findBySQL(sql, params);
//
//	}
	
}
