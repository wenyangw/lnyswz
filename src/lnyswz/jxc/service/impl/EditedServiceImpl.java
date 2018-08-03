package lnyswz.jxc.service.impl;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.ProBean;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.*;
import lnyswz.jxc.model.TEdited;
import lnyswz.jxc.model.TYwzz;
import lnyswz.jxc.service.YwzzServiceI;
import lnyswz.jxc.util.Constant;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改记录实现类
 * @author 王文阳
 *
 */
@Service("editedService")
public class EditedServiceImpl{
	private BaseDaoI<TEdited> editedDao;

	/**
	 * 增加修改记录
	 */
	public static void addEdited(Edited edited, BaseDaoI<TEdited> baseDao) {
		TEdited tEdited = new TEdited();
		BeanUtils.copyProperties(edited, tEdited);

		baseDao.save(tEdited);
	}
}
