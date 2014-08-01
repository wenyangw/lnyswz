package lnyswz.jxc.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.Kh;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.bean.Xskp;
import lnyswz.jxc.bean.Xshk;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.THkKp;
import lnyswz.jxc.model.TKh;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TXskp;
import lnyswz.jxc.model.TXshk;
import lnyswz.jxc.model.TYszz;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.service.XshkServiceI;
import lnyswz.jxc.util.Constant;

/**
 * 销售回款实现类
 * @author 王文阳
 *
 */
@Service("xshkService")
public class XshkServiceImpl implements XshkServiceI {
	private Logger logger = Logger.getLogger(XshkServiceImpl.class);
	private BaseDaoI<TXshk> xshkDao;
	private BaseDaoI<TXskp> xskpDao;
	private BaseDaoI<THkKp> hkKpDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TYszz> yszzDao;
	private BaseDaoI<TOperalog> operalogDao;
	

	@Override
	public Xshk save(Xshk xshk) {
		TXshk tXshk = new TXshk();
		BeanUtils.copyProperties(xshk, tXshk);
		String lsh = LshServiceImpl.updateLsh(xshk.getBmbh(), xshk.getLxbh(), lshDao);
		tXshk.setXshklsh(lsh);
		tXshk.setCreateTime(new Date());
		tXshk.setCreateId(xshk.getCreateId());
		tXshk.setCreateName(xshk.getCreateName());
		tXshk.setIsCancel("0");

		String depName = depDao.load(TDepartment.class, xshk.getBmbh()).getDepName();
		tXshk.setBmmc(depName);
		
		Department dep = new Department();
		dep.setId(xshk.getBmbh());
		dep.setDepName(depName);
		
		Kh kh = new Kh();
		kh.setKhbh(xshk.getKhbh());
		kh.setKhmc(xshk.getKhmc());
		
		User ywy = new User();
		ywy.setId(xshk.getYwyId());
		ywy.setRealName(xshk.getYwymc());
		
		xshkDao.save(tXshk);
		if(xshk.getIsLs().equals("0")){
			//处理商品明细
			ArrayList<Xskp> xskps = JSON.parseObject(xshk.getDatagrid(), new TypeReference<ArrayList<Xskp>>(){});
			//预付没有销售记录的
			if(xskps != null && xskps.size() > 0){
				Set<THkKp> tHkKps = new HashSet<THkKp>();
				for(Xskp x : xskps){
					THkKp tHkKp = new THkKp();
					tHkKp.setXskplsh(x.getXskplsh());
					tHkKp.setHkje(x.getHkje());
					tHkKp.setTXshk(tXshk);
					tHkKps.add(tHkKp);
					//tXshk.getTHkKps().add(tHkKp);
					hkKpDao.save(tHkKp);
					
					TXskp tXskp= xskpDao.load(TXskp.class, x.getXskplsh());
					tXskp.setHkje(tXskp.getHkje().add(x.getHkje()));
				}
				tXshk.setTHkKps(tHkKps);
			}
			YszzServiceImpl.updateYszzJe(dep, kh, ywy, tXshk.getHkje(), Constant.UPDATE_HK, yszzDao);
		}else{
			YszzServiceImpl.updateYszzJe(dep, kh, ywy, tXshk.getHkje(), Constant.UPDATE_HK_LS, yszzDao);
		}
				
//		OperalogServiceImpl.addOperalog(xshk.getCreateId(), xshk.getBmbh(), xshk.getMenuId(), tXshk.getXshklsh(), 
//				"生成销售提货单", operalogDao);
//		
		Xshk rXshk = new Xshk();
		rXshk.setXshklsh(lsh);
		return rXshk;
		
	}

