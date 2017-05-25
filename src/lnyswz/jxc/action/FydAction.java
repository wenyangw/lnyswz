package lnyswz.jxc.action;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Fyd;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.FydServiceI;
import lnyswz.jxc.util.Export;
import lnyswz.jxc.wllgs.Pr2LgService;;

/**
 * 付印单Action
 * 
 * @author 王文阳
 * 
 */
@Namespace("/jxc")
@Action("fydAction")
public class FydAction extends BaseAction implements ModelDriven<Fyd> {
	private static final long serialVersionUID = 1L;
	private Fyd fyd = new Fyd();
	private FydServiceI fydService;
	
	public void datagrid() {
		writeJson(fydService.datagrid(fyd));
	}

	public void detDatagrid() {
		writeJson(fydService.detDatagrid(fyd.getFydlsh()));
	}
	
	public void updateXsdj() {
		Json j = new Json();
		try{
			fydService.updateXsdj(fyd);
			j.setSuccess(true);
			j.setMsg("修改付印单单价成功！");
		}catch(Exception e){
			j.setMsg("修改付印单单价失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	public void sendFyd() {
		User user = (User)session.get("user");
		// 创建一个用于产生WebServiceImpl实例的工厂，WebServiceImplService类是wsimport工具生成的
		// WebServiceImplService factory = new WebServiceImplService();
		Pr2LgService factory = new Pr2LgService();
		// 通过工厂生成一个WebServiceImpl实例，WebServiceImpl是wsimport工具生成的
		// WebServiceImpl wsImpl = factory.getWebServiceImplPort();
		String result = factory.getPr2LgCfc().wsCargo(fydService.sendFyd(fyd));
		// 调用WebService的sayHello方法
		// String resResult = wsImpl.sayHello("孤傲苍狼");
		
		int pos = result.indexOf("<result code=\"0\">");
		
		Json j = new Json();
		if(pos > 0){
			fyd.setSendId(user.getId());
			fyd.setSendName(user.getRealName());
			fydService.updateFydSended(fyd);
			
			j.setSuccess(true);
			j.setMsg("上传数据成功！");
		}else{
			j.setMsg("上传数据失败！");
		}
		
		writeJson(j);
	}
	
	@Override
	public Fyd getModel() {
		return fyd;
	}

	@Autowired
	public void setFydService(FydServiceI fydService) {
		this.fydService = fydService;
	}

}
