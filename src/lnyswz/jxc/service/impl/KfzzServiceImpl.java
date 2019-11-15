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

import lnyswz.common.bean.ProBean;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Ck;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.Hw;
import lnyswz.jxc.bean.Kfzz;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.model.TKfzz;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.service.KfzzServiceI;
import lnyswz.jxc.util.Constant;

/**
 * 库房总账实现类
 * @author 王文阳
 *
 */
@Service("kfzzService")
public class KfzzServiceImpl implements KfzzServiceI {
	private Logger logger = Logger.getLogger(KfzzServiceImpl.class);
	private BaseDaoI<TKfzz> kfzzDao; 

	@Override
	public String findHwId(Kfzz kfzz){
		String hql = "from TKfzz t where t.spbh = :spbh and t.bmbh = :bmbh and jzsj = :jzsj and t.hwId != null";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("spbh", kfzz.getSpbh());
		params.put("bmbh", kfzz.getBmbh());
		params.put("jzsj", DateUtil.getCurrentDateString("yyyyMM"));
		List<TKfzz> kfzzs = kfzzDao.find(hql, params);
		if(kfzzs.size() > 0){
			return kfzzs.get(0).getHwId();
		}
		return "";
	}

	/**
	 * 更新库房总账数量
	 * @param sp
	 * @param bmbh
	 * @param hwId
	 * @param sppc
	 * @param rksl
	 * @param type
	 * @param baseDao
	 */
	public static void updateKfzzSl(Sp sp, Department dep, Ck ck, Hw hw, String sppc, BigDecimal zsl, BigDecimal csl, String type, BaseDaoI<TKfzz> baseDao) {
		
		String hql = "from TKfzz t where t.spbh = :spbh and t.bmbh = :bmbh and t.jzsj = :jzsj";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("spbh", sp.getSpbh());
		params.put("bmbh", dep.getId());
		params.put("jzsj", DateUtil.getCurrentDateString("yyyyMM"));
		//总账处理
		updateKfzz(sp, dep, null, null, null, zsl, csl, type, baseDao, hql + " and t.ckId = null and t.hwId = null and t.sppc = null", params);
		//总账中仓库入库更新
		hql += " and t.ckId = :ckId";
		params.put("ckId", ck.getId());
		updateKfzz(sp, dep, ck, null, null, zsl, csl, type, baseDao, hql + " and t.hwId = null and t.sppc = null", params);
		//总账中货位入库更新
		hql += " and t.hwId = :hwId";
		params.put("hwId", hw.getId());
		updateKfzz(sp, dep, ck, hw, null, zsl, csl, type, baseDao, hql + " and t.sppc = null", params);
		//总账中批次入库处理
		hql +=  " and t.sppc = :sppc";
		params.put("sppc", sppc);
		updateKfzz(sp, dep, ck, hw, sppc, zsl, csl, type, baseDao, hql, params);
		
	}
	
