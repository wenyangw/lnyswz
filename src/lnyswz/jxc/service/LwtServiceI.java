package lnyswz.jxc.service;


import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Lwt;

public interface LwtServiceI {
	DataGrid listKhByYwy(Lwt l);
	DataGrid listKhByYwyXsth(Lwt l);
	DataGrid listXstjByYears(Lwt l);
	DataGrid dgSpByCk(Lwt lwt);
	DataGrid dgSp(Lwt lwt);
	DataGrid dgKhs(Lwt lwt);
	DataGrid dgKhDets(Lwt lwt);
	DataGrid dgKhsByYwy(Lwt lwt);
	Long countYwyByYwy(Lwt lwt);
	DataGrid getYwyByBmbh(Lwt t);
	DataGrid getYwyByYwy(Lwt t);
	DataGrid getXsths(Lwt lwt);
	DataGrid listXstjByMonth(Lwt lwt);
}
