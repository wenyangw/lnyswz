package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Splb;

public interface SplbServiceI {

	public Splb add(Splb splb);

	public boolean edit(Splb splb);

	public void delete(int id);

	public List<Splb> listSplbs(Splb splb);

	public DataGrid datagrid(Splb splb);

}
