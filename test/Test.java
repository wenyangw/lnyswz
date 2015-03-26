import java.math.BigDecimal;


public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BigDecimal a1 = new BigDecimal("3.14");
		BigDecimal a2 = new BigDecimal("3.76");
		System.out.println(a1.multiply(a2));
		System.out.println(a1.multiply(a2).setScale(2, BigDecimal.ROUND_HALF_DOWN));
	}

}
