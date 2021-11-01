package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.Gys;
import lnyswz.jxc.bean.Ywrk;

import java.math.BigDecimal;
import java.util.List;


public interface YwrkServiceI {
	Ywrk save(Ywrk ywrk);
	Ywrk cjYwrk(Ywrk ywrk);
//	void updateComplete(Ywrk ywrk);
	DataGrid datagrid(Ywrk ywrk);
	DataGrid detDatagrid(String ywrklsh);
	DataGrid toKfrk(String ywrklsh);
	DataGrid toYwbt(String ywrklsh);
	DataGrid printYwrk(Ywrk ywrk);
	DataGrid getSpkc(Ywrk ywrk);
	DataGrid datagridDet(Ywrk ywrk);
	DataGrid ywrkmx(Ywrk ywrk);
	DataGrid toXsth(Ywrk ywrk);
	DataGrid changeYwrk(Ywrk ywrk);
	DataGrid printKfrk(Ywrk ywrk);
	Ywrk getYwrk(Ywrk ywrk);
	List<Gys> listGysYf(Ywrk ywrk);

//	List<Ywrk> listYwrkNoFk(Ywrk ywrk);


}
