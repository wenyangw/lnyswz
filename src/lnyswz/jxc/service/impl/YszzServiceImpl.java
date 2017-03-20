package lnyswz.jxc.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.Kh;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.model.TYszz;
import lnyswz.jxc.service.YszzServiceI;
import lnyswz.jxc.util.Constant;

/**
 * 应付总账实现类
 * @author 王文阳
 *
 */
@Service("yszzService")
public class YszzServiceImpl implements YszzServiceI {

	/**
	 * 更新应收
	 * 
	 */
	public static void updateYszzJe(Department dep, Kh kh, User ywy, BigDecimal je, String type, BaseDaoI<TYszz> baseDao) {
		String hql = "from TYszz t where t.bmbh = :bmbh and t.jzsj = :jzsj and t.khbh = :khbh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("khbh", kh.getKhbh());
		params.put("bmbh", dep.getId());
		params.put("jzsj", DateUtil.getCurrentDateString("yyyyMM"));
		
		//总账处理
		updateYszz(dep, kh, null, je, type, baseDao, hql + " and t.ywyId = 0", params);
		
		hql += " and ywyId = :ywyId";
		params.put("ywyId", ywy.getId());
		updateYszz(dep, kh, ywy, je, type, baseDao, hql, params);
		
	}
	
	private static void updateYszz(Department dep, Kh kh, User ywy, BigDecimal je, String type, BaseDaoI<TYszz> baseDao, String hql, Map<String, Object> params) {
		TYszz tYszz = baseDao.get(hql, params);
		if(tYszz == null){
			tYszz = new TYszz();
			tYszz.setBmbh(dep.getId());
			tYszz.setBmmc(dep.getDepName());
			tYszz.setKhbh(kh.getKhbh());
			tYszz.setKhmc(kh.getKhmc());
			
			tYszz.setJzsj(DateUtil.getCurrentDateString("yyyyMM"));
			
			if(ywy != null){
				tYszz.setYwyId(ywy.getId());
				tYszz.setYwymc(ywy.getRealName());
			}
			
			tYszz.setQcje(Constant.BD_ZERO);
			tYszz.setQcthje(Constant.BD_ZERO);
			
			if(type.equals(Constant.UPDATE_YS_LS)){
				tYszz.setLsje(je);
				tYszz.setKpje(Constant.BD_ZERO);
				tYszz.setThje(Constant.BD_ZERO);
				tYszz.setHkje(Constant.BD_ZERO);
			}
			
			if(type.equals(Constant.UPDATE_YS_TH)){
				tYszz.setLsje(Constant.BD_ZERO);
				tYszz.setKpje(Constant.BD_ZERO);
				tYszz.setThje(je);
				tYszz.setHkje(Constant.BD_ZERO);
			}
			if(type.equals(Constant.UPDATE_YS_KP)){
				tYszz.setLsje(Constant.BD_ZERO);
				tYszz.setKpje(je);
				tYszz.setThje(Constant.BD_ZERO);
				tYszz.setHkje(Constant.BD_ZERO);
			}
			if(type.equals(Constant.UPDATE_YS_KP_TH)){
				tYszz.setLsje(Constant.BD_ZERO);
				tYszz.setKpje(je);
				tYszz.setThje(je.negate());
				tYszz.setHkje(Constant.BD_ZERO);
			}
			if(type.equals(Constant.UPDATE_HK)){
				tYszz.setLsje(Constant.BD_ZERO);
				tYszz.setKpje(Constant.BD_ZERO);
				tYszz.setThje(Constant.BD_ZERO);
				tYszz.setHkje(je);
			}
			baseDao.save(tYszz);
		}else{
			if(type.equals(Constant.UPDATE_YS_LS)){
				tYszz.setLsje(je);
			}
			if(type.equals(Constant.UPDATE_YS_TH)){
				tYszz.setThje(tYszz.getThje().add(je));
			}
			if(type.equals(Constant.UPDATE_YS_KP)){
				tYszz.setKpje(tYszz.getKpje().add(je));
			}
			if(type.equals(Constant.UPDATE_YS_KP_TH)){
				tYszz.setKpje(tYszz.getKpje().add(je));
				tYszz.setThje(tYszz.getThje().subtract(je));
			}
			if(type.equals(Constant.UPDATE_HK)){
				tYszz.setHkje(tYszz.getHkje().add(je));
			}
			if(type.equals(Constant.UPDATE_HK_LS)){
				tYszz.setHkje(tYszz.getHkje().add(je));
				tYszz.setLsje(tYszz.getLsje().subtract(je));
			}
		}
	}
	
	public static BigDecimal getYsje(String bmbh, String khbh, int ywyId, String jzsj, BaseDaoI<TYszz> yszzDao){
		TYszz tYszz = getYszz(bmbh, khbh, ywyId, jzsj, yszzDao);
		if(tYszz != null){
			return tYszz.getQcje().add(tYszz.getKpje()).subtract(tYszz.getHkje());
		}
		return Constant.BD_ZERO; 
	}
	
