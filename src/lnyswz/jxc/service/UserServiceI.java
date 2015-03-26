package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.User;

public interface UserServiceI {
	public User login(User user);

	public User add(User user);

	public void edit(User user);
	
	public void delete(User user);
	
	public DataGrid datagrid(User user);
	
	public List<User> listYwys(String depId);

	public void editPassword(User user);

	public boolean checkPassword(User user);

	public List<User> listBgys(User user);

	
}
