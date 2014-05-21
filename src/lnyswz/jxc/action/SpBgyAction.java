package lnyswz.jxc.action;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Sp;
import lnyswz.jxc.bean.SpBgy;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.SpBgyServiceI;
import lnyswz.jxc.service.SpServiceI;
import lnyswz.jxc.util.Constant;
/**
 * 商品Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("spBgyAction")
public class SpBgyAction extends BaseAction implements ModelDriven<SpBgy>{
	private Logger logger = Logger.getLogger(SpBgyAction.class);
	private SpBgy spBgy = new SpBgy();
	private SpBgyServiceI spBgyService;
	
	/**
	 * 设置商品保管员信息
	 */
	public void saveSpBgy(){
		Json j = new Json();
		User u = (User)session.get("user");
		if(Constant.IS_BGY.equals(u.getIsBgy())){
			spBgy.setBgyId(u.getId());
			spBgy.setBgyName(u.getRealName());
			try{
				spBgyService.saveSpBgy(spBgy);
				j.setSuccess(true);
				j.setMsg("设置商品关联成功！");
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			j.setMsg("您不是保管员，无法进行此项操作！");
		}
		
		writeJson(j);
	}
		
	/**
	 * 删除商品
	 */
	public void deleteSpBgy(){
		Json j = new Json();
		User u = (User)session.get("user");
		spBgy.setBgyId(u.getId());
		try{
			spBgyService.deleteSpBgy(spBgy);
			j.setSuccess(true);
			j.setMsg("删除保管员商品关联成功!");
		}catch(Exception e){
			j.setMsg("删除保管员商品关联失败!");
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	
	public void datagridBgy(){
		Json j = new Json();
		User u = (User)session.get("user");
		if(Constant.IS_BGY.equals(u.getIsBgy())){
			spBgy.setBgyId(u.getId());
			writeJson(spBgyService.datagridBgy(spBgy));
		}else{
			j.setMsg("您不是保管员，无法进行此项操作！");
		}
	}
	
	
	@Override
	public SpBgy getModel() {
		return spBgy;
	}
	
	@Autowired
	public void setSpBgyService(SpBgyServiceI spBgyService) {
		this.spBgyService = spBgyService;
	}
}
