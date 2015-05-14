package lnyswz.jxc.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.ProBean;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Ck;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.Hw;
import lnyswz.jxc.bean.Kfck;
import lnyswz.jxc.bean.Kfpd;
import lnyswz.jxc.bean.KfpdDet;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.THw;
import lnyswz.jxc.model.TKfpd;
import lnyswz.jxc.model.TKfpdDet;
import lnyswz.jxc.model.TKfzz;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.model.TYwpd;
import lnyswz.jxc.model.TYwrk;
import lnyswz.jxc.service.KfpdServiceI;
import lnyswz.jxc.util.Constant;

/**
 * 库房盘点实现类
 * @author 王文阳
 *
 */
@Service("kfpdService")
public class KfpdServiceImpl implements KfpdServiceI {
	private Logger logger = Logger.getLogger(KfpdServiceImpl.class);
	private BigDecimal ZERO = new BigDecimal(0);
	private BaseDaoI<TKfpd> kfpdDao;
	private BaseDaoI<TKfpdDet> detDao;
	private BaseDaoI<TKfzz> kfzzDao;
	private BaseDaoI<TYwpd> ywpdDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TSp> spDao;
	private BaseDaoI<THw> hwDao;
	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public void save(Kfpd kfpd) {
		TKfpd tKfpd = new TKfpd();
		BeanUtils.copyProperties(kfpd, tKfpd);
		tKfpd.setCreateTime(new Date());
		tKfpd.setKfpdlsh(LshServiceImpl.updateLsh(kfpd.getBmbh(), kfpd.getLxbh(), lshDao));
		tKfpd.setBmmc(depDao.load(TDepartment.class, kfpd.getBmbh()).getDepName());
		
		tKfpd.setIsCj("0");
		
		//如果从业务盘点生成的单据，进行关联
		if(kfpd.getYwpdlsh() != null && kfpd.getYwpdlsh().trim().length() > 0){
			TYwpd tYwpd = ywpdDao.load(TYwpd.class, kfpd.getYwpdlsh());
			tYwpd.setTKfpd(tKfpd);
		}
		
		Department dep = new Department();
		dep.setId(kfpd.getBmbh());
		dep.setDepName(tKfpd.getBmmc());
		
		Ck ck = new Ck();
		ck.setId(kfpd.getCkId());
		ck.setCkmc(tKfpd.getCkmc());

		
		//处理商品明细
		Set<TKfpdDet> tDets = new HashSet<TKfpdDet>();
		ArrayList<KfpdDet> kfpdDets = JSON.parseObject(kfpd.getDatagrid(), new TypeReference<ArrayList<KfpdDet>>(){});
		for(KfpdDet kfpdDet : kfpdDets){
			TKfpdDet tDet = new TKfpdDet();
			BeanUtils.copyProperties(kfpdDet, tDet);
			tDet.setHwmc(hwDao.load(THw.class, kfpdDet.getHwId()).getHwmc());
			
			if("".equals(kfpdDet.getCjldwId()) || kfpdDet.getZhxs() == null || kfpdDet.getZhxs().compareTo(ZERO) == 0){
				tDet.setCdwsl(ZERO);
				tDet.setZhxs(ZERO);
				kfpdDet.setZhxs(ZERO);
			}
			
			tDet.setTKfpd(tKfpd);
			tDets.add(tDet);

			Sp sp = new Sp();
			BeanUtils.copyProperties(kfpdDet, sp);
			
			Hw hw = new Hw();
			hw.setId(kfpdDet.getHwId());
			hw.setHwmc(tDet.getHwmc());

			//更新库房总账
			KfzzServiceImpl.updateKfzzSl(sp, dep, ck, hw, kfpdDet.getSppc(), kfpdDet.getZdwsl(), tDet.getCdwsl(), Constant.UPDATE_RK, kfzzDao);
		}
		tKfpd.setTKfpdDets(tDets);
		kfpdDao.save(tKfpd);		
		OperalogServiceImpl.addOperalog(kfpd.getCreateId(), kfpd.getBmbh(), kfpd.getMenuId(), tKfpd.getKfpdlsh(), "生成库房盘点单", operalogDao);
		
	}
	