	public static BigDecimal getYsjeNoLs(String bmbh, String khbh, int ywyId, String jzsj, BaseDaoI<TYszz> yszzDao){
		TYszz tYszz = getYszz(bmbh, khbh, ywyId, jzsj, yszzDao);
		if(tYszz != null){
			return tYszz.getQcje().add(tYszz.getKpje()).subtract(tYszz.getHkje()).subtract(tYszz.getLsje());
		}
		return Constant.BD_ZERO; 
	}

	public static TYszz getYszz(String bmbh, String khbh, int ywyId,
			String jzsj, BaseDaoI<TYszz> yszzDao) {
		String hql = "from TYszz t where t.bmbh = :bmbh and t.khbh = :khbh and t.ywyId = :ywyId and t.jzsj = :jzsj";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", bmbh);
		params.put("khbh", khbh);
		params.put("ywyId", ywyId);
		if(jzsj == null){
			jzsj = DateUtil.getCurrentDateString("yyyyMM");
		}
		params.put("jzsj", jzsj);
		TYszz tYszz = yszzDao.get(hql, params);
		return tYszz;
	}
	
	public static BigDecimal getLsje(String bmbh, String khbh, int ywyId, BaseDaoI<TYszz> yszzDao){
		TYszz tYszz = getYszz(bmbh, khbh, ywyId, null, yszzDao);
		if(tYszz != null){
			return tYszz.getLsje();
		}
		return Constant.BD_ZERO; 
	}
	/**
	 * 销售回款时根据选定业务员列出对应的客户列表
	 * 2017-02-08
	 * @param bmbh
	 * @param ywyId
	 * @param yszzDao
	 * @return
	 */
	public static List<Kh> getKhsByYwy(String bmbh, int ywyId, BaseDaoI<TYszz> yszzDao){
		/*String hql = "from TYszz t where t.bmbh = :bmbh and t.ywyId = :ywyId and t.jzsj = :jzsj and t.ywyId > 0";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", bmbh);
		params.put("ywyId", ywyId);
		params.put("jzsj", DateUtil.getCurrentDateString("yyyyMM"));
		List<TYszz> tYszzs = yszzDao.find(hql, params);*/
		
		/*List<Kh> khs = new ArrayList<Kh>();
		
		for(TYszz t : tYszzs){
			Kh kh = new Kh();
			kh.setKhbh(t.getKhbh());
			kh.setKhmc(t.getKhmc());
			
			khs.add(kh);
		}*/
		
		String sql = "select distinct mx.khbh, kh.khmc from "
				+ " (select khbh from t_kh_det where depId = ? and ywyId = ?"
				+ " union all"
				+ " select khbh from t_yszz where jzsj = ? and bmbh = ? and ywyId = ?) mx"
				+ " left join t_kh kh on mx.khbh = kh.khbh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", bmbh);
		params.put("1", ywyId);
		params.put("2", DateUtil.getCurrentDateString("yyyyMM"));
		params.put("3", bmbh);
		params.put("4", ywyId);
		
		List<Object[]> results = yszzDao.findBySQL(sql, params);
		List<Kh> khs = new ArrayList<Kh>();
		
		Kh kh = null;
		for(Object[] o : results){
			if(o[0] != null && o[1] != null){
				kh = new Kh();
				kh.setKhbh(o[0].toString());
				kh.setKhmc(o[1].toString());
				khs.add(kh);
			}
			
		}
		results.clear();
		results = null;
		
		return khs;
	}
	
	/**
	 * 获取最早一笔未还款的发票(提货)或未开票的提货单
	 * @param bmbh
	 * @param khbh
	 * @param ywyId
	 * @param yszzDao
	 * @return
	 */
	public static Object[] getLatestXs(String bmbh, String khbh, int ywyId, BaseDaoI<TYszz> yszzDao){
		String sql = "select top 1 t.bmbh, t.khbh, t.lsh, t.createTime, t.payTime from v_xs_latest t where t.bmbh = ? and t.khbh = ? and t.ywyId = ? order by t.createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", bmbh);
		params.put("1", khbh);
		params.put("2", ywyId);
		
		return yszzDao.getMBySQL(sql, params);
		
	}
	
	/**
	 * 获取未还款的发票(提货)或未开票的提货单
	 * @param bmbh
	 * @param khbh
	 * @param ywyId
	 * @param yszzDao
	 * @return
	 */
	public static List<Object[]> getLatestXses(String bmbh, String khbh, int ywyId, BaseDaoI<TYszz> yszzDao){
		String sql = "select t.bmbh, t.khbh, t.lsh, t.createTime, t.payTime, t.hjje from v_xs_latest t where t.bmbh = ? and t.khbh = ? and t.ywyId = ? order by t.createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", bmbh);
		params.put("1", khbh);
		params.put("2", ywyId);
		
		return yszzDao.findBySQL(sql, params);
		
	}
	
}
