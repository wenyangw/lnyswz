package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Dict;

public interface DictServiceI {
	public Dict add(Dict d);

	public void edit(Dict b);

	public void delete(Dict dict);

	public DataGrid datagrid(Dict d);

	public List<Dict> listDict(Dict dict);
	
	public boolean isNeedDep(Dict d);
	
	public List<Dict> selectTree(Dict dict);

	public List<Dict> listFields(Dict dict);
}
