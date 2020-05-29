package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Khdd;

public interface KhddServiceI {
	Khdd saveKhdd(Khdd khdd);
	Khdd cancelKhdd(Khdd khdd);
	void refuseKhdd(Khdd khdd);
	Khdd getKhdd(Khdd khdd);
	DataGrid getKhdds(Khdd khdd);
	DataGrid getKhddDet(Khdd khdd);
}
