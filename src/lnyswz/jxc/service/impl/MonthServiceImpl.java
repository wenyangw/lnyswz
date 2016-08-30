package lnyswz.jxc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Dict;
import lnyswz.jxc.bean.MonthHandler;
import lnyswz.jxc.model.TButton;
import lnyswz.jxc.model.TDict;
import lnyswz.jxc.model.TFhzz;
import lnyswz.jxc.model.TKfzz;
import lnyswz.jxc.model.TLszz;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.model.TYszz;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.service.DictServiceI;
import lnyswz.jxc.service.MonthServiceI;

/**
 * 字典实现类
 * 
 * @author 王文阳
 * 
 */
@Service("monthService")
public class MonthServiceImpl implements MonthServiceI {
	private BaseDaoI<TYwzz> ywzzDao;
	private BaseDaoI<TLszz> lszzDao;
	private BaseDaoI<TKfzz> kfzzDao;
	private BaseDaoI<TYszz> yszzDao;
	private BaseDaoI<TFhzz> fhzzDao;
	
	/**
	 * 月末结账
	 */
	@Override
	public MonthHandler update(MonthHandler m) {
		//处理t_ywzz
		String ywzz_sql = 
		
		//处理t_lszz
		
		//处理t_kfzz
		
		//处理t_fhzz
		
		//处理t_yszz
		
		return new MonthHandler();
	}

	
	@Autowired
	public void setYwzzDao(BaseDaoI<TYwzz> ywzzDao) {
		this.ywzzDao = ywzzDao;
	}
	
	@Autowired
	public void setLszzDao(BaseDaoI<TLszz> lszzDao) {
		this.lszzDao = lszzDao;
	}
	
	@Autowired
	public void setKfzzDao(BaseDaoI<TKfzz> kfzzDao) {
		this.kfzzDao = kfzzDao;
	}
	
	@Autowired
	public void setFhzzDao(BaseDaoI<TFhzz> fhzzDao) {
		this.fhzzDao = fhzzDao;
	}
	
	@Autowired
	public void setYszzDao(BaseDaoI<TYszz> yszzDao) {
		this.yszzDao = yszzDao;
	}

	

}
