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
import lnyswz.jxc.bean.Shdz;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TShdz;
import lnyswz.jxc.service.ShdzServiceI;
@Service("shdzService")
public class ShdzServiceImpl implements ShdzServiceI {
	private BaseDaoI<TShdz> shdzDao;
	private BaseDaoI<TOperalog> opeDao;

	/**
	 * 保存送货地址
	 */
	@Override
	public Shdz add(Shdz shdz) {
		TShdz t = new TShdz();
		BeanUtils.copyProperties(shdz, t);
		shdzDao.save(t);
		Shdz r = new Shdz();
		
		BeanUtils.copyProperties(t, r);
		OperalogServiceImpl.addOperalog(shdz.getUserId(), shdz.getDepId(), shdz
				.getMenuId(), String.valueOf(shdz.getId()), "添加送货地址信息", opeDao);
		return r;
	}

	/**
	 * 编辑送货地址
	 */
	@Override
	public void edit(Shdz shdz) {
		TShdz g = shdzDao.load(TShdz.class, shdz.getId());
		BeanUtils.copyProperties(shdz, g);
		OperalogServiceImpl.addOperalog(shdz.getUserId(), shdz.getDepId(), shdz
				.getMenuId(), String.valueOf(shdz.getId()), "编辑送货地址信息", opeDao);
	}

	/**
	 * 删除送货地址
	 */
	@Override
	public void delete(Shdz shdz) {
		TShdz t = shdzDao.load(TShdz.class, shdz.getId());
		shdzDao.delete(t);
		OperalogServiceImpl.addOperalog(shdz.getUserId(), shdz.getDepId(), shdz
				.getMenuId(),String.valueOf(shdz.getId()), "删除送货地址信息", opeDao);
	}

	/**
	 * 不带分页的
	 */
	@Override
	public List<Shdz> listShdz(Shdz shdz) {
		String hql = "from TShdz t ";
		List<TShdz> list = shdzDao.find(hql);
		return changeShdz(list);
	}

	@Override
	public DataGrid datagrid(Shdz shdz) {
		DataGrid dg = new DataGrid();
		String hql = "from TShdz t ";
		// 获得总条数
		String totalHql = "select count(*) " + hql;
		// 传入页码、每页条数
		List<TShdz> l = shdzDao.find(hql, shdz.getPage(), shdz.getRows());
		// 处理返回信息
		dg.setTotal(shdzDao.count(totalHql));
		dg.setRows(changeShdz(l));
		return dg;
	}
	
	@Override
	public DataGrid shdzDg(Shdz shdz) {
		String hql = "from TShdz t";
		Map<String, Object> params = new HashMap<String, Object>();
		
		String countHql = "select count(*) " + hql;

		if(shdz.getQuery() != null && shdz.getQuery().trim().length() > 0){
			
			String where = " where t.khmc like :khmc";
			hql += where;
			countHql += where;
			params.put("khmc", "%" + shdz.getQuery() + "%");
		}
		
		DataGrid dg = new DataGrid();
		dg.setTotal(shdzDao.count(countHql, params));
		dg.setRows(changeShdz(shdzDao.find(hql, params, shdz.getPage(), shdz.getRows())));
		return dg;
		
	}

	private List<Shdz> changeShdz(List<TShdz> l) {
		List<Shdz> nl = new ArrayList<Shdz>();
		for (TShdz t : l) {
			Shdz nc = new Shdz();
			BeanUtils.copyProperties(t, nc);
			//nc.setBh(t.getKhmc());
			//nc.setMc(t.getKhdz());
			nl.add(nc);
		}
		return nl;
	}

	@Autowired
	public void setShdzDao(BaseDaoI<TShdz> shdzDao) {
		this.shdzDao = shdzDao;
	}

	@Autowired
	public void setOpeDao(BaseDaoI<TOperalog> opeDao) {
		this.opeDao = opeDao;
	}
}
