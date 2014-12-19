package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Kfrk;


public interface KfrkServiceI {
	public Kfrk save(Kfrk kfrk);
	public void cjKfrk(Kfrk kfrk);
//	public void updateComplete(Kfrk kfrk);
	public DataGrid datagrid(Kfrk kfrk);
	public DataGrid detDatagrid(String kfrklsh);
	public DataGrid toYwrk(String kfrklshs);
	public DataGrid printKfrk(Kfrk kfrk);
}
