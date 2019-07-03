package com.l9e.util.hcpjar.util;

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
	
	public static final String DATE_FMT1 = "yyyy-MM-dd";
	
	public static final String DATE_FMT2 = "yyyyMMddHHmmss";
	
	public static final String DATE_FMT3 = "yyyy-MM-dd HH-mm-ss";
	
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
	 * 获取上月的年、月、日 格式为2014-01-09
	 * @return
	 */
	public static String getFrontMonth(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		calendar.add(Calendar.MONTH, -1);
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
	
	public static void main(String[] args) {
		System.out.println(getFrontMonth());
	}
	
}
