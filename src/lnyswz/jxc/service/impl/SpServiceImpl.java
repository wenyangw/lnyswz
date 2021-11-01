package lnyswz.jxc.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.ProBean;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TJldw;
import lnyswz.jxc.model.TLszz;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.model.TSpDet;
import lnyswz.jxc.model.TSpdw;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.service.SpServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Util;

/**
 * 商品实现类
 * @author 王文阳
 *
 */
@Service("spService")
public class SpServiceImpl implements SpServiceI {
	private Logger logger = Logger.getLogger(SpServiceImpl.class);
	private BaseDaoI<TSp> spDao;
	private BaseDaoI<TSpDet> detDao;
	private BaseDaoI<TSpdw> spdwDao;
	private BaseDaoI<TJldw> jldwDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TYwzz> ywzzDao;
	private BaseDaoI<TLszz> lszzDao;
	private BaseDaoI<TOperalog> operalogDao;
	
	/**
	 * 增加商品
	 */
	@Override
	public Sp add(Sp sp) {
		TSp t = new TSp();
		BeanUtils.copyProperties(sp, t);
		//设置商品段位
		if(sp.getSpdwId() != null && sp.getSpdwId().trim().length() > 0){
			TSpdw spdw = spdwDao.get(TSpdw.class, sp.getSpdwId());
			if(spdw != null){
				t.setTSpdw(spdw);
			}
		}
		if (sp.getSpcd().equals("NULL") || sp.getSpcd().trim().equals("")) {
			t.setSpcd("");
		}
		//设置商品计量单位
		setJldw(t, sp);
		spDao.save(t);
		OperalogServiceImpl.addOperalog(sp.getUserId(), sp.getDepId(), sp.getMenuId(), t.getSpbh(), "增加商品记录", operalogDao);
		return changeSp(t, null, null);
	}

	/**
	 * 修改商品
	 */
	@Override
	public void edit(Sp sp) {
		TSp t = spDao.get(TSp.class, sp.getSpbh());
		BeanUtils.copyProperties(sp, t);
		//设置商品计量单位
		setJldw(t, sp);
		OperalogServiceImpl.addOperalog(sp.getUserId(), sp.getDepId(), sp.getMenuId(), t.getSpbh(), "修改商品记录", operalogDao);
	}
	
	/**
	 * 删除商品
	 */
	@Override
	public void delete(Sp sp) {
		TSp t = spDao.get(TSp.class, sp.getSpbh());
		spDao.delete(t);
		OperalogServiceImpl.addOperalog(sp.getUserId(), sp.getDepId(), sp.getMenuId(), t.getSpbh(), "删除商品记录", operalogDao);
	}

