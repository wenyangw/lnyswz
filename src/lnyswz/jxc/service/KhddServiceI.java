package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Khdd;
import lnyswz.jxc.bean.KhddDet;

import java.util.List;

public interface KhddServiceI {
	Khdd saveKhdd(Khdd khdd);
	Khdd cancelKhdd(Khdd khdd);
	void refuseKhdd(Khdd khdd);
	Khdd getKhdd(Khdd khdd);
	DataGrid getKhdds(Khdd khdd);
	DataGrid getKhddsByYwy(Khdd khdd);
	List<KhddDet> getKhddDet(Khdd khdd);
}
