package lnyswz.jxc.service;

import com.alibaba.fastjson.JSONObject;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Khdd;
import lnyswz.jxc.bean.KhddDet;

import java.util.List;

public interface KhddServiceI {
	Khdd saveKhdd(Khdd khdd);
	Khdd cancelKhdd(Khdd khdd);
	Khdd refuseKhdd(Khdd khdd);
	Khdd handleKhdd(Khdd khdd);
	Khdd getKhdd(Khdd khdd);
	DataGrid getKhdds(Khdd khdd);
	DataGrid getKhddsByYwy(Khdd khdd);
	List<KhddDet> getKhddDet(Khdd khdd);
}
