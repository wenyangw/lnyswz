package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Fplx;

public interface FplxServiceI {

	public DataGrid datagrid(Fplx fplx);

	public Fplx add(Fplx fplx);

	public void edit(Fplx fplx);

	public void delete(Fplx fplx);

	public List<Fplx> listFplx(Fplx fplx);
}
