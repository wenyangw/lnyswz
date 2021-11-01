package lnyswz.jxc.service;

import lnyswz.jxc.bean.Department;
import lnyswz.jxc.bean.Gys;
import lnyswz.jxc.bean.Ywrk;

import java.math.BigDecimal;
import java.util.List;

public interface RkServiceI {

	void saveYf(Department dep, Gys gys, BigDecimal hjje, String ywrklsh, String ywbtlsh, String type);
	List<Ywrk> listYwrkNoFk(Ywrk ywrk);
}
