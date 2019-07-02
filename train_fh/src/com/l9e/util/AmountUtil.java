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
	 *  liuyi02
	 * */
	public static double divGiveDown(double arg1, double arg2){
		BigDecimal a = new BigDecimal(Double.toString(arg1)); 
		BigDecimal b = new BigDecimal(Double.toString(arg2));
		return a.divide(b,2,BigDecimal.ROUND_DOWN).doubleValue();//不进位的截断 舍弃
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
	//12月12日转换十二月十二日
	public static String numToChinese(String str){
		String[] newStr = str.split("月");
		char[] ch = newStr[0].toCharArray();
		String month = "";
		if(ch.length==2){
			if(ch[0]=='1'){
				month = "十";
			}
			month += getNumChinese(ch[1]);
		}else{
			month = getNumChinese(ch[0]);
		}
		month += "月";
		String[] dayStr = newStr[1].split("日");
		char[] chDay = dayStr[0].toCharArray();
		String day = "";
		if(chDay.length==2){
			if(chDay[0]=='1'){
				day = "十";
			}else if(chDay[0]=='2'){
				day = "二十";
			}else if(chDay[0]=='3'){
				day = "三十";
			}
			day += getNumChinese(chDay[1]);
		}else{
			day += getNumChinese(chDay[0]);
		}
		day += "日";
		return month+day+dayStr[1];
	}
	
	public static String getNumChinese(char ch){
		if(ch=='1') return "一";
		if(ch=='2') return "二";
		if(ch=='3') return "三";
		if(ch=='4') return "四";
		if(ch=='5') return "五";
		if(ch=='6') return "六";
		if(ch=='7') return "七";
		if(ch=='8') return "八";
		if(ch=='9') return "九";
		return "";
	}
	
}
