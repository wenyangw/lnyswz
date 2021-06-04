package lnyswz.jxc.service.impl;


import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.*;
import lnyswz.jxc.model.*;
import lnyswz.jxc.service.RkServiceI;
import lnyswz.jxc.util.Constant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 入库通用实现类
 * @author 王文阳
 *
 */
@Service("rkService")
public class RkServiceImpl implements RkServiceI {
	private BaseDaoI<TYwrk> ywrkDao;
	private BaseDaoI<TYwrkGys> ywrkGysDao;
	private BaseDaoI<TYfzz> yfzzDao;
	private BaseDaoI<TRkfk> rkfkDao;
	private BaseDaoI<TFkRk> fkRkDao;
	private BaseDaoI<TLsh> lshDao;

	@Override
	public void saveYf(Department dep, Gys gys, BigDecimal hjje, String ywrklsh, String ywbtlsh, String type){

		TYwrkGys tYwrkGys = new TYwrkGys();
		tYwrkGys.setYwrklsh(ywrklsh);
		tYwrkGys.setGysbh(gys.getGysbh());
		tYwrkGys.setGysmc(gys.getGysmc());
		tYwrkGys.setHjje(hjje);
		tYwrkGys.setYfje(BigDecimal.ZERO);
		tYwrkGys.setFkje(BigDecimal.ZERO);
		tYwrkGys.setYwbtlsh(ywbtlsh);

		ywrkGysDao.save(tYwrkGys);

		// 负数入库，等同于一笔付款
		if (hjje.compareTo(BigDecimal.ZERO) < 0) {
			YfzzServiceImpl.updateYfzzJe(dep, gys, hjje.negate(), Constant.UPDATE_YF_FK, yfzzDao);
			tYwrkGys.setFkje(hjje);
			// t_rkfk 是否建立与t_ywrk的关联
			Rkfk rkfk = new Rkfk();
			rkfk.setCreateId(999);
			rkfk.setCreateName(type);
			rkfk.setCreateTime(new Date());
			rkfk.setBmbh(dep.getId());
			rkfk.setBmmc(dep.getDepName());
			rkfk.setGysbh(gys.getGysbh());
			rkfk.setGysmc(gys.getGysmc());
			rkfk.setFkje(hjje.negate());
			rkfk.setYfje(BigDecimal.ZERO);
			rkfk.setPayTime(new Date());
			rkfk.setIsYf("0");
			rkfk.setIsCancel("0");
			rkfk.setYwrkId(tYwrkGys.getId());
			rkfk.setYwlsh(ywbtlsh == null ? ywrklsh : ywbtlsh);

			saveRkfk(rkfk);
			ywrkGysDao.save(tYwrkGys);
			return;
		}

		YfzzServiceImpl.updateYfzzJe(dep, gys, hjje, Constant.UPDATE_YF_RK, yfzzDao);

		// 验证是否有预付
		List<TRkfk> rkfks = RkfkServiceImpl.listRkfksWithYf(dep.getId(), gys.getGysbh(), rkfkDao);
		if (rkfks.size() > 0) {
			TFkRk tFkRk = null;
			BigDecimal wfkje = hjje.subtract(tYwrkGys.getFkje());
			for (TRkfk tRkfk : rkfks) {
				tFkRk = new TFkRk();
				tFkRk.setYwrklsh(ywrklsh);
				tFkRk.setYwrkId(tYwrkGys.getId());
				tFkRk.setRkfklsh(tRkfk.getRkfklsh());
				tFkRk.setIsYf("1");
				tFkRk.setDeleted(0);
				if (tRkfk.getYfje().compareTo(wfkje) > 0) {
					tYwrkGys.setFkje(tYwrkGys.getFkje().add(wfkje));
					tYwrkGys.setYfje(tYwrkGys.getYfje().add(wfkje));
					tRkfk.setYfje(tRkfk.getYfje().subtract(wfkje));
					tFkRk.setFkje(wfkje);
					fkRkDao.save(tFkRk);
					break;
				}
				tYwrkGys.setFkje(tYwrkGys.getFkje().add(tRkfk.getYfje()));
				tYwrkGys.setYfje(tYwrkGys.getYfje().add(tRkfk.getYfje()));
				tRkfk.setIsYf("0");
				tFkRk.setFkje(tRkfk.getYfje());
				wfkje = wfkje.subtract(tRkfk.getYfje());
				tRkfk.setYfje(BigDecimal.ZERO);
				fkRkDao.save(tFkRk);
			}
		}
		ywrkGysDao.save(tYwrkGys);
	}

