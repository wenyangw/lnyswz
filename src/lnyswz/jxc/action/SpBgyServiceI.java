package lnyswz.jxc.action;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.SpBgy;

public interface SpBgyServiceI {
	public void saveSpBgy(SpBgy spBgy);

	public DataGrid datagridBgy(SpBgy spBgy);
}
