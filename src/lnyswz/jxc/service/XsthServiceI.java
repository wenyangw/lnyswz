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
	Xsth save(Xsth xsth);
	void cancelXsth(Xsth xsth);
	DataGrid datagrid(Xsth xsth);
	DataGrid xsthSpDg(Xsth xsth);
	DataGrid detDatagrid(Xsth xsth);
	DataGrid toKfck(Xsth xsth);
	DataGrid toCgjh(Xsth xsth);
	DataGrid toXskp(Xsth xsth);
	DataGrid datagridDet(Xsth xsth);
	DataGrid getSpkc(Xsth xsth);
	DataGrid printXsth(Xsth xsth);
	DataGrid printXsht(Xsth xsth);
	DataGrid printShd(Xsth xsth);
	DataGrid printXsthByBgy(Xsth xsth);
	void updateThsl(Xsth xsth);
	void updateLock(Xsth xsth);
	void updateUnlock(Xsth xsth);
	void updateShdz(Xsth xsth);
	void updateBz(Xsth xsth);
	void updateXsthOut(Xsth xsth);
	DataGrid getSpBgys(Xsth xsth);
	DataGrid getYsje(Xsth xsth);
	DataGrid refreshXsth(Xsth xsth);
	void updateZsComplete(Xsth xsth);
	void updateYf(Xsth xsth);
	boolean isCancel(Xsth xsth);
	boolean isLocked(Xsth xsth);
	boolean isSaved(Xsth xsth);
	DataGrid getXsth(Xsth xsth);
	DataGrid getXsthOutList(Xsth xsth);
	DataGrid getXsthOutDetail(Xsth xsth);
	DataGrid xsthCarDg(Xsth xsth);
	DataGrid loadXsth(Xsth xsth);
}
