package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Kfck;


public interface KfckServiceI {
	Kfck save(Kfck kfck);
	void cjKfck(Kfck kfck);
//	public void updateComplete(Kfck kfck);
	DataGrid datagrid(Kfck kfck);
	DataGrid detDatagrid(String kfcklsh);
//	public DataGrid toYwrk(String kfcklshs);
	DataGrid getSpkc(Kfck kfck);
	DataGrid printKfck(Kfck kfck);
	DataGrid loadKfck(Kfck kfck);
}
