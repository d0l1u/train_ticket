package com.l9e.util.elong;

public class StrUtil {
	public static String toString(Object obj){
		if(obj==null){
			return "";
		}else{
			return obj.toString();
		}
	}
	
	public static boolean isNotEmpty(String str){
		if(str==null||str.equals("")){
			return false;
		}else{
			return true;
		}
		
	}
	
	
	public static boolean isEmpty(String str){
		if(str==null||str.equals("")){
			return true;
		}else{
			return false;
		}
	}
}
