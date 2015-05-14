package lnyswz.jxc.util;

import java.util.Comparator;

import lnyswz.jxc.model.TCatalog;
import lnyswz.jxc.model.TMenu;


/**
 * 模块排序
 * 
 * @author 王文阳
 * 
 */
public class CatalogComparator implements Comparator<TCatalog> {

	public int compare(TCatalog o1, TCatalog o2) {
		return o1.getOrderNum() - o2.getOrderNum();
	}

}
