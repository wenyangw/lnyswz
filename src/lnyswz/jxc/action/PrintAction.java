package lnyswz.jxc.action;


import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Print;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.PrintServiceI;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 打印Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("printAction")
public class PrintAction extends BaseAction implements ModelDriven<Print>{
	private Print print = new Print();
	private PrintServiceI printService;
	
	public void getCounts(){
		Json j = new Json();
		j.setObj(printService.getCounts(print));
		writeJson(j);
	}

	@Override
	public Print getModel() {
		return print;
	}
	
	@Autowired
	public void setPrintService(PrintServiceI printService) {
		this.printService = printService;
	}
}
