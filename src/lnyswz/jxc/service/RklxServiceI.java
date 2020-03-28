package lnyswz.jxc.service;

import java.util.List;
import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Rklx;

public interface RklxServiceI {

	public DataGrid datagrid(Rklx rklx);

	public Rklx add(Rklx rklx);

	public void edit(Rklx rklx);

	public void delete(Rklx rklx);

	public List<Rklx> listRklx(Rklx rklx);
}
