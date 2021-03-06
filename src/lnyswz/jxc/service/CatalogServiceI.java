package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Catalog;

public interface CatalogServiceI {
	public Catalog add(Catalog c);
	public void edit(Catalog c);
	public void delete(String ids);
	public List<Catalog> listCatas(String type);
	public DataGrid datagrid(Catalog c);
}