	@Override
	public List<String> exportToJs(Sp sp) {
		List<String> result = new ArrayList<String>();
		//result.add("{商品编码}[分隔符]\"~~\"" + "\r\n");
		result.add("{商品编码}[分隔符]\"~~\"");
		result.add("// 每行格式 :");
		result.add("// 编码~~名称~~简码~~商品税目~~税率~~规格型号~~计量单位~~单价~~含税价标志~~隐藏标志~~中外合作油气田~~税收分类编码~~是否享受优惠政策~~税收分类编码名称~~优惠政策类型~~零税率标识~~编码版本号");

		String sql = null;
		if(sp.getDepId().equals("01")) {
			sql = "select spbh + '~~\"' + spbh + ' ' + case when CHARINDEX(' ', spmc) > 0 then RTRIM(left(spmc, CHARINDEX(' ', spmc))) else RTRIM(spmc) end + case when len(sppp) > 0 then '(' + sppp + ')' else '' end + case when len(spbz) > 0 then ' ' + spbz else '' end\n" +
					//"+ '\"~~~~~~0.17~~~~~~0~~False~~0000000000~~False~~' + isnull(jsbh, '') + '~~否~~' + isnull(jsmc, '') + '~~~~~~10.0'\n" +
					"+ '\"~~~~~~" + Constant.SHUILV + "~~~~~~0~~False~~0000000000~~False~~' + isnull(jsbh, '') + '~~否~~' + isnull(jsmc, '') + '~~~~~~10.0'" +
					//"from t_sp where SUBSTRING(spbh, 1, 1) in ('1', '3', '5', '6')";
					"from t_sp where SUBSTRING(spbh, 1, 1) in " + Constant.SP_JS.get(sp.getDepId());
		}else{
			sql = "select spbh + '~~\"' + spbh + '  ' + case when CHARINDEX(' ', spmc) > 0 then RTRIM(left(spmc, CHARINDEX(' ', spmc))) else RTRIM(spmc) end + case when len(spcd) > 0 then '(' + spcd + ')' else '' end\n" +
					//"+ '\"~~~~~~0.17~~~~~~0~~False~~0000000000~~False~~' + isnull(jsbh, '') + '~~否~~' + isnull(jsmc, '') + '~~~~~~10.0'\n" +
					"+ '\"~~~~~~" + Constant.SHUILV + "~~~~~~0~~False~~0000000000~~False~~' + isnull(jsbh, '') + '~~否~~' + isnull(jsmc, '') + '~~~~~~10.0'" +
					//"from t_sp where SUBSTRING(spbh, 1, 1) in ('1', '3', '5', '6')";
					"from t_sp where SUBSTRING(spbh, 1, 1) in " + Constant.SP_JS.get(sp.getDepId());
		}

		List<Object> lists = spDao.findOneBySQL(sql);

		if(lists.size() > 0){
			for (Object o : lists) {
				result.add(o.toString());
			}
		}

		return result;
	}

	/**
	 * 维护专属信息
	 */
	@Override
	public void editSpDet(Sp sp) {
		String keyId = "";
		//已存在，修改
		if(sp.getDetId() != 0){
			TSpDet det = detDao.get(TSpDet.class, sp.getDetId());
			BeanUtils.copyProperties(sp, det);
			keyId = sp.getSpbh() + "/" + det.getId();
 		}else{
			TSp t = spDao.get(TSp.class, sp.getSpbh());
			Set<TSpDet> dets= t.getTSpDets();
			TSpDet det = new TSpDet();
			BeanUtils.copyProperties(sp, det);
			det.setTSp(t);
			det.setTDepartment(depDao.get(TDepartment.class, sp.getDepId()));
			dets.add(det);
			keyId = sp.getSpbh() + "/" + det.getId();
		}
		OperalogServiceImpl.addOperalog(sp.getUserId(), sp.getDepId(), sp.getMenuId(), keyId, "修改商品专属信息", operalogDao);
	}
	
	/**
	 * 删除专属信息
	 */
	@Override
	public void deleteSpDet(Sp sp) {
		TSp tSp = spDao.get(TSp.class, sp.getSpbh());
		Set<TSpDet> dets = tSp.getTSpDets();
		tSp.setTSpDets(null);
		TSpDet det = detDao.get(TSpDet.class, sp.getDetId());
		dets.remove(det);
		detDao.delete(det);
		tSp.setTSpDets(dets);
		OperalogServiceImpl.addOperalog(sp.getUserId(), sp.getDepId(), sp.getMenuId(), sp.getSpbh() + "/" + sp.getDetId(), "删除商品专属信息", operalogDao);
	}
	
