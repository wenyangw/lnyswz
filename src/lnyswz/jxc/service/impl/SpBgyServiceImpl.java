package lnyswz.jxc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.SpBgy;
import lnyswz.jxc.model.TCk;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.model.TSpBgy;
import lnyswz.jxc.service.SpBgyServiceI;

/**
 * 商品保管员实现类
 * @author 王文阳
 *
 */
@Service("spBgyService")
public class SpBgyServiceImpl implements SpBgyServiceI {
	private Logger logger = Logger.getLogger(SpBgyServiceImpl.class);
	private BaseDaoI<TSp> spDao;
	private BaseDaoI<TSpBgy> spBgyDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TCk> ckDao;
	private BaseDaoI<TOperalog> operalogDao;
	
	@Override
	public void updateSpBgy(SpBgy spBgy) {
		String[] spbhs = spBgy.getSpbhs().split(",");
		String spbhsSel = spBgy.getSpbhsSel();
		
		for(String spbh : spbhs){
			String hql = "from TSpBgy t where t.depId = :depId and t.ckId = :ckId and t.bgyId = :bgyId and t.spbh = :spbh";
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("depId", spBgy.getDepId());
			params.put("ckId", spBgy.getCkId());
			params.put("bgyId", spBgy.getBgyId());
			params.put("spbh", spbh);
			//获取当前页每行在t_sp_bgy中的数据
			TSpBgy t = spBgyDao.get(hql, params);
			
			//选中的
			if(spbhsSel.indexOf(spbh, 0) != -1){
				//不存在就新增一条
				if(t == null){
					TSpBgy tSpBgy = new TSpBgy();
					tSpBgy.setDepId(spBgy.getDepId());
					tSpBgy.setDepName(depDao.load(TDepartment.class, spBgy.getDepId()).getDepName());
					tSpBgy.setCkId(spBgy.getCkId());
					tSpBgy.setCkmc(ckDao.load(TCk.class, spBgy.getCkId()).getCkmc());
					tSpBgy.setBgyId(spBgy.getBgyId());
					tSpBgy.setBgyName(spBgy.getBgyName());
					tSpBgy.setSpbh(spbh);
					TSp tSp = spDao.load(TSp.class, spbh);
					tSpBgy.setSpmc(tSp.getSpmc());
					tSpBgy.setSpcd(tSp.getSpcd());
					spBgyDao.save(tSpBgy);
				}
			}else{
				//没选中
				
				//存在该记录，删除
				if(t != null){
					spBgyDao.delete(t);
				}
			}
			
		}
		
	}
	
	@Override
	public void deleteSpBgy(SpBgy spBgy) {
		String hql = "from TSpBgy t where t.depId = :depId and t.ckId = :ckId and t.bgyId = :bgyId";
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("depId", spBgy.getDepId());
		params.put("ckId", spBgy.getCkId());
		params.put("bgyId", spBgy.getBgyId());
		
		List<TSpBgy> lists = spBgyDao.find(hql, params);
		if(lists != null && lists.size() > 0){
			for(TSpBgy t : lists){
				spBgyDao.delete(t);
			}
		}
	}
	
	/**
	 * 获得商品信息，供管理用，有分页 
	 */
	@Override
	public DataGrid datagridBgy(SpBgy spBgy) {
		if(spBgy.getBgyId() != 0){
			String hql = "from TSp t join t.TSpdw.TSplb.TSpdl.TDepartments deps where deps.id = :depId";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depId", spBgy.getDepId());
			if(spBgy.getSearch() != null){
				hql += " and (t.spbh like :searchSp or t.spmc like :searchMc)";
				params.put("searchSp", spBgy.getSearch() + "%");
				params.put("searchMc", "%" + spBgy.getSearch() + "%");
			}
			List<TSp> spLists = spDao.find("select t " + hql, params, spBgy.getPage(), spBgy.getRows());
			if(spLists == null){
				return null;
			}
			
			List<SpBgy> spBgyLists = new ArrayList<SpBgy>();
			Map<String, Object> bgyParams = new HashMap<String, Object>(); 
			for(TSp tSp : spLists){
				SpBgy s = new SpBgy();
				BeanUtils.copyProperties(tSp, s);
				String bgyHql = "from TSpBgy t where t.depId = :depId and t.ckId = :ckId and t.bgyId = :bgyId and t.spbh = :spbh";
				bgyParams.put("depId", spBgy.getDepId());
				bgyParams.put("ckId", spBgy.getCkId());
				bgyParams.put("bgyId", spBgy.getBgyId());
				bgyParams.put("spbh", tSp.getSpbh());
				TSpBgy tSpBgy = spBgyDao.get(bgyHql, bgyParams);
				if(tSpBgy != null){
					BeanUtils.copyProperties(tSpBgy, s);
				}
				spBgyLists.add(s);
				bgyParams.clear();
			}
			
			String countHql = "select count(t.spbh) " + hql;
			DataGrid dg = new DataGrid();
			dg.setTotal(spDao.count(countHql, params));
			dg.setRows(spBgyLists);
			return dg;
		}
		return null;
	}
	
	
	
	@Autowired
	public void setSpDao(BaseDaoI<TSp> spDao) {
		this.spDao = spDao;
	}
	
	@Autowired
	public void setSpBgyDao(BaseDaoI<TSpBgy> spBgyDao) {
		this.spBgyDao = spBgyDao;
	}

	@Autowired
	public void setDepDao(BaseDaoI<TDepartment> depDao) {
		this.depDao = depDao;
	}

	@Autowired
	public void setCkDao(BaseDaoI<TCk> ckDao) {
		this.ckDao = ckDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}



}
