package lnyswz.ws.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.ws.Endpoint;

import lnyswz.ws.WebServiceImpl;


/**
 * @author wenyang
 * 用于发布WebService的监听器
 */
public class WebServicePublishListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		//WebService的发布地址ַ
		String address = "http://192.168.0.2:829/lnyswz/lg2pr";
		//发布WebService，WebServiceImpl类是WebServie接口的具体实现类
		Endpoint.publish(address , new WebServiceImpl());
		System.out.println("使用WebServicePublishListener发布webservice成功!");
	}

}
