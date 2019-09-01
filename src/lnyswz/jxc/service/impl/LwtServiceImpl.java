package lnyswz.jxc.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lnyswz.jxc.model.TXsth;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Lwt;
import lnyswz.jxc.service.LwtServiceI;

@Service("lwtService")
public class LwtServiceImpl implements LwtServiceI {
	private BaseDaoI<Object> lwtDao;
	private BaseDaoI<TXsth> xsthDao;
	
	@Override
	public DataGrid listKhByYwy(Lwt lwt) {
		DataGrid dg = new DataGrid();
		//execute m_kh '部门ide','ywyId'
		String execHql = "execute m_ywy_kh '" + lwt.getBmbh() + "','" + lwt.getUserId() + "','" + lwt.getSearch().trim() + "','" + lwt.getCount() + "','"+ lwt.getPage() + "','" + lwt.getRows() + "'";
		List<Lwt> l = new ArrayList<Lwt>();
		List<Object[]> list = lwtDao.findBySQL(execHql);
		for(Object[] o : list){
			Lwt k = getKhByYwy(o);
			l.add(k);
		}
		String countexecHql = "execute m_ywy_kh '" + lwt.getBmbh() + "','" + lwt.getUserId() + "','" + lwt.getSearch().trim() + "','" + lwt.getCount() +  "','0','0'";
		dg.setTotal(lwtDao.countSQL(countexecHql));
		dg.setRows(l);
		return dg;
	}
	
	@Override
	public DataGrid listKhByYwyXsth(Lwt lwt) {
		// TODO Auto-generated method stub
		DataGrid dg = new DataGrid();
		String view = "";
		if(lwt.getType().equals("xsth")){
			view = "m_ywy_kh_xsth";
		}else if(lwt.getType().equals("needAudit_xsth")){
			view = "m_ywy_kh_needAudit_xsth";
		}else if(lwt.getType().equals("xskp_w_hk")){
			view = "m_xskp_w_hk_ywy_kh";
		}else if(lwt.getType().equals("xsth_w_kp")){
			view = "m_xsth_w_xskp_ywy_kh";
		}
		String execHql = "execute " + view + "'" + lwt.getBmbh() + "','" + lwt.getUserId() + "','" + lwt.getKhbh() + "','" + lwt.getPage() + "','" + lwt.getRows() + "'";
		List<Lwt> l = new ArrayList<Lwt>();
		List<Object[]> list = lwtDao.findBySQL(execHql);
		for(Object[] o : list){
			Lwt x = getKhByYwyXsth(o,lwt.getType());
			l.add(x);
		}
		String countexecHql = "execute " + view + "'" + lwt.getBmbh() + "','" + lwt.getUserId() + "','" + lwt.getKhbh() + "','0','0'";
		dg.setTotal(lwtDao.countSQL(countexecHql));
		dg.setRows(l);
		return dg;
	}
	
	

	
	@Override
	public DataGrid listXstjByYears(Lwt lwt) {
		DataGrid dg =  new DataGrid();
		String execHql = "execute m_xstj '" + lwt.getBmbh() + "','" + lwt.getUserId() + "','" + lwt.getKhbh()+ "','" + lwt.getYear() + "','" + lwt.getMonth() + "'," + lwt.getCount(); ;
		List<Lwt> l = new ArrayList<Lwt>();
		List<Object[]> list = lwtDao.findBySQL(execHql);
		if(list.size() > 0){
			for(Object[] o : list){
				Lwt x = getXstj(o);
				l.add(x);
			}
		}
		dg.setRows(l);
		return dg;
	}

