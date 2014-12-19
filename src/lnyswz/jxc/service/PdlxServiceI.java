package lnyswz.jxc.service;

import java.util.List;
import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Pdlx;

public interface PdlxServiceI {
	public Pdlx add(Pdlx pdlx);
	public void edit(Pdlx pdlx);
	public void delete(Pdlx pdlx);
	public DataGrid datagrid(Pdlx pdlx);
	public List<Pdlx> listPdlx(Pdlx pdlx);
}
