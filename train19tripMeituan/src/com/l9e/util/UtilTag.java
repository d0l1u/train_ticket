package com.l9e.util;

public class UtilTag {
	public static String fromTimeFormatStr(String startTime){
		//00:00 --- 06:00 返回1
		//06:00 --- 12:00 返回2
		//12:00 --- 18:00 返回3
		//18:00 --- 24:00 返回4
		String six = "06:00";
		String twelve = "12:00";
		String eighteen = "18:00";
		
		return startTime.compareTo(six)<0 ? "fzs":startTime.compareTo(twelve)<0?"fam":startTime.compareTo(eighteen)<0?"fpm":"fws" ;
	}
	
	public static String toTimeFormatStr(String toTime){
		//00:00 --- 06:00 返回1
		//06:00 --- 12:00 返回2
		//12:00 --- 18:00 返回3
		//18:00 --- 24:00 返回4
		String six = "06:00";
		String twelve = "12:00";
		String eighteen = "18:00";
		
		return toTime.compareTo(six)<0 ? "tzs":toTime.compareTo(twelve)<0?"tam":toTime.compareTo(eighteen)<0?"tpm":"tws" ;
	}
}
