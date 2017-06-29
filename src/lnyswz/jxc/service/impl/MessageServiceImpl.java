package lnyswz.jxc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lnyswz.jxc.bean.Paper;
import lnyswz.jxc.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Message;
import lnyswz.jxc.service.MessageServiceI;

@Service("messageService")
public class MessageServiceImpl implements MessageServiceI {
	private BaseDaoI<TMessage> messageDao;
	private BaseDaoI<TMessageRec> mesRecDao;
	private BaseDaoI<TPaper> paperDao;
	private BaseDaoI<TUser> userDao;
	private BaseDaoI<TOperalog> opeDao;

	/**
	 * 保存信息
	 */
	@Override
	public void add(Message message) {
		TMessage t = new TMessage();
		BeanUtils.copyProperties(message, t);
		t.setCreateTime(new Date());
		TUser tUser = userDao.load(TUser.class, message.getCreateId());
		t.setCreateName(tUser.getRealName());
		t.setOpened("0");
		messageDao.save(t);
		
		String[] receiverIds = message.getReceiverIds().split(",");
		TMessageRec tMesRec = null;
		for(String recId : receiverIds){
			tMesRec = new TMessageRec();
			tMesRec.setMessageId(t.getId());
			tMesRec.setReceiverId(Integer.parseInt(recId));
			tMesRec.setIsCancel("0");
			mesRecDao.save(tMesRec);
		}

		TPaper tPaper = null;
		ArrayList<Paper> papers = JSON.parseObject(message.getDatagrid(), new TypeReference<ArrayList<Paper>>(){});
		for(Paper p : papers){
			tPaper = new TPaper();
			tPaper.setFilename(p.getFilename());
			tPaper.setFilepath(p.getFilepath());
			tPaper.setTMessage(t);
			paperDao.save(tPaper);
		}
		
		message.setId(t.getId());
		OperalogServiceImpl.addOperalog(message.getCreateId(), "", message.getMenuId(), Integer.toString(message.getId()), "保存信息", opeDao);

		//return message;
	}

	/**
	 * 编辑仓库
	 */
	@Override
	public void edit(Message message) {
		TMessage g = messageDao.load(TMessage.class, message.getId());
		BeanUtils.copyProperties(message, g);
		OperalogServiceImpl.addOperalog(message.getCreateId(), "", message.getMenuId(), Integer.toString(message.getId()), "编辑信息属性", opeDao);
	}

	/**
	 * 删除仓库
	 */
	@Override
	public boolean delete(Message message) {
		boolean isOk = false;
		TMessage t = messageDao.load(TMessage.class, message.getId());
		OperalogServiceImpl.addOperalog(message.getCreateId(), "", message.getMenuId(), Integer.toString(message.getId()), "删除仓库信息", opeDao);
		return isOk;
	}

	/**
	 * 数据转换
	 * 
	 * @param l
	 * @return
	 */
	private List<Message> changeMessage(List<TMessage> l) {
		List<Message> nl = new ArrayList<Message>();
		for (TMessage t : l) {
			Message r = new Message();
			BeanUtils.copyProperties(t, r);
			nl.add(r);
		}
		return nl;
	}

	@Override
	public DataGrid sendDg(Message message) {
		DataGrid dg = new DataGrid();
		String hql = "from TMessage t where t.createId = :createId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("createId", message.getCreateId());
		List<Message> nl = new ArrayList<Message>();
		// 传入页码、每页条数
		
		String totalHql = "select count(*) " + hql;
		hql += " order by createTime desc";
		
		List<TMessage> l = messageDao.find(hql, params, message.getPage(), message.getRows());
		// 处理返回信息
		for (TMessage t : l) {
			Message nc = new Message();
			BeanUtils.copyProperties(t, nc, new String[]{"memo"});
			nl.add(nc);
		}
		
		dg.setTotal(messageDao.count(totalHql, params));
		dg.setRows(nl);
		return dg;
	}
	
	@Override
	public DataGrid receiveDg(Message c) {
		DataGrid dg = new DataGrid();
		String hql = "from TMessage t ";
		String totalHql = "select count(*) " + hql;
		List<Message> nl = new ArrayList<Message>();
		// 传入页码、每页条数
		List<TMessage> l = messageDao.find(hql, c.getPage(), c.getRows());
		// 处理返回信息
		for (TMessage t : l) {
			Message nc = new Message();
			BeanUtils.copyProperties(t, nc);
			nl.add(nc);
		}
		dg.setTotal(messageDao.count(totalHql));
		dg.setRows(nl);
		return dg;
	}


	@Autowired
	public void setMessageDao(BaseDaoI<TMessage> messageDao) {
		this.messageDao = messageDao;
	}

	@Autowired
	public void setMesRecDao(BaseDaoI<TMessageRec> mesRecDao) {
		this.mesRecDao = mesRecDao;
	}

	@Autowired
	public void setPaperDao(BaseDaoI<TPaper> paperDao) {
		this.paperDao = paperDao;
	}

	@Autowired
	public void setUserDao(BaseDaoI<TUser> userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setOpeDao(BaseDaoI<TOperalog> opeDao) {
		this.opeDao = opeDao;
	}

}
