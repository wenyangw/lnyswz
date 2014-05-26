package lnyswz.jxc.service;

import java.util.List;
import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Khlx;

public interface KhlxServiceI {

	public DataGrid datagrid(Khlx khlx);

	public Khlx add(Khlx khlx);

	public void edit(Khlx khlx);

	public void delete(Khlx khlx);

	public List<Khlx> listKhlx(Khlx khlx);
}
