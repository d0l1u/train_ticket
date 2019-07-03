package com.l9e.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 时间处理工具类
 * 
 * @author 肖有标
 * 
 */
public class DateUtil {

	public static final String DATE_HM = "HH:mm";

	public static final String DATE_FMT1 = "yyyy-MM-dd";

	public static final String DATE_FMT2 = "yyyyMMddHHmmss";

	// 刘毅
	public static final String DATE_FMT3 = "yyyy-MM-dd HH:mm:ss";
	// 一天的毫秒数
	private static final long ND = 1000 * 24 * 60 * 60;
	// 一分钟的毫秒数
	private static final long MD = 1000 * 60;

	/**
	 * 把时间转换成字符串
	 * 
	 * @param date
	 *            时间
	 * @param formatStr
	 *            要转换的格式
	 * @return
	 */
	public static String dateToString(Date date, String formatStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		sdf.format(date);
		return sdf.format(date);
	}

	public static Date stringToDate(String dateStr, String formatStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
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

	public static Date dateAddDays(Date date, int addDays) {
		if (null == date) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, addDays);
		return cal.getTime();
	}

	/**
	 * @author zuoyx
	 * @param date
	 *            时间
	 * @param addDays
	 *            要增加的天数
	 * @return
	 */
	public static String dateAddDays(String date, String addDays) {
		if (null == date) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(stringToDate(date, DATE_FMT1));
		cal.add(Calendar.DAY_OF_YEAR, Integer.valueOf(addDays));
		return dateToString(cal.getTime(), DATE_FMT1);
	}

	/**
	 * @author 刘毅
	 * @param date
	 *            时间1
	 * @param date2
	 *            时间2
	 * @return long 相差几天
	 */
	public static int dateDiff(Date d1, Date d2) {
		long n1 = d1.getTime() / ND;
		long n2 = d2.getTime() / ND;
		long diff = Math.abs(n1 - n2);
		return (int) diff + 1;
	}

	/**
	 * @author zuoyx
	 * @param date
	 *            时间1
	 * @param date2
	 *            时间2
	 * @return long 相差分钟
	 */
	public static String minuteDiff(String min1, String min2) {
		Date d1 = stringToDate(min1, DATE_HM);
		Date d2 = stringToDate(min2, DATE_HM);
		long n1 = d1.getTime() / MD;
		long n2 = d2.getTime() / MD;
		if (d2.before(d1)) {
			return String.valueOf(24 * 60 - Math.abs(n1 - n2));
		} else {
			return String.valueOf(Math.abs(n1 - n2));
		}
	}

	/**
	 * 分钟数值转换为HH:mm
	 * 
	 * @author zuoyx
	 * @param str
	 *            分钟
	 * @return HH:mm字符串
	 */
	public static String minuteFormat(String str) {
		int minute = Integer.valueOf(str);
		String result = "";
		int hours = (minute / 60);
		int min = (minute % 60);
		if (hours > 10) {
			result += hours + "";
		} else if (hours > 0) {
			result += "0" + hours;
		} else {
			result += "00";
		}
		if (min > 10) {
			result += ":" + min;
		} else {
			result += ":0" + min;
		}
		return result;
	}

	/**
	 * HH:mm转换为h小时m分钟
	 * 
	 * @author zuoyx
	 * @param str
	 *            HH:mm字符串
	 * @return
	 */
	public static String hmFormat(String str) {
		String[] strArr = str.split(":");
		int hour = Integer.valueOf(strArr[0]);
		int minute = Integer.valueOf(strArr[1]);
		return hour + "小时" + minute + "分";
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
	
	//获得一星期中的第几天
	public static int getDay(String dateString, String formatStr) {
		Date date = stringToDate(dateString, formatStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	//根据日期取得星期几   
	public static String getWeek(Date date){  
		String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};  
		Calendar cal = Calendar.getInstance();  
		cal.setTime(date);  
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;  
        if(week_index<0){  
            week_index = 0;  
       }   
        return weeks[week_index];  
	}  

	
	//获得当前日期三十天内的日期以及对应周几//60
	public static List<Map<String, String>> getOneMonthDayAndWeek(){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for(int i=0;i<70;i++){
			Map<String, String> map = new HashMap<String, String>();
			String day = dateToString(dateAddDays(new Date(), i), "MM-dd");
			String newday = dateToString(dateAddDays(new Date(), i), "yyyy-MM-dd");
			//String week = dateToString(dateAddDays(new Date(), i), "EEEE");
			String week = getWeek(dateAddDays(new Date(), i));
			map.put("day", day);
			map.put("newday", newday);
			map.put("week", week);
			list.add(map);
		}
		return list;
	}
	
	//获得指定日期（格式：2014-01-01）的前后4天的日期，返回总共九个日期的list
	//若前四天有日期小于今天的日期无效，则后面的日期增加
	public static List<Map<String, String>> getBeforeAndAfter4Days(String day){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Date date = stringToDate(day, "yyyy-MM-dd");
		int subDay = dateDiff(date, new Date());//选择的日期和今天相差几天
		if(subDay<4){//选择的日期为今天
			for(int i=0;i<9;i++){
				String someday = dateToString(dateAddDays(new Date(), i), "MM-dd");
				Map<String, String> map = new HashMap<String, String>();
				map.put("days", someday);
				list.add(map);
			}
		}else if(subDay>=4 && subDay<56){
			for(int i=-4;i<5;i++){
				String someday = dateToString(dateAddDays(date, i), "MM-dd");
				Map<String, String> map = new HashMap<String, String>();
				map.put("days", someday);
				list.add(map);
			}
		}else if(subDay>=56){
			int n = 59-subDay;
			for(int i=n-8;i<=n;i++){
				String someday = dateToString(dateAddDays(date, i), "MM-dd");
				System.out.println(i+"  "+someday);
				Map<String, String> map = new HashMap<String, String>();
				map.put("days", someday);
				list.add(map);
			}
		}
		return list;
	}
	
	//格式转换
	public static String stringToString(String dateStr,String formatStr){
		return dateToString(stringToDate(dateStr,formatStr), formatStr);
	}
		
	public static void main(String[] args) {
		System.out.println(DateUtil.stringToDate("2014-12-10 14:12:00",DateUtil.DATE_FMT3));
		
		System.out.println(DateUtil.dateToString(DateUtil.stringToDate("2014-12-10 14:12:00",DateUtil.DATE_FMT3), DateUtil.DATE_HM));
		System.out.println(DateUtil.minuteDiff(DateUtil.dateToString(new Date(),DateUtil.DATE_HM),DateUtil.dateToString(DateUtil.stringToDate("2014-12-10 14:21:00",DateUtil.DATE_FMT3),DateUtil.DATE_HM)));
	}

	private static String DATE_HM() {
		// TODO Auto-generated method stub
		return null;
	}
}
