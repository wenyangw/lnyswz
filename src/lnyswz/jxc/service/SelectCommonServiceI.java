package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.SelectCommon;

public interface SelectCommonServiceI {
	public DataGrid selectCommonList(SelectCommon selectCommon);
	public DataGrid selectCommonTree(SelectCommon selectCommon);
	public List<Object[]> Exprot(SelectCommon selectCommon);	
	public DataGrid selectCommonByFreeSpell(SelectCommon selectCommon);
}
