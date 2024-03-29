package lnyswz.jxc.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lnyswz.common.bean.Json;
import lnyswz.jxc.model.*;
import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.Common;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Ck;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.Hw;
import lnyswz.jxc.bean.Kfrk;
import lnyswz.jxc.bean.KfrkDet;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.service.KfrkServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Util;

/**
 * 采购需求实现类
 * @author 王文阳
 *
 */
@Service("kfrkService")
public class KfrkServiceImpl implements KfrkServiceI {
	private Logger logger = Logger.getLogger(KfrkServiceImpl.class);
	private BaseDaoI<TKfrk> kfrkDao;
	private BaseDaoI<TKfrkDet> detDao;
	private BaseDaoI<TKfzz> kfzzDao;
	private BaseDaoI<TCgjhDet> cgjhDetDao;
	private BaseDaoI<TYwrk> ywrkDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TSp> spDao;
	private BaseDaoI<THw> hwDao;
	private BaseDaoI<TSpBgy> spBgyDao;
	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public Kfrk save(Kfrk kfrk) {
		String lsh = LshServiceImpl.updateLsh(kfrk.getBmbh(), kfrk.getLxbh(), lshDao);
		TKfrk tKfrk = new TKfrk();
		BeanUtils.copyProperties(kfrk, tKfrk);
		tKfrk.setCreateTime(new Date());
		tKfrk.setKfrklsh(lsh);
		tKfrk.setBmmc(depDao.load(TDepartment.class, kfrk.getBmbh()).getDepName());
		
		tKfrk.setIsCj("0");
		
		//如果从计划生成的入库，进行关联
		String cgjhDetIds = kfrk.getCgjhDetIds();
		if(cgjhDetIds != null && cgjhDetIds.trim().length() > 0){
			Set<TCgjhDet> tCgjhs = new HashSet<TCgjhDet>();
			
			for(String detId : cgjhDetIds.split(",")){
				TCgjhDet tCgjhDet = cgjhDetDao.load(TCgjhDet.class, Integer.valueOf(detId));
				tCgjhs.add(tCgjhDet);
			}
			tKfrk.setTCgjhs(tCgjhs);
		}
		
		//如果从业务入库生成的入库，进行关联
		if(kfrk.getYwrklsh() != null && kfrk.getYwrklsh().trim().length() > 0){
			TYwrk tYwrk = ywrkDao.load(TYwrk.class, kfrk.getYwrklsh());
			tKfrk.setTYwrk(tYwrk);
		}
		
		//处理商品明细
		Set<TKfrkDet> tDets = new HashSet<TKfrkDet>();
		ArrayList<KfrkDet> kfrkDets = JSON.parseObject(kfrk.getDatagrid(), new TypeReference<ArrayList<KfrkDet>>(){});
		for(KfrkDet kfrkDet : kfrkDets){
			TKfrkDet tDet = new TKfrkDet();
			BeanUtils.copyProperties(kfrkDet, tDet);
			tDet.setHwmc(hwDao.load(THw.class, kfrkDet.getHwId()).getHwmc());
			
			if("".equals(kfrkDet.getCjldwId()) || kfrkDet.getZhxs() == null || kfrkDet.getZhxs().compareTo(Constant.BD_ZERO) == 0){
				tDet.setCdwsl(Constant.BD_ZERO);
				tDet.setZhxs(Constant.BD_ZERO);
				kfrkDet.setZhxs(Constant.BD_ZERO);
			}
			if(null == kfrkDet.getBzsl()){
				tDet.setBzsl(Constant.BD_ZERO);
			}

			tDet.setTKfrk(tKfrk);
			tDets.add(tDet);

			Sp sp = new Sp();
			BeanUtils.copyProperties(kfrkDet, sp);
			Department dep = new Department();
			dep.setId(kfrk.getBmbh());
			dep.setDepName(tKfrk.getBmmc());
			Ck ck = new Ck();
			ck.setId(kfrk.getCkId());
			ck.setCkmc(tKfrk.getCkmc());
			Hw hw = new Hw();
			hw.setId(kfrkDet.getHwId());
			hw.setHwmc(tDet.getHwmc());
			//更新库房总账
			KfzzServiceImpl.updateKfzzSl(sp, dep, ck, hw, kfrkDet.getSppc(), kfrkDet.getZdwsl(), tDet.getCdwsl(), Constant.UPDATE_RK, kfzzDao);
		}
		tKfrk.setTKfrkDets(tDets);
		kfrkDao.save(tKfrk);		
		OperalogServiceImpl.addOperalog(kfrk.getCreateId(), kfrk.getBmbh(), kfrk.getMenuId(), tKfrk.getKfrklsh(), "生成库房入库单", operalogDao);
		
		Kfrk rKfrk = new Kfrk();
		rKfrk.setKfrklsh(lsh);
		return rKfrk;
		
	}
	
