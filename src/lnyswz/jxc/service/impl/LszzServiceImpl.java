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
import lnyswz.jxc.model.TLszz;
import lnyswz.jxc.model.TSpDet;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.service.LszzServiceI;
import lnyswz.jxc.service.YwzzServiceI;
import lnyswz.jxc.util.Constant;

/**
 * 临时总账实现类
 * @author 王文阳
 *
 */
@Service("lszzService")
public class LszzServiceImpl implements LszzServiceI {
	private Logger logger = Logger.getLogger(LszzServiceImpl.class);

		
	/**
	 * 更新更新总账数量
	 */
	public static void updateLszzSl(Sp sp, Department dep, BigDecimal sl, BigDecimal je, String type, BaseDaoI<TLszz> baseDao) {
		
		String hql = "from TLszz t where t.spbh = :spbh and t.bmbh = :bmbh and t.jzsj = :jzsj";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("spbh", sp.getSpbh());
		params.put("bmbh", dep.getId());
		params.put("jzsj", DateUtil.getCurrentDateString("yyyyMM"));
		//总账处理
		updateLszz(sp, dep, sl, je, type, baseDao, hql, params);
//		//总账中仓库入库更新
//		hql += " and t.ckId = :ckId";
//		params.put("ckId", ck.getId());
//		updateYwzz(sp, dep, ck, sl, je, se, cb, type, baseDao, hql, params);
	}
	
	private static void updateLszz(Sp sp, Department dep, BigDecimal sl, BigDecimal je, String type,
			BaseDaoI<TLszz> baseDao, String hql, Map<String, Object> params) {
		TLszz tLszz = baseDao.get(hql, params);
		if(tLszz == null){
			tLszz = new TLszz();
			BeanUtils.copyProperties(sp, tLszz);
			if(null == sp.getZhxs()){
				tLszz.setZhxs(Constant.BD_ZERO);
			}
			tLszz.setBmbh(dep.getId());
			tLszz.setBmmc(dep.getDepName());
						
			tLszz.setJzsj(DateUtil.getCurrentDateString("yyyyMM"));
			
			
			
			tLszz.setQcsl(Constant.BD_ZERO);
			tLszz.setQcje(Constant.BD_ZERO);
			if(type.equals(Constant.UPDATE_RK)){
				tLszz.setLssl(sl);
				tLszz.setLsje(je);
				tLszz.setKpsl(Constant.BD_ZERO);
				tLszz.setKpje(Constant.BD_ZERO);
			}else{
				tLszz.setKpsl(sl);
				tLszz.setKpje(je);
				tLszz.setLssl(Constant.BD_ZERO);
				tLszz.setLsje(Constant.BD_ZERO);
			}
			baseDao.save(tLszz);
		}else{
			if(type.equals(Constant.UPDATE_RK)){
				tLszz.setLssl(tLszz.getLssl().add(sl));
				tLszz.setLsje(tLszz.getLsje().add(je));
			}else{
				tLszz.setKpsl(tLszz.getKpsl().add(sl));
				tLszz.setKpje(tLszz.getKpje().add(je));
			}
			
		}
	}
	
}
