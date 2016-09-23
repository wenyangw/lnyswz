package lnyswz.common.applet;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Date;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import lnyswz.common.util.DateUtil;
import lnyswz.jxc.util.Constant;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.view.JasperViewer;

public class ExportApplet extends javax.swing.JApplet {
	/**    
     *    
     */
	private URL url = null;
	private String type;
	private String fileName;
	private JasperPrint jasperPrint = null;

	/** Creates new form AppletViewer */
	public ExportApplet() {

	}

	/**    
    *    
    */
	public void init() {
		String strUrl = getParameter("REPORT_URL");
		type = getParameter("EXPORT_TYPE");
		fileName = getParameter("EXPORT_FILENAME");
		try {
			url = new URL(strUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void start() {
		if (url != null) {
			if (jasperPrint == null) {
				try {
					jasperPrint = (JasperPrint)JRLoader.loadObject(url);
				} catch (Exception e) {
					StringWriter swriter = new StringWriter();
					PrintWriter pwriter = new PrintWriter(swriter);
					e.printStackTrace(pwriter);
					JOptionPane.showMessageDialog(this, swriter.toString());
				}
			}

			if (jasperPrint != null) {
				final JasperPrint print = jasperPrint;
				
				try {
					//FileWriter fw = null;
		        	File dir = new File(Constant.JS_FILEPATH);
		        	if(dir.exists() == false){
		        		dir.mkdirs();
		        	}
		        	File outputFile = new File(Constant.JS_FILEPATH + fileName + "_" + DateUtil.dateToStringWithTime(new Date(),"yyyyMMddHHmmss") + ".pdf");
//		        	if(outputFile.exists()){
//		        		fw = new FileWriter(outputFile, true);
//		        	}else{
//		        		fw = new FileWriter(outputFile);
//		        	}
					
					JRPdfExporter exporter = new JRPdfExporter();  
			        exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT, jasperPrint);  
			        exporter.setParameter(JRPdfExporterParameter.CHARACTER_ENCODING, "UTF-8");  
			        //exporter.setParameter(JRPdfExporterParameter.OUTPUT_FILE_NAME, getRootPath() + "/pdf/" + pdfName);
			        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputFile);
	                //注意此处用的不是JRPdfExporterParameter.OUTPUT_FILE，要用这个，还需新建File  
			        exporter.exportReport();
	             } catch (Exception e) {  
	                 e.printStackTrace();  
	             }
			} else {
				JOptionPane.showMessageDialog(this, "Empty report.");
			}
		} else {
			JOptionPane.showMessageDialog(this,
					"start():Source URL not specified");
		}

	}

}