	@Override
	public DataGrid dgSpByCk(Lwt lwt) {
		DataGrid dg = new DataGrid();
		String sql = "select t.spbh, spmc, spcd, sppp, spbz, zjldwId, zjldwmc, cjldwId, cjldwmc, zhxs, kcsl, dwcb, isnull(xsdj, 0) xsdj, isnull(specXsdj, 0) specXsdj";
		String from = " from v_kc_sj_ck t left join t_sp_det d on t.bmbh = d.depId and t.spbh = d.spbh" +
				" where t.bmbh = ? and t.ckId = ?";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", lwt.getBmbh());
		params.put("1", lwt.getCkId());

		if (!"1".equals(lwt.getIsKcZero())) {
			from += " and t.kcsl > 0";
		}

		if (lwt.getSearch() != null && lwt.getSearch().length() > 0) {
			from += " and ("  + Util.getQuerySQLWhere(lwt.getSearch(), new String[]{"t.spbh", "t.spmc", "t.spcd", "t.sppp"}, params, 2) + ")";
		}

		List<Lwt> nl = new ArrayList<Lwt>();
		List<Object[]> list = lwtDao.findBySQL(sql + from + " order by t.spbh", params, lwt.getPage(), lwt.getRows());
		Lwt l = null;
		for(Object[] o: list) {
			l = new Lwt();
			l.setSpbh(o[0].toString());
			l.setSpmc(o[1].toString());
			l.setSpcd(o[2].toString());
			l.setSppp(o[3].toString());
			l.setSpbz(o[4].toString());
			l.setZjldwId(o[5].toString());
			l.setZjldwmc(o[6].toString());
			l.setCjldwId(o[7].toString());
			l.setCjldwmc(o[8].toString());
			l.setZhxs(new BigDecimal(o[9].toString()));
			l.setKcsl(new BigDecimal(o[10].toString()));
			if (l.getZhxs().compareTo(BigDecimal.ZERO) != 0) {
				l.setCkcsl(l.getKcsl().divide(l.getZhxs(), 3, BigDecimal.ROUND_HALF_UP));
			}
			l.setDwcb(new BigDecimal(o[11].toString()));
			l.setDwcbs(new BigDecimal(o[11].toString()).multiply(new BigDecimal(1).add(Constant.SHUILV)).setScale(4, BigDecimal.ROUND_HALF_UP));

			if ("01".equals(lwt.getBmbh()) && (lwt.getYwyId() == 115 || lwt.getYwyId() == 193)) {
				l.setXsdj(new BigDecimal(o[13].toString()));
			} else{
				l.setXsdj(new BigDecimal(o[12].toString()).multiply(new BigDecimal(1).add(Constant.SHUILV)).setScale(4, BigDecimal.ROUND_HALF_UP));
			}


			nl.add(l);
		}

		dg.setTotal(lwtDao.countSQL("select count(*)" + from, params));
		dg.setRows(nl);
		return dg;
	}

	@Override
	public DataGrid dgSp(Lwt lwt) {
		DataGrid dg = new DataGrid();
		String sql = "select spbh, spmc, spcd, sppp, spbz, zjldwId, zjldwmc, cjldwId, cjldwmc, zhxs, kcsl, dwcb, xsdj, specXsdj";
		String from = " from dbo.ft_sp_ywzz(?)";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", lwt.getBmbh());

		if (lwt.getSearch() != null && lwt.getSearch().length() > 0) {
			from += " where ("  + Util.getQuerySQLWhere(lwt.getSearch(), new String[]{"spbh", "spmc", "spcd", "sppp"}, params, 1) + ")";
		}

		List<Lwt> nl = new ArrayList<Lwt>();
		List<Object[]> list = lwtDao.findBySQL(sql + from + " order by spbh", params, lwt.getPage(), lwt.getRows());
		Lwt l = null;
		for(Object[] o: list) {
			l = new Lwt();
			l.setSpbh(o[0].toString());
			l.setSpmc(o[1].toString());
			l.setSpcd(o[2].toString());
			l.setSppp(o[3].toString());
			l.setSpbz(o[4].toString());
			l.setZjldwId(o[5].toString());
			l.setZjldwmc(o[6].toString());
			l.setCjldwId(o[7].toString());
			l.setCjldwmc(o[8].toString());
			l.setZhxs(new BigDecimal(o[9].toString()));
			l.setKcsl(new BigDecimal(o[10].toString()));
			l.setDwcb(new BigDecimal(o[11].toString()));
			if ("01".equals(lwt.getBmbh()) && (lwt.getYwyId() == 115 || lwt.getYwyId() == 193)) {
				l.setXsdj(new BigDecimal(o[13].toString()));
			} else{
				l.setXsdj(new BigDecimal(o[12].toString()).multiply(new BigDecimal(1).add(Constant.SHUILV)).setScale(4, BigDecimal.ROUND_HALF_UP));
			}

			nl.add(l);
		}

		dg.setTotal(lwtDao.countSQL("select count(*)" + from, params));
		dg.setRows(nl);
		return dg;
	}

