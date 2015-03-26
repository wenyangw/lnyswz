package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Kfpd;


public interface KfpdServiceI {
	public void save(Kfpd kfpd);
	public void cjKfpd(Kfpd kfpd);
	public DataGrid datagrid(Kfpd kfpd);
	public DataGrid detDatagrid(String kfpdlsh);
	public DataGrid getSpkc(Kfpd kfpd);
}
