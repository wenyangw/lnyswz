package lnyswz.jxc.action;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;





import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Xsth;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.XsthServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Export;
/**
 * 销售提货Action
 * @author 王文阳
 * @edited
 * 	2015.08.12 增加打印销售合同
 * 
 * 
 */
@Namespace("/jxc")
@Action("xsthAction")
public class XsthAction extends BaseAction implements ModelDriven<Xsth>{
	private Logger logger = Logger.getLogger(XsthAction.class);
	private Xsth xsth = new Xsth();
	private XsthServiceI xsthService;
	
	/**
	 * 保存数据
	 */
	public void save(){
		User user = (User)session.get("user");
		xsth.setCreateId(user.getId());
		xsth.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			j.setObj(xsthService.save(xsth));		
			//添加成功
			j.setSuccess(true);
			j.setMsg("保存销售提货成功！");
		}catch(Exception e){
			j.setMsg("保存销售提货失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 冲减单据
	 */
	public void cancelXsth(){
		User user = (User)session.get("user");
		xsth.setCancelId(user.getId());
		xsth.setCancelName(user.getRealName());
		Json j = new Json();
		try{
			xsthService.cancelXsth(xsth);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("取消销售提货成功！");
		}catch(Exception e){
			j.setMsg("取消销售提货失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	public void toKfck(){
		writeJson(xsthService.toKfck(xsth));
	}
	
	public void refreshXsth(){
		writeJson(xsthService.refreshXsth(xsth));
	}
	
	/**
	 * 修改销售提货数量
	 */
	public void updateThsl(){
		User user = (User)session.get("user");
		xsth.setCreateId(user.getId());
		xsth.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			xsthService.updateThsl(xsth);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("修改销售提货数量成功！");
		}catch(Exception e){
			j.setMsg("修改销售提货数量失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 锁定销售提货
	 */
	public void updateLock(){
		User user = (User)session.get("user");
		xsth.setLockId(user.getId());
		xsth.setLockName(user.getRealName());
		Json j = new Json();
		try{
			xsthService.updateLock(xsth);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("锁定销售提货成功！");
		}catch(Exception e){
			j.setMsg("锁定销售提货失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 解锁销售提货
	 */
	public void updateUnlock(){
		User user = (User)session.get("user");
		xsth.setLockId(user.getId());
		xsth.setLockName(user.getRealName());
		Json j = new Json();
		try{
			xsthService.updateUnlock(xsth);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("解锁销售提货成功！");
		}catch(Exception e){
			j.setMsg("解锁销售提货失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	public void toXskp(){
		writeJson(xsthService.toXskp(xsth.getXsthDetIds()));
	}
	
	public void getSpBgys(){
		writeJson(xsthService.getSpBgys(xsth));
	}
	
	public void datagrid(){
		User u = (User)session.get("user");
		if("1".equals(u.getIsYwy())){
			xsth.setYwyId(u.getId());
		}
		writeJson(xsthService.datagrid(xsth));
	}
	
	public void detDatagrid(){
		writeJson(xsthService.detDatagrid(xsth.getXsthlsh()));
	}
	
	public void datagridDet(){
		User u = (User)session.get("user");
		
		if("1".equals(u.getIsBgy())){
			xsth.setBgyId(u.getId());
		}
		writeJson(xsthService.datagridDet(xsth));
	}
	
	public void printXsth() {
		User user = (User)session.get("user");
		xsth.setCreateName(user.getRealName());
		DataGrid dg = xsthService.printXsth(xsth);
		Export.print(dg, Constant.REPORT_XSTH.get(xsth.getBmbh()));
	}
	
	public void printXsht() {
		User user = (User)session.get("user");
		xsth.setCreateName(user.getRealName());
		DataGrid dg = xsthService.printXsht(xsth);
		Export.print(dg, Constant.REPORT_XSHT.get(xsth.getBmbh()));
	}
	
	public void printThd() {
		User user = (User)session.get("user");
		xsth.setCreateName(user.getRealName());
		DataGrid dg = xsthService.printXsth(xsth);
		Export.print(dg, Constant.REPORT_XSTH_KF.get(xsth.getBmbh()));
	}
	
	public void printXsthByBgy() {
		User user = (User)session.get("user");
		xsth.setCreateName(user.getRealName());
		DataGrid dg = xsthService.printXsthByBgy(xsth);
		Export.print(dg, Constant.REPORT_XSTH_BGY.get(xsth.getBmbh()));
	}
	
	public void getSpkc(){
		writeJson(xsthService.getSpkc(xsth));
	}
	
	public void getYsje(){
		writeJson(xsthService.getYsje(xsth));
	}
	
	@Override
	public Xsth getModel() {
		return xsth;
	}

	@Autowired
	public void setXsthService(XsthServiceI xsthService) {
		this.xsthService = xsthService;
	}

}
