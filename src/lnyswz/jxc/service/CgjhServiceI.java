package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Cgjh;


public interface CgjhServiceI {
	Cgjh save(Cgjh cgjh);
	void updateCancel(Cgjh cgjh);
	void updateComplete(Cgjh cgjh);
	void updateUnComplete(Cgjh cgjh);
	void updateIsHt(Cgjh cgjh);
	void updateHt(Cgjh cgjh);
	void updateGys(Cgjh cgjh);
	void updateShdz(Cgjh cgjh);
	void updateLockSpInCgjh(Cgjh cgjh);
	void updateBackSpInCgjh(Cgjh cgjh);
	DataGrid datagrid(Cgjh cgjh);
	DataGrid detDatagrid(Cgjh cgjh);
	DataGrid datagridDet(Cgjh cgjh);
	DataGrid toKfrk(String cgjhDetIds);
	DataGrid getSpkc(Cgjh cgjh);
	DataGrid toYwrk(String cgjhDetIds);
	DataGrid printCgjh(Cgjh cgjh);
	DataGrid detDg(Cgjh cgjh);
	DataGrid toCgjhFromCgjh(Cgjh cgjh);
}
