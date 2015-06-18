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
import lnyswz.jxc.bean.Rklx;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TRklx; //import lnyswz.jxc.model.TRklxDet;
import lnyswz.jxc.service.RklxServiceI;

@Service("rklxService")
public class RklxServiceImpl implements RklxServiceI {
	private BaseDaoI<TRklx> rklxDao;
	private BaseDaoI<TOperalog> opeDao;

	/**
	 * 保存入库类型
	 */
	@Override
	public Rklx add(Rklx rklx) {
		TRklx t = new TRklx();
		BeanUtils.copyProperties(rklx, t);
		rklxDao.save(t);
		Rklx r = new Rklx();
		BeanUtils.copyProperties(t, r);
		OperalogServiceImpl.addOperalog(rklx.getUserId(), rklx.getDepId(), rklx
				.getMenuId(), rklx.getId(), "添加入库类型信息", opeDao);
		return r;
	}

	/**
	 * 编辑入库类型
	 */
	@Override
	public void edit(Rklx rklx) {
		TRklx g = rklxDao.load(TRklx.class, rklx.getId());
		BeanUtils.copyProperties(rklx, g);
		OperalogServiceImpl.addOperalog(rklx.getUserId(), rklx.getDepId(), rklx
				.getMenuId(), rklx.getId(), "编辑入库类型信息", opeDao);

	}

	/**
	 * 删除入库类型
	 */
	@Override
	public void delete(Rklx rklx) {
		TRklx t = rklxDao.load(TRklx.class, rklx.getId());
		rklxDao.delete(t);
		OperalogServiceImpl.addOperalog(rklx.getUserId(), rklx.getDepId(), rklx
				.getMenuId(), rklx.getId(), "删除入库类型信息", opeDao);

	}

	/**
	 * 不带分页的
	 */
	@Override
	public List<Rklx> listRklx(Rklx rklx) {
		String hql = "from TRklx t ";
		List<TRklx> list = rklxDao.find(hql);
		return changeRklx(list);
	}

	@Override
	public DataGrid datagrid(Rklx rklx) {
		DataGrid dg = new DataGrid();
		String hql = "from TRklx t ";
		// 获得总条数
		String totalHql = "select count(*) " + hql;
		// 传入页码、每页条数
		List<TRklx> l = rklxDao.find(hql, rklx.getPage(), rklx.getRows());
		// 处理返回信息
		dg.setTotal(rklxDao.count(totalHql));
		dg.setRows(changeRklx(l));
		return dg;
	}

	private List<Rklx> changeRklx(List<TRklx> l) {
		List<Rklx> nl = new ArrayList<Rklx>();
		for (TRklx t : l) {
			Rklx nc = new Rklx();
			BeanUtils.copyProperties(t, nc);
			nl.add(nc);
		}
		return nl;
	}

	@Autowired
	public void setRklxDao(BaseDaoI<TRklx> rklxDao) {
		this.rklxDao = rklxDao;
	}

	@Autowired
	public void setOpeDao(BaseDaoI<TOperalog> opeDao) {
		this.opeDao = opeDao;
	}
}