	@Override
	public void cjKfpd(Kfpd kfpd) {
		Date now = new Date(); 
		//获取原单据信息
		TKfpd yTKfpd = kfpdDao.load(TKfpd.class, kfpd.getKfpdlsh());
		//新增冲减单据信息
		TKfpd tKfpd = new TKfpd();
		BeanUtils.copyProperties(yTKfpd, tKfpd, new String[]{"TYwpd", "TKfpdDets"});
		//更新原单据冲减信息
		yTKfpd.setCjId(kfpd.getCjId());
		yTKfpd.setCjTime(now);
		yTKfpd.setCjName(kfpd.getCjName());
		yTKfpd.setIsCj("1");
		
		if(yTKfpd.getTYwpd() != null){
			TYwpd tYwpd = ywpdDao.load(TYwpd.class, yTKfpd.getTYwpd().getYwpdlsh());
			tYwpd.setTKfpd(null);
		}
		
		//更新新单据信息
		String lsh = LshServiceImpl.updateLsh(kfpd.getBmbh(), kfpd.getLxbh(), lshDao);
		tKfpd.setKfpdlsh(lsh);
		tKfpd.setCreateTime(now);
		tKfpd.setCreateId(kfpd.getCjId());
		tKfpd.setCreateName(kfpd.getCjName());
		tKfpd.setIsCj("1");
		tKfpd.setCjTime(new Date());
		tKfpd.setCjId(kfpd.getCjId());
		tKfpd.setCjName(kfpd.getCjName());
		tKfpd.setCjKfpdlsh(yTKfpd.getKfpdlsh());
		tKfpd.setBz(kfpd.getBz());
		
		Department dep = new Department();
		dep.setId(tKfpd.getBmbh());
		dep.setDepName(tKfpd.getBmmc());
		
		Ck ck = new Ck();
		ck.setId(tKfpd.getCkId());
		ck.setCkmc(tKfpd.getCkmc());
		
		Set<TKfpdDet> yTKfpdDets = yTKfpd.getTKfpdDets();
		Set<TKfpdDet> tDets = new HashSet<TKfpdDet>();
		for(TKfpdDet yTDet : yTKfpdDets){
			TKfpdDet tDet = new TKfpdDet();
			BeanUtils.copyProperties(yTDet, tDet, new String[]{"id"});
			tDet.setZdwsl(yTDet.getZdwsl().negate());
			if(yTDet.getCdwsl() != null){
				tDet.setCdwsl(yTDet.getCdwsl().negate());
			}
			tDet.setTKfpd(tKfpd);
			tDets.add(tDet);
			
			Sp sp = new Sp();
			BeanUtils.copyProperties(tDet, sp);
		
			Hw hw = new Hw();
			hw.setId(tDet.getHwId());
			hw.setHwmc(tDet.getHwmc());
			
			//更新库房总账
			KfzzServiceImpl.updateKfzzSl(sp, dep, ck, hw, tDet.getSppc(), tDet.getZdwsl(), tDet.getCdwsl(), Constant.UPDATE_RK, kfzzDao);
		}
		tKfpd.setTKfpdDets(tDets);
		kfpdDao.save(tKfpd);		
		OperalogServiceImpl.addOperalog(kfpd.getCjId(), kfpd.getBmbh(), kfpd.getMenuId(), tKfpd.getCjKfpdlsh() + "/" + tKfpd.getKfpdlsh(), "冲减库房盘点", operalogDao);
	}
	
