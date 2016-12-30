package lnyswz.jxc.util;


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
}
