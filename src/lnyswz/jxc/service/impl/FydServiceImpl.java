package lnyswz.jxc.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Fyd;
import lnyswz.jxc.bean.FydDet;
import lnyswz.jxc.model.TFyd;
import lnyswz.jxc.model.TFydDet;
import lnyswz.jxc.service.FydServiceI;
import lnyswz.jxc.util.Export;
import lnyswz.jxc.util.Util;

/**
 * 付印单实现类
 * @author 王文阳
 *
 */
@Service("fydService")
public class FydServiceImpl implements FydServiceI {
	private BaseDaoI<TFyd> fydDao;
	private BaseDaoI<TFydDet> detDao;
	
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
	public Fyd updateXsdj(Fyd fyd) {
		TFydDet tFydDet = detDao.load(TFydDet.class, fyd.getId());
		
		tFydDet.setDanjia(fyd.getDanjia());
		tFydDet.setGongj(fyd.getDanjia().multiply(tFydDet.getZzhjl()));
		
		fyd.setGongj(tFydDet.getGongj());
		return fyd;
	}
	
	@Override
	public void updateFydSended(Fyd fyd) {
		TFyd tFyd = fydDao.load(TFyd.class, fyd.getFydlsh());
		tFyd.setSended("1");
		tFyd.setSendId(fyd.getSendId());
		tFyd.setSendName(fyd.getSendName());
		tFyd.setSendTime(new Date());
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
		sb.append(tFyd.getPublisher().trim());
		sb.append("</publisher>");    
		sb.append("<publishercn>");
		sb.append(tFyd.getPublishercn().trim());
		sb.append("</publishercn>");  
		sb.append("<checkCode>");
		sb.append(tFyd.getCheckCode().trim());
		sb.append("</checkCode>");  
		sb.append("</head>");  
		sb.append("<deal id='" + tFyd.getFydlsh() + "' type='cbyztz' operation='0'>");
		sb.append("<tzdbh>");
		sb.append(tFyd.getTzdbh().trim());
		sb.append("</tzdbh>");
		sb.append("<cbsydsno>");
		sb.append(tFyd.getCbsydsno().trim());
		sb.append("</cbsydsno>");
		sb.append("<bsno>");
		sb.append(tFyd.getBsno().trim());
		sb.append("</bsno>");
		sb.append("<bname>");
		sb.append(tFyd.getBname().trim());
		sb.append("</bname>");
		sb.append("<yc>");
		sb.append(tFyd.getYc().trim());
		sb.append("</yc>");
		sb.append("<Details count='"); 
		sb.append(tFydDets.size());
		sb.append("'>");
		for(TFydDet tfd : tFydDets){
			sb.append("<Detail>");
			sb.append("<tzdbh>");
			sb.append(tfd.getTzdbh().trim());
			sb.append("</tzdbh>");
			sb.append("<cbsydsno>");
			sb.append(tfd.getCbsydsno().trim());
			sb.append("</cbsydsno>");
			sb.append("<sno>");
			sb.append(tfd.getSno());
			sb.append("</sno>");
			sb.append("<xmdm>");
			sb.append(tfd.getXmdm().trim());
			sb.append("</xmdm>");
			sb.append("<xmmc>");
			sb.append(tfd.getXmmc().trim());
			sb.append("</xmmc>");
			sb.append("<cldm>");
			sb.append(tfd.getCldm().trim());
			sb.append("</cldm>");
			sb.append("<zzmc>");
			sb.append(tfd.getZzmc().trim());
			sb.append("</zzmc>");
			sb.append("<zzgg>");
			sb.append(tfd.getZzgg().trim());
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

		Export.saveFile(Export.createDoc(sb.toString()), Export.getRootPath() + "/xml/fyd_" + fyd.getFydlsh() + "_" + DateUtil.dateToStringWithTime(new Date(),"yyyyMMddHHmmss") + ".xml");
		//OperalogServiceImpl.addOperalog(xsth.getCreateId(), xsth.getBmbh(), xsth.getMenuId(), String.valueOf(xsth.getId()),	"修改提货数量", operalogDao);
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

}
