package lnyswz;

import java.util.Date;

import lnyswz.common.util.DateUtil;

public class TestUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Date d = new Date();
		System.out.println("DDD:" + d);
		System.out.println(DateUtil.getDay(d).compareTo("22") < 0);
	}

}
