package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Sp;

public interface SpServiceI {

	public Sp add(Sp sp);
	public void edit(Sp sp);
	public void delete(Sp sp);
	public DataGrid datagrid(Sp sp);
	public boolean existSp(Sp sp);
	public void editSpDet(Sp sp);
	public void deleteSpDet(Sp sp);
	public Sp loadSp(String spbh, String depId);
	public DataGrid spDg(Sp sp);
	public DataGrid getSpkc(Sp sp);
	public DataGrid datagridBgy(Sp sp);
}
