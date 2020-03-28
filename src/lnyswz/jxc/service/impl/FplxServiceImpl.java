package lnyswz.jxc.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Fplx;
import lnyswz.jxc.model.TFplx; //import lnyswz.jxc.model.TFplxDet;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.service.FplxServiceI;

@Service("fplxService")
public class FplxServiceImpl implements FplxServiceI {
	private BaseDaoI<TFplx> fplxDao;
	private BaseDaoI<TOperalog> opeDao;

	/**
	 * 保存发票类型
	 */
	@Override
	public Fplx add(Fplx fplx) {
		TFplx t = new TFplx();
		BeanUtils.copyProperties(fplx, t);
		fplxDao.save(t);
		Fplx r = new Fplx();
		BeanUtils.copyProperties(t, r);
		OperalogServiceImpl.addOperalog(fplx.getUserId(), fplx.getDepId(), fplx
				.getMenuId(), fplx.getId(), "添加发票类型信息", opeDao);
		return r;

	}

	/**
	 * 编辑发票类型
	 */
	@Override
	public void edit(Fplx fplx) {
		TFplx g = fplxDao.load(TFplx.class, fplx.getId());
		BeanUtils.copyProperties(fplx, g);
		OperalogServiceImpl.addOperalog(fplx.getUserId(), fplx.getDepId(), fplx
				.getMenuId(), fplx.getId(), "编辑发票类型信息", opeDao);
	}

	/**
	 * 删除发票类型
	 */
	@Override
	public void delete(Fplx fplx) {
		TFplx t = fplxDao.load(TFplx.class, fplx.getId());
		fplxDao.delete(t);
		OperalogServiceImpl.addOperalog(fplx.getUserId(), fplx.getDepId(), fplx
				.getMenuId(), fplx.getId(), "删除发票类型信息", opeDao);
	}

	/**
	 * 不带分页的
	 */
	@Override
	public List<Fplx> listFplx(Fplx fplx) {
		String hql = "from TFplx t ";
		List<TFplx> list = fplxDao.find(hql);
		return changeFplx(list);
	}

	@Override
	public DataGrid datagrid(Fplx fplx) {
		DataGrid dg = new DataGrid();
		String hql = "from TFplx t ";
		String totalHql = "select count(*) " + hql;
		List<TFplx> l = fplxDao.find(hql, fplx.getPage(), fplx.getRows());
		// 处理返回信息
		dg.setTotal(fplxDao.count(totalHql));
		dg.setRows(changeFplx(l));
		return dg;
	}

	private List<Fplx> changeFplx(List<TFplx> l) {
		List<Fplx> nl = new ArrayList<Fplx>();
		for (TFplx t : l) {
			Fplx nc = new Fplx();
			BeanUtils.copyProperties(t, nc);
			nl.add(nc);
		}
		return nl;
	}

	@Autowired
	public void setFplxDao(BaseDaoI<TFplx> fplxDao) {
		this.fplxDao = fplxDao;
	}

	@Autowired
	public void setOpeDao(BaseDaoI<TOperalog> opeDao) {
		this.opeDao = opeDao;
	}

}
