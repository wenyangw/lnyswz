package lnyswz.jxc.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Rylx;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TRylx;
import lnyswz.jxc.service.RylxServiceI;

@Service("rylxService")
public class RylxServiceImpl implements RylxServiceI {
	private BaseDaoI<TRylx> rylxDao;
	private BaseDaoI<TOperalog> opeDao;

	/**
	 * 保存人员类型
	 */
	@Override
	public Rylx add(Rylx rylx) {
		TRylx t = new TRylx();
		BeanUtils.copyProperties(rylx, t);
		rylxDao.save(t);
		Rylx r = new Rylx();
		BeanUtils.copyProperties(t, r);
		OperalogServiceImpl.addOperalog(rylx.getUserId(), rylx.getDepId(), rylx
				.getMenuId(), rylx.getId(), "添加人员类型信息", opeDao);
		return r;
	}

	/**
	 * 编辑人员类型
	 */
	@Override
	public void edit(Rylx rylx) {
		TRylx g = rylxDao.load(TRylx.class, rylx.getId());
		BeanUtils.copyProperties(rylx, g);
		OperalogServiceImpl.addOperalog(rylx.getUserId(), rylx.getDepId(), rylx
				.getMenuId(), rylx.getId(), "编辑人员类型信息", opeDao);
	}

	/**
	 * 删除人员类型
	 */
	@Override
	public void delete(Rylx rylx) {
		TRylx t = rylxDao.load(TRylx.class, rylx.getId());
		rylxDao.delete(t);
		OperalogServiceImpl.addOperalog(rylx.getUserId(), rylx.getDepId(), rylx
				.getMenuId(), rylx.getId(), "删除人员类型信息", opeDao);
	}

	/**
	 * 不带分页的
	 */
	@Override
	public List<Rylx> listRylx(Rylx rylx) {
		String hql = "from TRylx t ";
		List<TRylx> list = rylxDao.find(hql);
		return changeRylx(list);
	}

	@Override
	public DataGrid datagrid(Rylx rylx) {
		DataGrid dg = new DataGrid();
		String hql = "from TRylx t ";
		// 获得总条数
		String totalHql = "select count(*) " + hql;
		// 传入页码、每页条数
		List<TRylx> l = rylxDao.find(hql, rylx.getPage(), rylx.getRows());
		// 处理返回信息
		dg.setTotal(rylxDao.count(totalHql));
		dg.setRows(changeRylx(l));
		return dg;
	}

	private List<Rylx> changeRylx(List<TRylx> l) {
		List<Rylx> nl = new ArrayList<Rylx>();
		for (TRylx t : l) {
			Rylx nc = new Rylx();
			BeanUtils.copyProperties(t, nc);
			nl.add(nc);
		}
		return nl;
	}

	@Autowired
	public void setRylxDao(BaseDaoI<TRylx> rylxDao) {
		this.rylxDao = rylxDao;
	}

	@Autowired
	public void setOpeDao(BaseDaoI<TOperalog> opeDao) {
		this.opeDao = opeDao;
	}
}
