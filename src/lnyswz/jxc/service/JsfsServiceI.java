package lnyswz.jxc.service;

import java.util.List;
import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Jsfs;

public interface JsfsServiceI {

	public DataGrid datagrid(Jsfs jsfs);

	public Jsfs add(Jsfs jsfs);

	public void edit(Jsfs jsfs);

	public void delete(Jsfs jsfs);

	public List<Jsfs> listJsfs(Jsfs jsfs);


}
