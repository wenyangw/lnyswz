package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Kfrk;

public interface KfrkServiceI {
	Kfrk save(Kfrk kfrk);
	void cjKfrk(Kfrk kfrk);
//	void updateComplete(Kfrk kfrk);
	DataGrid datagrid(Kfrk kfrk);
	DataGrid detDatagrid(String kfrklsh);
	DataGrid toYwrk(String kfrklshs);
	DataGrid printKfrk(Kfrk kfrk);
	DataGrid loadKfrk(Kfrk kfrk);
}
