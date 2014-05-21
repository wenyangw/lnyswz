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
import lnyswz.jxc.bean.Fh;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TFh;
import lnyswz.jxc.service.FhServiceI;

@Service("fhService")
public class FhServiceImpl implements FhServiceI {

	private BaseDaoI<TFh> fhDao;
	private BaseDaoI<TDepartment> depDao;

	/**
	 * 保存分库
	 */
	@Override
	public Fh add(Fh fh) {
		TFh t = new TFh();
		BeanUtils.copyProperties(fh, t);
		TDepartment dep = depDao.get(TDepartment.class, fh.getDepId());
		t.setTDepartment(dep);
		fhDao.save(t);
		Fh r = new Fh();
		BeanUtils.copyProperties(t, r);
		r.setDepName(dep.getDepName());
		r.setDepId(fh.getDepId());
		return r;
	}

	/**
	 * 编辑分库
	 */
	@Override
	public void edit(Fh fh) {
		TFh g = fhDao.get(TFh.class, fh.getId());
		BeanUtils.copyProperties(fh, g);
		TDepartment dep = depDao.get(TDepartment.class, fh.getDepId());
		g.setTDepartment(dep);

	}

	/**
	 * 删除分库
	 */
	@Override
	public void delete(Fh fh) {
		TFh t = fhDao.get(TFh.class, fh.getId());
		fhDao.delete(t);
	}

	@Override
	public List<Fh> listFhs(Fh fh) {
		String hql = "from TFh t where t.TDepartment.id = :depId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", fh.getDepId());
		List<TFh> l = fhDao.find(hql, params);
		return changeFh(l);
	}

	/**
	 * 数据转换
	 * 
	 * @param l
	 * @return
	 */
	private List<Fh> changeFh(List<TFh> l) {
		List<Fh> nl = new ArrayList<Fh>();
		for (TFh t : l) {
			Fh r = new Fh();
			BeanUtils.copyProperties(t, r);
			TDepartment dep = t.getTDepartment();
			r.setDepName(dep.getDepName());
			r.setDepId(dep.getId());
			nl.add(r);
		}
		return nl;
	}

	@Override
	public DataGrid datagrid(Fh c) {
		DataGrid dg = new DataGrid();
		String hql = "from TFh t ";
		Map<String, Object> params = new HashMap<String, Object>();
		//获得总条数
		String totalHql = "select count(*) " + hql;
		//List<Fh> nl = new ArrayList<Fh>();
		// 传入页码、每页条数
		List<TFh> l = fhDao.find(hql, params, c.getPage(), c.getRows());
		dg.setTotal(fhDao.count(totalHql, params));
		dg.setRows(changeFh(l));
		return dg;
	}

	@Autowired
	public void setFhDao(BaseDaoI<TFh> fhDao) {
		this.fhDao = fhDao;
	}

	@Autowired
	public void setDepDao(BaseDaoI<TDepartment> depDao) {
		this.depDao = depDao;
	}
}
