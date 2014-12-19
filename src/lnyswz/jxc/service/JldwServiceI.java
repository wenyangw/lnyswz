package lnyswz.jxc.service;

import java.util.List;
import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Jldw;

public interface JldwServiceI {

	public DataGrid datagrid(Jldw jldw);

	public Jldw add(Jldw jldw);

	public void edit(Jldw jldw);

	public void delete(Jldw jldw);

	public List<Jldw> listJldw(Jldw jldw);
}
