package lnyswz.jxc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import lnyswz.common.bean.DataGrid;

public class Export {
	private static final String CONTENTTYPE = "application/octet-stream";

	public static void print(DataGrid dg, String fileName) {
		File fileReport = new java.io.File(getRootPath() + "/report/"
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
	
	public static void export(DataGrid dg, String fileName, String location, String type) {
		File fileReport = new File(getRootPath() + "/report/" + fileName + ".jasper");

		JasperReport jasperReport = null;
		JasperPrint jasperPrint = null;
		OutputStream out;
		try {
			jasperReport = (JasperReport) JRLoader.loadObject(fileReport);
			JRDataSource datasource = new JRBeanCollectionDataSource(
					dg.getRows());
			// 填充报表
			jasperPrint = JasperFillManager.fillReport(jasperReport, (Map<String, Object>) dg.getObj(), datasource);
			out = new FileOutputStream(Export.getRootPath() + location);
			if (null != jasperPrint) {
				JRExporter exporter = getExporter(type);
				
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);  
				exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");  
				//注意此处用的不是JRPdfExporterParameter.OUTPUT_FILE，要用这个，还需新建File  
				//exporter.setParameter(JRPdfExporterParameter.OUTPUT_FILE_NAME, getRootPath() + "/pdf/" + pdfName);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
		        
				exporter.exportReport();
		        
		        out.close();
			}
		} catch (Exception e) {
		}

	}

	private static JRExporter getExporter(String type) {
		JRExporter exporter;
		if("xls".equals(type)){
			exporter = new JRXlsExporter();
		}else if("docx".equals(type)){
			exporter = new JRDocxExporter();
		}else if("rtf".equals(type)){
			exporter = new JRRtfExporter();
		}else{
			exporter = new JRPdfExporter();
		}
		
		return exporter;
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
	
	public static String getExportType(String etype) {
		if(etype != null){ 
			return etype;
		}else{
			return "pdf";
		}
	}
}
