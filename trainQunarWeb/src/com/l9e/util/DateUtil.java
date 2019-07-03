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
	
	public static final String DATE_HM = "HH:mm";
	
	public static final String DATE_FMT1 = "yyyy-MM-dd";
	
	public static final String DATE_FMT2 = "yyyyMMddHHmmss";
	
	public static final String DATE_FMT3 = "yyyy-MM-dd HH:mm:ss";

	//一分钟的毫秒数
	private static final long MD=1000*60;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return date;
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
	 * @param date	     时间
	 * @param addDays 要增加的天数
	 * @return "yyyy-MM-dd HH:mm:ss"
	 */
	public static String dateAddDaysFmt3(String date,String addDays){
		if(null==date){
			return null;
		}
		Calendar cal=Calendar.getInstance();
		cal.setTime(stringToDate(date, DATE_FMT3));
		cal.add(Calendar.DAY_OF_YEAR, Integer.valueOf(addDays));
		return dateToString(cal.getTime(),DATE_FMT3);
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
	
	public static void main(String[] args){
		System.out.println(minuteDiff(new Date(),stringToDate("2015-02-28 17:00:00", DATE_FMT3)));
		
		String from_15d = DateUtil.dateAddDaysFmt3("2015-01-01 12:00"+":00","-15");
		
		boolean mm = new Date().before(DateUtil.stringToDate("2014-12-17 12:00:00", "yyyy-MM-dd"));
		System.out.println(mm);
		double nn = 0.0;
		if(nn!=0){
			System.out.println("mmmmmmmmm");
		}
		if(new Date().before(DateUtil.stringToDate(from_15d, "yyyy-MM-dd"))){
			System.out.println("111111111");
		}
		
		System.out.println(DateUtil.dateToString(DateUtil.stringToDate("2015-01-01",DateUtil.DATE_FMT1),DateUtil.DATE_FMT1));
		
		if("2015-02-04".compareTo("2015-01-01")<0 && "2015-03-15".compareTo("2015-01-01")>0){
			System.out.println("nnnnnnnnnnnnnn");
		}
	}
}
