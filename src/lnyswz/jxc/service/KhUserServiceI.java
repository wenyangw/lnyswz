package lnyswz.jxc.service;

import lnyswz.jxc.bean.KhUser;

public interface KhUserServiceI {
	
	public KhUser add(KhUser khUser);
	
	public void edit(KhUser khUser);
	
	public void delete(KhUser khUser);

	public KhUser checkKhUser(KhUser khUser);

}
