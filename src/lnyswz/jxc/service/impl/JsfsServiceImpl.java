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
import lnyswz.jxc.bean.Jsfs;
import lnyswz.jxc.model.TJsfs; //import lnyswz.jxc.model.TJsfsDet;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.service.JsfsServiceI;

@Service("jsfsService")
public class JsfsServiceImpl implements JsfsServiceI {
	private BaseDaoI<TJsfs> jsfsDao;
	private BaseDaoI<TOperalog> opeDao;

	/**
	 * 保存结算方式
	 */
	@Override
	public Jsfs add(Jsfs jsfs) {
		TJsfs t = new TJsfs();
		BeanUtils.copyProperties(jsfs, t);
		jsfsDao.save(t);
		Jsfs r = new Jsfs();
		BeanUtils.copyProperties(t, r);
		OperalogServiceImpl.addOperalog(jsfs.getUserId(), jsfs.getDepId(), jsfs
				.getMenuId(), jsfs.getId(), "添加结算方式信息", opeDao);
		return r;
	}

	/**
	 * 编辑结算方式
	 */
	@Override
	public void edit(Jsfs jsfs) {
		TJsfs g = jsfsDao.load(TJsfs.class, jsfs.getId());
		BeanUtils.copyProperties(jsfs, g);
		OperalogServiceImpl.addOperalog(jsfs.getUserId(), jsfs.getDepId(), jsfs
				.getMenuId(), jsfs.getId(), "编辑结算方式信息", opeDao);
	}

	/**
	 * 删除结算方式
	 */
	@Override
	public void delete(Jsfs jsfs) {
		TJsfs t = jsfsDao.load(TJsfs.class, jsfs.getId());
		jsfsDao.delete(t);
		OperalogServiceImpl.addOperalog(jsfs.getUserId(), jsfs.getDepId(), jsfs
				.getMenuId(), jsfs.getId(), "删除结算方式信息", opeDao);
	}

	/**
	 * 不带分页
	 */
	@Override
	public List<Jsfs> listJsfs(Jsfs jsfs) {
		String hql = "from TJsfs t";
		List<TJsfs> list = jsfsDao.find(hql);
		return changeJsfs(list);
	}

	@Override
	public DataGrid datagrid(Jsfs c) {
		DataGrid dg = new DataGrid();
		String hql = "from TJsfs t ";
		// 获得总条数
		String totalHql = "select count(*) " + hql;
		// 传入页码、每页条数
		List<TJsfs> l = jsfsDao.find(hql,c.getPage(), c.getRows());
		// 处理返回信息
		dg.setTotal(jsfsDao.count(totalHql));
		dg.setRows(changeJsfs(l));
		return dg;
	}

	private List<Jsfs> changeJsfs(List<TJsfs> l) {
		List<Jsfs> nl = new ArrayList<Jsfs>();
		for (TJsfs t : l) {
			Jsfs nc = new Jsfs();
			BeanUtils.copyProperties(t, nc);
			nl.add(nc);
		}
		return nl;
	}

	@Autowired
	public void setJsfsDao(BaseDaoI<TJsfs> jsfsDao) {
		this.jsfsDao = jsfsDao;
	}

	@Autowired
	public void setOpeDao(BaseDaoI<TOperalog> opeDao) {
		this.opeDao = opeDao;
	}
}