	@Override
	public DataGrid spDg(Sp sp) {
		String hql = "from TSp t join t.TSpdw.TSplb.TSpdl.TDepartments deps where deps.id = :depId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", sp.getDepId());
		String countHql = "select count(spbh) " + hql;
		if(sp.getQuery() != null && sp.getQuery().trim().length() > 0){
			//String where = " and (t.spbh like :spbh or t.spmc like :spmc)";
			String where = " and (" + Util.getQueryWhere(sp.getQuery(), new String[]{"t.spbh", "t.spmc", "t.spcd"}, params)	+ ")";
			hql += where;
			countHql += where;
			//params.put("spbh", sp.getQuery() + "%");
			//params.put("spmc", "%" + sp.getQuery() + "%");
		}
		DataGrid dg = new DataGrid();
		dg.setTotal(spDao.count(countHql, params));
		dg.setRows(changeSps(spDao.find("select t " + hql, params, sp.getPage(), sp.getRows()), sp.getDepId(), sp.getCkId()));
		return dg;
	}

	/**
	 * 获得商品信息，供管理用，有分页
	 */
	@Override
	public DataGrid datagrid(Sp sp) {
		String hql = "from TSp t join t.TSpdw.TSplb.TSpdl.TDepartments deps where deps.id = :depId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", sp.getDepId());
		String countHql = "select count(spbh) " + hql;
		if(sp.getSpdwId() != null && sp.getSpdwId().trim().length() > 0){
			String where = " and t.TSpdw.id = :spdwId"; 
			hql += where;
			countHql += where;
			params.put("spdwId", sp.getSpdwId());
		}
		DataGrid dg = new DataGrid();
		dg.setTotal(spDao.count(countHql, params));
		dg.setRows(changeSps(spDao.find("select t " + hql + " order by t.spbh", params, sp.getPage(), sp.getRows()), sp.getDepId(), null));
		return dg;
	}
	
	/**
	 * 获得商品信息，供管理用，有分页 
	 */
	@Override
	public DataGrid datagridBgy(Sp sp) {
		logger.info(sp.getBgyId());
		if(sp.getBgyId() != 0){
			String hql = "TSp t join t.TSpdw.TSplb.TSpdl.TDepartments deps where deps.id = :depId";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depId", sp.getDepId());
			String countHql = "select count(t.spbh) " + hql;
			DataGrid dg = new DataGrid();
			dg.setTotal(spDao.count(countHql, params));
			dg.setRows(changeSps(spDao.find("select t " + hql, params, sp.getPage(), sp.getRows()), sp.getDepId(), null));
			return dg;
		}
		return null;
	}
	
	
	
	/**
	 * 校验商品编码是否存在
	 */
	@Override
	public boolean existSp(Sp sp) {
		TSp t = spDao.get(TSp.class, sp.getSpbh());
		if(t != null){
			return true;
		}
		return false;
	}
	
	@Override
	public Sp loadSp(String spbh, String depId) {
		String hql = "from TSp t join t.TSpdw.TSplb.TSpdl.TDepartments deps where deps.id = :depId and t.spbh = :spbh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", depId);
		params.put("spbh", spbh);
		TSp sp = spDao.get("select t " + hql, params);
		if(sp != null){
			return changeSp(sp, depId, null);
		}
		return null;
	}
	
	@Override
	public DataGrid getSpkc(Sp sp) {
		DataGrid dg = new DataGrid();
		List<ProBean> lists = new ArrayList<ProBean>();
		String groupName = "业务库存";
		ProBean pb1 = new ProBean();
		pb1.setGroup(groupName);
		pb1.setName("业务库存");
		pb1.setValue("28.35");
		lists.add(pb1);
		
		ProBean pb2 = new ProBean();
		pb2.setGroup(groupName);
		pb2.setName("总账库存");
		pb2.setValue("25.25");
		lists.add(pb2);

		groupName = "库房库存";
		ProBean pb3 = new ProBean();
		pb3.setGroup(groupName);
		pb3.setName("库房库存");
		pb3.setValue("23.34");
		lists.add(pb3);
		
		ProBean pb4 = new ProBean();
		pb4.setGroup(groupName);
		pb4.setName("总库存");
		pb4.setValue("56.25");
		lists.add(pb4);
		
		dg.setRows(lists);
		dg.setTotal((long)lists.size());
		return dg;
	}
	
	/**
	 * 转换列表
	 * @param l
	 * @param did
	 * @return
	 */
	private List<Sp> changeSps(List<TSp> l, String did, String ckId){
		List<Sp> nl = new ArrayList<Sp>();
		for(TSp t : l){
			nl.add(changeSp(t, did, ckId));
		}
		return nl;
	}
	
	/**
	 * 转换类对象
	 * @param t
	 * @param depId
	 * @return
	 */
	private Sp changeSp(TSp t, String depId, String ckId){
		Sp s = new Sp();
		BeanUtils.copyProperties(t, s);
		s.setSpdwId(t.getTSpdw().getId());
		s.setSpdwmc(t.getTSpdw().getSpdwmc());
		s.setZjldwId(t.getZjldw().getId());
		s.setZjldwmc(t.getZjldw().getJldwmc());
		if(t.getCjldw() != null){
			s.setCjldwId(t.getCjldw().getId());
			s.setCjldwmc(t.getCjldw().getJldwmc());
		}
		
		s.setKcsl(Constant.BD_ZERO);
		s.setCkcsl(Constant.BD_ZERO);
		Object[] yw = YwzzServiceImpl.getYwzzSl(depId, t.getSpbh(), ckId, "z", ywzzDao);
		Object[] ls = LszzServiceImpl.getLszzSl(depId, t.getSpbh(), ckId, "z", lszzDao);
		Object[] cyw = YwzzServiceImpl.getYwzzSl(depId, t.getSpbh(), ckId, "c", ywzzDao);
		Object[] cls = LszzServiceImpl.getLszzSl(depId, t.getSpbh(), ckId, "c", lszzDao);
		
		if(yw != null){
			s.setKcsl(new BigDecimal(yw[1].toString()));
		}
		if(ls != null){
			s.setKcsl(s.getKcsl().subtract(new BigDecimal(ls[1].toString())));
		}
		if(cyw != null){
			s.setCkcsl(new BigDecimal(cyw[1].toString()));
		}
		if(cls != null){
			s.setCkcsl(s.getCkcsl().subtract(new BigDecimal(cls[1].toString())));
		}
		
		BigDecimal dwcb = YwzzServiceImpl.getDwcb(depId, t.getSpbh(), ywzzDao);
		s.setDwcb(dwcb);
			
		Set<TSpDet> spDets = t.getTSpDets();
		if(spDets != null && spDets.size() > 0){
			for(TSpDet det : spDets){
				if(det.getTDepartment().getId().equals(depId) && det.getTSp().getSpbh().equals(t.getSpbh())){
					s.setDetId(det.getId());
					s.setDepId(depId);
					s.setMaxKc(det.getMaxKc());
					s.setMinKc(det.getMinKc());
					s.setXsdj(det.getXsdj());
					if(det.getXsdj() != null){
						s.setXsdjs(det.getXsdj().multiply(new BigDecimal(1).add(Constant.SHUILV)));
					}else{
						s.setXsdjs(Constant.BD_ZERO);
					}
					if(det.getSpecXsdj() != null){
						s.setSpecXsdj(det.getSpecXsdj());
					}
					s.setLimitXsdj(det.getLimitXsdj());
				}
			}
		}
		return s;
	}
	
	/**
	 * 增加、修改商品信息时设置商品计算单位
	 * @param tSp
	 * @param sp
	 */
	public void setJldw(TSp tSp, Sp sp){
		//设置商品主计量单位
		if(sp.getZjldwId() != null && sp.getZjldwId().trim().length() > 0){
			TJldw zjldw = jldwDao.get(TJldw.class, sp.getZjldwId());
			if(zjldw != null){
				tSp.setZjldw(zjldw);
			}
		}
		//设置商品次计量单位
		if(sp.getCjldwId() != null && sp.getCjldwId().trim().length() > 0){
			TJldw cjldw = jldwDao.get(TJldw.class, sp.getCjldwId());
			if(cjldw != null){
				tSp.setCjldw(cjldw);
			}
		}
	}
	
	public static Sp getSpCdw(String spbh, BaseDaoI<TSp> baseDao){
		TSp tSp = baseDao.load(TSp.class, spbh);
		if(tSp.getCjldw() != null){
			Sp sp = new Sp();
			sp.setCjldwId(tSp.getCjldw().getId());
			sp.setCjldwmc(tSp.getCjldw().getJldwmc());
			sp.setZhxs(tSp.getZhxs());
			return sp;
		}
		return null;
	}

	@Override
	public DataGrid getSpsByLb(Sp sp) {
		DataGrid dg = new DataGrid();
		StringBuffer sql = new StringBuffer("select spbh, spmc, spcd, sppp, spbz, zjldwId, zjldwmc");
		StringBuffer where = new StringBuffer(" from v_sp_mini where lbid = ?");
		StringBuffer countSql = new StringBuffer("select count(*)");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", sp.getSplbId());
        dg.setRows(getSpsJSONList(spDao.findBySQL(sql.append(where).toString(), params, sp.getPage(), sp.getRows())));
        dg.setTotal(spDao.countSQL(countSql.append(where).toString(), params));

		return dg;
	}

    @Override
    public DataGrid searchSps(Sp sp) {
        DataGrid dg = new DataGrid();
	    StringBuffer sql = new StringBuffer("select spbh, spmc, spcd, isnull(sppp, '') sppp, spbz, zjldwId, zjldwmc");
//	    StringBuffer where = new StringBuffer(" from v_sp_mini where spbh + spmc + spcd + sppp like ?");
		StringBuffer where = new StringBuffer(" from v_sp_mini");
	    StringBuffer sqlCount = new StringBuffer("select count(*)");
	    StringBuffer order = new StringBuffer(" order by spbh");
	    Map<String, Object> params = new HashMap<String, Object>();

//	    sp.setQuery(sp.getQuery().replace(" ", "%"));
//	    params.put("0", "%" + sp.getQuery() + "%");
		if (sp.getQuery() != null && sp.getQuery().length() > 0){
			where.append(" where " + Util.getQuerySQLWhere(sp.getQuery(), new String[]{"spbh", "spmc", "spcd", "sppp"}, params, 0));
		}


	    dg.setRows(getSpsJSONList(spDao.findBySQL(sql.append(where).append(order).toString(), params, sp.getPage(), sp.getRows())));
	    dg.setTotal(spDao.countSQL(sqlCount.append(where).toString(), params));
        return dg;
    }

    private List<JSONObject> getSpsJSONList(List<Object[]> list) {
        List<JSONObject> results = new ArrayList<JSONObject>();
        if (list.size() > 0) {
            JSONObject spBean = null;
            for (Object[] s : list) {
                spBean = new JSONObject();
                spBean.put("spbh", s[0].toString());
                spBean.put("spmc", s[1].toString());
                spBean.put("spcd", s[2].toString());
                spBean.put("sppp", s[3].toString());
                spBean.put("spbz", s[4].toString());
				spBean.put("zjldwId", s[5].toString());
                spBean.put("zjldwmc", s[6].toString());
                results.add(spBean);
            }
        }
        return results;
    }

    @Autowired
	public void setSpDao(BaseDaoI<TSp> spDao) {
		this.spDao = spDao;
	}
	
	@Autowired
	public void setJldwDao(BaseDaoI<TJldw> jldwDao) {
		this.jldwDao = jldwDao;
	}
	
	@Autowired
	public void setSpdwDao(BaseDaoI<TSpdw> spdwDao) {
		this.spdwDao = spdwDao;
	}
	
	@Autowired
	public void setDetDao(BaseDaoI<TSpDet> detDao) {
		this.detDao = detDao;
	}

	@Autowired
	public void setDepDao(BaseDaoI<TDepartment> depDao) {
		this.depDao = depDao;
	}

	@Autowired
	public void setYwzzDao(BaseDaoI<TYwzz> ywzzDao) {
		this.ywzzDao = ywzzDao;
	}

	@Autowired
	public void setLszzDao(BaseDaoI<TLszz> lszzDao) {
		this.lszzDao = lszzDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}

}
