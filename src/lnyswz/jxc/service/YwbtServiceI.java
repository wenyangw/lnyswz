package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Ywbt;


public interface YwbtServiceI {
	public Ywbt save(Ywbt ywbt);
	public DataGrid datagrid(Ywbt ywbt);
	public DataGrid detDatagrid(String ywbtlsh);
	public DataGrid printYwbt(Ywbt ywbt);
}
