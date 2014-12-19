package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Gys;
import lnyswz.jxc.bean.Kh;

public interface KhServiceI {

	public Kh add(Kh kh);
	public void edit(Kh kh);
	public boolean delete(Kh kh);
	public boolean existKh (Kh kh);
	public boolean existKhDet (Kh kh);
	public Kh addDet(Kh kh);
	public void editDet(Kh kh);
	public void deleteDet(Kh kh);
	public List<Kh> listKhs(Kh kh);
	public DataGrid datagrid(Kh kh);
	public DataGrid datagridDet(Kh kh);
	
	/**
	 * 王文阳
	 * 2013.10.08
	 * 获得客户信息
	 */
	public DataGrid khDg(Kh kh);
	
	/**
	 * 王文阳
	 * 2013.10.08
	 * 获得客户检索信息
	 */
	public Kh loadKh(String khbh, String depId);

	public Kh checkKh(Kh kh);

	public int getAuditLevel(Kh kh);
	
	public DataGrid sxkhDg(Kh kh);
	
	public boolean isSxkh(Kh kh);
	public DataGrid listKhByYwy(Kh kh);
}
