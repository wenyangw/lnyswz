package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Ywrk;


public interface YwrkServiceI {
	public Ywrk save(Ywrk ywrk);
	public Ywrk cjYwrk(Ywrk ywrk);
//	public void updateComplete(Ywrk ywrk);
	public DataGrid datagrid(Ywrk ywrk);
	public DataGrid detDatagrid(String ywrklsh);
	public DataGrid toKfrk(String ywrklsh);
	public DataGrid toYwbt(String ywrklsh);
	public DataGrid printYwrk(Ywrk ywrk);
	public DataGrid getSpkc(Ywrk ywrk);
	public DataGrid datagridDet(Ywrk ywrk);
	public DataGrid toXsth(Ywrk ywrk);
}
