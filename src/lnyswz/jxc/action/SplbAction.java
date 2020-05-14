package lnyswz.jxc.action;

import com.alibaba.fastjson.JSONObject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Splb;
import lnyswz.jxc.service.SplbServiceI;

import java.util.List;

/**
 * 商品类别Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("splbAction")
public class SplbAction extends BaseAction implements ModelDriven<Splb>{
	private static final long serialVersionUID = 1L;
	private Splb splb = new Splb();
	private SplbServiceI splbService;
	
	/**
	 * 添加商品类别
	 */
	public void add(){
		Json j = new Json();
		try{
			//将获得的前台内容传入Service
			Splb s = splbService.add(splb);
			//添加成功
			if(s != null){
				j.setSuccess(true);
				j.setMsg("增加商品类别成功！");
				j.setObj(s);
			}else{
				j.setMsg("请检查编号范围是否冲突！");
			}
		}catch(Exception e){
			j.setMsg("增加商品类别失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改商品类别
	 */
	public void edit(){
		Json j = new Json();
		try {
			//将获得的前台内容传入Service
			if(splbService.edit(splb)){
				j.setSuccess(true);
				j.setMsg("修改商品类别成功！");
			}else{
				j.setMsg("请检查编号范围是否冲突！");
			}
		} catch (Exception e) {
			j.setMsg("修改商品类别失败！");
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	
	/**
	 * 删除商品类别
	 */
	public void delete(){
		Json j = new Json();
		try{
			splbService.delete(splb.getId());
			j.setSuccess(true);
			j.setMsg("删除商品类别成功!");
		}catch(Exception e){
			j.setMsg("删除商品类别失败!");
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	
	/**
	 * 获得商品类别列表，供选择用
	 */
	public void getSplbs(){
		writeJson(splbService.listSplbs(splb));
	}
	
	/**
	 * 返回所有商品类别，供管理用，有分页
	 */
	public void datagrid(){
		writeJson(splbService.datagrid(splb));
	}


	/**
	 * 小程序使用
	 * 返回带有商品大类的商品类别列表
	 */
	public void getSplbsWithSpdl() {
	    Json j = new Json();
	    try {
            List<JSONObject> jsonObject = splbService.getSplbsWithSpdl(splb);
            j.setSuccess(true);
            j.setObj(jsonObject);
        } catch (Exception e) {
	        e.printStackTrace();
        }

		writeJson(j);
	}
	
	@Override
	public Splb getModel() {
		return splb;
	}
	
	@Autowired
	public void setSplbService(SplbServiceI splbService) {
		this.splbService = splbService;
	}
}
