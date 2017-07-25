package lnyswz.jxc.action;

import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Message;
import lnyswz.jxc.bean.Paper;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.PaperServiceI;
import lnyswz.jxc.util.Upload;
import lnyswz.jxc.util.Util;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Namespace("/oa")
@Action("paperAction")
public class PaperAction extends BaseAction implements ModelDriven<Paper> {
	private static final long serialVersionUID = 1L;
	private Paper paper = new Paper();
	private PaperServiceI paperService;
	private static final String CONTENTTYPE = "application/octet-stream";

	public void downloadFile(){
		//获取网站部署路径(通过ServletContext对象)，用于确定下载文件位置，从而实现下载
		String path = Util.getRootPath();
		HttpServletResponse response = ServletActionContext.getResponse();
		//1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
		response.setContentType("multipart/form-data");
		//2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)
		response.setHeader("Content-Disposition", "attachment;fileName="+paper.getFilename());
		ServletOutputStream out;
		//通过文件路径获得File对象(假如此路径中有一个download.pdf文件)
		File file = new File(path + paper.getFilepath());

		try {
			FileInputStream inputStream = new FileInputStream(file);

			//3.通过response获取ServletOutputStream对象(out)
			out = response.getOutputStream();

			int b = 0;
			byte[] buffer = new byte[512];
			while (b != -1){
				b = inputStream.read(buffer);
				//4.写到输出流(out)中
				out.write(buffer,0,b);
			}
			inputStream.close();
			out.close();
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteFile(){
		Upload.deleteFile(paper.getFilepath());
	}

	public void getPapers(){
		Json j = new Json();
		j.setObj(paperService.getPapers(paper));
		writeJson(j);
	}
	
	public Paper getModel() {
		return paper;
	}

	@Autowired
	public void setPaperService(PaperServiceI paperService) {
		this.paperService = paperService;
	}
}