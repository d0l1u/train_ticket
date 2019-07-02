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
	
	//刘毅
	public static final String DATE_FMT3 = "yyyy-MM-dd HH:mm:ss";
	
	public static final String DATE_FMT_YMD = "yyyyMMdd";
	
	//一天的毫秒数
	private static final long ND=1000*24*60*60;
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
	 * @return
	 */
	public static String dateAddDays(String date,String addDays){
		if(null==date){
			return null;
		}
		Calendar cal=Calendar.getInstance();
		cal.setTime(stringToDate(date, DATE_FMT1));
		cal.add(Calendar.DAY_OF_YEAR, Integer.valueOf(addDays));
		return dateToString(cal.getTime(),DATE_FMT1);
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
	 * @author 刘毅
	 * @param date	     时间1
	 * @param date2        时间2
	 * @return long 相差几天
	 */
	public static int dateDiff(Date d1, Date d2) {		
		long n1 = d1.getTime()/ND;	
		long n2 = d2.getTime()/ND;
		long diff = Math.abs(n1 - n2);				
		return (int)diff+1;	
	}
	
	/**
	 * @author zuoyx
	 * @param date	     时间1
	 * @param date2        时间2
	 * @return long 相差分钟
	 */
	public static String minuteDiff(String min1, String min2) {	
		Date d1 = stringToDate(min1,DATE_HM);
		Date d2 = stringToDate(min2,DATE_HM);
		long n1 = d1.getTime()/MD;	
		long n2 = d2.getTime()/MD;
		return  String.valueOf(Math.abs(n1 - n2));				
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
	
	/**分钟数值转换为HH:mm
	 * @author zuoyx
	 * @param  str   分钟
	 * @return HH:mm字符串
	 */
	public static String minuteFormat(String str) {	
		int minute = Integer.valueOf(str);
		String result = "";
		int hours = (int) (minute/60);
		int min = (int) (minute%60);
		if(hours>10){
			result += hours+"";
		}else if(hours>0){
			result +="0"+hours;
		}else{
			result +="00";
		}
		if(min>10){
			result +=":"+min;
		}else{
			result +=":0"+min;
		}
		return  result;	
	}
	
	public static String getDatePoor(Date endDate, Date nowDate) {
		 
	    long nd = 1000 * 24 * 60 * 60;
	    long nh = 1000 * 60 * 60;
	    long nm = 1000 * 60;
	    // long ns = 1000;
	    // 获得两个时间的毫秒时间差异
	    long diff = endDate.getTime() - nowDate.getTime();
	    // 计算差多少天
	    long day = diff / nd;
	    // 计算差多少小时
	    long hour = diff % nd / nh;
	    // 计算差多少分钟
	    long min = diff % nd % nh / nm;
	    // 计算差多少秒//输出结果
	    // long sec = diff % nd % nh % nm / ns;  
	    return  day+":"+hour+":"+min;
	}
	
	/**
	 * 根据出发和到达时间，相差天数,计算相差分钟数
	 * @param time1
	 * @param time2
	 * @param travel_time
	 * @param differ
	 */
	public static String dateDifferMin(String time1, String time2,
			String travel_time, String differ) {
		String travel_time_new=DateUtil.dateAddDays(travel_time, differ);
		String fdate=travel_time+time1;
		String ddate=travel_time_new+time2;
		System.out.println(fdate);
		System.out.println(ddate);
		Date date1=DateUtil.stringToDate(fdate, "yyyy-MM-ddHH:mm");
		Date date2=DateUtil.stringToDate(ddate, "yyyy-MM-ddHH:mm");
		return minuteDiff(date2,date1)+"";
	}
	
	public static void main(String[] args) {
		
		String  time1="19:16";
		String  time2="20:19";
		String  travel_time ="2017-10-01";
		String  differ="0";
		String ss=dateDifferMin(time1, time2, travel_time, differ);
	
		System.out.println(ss);
		
		int a=63/60;
		int  b=63%60;
		System.out.println(a+","+b);
	}

	
}
