package com.l9e.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author Administrator
 *
 */
public class MathUtil {

	private static final Object LOCK = new  Object();
	private static int no = 100;

	/**
	 * java.math.BigDecimal  to java.lang.Double
	 * @param a
	 * @return
	 */
	public static Double bigDecimalTODouble(BigDecimal a){
		return a.doubleValue();
	}
	
	
	/**
	 * java.math.BigDecimal  to java.lang.String
	 * @param a
	 * @return
	 */
	public static String bigDecimalTOString(BigDecimal a){
		return a.toString();
	}
	
	/**
	 * @param n
	 * @return
	 * 生成随机数n-3
	 */
	@SuppressWarnings("unused")
	public static String getFixLenthString(int n){
        long str=(long)(Math.random()*9*Math.pow(10,n-1)) + (long)Math.pow(10,n-1);
        return String.valueOf(str);
	}
	
	
	/**
	 * @param value
	 * @param fix
	 * @return
	 */
	public static BigDecimal  stringTOBigDecimal(String value,int fix){
		return new BigDecimal(value).setScale(fix,BigDecimal.ROUND_HALF_DOWN);
	}
	
	/**
	 * @param a
	 * @param b
	 * @return
	 */
	public static  boolean compareToBigDecimal(BigDecimal a,BigDecimal b) {	
		return a.equals(b);		
	}
	

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	/*	BigDecimal a = new BigDecimal(1000.23d);
		
		Double cDouble= a.doubleValue();
		
		System.out.println(cDouble);*/
		
		
		System.err.println(getFixLenthString(7));
		
		//System.err.print(Math.random()*9);
		
		System.err.print(Math.random()*9*Math.pow(10,7-1));
		
		
		/*BigDecimal a = stringTOBigDecimal("4.0",2);
		BigDecimal b = stringTOBigDecimal("3.50",2);
		System.err.println(compareToBigDecimal(a,b));*/
		
	}

}
