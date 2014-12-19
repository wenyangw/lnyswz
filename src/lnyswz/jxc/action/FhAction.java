package lnyswz.jxc.action;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Fh;
import lnyswz.jxc.service.FhServiceI;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/jxc")
@Action("fhAction")
public class FhAction extends BaseAction implements ModelDriven<Fh> {
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(LoginAction.class);
	private Fh fh = new Fh();
	private FhServiceI fhService;

	/**
	 * 增加分库
	 */
	public void add() {
		Fh r = fhService.add(fh);
		System.out.println("进入");
		Json j = new Json();
		try {
			j.setSuccess(true);
			j.setMsg("增加分户成功");
			j.setObj(r);
		} catch (Exception e) {
			j.setMsg("增加分户失败");
			e.printStackTrace();

		}

		writeJson(j);
	}

	/**
	 * 修改分库
	 */
	public void edit() {
		Json j = new Json();

		try {
			fhService.edit(fh);
			j.setSuccess(true);
			j.setMsg("编辑分户成功！");
		} catch (Exception e) {
			j.setMsg(e.getStackTrace().toString());
		}
		writeJson(j);
	}

	/**
	 * 删除仓库信息
	 */
	public void delete() {
		Json j = new Json();
		try {
			fhService.delete(fh);
			j.setSuccess(true);
			j.setMsg("删除分户成功");
		} catch(Exception e){
			j.setMsg(e.getStackTrace().toString());
		}
		writeJson(j);

	}

	public void datagrid() {

		super.writeJson(fhService.datagrid(fh));
	}

	public void listFhs() {
		writeJson(fhService.listFhs(fh));
	}

	public Fh getModel() {
		return fh;
	}

	@Autowired
	public void setFhService(FhServiceI fhService) {
		this.fhService = fhService;
	}
}