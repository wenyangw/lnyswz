package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Cgjh;


public interface CgjhServiceI {
	public Cgjh save(Cgjh cgjh);
	public void updateCancel(Cgjh cgjh);
	public void updateComplete(Cgjh cgjh);
	public void updateUnComplete(Cgjh cgjh);
	public void updateIsHt(Cgjh cgjh);
	public void updateHt(Cgjh cgjh);
	public DataGrid datagrid(Cgjh cgjh);
	public DataGrid detDatagrid(String cgjhlsh);
	public DataGrid datagridDet(Cgjh cgjh);
	public DataGrid toKfrk(String cgjhDetIds);
	public DataGrid getSpkc(Cgjh cgjh);
	public DataGrid toYwrk(String cgjhDetIds);
	public DataGrid printCgjh(Cgjh cgjh);
}
