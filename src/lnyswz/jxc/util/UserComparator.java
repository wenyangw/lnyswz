package lnyswz.jxc.util;

import java.util.Comparator;

import lnyswz.jxc.model.TButton;
import lnyswz.jxc.model.TUser;


/**
 * 按钮排序
 * 
 * @author 王文阳
 * 
 */
public class UserComparator implements Comparator<TUser> {

	public int compare(TUser o1, TUser o2) {
		return o1.getOrderNum() - o2.getOrderNum();
	}

}