	@Override
	public Json cjKfrk(Kfrk kfrk) {
		Json j = new Json();
		//获取原单据信息
		TKfrk yTKfrk = kfrkDao.get(TKfrk.class, kfrk.getKfrklsh());
		if("1".equals(yTKfrk.getIsCj())) {
            j.setMsg("此单据已取消，请刷新后重新操作！");
            return j;
        }

        Date now = new Date();

        //新增冲减单据信息
        TKfrk tKfrk = new TKfrk();
        BeanUtils.copyProperties(yTKfrk, tKfrk, new String[]{"TCgjhs", "TYwrk"});
        //更新原单据冲减信息
        yTKfrk.setCjId(kfrk.getCjId());
        yTKfrk.setCjTime(now);
        yTKfrk.setCjName(kfrk.getCjName());
        yTKfrk.setIsCj("1");

        if (yTKfrk.getTCgjhs() != null) {

//			Set<TCgjhDet> tcs = new HashSet<TCgjhDet>();
//			for(TCgjhDet tc : yTKfrk.getTCgjhs()){
//				TCgjhDet ntc = cgjhDetDao.load(TCgjhDet.class, tc.getId());
//				tcs.add(ntc);
//			}
//			tKfrk.setTCgjhs(tcs);
            yTKfrk.setTCgjhs(null);
        }

        if (yTKfrk.getTYwrk() != null) {
            yTKfrk.setTYwrk(null);
        }

        //更新新单据信息
        String lsh = LshServiceImpl.updateLsh(kfrk.getBmbh(), kfrk.getLxbh(), lshDao);
        tKfrk.setKfrklsh(lsh);
        tKfrk.setCreateTime(now);
        tKfrk.setCreateId(kfrk.getCjId());
        tKfrk.setCreateName(kfrk.getCjName());
        tKfrk.setIsCj("1");
        tKfrk.setCjTime(new Date());
        tKfrk.setCjId(kfrk.getCjId());
        tKfrk.setCjName(kfrk.getCjName());
        tKfrk.setCjKfrklsh(yTKfrk.getKfrklsh());
        tKfrk.setBz(kfrk.getBz());

        Department dep = new Department();
        dep.setId(tKfrk.getBmbh());
        dep.setDepName(tKfrk.getBmmc());
        Ck ck = new Ck();
        ck.setId(tKfrk.getCkId());
        ck.setCkmc(tKfrk.getCkmc());

        Set<TKfrkDet> yTKfrkDets = yTKfrk.getTKfrkDets();
        Set<TKfrkDet> tDets = new HashSet<TKfrkDet>();
        for (TKfrkDet yTDet : yTKfrkDets) {
            TKfrkDet tDet = new TKfrkDet();
            BeanUtils.copyProperties(yTDet, tDet, new String[]{"id"});
            tDet.setZdwsl(yTDet.getZdwsl().negate());
            if (yTDet.getCdwsl() != null) {
                tDet.setCdwsl(yTDet.getCdwsl().negate());
            }

            if (!("05".equals(tKfrk.getBmbh()) && "8".equals(tDet.getSpbh().substring(0, 1)))) {
                if (!Constant.SPPC.equals(tDet.getSppc())) {
                    tDet.setSppc(Constant.SPPC);
                }
            }

            tDet.setTKfrk(tKfrk);
            tDets.add(tDet);

            Sp sp = new Sp();
            BeanUtils.copyProperties(tDet, sp);
            Hw hw = new Hw();
            hw.setId(tDet.getHwId());
            hw.setHwmc(tDet.getHwmc());

            //更新库房总账
            KfzzServiceImpl.updateKfzzSl(sp, dep, ck, hw, tDet.getSppc(), tDet.getZdwsl(), tDet.getCdwsl(), Constant.UPDATE_RK, kfzzDao);
        }
        tKfrk.setTKfrkDets(tDets);
        kfrkDao.save(tKfrk);
        OperalogServiceImpl.addOperalog(kfrk.getCjId(), kfrk.getBmbh(), kfrk.getMenuId(), tKfrk.getCjKfrklsh() + "/" + tKfrk.getKfrklsh(), "冲减库房入库单", operalogDao);

        j.setSuccess(true);
        j.setMsg("库房入库取消成功！");
		return j;
	}
	
