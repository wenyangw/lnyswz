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
import lnyswz.jxc.bean.Ck;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TCk;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.service.CkServiceI;

@Service("ckService")
public class CkServiceImpl implements CkServiceI {
	private BaseDaoI<TCk> ckDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TOperalog> opeDao;

	/**
	 * 保存仓库
	 */
	@Override
	public Ck add(Ck ck) {
		TCk t = new TCk();
		BeanUtils.copyProperties(ck, t);
		TDepartment dep = depDao.load(TDepartment.class, ck.getDid());
		t.setTDepartment(dep);
		ckDao.save(t);
		Ck r = new Ck();
		BeanUtils.copyProperties(t, r);
		r.setDname(dep.getDepName());
		r.setDid(ck.getDid());
		OperalogServiceImpl.addOperalog(ck.getUserId(), ck.getDepId(), ck
				.getMenuId(), ck.getId(), "添加仓库信息", opeDao);
		return r;

	}

	/**
	 * 编辑仓库
	 */
	@Override
	public void edit(Ck ck) {
		TCk g = ckDao.load(TCk.class, ck.getId());
		BeanUtils.copyProperties(ck, g);
		TDepartment dep = depDao.load(TDepartment.class, ck.getDid());
		g.setTDepartment(dep);
		OperalogServiceImpl.addOperalog(ck.getUserId(), ck.getDepId(), ck
				.getMenuId(), ck.getId(), "编辑仓库信息", opeDao);
	}

	/**
	 * 删除仓库
	 */
	@Override
	public boolean delete(Ck ck) {
		boolean isOk = false;
		TCk t = ckDao.load(TCk.class, ck.getId());
		OperalogServiceImpl.addOperalog(ck.getUserId(), ck.getDepId(), ck
				.getMenuId(), ck.getId(), "删除仓库信息", opeDao);
		if (t.getTHws().size() == 0) {
			ckDao.delete(t);
			isOk = true;
			return isOk;
		}
		return isOk;
	}

	@Override
	public List<Ck> listCk(Ck ck) {
		String hql = "from TCk t where t.TDepartment.id = :depId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", ck.getDepId());
		List<TCk> l = ckDao.find(hql, params);
		return changeCk(l);
	}

	/**
	 * 数据转换
	 * 
	 * @param l
	 * @return
	 */
	private List<Ck> changeCk(List<TCk> l) {
		List<Ck> nl = new ArrayList<Ck>();
		for (TCk t : l) {
			Ck r = new Ck();
			BeanUtils.copyProperties(t, r);
			nl.add(r);
		}
		return nl;
	}

	@Override
	public DataGrid datagrid(Ck c) {
		DataGrid dg = new DataGrid();
		String hql = "from TCk t ";
		String totalHql = "select count(*) " + hql;
		List<Ck> nl = new ArrayList<Ck>();
		// 传入页码、每页条数
		List<TCk> l = ckDao.find(hql, c.getPage(), c.getRows());
		// 处理返回信息
		for (TCk t : l) {
			Ck nc = new Ck();
			BeanUtils.copyProperties(t, nc);
			TDepartment dep = t.getTDepartment();
			nc.setDname(dep.getDepName());
			nc.setDid(dep.getId());
			nl.add(nc);
		}
		dg.setTotal(ckDao.count(totalHql));
		dg.setRows(nl);
		return dg;
	}

	@Autowired
	public void setCkDao(BaseDaoI<TCk> ckDao) {
		this.ckDao = ckDao;
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
