package lnyswz.jxc.action;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Dict;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.DictServiceI;

@Namespace("/admin")
@Action("dictAction")
public class DictAction extends BaseAction implements ModelDriven<Dict> {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(DictAction.class);
	private Dict dict = new Dict();
	private DictServiceI dictService;

	/**
	 * 添加字典
	 */
	public void add() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			dict.setUserId(u.getId());
			// 将获得的前台内容传入Service
			Dict d = dictService.add(dict);
			j.setSuccess(true);
			j.setMsg("增加字典成功！");
			j.setObj(d);
		} catch (Exception e) {
			j.setMsg("增加字典失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改字典
	 */
	public void edit() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			dict.setUserId(u.getId());
			// 将获得的前台内容传入Service
			dictService.edit(dict);
			j.setSuccess(true);
			j.setMsg("修改字典成功！");
		} catch (Exception e) {
			j.setMsg("修改字典失败！");
			e.printStackTrace();
		}
		super.writeJson(j);
	}

	/**
	 * 删除字典
	 */
	public void delete() {
		Json j = new Json();
		try {
			User u = (User) session.get("user");
			dict.setUserId(u.getId());
			dictService.delete(dict);
			j.setSuccess(true);
			j.setMsg("删除字典成功！");
		} catch (Exception e) {
			j.setMsg("删除字典失败！");
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	

	/**
	 * 返回所有字典，供管理用，有分页
	 */
	public void datagrid() {
		writeJson(dictService.datagrid(dict));
	}
	/**
	 * 判断是否需要部门
	 */
	public void isNeedDep() {
		writeJson(dictService.isNeedDep(dict));
	}
	public void listDict() {
		writeJson(dictService.listDict(dict));
	}
	public void selectTree() {
		writeJson(dictService.selectTree(dict));
	}
	public void listFields() {
		logger.info(dict.getSelectType());
		writeJson(dictService.listFields(dict));
	}

	@Override
	public Dict getModel() {
		return dict;
	}

	@Autowired
	public void setDictService(DictServiceI dictService) {
		this.dictService = dictService;
	}
}
