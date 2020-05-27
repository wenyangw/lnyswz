package lnyswz.jxc.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jws.WebMethod;
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

import org.springframework.beans.BeanUtils;
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
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Fyd;
import lnyswz.jxc.bean.FydDet;
import lnyswz.jxc.model.TFyd;
import lnyswz.jxc.model.TFydDet;
import lnyswz.jxc.model.TLsh;
import lnyswz.jxc.service.WebServiceI;
import lnyswz.jxc.util.Constant;
import lnyswz.jxc.util.Export;

/**
 * @author wenyang
 * SEI的具体实现
 */
//使用@WebService注解标注WebServiceI接口的实现类WebServiceImpl
@WebService (endpointInterface = "lnyswz.jxc.service.WebServiceI")
@Service
public class WebServiceImpl extends SpringBeanAutowiringSupport implements WebServiceI {

	private BaseDaoI<TFyd> fydDao;
	private BaseDaoI<TLsh> lshDao;
	
	private Map<String, Object> headMap;
	private List<Map<String, Object>> details;
	
	@Override
	@WebMethod
	public String lg2pr(@WebParam(name="xml")String xml) {
		headMap = new HashMap<String, Object>();
		details = new ArrayList<Map<String, Object>>();
		
		//System.out.println(xml);
			
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
			
			//获取每个节点的内容，并保存到maps中
			getNodes(document.getChildNodes());
			
			return verify(document);
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
			System.out.println(e.getMessage());
			System.out.println(e.getMessage());
			return result("", "1", e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println("ParserConfigurationException");
			System.out.println(e.getMessage());
			System.out.println(e.getMessage());
			return result("", "1", e.getMessage());
		} catch (SAXParseException e) {
			System.out.println("SAXParseException");
			System.out.println(e.getMessage());
			return result("", "1", e.getMessage());
		} catch (SAXException e) {
			System.out.println("SAXException");
			System.out.println(e.getMessage());
			return result("", "1", "文件读取错误！");
		} catch (IOException e) {
			System.out.println("IOException");
			System.out.println(e.getMessage());
			System.out.println(e.getMessage());
			
		}
		return result("", "1", "失败");
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
				getNodes(node.getChildNodes());
			}else if(node.getNodeName().equals("deal")){
				getNodes(node.getChildNodes());
			}else if(node.getNodeName().equals("Details")){
				getDetail(node.getChildNodes());
			}else{
				getField(headMap, node);
			}
		}
	}
	
	/**
	 * 根据传入节点列表（只有子节点），循环获取节点内容
	 * @param lists
	 */
	private void getDetail(NodeList lists){
		Map<String, Object> detailMap = null;
		for (int i = 0; i < lists.getLength(); i++) {
			detailMap = new HashMap<String, Object>();
			NodeList ls = lists.item(i).getChildNodes();
			for(int j = 0; j < ls.getLength(); j++){
				getField(detailMap, ls.item(j));
			}
			details.add(detailMap);
		}
	}
	
	/**
	 * 根据传入node获取节点内容
	 * @param node
	 */
	private void getField(Map<String, Object> map, Node node){
		map.put(node.getNodeName(), node.getTextContent());
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception 
	 */
	private String verify(Document document){
		if(headMap.isEmpty() || details.isEmpty()){
			return result("", "1", "数据解析失败");
		}else{
			/*for(int i = 0; i < fields.length; i++){
				if(!maps.containsKey(fields[i])){
					return result("", "1", "没有包括" + fields[i]);
				}
			}*/
		}
		
		Fyd fyd = new Fyd();
		TFyd tFyd = new TFyd();
		
		FydDet fydDet = null;
		TFydDet tFydDet = null;
		
		convertMap2PO(headMap, fyd);
		BeanUtils.copyProperties(fyd, tFyd);
		
		String lsh = LshServiceImpl.updateLsh(Constant.BM_CB, Constant.YWLX_FYD, lshDao);
		tFyd.setFydlsh(lsh);
		tFyd.setBmbh(Constant.BM_CB);
		tFyd.setCreateTime(new Date());
		
		tFyd.setStatus(getFydStatus(fyd.getTzdbh()));
		tFyd.setSended("0");
		
		Set<TFydDet> tDets = new HashSet<TFydDet>();
		for(Map<String, Object> m : details){
			fydDet = new FydDet();
			tFydDet = new TFydDet();
			
			convertMap2PO(m, fydDet);
			BeanUtils.copyProperties(fydDet, tFydDet);
			
			tFydDet.setTFyd(tFyd);
			tDets.add(tFydDet);
		}
		
		tFyd.setTFydDets(tDets);
		
		fydDao.save(tFyd);
		Export.saveFile(document, Export.getRootPath() + "/xml/tzd_" + fyd.getTzdbh() + "_" + DateUtil.dateToStringWithTime(new Date(),"yyyyMMddHHmmss") + ".xml");
		
		return result("", "0", "");
	}
	
	private String getFydStatus(String tzdbh){
		String sql = "select fydlsh from t_fyd where tzdbh = ?";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", tzdbh);
		List<Object[]> lists = fydDao.findBySQL(sql, params);
		if(lists == null){
			return "0";
		}else{
			String updateSql = "update t_fyd set status = '2' where tzdbh = ? and (status = '0' or status = '1')";
			Map<String, Object> updateParams = new HashMap<String, Object>();
			updateParams.put("0", tzdbh);
			fydDao.updateBySQL(updateSql, updateParams);
			return "1";
		}
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
							field.set(o, convert(v, field.getType()));
							
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
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
	
	private static Object convert(Object object, Class<?> type) {
    	if (type.equals(int.class) || type.equals(Integer.class)) {
            return Integer.parseInt(object.toString());
        }
        if (type.equals(BigDecimal.class)) {
            return new BigDecimal(object.toString());
        }
        if (type.equals(Date.class)) {
            return DateUtil.stringToDate(object.toString()); 
        }
	    return object;
	}
	

	@Autowired
	public void setFydDao(BaseDaoI<TFyd> fydDao) {
		this.fydDao = fydDao;
	}

	@Autowired
	public void setLshDao(BaseDaoI<TLsh> lshDao) {
		this.lshDao = lshDao;
	}
}
