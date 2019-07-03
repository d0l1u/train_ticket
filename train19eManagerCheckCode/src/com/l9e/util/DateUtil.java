package com.l9e.util;

import java.text.DateFormat;
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
	
	public static final String DATE_FMT4="yyyy-MM-dd HH";
	
	public static final String DATE_FMT5="yyyy-MM-dd-HH";
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
			e.printStackTrace();
			return null;
		}
		return date;
	}
	
	public static String stringToString(String dateStr,String formatStr){
		return dateToString(stringToDate(dateStr,DATE_FMT3),formatStr);
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
	
	
	public static Date dateAddHour(Date date,int addHour){
		if(null==date){
			return null;
		}
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, addHour);
		return cal.getTime();
	}
	
	
	
	public static String nowDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 0);    //当前时间
		Date nowDate = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String now = df.format(nowDate);//当前时间
		return now;
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
	public static int minuteDiffInt(String min1, String min2) {	
		Date d1 = stringToDate(min1,DATE_HM);
		Date d2 = stringToDate(min2,DATE_HM);
		long n1 = d1.getTime()/MD;	
		long n2 = d2.getTime()/MD;
		return  (int) Math.abs(n1 - n2);				
	}
	
	/**
	 * @author zuoyx
	 * @param date	     时间1
	 * @param date2        时间2
	 * @return long 相差分钟6:23
	 */
	public static String minuteDiffFormat(String min1, String min2) {	
		Date d1 = stringToDate(min1,DATE_HM);
		Date d2 = stringToDate(min2,DATE_HM);
		long n1 = d1.getTime()/MD;	
		long n2 = d2.getTime()/MD;
		long minute = Math.abs(n1 - n2);	
		String result = "";
		int hours = (int) (minute/60);
		int min = (int) (minute%60);
		if(hours>10){
			result += hours+"";
		}else if(hours>0){
			result +="0"+hours;
		}
		if(min>10){
			result +=":"+min;
		}else{
			result +=":0"+min;
		}
		return  result;				
	}
	
	public static void main(String[] args){
		String m1 = "23:12";
		String m2 = "04:50";
		System.out.println(minuteDiffFormat(m2,m1));
		System.out.println(minuteDiff(m2,m1));
	}
}
