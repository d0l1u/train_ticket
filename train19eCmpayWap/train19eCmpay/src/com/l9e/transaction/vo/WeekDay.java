package com.l9e.transaction.vo;

import java.util.HashMap;
import java.util.Map;

public class WeekDay {

	public static final int SUN = 1;
	public static final int MON = 2;
	public static final int TUE = 3;
	public static final int WED = 4;
	public static final int THU = 5;
	public static final int FRI = 6;
	public static final int STA = 7;

	private static Map<Integer, String> WEEKDAY = new HashMap<Integer, String>();

	static {
		WEEKDAY.put(SUN, "周日");
		WEEKDAY.put(MON, "周一");
		WEEKDAY.put(TUE, "周二");
		WEEKDAY.put(WED, "周三");
		WEEKDAY.put(THU, "周四");
		WEEKDAY.put(FRI, "周五");
		WEEKDAY.put(STA, "周六");

	}

	public static Map<Integer, String> getWeekDayMap() {
		return WEEKDAY;
	}

	public static String getDay(int day) {
		return getWeekDayMap().get(day);
	}

}
