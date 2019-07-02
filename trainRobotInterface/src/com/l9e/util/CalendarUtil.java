package com.l9e.util;

import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {
	
	/**
	 * 检查传入时间是否在,某个时间范围之内 dayDiff=30,检查checkTime是否在当前时间之后30天之内,并且大于等于当前时间
	 * 
	 * @param checkTime
	 * @param dayDiff
	 */
	public static boolean checkNowDateInterval(Date checkTime, int dayDiff) {

		// 检查的时间
		Calendar checkDate = Calendar.getInstance();
		checkDate.setTime(checkTime);

		// 时间的范围[start,end]
		Date nowDate = new Date();
		Calendar start = Calendar.getInstance();
		start.setTime(nowDate);
		start.add(Calendar.DATE, -1); // 11

		Calendar end = Calendar.getInstance();
		end.setTime(nowDate);
		end.add(Calendar.DATE, dayDiff - 1); // 30

		if (checkDate.after(start) && checkDate.before(end)) {
			return true;
		} else {
			return false;
		}

	}

	public static void main(String[] args) {
		
		String fromTime = "2018-01-12";

		boolean zflag = checkNowDateInterval(DateUtil.stringToDate(fromTime, DateUtil.DATE_FMT1), 30);

		System.out.println(zflag);
		
		

	}

}
