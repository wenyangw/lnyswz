package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Ywsh;


public interface YwshServiceI {
	public Ywsh save(Ywsh ywsh);
	public DataGrid datagrid(Ywsh ywsh);
	public DataGrid detDatagrid(String ywshlsh);
	public DataGrid listAudits(Ywsh ywsh);
}
