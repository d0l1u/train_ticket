package com.l9e.util;

import java.math.BigDecimal;

/**
 * 金额计算
 */
public class AmountUtil {
	
	private static final long serialVersionUID = 1L;

	public static double add(double arg1, double arg2){
		BigDecimal a = new BigDecimal(Double.toString(arg1));
		BigDecimal b = new BigDecimal(Double.toString(arg2));
		return a.add(b).doubleValue();
	}
	
	public static double sub(double arg1, double arg2){
		BigDecimal a = new BigDecimal(Double.toString(arg1));
		BigDecimal b = new BigDecimal(Double.toString(arg2));
		return a.subtract(b).doubleValue();
	}
	
	public static double mul(double arg1, double arg2){
		BigDecimal a = new BigDecimal(Double.toString(arg1));
		BigDecimal b = new BigDecimal(Double.toString(arg2));
		return a.multiply(b).doubleValue();
	}
	
	public static double div(double arg1, double arg2){
		BigDecimal a = new BigDecimal(Double.toString(arg1));
		BigDecimal b = new BigDecimal(Double.toString(arg2));
		return a.divide(b,2,BigDecimal.ROUND_HALF_UP).doubleValue();//四舍五入
	}
	
	public static double divCeil(double arg1, double arg2){
		BigDecimal a = new BigDecimal(Double.toString(arg1)); 
		BigDecimal b = new BigDecimal(Double.toString(arg2));
		return a.divide(b,2,BigDecimal.ROUND_CEILING).doubleValue();//进1
	}
	
	public static double ceil(double arg1){
		BigDecimal a = new BigDecimal(Double.toString(arg1)); 
		return a.setScale(2, BigDecimal.ROUND_CEILING).doubleValue();//进1
	}
	
	/**
	 * 0.25/0.75进位
	 * @param arg
	 * @return
	 */
	public static double quarterConvert(double arg){
		String str = String.valueOf(arg);
		String[] array = str.split("\\.");
		if(arg < 0 || array == null || array.length<2){
			return arg;
		}else{
			double intPart = Double.parseDouble(array[0]);
			double decPart = Double.parseDouble("0." + array[1]);
			if(decPart < 0.25){
				return intPart;
			}else if(decPart >= 0.25 && decPart < 0.75){
				return add(intPart, 0.5);
			}else{
				return add(intPart, 1);
			}
		}
	}
}
