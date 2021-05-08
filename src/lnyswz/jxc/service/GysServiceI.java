package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Gys;

public interface GysServiceI {

	DataGrid datagrid(Gys gys);

	Gys add(Gys gys);

	void edit(Gys gys);

	boolean delete(Gys gys);

	boolean existGys(Gys gys);

	void editDet(Gys gys);

	void deleteDet(Gys gys);

	List<Gys> listGys(Gys gys);

	/**
	 * 王文阳 2013.10.08 获得供应商信息
	 */
	DataGrid gysDg(Gys gys);

	/**
	 * 王文阳 2013.10.08 获得供应商检索信息
	 */
	Gys loadGys(String gysbh);
}
