package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Spdl;

public interface SpdlServiceI {

	public Spdl add(Spdl spdl);
	public void edit(Spdl spdl);
	public void delete(String id);
	public List<Spdl> listSpdls();
	public DataGrid datagrid(Spdl spdl);
}
