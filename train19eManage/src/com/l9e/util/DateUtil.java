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
	//一分钟的毫秒数
	private static final long MD=1000*60;
	
	public static final String DATE_FMT4 = "yyyy-MM-dd HH:mm:ss";

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
	
	public static Date dateAddDays(Date date,int addDays){
		if(null==date){
			return null;
		}
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, addDays);
		return cal.getTime();
	}
	
	public static Date dateAddMinutes(Date date,int addMinutes){
		if(null==date){
			return null;
		}
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, addMinutes);
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
		System.out.println(getNowTime());
	}
	
	/**
	 * 把时间字符串转换成其他字符串格式
	 * @author 刘毅
	 * @param date  时间
	 * @param formatStr1  原格式
	 *  @param formatStr2  要转换的格式
	 * @return
	 */
	
	public static String fmtToFmt(String dateStr,String formatStr1,String formatStr2){
		return dateToString(stringToDate(dateStr,formatStr1),formatStr2);
	}
	
	
	/**
	 * 获取昨天的年、月、日 格式为2014-01-09
	 * @return
	 */
	public static String getFrontDay(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		Date date = calendar.getTime();
		return sdf.format(date);
		
	}
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getNowTime(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FMT3);
		Date date = calendar.getTime();
		return sdf.format(date);
	}
	
	/**
	 * 获取当天时间
	 * @return
	 */
	public static String getNowDAY(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FMT1);
		Date date = calendar.getTime();
		return sdf.format(date);
	}
	
	/**
	 * 获取昨天的月、日格式为01月9日
	 * @return
	 */
	public static String getFrontDate(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		Date date = calendar.getTime();
		String dates[]=sdf.format(date).split("年");
		return dates[1];
		
	}
	
	/**
	 * 获取某一天的月、日格式为01月9日
	 * @return
	 */
	public static String getDate(Date date,String formatStr){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		String dates[]=sdf.format(date).split("年");
		return dates[1];
		
	}
	
	/**
	 * 获取某一天是星期几,返回字符串为:星期五
	 */
	public static String getWeekByDate(Date date){
		SimpleDateFormat sdfs = new SimpleDateFormat("EEEE");  
        String week = sdfs.format(date);
        return week;
	}
	
	
	/**
	 * 获取昨天是星期几
	 * @return
	 */
	public static String getFrontWeek(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		Date date = calendar.getTime();
	    SimpleDateFormat sdfs = new SimpleDateFormat("EEEE");  
        String week = sdfs.format(date);
		return week;
	}
	
	/**
	 * 两个时间相差距离多少天多少小时多少分多少秒
	 * @param str1
	 *            时间参数 1 格式：1990-01-01 12:00:00
	 * @param str2
	 *            时间参数 2 格式：2009-01-01 12:00:00
	 * @return String 返回值为：xx天xx小时xx分xx秒     xx分钟
	 */
	public static String getDistanceTime(String str1, String str2) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			min = diff / (60 * 1000);
//			day = diff / (24 * 60 * 60 * 1000);
//			hour = (diff / (60 * 60 * 1000) - day * 24);
//			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
//			sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//return day + "天" + hour + "小时" + min + "分" + sec + "秒";
		return min+"";
	}
}
