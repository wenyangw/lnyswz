package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Kh;
import lnyswz.jxc.bean.Shdz;

public interface ShdzServiceI {

	public DataGrid datagrid(Shdz shdz);

	public Shdz add(Shdz shdz);

	public void edit(Shdz shdz);

	public void delete(Shdz shdz);

	public List<Shdz> listShdz(Shdz shdz);
	
	public DataGrid shdzDg(Shdz shdz);
}
