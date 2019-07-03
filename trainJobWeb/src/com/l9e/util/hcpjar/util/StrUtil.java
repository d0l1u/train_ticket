package com.l9e.util.hcpjar.util;

public class StrUtil {
	public static boolean isNotEmpty(String str){
		if(str!=null&&str.trim().length()>0){
			return true;
		}else{
			return false;
		}
		
	}
}
