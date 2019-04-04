package lnyswz.jxc.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.KfbgSp;
import lnyswz.jxc.bean.KfbgSpZz;
import lnyswz.jxc.model.TKfbgSp;
import lnyswz.jxc.model.TKfbgSpZz;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.service.KfbgSpServiceI;
import lnyswz.jxc.util.Constant;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**

 *
 */
@Service("kfbgSpService")
public class KfbgSpServiceImpl implements KfbgSpServiceI {
	private BaseDaoI<TKfbgSp> bgDao;
	private BaseDaoI<TKfbgSpZz> bgzzDao;
	private BaseDaoI<TSp> spDao;

	/**
	 * 增加按钮
	 */
	@Override
	public KfbgSp add(KfbgSp k) {

		String hql = " from TKfbgSpZz where spbh = :spbh ";
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("spbh", k.getSpbh());
		TKfbgSpZz bgzz = bgzzDao.get(hql,params);

		if(bgzz == null){
			String hqlSp = " from TSp where spbh = :spbh ";
			TSp tsp = spDao.get(hqlSp,params);
			
			TKfbgSpZz bgz = new TKfbgSpZz();
			BeanUtils.copyProperties(k, bgz);
			BeanUtils.copyProperties(tsp, bgz);
			
			bgz.setZdwmc(tsp.getZjldw().getJldwmc());
			bgz.setCdwmc(tsp.getCjldw().getJldwmc());
//			bgz.setFdwmc("");
			bgz.setZqcsl(BigDecimal.ZERO);
			bgz.setFqcsl(BigDecimal.ZERO);
			
			TKfbgSp bgmx = new TKfbgSp();
			BeanUtils.copyProperties(bgz, bgmx);
			bgmx.setKhmc(k.getKhmc());
			bgmx.setLsh(k.getLsh());
			bgmx.setCreateTime(new Date());
			bgmx.setType("9");

			if ( k.getBmbh().equals("00") ) {
				bgz.setZwdzy(BigDecimal.ZERO);
				bgz.setFwdzy(BigDecimal.ZERO);
				bgz.setZjc(BigDecimal.ZERO);
				bgz.setFjc(BigDecimal.ZERO);
				bgz.setZNoBm(k.getZrksl());
				bgz.setFNoBm(k.getFrksl());
				bgmx.setBmmc("总库存");
				
			} else if (k.getBmbh().equals("05")) {
				bgz.setZwdzy(k.getZrksl());
				bgz.setFwdzy(k.getFrksl());
				bgz.setZjc(BigDecimal.ZERO);
				bgz.setFjc(BigDecimal.ZERO);
				bgz.setZNoBm(BigDecimal.ZERO);
				bgz.setFNoBm(BigDecimal.ZERO);
				bgmx.setBmmc("文达纸业");
				
			} else if (k.getBmbh().equals("04")) {
				bgz.setZwdzy(BigDecimal.ZERO);
				bgz.setFwdzy(BigDecimal.ZERO);
				bgz.setZjc(k.getZrksl());
				bgz.setFjc(k.getFrksl());
				bgz.setZNoBm(BigDecimal.ZERO);
				bgz.setFNoBm(BigDecimal.ZERO);
				bgmx.setBmmc("教材中心");
			}

			bgmx.setBmbh(k.getBmbh());
			bgDao.save(bgmx);
			
			bgzzDao.save(bgz);
			BeanUtils.copyProperties(bgz, k);
		} else {

			if (bgzz.getFdwmc().equals("")) {
				bgzz.setFdwmc(k.getFdwmc());
			}
			
			TKfbgSp bgmx = new TKfbgSp();
			BeanUtils.copyProperties(bgzz, bgmx);
			bgmx.setType(k.getType());
			bgmx.setCreateTime(new Date());
			bgmx.setKhmc(k.getKhmc());
			bgmx.setLsh(k.getLsh());
			bgmx.setZcksl(k.getZcksl());
			bgmx.setZrksl(k.getZrksl());
			bgmx.setFcksl(k.getFcksl());
			bgmx.setFrksl(k.getFrksl());

			if (k.getType().equals(Constant.UPDATE_CK)) {
				if (k.getBmbh().equals("00")) {
					bgzz.setZNoBm(bgzz.getZNoBm().subtract(k.getZcksl()));
					bgzz.setFNoBm(bgzz.getFNoBm().subtract(k.getFcksl()));
					bgmx.setBmmc("不分部门");
					
				} else if (k.getBmbh().equals("05")) {
					bgzz.setZwdzy(bgzz.getZwdzy().subtract(k.getZcksl()));
					bgzz.setFwdzy(bgzz.getFwdzy().subtract(k.getFcksl()));
					bgmx.setBmmc("文达纸业");
					
				} else if (k.getBmbh().equals("04")) {
					bgzz.setZjc(bgzz.getZjc().subtract(k.getZcksl()));
					bgzz.setFjc(bgzz.getFjc().subtract(k.getFcksl()));
					bgmx.setBmmc("教材中心");
					
				}

			} else {
				if (k.getBmbh().equals("00")) {
					bgzz.setZNoBm(bgzz.getZNoBm().add(k.getZrksl()));
					bgzz.setFNoBm(bgzz.getFNoBm().add(k.getFrksl()));
					bgmx.setBmmc("不分部门");
				} else if (k.getBmbh().equals("05")) {
					bgzz.setZwdzy(bgzz.getZwdzy().add(k.getZrksl()));
					bgzz.setFwdzy(bgzz.getFwdzy().add(k.getFrksl()));
					bgmx.setBmmc("文达纸业");
				} else if (k.getBmbh().equals("04")) {
					bgzz.setZjc(bgzz.getZjc().add(k.getZrksl()));
					bgzz.setFjc(bgzz.getFjc().add(k.getFrksl()));
					bgmx.setBmmc("教材中心");
				}
			}

			bgmx.setBmbh(k.getBmbh());
			bgDao.save(bgmx);
			
			bgzzDao.update(bgzz);
			
			BeanUtils.copyProperties(bgzz, k);
		}
		return k;
	}
	

	
//	@Override
//	public DataGrid searchSp(KfbgSp k) {
//		DataGrid dg = new DataGrid();
//		if(k.getSpbh().trim() =="" ){
//			dg.setTotal((long) 0);	
//		}else{
//			String hql = "  from TKfbgSpZz t  where spbh like :spbh or spmc like :spbh" ;
//			Map<String, Object> params = new HashMap<String, Object>();
//			params.put("spbh", "%"+k.getSpbh()+"%");
//			List<TKfbgSpZz> l = bgzzDao.find(hql,params,k.getPage(),k.getRows());
//			List<KfbgSpZz> nl = new ArrayList<KfbgSpZz>();
//			for(TKfbgSpZz t : l){
//				KfbgSpZz s = new KfbgSpZz();
//				BeanUtils.copyProperties(t, s);
//				nl.add(s);
//			}
//			dg.setRows(nl);
//			dg.setTotal((long) l.size());	
//		}
//		
//	    return dg;
//	}
	
	
	@Override
	public KfbgSpZz getKfbgSpZz(KfbgSp k) {
		String hql = "from TKfbgSpZz t  where t.spbh = :spbh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("spbh", k.getSpbh());
		TKfbgSpZz tz =bgzzDao.get(hql, params); 
		if(tz != null){
			KfbgSpZz z =  new KfbgSpZz();
			BeanUtils.copyProperties(tz, z);
			z.setZdwsl(z.getZwdzy().add(z.getZjc()).add(z.getZNoBm()));
			z.setFdwsl(z.getFwdzy().add(z.getFjc()).add(z.getFNoBm()));
			if (z.getZhxs().compareTo(BigDecimal.ZERO) != 0) {
				z.setCdwsl(z.getZdwsl().divide(tz.getZhxs()));
			} else {
				z.setCdwsl(BigDecimal.ZERO);
			
			}			
			return z;
			
		}else{
			String spHql = "from TSp t  where t.spbh = :spbh";
	
			TSp sp = spDao.get(spHql, params);
			KfbgSpZz t = new KfbgSpZz();
			if(sp != null){
				BeanUtils.copyProperties(sp, t);
				t.setMark("00");
				t.setZdwmc(sp.getZjldw().getJldwmc());
				t.setCdwmc(sp.getCjldw().getJldwmc());
				t.setZdwsl(BigDecimal.ZERO);
				t.setCdwsl(BigDecimal.ZERO);
				t.setFdwsl(BigDecimal.ZERO);
				//数量是否处理
//				t.setSpbh( k.getSpbh());
			}		
			return  t;
		}
		
	}

