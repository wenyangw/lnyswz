package lnyswz.jxc.action;


import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Bgrk;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.BgrkServiceI;
import lnyswz.jxc.util.Export;
import lnyswz.jxc.util.Util;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 保管入库Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("bgrkAction")
public class BgrkAction extends BaseAction implements ModelDriven<Bgrk>{
	private Bgrk bgrk = new Bgrk();
	private BgrkServiceI bgrkService;
	
	/**
	 * 保存数据
	 */
	public void save(){
		User user = (User)session.get("user");
		bgrk.setCreateId(user.getId());
		bgrk.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			j.setObj(bgrkService.save(bgrk));		
			//添加成功
			j.setSuccess(true);
			j.setMsg("保管入库成功！");
		}catch(Exception e){
			j.setMsg("保管入库失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 冲减单据
	 */
	public void cjBgrk(){
		User user = (User)session.get("user");
		bgrk.setCjId(user.getId());
		bgrk.setCjName(user.getRealName());
		Json j = new Json();
		try{
			bgrkService.cjBgrk(bgrk);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("冲减保管入库成功！");
		}catch(Exception e){
			j.setMsg("冲减保管入库失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	public void datagrid(){
		User user = (User)session.get("user");
		bgrk.setCreateId(user.getId());
		writeJson(bgrkService.datagrid(bgrk));
	}
	
	public void detDatagrid(){
		writeJson(bgrkService.detDatagrid(bgrk.getBgrklsh()));
	}

	public void getSpkc(){
		writeJson(bgrkService.getSpkc(bgrk));
	}
	
	@Override
	public Bgrk getModel() {
		return bgrk;
	}

	@Autowired
	public void setBgrkService(BgrkServiceI bgrkService) {
		this.bgrkService = bgrkService;
	}

}
