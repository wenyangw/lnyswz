package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.model.TDepartment;

public interface DepartmentServiceI {
	public Department add(Department department);
	public void edit(Department department);
	public void delete(String ids);
	public DataGrid datagrid(Department department);
	public List<Department> listDeps();
	public List<Department> listYws(Department department);
	//public String getDepName(String str, BaseDaoI<TDepartment> depDao);
}
