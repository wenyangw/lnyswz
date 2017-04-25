package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Fyd;


public interface FydServiceI {
	public DataGrid datagrid(Fyd fyd);
	public DataGrid detDatagrid(String fydlsh);
	public void updateXsdj(Fyd fyd);
}
