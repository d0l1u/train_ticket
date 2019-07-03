package com.l9e.train.util;

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

	public static final String DATE_FMT3 = "yyyy-MM-dd HH:mm:ss";
	
	public static final String DATE_FMT4 = "HH:mm";
	
	private static final long ND=1000*24*60*60;
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
	 * @param date	     时间
	 * @param addDays 要增加的天数
	 * @return "yyyy-MM-dd HH:mm:ss"
	 */
	public static String dateAddDaysFmt3(String date,String addDays){
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
	 * @param date	    改签 
	 * @param addDays 要增加的天数
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public static String dateAlterAddDays(String date,String addDays) throws NumberFormatException, Exception{
		String alter_day = exceptionDay(Integer.valueOf(addDays));
		return alter_day;
	}
	/**
	 * @author zuoyx
	 * @param date	    改签 
	 * @param addDays 要增加的天数
	 * @return 2016年春运自2016年1月24日开始至2016年3月3日
	 */
	public static boolean weatherChunYun(String date){
		if(date.compareTo("2016-01-24")>=0 && date.compareTo("2016-03-03") <= 0){
			return true;
		}
		return false;
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
		return (int)diff;	
	}
	
	 /**
     * 判断当前日期是星期几<br>
     * <br>
     * @param pTime 修要判断的时间<br>
     * @return dayForWeek 判断结果<br>
     * @Exception 发生异常<br>
     */
	 public static int dayForWeek(String pTime) throws Exception {
		  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		  Calendar c = Calendar.getInstance();
		  c.setTime(format.parse(pTime));
		  int dayForWeek = 0;
		  if(c.get(Calendar.DAY_OF_WEEK) == 1){
		   dayForWeek = 7;
		  }else{
		   dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		  }
		  return dayForWeek;
	 }
	 
	 //获取星期二、三、四的日期
	 public static String exceptionDay(int days) throws Exception {
		 String new_day = DateUtil.dateAddDays(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1), String.valueOf(days));
		 int day = dayForWeek(new_day);
		 if(day==2||day==3||day==4){
			 return new_day;
		 }else{
			 if(day==1){
				 days++;
			 }else{
				 days+=4;
			 }
		 }
		 return DateUtil.dateAddDays(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1), String.valueOf(days));
	 }
	 public static void main(String[] args){
//		 if(Integer.valueOf("09:10".replace(":", ""))<Integer.valueOf("11:00".replace(":", ""))){
//			 System.out.println("1111");
//		 }
		 try {
			System.out.println(exceptionDay(15));
			System.out.println(exceptionDay(18));
			System.out.println(exceptionDay(21));
			System.out.println(exceptionDay(24));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		 System.out.println(DateUtil.dateAddDaysFmt3(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1),"58"));
	 }
}
