package lnyswz.jxc.action;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

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
import lnyswz.jxc.bean.Fyd;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.FydServiceI;
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
		User user = (User)session.get("user");
		//xsth.setCreateId(user.getId());
		//xsth.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			fydService.updateXsdj(fyd);
			//添加成功
			j.setSuccess(true);
			j.setMsg("修改付印单单价成功！");
		}catch(Exception e){
			j.setMsg("修改付印单单价失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	public void sendFyd() {
		// 创建一个用于产生WebServiceImpl实例的工厂，WebServiceImplService类是wsimport工具生成的
		// WebServiceImplService factory = new WebServiceImplService();
		Pr2LgService factory = new Pr2LgService();
		// 通过工厂生成一个WebServiceImpl实例，WebServiceImpl是wsimport工具生成的
		// WebServiceImpl wsImpl = factory.getWebServiceImplPort();
		String result = factory.getPr2LgCfc().wsCargo(fydService.sendFyd(fyd));
		// 调用WebService的sayHello方法
		// String resResult = wsImpl.sayHello("孤傲苍狼");
		System.out.println(result);
		
		int pos = result.indexOf("<result code=\"0\">");
		
		Json j = new Json();
		if(pos > 0){
			j.setSuccess(true);
			j.setMsg("上传数据成功！");
		}else{
			j.setMsg("上传数据失败！");
		}
		
		writeJson(j);
		//Document doc = parserXml(result);
		
		
		
		//Node node = getNode(doc.getChildNodes(), "result");
		
		//System.out.println("---" + node.getNodeName());

	}
	
	/**
	 * 根据传递的字符串生成对应的xml。
	 * 
	 * @param 传递过来的参数name
	 * @return 返回xml的字符串
	 */
	private static String createXml() {

		StringBuffer sb = new StringBuffer();

		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<root>");
		sb.append("<head>");
		sb.append("<publisher>test</publisher>");
		sb.append("<publishercn>测试出版社</publishercn>");
		sb.append("<checkCode>XMLtest</checkCode>");
		sb.append("</head>");
		sb.append("<deal id='16041514580031492' type='cbyztz' operation='0'>");
		sb.append("<tzdbh>2016001</tzdbh>");
		sb.append("<cbsydsno>1</cbsydsno>");
		sb.append("<bsno>12311</bsno>");
		sb.append("<bname>宋词三百首</bname>");
		sb.append("<yc>1-1</yc>");
		sb.append("<Details count='2'>");
		sb.append("<Detail>");
		sb.append("<tzdbh>2016001</tzdbh>");
		sb.append("<cbsydsno>1</cbsydsno>");
		sb.append("<sno>1</sno>");
		sb.append("<xmdm>ZW</xmdm>");
		sb.append("<xmmc>正文</xmmc>");
		sb.append("<cldm>A111</cldm>");
		sb.append("<zzmc>70克胶版纸</zzmc>");
		sb.append("<zzgg>787*1092</zzgg>");
		sb.append("<danjia>2.5</danjia>");
		sb.append("<gongj>100</gongj>");
		sb.append("</Detail>");
		sb.append("<Detail>");
		sb.append("<tzdbh>2016001</tzdbh>");
		sb.append("<cbsydsno>1</cbsydsno>");
		sb.append("<sno>2</sno>");
		sb.append("<xmdm>FM</xmdm>");
		sb.append("<xmmc>封面</xmmc>");
		sb.append("<cldm>A111</cldm>");
		sb.append("<zzmc>157克铜版纸</zzmc>");
		sb.append("<danjia>2.6</danjia>");
		sb.append("<gongj>120</gongj>");
		sb.append("</Detail>");
		sb.append("</Details>");
		sb.append("</deal>");
		sb.append("</root>");

		return sb.toString();
	}
	
	private Node getNode(NodeList lists, String nodeName){
		
		/*for (int i = 0; i < lists.getLength(); i++) {
			Node node = lists.item(i);
			System.out.println(node.getNodeName());
			if(node.hasChildNodes()){
				getNode(node.getChildNodes(), nodeName);
			}else{
				if(node.getNodeName().equals(nodeName)){
					return node;
				}else{
					continue;
				}
			}
		}
		return null;*/
		for (int i = 0; i < lists.getLength(); i++) {
			Node node = lists.item(i);
			System.out.println(node.getNodeName());
			if(node.getNodeName().equals("root")){
				System.out.println("root--");
				getNode(node.getChildNodes(), nodeName);
			}else if(node.getNodeName().equals("head")){
				System.out.println("head--");
				getNode(node.getChildNodes(), nodeName);
			}else if(node.getNodeName().equals("deals")){
				System.out.println("deals--");
				getNode(node.getChildNodes(), nodeName);
			}else if(node.getNodeName().equals("deal")){
				System.out.println("deal--");
				getNode(node.getChildNodes(), nodeName);
			}else{
				System.out.println(node.getNodeName() + "++");
				if(node.getNodeName().equals(nodeName)){
					System.out.println(node.getNodeName() + "**");
					return node;
				}else{
					System.out.println(node.getNodeName() + "%%");
					continue;
				}
			}
		}
		return null;
	}
	
	/**
	 * 解析xml字符串
	 * 
	 * @param str传递过来的xml字符串
	 */
	private Document parserXml(String str) {
		try {
			StringReader read = new StringReader(str);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(source);
			
			return document;
			//获取每个节点的内容，并保存到maps中
			//getNodes(document.getChildNodes());
			
			//return verify(document);
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
			System.out.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println("ParserConfigurationException");
			System.out.println(e.getMessage());
		} catch (SAXParseException e) {
			System.out.println("SAXParseException");
			System.out.println(e.getMessage());
			//return result("", "1", e.getMessage());
		} catch (SAXException e) {
			System.out.println("SAXException");
			System.out.println(e.getMessage());
			//return result("", "1", "文件读取错误！");
		} catch (IOException e) {
			System.out.println("IOException");
			System.out.println(e.getMessage());
		}
		return null;
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
