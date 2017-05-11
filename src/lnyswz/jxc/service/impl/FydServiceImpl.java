package lnyswz.jxc.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import lnyswz.common.bean.ProBean;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Ck;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.KfrkDet;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.bean.XsthDet;
import lnyswz.jxc.bean.Fyd;
import lnyswz.jxc.bean.FydDet;
import lnyswz.jxc.model.TCgjhDet;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TKfrk;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TSpDet;
import lnyswz.jxc.model.TXskp;
import lnyswz.jxc.model.TXsth;
import lnyswz.jxc.model.TFyd;
import lnyswz.jxc.model.TFydDet;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.service.FydServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Util;

/**
 * 付印单实现类
 * @author 王文阳
 *
 */
@Service("fydService")
public class FydServiceImpl implements FydServiceI {
	private Logger logger = Logger.getLogger(FydServiceImpl.class);
	private BaseDaoI<TFyd> fydDao;
	private BaseDaoI<TFydDet> detDao;
	private BaseDaoI<TLsh> lshDao;
	private BaseDaoI<TSpDet> spDetDao;
	private BaseDaoI<TDepartment> depDao;


	private BaseDaoI<TSp> spDao;
	private BaseDaoI<TOperalog> operalogDao;
	
	@Override
	public DataGrid datagrid(Fyd fyd) {
		DataGrid datagrid = new DataGrid();
		String hql = " from TFyd t where t.bmbh = :bmbh and t.createTime > :createTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", fyd.getBmbh());
		if(fyd.getCreateTime() != null){
			params.put("createTime", fyd.getCreateTime()); 
		}else{
			params.put("createTime", DateUtil.stringToDate(DateUtil.getFirstDateInMonth(new Date())));
		}
		
		
		if(fyd.getFromOther() != null){
			
		}else{
			if(fyd.getSearch() != null && fyd.getSearch().length() > 0){
				hql += " and (" + 
						Util.getQueryWhere(fyd.getSearch(), new String[]{"t.fydlsh", "t.tzdbh", "t.bname", "t.publishercn", "t.zdr"}, params)
						+ ")";
			}
		}
		String countHql = " select count(*)" + hql;
		hql += " order by t.createTime desc";
		List<TFyd> l = fydDao.find(hql, params, fyd.getPage(), fyd.getRows());
		List<Fyd> nl = new ArrayList<Fyd>();
		Fyd c = null;
		
		for(TFyd t : l){
			c = new Fyd();
			BeanUtils.copyProperties(t, c);
			c.setEdited("1");
			for(TFydDet tfd : t.getTFydDets()){
				if(tfd.getDanjia() == null || tfd.getDanjia().compareTo(BigDecimal.ZERO) == 0){
					c.setEdited("0");
					break;
				}
			}
			nl.add(c);
		}
		datagrid.setTotal(fydDao.count(countHql, params));
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public DataGrid detDatagrid(String fydlsh) {
		DataGrid datagrid = new DataGrid();
		String hql = "from TFydDet t where t.TFyd.fydlsh = :fydlsh";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fydlsh", fydlsh);
		List<TFydDet> l = detDao.find(hql, params);
		List<FydDet> nl = new ArrayList<FydDet>();
		FydDet c = null;
		for(TFydDet t : l){
			c = new FydDet();
			BeanUtils.copyProperties(t, c);
			nl.add(c);
		}
		datagrid.setRows(nl);
		return datagrid;
	}
	
	@Override
	public void updateXsdj(Fyd fyd) {
		TFydDet tFydDet = detDao.load(TFydDet.class, fyd.getId());
		
		tFydDet.setDanjia(fyd.getDanjia());
		tFydDet.setGongj(fyd.getDanjia().multiply(tFydDet.getZzhjl()));
		
		//OperalogServiceImpl.addOperalog(xsth.getCreateId(), xsth.getBmbh(), xsth.getMenuId(), String.valueOf(xsth.getId()),	"修改提货数量", operalogDao);
	}
	
	@Override
	public String sendFyd(Fyd fyd) {
		StringBuilder sb = new StringBuilder();
		TFyd tFyd = fydDao.load(TFyd.class, fyd.getFydlsh());
		Set<TFydDet> tFydDets = tFyd.getTFydDets();
		
		sb.append("<?xml version='1.0' encoding='UTF-8'?>"); 
		sb.append("<root>");
		sb.append("<head>");    
		sb.append("<publisher>"); 
		sb.append(tFyd.getPublisher());
		sb.append("</publisher>");    
		sb.append("<publishercn>");
		sb.append(tFyd.getPublishercn());
		sb.append("</publishercn>");  
		sb.append("<checkCode>");
		sb.append(tFyd.getCheckCode());
		sb.append("</checkCode>");  
		sb.append("</head>");  
		sb.append("<deal id='16041514580031492' type='cbyztz' operation='0'>");
		sb.append("<tzdbh>");
		sb.append(tFyd.getTzdbh());
		sb.append("</tzdbh>");
		sb.append("<cbsydsno>");
		sb.append(tFyd.getCbsydsno());
		sb.append("</cbsydsno>");
		sb.append("<bsno>");
		sb.append(tFyd.getBsno());
		sb.append("</bsno>");
		sb.append("<bname>");
		sb.append(tFyd.getBname());
		sb.append("</bname>");
		sb.append("<yc>");
		sb.append(tFyd.getYc());
		sb.append("</yc>");
		sb.append("<Details count='"); 
		sb.append(tFydDets.size());
		sb.append("'>");
		for(TFydDet tfd : tFydDets){
			sb.append("<Detail>");
			sb.append("<tzdbh>");
			sb.append(tfd.getTzdbh());
			sb.append("</tzdbh>");
			sb.append("<cbsydsno>");
			sb.append(tfd.getCbsydsno());
			sb.append("</cbsydsno>");
			sb.append("<sno>");
			sb.append(tfd.getSno());
			sb.append("</sno>");
			sb.append("<xmdm>");
			sb.append(tfd.getXmdm());
			sb.append("</xmdm>");
			sb.append("<xmmc>");
			sb.append(tfd.getXmmc());
			sb.append("</xmmc>");
			sb.append("<cldm>");
			sb.append(tfd.getCldm());
			sb.append("</cldm>");
			sb.append("<zzmc>");
			sb.append(tfd.getZzmc());
			sb.append("</zzmc>");
			sb.append("<zzgg>");
			sb.append(tfd.getZzgg());
			sb.append("</zzgg>");
			sb.append("<danjia>");
			sb.append(tfd.getDanjia());
			sb.append("</danjia>");
			sb.append("<gongj>");
			sb.append(tfd.getGongj());
			sb.append("</gongj>");
			sb.append("</Detail>");
		}
		sb.append("</Details>");
		sb.append("</deal>");  
		sb.append("</root>");

		
		//OperalogServiceImpl.addOperalog(xsth.getCreateId(), xsth.getBmbh(), xsth.getMenuId(), String.valueOf(xsth.getId()),	"修改提货数量", operalogDao);
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	@Autowired
	public void setFydDao(BaseDaoI<TFyd> fydDao) {
		this.fydDao = fydDao;
	}

	@Autowired
	public void setDetDao(BaseDaoI<TFydDet> detDao) {
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
	public void setSpDetDao(BaseDaoI<TSpDet> spDetDao) {
		this.spDetDao = spDetDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
}
