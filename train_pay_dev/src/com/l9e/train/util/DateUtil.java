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
	
	/**
	 * 两个日期之间相差的分钟数
	 * return 相差的分钟数
	 */
	public long twoDateDiffMinutes(Date date1,Date date2,String formatStr){
		
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		String date1Str=format.format(date1);
		String date2Str=format.format(date2);
		
		long diffMinutes=0;
		try {
			date1=format.parse(date1Str);
			date2=format.parse(date2Str);
			if(date1.getTime() > date2.getTime()){
				diffMinutes=(date1.getTime()-date2.getTime())/(1000*60);
			}else{
				diffMinutes=(date2.getTime()-date1.getTime())/(1000*60);
			}
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return diffMinutes;
	}
	
	/**
	 * 两个日期之间相差的小时数
	 * return 相差的小时数
	 */
	public long twoDateDiffHours(Date date1,Date date2,String formatStr){
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		String date1Str=format.format(date1);
		String date2Str=format.format(date2);
		
		long diffHours=0;
		try {
			date1=format.parse(date1Str);
			date2=format.parse(date2Str);
			
			if(date1.getTime() > date2.getTime()){
				diffHours=(date1.getTime()-date2.getTime())/(1000*60*60);
			}else{
				diffHours=(date2.getTime()-date1.getTime())/(1000*60*60);
			}
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return diffHours;
	}
	
	/**
	 * 两个日期之间相差的天数
	 * return 相差的天数
	 */
	public long twoDateDiffDays(Date date1,Date date2,String formatStr){
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		String date1Str=format.format(date1);
		String date2Str=format.format(date2);
		
		long diffDays=0;
		try {
			date1=format.parse(date1Str);
			date2=format.parse(date2Str);
			
			if(date1.getTime() > date2.getTime()){
				diffDays=(date1.getTime()-date2.getTime())/(1000*60*60*24);
			}else{
				diffDays=(date2.getTime()-date1.getTime())/(1000*60*60*24);
			}
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return diffDays;
  }
	
}
