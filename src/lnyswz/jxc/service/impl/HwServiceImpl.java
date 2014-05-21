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
import lnyswz.jxc.bean.Hw;
import lnyswz.jxc.model.TCk;
import lnyswz.jxc.model.THw;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.service.HwServiceI;

@Service("hwService")
public class HwServiceImpl implements HwServiceI {
	private BaseDaoI<THw> hwDao;
	private BaseDaoI<TCk> ckDao;
	private BaseDaoI<TOperalog> opeDao;

	/**
	 * 保存货位
	 */
	@Override
	public Hw add(Hw hw) {
		THw t = new THw();
		BeanUtils.copyProperties(hw, t);
		TCk ck = ckDao.load(TCk.class, hw.getCkId());
		t.setTCk(ck);
		hwDao.save(t);
		Hw h = new Hw();
		BeanUtils.copyProperties(hw, h);
		h.setCkmc(ck.getCkmc());
		h.setCkId(hw.getCkId());
		OperalogServiceImpl.addOperalog(hw.getUserId(), hw.getDepId(), hw
				.getMenuId(), hw.getId(), "添加货物信息", opeDao);
		return h;
	}

	/**
	 * 编辑货位
	 */
	@Override
	public void edit(Hw hw) {
		THw g = hwDao.load(THw.class, hw.getId());
		BeanUtils.copyProperties(hw, g);
		TCk ck = ckDao.load(TCk.class, hw.getCkId());
		g.setTCk(ck);
		OperalogServiceImpl.addOperalog(hw.getUserId(), hw.getDepId(), hw
				.getMenuId(), hw.getId(), "编辑货位信息", opeDao);
	}

	/**
	 * 删除货位
	 */
	@Override
	public void delete(Hw hw) {
		THw t = hwDao.load(THw.class, hw.getId());
		hwDao.delete(t);
		OperalogServiceImpl.addOperalog(hw.getUserId(), hw.getDepId(), hw
				.getMenuId(), hw.getId(), "删除货位信息", opeDao);
	}

	/**
	 * 不带分页
	 */
	@Override
	public List<Hw> listHw(Hw hw) {
		String hql = "from THw t where t.TCk.TDepartment.id = :depId and t.TCk.id = :ckId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", hw.getDepId());
		params.put("ckId", hw.getCkId());
		List<THw> list = hwDao.find(hql, params);
		return changeHw(list);
	}

	@Override
	public DataGrid datagrid(Hw c) {
		DataGrid dg = new DataGrid();
		String hql = "from THw t ";
		// 获得总条数
		String totalHql = "select count(*) " + hql;
		List<Hw> nl = new ArrayList<Hw>();
		// 传入页码、每页条数
		List<THw> l = hwDao.find(hql, c.getPage(), c.getRows());
		// 处理返回信息
		for (THw t : l) {
			Hw nc = new Hw();
			BeanUtils.copyProperties(t, nc);
			TCk ck = t.getTCk();
			nc.setCkmc(ck.getCkmc());
			nc.setCkId(ck.getId());
			nl.add(nc);
		}
		dg.setTotal(hwDao.count(totalHql));
		dg.setRows(nl);
		return dg;
	}

	private List<Hw> changeHw(List<THw> l) {
		List<Hw> nl = new ArrayList<Hw>();
		for (THw t : l) {
			Hw h = new Hw();
			BeanUtils.copyProperties(t, h);
			nl.add(h);
		}
		return nl;
	}

	@Autowired
	public void setHwDao(BaseDaoI<THw> hwDao) {
		this.hwDao = hwDao;
	}

	@Autowired
	public void setCkDao(BaseDaoI<TCk> ckDao) {
		this.ckDao = ckDao;
	}

	@Autowired
	public void setOpeDao(BaseDaoI<TOperalog> opeDao) {
		this.opeDao = opeDao;
	}
}
