package lnyswz.jxc.service.impl;

import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Print;
import lnyswz.jxc.model.TPrint;
import lnyswz.jxc.service.PrintServiceI;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * 打印实现类
 * @author 王文阳
 *
 */
@Service("printService")
public class PrintServiceImpl implements PrintServiceI{
	private BaseDaoI<TPrint> printDao;

	@Override
	public int getCounts(Print p) {
		String hql = "select count(*) from TPrint t where t.lsh = :lsh and t.bgyId = :bgyId and t.type = :type";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lsh", p.getLsh());
		params.put("bgyId", p.getBgyId());
		params.put("type", p.getType());

		long counts = printDao.count(hql, params);

		return (int)counts;
	}

	public static void save(Print b, BaseDaoI<TPrint> baseDao) {
		TPrint t = new TPrint();
		BeanUtils.copyProperties(b, t);
		baseDao.save(t);
	}

	@Autowired
	public void setPrintDao(BaseDaoI<TPrint> printDao) {
		this.printDao = printDao;
	}
}
