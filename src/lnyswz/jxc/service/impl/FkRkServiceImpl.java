package lnyswz.jxc.service.impl;

import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.model.TFkRk;
import lnyswz.jxc.service.FkRkServiceI;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 付款入库实现类
 * @author 王文阳
 *
 */
@Service("fkRkService")
public class FkRkServiceImpl implements FkRkServiceI {
	public static List<TFkRk> listByYwrk(String ywrklsh, long ywrkId, BaseDaoI<TFkRk> fkRkDao) {
		String hql = "from TFkRk t where t.ywrklsh = :ywrklsh and t.ywrkId = :ywrkId and t.deleted = 0";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ywrklsh", ywrklsh);
		params.put("ywrkId", ywrkId);
		return fkRkDao.find(hql, params);
	}
	public static List<TFkRk> listByRkfk(String rkfklsh, BaseDaoI<TFkRk> fkRkDao) {
		String hql = "from TFkRk t where t.rkfklsh = :rkfklsh and t.deleted = 0";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rkfklsh", rkfklsh);
		return fkRkDao.find(hql, params);
	}

}
