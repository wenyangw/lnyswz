package lnyswz.jxc.util;

import java.util.Comparator;

import lnyswz.jxc.model.TSplb;


/**
 * 商品类别排序
 * 
 * @author 王文阳
 * 
 */
public class SplbComparator implements Comparator<TSplb> {

	public int compare(TSplb o1, TSplb o2) {
		return o1.getIdFrom().compareTo(o2.getIdFrom());
	}

}
