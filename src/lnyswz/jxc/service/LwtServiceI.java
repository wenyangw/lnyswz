package lnyswz.jxc.service;


import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Lwt;

public interface LwtServiceI {
	public DataGrid listKhByYwy(Lwt l);
	public DataGrid listKhByYwyXsth(Lwt l);
}
