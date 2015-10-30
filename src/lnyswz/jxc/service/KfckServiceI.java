package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Kfck;


public interface KfckServiceI {
	public void save(Kfck kfck);
	public void cjKfck(Kfck kfck);
//	public void updateComplete(Kfck kfck);
	public DataGrid datagrid(Kfck kfck);
	public DataGrid detDatagrid(String kfcklsh);
//	public DataGrid toYwrk(String kfcklshs);
	public DataGrid getSpkc(Kfck kfck);
	public DataGrid printKfck(Kfck kfck);
}
