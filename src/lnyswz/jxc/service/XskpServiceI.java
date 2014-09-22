package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Chart;
import lnyswz.jxc.bean.Kfck;
import lnyswz.jxc.bean.Xskp;
import lnyswz.jxc.bean.Xsth;


public interface XskpServiceI {
	public Xskp save(Xskp xskp);
	public void cjXskp(Xskp xskp);
	public void createXsth(Xskp xskp);
//	public void updateComplete(Xskp xskp);
	public DataGrid datagrid(Xskp xskp);
	public DataGrid detDatagrid(String xskplsh);
	public DataGrid getSpkc(Xskp xskp);
	public List<String> toJs(String xskplsh);
	public DataGrid toYwrk(Xskp xskp);
	public DataGrid datagridDet(Xskp xskp);
	public Object toXsth(String xskpDetIds);
	public DataGrid getXskpNoHk(Xskp xskp);
	public DataGrid getXskpNoHkFirst(Xskp xskp);
	public DataGrid printXsqk(Xskp xskp);
	public List<Xskp> listFyrs(Xskp xskp);
	public DataGrid getLatestXs(Xskp xskp);
	public Chart getChartXsje(Xskp xskp);
}
