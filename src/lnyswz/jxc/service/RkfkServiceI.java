package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Rkfk;

public interface RkfkServiceI {
	Rkfk save(Rkfk rkfk);
	void cancelRkfk(Rkfk rkfk);
	DataGrid datagrid(Rkfk rkfk);
	DataGrid detDatagrid(Rkfk rkfk);
	boolean canCancel(String rkfklsh);
}
