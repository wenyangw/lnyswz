package lnyswz.jxc.service.impl;

import java.util.*;

import lnyswz.jxc.util.Constant;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Gys;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TGys;
import lnyswz.jxc.model.TGysDet;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.service.GysServiceI;

@Service("gysService")
public class GysServiceImpl implements GysServiceI {
	private Logger logger = Logger.getLogger(SpServiceImpl.class);
	private BaseDaoI<TGys> gysDao;
	private BaseDaoI<TGysDet> gysdetDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TOperalog> opeDao;

	/**
	 * 保存供应商
	 */
	@Override
	public Gys add(Gys gys) {
		TGys t = new TGys();
		BeanUtils.copyProperties(gys, t);
		gysDao.save(t);
		OperalogServiceImpl.addOperalog(gys.getUserId(), gys.getDepId(), gys
				.getMenuId(), gys.getGysbh(), "添加供应商通用信息", opeDao);
		Gys r = new Gys();
		BeanUtils.copyProperties(t, r);
		return r;
		
	}

	/**
	 * 编辑供应商
	 */
	@Override
	public void edit(Gys gys) {
		TGys g = gysDao.load(TGys.class, gys.getGysbh());
		BeanUtils.copyProperties(gys, g);
		OperalogServiceImpl.addOperalog(gys.getUserId(), gys.getDepId(), gys
				.getMenuId(), g.getGysbh(), "修改供应商通用信息", opeDao);
	}

	/**
	 * 删除供应商
	 */
	@Override
	public boolean delete(Gys gys) {
		boolean isOK = false;
		TGys t = gysDao.load(TGys.class, gys.getGysbh());
		if (t.getTGysDets().size() == 0) {
			gysDao.delete(t);
			isOK = true;
		}
		OperalogServiceImpl.addOperalog(gys.getUserId(), gys.getDepId(), gys
				.getMenuId(), gys.getGysbh(), "删除供应商通用信息", opeDao);
		return isOK;
	}

	/**
	 * 编辑供应商专属信息
	 */
	@Override
	public void editDet(Gys gys) {
		String keyId;
		if (gys.getDetId().equals("")) {
			TGys g = gysDao.load(TGys.class, gys.getGysbh());
			Set<TGysDet> gysDets = g.getTGysDets();
			TGysDet tGysDet = new TGysDet();
			BeanUtils.copyProperties(gys, tGysDet);
			tGysDet.setId(UUID.randomUUID().toString());
			tGysDet.setTGys(g);
			tGysDet.setTDepartment(depDao.load(TDepartment.class, gys
					.getDepId()));
			gysDets.add(tGysDet);
			keyId=gys.getGysbh()+gys.getDetId();
		} else {
			TGysDet v = gysdetDao.load(TGysDet.class, gys.getDetId());
			BeanUtils.copyProperties(gys, v);
			keyId = gys.getGysbh() + gys.getDetId();
		}
		OperalogServiceImpl.addOperalog(gys.getUserId(), gys.getDepId(), gys
				.getMenuId(), keyId, "编辑供应商专属信息", opeDao);
	}

	/**
	 * 删除供应商专属信息
	 */
	public void deleteDet(Gys gys) {
		TGys t = gysDao.load(TGys.class, gys.getGysbh());
		Set<TGysDet> dets = t.getTGysDets();
		t.setTGysDets(null);
		TGysDet det = gysdetDao.load(TGysDet.class, gys.getDetId());
		dets.remove(det);
		gysdetDao.delete(det);
		t.setTGysDets(dets);
		String keyId = t.getGysbh() + det.getId();
		OperalogServiceImpl.addOperalog(gys.getUserId(), gys.getDepId(), gys
				.getMenuId(), keyId, "删除供应商专属信息", opeDao);
	}

