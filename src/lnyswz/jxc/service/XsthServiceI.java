package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Xsth;
import lnyswz.jxc.bean.XsthDet;

import java.util.List;

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
	public DataGrid xsthSpDg(Xsth xsth);
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
	public void updateShdz(Xsth xsth);
	public void updateXsthOut(Xsth xsth);
	public DataGrid getSpBgys(Xsth xsth);
	public DataGrid getYsje(Xsth xsth);
	public DataGrid refreshXsth(Xsth xsth);
	public void updateZsComplete(Xsth xsth);
	public void updateYf(Xsth xsth);
	public boolean isCancel(Xsth xsth);
	public boolean isLocked(Xsth xsth);
	public boolean isSaved(Xsth xsth);
	public DataGrid getXsth(Xsth xsth);
	public DataGrid getXsthOutList(Xsth xsth);
	public DataGrid getXsthOutDetail(Xsth xsth);
	public DataGrid xsthCarDg(Xsth xsth);
}
