package lnyswz.jxc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Message;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.MessageServiceI;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/oa")
@Action("messageAction")
public class MessageAction extends BaseAction implements ModelDriven<Message> {
	private static final long serialVersionUID = 1L;
	private Message message = new Message();
	private MessageServiceI messageService;

	/**
	 * 增加信息
	 */
	public void add() {
		User u = (User) session.get("user");
		message.setCreateId(u.getId());
		Json j = new Json();
		try {
			messageService.add(message);
			j.setSuccess(true);
			j.setMsg("发送信息成功");
			//j.setObj(m);
		} catch (Exception e) {
			j.setMsg("发送信息失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 删除信息
	 */
	public void deleteMessage() {
		Json j = new Json();
		User u = (User) session.get("user");
		message.setCreateId(u.getId());
		try{
			messageService.deleteMessage(message);
			j.setSuccess(true);
			j.setMsg("删除信息成功");
		} catch (Exception e){
			j.setMsg("删除信息失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 取消已接收的信息
	 */
	public void cancelReceive() {
		Json j = new Json();
		User u = (User) session.get("user");
		message.setCreateId(u.getId());
		try{
			messageService.cancelReceive(message);
			j.setSuccess(true);
			j.setMsg("信息取消成功");
		}catch (Exception e){
			j.setMsg("信息取消失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 更改信息状态
	 */
	public void updateStatus() {
		Json j = new Json();
		User u = (User) session.get("user");
		message.setCreateId(u.getId());
		try {
			messageService.updateStatus(message);
			j.setSuccess(true);
			j.setMsg("信息状态更改成功");
		}catch (Exception e){
			j.setMsg("信息状态更改失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	public void getMessage(){
		User u = (User) session.get("user");
		message.setCreateId(u.getId());
		Json j = new Json();
		Message m = messageService.getMessage(message);
		j.setObj(m);
		writeJson(j);
	}

	public void sendDg() {
		User u = (User) session.get("user");
		message.setCreateId(u.getId());
		super.writeJson(messageService.sendDg(message));
	}
	
	public void receiveDg()	{
		User u = (User) session.get("user");
		message.setCreateId(u.getId());
		super.writeJson(messageService.receiveDg(message));
	}
	
	public Message getModel() {
		return message;
	}

	@Autowired
	public void setMessageService(MessageServiceI messageService) {
		this.messageService = messageService;
	}
}