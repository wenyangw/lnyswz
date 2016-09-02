package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Ywdb;


public interface YwdbServiceI {
	public Ywdb save(Ywdb ywdb);
	public void cjYwdb(Ywdb ywdb);
	public DataGrid datagrid(Ywdb ywdb);
	public DataGrid detDatagrid(String ywdblsh);
	public DataGrid getSpkc(Ywdb ywdb);
}
