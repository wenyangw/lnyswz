package lnyswz.jxc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Chart;

public interface ChartServiceI {
	
	public Chart getXstj(Chart chart);
	public Chart getKctj(Chart chart);
	public Chart getXsjgfx(Chart chart);
	public Chart getXskhtj(Chart chart);
	public DataGrid listKh(Chart chart);
}
