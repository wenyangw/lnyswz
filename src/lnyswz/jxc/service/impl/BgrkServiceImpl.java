package lnyswz.jxc.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.ProBean;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.Common;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.*;
import lnyswz.jxc.model.*;
import lnyswz.jxc.service.BgrkServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Util;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 采购需求实现类
 * @author 王文阳
 *
 */
@Service("bgrkService")
public class BgrkServiceImpl implements BgrkServiceI {
	private Logger logger = Logger.getLogger(BgrkServiceImpl.class);
	private BaseDaoI<TBgrk> bgrkDao;
	private BaseDaoI<TBgrkDet> detDao;
	private BaseDaoI<TKfzz> kfzzDao;
	private BaseDaoI<TBgzz> bgzzDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TSp> spDao;
	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public Bgrk save(Bgrk bgrk) {
		String lsh = LshServiceImpl.updateLsh(bgrk.getBmbh(), bgrk.getLxbh(), lshDao);
		TBgrk tBgrk = new TBgrk();
		BeanUtils.copyProperties(bgrk, tBgrk);
		tBgrk.setCreateTime(new Date());
		tBgrk.setBgrklsh(lsh);
		tBgrk.setBmmc(depDao.load(TDepartment.class, bgrk.getBmbh()).getDepName());
		
		tBgrk.setIsCj("0");

		Department dep = new Department();
		dep.setId(bgrk.getBmbh());
		dep.setDepName(tBgrk.getBmmc());

		//处理商品明细
		Set<TBgrkDet> tDets = new HashSet<TBgrkDet>();
		ArrayList<BgrkDet> bgrkDets = JSON.parseObject(bgrk.getDatagrid(), new TypeReference<ArrayList<BgrkDet>>(){});
		for(BgrkDet bgrkDet : bgrkDets){
			if(bgrkDet.getSpbh() != null) {
				TBgrkDet tDet = new TBgrkDet();
				BeanUtils.copyProperties(bgrkDet, tDet, new String[]{"id"});

				if ("".equals(bgrkDet.getCjldwId()) || bgrkDet.getZhxs() == null || bgrkDet.getZhxs().compareTo(Constant.BD_ZERO) == 0) {
					tDet.setZhxs(Constant.BD_ZERO);
					bgrkDet.setZhxs(Constant.BD_ZERO);
				}

				tDet.setTBgrk(tBgrk);
				tDets.add(tDet);

				Sp sp = new Sp();
				BeanUtils.copyProperties(bgrkDet, sp);

				//更新库房总账
				BgzzServiceImpl.updateBgzzSl(sp, dep, bgrkDet.getZdwsl(), Constant.UPDATE_RK, bgzzDao);
			}
		}
		tBgrk.setTBgrkDets(tDets);
		bgrkDao.save(tBgrk);		
		OperalogServiceImpl.addOperalog(bgrk.getCreateId(), bgrk.getBmbh(), bgrk.getMenuId(), tBgrk.getBgrklsh(), "生成保管入库单", operalogDao);

		bgrk.setBgrklsh(lsh);
		return bgrk;
	}
	
