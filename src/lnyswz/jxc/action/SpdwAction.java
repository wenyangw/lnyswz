package lnyswz.jxc.action;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Spdw;
import lnyswz.jxc.service.SpdwServiceI;
/**
 * 商品段位Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("spdwAction")
public class SpdwAction extends BaseAction implements ModelDriven<Spdw>{
	private static final long serialVersionUID = 1L;
	private Spdw spdw = new Spdw();
	private SpdwServiceI spdwService;
	
	/**
	 * 添加商品段位
	 */
	public void add(){
		Json j = new Json();
		try{
			//将获得的前台内容传入Service
			Spdw s = spdwService.add(spdw);
			//添加成功
			j.setSuccess(true);
			j.setMsg("增加商品段位成功！");
			j.setObj(s);
		}catch(Exception e){
			j.setMsg("增加商品段位失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改商品段位
	 */
	public void edit(){
		Json j = new Json();
		try {
			//将获得的前台内容传入Service
			spdwService.edit(spdw);
			j.setSuccess(true);
			j.setMsg("修改商品段位成功！");
		} catch (Exception e) {
			j.setMsg("修改商品段位失败！");
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	
	/**
	 * 删除商品段位
	 */
	public void delete(){
		Json j = new Json();
		try{
			spdwService.delete(spdw.getId());
			j.setSuccess(true);
			j.setMsg("删除商品段位成功!");
		}catch(Exception e){
			j.setMsg("删除商品段位失败!");
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	
	/**
	 * 商品管理界面，商品段位列表，导航使用
	 */
	public void getSpdws(){
		writeJson(spdwService.getSpdws());
	}
	
	/**
	 * 获得商品大类、类别树，供选择使用
	 */
	public void listSpfl(){
		writeJson(spdwService.listSpfl(spdw));
	}
	
	/**
	 * 返回所有功能按钮，供管理用，有分页
	 */
	public void datagrid(){
		writeJson(spdwService.datagrid(spdw));
	}
	
	@Override
	public Spdw getModel() {
		return spdw;
	}
	
	@Autowired
	public void setSpdwService(SpdwServiceI spdwService) {
		this.spdwService = spdwService;
	}
}
