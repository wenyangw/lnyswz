package lnyswz.jxc.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.ProBean;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Khdd;
import lnyswz.jxc.bean.KhddDet;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.model.*;
import lnyswz.jxc.service.KhddServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Util;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 客户订单实现类
 * @author 王文阳
 * 2020.04.23 新建
 *
 */
@Service("khddService")
public class KhddServiceImpl implements KhddServiceI {
	private BaseDaoI<TKhdd> khddDao;
	private BaseDaoI<TKhddDet> detDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TSp> spDao;
	private BaseDaoI<TOperalog> operalogDao;
	
	@Override
	public Khdd saveKhdd(Khdd khdd) {
		String lsh = LshServiceImpl.updateLsh(khdd.getBmbh(), khdd.getLxbh(), lshDao);
		TKhdd tKhdd = new TKhdd();
		BeanUtils.copyProperties(khdd, tKhdd);
		tKhdd.setCreateTime(new Date());
		tKhdd.setKhddlsh(lsh);
		tKhdd.setBmmc(depDao.load(TDepartment.class, khdd.getBmbh()).getDepName());
		
		tKhdd.setIsCancel("0");
		tKhdd.setIsRefuse("0");

		Department dep = new Department();
		dep.setId(khdd.getBmbh());
		dep.setDepName(tKhdd.getBmmc());

		//处理商品明细
		Set<TKhddDet> tDets = new HashSet<TKhddDet>();
		ArrayList<KhddDet> khddDets = JSON.parseObject(khdd.getDatagrid(), new TypeReference<ArrayList<KhddDet>>(){});
		TKhddDet tDet = null;
		for(KhddDet khddDet : khddDets){
			if(khddDet.getSpbh() != null) {
				tDet = new TKhddDet();
				BeanUtils.copyProperties(khddDet, tDet, new String[]{"id"});

				tDet.setTKhdd(tKhdd);
				tDets.add(tDet);

				Sp sp = new Sp();
				BeanUtils.copyProperties(khddDet, sp);
			}
		}
		tKhdd.setTKhddDets(tDets);
		khddDao.save(tKhdd);		
		OperalogServiceImpl.addOperalog(khdd.getCreateId(), khdd.getBmbh(), khdd.getMenuId(), tKhdd.getKhddlsh(), "保存客户订单", operalogDao);

		khdd.setKhddlsh(lsh);
		return khdd;
	}
	
	@Override
	public void cancelKhdd(Khdd khdd) {
		Date now = new Date();
		//获取原单据信息
		TKhdd tKhdd = khddDao.get(TKhdd.class, khdd.getKhddlsh());

		//更新原单据冲减信息
		tKhdd.setCancelId(khdd.getCancelId());
		tKhdd.setCancelTime(now);
		tKhdd.setCancelName(khdd.getCancelName());
		tKhdd.setIsCancel("1");

		OperalogServiceImpl.addOperalog(khdd.getCancelId(), tKhdd.getBmbh(), khdd.getMenuId(), tKhdd.getKhddlsh(), "取消客户订单", operalogDao);
	}

	@Override
	public void refuseKhdd(Khdd khdd) {
		Date now = new Date();
		//获取原单据信息
		TKhdd tKhdd = khddDao.get(TKhdd.class, khdd.getKhddlsh());

		//更新原单据冲减信息
		tKhdd.setRefuseId(khdd.getRefuseId());
		tKhdd.setRefuseTime(now);
		tKhdd.setRefuseName(khdd.getRefuseName());
		tKhdd.setIsRefuse("1");

		OperalogServiceImpl.addOperalog(khdd.getRefuseId(), tKhdd.getBmbh(), khdd.getMenuId(), tKhdd.getKhddlsh(), "退回客户订单", operalogDao);
	}

	@Override
	public Khdd getKhdd(Khdd khdd) {

		return null;
	}

	@Override
	public DataGrid getKhdds(Khdd khdd) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TKhdd t";
		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("bmbh1", "04");
//      params.put("bmbh2", "05");
//      params.put("createId", khdd.getCreateId());
		if(khdd.getCreateTime() != null){
			params.put("createTime", khdd.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}

		if(khdd.getSearch() != null){
			hql += " and (" + 
					Util.getQueryWhere(khdd.getSearch(), new String[]{"t.khddlsh", "t.bz"}, params)
					+ ")";
		}
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TKhdd> l = khddDao.find(hql, params, khdd.getPage(), khdd.getRows());
		List<Khdd> nl = new ArrayList<Khdd>();
        Khdd c;
		for(TKhdd t : l){
			c = new Khdd();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setTotal(khddDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid getKhddDet(Khdd khdd) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TKhddDet t where t.TKhdd.khddlsh = :khddlsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("khddlsh", khdd.getKhddlsh());
		List<TKhddDet> l = detDao.find(hql, params);
		List<KhddDet> nl = new ArrayList<KhddDet>();
		KhddDet c = null;
		for(TKhddDet t : l){
			c = new KhddDet();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}

	@Autowired
	public void setKhddDao(BaseDaoI<TKhdd> khddDao) {
		this.khddDao = khddDao;
	}

	@Autowired
	public void setDetDao(BaseDaoI<TKhddDet> detDao) {
		this.detDao = detDao;
	}

	@Autowired
	public void setLshDao(BaseDaoI<TLsh> lshDao) {
		this.lshDao = lshDao;
	}

	@Autowired
	public void setDepDao(BaseDaoI<TDepartment> depDao) {
		this.depDao = depDao;
	}

	@Autowired
	public void setSpDao(BaseDaoI<TSp> spDao) {
		this.spDao = spDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}

}
