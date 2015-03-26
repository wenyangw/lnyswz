package lnyswz.jxc.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Catalog;
import lnyswz.jxc.bean.Lsh;
import lnyswz.jxc.model.TCatalog;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.service.CatalogServiceI;
import lnyswz.jxc.service.LshServiceI;
import lnyswz.jxc.util.CatalogComparator;

/**
 * 流水号实现类
 * @author 王文阳
 *
 */
@Service("lshService")
public class LshServiceImpl implements LshServiceI {
	private final static Logger logger = Logger.getLogger(LshServiceImpl.class);
	private BaseDaoI<TLsh> lshDao;
	
	//获取单据流水号
	@Override
	public String getLsh(String bmbh, String lxbh) {
		//流水号前缀，年(2)+月(2)+部门(2)+单据类型()
		String lsh = DateUtil.getCurrentDateString("yyMM") + bmbh + lxbh;
		String hql = "from TLsh t where t.lsh like :lsh";
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("lsh",  lsh + "%");
		List<TLsh> l = lshDao.find(hql, params);
		//该部门类型是否存在流水号，存在加1，不存在从0001开始
		if(l != null && l.size() == 1){
			String lshStr = l.get(0).getLsh();
			lsh += String.format("%04d", Integer.valueOf(lshStr.substring(8)) + 1);
		}else{
			lsh += "0001";
		}
		
		return lsh;
	}
	
	public static String updateLsh(String bmbh, String lxbh, BaseDaoI<TLsh> baseDao){
		//流水号前缀，年(2)+月(2)+部门(2)+单据类型()
		String lsh = DateUtil.getCurrentDateString("yyMM") + bmbh + lxbh;
		String hql = "from TLsh t where t.lsh like :lsh";
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("lsh",  lsh + "%");
		List<TLsh> l = baseDao.find(hql, params);
		//该部门类型是否存在流水号，存在加1，不存在从0001开始
		if(l != null && l.size() == 1){
			String lshStr = l.get(0).getLsh();
			lsh += String.format("%04d", Integer.valueOf(lshStr.substring(8)) + 1);
			l.get(0).setLsh(lsh);
		}else{
			lsh += "0001";
			TLsh t = new TLsh();
			t.setBmbh(bmbh);
			t.setLxbh(lxbh);
			t.setLsh(lsh);
			baseDao.save(t);
		}
		return lsh;
	}
	
	@Autowired
	public void setLshDao(BaseDaoI<TLsh> lshDao) {
		this.lshDao = lshDao;
	}

}
