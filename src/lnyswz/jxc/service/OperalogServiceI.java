package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Operalog;

public interface OperalogServiceI {
	public void delete(String ids);

	public DataGrid datagrid(Operalog ope);

}
