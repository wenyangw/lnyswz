package lnyswz.jxc.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Pdlx;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TPdlx;
import lnyswz.jxc.service.PdlxServiceI;

@Service("pdlxService")
public class PdlxServiceImpl implements PdlxServiceI {
	private BaseDaoI<TPdlx> pdlxDao;
	private BaseDaoI<TOperalog> opeDao;

	/**
	 * 保存盘点类型
	 */
	@Override
	public Pdlx add(Pdlx pdlx) {
		TPdlx t = new TPdlx();
		BeanUtils.copyProperties(pdlx, t);
		pdlxDao.save(t);
		Pdlx r = new Pdlx();
		BeanUtils.copyProperties(t, r);
		OperalogServiceImpl.addOperalog(pdlx.getUserId(), pdlx.getDepId(), pdlx
				.getMenuId(), pdlx.getId(), "添加盘点类型信息", opeDao);
		return r;
	}

	/**
	 * 编辑盘点类型
	 */
	@Override
	public void edit(Pdlx pdlx) {
		TPdlx g = pdlxDao.load(TPdlx.class, pdlx.getId());
		BeanUtils.copyProperties(pdlx, g);
		OperalogServiceImpl.addOperalog(pdlx.getUserId(), pdlx.getDepId(), pdlx
				.getMenuId(), pdlx.getId(), "编辑盘点类型信息", opeDao);

	}

	/**
	 * 删除盘点类型
	 */
	@Override
	public void delete(Pdlx pdlx) {
		TPdlx t = pdlxDao.load(TPdlx.class, pdlx.getId());
		pdlxDao.delete(t);
		OperalogServiceImpl.addOperalog(pdlx.getUserId(), pdlx.getDepId(), pdlx
				.getMenuId(), pdlx.getId(), "删除盘点类型信息", opeDao);

	}

	/**
	 * 不带分页的
	 */
	@Override
	public List<Pdlx> listPdlx(Pdlx pdlx) {
		String hql = "from TPdlx t ";
		List<TPdlx> list = pdlxDao.find(hql);
		return changePdlx(list);
	}

	@Override
	public DataGrid datagrid(Pdlx pdlx) {
		DataGrid dg = new DataGrid();
		String hql = "from TPdlx t ";
		// 获得总条数
		String totalHql = "select count(*) " + hql;
		// 传入页码、每页条数
		List<TPdlx> l = pdlxDao.find(hql, pdlx.getPage(), pdlx.getRows());
		// 处理返回信息
		dg.setTotal(pdlxDao.count(totalHql));
		dg.setRows(changePdlx(l));
		return dg;
	}

	private List<Pdlx> changePdlx(List<TPdlx> l) {
		List<Pdlx> nl = new ArrayList<Pdlx>();
		for (TPdlx t : l) {
			Pdlx nc = new Pdlx();
			BeanUtils.copyProperties(t, nc);
			nl.add(nc);
		}
		return nl;
	}

	@Autowired
	public void setPdlxDao(BaseDaoI<TPdlx> pdlxDao) {
		this.pdlxDao = pdlxDao;
	}

	@Autowired
	public void setOpeDao(BaseDaoI<TOperalog> opeDao) {
		this.opeDao = opeDao;
	}
}