	@Override
	public DataGrid datagridKfbgSpMxs(KfbgSp k) {
		DataGrid dg = new DataGrid();
		String hql = "from TKfbgSp t  where t.spbh = :spbh order by id desc";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("spbh", k.getSpbh());
		List<TKfbgSp> lm = bgDao.find(hql, params, k.getPage(), k.getRows());
		List<KfbgSp> nl = new ArrayList<KfbgSp>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		for (TKfbgSp t : lm) {
			KfbgSp s = new KfbgSp();
			BeanUtils.copyProperties(t, s);
			setType(t, s);
			s.setShowDate(sdf.format(s.getCreateTime()));
			nl.add(s);
		}
		dg.setRows(nl);
		dg.setTotal((long) lm.size());
		return dg;
	}

		@Override
	public DataGrid datagridKfbgSpMxByDate(KfbgSp k) {
		DataGrid dg = new DataGrid();
		String hql = "from TKfbgSp t  where t.spbh = :spbh and CONVERT(char(7),createTime,23) = :searchDate order by createTime" ;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("spbh", k.getSpbh());
		params.put("searchDate", k.getSearch());

		List<TKfbgSp> lm = bgDao.find(hql, params, k.getPage(), k.getRows());
		List<KfbgSp> nl = new ArrayList<KfbgSp>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(TKfbgSp t : lm){
			KfbgSp s = new KfbgSp();
			BeanUtils.copyProperties(t, s);
			setType(t, s);
			s.setShowDate(sdf.format(s.getCreateTime()));
			nl.add(s);
		}
		dg.setRows(nl);
		dg.setTotal((long) lm.size());
		return dg;	
	}

		
		
