package lnyswz.jxc.action;

import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.jxc.bean.Show;
import lnyswz.jxc.service.ShowServiceI;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 展示Action
 * @author 王文阳
 *
 */
@Namespace("/jxc")
@Action("showAction")
public class ShowAction extends BaseAction implements
		ModelDriven<Show> {
	private static final long serialVersionUID = 1L;
	private Show show = new Show();
	private ShowServiceI showService;

	@Override
	public Show getModel() {
		return show;
	}

	@Autowired
	public void setShowService(ShowServiceI showService) {
		this.showService = showService;
	}
}