	@Override
	public DataGrid dgKhs(Lwt lwt) {
		DataGrid dg = new DataGrid();
		String sql = "select ky.khbh, kh.khmc, ky.ysje, ky.isLocked, ky.isUp, ky.khlxId, ky.limitJe, ky.limitPer, ky.postpontDay, ky.sxje, ky.sxzq, isnull(ky.cqDays, 0) cqDays, ky.thje";
		String from = " from dbo.ft_kh_ywy(?, ?) ky left join t_kh kh on ky.khbh = kh.khbh ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", lwt.getBmbh());
		params.put("1", lwt.getYwyId());
		if (lwt.getQuery() != null) {
			from += "where kh.khmc like ?";
			params.put("2", "%" + lwt.getQuery() + "%");
		}

		//获得总条数
		Long total = lwtDao.countSQL("select count(*) " + from, params);
		List<Lwt> nl = new ArrayList<Lwt>();
		// 传入页码、每页条数
		List<Object[]> lists = lwtDao.findBySQL(sql + from, params, lwt.getPage(), lwt.getRows());
		// 处理返回信息
		Lwt l = null;
		for (Object[] o : lists) {
			l = new Lwt();
			l.setKhbh(o[0].toString());
			l.setKhmc(o[1].toString());
			l.setYsje(new BigDecimal(o[2].toString()).add(new BigDecimal(o[12].toString())));
			l.setIsLocked(o[3].toString());
			l.setIsUp(o[4].toString());
			l.setKhlxId(o[5].toString());
			l.setLimitJe(new BigDecimal(o[6].toString()));
			l.setLimitPer(new BigDecimal(o[7].toString()));
			l.setPostponeDay((Integer)o[8]);
			l.setSxje(new BigDecimal(o[9].toString()));
			l.setSxzq((Integer)o[10]);
			l.setCqDays((Integer)o[11]);

			nl.add(l);
		}

