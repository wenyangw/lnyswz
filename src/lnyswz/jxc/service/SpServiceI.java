package lnyswz.jxc.service;


import com.alibaba.fastjson.JSONObject;
import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Sp;

import java.util.List;

public interface SpServiceI {

	Sp add(Sp sp);
	void edit(Sp sp);
	void delete(Sp sp);
	List<String> exportToJs(Sp sp);
	DataGrid datagrid(Sp sp);
	boolean existSp(Sp sp);
	void editSpDet(Sp sp);
	void deleteSpDet(Sp sp);
	Sp loadSp(String spbh, String depId);
	DataGrid spDg(Sp sp);
	DataGrid getSpkc(Sp sp);
	DataGrid datagridBgy(Sp sp);
	DataGrid getSpsByLb(Sp sp);
    DataGrid searchSps(Sp sp);

}
