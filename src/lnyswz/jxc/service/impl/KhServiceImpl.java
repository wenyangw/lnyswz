package lnyswz.jxc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Gys;
import lnyswz.jxc.bean.Kh;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TGys;
import lnyswz.jxc.model.TKh;
import lnyswz.jxc.model.TKhDet;
import lnyswz.jxc.model.TKhlx;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TUser;
import lnyswz.jxc.service.KhServiceI;
import lnyswz.jxc.util.Constant;

@Service("khService")
public class KhServiceImpl implements KhServiceI {

	private BaseDaoI<TKh> khDao;
	private BaseDaoI<TKhDet> khdetDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TUser> userDao;
	private BaseDaoI<TKhlx> khlxDao;
	private BaseDaoI<TOperalog> opeDao;

	/**
	 * 保存客户
	 */
	@Override
	public Kh add(Kh kh) {
		TKh t = new TKh();
		BeanUtils.copyProperties(kh, t);
		if(kh.getIsNsr() == null){
			t.setIsNsr("0");
		}
		khDao.save(t);
		Kh r = new Kh();
		BeanUtils.copyProperties(t, r);
		
		OperalogServiceImpl.addOperalog(kh.getUserId(), kh.getDepId(), kh.getMenuId(),kh.getKhbh(), "添加客户通用信息", opeDao);
		return r;
	}

	/**
	 * 编辑客户
	 */
	@Override
	public void edit(Kh kh) {
		TKh g = khDao.load(TKh.class, kh.getKhbh());
		BeanUtils.copyProperties(kh, g);
		OperalogServiceImpl.addOperalog(kh.getUserId(), kh.getDepId(), kh.getMenuId(),kh.getKhbh(), "编辑客户通用信息", opeDao);
	}

	/**
	 * 删除客户
	 */
	@Override
	public boolean delete(Kh kh) {
		boolean isOK = false;
		TKh t = khDao.load(TKh.class, kh.getKhbh());
		if (t.getTKhDets().size() == 0) {
			khDao.delete(t);
			isOK = true;
		}
		OperalogServiceImpl.addOperalog(kh.getUserId(), kh.getDepId(), kh.getMenuId(),kh.getKhbh(), "删除客户通用信息", opeDao);
		return isOK;
	}

	/**
	 * 编辑客户专属信息
	 */
	@Override
	public void editDet(Kh kh) {
		String keyId = "";
		TKh g = khDao.get(TKh.class, kh.getKhbh());
		if (kh.getDetId().equals("")) {
			Set<TKhDet> gdt = g.getTKhDets();
			TKhDet khDet = new TKhDet();
			BeanUtils.copyProperties(kh, khDet, new String[]{"id"});
			khDet.setId(UUID.randomUUID().toString());
			TDepartment dep = depDao.get(TDepartment.class, kh.getDepId());
			khDet.setTDepartment(dep);
			if(kh.getIsSx() == null){
				khDet.setIsSx("0");
			}
			if(kh.getSxje() == null){
				khDet.setSxje(Constant.BD_ZERO);
			}
			if(kh.getYfje() == null){
				khDet.setYfje(Constant.BD_ZERO);
			}
			khDet.setTKh(g);
			gdt.add(khDet);
			g.setTKhDets(gdt);
			keyId = kh.getKhbh() + "/" + khDet.getId();
		} else {
			TKhDet v = khdetDao.get(TKhDet.class, kh.getDetId());
			keyId=kh.getKhbh() + "/" + kh.getDetId();
			BeanUtils.copyProperties(kh, v);
		}
		
		OperalogServiceImpl.addOperalog(kh.getUserId(), kh.getDepId(), kh.getMenuId(),keyId, "修改客户专属信息", opeDao);
	}

