package lnyswz.jxc.service.impl;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.ProBean;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.*;
import lnyswz.jxc.model.TBgzz;
import lnyswz.jxc.model.TKfzz;
import lnyswz.jxc.service.BgzzServiceI;
import lnyswz.jxc.util.Constant;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保管总账实现类
 * @author 王文阳
 *
 */
@Service("bgzzService")
public class BgzzServiceImpl implements BgzzServiceI {
	private Logger logger = Logger.getLogger(BgzzServiceImpl.class);
	private BaseDaoI<TKfzz> kfzzDao;
	private BaseDaoI<TBgzz> bgzzDao; 

	/**
	 * 更新保管总账数量
	 * @param sp
	 * @param type
	 * @param baseDao
	 */
	public static void updateBgzzSl(Sp sp, Department dep, BigDecimal zsl, String type, BaseDaoI<TBgzz> baseDao) {
		String hql = "from TBgzz t where t.spbh = :spbh and t.bmbh = :bmbh and t.jzsj = :jzsj";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("spbh", sp.getSpbh());
		params.put("bmbh", dep.getId());
		params.put("jzsj", DateUtil.getCurrentDateString("yyyyMM"));
		//总账处理
		updateBgzz(sp, dep, zsl, type, baseDao, hql, params);
	}
	
	private static void updateBgzz(Sp sp, Department dep, BigDecimal zsl, String type, BaseDaoI<TBgzz> baseDao, String hql, Map<String, Object> params) {
		TBgzz tBgzz = baseDao.get(hql, params);
		if(tBgzz == null){
			tBgzz = new TBgzz();
			BeanUtils.copyProperties(sp, tBgzz);
			tBgzz.setBmbh(dep.getId());
			tBgzz.setBmmc(dep.getDepName());
			if(null == sp.getZhxs()){
				tBgzz.setZhxs(Constant.BD_ZERO);
			}
			tBgzz.setJzsj(DateUtil.getCurrentDateString("yyyyMM"));
			if(type.equals(Constant.UPDATE_RK)){
				tBgzz.setRksl(zsl);
				tBgzz.setCksl(new BigDecimal(0));
			}else{
				tBgzz.setCksl(zsl);
				tBgzz.setRksl(new BigDecimal(0));
			}
			tBgzz.setQcsl(new BigDecimal(0));
			baseDao.save(tBgzz);
		}else{
			if(type.equals(Constant.UPDATE_RK)){
				tBgzz.setRksl(tBgzz.getRksl().add(zsl));
			}else{
				tBgzz.setCksl(tBgzz.getCksl().add(zsl));
			}
		}
	}

	public static List<ProBean> getZzsl(String bmbh, String spbh, boolean isHj, BaseDaoI<TBgzz> baseDao){
		List<ProBean> resultList = new ArrayList<ProBean>();
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "select isnull(sum(qcsl + rksl - cksl), 0) from t_bgzz where spbh = ? and jzsj = ?";
		params.put("0", spbh);
		params.put("1", DateUtil.getCurrentDateString("yyyyMM"));
		if(isHj) {
			sql += " and (bmbh = ? or bmbh = ?)";
			params.put("2", "04");
			params.put("3", "05");
		} else {
			sql += " and bmbh = ?";
			params.put("2", bmbh);
		}

		List<Object[]> l = baseDao.findBySQL(sql, params);
		if(l != null && l.size() > 0){
			ProBean kc = new ProBean();
			kc.setGroup(isHj ? "保管总账" : "保管账");
			kc.setName("库存");
			kc.setValue("" + l.get(0));
			resultList.add(kc);

			return resultList;
		}
		return null;

	}

	@Autowired
	public void setKfzzDao(BaseDaoI<TKfzz> kfzzDao) {
		this.kfzzDao = kfzzDao;
	}

	@Autowired
	public void setBgzzDao(BaseDaoI<TBgzz> bgzzDao) {
		this.bgzzDao = bgzzDao;
	}

}
