package lnyswz.jxc.action;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.util.Export;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.SpServiceI;
import lnyswz.jxc.util.Constant;

import java.util.List;

/**
 * 商品Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("spAction")
public class SpAction extends BaseAction implements ModelDriven<Sp>{
	private static final long serialVersionUID = 1L;
	private Sp sp = new Sp();
	private SpServiceI spService;
	
	/**
	 * 添加商品
	 */
	public void add(){
		Json j = new Json();
		User u = (User)session.get("user");
		sp.setUserId(u.getId());
		try{
			//将获得的前台内容传入Service
			Sp s = spService.add(sp);
			//添加成功
			j.setSuccess(true);
			j.setMsg("增加商品成功！");
			j.setObj(s);
		}catch(Exception e){
			j.setMsg("增加商品失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改商品
	 */
	public void edit(){
		Json j = new Json();
		User u = (User)session.get("user");
		sp.setUserId(u.getId());
		try {
			//将获得的前台内容传入Service
			spService.edit(sp);
			j.setSuccess(true);
			j.setMsg("修改商品成功！");
		} catch (Exception e) {
			j.setMsg("修改商品失败！");
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	
	/**
	 * 删除商品
	 */
	public void delete(){
		Json j = new Json();
		User u = (User)session.get("user");
		sp.setUserId(u.getId());
		try{
			spService.delete(sp);
			j.setSuccess(true);
			j.setMsg("删除商品成功!");
		}catch(Exception e){
			j.setMsg("删除商品失败!");
			e.printStackTrace();
		}
		super.writeJson(j);
	}

	/**
	 * 导出商品代码到金穗
	 */
	public void exportToJs(){
		Export.toJs(spService.exportToJs(sp));
	}
		
	/**
	 * 维护专属信息
	 */
	public void editSpDet(){
		Json j = new Json();
		User u = (User)session.get("user");
		sp.setUserId(u.getId());
		try{
			spService.editSpDet(sp);
			j.setSuccess(true);
			j.setMsg("商品专属信息维护成功!");
		}catch(Exception e){
			j.setMsg("商品专属信息维护失败!");
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	
	/**
	 * 删除商品专属信息
	 */
	public void deleteSpDet(){
		Json j = new Json();
		User u = (User)session.get("user");
		sp.setUserId(u.getId());
		try{
			spService.deleteSpDet(sp);
			j.setSuccess(true);
			j.setMsg("删除商品专属信息成功!");
		}catch(Exception e){
			j.setMsg("删除商品专属信息失败!");
			e.printStackTrace();
		}
		super.writeJson(j);
	}

	/**
	 * 增加商品时，检验商品编码是否已存在
	 */
	public void existSp(){
		Json j = new Json();
		if(spService.existSp(sp)){
			j.setMsg("商品编号已存在!");
		}else{
			j.setSuccess(true);
		}
		super.writeJson(j);
	}
	
	public void loadSp(){
		Json j = new Json();
		try{
			Sp s = spService.loadSp(sp.getSpbh(), sp.getDepId());
			if(s != null){
				j.setSuccess(true);
				j.setObj(s);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	
	/**
	 * 获得商品选择表单
	 */
	public void spDg(){
		writeJson(spService.spDg(sp));
	}
	
	/**
	 * 返回商品信息，供管理用，有分页
	 */
	public void datagrid(){
		writeJson(spService.datagrid(sp));
	}
	
	/**
	 * 返回商品保管员信息
	 */
	public void datagridBgy(){
		User u = (User)session.get("user");
		if(Constant.IS_BGY.equals(u.getIsBgy())){
			sp.setBgyId(u.getId());
		}
		writeJson(spService.datagridBgy(sp));
	}
	
	public void getSpKc(){
		writeJson(spService.getSpkc(sp));
	}

	public void getSpsByLb() {
		writeJson(spService.getSpsByLb(sp));
	}

	public void searchSps() {
	    writeJson(spService.searchSps(sp));
    }
	
	@Override
	public Sp getModel() {
		return sp;
	}
	
	@Autowired
	public void setSpService(SpServiceI spService) {
		this.spService = spService;
	}
}
