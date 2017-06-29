package lnyswz.jxc.util;


import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lnyswz.common.util.Common;

public class Util {
	public static String getReportName(String bmbh, String fileName){
		JSONArray jsonArray = Common.readJson(fileName);
		for(int i = 0; i < jsonArray.size(); i++){
        	JSONObject jsonO = JSON.parseObject(jsonArray.get(i).toString());
        	if(bmbh.equals(jsonO.getString("bmbh"))){
        		return jsonO.getString("report");
        	}
        }
		return null;
	}
	
	public static String getQueryWhere(String input, String[] fields, Map<String, Object> params){
		String str = "";
		if(input.indexOf(" ") > 0 ){
			String[] in = input.split("\\s+");
			for(int i = 0; i < in.length; i++){
				String str1 = ""; 
				for(String s : fields){
					str1 = Common.joinString(s + " like :search" + i, str1, " OR ");
				}
				params.put("search" + i, "%" + in[i] + "%");
				str = Common.joinString("(" + str1 + ")", str, " AND ");
			}
		}else{
			for(String s : fields){
				str = Common.joinString(s + " like :search", str, " OR ");
			}
			params.put("search", "%" + input + "%");
		}
		return str;
	}

	public static String getRootPath() {
		// 因为类名为"Export"，因此" Export.class"一定能找到
		String result = Util.class.getResource("Util.class").toString();
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
}