	@Override
	public void cancelXshk(Xshk xshk) {
		Date now = new Date();
		String lsh = LshServiceImpl.updateLsh(xshk.getBmbh(), xshk.getLxbh(), lshDao);
		
		//获取原单据信息
		TXshk yTXshk = xshkDao.load(TXshk.class, xshk.getXshklsh());
		//新增冲减单据信息
		TXshk tXshk = new TXshk();
		BeanUtils.copyProperties(yTXshk, tXshk, new String[]{"THkKps"});

		//更新原单据信息
		yTXshk.setIsCancel("1");
		yTXshk.setCancelId(xshk.getCancelId());
		yTXshk.setCancelName(xshk.getCancelName());
		yTXshk.setCancelTime(now);
		
		tXshk.setXshklsh(lsh);
		tXshk.setCancelXshklsh(yTXshk.getXshklsh());
		tXshk.setCreateId(xshk.getCancelId());
		tXshk.setCreateTime(now);
		tXshk.setCreateName(xshk.getCancelName());
		tXshk.setIsCancel("1");
		tXshk.setCancelId(xshk.getCancelId());
		tXshk.setCancelName(xshk.getCancelName());
		tXshk.setCancelTime(now);
		tXshk.setHkje(yTXshk.getHkje().negate());
		
		Department dep = new Department();
		dep.setId(tXshk.getBmbh());
		dep.setDepName(tXshk.getBmmc());
		
		Kh kh = new Kh();
		kh.setKhbh(tXshk.getKhbh());
		kh.setKhmc(tXshk.getKhmc());
		
		User ywy = new User();
		ywy.setId(yTXshk.getYwyId());
		ywy.setRealName(yTXshk.getYwymc());
		
		if(yTXshk.getIsLs().equals("0")){
			//更新授信客户应付金额
			YszzServiceImpl.updateYszzJe(dep, kh, ywy, tXshk.getHkje(), Constant.UPDATE_HK, yszzDao);
			
			//String hql = "from THkKp t where t.xshklsh = :xshklsh order by t.xskplsh desc";
			//Map<String, Object> params = new HashMap<String, Object>();
			//params.put("xshklsh", yTXshk.getXshklsh());
			//List<THkKp> tHkKps = hkKpDao.find(hql, params);
			Set<THkKp> tHkKps = yTXshk.getTHkKps();
			for(THkKp tHkKp : tHkKps){
				TXskp tXskp = xskpDao.load(TXskp.class, tHkKp.getXskplsh());
				tXskp.setHkje(tXskp.getHkje().subtract(tHkKp.getHkje()));
				//删除与销售开票的关联
				//tHkKp.getTXshk().getTHkKps().remove(tHkKp);
				//tHkKp.setTXshk(null);
				//hkKpDao.delete(tHkKp);
			}
			
			Iterator<THkKp> it = tHkKps.iterator();  
	        while(it.hasNext()){
	            //CheckWork checkWork = it.next();  
	            THkKp t = it.next();
	            //t.getTXshk().getTHkKps().remove(t);
				//t.setTXshk(null);
				hkKpDao.delete(t);
				//it.remove();
	        }  
			
			//yTXshk.setHkje(null);
		}else{
			YszzServiceImpl.updateYszzJe(dep, kh, ywy, tXshk.getHkje(), Constant.UPDATE_HK_LS, yszzDao);
		}
		xshkDao.save(tXshk);
		
		OperalogServiceImpl.addOperalog(xshk.getCancelId(), xshk.getBmbh(), xshk.getMenuId(), tXshk.getCancelXshklsh() + "/" + tXshk.getXshklsh(), 
			"取消销售提货单", operalogDao);
		
	}
	
	
	@Override
	public DataGrid datagrid(Xshk xshk) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TXshk t where t.bmbh = :bmbh and t.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", xshk.getBmbh());

		//默认显示当前月数据
		if(xshk.getCreateTime() != null){
			params.put("createTime", xshk.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		
		if(xshk.getSearch() != null){
			hql += " and (t.xshklsh like :search or t.khmc like :search)"; 
			params.put("search", "%" + xshk.getSearch() + "%");
			
		}
		
		String countHql = "select count(*)" + hql;
		hql += " order by t.createTime desc";
		
		List<TXshk> l = xshkDao.find(hql, params, xshk.getPage(), xshk.getRows());
		List<Xshk> nl = new ArrayList<Xshk>();
		for(TXshk t : l){
			Xshk c = new Xshk();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setTotal(xshkDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(Xshk xshk) {
		DataGrid datagrid = new DataGrid();
		TXshk tXshk = xshkDao.load(TXshk.class, xshk.getXshklsh());
		Set<THkKp> tHkKps = tXshk.getTHkKps();
		List<Xskp> xskps = new ArrayList<Xskp>();
		for(THkKp t : tHkKps){
			Xskp xskp = new Xskp();
			TXskp tXskp = xskpDao.load(TXskp.class, t.getXskplsh());
			xskp.setXskplsh(t.getXskplsh());
			xskp.setCreateTime(tXskp.getCreateTime());
			xskp.setHjje(tXskp.getHjje().add(tXskp.getHjse()));
			xskp.setHkje(t.getHkje());
			xskps.add(xskp);
		}
		datagrid.setRows(xskps);
		return datagrid;
	}
	
	@Autowired
	public void setXshkDao(BaseDaoI<TXshk> xshkDao) {
		this.xshkDao = xshkDao;
	}

	@Autowired
	public void setXskpDao(BaseDaoI<TXskp> xskpDao) {
		this.xskpDao = xskpDao;
	}

	@Autowired
	public void setHkKpDao(BaseDaoI<THkKp> hkKpDao) {
		this.hkKpDao = hkKpDao;
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
	public void setYszzDao(BaseDaoI<TYszz> yszzDao) {
		this.yszzDao = yszzDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
}