	@Override
	public DataGrid datagrid(Kfpd kfpd) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TKfpd t where t.bmbh = :bmbh and t.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", kfpd.getBmbh());
		if(kfpd.getCreateTime() != null){
			params.put("createTime", kfpd.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TKfpd> l = kfpdDao.find(hql, params, kfpd.getPage(), kfpd.getRows());
		List<Kfpd> nl = new ArrayList<Kfpd>();
		for(TKfpd t : l){
			Kfpd c = new Kfpd();
			BeanUtils.copyProperties(t, c);
			if(t.getTYwpd() != null){
				c.setYwpdlsh(t.getTYwpd().getYwpdlsh());
			}
			nl.add(c);
		}
		datagrid.setTotal(kfpdDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(String kfpdlsh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TKfpdDet t where t.TKfpd.kfpdlsh = :kfpdlsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("kfpdlsh", kfpdlsh);
		List<TKfpdDet> l = detDao.find(hql, params);
		List<KfpdDet> nl = new ArrayList<KfpdDet>();
		for(TKfpdDet t : l){
			KfpdDet c = new KfpdDet();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid getSpkc(Kfpd kfpd) {
		DataGrid dg = new DataGrid();
		List<ProBean> lists = new ArrayList<ProBean>();
		
		List<ProBean> kf = KfzzServiceImpl.getZzsl(kfpd.getBmbh(), kfpd.getSpbh(), kfpd.getCkId(), null, null, kfzzDao);
		if(kf != null){
			lists.addAll(kf);
		}
		
		List<ProBean> pc = KfzzServiceImpl.getZzsl(kfpd.getBmbh(), kfpd.getSpbh(), kfpd.getCkId(), null, "", kfzzDao);
		if(pc != null){
			lists.addAll(pc);
		}
				
		dg.setRows(lists);
		dg.setTotal((long)lists.size());
		return dg;
	}
	
	public DataGrid toYwpd(String kfpdlshs){
		String sql = "select spbh, sum(zdwsl) zdwsl from t_kfpd_det t ";
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(kfpdlshs != null && kfpdlshs.trim().length() > 0){
			String[] ks = kfpdlshs.split(",");
			sql += "where ";
			for(int i = 0; i < ks.length; i++){
				sql += "t.kfpdlsh = ? ";
				params.put(String.valueOf(i), ks[i]);
				//sql += "t.kfpdlsh = '" + ks[i] + "'";
				if(i != ks.length - 1){
					sql += " or ";
				}
 			}
			
		}
		sql += " group by spbh";
		
		List<Object[]> l = detDao.findBySQL(sql, params);
		List<KfpdDet> nl = new ArrayList<KfpdDet>();
		
		for(Object[] os : l){
			String spbh = (String)os[0];
			BigDecimal zdwsl = new BigDecimal(os[1].toString());
			
			TSp sp = spDao.get(TSp.class, spbh);
			KfpdDet kd = new KfpdDet();
			BeanUtils.copyProperties(sp, kd);
			kd.setZjldwId(sp.getZjldw().getId());
			kd.setZjldwmc(sp.getZjldw().getJldwmc());
			kd.setZdwsl(zdwsl);
			if(sp.getCjldw() != null){
				kd.setCjldwId(sp.getCjldw().getId());
				kd.setCjldwmc(sp.getCjldw().getJldwmc());
				kd.setZhxs(sp.getZhxs());
				if(sp.getZhxs().compareTo(ZERO) != 0){
					kd.setCdwsl(zdwsl.divide(sp.getZhxs(), 3, BigDecimal.ROUND_HALF_DOWN));
				}else{
					kd.setCdwsl(ZERO);
				}
			}
			nl.add(kd);
		}
		nl.add(new KfpdDet());
		DataGrid dg = new DataGrid();
		dg.setRows(nl);
		return dg;
	}
	
	@Autowired
	public void setKfpdDao(BaseDaoI<TKfpd> kfpdDao) {
		this.kfpdDao = kfpdDao;
	}

	@Autowired
	public void setKfzzDao(BaseDaoI<TKfzz> kfzzDao) {
		this.kfzzDao = kfzzDao;
	}

	@Autowired
	public void setDetDao(BaseDaoI<TKfpdDet> detDao) {
		this.detDao = detDao;
	}

	@Autowired
	public void setYwpdDao(BaseDaoI<TYwpd> ywpdDao) {
		this.ywpdDao = ywpdDao;
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
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}

}
