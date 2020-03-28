package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.TreeNode;
import lnyswz.jxc.bean.Spdw;

public interface SpdwServiceI {
	public Spdw add(Spdw spdw);
	public void edit(Spdw spdw);
	public void delete(String id);
	public List<Spdw> getSpdws();
	public DataGrid datagrid(Spdw spdw);
	public List<TreeNode> listSpfl(Spdw spdw);
}