		dg.setTotal(total);
		dg.setRows(nl);
		return dg;
	}

	@Override
	public DataGrid dgKhsByYwy(Lwt lwt) {
		DataGrid dg = new DataGrid();
		String sql = "select ky.khbh, kh.khmc, ky.ysje, ky.isLocked, ky.isUp, ky.khlxId, ky.limitJe, ky.limitPer, ky.postpontDay, ky.sxje, ky.sxzq, isnull(ky.cqDays, 0) cqDays, ky.thje";
		String from = " from dbo.ft_kh_ywy_year(?, ?, ?) ky left join t_kh kh on ky.khbh = kh.khbh";
//		"where ky.bmbh = ?  and ky.ywyId = ?"
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", lwt.getBmbh());
		params.put("1", lwt.getYwyId());
		params.put("2", lwt.getYear());
		if (lwt.getQuery() != null) {
			from += " where kh.khmc like ?";
			params.put("3", "%" + lwt.getQuery() + "%");
		}

		//获得总条数
		Long total = lwtDao.countSQL("select count(*) " + from, params);
		List<Lwt> nl = new ArrayList<Lwt>();
		// 传入页码、每页条数
		List<Object[]> lists = lwtDao.findBySQL(sql + from + " order by ky.createTime desc" , params, lwt.getPage(), lwt.getRows());
		// 处理返回信息
		Lwt l = null;
		for (Object[] o : lists) {
			l = new Lwt();
			l.setKhbh(o[0].toString());
			l.setKhmc(o[1].toString());
			l.setYsje(new BigDecimal(o[2].toString()).add(new BigDecimal(o[12].toString())));
			l.setIsLocked(o[3].toString());
			l.setIsUp(o[4].toString());
			l.setKhlxId(o[5].toString());
			l.setLimitJe(new BigDecimal(o[6].toString()));
			l.setLimitPer(new BigDecimal(o[7].toString()));
			l.setPostponeDay((Integer)o[8]);
			l.setSxje(new BigDecimal(o[9].toString()));
			l.setSxzq((Integer)o[10]);
			l.setCqDays((Integer)o[11]);

			nl.add(l);
		}

		dg.setTotal(total);
		dg.setRows(nl);
		return dg;
	}

	@Override
	public DataGrid dgKhDets(Lwt lwt) {
		DataGrid dg = new DataGrid();
		StringBuilder sql = new StringBuilder("select id, bmbh, ywyId, khbh, khmc, lxr, khlxId, sxje, sxzq, lsje, isUp, postponeDay, isOther, limitPer, limitJe, isLocked, isDef, info ");
		StringBuilder total = new StringBuilder("select count(*) ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", lwt.getBmbh());
		params.put("1", lwt.getYwyId());
		if (lwt.getQuery() != null && lwt.getQuery().length() > 0) {
			sql.append(" from dbo.ft_kh_all(?, ?)");
			total.append(" from dbo.ft_kh_all(?, ?)");
			sql.append(" where khmc like ?");
			total.append(" where khmc like ?");
			params.put("2", "%" + lwt.getQuery() + "%");
			sql.append(" order by khbh");
		} else {
			sql.append(" from dbo.ft_kh_det_ywy(?, ?)");
			total.append(" from dbo.ft_kh_det_ywy(?, ?)");
			sql.append(" order by khbh");
		}
		List<Object[]> lists = lwtDao.findBySQL(sql.toString(), params, lwt.getPage(), lwt.getRows());

		List<Lwt> nl = new ArrayList<Lwt>();
		// 处理返回信息
		Lwt l = null;
		for (Object[] o : lists) {
			l = new Lwt();
			l.setId(o[0].toString());
			l.setBmbh(o[1].toString());
			l.setYwyId((Integer)o[2]);
			l.setKhbh(o[3].toString());
			l.setKhmc(o[4].toString());
			l.setLxr(o[5].toString());
			l.setKhlxId(o[6].toString());
			l.setSxje(new BigDecimal(o[7].toString()));
			l.setSxzq((Integer)o[8]);
			l.setLsje(new BigDecimal(o[9].toString()));
			l.setIsUp(o[10].toString());
			l.setPostponeDay((Integer)o[11]);
			l.setIsOther(o[12].toString());
			l.setLimitPer(new BigDecimal(o[13].toString()));
			l.setLimitJe(new BigDecimal(o[14].toString()));
			l.setIsLocked(o[15].toString());
			l.setIsDef(o[16].toString());
			l.setInfo(o[17].toString());

			nl.add(l);
		}
		//获得总条数
		dg.setTotal(lwtDao.countSQL(total.toString(), params));
		dg.setRows(nl);
		return dg;
	}
	
	@Override
	public Long countYwyByYwy(Lwt lwt) {
		String sql = "select count(*) from v_ywy_by_ywy where id = ?";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", lwt.getUserId());
		return lwtDao.countSQL(sql, params);
	
	}
	
	@Override
	public DataGrid getYwyByYwy(Lwt lwt) {
		String sql = "execute m_ywy_by_ywy_ys_wkp '" + lwt.getYwyId() + "'";
		DataGrid dg = new DataGrid();
		List<Lwt> nl = new ArrayList<Lwt>();
		List<Object[]> lists = lwtDao.findBySQL(sql);
		for(Object[] o : lists){
			Lwt l = new Lwt();
			String bmbh = (String)o[0];
			Integer ywyId = (Integer)o[1];
			String ywymc = (String)o[2];
			BigDecimal ysje = new BigDecimal(o[3].toString());
			BigDecimal wkpje = new BigDecimal(o[4].toString());
			
			l.setBmbh(bmbh);
			l.setYwyId(ywyId);
			l.setYwymc(ywymc);
			l.setYsje(ysje);
			l.setWkpje(wkpje);
			
			nl.add(l);
		}
		dg.setRows(nl);
		return dg;
	}
	
	@Override
	public DataGrid getYwyByBmbh(Lwt lwt) {
		String sql = "execute m_ywy_ys_wkp '" + lwt.getBmbh() + "'";
		DataGrid dg = new DataGrid();
		List<Lwt> nl = new ArrayList<Lwt>();
		List<Object[]> lists = lwtDao.findBySQL(sql);
		for(Object[] o : lists){
			Lwt l = new Lwt();
			Integer ywyId = (Integer)o[0];
			String ywymc = (String)o[1];
			BigDecimal ysje = new BigDecimal(o[2].toString());
			BigDecimal wkpje = new BigDecimal(o[3].toString());
			
			l.setYwyId(ywyId);
			l.setYwymc(ywymc);
			l.setYsje(ysje);
			l.setWkpje(wkpje);
			
			nl.add(l);
		}
		dg.setRows(nl);
		return dg;
	}

	@Override
	public DataGrid getXsths(Lwt lwt) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TXsth where bmbh = :bmbh and isFhth = '0'";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", lwt.getBmbh());
//		params.put("createTime", lwt.getCreateTime());

		if (lwt.getIsYwy() != null) {
			hql += " and (createId = :ywyId or ywyId = :ywyId)";
			params.put("ywyId", lwt.getCreateId());
		}
		//默认显示当前月数据
