package lnyswz.jxc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.model.TFhzz;
import lnyswz.jxc.model.TKfzz;
import lnyswz.jxc.model.TLszz;
import lnyswz.jxc.model.TYszz;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.service.MonthServiceI;

/**
 * 月末结账
 * 
 * @author 王文阳
 * 
 */
@Service("monthService")
public class MonthServiceImpl implements MonthServiceI {
	private BaseDaoI<TYwzz> ywzzDao;
	
	/**
	 * 月末结账
	 */
	@Override
	public void update() {
		//处理t_ywzz
		String ywzz_sql_bz = "update t_ywzz set xscb = qcje + rkje where jzsj = convert(char(6), GETDATE(), 112) and qcsl + rksl - xssl = 0 and qcje + rkje - xscb <> 0";
		ywzzDao.updateBySQL(ywzz_sql_bz);
		String ywzz_sql = "insert into t_ywzz"
				+ " select convert(char(6), dateadd(m, 1, GETDATE()), 112) jzsj,bmbh,bmmc,ckId,ckmc,spbh,spmc,spcd,sppp,spbz,zjldwId,zjldwmc,cjldwId,cjldwmc,zhxs,"
				+ " qcsl + rksl - xssl qcsl, qcje + rkje - xscb qcje, (qcje + rkje - xscb) / (qcsl + rksl - xssl) dwcb, 0 rksl, 0 rkje, 0 xssl, 0 xsje, 0 xsse, 0 xscb, cqcsl + crksl - cxssl cqcsl, 0 crksl, 0 cxssl"
				+ " from t_ywzz"
				+ " where jzsj = convert(char(6), GETDATE(), 112) and qcsl + rksl - xssl <> 0";
		ywzzDao.updateBySQL(ywzz_sql);
		
		
		//处理t_lszz
		String lszz_sql = "insert into t_lszz "
				+ " select bmbh, bmmc, ckId, ckmc, spbh, spmc, spcd, sppp, spbz, zjldwId, zjldwmc, cjldwId, cjldwmc, zhxs,"
				+ " qcsl + lssl - kpsl qcsl, qcje + lsje - kpje qcje, 0 lssl, 0 lsje, 0 kpsl, 0 kpje,"
				+ " convert(char(6), dateadd(m, 1, GETDATE()), 112) jzsj,"
				+ " cqcsl + clssl - ckpsl cqcsl, 0 clssl, 0 ckpsl"
				+ " from t_lszz where jzsj = convert(char(6), GETDATE(), 112) and qcsl + lssl - kpsl <> 0";
		ywzzDao.updateBySQL(lszz_sql);
		
		//处理t_kfzz
		String kfzz_sql = "insert into t_kfzz"
				+ " select spbh,spmc,spcd,sppp,spbz,zjldwId,zjldwmc,cjldwId,cjldwmc,zhxs,bmbh,bmmc,ckId,ckmc,hwId,hwmc,sppc,"
				+ " qcsl + rksl - cksl qcsl, cqcsl + crksl - ccksl cqcsl, 0 rksl, 0 crksl, 0 cksl, 0 ccksl, convert(char(6), dateadd(m, 1, GETDATE()), 112) jzsj"
				+ " from t_kfzz"
				+ " where jzsj = convert(char(6), GETDATE(), 112) and qcsl + rksl - cksl <> 0";
		ywzzDao.updateBySQL(kfzz_sql);
		
		//商品总数量已为0，但各分类仍然存在，并且正负相抵
		//String kfzz_delete = "delete from t_kfzz where jzsj = convert(char(6), dateadd(m, 1, GETDATE()), 112) and ckId is not null and spbh not in (select spbh from t_kfzz where jzsj = convert(char(6), dateadd(m, 1, GETDATE()), 112) and bmbh = t_kfzz.bmbh and spbh = t_kfzz.spbh and ckId is null)";
		//ywzzDao.updateBySQL(kfzz_delete);
		
		//处理t_fhzz;
		String fhzz_sql = "insert into t_fhzz"
				+ " select bmbh,bmmc,fhId,fhmc,convert(char(6), dateadd(m, 1, GETDATE()), 112) jzsj,spbh,spmc,spcd,sppp,spbz,zjldwId,zjldwmc,cjldwId,cjldwmc,zhxs,"
				+ " qcsl + rksl - cksl qcsl, 0 rksl, 0 cksl"
				+ "	from t_fhzz"
				+ " where jzsj = convert(char(6), GETDATE(), 112) and qcsl + rksl - cksl <> 0";
		ywzzDao.updateBySQL(fhzz_sql);
		
		//处理t_yszz
		String yszz_sql = "insert into t_yszz"
				+ " select bmbh, bmmc, khbh, khmc, ywyId, ywymc,"
				+ " lsje, qcje + kpje - hkje qcje, qcthje, 0 kpje, thje, 0 hkje, convert(char(6), dateadd(m, 1, GETDATE()), 112) jzsj"
				+ " from t_yszz where jzsj = convert(char(6), GETDATE(), 112) and (qcje + kpje - hkje <> 0 or thje <> 0)";
		ywzzDao.updateBySQL(yszz_sql);

		//处理t_bgzz;
		String bgzz_sql = "insert into t_bgzz"
				+ " select bmbh,bmmc,spbh,spmc,spcd,sppp,spbz,zjldwId,zjldwmc,cjldwId,cjldwmc,zhxs,"
				+ " qcsl + rksl - cksl qcsl, 0 rksl, 0 cksl,convert(char(6), dateadd(m, 1, GETDATE()), 112) jzsj"
				+ "	from t_bgzz"
				+ " where jzsj = convert(char(6), GETDATE(), 112) and qcsl + rksl - cksl <> 0";
		ywzzDao.updateBySQL(bgzz_sql);

		//处理t_yfzz;
		String yfzz_sql = "insert into t_yfzz"
				+ " select bmbh,bmmc,gysbh,gysmc,"
				+ " qcje + rkje - fkje qcje, 0 rkje, 0 fkje, convert(char(6), dateadd(m, 1, GETDATE()), 112) jzsj"
				+ "	from t_yfzz"
				+ " where jzsj = convert(char(6), GETDATE(), 112) and qcje + rkje - fkje <> 0";
		ywzzDao.updateBySQL(yfzz_sql);

		//库存平衡表处理
		String kcphb_sql = "exec p_kcphb '', 1, '', '', '=', '" + DateUtil.getCurrentDateString("yyyyMM") + "', 1, 1, 2";

		ywzzDao.updateBySQL(kcphb_sql);
	}

	
	@Autowired
	public void setYwzzDao(BaseDaoI<TYwzz> ywzzDao) {
		this.ywzzDao = ywzzDao;
	}
	
}
