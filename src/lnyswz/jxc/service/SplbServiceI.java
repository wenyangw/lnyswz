package lnyswz.jxc.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Splb;

public interface SplbServiceI {

	Splb add(Splb splb);

	boolean edit(Splb splb);

	void delete(int id);

	List<Splb> listSplbs(Splb splb);

	DataGrid datagrid(Splb splb);

	List<JSONObject> getSplbsWithSpdl(Splb splb);

}
