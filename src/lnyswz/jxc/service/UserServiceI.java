package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.User;

public interface UserServiceI {
	User login(User user);

	User add(User user);

	void edit(User user);
	
	void delete(User user);
	
	DataGrid datagrid(User user);
	
	List<User> listYwys(String depId);
	
	List<User> listYwyByYwy(int i);

	void editPassword(User user);

	boolean checkPassword(User user);

	List<User> listBgys(User user);

	List<Department> getContacts(User user);

	User checkYwy(User user);
}