	/**
	 * 删除客户专属信息
	 */
	public void deleteDet(Kh kh) {
		TKh t = khDao.load(TKh.class, kh.getKhbh());
		Set<TKhDet> dets = t.getTKhDets();
		t.setTKhDets(null);
		TKhDet det = khdetDao.load(TKhDet.class, kh.getDetId());
		dets.remove(det);
		khdetDao.delete(det);
		t.setTKhDets(dets);
		String keyId=kh.getKhbh() + "/" + det.getId();
		OperalogServiceImpl.addOperalog(kh.getUserId(), kh.getDepId(), kh.getMenuId(),keyId, "删除客户专属信息", opeDao);
	}

	/**
	 * 判断客户编号是否重复
	 */
	@Override
	public boolean existKh(Kh kh) {
		boolean isOK = false;
		String hql = "from TKh t where t.khbh=" + kh.getKhbh();
		List<TKh> list = khDao.find(hql);
		if (list.size() != 0) {
			isOK = true;
		}
		return isOK;
	}

	@Override
	public boolean isSxkh(Kh kh) {
		String hql = "from TKhDet t where t.TDepartment.id = :depId and t.TKh.khbh = :khbh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", kh.getDepId());
		params.put("khbh", kh.getKhbh());
		TKhDet tDet = khdetDao.get(hql, params);
		if(tDet == null){
			return false;
		}else{
			if("1".equals(tDet.getIsSx())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 不带分页
	 */
	@Override
	public List<Kh> listKhs(Kh kh) {
		String hql = "from Tkh t";
		List<TKh> list = khDao.find(hql);
		return changeKhs(list);
	}

	@Override
	public DataGrid datagrid(Kh c) {
		DataGrid dg = new DataGrid();
		String hql = "from TKh t ";
		Map<String, Object> params = new HashMap<String, Object>();
		//hql语句拼写
		hql = seletWhere(c, hql, params);
		//获得总条数
		String totalHql = "select count(*) " + hql;
		List<Kh> nl = new ArrayList<Kh>();
		// 传入页码、每页条数
		List<TKh> l = khDao.find(hql, params, c.getPage(), c.getRows());
		// 处理返回信息

		for (TKh t : l) {
			Kh nc = new Kh();
			BeanUtils.copyProperties(t, nc);

			if (c.getDepId() != null) {
				Set<TKhDet> khdet = t.getTKhDets();
				for (TKhDet m : khdet) {
					if (m.getTDepartment().getId().trim().equals(c.getDepId())) {
						
						BeanUtils.copyProperties(m, nc);
						nc.setDetId(m.getId());
						if(m.getYwyId() > 0){
							nc.setYwyId(m.getYwyId());
							nc.setYwyName(userDao.load(TUser.class, m.getYwyId()).getRealName());
						}
						if(m.getKhlxId() != null){
							nc.setKhlxmc(khlxDao.load(TKhlx.class, m.getKhlxId()).getKhlxmc());
						}
					}
				}
			}
			nl.add(nc);
		}

		dg.setTotal(khDao.count(totalHql, params));
		dg.setRows(nl);
		return dg;
	}
	
	@Override
	public DataGrid datagridDet(Kh kh) {
		DataGrid dg = new DataGrid();
		String hql = "from TKhDet t where t.TDepartment.id = :depId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", kh.getDepId());
		if(kh.getKhbh() != null){
			hql += " and t.TKh.khbh = :khbh";
			params.put("khbh", kh.getKhbh());
		}
		
		List<TKhDet> tKhDets = khdetDao.find(hql, params);
		List<Kh> l = new ArrayList<Kh>();
		if(tKhDets.size() > 0){
			for(TKhDet tDet : tKhDets){
				Kh k = new Kh();
				BeanUtils.copyProperties(tDet, k);
				
				if(tDet.getYwyId() > 0){
					k.setYwyName(userDao.load(TUser.class, tDet.getYwyId()).getRealName());
				}
				k.setKhlxmc(khlxDao.load(TKhlx.class, tDet.getKhlxId()).getKhlxmc());
				
				l.add(k);
			}
		}
		
		dg.setRows(l);
		return dg;
	}

	//条件筛选hql语句拼写
	private String seletWhere(Kh c, String hql, Map<String, Object> params) {
		if (c.getKhcs() != null && !c.getKhcs().trim().equals("")) {
			hql += "where t.khbh like :name" + " or t.khmc like :name";
			params.put("name", "%" + c.getKhcs().trim() + "%");
		}
		return hql;
	}

	private List<Kh> changeKhs(List<TKh> l) {
		List<Kh> nl = new ArrayList<Kh>();
		for (TKh t : l) {
//			Kh nc = new Kh();
//			BeanUtils.copyProperties(t, nc);
//			nc.setBh(t.getKhbh());
//			nc.setMc(t.getKhmc());
			nl.add(changeKh(t));
		}
		return nl;
	}
	
	private Kh changeKh(TKh t){
		Kh k = new Kh();
		BeanUtils.copyProperties(t, k);
		k.setBh(t.getKhbh());
		k.setMc(t.getKhmc());
		return k;
	}
	
	/**
	 * 王文阳
	 * 2013.10.08
	 * 获得客户信息
	 */
	@Override
	public Kh loadKh(String khbh, String depId) {
		
		TKh t = khDao.load(TKh.class, khbh);
		Kh k = new Kh();
		BeanUtils.copyProperties(t, k);

		String hql = "from TKhDet t where t.TDepartment.id = :depId and t.TKh.khbh = :khbh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", depId);
		params.put("khbh", khbh);
		TKhDet tKhDet = khdetDao.get(hql, params);
		if(tKhDet != null){
			BeanUtils.copyProperties(tKhDet, k);
		}
		return k;
	}
	
	
	/**
	 * 王文阳
	 * 2013.10.08
	 * 获得客户检索信息
	 */
	@Override
	public DataGrid khDg(Kh kh) {
		String hql;
		Map<String, Object> params = new HashMap<String, Object>();
		if("0".equals(kh.getIsSx()) || null == kh.getIsSx()){
			hql = "from TKh t";
			if(kh.getIsNsr() != null){
				hql += " where t.isNsr = :isNsr";
				params.put("isNsr", kh.getIsNsr());
			}
		}else{
			hql = "from TKh t join t.TKhDets det where det.isSx = :isSx and det.TDepartment.id = :depId";
			params.put("isSx", kh.getIsSx());
			params.put("depId", kh.getDepId());
			if(kh.getIsNsr() != null){
				hql += " and t.isNsr = :isNsr";
				params.put("isNsr", kh.getIsNsr());
			}
		}
		
		String countHql = "select count(*) " + hql;

		if(kh.getQuery() != null && kh.getQuery().trim().length() > 0){
			String where;
			if(("0".equals(kh.getIsSx()) || kh.getIsSx() == null) && kh.getIsNsr() == null){
				where = " where";
			}else{
				where = " and";
			}
			where += " t.khbh like :khbh or t.khmc like :khmc";
			hql += where;
			countHql += where;
			params.put("khbh", kh.getQuery() + "%");
			params.put("khmc", "%" + kh.getQuery() + "%");
		}
		
		DataGrid dg = new DataGrid();
		if(kh.getIsSx() != null){
			hql = "select t " + hql;
		}
		dg.setTotal(khDao.count(countHql, params));
		dg.setRows(changeKhs(khDao.find(hql, params, kh.getPage(), kh.getRows())));
		return dg;
	}
	
