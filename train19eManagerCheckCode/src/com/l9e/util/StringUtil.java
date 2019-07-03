package com.l9e.util;

public class StringUtil {
	
	public static boolean isNotEmpty(String value) {
		if(value == null) {
			return false;
		}
		if(value.length() == 0){
			return false;
		}
		return true;
	}
	
	/**
	 * null转空
	 * @param str
	 * @return
	 */
	public static String nullToEmpty(String str){
		if(str==null || "".equals(str.trim()) || "null".equals(str.trim())){
			return "";
		}else{
			return str.trim();
		}
	}
	public static boolean isNotBlank(Double value) {
		if(value == null) {
			return false;
		}
		return true;
	}
	public static boolean isNotBlank(String value) {
		if(value == null) {
			return false;
		}
		return true;
	}
	public static boolean isNotBlank(Integer value) {
		if(value == null) {
			return false;
		}
		return true;
	}
}
