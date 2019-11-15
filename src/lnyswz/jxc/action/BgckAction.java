package lnyswz.jxc.action;


import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Bgck;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.BgckServiceI;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 保管入库Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("bgckAction")
public class BgckAction extends BaseAction implements ModelDriven<Bgck>{
	private Bgck bgck = new Bgck();
	private BgckServiceI bgckService;
	
	/**
	 * 保存数据
	 */
	public void save(){
		User user = (User)session.get("user");
		bgck.setCreateId(user.getId());
		bgck.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			j.setObj(bgckService.save(bgck));		
			//添加成功
			j.setSuccess(true);
			j.setMsg("保管出库成功！");
		}catch(Exception e){
			j.setMsg("保管出库失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 冲减单据
	 */
	public void cjBgck(){
		User user = (User)session.get("user");
		bgck.setCjId(user.getId());
		bgck.setCjName(user.getRealName());
		Json j = new Json();
		try{
			bgckService.cjBgck(bgck);		
			//添加成功
			j.setSuccess(true);
			j.setMsg("冲减保管出库成功！");
		}catch(Exception e){
			j.setMsg("冲减保管出库失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	public void datagrid(){
		User user = (User)session.get("user");
		bgck.setCreateId(user.getId());
		writeJson(bgckService.datagrid(bgck));
	}
	
	public void detDatagrid(){
		writeJson(bgckService.detDatagrid(bgck.getBgcklsh()));
	}
	
	@Override
	public Bgck getModel() {
		return bgck;
	}

	@Autowired
	public void setBgckService(BgckServiceI bgckService) {
		this.bgckService = bgckService;
	}

}
