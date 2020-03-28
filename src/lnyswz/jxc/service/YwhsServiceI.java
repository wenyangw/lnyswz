package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Ywhs;


public interface YwhsServiceI {
	public Ywhs save(Ywhs ywhs);
	public Ywhs cjYwhs(Ywhs ywhs);
	public DataGrid datagrid(Ywhs ywhs);
	public DataGrid detDatagrid(Ywhs ywhs);
	public DataGrid toKfhs(String ywhslsh);
	public DataGrid getSpkc(Ywhs ywhs);
	public DataGrid printYwhs(Ywhs ywhs);
}