	@Override
	public KfbgSp getKfbgSpMx(KfbgSp k) {	
		TKfbgSp t = bgDao.get(TKfbgSp.class, k.getId());
		KfbgSp s = new KfbgSp();
		BeanUtils.copyProperties(t, s);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		setType(t, s);
		s.setShowDate(sdf.format(s.getCreateTime()));
		return s;	
	}	
		
	private void setType(TKfbgSp t, KfbgSp s) {
		if( t.getType().equals(Constant.UPDATE_XZ) ){
			s.setType("新增");
			s.setZdwsl(s.getZrksl());
			s.setFdwsl(s.getFrksl());
		} else if(t.getType().equals(Constant.UPDATE_RK)){
			s.setType("入库");
			s.setZdwsl(s.getZrksl());
			s.setFdwsl(s.getFrksl());
		} else if(t.getType().equals(Constant.UPDATE_CK)){
			s.setType("出库");
			s.setZdwsl(s.getZcksl());
			s.setFdwsl(s.getFcksl());
		}
	}
	@Autowired
	public void setBgDao(BaseDaoI<TKfbgSp> bgDao) {
		this.bgDao = bgDao;
	}
	@Autowired
	public void setBgzzDao(BaseDaoI<TKfbgSpZz> bgzzDao) {
		this.bgzzDao = bgzzDao;
	}
	@Autowired
	public void setSpDao(BaseDaoI<TSp> spDao) {
		this.spDao = spDao;
	}



	
}
