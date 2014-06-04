package lnyswz.jxc.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.Fh;
import lnyswz.jxc.bean.Kh;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.model.TFhzz;
import lnyswz.jxc.model.TYszz;
import lnyswz.jxc.service.FhzzServiceI;
import lnyswz.jxc.service.YszzServiceI;
import lnyswz.jxc.util.Constant;

/**
 * 应付总账实现类
 * @author 王文阳
 *
 */
@Service("yszzService")
public class YszzServiceImpl implements YszzServiceI {
	private Logger logger = Logger.getLogger(YszzServiceImpl.class);

	/**
	 * 更新应收
	 * 
	 */
	public static void updateYszzJe(Department dep, Kh kh, BigDecimal je, String type, BaseDaoI<TYszz> baseDao) {
		String hql = "from TYszz t where t.bmbh = :bmbh and t.jzsj = :jzsj and t.khbh = :khbh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("khbh", kh.getKhbh());
		params.put("bmbh", dep.getId());
		params.put("jzsj", DateUtil.getCurrentDateString("yyyyMM"));
		
		//总账处理
		updateYszz(dep, kh, je, type, baseDao, hql, params);
	}
	
	private static void updateYszz(Department dep, Kh kh, BigDecimal je, String type, BaseDaoI<TYszz> baseDao, String hql, Map<String, Object> params) {
		TYszz tYszz = baseDao.get(hql, params);
		if(tYszz == null){
			tYszz = new TYszz();
			tYszz.setBmbh(dep.getId());
			tYszz.setBmmc(dep.getDepName());
			tYszz.setKhbh(kh.getKhbh());
			tYszz.setKhmc(kh.getKhmc());
			tYszz.setJzsj(DateUtil.getCurrentDateString("yyyyMM"));
			
			tYszz.setQcje(Constant.BD_ZERO);
			tYszz.setQclsje(Constant.BD_ZERO);
			
			if(type.equals(Constant.UPDATE_YS_LS)){
				tYszz.setYsje(Constant.BD_ZERO);
				tYszz.setYslsje(je);
				tYszz.setHkje(Constant.BD_ZERO);
			}
			if(type.equals(Constant.UPDATE_YS_KP)){
				tYszz.setYsje(je);
				tYszz.setYslsje(Constant.BD_ZERO);
				tYszz.setHkje(Constant.BD_ZERO);
			}
			if(type.equals(Constant.UPDATE_YS_KP_LS)){
				tYszz.setYsje(je);
				tYszz.setYslsje(je.negate());
				tYszz.setHkje(Constant.BD_ZERO);
			}
			if(type.equals(Constant.UPDATE_HK)){
				tYszz.setYsje(Constant.BD_ZERO);
				tYszz.setYslsje(Constant.BD_ZERO);
				tYszz.setHkje(je);
			}
			baseDao.save(tYszz);
		}else{
			if(type.equals(Constant.UPDATE_YS_LS)){
				tYszz.setYslsje(tYszz.getYslsje().add(je));
			}
			if(type.equals(Constant.UPDATE_YS_KP)){
				tYszz.setYsje(tYszz.getYsje().add(je));
			}
			if(type.equals(Constant.UPDATE_YS_KP_LS)){
				tYszz.setYsje(tYszz.getYsje().add(je));
				tYszz.setYslsje(tYszz.getYslsje().subtract(je));
			}
			if(type.equals(Constant.UPDATE_HK)){
				tYszz.setHkje(tYszz.getHkje().add(je));
			}
		}
	}
	
	public static BigDecimal getYsje(String bmbh, String khbh){
		String hql = "from TYszz t where t.bmbh = :bmbh and t.khbh = :khbh and t.jzsj = :jzsj";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", bmbh);
		params.put("khbh", khbh);
		params.put
		return null; 
	}
}
