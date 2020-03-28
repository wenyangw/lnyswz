package lnyswz.jxc.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.ProBean;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.*;
import lnyswz.jxc.model.*;
import lnyswz.jxc.service.BgckServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Export;
import lnyswz.jxc.util.Util;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 保管出库实现类
 * @author 王文阳
 *
 */
@Service("bgckService")
public class BgckServiceImpl implements BgckServiceI {
	private Logger logger = Logger.getLogger(BgckServiceImpl.class);
	private BaseDaoI<TBgck> bgckDao;
	private BaseDaoI<TBgckDet> detDao;
	private BaseDaoI<TKfzz> kfzzDao;
	private BaseDaoI<TBgzz> bgzzDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public Bgck save(Bgck bgck) {
		TBgck tBgck = new TBgck();
		BeanUtils.copyProperties(bgck, tBgck);
		tBgck.setCreateTime(new Date());
		String lsh = LshServiceImpl.updateLsh(bgck.getBmbh(), bgck.getLxbh(), lshDao);
		tBgck.setBgcklsh(lsh);
		tBgck.setBmmc(depDao.load(TDepartment.class, bgck.getBmbh()).getDepName());
		tBgck.setIsCj("0");

		Department dep = new Department();
		dep.setId(bgck.getBmbh());
		dep.setDepName(tBgck.getBmmc());
		
		//处理商品明细
		Set<TBgckDet> tDets = new HashSet<TBgckDet>();
		ArrayList<BgckDet> bgckDets = JSON.parseObject(bgck.getDatagrid(), new TypeReference<ArrayList<BgckDet>>(){});
		for(BgckDet bgckDet : bgckDets){
			if(bgckDet.getSpbh() != null) {
				TBgckDet tDet = new TBgckDet();
				BeanUtils.copyProperties(bgckDet, tDet, new String[]{"id"});

				Sp sp = new Sp();
				BeanUtils.copyProperties(bgckDet, sp);

				if ("".equals(bgckDet.getCjldwId())) {
					tDet.setZhxs(Constant.BD_ZERO);
				} else {
					if (bgckDet.getZhxs().compareTo(Constant.BD_ZERO) == 0) {
					}
				}

				tDet.setTBgck(tBgck);
				//更新库房总账
				BgzzServiceImpl.updateBgzzSl(sp, dep, bgckDet.getZdwsl(), Constant.UPDATE_CK, bgzzDao);

				tDets.add(tDet);
			}
		}
		tBgck.setTBgckDets(tDets);
		bgckDao.save(tBgck);		
		
		OperalogServiceImpl.addOperalog(bgck.getCreateId(), bgck.getBmbh(), bgck.getMenuId(), tBgck.getBgcklsh(), "生成保管出库", operalogDao);

		Bgck rBgck = new Bgck();
		rBgck.setBgcklsh(lsh);
		return rBgck;
	}
	
	@Override
	public void cjBgck(Bgck bgck) {
		Date now = new Date();

		//获取原单据信息
		TBgck yTBgck = bgckDao.get(TBgck.class, bgck.getBgcklsh());
		//新增冲减单据信息
		TBgck tBgck = new TBgck();
		BeanUtils.copyProperties(yTBgck, tBgck);
		
		yTBgck.setCjId(bgck.getCjId());
		yTBgck.setCjTime(now);
		yTBgck.setCjName(bgck.getCjName());
		yTBgck.setIsCj("1");

        String lsh = LshServiceImpl.updateLsh(yTBgck.getBmbh(), bgck.getLxbh(), lshDao);
		tBgck.setBgcklsh(lsh);
		tBgck.setCjBgcklsh(yTBgck.getBgcklsh());
		tBgck.setCreateTime(now);
		tBgck.setCreateId(bgck.getCjId());
		tBgck.setCreateName(bgck.getCjName());
		tBgck.setIsCj("1");
		tBgck.setCjId(bgck.getCjId());
		tBgck.setCjTime(now);
		tBgck.setCjName(bgck.getCjName());

		Department dep = new Department();
		dep.setId(yTBgck.getBmbh());
		dep.setDepName(yTBgck.getBmmc());
		
		Set<TBgckDet> yTBgckDets = yTBgck.getTBgckDets();
		Set<TBgckDet> tDets = new HashSet<TBgckDet>();
        TBgckDet tDet;
		for(TBgckDet yTDet : yTBgckDets){
			tDet = new TBgckDet();
			BeanUtils.copyProperties(yTDet, tDet, new String[]{"id"});
			tDet.setZdwsl(yTDet.getZdwsl().negate());

			tDet.setTBgck(tBgck);
			tDets.add(tDet);
			
			Sp sp = new Sp();
			BeanUtils.copyProperties(tDet, sp);
			
			//更新保管总账
			BgzzServiceImpl.updateBgzzSl(sp, dep, tDet.getZdwsl(), Constant.UPDATE_CK, bgzzDao);
			
		}
		
		tBgck.setTBgckDets(tDets);
		bgckDao.save(tBgck);
		
		OperalogServiceImpl.addOperalog(bgck.getCjId(), yTBgck.getBmbh(), bgck.getMenuId(), tBgck.getCjBgcklsh() + "/" + tBgck.getBgcklsh(), "冲减保管出库", operalogDao);
	}
	
	@Override
	public DataGrid datagrid(Bgck bgck) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TBgck t where (t.bmbh = :bmbh1 or t.bmbh = :bmbh2) and t.createTime > :createTime and t.createId = :createId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh1", "04");
        params.put("bmbh2", "05");
        params.put("createId", bgck.getCreateId());
		if(bgck.getCreateTime() != null){
			params.put("createTime", bgck.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		if(bgck.getSearch() != null){
			hql += " and (" +
					Util.getQueryWhere(bgck.getSearch(), new String[]{"t.bgcklsh", "t.khbh", "t.khmc", "t.bz"}, params)
					+ ")";
		}
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TBgck> l = bgckDao.find(hql, params, bgck.getPage(), bgck.getRows());
		List<Bgck> nl = new ArrayList<Bgck>();
        Bgck c;
		for(TBgck t : l){
			c = new Bgck();
			BeanUtils.copyProperties(t, c);

			nl.add(c);
			
		}
		datagrid.setTotal(bgckDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(String bgcklsh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TBgckDet t where t.TBgck.bgcklsh = :bgcklsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bgcklsh", bgcklsh);
		List<TBgckDet> l = detDao.find(hql, params);
		List<BgckDet> nl = new ArrayList<BgckDet>();
		for(TBgckDet t : l){
			BgckDet c = new BgckDet();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}

	@Autowired
	public void setBgckDao(BaseDaoI<TBgck> bgckDao) {
		this.bgckDao = bgckDao;
	}

	@Autowired
	public void setKfzzDao(BaseDaoI<TKfzz> kfzzDao) {
		this.kfzzDao = kfzzDao;
	}

	@Autowired
	public void setBgzzDao(BaseDaoI<TBgzz> bgzzDao) {
		this.bgzzDao = bgzzDao;
	}

	@Autowired
	public void setDetDao(BaseDaoI<TBgckDet> detDao) {
		this.detDao = detDao;
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
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}

}
