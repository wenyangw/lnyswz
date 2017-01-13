package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Xsth;

/**
 * 
 * @author Wangwy
 * @edited
 * 	2015.08.12 增加打印销售合同
 */
public interface XsthServiceI {
	public Xsth save(Xsth xsth);
	public void cancelXsth(Xsth xsth);
	public DataGrid datagrid(Xsth xsth);
	public DataGrid detDatagrid(Xsth xsth);
	public DataGrid toKfck(Xsth xsth);
	public DataGrid toCgjh(Xsth xsth);
	public DataGrid toXskp(Xsth xsth);
	public DataGrid datagridDet(Xsth xsth);
	public DataGrid getSpkc(Xsth xsth);
	public DataGrid printXsth(Xsth xsth);
	public DataGrid printXsht(Xsth xsth);
	public DataGrid printShd(Xsth xsth);
	public DataGrid printXsthByBgy(Xsth xsth);
	public void updateThsl(Xsth xsth);
	public void updateLock(Xsth xsth);
	public void updateUnlock(Xsth xsth);
	public DataGrid getSpBgys(Xsth xsth);
	public DataGrid getYsje(Xsth xsth);
	public DataGrid refreshXsth(Xsth xsth);
	public void updateZsComplete(Xsth xsth);
	public void updateYf(Xsth xsth);
}
