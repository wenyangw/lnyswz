package lnyswz.jxc.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.model.TDepartment;
import lnyswz.jxc.model.TFyd;
import lnyswz.jxc.service.WebServiceI;

/**
 * @author wenyang
 * SEI的具体实现
 */
//使用@WebService注解标注WebServiceI接口的实现类WebServiceImpl
@WebService (endpointInterface = "lnyswz.jxc.service.WebServiceI")
@Service
public class WebServiceImpl extends SpringBeanAutowiringSupport implements WebServiceI {

	private BaseDaoI<TDepartment> depDao;
	
	private Map<String, Object> maps;
	private String[] fields = {"publisher", "publishercn", "checkCode", "tzdbh", "cbsydsno", "bsno", "bname", "yc", "sno", 
			"zzgg", "xmdm", "xmmc", "cldm", "zzmc", "danjia", "gongj"}; 

	@Override
	public String lg2pr(@WebParam(name="xml")String xml) {
		TDepartment tDep = depDao.load(TDepartment.class, "05");
		
		//转换xml，并返回验证结果xml
		return parserXml(xml);
	}
	
	
	/**
	 * 解析xml字符串
	 * 
	 * @param str传递过来的xml字符串
	 */
	private String parserXml(String str) {
		try {
			StringReader read = new StringReader(str);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(source);

			maps = new HashMap<String, Object>();
			//获取每个节点的内容，并保存到maps中
			getNodes(document.getChildNodes());
			
			return verify();
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
			//return result("", "1", e.getMessage());
		} catch (SAXException e) {
			System.out.println("SAXException");
			System.out.println(e.getMessage());
			//return result("", "1", "文件读取错误！");
		} catch (IOException e) {
			System.out.println("IOException");
			System.out.println(e.getMessage());
			System.out.println(e.getMessage());
		}
		return null;
	}	
	
	/**
	 * 根据传入节点列表，对不同节点类型（只包含一层节点：getField(NodeList lists)，包含多层子节点：getNodes(NodeList lists)）执行不同的操作
	 * @param lists
	 */
	private void getNodes(NodeList lists){
		for (int i = 0; i < lists.getLength(); i++) {
			Node node = lists.item(i);
			if(node.getNodeName().equals("root")){
				getNodes(node.getChildNodes());
			}else if(node.getNodeName().equals("head")){
				getField(node.getChildNodes());
			}else if(node.getNodeName().equals("deal")){
				getNodes(node.getChildNodes());
			}else if(node.getNodeName().equals("Details")){
				getNodes(node.getChildNodes());
			}else if(node.getNodeName().equals("Detail")){
				getField(node.getChildNodes());
			}else{
				getField(node);
			}
		}
	}
	
	/**
	 * 根据传入节点列表（只有子节点），循环获取节点内容
	 * @param lists
	 */
	private void getField(NodeList lists){
		for (int i = 0; i < lists.getLength(); i++) {
			getField(lists.item(i));
		}
	}
	
	/**
	 * 根据传入node获取节点内容
	 * @param node
	 */
	private void getField(Node node){
		//System.out.println(node.getNodeName() + ":" + node.getTextContent());
		maps.put(node.getNodeName(), node.getTextContent());
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception 
	 */
	private String verify(){
		if(maps.isEmpty()){
			return result("", "1", "数据解析失败");
		}else{
			for(int i = 0; i < fields.length; i++){
				if(!maps.containsKey(fields[i])){
					return result("", "1", "没有包括" + fields[i]);
				}
			}
		}
		
		TFyd fyd = new TFyd();

		convertMap2PO(maps, fyd);

		return result("", "0", "");
	}
	
	
	/**
	 * 返回结果xml字符串 
	 * @param id deals中的id
	 * @param code 0-成功; 1-失败
	 * @param excep
	 * @return
	 */
	
	private String result(String id, String code, String excep){
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<root>");
		sb.append("<head>");
		sb.append("<publisher>test</publisher>");
		sb.append("</head>");
		sb.append("<deals id='" + id + "' type='cbyztz' operation='0'>");
		sb.append("<deal>");
		sb.append("<result code='" + code + "'>" + (code.equals("1") ? "失败" : "成功") + "</result>");
		if(code.equals("1")){
			sb.append("<exception>" + excep + "</exception>");
		}
		sb.append("</deal>");
		sb.append("</deals>");
		sb.append("</root>");
		return sb.toString();
	}
	
	public Object convertMap2PO(Map<String,Object> map, Object o){  
        if (!map.isEmpty()) {  
            for (String k : map.keySet()) {
                Object v = "";  
                if (!k.isEmpty()) {  
                    v = map.get(k);
                }  
                Field[] fields = null;  
                fields = o.getClass().getDeclaredFields();
                //String clzName = o.getClass().getSimpleName();  
                //log.info("类："+o.getClass().getName());  
                //log.info("***map转"+clzName+"开始****");  
                for (Field field : fields) {  
                    int mod = field.getModifiers();  
                    if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){  
                        continue;  
                    }
                    if (field.getName().toUpperCase().equals(k.toUpperCase())) {  
                        field.setAccessible(true);  
                        try {
							field.set(o, v);
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}  
                        //log.info("key："+k+"value:"+v);  
                    }  
  
                }  
                //log.info("***map转"+clzName+"结束****");  
            }  
        }  
        return o;  
    }  
	
	/**
	 * 访问远程(WebService)xml数据后返回的xml格式字符串并生成为本地文件
	 * 
	 */
	public void saveFile(Document document, String savaFileURL){
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

	@Autowired
	public void setDepDao(BaseDaoI<TDepartment> depDao) {
		this.depDao = depDao;
	}
}
