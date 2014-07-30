package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Xsth;

public interface XsthServiceI {
	public Xsth save(Xsth xsth);
	public void cancelXsth(Xsth xsth);
	public DataGrid datagrid(Xsth xsth);
	public DataGrid detDatagrid(String xsthlsh);
	public DataGrid toKfck(Xsth xsth);
	public DataGrid toXskp(String xsthDetIds);
	public DataGrid datagridDet(Xsth xsth);
	public DataGrid getSpkc(Xsth xsth);
	public DataGrid printXsth(Xsth xsth);
	public DataGrid printXsthByBgy(Xsth xsth);
	public void updateThsl(Xsth xsth);
	public void updateLock(Xsth xsth);
	public void updateUnlock(Xsth xsth);
	public DataGrid getSpBgys(Xsth xsth);
}
