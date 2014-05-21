package lnyswz.jxc.service;

import java.util.List;
import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Rylx;

public interface RylxServiceI {

	public DataGrid datagrid(Rylx rylx);

	public Rylx add(Rylx rylx);

	public void edit(Rylx rylx);

	public void delete(Rylx rylx);

	public List<Rylx> listRylx(Rylx rylx);
}
