package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Bgrk;

public interface BgrkServiceI {
	Bgrk save(Bgrk bgrk);
	void cjBgrk(Bgrk bgrk);
	DataGrid datagrid(Bgrk bgrk);
	DataGrid detDatagrid(String bgrklsh);
	DataGrid getSpkc(Bgrk Bgrk);
}
