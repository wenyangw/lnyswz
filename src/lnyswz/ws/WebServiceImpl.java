package lnyswz.ws;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.dao.impl.BaseDaoImpl;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.service.DepartmentServiceI;
import lnyswz.jxc.service.impl.DepartmentServiceImpl;

/**
 * @author wenyang
 * SEI的具体实现
 */
//使用@WebService注解标注WebServiceI接口的实现类WebServiceImpl
@WebService
public class WebServiceImpl implements WebServiceI {
	
	@Override
	public String lg2pr(@WebParam(name="xml")String xml) {
		BaseDaoI<TDepartment> depDao = new BaseDaoImpl<TDepartment>(); 
		//DepartmentServiceI depI = new DepartmentServiceImpl();
		System.out.println(DepartmentServiceImpl.getDepName("05", depDao));
		
		return parserXml(xml);
	}
	
	/**
	 * 解析xml字符串
	 * 
	 * @param str传递过来的xml字符串
	 */
	private static String parserXml(String str) {
		System.out.println(str);
		try {
			StringReader read = new StringReader(str);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(source);

			NodeList docs = document.getChildNodes();
			System.out.println("docs:" + docs);
			for (int i = 0; i < docs.getLength(); i++) {
				Node employee = docs.item(i);
				System.out.println("docs.item(" + i + "):" + employee);
				NodeList employeeInfo = employee.getChildNodes();
				System.out.println("employee.getChildNodes():" + employeeInfo);
				for (int j = 0; j < employeeInfo.getLength(); j++) {
					Node node = employeeInfo.item(j);
					System.out.println("employeeInfo.item(" + j + "):" + node);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						System.out.println("node.getTextContent():" + node.getTextContent());
						if ("cinamaName".equals(node.getNodeName())) {
							System.out.println("��Ӱ����:" + node.getTextContent());
						} else if ("introduce".equals(node.getNodeName())) {
							System.out.println("��Ӱ���:" + node.getTextContent());
						} else if ("price".equals(node.getNodeName())) {
							System.out.println("��Ӱ�۸�:" + node.getTextContent());
						} else if ("director".equals(node.getNodeName())) {
							System.out.println("����:" + node.getTextContent());
						}
					}
				}
			}
			return result("", "0", "�ɹ�");
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
			System.out.println(e.getMessage());
			System.out.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println("ParserConfigurationException");
			System.out.println(e.getMessage());
			System.out.println(e.getMessage());
		} catch (SAXParseException e) {
			System.out.println("SAXParseException");
			System.out.println(e.getMessage());
			return result("", "1", e.getMessage());
		} catch (SAXException e) {
			System.out.println("SAXException");
			System.out.println(e.getMessage());
			return result("", "1", "�ļ���ȡ����");
		} catch (IOException e) {
			System.out.println("IOException");
			System.out.println(e.getMessage());
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	private static String result(String id, String code, String excep){
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<root>");
		sb.append("<head>");
		sb.append("<publisher>test</publisher>");
		sb.append("</head>");
		sb.append("<deals id='" + id + "' type='cbyztz' operation='0'>");
		sb.append("<deal>");
		sb.append("<result code='" + code + "'>" + (code.equals("1") ? "ʧ��" : "�ɹ�") + "</result>");
		if(code.equals("1")){
			sb.append("<exception>" + excep + "</exception>");
		}
		sb.append("</deal>");
		sb.append("</deals>");
		sb.append("</root>");
		return sb.toString();
	}
	
	/*访问远程(WebService)xml数据后返回的xml格式字符串并生成为本地文件*/
	public static void saveFile(Document document, String savaFileURL){
		TransformerFactory transF = TransformerFactory.newInstance();
		try{
			Transformer transformer = transF.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "YES");
			PrintWriter pw = new PrintWriter(new FileOutputStream(savaFileURL));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
			System.out.println("生成xml文件成功!");
		}catch(TransformerConfigurationException e){
			System.out.println(e.getMessage());
		}catch(IllegalArgumentException e){
			System.out.println(e.getMessage());
		}catch(FileNotFoundException e){
			System.out.println(e.getMessage());
		}catch(TransformerException e){
			System.out.println(e.getMessage());
		}
	}

}
