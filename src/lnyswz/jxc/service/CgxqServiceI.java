package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Cgxq;


public interface CgxqServiceI {
	public void save(Cgxq cgxq);
	public void updateCancel(Cgxq cgxq);
	public void updateRefuse(Cgxq cgxq);
//	public void updateComplete(Cgxq cgxq);
	public DataGrid datagrid(Cgxq cgxq);
//	public DataGrid detDatagrid(String cgxqlsh);
	public DataGrid toCgjh(String cgxqDetIds);
	public DataGrid getSpkc(Cgxq cgxq);
}
