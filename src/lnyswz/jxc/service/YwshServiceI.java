package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Ywsh;


public interface YwshServiceI {
	public void updateAudit(Ywsh ywsh);
	public void updateRefuse(Ywsh ywsh);
	public DataGrid datagrid(Ywsh ywsh);
	public DataGrid detDatagrid(String ywshlsh);
	public DataGrid listAudits(Ywsh ywsh);
}
