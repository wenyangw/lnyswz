package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Kfhs;


public interface KfhsServiceI {
	public void save(Kfhs kfhs);
	public void cjKfhs(Kfhs kfhs);
	public DataGrid datagrid(Kfhs kfhs);
	public DataGrid detDatagrid(String kfhslsh);
	public DataGrid printKfhs(Kfhs kfhs);
}
