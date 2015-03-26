package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Hw;

public interface HwServiceI {

	public DataGrid datagrid(Hw hw);

	public Hw add(Hw hw);

	public void edit(Hw hw);

	public void delete(Hw hw);

	public List<Hw> listHw(Hw hw);

}
