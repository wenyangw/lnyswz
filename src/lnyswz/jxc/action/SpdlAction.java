package lnyswz.jxc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Spdl;
import lnyswz.jxc.service.SpdlServiceI;
/**
 * 商品大类Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("spdlAction")
public class SpdlAction extends BaseAction implements ModelDriven<Spdl>{
	private static final long serialVersionUID = 1L;
	private Spdl spdl = new Spdl();
	private SpdlServiceI spdlService;
	
	/**
	 * 添加商品大类
	 */
	public void add(){
		Json j = new Json();
		try{
			//将获得的前台内容传入Service
			Spdl s = spdlService.add(spdl);
			//添加成功
			j.setSuccess(true);
			j.setMsg("增加商品大类成功！");
			j.setObj(s);
		}catch(Exception e){
			j.setMsg("增加商品大类失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改商品大类
	 */
	public void edit(){
		Json j = new Json();
		try {
			//将获得的前台内容传入Service
			spdlService.edit(spdl);
			j.setSuccess(true);
			j.setMsg("修改商品大类成功！");
		} catch (Exception e) {
			j.setMsg("修改商品大类失败！");
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	
	/**
	 * 删除商品大类
	 */
	public void delete(){
		Json j = new Json();
		try{
			spdlService.delete(spdl.getId());
			j.setSuccess(true);
			j.setMsg("删除商品大类成功!");
		}catch(Exception e){
			j.setMsg("删除商品大类失败!");
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	
	/**
	 * 返回所有商品大类，供选择用，无分页
	 */
	public void listSpdls(){
		writeJson(spdlService.listSpdls());
	}
	
	/**
	 * 返回所有商品大类，供管理用，有分页
	 */
	public void datagrid(){
		writeJson(spdlService.datagrid(spdl));
	}
	
	@Override
	public Spdl getModel() {
		return spdl;
	}
	
	@Autowired
	public void setSpdlService(SpdlServiceI spdlService) {
		this.spdlService = spdlService;
	}
}
