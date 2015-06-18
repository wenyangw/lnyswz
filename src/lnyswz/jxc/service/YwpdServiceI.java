package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Ywpd;


public interface YwpdServiceI {
	public Ywpd save(Ywpd ywpd);
	public Ywpd cjYwpd(Ywpd ywpd);
	public DataGrid datagrid(Ywpd ywpd);
	public DataGrid detDatagrid(String ywpdlsh);
	public DataGrid toKfpd(String ywpdlsh);
	public DataGrid getSpkc(Ywpd ywpd);
	public DataGrid printYwpd(Ywpd ywpd);
}
