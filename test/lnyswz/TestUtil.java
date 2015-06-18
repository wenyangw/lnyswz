package lnyswz;

import java.math.BigDecimal;
import java.util.Date;

import lnyswz.common.util.DateUtil;

public class TestUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//BigDecimal a1 = new BigDecimal("3.14");
		//BigDecimal a2 = new BigDecimal("2.76");
		//System.out.println(a1.multiply(a2));
		//System.out.println(a1.multiply(a2).setScale(2, BigDecimal.Ro));
		Date d = new Date();
		System.out.println("DDD:" + d);
		System.out.println(DateUtil.getDay(d).compareTo("22") < 0);
	}

}
