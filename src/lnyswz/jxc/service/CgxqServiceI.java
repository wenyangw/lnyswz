package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Cgxq;

public interface CgxqServiceI {
	public Cgxq save(Cgxq cgxq);
	public void updateCancel(Cgxq cgxq);
	public void updateRefuse(Cgxq cgxq);
	public void updateComplete(Cgxq cgxq);
	public void updateDbxq(Cgxq cgxq);
	public DataGrid printCgxq(Cgxq cgxq);
	public DataGrid datagrid(Cgxq cgxq);
//	public DataGrid detDatagrid(String cgxqlsh);
	public DataGrid toCgjh(String cgxqDetIds);
	public DataGrid toYwdb(Cgxq cgxq);
	public DataGrid getSpkc(Cgxq cgxq);
	public DataGrid detDatagrid(String cgxqlsh);
}