//		if(!(lwt.getSearch() != null)){
//			hql += " and t.createTime > :createTime";
//			if(lwt.getCreateTime() != null){
//				params.put("createTime", lwt.getCreateTime());
//			}else{
//				params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
//			}
//		}

		if (lwt.getWhere() != null && lwt.getWhere().length() > 0) {
			hql += " and " + lwt.getWhere();
		}

		if(lwt.getSearch() != null){
			hql += " and (" + Util.getQueryWhere(lwt.getSearch(), new String[]{"xsthlsh", "khmc", "bz", "bookmc"}, params) + ")";
		}

		String countHql = " select count(*)" + hql;

		if (lwt.getOrderBy() != null ) {
			hql += " order by " + lwt.getOrderBy();
		}

		List<TXsth> l = xsthDao.find(hql, params, lwt.getPage(), lwt.getRows());

		List<Lwt> nl = new ArrayList<Lwt>();
		for(TXsth t : l){
			Lwt c = new Lwt();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setTotal(xsthDao.count(countHql, params));
		datagrid.setRows(nl);

		l.clear();

		return datagrid;
	}
	
	@Override
	public DataGrid listXstjByMonth(Lwt lwt) {
		
		String sql = "execute m_xstj_by_month '" + lwt.getBmbh() + "','" + lwt.getUserId() + "','" + lwt.getKhbh() + "','" + lwt.getJzsj() + "','" + lwt.getType() + "'";
		DataGrid dg = new DataGrid();
		List<Lwt> nl = new ArrayList<Lwt>();
		List<Object[]> lists = lwtDao.findBySQL(sql);
		for(Object[] o : lists){
			Lwt l = new Lwt();
			BigDecimal tjje =  new BigDecimal(o[0].toString());
			String jzsj = (String)o[1];
			
			
			l.setTjje(tjje);
			l.setJzsj(jzsj);
			
			nl.add(l);
		}
		dg.setRows(nl);
		return dg;
	}

	private Lwt getKhByYwyXsth(Object[] o,String type) {
		Lwt x = new Lwt();
		if(type.equals("xskp_w_hk")){
			x.setLsh(o[0].toString());
			x.setHjje(new BigDecimal(o[1].toString()));
			x.setHkje(new BigDecimal(o[2].toString()));
			x.setBz(o[3].toString());
			x.setSpmc(o[4].toString());
		}else{
			String xsthlsh = (String)o[0];
			BigDecimal hjje = new BigDecimal(o[1].toString());
			BigDecimal hjsl = new BigDecimal(o[2].toString());
			String ckmc = (String)o[3];
			String bz = (String)o[4];
			String needAudit = o[5].toString();
			String isAudit = o[6].toString();
			String spmc = (String)o[7];

			x.setLsh(xsthlsh);
			x.setHjje(hjje);
			x.setHjsl(hjsl);
			x.setCkmc(ckmc);
			x.setBz(bz);
			x.setNeedAudit(needAudit);
			x.setIsAudit(isAudit);
			x.setSpmc(spmc);
		}
		
		return x;
	}
	
	private Lwt getKhByYwy(Object[] o) {
		Lwt k = new Lwt();
		String khbh = (String)o[0];
		String khmc = (String)o[1];
		BigDecimal sxje = new BigDecimal(o[2].toString());
		int sxzq = (Integer)o[3];
		BigDecimal ysje = new BigDecimal(o[4].toString());
		BigDecimal wkpje = new BigDecimal(o[5].toString());
		
		k.setKhbh(khbh);
		k.setKhmc(khmc);
		k.setSxje(sxje);
		k.setSxzq(sxzq);
		k.setWkpje(wkpje);
		k.setYsje(ysje);
		return k;
	}
	
	private Lwt getXstj(Object[] o){
		Lwt w = new Lwt();
		BigDecimal xsje = new BigDecimal(o[0].toString());
		BigDecimal hkje = new BigDecimal(o[1].toString());
		BigDecimal ysje = new BigDecimal(o[2].toString());
		String jzsj = (String) o[3];
		
	
		w.setXsje(xsje);
		w.setHkje(hkje);
		w.setYsje(ysje);
		w.setJzsj(jzsj);		
		return w;
	}
	
	@Autowired
	public void setLwtDao(BaseDaoI<Object> lwtDao) {
		this.lwtDao = lwtDao;
	}

	@Autowired
	public void setXsthDao(BaseDaoI<TXsth> xsthDao) {
		this.xsthDao = xsthDao;
	}
}
