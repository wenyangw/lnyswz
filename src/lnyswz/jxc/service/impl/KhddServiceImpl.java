package lnyswz.jxc.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Khdd;
import lnyswz.jxc.bean.KhddDet;
import lnyswz.jxc.model.*;
import lnyswz.jxc.service.KhddServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 客户订单实现类
 * @author 王文阳
 * 2020.04.23 新建
 *
 */
@Service("khddService")
public class KhddServiceImpl implements KhddServiceI {
	private BaseDaoI<TKhdd> khddDao;
	private BaseDaoI<TKhddDet> detDao;
	private BaseDaoI<TLsh> lshDao;
    private BaseDaoI<TKhUser> khUserDao;
	private BaseDaoI<TUser> ywyDao;
	private BaseDaoI<TKh> khDao;
	private BaseDaoI<TOperalog> operalogDao;
	
	@Override
	public Khdd saveKhdd(Khdd khdd) {
		TKhdd tKhdd = new TKhdd();
		BeanUtils.copyProperties(khdd, tKhdd);
		tKhdd.setCreateTime(new Date());

		TKhUser tKhUser = KhUserServiceImpl.getKhUserByOpenId(khdd.getOpenId(), khUserDao);

        tKhdd.setCreateId(tKhUser.getId());
		tKhdd.setCreateName(tKhUser.getRealName());
		if (tKhUser.getKhbh() != null) {
			TKh tKh = khDao.load(TKh.class, tKhUser.getKhbh());
			tKhdd.setKhbh(tKh.getKhbh());
			tKhdd.setKhmc(tKh.getKhmc());
		} else {
			tKhdd.setKhmc(tKhUser.getDwmc());
		}

		TUser tYwy = ywyDao.load(TUser.class, tKhUser.getYwyId());
		tKhdd.setYwyId(tYwy.getId());
        tKhdd.setYwymc(tYwy.getRealName());
		tKhdd.setBmbh(tYwy.getTDepartment().getId());
		tKhdd.setBmmc(tYwy.getTDepartment().getDepName());
		
		tKhdd.setIsCancel("0");
		tKhdd.setIsRefuse("0");

        String lsh = LshServiceImpl.updateLsh(tKhdd.getBmbh(), Constant.YWLX_KHDD, lshDao);
        tKhdd.setKhddlsh(lsh);

		//处理商品明细
		Set<TKhddDet> tDets = new HashSet<TKhddDet>();
		ArrayList<KhddDet> khddDets = JSON.parseObject(khdd.getDatagrid(), new TypeReference<ArrayList<KhddDet>>(){});
		TKhddDet tDet = null;
		for(KhddDet khddDet : khddDets){
			if(khddDet.getSpbh() != null) {
				tDet = new TKhddDet();
				BeanUtils.copyProperties(khddDet, tDet, new String[]{"id"});

				tDet.setTKhdd(tKhdd);
				tDets.add(tDet);
			}
		}
		tKhdd.setTKhddDets(tDets);
		khddDao.save(tKhdd);		
		OperalogServiceImpl.addOperalog(tKhdd.getCreateId(), tKhdd.getBmbh(), Constant.MENU_KHDD, tKhdd.getKhddlsh(), "保存客户订单", operalogDao);

//		khdd.setKhddlsh(lsh);
		return khdd;
	}

	@Override
	public Khdd cancelKhdd(Khdd khdd) {
		TKhUser tKhUser = KhUserServiceImpl.getKhUserByOpenId(khdd.getOpenId(), khUserDao);

		//获取原单据信息
		TKhdd tKhdd = khddDao.get(TKhdd.class, khdd.getKhddlsh());
		if(tKhdd.getIsCancel().equals("0") && tKhdd.getIsCancel().equals("0") && (tKhdd.getXsthlsh() == null) && tKhdd.getIsHandle().equals("0")) {
			//更新原单据冲减信息
			tKhdd.setCancelId(tKhUser.getId());
			tKhdd.setCancelTime(new Date());
			tKhdd.setCancelName(tKhUser.getRealName());
			tKhdd.setIsCancel("1");
			khddDao.update(tKhdd);
			BeanUtils.copyProperties(tKhdd, khdd);
			khdd.setStatus(getStatus(tKhdd));
			OperalogServiceImpl.addOperalog(tKhdd.getCancelId(), tKhdd.getBmbh(), Constant.MENU_KHDD, tKhdd.getKhddlsh(), "取消客户订单", operalogDao);
			return khdd;
		}
		return null;
	}

	@Override
	public void refuseKhdd(Khdd khdd) {
		Date now = new Date();
		//获取原单据信息
		TKhdd tKhdd = khddDao.get(TKhdd.class, khdd.getKhddlsh());

		//更新原单据冲减信息
		tKhdd.setRefuseId(khdd.getRefuseId());
		tKhdd.setRefuseTime(now);
		tKhdd.setRefuseName(khdd.getRefuseName());
		tKhdd.setIsRefuse("1");

		OperalogServiceImpl.addOperalog(khdd.getRefuseId(), tKhdd.getBmbh(), Constant.MENU_KHDD, tKhdd.getKhddlsh(), "退回客户订单", operalogDao);
	}

	@Override
	public Khdd getKhdd(Khdd khdd) {
		return null;
	}

