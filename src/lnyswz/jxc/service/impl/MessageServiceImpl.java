package lnyswz.jxc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Paper;
import lnyswz.jxc.model.*;
import lnyswz.jxc.util.Upload;
import lnyswz.jxc.util.Util;
import org.apache.commons.lang3.StringUtils;
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
		t.setOpened("1");
		t.setIsCancel("0");
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
			tPaper.setType(p.getType());
			tPaper.setTMessage(t);
			paperDao.save(tPaper);
		}

		message.setId(t.getId());
		OperalogServiceImpl.addOperalog(message.getCreateId(), "", message.getMenuId(), Integer.toString(message.getId()), "保存信息", opeDao);
	}

	/**
	 * 删除信息
	 */
	@Override
	public void deleteMessage(Message message) {
		TMessage t = messageDao.load(TMessage.class, message.getId());

		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.put("0", t.getId());

		//删除附件
		String fileSql = "select filepath from t_paper where messageId = ?";
		List<Object[]> files = messageDao.findBySQL(fileSql, sqlParams);
		for (Object file : files) {
			Upload.deleteFile(file.toString());
		}

		//删除t_paper
		String paperSql = "delete t_paper where messageId = ?";
		messageDao.updateBySQL(paperSql, sqlParams);

		//删除t_message_rec
		String recSql = "delete t_message_rec where messageId = ?";
		messageDao.updateBySQL(recSql, sqlParams);

		//删除t_message
		messageDao.delete(t);

		OperalogServiceImpl.addOperalog(message.getCreateId(), "", message.getMenuId(), Integer.toString(message.getId()), "删除仓库信息", opeDao);
	}

	/**
	 * 取消已接收信息
	 */
	@Override
	public void cancelReceive(Message message) {
		TMessageRec t = mesRecDao.load(TMessageRec.class, message.getRecId());
		t.setIsCancel("1");
		t.setCancelTime(new Date());
		OperalogServiceImpl.addOperalog(message.getCreateId(), "", message.getMenuId(), Integer.toString(message.getId()), "取消已接收信息", opeDao);
	}

	/**
	 * 更改信息状态
	 */
	@Override
	public void updateStatus(Message message) {
		TMessage t = messageDao.load(TMessage.class, message.getId());
		t.setOpened(t.getOpened().equals("1") ? "0" : "1");
		OperalogServiceImpl.addOperalog(message.getCreateId(), "", message.getMenuId(), Integer.toString(message.getId()), "更改已发送信息状态", opeDao);
	}

	@Override
	public Message getMessage(Message message){
		TMessage tMessage = messageDao.load(TMessage.class, message.getId());
		Message m = new Message();

		BeanUtils.copyProperties(tMessage, m);

		if(message.getSource().equals("receive")){
			String hql = "from TMessageRec t where t.messageId = :messageId and t.receiverId = :receiverId";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("messageId", message.getId());
			params.put("receiverId", message.getCreateId());
			TMessageRec tMessageRec = mesRecDao.get(hql, params);
			if(tMessageRec.getReadTime() == null) {
				tMessageRec.setReadTime(new Date());
				mesRecDao.save(tMessageRec);
			}
		}
		return m;
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
		if(message.getSearch() != null && message.getSearch().length() > 0){
			hql += " and (" + Util.getQueryWhere(message.getSearch(), new String[]{"t.subject", "t.memo"}, params) + ")";
		}
		List<Message> nl = new ArrayList<Message>();
		// 传入页码、每页条数

		String totalHql = "select count(*) " + hql;
		hql += " order by createTime desc";

		List<TMessage> l = messageDao.find(hql, params, message.getPage(), message.getRows());
		// 处理返回信息
		for (TMessage t : l) {
			Message nc = new Message();
			BeanUtils.copyProperties(t, nc, new String[]{"memo"});

			String sqlMr = "select realName from t_message_rec mr left join t_user u on mr.receiverId = u.id where mr.messageId = ?";
			Map<String, Object> paramsMr = new HashMap<String, Object>();
			paramsMr.put("0", t.getId());
			List<Object[]> list_mr = mesRecDao.findBySQL(sqlMr, paramsMr);
			nc.setReceiverNames(StringUtils.join(list_mr.toArray(), ","));
			nl.add(nc);
		}

		dg.setTotal(messageDao.count(totalHql, params));
		dg.setRows(nl);
		return dg;
	}

	@Override
	public DataGrid receiveDg(Message message) {
		DataGrid dg = new DataGrid();
		String sql = "select m.id, m.subject, m.createTime, m.createId, m.createName, mr.readTime, mr.id recId";
		String sqlFrom = " from t_message_rec mr left join t_message m on mr.messageId = m.id where mr.receiverId = ? and m.opened = '1' and mr.isCancel='0'";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", message.getCreateId());
		if(message.getSearch() != null && message.getSearch().length() > 0){
			sqlFrom += " and (" + Util.getQuerySQLWhere(message.getSearch(), new String[]{"m.subject", "m.memo", "m.createName"}, params, 1) + ")";
		}
		List<Message> nl = new ArrayList<Message>();
		// 传入页码、每页条数
		List<Object[]> l = messageDao.findBySQL(sql + sqlFrom +  " order by m.createTime desc", params, message.getPage(), message.getRows());
		// 处理返回信息
		for (Object[] o : l) {
			Message m = new Message();
			m.setId(Integer.parseInt(o[0].toString()));
			m.setSubject(o[1].toString());
			m.setCreateTime(DateUtil.stringToDate(o[2].toString(), DateUtil.DATETIME_PATTERN));
			m.setCreateId(Integer.parseInt(o[3].toString()));
			m.setCreateName(o[4].toString());
			m.setReadTime(DateUtil.stringToDate(o[5] == null ? "" : o[5].toString(), DateUtil.DATETIME_PATTERN));
			m.setRecId(Integer.parseInt(o[6].toString()));
			nl.add(m);
		}
		dg.setRows(nl);

		String totalSql = "select count(*) " + sqlFrom;
		dg.setTotal(messageDao.countSQL(totalSql, params));

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