	@Override
	public DataGrid printKfrk(Kfrk kfrk) {
		DataGrid datagrid = new DataGrid();
		TKfrk tKfrk = kfrkDao.load(TKfrk.class, kfrk.getKfrklsh());
		
		List<KfrkDet> nl = new ArrayList<KfrkDet>();
		BigDecimal hj = Constant.BD_ZERO;
		for (TKfrkDet yd : tKfrk.getTKfrkDets()) {
			KfrkDet kfrkDet = new KfrkDet();
			BeanUtils.copyProperties(yd, kfrkDet);
			nl.add(kfrkDet);
			hj = hj.add(yd.getCdwsl());
		}
		int num = nl.size();
		if (num < Constant.REPORT_NUMBER) {
			for (int i = 0; i < (Constant.REPORT_NUMBER - num); i++) {
				nl.add(new KfrkDet());
			}
		}
		//Kfrk kfrk = new Kfrk();
		//BeanUtils.copyProperties(yk, kfrk);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "库   房   入   库   单");
		map.put("kfrklsh", kfrk.getKfrklsh());
		map.put("bmmc", tKfrk.getBmmc());
		map.put("printName", kfrk.getCreateName());
		map.put("createTime", DateUtil.dateToString(tKfrk.getCreateTime(), DateUtil.DATETIME_NOSECOND_PATTERN));
		map.put("printTime", DateUtil.dateToString(new Date()));
		map.put("gysbh", tKfrk.getGysbh());
		map.put("gysmc", tKfrk.getGysmc());
		map.put("ckmc", tKfrk.getCkmc());
		map.put("hj", hj);
		map.put("bz", tKfrk.getBz());
		
		datagrid.setObj(map);
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid datagrid(Kfrk kfrk) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TKfrk t where t.bmbh = :bmbh and t.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", kfrk.getBmbh());
		if(kfrk.getCreateTime() != null){
			params.put("createTime", kfrk.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}

		if(kfrk.getSearch() != null){
			//hql += " and (t.kfrklsh like :search or t.gysmc like :search or t.bz like :search)"; 
			//params.put("search", "%" + kfrk.getSearch() + "%");
			hql += " and (" + 
					Util.getQueryWhere(kfrk.getSearch(), new String[]{"t.kfrklsh", "t.gysmc", "t.bz"}, params)
					+ ")";
		}
		if(kfrk.getFromOther() != null) {
//			hql += " and t.createId = :createId";
//			params.put("createId", kfrk.getCreateId());
//		}else{
			if(kfrk.getBmbh().equals("05")) {
				String ckSql = "select cks from v_zy_cks where createId = ?";
				Map<String, Object> ckParams = new HashMap<String, Object>();
				ckParams.put("0", kfrk.getCreateId());
				Object cks = kfrkDao.getBySQL(ckSql, ckParams);

				if(cks != null){
					hql += " and t.ckId in " + cks.toString();
				}
			}
			hql += " and t.isCj = '0' and t.TYwrk = null";
		}
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TKfrk> l = kfrkDao.find(hql, params, kfrk.getPage(), kfrk.getRows());
		List<Kfrk> nl = new ArrayList<Kfrk>();
		for(TKfrk t : l){
			Kfrk c = new Kfrk();
			BeanUtils.copyProperties(t, c);
			//采购计划处理
			Set<TCgjhDet> tCgjhs = t.getTCgjhs();
			if(tCgjhs != null && tCgjhs.size() > 0){
				String cgjhlshs = "";
				for(TCgjhDet tc : tCgjhs){
					cgjhlshs = Common.joinString(cgjhlshs, tc.getTCgjh().getCgjhlsh(), ",");
//					if(cgjhlshs.indexOf(tc.getTCgjh().getCgjhlsh()) < 0){
//						if(cgjhlshs.length() > 0){
//							cgjhlshs += ",";
//						}
//						cgjhlshs += tc.getTCgjh().getCgjhlsh();
//					}
				}
				
				c.setCgjhlshs(cgjhlshs);
			}
			if(t.getTYwrk() != null){
				c.setYwrklsh(t.getTYwrk().getYwrklsh());
			}
			nl.add(c);
		}
		datagrid.setTotal(kfrkDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(String kfrklsh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TKfrkDet t where t.TKfrk.kfrklsh = :kfrklsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("kfrklsh", kfrklsh);
		List<TKfrkDet> l = detDao.find(hql, params);
		List<KfrkDet> nl = new ArrayList<KfrkDet>();
		for(TKfrkDet t : l){
			KfrkDet c = new KfrkDet();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid toYwrk(String kfrklshs){
		String sql = "select t.spbh, sum(t.zdwsl) zdwsl, isnull(MAX(ck.zdwdj), 0) zdwdj, sum(t.cdwsl) cdwsl, isnull(MAX(ck.cdwdj), 0) cdwdj from t_kfrk_det t"
				+ " left join (select ck.kfrklsh, jh.spbh, jh.zdwdj, jh.cdwdj from t_cgjh_kfrk ck left join t_cgjh_det jh on ck.cgjhdetId = jh.id) ck on t.kfrklsh = ck.kfrklsh and t.spbh = ck.spbh";
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(kfrklshs != null && kfrklshs.trim().length() > 0){
			String[] ks = kfrklshs.split(",");
			sql += " where ";
			for(int i = 0; i < ks.length; i++){
				sql += "t.kfrklsh = ? ";
				params.put(String.valueOf(i), ks[i]);
				if(i != ks.length - 1){
					sql += " or ";
				}
 			}
			
		}
		sql += " group by t.spbh";
		
		List<Object[]> l = detDao.findBySQL(sql, params);
		List<KfrkDet> nl = new ArrayList<KfrkDet>();
		
		for(Object[] os : l){
			String spbh = (String)os[0];
			BigDecimal zdwsl = new BigDecimal(os[1].toString());
			BigDecimal zdwdj = new BigDecimal(os[2].toString());
			BigDecimal cdwsl = new BigDecimal(os[3].toString());
			BigDecimal cdwdj = new BigDecimal(os[4].toString());
			
			TSp sp = spDao.get(TSp.class, spbh);
			KfrkDet kd = new KfrkDet();
			BeanUtils.copyProperties(sp, kd);
			kd.setZjldwId(sp.getZjldw().getId());
			kd.setZjldwmc(sp.getZjldw().getJldwmc());
			kd.setZdwsl(zdwsl);
			kd.setZdwdj(zdwdj);
			if(sp.getCjldw() != null){
				kd.setCjldwId(sp.getCjldw().getId());
				kd.setCjldwmc(sp.getCjldw().getJldwmc());
				kd.setZhxs(sp.getZhxs());
				kd.setCdwsl(cdwsl);
				kd.setCdwdj(cdwdj);
				if(sp.getZhxs().compareTo(Constant.BD_ZERO) != 0){
					//kd.setCdwsl(zdwsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
					//kd.setCdwdj(zdwdj.multiply(sp.getZhxs()).multiply(new BigDecimal(1).add(Constant.SHUILV)).setScale(2, BigDecimal.ROUND_HALF_UP)	);
				}else{
					//kd.setCdwsl(Constant.BD_ZERO);
					//kd.setCdwdj(Constant.BD_ZERO);
				}
			}
			nl.add(kd);
		}
		nl.add(new KfrkDet());
		DataGrid dg = new DataGrid();
		dg.setRows(nl);
		return dg;
	}

	@Override
	public DataGrid loadKfrk(Kfrk kfrk) {
		DataGrid datagrid = new DataGrid();
		try {
            TKfrk tKfrk = kfrkDao.load(TKfrk.class, kfrk.getKfrklsh());
            if ("1".equals(tKfrk.getIsCj())) {
                datagrid.setMsg("对应的库房入库已冲减！请重新录入！");
            } else {
                kfrk.setGysbh(tKfrk.getGysbh());
                kfrk.setGysmc(tKfrk.getGysmc());
                kfrk.setBz(tKfrk.getBz());

                List<KfrkDet> nl = new ArrayList<KfrkDet>();
                KfrkDet kfrkDet;
                String hql = "from TSpBgy t where t.depId = :bmbh and t.bgyId = :bgyId and spbh = :spbh";
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("bmbh", kfrk.getKfrklsh().substring(4, 6));
                params.put("bgyId", kfrk.getCreateId());
                for (TKfrkDet yd : tKfrk.getTKfrkDets()) {
                    params.put("spbh", yd.getSpbh());
                    if (spBgyDao.get(hql, params) != null) {
                        kfrkDet = new KfrkDet();
                        BeanUtils.copyProperties(yd, kfrkDet);
                        kfrkDet.setZdwrksl(yd.getZdwsl());
                        nl.add(kfrkDet);
                    }
                }

                if (nl.size() > 0) {
                    datagrid.setObj(kfrk);
                    datagrid.setRows(nl);
                } else {
                    datagrid.setMsg("录入的库房入库单不包含此保管员管理品种，请重新录入！");
                }
            }
        }catch (ObjectNotFoundException e) {
            datagrid.setMsg("对应的库房入库单据不存在！");
        }
		return datagrid;
	}

	@Autowired
	public void setKfrkDao(BaseDaoI<TKfrk> kfrkDao) {
		this.kfrkDao = kfrkDao;
	}

	@Autowired
	public void setKfzzDao(BaseDaoI<TKfzz> kfzzDao) {
		this.kfzzDao = kfzzDao;
	}

	@Autowired
	public void setDetDao(BaseDaoI<TKfrkDet> detDao) {
		this.detDao = detDao;
	}

	@Autowired
	public void setCgjhDetDao(BaseDaoI<TCgjhDet> cgjhDetDao) {
		this.cgjhDetDao = cgjhDetDao;
	}

	@Autowired
	public void setYwrkDao(BaseDaoI<TYwrk> ywrkDao) {
		this.ywrkDao = ywrkDao;
	}

	@Autowired
	public void setLshDao(BaseDaoI<TLsh> lshDao) {
		this.lshDao = lshDao;
	}

	@Autowired
	public void setDepDao(BaseDaoI<TDepartment> depDao) {
		this.depDao = depDao;
	}

	@Autowired
	public void setSpDao(BaseDaoI<TSp> spDao) {
		this.spDao = spDao;
	}

	@Autowired
	public void setHwDao(BaseDaoI<THw> hwDao) {
		this.hwDao = hwDao;
	}

    @Autowired
    public void setSpBgyDao(BaseDaoI<TSpBgy> spBgyDao) {
        this.spBgyDao = spBgyDao;
    }

    @Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}

}
