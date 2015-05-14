package lnyswz.jxc.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Jldw;
import lnyswz.jxc.model.TJldw;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.service.JldwServiceI;

@Service("jldwService")
public class JldwServiceImpl implements JldwServiceI {
	private BaseDaoI<TJldw> jldwDao;
	private BaseDaoI<TOperalog> opeDao;

	/**
	 * 保存计量单位
	 */
	@Override
	public Jldw add(Jldw jldw) {
		TJldw t = new TJldw();
		BeanUtils.copyProperties(jldw, t);
		jldwDao.save(t);
		Jldw r = new Jldw();
		BeanUtils.copyProperties(t, r);
		OperalogServiceImpl.addOperalog(jldw.getUserId(), jldw.getDepId(), jldw
				.getMenuId(), jldw.getId(), "添加计量单位信息", opeDao);
		return r;
	}

	/**
	 * 编辑计量单位
	 */
	@Override
	public void edit(Jldw jldw) {
		TJldw g = jldwDao.load(TJldw.class, jldw.getId());
		BeanUtils.copyProperties(jldw, g);
		OperalogServiceImpl.addOperalog(jldw.getUserId(), jldw.getDepId(), jldw
				.getMenuId(), jldw.getId(), "编辑计量单位信息", opeDao);
	}

	/**
	 * 删除计量单位
	 */
	@Override
	public void delete(Jldw jldw) {
		TJldw t = jldwDao.load(TJldw.class, jldw.getId());
		jldwDao.delete(t);
		OperalogServiceImpl.addOperalog(jldw.getUserId(), jldw.getDepId(), jldw
				.getMenuId(), jldw.getId(), "删除计量单位信息", opeDao);
	}

	/**
	 * 不带分页的
	 */
	@Override
	public List<Jldw> listJldw(Jldw jldw) {
		String hql = "from TJldw t ";
		List<TJldw> list = jldwDao.find(hql);
		return changeJldw(list);
	}

	@Override
	public DataGrid datagrid(Jldw jldw) {
		DataGrid dg = new DataGrid();
		String hql = "from TJldw t ";
		// 获得总条数
		String totalHql = "select count(*) " + hql;
		// 传入页码、每页条数
		List<TJldw> l = jldwDao.find(hql,jldw.getPage(), jldw
				.getRows());
		// 处理返回信息
		dg.setTotal(jldwDao.count(totalHql));
		dg.setRows(changeJldw(l));
		return dg;
	}

	private List<Jldw> changeJldw(List<TJldw> l) {
		List<Jldw> nl = new ArrayList<Jldw>();
		for (TJldw t : l) {
			Jldw nc = new Jldw();
			BeanUtils.copyProperties(t, nc);
			nl.add(nc);
		}
		return nl;
	}

	@Autowired
	public void setJldwDao(BaseDaoI<TJldw> jldwDao) {
		this.jldwDao = jldwDao;
	}

	@Autowired
	public void setOpeDao(BaseDaoI<TOperalog> opeDao) {
		this.opeDao = opeDao;
	}

}
