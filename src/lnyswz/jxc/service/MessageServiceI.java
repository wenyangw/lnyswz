package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Message;

public interface MessageServiceI {
	public void add(Message message);

	public void edit(Message message);

	public boolean delete(Message message);

	public DataGrid sendDg(Message message);
	
	public DataGrid receiveDg(Message message);

    public Message getMessage(Message message);
}
