package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Ywdb;


public interface YwdbServiceI {
	Ywdb save(Ywdb ywdb);
	void cjYwdb(Ywdb ywdb);
	DataGrid datagrid(Ywdb ywdb);
	DataGrid detDatagrid(String ywdblsh);
	DataGrid getSpkc(Ywdb ywdb);

    DataGrid printYwdb(Ywdb ywdb);
}