	@Override
	public DataGrid getKhdds(Khdd khdd) {
		TKhUser tKhUser = KhUserServiceImpl.getKhUserByOpenId(khdd.getOpenId(), khUserDao);

		String sql = "select distinct khddlsh from v_khdd where createTime > ? and (khbh = ? or createId = ?)";
		Map<String, Object> params = new HashMap<String, Object>();
		if(khdd.getCreateTime() != null){
			params.put("0", khdd.getCreateTime());
		}else{
			params.put("0", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		params.put("1",tKhUser.getKhbh());
		params.put("2",tKhUser.getId());
		if(khdd.getSearch() != null){
			sql += " and (" +
					Util.getQuerySQLWhere(khdd.getSearch(), new String[]{"khddlsh", "bz", "spmc"}, params, 0)
					+ ")";
		}
		List<Object[]> lb  = khddDao.findBySQL(sql, params);
		if(lb != null){
			return getKhddsByLsh(khdd, lb, true);
		}
		return null;
	}

	@Override
	public DataGrid getKhddsByYwy(Khdd khdd) {
		String sql = "select distinct khddlsh from v_khdd where ywyId = ?";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0",khdd.getYwyId());
		if (khdd.getWhere() != null) {
			sql += " and " + khdd.getWhere();
		}
		if(khdd.getSearch() != null){
			sql += " and (" +
					Util.getQuerySQLWhere(khdd.getSearch(), new String[]{"khddlsh", "khmc", "bz", "spmc"}, params, 0)
					+ ")";
		}
		List<Object[]> lb  = khddDao.findBySQL(sql, params);
		if(lb != null){
			return getKhddsByLsh(khdd, lb, false);
		}
		return null;
	}

	private DataGrid getKhddsByLsh(Khdd khdd, List<Object[]> lb, Boolean showDets) {
		DataGrid datagrid = new DataGrid();
		String lsh= "(" + StringUtils.join(lb,",") + ")";
		String hql = " from TKhdd t where khddlsh in " + lsh;
		List<TKhdd> l = khddDao.find(hql, khdd.getPage(), khdd.getRows());
		List<Khdd> nl = new ArrayList<Khdd>();
		Khdd c;
		for(TKhdd t : l){
			c = new Khdd();
			BeanUtils.copyProperties(t, c);
			c.setStatus(getStatus(t));
			if (showDets == true) {
                c.setKhddDets(getKhddDetBeans(t.getTKhddDets(), false));
            }

			nl.add(c);
		}
		String totalHql = "select count(*) from t_khdd t where khddlsh in " + lsh;
		datagrid.setTotal(khddDao.countSQL(totalHql));
		datagrid.setRows(nl);
		return datagrid;
	}

	@Override
	public List<KhddDet> getKhddDet(Khdd khdd) {
		String hql = "from TKhddDet t where t.TKhdd.khddlsh = :khddlsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("khddlsh", khdd.getKhddlsh());
		List<TKhddDet> tKhddDets = detDao.find(hql, params);
		return getKhddDetBeans(tKhddDets, true);
	}

	private List<KhddDet> getKhddDetBeans(Collection<TKhddDet> tKhddDets, Boolean showMore) {
        List<KhddDet> nl = new ArrayList<KhddDet>();
        KhddDet c = null;
        for(TKhddDet t : tKhddDets){
            c = new KhddDet();
            BeanUtils.copyProperties(t, c);
            if (showMore) {
                String sql = "select cjldwId, cjldwmc, zhxs, kcsl, dwcb, isnull(xsdj, 0) xsdj, isnull(specXsdj, 0) specXsdj" +
                 " from v_kc_sj_ck t left join t_sp_det d on t.bmbh = d.depId and t.spbh = d.spbh" +
                 " where t.bmbh = '01' and t.ckId = '02' and t.spbh = ?";
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("0", t.getSpbh());
                Object[] o = khddDao.getMBySQL(sql, params);
                if (o != null) {
                    c.setCjldwId(o[0].toString());
                    c.setCjldwmc(o[1].toString());
                    c.setZhxs(new BigDecimal(o[2].toString()));
                    c.setKcsl(new BigDecimal(o[3].toString()));
                    c.setDwcb(new BigDecimal(o[4].toString()));
                    c.setXsdj(new BigDecimal(o[5].toString()));
                    c.setSpecXsdj(new BigDecimal(o[6].toString()));
                } else {
                    c.setKcsl(BigDecimal.ZERO);
                    c.setDwcb(BigDecimal.ZERO);
                    c.setXsdj(BigDecimal.ZERO);
                    c.setSpecXsdj(BigDecimal.ZERO);
                }
            }
            nl.add(c);
        }
        return nl;
    }

    public String getStatus(TKhdd t){
		if (t.getIsCancel().equals("1")) {
			return "已取消";
		} else if (t.getIsRefuse().equals("1")) {
			return "已退回";
		} else if (t.getXsthlsh() != null) {
			return "已发货";
		} else if (t.getIsHandle().equals("1")) {
			return "已处理";
		} else {
			return "等待处理";
		}
	}

	@Autowired
	public void setKhddDao(BaseDaoI<TKhdd> khddDao) {
		this.khddDao = khddDao;
	}

	@Autowired
	public void setDetDao(BaseDaoI<TKhddDet> detDao) {
		this.detDao = detDao;
	}

	@Autowired
	public void setLshDao(BaseDaoI<TLsh> lshDao) {
		this.lshDao = lshDao;
	}

    @Autowired
    public void setYwyDao(BaseDaoI<TUser> ywyDao) {
        this.ywyDao = ywyDao;
    }

	@Autowired
	public void setKhDao(BaseDaoI<TKh> khDao) {
		this.khDao = khDao;
	}

    @Autowired
    public void setKhUserDao(BaseDaoI<TKhUser> khUserDao) {
        this.khUserDao = khUserDao;
    }

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}

}
