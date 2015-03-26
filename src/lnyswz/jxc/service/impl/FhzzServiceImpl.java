package lnyswz.jxc.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.ProBean;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.Fh;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.model.TFhzz;
import lnyswz.jxc.model.TKfzz;
import lnyswz.jxc.service.FhzzServiceI;
import lnyswz.jxc.util.Constant;

/**
 * 分户总账实现类
 * @author 王文阳
 *
 */
@Service("fhzzService")
public class FhzzServiceImpl implements FhzzServiceI {
	private Logger logger = Logger.getLogger(FhzzServiceImpl.class);
	private static BigDecimal ZERO = new BigDecimal(0);
	

	/**
	 * 更新分户
	 * 
	 */
	public static void updateFhzzSl(Sp sp, Department dep, Fh fh, BigDecimal sl, String type, BaseDaoI<TFhzz> baseDao) {
		String hql = "from TFhzz t where t.spbh = :spbh and t.bmbh = :bmbh and t.jzsj = :jzsj";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("spbh", sp.getSpbh());
		params.put("bmbh", dep.getId());
		params.put("jzsj", DateUtil.getCurrentDateString("yyyyMM"));
		//总账处理
		updateFhzz(sp, dep, null, sl, type, baseDao, hql + " and t.fhId = null", params);
		//分户处理
		hql += " and t.fhId = :fhId";
		params.put("fhId", fh.getId());
		updateFhzz(sp, dep, fh, sl, type, baseDao, hql, params);
	}
	
	private static void updateFhzz(Sp sp, Department dep, Fh fh, BigDecimal sl, String type, BaseDaoI<TFhzz> baseDao, String hql, Map<String, Object> params) {
		TFhzz tFhzz = baseDao.get(hql, params);
		if(tFhzz == null){
			tFhzz = new TFhzz();
			BeanUtils.copyProperties(sp, tFhzz);
			tFhzz.setBmbh(dep.getId());
			tFhzz.setBmmc(dep.getDepName());
			if(fh != null){
				tFhzz.setFhId(fh.getId());
				tFhzz.setFhmc(fh.getFhmc());
			}
			tFhzz.setJzsj(DateUtil.getCurrentDateString("yyyyMM"));
			if(type.equals(Constant.UPDATE_RK)){
				tFhzz.setRksl(sl);
				tFhzz.setCksl(ZERO);
			}else{
				tFhzz.setRksl(ZERO);
				tFhzz.setCksl(sl);
			}
			tFhzz.setQcsl(ZERO);
			baseDao.save(tFhzz);
		}else{
			if(type.equals(Constant.UPDATE_RK)){
				tFhzz.setRksl(tFhzz.getRksl().add(sl));
			}else{
				tFhzz.setCksl(tFhzz.getCksl().add(sl));
			}
		}
	}

	public static List<ProBean> getZzsl(String bmbh, String spbh, String fhId, BaseDaoI<TFhzz> baseDao){
		List<ProBean> resultList = new ArrayList<ProBean>();
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "select  fhmc, qcsl + rksl - cksl from t_fhzz where bmbh = ? and spbh = ? and jzsj = ? and fhId = ? ";
		params.put("0", bmbh);
		params.put("1", spbh);
		params.put("2", DateUtil.getCurrentDateString("yyyyMM"));
		params.put("3", fhId);
		
		
		List<Object[]> l = baseDao.findBySQL(sql, params);
		if(l != null && l.size() > 0){
			ProBean kc = new ProBean();
			kc.setGroup("分户库存");
			kc.setName((String)l.get(0)[0]);
			kc.setValue("" + l.get(0)[1]);
			resultList.add(kc);
			return resultList;
		}
		return null;
	}
	
	
	/**
	 * 更新分户出库
	 * 
	 */
//	public static void updateCk(Sp sp, Department dep, Fh fh, BigDecimal cksl, BaseDaoI<TFhzz> baseDao) {
//		String hql = "from TFhzz t where t.spbh = :spbh and t.bmbh = :bmbh and t.jzsj = :jzsj and t.fhId = :fhId";
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("spbh", sp.getSpbh());
//		params.put("bmbh", dep.getId());
//		params.put("fhId", fh.getId());
//		params.put("jzsj", DateUtil.getCurrentDateString("yyyyMM"));
//		//总账处理
//		TFhzz tFhzz = baseDao.get(hql, params);
//		if(tFhzz == null){
//			tFhzz = new TFhzz();
//			BeanUtils.copyProperties(sp, tFhzz);
//			BeanUtils.copyProperties(dep, tFhzz);
//			BeanUtils.copyProperties(fh, tFhzz);
//			tFhzz.setJzsj(DateUtil.getCurrentDateString("yyyyMM"));
//			tFhzz.setCksl(cksl);
//			tFhzz.setQcsl(ZERO);
//			tFhzz.setRksl(ZERO);
//			baseDao.save(tFhzz);
//		}else{
//			tFhzz.setCksl(tFhzz.getCksl().add(cksl));
//		}
//	}
	
}
