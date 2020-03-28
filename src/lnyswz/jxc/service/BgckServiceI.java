package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Bgck;

public interface BgckServiceI {
	Bgck save(Bgck bgck);
	void cjBgck(Bgck bgck);
	DataGrid datagrid(Bgck bgck);
	DataGrid detDatagrid(String bgcklsh);

}
