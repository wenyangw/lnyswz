package lnyswz.common.applet;

import java.applet.Applet;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.swing.JOptionPane;

import lnyswz.jxc.util.Constant;

public class FileApplet extends Applet {
	
	private URL url = null;
	private String isZzs = "1";
	private String fileName = null;
	
	@Override
    public void init()
    {
        super.init();
        String strUrl = getParameter("DATA_URL");
        isZzs = getParameter("FP_ZZS");
        if("1".equals(isZzs)){
        	fileName = Constant.JS_ZZS_FILENAME;
        }else{
        	fileName = Constant.JS_PT_FILENAME;
        }
        try {
			url = new URL(strUrl);
		} catch (Exception e) {
			StringWriter swriter = new StringWriter();
			PrintWriter pwriter = new PrintWriter(swriter);
			e.printStackTrace(pwriter);
			JOptionPane.showMessageDialog(this, swriter.toString());
		}
    }
	public void start(){
        try
        {
        	boolean isExist = true;
        	FileWriter fw = null;
        	File dir = new File(Constant.JS_FILEPATH);
        	if(dir.exists() == false){
        		dir.mkdirs();
        	}
        	File outputFile = new File(Constant.JS_FILEPATH + fileName);
        	if(outputFile.exists()){
        		fw = new FileWriter(outputFile, true);
        	}else{
        		fw = new FileWriter(outputFile);
        		isExist = false;
        	}
            BufferedWriter bw = new BufferedWriter(fw);
            if(isExist){
            	bw.newLine();
            }
        	URLConnection urlcon = url.openConnection(); 
        	urlcon.connect(); 
        	ObjectInputStream ois=new ObjectInputStream(urlcon.getInputStream()); 
        	List<String> crs = (List<String>)ois.readObject();
        	int i = 0;
        	for(String s : crs){
        		bw.append(s);
        		if(i != crs.size() - 1){
        			bw.newLine();
        		}
        		i++;
        	}
        	bw.flush();    //刷新该流的缓冲
            bw.close();
            fw.close();
        }
        catch (Exception e){}
    }
}
