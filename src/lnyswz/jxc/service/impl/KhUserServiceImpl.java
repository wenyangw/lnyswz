package lnyswz.jxc.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lnyswz.jxc.model.TUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.KhUser;
import lnyswz.jxc.model.TKhUser;
import lnyswz.jxc.model.TOperalog;
import lnyswz.jxc.service.KhUserServiceI;

@Service("khUserService")
public class KhUserServiceImpl implements KhUserServiceI {
	private BaseDaoI<TKhUser> khUserDao;
	private BaseDaoI<TUser> ywyDao;
	private BaseDaoI<TOperalog> opeDao;
	
	/**
	 * 保存客户用户
	 */
	@Override
	public KhUser add(KhUser khUser) {
		TKhUser t = new TKhUser();
		khUser.setCreateTime(new Date());
		BeanUtils.copyProperties(khUser, t);

		TUser tYwy = ywyDao.load(TUser.class, khUser.getYwyId());
		khUserDao.save(t);
		khUser.setId(t.getId());
		OperalogServiceImpl.addOperalog(khUser.getId(), tYwy.getTDepartment().getId(), khUser.getMenuId(), khUser.getId() + "" , "添加客户用户", opeDao);
		return khUser;
	}

	/**
	 * 修改客户用户
	 */
	@Override
	public void edit(KhUser khUser) {
	    String hql = "from TKhUser t where t.openId = :openId";
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("openId", khUser.getOpenId());
		TKhUser t = khUserDao.get(hql, params);
		t.setRealName(khUser.getRealName());
		t.setPhone(khUser.getPhone());
		t.setDwmc(khUser.getDwmc());

        TUser tYwy = ywyDao.load(TUser.class, t.getYwyId());
		OperalogServiceImpl.addOperalog(khUser.getId(), tYwy.getTDepartment().getId(), khUser.getMenuId(), t.getId() + "" , "修改客户用户", opeDao);
	}

	/**
	 * 删除客户用户
	 */
	@Override
	public void delete(KhUser khUser) {
		TKhUser t = khUserDao.load(TKhUser.class, khUser.getId());
		TUser tYwy = ywyDao.load(TUser.class, khUser.getYwyId());
		khUserDao.delete(t);
		OperalogServiceImpl.addOperalog(khUser.getCreateId(), tYwy.getTDepartment().getId(), khUser.getMenuId(), t.getId() + "" , "删除客户用户", opeDao);
	}
	
	/**
	 * 验证用户是否已在系统注册
	 */
	@Override
	public KhUser checkKhUser(KhUser khUser) {
		String sql = "from TKhUser u where u.openId = :openId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("openId", khUser.getOpenId());
		TKhUser t = khUserDao.get(sql, params);
		KhUser k = null;
		if(t != null) {
			k = new KhUser();
			BeanUtils.copyProperties(t, k);
		}
		return k;
	}

	public static TKhUser getKhUserByOpenId (String openId, BaseDaoI<TKhUser> uDao){
		String hql = "from TKhUser t where t.openId = :openId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("openId", openId);
		TKhUser tKhUser = uDao.get(hql, params);
		return tKhUser;
	}

	@Autowired
	public void setKhUserDao(BaseDaoI<TKhUser> khUserDao) {
		this.khUserDao = khUserDao;
	}

	@Autowired
	public void setYwyDao(BaseDaoI<TUser> ywyDao) {
		this.ywyDao = ywyDao;
	}

	@Autowired
	public void setOpeDao(BaseDaoI<TOperalog> opeDao) {
		this.opeDao = opeDao;
	}

}
