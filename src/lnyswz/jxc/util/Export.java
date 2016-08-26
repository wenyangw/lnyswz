package lnyswz.jxc.util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import lnyswz.common.bean.DataGrid;

public class Export {
	private static final String CONTENTTYPE = "application/octet-stream";
	private static final String CONTENTTYPE_PDF = "application/pdf;charset=GBK";

	public static void print(DataGrid dg, String fileName) {
		java.io.File fileReport = new java.io.File(getRootPath() + "/report/"
				+ fileName + ".jasper");

		JasperReport jasperReport = null;
		JasperPrint jasperPrint = null;
		try {
			jasperReport = (JasperReport) JRLoader.loadObject(fileReport);
			JRDataSource datasource = new JRBeanCollectionDataSource(
					dg.getRows());
			// 填充报表
			jasperPrint = JasperFillManager.fillReport(jasperReport,
					(Map<String, Object>) dg.getObj(), datasource);
			if (null != jasperPrint) {
				HttpServletResponse response = ServletActionContext
						.getResponse();
				response.setContentType(CONTENTTYPE);
				ServletOutputStream ouputStream = response.getOutputStream();

				ObjectOutputStream oos = new ObjectOutputStream(ouputStream);
				oos.writeObject(jasperPrint);
				oos.flush();
				oos.close();

				ouputStream.flush();
				ouputStream.close();
			}

		} catch (Exception e) {
		}

	}
	
	public static String getRootPath() {
		// 因为类名为"Export"，因此" Export.class"一定能找到
		String result = Export.class.getResource("Export.class").toString();
		int index = result.indexOf("WEB-INF");
		if (index == -1) {
			index = result.indexOf("bin");
		}
		result = result.substring(0, index);
		if (result.startsWith("jar")) {
			// 当class文件在jar文件中时，返回"jar:file:/F:/ ..."样的路径
			result = result.substring(10);
		} else if (result.startsWith("file")) {
			// 当class文件在class文件中时，返回"file:/F:/ ..."样的路径
			result = result.substring(6);
		}
		if (result.endsWith("/"))
			result = result.substring(0, result.length() - 1);// 不包含最后的"/"
		return result;
	}

	public static void toJs(List<String> lists) {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType(CONTENTTYPE);
			ServletOutputStream ouputStream = response.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(ouputStream);
			oos.writeObject(lists);
			oos.flush();
			oos.close();
		} catch (Exception e) {

		}
	}
}
