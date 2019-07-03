package com.l9e.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理工具类
 * @author 肖有标
 *
 */
public class DateUtil {
	
	//一天的毫秒数
	private static final long DD=1000*60*60*24;
	
	//一分钟的毫秒数
	private static final long MD=1000*60;
	
	public static final String DATE_FMT1 = "yyyy-MM-dd";
	
	public static final String DATE_FMT2 = "yyyyMMddHHmmss";
	
	public static final String DATE_FMT3 = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 把时间转换成字符串
	 * @param date  时间
	 * @param formatStr  要转换的格式
	 * @return
	 */
	public static String dateToString(Date date,String formatStr){
		SimpleDateFormat sdf=new SimpleDateFormat(formatStr);
		sdf.format(date);
		return sdf.format(date);
	}
	
	public static Date stringToDate(String dateStr,String formatStr){
		SimpleDateFormat sdf=new SimpleDateFormat(formatStr);
		Date date;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return date;
	}
	
	//格式转换
	public static String stringToString(String dateStr,String formatStr){
		return dateToString(stringToDate(dateStr,formatStr), formatStr);
	}
	
	public static Date dateAddDays(Date date,int addDays){
		if(null==date){
			return null;
		}
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, addDays);
		return cal.getTime();
	}
	
	/**
	 * @author zuoyx
	 * @param date	     时间1
	 * @param date2        时间2
	 * @return long 相差小时
	 */
	public static long dayDiff(String min1, String min2) {	
		long n1 = stringToDate(min1,DATE_FMT1).getTime()/DD;	
		long n2 = stringToDate(min2,DATE_FMT1).getTime()/DD;
		return  Math.abs(n1 - n2);				
	}
	
	/**
	 * @author zuoyx
	 * @param date	     时间1
	 * @param date2        时间2
	 * @return long 相差分钟
	 */
	public static long minuteDiff(Date min1, Date min2) {	
		long n1 = min1.getTime()/MD;	
		long n2 = min2.getTime()/MD;
		return  Math.abs(n1 - n2);				
	}
	
	
	/**
	 * @author zuoyx
	 * @param val	    相差时间值
	 * @return String
	 */
	public static String minuteBefore(int val) {	
		String oneHoursAgoTime = "";
 		Calendar cal = Calendar.getInstance();
 		cal.set(Calendar.MINUTE, Calendar.MINUTE - val);
 		oneHoursAgoTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
 		.format(cal.getTime());
 		return oneHoursAgoTime;
	}
	
	public static void main(String[] args){
		System.out.println(minuteDiff(new Date(),stringToDate("2015-02-04 14:00:00", DATE_FMT3)));
		
		System.out.println(dayDiff("2015-02-10","2015-02-05"));
	}
}
