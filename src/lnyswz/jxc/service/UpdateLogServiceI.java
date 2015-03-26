package lnyswz.jxc.service;

import java.util.List;
import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.UpdateLog;

public interface UpdateLogServiceI {

	public List<UpdateLog> listUpdateLog(UpdateLog update);

	public DataGrid datagrid(UpdateLog update);

	public UpdateLog add(UpdateLog update);

	public void edit(UpdateLog update);

	public void delete(String ids);

}
