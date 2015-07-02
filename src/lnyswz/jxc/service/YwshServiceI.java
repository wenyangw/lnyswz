package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Ywsh;


public interface YwshServiceI {
	public void updateAudit(Ywsh ywsh);
	public void updateXqshAudit(Ywsh ywsh);
	public void updateJhshAudit(Ywsh ywsh);
	public void updateRefuse(Ywsh ywsh);
	public void updateXqshRefuse(Ywsh ywsh);
	public void updateJhshRefuse(Ywsh ywsh);
	public DataGrid datagrid(Ywsh ywsh);
	public DataGrid xqshDatagrid(Ywsh ywsh);
	public DataGrid jhshDatagrid(Ywsh ywsh);
	public DataGrid detDatagrid(String ywshlsh);
	public DataGrid listAudits(Ywsh ywsh);
	public DataGrid listCgxqAudits(Ywsh ywsh);
	public DataGrid listCgjhAudits(Ywsh ywsh);
	public DataGrid refreshYwsh(Ywsh ywsh);
	public DataGrid refreshXqsh(Ywsh ywsh);
	public DataGrid refreshJhsh(Ywsh ywsh);
	
}