	public void saveRkfk(Rkfk rkfk) {
		TRkfk tRkfk = new TRkfk();
		BeanUtils.copyProperties(rkfk, tRkfk);
		String lsh = LshServiceImpl.updateLsh(rkfk.getBmbh(), Constant.YWLX_RKFK, lshDao);
		tRkfk.setRkfklsh(lsh);

		// 处理是否有未付款的业务入库，进行付款
		Ywrk ywrk = new Ywrk();
		ywrk.setBmbh(rkfk.getBmbh());
		ywrk.setGysbh(rkfk.getGysbh());
		List<Ywrk> ywrkList =  listYwrkNoFk(ywrk);
		BigDecimal fkje = rkfk.getFkje();
		if (ywrkList != null && ywrkList.size() > 0) {
			TFkRk tFkRk = null;
			for (Ywrk y : ywrkList) {
				BigDecimal hjje = y.getHjje().subtract(y.getFkedje());
				tFkRk = new TFkRk();
				tFkRk.setYwrkId(rkfk.getYwrkId());
				tFkRk.setYwrklsh(y.getYwrklsh());
				tFkRk.setRkfklsh(lsh);
				tFkRk.setIsYf("0");
				tFkRk.setDeleted(0);
				TYwrkGys tYwrkGys = ywrkGysDao.load(TYwrkGys.class, y.getYwrkId());
				if (fkje.compareTo(hjje) > 0) {
					tYwrkGys.setFkje(tYwrkGys.getFkje().add(hjje));
					tFkRk.setFkje(hjje);
					fkRkDao.save(tFkRk);
					fkje = fkje.subtract(hjje);
				} else {
					tYwrkGys.setFkje(tYwrkGys.getFkje().add(fkje));
					tFkRk.setFkje(fkje);
					fkRkDao.save(tFkRk);
					fkje = fkje.subtract(hjje);
					break;
				}
			}
		}
		if (fkje.compareTo(BigDecimal.ZERO) > 0) {
			tRkfk.setYfje(fkje);
			tRkfk.setIsYf("1");
		}

		rkfkDao.save(tRkfk);
	}

	@Override
	public List<Ywrk> listYwrkNoFk(Ywrk ywrk) {
//		String hql = "from TYwrk where isCj = '0' and rklxId = :rklxId and bmbh = :bmbh and gysbh = :gysbh and (hjje + hjse) <> fkje order by fpDate";
		String sql = "select fpDate, rk.ywrklsh, rkgys.id, rkgys.hjje, rkgys.fkje, rk.bz" +
				" from t_ywrk rk" + " left join t_ywrk_gys rkgys on rk.ywrklsh = rkgys.ywrklsh" +
				" where rklxId = ? and isCj = '0' and rkgys.id is not null and rkgys.hjje <> rkgys.fkje and bmbh = ? and rkgys.gysbh = ?" +
				" order by fpDate";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", Constant.RKLX_ZS);
		params.put("1", ywrk.getBmbh());
		params.put("2", ywrk.getGysbh());
		List<Object[]> list = ywrkDao.findBySQL(sql, params);

		if (list == null || list.size() == 0) {
			return new ArrayList<>();
		}

		List<Ywrk> ywrks = new ArrayList<Ywrk>();
		Ywrk y = null;
		for (Object[] o : list) {
			y = new Ywrk();
			y.setCreateTime(DateUtil.stringToDate(o[0].toString()));
//			y.setLsh(o[1].toString());
			y.setYwrklsh(o[1].toString());
			y.setYwrkId(Long.parseLong(o[2].toString()));
			y.setHjje(new BigDecimal(o[3].toString()));
			y.setFkedje(new BigDecimal(o[4].toString()));
			y.setBz(o[5].toString());
			ywrks.add(y);
		}

		return ywrks;
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
	public void setYfzzDao(BaseDaoI<TYfzz> yfzzDao) {
		this.yfzzDao = yfzzDao;
	}

	@Autowired
	public void setRkfkDao(BaseDaoI<TRkfk> rkfkDao) {
		this.rkfkDao = rkfkDao;
	}

	@Autowired
	public void setFkRkDao(BaseDaoI<TFkRk> fkRkDao) {
		this.fkRkDao = fkRkDao;
	}

	@Autowired
	public void setLshDao(BaseDaoI<TLsh> lshDao) {
		this.lshDao = lshDao;
	}
}
