package lnyswz.jxc.action;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

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
	
	
	@Override
	public Fyd getModel() {
		return fyd;
	}

	@Autowired
	public void setFydService(FydServiceI fydService) {
		this.fydService = fydService;
	}

}
