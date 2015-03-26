package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Fh;


public interface FhServiceI {

	public DataGrid datagrid(Fh fh);

	public Fh add(Fh fh);

	public void edit(Fh fh);

	public void delete(Fh fh);
	
	public List<Fh> listFhs(Fh fh);
}
