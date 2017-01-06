package lnyswz.jxc.action;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Xskp;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.XskpServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Export;
import lnyswz.jxc.util.Util;
/**
 * 销售开票Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("xskpAction")
public class XskpAction extends BaseAction implements ModelDriven<Xskp>{
	private static final long serialVersionUID = 1L;
	private Xskp xskp = new Xskp();
	private XskpServiceI xskpService;
	
	/**
	 * 保存数据
	 */
	public void save(){
		User user = (User)session.get("user");
		xskp.setCreateId(user.getId());
		xskp.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			j.setObj(xskpService.save(xskp));		
			//添加成功
			j.setSuccess(true);
			j.setMsg("保存销售开票成功！");
		}catch(Exception e){
			j.setMsg("保存销售开票失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 保存返利数据
	 */
	public void saveXsfl(){
		User user = (User)session.get("user");
		xskp.setCreateId(user.getId());
		xskp.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			j.setObj(xskpService.saveXsfl(xskp));		
			//添加成功
			j.setSuccess(true);
			j.setMsg("保存销售返利成功！");
		}catch(Exception e){
			j.setMsg("保存销售返利失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	
	/**
	 * 冲减单据
	 */
	public void cjXskp(){
		User user = (User)session.get("user");
		xskp.setCjId(user.getId());
		xskp.setCjName(user.getRealName());
		Json j = new Json();
		try{
			xskpService.cjXskp(xskp);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("冲减销售开票成功！");
		}catch(Exception e){
			j.setMsg("冲减销售开票失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 冲减返利单据
	 */
	public void cjXsfl(){
		User user = (User)session.get("user");
		xskp.setCjId(user.getId());
		xskp.setCjName(user.getRealName());
		Json j = new Json();
		try{
			xskpService.cjXsfl(xskp);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("冲减销售成功！");
		}catch(Exception e){
			j.setMsg("冲减销售失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 生成销售提货单
	 */
	public void createXsth(){
		User user = (User)session.get("user");
		xskp.setCreateId(user.getId());
		xskp.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			xskpService.createXsth(xskp);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("生成销售提货单成功！");
		}catch(Exception e){
			j.setMsg("生成销售提货单失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	public void toJs(){
		Export.toJs(xskpService.toJs(xskp.getXskplsh()));
	}
	
	public void printXsqk() {
		User user = (User)session.get("user");
		xskp.setCreateName(user.getRealName());
		DataGrid dg = xskpService.printXsqk(xskp);
		Export.print(dg, Util.getReportName(xskp.getBmbh(), "report_xsqk.json"));
		//Export.print(dg, Constant.REPORT_XSQK.get(xskp.getBmbh()));
	}
	
	public void datagrid(){
		writeJson(xskpService.datagrid(xskp));
	}
	
	public void datagridXsfl(){
		writeJson(xskpService.datagridXsfl(xskp));
	}
	
	public void detDatagrid(){
		writeJson(xskpService.detDatagrid(xskp.getXskplsh()));
	}
	
	public void datagridDet(){
		writeJson(xskpService.datagridDet(xskp));
	}
	
	public void toYwrk(){
		writeJson(xskpService.toYwrk(xskp));
	}
	
	public void toXsth(){
		writeJson(xskpService.toXsth(xskp.getXskpDetIds()));
	}
	
	public void getSpkc(){
		writeJson(xskpService.getSpkc(xskp));
	}
	
	public void getXskpNoHk(){
		writeJson(xskpService.getXskpNoHk(xskp));
	}
	
	public void getXskpNoHkFirst(){
		writeJson(xskpService.getXskpNoHkFirst(xskp));
	}
	
	public void getLatestXs(){
		writeJson(xskpService.getLatestXs(xskp));
	}
	
	public void listFyrs(){
		writeJson(xskpService.listFyrs(xskp));
	}
	
	@Override
	public Xskp getModel() {
		return xskp;
	}

	@Autowired
	public void setXskpService(XskpServiceI xskpService) {
		this.xskpService = xskpService;
	}

}
