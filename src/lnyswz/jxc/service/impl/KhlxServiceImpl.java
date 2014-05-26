package lnyswz.jxc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Khlx;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TKhlx;
import lnyswz.jxc.service.KhlxServiceI;

@Service("khlxService")
public class KhlxServiceImpl implements KhlxServiceI {
	private BaseDaoI<TKhlx> khlxDao;
	private BaseDaoI<TOperalog> opeDao;

	/**
	 * 保存入库类型
	 */
	@Override
	public Khlx add(Khlx khlx) {
		TKhlx t = new TKhlx();
		BeanUtils.copyProperties(khlx, t);
		khlxDao.save(t);
		Khlx r = new Khlx();
		BeanUtils.copyProperties(t, r);
		OperalogServiceImpl.addOperalog(khlx.getUserId(), khlx.getDepId(), khlx
				.getMenuId(), khlx.getId(), "添加入库类型信息", opeDao);
		return r;
	}

	/**
	 * 编辑入库类型
	 */
	@Override
	public void edit(Khlx khlx) {
		TKhlx g = khlxDao.load(TKhlx.class, khlx.getId());
		BeanUtils.copyProperties(khlx, g);
		OperalogServiceImpl.addOperalog(khlx.getUserId(), khlx.getDepId(), khlx
				.getMenuId(), khlx.getId(), "编辑入库类型信息", opeDao);

	}

	/**
	 * 删除入库类型
	 */
	@Override
	public void delete(Khlx khlx) {
		TKhlx t = khlxDao.load(TKhlx.class, khlx.getId());
		khlxDao.delete(t);
		OperalogServiceImpl.addOperalog(khlx.getUserId(), khlx.getDepId(), khlx
				.getMenuId(), khlx.getId(), "删除入库类型信息", opeDao);

	}

	/**
	 * 不带分页的
	 */
	@Override
	public List<Khlx> listKhlx(Khlx khlx) {
		String hql = "from TKhlx t ";
		List<TKhlx> list = khlxDao.find(hql);
		return changeKhlx(list);
	}

	@Override
	public DataGrid datagrid(Khlx khlx) {
		DataGrid dg = new DataGrid();
		String hql = "from TKhlx t ";
		// 获得总条数
		String totalHql = "select count(*) " + hql;
		// 传入页码、每页条数
		List<TKhlx> l = khlxDao.find(hql, khlx.getPage(), khlx.getRows());
		// 处理返回信息
		dg.setTotal(khlxDao.count(totalHql));
		dg.setRows(changeKhlx(l));
		return dg;
	}

	private List<Khlx> changeKhlx(List<TKhlx> l) {
		List<Khlx> nl = new ArrayList<Khlx>();
		for (TKhlx t : l) {
			Khlx nc = new Khlx();
			BeanUtils.copyProperties(t, nc);
			nl.add(nc);
		}
		return nl;
	}

	@Autowired
	public void setKhlxDao(BaseDaoI<TKhlx> khlxDao) {
		this.khlxDao = khlxDao;
	}

	@Autowired
	public void setOpeDao(BaseDaoI<TOperalog> opeDao) {
		this.opeDao = opeDao;
	}
}
