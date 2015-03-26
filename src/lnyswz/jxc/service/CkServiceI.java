package lnyswz.jxc.service;

import java.util.List;
import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Ck;

public interface CkServiceI {

	public DataGrid datagrid(Ck ck);

	public Ck add(Ck ck);

	public void edit(Ck ck);

	public boolean delete(Ck ck);

	public List<Ck> listCk(Ck ck);
}
