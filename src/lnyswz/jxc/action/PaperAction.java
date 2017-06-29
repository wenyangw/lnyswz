package lnyswz.jxc.action;

import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.jxc.bean.Paper;
import lnyswz.jxc.util.Upload;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

@Namespace("/oa")
@Action("paperAction")
public class PaperAction extends BaseAction implements ModelDriven<Paper> {
	private static final long serialVersionUID = 1L;
	private Paper paper = new Paper();

	public void deleteFile(){
		Upload.deleteFile(paper.getFilepath());
	}
	
	public Paper getModel() {
		return paper;
	}
}