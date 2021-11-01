package lnyswz.jxc.action;


import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.bean.Kfdb;
import lnyswz.jxc.service.KfdbServiceI;
import lnyswz.jxc.util.Export;
import lnyswz.jxc.util.Util;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 库房调拨Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("kfdbAction")
public class KfdbAction extends BaseAction implements ModelDriven<Kfdb>{
	private static final long serialVersionUID = 1L;
	private Kfdb kfdb = new Kfdb();
	private KfdbServiceI kfdbService;
	
	/**
	 * 保存数据
	 */
	public void save(){
		User user = (User)session.get("user");
		kfdb.setCreateId(user.getId());
		kfdb.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			j.setObj(kfdbService.save(kfdb));		
			//添加成功
			j.setSuccess(true);
			j.setMsg("保存库房调拨成功！");
		}catch(Exception e){
			j.setMsg("保存库房调拨失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 冲减单据
	 */
	public void cjKfdb(){
		User user = (User)session.get("user");
		kfdb.setCjId(user.getId());
		kfdb.setCjName(user.getRealName());
		Json j = new Json();
		try{
			kfdbService.cjKfdb(kfdb);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("冲减库房调拨成功！");
		}catch(Exception e){
			j.setMsg("冲减库房调拨失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	public void datagrid(){
		User user = (User)session.get("user");
		kfdb.setCreateId(user.getId());
		writeJson(kfdbService.datagrid(kfdb));
	}
	
	public void detDatagrid(){
		writeJson(kfdbService.detDatagrid(kfdb.getKfdblsh()));
	}

	@Override
	public Kfdb getModel() {
		return kfdb;
	}

	@Autowired
	public void setKfdbService(KfdbServiceI kfdbService) {
		this.kfdbService = kfdbService;
	}

}