	private static void updateKfzz(Sp sp, Department dep, Ck ck, Hw hw, String sppc,
			BigDecimal zsl, BigDecimal csl, String type, BaseDaoI<TKfzz> baseDao, String hql, Map<String, Object> params) {
		TKfzz tKfzz = baseDao.get(hql, params);
		if(tKfzz == null){
			tKfzz = new TKfzz();
			BeanUtils.copyProperties(sp, tKfzz);
			tKfzz.setBmbh(dep.getId());
			tKfzz.setBmmc(dep.getDepName());
			if(ck != null){
				tKfzz.setCkId(ck.getId());
				tKfzz.setCkmc(ck.getCkmc());
			}
			if(hw != null){
				tKfzz.setHwId(hw.getId());
				tKfzz.setHwmc(hw.getHwmc());
			}
			if(sppc != null){
				tKfzz.setSppc(sppc);
			}
			if(null == sp.getZhxs()){
				tKfzz.setZhxs(Constant.BD_ZERO);
			}
			tKfzz.setJzsj(DateUtil.getCurrentDateString("yyyyMM"));
			if(type.equals(Constant.UPDATE_RK)){
				tKfzz.setRksl(zsl);
				tKfzz.setCrksl(csl);
				tKfzz.setCksl(new BigDecimal(0));
				tKfzz.setCcksl(new BigDecimal(0));
			}else{
				tKfzz.setCksl(zsl);
				tKfzz.setCcksl(csl);
				tKfzz.setRksl(new BigDecimal(0));
				tKfzz.setCrksl(new BigDecimal(0));
			}
			tKfzz.setQcsl(new BigDecimal(0));
			tKfzz.setCqcsl(new BigDecimal(0));
			baseDao.save(tKfzz);
		}else{
			if(type.equals(Constant.UPDATE_RK)){
				tKfzz.setRksl(tKfzz.getRksl().add(zsl));
				tKfzz.setCrksl(tKfzz.getCrksl().add(csl));
			}else{
				tKfzz.setCksl(tKfzz.getCksl().add(zsl));
				tKfzz.setCcksl(tKfzz.getCcksl().add(csl));
			}
			
		}
	}
	
