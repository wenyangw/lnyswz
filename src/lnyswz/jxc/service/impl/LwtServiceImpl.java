package lnyswz.jxc.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Lwt;
import lnyswz.jxc.bean.Xsth;
import lnyswz.jxc.service.LwtServiceI;

@Service("lwtnService")
public class LwtServiceImpl implements LwtServiceI {
	private BaseDaoI<Object> lwtDao;


	
	
	@Override
	public DataGrid listKhByYwy(Lwt lwt) {
		DataGrid dg = new DataGrid();
		//execute m_kh '部门ide','ywyId'
		String execHql = "execute m_ywy_kh '" + lwt.getBmbh() + "','" + lwt.getUserId() + "','" + lwt.getSearch().trim() + "','" + lwt.getPage() + "','" + lwt.getRows() + "'";
		List<Lwt> l = new ArrayList<Lwt>();
		List<Object[]> list = lwtDao.findBySQL(execHql);
		for(Object[] o : list){
			Lwt k = getKhByYwy(o);
			l.add(k);
		}
		String countexecHql = "execute m_ywy_kh '" + lwt.getBmbh() + "','" + lwt.getUserId() + "','" + lwt.getSearch().trim() + "','0','0'";
		dg.setTotal(lwtDao.countSQL(countexecHql));
		
		dg.setRows(l);
		return dg;
	}
	
	@Override
	public DataGrid listKhByYwyXsth(Lwt lwt) {
		// TODO Auto-generated method stub
		DataGrid dg = new DataGrid();
		
		String sql = "select distinct xsthlsh, hjje, hjsl, ckmc, bz, needAudit, isAudit from v_xsth_det "; 
		String where = " where bmbh = ? and ywyId = ? and khbh = ?  ";
		if(lwt.getType().equals("xsth")){
			where += " and zdwsl = cksl and isCancel = '0'  and SUBSTRING(xsthlsh,1,2) > 16  "; 
		}else if(lwt.getType().equals("needAudit_xsth")){
			where += " and  isCancel='0' and needAudit > isAudit and SUBSTRING(xsthlsh,1,2) > 16 ";
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", lwt.getBmbh());
		params.put("1", lwt.getUserId());
		params.put("2", lwt.getKhbh());
		sql = sql + where ;
		String sqlOrder = " order by xsthlsh desc";
		List<Xsth> l = new ArrayList<Xsth>();
		List<Object[]> list = lwtDao.findBySQL(sql+sqlOrder,params, lwt.getPage(), lwt.getRows());
		for(Object[] o : list){
			Xsth x = getKhByYwyXsth(o);

			l.add(x);
		}
		String totalHql = "select count(*)  from (" + sql + ")as count";
		dg.setTotal(lwtDao.countSQL(totalHql,params));
		dg.setRows(l);
		return dg;
	}
	
	
	
	private Xsth getKhByYwyXsth(Object[] o) {
		Xsth x = new Xsth();
		String xsthlsh = (String)o[0];
		BigDecimal hjje = new BigDecimal(o[1].toString());
		BigDecimal hjsl = new BigDecimal(o[2].toString());
		String ckmc = (String)o[3];
		String bz = (String)o[4];
		String needAudit = ((Character) o[5]).toString();	
		String isAudit = ((Character)o[6]).toString();	

		x.setXsthlsh(xsthlsh);
		x.setHjje(hjje);
		x.setHjsl(hjsl);
		x.setCkmc(ckmc);
		x.setBz(bz);
		x.setNeedAudit(needAudit);
		x.setIsAudit(isAudit);
		return x;
	}
	
	private Lwt getKhByYwy(Object[] o) {
		Lwt k = new Lwt();
		String khbh = (String)o[0];
		String khmc = (String)o[1];
		BigDecimal sxje = new BigDecimal(o[2].toString());
		int sxzq = (Integer)o[3];
		BigDecimal ysje = new BigDecimal(o[4].toString());
		BigDecimal thje = new BigDecimal(o[5].toString());
		
		k.setKhbh(khbh);
		k.setKhmc(khmc);
		k.setSxje(sxje);
		k.setSxzq(sxzq);
		k.setThje(thje);
		k.setYsje(ysje);
		return k;
	}

	
	@Autowired
	public void setLwtDao(BaseDaoI<Object> lwtDao) {
		this.lwtDao = lwtDao;
	}


}
