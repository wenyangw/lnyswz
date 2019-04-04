package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.KfbgSp;
import lnyswz.jxc.bean.KfbgSpZz;



public interface KfbgSpServiceI {
	
	public KfbgSp add(KfbgSp kfbgSp);
	
//	public DataGrid searchSp(KfbgSp kfbgSp);
	
	public KfbgSpZz getKfbgSpZz(KfbgSp kfbgSp);
	
	public DataGrid datagridKfbgSpMxs(KfbgSp kfbgSp);
	
	public DataGrid datagridKfbgSpMxByDate(KfbgSp kfbgSp);
	
	public KfbgSp getKfbgSpMx(KfbgSp kfbgSp);


}
