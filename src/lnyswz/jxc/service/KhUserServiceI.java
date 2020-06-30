package lnyswz.jxc.service;

import lnyswz.jxc.bean.KhUser;

public interface KhUserServiceI {
	
	KhUser add(KhUser khUser);
	
	void edit(KhUser khUser);
	
	void delete(KhUser khUser);

	KhUser checkKhUser(KhUser khUser);

}
