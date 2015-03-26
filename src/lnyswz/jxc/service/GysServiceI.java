package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Gys;

public interface GysServiceI {

	public DataGrid datagrid(Gys gys);

	public Gys add(Gys gys);

	public void edit(Gys gys);

	public boolean delete(Gys gys);

	public boolean existGys(Gys gys);

	public void editDet(Gys gys);

	public void deleteDet(Gys gys);

	public List<Gys> listGys(Gys gys);

	/**
	 * 王文阳 2013.10.08 获得供应商信息
	 */
	public DataGrid gysDg(Gys gys);

	/**
	 * 王文阳 2013.10.08 获得供应商检索信息
	 */
	public Gys loadGys(String gysbh);
}
