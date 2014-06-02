package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Xshk;

public interface XshkServiceI {
	public Xshk save(Xshk xshk);
	public void cancelXshk(Xshk xshk);
	public DataGrid datagrid(Xshk xshk);
	public DataGrid detDatagrid(Xshk xshk);
}
