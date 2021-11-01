package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Xskp;


public interface XskpServiceI {
	Xskp save(Xskp xskp);
	Xskp saveXsfl(Xskp xskp);
	void cjXskp(Xskp xskp);
	void cjXsfl(Xskp xskp);
	void createXsth(Xskp xskp);
//	public void updateComplete(Xskp xskp);
	DataGrid datagrid(Xskp xskp);
	DataGrid datagridXsfl(Xskp xskp);
	DataGrid detDatagrid(String xskplsh);
	DataGrid getSpkc(Xskp xskp);
	List<String> toJs(String xskplsh);
	DataGrid toYwrk(Xskp xskp);
	DataGrid datagridDet(Xskp xskp);
	Object toXsth(String xskpDetIds);
	DataGrid getXskpNoHk(Xskp xskp);
	DataGrid getXskpNoHkFirst(Xskp xskp);
	DataGrid printXsqk(Xskp xskp);
	List<Xskp> listFyrs(Xskp xskp);
	DataGrid getLatestXs(Xskp xskp);

    Xskp getXskp(Xskp xskp);
}