	@Override
	public Kh checkKh(Kh kh) {
		String hql = "from TKhDet det where det.TKh.khbh = :khbh and det.TDepartment.id = :depId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("khbh", kh.getKhbh());
		params.put("depId", kh.getDepId());
		TKhDet t = khdetDao.get(hql, params);
		
		Kh k = new Kh();
		BeanUtils.copyProperties(t, k);
		k.setKhbh(t.getTKh().getKhbh());
		return k;
	}
	
	@Override
	public DataGrid sxkhDg(Kh kh) {
		String hql = "from TKhDet t where t.ywyId = :ywyId and t.TDepartment.id = :depId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ywyId", kh.getYwyId());
		params.put("depId", kh.getDepId());
		
		List<TKhDet> dets = khdetDao.find(hql, params);
		if(dets != null && dets.size() > 0){
			List<Kh> khs = new ArrayList<Kh>();
			for(TKhDet tDet : dets){
				Kh k = new Kh();
				k.setKhbh(tDet.getTKh().getKhbh());
				k.setKhmc(tDet.getTKh().getKhmc());
				k.setSxje(tDet.getSxje());
				k.setSxzq(tDet.getSxzq());
				khs.add(k);
			}
			DataGrid dg = new DataGrid();
			dg.setRows(khs);
			return dg;
		}
		return null;
	}
	
	@Override
	public int getAuditLevel(Kh kh) {
		String hql = "from TKHDet t where t.TKh.khbh = :khbh and t.TDepartment.id = :depId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("khbh", kh.getKhbh());
		params.put("depId", kh.getDepId());
		TKhDet khDet = khdetDao.get(hql, params);
		if(khDet != null){
			
		}
		return 0;
	}
	
	@Override
	public DataGrid listKhByYwy(Kh kh) {
		String hql = "from TKhDet t where t.TDepartment.id = :depId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", kh.getDepId());
		if(kh.getYwyId() > 0){
			hql += " and t.ywyId = :ywyId";
			params.put("ywyId", kh.getYwyId());
		}
		hql += " order by t.TKh.khbh";
		List<TKhDet> tDets = khdetDao.find(hql, params);
		List<Kh> khs = new ArrayList<Kh>();
		for(TKhDet tDet : tDets){
			Kh k = new Kh();
			BeanUtils.copyProperties(tDet, k);
			
			k.setKhbh(tDet.getTKh().getKhbh());
			k.setKhmc(tDet.getTKh().getKhmc());
			
			khs.add(k);
		}
		DataGrid dg = new DataGrid();
		dg.setRows(khs);
		return dg;
	}
	
	public static Kh getKhsx(String khbh, String depId, BaseDaoI<TKhDet> khDetDao, BaseDaoI<TKhlx> khlxDao) {
		Kh kh = new Kh();

		String hql = "from TKhDet t where t.TDepartment.id = :depId and t.TKh.khbh = :khbh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", depId);
		params.put("khbh", khbh);
		TKhDet tKhDet = khDetDao.get(hql, params);
		if(tKhDet != null){
			BeanUtils.copyProperties(tKhDet, kh);
			kh.setKhlxmc(khlxDao.load(TKhlx.class, tKhDet.getKhlxId()).getKhlxmc());
		}else{
			kh.setKhlxId(Constant.KHLX_XK);
			kh.setKhlxmc(Constant.KHLX_XK_NAME);
			kh.setSxje(Constant.BD_ZERO);
			kh.setSxzq(0);
			kh.setYfje(Constant.BD_ZERO);
		}
		return kh;
	}

	@Autowired
	public void setKhDao(BaseDaoI<TKh> khDao) {
		this.khDao = khDao;
	}

	@Autowired
	public void setKhdetDao(BaseDaoI<TKhDet> khdetDao) {
		this.khdetDao = khdetDao;
	}

	@Autowired
	public void setDepDao(BaseDaoI<TDepartment> depDao) {
		this.depDao = depDao;
	}

	@Autowired
	public void setUserDao(BaseDaoI<TUser> userDao) {
		this.userDao = userDao;
	}
	
	@Autowired
	public void setKhlxDao(BaseDaoI<TKhlx> khlxDao) {
		this.khlxDao = khlxDao;
	}

	@Autowired
	public void setOpeDao(BaseDaoI<TOperalog> opeDao) {
		this.opeDao = opeDao;
	}

}
