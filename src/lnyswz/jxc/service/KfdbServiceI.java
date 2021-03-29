package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Kfdb;


public interface KfdbServiceI {
	Kfdb save(Kfdb kfdb);
	void cjKfdb(Kfdb kfdb);
	DataGrid datagrid(Kfdb kfdb);
	DataGrid detDatagrid(String kfdblsh);
}