	public static List<ProBean> getZzsl(String bmbh, String spbh, String ckId, String hwId, String sppc, BaseDaoI<TKfzz> baseDao){
		List<ProBean> resultList = new ArrayList<ProBean>();
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "select ";
		if(sppc != null){
			sql += " sppc, hwmc, ";
		}
		sql += " qcsl + rksl - cksl from t_kfzz where bmbh = ? and spbh = ? and jzsj = ?";
		params.put("0", bmbh);
		params.put("1", spbh);
		params.put("2", DateUtil.getCurrentDateString("yyyyMM"));
		if(ckId != null) {
			sql +=  "and ckId = ?";
			params.put("3", ckId);
		} else {
			sql +=  "and ckId is null";
		}

		if(hwId == null && sppc == null){
			sql += " and hwId is null and sppc is null ";
		}
		if(hwId == null && sppc != null ){
			sql += " and sppc is not null order by sppc";
		}
		List<Object[]> l = baseDao.findBySQL(sql, params);
		if(l != null && l.size() > 0){
			if(hwId == null && sppc == null){
				ProBean kc = new ProBean();
				kc.setGroup("库房库存");
				kc.setName("库存");
				kc.setValue("" + l.get(0));
				resultList.add(kc);
			}
			if(hwId != null && sppc == null){
				for(Object[] o : l){
					if(new BigDecimal(o[1].toString()).compareTo(Constant.BD_ZERO) != 0){
						ProBean hw = new ProBean();
						hw.setGroup("货位库存");
						hw.setName((String)o[0]);
						hw.setValue("" + o[1]);
						resultList.add(hw);
					}
				}
			}
			if(hwId == null && sppc != null){
				for(Object[] p : l){
					if(new BigDecimal(p[2].toString()).compareTo(Constant.BD_ZERO) != 0){
						ProBean pc = new ProBean();
						pc.setGroup("批次库存");
						pc.setName((String)p[0] + "(" + (String)p[1] + ")");
						pc.setValue("" + p[2]);
						resultList.add(pc);
					}
				}
			}
			return resultList;
		}
		return null;
		
	}
	
	
//	private static void updateKfzzRk(Sp sp, Department dep, Ck ck, Hw hw, String sppc,
//			BigDecimal rksl, BaseDaoI<TKfzz> baseDao, String hql, Map<String, Object> params) {
//		TKfzz tKfzz = baseDao.get(hql, params);
//		if(tKfzz == null){
//			tKfzz = new TKfzz();
//			BeanUtils.copyProperties(sp, tKfzz);
//			tKfzz.setBmbh(dep.getId());
//			tKfzz.setBmmc(dep.getDepName());
//			if(ck != null){
//				tKfzz.setCkId(ck.getId());
//				tKfzz.setCkmc(ck.getCkmc());
//			}
//			if(hw != null){
//				tKfzz.setHwId(hw.getId());
//				tKfzz.setHwmc(hw.getHwmc());
//			}
//			if(sppc != null){
//				tKfzz.setSppc(sppc);
//			}
//			tKfzz.setJzsj(DateUtil.getCurrentDateString("yyyyMM"));
//			tKfzz.setRksl(rksl);
//			tKfzz.setQcsl(new BigDecimal(0));
//			tKfzz.setCksl(new BigDecimal(0));
//			baseDao.save(tKfzz);
//		}else{
//			tKfzz.setRksl(tKfzz.getRksl().add(rksl));
//			
//		}
//	}
//
//	/**
//	 * 更新库房总账出库
//	 * @param sp
//	 * @param bmbh
//	 * @param hwId
//	 * @param sppc
//	 * @param cksl
//	 * @param baseDao
//	 */
//	private static void updateCk(Sp sp, Department dep, Ck ck, Hw hw, String sppc, BigDecimal cksl, BaseDaoI<TKfzz> baseDao) {
//		
//		String hql = "from TKfzz t where t.TSp.spbh = :spbh and t.bmbh = :bmbh and t.jzsj = :jzsj";
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("spbh", sp.getSpbh());
//		params.put("bmbh", dep.getId());
//		params.put("jzsj", DateUtil.getCurrentDateString("yyyyMM"));
//		//总账处理
//		updateKfzzCk(sp, dep, null, null, null, cksl, baseDao, hql + " and ckId = null and t.sppc = null and t.hwId = null", params);
//		//总账中仓库出库更新
//		hql += " and t.ckId = :ckId";
//		params.put("ckId", ck.getId());
//		updateKfzzCk(sp, dep, ck, hw, null, cksl, baseDao, hql + " and t.hwId  = null and t.sppc = null", params);
//
//		//总账中货位入库更新
//		hql += " and t.hwId = :hwId";
//		params.put("hwId", hw.getId());
//		updateKfzzCk(sp, dep, ck, hw, null, cksl, baseDao, hql + " and t.sppc = null", params);
//		//总账中批次入库处理
//		hql +=  " and t.sppc = :sppc";
//		params.put("sppc", sppc);
//		updateKfzzCk(sp, dep, ck, hw, sppc, cksl, baseDao, hql, params);
//		
//	}
//	
//	private static void updateKfzzCk(Sp sp, Department dep, Ck ck, Hw hw, String sppc,
//			BigDecimal cksl, BaseDaoI<TKfzz> baseDao, String hql, Map<String, Object> params) {
//		TKfzz tKfzz = baseDao.get(hql, params);
//		if(tKfzz == null){
//			tKfzz = new TKfzz();
//			BeanUtils.copyProperties(sp, tKfzz);
//			tKfzz.setBmbh(dep.getId());
//			tKfzz.setBmmc(dep.getDepName());
//			if(ck != null){
//				tKfzz.setCkId(ck.getId());
//				tKfzz.setCkmc(ck.getCkmc());
//			}
//			if(hw != null){
//				tKfzz.setHwId(hw.getId());
//				tKfzz.setHwmc(hw.getHwmc());
//			}
//			if(sppc != null){
//				tKfzz.setSppc(sppc);
//			}
//			tKfzz.setJzsj(DateUtil.getCurrentDateString("yyyyMM"));
//			tKfzz.setCksl(cksl);
//			tKfzz.setQcsl(new BigDecimal(0));
//			tKfzz.setRksl(new BigDecimal(0));
//			baseDao.save(tKfzz);
//		}else{
//			tKfzz.setCksl(tKfzz.getCksl().add(cksl));
//			
//		}
//	}
//
//
//
//	@Override
//	public void update(Kfzz hfzz) {
//		// TODO Auto-generated method stub
//
//	}

	@Autowired
	public void setKfzzDao(BaseDaoI<TKfzz> kfzzDao) {
		this.kfzzDao = kfzzDao;
	}

}