	/**
	 * 判断供应商编号是否重复
	 */
	@Override
	public boolean existGys(Gys gys) {
		boolean isOK = false;
		String hql = "from TGys t where t.gysbh = :gysbh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gysbh", gys.getGysbh());
		List<TGys> list = gysDao.find(hql, params);
		if (list.size() != 0) {
			isOK = true;
		}
		return isOK;
	}

	/**
	 * 不带分页
	 */
	@Override
	public List<Gys> listGys(Gys gys) {
		String hql = "from TGys t";
		List<TGys> list = gysDao.find(hql);

		return changeGys(list);
	}

	@Override
	public DataGrid datagrid(Gys c) {
		DataGrid dg = new DataGrid();
		String hql = "from TGys t ";
		Map<String, Object> params = new HashMap<String, Object>();
		// hql语句拼写
		hql = seletWhere(c, hql, params);
		// 获得总条数
		String totalHql = "select count(*) " + hql;
		List<Gys> nl = new ArrayList<Gys>();
		// 传入页码、每页条数
		List<TGys> l = gysDao.find(hql, params, c.getPage(), c.getRows());
		// 处理返回信息

		for (TGys t : l) {
			Gys nc = new Gys();
			BeanUtils.copyProperties(t, nc);
			if (c.getDepId() != null) {
				Set<TGysDet> gysdet = t.getTGysDets();
				for (TGysDet m : gysdet) {
					if (m.getTDepartment().getId().trim().equals(c.getDepId().trim())) {
						nc.setLxr(m.getLxr());
						nc.setSxzq(m.getSxzq());
						nc.setSxje(m.getSxje());
						nc.setDetId(m.getId());
					}
				}
			}
			nl.add(nc);
		}

		dg.setTotal(gysDao.count(totalHql, params));
		dg.setRows(nl);
		return dg;
	}

	// 条件筛选hql语句拼写
	private String seletWhere(Gys c, String hql, Map<String, Object> params) {
		if (c.getGyscs() != null && !c.getGyscs().trim().equals("")) {
			hql += "where t.gysbh like :gysbh or t.gysmc like :gysmc";
			params.put("gysbh", c.getGyscs().trim() + "%");
			params.put("gysmc", "%" + c.getGyscs().trim() + "%");
		}
		return hql;
	}

	private List<Gys> changeGys(List<TGys> l) {
		List<Gys> nl = new ArrayList<Gys>();
		for (TGys t : l) {
			Gys nc = new Gys();
			BeanUtils.copyProperties(t, nc);
			nc.setBh(t.getGysbh());
			nc.setMc(t.getGysmc());
			nl.add(nc);
		}
		return nl;
	}
	
	
	/**
	 * 王文阳
	 * 2013.10.08
	 * 获得供应商信息
	 */
	@Override
	public Gys loadGys(String gysbh) {
		// String hql = "from TGys t where t.gysbh = :gysbh";
		// Map<String, Object> params = new HashMap<String, Object>();
		// params.put("gysbh", gysbh);
		// TGys t = gysDao.find(hql, params).load(0);
		TGys t = gysDao.load(TGys.class, gysbh);
		Gys g = new Gys();
		BeanUtils.copyProperties(t, g);
		return g;
	}
	
	
	/**
	 * 王文阳
	 * 2013.10.08
	 * 获得供应商检索信息
	 */
	@Override
	public DataGrid gysDg(Gys gys) {
		String hql = "from TGys t";
		String countHql = "select count(*) " + hql;
		Map<String, Object> params = new HashMap<String, Object>();
		if(gys.getQuery() != null && gys.getQuery().trim().length() > 0){
			String where = " where t.gysbh like :gysbh or t.gysmc like :gysmc"; 
			hql += where;
			countHql += where;
			params.put("gysbh", gys.getQuery() + "%");
			params.put("gysmc", "%" + gys.getQuery() + "%");
		}
		DataGrid dg = new DataGrid();
		dg.setTotal(gysDao.count(countHql, params));
		dg.setRows(changeGys(gysDao.find(hql, params, gys.getPage(), gys.getRows())));
		return dg;
	}
	

	@Autowired
	public void setGysDao(BaseDaoI<TGys> gysDao) {
		this.gysDao = gysDao;
	}

	@Autowired
	public void setGysdetDao(BaseDaoI<TGysDet> gysdetDao) {
		this.gysdetDao = gysdetDao;
	}

	@Autowired
	public void setDepDao(BaseDaoI<TDepartment> depDao) {
		this.depDao = depDao;
	}
	@Autowired
	public void setOpeDao(BaseDaoI<TOperalog> opeDao) {
		this.opeDao = opeDao;
	}
	

}
