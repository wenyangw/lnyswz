package lnyswz.jxc.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

/**
 * @author wenyang
 * 定义SEI(WebService EndPoint Interface(终端))
 */
//使用@WebService注解标注WebServiceI接口
@WebService
@SOAPBinding(style = Style.DOCUMENT, use=Use.LITERAL)
public interface WebServiceI {
	//使用@WebMethod注解标注WebServiceI接口中的方法
	@WebMethod
	String lg2pr(
			@WebParam(name="xml")
			String xml);
}