	@Override
	public void cjBgrk(Bgrk bgrk) {
		Date now = new Date(); 
		//获取原单据信息
		TBgrk yTBgrk = bgrkDao.get(TBgrk.class, bgrk.getBgrklsh());
		//新增冲减单据信息
		TBgrk tBgrk = new TBgrk();
		BeanUtils.copyProperties(yTBgrk, tBgrk);
		//更新原单据冲减信息
		yTBgrk.setCjId(bgrk.getCjId());
		yTBgrk.setCjTime(now);
		yTBgrk.setCjName(bgrk.getCjName());
		yTBgrk.setIsCj("1");
		
		//更新新单据信息
		String lsh = LshServiceImpl.updateLsh(tBgrk.getBmbh(), bgrk.getLxbh(), lshDao);
		tBgrk.setBgrklsh(lsh);
		tBgrk.setCreateTime(now);
		tBgrk.setCreateId(bgrk.getCjId());
		tBgrk.setCreateName(bgrk.getCjName());
		tBgrk.setIsCj("1");
		tBgrk.setCjTime(new Date());
		tBgrk.setCjId(bgrk.getCjId());
		tBgrk.setCjName(bgrk.getCjName());
		tBgrk.setCjBgrklsh(yTBgrk.getBgrklsh());
		tBgrk.setBz(bgrk.getBz());
		
		Department dep = new Department();
		dep.setId(tBgrk.getBmbh());
		dep.setDepName(tBgrk.getBmmc());

		Set<TBgrkDet> yTBgrkDets = yTBgrk.getTBgrkDets();
		Set<TBgrkDet> tDets = new HashSet<TBgrkDet>();
        TBgrkDet tDet;
		for(TBgrkDet yTDet : yTBgrkDets){
			tDet = new TBgrkDet();
			BeanUtils.copyProperties(yTDet, tDet, new String[]{"id"});
			tDet.setZdwsl(yTDet.getZdwsl().negate());

			tDet.setTBgrk(tBgrk);
			tDets.add(tDet);
			
			Sp sp = new Sp();
			BeanUtils.copyProperties(tDet, sp);

			//更新保管总账
			BgzzServiceImpl.updateBgzzSl(sp, dep, tDet.getZdwsl(), Constant.UPDATE_RK, bgzzDao);
		}
		tBgrk.setTBgrkDets(tDets);
		bgrkDao.save(tBgrk);		
		OperalogServiceImpl.addOperalog(bgrk.getCjId(), tBgrk.getBmbh(), bgrk.getMenuId(), tBgrk.getCjBgrklsh() + "/" + tBgrk.getBgrklsh(), "冲减保管入库", operalogDao);
	}

	@Override
	public DataGrid datagrid(Bgrk bgrk) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TBgrk t where (t.bmbh = :bmbh1 or t.bmbh = :bmbh2) and t.createTime > :createTime and t.createId = :createId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh1", "04");
        params.put("bmbh2", "05");
        params.put("createId", bgrk.getCreateId());
		if(bgrk.getCreateTime() != null){
			params.put("createTime", bgrk.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}


		if(bgrk.getSearch() != null){
			hql += " and (" + 
					Util.getQueryWhere(bgrk.getSearch(), new String[]{"t.bgrklsh", "t.gysmc", "t.bz", "t.lsh"}, params)
					+ ")";
		}
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TBgrk> l = bgrkDao.find(hql, params, bgrk.getPage(), bgrk.getRows());
		List<Bgrk> nl = new ArrayList<Bgrk>();
        Bgrk c;
		for(TBgrk t : l){
			c = new Bgrk();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setTotal(bgrkDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(String bgrklsh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TBgrkDet t where t.TBgrk.bgrklsh = :bgrklsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bgrklsh", bgrklsh);
		List<TBgrkDet> l = detDao.find(hql, params);
		List<BgrkDet> nl = new ArrayList<BgrkDet>();
		for(TBgrkDet t : l){
			BgrkDet c = new BgrkDet();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}

	@Override
	public DataGrid getSpkc(Bgrk bgrk) {
		DataGrid dg = new DataGrid();
		List<ProBean> lists = new ArrayList<ProBean>();

		List<ProBean> kf = KfzzServiceImpl.getZzsl(bgrk.getBmbh(), bgrk.getSpbh(), null, null, null, kfzzDao);
		if(kf != null){
			lists.addAll(kf);
		}

		List<ProBean> bg = BgzzServiceImpl.getZzsl(bgrk.getBmbh(), bgrk.getSpbh(), false, bgzzDao);
		if(bg != null){
			lists.addAll(bg);
		}

        List<ProBean> bgz = BgzzServiceImpl.getZzsl(bgrk.getBmbh(), bgrk.getSpbh(), true, bgzzDao);
        if(bgz != null){
            lists.addAll(bgz);
        }

		dg.setRows(lists);
		dg.setTotal((long)lists.size());
		return dg;
	}
	
	@Autowired
	public void setBgrkDao(BaseDaoI<TBgrk> bgrkDao) {
		this.bgrkDao = bgrkDao;
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
	public void setDetDao(BaseDaoI<TBgrkDet> detDao) {
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
	public void setSpDao(BaseDaoI<TSp> spDao) {
		this.spDao = spDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}

}
